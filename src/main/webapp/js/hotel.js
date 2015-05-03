var t;
$(document).ready(function(){
	// 表格
	t = $("#hotelTable").dataTable({
		"bPaginate": true,
		"bLengthChange": false, 			//改变每页显示数据数量
		'iDisplayLength': 10, 				//每页显示记录数
	    "bProcessing": false,                    //加载数据时显示正在加载信息   
	    "bServerSide": true,                    //指定从服务器端获取数据   
	    "sAjaxSource": "hotels",		//获取数据的url   
	    "fnServerData": function (sSource, aoData, fnCallback) {
	    	aoData.push(
	    		{"name": "name", "value": $("#search_div #search_hotel_name").val()},
	    		{"name": "userId", "value": $("#search_maintainer_list").children('option:selected').val()}
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
	    "aoColumns": [   {"mData":"id", "bVisible": false },  
	                     {"mData":"name", "fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {
                    		 $(nTd).attr("id", oData.id);
			             }},
	                     {"mData":"user.name"}],  // 与后台返回属性一致
	    "bFilter": false,                       //不使用过滤功能     
	    "sPaginationType": "full_numbers",      //翻页界面类型   
	    "oLanguage": {                          //国际化   
	    	"sUrl": "media/i18n/zh_CN.txt"
	    }   
	});
	
	/*
	 * 加载维保人员下拉框内容
	 */ 
	$.get("maintainerNames", function(data){
		
		// 查询区下拉框
		var $searchMaintainerList = $("#search_maintainer_list");
		
		// form区下拉框
		var $formMaintainerList = $("#form_div_select_maintainer");
		
		for(var index in data){
			var option = "<option value='"+ data[index].id +"'>"+ data[index].name +"</option>";
			$searchMaintainerList.append(option);
			$formMaintainerList.append(option);
		}
	});
});

//显示选中行
$('#hotelTable tbody').on('click', 'tr', function() {
	if ($(this).hasClass('selected')) {
		$(this).removeClass('selected');
	} else {
		t.$('tr.selected').removeClass('selected');
		$(this).addClass('selected');
	}
});
 
//事件 - 维保人员下拉选择
$("#search_div #search_maintainer_list").change(function(){
	$("#hotelTable").DataTable().draw();   // 取表格对象 刷新
});

//事件 - 酒店模糊查询
$("#search_div #search_hotel_name").keyup(function(){
	$("#hotelTable").DataTable().draw();   // 取表格对象 刷新
});

// 按钮点击事件 - 增加
$("#btn_add").click(function(){
	$("#hotel_content").hide();
	$("#form_div .panel-title").text("新增酒店");
	$("#form_div").show();
	
	$("#form_div #form_div_select_maintainer").children("option:eq(0)").prop("selected", true);
	
	// 保存
	$("#form_div #btn_save").click(function(){
		$("#form_div form").ajaxSubmit({
			type: "post",
			url: "hotel",
		    dataType: "json",
			success: function(data){
				if(!data.success){
					alert(data.msg);
					return;
				}
				$("#form_div form").clearForm();
				$("#hotelTable").DataTable().draw();   // 取表格对象 刷新
				alert("保存成功");
			}
		});
		return false;
	});
	
	// 取消
	$("#form_div #btn_cancel").click(function(){
		$("#hotel_content").show();
		$("#form_div").hide().clearForm().find("button").unbind();   // 也可以改为页面加载后绑定，这里就不用解除了
	});
})

/*
 * 按钮点击事件 - 删除
 */
$("#btn_remove").click(function(){
	if (!$("#hotelTable tbody tr").hasClass('selected')) {
		alert("请选择一行");
		return;
	}
	var hid = t.$('tr.selected').children().eq(0).text();
	if(confirm("删除"+ hid +"？")==false){
		return;
	}
	// 提交后台删除
	$.ajax({
		type: 'delete',
		url: "hotel/" + hid,
		dataType: 'json',
		success:function(data){
			alert("删除成功");
			$("#hotelTable").DataTable().draw();   // 取表格对象 刷新
		}	
	});
});

/*
 * 按钮点击事件 - 编辑
 */
$("#btn_edit").click(function(){
	if (!$("#hotelTable tbody tr").hasClass('selected')) {
		alert("请选择一行");
		return;
	}
	
	// 从后台读数据
	$.ajax({
		type: 'get',
		url: "hotel/" + t.$('tr.selected').children().eq(0).attr("id"),
		dataType: 'json',
		success:function(data){
			$("#form_div #form_div_name").val(data.name);
			$("#form_div #form_div_select_maintainer").children("option[value='"+data.user.id+"']").prop("selected", true);
		}	
	});
	
	$("#form_div .panel-title").text("编辑酒店");
	$("#form_div").show();
	$("#hotel_content").hide();
	
	// 保存
	$("#btn_save").click(function(){
		$("#form_div form").ajaxSubmit({
			type: "put",
			url: "hotel/"+ t.$('tr.selected').children().eq(0).attr("id"),
		    dataType: "json",
			success: function(data){
				if(!data.success){
					alert(data.msg);
					return;
				}
				alert("保存成功");
				$("#hotelTable").DataTable().draw();   // 取表格对象 刷新
				$("#btn_cancel").click();
				$("#form_div form").clearForm();
			}
		});
		return false;
	});
	
	// 取消
	$("#btn_cancel").click(function(){
		$("#hotel_content").show();
		$("#form_div").hide().clearForm().find("button").unbind();
	});
})