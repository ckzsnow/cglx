package com.ddcb.weixin.service;

import javax.servlet.http.HttpServletRequest;

public interface IMessageProcessService {

	 public String processWeixinMessage(HttpServletRequest request);
	
}
