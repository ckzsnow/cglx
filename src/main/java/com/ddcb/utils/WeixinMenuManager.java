package com.ddcb.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeixinMenuManager {

	private static final Logger logger = LoggerFactory
			.getLogger(WeixinMenuManager.class);

	public static void createMenu(String params, String accessToken) {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="
				+ accessToken;
		sendRequest(params, url);
	}

	public static void deleteMenu(String accessToken) {
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="
				+ accessToken;
		sendRequest("", url);
	}

	private static void sendRequest(String params, String url) {
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
			OutputStreamWriter out = new OutputStreamWriter(
					conn.getOutputStream());
			out.write(params);
			out.flush();
			out.close();
			InputStream in = conn.getInputStream();
			BufferedReader read = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String valueString = null;
			while ((valueString = read.readLine()) != null) {
				bufferRes.append(valueString);
			}
			logger.debug(bufferRes.toString());
			System.out.println(bufferRes.toString());
			in.close();
			if (conn != null) {
				conn.disconnect();
			}
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	public static void main(String[] args) {
		//String s = "{\"button\":[{\"name\":\"报销\",\"sub_button\":[{\"type\":\"view\",\"name\":\"上传发票\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx309df15b6ddc5371&redirect_uri=http%3a%2f%2f121.40.63.208%2fgetOpenIdRedirect%3fview%3dupload_bill.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"我的报销单\",\"url\":\"http://121.40.63.208/my_bills\"},{\"type\":\"view\",\"name\":\"收件箱\",\"url\":\"http://121.40.63.208/my_inbox\"}]},{\"name\":\"看报表\",\"sub_button\":[{\"type\":\"view\",\"name\":\"资产管理分析\",\"url\":\"http://121.40.63.208/assets_analyse\"},{\"type\":\"view\",\"name\":\"产品销量分析\",\"url\":\"http://121.40.63.208/sales_analyse\"},{\"type\":\"view\",\"name\":\"费用分析\",\"url\":\"http://121.40.63.208/expenses_analyse\"},{\"type\":\"view\",\"name\":\"更多报表\",\"url\":\"http://121.40.63.208/more_reports\"}]},{\"name\":\"设置\",\"sub_button\":[{\"type\":\"view\",\"name\":\"账号设置\",\"url\":\"http://121.40.63.208/settings\"},{\"type\":\"view\",\"name\":\"绑定公司\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx309df15b6ddc5371&redirect_uri=http%3a%2f%2f121.40.63.208%2fgetOpenIdRedirect%3fview%3dbind_company.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"帮助和教程\",\"url\":\"http://121.40.63.208/help\"}]}]}";
		// String s =
		// "{\"button\":[{\"name\":\"报销\",\"sub_button\":[{\"type\":\"view\",\"name\":\"上传发票\",\"url\":\"http://121.40.63.208/upload_bill\"},{\"type\":\"view\",\"name\":\"我的报销单\",\"url\":\"http://121.40.63.208/my_bills\"},{\"type\":\"view\",\"name\":\"收件箱\",\"url\":\"http://121.40.63.208/my_inbox\"}]},{\"name\":\"看报表\",\"sub_button\":[{\"type\":\"view\",\"name\":\"资产管理分析\",\"url\":\"http://121.40.63.208/assets_analyse\"},{\"type\":\"view\",\"name\":\"产品销量分析\",\"url\":\"http://121.40.63.208/sales_analyse\"},{\"type\":\"view\",\"name\":\"费用分析\",\"url\":\"http://121.40.63.208/expenses_analyse\"},{\"type\":\"view\",\"name\":\"更多报表\",\"url\":\"http://121.40.63.208/more_reports\"}]},{\"name\":\"设置\",\"sub_button\":[{\"type\":\"view\",\"name\":\"账号设置\",\"url\":\"http://121.40.63.208/settings\"},{\"type\":\"view\",\"name\":\"绑定公司\",\"url\":\"http://121.40.63.208/bind_company\"},{\"type\":\"view\",\"name\":\"帮助和教程\",\"url\":\"http://121.40.63.208/help\"}]}]}";
		//String s = "{\"button\":[{\"name\":\"报销\",\"sub_button\":[{\"type\":\"view\",\"name\":\"上传发票\",\"url\":\"\"},{\"type\":\"view\",\"name\":\"我的报销单\",\"url\":\"\"},{\"type\":\"view\",\"name\":\"收件箱\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx309df15b6ddc5371&redirect_uri=http%3a%2f%2f121.40.63.208%2fgetOpenIdRedirect%3fview%3dmy_inbox.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"}]},{\"name\":\"看报表\",\"sub_button\":[{\"type\":\"view\",\"name\":\"资产管理分析\",\"url\":\"http://121.40.63.208/views/weixinviews/weixin_empty.html\"},{\"type\":\"view\",\"name\":\"产品销量分析\",\"url\":\"http://121.40.63.208/views/weixinviews/weixin_empty.html\"},{\"type\":\"view\",\"name\":\"费用分析\",\"url\":\"http://121.40.63.208/views/weixinviews/weixin_empty.html\"},{\"type\":\"view\",\"name\":\"更多报表\",\"url\":\"http://121.40.63.208/views/weixinviews/weixin_empty.html\"}]},{\"name\":\"设置\",\"sub_button\":[{\"type\":\"view\",\"name\":\"账号设置\",\"url\":\"http://121.40.63.208/views/weixinviews/weixin_empty.html\"},{\"type\":\"view\",\"name\":\"绑定公司\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx309df15b6ddc5371&redirect_uri=http%3a%2f%2f121.40.63.208%2fgetOpenIdRedirect%3fview%3dbind_company.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"帮助和教程\",\"url\":\"http://121.40.63.208/views/weixinviews/weixin_empty.html\"}]}]}";
		
		//String single = "{\"button\":[{\"type\":\"view\",\"name\":\"发票\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dexpense_account.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"name\":\"资产分析\",\"sub_button\":[{\"type\":\"view\",\"name\":\"资产负债表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dsheet_balance.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"利润表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dsheet_income.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"现金流量表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dsheet_cash.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"薪酬及税费表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dsheet_salary.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"}]},{\"type\":\"view\",\"name\":\"设置\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dsettings.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"}]}";

		//String bbzoldMenu = "{\"button\":[{\"name\":\"发票\",\"sub_button\":[{\"type\":\"view\",\"name\":\"收件箱\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dinbox.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"发票上传\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dupload.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"发票识别\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3ddemo.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"发票审核\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3daudit.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"服务与帮助\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dhelp.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"}]},{\"name\":\"资产分析\",\"sub_button\":[{\"type\":\"view\",\"name\":\"资产负债表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dsheet_balance.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"利润表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dsheet_income.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"现金流量表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dsheet_cash.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"薪酬及税费表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dsheet_salary.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"}]},{\"name\":\"设置\",\"sub_button\":[{\"type\":\"view\",\"name\":\"绑定公司\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dbindcompany.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"解除绑定\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dunbind.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"帐号设置\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3daccounterset.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"服务与帮助\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dhelp2.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"}]}]}";
		//String bbznewMenu = "{\"button\":[{\"name\":\"发票\",\"sub_button\":[{\"type\":\"view\",\"name\":\"审核发票\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dinvoice_audit.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"个人发票上传\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dperson_upload_invoice.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"公司发票上传\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dcompany_upload_invoice.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"已审核发票\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dfinish_audit_invoice.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"待审核发票\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dwait_audit_invoice.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"}]},{\"name\":\"资产分析\",\"sub_button\":[{\"type\":\"view\",\"name\":\"资产负债表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dasset_sheet.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"利润表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dincome_sheet.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"现金流量表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dcash_sheet.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"薪酬及税费表\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3dsalary_sheet.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"}]},{\"type\":\"view\",\"name\":\"账号设置\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx333ea15ba860f932&redirect_uri=http%3a%2f%2fwww.bangbangzhang.com%2fgetOpenIdRedirect%3fview%3daccount_settings.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"}]}";
		//String ddcbMenu = "{\"button\":[{\"type\":\"view\",\"name\":\"公开课\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx55d4da6e29cc6c83&redirect_uri=http%3a%2f%2fwww.diandou.me%2fgetOpenIdRedirect%3fview%3dopen_class.html&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"近期课程\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx55d4da6e29cc6c83&redirect_uri=http%3a%2f%2fwww.diandou.me%2fgetOpenIdRedirect%3fview%3drecent_class.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"name\":\"招聘合作\",\"sub_button\":[{\"type\":\"view\",\"name\":\"投稿合作\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx55d4da6e29cc6c83&redirect_uri=http%3a%2f%2fwww.diandou.me%2fgetOpenIdRedirect%3fview%3dcontributions.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"},{\"type\":\"view\",\"name\":\"点豆招人\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx55d4da6e29cc6c83&redirect_uri=http%3a%2f%2fwww.diandou.me%2fgetOpenIdRedirect%3fview%3demployment.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"}, {\"type\":\"view\",\"name\":\"导师招募\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx55d4da6e29cc6c83&redirect_uri=http%3a%2f%2fwww.diandou.me%2fgetOpenIdRedirect%3fview%3dteacher.html&response_type=code&scope=snsapi_base&state=1#wechat_redirect\"}]}]}";
		//String cglxMenu = "{\"button\":[{\"type\":\"view\",\"name\":\"留学头条\",\"url\":\"http://www.udiyclub.com/view/homepage.html\"},{\"name\":\"在线课程\",\"sub_button\":[{\"type\":\"view\",\"name\":\"课程报名\",\"url\":\"http://www.udiyclub.com/courses/views/courses.html\"},{\"type\":\"view\",\"name\":\"导师合作\",\"url\":\"http://www.udiyclub.com/view/apply_report.html\"}]},{\"name\":\"UDIY干货\",\"sub_button\":[{\"type\":\"view\",\"name\":\"精品课程\",\"url\":\"http://mp.weixin.qq.com/mp/homepage?__biz=MzI2Nzc0OTIxMg==&hid=9&sn=29ed194a12850e85806a596cc4b42a28&scene=18#wechat_redirect\"},{\"type\":\"view\",\"name\":\"文书干货\",\"url\":\"http://mp.weixin.qq.com/mp/homepage?__biz=MzI2Nzc0OTIxMg==&hid=2&sn=84f42e52b318cd64bd70d9b7a623432d&scene=18#wechat_redirect\"},{\"type\":\"view\",\"name\":\"语言类干货\",\"url\":\"http://mp.weixin.qq.com/mp/homepage?__biz=MzI2Nzc0OTIxMg==&hid=3&sn=57e768783c65b4742d7aa88c05dbb9c3&scene=18#wechat_redirect\"},{\"type\":\"view\",\"name\":\"大咖分享\",\"url\":\"http://mp.weixin.qq.com/mp/homepage?__biz=MzI2Nzc0OTIxMg==&hid=5&sn=3d89794ccdf023759e26cbb23e5a6e7a&scene=18#wechat_redirect\"},{\"type\":\"view\",\"name\":\"其他干货\",\"url\":\"http://mp.weixin.qq.com/mp/homepage?__biz=MzI2Nzc0OTIxMg==&hid=6&sn=685a54a45218fb06e8cffde1634c8a0f&scene=18#wechat_redirect\"}]}]}";
		
		String cglxMenu = "{\"button\":[{\"type\":\"click\",\"name\":\"邀请卡\",\"key\":\"invite_card\"},{\"name\":\"在线课程\",\"sub_button\":[{\"type\":\"view\",\"name\":\"课程报名\",\"url\":\"http://www.udiyclub.com/courses/views/courses.html\"},{\"type\":\"view\",\"name\":\"导师合作\",\"url\":\"http://www.udiyclub.com/view/apply_report.html\"}]},{\"name\":\"留学服务\",\"sub_button\":[{\"type\":\"view\",\"name\":\"免费申请\",\"url\":\"http://www.udiyclub.com/view/mentor.html\"},{\"type\":\"view\",\"name\":\"关于我们\",\"url\":\"http://www.udiyclub.com/view/about.html\"}]}]}";
		
		String accessToken = "5_Ko5I8J1tZXiUWciyBKdNsy-DAlilOfFNbhEVPpXaxGqmiohl3EZCnIUHKo8n4wjrcZcoFeBchMccZ3Kvztd9ZM2_V4zHrcysnxJ1r0gY7CenN4uTtVQopEtrh95OL2s2PJO1D39aabG5IYgrYIPaAJAFPW";
		createMenu(cglxMenu, accessToken);
		//deleteMenu(accessToken);
	}

}
