var id_of_clearInterval;

$(document).ready(function(){
	// 表格
	var t = $("#monitorValueTable").dataTable({
		"bPaginate": true,
		"bLengthChange": false, 			//改变每页显示数据数量
		'iDisplayLength':50, 				//每页显示记录数
		"bSort": false,						//排序
		"bFilter": false, 					//过滤功能
	    //"bProcessing": true,                    //加载数据时显示正在加载信息   
	    "bServerSide": true,                    //指定从服务器端获取数据   
	    "sAjaxSource": "monitorValue",	//获取数据的url 
	    "fnServerData": function (sSource, aoData, fnCallback) {
	    	$.ajax( {
		    	"dataType": 'json',
		    	"type": "get",
		    	"url": sSource+"?hotelId="+$("#hotelList").children('option:selected').val(),
		    	"success": fnCallback
	    	});
    	},
	    "aoColumns": [   {"mDataProp":"id"},  
	                     {"mDataProp":"devname"},
	                     {"mDataProp":"status"},
	                     {"mDataProp":"isOpen"},
	                     {"mDataProp":"temperature"},
	                     {"mDataProp":"humidity"},
	                     {"mDataProp":"co2"},
	                     {"mDataProp":"nh3"}],  // 与后台返回属性一致
	    //"bFilter": false,                       //不使用过滤功能     
	    "sPaginationType": "full_numbers",      //翻页界面类型   
	    "oLanguage": {                          //国际化   
	    	"sUrl": "media/i18n/zh_CN.txt"   
	    }   
	});
	
	id_of_clearInterval = setInterval(refresh, 30000);
});

// 每隔30s自动刷新
function refresh(){
	$("#loading").show();
	$("#monitorValueTable").DataTable().draw();   // 取表格对象 刷新
}

// 事件 - 酒店下拉选择
$("#hotelList").change(function(){
	$("#loading").show();
	//$(this).children("option[value='-1']").remove();
	//console.log($(this).children('option:selected').val());
	$("#monitorValueTable").DataTable().draw();   // 取表格对象 刷新
	
	clearInterval(id_of_clearInterval);
	id_of_clearInterval = setInterval(refresh, 30000);
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


// 事件 - 表格加载后触发，用于绘制模块视图
$('#monitorValueTable').DataTable().on( 'draw', function () {
	// 数据加载完成后设置图标不显示
	$("#loading").hide();
	
	$("#modal_div").html("");
    var total = $('#monitorValueTable tbody tr').size();
    var rowSize = 6;   // 每行6个
    // 共多少行
    for(var i=0; i<total/rowSize; i++){
    	$("#modal_div").append("<div class='row'></div>");
    }
    
    // 遍历每行 增加视块
    $("#modal_div .row").each(function(rowIndex){
    	// 共多少行
    	var offset = rowIndex * rowSize;
        for(var i=offset ;i<offset+rowSize && i<total; i++){
        	$("#template_div .col-md-2").clone().appendTo($(this));

        	// 从表中的第i行取数据
        	var $tr = $('#monitorValueTable tbody tr:eq('+ i +')')
        	
        	// 取最新添加的视块
        	var $modalDiv = $(this).children(":last");
        	
//        	// 根据设备的状态：在线/离线、开机/关机 设置面板标题的背景色
        	if( "在线" == $tr.children("td:eq(2)").text() ){
        		$modalDiv.find("div.panel").removeClass("panel-warning");
	        	// 只有在线才能判断是否开机
	    		if( "关" == $tr.children("td:eq(3)").text() ){
	        		$modalDiv.find("div.panel").removeClass("panel-info").addClass("panel-default");
	        	} 
	    		else if( "开" == $tr.children("td:eq(3)").text() ){
	    			$modalDiv.find("div.panel").removeClass("panel-default").addClass("panel-info");
	    		}
        	}
        	else{
        		$modalDiv.find("div.panel").removeClass("panel-default panel-info").addClass("panel-warning");
        	}
        	
    		
        	$modalDiv.find("div.panel-heading").text( $tr.children("td:eq(1)").text() );  // name
//        	$modalDiv.find("div.panel-body li").each(function(index){
//        		var tdIndex = index + 4;  // 视块与表格数据的对应顺序固定
//        		$(this).children("span").text( $tr.children("td:eq("+ tdIndex +")").text() );
//        	});
        	$modalDiv.find("div.panel-body li:eq(0) span").text( $tr.children("td:eq(4)").text() );  // 温度
        	$modalDiv.find("div.panel-body li:eq(1) span").text( $tr.children("td:eq(5)").text() );  // 湿度
        	$modalDiv.find("div.panel-body li:eq(2) span").text( $tr.children("td:eq(6)").text() );  // co2
        	$modalDiv.find("div.panel-body li:eq(3) span").text( $tr.children("td:eq(7)").text() );  // nh3
        	
        	// 图标点击事件
        	$modalDiv.find("div.panel-footer a:eq(0)").click(function(){
        		$("#edit_modal .modal-title").text("设备维护");
        		//console.log($("#edit_modal input[name='id']"));
        		$("#edit_modal input[name='id']").val($tr.children("td:eq(0)").text());
        		$("#edit_modal").modal(); 
        	})
        }
    });
});