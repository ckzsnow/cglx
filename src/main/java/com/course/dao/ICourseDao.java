package com.course.dao;

import java.util.List;
import java.util.Map;

public interface ICourseDao {

	List<Map<String, Object>> getRecommendCourse();

	List<Map<String, Object>> getAllCourse();

	List<Map<String, Object>> getCourseByTag(String tag);

	List<Map<String, Object>> getSeriesCourseCourseName();

	long addCourse(Map<String, String[]> paramsMap);

	List<Map<String, Object>> getCourseBriefBak();
	
	List<Map<String, Object>> getInviteCardCourse();

	boolean deleteCourseById(Long course_id);

	Map<String, Object> getCourseById(Long course_id);

	List<Map<String, Object>> getAllSeriesBrief();

	List<Map<String, Object>> getAllSubCourseBrief();

	Map<String, Object> getSeriesDetailById(Long id, String user_id);

	List<Map<String, Object>> getSeriesSubCourseById(Long id_);

	List<Map<String, Object>> getNavRecommendCourse();

	List<Map<String, Object>> getPaidCourse(String user_id);

	boolean addUserCourse(String user_id, String course_id, String tradeNo);
	
	boolean addUserCourseAndPayStatus(String user_id, String course_id, String tradeNo);
	
	Map<String, Object> getCourseByTradeNo(String trade_no);

	boolean updateUserCourseByTradeNo(String trade_no, int status);

	Map<String, Object> getSubcourseDetailById(int id, String user_id);

	int getPayStatusByOrderIdAndCourseId(String courseId, String orderId);

	List<Map<String, Object>> getReminderCourse();
	boolean addFreeCourse(String user_id, String course_id);

	boolean updateCourseByCourseId(Map<String, String[]> paramsMap);

	List<Map<String, Object>> getPaidList();

	List<Map<String, Object>> getPaidUserListByCourseId(String course_id);

	boolean addPlayHistory(String user_id, String course_id);

	List<Map<String, Object>> getPlayHistoryList();

	boolean updateUserCourseById(String userCourseId);

}
