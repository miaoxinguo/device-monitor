$(document).ready(function(){
	// 事件 - 登录按钮
	$("#btnSubmit").click(function() {
		var username = $("#inputUsername").val();
		var password = $("#inputPassword").val();
		
		$.ajax({
			url: '/device-monitor/auth',
			type: 'POST',
			dataType: 'json',
			data: {
				username: $("#inputUsername").val(),
				password: hex_sha1($("#inputPassword").val())
			},
			success:function(data){
				if(!data.success){
					$("#errMsg").text(data.msg).show();
					return;
				}
				self.location.href="main.html";
			}
		});
	});
	
	// 事件 - 回车
	document.onkeydown = function(e){
	    var ev = document.all ? window.event : e;
	    if(ev.keyCode==13) {
	           $('#btnSubmit').click();
	     }
	}
	
});