var user_id = '';

$.ajax({
	url: '/user/getUserInfo',
	type: "POST",
	data: {},
	success: function(data) {
		if (!checkJsonIsEmpty(data)) {
			$("#user_id").html(data.phone);
			user_id = data.id;
			$(".login-false-wrapper").hide();
			$(".login-success-wrapper").show();
			$("#mobile_not_login").hide();
			$("#mobile_login").show();
		}
	}
});
$("#login_submit").click(function(){
	$("#login_tip").html("");
	$("#login_tip").hide();
	var phone = $("#login_phone").val();
	var pwd = $("#login_pwd").val();
	$.ajax({
		url: '/user/userLogin',
		type: "POST",
		data: {user_id:phone,user_pwd:pwd},
		success: function(data) {
			if (!checkJsonIsEmpty(data)) {
				if(data.error != "0") {
					$("#login_tip").html("");
					$("#login_tip").show();
					$("#login_tip").html(data.error_msg);
				} else {
					if(data.user_type == "1") window.location.href="/background/bannerlist.html";
					user_id = data.id;
					$("#user_id").html(data.user_id);
					$(".pc-login").hide();
					$(".login-false-wrapper").hide();
					$(".login-success-wrapper").show();
					window.location.reload();
				}
			} else {
				$("#login_tip").html("");
				$("#login_tip").show();
				$("#login_tip").html("服务器暂时无法完成您的请求！");
			}
		},
		error: function(status, error) {
			$("#login_tip").html("");
			$("#login_tip").show();
			$("#login_tip").html("服务器暂时无法完成您的请求！");
		}
	});
});

$("#reg_submit").click(function(){
	$("#reg_tip").html("");
	$("#reg_tip").hide();
	var phone = $("#reg_phone").val();
	var pwd = $("#reg_pwd").val();
	var check_code = $("#reg_verify").val();
	$.ajax({
		url: '/user/userRegister',
		type: "POST",
		data: {user_id:phone,user_pwd:pwd,check_code:check_code},
		success: function(data) {
			if (!checkJsonIsEmpty(data)) {
				if(data.error != "0") {
					$("#reg_tip").html("");
					$("#reg_tip").show();
					$("#reg_tip").html(data.msg);
				} else {
					$("#reg_tip").html("");
					$("#reg_tip").show();
					$("#reg_tip").html("注册成功，请登录！");
				}
			} else {
				$("#reg_tip").html("");
				$("#reg_tip").show();
				$("#reg_tip").html("服务器暂时无法完成您的请求！");
			}
		},
		error: function(status, error) {
			$("#login_tip").html("");
			$("#login_tip").show();
			$("#login_tip").html("服务器暂时无法完成您的请求！");
		}
	});
});

$("#reg_get_code").click(function(){
	$(this).attr({"disabled":"disabled"});
	$(this).attr("style", "background: rgb(204, 204, 204);");
	var j = 60;
	$("#reg_get_code").val(j + "s");
	var i = setInterval(function(){
		j--;
		if(j<0) {
			clearInterval(i);
			$("#reg_get_code").removeAttr("disabled");
			$("#reg_get_code").removeAttr("style");
			$("#reg_get_code").val("发送验证码");
		} else {
			$("#reg_get_code").val(j + "s");
		}
	}, 1000);
	$.ajax({
		url: '/user/userGetCheckCode',
		type: "POST",
		data: {user_id:$("#reg_phone").val()},
		success: function(data) {
			if (!checkJsonIsEmpty(data)) {
				if(data.error != "0") {
					$("#reg_tip").html("");
					$("#reg_tip").show();
					$("#reg_tip").html("");
				}
			} else {
				$("#reg_tip").html("");
				$("#reg_tip").show();
				$("#reg_tip").html("服务器暂时无法完成您的请求！");
			}
		},
		error: function(status, error) {
			$("#reg_tip").html("");
			$("#reg_tip").show();
			$("#reg_tip").html("服务器暂时无法完成您的请求！");
		}
	});
});

$("#forget_submit").click(function(){
	$("#forget_tip").html("");
	$("#forget_tip").hide();
	var phone = $("#forget_phone").val();
	var pwd = $("#forget_pwd").val();
	var check_code = $("#forget_verify").val();
	$.ajax({
		url: '/user/userForgetPwd',
		type: "POST",
		data: {user_id:phone,user_pwd:pwd,check_code:check_code},
		success: function(data) {
			if (!checkJsonIsEmpty(data)) {
				if(data.error != "0") {
					$("#forget_tip").html("");
					$("#forget_tip").show();
					$("#forget_tip").html(data.msg);
				} else {
					$("#forget_tip").html("");
					$("#forget_tip").show();
					$("#forget_tip").html("密码修改成功，请登录！");
				}
			} else {
				$("#forget_tip").html("");
				$("#forget_tip").show();
				$("#forget_tip").html("服务器暂时无法完成您的请求！");
			}
		},
		error: function(status, error) {
			$("#forget_tip").html("");
			$("#forget_tip").show();
			$("#forget_tip").html("服务器暂时无法完成您的请求！");
		}
	});
});

