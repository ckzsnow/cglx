package com.ddcb.weixin.service.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ddcb.utils.WeixinCache;
import com.ddcb.weixin.service.IWeixinPushService;

@Service("weixinPushService")
public class WeixinPushServiceImpl implements IWeixinPushService {

	private static final Logger logger = LoggerFactory
			.getLogger(WeixinPushServiceImpl.class);

	@Override
	public boolean pushTextMessage(String openId, String message) {
		boolean status = false;
		String json = "{\"touser\": \"" + openId
				+ "\",\"msgtype\": \"text\", \"text\": {\"content\": \""
				+ message + "\"}}";
		String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
				+ WeixinCache.getAccessToken();
		logger.debug("pushTextMessage openId : {}", openId);
		try {
			status = connectWeiXinInterface(action, json);
		} catch (Exception e) {
			logger.error("weixin push failed, exception : {}", e.toString());
		}
		return status;
	}

	private boolean connectWeiXinInterface(String action, String json) {
		boolean status = false;
		URL url;
		try {
			url = new URL(action);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			http.connect();
			OutputStream os = http.getOutputStream();
			os.write(json.getBytes("UTF-8"));
			InputStream is = http.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			String result = new String(jsonBytes, "UTF-8");
			logger.debug("push result : {}", result);
			os.flush();
			os.close();
			status = true;
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return status;
	}
}
