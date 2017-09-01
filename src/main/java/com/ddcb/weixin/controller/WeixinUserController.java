package com.ddcb.weixin.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.InputSource;

import com.course.dao.ICourseDao;
import com.ddcb.utils.WeixinPayUtils;
import com.ddcb.utils.WxPayDto;
import com.ddcb.utils.WxPayResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

@Controller
public class WeixinUserController {

	private static final Logger logger = LoggerFactory.getLogger(WeixinUserController.class);

	@Autowired
	private ICourseDao courseDao;
	
	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	@RequestMapping("/courses/jsp")
	public String jspRedirect(HttpServletRequest request) {
		String id = request.getParameter("id");
		String view = request.getParameter("view");
		request.getSession().setAttribute("course_id", id);
		if (view == null || view.isEmpty()) {
			return "courses/views/seriesDetail";
		} else {
			return "courses/views/" + view;
		}
	}

	@RequestMapping("/weixinUserCoursePay")
	@ResponseBody
	public String weixinUserCoursePay(HttpSession httpSession, HttpServletRequest request) {
		String userId = (String) httpSession.getAttribute("user_id");
		String openId = (String) httpSession.getAttribute("openid");
		String courseId = request.getParameter("course_id");
		Map<String, Object> courseMap = courseDao.getCourseById(Long.valueOf(courseId));
		String fee = (String) courseMap.get("cost");
		logger.debug("weixinUserCoursePay course_id : {}", courseId);
		if (userId == null || userId.isEmpty()) {
			return "\"error_msg\":\"no user id\"";
		}
		WeixinPayUtils.setNotifyurl("http://www.udiyclub.com/weixinUserCoursePayResult");
		logger.debug("weixinUserCoursePay openid : {}", openId);
		logger.debug("weixinUserCoursePay fee : {}", fee);
		WxPayDto tpWxPay = new WxPayDto();
		tpWxPay.setOpenId(openId);
		tpWxPay.setBody("UDIY研习社");
		tpWxPay.setOrderId(WeixinPayUtils.getNonceStr());
		tpWxPay.setSpbillCreateIp(request.getRemoteAddr());
		tpWxPay.setTotalFee(fee);
		tpWxPay.setAttach(courseId);
		String finalPK = WeixinPayUtils.getPackage(tpWxPay);
		if (finalPK == null || finalPK.isEmpty()) {
			return "\"error_msg\":\"微信服务器无法获取到支付ID，请稍后重试！\"";
		}
		if (courseDao.addUserCourse(userId, courseId, tpWxPay.getOrderId())) {
			return finalPK;
		} else {
			return "\"error_msg\":\"写数据库错误，请稍后重试！\"";
		}
	}

	@RequestMapping("/weixinUserCoursePayAtPC")
	@ResponseBody
	public String weixinUserCoursePayAtPC(HttpSession httpSession, HttpServletRequest request) {
		String userId = (String) httpSession.getAttribute("user_id");
		String openId = (String) httpSession.getAttribute("openid");
		String courseId = request.getParameter("course_id");
		Map<String, Object> courseMap = courseDao.getCourseById(Long.valueOf(courseId));
		String fee = (String) courseMap.get("cost");
		logger.debug("weixinUserCoursePay course_id : {}", courseId);
		if (userId == null || userId.isEmpty()) {
			return "\"error_msg\":\"no user id\"";
		}
		WeixinPayUtils.setNotifyurl("http://www.udiyclub.com/weixinUserCoursePayResult");
		logger.debug("weixinUserCoursePay openid : {}", openId);
		logger.debug("weixinUserCoursePay fee : {}", fee);
		WxPayDto tpWxPay = new WxPayDto();
		tpWxPay.setOpenId(openId);
		tpWxPay.setBody("UDIY研习社");
		tpWxPay.setOrderId(WeixinPayUtils.getNonceStr());
		tpWxPay.setSpbillCreateIp(request.getRemoteAddr());
		tpWxPay.setTotalFee(fee);
		tpWxPay.setAttach(courseId);
		String qrcode = WeixinPayUtils.getCodeurl(tpWxPay);
		if (qrcode == null || qrcode.isEmpty()) {
			return "\"error_msg\":\"微信服务器无法获取到支付支付二维码，请稍后重试！\"";
		}
		if (courseDao.addUserCourse(userId, courseId, tpWxPay.getOrderId())) {
			return generateQrcode(qrcode);
		} else {
			return "\"error_msg\":\"写数据库错误，请稍后重试！\"";
		}
	}

