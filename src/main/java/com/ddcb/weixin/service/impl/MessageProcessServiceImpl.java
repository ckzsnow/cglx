package com.ddcb.weixin.service.impl;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.ddcb.utils.WeixinMsgType;
import com.ddcb.weixin.service.ICourseInviteCardService;
import com.ddcb.weixin.service.IMessageProcessService;
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
			if (msgType.equals(WeixinMsgType.Text.toString())) {
				logger.debug("开发者微信号：" + inputMsg.getToUserName());
				logger.debug("发送方帐号：" + inputMsg.getFromUserName());
				logger.debug("消息创建时间：" + inputMsg.getCreateTime());
				logger.debug("消息内容：" + inputMsg.getContent());
				logger.debug("消息Id：" + inputMsg.getMsgId());
				logger.debug("当前收到的消息内容" + inputMsg.getContent());
				if(inputMsg.getContent().indexOf("入群") != -1){
					logger.debug("当前收到的消息内容" + inputMsg.getContent());
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
					image.setMediaId("PMCPFwuiCp2HPAfNSNNt9eJz-gyBI5v2JrU9lieA-Aw");
					imageOutputMessage.setImage(image);
					try {
						setOutputMsgInfo(imageOutputMessage, inputMsg);
					} catch (Exception e) {
						logger.debug(e.toString());
					}
					xstream.alias("xml", imageOutputMessage.getClass());
					result = new String(xstream.toXML(imageOutputMessage).getBytes());
					logger.debug("xml result : {}", result);
				} else {
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
					//outputMsg.setContent("客官，等您很久了～这里既分享各类线下讲座的内容，也允许优质机构和个人在线开办讲座，让用户足不出户参与讲座，并和导师互动。快点击“点豆大讲堂”来看看我们吧～");
					outputMsg.setContent("亲爱的小伙伴，千山万水你还是来了。无论你身在何方，在做着什么，你找到组织啦。\r\n我们是一个有爱有干货的留学生互助共享平台。DIY研习社，让留学不孤单。\r\n后台回复关键词“入群”，即可加入DIY研习社最新创建的社群。如果无法入群，加群理事V信号senyuyan0904即可入群。");
					try {
						setOutputMsgInfo(outputMsg, inputMsg);
					} catch (Exception e) {
						logger.debug(e.toString());
					}
					xstream.alias("xml", outputMsg.getClass());
					result = new String(xstream.toXML(outputMsg).getBytes());
					logger.debug("xml result : {}", result);
				}
			}  else if (msgType.equals(WeixinMsgType.Event.toString())) {
				logger.info("inputMsg.getEvent() : {}", inputMsg
						.getEvent().trim());
				if (("subscribe").equals(inputMsg.getEvent().trim())) {
					String qrcodeArgs = inputMsg.getEventKey().trim();
					logger.debug("subscribe, args :{}", qrcodeArgs);
					if(qrcodeArgs != null && qrcodeArgs.startsWith("qrscene_")){
						qrcodeArgs = qrcodeArgs.substring(8);
						int pos = qrcodeArgs.indexOf("###");
						if(pos != -1) {
							final String courseId = qrcodeArgs.substring(0, pos);
							qrcodeArgs = qrcodeArgs.substring(pos+3);
							pos = qrcodeArgs.indexOf("###");
							if(pos != -1){
								final String isSeries = qrcodeArgs.substring(0, pos);
								final String srcOpenId = qrcodeArgs.substring(pos + 3); 
								logger.debug("courseId : {}, isSeries:{}, srcOpenId : {}", courseId, isSeries, srcOpenId);
								new Thread(new Runnable() {
									@Override
									public void run() {
										courseInviteCardService.pushCourseInviteNotify(srcOpenId, inputMsg.getFromUserName(), courseId, isSeries, httpSession);
									}
								}).start();
							}
						}else {
							logger.debug("not found ###");
						}
					}
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
					//outputMsg.setContent("客官，等您很久了～这里既分享各类线下讲座的内容，也允许优质机构和个人在线开办讲座，让用户足不出户参与讲座，并和导师互动。快点击“点豆大讲堂”来看看我们吧～");
					outputMsg.setContent("亲爱的小伙伴，千山万水你还是来了。\r\n欢迎关注UDIY研习社！我们是一个有爱有干货的留学生互助共享平台。\r\n点击下方免费领取微课~\r\n<a href='http://www.udiyclub.com/courses/jsp?id=2&view=detail'>95后博士教你如何制霸北美CS专业</a>\r\n<a href='http://www.udiyclub.com/courses/jsp?id=23&view=detail'>全方位雅思口语短期内大提分</a>\r\n更多有趣实用的留学内容，点击菜单查看哦~");
					try {
						setOutputMsgInfo(outputMsg, inputMsg);
					} catch (Exception e) {
						logger.debug(e.toString());
					}
					xstream.alias("xml", outputMsg.getClass());
					result = new String(xstream.toXML(outputMsg).getBytes());
					logger.debug("xml result : {}", result);
					new Thread(new Runnable() {
						@Override
						public void run() {
							cglxDao.addUserByOpenid(inputMsg.getFromUserName());
						}
					}).start();
					new Thread(new Runnable() {
						@Override
						public void run() {
							courseInviteCardService.pushCourseInviteCard(inputMsg.getFromUserName());
						}
					}).start();
				} else if(("SCAN").equals(inputMsg.getEvent().trim())){
					logger.debug("SCAN, args :{}", inputMsg.getEventKey().trim());
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
					String qrcodeArgs = inputMsg.getEventKey().trim();
					outputMsg.setContent("亲爱的小伙伴，活动您已经支持过了，不能重复支持呦。");
					if(qrcodeArgs != null){
						logger.debug("SCANqrcodeArgs : {}",qrcodeArgs);
						int pos = qrcodeArgs.indexOf("###");
						if(pos != -1) {
							String courseId = qrcodeArgs.substring(0, pos);
							qrcodeArgs = qrcodeArgs.substring(pos+3);
							logger.debug("SCANqrcodeArgs : {}",qrcodeArgs);
							pos = qrcodeArgs.indexOf("###");
							String isSeries = qrcodeArgs.substring(0, pos);
							String srcOpenId = qrcodeArgs.substring(pos + 3);
							logger.debug("SCANqrcodeArgs, srcOpenId: {}, currentOpenId:{}",srcOpenId,inputMsg.getFromUserName());
							if(inputMsg.getFromUserName().equals(srcOpenId)){
								outputMsg.setContent("亲爱的小伙伴，您不能支持您自己呦，赶紧邀请您的好友来支持吧~");
							}
						}else {
							logger.debug("not found ###");
						}
					}
					try {
						setOutputMsgInfo(outputMsg, inputMsg);
					} catch (Exception e) {
						logger.debug(e.toString());
					}
					xstream.alias("xml", outputMsg.getClass());
					result = new String(xstream.toXML(outputMsg).getBytes());
					logger.debug("xml result : {}", result);
				}
			} else if(msgType.equals(WeixinMsgType.Image.toString())) {
				logger.debug("xml result : {}", result);
			}
		} catch (IOException e) {
			logger.info(e.toString());
		} catch (Exception e) {
			logger.info(e.toString());
		}
		logger.debug("finish in processing weixin message.");
		return result;
	}
	
	private static void setOutputMsgInfo(OutputMessage oms, InputMessage msg)
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

}
