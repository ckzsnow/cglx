package com.ddcb.weixin.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.dao.ICourseDao;
import com.ddcb.utils.WeixinCache;
import com.ddcb.weixin.service.ICourseInviteCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.ocfisher.dao.ICourseInviteCardDao;

import net.sf.json.JSONObject;

@Service("courseInviteCardService")
public class CourseInviteCardServiceImpl implements ICourseInviteCardService {

	private static final Logger logger = LoggerFactory
			.getLogger(CourseInviteCardServiceImpl.class);
	
	private static final int BLACK = 0xFF000000;  
	  
	private static final int WHITE = 0xFFFFFFFF;  
	
	@Autowired
	private ICourseInviteCardDao courseInviteCardDao;
	
	@Autowired
	private ICourseDao courseDao;

	@SuppressWarnings("unchecked")
	@Override
	public void pushCourseInviteCard(String openid) {
		try{
			List<Map<String, Object>> courseList = courseInviteCardDao.getAllCourse();
			logger.debug("invite card course list : {}", courseList.toString());
			logger.debug("openid : {}", openid);
			ObjectMapper om = new ObjectMapper();
			Map<String,Object> retMap = om.readValue(getUserInfoByOpenId(openid), Map.class);
			String nickname = (String)retMap.get("nickname");
			String headImgUrl = (String)retMap.get("headimgurl");
			String unionid = (String)retMap.get("unionid");
			for(Map<String, Object> courseMap : courseList) {
				Long courseId = (Long)courseMap.get("course_id");
				Integer isSeries = (Integer)courseMap.get("is_series");
				String templateName = (String)courseMap.get("template_name");
				String args = courseId + "###" + isSeries + "###" + openid;
				String json = "{\"expire_seconds\": 2592000, \"action_name\": \"QR_STR_SCENE\", \"action_info\""+
						": {\"scene\": {\"scene_str\": \""+args+"\"}}}";
				String action = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
						+ WeixinCache.getAccessToken();
				String ret = connectWeiXinInterface(action, json);
				JSONObject jsonObject = JSONObject.fromObject(ret);
				if (jsonObject.has("url")) {
					System.out.println("url:" + jsonObject.getString("url"));
					String url = jsonObject.getString("url");
					logger.debug("qrcode url : {}", url);
					generateQRCode(url, openid, templateName);
					logger.debug("pushCourseInviteCard, nickname:{},headImgUrl:{},unionid:{}",nickname,headImgUrl,unionid);
					BufferedImage headImage = ImageIO.read(new URL(headImgUrl));
					generateHeadImageCode(headImage, nickname, openid);
					pushImageToUser(openid);
				} else {
					System.out.println(jsonObject.toString());
				}
			}
		}catch(Exception ex){
			logger.error(ex.toString());
		}
	}
	
	private static String getUserInfoByOpenId(String openId){
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
			logger.debug("CourseInviteCardServiceImpl getUserInfoByOpenId result : {}", ret);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return ret;	
	}
	
	public static String connectWeiXinInterface(String action, String json) {
		String ret = "";
		URL url;
		try {
			url = new URL(action);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			OutputStream os = http.getOutputStream();
			os.write(json.getBytes("UTF-8"));
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			ret = new String(jsonBytes, "UTF-8");
			logger.debug("push result : {}", ret);
			os.flush();
			os.close();
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return ret;
	}
	
	public static void generateQRCode(String content, String unionId, String templateName) {
		BufferedImage templateImage = null;
        try {
        	templateImage = ImageIO.read(new File("/data/cglx/course_invite_card/"+templateName));
            int width = 200;  
            int height = 200;
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,  
			        BarcodeFormat.QR_CODE, width, height, hints);
            for (int i = 0; i < width; i++) {  
                for (int j = 0; j < height; j++) {  
                	templateImage.setRGB(36+i, 976+j, bitMatrix.get(i, j)? BLACK : WHITE);  
                }  
            }
            File imgFile = new File("/data/cglx/course_invite_card/"+unionId+".jpg");  
            if(!imgFile.exists())  
                imgFile.createNewFile();
            ImageIO.write(templateImage, "jpg", imgFile); 
        } catch (Exception e) {
        	logger.error(e.toString());
        }
    }
	