$("#forget_get_code").click(function(){
	$(this).attr({"disabled":"disabled"});
	$(this).attr("style", "background: rgb(204, 204, 204);");
	var j = 60;
	$("#forget_get_code").val(j + "s");
	var i = setInterval(function(){
		j--;
		if(j<0) {
			clearInterval(i);
			$("#forget_get_code").removeAttr("disabled");
			$("#forget_get_code").removeAttr("style");
			$("#forget_get_code").val("发送验证码");
		} else {
			$("#forget_get_code").val(j + "s");
		}
	}, 1000);
	$.ajax({
		url: '/user/userGetCheckCode',
		type: "POST",
		data: {user_id:$("#forget_phone").val()},
		success: function(data) {
			if (!checkJsonIsEmpty(data)) {
				if(data.error != "0") {
					$("#forget_tip").html("");
					$("#forget_tip").show();
					$("#forget_tip").html("");
				}
			} else {
				$("#forget_tip").html("");
				$("#forget_tip").show();
				$("#forget_tip").html("服务器暂时无法完成您的请求！");
			}
		},
		error: function(status, error) {
			$("#forget_tip").html("");
			$("#forget_tip").show();
			$("#forget_tip").html("服务器暂时无法完成您的请求！");
		}
	});
});

$("#mobile_login_submit").click(function(){
	$("#mobile_login_tip").html("");
	$("#mobile_login_tip").hide();
	var phone = $("#mobile_login_phone").val();
	var pwd = $("#mobile_login_pwd").val();
	$.ajax({
		url: '/user/userLogin',
		type: "POST",
		data: {user_id:phone,user_pwd:pwd},
		success: function(data) {
			if (!checkJsonIsEmpty(data)) {
				if(data.error != "0") {
					$("#mobile_login_tip").html("");
					$("#mobile_login_tip").show();
					$("#mobile_login_tip").html(data.error_msg);
				} else {
					if(data.user_type == "1") window.location.href="/background/bannerlist.html";
					user_id = data.id;
					$('.mobile-login').css('display', 'none');
					$('.transparent-mask').css('display', 'none');
					$("#mobile_not_login").hide();
					$("#mobile_login").show();
					window.location.reload();
				}
			} else {
				$("#mobile_login_tip").html("");
				$("#mobile_login_tip").show();
				$("#mobile_login_tip").html("服务器暂时无法完成您的请求！");
			}
		},
		error: function(status, error) {
			$("#mobile_login_tip").html("");
			$("#mobile_login_tip").show();
			$("#mobile_login_tip").html("服务器暂时无法完成您的请求！");
		}
	});
});

$("#mobile_reg_submit").click(function(){
	$("#mobile_reg_tip").html("");
	$("#mobile_reg_tip").hide();
	var phone = $("#mobile_reg_phone").val();
	var pwd = $("#mobile_reg_pwd").val();
	var check_code = $("#mobile_reg_verify").val();
	$.ajax({
		url: '/user/userRegister',
		type: "POST",
		data: {user_id:phone,user_pwd:pwd,check_code:check_code},
		success: function(data) {
			if (!checkJsonIsEmpty(data)) {
				if(data.error != "0") {
					$("#mobile_reg_tip").html("");
					$("#mobile_reg_tip").show();
					$("#mobile_reg_tip").html(data.msg);
				} else {
					$("#mobile_reg_tip").html("");
					$("#mobile_reg_tip").show();
					$("#mobile_reg_tip").html("注册成功，请登录！");
				}
			} else {
				$("#mobile_reg_tip").html("");
				$("#mobile_reg_tip").show();
				$("#mobile_reg_tip").html("服务器暂时无法完成您的请求！");
			}
		},
		error: function(status, error) {
			$("#mobile_login_tip").html("");
			$("#mobile_login_tip").show();
			$("#mobile_login_tip").html("服务器暂时无法完成您的请求！");
		}
	});
});

