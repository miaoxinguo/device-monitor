// 保存按钮
$("#pw .modal .modal-footer .btn-primary").click(function(){
	
	// 初始化提示信息
	$("#pw .form-control-static").removeClass("text-danger").addClass("text-info").text("6-16位数字、字母组合");
	
	var $oldPw = $("#pw #oldPw");
	var $newPw = $("#pw #newPw");
	var $reNewPw = $("#pw #reNewPw");
	
	if(!isNormalChar($oldPw.val()) || $oldPw.val().length < 6 || $oldPw.val().length > 16){
		$oldPw.parent().next("div").children("p").text("请输入6-16位字母和数字的组合").removeClass("text-info").addClass("text-danger");
		return;
	}
	if(!isNormalChar($newPw.val()) || $newPw.val().length < 6 || $newPw.val().length > 16){
		$newPw.parent().next("div").children("p").text("请输入6-16位字母和数字的组合").removeClass("text-info").addClass("text-danger");
		return;
	}
	
	if($newPw.val() != $reNewPw.val()){
		$reNewPw.parent().next("div").children("p").text("两次输入的密码不一致").removeClass("text-info").addClass("text-danger");
		return;
	}
	
	$.ajax({
		"type": "put",
		"url": "password",
		"data": {
			"oldPw": hex_sha1($oldPw.val()),
			"newPw": hex_sha1($newPw.val()),
		},
		"success": function(result){
			if(!result.success){
				alert(result.msg);
				return;
			}
			alert("保存成功");
			$("#pw .modal").modal('hide');  // 隐藏
		}
	});
});