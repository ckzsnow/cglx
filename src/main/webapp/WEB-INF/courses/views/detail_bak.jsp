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
					<li class="activity" data-id="activity"><a href="/view/success.html">成功案例</a></li>
					<li class="gift" data-id="gift"><a href="/view/about.html">关于我们</a>
					</li>
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
				<div class="my-course"><a href="/view/mycourse.html">我的课堂</a></div>
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
				<li class="my-course"><a href="/view/mycoures.html">我的课堂</a></li>
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
									<i>分享至：</i> 
									<a class="share_bg weixin weixin-top"></a> 
									<a class="share_bg sina" target="_blank" href="javascript:callshare()" id="share_weibo" data=""></a>
									<div class="panel-weixin panel-weixin-top" style="display: none;">
									<section class="weixin-section">
										<div id="weixin_qrcode" style="margin:10px;"></div>
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
					<video id="video_src" webkit-playsinline="true" playsinline="true" width="100%" height="85%" style="display:none;">
					</video>
					
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
							<span>关于DIY研习社在线课程</span>
						</div>
						<div class="content">
							<p id="about">
							DIY研习社在线课程是DIY研习社旗下的视频课程产品，旨在汇聚各大机构的优秀顾问和有成功经验的个人导师，为留学申请者提供实战的指导，帮大家更低成本、更高效率、更好体验的获取留学申请知识和方法。课程内容覆盖留学申请、出国语言、名企实习面试等，课程体验广受学生好评。
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
				
				<!-- 手机底部购买 -->
				<div class="price-pay-box" style="height: .6rem;background: #fff;padding: 0 15px;line-height: .6rem;position: fixed;z-index: 100;width: 100%;bottom: -15px;box-sizing: border-box;box-shadow: rgba(0,0,0,.1) 0 0 4px;border-top: 1px solid rgba(0,0,0,.1);">
	                <div class="price" style="float: left;color: #ff4c7c;font-size: .2rem;"><span>¥</span><span class="cost"></span></div>
	                <div class="join-button watch-video my-btn" style="font-size: .18rem; float: right;width: 1rem;height: .4rem;color: #fff;cursor: pointer;line-height: 40px;margin-top: .1rem;background: #f5a623;text-align: center;border-radius: 4px;">立即购买</div>
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

	<script src="/js/main.js"></script>
	<script type="text/javascript" src="/js/login2.js"></script>
	<script src="/courses/js/courseSeriesDetail.min.js"></script>
	<script type="text/javascript" src="/js/qrcode.min.js"></script>
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
		
		function callshare(){
			var shareTitle = $("#share_weibo").attr("title");
			var shareImg = $("#share_weibo").attr("image");
			var shareURL = $("#share_weibo").attr("url");
			window.open("http://v.t.sina.com.cn/share/share.php?title="+encodeURIComponent(shareTitle)+"&url="+encodeURIComponent(shareURL)+"&pic="+encodeURIComponent(shareImg)+"&source=bookmark","_blank");
		};
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
		var cost;
		var playVideoEvent = 1;
		var seekingVideoEvent = 0;
		var courseDate = 0;
		var currentDate = 0;
		var courseLength = 0;
		document.addEventListener("WeixinJSBridgeReady", function () {
			$.post('/course/getSubcourseDetailById', {id : id}, function(data) {
				if (!checkJsonIsEmpty(data)) {
					cost = data.cost;
					$('.course_title').html(data.title);
					$('.cost').html(data.fee);
					$('#time').html(data.time);
					$('#teacher_image').attr('src', '/cglx/files/imgs/' + data.final_image);
					$('#teacher').html(data.final_tea);
					$('#teacher_position').html(data.final_position);
					
					var final_abstract = data.final_abstract;
					final_abstract = final_abstract.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
					$('#teacher_abstract').html(final_abstract);
					
					var description = data.description;
					//description = description.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
					$('#description').html(description);
					
					var outline = data.outline;
					outline = outline.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
					$('#outline').html(outline);
					
					var info = data.info;
					info = info.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
					$('#info').html(info);
					
					var crowds = data.crowds;
					crowds = crowds.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
					$('#crowds').html(crowds);
					
					var help = data.final_help;
					help = help.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;')
					$('#help').html(help);
					
					//$('#about').html(data.final_about);
					$('.buy-status').html(data.pay_status == 1?'已购买':'未购买')
					$('#course_snapshot').attr('src', '/cglx/files/imgs/' + data.snapshot);
					if(data.pay_status == 1) {
						$('.my-btn').html('已购买').css('background-color', '#ccc');
						$('#video-snap').css('display', 'none');
						$('#video_src').css('display', 'block');
						document.getElementById("video_src").poster = "/cglx/files/imgs/" + data.snapshot;
						var playtime = data.playtime;
						var str =playtime;
						str = str.replace(/-/g,"/");
						courseDate = new Date(str).getTime() / 1000;
						currentDate = new Date().getTime() / 1000;
						courseLength = parseInt(data.time) * 60;
						if(currentDate < courseDate) {
							alert('课程尚未开播，开播时间为：' + playtime);
							var interval = window.setInterval(function(){
								currentDate = new Date().getTime() / 1000;
								if(currentDate >= courseDate) {
									document.getElementById("video_src").src = data.video_src;
									document.getElementById("video_src").controls = "controls";
									document.getElementById("video_src").poster = "/cglx/files/imgs/" + data.snapshot;
									clearInterval(interval);
								} 
							},3000);
						} else {
							document.getElementById("video_src").src = data.video_src;
						}
						document.getElementById("video_src").addEventListener('timeupdate', function(){
							if(document.getElementById("video_src").currentTime != 0 && playVideoEvent == 1) {
								playVideoEvent = 2;
								document.getElementById("video_src").pause();
								courseDate = new Date(str).getTime() / 1000;
								if(currentDate - courseDate < courseLength) {
									document.getElementById("video_src").currentTime = currentDate - courseDate;
								}
								document.getElementById("video_src").play();
							}
						});
						var slideTime = true;
						document.getElementById("video_src").addEventListener('seeking', function(){
							if(playVideoEvent == 2 && seekingVideoEvent == 0) {
								seekingVideoEvent = 1;
								return;
							}
							if(!slideTime) {
								seekingVideoEvent = 1;
								slideTime = true;
								return;
							}
							if(seekingVideoEvent == 2) {
								seekingVideoEvent = 0;
								courseDate = new Date(str).getTime() / 1000;
								if(currentDate - courseDate < courseLength) {
									slideTime = false;
									document.getElementById("video_src").currentTime = currentDate - courseDate;
								}
							}
							seekingVideoEvent++;
						});
						
					}
					imgUrl = "http://www.udiyclub.com/cglx/files/imgs/"+data.snapshot;
					shareTitle = data.title;
					execWeixinShare();
				}
			});
		});
		if(!isWeiXin()) {
			$.post('/course/getSubcourseDetailById', {id : id}, function(data) {
				if (!checkJsonIsEmpty(data)) {
					cost = data.cost;
					$('.course_title').html(data.title);
					
					/* var rebate = data.rebate / 10;
					var starttime = data.starttime;
					var deadline = data.deadline;
					var cost = data.cost;
					var starttimeDate = new Date(starttime).getTime() / 1000;
					var deadlineDate = new Date(deadline).getTime() / 1000;
					var currentDate = new Date().getTime() / 1000;
					if(starttime!=null && deadline!=null) {
						if(deadlineDate>currentDate && currentDate>starttimeDate) {
							$('.cost').html(rebate*cost);
						}
					} else {
						$('.cost').html(data.cost);
					} */
					$('.cost').html(data.fee);
					$('#time').html(data.time);
					$('#teacher_image').attr('src', '/cglx/files/imgs/' + data.final_image);
					$('#teacher').html(data.final_tea);
					$('#teacher_position').html(data.final_position);
					
					var final_abstract = data.final_abstract;
					final_abstract = final_abstract.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
					$('#teacher_abstract').html(final_abstract);
					
					var description = data.description;
					//description = description.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
					$('#description').html(description);
					
					var outline = data.outline;
					outline = outline.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
					$('#outline').html(outline);
					
					var info = data.info;
					info = info.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
					$('#info').html(info);
					
					var crowds = data.crowds;
					crowds = crowds.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
					$('#crowds').html(crowds);
					
					var help = data.final_help;
					help = help.replace(/_@/g, '<br/>').replace(/_#/g, '<br/>').replace(/\s/g, '&nbsp;');
					$('#help').html(help);
					
					//$('#about').html(data.final_about);
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
						var str =playtime;
						str = str.replace(/-/g,"/");
						var courseDate = new Date(str).getTime() / 1000;
						var currentDate = new Date().getTime() / 1000;
						var courseLength = parseInt(data.time) * 60;
						if(currentDate > courseDate && currentDate-courseDate>courseLength) {
							document.getElementById("video_src").controls = "controls";
							document.getElementById("video_src").play();
						} else if(currentDate > courseDate && currentDate-courseDate<courseLength) {
							document.getElementById("video_src").currentTime = currentDate-courseDate;
							document.getElementById("video_src").play();
						} else {
							alert('课程尚未开播，开播时间为：' + playtime);
							var interval = window.setInterval(function(){
								currentDate = new Date().getTime() / 1000;
								if(currentDate > courseDate && currentDate-courseDate>courseLength) {
									document.getElementById("video_src").controls = "controls";
									document.getElementById("video_src").play();
									clearInterval(interval); 
								} else if(currentDate > courseDate && currentDate-courseDate<courseLength) {
									document.getElementById("video_src").currentTime = currentDate-courseDate;
									document.getElementById("video_src").play();
									clearInterval(interval); 
								} 
							},5000);
						}
					}
					imgUrl = "http://www.udiyclub.com/cglx/files/imgs/"+data.snapshot;
					shareTitle = data.title;
					execWeixinShare();
				}
			});
		}
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
		
		bindPayEvent('my-btn', $('.my-btn').html());
		bindPayEvent('video-detail', null);
		$.post('/course/getParentCourseBrief', {id : id}, function(data) {
			if(!checkJsonIsEmpty(data)) {
				$('.parent_title').html(data.title);
				$('.price-and-len').html('<p>组合价: <span>¥' + data.total + '</span>(省' + data.discount + '元)</p>'
						+ '<p>共'+ data.sub_count + '节课 - ' + data.sub_time_total +'分钟</p>');
				
				$('.parent_href').attr('href', '/courses/jsp?id=' + data.parent_id);
				$('#parent_snapshot').attr('src', '/cglx/files/imgs/' + data.snapshot);
			} else {
				$('.series-info').css('display', 'none');
				$('.series-recommend').css('display', 'none');
			} 
		});
		
		$.post('/course/getNavRecommendCourse', function(data) {
			for(var index in data) {
				if(data[index].is_series == 1) {
					//var link = !isWeiXin() ? ('/courses/jsp?id='+data[index].id) : ('https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf139053a88924f58&redirect_uri=http%3A%2F%2Fwww.udiyclub.com%2FgetOpenIdRedirect%3Fview=_courses_jspARGSid='+data[index].id+'&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect');
					var link = '/courses/jsp?id='+data[index].id;
					$('#big').append('<div class="recommend-big"><a href="'+link+'" target="_blank">' 
							+ '<img src="/cglx/files/imgs/'+data[index].snapshot+'" alt=""><div class="detail"><span class="left">'
							+ '共'+data[index].sub_count+'节课</span> <span class="right"><span class="spend-price">¥'+data[index].total+'</span>'
							+ '(省'+data[index].discount+'元) </span></div></a></div>');
					
					$('#recommend-big').append('<div class="item"><a href="'+link+'"><div class="photo">'
							+ '<img	src="/cglx/files/imgs/'+data[index].snapshot+'"></div><div class="info"><span class="left">'
							+ '共'+data[index].sub_count+'节课</span> <span class="right"><span class="spend-price">¥'+data[index].total+'</span>'
							+ '(省'+data[index].discount+'元) </span></div></a></div>');						
				} else {
					//var link = !isWeiXin() ? ('/courses/jsp?id='+data[index].id+'&view=detail') : ('https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf139053a88924f58&redirect_uri=http%3A%2F%2Fwww.udiyclub.com%2FgetOpenIdRedirect%3Fview=_courses_jspARGSid='+data[index].id+'ANDview=detail&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect');
					var link = '/courses/jsp?id='+data[index].id+'&view=detail';
					$('#small').append('<div class="recommend-every-course"><a href="'+link+'" target="_blank">'
							+ '<div class="recommend-every-pic"><img src="/cglx/files/imgs/'+data[index].snapshot+'" alt=""></div>'
							+ '<div class="recommend-every-introduct"><div class="every-title">'+data[index].title+'</div>'
							+ '<div class="every-price-mobile"><span class="money">￥'+data[index].cost_+'</span></div></div></a></div>');
					
					$('#recommend-small').append('<div class="every-course"><a href="'+link+'"><div class="info-left-recommond">'
							+ '<div class="video-title">'+data[index].title+'</div><div class="video-teacher">'+data[index].final_tea+'/'+data[index].final_position+'</div>'
							+ '</div><div class="photo-right"><span class="tags"></span> <img src="/cglx/files/imgs/'+data[index].snapshot+'" alt="">'
							+ '</div></a></div>');
				}
			}
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
	    		            	/* window.location.href = '/view/mycourse.html'; */
	    		            	window.location.reload();
	    		            	if(res.errMsg != null && res.errMsg == "chooseWXPay:ok") {
	                				ele.innerHTML = "点击进入";
	                				ele.removeEventListener('tap', handler);
	                				ele.addEventListener('tap', enterClass);
	    		            		alert("支付成功!");
	    		            		window.location.reload();
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
		
		var weixin_share_url = "http://www.udiyclub.com/courses/jsp?id=" + id + "&view=detail";
		var qrcode = new QRCode(document.getElementById("weixin_qrcode"), {
			text: weixin_share_url,
			width: 70,
			height: 70,
			colorDark : "#000000",
			colorLight : "#ffffff",
			correctLevel : QRCode.CorrectLevel.H
		});
		
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