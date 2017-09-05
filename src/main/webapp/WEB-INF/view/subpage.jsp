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
	Map<String, String> result = new HashMap<>();
	result = WeixinTools.getSign(
			"http://www.udiyclub.com/view/subpage?code=" + code + "&state=123");
	String subpageId = (String) session.getAttribute("subpage_id");
	String subpageTagId = (String) session.getAttribute("subpage_tagid");
%>
<!DOCTYPE html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="user-scalable=no,width=device-width,initial-scale=1.0">
		<meta name="keywords" content="留学、美国留学、英国留学、澳洲留学、加拿大留学、韩国留学、日本留学、法国留学、德国留学、欧洲留学、新西兰留学、香港留学、新加坡留学、留学申请、留学文书、个人陈述、留学简历、留学推荐信、DIY留学、留学中介">
		<meta name="description" content="DIY研习社是国内首家留学互助共享平台，致力于为所有留学申请者提供资源共享、交际交流的渠道和机会，社员和内容覆盖留学申请所有主流国家。">
		<title>DIY研习社－中国留学生互助交流平台，让留学不孤单</title>
		<link rel="icon" href="/images/favicon.ico" type="image/x-icon" />
		<link href="/css/newsDetail.min.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" href="/css/login.css">
		<script type="text/javascript" src="/js/jquery.min.js"></script>
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
					<a href="#"></a>
				</div>
				<nav class="PC-nav">
					<ul>
						<li class="news" data-id="news">
							<a href="homepage.html">留学专栏</a>
						</li>
						<li class="activity" data-id="activity">
							<a href="success.html">成功案例</a>
						</li>
						<li class="gift" data-id="gift">
							<a href="about.html">关于我们</a>
						</li>
						<li class="ventureservice" data-id="ventureservice">
							<a href="report.html">社员风采</a>
						</li>
						<li class="search" data-id="search">
							<a href="search.html">搜索</a>
						</li>
					</ul>
				</nav>
				<nav class="mobile-nav">
					<i class="top-menu-expand-icon"></i>
					<ul class="nav-list">
						<li class="news active" data-id="news">
							<a href="homepage.html">留学专栏</a>
						</li>
						<li class="course" data-id="course">
							<a href="success.html">成功案例</a>
						</li>
					</ul>

					<ul class="expand-nav-list" id="mobile_not_login">
						<li class="user-info">
							<a href="javascript:void(0)" id="login-btn-m">登录</a>
						</li>
						<li class="activity">
							<a href="success.html">成功案例</a>
						</li>
						<li class="search">
							<a href="report.html">社员风采</a>
						</li>
					</ul>
					
					<ul class="expand-nav-list" id="mobile_login" style="display:none;">
						<li class="activity">
							<a href="personalInfo.html" class="myorder-btn">个人信息</a>
						</li>
						<li class="search">
							<a href="estimate.html">背景评估</a>
						</li>
						<!-- <li class="search">
							<a href="collection.html">内容收藏</a>
						</li> -->
						<li class="search">
							<a href="javascript:userlogout();">退出</a>
						</li>
					</ul>
					
				</nav>
				<div class="login-container">
					<div class="logout" id="logout" style="display: none;height: 140px;">
						<i class="triangle"></i>
						<a href="personalInfo.html" class="myorder-btn">个人信息</a>
						<a href="estimate.html">背景评估</a>
						<!-- <a href="collection.html">内容收藏</a> -->
						<a href="javascript:userlogout();">退出</a>
					</div>
					<div class="login-false-wrapper">
						<i class="icon"></i>
						<a href="javascript:void(0)" id="login-btn">登录</a>
					</div>
					<div class="login-success-wrapper" style="display:none;">
						<div class="photo">
							<img src="/images/topic_photo.png">
						</div>
						<div class="name" id="user_id"></div>
					</div>
				</div>
			</div>
		</header>

		<div class="cglx-login-mask pc-login" style="display: none;position: fixed;z-index: 99999;background:rgba(56,62,68,.9)">
			<div class="login-dialog dialog" id="login_view">
				<div class="login-close close"></div>
				<div class="dialog-bg">
					<div class="dialog-title" style="background:url(/images/logo.png) no-repeat center; width:320px; height:45px"></div>
				</div>
				<div class="login-fill-card">
					<div class="login-card card">
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
								<span style="text-decoration: none;"><a href="javascript:$('#login_view').hide();$('#register_view').show();">注册账号</a></span>
								<span style="color:black;cursor: none;text-decoration: none;">|</span>
								<span style="text-decoration: none;"><a href="javascript:$('#login_view').hide();$('#forget_view').show();">忘记密码</a></span>
							</p>
						</div>
					</div>
				</div>
			</div>

			<div class="login-dialog dialog" id="register_view" style="display:none;height:330px;">
				<div class="login-close close"></div>
				<div class="dialog-bg">
					<div class="dialog-title" style="background:url(/images/logo.png) no-repeat center; width:320px; height:45px"></div>
				</div>
				<div class="login-fill-card">
					<div class="login-card card" style="height:240px;margin-top: -102px;">
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
								<span style="text-decoration: none;"><a href="javascript:$('#register_view').hide();$('#login_view').show();">登录账号</a></span>
								<span style="color:black;cursor: none;text-decoration: none;">|</span>
								<span style="text-decoration: none;"><a href="javascript:$('#register_view').hide();$('#forget_view').show();">忘记密码</a></span>
							</p>
						</div>
					</div>
				</div>
			</div>

			<div class="login-dialog dialog" id="forget_view" style="display:none;height:330px;">
				<div class="login-close close"></div>
				<div class="dialog-bg">
					<div class="dialog-title" style="background:url(/images/logo.png) no-repeat center; width:320px; height:45px"></div>
				</div>
				<div class="login-fill-card">
					<div class="login-card card" style="height:240px;margin-top: -102px;">
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
								<span style="text-decoration: none;"><a href="javascript:$('#forget_view').hide();$('#login_view').show();">登录账号</a></span>
								<span style="color:black;cursor: none;text-decoration: none;">|</span>
								<span style="text-decoration: none;"><a href="javascript:$('#forget_view').hide();$('#register_view').show();">注册账号</a></span>
							</p>
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
						<span style="text-decoration: none;"><a href="javascript:$('#mobile_login_view').hide();$('#mobile_register_view').show();">注册账号</a></span>
						<span style="color:black;cursor: none;text-decoration: none;">|</span>
						<span style="text-decoration: none;"><a href="javascript:$('#mobile_login_view').hide();$('#mobile_forget_view').show();">忘记密码</a></span>
					</p>
				</div>
			</div>

			<div class="mobile-dialog login-regist" style="display:none;height:780px;" id="mobile_register_view">
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
						<span style="text-decoration: none;"><a href="javascript:$('#mobile_register_view').hide();$('#mobile_login_view').show();">登录账号</a></span>
						<span style="color:black;cursor: none;text-decoration: none;">|</span>
						<span style="text-decoration: none;"><a href="javascript:$('#mobile_register_view').hide();$('#mobile_forget_view').show();">忘记密码</a></span>
					</p>
				</div>
			</div>

			<div class="mobile-dialog login-regist" style="display:none;height:780px;" id="mobile_forget_view">
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
					<p class="tips" id="mobile_forget_tip" ></p>
					<div class="login-submit submit" id="mobile_forget_submit">提交</div>
					<p class="voice-code" style="text-align: center;">
						<span style="text-decoration: none;"><a href="javascript:$('#mobile_forget_view').hide();$('#mobile_login_view').show();">登录账号</a></span>
						<span style="color:black;cursor: none;text-decoration: none;">|</span>
						<span style="text-decoration: none;"><a href="javascript:$('#mobile_forget_view').hide();$('#mobile_register_view').show();">注册账号</a></span>
					</p>
				</div>
			</div>
		</div>

		<div class="wrapper" id="wrapper">
			<div class="page-newsDetail">
				<div class="detail" id="newsDetail">
					<div class="detail-content">
						<h1 class="title" id="article_title"></h1>
						<div class="author-share">
							<div class="author">
								<span class="name_time">
                                <span class="author-name" id="article_author"></span>
								<span class="time" id="article_time"></span>
								</span>
								<span class="article-type" id="article_tag"></span>
							</div>
							<div class="wxApi">
								<!-- 新闻详情的title和photo -->
								<input type="hidden" name="article-title" id="article-title">
								<input type="hidden" name="article-intro" id="article-intro">
								<input type="hidden" name="article-photo" id="article-photo">
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
						<video id="video_src" width="100%" controls="controls">
						</video>
						<div id="article_detail" class="content-detail">
							
						</div>

						<div class="article_from">

							原文来源:<span class="from">DIY研习社</span> 作者:

							<span class="from" id="article_author_">姚猛</span>

						</div>

					</div>

					<div class="author-wrap">

						<div class="report">
							希望与他们分享自己的经历、经验和知识，请点击
							<a href="apply_report.html" target="_blank">我要分享</a>
						</div>
					</div>

					<div class="relative-articles">
						<h1>相关推荐</h1>
						<ul id="recommend_list" class="news-list">
						</ul>
					</div>
				</div>
				<div class="side" id="side">
					<div class="side-activities">
						<div class="title">成功案例
							<a href="success.html">更多</a>
						</div>
						<div class="activities-img">
							<a id="nav_recommend_href" target="_blank">
								<img id="nav_recommend_image">
							</a>
							<div id="nav_recommend_title" class="title-tips"></div>
						</div>
						<div class="activities-img" id="recommond2" style="margin-top:10px">
							<a id="nav_recommend_href2" target="_blank">
								<img id="nav_recommend_image2">
							</a>
							<div id="nav_recommend_title2" class="title-tips"></div>
						</div>
					</div>
					<div class="side-download">
						<div class="side-download-address">
							<div class="title">关注DIY研习社</div>
							<div class="zhihu item">
								<a href="#" target="_blank">DIY研习社</a>
							</div>
							<div class="weibo item">
								<a href="#" target="_blank">DIY研习社</a>
							</div>
							<div class="toutiao item">
								<a href="#" target="_blank">DIY研习社</a>
							</div>
						</div>
						<div class="side-qr">
							<span class="side-qr-img"></span>
							<span class="side-qr-title">扫码关注DIY研习社<br>微信公众平台<br>udiyclub</span>
						</div>
					</div>
					<div class="side-download">
						<div class="side-download-address">
							<div class="title">新建社群</div>
						<div class="side-qr" style="margin-top:21px">
							<span class="side-qr-img" style="background:url(/images/community2.jpeg) no-repeat;background-size: 100% 100%; width: 95px; height:125px"></span>
							<span class="side-qr-title">若扫码不成功<br>可加群秘书微信入群<br>udiy01</span>
						</div>
						</div>
						<div class="side-qr">
							<span class="side-qr-img" style="background:url(/images/community1.jpeg) no-repeat;background-size: 100% 100%; width: 95px; height:125px"></span>
							<span class="side-qr-title">若扫码不成功<br>可加群秘书微信入群<br>udiy02</span>
						</div>
					</div>
				</div>
			</div>
			<div class="loading"></div>
			<div class="mask">
				<div class="bigImg"></div>
			</div>
		</div>

		<footer class="footer-simple">
			<div class="section">
				<section class="link-section">
					<a href="apply_report.html"
						target="_blank">
						<div class="foot-logo"></div>
					</a>
					<div class="links">
						<a
							href="about.html"
							target="_blank"> </a> <a href="about.html">关于我们</a>
						| <a href="report.html">社员风采</a> <!-- | <a
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
						<span class="address"><!-- 地址：北京市朝阳区东三环北路38号院1号楼17层2001内1、16室 --></span> <span
							class="phonenum"><!-- 联系方式：400-810-1090（工作日10点-18点) --></span>
					</div>
				</section>
			</div>
			<div class="bottom">
				<p>©2017 沪ICP备15002235号-6</p>
			</div>
		</footer>
		<script type="text/javascript" src="/js/main.js"></script>
		<script type="text/javascript" src="/js/login.js"></script>
		<script src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
		<script type="text/javascript" src="/js/qrcode.min.js"></script>
		<script type="text/javascript">
		var subpageId = <%=subpageId%>;
		var subpageTagId = <%=subpageId%>;
		var imgUrl = "http://www.udiyclub.com/images/logo.png";
		var descContent = "DIY研习社－中国留学生互助交流平台，让留学不孤单";
		var shareTitle = "DIY研习社";
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
				var _con = $('#logout');
				$('#logout').hide(500);
			});
			(function ($) {
	            $.getUrlParam = function (name) {
	                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	                var r = window.location.search.substr(1).match(reg);
	                if (r != null) return unescape(r[2]); return null;
	            }
	        })(jQuery);
			var exculdeId = subpageId;
			var tagId = subpageTagId;
			var weixin_share_url = "http://www.udiyclub.com/view/subpagejsp?id="+subpageId+"&"+"tagId="+subpageTagId;
			var lineLink = weixin_share_url;
			var qrcode = new QRCode(document.getElementById("weixin_qrcode"), {
				text: weixin_share_url,
				width: 70,
				height: 70,
				colorDark : "#000000",
				colorLight : "#ffffff",
				correctLevel : QRCode.CorrectLevel.H
			});
			$.ajax({
				url: '/articles/getArticlesByTagExcludeId',
				type: "POST",
				data: {tag_id:tagId,exculde_id:exculdeId},
				success: function(data) {
					if (!checkJsonIsEmpty(data)) {
						for(var index in data) {
							$("#recommend_list").append("<li><a class='li-container' href='/view/subpage.html?id="+data[index].id+"&tagId="+data[index].tag_id+"' target='_blank'><div class='li-img'><img src='/cglx/files/imgs/"+data[index].image+"' style='width:100%;height:100%'></div><div class='li-content'><div class='li-title'>"+data[index].title+"</div><div class='li-detail'>"+data[index].abstract+"</div><div class='li-other'> <span class='li-type'>"+data[index].name+"</span> <span class='li-time'>"+data[index].readable_date+"</span> <span class='li-author'><i class='li-author-name'>"+data[index].author+"</i> </span></div> </div></a></li>");
						}
					}
				},
				error: function(status, error) {
				}
			});
			$.ajax({
				url: '/articles/getArticleDetail',
				type: "POST",
				data: {id:exculdeId},
				success: function(data) {
					if (!checkJsonIsEmpty(data)) {
						$("#article_title").html(data.title);
						$("#article_author").html(data.author);
						$("#article_time").html(data.readable_date);
						$("#article_tag").html(data.name);
						$("#article_author_").html(data.author);
						$("#article-title").html(data.title);
						
						$("#article-intro").html(data.abstract);
						$("#article-photo").html(data.snapshot);
						$("#article_detail").html(data.contents);
						if(data.video_src == undefined || data.video_src == '') {
							$('#video_src').css('display', 'none');
						} else {
							document.getElementById("video_src").src = data.video_src;
							document.getElementById("video_src").poster = "/cglx/files/imgs/" + data.image;
						}
						$("#share_weibo").attr("url", window.location.href);
						$("#share_weibo").attr("title", data.title);
						$("#share_weibo").attr("image", "/cglx/files/imgs/"+data.image);
						imgUrl = "http://www.udiyclub.com/cglx/files/imgs/"+data.image;
						shareTitle = data.title;
					}
					execWeixinShare();
				},
				error: function(status, error) {
				}
			});
			function callshare(){
				var shareTitle = $("#share_weibo").attr("title");
				var shareImg = $("#share_weibo").attr("image");
				var shareURL = $("#share_weibo").attr("url");
				window.open("http://v.t.sina.com.cn/share/share.php?title="+encodeURIComponent(shareTitle)+"&url="+encodeURIComponent(shareURL)+"&pic="+encodeURIComponent(shareImg)+"&source=bookmark","_blank");
			};
			$.ajax({
				url: '/cases/getNavRecommend',
				type: "POST",
				data: {},
				success: function(data) {
					if (!checkJsonIsEmpty(data)) {
						$("#nav_recommend_title").html(data[0].title);
						$("#nav_recommend_image").attr("src", "/cglx/files/imgs/" + data[0].image);
						$("#nav_recommend_href").attr("href", "item.html?id="+data[0].id); 
						
						if(data.length >= 2) {
							$("#nav_recommend_title2").html(data[1].title);
							$("#nav_recommend_image2").attr("src", "/cglx/files/imgs/" + data[1].image);
							$("#nav_recommend_href2").attr("href", "item.html?id="+data[1].id); 
						} else {
							$('#recommond2').css('display', 'none');
						}
					}
				},
				error: function(status, error) {
				}
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