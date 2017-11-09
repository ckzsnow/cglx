package com.ddcb.weixin.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
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

import com.ddcb.utils.WebAppCache;
import com.ddcb.utils.WeixinTools;
import com.ddcb.weixin.service.IMessageProcessService;
import com.ddcb.weixin.service.ITokenCheckService;
import com.ocfisher.dao.ICglxDao;
import com.ocfisher.dao.IUserOpenIdUnionIdDao;

@Controller
public class CommonController {

	private static final Logger logger = LoggerFactory
			.getLogger(CommonController.class);

	@Autowired
	private ITokenCheckService tokenCheckService;
	
	@Autowired
	private IMessageProcessService messageProcessService;
	
	@Autowired
	private ICglxDao cglxDao;
	
	@Autowired
	private IUserOpenIdUnionIdDao userOpenIdUnionIdDao;
	
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
	public String getOpenIdRedirect(HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		logger.debug("getOpenIdRedirect");
		String code = request.getParameter("code");
		String view = request.getParameter("view");
		logger.debug("getOpenIdRedirect, code:{}",code);
		logger.debug("getOpenIdRedirect, view:{}",view);
		if (code == null || code.isEmpty()) {
			httpSession.setAttribute("openid", "");
		} else {
			Map<String, Object> retMap = WeixinTools.getUserWeixinInfo(code);
			String nickname = (String)retMap.get("nickname");
			String headImgUrl = (String)retMap.get("headimgurl");
			String unionid = (String)retMap.get("unionid");
			String openid = (String)retMap.get("openid");
			logger.debug("getOpenIdRedirect, nickname:{},headImgUrl:{},unionid:{},openid:{}",nickname,headImgUrl,unionid,openid);
			long pId = cglxDao.addUserByOpenid(unionid, nickname,headImgUrl);
			logger.debug("getOpenIdRedirect, pId:{}",pId);
			httpSession.setAttribute("user_id", pId);
			httpSession.setAttribute("openid", unionid);
			httpSession.setAttribute("openid_", openid);
			new Thread(new Runnable(){
				@Override
				public void run() {
					Map<String, Object> map = cglxDao.getUserByOpenId(unionid);
					logger.debug("getOpenIdRedirect, getUserByOpenId map:{}",map==null?"is null":map.toString());
					if(map == null || map.isEmpty()) {
						logger.debug("getOpenIdRedirect, getUserByOpenId is null!");
					} else {
						String name = (String)map.get("name");
						logger.debug("getOpenIdRedirect, getUserByOpenId name :{}", name);
						if(name == null || ("null").equals(name) || name.isEmpty()){
							cglxDao.updateUserInfoByOpenId(unionid, nickname, headImgUrl);
						}
						Map<String, Object> retMap = userOpenIdUnionIdDao.getUserByOpenIdAndUnionId(openid, unionid);
						if(retMap == null || retMap.isEmpty()){
							logger.debug("getUserWeixinInfo, userOpenIdUnionIdDao, getUserByOpenIdAndUnionId not found.");
							userOpenIdUnionIdDao.addUser(openid, unionid);
						} else {
							logger.debug("getUserWeixinInfo, userOpenIdUnionIdDao, getUserByOpenIdAndUnionId found.");
						}
					}
				}}
			).start();
		}
		logger.debug("finishGetOpenIdRedirect");
		view = view.replaceAll("_", "/").replaceAll("ARGS", "?").replaceAll("ARG","&");
		logger.debug("getOpenIdRedirect, view:{}",view);
		return "forward:" + view;
	}
	
