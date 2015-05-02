var t;
$(document).ready(function(){
	// 表格
	t = $("#userTable").dataTable({
		"bPaginate": true,
		"bLengthChange": false, 			//改变每页显示数据数量
		'iDisplayLength': 10, 				//每页显示记录数
	    "bProcessing": false,                    //加载数据时显示正在加载信息   
	    "bServerSide": true,                    //指定从服务器端获取数据  
	    "sAjaxSource": "users",		//获取数据的url   
	    "fnServerData": function (sSource, aoData, fnCallback) {
	    	aoData.push(
	    		{"name": "role", "value": $("#search_role").children('option:selected').val()},
	    		{"name": "name", "value": $("#search_div #search_username").val()}
	    	);
	    	$.ajax( {
		    	"dataType": 'json',
		    	"type": "get",
		    	"url": sSource,
		    	"data": aoData,
		    	"success": function(resp){
		    		fnCallback(resp);
		    	}
	    	});
		},   
	    "aoColumns": [{"mData":"id", "bVisible": false},
	                  {"mData":"name", "fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {
                 	 	 $(nTd).attr("id", oData.id);
		              }},
	                  {"mData":"role", "bVisible": false},
                      {"mData":"roleName"}],  // 与后台返回属性一致
	    "bFilter": false,                       //不使用过滤功能     
	    "sPaginationType": "full_numbers",      //翻页界面类型   
	    "oLanguage": {    
	    	"sUrl": "media/i18n/zh_CN.txt"
	    }   
	});
	
	/*
	 * 加载酒店下拉框内容
	 */ 
	$.get("hotelNames", function(data){
		
		// form区下拉框
		var $form_hotelSelect = $("#form_div_select_hotel");
		
		for(var index in data){
			var option = "<option value='"+ data[index].id +"'>"+ data[index].name +"</option>";
			$form_hotelSelect.append(option).val("");
		}
	});
});

//显示选中行
$('#userTable tbody').on('click', 'tr', function() {
	if ($(this).hasClass('selected')) {
		$(this).removeClass('selected');
	} else {
		t.$('tr.selected').removeClass('selected');
		$(this).addClass('selected');
	}
});

//事件 - 角色下拉选择
$("#search_div #search_role").change(function(){
	$("#userTable").DataTable().draw();   // 取表格对象 刷新
});

//事件 - 酒店模糊查询
$("#search_div #search_username").keyup(function(){
	$("#userTable").DataTable().draw();   // 取表格对象 刷新
});

//事件 - 待选酒店下拉选择
$("#form_div #form_div_select_hotel").change(function(){
	var $opt = $(this).children('option:selected');
	var $hotelIds = $("#form_selected_hotel_ids");
	
	$hotelIds.append("<input type='text' value='"+ $opt.val() +"'>");  // 添加到隐藏域
	$hotelIds.children().each(function(index){
		$(this).prop("name", "hotels["+ index +"].id");
	})
	$("#form_div_selected_hotel").append($opt).val("");  // 添加到已选区并设置没有选中
	$(this).val("");  // 待选区设置没有选中
	
	$opt.dblclick(function(){  // 复制到已选区的同时为其增加”回到待选区“双击事件
		$("#form_div #form_div_select_hotel").append($(this)).val(""); // 待选区设置没有选中
		$hotelIds.children("input[value='"+ $(this).val() +"']").remove();   // 隐藏域中删除
		$hotelIds.children().each(function(index){
			$(this).prop("name", "hotels["+ index +"].id");
		})
	});
});

