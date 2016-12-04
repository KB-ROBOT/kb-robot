<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>凯博机器人后台管理系统</title>
<jsp:include page="./includePage/linkSource.jsp"></jsp:include>
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
				<div class="panel col-sm-12">
					<div class="panel-cont">
						<h4 class="title">用户列表</h4>
						<div class="panel-body">
							<div class="row">
								<div class="col-lg-12" style="margin-bottom: 20px;">
									<form>
										<div class="col-sm-4">
											<a href="" class="btn btn-success">
												<i class="fa fa-plus" style="margin-right: 6px;"></i>新增
											</a>
											<a href="" class="btn btn-default">批量删除</a>
										</div>
										<div class="col-sm-4" style="float: right;">
											<div class="datatable-search">
												<input type="text" class="form-control" placeholder="请输入用户名或角色名称">
												<button type="reset">
													<i class="fa fa-search"></i>
												</button>
											</div>
										</div>

									</form>
								</div>
								<div class="col-lg-12">
									<table class="table table-model-2 roleTable">
										<tr>
											<th>序号</th>
											<th>用户名</th>
											<th>角色</th>
											<th>真实姓名</th>
											<th>联系方式</th>
											<th>操作</th>
										</tr>
										<!--<tr>
                                    	<td colspan="6" align="center">暂无数据</td>
                                    </tr>-->
										<tr>
											<td>1</td>
											<td>admin</td>
											<td>系统管理员</td>
											<td>李四</td>
											<td>12345678911</td>
											<td class="role-set">
												<a href="" title="编辑">
													<i class="fa fa-pencil"></i>编辑
												</a>
												|
												<a href="" title="删除">
													<i class="fa fa-trash-o"></i>删除
												</a>
												|
												<a href="" title="权限设置">
													<i class="fa fa-edit"></i>权限设置
												</a>
											</td>
										</tr>
									</table>
								</div>
								<!-- 页码 -->
								<div class="col-lg-12">
									<div class="col-sm-6">
										本页&nbsp;<b>0</b>&nbsp;条&nbsp;共&nbsp;<b>0</b>&nbsp;条
									</div>
									<div class="col-sm-6">
										<ul class="pagination fr">
											<li class="disabled">
												<a href="#">上一页</a>
											</li>
											<li>
												<a href="#" class="active">1</a>
											</li>
											<li>
												<a href="#">2</a>
											</li>
											<li>
												<a href="#">3</a>
											</li>
											<li>
												<a href="#">下一页</a>
											</li>
										</ul>
									</div>
								</div>
								<!-- 页码 -->
							</div>

						</div>

					</div>
				</div>
			</div>
		</div>

	</div>
</body>
</html>