$("#mobile_reg_get_code").click(function(){
	$(this).attr({"disabled":"disabled"});
	$(this).attr("style", "background: rgb(204, 204, 204);");
	var j = 60;
	$("#mobile_reg_get_code").html(j + "s");
	var i = setInterval(function(){
		j--;
		if(j<0) {
			clearInterval(i);
			$("#mobile_reg_get_code").removeAttr("disabled");
			$("#mobile_reg_get_code").removeAttr("style");
			$("#mobile_reg_get_code").html("发送验证码");
		} else {
			$("#mobile_reg_get_code").html(j + "s");
		}
	}, 1000);
	$.ajax({
		url: '/user/userGetCheckCode',
		type: "POST",
		data: {user_id:$("#mobile_reg_phone").val()},
		success: function(data) {
			if (!checkJsonIsEmpty(data)) {
				if(data.error != "0") {
					$("#mobile_reg_tip").html("");
					$("#mobile_reg_tip").show();
					$("#mobile_reg_tip").html("");
				}
			} else {
				$("#mobile_reg_tip").html("");
				$("#mobile_reg_tip").show();
				$("#mobile_reg_tip").html("服务器暂时无法完成您的请求！");
			}
		},
		error: function(status, error) {
			$("#mobile_reg_tip").html("");
			$("#mobile_reg_tip").show();
			$("#mobile_reg_tip").html("服务器暂时无法完成您的请求！");
		}
	});
});

$("#mobile_forget_submit").click(function(){
	$("#mobile_forget_tip").html("");
	$("#mobile_forget_tip").hide();
	var phone = $("#mobile_forget_phone").val();
	var pwd = $("#mobile_forget_pwd").val();
	var check_code = $("#mobile_forget_verify").val();
	$.ajax({
		url: '/user/userForgetPwd',
		type: "POST",
		data: {user_id:phone,user_pwd:pwd,check_code:check_code},
		success: function(data) {
			if (!checkJsonIsEmpty(data)) {
				if(data.error != "0") {
					$("#mobile_forget_tip").html("");
					$("#mobile_forget_tip").show();
					$("#mobile_forget_tip").html(data.msg);
				} else {
					$("#mobile_forget_tip").html("");
					$("#mobile_forget_tip").show();
					$("#mobile_forget_tip").html("密码修改成功，请登录！");
				}
			} else {
				$("#mobile_forget_tip").html("");
				$("#mobile_forget_tip").show();
				$("#mobile_forget_tip").html("服务器暂时无法完成您的请求！");
			}
		},
		error: function(status, error) {
			$("#mobile_forget_tip").html("");
			$("#mobile_forget_tip").show();
			$("#mobile_forget_tip").html("服务器暂时无法完成您的请求！");
		}
	});
});

$("#mobile_forget_get_code").click(function(){
	$(this).attr({"disabled":"disabled"});
	$(this).attr("style", "background: rgb(204, 204, 204);");
	var j = 60;
	$("#mobile_forget_get_code").html(j + "s");
	var i = setInterval(function(){
		j--;
		if(j<0) {
			clearInterval(i);
			$("#mobile_forget_get_code").removeAttr("disabled");
			$("#mobile_forget_get_code").removeAttr("style");
			$("#mobile_forget_get_code").html("发送验证码");
		} else {
			$("#mobile_forget_get_code").html(j + "s");
		}
	}, 1000);
	$.ajax({
		url: '/user/userGetCheckCode',
		type: "POST",
		data: {user_id:$("#mobile_forget_phone").val()},
		success: function(data) {
			if (!checkJsonIsEmpty(data)) {
				if(data.error != "0") {
					$("#mobile_forget_tip").html("");
					$("#mobile_forget_tip").show();
					$("#mobile_forget_tip").html("");
				}
			} else {
				$("#mobile_forget_tip").html("");
				$("#mobile_forget_tip").show();
				$("#mobile_forget_tip").html("服务器暂时无法完成您的请求！");
			}
		},
		error: function(status, error) {
			$("#mobile_forget_tip").html("");
			$("#mobile_forget_tip").show();
			$("#mobile_forget_tip").html("服务器暂时无法完成您的请求！");
		}
	});
});

function userlogout() {
	$('#logout').hide(500);
	$(".login-success-wrapper").hide();
	$("#user_id").html("");
	$(".login-false-wrapper").show();
	$(".header .transparent-mask").click();
	$("#mobile_not_login").show();
	$("#mobile_login").hide();
	/*$.ajax({
		url: '/user/userLogoutAjax',
		type: "POST",
		data: {},
		success: function(data) {},
		error: function(status, error){}
	});*/
	window.location.href = '/user/userLogout?view=homepage';
}