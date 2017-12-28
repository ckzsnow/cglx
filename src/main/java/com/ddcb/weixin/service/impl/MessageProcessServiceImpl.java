package com.ddcb.weixin.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddcb.utils.ImageOutputMessage;
import com.ddcb.utils.Image;
import com.ddcb.utils.InputMessage;
import com.ddcb.utils.OutputMessage;
import com.ddcb.utils.TextOutputMessage;
import com.ddcb.utils.WeixinCache;
import com.ddcb.utils.WeixinMsgType;
import com.ddcb.weixin.service.ICourseInviteCardService;
import com.ddcb.weixin.service.IMessageProcessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocfisher.dao.ICglxDao;
import com.ocfisher.dao.ICourseInviteCardDao;
import com.ocfisher.dao.ICourseInviteCardUnionidDao;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import net.sf.json.JSONObject;

@Service("weixinMessageProcessService")
public class MessageProcessServiceImpl implements IMessageProcessService {

	private static final Logger logger = LoggerFactory.getLogger(MessageProcessServiceImpl.class);

	@Autowired
	private ICglxDao cglxDao;

	@Autowired
	private ICourseInviteCardService courseInviteCardService;

	@Autowired
	private ICourseInviteCardDao courseInviteCardDao;

	@Autowired
	private ICourseInviteCardUnionidDao courseInviteCardUnionidDao;

