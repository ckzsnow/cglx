<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ddcb.utils.WeixinTools"%>
<%
	WebApplicationContext wac = WebApplicationContextUtils
			.getRequiredWebApplicationContext(this.getServletContext());
	String courseId = (String) session.getAttribute("course_id");
	Map<String, String> result = new HashMap<>();
	result = WeixinTools.getSign(
			"http://www.udiyclub.com/courses/jsp?id=" + courseId);	
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="user-scalable=no,width=device-width,initial-scale=1.0">
<meta name="keywords"
	content="留学、美国留学、英国留学、澳洲留学、加拿大留学、韩国留学、日本留学、法国留学、德国留学、欧洲留学、新西兰留学、香港留学、新加坡留学、留学申请、留学文书、个人陈述、留学简历、留学推荐信、DIY留学、留学中介">
<meta name="description"
	content="DIY研习社是国内首家留学互助共享平台，致力于为所有留学申请者提供资源共享、交际交流的渠道和机会，社员和内容覆盖留学申请所有主流国家。">
<title>DIY研习社－中国留学生互助交流平台，让留学不孤单</title>
<link rel="icon" href="/images/favicon.ico" type="image/x-icon" />
<link href="/courses/css/courseSeriesDetail.min.css" rel="stylesheet">
<script src="/js/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="/css/login.css">
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?5be0eb674b2390f73d8ef4abb25cb281";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>

