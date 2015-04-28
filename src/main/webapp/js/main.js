$(document).ready(function(){
	// ajax全局设置
	$.ajaxSetup({
		dataType: 'json',
		cache: false,
		contentType: "application/x-www-form-urlencoded;charset=utf-8",
		dataFilter: function(data, type){
			if("json|html".indexOf(type) > -1 && data == "timeout"){
				alert("已过期，请重新登录！");
				window.location.href = 'login.html';
				return;
			}
			return data;
		}
	});
	
	// 根据用户角色，控制要显示的菜单
	$.ajax({
		url: "role",
		type: "get",
		dataType: "json",
		success: function(data){
			if(!data.success){
				window.location.href="login.html";
				return;
			}
			if(data.msg == "waiter"){   // 酒店人员不显示系统管理
				$("#navbar ul:eq(0) li:eq(1)").hide();
				$("#homePage").click();
			}
			else if(data.msg == "maintainer"){  
				$("#navbar ul:eq(0) li").hide();     // 给维保人员单独做个页面更合适
				loadPage("deviceInfo.html");  
			}
			else if(data.msg == "admin"){
				$("#navbar ul:eq(0) li:eq(0)").hide();
				$("#systemPage").click();
			}
		}
	});
});

/*
 * 加载首页 监测值页
 */
$("#homePage").click(function(){
	// 首页不采用左侧菜单方式
	$("#contentContainer #leftMenu").hide().removeClass("col-md-2");
	$("#contentContainer #content").removeClass("col-md-10").addClass("col-md-12");
	
	// 加载首页 设备指标页
	loadPage("monitorValue.html");
	
	$("#navbar li.active").removeClass("active");
	$(this).parent().addClass("active");
});

/*
 * 加载系统管理页
 */
$("#systemPage").click(function(){
	loadLeftMenu("systemLeftMenu.html");
	loadPage("device.html");  // 默认显示设备页
	$("#navbar li.active").removeClass("active");
	$(this).parent().addClass("active");
});

/*
 * 加载修改密码模态窗口
 */
$("#settingPage").click(function(){
	$.get("html/setting.html", function(data){
		$("#pw").html(data).children(".modal").modal();
	}, "html");
});

/**
 * 加载左侧导航
 */
function loadLeftMenu(targetPage){
	// 首页不采用左侧菜单方式 ，所以在每次加载导航时设置一次
	$("#contentContainer #leftMenu").show().addClass("col-md-2");
	$("#contentContainer #content").removeClass("col-md-12").addClass("col-md-10");
	
	$.ajax({
		url: 'html/' + targetPage,
		type: 'get',
		dataType: 'html',
		success:function(data){
			$("#leftMenu").html(data);
		}
	});
}

/**
 * 加载内容页面
 */
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
