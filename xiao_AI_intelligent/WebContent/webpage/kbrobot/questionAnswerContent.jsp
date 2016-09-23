<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${question.questionTitle}</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="format-detection" content="telephone=no">
<script type="text/javascript">
            document.domain = "qq.com";
            var _wxao = window._wxao || {};
            _wxao.begin = ( + new Date());
        </script>
<link rel="stylesheet" type="text/css" href="plug-in/weixin/core/wx-article/client-page1e4a15.css">
<!--[if lt IE 9]>
            <link rel="stylesheet" type="text/css" href="http://res.wx.qq.com/mmbizwap/zh_CN/htmledition/style/pc-page1ea1b6.css"
            />
        <![endif]-->
<link media="screen and (min-width:1000px)" rel="stylesheet" type="text/css" href="plug-in/weixin/core/wx-article/pc-page1ea1b6.css">
<style>
body {
	-webkit-touch-callout: none;
	-webkit-text-size-adjust: none;
}
</style>
<style>
#nickname {
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
	max-width: 90%;
}

.page-toolarea a.random_empha {
	color: #607fa6;
}

ol, ul {
	list-style-position: inside;
}

#activity-detail .page-content .text {
	font-size: 16px;
}
</style>
</head>

<body id="activity-detail">
	<img width="12px" style="position: absolute; top: -1000px;" src="plug-in/weixin/core/wx-article/ico_loading1984f1.gif">
	<div class="wrp_page">
		<div class="page-bizinfo">
			<div class="header">
				<h1 id="activity-name">${question.questionTitle}</h1>
				<p class="activity-info">
					<span id="post-date" class="activity-meta no-extra">
						<fmt:formatDate value='${question.createTime}' type="date" pattern="yyyy-MM-dd" />
					</span>
					<span class="activity-meta"> <a href="http://www.kb-robot.com/about.html">凯博智能机器人</a> </span>
				</p>
			</div>
		</div>
		<div id="page-content" class="page-content" lang="en">
			<div id="img-content">
				<div class="text" id="js_content">${question.questionAnswer}</div>
			</div>
		</div>
	</div>
</body>

</html>