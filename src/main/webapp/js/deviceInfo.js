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
	    "aoColumns": [   {"mData":"id", "bVisible": false},   // 与后台返回属性一致
	                     {"mData":"sid"},
	                     {"mData":"name"},
	                     {"mData":"hotel.name"},
	                     {"mData":"room"},
	                     {"mData":"usedHours", "fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {
	                    	 if(sData > 1500){
	                    		 $(nTd).addClass("text-danger");
	                    	 }
			             }}],  
	    "bFilter": false,                       //不使用过滤功能     
	    "sPaginationType": "full_numbers",      //翻页界面类型   
	    "oLanguage": {                          //国际化   
	    	"sUrl": "media/i18n/zh_CN.txt"
	    }   
	});
	
	// 加载酒店下拉框内容
	$.get("hotelNames", function(data){
		var $hotelSelect = $("#hotelList");
		for(var index in data){
			var option = "<option value='"+ data[index].id +"'>"+ data[index].name +"</option>"
			$hotelSelect.append(option);
		}
	});
});

//事件 - 酒店下拉选择
$("#search_div #hotelList").change(function(){
	$("#deviceTable").DataTable().draw();   // 取表格对象 刷新
});

//事件 - 酒店模糊查询
$("#search_div #room").keyup(function(){
	$("#deviceTable").DataTable().draw();   // 取表格对象 刷新
});