package com.ddcb.weixin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ddcb.utils.WeixinTools;
import com.ddcb.weixin.service.IMessageProcessService;
import com.ddcb.weixin.service.ITokenCheckService;

@Controller
public class CommonController {

	private static final Logger logger = LoggerFactory
			.getLogger(CommonController.class);

	@Autowired
	private ITokenCheckService tokenCheckService;
	
	@Autowired
	private IMessageProcessService messageProcessService;

	@RequestMapping("/weixinRequest")
	@ResponseBody
	public String processWeixinRequest(HttpServletRequest request,HttpSession httpSession) {
		String signature = request.getParameter("signature");
		String echostr = request.getParameter("echostr");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String ret = "";
		logger.debug("receive a weixin request");
		if (echostr != null && !echostr.isEmpty()) {
			logger.debug(
					"weixin request token check, signature:{}, echostr:{}, timestamp:{}, nonce:{}",
					signature, echostr, timestamp, nonce);
			ret = tokenCheckService.tokenCheck(signature, echostr, timestamp,
					nonce);
		} else {
			ret = messageProcessService.processWeixinMessage(request,httpSession);
		}
		logger.debug("finish a weixin request");
		return ret;
	}

	@RequestMapping("/getJsConfigInfo")
	@ResponseBody
	public Map<String, String> getJsConfigInfo(HttpServletRequest request) {
		logger.debug("getJsConfigInfo");
		Map<String, String> result = new HashMap<>();
		String url = request.getParameter("url");
		logger.debug("url : {}", url);
		result = WeixinTools.getSign(url);
		logger.debug("finishGetJsConfigInfo");
		logger.debug("resultMap :{}", result.toString());
		return result;
	}

	@RequestMapping("/getOpenIdRedirect")
	public String getOpenIdRedirect(HttpSession httpSession,
			HttpServletRequest request) {
		logger.debug("getOpenIdRedirect");
		String code = request.getParameter("code");
		String id = request.getParameter("id");
		String course_id = request.getParameter("course_id");
		String is_series = request.getParameter("is_series");
		logger.debug("getOpenIdRedirect, id:{},courseId:{},isSeries:{}",id,course_id,is_series);
		String openid = "";
		if (code == null || code.isEmpty()) {
			httpSession.setAttribute("openid", "");
		} else {
			openid = WeixinTools.getOpenId(code);
			httpSession.setAttribute("openid", openid);
			httpSession.setAttribute("user_id", id);
		}
		logger.debug("finishGetOpenIdRedirect");
		logger.debug("code :{}, openId :{}, id :{}", code, openid, id);
		if(("1").equals(is_series)){
			return "redirect:courses/jsp?id="+course_id;
		} else {
			return "redirect:courses/jsp?id="+course_id+"&view=detail";
		}		
	}

	@RequestMapping("/getUserOpenId")
	@ResponseBody
	public Map<String, String> getUserOpenId(HttpSession httpSession,
			HttpServletRequest request) {
		logger.debug("getUserOpenId");
		Map<String, String> result = new HashMap<>();
		String openid = (String) request.getSession().getAttribute("openid");
		result.put("openId", "");
		if (openid == null || openid.isEmpty()) {
			result.put("openId", "");
		} else {
			result.put("openId", openid);
		}
		logger.debug("finishGetUserOpenId");
		logger.debug("resultMap :{}", result.toString());
		return result;
	}
}
