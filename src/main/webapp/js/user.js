$(document).ready(function(){
	// 表格
	var t = $("#userTable").dataTable({
		"bPaginate": true,
	    "bProcessing": true,                    //加载数据时显示正在加载信息   
	    //"bServerSide": true,                    //指定从服务器端获取数据   
	    //"sAjaxSource": "deviceStatus",	//获取数据的url   
	    "aoColumns": [   {"mDataProp":"id"},  
	                     {"mDataProp":"wendu"},
	                     {"mDataProp":"shidu"},
	                     {"mDataProp":"co2"}],  // 与后台返回属性一致
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