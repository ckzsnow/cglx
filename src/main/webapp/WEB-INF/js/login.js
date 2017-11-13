function isWeiXin(){ 
		var ua = window.navigator.userAgent.toLowerCase(); 
		if(ua.match(/MicroMessenger/i) == 'micromessenger'){ 
			return true; 
		}else{ 
			return false; 
		} 
	}
function userlogout() {
    $("#logout").hide(500),
    $(".login-success-wrapper").hide(),
    $("#user_id").html(""),
    $(".login-false-wrapper").show(),
    $(".header .transparent-mask").click(),
    $("#mobile_not_login").show(),
    $("#mobile_login").hide(),
    window.location.href = "/user/userLogout?view=homepage"
}
$.ajax({
    url: "/user/getUserInfo",
    type: "POST",
    data: {},
    success: function(data) {
    	if (!checkJsonIsEmpty(data)) {
			$("#user_id").html((data.name!=null&&data.name!='')?data.name:(data.openid!=null?data.openid:data.phone));
			user_id = data.id;
			$('#headimgurl').attr('src', data.headimage==null?'/images/topic_photo.png':data.headimage);
			$(".login-false-wrapper").hide();
			$(".login-success-wrapper").show();
			$("#mobile_not_login").hide();
			$("#mobile_login").show();
			
			if(isWeiXin()) {
				$.post('/user/checkSubscribe', function(data) {
					if(!checkJsonIsEmpty(data)) {
						var status = data.check_code;
						if(status == 0) {
							$('#focus_button').css('display', 'block');
						}
					}
				});
			}
			
		} else if(isWeiXin()){
			var url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf139053a88924f58&redirect_uri=http%3A%2F%2Fwww.udiyclub.com%2FgetOpenIdRedirect%3Fview=###########&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
			var searchStr = window.location.search;
			var pathname = window.location.pathname;
			var view= pathname.replace(new RegExp("/","gm"),"_");
			var args = searchStr.replace("?","ARGS").replace(new RegExp("&","gm"),"ARG");
			console.log(view+args);
			window.location.href = url.replace("###########",view+args);
		}
    }
}),
$("#login_submit").click(function() {
    var a, b;
    $("#login_tip").html(""),
    $("#login_tip").hide(),
    a = $("#login_phone").val(),
    b = $("#login_pwd").val(),
    $.ajax({
        url: "/user/userLogin",
        type: "POST",
        data: {
            user_id: a,
            user_pwd: b
        },
        success: function(a) {
            checkJsonIsEmpty(a) ? ($("#login_tip").html(""),
            $("#login_tip").show(),
            $("#login_tip").html("服务器暂时无法完成您的请求！")) : "0" != a.error ? ($("#login_tip").html(""),
            $("#login_tip").show(),
            $("#login_tip").html(a.error_msg)) : ("1" == a.user_type && (window.location.href = "/background/bannerlist.html"),
            $("#user_id").html(a.user_id),
            $(".pc-login").hide(),
            $(".login-false-wrapper").hide(),
            $(".login-success-wrapper").show())
        },
        error: function() {
            $("#login_tip").html(""),
            $("#login_tip").show(),
            $("#login_tip").html("服务器暂时无法完成您的请求！")
        }
    })
}),
$("#reg_submit").click(function() {
    var a, b, c;
    $("#reg_tip").html(""),
    $("#reg_tip").hide(),
    a = $("#reg_phone").val(),
    b = $("#reg_pwd").val(),
    c = $("#reg_verify").val(),
    $.ajax({
        url: "/user/userRegister",
        type: "POST",
        data: {
            user_id: a,
            user_pwd: b,
            check_code: c
        },
        success: function(a) {
            checkJsonIsEmpty(a) ? ($("#reg_tip").html(""),
            $("#reg_tip").show(),
            $("#reg_tip").html("服务器暂时无法完成您的请求！")) : "0" != a.error ? ($("#reg_tip").html(""),
            $("#reg_tip").show(),
            $("#reg_tip").html(a.msg)) : ($("#reg_tip").html(""),
            $("#reg_tip").show(),
            $("#reg_tip").html("注册成功，请登录！"))
        },
        error: function() {
            $("#login_tip").html(""),
            $("#login_tip").show(),
            $("#login_tip").html("服务器暂时无法完成您的请求！")
        }
    })
}),
$("#reg_get_code").click(function() {
    var a, b;
    $(this).attr({
        disabled: "disabled"
    }),
    $(this).attr("style", "background: rgb(204, 204, 204);"),
    a = 60,
    $("#reg_get_code").val(a + "s"),
    b = setInterval(function() {
        a--,
        0 > a ? (clearInterval(b),
        $("#reg_get_code").removeAttr("disabled"),
        $("#reg_get_code").removeAttr("style"),
        $("#reg_get_code").val("发送验证码")) : $("#reg_get_code").val(a + "s")
    }, 1e3),
    $.ajax({
        url: "/user/userGetCheckCode",
        type: "POST",
        data: {
            user_id: $("#reg_phone").val()
        },
        success: function(a) {
            checkJsonIsEmpty(a) ? ($("#reg_tip").html(""),
            $("#reg_tip").show(),
            $("#reg_tip").html("服务器暂时无法完成您的请求！")) : "0" != a.error && ($("#reg_tip").html(""),
            $("#reg_tip").show(),
            $("#reg_tip").html(""))
        },
        error: function() {
            $("#reg_tip").html(""),
            $("#reg_tip").show(),
            $("#reg_tip").html("服务器暂时无法完成您的请求！")
        }
    })
}),
$("#forget_submit").click(function() {
    var a, b, c;
    $("#forget_tip").html(""),
    $("#forget_tip").hide(),
    a = $("#forget_phone").val(),
    b = $("#forget_pwd").val(),
    c = $("#forget_verify").val(),
    $.ajax({
        url: "/user/userForgetPwd",
        type: "POST",
        data: {
            user_id: a,
            user_pwd: b,
            check_code: c
        },
        success: function(a) {
            checkJsonIsEmpty(a) ? ($("#forget_tip").html(""),
            $("#forget_tip").show(),
            $("#forget_tip").html("服务器暂时无法完成您的请求！")) : "0" != a.error ? ($("#forget_tip").html(""),
            $("#forget_tip").show(),
            $("#forget_tip").html(a.msg)) : ($("#forget_tip").html(""),
            $("#forget_tip").show(),
            $("#forget_tip").html("密码修改成功，请登录！"))
        },
        error: function() {
            $("#forget_tip").html(""),
            $("#forget_tip").show(),
            $("#forget_tip").html("服务器暂时无法完成您的请求！")
        }
    })
}),
$("#forget_get_code").click(function() {
    var a, b;
    $(this).attr({
        disabled: "disabled"
    }),
    $(this).attr("style", "background: rgb(204, 204, 204);"),
    a = 60,
    $("#forget_get_code").val(a + "s"),
    b = setInterval(function() {
        a--,
        0 > a ? (clearInterval(b),
        $("#forget_get_code").removeAttr("disabled"),
        $("#forget_get_code").removeAttr("style"),
        $("#forget_get_code").val("发送验证码")) : $("#forget_get_code").val(a + "s")
    }, 1e3),
    $.ajax({
        url: "/user/userGetCheckCode",
        type: "POST",
        data: {
            user_id: $("#forget_phone").val()
        },
        success: function(a) {
            checkJsonIsEmpty(a) ? ($("#forget_tip").html(""),
            $("#forget_tip").show(),
            $("#forget_tip").html("服务器暂时无法完成您的请求！")) : "0" != a.error && ($("#forget_tip").html(""),
            $("#forget_tip").show(),
            $("#forget_tip").html(""))
        },
        error: function() {
            $("#forget_tip").html(""),
            $("#forget_tip").show(),
            $("#forget_tip").html("服务器暂时无法完成您的请求！")
        }
    })
}),
$("#mobile_login_submit").click(function() {
    var a, b;
    $("#mobile_login_tip").html(""),
    $("#mobile_login_tip").hide(),
    a = $("#mobile_login_phone").val(),
    b = $("#mobile_login_pwd").val(),
    $.ajax({
        url: "/user/userLogin",
        type: "POST",
        data: {
            user_id: a,
            user_pwd: b
        },
        success: function(a) {
            checkJsonIsEmpty(a) ? ($("#mobile_login_tip").html(""),
            $("#mobile_login_tip").show(),
            $("#mobile_login_tip").html("服务器暂时无法完成您的请求！")) : "0" != a.error ? ($("#mobile_login_tip").html(""),
            $("#mobile_login_tip").show(),
            $("#mobile_login_tip").html(a.error_msg)) : ("1" == a.user_type && (window.location.href = "/background/bannerlist.html"),
            $(".mobile-login").css("display", "none"),
            $(".transparent-mask").css("display", "none"),
            $("#mobile_not_login").hide(),
            $("#mobile_login").show())
        },
        error: function() {
            $("#mobile_login_tip").html(""),
            $("#mobile_login_tip").show(),
            $("#mobile_login_tip").html("服务器暂时无法完成您的请求！")
        }
    })
}),
$("#mobile_reg_submit").click(function() {
    var a, b, c;
    $("#mobile_reg_tip").html(""),
    $("#mobile_reg_tip").hide(),
    a = $("#mobile_reg_phone").val(),
    b = $("#mobile_reg_pwd").val(),
    c = $("#mobile_reg_verify").val(),
    $.ajax({
        url: "/user/userRegister",
        type: "POST",
        data: {
            user_id: a,
            user_pwd: b,
            check_code: c
        },
        success: function(a) {
            checkJsonIsEmpty(a) ? ($("#mobile_reg_tip").html(""),
            $("#mobile_reg_tip").show(),
            $("#mobile_reg_tip").html("服务器暂时无法完成您的请求！")) : "0" != a.error ? ($("#mobile_reg_tip").html(""),
            $("#mobile_reg_tip").show(),
            $("#mobile_reg_tip").html(a.msg)) : ($("#mobile_reg_tip").html(""),
            $("#mobile_reg_tip").show(),
            $("#mobile_reg_tip").html("注册成功，请登录！"))
        },
        error: function() {
            $("#mobile_login_tip").html(""),
            $("#mobile_login_tip").show(),
            $("#mobile_login_tip").html("服务器暂时无法完成您的请求！")
        }
    })
}),
$("#mobile_reg_get_code").click(function() {
    var a, b;
    $(this).attr({
        disabled: "disabled"
    }),
    $(this).attr("style", "background: rgb(204, 204, 204);"),
    a = 60,
    $("#mobile_reg_get_code").html(a + "s"),
    b = setInterval(function() {
        a--,
        0 > a ? (clearInterval(b),
        $("#mobile_reg_get_code").removeAttr("disabled"),
        $("#mobile_reg_get_code").removeAttr("style"),
        $("#mobile_reg_get_code").html("发送验证码")) : $("#mobile_reg_get_code").html(a + "s")
    }, 1e3),
    $.ajax({
        url: "/user/userGetCheckCode",
        type: "POST",
        data: {
            user_id: $("#mobile_reg_phone").val()
        },
        success: function(a) {
            checkJsonIsEmpty(a) ? ($("#mobile_reg_tip").html(""),
            $("#mobile_reg_tip").show(),
            $("#mobile_reg_tip").html("服务器暂时无法完成您的请求！")) : "0" != a.error && ($("#mobile_reg_tip").html(""),
            $("#mobile_reg_tip").show(),
            $("#mobile_reg_tip").html(""))
        },
        error: function() {
            $("#mobile_reg_tip").html(""),
            $("#mobile_reg_tip").show(),
            $("#mobile_reg_tip").html("服务器暂时无法完成您的请求！")
        }
    })
}),
$("#mobile_forget_submit").click(function() {
    var a, b, c;
    $("#mobile_forget_tip").html(""),
    $("#mobile_forget_tip").hide(),
    a = $("#mobile_forget_phone").val(),
    b = $("#mobile_forget_pwd").val(),
    c = $("#mobile_forget_verify").val(),
    $.ajax({
        url: "/user/userForgetPwd",
        type: "POST",
        data: {
            user_id: a,
            user_pwd: b,
            check_code: c
        },
        success: function(a) {
            checkJsonIsEmpty(a) ? ($("#mobile_forget_tip").html(""),
            $("#mobile_forget_tip").show(),
            $("#mobile_forget_tip").html("服务器暂时无法完成您的请求！")) : "0" != a.error ? ($("#mobile_forget_tip").html(""),
            $("#mobile_forget_tip").show(),
            $("#mobile_forget_tip").html(a.msg)) : ($("#mobile_forget_tip").html(""),
            $("#mobile_forget_tip").show(),
            $("#mobile_forget_tip").html("密码修改成功，请登录！"))
        },
        error: function() {
            $("#mobile_forget_tip").html(""),
            $("#mobile_forget_tip").show(),
            $("#mobile_forget_tip").html("服务器暂时无法完成您的请求！")
        }
    })
}),
$("#mobile_forget_get_code").click(function() {
    var a, b;
    $(this).attr({
        disabled: "disabled"
    }),
    $(this).attr("style", "background: rgb(204, 204, 204);"),
    a = 60,
    $("#mobile_forget_get_code").html(a + "s"),
    b = setInterval(function() {
        a--,
        0 > a ? (clearInterval(b),
        $("#mobile_forget_get_code").removeAttr("disabled"),
        $("#mobile_forget_get_code").removeAttr("style"),
        $("#mobile_forget_get_code").html("发送验证码")) : $("#mobile_forget_get_code").html(a + "s")
    }, 1e3),
    $.ajax({
        url: "/user/userGetCheckCode",
        type: "POST",
        data: {
            user_id: $("#mobile_forget_phone").val()
        },
        success: function(a) {
            checkJsonIsEmpty(a) ? ($("#mobile_forget_tip").html(""),
            $("#mobile_forget_tip").show(),
            $("#mobile_forget_tip").html("服务器暂时无法完成您的请求！")) : "0" != a.error && ($("#mobile_forget_tip").html(""),
            $("#mobile_forget_tip").show(),
            $("#mobile_forget_tip").html(""))
        },
        error: function() {
            $("#mobile_forget_tip").html(""),
            $("#mobile_forget_tip").show(),
            $("#mobile_forget_tip").html("服务器暂时无法完成您的请求！")
        }
    })
});

$('.weixinLogin').on('click', function() {
	var url = window.location.href;
	url = url.substring(url.indexOf('//')+2);
	var redirect = url.substring(url.indexOf('/'));
	redirect = encodeURIComponent(redirect);
	window.location.href = ('/getQrcodeUrl?redirect=' + redirect);
	console.log('=======');
	return false;
});

$('.weixinLogin').mouseover(function() {
	$('.loginLogo').attr('src', '/images/login2.png');
});
$('.weixinLogin').mouseout(function() {
	$('.loginLogo').attr('src', '/images/login1.png');
});

(function() {
	$('#mobile_login').prepend('<li class="activity"><a href="/view/success.html" class="myorder-btn">成功案例</a></li>');
	if(isWeiXin()) {
		$($('#mobile_login').find('li')[3]).remove();
	}
})();
$('#focus_button').on('click', function() {
	$('#focus').css('display', 'block');
});
