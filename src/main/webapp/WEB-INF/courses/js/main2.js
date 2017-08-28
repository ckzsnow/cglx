var page = 1;
var length = 5;
function checkJsonIsEmpty(json) {
	var isEmpty = true;
	if (json == null) return true;
	for (var jsonKey in json) {
		isEmpty = false;
		break;
	}
	return isEmpty;
}
$('.login-false-wrapper').on('click', function() {
	$('.pc-login').css('display', 'block');
});

$('.login-close').on('click', function() {
	$('.pc-login').css('display', 'none');
	$('.mobile-login').css('display', 'none');
	$('.transparent-mask').css('display', 'none');
});

$('.news-nav li').each(function() {
	$(this).on('click', function() {
		/*if($(this).hasClass("main")) return;*/
		page = 1;
		$('.news-nav li').each(function() {
			$(this).removeClass('active');
			/*if(!$(this).hasClass("main")) {
			}*/
		});
		$(this).addClass('active');
		$("#articlelist").html("");
		var tag_id = $(this).attr("tag-id");
		var url = "/articles/getArticlesByTag";
		if(tag_id == 0) {
			url = "/articles/getAllArticlesForHomepage";
		}
		$("#articlelist").attr("current-tag-id", tag_id);
		$.ajax({
			url: url,
			type: "POST",
			data: {page:page,length:length,tag_id:tag_id},
			success: function(data) {
				if (!checkJsonIsEmpty(data)) {
					for(var index in data) {
						$("#articlelist").append("<li><a class='li-container' href='/view/subpage.html?id="+data[index].id+"&tagId="+data[index].tag_id+"' target='_blank'><div class='li-img'><img src='/cglx/files/imgs/"+data[index].image+"'></div><div class='li-content'><div class='li-title'>"+data[index].title+"</div><div class='li-detail'>"+data[index].abstract+"</div><div class='li-other'> <span class='li-type'>"+data[index].name+"</span> <span class='li-time'>"+data[index].readable_date+"</span> <span class='li-author'><i class='li-author-name'>"+data[index].author+"</i> </span></div> </div></a></li>");
					}
				}
			},
			error: function(status, error) {
			}
		});
	});
});

$('.mynav li').each(function() {
	$(this).on('click', function() {
		$('.mynav li').each(function() {
			$(this).removeClass('active');
		});
		$(this).addClass('active');
	});
});

$('.top-menu-expand-icon').on('click', function() {
	$('.transparent-mask').css('display', 'block');
});


$('.more-news').on('click', function() {
	 $(".more-news").hide();
	 $("#loading_tip").show();
	 var tag_id = $("#articlelist").attr("current-tag-id");
	 var url = "/articles/getArticlesByTag";
	 if(tag_id == 0) {
		url = "/articles/getAllArticlesForHomepage";
	 }
	 page = page + 1;
		$.ajax({
			url: url,
			type: "POST",
			data: {page:page,length:length,tag_id:tag_id},
			success: function(data) {
				if (!checkJsonIsEmpty(data)) {
					for(var index in data) {
						$("#articlelist").append("<li><a class='li-container' href='/view/subpage.html?id="+data[index].id+"&tagId="+data[index].tag_id+"' target='_blank'><div class='li-img'><img src='/cglx/files/imgs/"+data[index].image+"'></div><div class='li-content'><div class='li-title'>"+data[index].title+"</div><div class='li-detail'>"+data[index].abstract+"</div><div class='li-other'> <span class='li-type'>"+data[index].name+"</span> <span class='li-time'>"+data[index].readable_date+"</span> <span class='li-author'><i class='li-author-name'>"+data[index].author+"</i> </span></div> </div></a></li>");
					}
				}
				$("#loading_tip").hide();
				$(".more-news").show();
			},
			error: function(status, error) {
				$("#loading_tip").hide();
				$(".more-news").show();
			}
		});
});

function bindEvent() {
	var e = $("body"),
		t = $(".transparent-mask");
	$('.user-info').on('click', function() {
		e.removeClass('pusher');
		showLogin();
	});
	var t = $("body"),
		n = $(".header .transparent-mask");
	$(".top-menu-expand-icon").on("click", function() {
		t.hasClass("pusher") ? (t.removeClass("pusher"), n.hide()) : (t.addClass("pusher"), n.show())
	}), n.on("click", function() {
		return t.hasClass("pusher") && (t.removeClass("pusher"), $(this).hide()), !1
	})
}

function showLogin() {
	$('.mobile-login').css('display', 'block');
}

function touchInit() {
	bindEvent();
}

function isPC() {
	var sUserAgent = navigator.userAgent.toLowerCase();
    var bIsIpad = sUserAgent.match(/ipad/i) == "ipad";
    var bIsIphoneOs = sUserAgent.match(/iphone os/i) == "iphone os";
    var bIsMidp = sUserAgent.match(/midp/i) == "midp";
    var bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == "rv:1.2.3.4";
    var bIsUc = sUserAgent.match(/ucweb/i) == "ucweb";
    var bIsAndroid = sUserAgent.match(/android/i) == "android";
    var bIsCE = sUserAgent.match(/windows ce/i) == "windows ce";
    var bIsWM = sUserAgent.match(/windows mobile/i) == "windows mobile";
    if (!(bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM) ){
        return true;
    } else {
    	return false;
    }
}

function init() {
	isPC() ? ($("body").attr("device", "pc"), null) : ($("body").attr("device", "touch"), touchInit())
}
init();