	@RequestMapping("/getUnionIdRedirectForCourseInviteCard")
	public String getUnionIdRedirectForCourseInviteCard(HttpSession httpSession,
			HttpServletRequest request) {
		logger.debug("getOpenIdRedirect");
		String code = request.getParameter("code");
		String id = request.getParameter("id");
		String course_id = request.getParameter("course_id");
		String is_series = request.getParameter("is_series");
		String unionid = "";
		logger.debug("getOpenIdRedirect, id:{},courseId:{},isSeries:{}",id,course_id,is_series);
		if (code == null || code.isEmpty()) {
			httpSession.setAttribute("openid", "");
		} else {
			Map<String, Object> retMap = WeixinTools.getUserWeixinInfo(code);
			unionid = (String)retMap.get("unionid");
			httpSession.setAttribute("openid", unionid);
			httpSession.setAttribute("user_id", id);
		}
		logger.debug("finishGetOpenIdRedirect");
		logger.debug("code :{}, openId :{}, id :{}", code, unionid, id);
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
	
	@RequestMapping("/getQrcodeUrl")
	public String getQrcodeUrl(HttpServletRequest request) {
		String redirect = request.getParameter("redirect");
		redirect = URLEncoder.encode(redirect);
		/*redirect = redirect.replace("/", "SPRI")
				.replace("?", "QUES")
				.replace("=", "EQUA");*/
		logger.debug("redirect : {}", redirect);
		String url = WebAppCache.generateQrcodeUrl("uri="+redirect);
		logger.debug("qrcode url :{}", url);
		return "redirect:" + url;
	}
	
	@RequestMapping("/wxLoginSuccess")
	public String wxLoginSuccess(HttpServletRequest request) {
		String unionid = "";
		String nickname = "";
		String headimgurl = "";
		String openid = "";
		
		logger.debug("wxLoginSuccess");
		String code = request.getParameter("code");
		HttpSession httpSession = request.getSession();
		logger.debug("wxLoginSuccess, code:{}", code);
		Map<Object, Object> userInfoMap = new HashMap<>();
		String id = "";
		if (code == null || code.isEmpty()) {
			httpSession.setAttribute("unionid", "");
			httpSession.setAttribute("openid", "");
		} else {
			userInfoMap = WebAppCache.getUserInfoMap(code);
			logger.debug("wxLoginSuccess userInfoMap : {}", userInfoMap.toString());
			
			if (userInfoMap.containsKey("unionid"))
				unionid = (String) userInfoMap.get("unionid");
			if (userInfoMap.containsKey("nickname"))
				nickname = (String) userInfoMap.get("nickname");
			if (userInfoMap.containsKey("headimgurl"))
				headimgurl = (String) userInfoMap.get("headimgurl");
			if (userInfoMap.containsKey("openid"))
				openid = (String) userInfoMap.get("openid");
			
			logger.debug("wxLoginSuccess uninid : {}", unionid);
			logger.debug("wxLoginSuccess nickname : {}", nickname);
			logger.debug("wxLoginSuccess headimgurl : {}", headimgurl);
			logger.debug("wxLoginSuccess openid : {}", openid);
			
			if(unionid != null && !unionid.isEmpty()
					&& nickname != null && !nickname.isEmpty()
					&& headimgurl != null && !headimgurl.isEmpty()
					&& openid != null && !openid.isEmpty()) {
				
				try{
					cglxDao.addUserByOpenid(unionid, nickname, headimgurl);
					Map<String, Object> userMap = cglxDao.getUserByOpenId(unionid);
					logger.debug("execute getUserByOpenId map : {}", userMap.toString());
					id = String.valueOf(userMap.get("id"));
					if(id != null && !id.isEmpty()) {
						httpSession.setAttribute("user_id", id);
					}
					httpSession.setAttribute("openid", openid);
					httpSession.setAttribute("unionid", openid);
				} catch(Exception e) {
					logger.error(e.toString());
				}
			}
		}
		logger.debug("finishGetOpenIdRedirect");
		logger.debug("code :{}, openId :{}, id :{}", code, openid, id);
		String redirect = request.getParameter("uri");
		/*redirect = redirect.replace("SPRI", "/")
				.replace("QUES", "?")
				.replace("EQUA", "=");*/
		redirect = URLDecoder.decode(redirect);
		logger.debug("redirect : {}", redirect);
		return "redirect:" + redirect;
	}
	
}
