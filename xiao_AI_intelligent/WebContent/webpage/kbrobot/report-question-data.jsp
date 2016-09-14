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
						<h4 class="title">问题明细统计</h4>
						<div class="panel-body">
							<div class="col-lg-12">
								<table class="table table-model-2">
									<tr>
										<th width="5%">序号</th>
										<th>问题</th>
										<th  width="5%">被问次数</th>
									</tr>

									<c:if test="${topQuestionList.size()==0 }">
										<tr>
											<td colspan="4" align="center">暂无数据</td>
										</tr>
									</c:if>
									<c:if test="${topQuestionList.size()!=0 }">
										<c:forEach items="${topQuestionList}" var="topQuestion" varStatus="status">
											<tr>
												<td style="text-align: center;">${status.index+1 }</td>
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
				</div>
			</div>
		</div>

	</div>

	<div class="modal fade" id="hot_modal">
		<div class="modal-dialog" style="width: 80%">
			<div class="modal-content">
				<div class="modal-header">
					&nbsp;
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
