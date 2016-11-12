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
	$(document).ready(function(){
		$(".detailBtn").click(function(){
			var content = $(this).find("div").text();
			$(".messageContent").text(content);
			$("#advice_modal").modal("show");
		});
		
		$(".select").click(function(){
			var id = $(this).data("id");
			
			var url = "./commonAdviceController.do?confirmReply";
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				dataType : "json",
				data : {
					"id" : id
				},
				success : function(data) {
					setTimeout("location.reload()",500);
				},
				error : function() {// 请求失败处理函数

				},
			});
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
						<h4 class="title">问题与建议</h4>
						<div class="row">
							<div class="adviceBox col-lg-12">
								<table class="table table-model-2 table-hover">
									<tr>
										<th>序号</th>
										<th>姓名</th>
										<th>联系方式</th>
										<th>操作</th>
									</tr>
									<c:forEach items="${customerAdviceList}" var="customerAdvice" varStatus="status">
										<tr>
											<td>${status.index+1}</td>
											<td>${customerAdvice.name }</td>
											<td>${customerAdvice.mobile}</td>
											<td>
												<button class="detailBtn">
													查看
													<div style="display:none;">
														${customerAdvice.content}
													</div>
												</button>
												<c:if test="${customerAdvice.isReply==0}">
													<a href="#" data-id="${customerAdvice.id}" class="select">标记已回复</a>
												</c:if>
												<c:if test="${customerAdvice.isReply!=0}">
													<a class="uncheck">已回复</a>
												</c:if>
												
											</td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
						<!-- 页码 -->
						<%-- <div class="row">${questionPageList.pager.getToolsBarByUrl()}</div> --%>
						<!-- 页码 -->
					</div>
				</div>
			</div>
		</div>

	</div>

	<!--留言内容-->
	<div id="advice_modal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">留言内容</h4>
				</div>
				<div class="modal-body">
					<p class="messageContent"></p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">确定</button>

				</div>
			</div>
		</div>
	</div>
	<!--留言内容结束-->
</body>
</html>
