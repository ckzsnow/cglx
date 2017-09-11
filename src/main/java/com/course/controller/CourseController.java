package com.course.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.course.dao.ICourseDao;

@Controller
public class CourseController {
	
	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
	
	@Autowired
	private ICourseDao courseDao;
	
	@RequestMapping("/course/getRecommendCourse")
	@ResponseBody
	public List<Map<String, Object>> getRecommendCourse() {
		return courseDao.getRecommendCourse();
	}
	
	@RequestMapping("/course/getCourseBriefBak")
	@ResponseBody
	public List<Map<String, Object>> getCourseBriefBak() {
		return courseDao.getCourseBriefBak();
	}
	
	@RequestMapping("/course/getCourseByTag")
	@ResponseBody
	public List<Map<String, Object>> getCourseByTag(HttpServletRequest request) {
		String tag = request.getParameter("tag");
		return courseDao.getCourseByTag(tag);
	}
	
	@RequestMapping("/course/addSeries")
	@ResponseBody
	public Map<String, String> addSeries(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, String> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
		int index = 0;
		if(multipartRequest != null) {
			Iterator<String> ite = multipartRequest.getFileNames();
			while(ite.hasNext()) {
				MultipartFile file = multipartRequest.getFile(ite.next());
				fileName = getGernarateFileName(file);
				try {
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
				} catch(Exception e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
				switch(index) {
				case 0:
					paramsMap.put("banner", new String[] {fileName});
					break;
				case 1:
					paramsMap.put("snapshot", new String[] {fileName});
					break;
				case 2:
					paramsMap.put("teacher_image", new String[] {fileName});
					break;
				}
				index ++;
			}
		}
		
		courseDao.addCourse(paramsMap);
		retMap.put("msg", "添加成功！");
		retMap.put("error", "0");
		return retMap;
	}
	
	@RequestMapping("/course/updateCourseByCourseId")
	@ResponseBody
	public Map<String, String> updateCourseByCourseId(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, String> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		List<String> identifiers =  Arrays.asList(paramsMap.get("identifier")[0].split(","));
		paramsMap.remove("identifier");
		List<String> images = Arrays.asList("banner", "snapshot", "teacher_image");;
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
		
		int count = 0;
		if(multipartRequest != null) {
			Iterator<String> ite = multipartRequest.getFileNames();
			while(ite.hasNext()) {
				MultipartFile file = multipartRequest.getFile(ite.next());
				fileName = getGernarateFileName(file);
				try {
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
				} catch (IOException e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
				for(int index=count; index<identifiers.size(); index++) {
					if(identifiers.get(index).equals("0")){
						paramsMap.remove(images.get(index));
						continue;
					}
					else {
						paramsMap.put(images.get(index), new String[] {fileName});
						count = index+1;
						break;
					}
				}
			}
		}
		courseDao.updateCourseByCourseId(paramsMap);
		retMap.put("msg", "添加成功！");
		retMap.put("error", "0");
		return retMap;
	}
	
	@RequestMapping("/course/addSubCourse")
	@ResponseBody
	public Map<String, String> addSubCourse(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, String> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
		if(multipartRequest != null) {
			MultipartFile file = multipartRequest.getFile("snapshot");
			fileName = getGernarateFileName(file);
			try {
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
			} catch(Exception e) {
				logger.error("Failed in saving file, exception : {}", e.toString());
			}
			paramsMap.put("snapshot", new String[] {fileName});
		}
		
		courseDao.addCourse(paramsMap);
		retMap.put("msg", "添加成功！");
		retMap.put("error", "0");
		return retMap;
	}
	
	@RequestMapping("/course/updateSubCourse")
	@ResponseBody
	public Map<String, String> updateSubCourse(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, String> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
		if(multipartRequest != null) {
			MultipartFile file = multipartRequest.getFile("snapshot");
			fileName = getGernarateFileName(file);
			try {
				FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
			} catch(Exception e) {
				logger.error("Failed in saving file, exception : {}", e.toString());
			}
			paramsMap.put("snapshot", new String[] {fileName});
		}
		
		courseDao.addCourse(paramsMap);
		retMap.put("msg", "添加成功！");
		retMap.put("error", "0");
		return retMap;
	}
	
	@RequestMapping("/course/addCourse")
	@ResponseBody
	public Map<String, String> addCourse(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, String> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
		int index = 0;
		if(multipartRequest != null) {
			Iterator<String> ite = multipartRequest.getFileNames();
			while(ite.hasNext()) {
				MultipartFile file = multipartRequest.getFile(ite.next());
				fileName = getGernarateFileName(file);
				try {
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
				} catch(Exception e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
				switch(index) {
				case 0:
					paramsMap.put("snapshot", new String[] {fileName});
					break;
				case 1:
					paramsMap.put("teacher_image", new String[] {fileName});
					break;
				}
				index ++;
			}
		}
		
		courseDao.addCourse(paramsMap);
		retMap.put("msg", "添加成功！");
		retMap.put("error", "0");
		return retMap;
	}
	
	@RequestMapping("/course/updateCourse")
	@ResponseBody
	public Map<String, String> updateCourse(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, String> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
		int index = 0;
		if(multipartRequest != null) {
			Iterator<String> ite = multipartRequest.getFileNames();
			while(ite.hasNext()) {
				MultipartFile file = multipartRequest.getFile(ite.next());
				fileName = getGernarateFileName(file);
				try {
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
				} catch(Exception e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
				switch(index) {
				case 0:
					paramsMap.put("snapshot", new String[] {fileName});
					break;
				case 1:
					paramsMap.put("teacher_image", new String[] {fileName});
					break;
				}
				index ++;
			}
		}
		
		courseDao.addCourse(paramsMap);
		retMap.put("msg", "添加成功！");
		retMap.put("error", "0");
		return retMap;
	}
	
	@RequestMapping("/course/getSeriesCourseName")
	@ResponseBody
	public List<Map<String, Object>> getSeriesCourseCourseName() {
		return courseDao.getSeriesCourseCourseName();
	}
	
	@RequestMapping("/course/deleteCourseById")
	@ResponseBody
	public Map<String, Object> deleteCourseById(HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<>();
		Long course_id = Long.valueOf(request.getParameter("id"));
		if(courseDao.deleteCourseById(course_id)) {
			retMap.put("msg", "删除成功!");
			retMap.put("error_code", "0");
			return retMap;
		} else {
			retMap.put("msg", "删除失败！");
			retMap.put("error_code", "1");
			return retMap;
		}
	}
	
	@RequestMapping("/course/getCourseById")
	@ResponseBody
	public Map<String, Object> getCourseById(HttpServletRequest request) {
		Long course_id = Long.valueOf(request.getParameter("id"));
		return courseDao.getCourseById(course_id);
	}
	
	@RequestMapping("/course/getAllSeriesBrief")
	@ResponseBody
	public List<Map<String, Object>> getAllSeriesBrief() {
		return courseDao.getAllSeriesBrief();
	}
	
	@RequestMapping("/course/getAllSubCourseBrief")
	@ResponseBody
	public List<Map<String, Object>> getAllSubCourseBrief() {
		return courseDao.getAllSubCourseBrief();
	}
	
	@RequestMapping("/course/getSeriesDetailById")
	@ResponseBody
	public Map<String, Object> getSeriesDetailById(HttpServletRequest request) {
		String id = request.getParameter("id");
		String user_id = "";
		long id_ = 0;
		try {
			id_ = Long.valueOf(id);
			user_id = (String)request.getSession().getAttribute("user_id");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return courseDao.getSeriesDetailById(id_, user_id);
	}
	
	@RequestMapping("/course/getSeriesSubCourseById")
	@ResponseBody
	public List<Map<String, Object>> getSeriesSubCourseById(HttpServletRequest request) {
		String id = request.getParameter("id");
		long id_ = 0;
		try {
			id_ = Long.valueOf(id);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return courseDao.getSeriesSubCourseById(id_);
	}
	
	@RequestMapping("/course/getNavRecommendCourse")
	@ResponseBody
	public List<Map<String, Object>> getNavRecommendCourse() {
		return courseDao.getNavRecommendCourse();
	}
	
	@RequestMapping("/course/getPaidCourse")
	@ResponseBody
	public List<Map<String, Object>> getMyCourse(HttpServletRequest request){
		HttpSession session = request.getSession();
		String user_id = (String)session.getAttribute("user_id");
		return courseDao.getPaidCourse(user_id);
	} 
	
	@RequestMapping("/course/getSubcourseDetailById")
	@ResponseBody
	public Map<String, Object> getSubcourseDetailById(HttpServletRequest request) {
		String id = request.getParameter("id");
		String user_id = "";
		int id_ = 0;
		try {
			id_ = Integer.valueOf(id);
			user_id = (String) request.getSession().getAttribute("user_id");
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return courseDao.getSubcourseDetailById(id_, user_id);
	}
	
	@RequestMapping("/course/getParentCourseBrief")
	@ResponseBody
	public Map<String, Object> getParentCourseBrief(HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<>();
		String id = request.getParameter("id");
		long id_ = 0;
		try {
			id_ = Integer.valueOf(id);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		long parent_id = (long)courseDao.getCourseById(id_).get("parent_id");
		if(courseDao.getSeriesDetailById(parent_id, "") == null) {
			return retMap;
		} else {
			retMap = courseDao.getSeriesDetailById(parent_id, "");
			retMap.put("parent_id", parent_id);
			return retMap;
		}
	}
	
	@RequestMapping("/course/getPayStatusByOrderIdAndCourseId")
	@ResponseBody
	public Map<String, Object> getPayStatusByOrderIdAndCourseId(HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<>();
		String courseId = request.getParameter("courseId");
		String orderId = request.getParameter("orderId");
		int pay_status = courseDao.getPayStatusByOrderIdAndCourseId(courseId, orderId);
		retMap.put("pay_status", pay_status);
		return retMap;
	}
	
	@RequestMapping("/course/addFreeCourse")
	@ResponseBody
	public Map<String, Object> addFreeCourse(HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<>();
		String user_id = "";
		String course_id = "";
		try {
			user_id = (String) request.getSession().getAttribute("user_id");
			course_id = request.getParameter("course_id");
		} catch(Exception e) {
			logger.error(e.toString());
		}
		if(courseDao.addFreeCourse(user_id, course_id)) {
			retMap.put("error_code", "0");
			retMap.put("error_msg", "");
			return retMap;
		} else {
			retMap.put("error_code", "-1");
			retMap.put("error_msg", "购买失败，请稍后重试！");
			return retMap;
		} 
	}
	
	@RequestMapping("/course/getPaidList")
	@ResponseBody
	public List<Map<String, Object>> getPaidList() {
		return courseDao.getPaidList();
	}
	
	private String getGernarateFileName(MultipartFile file) {
		String extendName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
		return UUID.randomUUID().toString() + (extendName == null ? ".unknown" : "." + extendName);
	}
}
