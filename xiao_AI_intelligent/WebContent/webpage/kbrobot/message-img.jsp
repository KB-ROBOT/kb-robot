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
		
		//删除
		$(".newsDel").on("click",function(){
			var newsId = $(this).data("newsid");
			dialog({
			    title: '消息',
			    content:"确认删除吗？",
			    ok: function () {
			    	var url = "./newsTemplateController.do?del";
					$.ajax({
						url : url,
		                dataType:"json",
						data: {"id":newsId},
						success : function(data) {
							if(data.success){
								setTimeout("location.reload()",500);
							}
							else{
								dialog({
									content:data.msg,
									title:"错误消息"
								}).show();
							}
						},
						error : function() {// 请求失败处理函数
							
						},
					});
			    },
			    cancel: function () {
			    }
			})
			.width(320)
			.show();
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
				<div class="col-sm-1"></div>
				<div class="panel col-sm-10">
					<div class="panel-cont">
						<h4 class="title">
							图文消息列表
							<a href="newsTemplateController.do?goAddNewsTemplate" class="fa fr addBtn">
								<i class="fa fa-plus-circle"></i>添加图文消息
							</a>
						</h4>

						<div class="col-lg-12">
							<!-- 图文列表  -->
							<c:forEach items="${newsList}" var="news" varStatus="status">
								<div class="col-lg-4">
									<div class="doubleImg-list">
										<div class="doubleImg-box">
											<h5>${news.templateName }</h5>
											<p>${news.addTime }</p>
											<c:forEach items="${news.newsItemList}" var="newsItem" begin="0" end="0" varStatus="status">
												<div>
													<img src="${newsItem.imagePath}">
												</div>
											</c:forEach>
										</div>
										<ul>
											<c:forEach items="${news.newsItemList}" var="newsItem" begin="1" varStatus="status">
												<li>
													<h5>${newsItem.title}</h5>
													<span class="fr">
														<img src="${newsItem.imagePath}">
													</span>
												</li>
											</c:forEach>
										</ul>
										<div class="Img-btn">
											<a href="newsTemplateController.do?goNewsTemplateEdit&id=${news.id}" class="fa fa-pencil"></a>
											<a href="javascript:void(0)" data-newsid="${news.id}" class="fa fa-trash-o newsDel"></a>
										</div>
									</div>
								</div>
							</c:forEach>
							<!-- 图文列表  -->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
