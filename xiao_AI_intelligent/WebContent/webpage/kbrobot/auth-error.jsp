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
	function close(){
		window.close();
	}
	
	$(document).ready(function(){
		setTimeout("close()",5000);
	});
</script>
</head>

<body>
	<div class="page-container">
		<div class="row" style="margin: 0;">
			<div class="col-lg-12 auth-header">
				<div class="col-lg-2">
					<a href="home.html">
						<img src="plug-in/kbrobot/images/logo1.png">
					</a>
				</div>
				<div class="col-lg-2">
					<h1>|&nbsp;&nbsp;微信对接</h1>
				</div>
			</div>
		</div>
		<div class="row" style="margin: 30px;">
			<div class="col-lg-12 auth-content">
				<div class="col-lg-5"></div>
				<div class="col-lg-2">
					<img src="plug-in/kbrobot/images/error.png">
					<h3>授权失败</h3>
					<h5>5秒后将关闭此窗口</h5>
				</div>
				<div class="col-lg-5"></div>
			</div>

		</div>

	</div>

</body>
</html>
