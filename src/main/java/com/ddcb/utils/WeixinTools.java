package com.ddcb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocfisher.dao.ICglxDao;
import com.ocfisher.dao.IUserOpenIdUnionIdDao;
import com.ocfisher.dao.impl.CglxDaoImpl;
import com.ocfisher.dao.impl.UserOpenIdUnionIdDaoImpl;

public class WeixinTools {

	private static final Logger logger = LoggerFactory
			.getLogger(WeixinTools.class);

	public static Map<String, String> getSign(String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = createNonceStr();
		String timestamp = createTimestamp();
		String string1;
		String signature = "";
		String jsapiTicket = WeixinCache.getJsTicket();
		String[] paramArr = new String[] {"jsapi_ticket=" + jsapiTicket,
                "timestamp=" + timestamp, "noncestr=" + nonce_str, "url=" + url};
		Arrays.sort(paramArr);
		string1 = paramArr[0].concat("&"+paramArr[1]).concat("&"+paramArr[2])
                .concat("&"+paramArr[3]);
		logger.debug("sign url : {}", string1);

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.warn(e.toString());
		} catch (UnsupportedEncodingException e) {
			logger.warn(e.toString());
		}
		ret.put("url", url);
		ret.put("jsapi_ticket", WeixinCache.getJsTicket());
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		logger.debug("sign result : {}", ret);
		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String createNonceStr() {
		return UUID.randomUUID().toString();
	}

	private static String createTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

	@SuppressWarnings("unchecked")
	public static Map<Object, Object> httpGet(String url) {
		logger.debug("sending http get request, url : {}", url);
		Map<Object, Object> ret = null;
		StringBuffer bufferRes = new StringBuffer();
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			conn.setConnectTimeout(25000);
			conn.setReadTimeout(25000);
			HttpURLConnection.setFollowRedirects(true);
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0");
			conn.setRequestProperty("Referer", "https://api.weixin.qq.com/");
			conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String valueString = null;
			while ((valueString = read.readLine()) != null) {
				bufferRes.append(valueString);
			}
			ObjectMapper mapper = new ObjectMapper();
			ret = mapper.readValue(bufferRes.toString(), Map.class);
			in.close();
			if (conn != null) {
				conn.disconnect();
			}
		} catch (MalformedURLException e) {
			logger.warn(e.toString());
		} catch (IOException e) {
			logger.warn(e.toString());
		}
		logger.debug("finish in sending http get request, response : {}",
				bufferRes.toString());
		return ret;
	}
 
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getUserWeixinInfo(String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		url = url.replace("APPID", WeixinConstEnum.COMPANY_APP_ID.toString())
				.replace("SECRET", WeixinConstEnum.COMPANY_APP_SECRET.toString())
				.replace("CODE", code);
		logger.info("GetOpenId URL : {}", url);
		Map<Object, Object> map = httpGet(url);
		logger.debug("GetOpenId code return info:{}", map.toString());
		String openId = (String)map.get("openid");
		String accessToken = (String)map.get("access_token"); 
		logger.debug("GetOpenId openId : {}", openId);
		ObjectMapper om = new ObjectMapper();
		Map<String, Object> retMap = new HashMap<>();
		try {
			retMap = om.readValue(getUserInfoByOpenId(accessToken, openId), Map.class);
		} catch (JsonParseException e) {
			logger.error(e.toString());
		} catch (JsonMappingException e) {
			logger.error(e.toString());
		} catch (IOException e) {
			logger.error(e.toString());
		}
		logger.debug("GetOpenId Map:{}", retMap.toString());
		return retMap;
	}
	
	private static String getUserInfoByOpenId(String accessToken, String openId){
		String ret = "";
		URL url;
		try {
			url = new URL("https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			ret = new String(jsonBytes, "UTF-8");
			logger.debug("push result : {}", ret);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return ret;	
	}
	
	public static void main(String[] args) {
		String ret = "";
		URL url;
		try {
			url = new URL("https://api.weixin.qq.com/sns/userinfo?access_token=&openid=&lang=zh_CN");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			ret = new String(jsonBytes, "UTF-8");
			logger.debug("push result : {}", ret);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
	}
}
