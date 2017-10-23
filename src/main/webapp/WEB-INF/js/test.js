function getScrollTop() {
	var scrollTop = 0;
	if (document.documentElement && document.documentElement.scrollTop) {
		scrollTop = document.documentElement.scrollTop;
	} else if (document.body) {
		scrollTop = document.body.scrollTop;
	}
	return scrollTop;
};
function offset(curEle) {
	var totalLeft = null, totalTop = null, par = curEle.offsetParent;
	totalLeft += curEle.offsetLeft;
	totalTop += curEle.offsetTop;
	while (par) {
		if (navigator.userAgent.indexOf('MSIE 8.0') === -1) {
			totalLeft += par.clientLeft;
			totalTop += par.clientTop;
		}
		totalLeft += par.offsetLeft;
		totalTop += par.offsetTop;
		par = par.offsetParent;
	}
	return {
		left : totalLeft,
		top : totalTop
	}
};
scrollH = getScrollTop();
left = parseInt(offset($('a')[2]).left);
topPos = parseInt(offset($('a')[2]).top
		- scrollH);
clickX = parseInt(left + 10);
clickY = parseInt(topPos + 10);
console.log(clickX);
console.log(clickY);