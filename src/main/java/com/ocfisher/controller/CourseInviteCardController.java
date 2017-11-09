package com.ocfisher.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.ddcb.utils.WeixinCache;
import com.ddcb.weixin.service.impl.CourseInviteCardServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.ocfisher.dao.ICourseInviteCardDao;

import net.sf.json.JSONObject;

@Controller
public class CourseInviteCardController {

	private static final Logger logger = LoggerFactory.getLogger(CourseInviteCardController.class);

	private static final int BLACK = 0xFF000000;  
	  
	private static final int WHITE = 0xFFFFFFFF;  
	
	@Autowired
	private ICourseInviteCardDao courseInviteCardDao;

	@RequestMapping("/course/deleteCourseInviteCardById")
	@ResponseBody
	public Map<String, String> deleteCourseInviteCardById(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		retMap.put("msg", "删除失败！");
		retMap.put("error", "1");
		String id = request.getParameter("id");
		long courseId_ = 0;
		try {
			courseId_ = Long.valueOf(id);
		} catch(Exception ex){
			logger.error(ex.toString());
		}
		if(courseInviteCardDao.delCourseCard(courseId_)){
			retMap.put("msg", "删除成功！");
			retMap.put("error", "0");
			logger.debug("deleteCourseInviteCardById success!");
		} else {
			logger.debug("deleteCourseInviteCardById fail!");
		}
		return retMap;
	}
	
	@RequestMapping("/course/addCourseInviteCard")
	@ResponseBody
	public Map<String, String> addCourseInviteCard(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, String> retMap = new HashMap<>();
		retMap.put("msg", "添加失败！");
		retMap.put("error", "1");
		String course_id = request.getParameter("course_id");
		String person_count = request.getParameter("person_count");
		String isSeries = request.getParameter("isSeries");
		String copywriter = request.getParameter("copywriter");
		String realPath = "/data/cglx/course_invite_card";
		String inviteCardTemplateImg = "";
		String spreadCardTemplateImg = "";
		if(multipartRequest != null) {
			Iterator<String> ite = multipartRequest.getFileNames();
			int index = 0;
			try {
				while(ite.hasNext()){
					MultipartFile file = multipartRequest.getFile(ite.next());
					String fileName = "";
					if(index == 0){
						spreadCardTemplateImg = UUID.randomUUID().toString() + ".jpg";
						fileName = spreadCardTemplateImg;
						index++;
					} else {
						inviteCardTemplateImg = UUID.randomUUID().toString() + ".jpg";
						fileName = inviteCardTemplateImg;
					}
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
				}
				String spreadQrCodeFileName =  generateSpreadCard(course_id, isSeries, spreadCardTemplateImg); 
				long courseId = Long.valueOf(course_id);
				int isSeries_ = Integer.valueOf(isSeries);
				int needInvitePersonCount = Integer.valueOf(person_count);
				if(courseInviteCardDao.addCourseCard(courseId, isSeries_, inviteCardTemplateImg, needInvitePersonCount, spreadQrCodeFileName, copywriter)){
					retMap.put("msg", "添加成功！");
					retMap.put("error", "0");
				}
			} catch(Exception e) {
				logger.error("Failed in saving file, exception : {}", e.toString());
			}
		}
		return retMap;
	}
	
	@RequestMapping("/course/updateCourseInviteCardPublishStatus")
	@ResponseBody
	public Map<String, String> updateCourseInviteCardPublishStatus(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		retMap.put("msg", "更新失败！");
		retMap.put("error", "1");
		String course_id = request.getParameter("id");
		String status = request.getParameter("status");
		try {
			long courseId = Long.valueOf(course_id);
			int status_ = Integer.valueOf(status);
			if(courseInviteCardDao.updateCourseCardPublishStatus(courseId, status_)){
				retMap.put("msg", "更新成功！");
				retMap.put("error", "0");
			}
		} catch(Exception e) {
			logger.error("Failed in saving file, exception : {}", e.toString());
		}
		return retMap;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/course/getInviteCardByCourseId")
	@ResponseBody
	public void getInviteCardByCourseId(HttpServletRequest request) {
		String open_id = request.getParameter("open_id");
		String course_id = request.getParameter("course_id");
		logger.debug("getInviteCardByCourseId openid : {}", open_id);
		logger.debug("getInviteCardByCourseId course_id : {}", course_id);
		try{
			ObjectMapper om = new ObjectMapper();
			Map<String,Object> retMap = om.readValue(CourseInviteCardServiceImpl.getUserInfoByOpenId(open_id), Map.class);
			String nickname = (String)retMap.get("nickname");
			String headImgUrl = (String)retMap.get("headimgurl");
			String unionid = (String)retMap.get("unionid");
			Map<String, Object> courseMap = courseInviteCardDao.getCourseById(Long.valueOf(course_id));
			Integer isSeries = (Integer)courseMap.get("is_series");
			String templateName = (String)courseMap.get("template_name");
			String args = course_id + "###" + isSeries + "###" + open_id;
			String json = "{\"expire_seconds\": 2592000, \"action_name\": \"QR_STR_SCENE\", \"action_info\""+
					": {\"scene\": {\"scene_str\": \""+args+"\"}}}";
			String action = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
					+ WeixinCache.getAccessToken();
			String ret = CourseInviteCardServiceImpl.connectWeiXinInterface(action, json);
			JSONObject jsonObject = JSONObject.fromObject(ret);
			if (jsonObject.has("url")) {
				System.out.println("url:" + jsonObject.getString("url"));
				String url = jsonObject.getString("url");
				logger.debug("qrcode url : {}", url);
				CourseInviteCardServiceImpl.generateQRCode(url, open_id, templateName);
				logger.debug("pushCourseInviteCard, nickname:{},headImgUrl:{},unionid:{}",nickname,headImgUrl,unionid);
				BufferedImage headImage = ImageIO.read(new URL(headImgUrl));
				CourseInviteCardServiceImpl.generateHeadImageCode(headImage, nickname, open_id);
				CourseInviteCardServiceImpl.pushImageToUser(open_id);
			} else {
				System.out.println(jsonObject.toString());
			}
		}catch(Exception ex){
			logger.error(ex.toString());
		}
	}
	
	
	
	private String generateSpreadCard(String course_id, String is_series, String templateName) {
		String qrCodeFileName = "";
		String args = course_id + "###" + is_series;
		String json = "{\"expire_seconds\": 2592000, \"action_name\": \"QR_STR_SCENE\", \"action_info\""+
				": {\"scene\": {\"scene_str\": \""+args+"\"}}}";
		String action = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="
				+ WeixinCache.getAccessToken();
		String ret = CourseInviteCardServiceImpl.connectWeiXinInterface(action, json);
		JSONObject jsonObject = JSONObject.fromObject(ret);
		if (jsonObject.has("url")) {
			System.out.println("url:" + jsonObject.getString("url"));
			String url = jsonObject.getString("url");
			logger.debug("qrcode url : {}", url);
			qrCodeFileName = generateSpreadQRCode(url, templateName);
		} else {
			System.out.println(jsonObject.toString());
		}
		return qrCodeFileName;
	}
	
	public static String generateSpreadQRCode(String content, String templateName) {
		BufferedImage templateImage = null;
		String realPath = "/data/cglx/course_invite_card";
		String fileName = UUID.randomUUID().toString() + ".jpg";
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
            File imgFile = new File(realPath, fileName);  
            if(!imgFile.exists())  
                imgFile.createNewFile();
            ImageIO.write(templateImage, "jpg", imgFile); 
        } catch (Exception e) {
        	logger.error(e.toString());
        }
        return fileName;
    }
	
}