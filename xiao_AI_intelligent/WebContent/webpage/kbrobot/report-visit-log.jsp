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
		//开始和结束时间
		var startTime = null;
		var endTime = null;

		//开始时间
		$(".begin_datetime").datetimepicker({
			todayBtn : "linked",
			autoclose : true,
			todayHighlight : true,
			endDate : new Date(),
			format : "yyyy-mm-dd hh:ii",
			language : 'zh-CN'
		}).on('changeDate', function(e) {
			startTime = e.date;
			$('.end_datetime').datetimepicker("setStartDate", startTime);
		});

		//结束时间
		$(".end_datetime").datetimepicker({
			todayBtn : "linked",
			autoclose : true,
			todayHighlight : true,
			endDate : new Date(),
			format : "yyyy-mm-dd hh:ii",
			language : 'zh-CN'
		}).on('changeDate', function(e) {
			endTime = e.date;
			$('.begin_datetime').datetimepicker("setEndDate", endTime);
		});

		$(".export").click(function() {
			$("#searchForm").append('<input name="export" value="export"></input>');

			$("#searchForm").submit();
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
						<h4 class="title">访客日志</h4>
						<div class="panel-body">
							<div class="row">
								<div class="col-lg-12" style="margin-bottom: 20px;">
									<form action="dataReportController.do?report-visit-log" method="post" id="searchForm">
										<div class="col-sm-2">
											<span class="dropdown" style="padding: 10px 20px; background: #eee; top: 6px;">
												<a data-toggle="dropdown" class="dropdown-toggle" href="#">
													全部访客<b class="caret" style="margin-left: 30px;"></b>
												</a>
												<ul class="dropdown-menu" role="menu" style="min-width: 95px;">
													<li>
														<a href="#">全部访客</a>
													</li>
													<!-- <li>
														<a href="#">微信访客</a>
													</li>
													<li>
														<a href="#">网页访客</a>
													</li> -->
												</ul>
											</span>
										</div>
										<div class="col-sm-3">
											起始时间： <input type="text" name="startTime" value="${startTime}" class="form-control input-append begin_datetime" size="16" style="width: 60%; display: inline-block;">
										</div>
										<div class="col-sm-3">
											结束时间： <input type="text" name="endTime" value="${endTime}" class="form-control input-append end_datetime" size="16" style="width: 60%; display: inline-block;">
										</div>
										<div class="col-sm-2">
											<div class="btn-group">
												<button type="button" class="btn btn-success" onclick="submit();">
													<i class="fa fa-search visitLogSearch"></i>&nbsp;查询
												</button>
												<button type="reset" class="btn btn-white">清空</button>
											</div>
											<button class="btn btn-white export">导出</button>
										</div>
									</form>
								</div>
								<div class="col-lg-12">
									<table class="table table-model-2">
										<tr>
											<th>访客类型</th>
											<th>记录条数</th>
											<th>停留时间</th>
											<th>开始时间</th>
											<th>结束时间</th>
											<th>访客地址</th>
											<th>操作</th>
										</tr>
										<c:if test="${resultList.size()==0 }">
											<td colspan="7"></td>
										</c:if>
										<c:if test="${resultList.size()!=0 }">
											<c:forEach items="${resultList}" var="visitData" varStatus="status">
												<tr>
													<td>${visitData.visitType }</td>
													<td>${visitData.conversationCount }</td>
													<td>${visitData.stayTime }分钟</td>
													<td>${visitData.conversationStartTime }</td>
													<td>${visitData.conversationEndTime }</td>
													<td>${visitData.conversationAddress }</td>
													<td>
														<a href="#${visitData.conversationId }">查看聊天记录</a>
													</td>
												</tr>
											</c:forEach>
										</c:if>
									</table>
								</div>
							</div>
							<div class="row">${pageTools}</div>
						</div>

					</div>
				</div>
			</div>
		</div>

	</div>
</body>
</html>
