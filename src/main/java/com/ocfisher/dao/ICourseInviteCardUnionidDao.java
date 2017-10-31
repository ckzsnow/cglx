package com.ocfisher.dao;

import java.util.Map;

public interface ICourseInviteCardUnionidDao {
	
	public Map<String, Object> getInfoByUnionIdAndCourseId(String unionid, long courseid);
	
	public int getCountByUnionIdAndCourseId(String unionid, long courseid);
	
	public boolean addInfo(String unionId, long courseid, int isSeries);
	
}
