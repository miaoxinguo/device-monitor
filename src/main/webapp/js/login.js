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
			window.location.href="main.html";
		}
	});
});