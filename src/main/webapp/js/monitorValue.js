var id_of_clearInterval;

$(document).ready(function(){
	//表格
	$("#monitorValueTable").dataTable({
		"bPaginate": true,
		"bLengthChange": false, 			//改变每页显示数据数量
		'iDisplayLength': 12, 				//每页显示记录数
		"bSort": false,						//排序
		"bFilter": false, 					//过滤功能
	    //"bProcessing": true,                    //加载数据时显示正在加载信息   
	    "bServerSide": true,                    //指定从服务器端获取数据   
	    "sAjaxSource": "monitorValues",	//获取数据的url 
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
	    "aoColumns": [{"mData":"deviceSid"},  
		              {"mData":"deviceName"},
		              {"mData":"onLine", "fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {
		                  $(nTd).text(sData ? '在线':'离线');
		              }},
		              {"mData":"open", "fnCreatedCell": function (nTd, sData, oData, iRow, iCol) {
		            	  $(nTd).text(sData ? '开':'关');
		              }},
		              {"mData":"temperature"},
		              {"mData":"humidity"},
		              {"mData":"co2"},
		              {"mData":"nh3"},
		              {"mData":"room"}],  // 与后台返回属性一致
	    //"bFilter": true,                       //不使用过滤功能     
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
		//$hotelSelect.children("option:eq(0)").attr("selected",true);
		//$hotelSelect.change();
	});
	
	// 定时刷新数据
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

//事件 - 酒店模糊查询
$("#search_div #room").keyup(function(){
	$("#monitorValueTable").DataTable().draw();   // 取表格对象 刷新
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
	
	$("#modal_div_content").html("");
    var total = $('#monitorValueTable tbody tr').size();
    var rowSize = 6;   // 每行6个
    // 共多少行
    for(var i=0; i<total/rowSize; i++){
    	$("#modal_div_content").append("<div class='row'></div>");
    }
    
    // 如果只有一行错误提示，那么相当于没有数据
	if(total>0 && !$('#monitorValueTable tbody tr td').hasClass("dataTables_empty")){
		
		// 遍历每行 增加视块
	    $("#modal_div_content .row").each(function(rowIndex){
	    	
	    	// 每行第一个元素的偏移量
	    	var offset = rowIndex * rowSize;
	        for(var i=offset ;i<offset+rowSize && i<total; i++){
	        	$("#template_div .col-md-2").clone().appendTo($(this));

	        	// 从表中的第i行取数据
	        	var $tr = $('#monitorValueTable tbody tr:eq('+ i +')')
	        	
	        	// 取最新添加的视块
	        	var $modalDiv = $(this).children(":last");
	        	
	        	// 根据设备的状态：在线/离线、开机/关机 设置面板标题的背景色
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
	        		$modalDiv.find("div.panel").removeClass("panel-default panel-info").addClass("panel-danger");
	        	}
	        	
	        	$modalDiv.find("div.panel-heading #name").text( $tr.children("td:eq(1)").text() );  // name
	        	$modalDiv.find("div.panel-heading #room").text( $tr.children("td:eq(8)").text() );  // room
	        	$modalDiv.find("div.panel-body li").each(function(index){
	        		var tdIndex = index + 4;  // 视块与表格数据的对应顺序固定
	        		$(this).children("span").text( $tr.children("td:eq("+ tdIndex +")").text() );
	        	});
	        	
	        	// 图标点击事件
	        	$modalDiv.find("div.panel-footer a:eq(0)").click(function(){
	        		$("#edit_modal #form_name").next("p.form-control-static").text("");
	        		$("#edit_modal .modal-title").text("设备维护");
	        		$("#edit_modal #form_sid").val($tr.children("td:eq(0)").text());
	        		$("#edit_modal #form_name").val($tr.children("td:eq(1)").text());
	        		$("#edit_modal").modal(); 
	        	})
	        }
	    });
	}
	
    // 把表格的分页部分复制到视块视图
    $("#info_div").html("").append($("#monitorValueTable_info").clone(true));
    $("#pagination_div").html("").append($("#monitorValueTable_paginate").clone(true));
});

/**
 * 事件 -- 模态框保存 
 */
$("#edit_modal #modal_btn_save").click(function(){
	var $btn = $(this).button('loading');
    
	$("#edit_modal #form_name").next("p.form-control-static").text("");
	
	var name = $("#edit_modal #form_name").val();
	if(name.length<6 || !isNormalChar(name)){
		$("#edit_modal #form_name").next("p.form-control-static").addClass("text-danger").text("设备名不合法");
		return;
	}
	var sid = $("#edit_modal #form_sid").val();
	
	$.ajax({
		type: 'put',
		url: "deviceName/"+ sid +"/"+ name,
		success: function(result){
			$btn.button('reset');
			if(!result.success){
				$("#edit_modal #form_name").next("p.form-control-static").addClass("text-danger").text(result.msg);
				reutrn;
			}
			$("#edit_modal #form_name").next("p.form-control-static").addClass("text-success").text("保存成功");
		}
	});
});
