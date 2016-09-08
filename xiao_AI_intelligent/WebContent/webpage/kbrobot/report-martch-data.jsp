<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%@include file="/context/mytags.jsp"%>
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
				<div class="panel col-sm-12">
					<div class="panel-cont">
						<h4 class="title">知识库统计</h4>
						<div class="panel-body">
							<div class="col-lg-12">
								<table class="table table-model-2">
									<tr>
										<th>类别</th>
										<th>问题数量</th>
										<th>占比</th>
									</tr>
									<tr>
										<td>产品概览</td>
										<td>6</td>
										<td>60%</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
	<script type="text/ecmascript" src="js/jquery-ui.js"></script>
	<script type="text/ecmascript" src="js/jquery-ui.min.js"></script>
	<script type="text/ecmascript" src="js/bootstrap-datetimepicker.js"></script>
	<script type="text/ecmascript" src="js/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/ecmascript" src="js/style.js"></script>
</body>
</html>
