package com.ocfisher.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

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

import com.ocfisher.dao.ICourseInviteCardDao;

@Controller
public class CourseInviteCardController {

	private static final Logger logger = LoggerFactory.getLogger(CourseInviteCardController.class);

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
					} else {
						inviteCardTemplateImg = UUID.randomUUID().toString() + ".jpg";
						fileName = inviteCardTemplateImg;
					}
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
				}
				long courseId = Long.valueOf(course_id);
				int isSeries_ = Integer.valueOf(isSeries);
				int needInvitePersonCount = Integer.valueOf(person_count);
				if(courseInviteCardDao.addCourseCard(courseId, isSeries_, inviteCardTemplateImg, needInvitePersonCount, spreadCardTemplateImg)){
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
}