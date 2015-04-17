$(document).ready(function(){
	// 表格
	var t = $("#deviceTable").dataTable({
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
	    	"sUrl": "media/i18n/zh_CN.txt"
	    }   
	});
});