	@RequestMapping("/weixinUserCoursePayResult")
	@ResponseBody
	public String weixinUserCoursePayResult(HttpSession httpSession, HttpServletRequest request) {
		String inputLine;
		String notityXml = "";
		String resXml = "";
		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityXml += inputLine;
			}
			request.getReader().close();
		} catch (Exception e) {
			logger.debug(e.toString());
		}

		logger.debug("receive xml:" + notityXml);
		Map<?, ?> m = parseXmlToList2(notityXml);
		WxPayResult wpr = new WxPayResult();
		wpr.setAppid(m.get("appid").toString());
		wpr.setBankType(m.get("bank_type").toString());
		wpr.setCashFee(m.get("cash_fee").toString());
		wpr.setFeeType(m.get("fee_type").toString());
		wpr.setIsSubscribe(m.get("is_subscribe").toString());
		wpr.setMchId(m.get("mch_id").toString());
		wpr.setNonceStr(m.get("nonce_str").toString());
		wpr.setOpenid(m.get("openid").toString());
		wpr.setOutTradeNo(m.get("out_trade_no").toString());
		wpr.setResultCode(m.get("result_code").toString());
		wpr.setReturnCode(m.get("return_code").toString());
		wpr.setSign(m.get("sign").toString());
		wpr.setTimeEnd(m.get("time_end").toString());
		wpr.setTotalFee(m.get("total_fee").toString());
		wpr.setTradeType(m.get("trade_type").toString());
		wpr.setTransactionId(m.get("transaction_id").toString());
		String courseId = m.get("attach").toString();
		logger.debug("wpr.getOutTradeNo() : {}", wpr.getOutTradeNo());
		logger.debug("weixinLiveClassPayResult courseOd:" + courseId);
		logger.debug("weixinLiveClassPayResult openid:" + wpr.getOpenid());
		if ("SUCCESS".equals(wpr.getResultCode())) {
			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
					+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			courseDao.updateUserCourseByTradeNo(wpr.getOutTradeNo(), 1);
		} else {
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}
		return resXml;
	}

	private Map parseXmlToList2(String xml) {
		Map retMap = new HashMap();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			org.jdom.input.SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			org.jdom.Document doc = (org.jdom.Document) sb.build(source);
			org.jdom.Element root = doc.getRootElement();// 指向根节点
			List<org.jdom.Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (org.jdom.Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			logger.debug(e.toString());
		}
		return retMap;
	}

	private String generateQrcode(String codeurl) {

		File foldler = new File("/data/cglx/files" + "qrcode");

		if (!foldler.exists()) {
			foldler.mkdirs();
		}
		String f_name = UUID.randomUUID() + ".png";
		try {
			File f = new File("/data/cglx/files" + "qrcode", f_name);
			FileOutputStream fio = new FileOutputStream(f);
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			Map hints = new HashMap();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			// 设置字符集编码类型
			BitMatrix bitMatrix = null;
			bitMatrix = multiFormatWriter.encode(codeurl, BarcodeFormat.QR_CODE, 300, 300, hints);
			BufferedImage image = toBufferedImage(bitMatrix);
			// 输出二维码图片流
			ImageIO.write(image, "png", fio);
			return ("/data/cglx/files/qrcode/" + f_name);

		} catch (Exception e) {
			logger.error(e.toString());
			return "";
		}
	}

	private BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
}
