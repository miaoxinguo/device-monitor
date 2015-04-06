$(document).ready(function(){
	// 加载该用户所能查看的所有酒店
	/*
	$.ajax({
		url: '/device-monitor/hotels',
		type: 'get',
		dataType: 'json',
		success:function(data){
			if(!data.success){
				alert(data.msg);
				return;
			}
			console.log(data);
		}
	});
	*/
	
	// 表格
	var t = $("#deviceStatusTable").dataTable({
		"bPaginate": true,
	    "bProcessing": true,                    //加载数据时显示正在加载信息   
	    //"bServerSide": true,                    //指定从服务器端获取数据   
	    //"sAjaxSource": "deviceStatus",	//获取数据的url   
	    "aoColumns": [   {"mDataProp":"id"},  
	                     {"mDataProp":"wendu"},
	                     {"mDataProp":"shidu"},
	                     {"mDataProp":"co2"},
	                     {"mDataProp":"nh3"}],  // 与后台返回属性一致
	    //"bFilter": false,                       //不使用过滤功能     
	    "sPaginationType": "full_numbers",      //翻页界面类型   
	    "oLanguage": {                          //国际化   
	        "sLengthMenu": "每页显示 _MENU_ 条记录",   
	        "sZeroRecords": "没有检索到数据",   
	        "sInfo": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",   
	        "sInfoEmtpy": "没有数据",   
	        "sProcessing": "正在加载数据...",   
	        "oPaginate": {   
	            "sFirst": "首页",   
	            "sPrevious": "前页",   
	            "sNext": "后页",   
	            "sLast": "尾页"  
	        }   
	    }   
	});
});

// 事件 - 酒店下拉选择
$("#hotelList").change(function(){
	$(this).children("option[value='0']").remove();
	//console.log($(this).children('option:selected').val());
});

//事件 - 显示模式切换
$("#btnModalDiv").click(function(){
	$("#modal_div").show();
	$("#modal_list").hide();
	$(this).removeClass("btn-deauft").addClass("btn-primary");
	$("#btnModalList").removeClass("btn-primary").addClass("btn-deauft");
	
});
$("#btnModalList").click(function(){
	$("#modal_list").show();
	$("#modal_div").hide();
	$(this).removeClass("btn-deauft").addClass("btn-primary");
	$("#btnModalDiv").removeClass("btn-primary").addClass("btn-deauft");
});

function loadDeviceStatus(hotelId){
	$.ajax({
		url: '/device-monitor/deviceStatus',
		type: 'get',
		dataType: 'json',
		data: {
			hotelId: '12'
		},
		success:function(data){
			if(!data.success){
				$("#errMsg").text(data.msg).show();
				return;
			}
		}
	});
}
