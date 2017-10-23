package com.ddcb.weixin.service.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ddcb.utils.WeixinCache;
import com.ddcb.weixin.service.IWeixinPushService;

import net.sf.json.JSONObject;

@Service("weixinPushService")
public class WeixinPushServiceImpl implements IWeixinPushService {

	private static final Logger logger = LoggerFactory.getLogger(WeixinPushServiceImpl.class);

	@Override
	public boolean pushTextMessage(String openId, String message) {
		boolean status = false;
		String json = "{\"touser\": \"" + openId + "\",\"msgtype\": \"text\", \"text\": {\"content\": \"" + message
				+ "\"}}";
		String action = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
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

	@Override
	public boolean pushImageMessage(String openId) {
		boolean status = false;
		try {
			String result = null;
			File file = new File("/data/cglx/course_invite_card/" + openId + ".jpg");
			URL url = new URL("https://api.weixin.qq.com/cgi-bin/media/upload?access_token="
					+ WeixinCache.getAccessToken() + "&type=image");
			long filelength = file.length();
			String fileName = file.getName();
			String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
			String type = "image/jpeg";
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			String BOUNDARY = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			// 请求正文信息
			// 第一部分：

			StringBuilder sb = new StringBuilder();

			// 这块是post提交type的值也就是文件对应的mime类型值
			sb.append("--"); // 必须多两道线
								// 这里说明下，这两个横杠是http协议要求的，用来分隔提交的参数用的，不懂的可以看看http
								// 协议头
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"type\" \r\n\r\n"); // 这里是参数名，参数名和值之间要用两次
			sb.append(type + "\r\n"); // 参数的值

			/**
			 * 这里重点说明下，上面两个参数完全可以卸载url地址后面 就想我们平时url地址传参一样，
			 * http://api.weixin.qq.com/cgi-bin/material/add_material?
			 * access_token=##ACCESS_TOKEN##&type=""&description={}
			 * 这样，如果写成这样，上面的 那两个参数的代码就不用写了，不过media参数能否这样提交我没有试，感兴趣的可以试试
			 */

			sb.append("--"); // 必须多两道线
			sb.append(BOUNDARY);
			sb.append("\r\n");
			// 这里是media参数相关的信息，这里是否能分开下我没有试，感兴趣的可以试试
			sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + fileName + "\";filelength=\""
					+ filelength + "\" \r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			System.out.println(sb.toString());
			byte[] head = sb.toString().getBytes("utf-8");
			// 获得输出流
			OutputStream out = new DataOutputStream(con.getOutputStream());
			// 输出表头
			out.write(head);
			// 文件正文部分
			// 把文件已流文件的方式 推入到url中
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			// 结尾部分，这里结尾表示整体的参数的结尾，结尾要用"--"作为结束，这些都是http协议的规定
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);
			out.flush();
			out.close();
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = null;
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
			// 使用JSON-lib解析返回结果
			JSONObject jsonObject = JSONObject.fromObject(result);
			if (jsonObject.has("media_id")) {
				System.out.println("media_id:" + jsonObject.getString("media_id"));
			} else {
				System.out.println(jsonObject.toString());
			}
			System.out.println("json:" + jsonObject.toString());

			String json = "{\"touser\": \"" + openId + "\",\"msgtype\": \"image\", \"image\": {\"media_id\": \""
					+ jsonObject.getString("media_id") + "\"}}";
			String action = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
					+ WeixinCache.getAccessToken();
			logger.debug("pushImageMessage openId : {}", openId);
			try {
				status = connectWeiXinInterface(action, json);
			} catch (Exception e) {
				logger.error("weixin push failed, exception : {}", e.toString());
			}
		} catch (Exception e) {
			logger.debug(e.toString());
		}
		return status;
	}
}
