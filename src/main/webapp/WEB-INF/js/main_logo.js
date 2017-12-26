(function($){
    "use strict";

    //投资机构列表
    var dataImage = [
        { name: "f_bai"},{ name: "f_bojiang"},{ name: "f_chuxin"},{ name: "f_chuangxin"},{ name: "f_dachen"},{ name: "f_detong"},{ name: "f_diedai"},{ name: "f_dingju"},{ name: "f_dongfang"},
        { name: "f_fenghou"},{ name: "f_fengrui"},{ name: "f_fosun"},{ name: "f_gaorong"},{ name: "f_guoke"},{ name: "f_haiquan"},{ name: "f_hakim"},{ name: "f_hongtai"},{ name: "f_hupanshan"},{ name: "f_huachuang"},{ name: "f_huarui"},
        { name: "f_huaying"},{ name: "f_huoshanshi"},{ name: "f_IDG"},{ name: "f_jishi"},{ name: "f_jiyuan"},{ name: "f_matrix"},{ name: "f_jiuhe"},{ name: "f_junshang"},{ name: "f_kpcb"},{ name: "f_bluelake"},{ name: "f_langmafeng"},{ name: "f_liyuan"},
        { name: "f_plum"},{ name: "f_niuxin"},{ name: "f_puhua"},{ name: "f_qifu"},{ name: "f_qiming"},{ name: "f_qingsong"},{ name: "f_rushan"},{ name: "f_saif"},{ name: "f_shanxing"},
        { name: "f_shengjing"},{ name: "f_shunwei"},{ name: "f_toutoushidao"},{ name: "f_xianxing"},{ name: "f_xintian"},{ name: "f_xiongmao"},{ name: "f_yinjiang"},{ name: "f_yintai"},{ name: "f_yinxinggu"},{ name: "f_yingnuo"},
        { name: "f_yuyue"},{ name: "f_yunjing"},{ name: "f_atom"},{ name: "f_yunqi"},{ name: "f_zhenfund"},{ name: "f_zhenshun"},{ name: "f_zhiyi"},{ name: "f_zihunchuangtou"},{ name: "f_mingdao"},{ name: "f_zhoumoqunaer"},{ name: "f_zhuimengwang"},{ name: "f_zuji"},{ name: "f_blank"}
    ]
    // 显示投资人列表
    var renderPartern = function(dataImage,col){
        // console.log(col);
        var slider = '<div class="swiper-slide"><div class="partern-item">';
        var html = slider;
        var cols = parseInt(col);
        $.each(dataImage,function(index,el){
            if(index > 0 && index % cols == (cols - 1) && index !== (dataImage.length-1)){
                html += '<a class="partner '+el.name+'" href="javascript:;"></a></div></div>'+slider;
            }else{
                html += '<a class="partner '+el.name+'" href="javascript:;"></a>'
            }
        });
        html += "</div></div>";
        return html;
    }
    var index = {
        init: function() {
            this.fixedHeader();
            this.sliderWrap();
            this.bindEvent();
            console.info('                        . .                       \n                      .     .                     \n                     .  ..  .                     \n                     .   . .                      \n                      .                           \n                     ........                     \n                ..................                \n              .......................             \n            ..........................            \n           .............................          \n          .............     .............         \n         ..............     ..............        \n        ................. .................       \n       ................     ................      \n      ..............           ..............     \n     .............               .............    \n     ............                 ............    \n     ............                 ............    \n     ............                 ............    \n     .............               ............     \n      .............             .............     \n       ..............         ..............      \n       ......................................     \n  ............................................... \n.........    .........................     .......\n ......      .....  ..... .....  .....      ..... \n             .....   .... ....   .....            \n             .....   .... ....   .....            \n               ..     ..   ...     .              \n\n           招前端开发，mailto: hr@welian.com');
        },
        bindEvent: function() {
            // 滚动条下滑，导航改变
            // $(window).on('scroll', function() {
            //     var yheight = window.pageYOffset; //滚动条距顶端的距离
            //     if (yheight > 0) {
            //         $('#J_header').addClass('header-fixed');
            //     } else {
            //         $('#J_header').removeClass('header-fixed');
            //     }
            // });
            //
            var video = document.getElementById('Video');
            video.controls = false;

            // 我是创业者
            $(document).on('mouseover', '.J_tab', function() {
                var self = $(this),
                    tab = self.data('tab');
                self.addClass('current').append('<em></em>')
                self.siblings().removeClass('current').find('em').remove();
                $('.J_tab_' + tab).show().siblings('.tab-content').hide();
            });

            // $(document).on('click', '.J_videoPlay', function(){
            //     var video = document.getElementById('Video');
            //     $('html').addClass('has-modal modal-open');
            //     video.currentTime = 0;
            //     video.play();
            // });
            $(document).on('click', '.J_videoPlay', function() {
                video.play();
                video.controls = true;
                $(this).fadeOut();
                $('.video-model').fadeOut();
            });

            $(document).on('click', '.J_closeModal', function(){
                // var video = $(this).parents().find('video');
                var video = document.getElementById('Video');
                video.currentTime = 0;
                video.paused ? '' : video.pause();
                $('html').removeClass('has-modal modal-open');
            });
            // tab转换
            $(".page2").on("click",".tab-title",function(){
                if(!$(this).hasClass('active')){
                    $(this).addClass('active');
                    $(this).siblings().removeClass('active');
                    var index = $(this).index();
                    console.log(index,$('.page2-content').eq(index));
                    $('.page2-content').children().eq(index).show().siblings().hide();
                }
            });
            //投资列表悬浮暂停
            $('.swiper-container').on('mouseleave','.swiper-slide',function(){
                mySwiper.startAutoplay();
            })
            $('.swiper-container').on('mouseenter','.swiper-slide',function(){
                mySwiper.stopAutoplay();
            })
            //窗口大小改变
            $(window).resize(index.sliderWrap);
            //滚动滚动条
            window.onscroll = index.fixedHeader;
        },
        fixedHeader : function(){
            var scrollTop = $(document).scrollTop();
            if(scrollTop <= 0){
                $(".header-index").removeClass('scroll')
            }else{
                $(".header-index").addClass('scroll')
            }
        },
        sliderWrap : function(){
            var w = $(document).width();
            var html = '';
            console.log('窗口大小改变',w)
            if(w > 1134){
                html = renderPartern(dataImage,28);
            }else if(w >972){
                html = renderPartern(dataImage,24);
            }else if(w>810){
                html = renderPartern(dataImage,20);
            }else{
                html = renderPartern(dataImage,16);
            }
            
            $(".page6").find(".swiper-wrapper").html(html);
            mySwiper.destroy(false);
            mySwiper = new Swiper('.swiper-container', {
                autoplay: 5000,//可选选项，自动滑动
                speed:500,
                pagination : '.swiper-pagination',
                paginationClickable: true,
                prevButton:'.swiper-button-prev',
                nextButton:'.swiper-button-next',
                loop: true,
                grabCursor:true
            });
            
        }
    };

    var mySwiper = new Swiper('.swiper-container', {
        autoplay: 5000,//可选选项，自动滑动
        speed:500,
        pagination : '.swiper-pagination',
        paginationClickable: true,
        prevButton:'.swiper-button-prev',
        nextButton:'.swiper-button-next',
        loop: true,
        grabCursor:true
    })

    // 初始化程序
    index.init();
})(jQuery);