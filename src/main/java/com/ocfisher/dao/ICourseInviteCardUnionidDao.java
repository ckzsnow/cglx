package com.ocfisher.dao;

import java.util.List;
import java.util.Map;

public interface ICourseInviteCardUnionidDao {
	
	public List<Map<String, Object>> getNotSendCourseInvite(String unionid);
	
	public List<Map<String, Object>> getInfoByUnionIdAndCourseId(String unionid);
	
	public int getCountByUnionIdAndCourseId(String unionid, long courseid);
	
	public boolean addInfo(String unionId, long courseid, int isSeries);
	
}
