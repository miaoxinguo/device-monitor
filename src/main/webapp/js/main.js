$(document).ready(function(){
	// ajax全局设置
	$.ajaxSetup({
		dataType: 'json',
		cache: false,
		contentType: "application/x-www-form-urlencoded;charset=utf-8"
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
				$("#navbar ul:eq(0) li:gt(1)").hide();
			}
			else if(data.msg == "maintainer"){   // 维保人与只显示首页
				$("#navbar ul:eq(0) li:gt(0)").hide();     // TODO 给维保人员单独做个页面可能更合适
			}
		}
	});
	
	$("#homePage").click();
});

//加载首页 监测值页
$("#homePage").click(function(){
	// 首页不采用左侧菜单方式
	$("#contentContainer #leftMenu").hide().removeClass("col-md-2");
	$("#contentContainer #content").removeClass("col-md-10").addClass("col-md-12");
	
	// 加载首页 设备指标页
	loadPage("monitorValue.html");
	
	$("#navbar li.active").removeClass("active");
	$(this).parent().addClass("active");
});

//加载系统管理页
$("#systemPage").click(function(){
	loadLeftMenu("systemLeftMenu.html");
	loadPage("device.html");  // 默认显示设备页
	$("#navbar li.active").removeClass("active");
	$(this).parent().addClass("active");
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
