$(document).ready(function(){
	// 表格
	$("#userTable").dataTable({
		"bPaginate": true,
		"bLengthChange": false, 			//改变每页显示数据数量
		'iDisplayLength': 10, 				//每页显示记录数
	    "bProcessing": false,                    //加载数据时显示正在加载信息   
	    "bServerSide": true,                    //指定从服务器端获取数据   
	    "sAjaxSource": "users",		//获取数据的url   
	    "fnServerData": function (sSource, aoData, fnCallback) {
	    	/*
	    	aoData.push(
	    		{"name": "userId", "value": $("#hotelList").children('option:selected').val()},
	    		{"name": "user.name", "value": $("#search_div #username").val()}
	    	);
	    	*/
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
	    "aoColumns": [{"mData":"id"},
	                  {"mData":"name"},
	                  {"mData":"role"},
                      {"mData":"roleName"}],  // 与后台返回属性一致
//	    "bFilter": false,                       //不使用过滤功能     
	    "sPaginationType": "full_numbers",      //翻页界面类型   
	    "oLanguage": {    
	    	"sUrl": "media/i18n/zh_CN.txt"
	    }   
	});
});