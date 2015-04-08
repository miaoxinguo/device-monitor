$(document).ready(function(){
	$.get("menu", function(data){
		if(!data.success){
			window.location.href="login.html";
			return;
		}
		if(data.msg != "1"){
			$("#navbar li:gt(0)").hide();
		}
	},"json");
	
	// 加载首页 设备指标页
	loadPage("deviceStatus.html");
});

//加载首页 设备指标页
$("#homePage").click(function(){
	loadPage("deviceStatus.html");
	$("#navbar li.active").removeClass("active");
	$(this).parent().addClass("active");
});

// 加载设备页
$("#devicePage").click(function(){
	loadPage("device.html");
	$("#navbar li.active").removeClass("active");
	$(this).parent().addClass("active");
});

//加载用户页
$("#userPage").click(function(){
	loadPage("user.html");
	$("#navbar li.active").removeClass("active");
	$(this).parent().addClass("active");
});

function loadPage(targetPage){
	$.ajax({
		url: 'html/' + targetPage,
		type: 'get',
		dataType: 'html',
		success:function(data){
			$("#content").html(data);
		}
	});
}
