$(function() {

	// init date tables
	var jobTable = $("#cache_list").dataTable({
		"deferRender": true,
		"processing" : true, 
	    "serverSide": true,
		"ajax": {
			url: base_url + "/cache/pageList",
			type:"post",
	        data : function ( d ) {
	        	var obj = {};
	        	obj.key = $('#key').val();
	        	obj.start = d.start;
	        	obj.length = d.length;
                return obj;
            }
	    },
	    "searching": false,
	    "ordering": false,
	    //"scrollX": true,	// X轴滚动条，取消自适应
	    "columns": [
	                { "data": 'id', "bSortable": false, "visible" : false},
	                { "data": 'key', "visible" : true},
					{ "data": 'intro', "visible" : true},
	                { "data": '操作' ,
	                	"render": function ( data, type, row ) {
	                		return function(){
								// html
								var html = '<p id="'+ row.id +'" '+
									' key="'+ row.key +'" '+
									' intro="'+ row.intro +'" '+
									'>'+
									'<button class="btn btn-primary btn-xs cache_manage" type="button">缓存操作</button>  '+
									'<button class="btn btn-warning btn-xs cache_update" type="button">编辑</button>  '+
									'<button class="btn btn-danger btn-xs cache_delete" type="button">删除</button>  '+
									'</p>';

	                			return html;
							};
	                	}
	                }
	            ],
		"language" : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "每页 _MENU_ 条记录",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页 )",
			"sInfoEmpty" : "无记录",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}
	});
	
	// search
	$('#searchBtn').on('click', function(){
		jobTable.fnDraw();
	});
	
	// cache_delete
	$("#cache_list").on('click', '.cache_delete',function() {
		var id = $(this).parent('p').attr("id");
		ComConfirm.show("确认删除缓存模板?", function(){
			$.ajax({
				type : 'post',
				url : base_url + "/cache/delete",
				data : {
					"id":id
				},
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
						ComAlert.show(1, "删除成功", function(){
							//window.location.reload();
							jobTable.fnDraw();
						});
					} else {
						toastr.error("删除失败");
					}
				},
			});
		});
	});
	
	// jquery.validate 自定义校验 “英文字母开头，只含有英文字母、数字和下划线”
	jQuery.validator.addMethod("keyValid", function(value, element) {
		var length = value.length;
		var valid = /^[a-zA-Z][a-zA-Z0-9_{}]*$/;
		return this.optional(element) || valid.test(value);
	}, "缓存Key应该以英文字母开头，只可包含英文字母、数字、下划线以及占位符'{}'");
	
	// 新增
	$(".add").click(function(){
		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {
			key : {
				required : true,
				keyValid : true
			},
			intro : {
            	required : true
            }
        }, 
        messages : {
			key : {
            	required :"请输入“缓存模板”"
            },
			intro : {
            	required :"请输入“缓存简介””"
            }
        },
		highlight : function(element) {  
            $(element).closest('.form-group').addClass('has-error');  
        },
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        },
        submitHandler : function(form) {
        	$.post(base_url + "/cache/save",  $("#addModal .form").serialize(), function(data, status) {
    			if (data.code == "200") {
    				ComAlert.show(1, "新增任务成功", function(){
    					//window.location.reload();
						$('#addModal').modal('hide');
						jobTable.fnDraw();
    				});
    			} else {
    				if (data.msg) {
						toastr.error(data.msg);
    				} else {
						toastr.error("新增失败");
					}
    			}
    		});
		}
	});
	$("#addModal").on('hide.bs.modal', function () {
		$("#addModal .form")[0].reset();
		addModalValidate.resetForm();
		$("#addModal .form .form-group").removeClass("has-error");
		$(".remote_panel").show();	// remote
	});


	// 更新
	$("#cache_list").on('click', '.cache_update',function() {
		// base data
		$("#updateModal .form input[name='id']").val($(this).parent('p').attr("id"));
		$("#updateModal .form input[name='key']").val($(this).parent('p').attr("key"));
		$("#updateModal .form input[name='intro']").val($(this).parent('p').attr("intro"));

		// show
		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,
		rules : {
			key : {
				required : true,
				keyValid : true
			},
			intro : {
				required : true
			}
		},
		messages : {
			key : {
				required :"请输入“缓存模板”"
			},
			intro : {
				required :"请输入“缓存简介”"
			}
		},
		highlight : function(element) {
            $(element).closest('.form-group').addClass('has-error');  
        },
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        },
        submitHandler : function(form) {
			// post
    		$.post(base_url + "/cache/update", $("#updateModal .form").serialize(), function(data, status) {
    			if (data.code == "200") {
    				ComAlert.show(1, "更新成功", function(){
    					//window.location.reload();
						$('#updateModal').modal('hide');
						jobTable.fnDraw();
    				});
    			} else {
    				if (data.msg) {
						toastr.error(data.msg);
					} else {
						toastr.error("更新失败");
					}
    			}
    		});
		}
	});
	$("#updateModal").on('hide.bs.modal', function () {
		$("#updateModal .form")[0].reset()
	});

	// 缓存管理
	$("#cache_list").on('click', '.cache_manage',function() {
		// base data
		$("#cacheManageModal .form input[name='key']").val($(this).parent('p').attr("key"));

		// hide cacheDetail
		$("#cacheManageModal .cacheDetail").hide();
		$("#cacheManageModal .cacheVal").html('');

		// show
		$('#cacheManageModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	$("#cacheManageModal").on('hide.bs.modal', function () {
		$("#cacheManageModal .form")[0].reset()
	});

	// get cache
	$("#cacheManageModal").on('click', '.getCache',function() {

		// hide cacheDetail
		$("#cacheManageModal .cacheDetail").hide();
		$("#cacheManageModal .cacheVal").html('');

		// data used to get final key
		var key = $("#cacheManageModal .form input[name='key']").val();
		var params = $("#cacheManageModal .form input[name='params']").val();


		$.post(base_url + "/cache/getCacheInfo", {"key":key, "param":params}, function(data, status) {
			if (data.code == "200") {
				var temp = '';
				temp += '<b>FinalKey:</b> ' + data.content.finalKey + '<br><br>';
				temp += '<b>类型:</b> ' + data.content.type + '<br><br>';
				temp += '<b>长度:</b> ' + data.content.length + '<br><br>';
				temp += '<b>内容:</b> ' + data.content.info + '<br><br>';
				temp += '<b>JSON:</b> ' + data.content.json + '<br><br>';

				// show cacheDetail
				$("#cacheManageModal .cacheDetail").show();
				$("#cacheManageModal .cacheVal").html(temp);
			} else {
				if (data.msg) {
					toastr.error(data.msg, null, {"progressBar": true, "positionClass": "toast-top-center"});
				} else {
					toastr.error("查询失败", null, {"progressBar": true, "positionClass": "toast-top-center"});
				}
			}
		});

	});

	// remove cache
	$("#cacheManageModal").on('click', '.removeCache',function() {

		// hide cacheDetail
		$("#cacheManageModal .cacheDetail").hide();
		$("#cacheManageModal .cacheVal").html('');

		// data used to get final key
		var key = $("#cacheManageModal .form input[name='key']").val();
		var params = $("#cacheManageModal .form input[name='params']").val();

		ComConfirm.show("确认清除缓存?", function(){

			$.post(base_url + "/cache/removeCache", {"key":key, "param":params}, function(data, status) {
				// finakKey
				var finalKey = data.content;

				if (data.code == "200") {
					toastr.success("缓存清除成功", null, {"progressBar": true, "positionClass": "toast-top-center"});
				} else {
					if (data.msg) {
						toastr.error(data.msg, null, {"progressBar": true, "positionClass": "toast-top-center"});
					} else {
						toastr.error("缓存清除失败", null, {"progressBar": true, "positionClass": "toast-top-center"});
					}
				}
			});

		});
	});

});
