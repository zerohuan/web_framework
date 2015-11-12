// JavaScript Document
var host = "http://127.0.0.1:8081/ThemeSE/";
var dfLimit = 20;

function getParameter(name) {
var url = location.search;
	if (url.indexOf("?") != -1) {
      	var str = url.substr(1);
      	strs = str.split("&");
      	for(var i = 0; i < strs.length; i++) {
        	 if(strs[i].split("=")[0] == name) 
			 	return decodeURI(strs[i].split("=")[1]);
   	    } 
	}
	return null;
}

String.prototype.endWith=function(s){
  if(s==null||s==""||this.length==0||s.length>this.length)
     return false;
  if(this.substring(this.length-s.length)==s)
     return true;
  else
     return false;
  return true;
 }

 String.prototype.startWith=function(s){
  if(s==null||s==""||this.length==0||s.length>this.length)
   return false;
  if(this.substr(0,s.length)==s)
     return true;
  else
     return false;
  return true;
 }

var itemCount = 1;
var totalCount = 0;
var keys=getParameter("keys");
var page=getParameter("page");
var limit=getParameter("limit");
(function search() {
  if(keys != null) {
      $.ajax({
      url: host + "searchbykey.do",
      type: 'post',
      data: {keys : keys,
             limit : limit,
             page : page},
      cache: false,
      success: function (text) {
        jsonArr = eval("(" + text + ")");
        var item = $('.blog_main').eq(0);
        var itemWrapper = $('#itemWrapper');
        var len = jsonArr.length;
        
        //更新检索结果数
        if(len > 0) {
          totalCount = jsonArr[0].count;
        }
        $('#search-result-h').text("检索结果：共有" + totalCount + "条");
        
        for(var i = 0; i < len; i++) {
          var itemNew = item.clone();
          itemNew.css("display", "block");
          itemNew.find('h4 a').attr("href", jsonArr[i].url).text(jsonArr[i].title);
          itemNew.find('.read_more a').attr("href", jsonArr[i].url);
          itemNew.find('.para').html(jsonArr[i].content.replace(/\[:red/gm, "<font style='color:#F00;'>").replace(/red:\]/gm, "</font>"));
          itemNew.find('.indexTime').text(jsonArr[i].createTime);
          itemWrapper.append(itemNew);
        }
        
        //创建分页按钮
        if(page > 0 && limit > 0)
          createPageBtn(totalCount);
      },
      error: function (jqXHR, textStatus, errorThrown) {
        alert("搜索结果异常");
      }
   });
   
  }
}());

function startCrawel() {
					var keys = $('#keys').val();
					var count = $('#count').val();
					var mins = $('#mins').val();

					if(!count.match(/\d+/g)) {
            alert("数量上限应该为数字");
            return;
					}
					if(!mins.match(/\d+/g)) {
            alert("时间上限应该为数字");
            return;
					}
					if(keys == "主题/关键字") {
            keys = "";
					}
					$.ajax({
						url: host + "startCrawl.do",
						type: 'post',
						data: {keys : keys,
             				count : count,
             				mins : mins},
      			cache: false,
      			success: function(text) {
      						jsonArr = eval("(" + text + ")");
      						if(jsonArr.success) {
      							alert("抓取任务启动成功，正在后台运行");
      						} else {
      							alert("正在进行抓取任务中");
      						}
      					}
					});
					return;
				}

function createPageBtn(tc) {
  var btnWrapper = $('#page-controller');
  var lastPage = Math.ceil(tc / limit);
  var pageN = Number(page);
  var startCursor = 0;
  if(pageN >= 6) {
    startCursor = - 5;
  } else {
    startCursor = 1 - pageN;
  }
  
  var lastShowPage = startCursor + pageN + 9;
  if(lastPage < lastShowPage)
    lastShowPage = lastPage;

  for(var i = startCursor + pageN; i <= lastShowPage; i++) {
    if(i == pageN) {
      btnWrapper.append("<a href='javascript:void(0);' class='disable'>" + i + "</a>&nbsp;&nbsp;&nbsp;");
    } else {
      btnWrapper.append("<a href='blog.html?keys=" + keys + "&page=" + i + "&limit=" + limit + "'>" + i + "</a>&nbsp;&nbsp;&nbsp;");
    }
  }
}

function searchByInput() {
  var keys = $('#search-input').val();
  if(keys == null || keys == "") {
    alert("请输入检索内容");
  } else {
    window.location = "blog.html?keys=" + keys + "&page=1&limit=" + dfLimit;
  }
}

function contact() {
  alert("提交成功，感谢您的反馈！");
}

