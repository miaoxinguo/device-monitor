$(document).ready(function(){
	// 表格
	var t = $("#hotelTable").dataTable({
		"bPaginate": true,
		"bLengthChange": false, 			//改变每页显示数据数量
		'iDisplayLength': 10, 				//每页显示记录数
	    "bProcessing": false,                    //加载数据时显示正在加载信息   
	    "bServerSide": true,                    //指定从服务器端获取数据   
	    "sAjaxSource": "hotels",		//获取数据的url   
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
	    "aoColumns": [   {"mData":"id"},  
	                     {"mData":"name"},
	                     {"mData":"user.name"}],  // 与后台返回属性一致
	    "bFilter": false,                       //不使用过滤功能     
	    "sPaginationType": "full_numbers",      //翻页界面类型   
	    "oLanguage": {                          //国际化   
	    	"sUrl": "media/i18n/zh_CN.txt"
	    }   
	});
	
	// 显示选中行
	$('#hotelTable tbody').on('click', 'tr', function() {
		if ($(this).hasClass('selected')) {
			$(this).removeClass('selected');
		} else {
			t.$('tr.selected').removeClass('selected');
			$(this).addClass('selected');
		}
	});
	 
	// 增加按钮点击事件
	$("#btn_add").click(function(){
		$("#device_modal .modal-title").text("增加");
		$("#device_modal #modal_btn_save").show();
		$("#device_modal #modal_btn_edit").hide();
		$("#device_modal").modal();  // 用的是bootstrap的modal，可以按照bootstrap的文档设置参数
	})
});