	@Override
	public String processWeixinMessage(HttpServletRequest request, HttpSession httpSession) {
		String result = "";
		logger.debug("begin to process weixin message.");
		try {
			ServletInputStream in;
			in = request.getInputStream();
			XStream xs = new XStream(new DomDriver());
			xs.alias("xml", InputMessage.class);
			StringBuilder xmlMsg = new StringBuilder();
			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) {
				xmlMsg.append(new String(b, 0, n, "UTF-8"));
			}
			logger.info(xmlMsg.toString());
			InputMessage inputMsg = (InputMessage) xs.fromXML(xmlMsg.toString());
			String msgType = inputMsg.getMsgType();
			logger.debug("Message Type : {}", msgType);
			logger.debug("Event : {}", inputMsg.getEvent());
			logger.debug("WeixinMsgType.Event.toString() : {}", WeixinMsgType.Event.toString().trim());
			logger.debug("开发者微信号：" + inputMsg.getToUserName());
			logger.debug("发送方帐号：" + inputMsg.getFromUserName());
			logger.debug("消息创建时间：" + inputMsg.getCreateTime());
			logger.debug("消息内容：" + inputMsg.getContent());
			logger.debug("消息Id：" + inputMsg.getMsgId());
			logger.debug("当前收到的消息内容" + inputMsg.getContent());
			if (msgType.equals(WeixinMsgType.Text.toString())) {
				return processTextMessage(inputMsg);
			} else if (msgType.equals(WeixinMsgType.Event.toString())) {
				if (("subscribe").equals(inputMsg.getEvent().trim())) {
					return processSubscribeEvent(inputMsg);
				} else if (("SCAN").equals(inputMsg.getEvent().trim())) {
					return processScanEvent(inputMsg);
				} else if (("CLICK").equals(inputMsg.getEvent().trim())) {
					return processClickEvent(inputMsg);
				} else if (("unsubscribe").equals(inputMsg.getEvent().trim())) {
					return processUnsubscribeEvent(inputMsg);
				}
			}
		} catch (IOException e) {
			logger.info(e.toString());
		} catch (Exception e) {
			logger.info(e.toString());
		}
		logger.debug("finish in processing weixin message.");
		return result;
	}

	// 用户取消关注服务号
	private String processUnsubscribeEvent(InputMessage inputMsg) {
		final String open_id = inputMsg.getFromUserName();
		new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				ObjectMapper om = new ObjectMapper();
				Map<String, Object> retMap = null;
				try {
					retMap = om.readValue(getUserInfoByOpenId(open_id), Map.class);
				} catch (Exception e) {
					logger.error("processSubscribeEvent readValue exception : {}", e.toString());
				}
				String union_id = (String) retMap.get("unionid");
				courseInviteCardDao.updateUserSubscribeStatus(union_id, 0);
			}
		}).start();
		return "";
	}

	// 用户点击服务号’邀请卡‘按钮
	private String processClickEvent(InputMessage inputMsg) {
		String result = "抱歉，当前暂无可参加的活动的邀请卡。";
		try {
			logger.debug("processScanEvent, scan args :{}", inputMsg.getEventKey().trim());
			List<Map<String, Object>> inviteCardList = courseInviteCardDao.getAllCourseInviteCardDetail();
			if (inviteCardList != null && !inviteCardList.isEmpty()) {
				result = "当前可参加活动的课程邀请卡有：\r\n";
				for (int index = 1; index <= inviteCardList.size(); index++) {
					result += (index + "、" + inviteCardList.get(index - 1).get("title") + "\r\n");
				}
				result += "回复编号，获取课程邀请卡哦~";
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		logger.debug("processClickEvent result : {}", result);
		result = sendTextMessage(result, inputMsg);
		return result;
	}

	// 用户订阅服务号
	@SuppressWarnings("unchecked")
	private String processSubscribeEvent(InputMessage inputMsg) {
		String result = "";
		String qrcodeArgs = inputMsg.getEventKey().trim();
		String copywriter = "";
		logger.debug("processSubscribeEvent, subscribe, args :{}", qrcodeArgs);

		// 获取用户union_id
		String open_id = inputMsg.getFromUserName();
		ObjectMapper om = new ObjectMapper();
		Map<String, Object> retMap = null;
		try {
			retMap = om.readValue(getUserInfoByOpenId(open_id), Map.class);
			String union_id = (String) retMap.get("unionid");

			// 添加关注用户
			AddUserSubscribe(open_id, union_id);

			// 二维码中不带参数，自然搜索服务号订阅
			if (qrcodeArgs == null || !qrcodeArgs.startsWith("qrscene_")) {
				result = sendTextMessage(
						"亲爱的小伙伴，千山万水你还是来了。\r\n欢迎关注UDIY研习社！我们是一个有" + "爱有干货的留学生互助共享平台。\r\n点击下方免费领取微课~\r\n<a href"
								+ "='http://www.udiyclub.com/courses/jsp?id=2&view=detail'>"
								+ "95后博士教你如何制霸北美CS专业</a>\r\n<a href='http://www.udiyclub.c"
								+ "om/courses/jsp?id=23&view=detail'>全方位雅思口语短期内大" + "提分</a>\r\n更多有趣实用的留学内容，点击菜单查看哦~",
						inputMsg);
			}

			// 用户扫描二维码订阅
			else {

				// 三个参数为扫描邀请卡，参数为course_id，is_series，邀请人open_id
				if (qrcodeArgs.split("###").length == 3) {

					qrcodeArgs = qrcodeArgs.substring(8);
					List<String> qrsceneList = Arrays.asList(qrcodeArgs.split("###"));
					logger.debug("qrsceneList : {}", qrsceneList.toString());
					final String courseId = qrsceneList.get(0);
					final String isSeries = qrsceneList.get(1);
					final String srcOpenId = qrsceneList.get(2);
					Map<String, Object> resultMap = courseInviteCardService.getInviteCardByCourseId(courseId);
					logger.debug("processSubscribeEvent resultMap : {}", resultMap.toString());
					
					Map<String, Object> sourceMap = om.readValue(getUserInfoByOpenId(srcOpenId), Map.class);
					String srcUnionId = (String) sourceMap.get("unionid");
					
					if (union_id != null && union_id.equals(srcUnionId)) {
						result = sendTextMessage("亲爱的小伙伴，您不能支持您自己呦，赶紧邀请您的好友来支持吧~", inputMsg);
						generateInviteCardAndPushToUser(resultMap, open_id);
						addPushRecord(open_id, courseId, isSeries);
					} else {
						int count = courseInviteCardDao.getCourseInviteRecordByFriendOpendIdAndCourseId(union_id, courseId);
						if (count == 0) {
							copywriter = (String) resultMap.get("copywriter");
							copywriter = copywriter.replace("_@", "\n").replace("_#", "\r").replace("_$", " ");
							result = sendTextMessage(copywriter, inputMsg);
							// 给邀请用户发送助攻通知
							pushCourseInviteNotify(srcOpenId, open_id, courseId, isSeries);
							// 给当前用户推送课程邀请卡
							generateInviteCardAndPushToUser(resultMap, open_id);
							addPushRecord(open_id, courseId, isSeries);
						} else {
							result = sendTextMessage("亲爱的小伙伴，活动您已经支持过了，不能重复支持呦。", inputMsg);
							generateInviteCardAndPushToUser(resultMap, open_id);
							addPushRecord(open_id, courseId, isSeries);
						}
					} 
				}

				// 两个参数为扫描宣传卡，参数为course_id,is_series
				else if (qrcodeArgs.split("###").length == 2) {
					qrcodeArgs = qrcodeArgs.substring(8);
					List<String> qrsceneList = Arrays.asList(qrcodeArgs.split("###"));
					logger.debug("qrsceneList : {}", qrsceneList.toString());
					final String courseId = qrsceneList.get(0);
					final String isSeries = qrsceneList.get(1);
					Map<String, Object> resultMap = courseInviteCardService.getInviteCardByCourseId(courseId);
					logger.debug("processSubscribeEvent resultMap : {}", resultMap.toString());
					if (resultMap != null && !resultMap.isEmpty()) {
						copywriter = (String) resultMap.get("copywriter");
						copywriter = copywriter.replace("_@", "\n").replace("_#", "\r").replace("_$", " ");
					}
					result = sendTextMessage(copywriter, inputMsg);
					generateInviteCardAndPushToUser(resultMap, open_id);
					addPushRecord(open_id, courseId, isSeries);
				}
			}
		} catch (Exception e) {
			logger.debug("processSubscribeEvent readValue exception : {}", e.toString());
			return result;
		}
		
		//添加user表，订阅即注册
		final String userOpenId = inputMsg.getFromUserName();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ObjectMapper om = new ObjectMapper();
					Map<String, Object> retMap = om.readValue(getUserInfoByOpenId(userOpenId), Map.class);
					String nickname = (String) retMap.get("nickname");
					String headImgUrl = (String) retMap.get("headimgurl");
					String unionid = (String) retMap.get("unionid");
					cglxDao.addUserByOpenid(unionid, nickname, headImgUrl);
				} catch (Exception ex) {
					logger.error(ex.toString());
				}
			}
		}).start();
		
		return result;
	}

	// 用户已关注，浏览服务号
	@SuppressWarnings("unchecked")
	private String processScanEvent(InputMessage inputMsg) {
		String result = sendTextMessage("抱歉，服务器出了点小意外，请您谅解~", inputMsg);
		String copywriter = "";
		try {
			String open_id = inputMsg.getFromUserName();
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> retMap = null;
			retMap = om.readValue(getUserInfoByOpenId(open_id), Map.class);
			String union_id = (String) retMap.get("unionid");
			String qrcodeArgs = inputMsg.getEventKey().trim();
			logger.debug("processScanEvent qrcodeArgs : {}", qrcodeArgs);
			if (qrcodeArgs != null && qrcodeArgs.contains("###")) {
				// 三个参数为扫描邀请卡，参数为course_id，is_series，邀请人open_id
				if (qrcodeArgs.split("###").length == 3) {
					List<String> qrsceneList = Arrays.asList(qrcodeArgs.split("###"));
					logger.debug("qrsceneList : {}", qrsceneList.toString());
					final String courseId = qrsceneList.get(0);
					final String isSeries = qrsceneList.get(1);
					final String srcOpenId = qrsceneList.get(2);
					logger.debug("processScanEvent courseId:{}, isSeries:{}, srcOpenId:{}", courseId, isSeries, srcOpenId);
					Map<String, Object> resultMap = courseInviteCardService.getInviteCardByCourseId(courseId);
					if(resultMap == null || resultMap.isEmpty()) {
						result = sendTextMessage("抱歉，此课程邀请卡活动已经结束，请关注公众号最新活动~", inputMsg);
						return result;
					}
					logger.debug("processScanEvent resultMap : {}", resultMap.toString());
					
					Map<String, Object> sourceMap = om.readValue(getUserInfoByOpenId(srcOpenId), Map.class);
					String srcUnionId = (String) sourceMap.get("unionid");
					
					if (union_id != null && union_id.equals(srcUnionId)) {
						result = sendTextMessage("亲爱的小伙伴，您不能支持您自己呦，赶紧邀请您的好友来支持吧~", inputMsg);
						generateInviteCardAndPushToUser(resultMap, open_id);
						addPushRecord(open_id, courseId, isSeries);
					} else {
						int count = courseInviteCardDao.getCourseInviteRecordByFriendOpendIdAndCourseId(union_id, courseId);
						if (count == 0) {
							copywriter = (String) resultMap.get("copywriter");
							copywriter = copywriter.replace("_@", "\n").replace("_#", "\r").replace("_$", " ");
							logger.debug("processScanEvent copywriter : {}", copywriter);
							result = sendTextMessage(copywriter, inputMsg);
							// 给邀请用户发送助攻通知
							pushCourseInviteNotify(srcOpenId, open_id, courseId, isSeries);
							// 给当前用户推送课程邀请卡
							generateInviteCardAndPushToUser(resultMap, open_id);
							addPushRecord(open_id, courseId, isSeries);
						} else {
							result = sendTextMessage("亲爱的小伙伴，活动您已经支持过了，不能重复支持呦。", inputMsg);
							generateInviteCardAndPushToUser(resultMap, open_id);
							addPushRecord(open_id, courseId, isSeries);
						}
					} 
				}

				// 两个参数为扫描宣传卡，参数为course_id,is_series
				else if (qrcodeArgs.split("###").length == 2) {
					List<String> qrsceneList = Arrays.asList(qrcodeArgs.split("###"));
					logger.debug("qrsceneList : {}", qrsceneList.toString());
					final String courseId = qrsceneList.get(0);
					final String isSeries = qrsceneList.get(1);
					Map<String, Object> resultMap = courseInviteCardService.getInviteCardByCourseId(courseId);
					if(resultMap == null || resultMap.isEmpty()) {
						result = sendTextMessage("抱歉，此课程邀请卡活动已经结束，请关注公众号最新活动~", inputMsg);
						return result;
					}
					logger.debug("processSubscribeEvent resultMap : {}", resultMap.toString());
					if (resultMap != null && !resultMap.isEmpty()) {
						copywriter = (String) resultMap.get("copywriter");
						copywriter = copywriter.replace("_@", "\n").replace("_#", "\r").replace("_$", " ");
					}
					result = sendTextMessage(copywriter, inputMsg);
					generateInviteCardAndPushToUser(resultMap, open_id);
					addPushRecord(open_id, courseId, isSeries);
				}
			}
		} catch (Exception e) {
			logger.debug("processSubscribeEvent readValue exception : {}", e.toString());
			return result;
		}
		return result;
	}

	// 用户发送信息与服务号交互
	@SuppressWarnings("unchecked")
	private String processTextMessage(InputMessage inputMsg) {
		String result = "";
		List<Map<String, Object>> inviteCardList = courseInviteCardDao.getAllCourse();
		logger.debug("processTextMessage inviteCardList : {}", inviteCardList.toString());
		if (inputMsg.getContent().indexOf("入群") != -1) {
			sendTextMessage("", inputMsg);
			logger.debug("当前收到的消息内容" + inputMsg.getContent());
			result = sendImageMessage("PMCPFwuiCp2HPAfNSNNt9eJz-gyBI5v2JrU9lieA-Aw", inputMsg);
		} else if(inputMsg.getContent().indexOf("英文小说") != -1) {
			result = sendTextMessage("Hi~这里是DIY研习社课堂！\n\n/:gift<Modern Library 100 Best Novels>分享给你 \n链接：https://pan.baidu.com/s/1eSk1g6e \n密码：v31y \n\n/:li点击菜单【邀请卡】，即可获取世界顶尖名校的学习工作经验~\n\n接下来还会有更多优质的内容与资源，欢迎持续关注哦！", inputMsg);
		} else if (isNumeric(inputMsg.getContent().trim())) {
			logger.debug("processTextMessage isNumeric check complete!");
			int index = Integer.valueOf(inputMsg.getContent().trim()) - 1;
			if (index > inviteCardList.size()) {
				result = sendTextMessage("抱歉，你所输入的课程邀请卡不存在~", inputMsg);
			} else {
				result = sendTextMessage("请稍等，您的专属课程邀请卡正在制作中哦~", inputMsg);
				Map<String, Object> courseMap = inviteCardList.get(index);
				logger.debug("processTextMessage chosen courseMap : {}, index:{}", courseMap.toString(), index);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							String open_id = inputMsg.getFromUserName();
							logger.debug("processTextMessage open_id : {}", open_id);
							ObjectMapper om = new ObjectMapper();
							Map<String, Object> retMap = om
									.readValue(CourseInviteCardServiceImpl.getUserInfoByOpenId(open_id), Map.class);
							String nickname = (String) retMap.get("nickname");
							String headImgUrl = (String) retMap.get("headimgurl");
							String unionid = (String) retMap.get("unionid");
							Integer isSeries = (Integer) courseMap.get("is_series");
							String templateName = (String) courseMap.get("template_name");
							String course_id = String.valueOf(courseMap.get("course_id"));
							String args = course_id + "###" + isSeries + "###" + open_id;
							logger.debug("processTextMessage args : {}", args);
							String json = "{\"expire_seconds\": 2592000, \"action_name\": \"QR_STR_SCENE\", \"action_info\""
									+ ": {\"scene\": {\"scene_str\": \"" + args + "\"}}}";
							String action = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
									+ WeixinCache.getAccessToken();
							String ret = CourseInviteCardServiceImpl.connectWeiXinInterface(action, json);
							logger.debug("processTextMessage ret : {}", ret);
							JSONObject jsonObject = JSONObject.fromObject(ret);
							if (jsonObject.has("url")) {
								System.out.println("url:" + jsonObject.getString("url"));
								String url = jsonObject.getString("url");
								logger.debug("qrcode url : {}", url);
								CourseInviteCardServiceImpl.generateQRCode(url, open_id, templateName);
								logger.debug("pushCourseInviteCard, nickname:{},headImgUrl:{},unionid:{}", nickname,
										headImgUrl, unionid);
								BufferedImage headImage = ImageIO.read(new URL(headImgUrl));
								CourseInviteCardServiceImpl.generateHeadImageCode(headImage, nickname, open_id);
								CourseInviteCardServiceImpl.pushImageToUser(open_id);
							} else {
								System.out.println(jsonObject.toString());
							}
						} catch (Exception e) {
							logger.error("processTextMessage ObjectMapper error : {}", e.toString());
						}

					}
				}).start();
			}
		} else {
			result = sendTextMessage(
					"亲爱的小伙伴，千山万水你还是来了。无论你身在何方，在做着什么，你找到组织啦。\r\n我们是一个有爱有干货的留学生互助共享平台。DIY研习社，让留学不孤单。\r\n后台回复关键词“入群”，即可加入DIY研习社最新创建的社群。如果无法入群，加群理事V信号senyuyan0904即可入群。",
					inputMsg);
		}
		return result;
	}

	private void AddUserSubscribe(String open_id, String union_id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				courseInviteCardDao.addUserSubscribe(open_id, union_id);
			}
		}).start();
	}

	private void setOutputMsgInfo(OutputMessage oms, InputMessage msg) throws Exception {
		Class<?> outMsg = oms.getClass().getSuperclass();
		Field CreateTime = outMsg.getDeclaredField("CreateTime");
		Field ToUserName = outMsg.getDeclaredField("ToUserName");
		Field FromUserName = outMsg.getDeclaredField("FromUserName");

		ToUserName.setAccessible(true);
		CreateTime.setAccessible(true);
		FromUserName.setAccessible(true);

		CreateTime.set(oms, new Date().getTime());
		ToUserName.set(oms, msg.getFromUserName());
		FromUserName.set(oms, msg.getToUserName());
	}

	private String sendTextMessage(String content, InputMessage inputMsg) {
		String result = "";
		XStream xstream = new XStream(new XppDriver() {
			@Override
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					@Override
					protected void writeText(QuickWriter writer, String text) {
						if (!text.startsWith("<![CDATA[")) {
							text = "<![CDATA[" + text + "]]>";
						}
						writer.write(text);
					}
				};
			}
		});
		TextOutputMessage outputMsg = new TextOutputMessage();
		outputMsg.setContent(content);
		try {
			setOutputMsgInfo(outputMsg, inputMsg);
		} catch (Exception e) {
			logger.debug(e.toString());
		}
		xstream.alias("xml", outputMsg.getClass());
		result = new String(xstream.toXML(outputMsg).getBytes());
		logger.debug("xml result : {}", result);
		return result;
	}

	private String sendImageMessage(String mediaId, InputMessage inputMsg) {
		String result = "";
		XStream xstream = new XStream(new XppDriver() {
			@Override
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					@Override
					protected void writeText(QuickWriter writer, String text) {
						if (!text.startsWith("<![CDATA[")) {
							text = "<![CDATA[" + text + "]]>";
						}
						writer.write(text);
					}
				};
			}
		});
		ImageOutputMessage imageOutputMessage = new ImageOutputMessage();
		Image image = new Image();
		image.setMediaId(mediaId);
		imageOutputMessage.setImage(image);
		try {
			setOutputMsgInfo(imageOutputMessage, inputMsg);
		} catch (Exception e) {
			logger.debug(e.toString());
		}
		xstream.alias("xml", imageOutputMessage.getClass());
		result = new String(xstream.toXML(imageOutputMessage).getBytes());
		logger.debug("xml result : {}", result);
		return result;
	}

	private String getUserInfoByOpenId(String openId) {
		String ret = "";
		URL url;
		try {
			url = new URL("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + WeixinCache.getAccessToken()
					+ "&openid=" + openId + "&lang=zh_CN");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			ret = new String(jsonBytes, "UTF-8");
			logger.debug("push result : {}", ret);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return ret;
	}

	private static boolean isNumeric(String str) {
		logger.debug("isNumeric str : {}", str);
		Pattern pattern = Pattern.compile("[0-9]*$");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public void generateInviteCardAndPushToUser(Map<String, Object> courseMap, String open_id) {
		new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					logger.debug("generateInviteCardAndPushToUser open_id : {}", open_id);
					ObjectMapper om = new ObjectMapper();
					Map<String, Object> retMap = om.readValue(CourseInviteCardServiceImpl.getUserInfoByOpenId(open_id),
							Map.class);
					String nickname = (String) retMap.get("nickname");
					String headImgUrl = (String) retMap.get("headimgurl");
					String unionid = (String) retMap.get("unionid");
					Integer isSeries = (Integer) courseMap.get("is_series");
					String templateName = (String) courseMap.get("template_name");
					String course_id = String.valueOf(courseMap.get("course_id"));
					String args = course_id + "###" + isSeries + "###" + open_id;
					logger.debug("processTextMessage args : {}", args);
					String json = "{\"expire_seconds\": 2592000, \"action_name\": \"QR_STR_SCENE\", \"action_info\""
							+ ": {\"scene\": {\"scene_str\": \"" + args + "\"}}}";
					String action = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
							+ WeixinCache.getAccessToken();
					String ret = CourseInviteCardServiceImpl.connectWeiXinInterface(action, json);
					logger.debug("processTextMessage ret : {}", ret);
					JSONObject jsonObject = JSONObject.fromObject(ret);
					if (jsonObject.has("url")) {
						System.out.println("url:" + jsonObject.getString("url"));
						String url = jsonObject.getString("url");
						logger.debug("qrcode url : {}", url);
						CourseInviteCardServiceImpl.generateQRCode(url, open_id, templateName);
						logger.debug("pushCourseInviteCard, nickname:{},headImgUrl:{},unionid:{}", nickname, headImgUrl,
								unionid);
						BufferedImage headImage = ImageIO.read(new URL(headImgUrl));
						CourseInviteCardServiceImpl.generateHeadImageCode(headImage, nickname, open_id);
						CourseInviteCardServiceImpl.pushImageToUser(open_id);
					} else {
						System.out.println(jsonObject.toString());
					}
				} catch (Exception e) {
					logger.error("processTextMessage ObjectMapper error : {}", e.toString());
				}

			}
		}).start();
	}

	public void pushCourseInviteNotify(String srcOpenId, String open_id, String course_id, String is_series) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				courseInviteCardService.pushCourseInviteNotify(srcOpenId, open_id, course_id, is_series);
			}
		}).start();
	}

	public void addPushRecord(String open_id, String course_id, String is_series) {
		//添加推送邀请卡的记录
		new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				ObjectMapper om = new ObjectMapper();
				try {
					Map<String, Object> retMap = om.readValue(getUserInfoByOpenId(open_id), Map.class);
					String unionid = (String) retMap.get("unionid");
					courseInviteCardUnionidDao.addInfo(unionid, Long.valueOf(course_id), Integer.valueOf(is_series));
				} catch (Exception e) {
					logger.error(e.toString());
				}
			}
		}).start();
	}
	
	public static void main(String[] args) {
		System.out.println(isNumeric("35a"));
	}
}