</head>
<body device="pc">
	<header class="header">
		<div class="transparent-mask"></div>
		<div class="contain">
			<div class="logo">
				<a href="/view/homepage.html"></a>
			</div>
			<nav class="PC-nav">
				<ul>
					<li class="news" data-id="news"><a href="/view/homepage.html">留学专栏</a>
					</li>
					<li class="news"><a href="/view/mentor.html">留学服务</a>
					</li>
					<li class="active"><a href="/courses/views/courses.html">在线课程</a></li>
					<li class="activity" data-id="activity"><a
						href="/view/success.html">成功案例</a></li>
					<li class="gift" data-id="gift"><a href="/view/about.html">关于我们</a>
					</li>
					<li class="search"><a href="/view/document.html">资料下载</a></li>
					<li class="search" data-id="search"><a href="/view/search.html">搜索</a>
					</li>
				</ul>
			</nav>
			<nav class="mobile-nav">
				<i class="top-menu-expand-icon"></i>
				<ul class="nav-list">
					<li class="news" data-id="news"><a href="/view/homepage.html">留学专栏</a>
					</li>
					<li class="fa active" data-id="fa"><a href="/courses/views/courses.html">在线课程</a></li>
				</ul>
				<ul class="expand-nav-list" id="mobile_not_login">
					<li class="user-info"><a href="javascript:void(0)"
						id="login-btn-m">登录</a></li>
					<li class="activity"><a href="/view/success.html">成功案例</a></li>
					<li class="search"><a href="/courses/views/courses.html">在线课程</a></li>
				</ul>
				<ul class="expand-nav-list" id="mobile_login" style="display: none;">
					<li class="activity"><a href="/view/personalInfo.html"
						class="myorder-btn">个人信息</a></li>
					<li class="search"><a href="/view/estimate.html">背景评估</a></li>
					<li class="search"><a href="javascript:userlogout();">退出</a></li>
				</ul>
			</nav>
			<div class="login-container">
				<div class="logout" id="logout"
					style="display: none; height: 140px;">
					<i class="triangle"></i> <a href="/view/personalInfo.html"
						class="myorder-btn">个人信息</a> <a href="/view/estimate.html">背景评估</a> <a
						href="javascript:userlogout();">退出</a>
				</div>
				<div class="login-false-wrapper">
					<i class="icon"></i> <a href="javascript:void(0)" id="login-btn">登录</a>
				</div>
				<div class="login-success-wrapper" style="display: none;">
					<div class="photo">
						<img id="headimgurl">
					</div>
					<div class="name" id="user_id"></div>
				</div>
			</div>
		</div>
	</header>

	<div class="cglx-login-mask pc-login" style="display: none;position: fixed;z-index: 99999;background:rgba(56,62,68,.9)">
            <div class="login-dialog dialog" id="login_view" style="height:330px">
                <div class="login-close close"></div>
                <div class="dialog-bg">
                    <div class="dialog-title" style="background:url(/images/logo.png) no-repeat center; width:320px; height:45px"></div>
                </div>
                <div class="login-fill-card">
                    <div class="login-card card" style="height:225px">
                        <div class="inputs">
                            <div class="login-phone-input">
                                <input id="login_phone" type="text" pattern="^\d{11}$" class="login-phone input long-input" placeholder="请输入手机号" maxlength="11" autocomplete="off">
                                <i class="phone-status-icon"></i>
                            </div>
                            <p class="button-height"></p>
                            <div class="login-phone-input">
                                <input id="login_pwd" type="password" class="login-phone-pwd input long-input" style="margin:0 auto;" placeholder="请输入密码" maxlength="11" autocomplete="off">
                            </div>
                            <p class="button-height"></p>
                            <p class="phone-tips" id="login_tip" style="display:none;"></p>
                            <div class="login-submit submit" id="login_submit">登录</div>
                            <p class="voice-code" style="text-align: center;">
                                <span style="text-decoration: none;">
                                    <a href="javascript:$('#login_view').hide();$('#register_view').show();">注册账号</a>
                                </span>
                                <span style="color:black;cursor: none;text-decoration: none;">|</span>
                                <span style="text-decoration: none;">
                                    <a href="javascript:$('#login_view').hide();$('#forget_view').show();">忘记密码</a>
                                </span>
                            </p>
                            <div class="voice-code" style="text-align: center;">
                            	<a class="weixinLogin" href="">
                            		<img class="loginLogo" alt="" src="/images/login1.png" style="width:24px;height:24px;border-radius:50%"></img>
                            	</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="login-dialog dialog" id="register_view" style="display:none;height:370px;">
                <div class="login-close close"></div>
                <div class="dialog-bg">
                    <div class="dialog-title" style="background:url(/images/logo.png) no-repeat center; width:320px; height:45px"></div>
                </div>
                <div class="login-fill-card">
                    <div class="login-card card" style="height:270px;margin-top: -112px;">
                        <div class="inputs">
                            <div class="login-phone-input">
                                <input id="reg_phone" type="text" pattern="^\d{11}$" class="login-phone input long-input" placeholder="仅支持中国大陆手机号" maxlength="11" autocomplete="off">
                            </div>
                            <p class="button-height"></p>
                            <div class="login-phone-input">
                                <input id="reg_pwd" type="password" class="login-phone-pwd input long-input" style="margin:0 auto;" placeholder="请输入密码" maxlength="11" autocomplete="off">
                            </div>
                            <p class="button-height"></p>
                            <div class="verify">
                                <input id="reg_verify" type="text" pattern="^\d{4}$" class="verify-code input short-input" placeholder="验证码" maxlength="4" validate="^\d{4}$" autocomplete="off">
                                <input id="reg_get_code" type="button" class="send-verify-code input-button short-button btn-disable" send="false" value="发送验证码">
                            </div>
                            <p class="button-height"></p>
                            <p class="phone-tips" id="reg_tip" style="display:none;"></p>
                            <div class="login-submit submit" id="reg_submit">注册</div>
                            <p class="voice-code" style="text-align: center;">
                                <span style="text-decoration: none;">
                                    <a href="javascript:$('#register_view').hide();$('#login_view').show();">登录账号</a>
                                </span>
                                <span style="color:black;cursor: none;text-decoration: none;">|</span>
                                <span style="text-decoration: none;">
                                    <a href="javascript:$('#register_view').hide();$('#forget_view').show();">忘记密码</a>
                                </span>
                            </p>
                            <div class="voice-code" style="text-align: center;">
                            	<a class="weixinLogin" href="">
                            		<img class="loginLogo" alt="" src="/images/login1.png" style="width:24px;height:24px;border-radius:50%"></img>
                            	</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="login-dialog dialog" id="forget_view" style="display:none;height:370px;">
                <div class="login-close close"></div>
                <div class="dialog-bg">
                    <div class="dialog-title" style="background:url(/images/logo.png) no-repeat center; width:320px; height:45px"></div>
                </div>
                <div class="login-fill-card">
                    <div class="login-card card" style="height:270px;margin-top: -112px;">
                        <div class="inputs">
                            <div class="login-phone-input">
                                <input id="forget_phone" type="text" pattern="^\d{11}$" class="login-phone input long-input" placeholder="仅支持中国大陆手机号" maxlength="11" autocomplete="off">
                            </div>
                            <p class="button-height"></p>
                            <div class="login-phone-input">
                                <input id="forget_pwd" type="password" class="login-phone-pwd input long-input" style="margin:0 auto;" placeholder="设定新密码" maxlength="11" autocomplete="off">
                            </div>
                            <p class="button-height"></p>
                            <div class="verify">
                                <input id="forget_verify" type="text" pattern="^\d{4}$" class="verify-code input short-input" placeholder="验证码" maxlength="4" validate="^\d{4}$" autocomplete="off">
                                <input id="forget_get_code" type="button" class="send-verify-code input-button short-button btn-disable" send="false" value="发送验证码">
                            </div>
                            <p class="button-height"></p>
                            <p class="phone-tips" id="forget_tip" style="display:none;"></p>
                            <div class="login-submit submit" id="forget_submit">提交</div>
                            <p class="voice-code" style="text-align: center;">
                                <span style="text-decoration: none;">
                                    <a href="javascript:$('#forget_view').hide();$('#login_view').show();">登录账号</a>
                                </span>
                                <span style="color:black;cursor: none;text-decoration: none;">|</span>
                                <span style="text-decoration: none;">
                                    <a href="javascript:$('#forget_view').hide();$('#register_view').show();">注册账号</a>
                                </span>
                            </p>
                            <div class="voice-code" style="text-align: center;">
                            	<a class="weixinLogin" href="">
                            		<img class="loginLogo" alt="" src="/images/login1.png" style="width:24px;height:24px;border-radius:50%"></img>
                            	</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="cglx-login-mask mobile-login" style="display: none;position: fixed;top: 0;bottom: 0;left: 0;right: 0;z-index: 99999;background: rgba(0,0,0,.8);">
            <div class="mobile-dialog login-regist" id="mobile_login_view">
                <div class="login-close close"></div>
                <div class="login-regist-card card" data-show="true">
                    <div class="logo"></div>
                    <div class="login-regist-phone-input">
                        <input id="mobile_login_phone" type="tel" pattern="^d{11}$" class="login-phone input long-input" placeholder="手机号" maxlength="11">
                        <i class="phone-status-icon"></i>
                    </div>
                    <div class="login-regist-phone-input">
                        <input id="mobile_login_pwd" type="password" class="login-phone input long-input" placeholder="密码" maxlength="11">
                        <i class="phone-status-icon"></i>
                    </div>
                    <p class="tips" id="mobile_login_tip"></p>
                    <div class="login-submit submit" id="mobile_login_submit">立即登录</div>
                    <p class="voice-code" style="text-align: center;">
                        <span style="text-decoration: none;">
                            <a href="javascript:$('#mobile_login_view').hide();$('#mobile_register_view').show();">注册账号</a>
                        </span>
                        <span style="color:black;cursor: none;text-decoration: none;">|</span>
                        <span style="text-decoration: none;">
                            <a href="javascript:$('#mobile_login_view').hide();$('#mobile_forget_view').show();">忘记密码</a>
                        </span>
                    </p>
                    <div class="voice-code" style="text-align: center;">
                    	<a class="weixinLogin" href="">
                    		<img class="loginLogo" alt="" src="/images/login1.png" style="width:50px;height:50px;border-radius:50%"></img>
                    	</a>
                    </div>
                </div>
            </div>
            <div class="mobile-dialog login-regist" style="display:none;height:5rem" id="mobile_register_view">
                <div class="login-close close"></div>
                <div class="login-regist-card card" data-show="true">
                    <div class="logo"></div>
                    <div class="login-regist-phone-input">
                        <input id="mobile_reg_phone" type="tel" pattern="^d{11}$" class="login-phone input long-input" placeholder="手机号" maxlength="11">
                        <i class="phone-status-icon"></i>
                    </div>
                    <div class="login-regist-phone-input">
                        <input id="mobile_reg_pwd" type="password" class="login-phone input long-input" placeholder="密码" maxlength="11">
                        <i class="phone-status-icon"></i>
                    </div>
                    <p class="p-height"></p>
                    <div class="verify">
                        <input id="mobile_reg_verify" type="tel" pattern="^d{4}$" class="verify-code input short-input" placeholder="验证码" maxlength="4" validate="^d{4}$">
                        <div id="mobile_reg_get_code" class="send-verify-code input-button short-button btn-disable" send="false">发送验证码</div>
                    </div>
                    <p class="tips" id="mobile_reg_tip"></p>
                    <div class="login-submit submit" id="mobile_reg_submit">立即注册</div>
                    <p class="voice-code" style="text-align: center;">
                        <span style="text-decoration: none;">
                            <a href="javascript:$('#mobile_register_view').hide();$('#mobile_login_view').show();">登录账号</a>
                        </span>
                        <span style="color:black;cursor: none;text-decoration: none;">|</span>
                        <span style="text-decoration: none;">
                            <a href="javascript:$('#mobile_register_view').hide();$('#mobile_forget_view').show();">忘记密码</a>
                        </span>
                    </p>
                    <div class="voice-code" style="text-align: center;">
                    	<a class="weixinLogin" href="">
                    		<img class="loginLogo" alt="" src="/images/login1.png" style="width:50px;height:50px;border-radius:50%"></img>
                    	</a>
                    </div>
                </div>
            </div>
            <div class="mobile-dialog login-regist" style="display:none;height:5rem" id="mobile_forget_view">
                <div class="login-close close"></div>
                <div class="login-regist-card card" data-show="true">
                    <div class="logo"></div>
                    <div class="login-regist-phone-input">
                        <input id="mobile_forget_phone" type="tel" pattern="^d{11}$" class="login-phone input long-input" placeholder="手机号" maxlength="11">
                        <i class="phone-status-icon"></i>
                    </div>
                    <div class="login-regist-phone-input">
                        <input id="mobile_forget_pwd" type="password" class="login-phone input long-input" placeholder="设定新密码" maxlength="11">
                        <i class="phone-status-icon"></i>
                    </div>
                    <p class="p-height"></p>
                    <div class="verify">
                        <input id="mobile_forget_verify" type="tel" pattern="^d{4}$" class="verify-code input short-input" placeholder="验证码" maxlength="4" validate="^d{4}$">
                        <div id="mobile_forget_get_code" class="send-verify-code input-button short-button btn-disable" send="false">发送验证码</div>
                    </div>
                    <p class="tips" id="mobile_forget_tip"></p>
                    <div class="login-submit submit" id="mobile_forget_submit">提交</div>
                    <p class="voice-code" style="text-align: center;">
                        <span style="text-decoration: none;">
                            <a href="javascript:$('#mobile_forget_view').hide();$('#mobile_login_view').show();">登录账号</a>
                        </span>
                        <span style="color:black;cursor: none;text-decoration: none;">|</span>
                        <span style="text-decoration: none;">
                            <a href="javascript:$('#mobile_forget_view').hide();$('#mobile_register_view').show();">注册账号</a>
                        </span>
                    </p>
                    <div class="voice-code" style="text-align: center;">
                    	<a class="weixinLogin" href="">
                    		<img class="loginLogo" alt="" src="/images/login1.png" style="width:50px;height:50px;border-radius:50%"></img>
                    	</a>
                    </div>
                </div>
            </div>
        </div>
        
        
        <div id="focus_button" style="position:fixed;right:.2rem;bottom:1rem;z-index: 9999;width: 60px;height: 60px;background: url(/images/focus.png) no-repeat;background-size: 100%;display:none"></div>
        
        <div id="focus" class="cglx-login-mask mobile-login" style="display: none;position: fixed;top: 0;bottom: 0;left: 0;right: 0;z-index: 99999;background: rgba(0,0,0,.8);">
            <div class="mobile-dialog login-regist">
                <div class="login-close close"></div>
                <div class="login-regist-card card" data-show="true">
                    <div class="logo"></div>
                    <img src="/images/qrcode.jpg" width="100%">
                    <p style="text-align:center">关注公众号，获取更多内容</p>
                </div>
            </div>
        </div>

	<div class="wap-wrapper" id="wap-wrapper">
		<div class="tips">很抱歉，因微信支付限制，只能从微信内或者电脑端购买</div>
		<div class="info">
			<div class="title">文化娱乐产业投资逻辑</div>
			<div class="link">http://www.xfz.cn/course/series/10.html</div>
		</div>
		<div class="wx-tips">微信内打开方法：长按网址复制，在微信内发送该网址，在消息内打开。</div>
		<div class="bottom-logo"></div>
	</div>
	<div class="wrapper" id="wrapper">
		<input type="hidden" name="productId"
			value="e683b6a20f4a4dd088a72bcf84316baa"> <input
			type="hidden" name="title" value="文化娱乐产业投资逻辑"> <input
			type="hidden" name="img"
			value="http://static-image.xfz.cn/1501670834_106.jpg-seriescourse.list">
		<input type="hidden" name="desc" value="文娱大IP是如何投出来的？">
		<nav class="video-nav">
			<div class="container">
				<div class="nav-title">
					<a href="/courses/views/courses.html">在线课堂</a>
				</div>
				<ul>
					<li><a href="/courses/views/courses.html">最新课程</a></li>
					<li><a href="/courses/views/essay.html">文书写作</a></li>
					<li><a href="/courses/views/major.html">院校专业</a></li>
					<li><a href="/courses/views/case.html">实习求职</a></li>
					<li><a href="/courses/views/experience.html">留学体验</a></li>
					<li class="active"><a href="/courses/views/series.html">系列课</a></li>
				</ul>
				<div class="my-course"><a href="/view/mycourse.html">我的课堂</a></div>
			</div>
		</nav>
		<!-- mobile nav -->
		<nav class="second-mobile-nav">
			<ul>
				<li><a href="/courses/views/courses.html">最新课程</a></li>
				<li><a href="/courses/views/essay.html">文书写作</a></li>
				<li><a href="/courses/views/major.html">院校专业</a></li>
				<li><a href="/courses/views/case.html">实习求职</a></li>
				<li><a href="/courses/views/experience.html">留学体验</a></li>
				<li class="active"><a href="/courses/views/series.html">系列课</a></li>
				<li class="my-course">我的课堂</li>
			</ul>
		</nav>
		<div class=""></div>
		<div class="page-video-series">
			<div class="detail-content">
				<div class="info-left">
					<!-- 系类课程banner部分 -->
					<div class="series-banner">
						<div class="series-banner-img">
							<img src="" id="banner">
						</div>
						<div class="series-word" id="abstract"></div>
					</div>
					<div class="series-main">

						<!-- 系列课程购买价格栏 -->
						<div class="series-all-price">
							<div class="series-course-price">
								<span class="number-course">共
									<span class="course sub_count"></span>
									节课 - 
									<span class="min sub_time_total"></span>
								</span> 
								<span class="price-course">组合价：
									<span class="price cost"></span>
									<span class="money">&nbsp;&nbsp;(省<span class="discount"></span>元)</span>
                                </span>
							</div>

							<button class="series-buy-btn buy-series">立即购买</button>

						</div>



						<!-- 系列课程讲师介绍 -->
						<div class="series-introduct-teacher">
							<p class="teacher">课程讲师</p>
							<div class="intro">
								<div class="photo">
									<img id="teacher_img" src="" width="60" height="60" alt="">
								</div>
								<div class="info">
									<p class="name" id="teacher"></p>
									<p class="position" id="teacher_position"></p>
								</div>
							</div>
							<p class="text" id="teacher_abstract"></p>
						</div>


						<div class="course-title">
							<p>课程详情</p>
						</div>

						<div class="series-course-all">
						</div>
						<!-- 帮助中心 -->
						<div class="series-help">
							<div class="help-title">
								<p>帮助中心</p>
							</div>
							<div>
								<ul>
									<li id="help"></li>
									<!-- <li>2. 课程暂不支持下载观看，均为在线观看视频。</li>
									<li>3. 课程一经购买，不可转让、不可退款；仅限购买账号观看。</li>
									<li>4. 如有问题请咨询客服饭桌君： 电话：18618172287 微信：fanzhuojun888</li> -->
								</ul>
							</div>
						</div>
						<!-- 小饭桌夜校栏目 -->
						<div class="series-about">
							<div class="series-about-title">
								<p>关于DIY研习社在线课堂</p>
							</div>
							<div>
								<ul>
									<li id="about">
										DIY研习社在线课程是DIY研习社旗下的视频课程产品，旨在汇聚各大机构的优秀顾问和有成功经验的个人导师，为留学申请者提供实战的指导，帮大家更低成本、更高效率、更好体验的获取留学申请知识和方法。课程内容覆盖留学申请、出国语言、名企实习面试等，课程体验广受学生好评。
									</li>
								</ul>
							</div>
						</div>
						<!-- 课程推荐 -->
						<div class="series-recommend-all">
							<div class="series-height"></div>
							<div class="series-title">
								<span>课程推荐</span>
							</div>
							<!-- 手机推荐课堂-->
							<div class="course-recommend">
								<div class="big" id="big">


								</div>
								<div class="small" id="small">


								</div>
							</div>

						</div>
					</div>
				</div>
				<!-- 右侧栏 -->
				<div class="info-right">
					<!-- pc推荐课堂 -->
					<div class="recommend">
						<div class="sub-title">
							<span>推荐课堂</span>
						</div>
						<div class="recommend-big" id="recommend-big">


						</div>
						<div class="recommend-small" id="recommend-small">

						</div>
					</div>

				</div>

				<!-- mobile 固定购买栏 -->
				<div class="bottom-buy-link-mobile">
                    <div class="price">
                        
                        <p class="money">组合价:<span class="fee cost"></span><span style="font-size:18px;font-style:italic;color:gray;text-decoration:line-through;" class="original-price"></span></p>
                        
                        <p class="p-course" style="font-size:12px"><span class="number-course">共<span class="course sub_count"></span>节课</span><span class="remain"></span></p>
                    </div>
                    
                        <div class="buy-link-btn buy-series">立即购买</div>
                    
                </div>
				<!-- pc 购买栏 -->
				<div class="bottom-buy-link-wrap">
                    <div class="bottom-buy-link" style="display: none;">
                        
                            <div class="buy-link-btn buy-series">立即购买</div>
                        
                        <div class="info">
                            <span class="number-course">共<span class="course sub_count"></span>节课 - <span class="min sub_time_total"></span></span>
                            <span class="price-course">组合价：<span class="price cost"></span><span class="money">&nbsp;&nbsp;(省<span class="discount"></span>元)</span>
                            </span>
                        </div>
                    </div>
                </div>
				<!-- pc 购买浮动栏 -->
				<div class="bottom-buy-link-float" style="display: block;">
                    
                        <div class="buy-link-btn buy-series">立即购买</div>
                    
                    <div class="info">
                        <span class="number-course">共<span class="course sub_count"></span>节课 - <span class="min sub_time_total"></span></span>
                        <span class="price-course">组合价：<span class="price cost"></span><span class="money">&nbsp;&nbsp;(省<span class="discount"></span>元)</span>
                        </span>
                    </div>
                </div>

			</div>
		</div>
		<div class="mask">
			<div class="bigImg"></div>
		</div>
	</div>


	<div class="cglx-login-mask bindPhonePC" style="display: none;position: fixed;z-index: 99999;background:rgba(56,62,68,.9)">
	    <div class="login-dialog dialog" style="display:block;height:470px;background:none">
	        <div class="login-close close" style="top:0"></div>
	        <div class="login-fill-card">
	            <div class="login-card card" style="height:200px;margin-top: -112px;">
	                <div class="inputs">
	                	<p style="text-align:center;font-weight:bold;font-size:12px;margin:20px 0px;">手机验证</p>
	                    <div class="login-phone-input">
	                        <input id="phoneNum" type="text" pattern="^\d{11}$" class="login-phone input long-input" placeholder="仅支持中国大陆手机号" maxlength="11" autocomplete="off">
	                    </div>
	                    <div class="verify" style="margin-top:10px">
	                        <input id="verify_code" type="text" pattern="^\d{4}$" class="verify-code input short-input" placeholder="验证码" maxlength="4" validate="^\d{4}$" autocomplete="off">
	                        <input id="get_verify_code" type="button" class="send-verify-code input-button short-button btn-disable" send="false" value="发送验证码">
	                    </div>
	                    <p class="tips" id="pc_bind_tip" style="font-size: 12px;margin: 10px 40px;color: red;"></p>
	                    <div class="login-submit submit" id="bindphonePC" style="margin-top:20px">确定</div>
	                </div>
	            </div>
	        </div>
	    </div>
    </div>

	<div class="cglx-login-mask bindPhoneMB" style="display: none;position: fixed;top: 0;bottom: 0;left: 0;right: 0;z-index: 99999;background: rgba(0,0,0,.8);">
        <div class="mobile-dialog login-regist" style="display:block;height:3rem" id="mobile_register_view">
            <div class="login-close close"></div>
            <div class="login-regist-card card" data-show="true">
            	<p style="text-align:center;font-weight:bold;font-size:0.18rem;margin:20px 0px;">手机验证</p>
                <div class="login-regist-phone-input">
                    <input id="phoneNumMB" type="tel" pattern="^d{11}$" class="login-phone input long-input" placeholder="手机号" maxlength="11">
                    <i class="phone-status-icon"></i>
                </div>
                <div class="verify" style="margin-top:20px">
                    <input id="verify_code_mb" type="tel" pattern="^d{4}$" class="verify-code input short-input" placeholder="验证码" maxlength="4" validate="^d{4}$">
                    <div id="get_verify_code_mb" class="send-verify-code input-button short-button btn-disable" send="false">发送验证码</div>
                </div>
                <p class="tips" id="mobile_bind_tip"></p>
                <div class="login-submit submit" id="bindphoneMB">确定</div>
            </div>
        </div>
    </div>


	<footer class="footer-simple">
		<div class="section">
			<section class="link-section">
				<div class="foot-logo"></div>
				<div class="links">
					<a href="/view/about.html">关于我们</a> |<a href="/courses/views/courses.html">在线课程</a>
				</div>
				<div class="attention">
					关注我们：<a class="showWx"><span class="wx">DIY研习社</span></a><a
						class="showXl" href="http://weibo.com/u/5108257900"
						target="_blank"><span class="xl">DIY研习社</span></a>
					<div class="wxQr"></div>
				</div>
				<div class="company-info">
                    <!-- <span class="address">地址：北京市朝阳区东三环北路38号院泰康金融大厦</span> -->
                    <span class="phonenum">客服电话：021-60277506（工作日10点－18点）</span>
                </div>
			</section>
		</div>
		<div class="bottom">
			<p>©2017 上海邦融网络科技有限责任公司  沪ICP备15002235号-6</p>
		</div>
	</footer>
	<script src="/courses/js/main.js"></script>
	<script type="text/javascript" src="/js/login2.js"></script>
	<script src="/courses/js/courseSeriesDetail.min.js"></script>
	<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
	
		wx.config({
			appId: 'wxf139053a88924f58',
			timestamp: <%=result.get("timestamp")%>,
			nonceStr: '<%=result.get("nonceStr")%>',
			signature: '<%=result.get("signature")%>',
			jsApiList: [
				'onMenuShareQQ',
				'onMenuShareTimeline',
				'onMenuShareAppMessage',
				'chooseWXPay'
			]
		});
		var imgUrl = "http://www.udiyclub.com/images/logo.png";
		var descContent = "DIY研习社－中国留学生互助交流平台，让留学不孤单";
		var shareTitle = "DIY研习社";
		var lineLink = "http://www.udiyclub.com";
		
		$('.name').on('click', function(event) {
			event.stopPropagation();
			$('#logout').toggle('fast');
		});
		$(document).on('click', function() {
			$('#logout').hide(500);
		});
		
		(function ($) {
	        $.getUrlParam = function (name) {
	            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	            var r = window.location.search.substr(1).match(reg);
	            if (r != null) return unescape(r[2]); return null;
	        }
	    })(jQuery);	

		var id = <%=courseId%>;
		var cost;
		
		if(isWeiXin()) {
			$('.bottom-buy-link-float').css('display', 'none');
		}
		
		$.post('/course/getSeriesDetailById', {id : id}, function(data) {
			
			cost = data.cost;
			$('#banner').attr('src', '/cglx/files/imgs/' + data.banner);


			var deadline = data.deadline;
			var now = (new Date()).valueOf();
			var d_value = deadline-now;
			var remain_day = parseInt((d_value)/1000/60/60/24);
			var remain_hour = parseInt((d_value - remain_day*1000*60*60*24)/1000/60/60);
			var remain_min = parseInt((d_value - remain_day*1000*60*60*24 - remain_hour*1000*60*60)/1000/60);
			
			$('.cost_title').html(data.fee + '  <span style="font-size:12px;color:black">(' + data.rebate + '折 剩余时间：'+remain_day+'天'+remain_hour+'小时'+remain_min+'分钟)</span>');
			$('.cost').html('￥' + data.total + '   ');
			if(now < deadline && now > data.starttime) {
				$('.remain').html('&nbsp;还剩' + remain_day+'天'+remain_hour+'小时'+remain_min+'分钟');
			}
			$('.original-price').html('￥' + data.cost);
			
			$('.discount').html(data.discount);
			var abstract_ = data.abstract;
			abstract_ = abstract_.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
			$('#abstract').html(abstract_);
			
			$('.sub_count').html(data.sub_count);
			$('.sub_time_total').html(data.sub_time_total + "分钟");
			$('#teacher_img').attr('src', '/cglx/files/imgs/' + data.teacher_image);
			$('#teacher_position').html(data.teacher_position);
			
			var teacher_abstract = data.teacher_abstract;
			teacher_abstract = teacher_abstract.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
			$('#teacher_abstract').html(teacher_abstract);
			
			var help = data.help;
			help = help.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
			$('#help').html(help);
			
			//$('#about').html(data.about);
			if(data.pay_status == 1) {
				$('.buy-series').html('已购买').css('background-color', '#ccc');
			}
			imgUrl = "http://www.udiyclub.com/cglx/files/imgs/"+data.banner;
			shareTitle = data.title;
			lineLink = window.location.href;
			execWeixinShare();
			
			$('.buy-series').on('click', function() {
				if(user_id == '') {
					if(window.screen.width < 700) {
						$('#login-btn-m').click();
		 			} else {
		 				$('#login-btn').click();
		 			}
				} else {
					if($(this).html() == '已购买') return;
					$.post('/user/getUserInfo',function(data){
						var phone = data.phone;
						if(phone==null || phone=='') {
							if(isWeiXin()){
								$('.bindPhoneMB').css('display','block');
							} else {
								$('.bindPhonePC').css('display','block');
							}
						} else {
							if(cost == 0) {
								$.post('/course/addFreeCourse', {course_id : id}, function(data) {
									if(data.error_code == 0) {
										window.location.href = '/view/mycourse.html';
									} else {
										alert(data.error_msg);
										return;
									}
								});
							} else {
								if(isWeiXin()) {
									weixinPay();
								} else {
						 			window.location.href = '/courses/views/orderPay.html?id=' + id;
								}
							}
						}
					});
				}
			});
			
		});
		
		$.post('/course/getSeriesSubCourseById', {id : id}, function(data) {
			for(var index in data) {
				$('.series-course-all').append('<div class="series-every-course"><a href="/courses/jsp?id='+data[index].id+'&view=detail" target="_blank">'
						+ '<div class="series-every-pic"><img src="/cglx/files/imgs/'+data[index].snapshot+'" alt="">'
						+ '</div><div class="series-every-introduct"><div class="every-title"><span class="course">【课时'+(parseInt(index)+1)+'】'
						+ '</span>'+data[index].title+'</div><div class="every-price-pc"><span class="time">时长：&nbsp;&nbsp;'+data[index].time+'分钟</span>'
						+ '<span class="money">单价:<span class="monpri">￥'+data[index].fee+'</span></span></div><div class="every-price-mobile">'
						+ '单价: <span class="money">￥'+data[index].fee+'</span></div><div class="every-lead">'+data[index].abstract+'</div></div></a></div>');
			}
		});
		
		$.post('/course/getNavRecommendCourse', function(data) {
			for(var index in data) {
				if(data[index].is_series == 1) {
					//var link = !isWeiXin() ? ('/courses/jsp?id='+data[index].id) : ('https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf139053a88924f58&redirect_uri=http%3A%2F%2Fwww.udiyclub.com%2FgetOpenIdRedirect%3Fview=_courses_jspARGSid='+data[index].id+'&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect');
					var link = '/courses/jsp?id='+data[index].id;
					$('#big').append('<div class="recommend-big"><a href="/courses/jsp?id='+data[index].id+'" target="_blank">' 
							+ '<img src="/cglx/files/imgs/'+data[index].snapshot+'" alt=""><div class="detail"><span class="left">'
							+ '共'+data[index].sub_count+'节课</span> <span class="right"><span class="spend-price">¥'+data[index].total+'</span>'
							+ '(省'+data[index].discount+'元) </span></div></a></div>');
					
					$('#recommend-big').append('<div class="item"><a href="/courses/jsp?id='+data[index].id+'"><div class="photo">'
							+ '<img	src="/cglx/files/imgs/'+data[index].snapshot+'"></div><div class="info"><span class="left">'
							+ '共'+data[index].sub_count+'节课</span> <span class="right"><span class="spend-price">¥'+data[index].total+'</span>'
							+ '(省'+data[index].discount+'元) </span></div></a></div>');						
				} else {
					//var link = !isWeiXin() ? ('/courses/jsp?id='+data[index].id+'&view=detail') : ('https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf139053a88924f58&redirect_uri=http%3A%2F%2Fwww.udiyclub.com%2FgetOpenIdRedirect%3Fview=_courses_jspARGSid='+data[index].id+'ANDview=detail&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect');
					var link = '/courses/jsp?id='+data[index].id+'&view=detail';
					$('#small').append('<div class="recommend-every-course"><a href="/courses/jsp?id='+data[index].id+'&view=detail" target="_blank">'
							+ '<div class="recommend-every-pic"><img src="/cglx/files/imgs/'+data[index].snapshot+'" alt=""></div>'
							+ '<div class="recommend-every-introduct"><div class="every-title">'+data[index].title+'</div>'
							+ '<div class="every-price-mobile"><span class="money">￥'+data[index].cost_+'</span></div></div></a></div>');
					
					$('#recommend-small').append('<div class="every-course"><a href="/courses/jsp?id='+data[index].id+'&view=detail"><div class="info-left-recommond">'
							+ '<div class="video-title">'+data[index].title+'</div><div class="video-teacher">'+data[index].final_tea+'/'+data[index].final_position+'</div>'
							+ '</div><div class="photo-right"><span class="tags"></span> <img src="/cglx/files/imgs/'+data[index].snapshot+'" alt="">'
							+ '</div></a></div>');
				}
			}
		});
		function isWeiXin(){ 
			var ua = window.navigator.userAgent.toLowerCase(); 
			if(ua.match(/MicroMessenger/i) == 'micromessenger'){ 
				return true; 
			}else{ 
				return false; 
			} 
		}
		
		function weixinPay() {
			wx.ready(function() {
				$.post('/weixinUserCoursePay?course_id='+id, function(data) {
					var jsonData = JSON.parse("{"+data+"}");
	    			if(jsonData.ddcb_error_msg != null) {
	    				alert(jsonData.ddcb_error_msg);
	    			} else {
	    				wx.chooseWXPay({
	    		            timestamp: jsonData.timeStamp,
	    		            nonceStr: jsonData.nonceStr,
	    		            package: jsonData.package,
	    		            signType: jsonData.signType,
	    		            paySign: jsonData.paySign,
	    		            success: function (res) {
	    		            	window.location.reload();
	    		            	if(res.errMsg != null && res.errMsg == "chooseWXPay:ok") {
	                				ele.innerHTML = "点击进入";
	                				ele.removeEventListener('tap', handler);
	                				ele.addEventListener('tap', enterClass);
	    		            		alert("支付成功!");
	    		            	} else {
	    		            		alert("支付失败！");
	    		            	}																            
	    		            },
	    		            fail:function(res) {
	    		            	alert(JSON.stringify(res));
	    		            }
	    		        });
	    			}
	    		});
			});
		}
		
		function execWeixinShare(){
			wx.ready(function() {
				setTimeout(function() {
					wx.onMenuShareTimeline({
						title : shareTitle, // 分享标题
						link : lineLink, // 分享链接
						imgUrl : imgUrl, // 分享图标
						success : function() {
							/* alert("报名成功！");
							currentImageSelectEle.setAttribute("disabled", true);
		    		    	currentImageSelectEle.innerHTML = "已经报名";
		    		    	mui.ajax({
		                		url: "/course/uploadUserShare",
		                		type: "POST",
		                		data: {courseId:currentImageSelectEle.getAttribute("course_id")},
		                		success: function(data) {
		                			
		                		},
		                		error: function(status, error) {
		                			
		                		}
		                	}); */
						},
						cancel : function() {
							//alert("您没有分享，报名失败！");
						}
					});
					wx.onMenuShareAppMessage({
						title : shareTitle, // 分享标题
						desc : descContent, // 分享描述
						link : lineLink, // 分享链接
						imgUrl : imgUrl, // 分享图标
						type : '', // 分享类型,music、video或link，不填默认为link
						dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
						success : function() {
							// 用户确认分享后执行的回调函数
						},
						cancel : function() {
							// 用户取消分享后执行的回调函数
						}
					});
					wx.onMenuShareQQ({
						title : shareTitle, // 分享标题
						desc : descContent, // 分享描述
						link : lineLink, // 分享链接
						imgUrl : imgUrl, // 分享图标
						success : function() {
							// 用户确认分享后执行的回调函数
						},
						cancel : function() {
							// 用户取消分享后执行的回调函数
						}
					});
				}, 500);
			});
		}
	</script>
</body>
</html>