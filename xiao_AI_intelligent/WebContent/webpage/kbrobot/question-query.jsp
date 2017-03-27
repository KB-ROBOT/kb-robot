<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp" />

<title>问题查询</title>
<link rel="stylesheet" type="text/css" href="plug-in/kbrobot/css/mobile.css">
<link rel="stylesheet" type="text/css" href="plug-in/kbrobot/css/bootstrap.css" />

<script type="text/javascript" src="plug-in/kbrobot/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="plug-in/kbrobot/js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {

		$(".queryTitle").on("click", function() {

			var questionAnswer = $(this).find("div").html();
			$("#query_modal .content p").html(questionAnswer);

			$("#query_modal").modal("show");
		});
		
		$("select[name=searchKey]").val($("select[name=searchKey]").attr("value"));
	});
</script>
<style type="text/css">
.quesBox input,.quesBox select{width:70%;height:30px;line-height:30px;background:#fff;border:1px solid #dedede;padding:0 10px;}
.quesBox select{width:20%;}
.quesBox button{display:block;width:100%;background:#3fb2f3;border:none;color:#fff;text-align:center;font-size:1rem;line-height:2.4rem;margin-top:10px; letter-spacing:3px;}
.query-list {
	padding: 20px 0px;
}

.query-list li a {
	display: block;
	border-bottom: 1px solid #dedede;
	line-height: 20px;
	padding: 10px;
}

.queryTitle div {
	display: none;
}
</style>
</head>

<body>


	<div class="wrap">
		<h1 class="title">问题查询</h1>
		<div class="content">
			<div class="quesBox">
				<form id="search_form" action="robotQuestionController.do?questionQuery" method="post">
					<input type="hidden" name="accountId"  value="${accountId}" >
					<div class="notabs" id="cont1" style="display: block;">
						
						<select name="searchKey"  value="${searchKey}">
								<option value="questionTitle">问题</option>
								<option value="questionAnswer">答案</option>
						</select>
						<input type="text" name="searchParam" value="${searchParam}">
					</div>

					<button>查询</button>
				</form>
			</div>
			<ul class="query-list">
				<c:forEach items="${questionPageList.resultList }" var="question" varStatus="status">
					<li>
						<a href="javascript:void();" class="queryTitle">
							${status.index + 1} .&nbsp;${question.questionTitle}
							<div>${question.questionAnswer}</div>
						</a>
					</li>
				</c:forEach>
			</ul>
			<!-- 页码 -->
			<div class="row">${questionPageList.pager.getToolsBarByUrl()}</div>
			<!-- 页码 -->
		</div>

	</div>

	<!--弹出框-->
	<div class="modal fade" id="query_modal">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					答案
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				</div>
				<div class="modal-body">
					<div class="nano has-scrollbar">
						<div class="content" tabindex="0" style="right: -17px;">
							<p></p>
						</div>
					</div>
				</div>
				<div class="modal-footer">&nbsp;</div>
			</div>
		</div>
	</div>

</body>
</html>
