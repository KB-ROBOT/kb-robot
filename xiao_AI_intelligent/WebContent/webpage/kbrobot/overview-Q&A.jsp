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

		//富文本初始化
		var globalEditor = UE.getEditor('questionAnswer');
		
		//编辑
		$(".question-edit").on("click", function() {

			var questionId = $(this).data("questionid");

			var url = "./robotQuestionController.do?getQuestionDetail";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data : {
					"id" : questionId
				},
				success : function(data) {

					if (data.success) {

						$("#editqueModal input[name=questionTitle]").val(data.obj.questionTitle);
						globalEditor.setContent((data.obj.questionAnswer==null||data.obj.questionAnswer==undefined)?"":data.obj.questionAnswer);

						//显示模态框
						$("#editqueModal").modal("show");
					} else {
						dialog({
							title : "出错了",
							content : data.msg,
							ok : function() {

							}
						}).width(320).showModal();
					}
				},
				error : function() {
				}
			});
			
			//模态框修改按钮绑定click事件
			$("#editqueTitleBtn").click(function(){
				var questionTitle = $("#editqueModal input[name=questionTitle]").val();
				var questionAnswer = globalEditor.getContent();
				
				if(!globalEditor.hasContents()){
					dialog({
						title : "出错了",
						content : "答案不能为空",
						ok : function() {
						}
					}).width(320).showModal();
					return;
				}
				if(questionTitle==undefined||questionTitle==''){
					dialog({
						title : "出错了",
						content : "请填写问题",
						ok : function() {
						}
					}).width(320).showModal();
					return;
				}
				
				var url = "./robotQuestionController.do?saveOrUpdate";
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data : {
						"id" : questionId,
						"questionTitle":questionTitle,
						"questionAnswer":questionAnswer
					},
					success : function(data) {

						if (data.success) {
							//隐藏模态框
							$("#editqueModal").modal("hide");
							setTimeout("location.reload()", 100);
						} else {
							dialog({
								title : "出错了",
								content : data.msg,
								ok : function() {
								}
							}).width(320).showModal();
						}
					},
					error : function() {

					}
				});
			});
			
			//
			
		});
		
		//删除问题
		$(".question-del").click( function() {
			var questionId = $(this).data("questionid");
			dialog({
				title : "警告",
				content : "确认删除？",
				ok : function() {
					var url = './robotQuestionController.do?del';
					$.ajax({
						url : url,// 请求的action路径
						type : 'post',
						dataType : "json",
						data : {
							"id" : questionId
						},
						success : function(data) {
							setTimeout("location.reload()", 100);
						},
						error : function() {// 请求失败处理函数

						},
					});
				},
				cancle : function() {
				}
			}).width(320).showModal();
		});
		//删除相似问题
		$(".similar-question-del").click(function(){
			
			var similarId = $(this).data("similarid");
			
			dialog({
				title : "警告",
				content : "确认删除？",
				ok : function() {
					var url = './robotSimilarQuestionController.do?del';
					$.ajax({
						url : url,// 请求的action路径
						type : 'post',
						dataType : "json",
						data : {
							"id" : similarId
						},
						success : function(data) {
							setTimeout("location.reload()", 100);
						},
						error : function() {// 请求失败处理函数
						},
					});
				},
				cancle : function() {
				}
			}).width(320).showModal();
		});
		
		//添加相似问题
		$(".add_que_similar").click(function(){
			
			var parentElement = $(this).parent();
			var questionId = parentElement.find("input[name=questionId]").val();
			var similarQuestion = parentElement.find("input[name=similarQuestion]").val();
			
			var url = 'robotSimilarQuestionController.do?saveOrUpdate';
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				dataType : "json",
				data : {
					"similarQuestionTitle" : similarQuestion,
					"question.id":questionId
				},
				success : function(data) {
					setTimeout("location.reload()", 100);
				},
				error : function() {// 请求失败处理函数
				},
			});
		});
		
		//相似问题编辑
		$(".similar-question-edit").click(function(){
			
			var id = $(this).data("id");
			var questiontitle = $(this).data("questiontitle");
			$("#editSimiQueModal input[name=similarQuestion]").val(questiontitle);
			
			$("#editSimiQueModal").modal("show");
			$("#editsimiliar_Btn").click(function(){
				var similarQuestion = $("#editSimiQueModal input[name=similarQuestion]").val();
				
				var url = 'robotSimilarQuestionController.do?saveOrUpdate';
				$.ajax({
					url : url,// 请求的action路径
					type : 'post',
					dataType : "json",
					data : {
						"id":id,
						"similarQuestionTitle":similarQuestion
					},
					success : function(data) {
						setTimeout("location.reload()", 100);
						$("#editSimiQueModal input[name=similarQuestion]").val("");
					},
					error : function() {// 请求失败处理函数
					},
				});
			});
		});
		
		
		$(".importQuestion").click(function(){
			$("#importQuestionModal").modal("show");
		});
	});
	
	//文件上传
	function questionFileUpload() {
		$("#questionImport").fileupload({
			url:"./robotQuestionController.do?uploadImportQuestion",
			dataType : 'json',
			acceptFileTypes : /(\.|\/)(csv|xlsx|xls)$/i,
			maxFileSize : 15000000, // 15 MB
			done : function(e, data) {
				if(data.result.success){
					
					if(!$("#importQuestionModal .modal-body .importInfo").length>0){
						$("#importQuestionModal .modal-body").append("<p class=\"control-label importInfo\">"+data.result.msg+"</p>");
					}
					else{
						$("#importQuestionModal .modal-body .importInfo").text(data.result.msg);
					}
					
					if(!$("#importQuestionModal .modal-footer .importQuestion").length>0){
						$("#importQuestionModal .modal-footer").append("<button type=\"button\" class=\"btn btn-info readyImport\">确认导入</button>");
					}
					
				
					$(".readyImport").on("click",function(){
						var url = 'robotQuestionController.do?importQuestion';
						$.ajax({
							url : url,// 请求的action路径
							type : 'post',
							dataType : "json",
							data : {
								"importFilePath":data.result.attributes.filePath
							},
							success : function(data) {
								$("#progress .progress-bar").css('width','0%');
								setTimeout("location.reload()", 100);
							},
							error : function() {// 请求失败处理函数
							},
						});
					});
				}
				else{
					dialog({
						title:'上传出错',
						content:data.result.msg,
						ok:function(){
							
						}
					}).width(320).showModal();
					$("#progress .progress-bar").css('width','0%');
				}
			},
			progressall : function(e, data) {
				var progress = parseInt(data.loaded / data.total * 100, 10);
				$("#progress .progress-bar").css('width', progress + '%');
			}
		    ,
			dropZone : $('#dropzone')//拖拽区域
		});
	}
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
						<h4 class="title">问答总览</h4>
						<div class="row">
							<div class="col-sm-12">
								<div class="col-sm-9" style="padding: 0;">
									<div class="btn-toolbar  pull-left">
										<div class="btn-group focus-btn-group ">
											<a href="./robotQuestionController.do?goQuestionAdd" class="btn btn-success">
												<span class="fa fa-plus-square"></span>
												添加问题
											</a>
											<a href="javscript:;" class="btn btn-success importQuestion" style="margin-left: 10px;">
												<span class="fa fa-files-o"></span>
												批量导入
											</a>
										</div>
									</div>
									<form id="search_form" action="robotQuestionController.do?questionList" method="POST" class="navbar-left navbar-form" style="margin: 0;">
										<!-- <input type="hidden" name="isLeaf" class="isLeaf"> <input type="hidden" name="groupId"> <input type="hidden" name="queryType" value="1"> -->
										<%-- ${searchParam}  ${searchKey} --%>
										<div class="input-group input-group-minimal form-inline">
											<input type="text" name="searchParam" class="form-control pull-right" value="${searchParam}" style="height: auto !important; border-right: 1px solid #e4e4e4;">
											<span class="input-group-btn yun_btn">
												<div class="form-group">
													<select name="searchKey" class=" form-control" value="${searchKey}">
														<option value="questionTitle">问题</option>
														<option value="questionAnswer">答案</option>
													</select>
												</div>
												<button class="btn btn-white dropdown-toggle" style="margin-left: 3px;" onclick="submit();">
													<i class="fa fa-search"></i>
												</button>
											</span>
										</div>
									</form>

								</div>

								<div class="col-sm-3">
									<ul class="nav nav-pills pull-right">
										<li>
											<a href="javascript:;" class="panpectDelPL">批量删除</a>
										</li>
										<li class="dropdown" style="background-color: #fff;">
											<a data-toggle="dropdown" class="dropdown-toggle" href="#">
												排序 <b class="caret"></b>
											</a>
											<ul class="dropdown-menu" role="menu" style="min-width: 95px;">
												<li>
													<a href="#" onclick="listPanpect(1,0,4);return false;">默认排序</a>
												</li>
												<li>
													<a href="#" onclick="listPanpect(1,0,3);return false;">时间正序</a>
												</li>
												<li>
													<a href="#" onclick="listPanpect(1,0,4);return false;">时间倒序</a>
												</li>
												<li>
													<a href="#" onclick="listPanpect(1,0,5);return false;">点击正序</a>
												</li>
												<li>
													<a href="#" onclick="listPanpect(1,0,6);return false;">点击倒序</a>
												</li>
												<li>
													<a href="#" onclick="listPanpect(1,0,4,-2);return false;">审核状态</a>
												</li>
												<li>
													<a href="#" onclick="listPanpect(1,0,4,-4);return false;">过期状态</a>
												</li>
												<li>
													<a href="#" onclick="listPanpect(1,0,4,0);return false;">发布状态</a>
												</li>
											</ul>
										</li>
									</ul>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-lg-12">
								<ul class="view-QA">
									<c:forEach items="${questionPageList.resultList}" var="question" varStatus="status">
										<li>
											<div class="QA-div">
												<div class="QA-header">
													<p class="fl">
														${status.index + 1} .&nbsp;${question.questionTitle}
														<a class="question-edit" data-questionid="${question.id}" href="javascript:;">
															<i class="fa fa-edit"></i>
														</a>
													</p>
													<ul class="QA-list fr">
														<li class="dropdown hover-line">
															<!-- <a>
																<i class="fa fa-asterisk"></i>
															</a> -->
														</li>
														<li class="dropdown hover-line">
															<a href="javascript:;" class="question-del" data-questionid="${question.id}">
																<i class="fa fa-trash-o"></i>
															</a>
														</li>
													</ul>
												</div>
												答案：
												<div class="QA-content">
													<p>
														<span>${question.questionAnswer}</span>
													</p>
												</div>
												<div class="QA-foot">
													<a href="javascript:;" class="similarBtn">添加相似问法（${question.similarQuestionList.size()}）个</a>
													&nbsp;&nbsp;|&nbsp;&nbsp;
													<span>最后编辑时间：${question.updateTime}</span>
													<div class="add-similar" style="display: none;">
														<dl>
															<c:forEach items="${question.similarQuestionList}" var="similarQuestion" varStatus="similarStatus">
																<dd>
																	${similarStatus.index+1}. ${similarQuestion.similarQuestionTitle}
																	<span>
																		<a href="javascript:;" data-id="${similarQuestion.id}" data-questiontitle="${similarQuestion.similarQuestionTitle}" class="similar-question-edit">编辑</a>
																		&nbsp;&nbsp;|&nbsp;&nbsp;
																		<a href="javascript:;" data-similarid=${similarQuestion.id } class="similar-question-del">删除</a>
																	</span>
																</dd>
															</c:forEach>

														</dl>
														<form style="height: 100%;">
															<div class="input-group" style="clear: both;">
																<input type="hidden" name="questionId" value="${question.id}" /> <input type="text" name="similarQuestion" class="form-control" maxlength="200" style="width: 300px;">&nbsp;
																<button type="button" onClick="" class="btn btn-white btn-sm add_que_similar" style="margin-top: -3px;">添加相似问法</button>
															</div>
														</form>
													</div>
												</div>
											</div>
										</li>
									</c:forEach>
								</ul>
							</div>
						</div>
						<!-- 页码 -->
						<div class="row">${questionPageList.pager.getToolsBarByUrl()}</div>
						<!-- 页码 -->
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- 编辑问题答案   -->
	<div class="modal fade" id="editqueModal">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">编辑问题</h4>
				</div>
				<div class="modal-body">
					<form id="editqueQuestionFrom">
						<!-- <input type="hidden" name="qid"> <input type="hidden" name="solutionId"> -->
						<div class="form-group">
							<label class="control-label">问题</label> <input type="text" class="form-control" name="questionTitle" maxlength="100" onKeyDown="if(event.keyCode==13){editQuePantect(); return false;}">
						</div>
						<div class="form-group">
							<label class="control-label">答案</label>
							<script id="questionAnswer" type="text/plain" style="height: 200px; width: 100%;">
								
							</script>
						</div>
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-info" id="editqueTitleBtn">修改</button>
					<button type="button" class="btn btn-white" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>


	<!-- 编辑相似问法-->
	<div class="modal fade" id="editSimiQueModal">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">编辑相似问法</h4>
				</div>

				<div class="modal-body">
					<form id="editSimiForm">
						<div class="form-group">
							<label class="control-label">问题</label> <input type="text" class="form-control" name="similarQuestion">
						</div>
						<input type="hidden" name="qid"> <input type="hidden" name="solutionId">
					</form>
				</div>

				<div class="modal-footer">
					<input type="hidden" name="id">
					<button type="button" class="btn btn-info" id="editsimiliar_Btn">修改</button>
					<button type="button" class="btn btn-white" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 编辑相似问法-->
	<div class="modal fade" id="importQuestionModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">问题导入</h4>
				</div>

				<div class="modal-body">
					<p class="control-label">1.一次性导入的问题不能超过300个。</p>
					<p class="control-label">2.导入仅支持xlsx、xls。</p>
					<p class="control-label">
						3.导入要按照模板规格填写，否则导入失败。下载连接 →<a href="./downloadFile/导入模板.xls">点击下载模板</a>
					</p>
					<p class="control-label">
						<input id="questionImport" type="file" name="file" value="" onClick="questionFileUpload()">
						<div id="progress" class="progress">
							<div class="progress-bar progress-bar-success" style="width: 0%;"></div>
						</div>
					</p>

				</div>

				<div class="modal-footer">
					<input type="hidden" name="id">
					<button type="button" class="btn btn-white" data-dismiss="modal">取消导入</button>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