//按钮点击事件 - 增加
$("#btn_add").click(function(){
	$("#user_content").hide();
	$("#form_div").show();
	
	// 设置form: 用户名、角色可用，密码允许修改
	$("#form_div #form_div_name").prop("readonly", false).parent().next("div").children("p").text("必填")
	$("#form_div #form_div_role").prop("readonly", false).parent().next("div").children("p").text("请选择")
	$("#form_div #form_div_password").parents("div.form-group").show().next("div.form-group").show();
	
	// 保存
	$("#form_div #btn_save").click(function(){
		// 初始化提示信息
		$("#form_div .form-group:eq(1) .form-control-static").removeClass("text-danger").addClass("text-info").text("6-16位数字、字母组合");
		$("#form_div .form-group:eq(2) .form-control-static").removeClass("text-danger").addClass("text-info").text("6-16位数字、字母组合");
		
		var $pw = $("#form_div_password");
		if(!isNormalChar($pw.val()) || $pw.val().length < 6 || $pw.val().length > 16){
			$pw.parent().next("div").children("p").text("请输入6-16位字母和数字的组合").removeClass("text-info").addClass("text-danger");
			return;
		}
		if($pw.val != $("#form_div_password_re").val()){
			$("#form_div_password_re").parent().next().children("p").text("两次输入的密码不一致").removeClass("text-info").addClass("text-danger");
			return;
		}
		
		
		
		$pw.val(hex_sha1($pw.val()));  // 对密码做sha1运算
		
		
		$("#form_div form").ajaxSubmit({
			type: "post",
			url: "user",
		    dataType: "json",
			success: function(data){
				if(!data.success){
					alert(data.msg);
					return;
				}
				$("#form_div form").clearForm();
				$("#userTable").DataTable().draw();   // 取表格对象 刷新
				$("#form_div_selected_hotel").val("");  // 清空已选区
				alert("保存成功");
			}
		});
		return false;
	});
	
	// 取消
	$("#form_div #btn_cancel").click(function(){
		$("#user_content").show();
		$("#form_div").hide().clearForm().find("button").unbind();   // 也可以改为页面加载后绑定，这里就不用解除了
	});
})

/*
 * 按钮点击事件 - 删除
 */
$("#btn_remove").click(function(){
	if (!$("#userTable tbody tr").hasClass('selected')) {
		alert("请选择一行");
		return;
	}
	// 提交后台删除
	$.ajax({
		type: 'delete',
		url: "user/" + t.$('tr.selected').children().eq(0).attr("id"),
		dataType: 'json',
		success:function(data){
			alert("删除成功");
			$("#userTable").DataTable().draw();   // 取表格对象 刷新
		}	
	});
});

/*
 * 按钮点击事件 - 编辑
 */
$("#btn_edit").click(function(){
	if (!$("#userTable tbody tr").hasClass('selected')) {
		alert("请选择一行");
		return;
	}
	
	// 设置form: 用户名只读，密码不允许修改
	$("#form_div #form_div_name").prop("disabled", true).parent().next("div").children("p").text("");
	$("#form_div #form_div_select_role").prop("disabled", true).parent().next("div").children("p").text("");
	$("#form_div #form_div_password").parents("div.form-group").hide().next("div.form-group").hide();
	
	// 从后台读数据
	$.ajax({
		type: 'get',
		url: "user/" + t.$('tr.selected').children().eq(0).attr("id"),
		dataType: 'json',
		success:function(data){
			$("#form_div #form_div_name").val(data.name);
			$("#form_div #form_div_select_role").children("option[value='"+data.role+"']").prop("selected", true);
			for(var i=0; i<data.hotels.length; i++){
				$("#form_div_select_hotel").children("option[value="+data.hotels[i].id+"]").selected().change(); 
			}
		}	
	});
	
	$("#form_div").show();
	$("#user_content").hide();
	
	// 保存
	$("#btn_save").click(function(){
		$("#form_div #form_div_select_role").prop("disabled", false);
		$("#form_div form").ajaxSubmit({
			type: "put",
			url: "user/"+ t.$('tr.selected').children().eq(0).attr("id"),
		    dataType: "json",
			success: function(data){
				if(!data.success){
					alert(data.msg);
					return;
				}
				alert("保存成功");
				$("#userTable").DataTable().draw();   // 取表格对象 刷新
				$("#btn_cancel").click();
				$("#form_div form").clearForm();
				$("#form_div_selected_hotel").val("");  // 清空已选区
			}
		});
		return false;
	});
	
	// 取消
	$("#btn_cancel").click(function(){
		$("#user_content").show();
		$("#form_div").hide().clearForm().find("button").unbind();
	});
})