package com.ddcb.weixin.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface IMessageProcessService {

	 public String processWeixinMessage(HttpServletRequest request,HttpSession httpSession);
	
}
