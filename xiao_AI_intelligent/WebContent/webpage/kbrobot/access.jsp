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

			<div class="section">
				<div class="row" style="width: 900px; margin: 100px auto;">
					<div class="col-sm-12">
						<div class="col-sm-6">
							<div class="knowledge">
								<a href="access-web.html">
									<img src="images/web.png">
								</a>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="knowledge">
								<a href="access-wx.html">
									<img src="images/wechat.png">
								</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
</body>
</html>
