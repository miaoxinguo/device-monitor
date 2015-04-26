$(document).ready(function(){
	// 表格
	var t = $("#deviceTable").dataTable({
		"bPaginate": true,
		"bLengthChange": false, 			//改变每页显示数据数量
		'iDisplayLength': 10, 				//每页显示记录数
	    "bProcessing": false,                    //加载数据时显示正在加载信息   
	    "bServerSide": true,                    //指定从服务器端获取数据   
	    "sAjaxSource": "devices",				//获取数据的url   
	    "fnServerData": function (sSource, aoData, fnCallback) {
	    	aoData.push(
	    		{"name": "hotelId", "value": $("#hotelList").children('option:selected').val()},
	    		{"name": "room", "value": $("#search_div #room").val()}
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
	    "aoColumns": [   {"mData":"id", "bVisible": false},  
	                     {"mData":"sid"},
	                     {"mData":"name"},
	                     {"mData":"hotel.name"},
	                     {"mData":"room"},
	                     {"mData":"lastDate"},
	                     {"mData":"nextDate"}],  // 与后台返回属性一致
	    "bFilter": false,                       //不使用过滤功能     
	    "sPaginationType": "full_numbers",      //翻页界面类型   
	    "oLanguage": {                          //国际化   
	    	"sUrl": "media/i18n/zh_CN.txt"
	    }   
	});
	
	// 显示选中行
	$('#deviceTable tbody').on('click', 'tr', function() {
		if ($(this).hasClass('selected')) {
			$(this).removeClass('selected');
		} else {
			t.$('tr.selected').removeClass('selected');
			$(this).addClass('selected');
		}
	});
	 
	/*
	 * 增加按钮点击事件
	 */ 
	$("#btn_add").click(function(){
		$("#form_add #form_add_sid").removeAttr("readonly");
		$("#form_add").show();
		$("#content_list").hide();
		
		// 加载酒店下拉框
		$.get("hotelNames", function(data){
			var $hotelSelect = $("#select_hotel");
			for(var index in data){
				$hotelSelect.append("<option value='" + data[index].id + "'>"+data[index].name+"</option>");
			}
		});
		
		// 保存按钮
		$("#btn_save").click(function(){
			$("#form_add form").ajaxSubmit({
				type: "post",
				url: "device",
			    dataType: "json",
				success: function(data){
					if(!data.success){
						alert(data.msg);
						return;
					}
					$("#form_add form").clearForm();
					$("#deviceTable").DataTable().draw();   // 取表格对象 刷新
					alert("保存成功");
				}
			});
			return false;
		});
		
		// 取消按钮
		$("#btn_cancel").click(function(){
			$("#content_list").show();
			$("#form_add").hide();
			$("#form_add").clearForm();
		});
	});
	
	/*
	 * 删除按钮点击事件
	 */
	$("#btn_remove").click(function(){
		if (!$("#deviceTable tbody tr").hasClass('selected')) {
			alert("请选择一行");
			return;
		}
		
		// 提交后台删除
		$.ajax({
			type: 'delete',
			url: "device/" + t.$('tr.selected').children().eq(0).text(),
			dataType: 'json',
			success:function(data){
				alert("删除成功");
				$("#deviceTable").DataTable().draw();   // 取表格对象 刷新
			}	
		});
	});
	
	/*
	 * 编辑按钮点击事件
	 */
	$("#btn_edit").click(function(){
		if (!$("#deviceTable tbody tr").hasClass('selected')) {
			alert("请选择一行");
			return;
		}
		
		// 加载酒店下拉框
		$.get("hotelNames", function(data){
			var $hotelSelect = $("#select_hotel");
			for(var index in data){
				$hotelSelect.append("<option value='" + data[index].id + "'>"+data[index].name+"</option>");
			}
			
			// 从后台读数据
			$.ajax({
				type: 'get',
				url: "device?sid=" + t.$('tr.selected').children().eq(0).text(),
				dataType: 'json',
				success:function(data){
					$("#form_add #form_add_sid").val(data.sid);
					$("#form_add #select_hotel").children("option[value='"+data.hotel.id+"']").attr("selected",true);
					$("#form_add #form_add_room").val(data.room);
					$("#form_add #form_add_last_date").val(data.lastDate);
					$("#form_add #form_add_next_date").val(data.nextDate);
				}	
			});
		});
		
		$("#form_add #form_add_sid").attr("readonly","readonly");
		$("#form_add").show();
		$("#content_list").hide();
		
		// 保存按钮
		$("#btn_save").click(function(){
			$("#form_add form").ajaxSubmit({
				type: "put",
				url: "device",
			    dataType: "json",
				success: function(data){
					if(!data.success){
						alert(data.msg);
						return;
					}
					alert("保存成功");
					$("#deviceTable").DataTable().draw();   // 取表格对象 刷新
					$("#btn_cancel").click();
					$("#form_add form").clearForm();
				}
			});
			return false;
		});
		
		// 取消按钮
		$("#btn_cancel").click(function(){
			$("#content_list").show();
			$("#form_add").hide();
			$("#form_add").clearForm();
		});
	})
});