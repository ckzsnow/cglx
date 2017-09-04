package com.ocfisher.alipay;



import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/* *
 *功能：即时到账交易接口接入页
 *版本：3.3
 *日期：2012-08-14
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 *************************注意*****************
 *如果您在接口集成过程中遇到问题，可以按照下面的途径来解决
 *1、商户服务中心（https://b.alipay.com/support/helperApply.htm?action=consultationApply），提交申请集成协助，我们会有专业的技术工程师主动联系您协助解决
 *2、商户帮助中心（http://help.alipay.com/support/232511-16307/0-16307.htm?sh=Y&info_type=9）
 *3、支付宝论坛（http://club.alipay.com/read-htm-tid-8681712.html）
 *如果不想使用扩展功能请把扩展功能参数赋空值。
 **********************************************
 */

import java.util.HashMap ;
import java.util.Map;
import java.util.UUID;
public class AlipayService {
	
	public String buildAlipayRequest(Double amount, String orderNo) throws UnsupportedEncodingException{
		
		//必填，不能修改
		//服务器异步通知页面路径

		//商户订单号
		String out_trade_no = new String(orderNo);
		//商户网站订单系统中唯一订单号，必填

		//订单名称
		String subject = "UDIY";
		//必填

		//付款金额
		String total_fee = String.valueOf(amount);
		//必填

		//订单描述

		String body = "UDIY";
		//商品展示地址
		String show_url = new String("www.baidu.com");
		//需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html

		//防钓鱼时间戳
		//String anti_phishing_key = "aaa";
		//若要使用请调用类文件submit中的query_timestamp函数

		//客户端的IP地址
		//String exter_invoke_ip = "aaa";
		//非局域网的外网IP地址，如：221.0.0.1
		
		
		//////////////////////////////////////////////////////////////////////////////////
		
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_email", AlipayConfig.seller_email);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        /*sParaTemp.put("sign_type", AlipayConfig.sign_type);*/
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", AlipayConfig.notify_url);
		sParaTemp.put("return_url", AlipayConfig.return_url);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		sParaTemp.put("show_url", show_url);
		/*sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);*/
		
		//建立请求
		String sHtmlText = "";
		try {
			sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"post","确认");
		} catch (Exception e) {
			e.getCause();
			System.out.println(e.getCause());
			
		}
		System.out.println(sHtmlText);

		return sHtmlText;

	}
	
	public String buildPayoffRequest() {

        // 服务器异步通知页面路径
        String notify_url = "http://www.udiyclub.com/cashbackPayFinished";
        // 需http://格式的完整路径，不能加?id=123这类自定义参数

        //付款账号
        //String email = "lb0233@yahoo.cn";
        //必填

        //付款账户名
        //String account_name = "李奔";
        //必填，个人支付宝账号是真实姓名---公司支付宝账号是公司名称

        //批次号
        //必填，格式：当天日期[8位]+序列号[3至16位]，如：201008010000001

        //付款总金额
        //String batch_fee = "";
        //必填，即参数detail_data的值中所有金额的总和

        //付款笔数
        //String batch_num = "";
        //必填，即参数detail_data的值中，“|”字符出现的数量加1，最大支持1000笔（即“|”字符出现的数量999个）

        //付款详细数据
        //必填，格式：流水号1^收款方帐号1^真实姓名^付款金额1^备注说明1|流水号2^收款方帐号2^真实姓名^付款金额2^备注说明2....
        
        String serial_num = UUID.randomUUID().toString();
        
        String detail_data = serial_num + "^lb0233@yahoo.com.cn^李奔^0.0.1^hello";
      
        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", "batch_trans_notify");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("notify_url", notify_url);
        sParaTemp.put("email", AlipayConfig.seller_email);
        sParaTemp.put("account_name", AlipayConfig.account_name);
        //付款当天日期//必填，格式：年[4位]月[2位]日[2位]，如：20100801
        sParaTemp.put("pay_date", AlipayConfig.pay_date);
        sParaTemp.put("batch_no", AlipayConfig.batch_no);
        sParaTemp.put("batch_fee", AlipayConfig.batch_fee);
        sParaTemp.put("batch_num", AlipayConfig.batch_num);
        sParaTemp.put("detail_data", detail_data);

        /**建立请求*/
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"post","确认");
        System.out.println(sHtmlText);
        return sHtmlText;
	}
}
