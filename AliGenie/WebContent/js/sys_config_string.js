//所有项目适用的全局常量

//读数据库相关
var GET_DATA = '../do';

//数据提交相关
var LAN = 'cn';

//会话id


/*--获取网页传递的参数--*/
function request(paras) {
    var url = location.href;
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");
    var paraObj = {}
    for (i = 0; j = paraString[i]; i++) {
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);
    }
    var returnValue = paraObj[paras.toLowerCase()];
    if (typeof (returnValue) == "undefined") {
        return "";
    } else {
        return returnValue;
    }
}

//==================================
//描述：读取cookies
//程序员：江晓东
//日期：2008-11-23
//参数说明：name--cookies的名字，返回指定cookies的值
//==================================
function readCookie(name) {
    var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null) return unescape(arr[2]);
    return null;
}
function readCookieSecond(name1, name2) {
    var cookieValue = "";
    var search = name2 + "=";
    var firstvalue = readCookie(name1);
    if (firstvalue.length > 0) {
        var offset = firstvalue.indexOf(search);
        if (offset != -1) {
            offset += search.length;
            end = firstvalue.indexOf("&", offset);
            if (end == -1) end = firstvalue.length;
            cookieValue = unescape(firstvalue.substring(offset, end));
        }
    }
    return cookieValue;
}
function setCookie(name, value) {
    document.cookie = name + '=' + value;
}

//
function string2Date(datestr) {
    var converted = Date.parse(datestr);
    var myDate = new Date(converted);
    if (isNaN(myDate)) {
        var arys = datestr.split('-');
        myDate = new Date(arys[0], --arys[1], arys[2]);
    }
    return myDate;
}
function dateDiff(interval, date1, date2) {
    var objInterval = { 'D': 1000 * 60 * 60 * 24, 'H': 1000 * 60 * 60,
        'M': 1000 * 60, 'S': 1000, 'T': 1
    };
    interval = interval.toUpperCase();
    var dt1 = Date.parse(date1.replace(/-/g, '/'));
    var dt2 = Date.parse(date2.replace(/-/g, '/'));
    try {
        return Math.round((dt2 - dt1) / eval('(objInterval.' + interval + ')'));
    }
    catch (e) {
        return e.message;
    }
}
//==================================
//描述：重载setTimeout方法，使之可以执行带参数的函数
//程序员：江晓东
//日期：2008-11-23
//参数说明：name--cookies的名字，返回指定cookies的值
//==================================
_setTimeout = function (callback, timeout, param) {
    var args = Array.prototype.slice.call(arguments, 2);
    var _cb = function () {
        callback.apply(null, args);
    }
    setTimeout(_cb, timeout);
}
//==================================
//描述：只适用于宝特工作室的网站，用于后台转换图片路径
//程序员：江晓东
//==================================
function resetImagePath(sourcePath) {
    if (sourcePath == null) return "";
    if (sourcePath.substring(0, 4) == 'http')return sourcePath
    var regimg = /\.\.\/images\/upload/g;
    return sourcePath.replace(regimg, '../images/upload');
}
//==================================
//描述：只适用于宝特工作室的网站，用于后台转换图片路径
//程序员：江晓东
//==================================
function cleanImage(target_img, target_hidden) {
    target_img.attr("src", target_img.attr("defaultValue") || '');
    target_hidden.val(target_hidden.attr("defaultValue") || '');
}
//==================================
//描述：通用打开编辑窗口对话框
//程序员：江晓东
//==================================
function openDialogEdit(url) {
    var objs = new Array();
    var getv = window.showModalDialog(url, objs, 'dialogWidth=800px;dialogHeight=470px;status=no;scroll=auto;location=no');
    return getv;
}
//==================================
//描述：强制保留两位小数
//程序员：WEB
//==================================
function toDecimal2(x) {
    var f = parseFloat(x);
    if (isNaN(f)) {
        return false;
    }
    var f = Math.round(x * 100) / 100;
    var s = f.toString();
    var rs = s.indexOf('.');
    if (rs < 0) {
        rs = s.length;
        s += '.';
    }
    while (s.length <= rs + 2) {
        s += '0';
    }
    return s;
}

//==================================
//描述：sql日期转换成标准日期yyyy-MM-dd
//程序员：WEB
//==================================
function data_string(str) {
    var d = new Date(parseInt(str));
    var ar_date = [d.getFullYear(), d.getMonth() + 1, d.getDate()];
    for (var i = 0; i < ar_date.length; i++) ar_date[i] = dFormat(ar_date[i]); return ar_date.join('-');
    function dFormat(i) { return i < 10 ? "0" + i.toString() : i; }
}

//==================================
//描述：判断是手机版还是PC版
//程序员：WEB
//==================================
function uaredirect() {
    try {
        if (document.getElementById("bdmark") != null) {
            return false;
        }
        var urlhash = window.location.hash;
        if (!urlhash.match("fromapp")) {
            if ((navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i))) {
                return true;
            }
        }
        return false;
    } catch (err) {
        return false;
    }
}

// ==================================
// js从数组中删除指定值（不是指定位置）的元素
// ==================================
Array.prototype.indexOf = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
    }
    return -1;
};
Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};



if (typeof String.prototype.startsWith != 'function') {
  String.prototype.startsWith = function (prefix){
    return this.slice(0, prefix.length) === prefix;
  };
}

if (typeof String.prototype.endsWith != 'function') {
  String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
  };
}

//触摸事件
var touchEvents = {
    touchstart: "touchstart",
    touchmove: "touchmove",
    touchend: "touchend",

    /**
     * @desc:判断是否pc设备，若是pc，需要更改touch事件为鼠标事件，否则默认触摸事件
     */
    initTouchEvents: function () {
        if (isPC()) {
            this.touchstart = "mousedown";
            this.touchmove = "mousemove";
            this.touchend = "mouseup";
        }
    }
};