	public static void generateHeadImageCode(BufferedImage headImage, String nickName, String openId) {
        try {
        	BufferedImage templateImage = ImageIO.read(new File("/data/cglx/course_invite_card/"+openId + ".jpg"));
        	Image scaledImage = headImage.getScaledInstance(117, 117,Image.SCALE_DEFAULT);
        	BufferedImage finalImage = new BufferedImage(117, 117, BufferedImage.TYPE_INT_BGR);
        	finalImage.createGraphics().drawImage(scaledImage, 0, 0, null);
            for (int i = 0; i < finalImage.getWidth(); i++) {  
                for (int j = 0; j < finalImage.getHeight(); j++) {  
                	templateImage.setRGB(23+i, 20+j, finalImage.getRGB(i, j));  
                }  
            }
            Graphics2D g=templateImage.createGraphics();
            g.setColor(Color.black);
            g.setBackground(Color.white);
            g.setFont(new Font("SimSun",Font.PLAIN,28));
            g.drawString(nickName,166,100);
            g.dispose();
            File imgFile = new File("/data/cglx/course_invite_card/"+openId+".jpg");  
            if(!imgFile.exists())  
                imgFile.createNewFile();
            ImageIO.write(templateImage, "jpg", imgFile); 
        } catch (Exception e) {
        	logger.error(e.toString());
        }
    }
	
	public static void pushImageToUser(String openId){
		try {
			String result = null;
			File file = new File("/data/cglx/course_invite_card/"+openId+".jpg");
			URL url = new URL("https://api.weixin.qq.com/cgi-bin/media/upload?access_token="
					+ WeixinCache.getAccessToken() + "&type=image");
			long filelength = file.length();
			String fileName = file.getName();
			String type = "image/jpeg";
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			String BOUNDARY = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			// 请求正文信息
			// 第一部分：

			StringBuilder sb = new StringBuilder();

			// 这块是post提交type的值也就是文件对应的mime类型值
			sb.append("--"); // 必须多两道线
								// 这里说明下，这两个横杠是http协议要求的，用来分隔提交的参数用的，不懂的可以看看http
								// 协议头
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"type\" \r\n\r\n"); // 这里是参数名，参数名和值之间要用两次
			sb.append(type + "\r\n"); // 参数的值

			/**
			 * 这里重点说明下，上面两个参数完全可以卸载url地址后面 就想我们平时url地址传参一样，
			 * http://api.weixin.qq.com/cgi-bin/material/add_material?
			 * access_token=##ACCESS_TOKEN##&type=""&description={}
			 * 这样，如果写成这样，上面的 那两个参数的代码就不用写了，不过media参数能否这样提交我没有试，感兴趣的可以试试
			 */

			sb.append("--"); // 必须多两道线
			sb.append(BOUNDARY);
			sb.append("\r\n");
			// 这里是media参数相关的信息，这里是否能分开下我没有试，感兴趣的可以试试
			sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + fileName + "\";filelength=\""
					+ filelength + "\" \r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			System.out.println(sb.toString());
			byte[] head = sb.toString().getBytes("utf-8");
			// 获得输出流
			OutputStream out = new DataOutputStream(con.getOutputStream());
			// 输出表头
			out.write(head);
			// 文件正文部分
			// 把文件已流文件的方式 推入到url中
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			// 结尾部分，这里结尾表示整体的参数的结尾，结尾要用"--"作为结束，这些都是http协议的规定
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);
			out.flush();
			out.close();
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = null;
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
			// 使用JSON-lib解析返回结果
			JSONObject jsonObject = JSONObject.fromObject(result);
			if (jsonObject.has("media_id")) {
				System.out.println("media_id:" + jsonObject.getString("media_id"));
			} else {
				System.out.println(jsonObject.toString());
			}
			System.out.println("json:" + jsonObject.toString());

