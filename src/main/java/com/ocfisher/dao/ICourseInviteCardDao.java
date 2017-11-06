package com.ocfisher.dao;

import java.util.List;
import java.util.Map;

public interface ICourseInviteCardDao {
		
	public List<Map<String, Object>> getAllCourse();
	
	public Map<String, Object> getCourseById(long courseId);
	
	public boolean addCourseActivityExpireRecord(String openId, long courseId);
	
	public int getCourseActivityExpireRecordByOpenIdAndCourseId(String openId, long courseId);
	
	public boolean addCourseCard(long courseId, int isSeries, String templateName, int needInvitePersonCount, String spreadTemplateName);
	
	public boolean delCourseCard(long courseId);
	
	public boolean updateCourseCardPublishStatus(long courseId, int status);
	
	public boolean addCourseInviteRecord(String srcOpenId, String friendOpenId, long courseId);
	
	public Map<String, Object> getCourseInviteRecord(String srcOpenId, String friendOpenId, long courseId);
	
	public int getCourseInviteSupportTotal(String srcOpenId, long courseId);
	
	
	public Map<String, Object> getUserAndUserCourseByUserOpenId(String openId, long courseId);
}
