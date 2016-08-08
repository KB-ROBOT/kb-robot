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
				<div class="tabs col-sm-12">
					<h4 class="title">微信接入</h4>
					<div class="tabs-nav">
						<ul class="clearfix">
							<a href="#cont1"><li data="cont1" class="curr">公众号绑定</li></a>
							<a href="#cont2"><li data="cont2">关注欢迎语</li></a>
							<a href="#cont3"><li data="cont3">关键字管理</li></a>
							<a href="#cont4"><li data="cont4">菜单管理</li></a>
						</ul>
					</div>
					<div class="conts">
						<div class="cont" id="cont1" style="display: block;">
							<h4 class="title">微信对接类型</h4>
							<div class="row">
								<span class="col-xs-4">
									自动一键接入
									<a href="openwx/goAuthor.do" target="_blank" class="dock-btn">
										<img src="./plug-in/kbrobot/images/icon_button3_3.png">
									</a>
								</span>
								<span class="col-xs-4">
									手动接入
									<a href="" class="dock-btn">
										<img src="./plug-in/kbrobot/images/icon_button3.png">
									</a>
								</span>
							</div>
							<div class="row" style="margin-right: 0px;">
								<h3 class="title">已接入微信公众号列表</h3>
								<table class="table table-model-2" style="margin-top: 20px;">
									<tr>
										<th>头像</th>
										<th>公众号名称</th>
										<th>AppID/CorpID</th>
										<th>认证情况</th>
										<th>公众号类型</th>
										<th>原始ID</th>
										<th>操作</th>
									</tr>
									<c:if test="${weixinAccountList.size()==0}">
										<tr>
											<td align="center" colspan="7">还未绑定公众号</td>
										</tr>
									</c:if>
									<c:forEach items="${weixinAccountList}" var="weixinAccount" varStatus="status">
										<tr>
											<td>
												<img src="${weixinAccount.headImg}" />
											</td>
											<td>${weixinAccount.accountName}</td>
											<td>${weixinAccount.accountAppid}</td>
											<td>
												<robot:dict fieldValue="${weixinAccount.verifyTypeInfo}" dictionary="verifyType"></robot:dict>
											</td>
											<td>
												<robot:dict fieldValue="${weixinAccount.accountType}" dictionary="weixintype"></robot:dict>
											</td>
											<td>${weixinAccount.weixinAccountId}</td>
											<td align="center">
												<a href="" class="fa fa-trash-o"></a>
											</td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
						<div class="cont" id="cont2" style="display: none;">
							<h4 class="title">
								关注欢迎语
								<a data-toggle="modal" data-target="#focus_modal" href="javascript:;" class="fa fa-plus fr" style="margin-top: 20px;" title="添加"></a>
							</h4>
							<h5>当前公众号[${sessionScope.WEIXIN_ACCOUNT.accountName}]</h5>
							<table>
								<tr>
									<th>名称</th>
									<th>类型</th>
									<th>操作</th>
								</tr>
								<c:forEach items="${subscribeList}" var="subscribe" varStatus="status">
									<tr>
										<td>${subscribe.templateName}</td>
										<td>
											<c:if test="${subscribe.msgType=='text'}">文本</c:if>
											<c:if test="${subscribe.msgType=='news'}">图文</c:if>
										</td>
										<td>
											<div class="choose-btn">
												<a>编辑</a>
												<a class="delete-btn">删除</a>
											</div>
										</td>
									</tr>
								</c:forEach>

							</table>
						</div>
						<div class="cont" id="cont3" style="display: none;">
							<h4 class="title">
								关键字管理
								<a data-toggle="modal" data-target="#focus_modal" href="javascript:;" class="fa fa-plus fr" style="margin-top: 20px;" title="添加"></a>
							</h4>
							<h5>当前公众号[${sessionScope.WEIXIN_ACCOUNT.accountName}]</h5>
							<table>
								<tr>
									<th>关键字</th>
									<th>素材</th>
									<th>操作</th>
								</tr>
								<c:forEach items="${autoResponseList}" var="autoResponse" varStatus="status">
									<tr>
										<td>${autoResponse.keyWord }</td>
										<td>${autoResponse.templateName }</td>
										<td>
											<div class="choose-btn">
												<a>编辑</a>
												<a class="delete-btn">删除</a>
											</div>
										</td>
									</tr>
								</c:forEach>
							</table>
						</div>
						<div class="cont" id="cont4" style="display: none;">
							<div class="col-lg-12" style="float: none;">
								<h4 class="title">
									自定义菜单
									<a data-toggle="modal" data-target="#myModal" href="javascript:;" class="fa fa-plus fr" style="margin-top: 20px;" title="添加主菜单"></a>
								</h4>
								<h5>当前公众号[${sessionScope.WEIXIN_ACCOUNT.accountName}]</h5>
								<div class="fans-content">
									<table>
										<tr>
											<th>菜单名称</th>
											<th>设置动作</th>
										</tr>
										<tr>
											<td valign="top" style="width: 30%;">
												<!-- <p data="define1" class="nav-define">
													<b>我要咨询</b>
													<span class="fr">
														<a data-toggle="modal" data-target="#myModal" href="javascript:;" class="fa fa-plus-square show-click" title="添加子菜单"></a>
														<a href="javascript:;" class="fa fa-pencil" title="编辑"></a>
														<a href="javascript:;" class="fa fa-trash-o" title="删除"></a>
													</span>
												</p>
												<div class="sub-menu">
													<p data="define2" class="nav-define">
														法律法规
														<span class="fr">
															<a href="javascript:;" class="fa fa-pencil" title="编辑"></a>
															<a href="javascript:;" class="fa fa-trash-o" title="删除"></a>
														</span>
													</p>
													<p data="define2" class="nav-define">
														最新资讯
														<span class="fr">
															<a href="javascript:;" class="fa fa-pencil" title="编辑"></a>
															<a href="javascript:;" class="fa fa-trash-o" title="删除"></a>
														</span>
													</p>
												</div> -->
												<c:forEach items="${fatherMenuList}" var="fatherMenu" varStatus="status">
													<p data="define1" class="nav-define">
														<b>${fatherMenu.menuKey}</b>
														<span class="fr">
															<a data-toggle="modal" data-target="#myModal" href="javascript:;" class="fa fa-plus-square show-click" title="添加子菜单"></a>
															<a href="javascript:;" class="fa fa-pencil" title="编辑"></a>
															<a href="javascript:;" class="fa fa-trash-o" title="删除"></a>
														</span>
													</p>
													<c:forEach items="${menuList}" var="menu" varStatus="status">
														<c:if test="${fatherMenu.id==menu.id&&menu.menuEntity!=null }">
															<div class="sub-menu">
																<p data="define2" class="nav-define">
																	${menu.menuKey }
																	<span class="fr">
																		<a href="javascript:;" class="fa fa-pencil" title="编辑"></a>
																		<a href="javascript:;" class="fa fa-trash-o" title="删除"></a>
																	</span>
																</p>
															</div>
														</c:if>
													</c:forEach>
												</c:forEach>

											</td>
											<td>
												<div class="defines">
													<p class="define-none" style="text-align: center;">你可以先添加一个菜单，然后开始为其设置响应动作</p>
													<!-- 无法设置动作   -->
													<div class="define" id="define1" style="display: none; text-align: center;">已有子菜单无法设置动作</div>
													<!-- 无法设置动作   -->

													<!-- 设置动作   -->
													<div class="define" id="define2" style="display: none;">
														<div class="col-lg-12 choose-btn">
															<p>请选择订阅者点击菜单后，公众号做出的相应动作</p>
															<a class="tab-pane">
																<i class="fa fa-envelope-o"></i>发送消息
															</a>
															<a class="cancel-btn tab-url">
																<i class="fa fa-link"></i>跳转链接
															</a>
														</div>
														<div class="col-lg-12 news" style="display: none;">
															<p>订阅者点击该子菜单会显示以下信息</p>
															<div class="new-nav">
																<span class="curre" data="new1">文字消息</span>
																<span data="new2">图文消息</span>
																<span data="new3">语音消息</span>
															</div>
															<div class="new-conts">
																<div class="new" id="new1" style="display: block;">
																	<textarea class="form-control" name="robotName"></textarea>
																	<div class="col-lg-5 choose-btn">
																		<a>保存</a>
																		<a class="cancel-btn tab-return">返回</a>
																	</div>
																</div>
																<div class="new lookup" id="new2" style="display: none;">
																	<input type="text" class="form-control" disabled="disabled" placeholder="请选择图文">
																	<button data-toggle="modal" data-target="#film_modal">查找图文</button>
																	<div class="col-lg-5 choose-btn">
																		<a>保存</a>
																		<a class="cancel-btn tab-return">返回</a>
																	</div>
																</div>
																<div class="new lookup" id="new3" style="display: none;">
																	<input type="text" class="form-control" disabled="disabled" placeholder="请选择语音文件">
																	<button data-toggle="modal" data-target="#volume_modal">查找语音</button>
																	<div class="col-lg-5 choose-btn">
																		<a>保存</a>
																		<a class="cancel-btn tab-return">返回</a>
																	</div>
																</div>
															</div>
														</div>
														<div class="col-lg-12 urls" style="display: none;">
															<p>订阅者点击该子菜单会跳到以下链接</p>
															<input type="text" class="form-control" placeholder="请输入URL">
															<div class="col-lg-5 choose-btn">
																<a>保存</a>
																<a class="cancel-btn url-return">返回</a>
															</div>
														</div>
													</div>
												</div>
												<!-- 设置动作   -->
											</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
		<!--查找图文对话框开始-->
		<div id="film_modal" class="modal fade custom-width in film_modal">
			<div class="modal-dialog" style="width: 750px;">
				<div class="modal-content">
					<div class="modal-header">
						<button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
						<h4 class="modal-title">请选择图文消息</h4>
					</div>
					<div class="modal-body ">
						<div class="scrollable" data-max-height="400" style="height: 400px;">
							<div id="img_boxs"></div>
						</div>
						<ul id="VpageList4" style="float: right; margin: 0;">
						</ul>
					</div>
					<div class="modal-footer">
						<button data-dismiss="modal" class="btn btn-white" type="button">取消</button>
						<button class="btn btn-info" type="button" onClick="addImgTxt('film_modal')">确定</button>
					</div>
				</div>
			</div>
		</div>

		<!--查找图文对话框结束-->

		<!--查找语音对话框开始-->
		<div id="volume_modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width: 750px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h5>请选择语音文件</h5>
					</div>
					<div class="modal-body">
						<div class="nano has-scrollbar">
							<div class="content" tabindex="0" style="right: -17px;">
								<form class="control-group row-fluid vedioForm">
									<input type="text" placeholder="请输入要查找的文件名" class="input-medium span6 form-control">
									<button class="btn btn-info yun_btn voice_btn" type="button">查找</button>
								</form>
								<table class="table table-bordered">
									<thead>
										<tr>
											<th width="10%">选择</th>
											<th width="90%">文件名</th>
										</tr>
									</thead>
									<tbody id="voiceDiv">
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button data-dismiss="modal" class="btn btn-white" type="button">取消</button>
						<button class="btn btn-info" type="button" onClick="addImgTxt('film_modal')">确定</button>
					</div>
				</div>
			</div>
		</div>
		<!--查找语音对话框结束-->

		<!--关注欢迎语-->
		<div id="focus_modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">信息录入</h4>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<label class="control-label">消息类型：</label> <select class="form-control">
								<option>文本消息</option>
								<option>图文消息</option>
								<option>语音消息</option>
							</select>
						</div>
						<div class="form-group">
							<label class="control-label">消息类型：</label> <select class="form-control">
								<option>11111</option>
								<option>22222</option>
								<option>33333</option>
								<option>44444</option>
							</select>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary">确定</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>

					</div>
				</div>
			</div>
		</div>
		<!--关注欢迎语结束-->

		<!-- 添加菜单对话框 -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel">菜单</h4>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<label class="control-label">菜单名的名字长度不多于6个文字或字符</label> <input type="text" class="form-control" name="robotName" maxlength="6" placeholder="请输入菜单名">
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary">确定</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
				</div>
			</div>
		</div>
		<!-- 添加菜单对话框结束 -->
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			var currHash = window.location.hash;
			currHash = currHash.replace(/#/,'');
			$('.tabs li[data='+currHash+']').click();
		});
	</script>

</body>
</html>
