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
											<a href="add-question.html" class="btn btn-success">
												<span class="fa fa-plus-square"></span>
												添加问题
											</a>
											<a href="import-question.html" class="btn btn-success" style="margin-left: 10px;">
												<span class="fa fa-files-o"></span>
												批量导入
											</a>
										</div>
									</div>
									<form id="search_form" action="robotQuestionController.do?questionList" method="POST" class="navbar-left navbar-form" style="margin: 0;">
										<!-- <input type="hidden" name="isLeaf" class="isLeaf"> <input type="hidden" name="groupId"> <input type="hidden" name="queryType" value="1"> -->
										<div class="input-group input-group-minimal form-inline">
											<input type="text" name="searchParam" class="form-control pull-right" vlaue="" style="height: auto !important; border-right: 1px solid #e4e4e4;">
											<span class="input-group-btn yun_btn">
												<div class="form-group">
													<select name="searchKey" class=" form-control">
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
														<a data-toggle="modal" data-target="#editqueModal" data-questionid="${question.id}" href="javascript:;">
															<i class="fa fa-edit"></i>
														</a>
													</p>
													<ul class="QA-list fr">
														<li class="dropdown hover-line">
															<a>
																<i class="fa fa-asterisk"></i>
															</a>
														</li>
														<li class="dropdown hover-line">
															<a data-toggle="modal" data-target="#delQueModal" href="javascript:;">
																<i class="fa fa-trash-o"></i>
															</a>
														</li>
													</ul>
												</div>
												<div class="QA-content">
													答案：
													<p>
														<span>${question.questionAnswer}</span>
													</p>
												</div>
												<%-- <div class="QA-foot">
													<a href="javascript:;" class="similarBtn">添加相似问法（1）个</a>
													&nbsp;&nbsp;|&nbsp;&nbsp;
													<a href="javascript:;" data-toggle="modal" data-target="#editAnsModal">编辑答案</a>
													&nbsp;&nbsp;|&nbsp;&nbsp;
													<span>最后编辑时间：2016-07-29 19:01</span>
													<div class="add-similar" style="display: none;">
														<dl>
															<dd>
																1. 如何办理发票
																<span>
																	<a href="javascript:;" data-toggle="modal" data-target="#editSimiQueModal">编辑</a>
																	&nbsp;&nbsp;|&nbsp;&nbsp;
																	<a href="">删除</a>
																</span>
															</dd>
														</dl>
														<form style="height: 100%;">
															<div class="input-group" style="clear: both;">
																<input type="text" name="question" class="form-control" maxlength="200" style="width: 300px;">&nbsp;
																<button type="button" class="btn btn-white btn-sm add_Que_similar" style="margin-top: -3px;">添加相似问法</button>
															</div>
														</form>
													</div>
												</div> --%>
											</div>
										</li>
									</c:forEach>


									<%-- <li>
										<div class="QA-div">
											<div class="QA-header">
												<p class="fl">
													1.&nbsp;公司介绍&nbsp;&nbsp;&nbsp;&nbsp;
													<a data-toggle="modal" data-target="#editqueModal" href="javascript:;">
														<i class="fa fa-edit"></i>
													</a>
												</p>
												<ul class="QA-list fr">
													<li class="dropdown hover-line">
														<a>
															<i class="fa fa-asterisk"></i>
														</a>
													</li>
													<li class="dropdown hover-line">
														<a data-toggle="modal" data-target="#delQueModal" href="javascript:;">
															<i class="fa fa-trash-o"></i>
														</a>
													</li>
												</ul>
											</div>
											<div class="QA-content">
												<p>
													答案：
													<span>办理发票</span>
												</p>
											</div>
											<div class="QA-foot">
												<a href="javascript:;" class="similarBtn">添加相似问法（0）个</a>
												&nbsp;&nbsp;|&nbsp;&nbsp;
												<a href="javascript:;" data-toggle="modal" data-target="#editAnsModal">编辑答案</a>
												&nbsp;&nbsp;|&nbsp;&nbsp;
												<span>最后编辑时间：2016-07-29 19:01</span>
												<div class="add-similar" style="display: none;">
													<dl>
														<dt>暂时没有相似问法，请添加！</dt>
														<dd>1. 如何办理发票<span><a href="javascript:;" data-toggle="modal" data-target="#editSimiQueModal">编辑</a>
                                                    	&nbsp;&nbsp;|&nbsp;&nbsp;<a href="">删除</a></span>
                                                    </dd>
													</dl>
													<form style="height: 100%;">
														<div class="input-group" style="clear: both;">
															<input type="text" name="question" class="form-control" maxlength="200" style="width: 300px;">&nbsp;
															<button type="button" class="btn btn-white btn-sm add_Que_similar" style="margin-top: -3px;">添加相似问法</button>
														</div>
													</form>
												</div>
											</div>
										</div>
									</li> --%>

								</ul>
							</div>
						</div>
						<!-- 页码 -->
						<div class="row">
							${questionPageList.pager.getToolsBarByUrl()}
							<%-- <div class="col-sm-6">
								共&nbsp;<b>${questionPageList.pager.pageCount}</b>&nbsp;页&nbsp;第&nbsp;<b>${questionPageList.pager.curPageNO}</b>&nbsp;页
							</div>
							<div class="col-sm-6">
								<ul class="pagination fr">
									<li class="disabled">
										<a href="#">上一页</a>
									</li>
									<li>
										<a href="#" class="active">1</a>
									</li>
									<li>
										<a href="#">2</a>
									</li>
									<li>
										<a href="#">3</a>
									</li>
									<li>
										<a href="#">下一页</a>
									</li>
								</ul>
							</div> --%>
							${questionPageList.pager.getToolBar(questionPageList.pager.url)}
						</div>
						<!-- 页码 -->
					</div>
				</div>
			</div>
		</div>

	</div>

	<!-- 删除问题框  -->
	<div class="modal fade" id="delQueModal">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">删除答案</h4>
				</div>

				<div class="modal-body">删除该答案将会一并删除与之相关的问题、相似问法，您确定删除吗？</div>

				<div class="modal-footer">
					<input type="hidden" name="id">
					<button type="button" class="btn btn-info" id="delQueBtn">删除</button>
					<button type="button" class="btn btn-white" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>


	<!-- 编辑问题答案   -->
	<div class="modal fade" id="editqueModal">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">编辑问题</h4>
				</div>

				<div class="modal-body">
					<form id="editqueTitleFrom">
						<div class="form-group">
							<label class="control-label">问题</label> <input type="text" class="form-control" name="question" maxlength="100" onKeyDown="if(event.keyCode==13){editQuePantect(); return false;}">
						</div>
						<input type="hidden" name="qid"> <input type="hidden" name="solutionId">
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-info" id="editqueTitleBtn">修改</button>
					<button type="button" class="btn btn-white" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 编辑答案-->
	<div class="modal fade" id="editAnsModal">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">编辑答案</h4>
				</div>

				<div class="modal-body">
					<form id="editqueAnsFrom" class="editqueAnsFrom">
						<div class="form-group">
							<label class="control-label">答案</label>
							<textarea class="form-control" name="answer" style="height: 100px;"></textarea>
						</div>
						<div class="form-group" style="position: relative;">
							<label class="control-label" style="margin-top: 10px;">分类： <a data-toggle="dropdown" class="dropdown-toggle" href="#">
									请选择<b class="caret"></b>
								</a>
								<ul class="dropdown-menu" role="menu" style="min-width: 95px;">
									<li>
										<a href="#">基础类</a>
									</li>
								</ul>
							</label>
						</div>
					</form>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-success" id="editqueAnsBtn">修改</button>
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
							<label class="control-label">问题</label> <input type="text" class="form-control" name="question" onkeydown="if(event.keyCode==13){editSimiliar(); return false;}">
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

</body>
</html>
