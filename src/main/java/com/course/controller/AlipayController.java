package com.course.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.course.dao.ICourseDao;
import com.ddcb.utils.RealCostUtil;
import com.ocfisher.alipay.AlipayNotify;
import com.ocfisher.alipay.AlipayService;

@Controller
public class AlipayController {
	
	private static final Logger logger = LoggerFactory.getLogger(AlipayController.class);
	
	@Autowired
	private ICourseDao courseDao;
	
	@RequestMapping("/pay")
	@ResponseBody
	public String pay(HttpServletRequest request) throws ServletException, IOException {
		String user_id = (String)request.getSession().getAttribute("user_id");
		String course_id = request.getParameter("course_id");
		Map<String, Object> courseMap = courseDao.getCourseById(Long.valueOf(course_id));
		double cost = RealCostUtil.getRealCost(courseMap.get("cost"), courseMap.get("starttime"), courseMap.get("deadline"), courseMap.get("rebate"));
		String tradeNo = UUID.randomUUID().toString();
		if(!courseDao.addUserCourse(user_id, course_id, tradeNo)) {
			//TODO
			return "error";
		} else {
			AlipayService service = new AlipayService();
			String rHtml = service.buildAlipayRequest(Double.valueOf(cost), tradeNo);
			return rHtml;
		}
	}
	
	@RequestMapping("/payCashback")
	@ResponseBody
	public String payCashback(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AlipayService service = new AlipayService();
		String rHtml = service.buildPayoffRequest();
		return rHtml;
	}
	
	@RequestMapping("/payCashbackFinished")
	@ResponseBody
	public String payCashbackFinished(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AlipayService service = new AlipayService();
		String rHtml = service.buildPayoffRequest();
		return rHtml;
	}
	
	@RequestMapping("/payFinished")
	public String payFinished(HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String,String> params = new HashMap<String,String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			if(("pAction").equals(name)) {
				continue;
			}
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		params.put("body", "thank you!");
		params.put("subject", "course cost");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String orderNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"utf-8");
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"utf-8");
		//交易状态
		//String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"utf-8");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		//计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params);
		if(verify_result) {
			courseDao.updateUserCourseByTradeNo(orderNo, 1);
			return "redirect:/view/mycourse.html";
		} else {
			Map<String, Object> courseMap = courseDao.getCourseByTradeNo(orderNo);
			if((Integer)courseMap.get("is_series") == 1) {
				return "redirect:/courses/views/seriesDetail.html?id=" + courseMap.get("course_id");
			} else {
				return "redirect:/courses/views/detail.html?id=" + courseMap.get("course_id");
			}
		}
	}
	
	@RequestMapping("/payAsyFinished")
	public String payAsyFinished(HttpServletRequest request) throws UnsupportedEncodingException {
		Map<String,String> params = new HashMap<String,String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		params.put("body", "感谢您购买UDIY研习社课程!");
		params.put("subject", "课程费用");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		String orderNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"utf-8");
		//支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"utf-8");
		//交易状态
		//String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"utf-8");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		//计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params);
		if(verify_result) {
			if(courseDao.updateUserCourseByTradeNo(orderNo, 1)) {
				logger.debug("trade_no : {}, success", orderNo);
			} else {
				logger.debug("trade_no : {}, failed", orderNo);
			}
		} else {
			//TODO
			courseDao.updateUserCourseByTradeNo(orderNo, 0);
		}
		return "success";
	}
}
