<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="java.util.*"%>
<%@ page import="com.ddcb.utils.WeixinTools"%>
<%
	WebApplicationContext wac = WebApplicationContextUtils
			.getRequiredWebApplicationContext(this.getServletContext());
	String code = (String) session.getAttribute("url_code");
	String courseId = (String) session.getAttribute("course_id");
	Map<String, String> result = new HashMap<>();
	result = WeixinTools.getSign(
			"http://www.udiyclub.com/courses/views?code=" + code + "&state=123");	
%>

<!DOCTYPE html>
<!-- saved from url=(0033)http://www.xfz.cn/course/117.html -->
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

<link href="/courses/css/video.min.css" rel="stylesheet">
<script src="/js/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="/css/login.css">
<style>
	video::-internal-media-controls-download-button {
	    display:none;
	}
	
	video::-webkit-media-controls-enclosure {
	    overflow:hidden;
	}
	
	video::-webkit-media-controls-panel {
	    width: calc(100% + 30px); /* Adjust as needed */
	}
</style>

</head>
<body device="pc">
	<header class="header">
		<div class="transparent-mask"></div>
		<div class="contain">
			<div class="logo">
				<a href="homepage.html"></a>
			</div>
			<nav class="PC-nav">
				<ul>
					<li class="news" data-id="news"><a href="/view/homepage.html">留学专栏</a>
					</li>
					<li class="active"><a href="courses.html">在线课程</a></li>
					<li class="activity" data-id="activity"><a href="/view/success.html">成功案例</a></li>
					<li class="gift" data-id="gift"><a href="/view/about.html">关于我们</a>
					</li>
					<li class="ventureservice" data-id="ventureservice"><a
						href="/view/report.html">社员风采</a></li>
					<li class="search" data-id="search"><a href="/view/search.html">搜索</a>
					</li>
				</ul>
			</nav>
			<nav class="mobile-nav">
				<i class="top-menu-expand-icon"></i>
				<ul class="nav-list">
					<li class="news" data-id="news"><a href="/view/homepage.html">留学专栏</a>
					</li>
					<li class="course active" data-id="course"><a
						href="/view/success.html">成功案例</a></li>
				</ul>
				<ul class="expand-nav-list" id="mobile_not_login">
					<li class="user-info"><a href="javascript:void(0)"
						id="login-btn-m">登录</a></li>
					<li class="activity"><a href="/view/success.html">成功案例</a></li>
					<li class="search"><a href="/view/report.html">社员风采</a></li>
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
					<i class="triangle"></i> <a href="personalInfo.html"
						class="myorder-btn">个人信息</a> <a href="estimate.html">背景评估</a> <a
						href="javascript:userlogout();">退出</a>
				</div>
				<div class="login-false-wrapper">
					<i class="icon"></i> <a href="javascript:void(0)" id="login-btn">登录</a>
				</div>
				<div class="login-success-wrapper" style="display: none;">
					<div class="photo">
						<img src="/images/topic_photo.png">
					</div>
					<div class="name" id="user_id"></div>
				</div>
			</div>
		</div>
	</header>

	<div class="cglx-login-mask pc-login"
		style="display: none; position: fixed; z-index: 99999; background: rgba(56, 62, 68, .9)">
		<div class="login-dialog dialog" id="login_view">
			<div class="login-close close"></div>
			<div class="dialog-bg">
				<div class="dialog-title"
					style="background: url(/images/logo.png) no-repeat center; width: 320px; height: 45px"></div>
			</div>
			<div class="login-fill-card">
				<div class="login-card card">
					<div class="inputs">
						<div class="login-phone-input">
							<input id="login_phone" type="text" pattern="^\d{11}$"
								class="login-phone input long-input" placeholder="请输入手机号"
								maxlength="11" autocomplete="off"> <i
								class="phone-status-icon"></i>
						</div>
						<p class="button-height"></p>
						<div class="login-phone-input">
							<input id="login_pwd" type="password"
								class="login-phone-pwd input long-input" style="margin: 0 auto;"
								placeholder="请输入密码" maxlength="11" autocomplete="off">
						</div>
						<p class="button-height"></p>
						<p class="phone-tips" id="login_tip" style="display: none;"></p>
						<div class="login-submit submit" id="login_submit">登录</div>
						<p class="voice-code" style="text-align: center;">
							<span style="text-decoration: none;"><a
								href="javascript:$('#login_view').hide();$('#register_view').show();">注册账号</a></span>
							<span style="color: black; cursor: none; text-decoration: none;">|</span>
							<span style="text-decoration: none;"><a
								href="javascript:$('#login_view').hide();$('#forget_view').show();">忘记密码</a></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		<div class="login-dialog dialog" id="register_view"
			style="display: none; height: 330px;">
			<div class="login-close close"></div>
			<div class="dialog-bg">
				<div class="dialog-title"
					style="background: url(/images/logo.png) no-repeat center; width: 320px; height: 45px"></div>
			</div>
			<div class="login-fill-card">
				<div class="login-card card"
					style="height: 240px; margin-top: -102px;">
					<div class="inputs">
						<div class="login-phone-input">
							<input id="reg_phone" type="text" pattern="^\d{11}$"
								class="login-phone input long-input" placeholder="仅支持中国大陆手机号"
								maxlength="11" autocomplete="off">
						</div>
						<p class="button-height"></p>
						<div class="login-phone-input">
							<input id="reg_pwd" type="password"
								class="login-phone-pwd input long-input" style="margin: 0 auto;"
								placeholder="请输入密码" maxlength="11" autocomplete="off">
						</div>
						<p class="button-height"></p>
						<div class="verify">
							<input id="reg_verify" type="text" pattern="^\d{4}$"
								class="verify-code input short-input" placeholder="验证码"
								maxlength="4" validate="^\d{4}$" autocomplete="off"> <input
								id="reg_get_code" type="button"
								class="send-verify-code input-button short-button btn-disable"
								send="false" value="发送验证码">
						</div>
						<p class="button-height"></p>
						<p class="phone-tips" id="reg_tip" style="display: none;"></p>
						<div class="login-submit submit" id="reg_submit">注册</div>
						<p class="voice-code" style="text-align: center;">
							<span style="text-decoration: none;"><a
								href="javascript:$('#register_view').hide();$('#login_view').show();">登录账号</a></span>
							<span style="color: black; cursor: none; text-decoration: none;">|</span>
							<span style="text-decoration: none;"><a
								href="javascript:$('#register_view').hide();$('#forget_view').show();">忘记密码</a></span>
						</p>
					</div>
				</div>
			</div>
		</div>
		<div class="login-dialog dialog" id="forget_view"
			style="display: none; height: 330px;">
			<div class="login-close close"></div>
			<div class="dialog-bg">
				<div class="dialog-title"
					style="background: url(/images/logo.png) no-repeat center; width: 320px; height: 45px"></div>
			</div>
			<div class="login-fill-card">
				<div class="login-card card"
					style="height: 240px; margin-top: -102px;">
					<div class="inputs">
						<div class="login-phone-input">
							<input id="forget_phone" type="text" pattern="^\d{11}$"
								class="login-phone input long-input" placeholder="仅支持中国大陆手机号"
								maxlength="11" autocomplete="off">
						</div>
						<p class="button-height"></p>
						<div class="login-phone-input">
							<input id="forget_pwd" type="password"
								class="login-phone-pwd input long-input" style="margin: 0 auto;"
								placeholder="设定新密码" maxlength="11" autocomplete="off">
						</div>
						<p class="button-height"></p>
						<div class="verify">
							<input id="forget_verify" type="text" pattern="^\d{4}$"
								class="verify-code input short-input" placeholder="验证码"
								maxlength="4" validate="^\d{4}$" autocomplete="off"> <input
								id="forget_get_code" type="button"
								class="send-verify-code input-button short-button btn-disable"
								send="false" value="发送验证码">
						</div>
						<p class="button-height"></p>
						<p class="phone-tips" id="forget_tip" style="display: none;"></p>
						<div class="login-submit submit" id="forget_submit">提交</div>
						<p class="voice-code" style="text-align: center;">
							<span style="text-decoration: none;"><a
								href="javascript:$('#forget_view').hide();$('#login_view').show();">登录账号</a></span>
							<span style="color: black; cursor: none; text-decoration: none;">|</span>
							<span style="text-decoration: none;"><a
								href="javascript:$('#forget_view').hide();$('#register_view').show();">注册账号</a></span>
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="cglx-login-mask mobile-login"
		style="display: none; position: fixed; top: 0; bottom: 0; left: 0; right: 0; z-index: 99999; background: rgba(0, 0, 0, .8);">
		<div class="mobile-dialog login-regist" id="mobile_login_view">
			<div class="login-close close"></div>
			<div class="login-regist-card card" data-show="true">
				<div class="logo"></div>
				<div class="login-regist-phone-input">
					<input id="mobile_login_phone" type="tel" pattern="^d{11}$"
						class="login-phone input long-input" placeholder="手机号"
						maxlength="11"> <i class="phone-status-icon"></i>
				</div>
				<div class="login-regist-phone-input">
					<input id="mobile_login_pwd" type="password"
						class="login-phone input long-input" placeholder="密码"
						maxlength="11"> <i class="phone-status-icon"></i>
				</div>
				<p class="tips" id="mobile_login_tip"></p>
				<div class="login-submit submit" id="mobile_login_submit">立即登录</div>
				<p class="voice-code" style="text-align: center;">
					<span style="text-decoration: none;"><a
						href="javascript:$('#mobile_login_view').hide();$('#mobile_register_view').show();">注册账号</a></span>
					<span style="color: black; cursor: none; text-decoration: none;">|</span>
					<span style="text-decoration: none;"><a
						href="javascript:$('#mobile_login_view').hide();$('#mobile_forget_view').show();">忘记密码</a></span>
				</p>
			</div>
		</div>
		<div class="mobile-dialog login-regist" style="display: none;"
			id="mobile_register_view">
			<div class="login-close close"></div>
			<div class="login-regist-card card" data-show="true">
				<div class="logo"></div>
				<div class="login-regist-phone-input">
					<input id="mobile_reg_phone" type="tel" pattern="^d{11}$"
						class="login-phone input long-input" placeholder="手机号"
						maxlength="11"> <i class="phone-status-icon"></i>
				</div>
				<div class="login-regist-phone-input">
					<input id="mobile_reg_pwd" type="password"
						class="login-phone input long-input" placeholder="密码"
						maxlength="11"> <i class="phone-status-icon"></i>
				</div>
				<p class="p-height"></p>
				<div class="verify">
					<input id="mobile_reg_verify" type="tel" pattern="^d{4}$"
						class="verify-code input short-input" placeholder="验证码"
						maxlength="4" validate="^d{4}$">
					<div id="mobile_reg_get_code"
						class="send-verify-code input-button short-button btn-disable"
						send="false">发送验证码</div>
				</div>
				<p class="tips" id="mobile_reg_tip"></p>
				<div class="login-submit submit" id="mobile_reg_submit">立即注册</div>
				<p class="voice-code" style="text-align: center;">
					<span style="text-decoration: none;"><a
						href="javascript:$('#mobile_register_view').hide();$('#mobile_login_view').show();">登录账号</a></span>
					<span style="color: black; cursor: none; text-decoration: none;">|</span>
					<span style="text-decoration: none;"><a
						href="javascript:$('#mobile_register_view').hide();$('#mobile_forget_view').show();">忘记密码</a></span>
				</p>
			</div>
		</div>
		<div class="mobile-dialog login-regist" style="display: none;"
			id="mobile_forget_view">
			<div class="login-close close"></div>
			<div class="login-regist-card card" data-show="true">
				<div class="logo"></div>
				<div class="login-regist-phone-input">
					<input id="mobile_forget_phone" type="tel" pattern="^d{11}$"
						class="login-phone input long-input" placeholder="手机号"
						maxlength="11"> <i class="phone-status-icon"></i>
				</div>
				<div class="login-regist-phone-input">
					<input id="mobile_forget_pwd" type="password"
						class="login-phone input long-input" placeholder="设定新密码"
						maxlength="11"> <i class="phone-status-icon"></i>
				</div>
				<p class="p-height"></p>
				<div class="verify">
					<input id="mobile_forget_verify" type="tel" pattern="^d{4}$"
						class="verify-code input short-input" placeholder="验证码"
						maxlength="4" validate="^d{4}$">
					<div id="mobile_forget_get_code"
						class="send-verify-code input-button short-button btn-disable"
						send="false">发送验证码</div>
				</div>
				<p class="tips" id="mobile_forget_tip"></p>
				<div class="login-submit submit" id="mobile_forget_submit">提交</div>
				<p class="voice-code" style="text-align: center;">
					<span style="text-decoration: none;"><a
						href="javascript:$('#mobile_forget_view').hide();$('#mobile_login_view').show();">登录账号</a></span>
					<span style="color: black; cursor: none; text-decoration: none;">|</span>
					<span style="text-decoration: none;"><a
						href="javascript:$('#mobile_forget_view').hide();$('#mobile_register_view').show();">注册账号</a></span>
				</p>
			</div>
		</div>
	</div>
	
	<div class="modal-mask pc" style="opacity: 1; display: none;">
		<div class="pay-fill-form-dialog dialog" style="opacity: 1;">
		    <div class="pay-fill-form-close close"></div>
		    <div class="content">
		        <div class="title">请选择支付方式：</div>
		        <div style="text-align:center">
		        	<a href="#" id="alipay_btn"><img src="/courses/images/alipay.jpg" width="100px" height="70px" style="margin-right:100px"></a>
		        	<a href="#" id="wechat_btn"><img src="/courses/images/WeChat.jpg" width="65px" height="60px"></a>
		        </div>
			</div>
		</div>
	</div>

	<div class="modal-mask mobile" style="opacity: 1; display: none;">
		<div class="pay-fill-form-dialog dialog" style="opacity: 1;">
		    <div class="pay-fill-form-close close"></div>
		    <div class="content">
		        <div class="title">请选择支付方式：</div>
		        <div style="text-align:center">
		        	<a href="#" id="alipay_btn_mobile"><img src="/courses/images/alipay.jpg" width="100px" height="70px" style="margin-right:100px"></a>
		        	<a href="#" id="wechat_btn_mobile"><img src="/courses/images/WeChat.jpg" width="65px" height="60px"></a>
		        </div>
			</div>
		</div>
	</div>


	<div class="wrapper" id="wrapper">
		<nav class="video-nav">
			<div class="container">
				<div class="nav-title">
					<a href="/courses/views/courses.html">在线课堂</a>
				</div>
				<ul>
					<li class="active"><a href="/courses/views/courses.html">最新课程</a></li>
					<li><a href="/courses/views/essay.html">文书写作</a></li>
					<li><a href="/courses/views/major.html">院校专业</a></li>
					<li><a href="/courses/views/case.html">成功案例</a></li>
					<li><a href="/courses/views/experience.html">留学体验</a></li>
					<li><a href="/courses/views/series.html">系列课</a></li>
				</ul>
				<div class="my-course">我的课堂</div>
			</div>
		</nav>
		<!-- mobile nav -->
		<nav class="second-mobile-nav">
			<ul>
				<li class="active"><a href="/courses/views/courses.html">最新课程</a></li>
				<li><a href="/courses/views/essay.html">文书写作</a></li>
				<li><a href="/courses/views/major.html">院校专业</a></li>
				<li><a href="/courses/views/case.html">成功案例</a></li>
				<li><a href="/courses/views/experience.html">留学体验</a></li>
				<li><a href="/courses/views/series.html">系列课</a></li>
				<li class="my-course">我的课堂</li>
			</ul>
		</nav>
		<div class="page-video">
			<div class="detail-content">
				<div class="video">
					<!-- 视频区 头部banner -->
					<div class="banner">
						<!-- banner信息 -->
						<div class="banner-left">
							<div class="video-title course_title"></div>
							<div class="video-info">
								<div class="wxApi">
									<input type="hidden" name="course-photo" id="course-photo"
										value="http://static-image.xfz.cn/1503558453_980.jpg">
								</div>
								<div class="share">
									<i>分享至：</i> <a class="share_bg weixin"></a> <a
										class="share_bg sina" target="_blank"></a>
									<wb:share-button appkey="123673426" addition="simple"
										type="icon"></wb:share-button>
									<div class="panel-weixin">
										<section class="weixin-section">
											<p>
												<img alt="UDIY" src="">
											</p>
										</section>
										<h3>打开微信“扫一扫”，打开网页后点击屏幕右上角分享按钮</h3>
									</div>
								</div>
							</div>
						</div>
						<!-- banner buy button -->
						<div class="buy-link">


							<div class="price">
								<span>¥</span><span class="cost"></span>
							</div>

							<!-- button -->

							<div class="buy-btn not_buy join-button watch-video my-btn">立即购买</div>


						</div>
					</div>
					<!-- 视频播放区 -->
					<div class="video-detail watch-video" id="video-snap" style="display:block">
						<!-- 无播放权限 -->
						<div class="no-auth-player">
							<div class="bg-photo">
								<img id="course_snapshot" src="">
							</div>
							<div class="no-login-mask">
								<div class="player-icon">
									<img src="/courses/images/pc-icon-replay.png">
								</div>
							</div>
						</div>
					</div>
					<video id="video_src" width="100%" style="display:none"></video>
					
				</div>
				<!-- mobile 购买按钮状态 -->
				<div class="mobile-buy-status">
					<div class="title course_title"></div>
					<div class="time">时长: <span id="time"></span>分钟</div>

					<div class="buy-status no-buy"></div>

					<!-- 所属系列课手机 -->

					<div class="series-recommend">
						<span class="tips"></span>
						<p class="info-title">所属系列课:<span class="parent_title"></span></p>
						<div class="info">
							<div class="price-and-len">
							</div>
							<div class="detail">
								<a class="parent_href" target="_blank">查看详情></a>
							</div>
						</div>
					</div>


				</div>
				<!-- 课程详情介绍 -->
				<div class="info-left">
					<!-- 所属系列课pc -->

					<div class="series-info">
						<div class="sub-title">
							<span>所属系列课</span>
						</div>
						<div class="main-content">
							<div class="info-img-left">
								<img id="parent_snapshot" >
							</div>
							<div class="info-detail-right">
								<p class="content parent_title"></p>
								<div class="price-and-len">
								</div>
								<div class="detail">
									<a class="parent_href" target="_blank">查看详情></a>
								</div>
							</div>
						</div>
					</div>


					<div class="teacher-info">
						<div class="teacher-title">课程讲师</div>
						<div class="icon guide-img"></div>
						<div class="guide-person">

							<div class="photo">
								<img id="teacher_image" src="">
							</div>

							<div class="person-info">
								<span class="name" id="teacher"></span> <span class="position"
									id="teacher_position"></span>
							</div>
						</div>
						<div class="content" id="teacher_abstract"></div>
					</div>

					<div class="info">
						<div class="sub-title">
							<span>课程简介</span>
						</div>
						<div class="content">
							<p id="description"></p>
						</div>
					</div>



					<div class="info">
						<div class="sub-title">
							<span>课程大纲</span>
						</div>
						<div class="content">
							<p id="outline"></p>
						</div>
					</div>





					<div class="info">
						<div class="sub-title">
							<span>课程信息</span>
						</div>
						<div class="content">
							<p id="info"></p>
						</div>
					</div>



					<div class="info">
						<div class="sub-title">
							<span>适宜人群</span>
						</div>
						<div class="content">
							<p id="crowds"></p>
						</div>
					</div>


					<div class="info">
						<div class="sub-title">
							<span>帮助中心</span>
						</div>
						<div class="content">
							<p id="help"></p>
						</div>
					</div>
					<div class="info">
						<div class="sub-title">
							<span>关于小饭桌在线课堂</span>
						</div>
						<div class="content">
							<p id="about">
							</p>
						</div>
					</div>
					<!-- mobile 课程推荐 -->
					<div class="info mobile-course-recommend">
						<div class="sub-title">
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
				<!-- pc 右侧课程推荐 -->
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
				<!-- pc 立即购买固定栏 -->
				<div class="bottom-buy-link-wrap">
					<div class="bottom-buy-link">

						<div class="buy-link-btn watch-video my-btn">立即购买</div>

						<div class="info course_title"></div>

						<div class="price">
							<span>￥</span> <span class="cost"></span>
						</div>

					</div>
				</div>
				<!-- pc 立即购买浮动栏 -->
				<div class="bottom-buy-link-float">

					<div class="buy-link-btn watch-video my-btn">立即购买</div>

					<div class="info course_title"></div>

					<div class="price">
						<span>￥</span> <span class="cost"></span>
					</div>

				</div>
			</div>
		</div>
		<div class="mask">
			<div class="bigImg"></div>
		</div>
	</div>
	<!-- <div class="loading"></div> -->
	<div class="mask">
		<div class="bigImg"></div>
	</div>
	</div>

	<footer class="footer-simple">
		<div class="section">
			<section class="link-section">
				<a href="apply_report.html" target="_blank">
					<div class="foot-logo"></div>
				</a>
				<div class="links">
					<a href="about.html" target="_blank"> </a> <a href="about.html">关于我们</a>
					| <a href="report.html">社员风采</a>
					<!-- | <a
						href="http://www.xfz.cn/apply/media/?code=report&amp;channel=web"
						target="_blank">寻求报道</a> | <a href="http://www.xfz.cn/xfzgifts"
						target="_blank">创业礼包</a> -->
				</div>
				<div class="attention">
					关注我们： <a class="showWx"><span class="wx">DIY研习社</span></a> <a
						class="showXl" href="http://weibo.com/u/5108257900"
						target="_blank"><span class="xl">DIY研习社</span></a>
					<div class="wxQr"></div>
				</div>
				<div class="company-info">
					<span class="address"> <!-- 地址：北京市朝阳区东三环北路38号院1号楼17层2001内1、16室 -->
					</span> <span class="phonenum"> <!-- 联系方式：400-810-1090（工作日10点-18点) -->
					</span>
				</div>
			</section>
		</div>
		<div class="bottom">
			<p>©2017 沪ICP备13040906号－5</p>
		</div>
	</footer>

	<script src="/courses/js/main.js"></script>
	<script type="text/javascript" src="/js/login2.js"></script>
	<script src="/courses/js/courseSeriesDetail.min.js"></script>
	<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script>
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
	
		//var id = $.getUrlParam('id');
		var video_src;
		var id = <%=courseId%>;
		$.post('/course/getSubcourseDetailById', {id : id}, function(data) {
			$('.course_title').html(data.title);
			$('.cost').html(data.cost);
			$('#time').html(data.time);
			$('#teacher_image').attr('href', '/cglx/files/imgs/' + data.final_image);
			$('#teacher').html(data.final_tea);
			$('#teacher_position').html(data.final_position);
			$('#teacher_abstract').html(data.final_abstract);
			$('#description').html(data.description);
			$('#outline').html(data.outline);
			$('#info').html(data.info);
			$('#crowds').html(data.crowds);
			$('#help').html(data.final_help);
			$('#about').html(data.final_about);
			$('.buy-status').html(data.pay_status == 1?'已购买':'未购买')
			$('#course_snapshot').attr('src', '/cglx/files/imgs/' + data.snapshot);
			if(data.pay_status == 1) {
				$('.my-btn').html('已购买').css('background-color', '#ccc');
				$('#video-snap').css('display', 'none');
				$('#video_src').css('display', 'block');
				document.getElementById("video_src").src = data.video_src;
				document.getElementById("video_src").poster = "/cglx/files/imgs/" + data.snapshot;
				//document.getElementById("video_src").controls = "controls";
				var playtime = data.playtime;
				var year = playtime.substring(0, 4);
				var month = playtime.substring(5, 7);
				var day = playtime.substring(8, 10);
				var hour = playtime.substring(11, 13);
				var minute = playtime.substring(14, 16);
				var second = playtime.substring(17, 19);
				
				var courseDate = new Date(year, month-1, day, hour, minute, second).getTime() / 1000;
				var currentDate = new Date().getTime() / 1000;
				var courseLength = parseInt(data.time) * 60;
				if(currentDate > courseDate) {
					document.getElementById("video_src").controls = "controls";
					document.getElementById("video_src").play();
				} else {
					alert('课程尚未开播，开播时间为：' + playtime);
				}
			}
			
		});
		
		$.post('/course/getParentCourseBrief', {id : id}, function(data) {
			if(!checkJsonIsEmpty(data)) {
				$('.parent_title').html(data.title);
				$('.price-and-len').html('<p>组合价: <span>¥' + data.total + '</span>(省' + data.discount + '元)</p>'
						+ '<p>共'+ data.sub_count + '节课 - ' + data.sub_time_total +'分钟</p>');
				
				$('.parent_href').attr('href', '/courses/jsp?id=' + data.id);
				$('#parent_snapshot').attr('src', '/cglx/files/imgs/' + data.snapshot);
			} else {
				$('.series-info').css('display', 'none');
				$('.series-recommend').css('display', 'none');
			} 
		});
		
		$.post('/course/getNavRecommendCourse', function(data) {
			for(var index in data) {
				if(data[index].is_series == 1) {
					$('#big').append('<div class="recommend-big"><a href="/courses/jsp?id='+data[index].id+'" target="_blank">' 
							+ '<img src="/cglx/files/imgs/'+data[index].snapshot+'" alt=""><div class="detail"><span class="left">'
							+ '共'+data[index].sub_count+'节课</span> <span class="right"><span class="spend-price">¥'+data[index].total+'</span>'
							+ '(省'+data[index].discount+'元) </span></div></a></div>');
					
					$('#recommend-big').append('<div class="item"><a href="/courses/jsp?id='+data[index].id+'"><div class="photo">'
							+ '<img	src="/cglx/files/imgs/'+data[index].snapshot+'"></div><div class="info"><span class="left">'
							+ '共'+data[index].sub_count+'节课</span> <span class="right"><span class="spend-price">¥'+data[index].total+'</span>'
							+ '(省'+data[index].discount+'元) </span></div></a></div>');						
				} else {
					
					$('#small').append('<div class="recommend-every-course"><a href="/courses/jsp?id='+data[index].id+'&view=detail" target="_blank">'
							+ '<div class="recommend-every-pic"><img src="/cglx/files/imgs/'+data[index].snapshot+'" alt=""></div>'
							+ '<div class="recommend-every-introduct"><div class="every-title">'+data[index].title+'</div>'
							+ '<div class="every-price-mobile"><span class="money">￥'+data[index].cost+'</span></div></div></a></div>');
					
					$('#recommend-small').append('<div class="every-course"><a href="/courses/jsp?id='+data[index].id+'&view=detail"><div class="info-left-recommond">'
							+ '<div class="video-title">'+data[index].title+'</div><div class="video-teacher">'+data[index].final_tea+'/'+data[index].final_position+'</div>'
							+ '</div><div class="photo-right"><span class="tags"></span> <img src="/cglx/files/imgs/'+data[index].snapshot+'" alt="">'
							+ '</div></a></div>');
				}
			}
		});
		
		function bindPayEvent(eleClass, arg) {
			$('.' + eleClass).on('click', function() {
				if(user_id == '') {
					if(window.screen.width < 700) {
						$('#login-btn-m').click();
		 			} else {
		 				$('#login-btn').click();
		 			}
				} else {
					if(arg == '已购买') return;
					if(isWeiXin()) {
						weixinPay();
					} else {
						if(window.screen.width < 700) {
							$('#alipay_btn_mobile').attr('href', '/pay?course_id=' + id);
							$('.modal-mask.mobile').css('display', 'block');
			 			} else {
							$('#alipay_btn').attr('href', '/pay?course_id=' + id);
			 				$('.modal-mask.pc').css('display', 'block');
			 			}
					}
				}
			});
		}
		
		bindPayEvent('my-btn', $('.my-btn').html());
		bindPayEvent('video-detail', null);
		
		$('.pay-fill-form-close').on('click', function() {
			$('.modal-mask').css('display', 'none');
		});
		
		$('.mobile-mycourse').on('click', function() {
			window.location.href = '/view/mycourse.html';
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
	</script>

</body>
</html>