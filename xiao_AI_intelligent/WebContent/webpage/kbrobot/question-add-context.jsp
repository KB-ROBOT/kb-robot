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

		//提交
		$(".question-add-submit").click(function() {
			var questionList = new Array();

			$('.questionArea .form-group').each(function(index, element){
				var questionObj = new Object();
				questionObj.questionTitle = $(element).find('input').val();
				questionObj.questionAnswer = $(element).find('textarea').val();
				//{'questionTitle':questionTitle,'questionAnswer':questionAnswer}
				questionList.push(questionObj);
			});
			
			var result = JSON.stringify(questionList);//jQuery.parseJSON(questionList);//
			
			//保存
			var url = "./robotQuestionController.do?saveContextQuestion";
			$.ajax({
				url : url,// 请求的action路径
				dataType : "json",
				contentType : 'application/json;charset=utf-8',
				type : "post",
				data : result,
				
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
		
		
		//添加关联问题
		$('.addContextQuestion').click(function(){
			$('.questionArea').append(function(){
				
				var questionCount = $('.questionArea .form-group').length;
				
				return '<div class=\"form-group\" >\
				<label class=\"control-label\">问题'+ (questionCount+1) +'</label> \
				<input type=\"text\" class=\"form-control\" name=\"questionTitle_'+ (questionCount+1) +'\" maxlength=\"200\" style=\"height: 35px; border-right: 1px solid #f4f4f4;\">\
				<label class=\"control-label\">答案</label>\
				<textarea class=\"form-control\" style=\"resize: none;\" name=\"questionAnswer_'+ (questionCount+1) +'\" maxlength=\"225\"></textarea>\
			</div>';
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
					<div class="panel-cont questionArea">
						<h4 class="title">
							添加上下文问题
							<a  href="javascript:;" class="fa fr addContextQuestion" title="添加">
								<i class="fa fa-plus-circle"></i>添加关联问题
							</a>
						</h4>
						
						<div class="form-group" >
							<label class="control-label">问题1</label>
							<input type="text" class="form-control" name="questionTitle_1" maxlength="200" style="height: 35px; border-right: 1px solid #f4f4f4;">
							<label class="control-label">答案</label>
							<textarea class="form-control" style="resize: none;" name="questionAnswer_1" maxlength="225"></textarea>
						</div>
						<div class="form-group" >
							<label class="control-label">问题2</label> <input type="text" class="form-control" name="questionTitle_2" maxlength="200" id="question" style="height: 35px; border-right: 1px solid #f4f4f4;">
							<label class="control-label">答案</label>
							<textarea class="form-control" style="resize: none;" name="questionAnswer_2" maxlength="225"></textarea>
						</div>
					</div>
					<div class="panel-cont">
						<div class="form-group">
							<label class="control-label">选择分类:&nbsp;&nbsp;<a>请选择</a></label>
						</div>
						<div class="form-group">
							<div class="col-lg-12 choose-btn question-add-submit">
								<a href="javascript:;">确认添加</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
