package com.ocfisher.task;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;  
import org.springframework.stereotype.Component;

import com.course.dao.ICourseDao;


@Component
public class RemiderTask {

	private static final Logger logger = LoggerFactory.getLogger(RemiderTask.class);

	@Autowired
	private ICourseDao courseDao;
	
	@Scheduled(cron="0 1 0 ? * *")
    public void taskCycle(){  
        logger.debug("Execute reminder task.");
        List<Map<String, Object>> retList = courseDao.getReminderCourse();
        logger.debug("reminder task count : {}", retList == null || retList.isEmpty()? 0 : retList.size());
        for(Map<String, Object> map : retList) {
        	sendSMSCode((String)map.get("title"), (String)map.get("user_id"));
        }
        logger.debug("finish reminder.");
    }
	
	private void sendSMSCode(String title, String phone) {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://106.ihuyi.com/webservice/sms.php?method=Submit");
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
		String content = new String("您好，您在DIY研习社报名的课程:"+title+"，即将开播，请及时关注，避免错过。详情，请登录DIY研习社进入“我的课程”查看。");
		NameValuePair[] data = { // 提交短信
				new NameValuePair("account", "cf_ckzsnow"),
				new NameValuePair("password", "52d39430ddb918ddc0f57d5fc6717b24"), // 密码可以使用明文密码或使用32位MD5加密
				new NameValuePair("mobile", phone), new NameValuePair("content", content), };
		method.setRequestBody(data);
		try {
			client.executeMethod(method);
			String SubmitResult = method.getResponseBodyAsString();
			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();
			String code = root.elementText("code");
			logger.debug("RemiderTask send status : {}", code);
		} catch (IOException e) {
			logger.error(e.toString());
		} catch (DocumentException e) {
			logger.error(e.toString());
		}
	}
	
}