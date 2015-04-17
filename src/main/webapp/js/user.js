$(document).ready(function(){
	// 表格
	$("#userTable").dataTable({
		"bPaginate": true,
	    "bProcessing": true,                    //加载数据时显示正在加载信息   
	    //"bServerSide": true,                    //指定从服务器端获取数据   
	    //"sAjaxSource": "users",	//获取数据的url   
	    "aoColumns": [   {"mDataProp":"id"},  
	                     {"mDataProp":"name"},
	                     {"mDataProp":"role"},
	                     {"mDataProp":"hotel"}],  // 与后台返回属性一致
	    //"bFilter": false,                       //不使用过滤功能     
	    "sPaginationType": "full_numbers",      //翻页界面类型   
	    "oLanguage": {    
	    	"sUrl": "media/i18n/zh_CN.txt"
	    }   
	});
});