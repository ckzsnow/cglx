package com.document.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.xml.sax.InputSource;

import com.course.controller.CourseController;
import com.ddcb.utils.WeixinPayUtils;
import com.ddcb.utils.WxPayDto;
import com.ddcb.utils.WxPayResult;
import com.document.dao.IDocDao;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

@Controller
public class DocController {

	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
	
	@Autowired
	private IDocDao docDao;
	
	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;
	
	@RequestMapping("/document/addDocument")
	@ResponseBody
	public Map<String, Object> addDocument(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, Object> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		
		String realPath = "/data/cglx/files/docfiles";
		String fileName = "";
		int index = 0;
		if(multipartRequest != null) {
			Iterator<String> ite = multipartRequest.getFileNames();
			while(ite.hasNext()) {
				MultipartFile file = multipartRequest.getFile(ite.next());
				fileName = getGernarateFileName(file);
				try {
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
				} catch(Exception e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
				switch(index) {
				case 0:
					paramsMap.put("snapshot", new String[] {fileName});
					break;
				case 1:
					paramsMap.put("document", new String[] {fileName});
					break;
				}
				index ++;
			}
		}
		
		if(docDao.addDocument(paramsMap)) {
			retMap.put("msg", "添加成功！");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "添加失败，请重试！");
			retMap.put("error", "1");
		}
		return retMap;
	}
	
