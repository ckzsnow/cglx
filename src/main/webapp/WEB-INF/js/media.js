var media = {
    roundOtherShow: false,
    needOtherShow: false,
    postData: {},
    init: function () {
        $('.form input[type=text], .form textarea, .form select').each(function (_, ele) {
            $(ele).on('focus', function () {
                $(ele).parent().removeClass('error')
            })
        })
    },
    validateForm: function () {
        var self = this
        var flag = true
        $('.form input[type=text], .form textarea, .form select').each(function (_, ele) {
            if (!$(ele).val()) {
                var name = $(ele).attr('name')
                $(ele).parent().addClass('error');
                flag = false;
            }
        })
        if (!flag) {
            $('.submit .tips').show()
            return
        }
        this.addDataToPostData()
    },
    addDataToPostData: function () {
        var self = this,
            name
        $('.entrepreneur-form-items input[type=text]').each(function (_, ele) {
            name = $(ele).attr('name')
            if (name) {
                self.postData[name] = $(ele).val()
            }
        })
        this.submitForm()
    },
    submitForm: function () {
        var self = this
        $('.btn-submit').html('正在提交中……').off()
        $.ajax({
            url: '/applyReport/updateApplyReport',
            type: 'post',
            data: this.postData,
            success: function (res) {
                if (res.error == "0") {
                    $('.form').addClass('hide')
                    $('.intro').addClass('hide')
                    $('.submit-success').removeClass('hide')
                }
            },
            error: function (status, error) {
                $('.btn-submit').html('提交失败，请点击重新提交')
            }
        })
    }
}