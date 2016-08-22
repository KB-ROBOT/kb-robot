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
<script type="text/javascript">
	$(document).ready(function() {

		//富文本初始化
		var globalEditor = UE.getEditor('questionAnswer');

		//添加相似问法
		$("#add_Que_similar").click(function() {
			$("#add_similar").append(function(n) {
				return "<div class=\"form-group simi_group\">\
				<label class=\"control-label\">相似问法" + ($("#add_similar .simi_group").length + 1) + "</label>\
				<div class=\"input-group input-group-minimal\">\
				<input name=\"similarQuestion\" class=\"form-control\" type=\"text\">\
				<span class=\"input-group-btn\">\
				<a href=\"javascript:;\" class=\"del_que btn btn-white del_que_similar\">\
				<i class=\"fa fa-minus-square-o\"></i>\
				</a>\
				</span>\
				</div>\
				</div>";
			});
			//删除
			$(".del_que_similar").on("click", function() {
				var groupParent = $(this).parent().parent();
				var content = groupParent.find("input[name=similarQuestion]:eq(0)").val();
				if (content == undefined || content == '') {
					$(groupParent.parent()).remove();
				} else {
					dialog({
						title : "警告",
						content : "内容不为空，确认删除吗？",
						ok : function() {
							$(groupParent.parent()).remove();
						},
						cancle : function() {
						}
					}).width(320).showModal();
				}

			});
		});
		
		//提交
		$(".question-add-submit").click(function(){
			var questionObject = {};
			questionObject["questionTitle"] = $("input[name=questionTitle]").val();
			questionObject["questionAnswer"] = globalEditor.getContent();
			//遍历相似问题
			$("input[name=similarQuestion]").each(function(index,element){
				questionObject["similarQuestionList["+index+"].similarQuestionTitle"] = $(element).val();
			});
			
			//保存
			var url = "./robotQuestionController.do?saveOrUpdate";
			$.ajax({
				url : url,// 请求的action路径
				dataType : "json",
				type:"post",
				data : questionObject,
				success : function(data) {
					if (data.success) {
						setTimeout("location.href='robotQuestionController.do?questionList'", 100);
					} else {
						dialog({
							content : data.msg,
							title : "错误消息"
						}).show();
					}
				},
				error : function() {// 请求失败处理函数

				},
			});
		});
	});
</script>
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
				<div class="col-sm-3"></div>
				<div class="panel col-sm-6">
					<div class="panel-cont">
						<h4 class="title">添加问题</h4>
						<form id="add_que_form">
							<div class="form-group" id="add_similar">
								<label class="control-label">问题</label>
								<div class="input-group input-group-minimal" style="margin-bottom: 20px;">
									<input type="text" class="form-control" name="questionTitle" maxlength="200" id="question" style="height: 35px; border-right: 1px solid #f4f4f4;">
									<span class="input-group-btn">
										<a class="btn btn-white" href="javascript:;" id="add_Que_similar" style="height: 35px; background-color: #f2f2f2;">
											<i class="fa fa-plus-square"></i>
										</a>
									</span>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">答案</label>
								<script id="questionAnswer" type="text/plain" style="height: 200px; width: 100%;">
								
							</script>
							</div>
							<div class="form-group">
								<label class="control-label">选择分类:&nbsp;&nbsp;<a>请选择</a></label>
							</div>
							<div class="form-group">
								<div class="col-lg-12 choose-btn question-add-submit">
									<a href="javascript:;">确认添加</a>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

	</div>

</body>
</html>
