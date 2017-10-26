package com.ddcb.weixin.service.impl;

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
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

@Service("weixinMessageProcessService")
public class MessageProcessServiceImpl implements IMessageProcessService {

	private static final Logger logger = LoggerFactory
			.getLogger(MessageProcessServiceImpl.class);
	
	@Autowired
	private ICglxDao cglxDao;
	
	@Autowired
	private ICourseInviteCardService courseInviteCardService;
	
	@Override
	public String processWeixinMessage(HttpServletRequest request,HttpSession httpSession) {
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
			InputMessage inputMsg = (InputMessage) xs
					.fromXML(xmlMsg.toString());
			String msgType = inputMsg.getMsgType();
			logger.debug("Message Type : {}", msgType);
			logger.debug("Event : {}", inputMsg.getEvent());
			logger.debug("WeixinMsgType.Event.toString() : {}",
					WeixinMsgType.Event.toString().trim());
			logger.debug("开发者微信号：" + inputMsg.getToUserName());
			logger.debug("发送方帐号：" + inputMsg.getFromUserName());
			logger.debug("消息创建时间：" + inputMsg.getCreateTime());
			logger.debug("消息内容：" + inputMsg.getContent());
			logger.debug("消息Id：" + inputMsg.getMsgId());
			logger.debug("当前收到的消息内容" + inputMsg.getContent());
			if (msgType.equals(WeixinMsgType.Text.toString())) {
				return processTextMessage(inputMsg);
			}  else if (msgType.equals(WeixinMsgType.Event.toString())) {
				if (("subscribe").equals(inputMsg.getEvent().trim())) {
					return processSubscribeEvent(inputMsg);
				} else if(("SCAN").equals(inputMsg.getEvent().trim())){
					return processScanEvent(inputMsg);
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
	
	private String processSubscribeEvent(InputMessage inputMsg){
		String result = sendTextMessage(
				"亲爱的小伙伴，千山万水你还是来了。\r\n欢迎关注UDIY研习社！我们是一个有爱有干货的留学生互助共享平台。\r\n点击下方免费领取微课~\r\n<a href='http://www.udiyclub.com/courses/jsp?id=2&view=detail'>95后博士教你如何制霸北美CS专业</a>\r\n<a href='http://www.udiyclub.com/courses/jsp?id=23&view=detail'>全方位雅思口语短期内大提分</a>\r\n更多有趣实用的留学内容，点击菜单查看哦~", 
				inputMsg);
		String qrcodeArgs = inputMsg.getEventKey().trim();
		logger.debug("processSubscribeEvent, subscribe, args :{}", qrcodeArgs);
		if(qrcodeArgs != null && qrcodeArgs.startsWith("qrscene_")&&
				qrcodeArgs.split("###").length ==3){
			qrcodeArgs = qrcodeArgs.substring(8);
			List<String> qrsceneList = Arrays.asList(qrcodeArgs.split("###"));
			logger.debug("qrsceneList : {}", qrsceneList.toString());
			final String courseId = qrsceneList.get(0);
			final String isSeries = qrsceneList.get(1);
			final String srcOpenId = qrsceneList.get(2);
			final String friendOpenId = inputMsg.getFromUserName();
			new Thread(new Runnable() {
				@Override
				public void run() {
					courseInviteCardService.pushCourseInviteNotify(srcOpenId, friendOpenId, courseId, isSeries);
				}
			}).start();
		} else {
			logger.debug("processSubscribeEvent, qrcodeArgs:{}", qrcodeArgs);
		}
		final String userOpenId = inputMsg.getFromUserName();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					ObjectMapper om = new ObjectMapper();
					@SuppressWarnings("unchecked")
					Map<String,Object> retMap = om.readValue(getUserInfoByOpenId(userOpenId), Map.class);
					String nickname = (String)retMap.get("nickname");
					String headImgUrl = (String)retMap.get("headimgurl");
					String unionid = (String)retMap.get("unionid");
					cglxDao.addUserByOpenid(unionid, nickname,headImgUrl);
				} catch(Exception ex){
					logger.error(ex.toString());
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				courseInviteCardService.pushCourseInviteCard(userOpenId);
			}
		}).start();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private String processScanEvent(InputMessage inputMsg){
		String result = "";
		try {
			logger.debug("processScanEvent, scan args :{}", inputMsg.getEventKey().trim());
			result = sendTextMessage("亲爱的小伙伴，活动您已经支持过了，不能重复支持呦。", inputMsg);
			String qrcodeArgs = inputMsg.getEventKey().trim();
			if(qrcodeArgs != null && qrcodeArgs.split("###").length ==3){
				List<String> qrsceneList = Arrays.asList(qrcodeArgs.split("###"));
				logger.debug("qrsceneList : {}", qrsceneList.toString());
				final String srcOpenId = qrsceneList.get(2);
				final String currentOpenId = inputMsg.getFromUserName();
				ObjectMapper om = new ObjectMapper();
				Map<String,Object> retMap = om.readValue(getUserInfoByOpenId(srcOpenId), Map.class);
				String srcUnionId = (String)retMap.get("unionid");
				retMap = om.readValue(getUserInfoByOpenId(currentOpenId), Map.class);
				String currentUnionId = (String)retMap.get("unionid");
				if(currentUnionId != null && currentUnionId.equals(srcUnionId)){
					result = sendTextMessage("亲爱的小伙伴，您不能支持您自己呦，赶紧邀请您的好友来支持吧~", inputMsg);
				}
			}
		} catch(Exception ex) {
			logger.error(ex.toString());
		}
		return result;
	}
	
	private String processTextMessage(InputMessage inputMsg){
		String result = "";
		if(inputMsg.getContent().indexOf("入群") != -1){
			sendTextMessage(
					"",
					inputMsg);
			logger.debug("当前收到的消息内容" + inputMsg.getContent());
			result = sendImageMessage(
					"PMCPFwuiCp2HPAfNSNNt9eJz-gyBI5v2JrU9lieA-Aw",
					inputMsg);
		} else {
			result = sendTextMessage(
					"亲爱的小伙伴，千山万水你还是来了。无论你身在何方，在做着什么，你找到组织啦。\r\n我们是一个有爱有干货的留学生互助共享平台。DIY研习社，让留学不孤单。\r\n后台回复关键词“入群”，即可加入DIY研习社最新创建的社群。如果无法入群，加群理事V信号senyuyan0904即可入群。",
					inputMsg);
		}
		return result;
	}
	
	private void setOutputMsgInfo(OutputMessage oms, InputMessage msg)
			throws Exception {
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
	
	private String sendTextMessage(String content, InputMessage inputMsg){
		String result = "";
		XStream xstream = new XStream(new XppDriver() {
			@Override
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					@Override
					protected void writeText(QuickWriter writer,
							String text) {
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
	
	private String sendImageMessage(String mediaId, InputMessage inputMsg){
		String result = "";
		XStream xstream = new XStream(new XppDriver() {
			@Override
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					@Override
					protected void writeText(QuickWriter writer,
							String text) {
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
	
	private String getUserInfoByOpenId(String openId){
		String ret = "";
		URL url;
		try {
			url = new URL("https://api.weixin.qq.com/cgi-bin/user/info?access_token="+WeixinCache.getAccessToken()+"&openid="+openId+"&lang=zh_CN");
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

}
