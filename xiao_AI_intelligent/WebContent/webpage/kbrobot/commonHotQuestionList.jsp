<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>热点问题排行榜</title>
<jsp:include page="./includePage/linkSource.jsp"></jsp:include>

<style type="text/css">
body{font-size:32px;}

#hot_modal .modal-header .close {
	margin-top: -19px;
}

#hot_modal .content  p {
	text-indent: 2em;
}

.table tr td {
	font-size: 0.32rem;
}

.table tr th {
	font-size: 0.32rem;
	text-align: center;
} 
</style>
<script type="text/javascript">
	$(document).ready(function() {

		$(".questionTile").on("click", function() {

			var questionAnswer = $(this).find("div").html();
			$("#hot_modal .content p").html(questionAnswer);

			$("#hot_modal").modal("show");
		});

	});
</script>
</head>
<body>
	<div class="page-container">

		<div class="main-content">

			<div class="container">
				<table>
					<tr>
						<th>问题</th>
						<th width="20%">被问次数</th>
					</tr>
					<c:if test="${topQuestionList.size()==0}">
						<tr>
							<td colspan="2" style="text-align: center;">暂时还没有热点问题，快去公众号问问题吧~~</td>
						</tr>
					</c:if>
					<c:if test="${topQuestionList.size()!=0}">
						<c:forEach items="${topQuestionList}" var="topQuestion" varStatus="status">
							<tr>
								<td>
									<a href="javascript:;" class="questionTile">
										${topQuestion.questionTitle }
										<div style="display: none;">${topQuestion.questionAnswer }</div>
									</a>
								</td>
								<td style="text-align: center;">${topQuestion.matchTimes }</td>

							</tr>
						</c:forEach>
					</c:if>
				</table>
			</div>
		</div>
	</div>

	<div class="modal fade" id="hot_modal">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				</div>
				<div class="modal-body">
					<div class="nano has-scrollbar">
						<div class="content" tabindex="0" style="right: -17px;">
							<p></p>
						</div>
					</div>
				</div>
				<div class="modal-footer"></div>
			</div>
		</div>
	</div>
</body>
</html>
