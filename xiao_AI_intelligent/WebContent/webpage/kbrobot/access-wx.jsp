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
		var currHash = window.location.hash;
		currHash = currHash.replace(/#/, '');
		$('.tabs li[data=' + currHash + ']').click();

		//首先查找相数据  将mediaList放在全局变量中
		var textTemplateList;
		var newsTemplateList;
		var voiceTemplateList;
		//素材查找
		var url = "./robotBindController.do?mediaSourceFind";
		$.ajax({
			url : url,// 请求的action路径
			type : 'post',
			dataType : "json",
			success : function(data) {
				textTemplateList = data.attributes.textTemplateList;
				newsTemplateList = data.attributes.newsTemplateList;
				voiceTemplateList = data.attributes.voiceTemplateList;

				$('.subscribeList').html("");
				$("select[name^=menuText_]").html();
				var innerHtml = '';
				for (var i = 0; i < textTemplateList.length; i++) {
					innerHtml = '<option value="'+textTemplateList[i].id+'">' + textTemplateList[i].templateName + '</option>';
					$('.subscribeList').append(innerHtml);
					//菜单选择
					$("select[name^=menuText_]").html(innerHtml);
				}

			},
			error : function() {// 请求失败处理函数

			},
		});

		//下拉列表框
		$(".subscribeType").on("change", function() {
			var type = $(this).val();
			if (type == 'text') {
				var eachList = textTemplateList;
			} else if (type == 'news') {
				var eachList = newsTemplateList;
			} else if (type == 'voice') {
				var eachList = voiceTemplateList;
			}

			$('.subscribeList').html("");
			for (var i = 0; i < eachList.length; i++) {
				$('.subscribeList').append('<option value="'+eachList[i].id+'">' + eachList[i].templateName + '</option>');
			}
		});

		//关键字
		$(".fa-plus").on("click", function() {
			currHash = window.location.hash;
			currHash = currHash.replace(/#/, '');
			if (currHash == 'cont3' && $(".groupMsg").length <= 0) {
				//关键字表单
				var groupMsg = '';
				groupMsg += '<div class="form-group groupMsg">';
				groupMsg += '<label class="control-label">群发名称：</label>';
				groupMsg += '<input type="text" name="groupMsgName" class="form-control" maxlength="60" placeholder="请输入群发名称" />';
				groupMsg += '<label class="control-label">群发描述：</label>';
				groupMsg += '<input type="text" name="groupMsgDesc" class="form-control" maxlength="100" placeholder="请输入群发描述" />';
				groupMsg += '</div>';
				//groupMsg += $("#focus_modal").find(".modal-body").html();

				$("#focus_modal").find(".modal-body").append(groupMsg);
			} else if (currHash != 'cont3' && $(".groupMsg").length > 0) {//如果已经存在了关键字输入框
				$("#focus_modal").find(".form-group:eq(0)").remove();
			}
		});

		//选择素材
		$(".mediaSubmit").on("click", function() {
			currHash = window.location.hash;
			currHash = currHash.replace(/#/, '');
			var data = {};
			var msgType = $("select[name=mediaType]").val();
			var templateId = $("select[name=mediaId]").val();

			//群发消息
			var groupMsgName = $("#focus_modal input[name='groupMsgName']").val();
			var groupMsgDesc = $("#focus_modal input[name='groupMsgDesc']").val();

			if (currHash == 'cont3') {
				if (groupMsgName == undefined || groupMsgName == '') {
					$("#focus_modal input[name='groupMsgName']").focus();
					return;
				}
				if (groupMsgDesc == undefined || groupMsgDesc == '') {
					$("#focus_modal input[name='groupMsgDesc']").focus();
					return;
				}

				data.groupMsgName = groupMsgName;
				data.groupMsgDesc = groupMsgDesc;
			}

			if (templateId == undefined || templateId == '') {
				$("select[name=mediaId]").focus();
				return;
			}
			data.templateId = templateId;
			data.msgType = msgType;

			//请求的url
			var url = '';
			switch (currHash) {
			case 'cont1':
				return;
				break;
			case 'cont2':
				url = './subscribeController.do?su';
				break;
			case 'cont3':

				url = './weixinSendMsgAllController.do?addSendMsgAll';
				break;
			case 'cont4':
				url = './.do?doSave';
				break;
			}

			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				dataType : "json",
				data : data,
				success : function(data) {
					setTimeout("location.reload()", 100);
				},
				error : function() {// 请求失败处理函数

				},
			});
		});

		//菜单编辑
		$('.news span').click(function() {
			$('.news span').removeClass('curre');
			var newtId = $(this).attr('data');
			$('.new-conts .new').hide();
			$('#' + newtId).show();
			$(this).addClass('curre');
		});
		//菜单设置
		$(".fans-content p.nav-define").on("click", function() {

			$(".fans-content p.nav-define").removeClass("on");
			$(this).addClass("on");

			$(".menu_action_show .defines").children("div").hide();

			var menuType = $(this).data("menutype");
			var childMenu = $(this).parent().find(".childMenu");
			var menuId = $(this).data("menuid");
			//如果有子菜单
			if (childMenu.length > 0) {
				$("#define_none_action").show();
			} else if (menuType != undefined && menuType != '') {

				var url = './menuManagerController.do?menuEnetityDetail';
				$.ajax({
					url : url,// 请求的action路径
					type : 'post',
					dataType : "json",
					data : {
						"id" : menuId
					},
					success : function(data) {
						if (data.success) {
							//data.obj;
							$("#define_reday_action").text("");
							if (data.attributes.type == 'click') {

								$("#define_reday_action").append(data.attributes.templateObject.templateName);
							} else {
								$("#define_reday_action").append(data.attributes.url);
							}

							$("#define_reday_action").show();
						}
					},
					error : function() {// 请求失败处理函数

					},
				});
			} else {
				$("#define_set_action").show();
			}
			var innerHtml = '';
			//菜单初始化
			for (var i = 0; i < textTemplateList.length; i++) {
				innerHtml += '<option value="'+textTemplateList[i].id+'">' + textTemplateList[i].templateName + '</option>';
			}
			//菜单选择
			$("select[name=menuText]").html(innerHtml);

			innerHtml = '';
			for (var i = 0; i < newsTemplateList.length; i++) {
				//菜单选择
				innerHtml += '<option value="'+newsTemplateList[i].id+'">' + newsTemplateList[i].templateName + '</option>';
			}
			$("select[name=menuNews]").html(innerHtml);

			innerHtml = '';
			for (var i = 0; i < voiceTemplateList.length; i++) {
				//菜单选择
				innerHtml += '<option value="'+voiceTemplateList[i].id+'">' + voiceTemplateList[i].templateName + '</option>';
			}
			$("select[name=menuVoice]").html(innerHtml);

		});

		//保存菜单设置
		$(".menu_action_save").on("click", function() {
			var type = $(this).data("type");
			var selectMenuId = $(".fans-content .on").data("menuid");
			var menuObject = {};
			menuObject.type = type;
			menuObject.id = selectMenuId;
			if ("click" == type) {

				var msgType = $(this).data("msgtype");
				var templateId;
				if ("text" == msgType) {
					templateId = $("select[name=menuText]").val();
				} else if ("news" == msgType) {
					templateId = $("select[name=menuNews]").val();
				} else if ("voice" == msgType) {
					templateId = $("select[name=menuVoice]").val();
				}
				else{
					msgType = $('select[name=menuOtherAction]').val();
				}
				menuObject.msgType = msgType;
				menuObject.templateId = templateId;
			} else if ("view" == type) {
				var url = $("input[name=menuUrl]").val();
				menuObject.url = url;
			}

			var url = './menuManagerController.do?su';
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				dataType : "json",
				data : menuObject,
				success : function(data) {
					setTimeout("location.reload()", 100);
				},
				error : function() {// 请求失败处理函数

				},
			});

		});
		//菜单删除
		$(".menu-delete").click(function() {
			var menuId = $(this).data("menuid");
			dialog({
				title : "警告",
				content : "确认删除？",
				ok : function() {
					var url = './menuManagerController.do?del';
					$.ajax({
						url : url,// 请求的action路径
						type : 'post',
						dataType : "json",
						data : {
							"id" : menuId
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

		//添加子菜单
		$(".child-menu-add").click(function() {
			var fatherMenuId = $(this).data("menuid");

			$("#myModal input[name=fatherMenuId]").val(fatherMenuId);

			$("#myModal").modal("show");
		});

		//菜单添加
		$(".menuAddSubmit").on("click", function() {
			var menuName = $("input[name=menuName]").val();
			var fatherName = $("input[name=fatherMenuId]").val();
			var url = './menuManagerController.do?su';
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				dataType : "json",
				data : {
					"name" : menuName,
					"fatherName" : fatherName
				},
				success : function(data) {
					$("input[name=fatherMenuId]").val("");
					setTimeout("location.reload()", 100);
				},
				error : function() {// 请求失败处理函数

				},
			});
		});

		//微信账户删除
		$(".weixin-account-del").click(function() {
			var accountId = $(this).data("accountid");
			dialog({
				title : "警告",
				content : "确认删除？<br/>已经添加的数据将会一并删除",
				ok : function() {
					var url = './weixinAccountController.do?doDel';
					$.ajax({
						url : url,// 请求的action路径
						type : 'post',
						dataType : "json",
						data : {
							"id" : accountId
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

		//菜单同步
		$(".weixinMenuSubmit").click(function() {
			var url = './menuManagerController.do?sameMenu';
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				dataType : "json",
				success : function(data) {
					dialog({
						title : "同步情况",
						content : data.msg,
						ok : function() {
							setTimeout("location.reload()", 100);
						}
					}).width(320).showModal();
				},
				error : function() {// 请求失败处理函数
				},
			});
		});

		//群发消息
		$(".groupMsgSend").click(function() {
			var id = $(this).data("msgid");

			dialog({
				title : "发送群发",
				content : "确认发送群发消息吗？",
				ok : function() {
					var url = './weixinSendMsgAllController.do?groupMsgSend';
					$.ajax({
						url : url,// 请求的action路径
						type : 'post',
						dataType : "json",
						data : {
							"id" : id
						},
						success : function(data) {
							alert(data.msg);
							//setTimeout("location.reload()", 100);
						},
						error : function() {// 请求失败处理函数
						},
					});
				},
				cancle : function() {
				}
			}).width(320).showModal();
		});

		//刷新当前菜单配置
		/* $(".refreshMenu").on("click",function(){
			
			var url = './menuManagerController.do?refreshMenu';
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				dataType : "json",
				success : function(data) {
					setTimeout("location.reload()", 100);
				},
				error : function() {// 请求失败处理函数
				},
			});
		}); */

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
				<div class="tabs col-sm-12">
					<h4 class="title">微信接入</h4>
					<div class="tabs-nav">
						<ul class="clearfix">
							<a href="#cont1">
								<li data="cont1" class="curr">公众号绑定</li>
							</a>
							<a href="#cont2">
								<li data="cont2">关注欢迎语</li>
							</a>
							<a href="#cont3">
								<li data="cont3">群发消息</li>
							</a>
							<!-- <a href="#cont5">
								<li data="cont5">关键字管理</li>
							</a> -->
							<a href="#cont4">
								<li data="cont4">菜单管理</li>
							</a>
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
										<th>状态</th>
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
												<c:if test="${weixinAccount.accountAuthorizeType=='2'}">
													已取消授权<br />
													<a href="openwx/goAuthor.do" target="_blank" class="choose-btn">点击重新授权</a>
												</c:if>
												<c:if test="${weixinAccount.accountAuthorizeType!='2'}">
													已经授权
												</c:if>
											</td>
											<td align="center">
												<a href="javascript:void(0);" data-accountid="${weixinAccount.id}" class="fa fa-trash-o weixin-account-del"></a>
											</td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>
						<div class="cont" id="cont2" style="display: none;">
							<h4 class="title">
								关注欢迎语
								<a data-toggle="modal" data-target="#focus_modal" href="javascript:void(0);" class="fa fa-plus fr" style="margin-top: 20px;" title="添加"></a>
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
											<c:if test="${subscribe.msgType=='voice'}">语音</c:if>
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
								群发消息
								<a href="javascript:;" data-toggle="modal" data-target="#focus_modal" class="fa fa-plus fr" style="margin-top: 20px;" title="添加"></a>
							</h4>
							<h5>当前公众号[${sessionScope.WEIXIN_ACCOUNT.accountName}]</h5>
							<!-- 本月群发消息还剩：1次 -->

							<table>
								<tr>
									<th>名称</th>
									<th>描述</th>
									<th>类型</th>
									<th>状态</th>
									<th>发送总量</th>
									<th>过滤后总量</th>
									<th>成功</th>
									<th>失败</th>
									<th>创建时间</th>
									<th>发送时间</th>
									<th>操作</th>
								</tr>
								<c:forEach items="${sendGroupMsgList}" var="sendGroupMsg" varStatus="status">
									<tr>
										<th>${sendGroupMsg.groupMsgName}</th>
										<th>
											<a href="#cont3" title="${sendGroupMsg.groupMsgDesc}">${fn:substring(sendGroupMsg.groupMsgDesc, 0, 10)}</a>
										</th>
										<th>${sendGroupMsg.msgType}</th>
										<th>${sendGroupMsg.status}</th>
										<th>${sendGroupMsg.totalCount}</th>
										<th>${sendGroupMsg.filterCount}</th>
										<th>${sendGroupMsg.sendCount}</th>
										<th>${sendGroupMsg.errorCount}</th>
										<th>${sendGroupMsg.createTime}</th>
										<th>${sendGroupMsg.sendTime}</th>
										<td width="10%">
											<div class="choose-btn">
												<c:if test="${sendGroupMsg.sendCount==0||sendGroupMsg.sendCount==''||sendGroupMsg.sendCount==null}">
													<a class="groupMsgSend" data-msgId="${sendGroupMsg.id}">确认发送</a>
												</c:if>
												<a class="delete-btn groupMsgDel">删除</a>
											</div>
										</td>
									</tr>
								</c:forEach>
								<c:if test="${sendGroupMsgList.size()==0}">
									<tr>
										<td colspan="10">还没有群发过消息</td>
									</tr>
								</c:if>
							</table>
						</div>
						<%-- <div class="cont" id="cont5" style="display: none;">
							<h4 class="title">
								关键字管理
								<a data-toggle="modal" data-target="#focus_modal" href="javascript:void(0);" class="fa fa-plus fr" style="margin-top: 20px;" title="添加"></a>
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
						</div> --%>
						<div class="cont" id="cont4" style="display: none;">
							<div class="col-lg-12" style="float: none;">
								<h4 class="title">
									自定义菜单
									<a data-toggle="modal" data-target="#myModal" href="javascript:;" class="fa fa-plus fr" style="margin-top: 20px;" title="添加主菜单"></a>
									<!-- <a href="javascript:;" class="fa fa-refresh fr refreshMenu" style="margin-top: 20px;" title="刷新当前菜单配置"></a> -->
								</h4>
								<h5>当前公众号[${sessionScope.WEIXIN_ACCOUNT.accountName}]</h5>
								<div class="fans-content">
									<table>
										<tr>
											<th>菜单名称</th>
											<th>设置动作</th>
										</tr>
										<c:if test="${fatherMenuList.size()==0}">
											<tr>
												<td></td>
												<td>
													<p class="define-none" style="text-align: center;">你可以先添加一个菜单，然后开始为其设置响应动作</p>
												</td>
											</tr>
										</c:if>
										<c:forEach items="${fatherMenuList}" var="fatherMenu" varStatus="status">
											<tr>
												<td valign="top" style="width: 30%;">

													<p data-seq="${status.index}" data-menuid="${fatherMenu.id}" data-menutype="${fatherMenu.type}" class="nav-define">
														<b>${fatherMenu.name}</b>
														<span class="fr">
															<a href="javascript:void(0);" data-menuid="${fatherMenu.id}" class="fa fa-plus-square show-click child-menu-add" title="添加子菜单"></a>
															<a href="javascript:void(0);" class="fa fa-pencil" title="编辑"></a>
															<a href="javascript:void(0);" data-menuid="${fatherMenu.id}" class="fa fa-trash-o menu-delete" title="删除"></a>
														</span>
													</p>
													<c:forEach items="${menuList}" var="menu" varStatus="statusChild">
														<c:if test="${fatherMenu.id==menu.menuEntity.id&&menu.menuEntity!=null }">
															<div class="sub-menu childMenu">
																<p data-menuid="${menu.id}" data-menutype="${menu.type}" class="nav-define">
																	${menu.name }
																	<span class="fr">
																		<a href="javascript:void(0);" class="fa fa-pencil" title="编辑"></a>
																		<a href="javascript:void(0);" data-menuid="${menu.id}" class="fa fa-trash-o menu-delete" title="删除"></a>
																	</span>
																</p>
															</div>
														</c:if>
													</c:forEach>
												</td>
												<c:if test="${status.isFirst()}">
													<td class="menu_action_show" rowspan="${fatherMenuList.size()}">
														<div class="defines">
															<!-- 无法设置动作   -->
															<div class="define" id="define_none_action" style="display: none; text-align: center;">已有子菜单无法设置动作</div>
															<!-- 无法设置动作   -->

															<!-- 已经设置了动作 -->
															<div class="define" id="define_reday_action" style="display: none; text-align: center;">action is all ready set .</div>

															<!-- 设置动作   -->
															<div class="define" id="define_set_action">
																<div class="col-lg-12 choose-btn">
																	<p>请选择订阅者点击菜单后，公众号做出的相应动作</p>
																	<a class="cancel-btn tab-msg">
																		<i class="fa fa-envelope-o"></i>发送消息
																	</a>
																	<a class="cancel-btn tab-url">
																		<i class="fa fa-link"></i>跳转链接
																	</a>
																	<a class="cancel-btn tab-other">
																		<i class="fa fa-th"></i>其他功能
																	</a>
																</div>
																<div class="col-lg-12 news" style="display: none;">
																	<p>订阅者点击该子菜单会显示以下信息</p>
																	<div class="new-nav">
																		<span class="curre" data="text">文字消息</span>
																		<span data="news">图文消息</span>
																		<span data="voice">语音消息</span>
																	</div>
																	<div class="new-conts">
																		<div class="new" id="text" style="display: block;">
																			<select name="menuText" class="form-control subscribeType">
																				<option value="id">模板名称</option>
																			</select>
																			<div class="col-lg-5 choose-btn">
																				<a data-type="click" data-msgType="text" class="menu_action_save">保存</a>
																				<a class="cancel-btn menu-return">返回</a>
																			</div>
																		</div>
																		<div class="new lookup" id="news" style="display: none;">
																			<select name="menuNews" class="form-control subscribeType">
																				<option value="id">模板名称</option>
																			</select>
																			<div class="col-lg-5 choose-btn">
																				<a data-type="click" data-msgType="news" class="menu_action_save">保存</a>
																				<a class="cancel-btn menu-return">返回</a>
																			</div>
																		</div>
																		<div class="new lookup" id="voice" style="display: none;">
																			<select name="menuVoice" class="form-control subscribeType">
																				<option value="id">模板名称</option>
																			</select>
																			<div class="col-lg-5 choose-btn">
																				<a data-type="click" data-msgType="voice" class="menu_action_save">保存</a>
																				<a class="cancel-btn menu-return">返回</a>
																			</div>
																		</div>
																	</div>
																</div>
																<div class="col-lg-12 urls" style="display: none;">
																	<p>订阅者点击该子菜单会跳到以下链接</p>
																	<input type="text" name="menuUrl" class="form-control" placeholder="请输入URL">
																	<div class="col-lg-5 choose-btn">
																		<a data-type="view" class="menu_action_save">保存</a>
																		<a class="cancel-btn menu-return">返回</a>
																	</div>
																</div>
																<div class="col-lg-12 others" style="display: none;">
																	<p>订阅者点击该子菜单会有响应的动作</p>
																	<select name="menuOtherAction" class="form-control subscribeType">
																		<option value="customerservice">转接人工</option>
																	</select>
																	<div class="col-lg-5 choose-btn">
																		<a data-type="click"  class="menu_action_save">保存</a>
																		<a class="cancel-btn menu-return">返回</a>
																	</div>
																</div>
															</div>
														</div>
														<!-- 设置动作   -->
													</td>
												</c:if>
											</tr>
										</c:forEach>
									</table>
									<p style="padding-left: 20px;">发布之后才可以在微信里面显示</p>
									<div class="col-lg-12 choose-btn">
										<a class="weixinMenuSubmit" href="javascript:;">发布到微信</a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>

		<!--素材选择-->
		<div id="focus_modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<form action="">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h4 class="modal-title" id="myModalLabel">信息录入</h4>
						</div>
						<div class="modal-body">
							<div class="form-group">
								<label class="control-label">消息类型：</label> <select name="mediaType" class="form-control subscribeType">
									<option value="text">文本消息</option>
									<option value="news">图文消息</option>
									<option value="voice">语音消息</option>
								</select>
							</div>
							<div class="form-group">
								<label class="control-label">消息类型：</label> <select name="mediaId" class="form-control subscribeList">
									<option value="1">1</option>
								</select>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-primary mediaSubmit">确定</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						</div>
					</form>
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
							<label class="control-label">菜单名的名字长度不多于6个文字或字符</label> <input type="text" class="form-control" name="menuName" maxlength="6" placeholder="请输入菜单名"> <input type="hidden" name="fatherMenuId">
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary menuAddSubmit">确定</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
				</div>
			</div>
		</div>
		<!-- 添加菜单对话框结束 -->
	</div>

</body>
</html>