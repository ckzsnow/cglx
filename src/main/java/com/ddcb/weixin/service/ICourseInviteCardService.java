package com.ddcb.weixin.service;

import java.util.Map;

public interface ICourseInviteCardService {

	 public void pushCourseInviteCard(String openid);
	 
	 public void pushCourseInviteNotify(String srcOpenId, String friendOpenid, String courseId, String isSeries);

	public Map<String, Object> getInviteCardByCourseId(String courseId);
	
}
