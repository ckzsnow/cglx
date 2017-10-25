package com.ddcb.weixin.service;

import javax.servlet.http.HttpSession;

public interface ICourseInviteCardService {

	 public void pushCourseInviteCard(String openid);
	 
	 public void pushCourseInviteNotify(String srcOpenId, String friendOpenid, String courseId, String isSeries, HttpSession httpSession);
	
}
