package com.document.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import com.document.dao.IDocInviteCardDao;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import net.sf.json.JSONObject;

@Controller
public class DocInviteCardController {
	
	private static final Logger logger = LoggerFactory.getLogger(DocInviteCardController.class);
	
	private static final int BLACK = 0xFF000000;  
	  
	private static final int WHITE = 0xFFFFFFFF;  
	
	@Autowired
	private IDocInviteCardDao docInviteCardDao;
	
	@RequestMapping("/document/getInviteCardDoc")
	@ResponseBody
	public List<Map<String, Object>> getInviteCardDoc() {
		return docInviteCardDao.getInviteCardDoc();
	}
	
	@RequestMapping("/document/deleteDocInviteCardById")
	@ResponseBody
	public Map<String, String> deleteDocInviteCardById(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		retMap.put("msg", "删除失败！");
		retMap.put("error", "1");
		String id = request.getParameter("id");
		if(docInviteCardDao.delCourseCard(id)){
			retMap.put("msg", "删除成功！");
			retMap.put("error", "0");
			logger.debug("deleteCourseInviteCardById success!");
		} else {
			logger.debug("deleteCourseInviteCardById fail!");
		}
		return retMap;
	}
	
	@RequestMapping("/document/updateDocInviteCardPublishStatus")
	@ResponseBody
	public Map<String, String> updateDocInviteCardPublishStatus(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		retMap.put("msg", "更新失败！");
		retMap.put("error", "1");
		String course_id = request.getParameter("id");
		String status = request.getParameter("status");
		if(docInviteCardDao.updateCourseCardPublishStatus(course_id, status)){
			retMap.put("msg", "更新成功！");
			retMap.put("error", "0");
		}
		return retMap;
	}
	
	@RequestMapping("/document/addDocInviteCard")
	@ResponseBody
	public Map<String, String> addDocInviteCard(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, String> retMap = new HashMap<>();
		retMap.put("msg", "添加失败！");
		retMap.put("error", "1");
		String id = request.getParameter("id");
		String person_count = request.getParameter("person_count");
		String copywriter = request.getParameter("copywriter");
		String realPath = "/data/cglx/doc_invite_card";
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
				String spreadQrCodeFileName =  generateSpreadCard(id, spreadCardTemplateImg); 
				if(docInviteCardDao.adddocInviteCard(id, inviteCardTemplateImg, person_count, spreadQrCodeFileName, copywriter)){
					retMap.put("msg", "添加成功！");
					retMap.put("error", "0");
				}
			} catch(Exception e) {
				logger.error("Failed in saving file, exception : {}", e.toString());
			}
		}
		return retMap;
	}
	
	private String generateSpreadCard(String id, String templateName) {
		String qrCodeFileName = "";
		String json = "{\"expire_seconds\": 2592000, \"action_name\": \"QR_STR_SCENE\", \"action_info\""+
				": {\"scene\": {\"scene_str\": \""+id+"\"}}}";
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
