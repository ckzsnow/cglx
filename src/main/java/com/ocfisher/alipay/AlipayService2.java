package com.ocfisher.alipay;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;

public class AlipayService2 {
	
	private static final String APPID = "2017021705722714";
	
	private static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCLLP97WNPGZDC/AuZDvbro3YChwJdsnbfu91kVfsGN3bZDkr0B8/3Td/KKDkt5gVWMfktnRmeLWY6+eA6gwqI9Ux8bf6RVMXpElcMqCDZQgw+AyY37g9jfH4YJpGCeKCHiMjzWf9dAAewom7d/DewifNIL15Nmxpah2tA+8LLsChQRzpIfs1xh4YwLpA2YEddQLQEgLY6WAP9lhagB684mm4FpcuI3sINejxJhyqC+ycZvwNa0Mx4cIa5VFNcwp54N4Wkz2Ks5LRSGXad0Fvcsk+ZHFRvqPw1Roo+9KzoweOmG33eBgDysuBYQK0alWK06Qlt0pJjIq2VNKpyUuNPVAgMBAAECggEAf8AtPw7ZTclXyW2i6dl1gR0ZXuCG3mejl5GqTwXMKymxjTNblThe7Ma0di8K7pxeZ5jiJAlHMG1jjshrPPuLqtjqu/YdZSoGfoAt5h4lpR+4klHwp0duQdkQ0zb0jRhvic7tc0Nj1Ngu61OMSu7/quSCdeZK4lM1n5pX/v44blqwtRmevqxnU7NfwXHZw5XCDFvnTN8OUZv/YV/5P6ZOiCB9i9uZ5EFzoY4lG7k0TIST9wKPbPSPCx1cqYc/n/lyRlqrl3Lf28v4m5+CKFZ+7QXlIx/aVuPptv4HQ1NNa1ru6bZ+zCJI9VWr8MrS3KBG8QaUzirTXv0E3dxZkBGcYQKBgQDV6cdGWnRNodJavBsptibqe0zrjc7E0SSPDrZpeB38bEdYMJp75FWNih6KcU/11GNpt/ByZLPCuFcT4OmL2+mJ1f/DZ/vWPKhQupTx7dMRivXz+1TdbpcKDu+34dWszu60p90SAmpEIvwV3UM60pVlUvZwjvrEjgp31Fo6dMBVKQKBgQCmjuZ7c9VHIrVFc/AvDONHjOszf2Oax+TZCU5p97R49+f8n2LOw/WfsHkH3qWsh5dYdcHndrzhFCbUhV7PonGmCPBb93RNU2IcPY81kvYArEplndCNtZgjFF2PrnP0cnrJn+6gmmOTBzgDqS+INedBNVCzlbUxeQacIu+7ZDHSzQKBgQDVzIhtWq7vgJNcOLH1aKdQqNfifqNwt5AQo6XRmEpwJlDQZhOhTD/t7WRE/qfqjdKmsT9TNi8sqU3vAlaqgKvUJd0xktZz88BOn42KmCbjW74jksnpX0T6up8BbGhCBbkzOquL+nn3i/fkandtbVgAI4FIUr90gHsZ9VYRIuuwsQKBgF7JFAFNeiJxbheIUzdzbiRZ3cAAzv5Wo7WfxjAn91ygODVbvy9L9YxbMYV4/2+f+lEtMpw2xmaQwUhQXhRrraF/lMnBmT4oJb1MiyNB7x2S7wPFO9ppbZsJC/WoofSK8bWsfnCHbANQKGBs98/cXS6O2M5adDMjXM+eUi+iZXhBAoGAD0yHhJjW5PsfxXHJ2vbo/UnN1kH0eixkpmV8rjiG9PPdVR/PmrtBXAPvuDSvpffZSZEkyuUynmmuLLvnYRE2vYwBkkTXkv3agPu5lwnN/0Chtt61UCFQJw//wUpywTXWaredyuKKrIK9otkHMUbf3sR2wD6jfmaJkmEffkP288s=";
	
	private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtjVvr0Zf2GTxahg6+wu5bv8Zyvo9ZGhkNi7ZwLM6bo8w6WhFZ6bLLdXBqG67mqMIbc2OY3tWLVD2GimZX9D0iif8UjC616Md2rcsgUqVDnL/CCtfvbJIHgO35JEmLf7Gj1+ClzxMJIo0Wel5gf5qS95kXxFC+M5jqHtpyn+bELOPl7NxMUCylZyVdpQM15zZsoDDl0219nySYyZXtat75NQuCLRzVCyJFTK+LE41zm0+XsJVil+dnNWe/feRpN5R+IwdLM+6a8s6WIxTx8g+g4s+0+9fc4R8q1bfPxwHj2v3j9grnyqPmL7c3GHW2VKeCtXTwie/vziwLfVxl0eT1wIDAQAB";
	
	private static AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APPID, PRIVATE_KEY, "json", "utf-8", PUBLIC_KEY, "RSA2");
	
	public static String doPost(Double amount, String orderNo) throws ServletException, IOException{
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
		alipayRequest.putOtherTextParam("", "");
		alipayRequest.setReturnUrl("http://localhost:8080/alipayFinished");
		alipayRequest.setNotifyUrl("http://localhost:8080/alipayFinished");//在公共参数中设置回跳和通知地址
		alipayRequest.setBizContent("{" +
		"    \"out_trade_no\":\"111111111\"," +
		"    \"total_amount\":\"0.01\"," +
		"    \"subject\":\"Test\"," +
		"    \"seller_id\":\"lb0233@yahoo.cn\"," +
		"    \"product_code\":\"\"" +
		"  }");//填充业务参数
		String form = "";
		try {
			form = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //调用SDK生成表单
		System.out.println(form);
		return form;
	}
}
