package com.ddcb.weixin.service;

public interface IWeixinPushService {

	public boolean pushTextMessage(String openId, String message);
	
}
