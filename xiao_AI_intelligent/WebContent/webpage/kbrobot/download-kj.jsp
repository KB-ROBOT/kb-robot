<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp" />

<title>课件下载</title>
<link rel="stylesheet" type="text/css" href="plug-in/kbrobot/css/mobile.css">
<link rel="stylesheet" type="text/css" href="plug-in/kbrobot/css/sweet-alert.css">

<script type="text/javascript" src="plug-in/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="plug-in/kbrobot/js/sweet-alert.min.js"></script>

</head>

<body>


	<div class="wrap">
		<h1 class="title">
			课件下载
		</h1>
		<div class="search">
			<input type="search" placeholder="搜索">
			<a href="">搜索</a>
		</div>
		<div class="content">
			<div class="downBox">
				<ul class="downList">
					<c:forEach items="${pageList.resultList}" var="entity" varStatus="status">
						<li>
							<p>${entity.fileName}</p>
							<a class="downloadFile" href="./commonFileDownloadController.do?commonDownloadFile&fileKey=${entity.fileKey}">下载</a>
						</li>
					</c:forEach>
				</ul>
			</div>

		</div>
	</div>




</body>
</html>