	@RequestMapping("/document/updateDocumentById")
	@ResponseBody
	public Map<String, Object> updateDocumentById(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, Object> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
		
		String indexStr = request.getParameter("index");
		int index = -1;
		try{
			index = Integer.valueOf(indexStr);
		} catch(Exception e) {
			logger.error(e.toString());
		}
		
		int count = 0;
		if (multipartRequest != null) {
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
				fileName = getGernarateFileName(multifile);
				try {
					FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, fileName));
				} catch (IOException e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
				if(index != 2) {
					count = index;
				}
				switch(count) {
				case 0: paramsMap.put("snapshot", new String[] { fileName });
				break;
				case 1: paramsMap.put("document", new String[] { fileName });
				break;
				}
				count ++;
			}
		}
		
		if(docDao.updateDocument(paramsMap)) {
			retMap.put("msg", "更新成功！");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "更新失败，请重试！");
			retMap.put("error", "1");
		}
		return retMap;
		
	}
	
	@RequestMapping("/document/getAllDocument")
	@ResponseBody
	public List<Map<String, Object>> getAllDocument(HttpServletRequest request) {
		List<Map<String, Object>> retList = new ArrayList<>();
		retList = docDao.getAllDocument();
		return retList;
	}
	
	@RequestMapping("/document/getDocumentList")
	@ResponseBody
	public List<Map<String, Object>> getDocumentList(HttpServletRequest request) {
		List<Map<String, Object>> retList = new ArrayList<>();
		
		return retList;
	}
	
	@RequestMapping("/document/getDocDetailById")
	@ResponseBody
	public Map<String, Object> getDocDetailById(HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<>();
		String id = request.getParameter("id");
		retMap = docDao.getDocDetailById(id);
		return retMap;
	}
	
	@RequestMapping("/document/deleteDocumentById")
	@ResponseBody
	public Map<String, Object> deleteDocumentById(HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<>();
		String id = request.getParameter("id");
		if(docDao.deleteDocumentById(id)) {
			retMap.put("msg", "删除成功！");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "删除失败，请重试！");
			retMap.put("error", "1");
		}
		return retMap;
	}
	
	@RequestMapping("/document/getDocByIndexAndKey")
	@ResponseBody
	public List<Map<String, Object>> getDocByIndexAndKey(HttpServletRequest request) {
		int index = 1;
		int length = 10;
		String key = "";
		List<Map<String, Object>> retList = null;
		try {
			index = Integer.valueOf(request.getParameter("index"));
			key = request.getParameter("key");
			retList = docDao.getDocByIndexAndKey((index-1)*length, length, key);
		} catch (Exception e) {
			logger.error("getDocByIndexAndKey error : {}", e.toString());
		}
		return retList;
	}
	
	@RequestMapping("/document/getDocTotalPageByKey")
	@ResponseBody
	public int getDocTotalPageByKey(HttpServletRequest request) {
		int length = 10;
		int total = 0;
		int totalPage = 0;
		String key = "";
		try {
			length = Integer.valueOf(request.getParameter("length"));
			key = request.getParameter("key");
			total = docDao.getAllDocumentByKey(key).size();
			if(total != 0) {
				if((total%length) == 0) {
					totalPage = (total/length);
				} else {
					totalPage = (total/length) + 1;
				}
			}
		} catch(Exception e) {
			e.toString();
		}
		return totalPage;
	}
	
	@RequestMapping("/document/getUserDocByOpenIdAndDocId")
	@ResponseBody
	public Map<String, Object> getUserDocByOpenIdAndDocId(HttpServletRequest request) {
		String user_id = request.getParameter("user_id");
		String doc_id = request.getParameter("doc_id");
		Map<String, Object> userDocMap = docDao.getUserDocByUserIdAndDocId(user_id, doc_id);
		return userDocMap;
	}
	
	@RequestMapping("/document/getPayStatusByOrderIdAndDocId")
	@ResponseBody
	public Map<String, Object> getPayStatusByOrderIdAndDocId(HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<>();
		String doc_id = request.getParameter("doc_id");
		String orderId = request.getParameter("orderId");
		int pay_status = docDao.getPayStatusByOrderIdAndCourseId(doc_id, orderId);
		retMap.put("pay_status", pay_status);
		return retMap;
	}
	
	@RequestMapping("/document/downloadDocument")
	public ResponseEntity<byte[]> downloadDocument(HttpServletRequest request) {
		String doc_id = request.getParameter("doc_id");
		Map<String, Object> docMap = docDao.getDocDetailById(doc_id);
		String realPath = "/data/cglx/files/docfiles/" + docMap.get("document");
		ResponseEntity<byte[]> re = null;
		File file = new File(realPath);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentDispositionFormData("attachment", new String(realPath.getBytes("UTF-8"), "iso-8859-1"));
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			re = new ResponseEntity<byte[]>(
					FileUtils.readFileToByteArray(file), headers,
					HttpStatus.CREATED);
		} catch (IOException e) {
			logger.debug("download exception : {}", e.toString());
		}
		return re;
	}
	
	@RequestMapping("/document/weixinDocPay")
	@ResponseBody
	public Map<String, String> weixinDocPay(HttpSession httpSession, HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String userId = (String) httpSession.getAttribute("user_id");
		logger.debug("DocController weixinDocPay user_id : {}", userId);
		String doc_id = request.getParameter("doc_id");
		Map<String, Object> docMap = docDao.getDocDetailById(doc_id);
		double fee = Double.valueOf(String.valueOf(docMap.get("price")));
		logger.debug("DocController weixinDocPay doc_id : {}", doc_id);
		if (userId == null || userId.isEmpty()) {
			retMap.put("error_msg", "no user id");
			return retMap;
		}
		WeixinPayUtils.setNotifyurl("http://www.udiyclub.com/document/weixinDocPayResult");
		logger.debug("DocController weixinDocPay fee : {}", fee);
		WxPayDto tpWxPay = new WxPayDto();
		tpWxPay.setOpenId("oSTV_t9z_fYa7AQVYO0y5-OMFavQ");
		tpWxPay.setBody("UDIY研习社");
		tpWxPay.setOrderId(WeixinPayUtils.getNonceStr());
		tpWxPay.setSpbillCreateIp(request.getRemoteAddr());
		tpWxPay.setTotalFee(String.valueOf(fee));
		tpWxPay.setAttach(doc_id);
		String qrcode = WeixinPayUtils.getCodeurl(tpWxPay);
		if (qrcode == null || qrcode.isEmpty()) {
			retMap.put("error_msg", "微信服务器无法获取到支付支付二维码，请稍后重试！");
			return retMap;
		}
		logger.debug("DocController weixinDocPay params : {}, {}", userId, doc_id);
		if (docDao.addUserDoc(userId, doc_id, String.valueOf(fee), tpWxPay.getOrderId())) {
			retMap.put("error_msg", "generateQrcode success");
			retMap.put("qrcodePath", generateQrcode(qrcode));
			retMap.put("orderId", tpWxPay.getOrderId());
			return retMap;
		} else {
			retMap.put("error_msg", "写数据库错误，请稍后重试！");
			return retMap;
		}
	}
	
	@RequestMapping("/document/weixinDocPayResult")
	@ResponseBody
	public String weixinDocPayResult(HttpSession httpSession, HttpServletRequest request) {
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
			docDao.updateUserDocByTradeNo(wpr.getOutTradeNo(), 1);
		} else {
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}
		return resXml;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	
	private String getGernarateFileName(MultipartFile file) {
		String extendName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
		return UUID.randomUUID().toString() + (extendName == null ? ".unknown" : "." + extendName);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String generateQrcode(String codeurl) {
		File foldler = new File("/data/cglx/files", "qrcode");
		if (!foldler.exists()) {
			foldler.mkdirs();
		}
		String f_name = UUID.randomUUID() + ".png";
		try {
			File f = new File("/data/cglx/files/qrcode", f_name);
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
			return ("/cglx/files/qrcode/" + f_name);

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
