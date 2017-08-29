package com.ddcb.weixin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JspController {
	
	@RequestMapping("/getDDCBOpenClass")
	public String getOpenIdRedirect(HttpSession httpSession,
			HttpServletRequest request) {
		return "view/weixinview/ddcb_open_class";
	}
	
	@RequestMapping("/playDDCBOpenClass")
	public String playDDCBOpenClass(HttpSession httpSession,
			HttpServletRequest request) {
		String phone_id = (String) request.getSession().getAttribute("WEIXIN_REG_USER_ID");
		if(phone_id == null || phone_id.isEmpty()) {
			return "redirect:/view/weixinview/login.html";
		}
		return "view/weixinview/ddcb_play_class";
	}
	
	@RequestMapping("/getDDCBDreamClass")
	public String getDDCBDreamClass(HttpSession httpSession,
			HttpServletRequest request) {
		return "view/weixinview/ddcb_dream_class";
	}
	
	@RequestMapping("/weixin/getDDCBBuyVip")
	public String getDDCBBuyVip(HttpSession httpSession,
			HttpServletRequest request) {
		return "view/weixinview/ddcb_buy_vip";
	}
	
	@RequestMapping("/getDDCBLiveClass")
	public String getDDCBLiveClass(HttpSession httpSession,
			HttpServletRequest request) {
		return "view/weixinview/ddcb_live_class";
	}
	
	@RequestMapping("/playDDCBLiveClass")
	public String playDDCBLiveClass(HttpSession httpSession,
			HttpServletRequest request) {
		String phone_id = (String) request.getSession().getAttribute("WEIXIN_REG_USER_ID");
		if(phone_id == null || phone_id.isEmpty()) {
			return "redirect:/view/weixinview/login.html";
		}
		String userAgent = request.getHeader("user-agent");
		if(userAgent!=null) userAgent = userAgent.toLowerCase();
		if(userAgent != null && userAgent.indexOf("android") != -1) {
			return "view/weixinview/ddcb_play_live_class_android";
		} else {
			return "view/weixinview/ddcb_play_live_class";
		}
	}
	
	@RequestMapping("/weixin/getDDCBUserCenter")
	public String getUserCenter(HttpSession httpSession,
			HttpServletRequest request) {
		return "view/weixinview/ddcb_user_center";
	}
	
	@RequestMapping("/weixin/browserLiveClass")
	public String browserLiveClass(HttpSession httpSession,
			HttpServletRequest request) {
		return "view/weixinview/ddcb_browser_live_class";
	}
	
}