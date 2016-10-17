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
	function refreshKfaccount() {
		var url = "./customServiceController.do?refreshCustomList";
		$.ajax({
			url : url,// 请求的action路径
			type : 'post',
			dataType : "json",
			success : function(data) {
				if (data.success) {
					var successDialog = dialog({
						content : "刷新列表成功",
						title : "成功",
						onclose : function() {
							location.reload();
						}
					}).show();

					setTimeout(function() {
						successDialog.close().remove();
					}, 1000);
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
	}

	function uploadHeadImg(fileObj) {
		$("#progress_headImg .progress-bar").css('width', '0%');
		$(fileObj).fileupload({
			dataType : 'json',
			acceptFileTypes : /(\.|\/)(jpe?g|png)$/i,
			maxFileSize : 2000000, // 2 MB
			done : function(e, data) {
				$("#previewImg").remove();
				$(this).parent().append("<img width='50px' height='50px' id='previewImg' src='"+data.result.attributes.url+"' />");
				$(this).parent().find("input[name='headImg']").val(data.result.attributes.url);
			},
			progressall : function(e, data) {
				var progress = parseInt(data.loaded / data.total * 100, 10);
				$("#progress_headImg .progress-bar").css('width', progress + '%');
			},
			dropZone : $('#dropzone')
		//拖拽区域
		});
	}

	$(document).ready(function() {

		$(".updateBtn").click(function() {
			refreshKfaccount();
		});

		$(".customBindWeixin").click(function() {

			$("#bindWeixin").modal();
			var kfAccount = $(this).data("kfaccount");
			$("#bindWeixin").find("input[name='kfaccount']").val(kfAccount);
		});

		//确认绑定
		$(".submitBind").click(function() {

			var inviteWx = $("#bindWeixin").find("input[name='inviteWx']").val();
			var kfAccount = $("#bindWeixin").find("input[name='kfaccount']").val();
			if (inviteWx == '' || inviteWx == undefined) {
				$("#bindWeixin").find("input[name='inviteWx']").focus();
				return;
			}

			var url = "./customServiceController.do?bindWeixinAccount";
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				data : {
					"inviteWx" : inviteWx,
					"kfAccount" : kfAccount
				},
				dataType : "json",
				success : function(data) {
					if (data.success) {
						var successDialog = dialog({
							content : "绑定成功",
							title : "成功",
							onclose : function() {
								refreshKfaccount();
							}
						}).show();

						setTimeout(function() {
							successDialog.close().remove();
						}, 2000);
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

		//查询验证
		/* $(".checkOpenId").click(function(){
			var openId = $("#bindWeixin").find("input[name='inviteWx']").val();
			var url = "./customServiceController.do?checkOpenId";
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				data : {
					"openId" : openId
				},
				dataType : "json",
				success : function(data) {
					if (data.success) {
						$(this).parent().append("<img width='50px' height='50px' src='"+data.wxUser.headimgurl+"' />");
						weixinCheck = true;
					}
					else {
						weixinCheck = false;
						dialog({
							content : data.msg,
							title : "错误消息"
						}).show();
					}
				},
				error : function() {// 请求失败处理函数
				},
			});
		}); */

		$(".submitCustomAdd").click(function() {
			var accountNum = $("#customAdd").find("input[name='accountNum']").val();
			var nickname = $("#customAdd").find("input[name='nickname']").val();

			if (accountNum == null || accountNum == '') {
				$("#customAdd").find("input[name='accountNum']").focus();
				return;
			}
			if (nickname == null || nickname == '') {
				$("#customAdd").find("input[name='nickname']").focus();
				return;
			}

			var url = "./customServiceController.do?sustomServiceAdd";
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				data : {
					"accountNum" : accountNum,
					"nickname" : nickname
				},
				dataType : "json",
				success : function(data) {
					if (data.success) {

						var successDialog = dialog({
							content : "添加成功，稍后刷新",
							title : "成功",
							onclose : function() {
								refreshKfaccount();
							}
						}).show();

						setTimeout(function() {
							successDialog.close().remove();
						}, 500);
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

		$(".kfDel").click(function() {
			var customid = $(this).data("customid");
			dialog({
				title : "警告",
				content : "确认删除？",
				ok : function() {
					var url = './customServiceController.do?sustomServiceDel';
					$.ajax({
						url : url,// 请求的action路径
						type : 'post',
						dataType : "json",
						data : {
							"customId" : customid
						},
						success : function(data) {
							var successDialog = dialog({
								content : "删除成功，稍后刷新",
								title : "成功",
								onclose : function() {
									refreshKfaccount();
								}
							}).show();

							setTimeout(function() {
								successDialog.close().remove();
							}, 500);
						},
						error : function() {// 请求失败处理函数

						},
					});
				},
				cancle : function() {
				}
			}).width(320).showModal();
		});
		
		//客服信息编辑
		$(".kfEdit").click(function(){
			var customId = $(this).data("customid");
			$("#customEdit").find("input[name=customid]").val(customId);
			$("#customEdit").modal();
		});
		//确认
		$(".submitCustomEdit").click(function(){
			var customid = $("#customEdit").find("input[name='customid']").val();
			var nickname = $("#customEdit").find("input[name='nickname']").val();
			var headImg = $("#customEdit").find("input[name='headImg']").val();
			
			var url = './customServiceController.do?sustomServiceEdit';
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				dataType : "json",
				data : {
					"customid" : customid,
					"nickname":nickname,
					"headImg":headImg
				},
				success : function(data) {
					var successDialog = dialog({
						content : "保存成功，稍后刷新",
						title : "成功",
						onclose : function() {
							
							$("#customEdit").find("input[name=customid]").val("");
							$("#customEdit").find("input[name=nickname]").val("");
							$("#customEdit").find("input[name=headImg]").val("");
							refreshKfaccount();
						}
					}).show();

					setTimeout(function() {
						successDialog.close().remove();
					}, 500);
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
				<div class="col-sm-3"></div>
				<div class="panel col-sm-12">
					<div class="cont">
						<h4 class="title">
							人工客服列表
							<a data-toggle="modal" data-target="#customAdd" href="javascript:;" class="fa fr addBtn" title="添加">
								<i class="fa fa-plus-circle"></i>添加客服
							</a>
							<a href="javascript:;" class="fa fr updateBtn" title="刷新">
								<i class="fa fa-refresh"></i>同步客服列表
							</a>
						</h4>
						<table class="table table-model-2">
							<tr>
								<th>序号</th>
								<th>帐号</th>
								<th>头像</th>
								<th>编号</th>
								<th>昵称</th>
								<th>已绑定微信号</th>

								<th>绑定邀请微信号</th>
								<th>邀请过期时间</th>
								<th>邀请状态</th>
								<th>操作</th>
							</tr>
							<c:if test="${customList.size()==0}">
								<tr>
									<td align="center" colspan="10">还未添加任何客服（同步客服列表试试看~）</td>
								</tr>
							</c:if>
							<c:forEach items="${customList}" var="custom" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${custom.kfAccount}</td>
									<td>
										<c:if test="${custom.kfHeadimgurl==''||custom.kfHeadimgurl==null}">
											<img src="./plug-in/kbrobot/images/noheadimg.png" />
										</c:if>
										<c:if test="${custom.kfHeadimgurl!=null&&custom.kfHeadimgurl!=''}">
											<img src="${custom.kfHeadimgurl}" />
										</c:if>
									</td>
									<td>${custom.kfId}</td>
									<td>${custom.kfNick}</td>
									<td>${custom.kfWx}</td>
									<td>${custom.inviteWx}</td>
									<td>${custom.inviteExpireTime}</td>
									<td>${custom.inviteStatus}</td>

									<td align="center">
										<c:if test="${custom.kfWx==null||custom.kfWx==''}">
											<a href="javascript:void(0);" data-kfaccount="${custom.kfAccount}" class="customBindWeixin">绑定微信号</a>
										</c:if>
										<a href="javascript:void(0);" data-customid="${custom.id}" class="kfEdit">编辑</a>
										<a href="javascript:void(0);" class="kfDel" data-customid="${custom.id}">删除</a>
									</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 添加客服 -->
	<div class="modal fade" id="customAdd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="myModalLabel">添加客服</h3>
				</div>
				<div class="panel-cont">
					<input type="hidden" name="id">
					<div class="form-group">
						<label class="control-label">账号</label>
						<span>:最多10个字符，必须是英文、数字字符或者下划线</span>
						<input type="text" class="form-control" name="accountNum" maxlength="10">
					</div>
					<div class="form-group">
						<label class="control-label">昵称</label> <input type="text" class="form-control" name="nickname" maxlength="16">
					</div>
					<a href="javascript:void(0);" class="submit-btn submitCustomAdd">保存</a>
				</div>
			</div>
		</div>
	</div>
	<!-- 添加客服 -->
	<!-- 绑定微信号 -->
	<div class="modal fade" id="bindWeixin" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="myModalLabel">绑定微信</h3>
				</div>
				<div class="panel-cont">
					<input type="hidden" name="kfaccount">
					<div class="form-group">
						<label class="control-label">微信号</label>
						<!-- <div>
								<input type="text" class="form-control" name="inviteWx" maxlength="50" style="width: 87%; display: inline-block;">
								<a class="btn btn-default checkOpenId">验证</a>
							</div> -->
						<input type="text" class="form-control" name="inviteWx" maxlength="50" style="width: 87%; display: inline-block;">
					</div>
					<a href="javascript:void(0);" class="submit-btn submitBind">确认绑定</a>
				</div>
			</div>
		</div>
	</div>
	<!-- 绑定微信号 -->
	<!-- 编辑客服 -->
	<div class="modal fade" id="customEdit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="myModalLabel">编辑客服</h3>
				</div>
				<div class="panel-cont">
					<input type="hidden" name="customid">
					<div class="form-group">
						<label class="control-label">昵称</label>
						<input type="text" class="form-control" name="nickname" maxlength="16">
					</div>
					<div class="form-group">
						<label class="control-label">上传头像</label>
						<input id="fileupload" type="file"  data-url="weixinArticleController.do?upload" onClick="uploadHeadImg(this)"\>
						<input type="hidden" name="headImg" />
						<div id="progress_headImg" class="progress">
							<div class="progress-bar progress-bar-success" style="width:0%;"></div>
						</div>
					</div>
					<a href="javascript:void(0);" class="submit-btn submitCustomEdit">保存</a>
				</div>
			</div>
		</div>

	</div>
	<!-- 编辑客服 -->
</body>
</html>
