<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>凯博机器人后台管理系统</title>
<jsp:include page="./includePage/linkSource.jsp"></jsp:include>
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
						<h4 class="title">
							电话客服列表
							<a data-toggle="modal" data-target="#customAdd" href="javascript:;" class="fa fr addBtn" title="添加">
								<i class="fa fa-plus-circle"></i>添加客服
							</a>
						</h4>
						<table class="table table-model-2">
							<tr>
								<th>序号</th>
								<th>姓名</th>
								<th>头像</th>
								<th>所属部门</th>
								<th>电话号码</th>
								<th>操作</th>
							</tr>
							<c:if test="${customList.size()==0}">
								<tr>
									<td align="center" colspan="6">还未添加任何客服</td>
								</tr>
							</c:if>
							<c:forEach items="${customList}" var="custom" varStatus="status">
								<tr>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td align="center">
										<a href="javascript:void(0);" data-customid="${custom.id}" class="kfEdit">编辑</a>
										<a href="javascript:void(0);" class="kfDel" data-customid="${custom.id}">删除</a>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 添加客服 -->
	<div class="modal fade" id="customAdd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="myModalLabel">添加客服</h3>
				</div>
				<div class="panel-cont">
					<input type="hidden" name="id">
					<div class="form-group">
						<label class="control-label">姓名：</label>
						<input type="text" class="form-control" name="accountNum" maxlength="10">
					</div>
                    <div class="form-group">
						<label class="control-label">头像：</label>
						<input type="text" class="form-control" name="accountNum" maxlength="10">
					</div>
                    <div class="form-group">
						<label class="control-label">所属部门：</label>
						<input type="text" class="form-control" name="accountNum" maxlength="10">
					</div>
					<div class="form-group">
						<label class="control-label">电话号码：</label> <input type="tel" class="form-control" name="nickname" maxlength="11">
					</div>
					<a href="javascript:void(0);" class="submit-btn submitCustomAdd">保存</a>
				</div>
			</div>
		</div>
	</div>
	<!-- 添加客服 -->

	<!-- 编辑客服 -->
	<div class="modal fade" id="customEdit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="myModalLabel">编辑客服</h3>
				</div>
				<div class="panel-cont">
					<input type="hidden" name="customid">
					<div class="form-group">
						<label class="control-label">姓名：</label>
						<input type="text" class="form-control" name="nickname" maxlength="16">
					</div>
					<div class="form-group">
						<label class="control-label">上传头像</label>
						<input id="fileupload" type="file"  data-url="weixinArticleController.do?upload" onClick="uploadHeadImg(this)"\>
						<input type="hidden" name="headImg" />
						<div id="progress_headImg" class="progress">
							<div class="progress-bar progress-bar-success" style="width:0%;"></div>
						</div>
					</div>
					<a href="javascript:void(0);" class="submit-btn submitCustomEdit">保存</a>
				</div>
			</div>
		</div>

	</div>
	<!-- 编辑客服 -->
</body>
</html>
