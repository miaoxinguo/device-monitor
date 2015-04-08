var id_of_clearInterval;

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
		"bLengthChange": false, 			//改变每页显示数据数量
		'iDisplayLength':50, 				//每页显示记录数
		"bSort": false,						//排序
		"bFilter": false, 					//过滤功能
	    //"bProcessing": true,                    //加载数据时显示正在加载信息   
	    "bServerSide": true,                    //指定从服务器端获取数据   
	    "sAjaxSource": "deviceStatus",	//获取数据的url 
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
	                     {"mDataProp":"temperature"},
	                     {"mDataProp":"humidity"},
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
	
	id_of_clearInterval = setInterval(refresh, 30000);
});

// 每隔30s自动刷新
function refresh(){
	$("#loading").show();
	$("#deviceStatusTable").DataTable().draw();   // 取表格对象 刷新
}

// 事件 - 酒店下拉选择
$("#hotelList").change(function(){
	$("#loading").show();
	//$(this).children("option[value='-1']").remove();
	//console.log($(this).children('option:selected').val());
	$("#deviceStatusTable").DataTable().draw();   // 取表格对象 刷新
	
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
$('#deviceStatusTable').DataTable().on( 'draw', function () {
	// 数据加载完成后设置图标不显示
	$("#loading").hide();
	
	$("#modal_div").html("");
    var total = $('#deviceStatusTable tbody tr').size();
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
        	var $tr = $('#deviceStatusTable tbody tr:eq('+ i +')')
        	
        	// 取最新添加的视块
        	var $modalDiv = $(this).children(":last");
        	
        	$modalDiv.find("div.panel-heading").text( $tr.children("td:eq(1)").text() );  // name
        	$modalDiv.find("div.panel-body li:eq(0) span").text( $tr.children("td:eq(3)").text() );  // 温度
        	$modalDiv.find("div.panel-body li:eq(1) span").text( $tr.children("td:eq(4)").text() );  // 湿度
        	$modalDiv.find("div.panel-body li:eq(2) span").text( $tr.children("td:eq(5)").text() );  // co2
        	$modalDiv.find("div.panel-body li:eq(3) span").text( $tr.children("td:eq(6)").text() );  // nh3
        	
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