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
		$(".submit-btn").click(function() {
			var templateName = $("input[name='templateName']").val();
			var content = $("textarea[name='voiceText']").val();
			var id = $("input[name='id']").val();

			if (templateName == undefined || templateName == '') {
				alert("请填写标题！");
				return;
			}
			if (content == undefined || content == '') {
				alert("请填写内容！");
				return;
			}
			var formData = new Object();
			formData["templateName"] = templateName;
			formData["voiceText"] = content;
			formData["id"] = id;

			//执行添加
			var actionUrl = "./voiceTemplateController.do?doSave";
			$.ajax({
				// 获取id，challenge，success（是否启用failback）
				url : actionUrl,
				type : "post",
				data : formData,
				dataType : "json", // 使用json格式
				success : function(data) {
					//隐藏添加对话框
					$("#voiceTemplateAdd").modal('hide');
					var dialogReload = dialog({
						ok : function() {
							//关闭对话框，并刷新界面
							location.reload();
						}
					});

					if (data.success) {
						dialogReload.content(data.msg);
						dialogReload.showModal();
					} else {
						dialogReload.content(data.msg);
						dialogReload.showModal();
					}
				},
				error : function(data) {

				}
			});
		});
		var templateId = '';
		
		$(".deleteLink").click(function(){
			templateId = this.dataset.templateid;
		});
		$(".editLink").click(function() {
			templateId = this.dataset.templateid;
			//编辑
			var actionUrl = "./voiceTemplateController.do?goEdit";
			$.ajax({
				// 获取id，challenge，success（是否启用failback）
				url : actionUrl,
				type : "post",
				data : {"id":templateId},
				dataType : "json", // 使用json格式
				success : function(data) {

					if (data.success) {
						$("#voiceTemplateAdd").modal('show')
						$("input[name='templateName']").val(data.obj.templateName);
						$("textarea[name='voiceText']").val(data.obj.voiceText);
						$("input[name='id']").val(templateId);
					} else {
						alert(data.msg);
					}
				},
				error : function(data) {
					alert("编辑失败！");
				}
			});
		});
		
		$(".btn-ok").click(function(){
			if(templateId==''){
				return;
			}
			
			//执行删除
			var actionUrl = "./voiceTemplateController.do?del";
			$.ajax({
				// 获取id，challenge，success（是否启用failback）
				url : actionUrl,
				type : "post",
				data : {"id":templateId},
				dataType : "json", // 使用json格式
				success : function(data) {
					//隐藏添加对话框
					$("#delModal").modal('hide')
					var dialogReload = dialog({
						ok : function() {
							//关闭对话框，并刷新界面
							location.reload();
						}
					});

					if (data.success) {
						dialogReload.content(data.msg);
						dialogReload.showModal();
					} else {
						dialogReload.content(data.msg);
						dialogReload.showModal();
					}
				},
				error : function(data) {
					alert("删除失败！");
					location.reload();
				}
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
				<div class="panel col-sm-6">
					<div class="panel-cont">
						<h4 class="title">
							语音消息列表
							<a data-toggle="modal" data-target="#voiceTemplateAdd" href="javascript:;" class="fa fr addBtn" title="添加">
								<i class="fa fa-plus-circle"></i>添加语音消息
							</a>
						</h4>
						<table class="table table-model-2">
							<tr>
								<th>标题</th>
								<th>内容</th>
								<th>操作</th>
							</tr>
							<c:if test="${voiceTemplateList.size()==0}">
								<tr>
									<td align="center" colspan="3">还未添加任何语音消息</td>
								</tr>
							</c:if>
							<c:forEach items="${voiceTemplateList}" var="voiceTemplate" varStatus="status">
								<tr>
									<td>${voiceTemplate.templateName}</td>
									<td width="50%">${fn:substring(voiceTemplate.voiceText,0,50)}</td>
									<td width="10%">
										<a href="javascript:;" data-templateid="${voiceTemplate.id}" class="fa fa-pencil editLink" title="编辑" style="margin-right: 5px;"></a>
										<a href="#" data-toggle="modal" data-target="#delModal" data-templateid="${voiceTemplate.id}" class="fa fa-trash-o deleteLink" title="删除"></a>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>

	</div>
	
	<!-- 添加语音消息 -->
	<div class="modal fade" id="voiceTemplateAdd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="myModalLabel">语音消息</h3>
				</div>
				<div class="panel-cont">
					<form>
						<input type="hidden" name="id">
						<div class="form-group">
							<label class="control-label">标题</label> <input type="text" class="form-control" name="templateName" maxlength="50">
						</div>
						<div class="form-group">
							<label class="control-label">内容</label>
							<textarea class="form-control" name="voiceText" maxlength="255"></textarea>
						</div>
					</form>
					<a href="javascript:void(0);" class="submit-btn">保存</a>
				</div>
			</div>
		</div>
	</div>
	<!-- 添加语音消息结束 -->
	<!-- 删除确认 -->
	<div class="modal small fade" id="delModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="myModalLabel">删除确认</h3>
				</div>
				<div class="modal-body">
					<p class="error-text">
						<i class="icon-warning-sign modal-icon"></i>确认删除吗?
					</p>
				</div>
				<div class="modal-footer">
					<button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
					<a class="btn btn-danger btn-ok">确认</a>
				</div>
			</div>
		</div>

	</div>
	<!-- 删除确认结束 -->
	
</body>
</html>
