package com.ddcb.utils;

public enum WeixinMsgType {

	Text("text"),
	Image("image"),
	Music("music"),
	Video("video"),
	Voice("voice"),
	Location("location"),
	Link("link"),
	Event("event");
	
	private String msgType = "";

	WeixinMsgType(String msgType) {
		this.msgType = msgType;
	}

	@Override
	public String toString() {
		return msgType;
	}

}
