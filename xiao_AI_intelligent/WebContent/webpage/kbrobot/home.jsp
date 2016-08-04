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
				<div class="row">
					<div class="col-lg-12">
						<div class="col-lg-3">
							<div class="dashboard-stat2">
								<div class="dashboard-cont">
									<div class="fl">
										<h3>0</h3>
										<p>昨日访问量</p>
									</div>
									<span class="fr">
										<i class="fa fa-eye"></i>
									</span>
								</div>
								<div class="dashboard-bar" style="background: #4DB3A2;">
									<span class="progress-bar"></span>
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="dashboard-stat2">
								<div class="dashboard-cont">
									<div class="fl">
										<h3>0</h3>
										<p>昨日聊天数量</p>
									</div>
									<span class="fr">
										<i class="fa fa-comment-o"></i>
									</span>
								</div>
								<div class="dashboard-bar">
									<span class="progress-bar"></span>
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="dashboard-stat2">
								<div class="dashboard-cont">
									<div class="fl">
										<h3>0</h3>
										<p>昨日未知问题数量</p>
									</div>
									<span class="fr">
										<i class="fa fa-info"></i>
									</span>
								</div>
								<div class="dashboard-bar" style="background: #f36a5a;">
									<span class="progress-bar"></span>
								</div>
							</div>
						</div>
						<div class="col-lg-3">
							<div class="dashboard-stat2">
								<div class="dashboard-cont">
									<div class="fl">
										<h3>0</h3>
										<p>昨日新增知识数量</p>
									</div>
									<span class="fr">
										<i class="fa fa-lightbulb-o"></i>
									</span>
								</div>
								<div class="dashboard-bar" style="background: #8877a9;">
									<span class="progress-bar"></span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