			String json = "{\"touser\": \"" + openId + "\",\"msgtype\": \"image\", \"image\": {\"media_id\": \""
					+ jsonObject.getString("media_id") + "\"}}";
			String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
					+ WeixinCache.getAccessToken();
			logger.debug("pushImageMessage openId : {}", openId);
			try {
				connectWeiXinInterface(action, json);
			} catch (Exception e) {
				logger.error("weixin push failed, exception : {}", e.toString());
			}
		} catch (Exception e) {
			logger.debug(e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pushCourseInviteNotify(String srcOpenId, String friendOpenId, String courseId, String isSeries) {
		long courseId_ = 0;
		try {
			courseId_ = Long.valueOf(courseId);
			ObjectMapper om = new ObjectMapper();
			Map<String,Object> retMap = om.readValue(getUserInfoByOpenId(friendOpenId), Map.class);
			String friendName = (String)retMap.get("nickname");
			String friendUnionId = (String)retMap.get("unionid");
			retMap = om.readValue(getUserInfoByOpenId(srcOpenId), Map.class);
			String srcUnionId = (String)retMap.get("unionid");
			Map<String, Object> inviteCardCourseMap = courseInviteCardDao.getCourseById(courseId_);
			if(inviteCardCourseMap == null){				
				int expireCount = courseInviteCardDao.getCourseActivityExpireRecordByOpenIdAndCourseId(srcUnionId, courseId_);
				if(expireCount > 0) {
					logger.debug("has push expire message!");
					return;
				} else {
					courseInviteCardDao.addCourseActivityExpireRecord(srcUnionId, courseId_);
				}
				Map<String,Object> courseInfoMap = null;
				if(("1").equals(isSeries)){
					courseInfoMap = courseDao.getSeriesDetailById((long)courseId_, "");
				} else {
					courseInfoMap = courseDao.getSubcourseDetailById((int)courseId_, "");
				}
				logger.debug("pushCourseInviteNotify, course not found in course_invite_card, courseid :{}", courseId_);
				String json = "{\"touser\": \"" + srcOpenId + "\",\"msgtype\": \"text\", \"text\": {\"content\": \"亲爱的小伙伴，您参与的课程'"+(String)courseInfoMap.get("title")+"'的邀请卡活动已经过期，更多活动请关注公众号的最新通知~\"}}";
				String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
						+ WeixinCache.getAccessToken();
				try {
					connectWeiXinInterface(action, json);
				} catch (Exception e) {
					logger.error("weixin push failed, exception : {}", e.toString());
				}
				return;
			}
			Map<String, Object> map = courseInviteCardDao.getCourseInviteRecord(srcUnionId, friendUnionId, courseId_);
			if(map != null) {
				String json = "{\"touser\": \"" + friendOpenId + "\",\"msgtype\": \"text\", \"text\": {\"content\": \"亲爱的小伙伴，活动您已经支持过了，不能重复支持呦~\"}}";
				String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
						+ WeixinCache.getAccessToken();
				try {
					connectWeiXinInterface(action, json);
				} catch (Exception e) {
					logger.error("weixin push failed, exception : {}", e.toString());
				}
			} else {
				if(courseInviteCardDao.addCourseInviteRecord(srcUnionId, friendUnionId, courseId_)){
					logger.debug("courseInviteCardDao, add success");
					int currentSupportTotal = courseInviteCardDao.getCourseInviteSupportTotal(srcUnionId, courseId_);
					logger.debug("courseInviteCardDao, currentSupportTotal={}", currentSupportTotal);
					Map<String, Object> courseMap = courseInviteCardDao.getCourseById(courseId_);
					if(courseMap == null){
						logger.debug("pushCourseInviteNotify, course not found in course_invite_card, courseid :{}", courseId_);
						return;
					}
					int needInvitePersonCount = (Integer)courseMap.get("need_invite_person_count");
					Map<String,Object> courseInfoMap = null;
					if(("1").equals(isSeries)){
						courseInfoMap = courseDao.getSeriesDetailById((long)courseId_, "");
					} else {
						courseInfoMap = courseDao.getSubcourseDetailById((int)courseId_, "");
					}
					if(courseInfoMap == null || courseInfoMap.isEmpty()) {
						logger.debug("pushCourseInviteNotify not found courseInfo, courseid:{}", courseId_);
					} else {
						logger.debug("pushCourseInviteNotify courseInfoMap : {}", courseInfoMap);
					}
					String coursePrice = (String)courseInfoMap.get("cost");
					String courseTitle = (String)courseInfoMap.get("title");
					Date currentTime = new Date();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dateString = formatter.format(currentTime);
					if(currentSupportTotal >= needInvitePersonCount) {
						//需要添加用户已经购买课程，同时如果已经添加，那么不应该再次推送消息给用户
						Map<String, Object> uMap = courseInviteCardDao.getUserAndUserCourseByUserOpenId(srcUnionId, courseId_);
						if(uMap != null && !uMap.isEmpty() && ((String)uMap.get("user_course_id"))!=null){
							logger.debug("pushCourseInviteNotify, has pushed!");
							logger.debug("pushCourseInviteNotify getUserCourseCountByUserOpenId : {}", uMap.toString());
						} else if(uMap != null && !uMap.isEmpty() && uMap.get("user_course_id")==null){
							logger.debug("pushCourseInviteNotify getUserCourseCountByUserOpenId : {}", uMap.toString());
							if(courseDao.addUserCourseAndPayStatus(String.valueOf(uMap.get("user_id")), courseId, "invite_card")){
								String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf139053a88924f58&redirect_uri=http%3A%2F%2Fwww.udiyclub.com%2FgetUnionIdRedirectForCourseInviteCard%3Fid%3D"+String.valueOf(uMap.get("user_id"))+"%26course_id%3D"+courseId+"%26is_series%3D"+isSeries+"&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
								String json = "{"+""
										+ "\"touser\": \"" + srcOpenId + "\","
										+ "\"template_id\": \"Zf7etJwv9pv2RSTo95WuMIcLMNMQRNCrTFMChsx79Is\","
										+ "\"url\": \""+url+"\","
										+ "\"miniprogram\": {\"appid\":\"\",\"pagepath\":\"\"},"
										+ "\"data\": {"
										+     "\"first\": {\"value\":\"收到好友【助攻】x1，目前【助攻】总数："+String.valueOf(currentSupportTotal)+"\",\"color\":\"\"},"
										+     "\"keyword1\": {\"value\":\""+friendName+"\",\"color\":\"\"},"
										+     "\"keyword2\": {\"value\":\""+dateString+"\",\"color\":\"\"},"
										+     "\"remark\": {\"value\":\"您已经获得"+String.valueOf(needInvitePersonCount)+"个好友的【助攻】，现在可以免费获得价值"+coursePrice+"元的'"+courseTitle+"'课程啦~点击详情即可进入！\",\"color\":\"\"}"
										+ "}"
										+ "}";
								logger.debug("pushCourseInviteNotify, json dta : {}", json);
								String action = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
										+ WeixinCache.getAccessToken();
								try {
									String ret = connectWeiXinInterface(action, json);
									logger.debug("pushCourseInviteNotify ret : {}", ret);
								} catch (Exception e) {
									logger.error("weixin push failed, exception : {}", e.toString());
								}
							} else {
								logger.debug("pushCourseInviteNotify, add usercourse into db fail.");
							}
						}
					} else if(currentSupportTotal<needInvitePersonCount){
						String json = "{"+""
								+ "\"touser\": \"" + srcOpenId + "\","
								+ "\"template_id\": \"Zf7etJwv9pv2RSTo95WuMIcLMNMQRNCrTFMChsx79Is\","
								+ "\"url\": \"\","
								+ "\"miniprogram\": {\"appid\":\"\",\"pagepath\":\"\"},"
								+ "\"data\": {"
								+     "\"first\": {\"value\":\"收到好友【助攻】x1，目前【助攻】总数："+String.valueOf(currentSupportTotal)+"\",\"color\":\"\"},"
								+     "\"keyword1\": {\"value\":\""+friendName+"\",\"color\":\"\"},"
								+     "\"keyword2\": {\"value\":\""+dateString+"\",\"color\":\"\"},"
								+     "\"remark\": {\"value\":\"还差"+String.valueOf(needInvitePersonCount-currentSupportTotal)+"个好友【助攻】就可以免费获得价值"+coursePrice+"元的'"+courseTitle+"'课程啦~再接再厉呦！\",\"color\":\"\"}"
								+ "}"
								+ "}";
						logger.debug("pushCourseInviteNotify, json dta : {}", json);
						String action = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
								+ WeixinCache.getAccessToken();
						try {
							String ret = connectWeiXinInterface(action, json);
							logger.debug("pushCourseInviteNotify ret : {}", ret);
						} catch (Exception e) {
							logger.error("weixin push failed, exception : {}", e.toString());
						}
					}
				} else {
					logger.debug("courseInviteCardDao, add fail.");
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}
	
	public static void main(String[] args) throws IOException{
		
	}

	@Override
	public Map<String, Object> getInviteCardByCourseId(String courseId) {
		return courseInviteCardDao.getInviteCardByCourseId(courseId);
	}
}
