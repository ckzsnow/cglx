package com.ddcb.weixin.service;

public interface ICourseInviteCardService {

	 public void pushCourseInviteCard(String openid);
	 
	 public void pushCourseInviteNotify(String srcOpenId, String friendOpenid, String courseId);
	
}
