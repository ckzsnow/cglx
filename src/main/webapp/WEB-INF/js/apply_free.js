var media = {
    	    roundOtherShow: false,
    	    needOtherShow: false,
    	    postData: {},
    	    init: function () {
    	        $('.form input[type=text], .form textarea, .form select').each(function (_, ele) {
    	            $(ele).on('focus', function () {
    	                $(ele).parent().removeClass('error');
    	                $('.tips').css('display','none');
    	            })
    	        })
    	    },
    	    validateForm: function () {
    	        var self = this;
    	        var flag = true;
    	        $('.form input[type=text], .form textarea, .form select').each(function (_, ele) {
    	        	if($(ele).attr('id') == 'language' || $(ele).attr('id') == 'standard_score') return;
    	            if (!$(ele).val()) {
    	                var name = $(ele).attr('id');
    	                $(ele).parent().addClass('error');
    	                flag = false;
    	            }
    	        });
    	        if (!flag) {
    	            $('.submit .tips').show();
    	            return;
    	        }
    	        this.addDataToPostData()
    	    },
    	    addDataToPostData: function () {
    	        var self = this,
    	            name
    	        $('.entrepreneur-form-items input[type=text]').each(function (_, ele) {
    	            name = $(ele).attr('id');
    	            if (name) {
    	                self.postData[name] = $(ele).val();
    	            }
    	        });
    	        console.log(self.postData);
    	        this.submitForm()
    	    },
    	    submitForm: function () {
    	    	if($('.btn-submit').html() == '正在提交中……') return;
    	        var self = this
    	        $('.btn-submit').html('正在提交中……')
    	        $('.btn-submit').attr('disabled', 'true');
    	        $.post('/applyFree/updateApplyFree', this.postData, function(data){
    	        	$('.btn-submit').html('提交')
    	        	if(data.error==0) {
	    	        	$('.submit-success').css('display', 'block');
	    	        	location.href="#success";  
    	        	} else {
    	        		$('.tips').html(data.msg);
    	        		$('.tips').css('display', 'block');
    	        		
    	        	}
    	        });
    	    }
    	}
        window.onload = function() {
            media.init()
        }
        $("#getVerifyCode").click(function() {
        	var phone = $('#phone').val();
        	if(!checkMobile(phone)) {
        		$('#phone').parent().addClass('error');
        		$('.tips').html('请填写正确的手机号！');
        		$('.tips').css('display', 'block');
        		return;
        	}
            var a, b;
            $(this).attr({
                disabled: "disabled"
            }),
            $(this).css("background", "rgb(204, 204, 204)"),
            a = 60,
            $("#getVerifyCode").html(a + "s 后重试"),
            b = setInterval(function() {
                a--,
                0 > a ? (clearInterval(b),
                $("#getVerifyCode").removeAttr("disabled"),
                $("#getVerifyCode").css("background", '#0697DA'),
                $("#getVerifyCode").html("发送验证码")) : $("#getVerifyCode").html(a + "s 后重试")
            }, 1e3);
            $.post('/user/userGetCheckCode',{user_id: $("#phone").val()},function(data) {
            	if(data.error != 0){
                	$('.tips').html(a.msg);
                	$('.tips').show();
                }
            });
        });
        
        function checkMobile(phone){ 
 			if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(phone))){
  				return false; 
 			} 
 			return true;
		} 