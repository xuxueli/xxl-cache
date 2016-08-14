<!DOCTYPE html>
<html>
<head>
  	<title>${CACHE_ENUM}缓存管理</title>
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">

	<#-- select2
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/select2/select2.min.css">
    <script src="${request.contextPath}/static/adminlte/plugins/select2/select2.min.js"></script>
    //$(".select2").select2();
    -->

</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>${CACHE_ENUM}缓存管理<small></small></h1>
			<!--
			<ol class="breadcrumb">
				<li><a><i class="fa fa-dashboard"></i>调度管理</a></li>
				<li class="active">调度中心</li>
			</ol>
			-->
		</section>
		
		<!-- Main content -->
	    <section class="content">
	    
	    	<div class="row">
                <div class="col-xs-4">
                    <div class="input-group">
                        <span class="input-group-addon">缓存模板</span>
                        <input type="text" class="form-control" id="key" value="${key}" autocomplete="on" >
                    </div>
                </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-info" id="searchBtn">搜索</button>
	            </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-success add" type="button">新增缓存模板</button>
	            </div>
          	</div>
	    	
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <#--<div class="box-header">
			            	<h3 class="box-title">调度列表</h3>
			            </div>-->
			            <div class="box-body">
			              	<table id="cache_list" class="table table-bordered table-striped">
				                <thead>
					            	<tr>
					            		<th name="id" >id</th>
					                	<th name="key" >缓存模板</th>
                                        <th name="intro" >缓存简介</th>
					                  	<th>操作</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
				                <tfoot></tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
	    </section>
	</div>
	
	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<!-- 新增.模态框 -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >新增缓存模板</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">缓存模板<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="key" placeholder="请输入“缓存Key”,占位符用{0}、{1}、{2}依次替代" maxlength="250" ></div>
					</div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">缓存简介<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="intro" placeholder="请输入“简介”" maxlength="100" ></div>
                    </div>
                    <hr>
					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-6">
							<button type="submit" class="btn btn-primary"  >保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>

<!-- 更新.模态框 -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >更新缓存模板</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">缓存模板<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="key" placeholder="请输入“缓存Key,占位符用{0}、{1}、{2}依次替代”" maxlength="250" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">缓存描述<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="intro" placeholder="请输入“简介”" maxlength="100" ></div>
                    </div>
                    <hr>
                    <div class="form-group">
                        <div class="col-sm-offset-4 col-sm-6">
                            <button type="submit" class="btn btn-primary"  >保存</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
							<input type="hidden" name="id" />
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 缓存操作.模态框 -->
<div class="modal fade" id="cacheManageModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >缓存管理</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">缓存模板<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="key" placeholder="请输入“缓存Key”" maxlength="250" readonly ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">缓存参数<font color="black">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="params" placeholder="多个参数逗号分隔,依次替换占位符{0}、{1}、{2}的位置" maxlength="100" ></div>
                    </div>
                    <hr>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-8">
                            <button type="button" class="btn btn-primary getCache" >查询缓存</button>
                            <button type="button" class="btn btn-primary removeCache" >清除缓存</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </div>
                    </div>
                    <hr>
                    <div class="form-group cacheDetail">
                        <label for="lastname" class="col-sm-2 control-label">缓存详情:</label>
                        <div class="col-sm-8"><div class="cacheVal"></div></div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<@netCommon.commonScript />
<@netCommon.comAlert />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<!-- daterangepicker -->
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/daterangepicker.js"></script>
<script src="${request.contextPath}/static/js/cache.index.1.js"></script>
</body>
</html>
