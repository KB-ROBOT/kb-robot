<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>凯博机器人后台管理系统</title>
<jsp:include page="./includePage/linkSource.jsp"></jsp:include>

<script type="text/javascript">
	$(document).ready(function() {
		//上传新文件
		$(".uploadNewFile").click(function() {
			$("#fileUploadModal").modal("show");
		});

		//文件上传
		$("#fileUpload").fileupload({
			url : "./commonFileDownloadController.do?uploadFile",
			dataType : 'json',
			//acceptFileTypes : /(\.|\/)(ppt|pptx|xlsx|xls)$/i,
			maxFileSize : 15000000, // 15 MB
			add : function(e, data) {
				
				$("#fileArea").text(data.files[data.files.length-1].name);
				
				//data.files = data.files.splice(0,data.files.length-1);
				
				$('.uploadFileSubmit').unbind();
				
				data.context = $('.uploadFileSubmit').click(function() {
					
					data.formData = {
						"fileType" : "excel"
					};

					data.submit();
				});
			},
			done : function(e, data) {
				if (data.result.success) {
					
					setTimeout(location.reload(),500);
					
					
				} else {
					dialog({
						title : '上传出错',
						content : data.result.msg,
						ok : function() {

						}
					}).width(320).showModal();
					$("#progress .progress-bar").css('width', '0%');
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				//alert("error");
				$("#progress .progress-bar").css('width', '0%');
			},
			progressall : function(e, data) {
				var progress = parseInt(data.loaded / data.total * 100, 10);
				$("#progress .progress-bar").css('width', progress + '%');
			},
			dropZone : $('#dropzone')
		//拖拽区域
		});
		
		
		$(".fileDel").click(function(){
			var delurl = "./" + $(this).data("delurl");
			var id = $(this).data("id");
			
			
			
			
			
			
			//var url = "./commonAdviceController.do?addLeaveMessage&accountId=" + accountId;
			$.ajax({
				url : delurl,// 请求的action路径
				type : 'post',
				dataType : "json",
				success : function(data) {
					if (data.success) {
					}
					else {
						dialog({
							title : '出错',
							content : data.result.msg,
							ok : function() {

							}
						}).width(320).showModal();
					}
				},
				error : function() {// 请求失败处理函数
					dialog({
						title : '出错',
						content : "出错了,稍后再试",
						ok : function() {

						}
					}).width(320).showModal();
				},
			});
			
			//
			var url = "./commonFileDownloadController.do?delFile";
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				dataType : "json",
				data : {
					"id" : id
				},
				success : function(data) {
					if (data.success) {
						dialog({
							title : '操作成功',
							content : "文件删除成功",
							ok : function() {
								setTimeout(location.reload(),500);
							}
						}).width(320).showModal();
					}
					else {
						dialog({
							title : '出错',
							content : data.result.msg,
							ok : function() {

							}
						}).width(320).showModal();
					}
				},
				error : function() {// 请求失败处理函数
					dialog({
						title : '出错',
						content : "出错了,稍后再试",
						ok : function() {

						}
					}).width(320).showModal();
				},
			});
		});
	});
</script>
</head>

<body>
	<div class="page-container">
		<!-- 左侧导航start -->
		<jsp:include page="./includePage/sidebarSection.jsp"></jsp:include>
		<!-- 左侧导航end -->

		<div class="main-content">
			<!-- 顶部导航start -->
			<jsp:include page="./includePage/nvbarSection.jsp"></jsp:include>
			<!-- 顶部导航end -->

			<div class="section row">
				<div class="col-sm-3"></div>
				<div class="panel col-sm-12">
					<div class="cont">
						<div class="panel-body">
							<div class="col-lg-12" style="margin-bottom: 50px;">
								<div class="cont">
									<h4 class="title">
										表格列表
										<a data-toggle="modal" data-target="#customAdd" href="javascript:;" class="fa fr addBtn uploadNewFile" title="添加">
											<i class="fa fa-plus-circle"></i>上传新文件
										</a>
									</h4>
									<table class="table table-model-2">
										<tr>
											<th>序号</th>
											<th>文件名</th>
											<th>上传日期</th>
											<th>操作</th>
										</tr>
										<c:if test="${pageList.resultList.size()==0}">
											<tr>
												<td align="center" colspan="8">还未添加任何表格</td>
											</tr>
										</c:if>
										<c:forEach items="${pageList.resultList}" var="entity" varStatus="status">
											<tr>
												<td>${status.index + 1}</td>
												<td>${entity.fileName}</td>
												<td>${entity.createDate}</td>
												<td align="center">
													<a href="javascript:void(0);" class="fileDel" data-delurl="${ entity.delUrl }" data-id="${entity.id}">删除</a>
												</td>
											</tr>
										</c:forEach>
									</table>
								</div>
								<!-- 页码 -->
								<div class="row">${pageList.pager.getToolsBarByUrl()}</div>
								<!-- 页码 -->
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>


	<div class="modal fade" id="fileUploadModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">上传文件</h4>
				</div>

				<div class="modal-body">
					<label class="control-label">选择文件：</label>
					<div class="form-group">

						<div style="margin-top: 10px; position: relative;">
							<span class="btn btn-success fileinput-button">
								<i class="fa fa-plus"></i>
								<span>请选择文件</span>
								<input id="fileUpload" type="file" name="file">
							</span>
						</div>
						
						<div id="fileArea" style="margin: 15px 0 0 0 ;font-size:20px;font-weight:500;border:1px dashed #dedede;">
							
						</div>

					</div>

					<!-- <div class="form-group">
						<label class="control-label">文件类型：</label> <select class="form-control" name="fileType">
							<option value="excel">表格</option>
							<option value="pwoerPoint">课件</option>
						</select>
					</div> -->

					<div class="form-group">
						<label class="control-label">上传进度：</label>
						<div id="progress" class="progress">
							<div class="progress-bar progress-bar-success" style="width: 0%;"></div>
						</div>
					</div>

				</div>

				<div class="modal-footer">
					<input type="hidden" name="id">
					<button type="button" class="btn btn-info uploadFileSubmit">确认上传</button>
					<button type="button" class="btn btn-white uploadCancle" data-dismiss="modal">取消上传</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
