package com.ocfisher.dao;

import java.util.List;
import java.util.Map;

public interface ICourseInviteCardDao {
		
	public List<Map<String, Object>> getAllCourse();
	
	public boolean addCourseCard(long courseId, String templateName);
	
	public boolean addCourseInviteRecord(String srcOpenId, String friendOpenId, long courseId);
	
	public Map<String, Object> getCourseInviteRecord(String srcOpenId, String friendOpenId, long courseId);
	
	public int getCourseInviteSupportTotal(String srcOpenId, long courseId);

}
