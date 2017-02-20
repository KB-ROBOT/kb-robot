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
		//添加
		$(".submitCustomAdd").click(function(){
			
			var customerCallService = {};
			var successFlag = false;
			
			$(this).parent().find(".form-control").each(function(index,element){
				
				var elementValue = $(element).val();
				if((elementValue==""||elementValue === undefined)&& $(element).attr("required") == 'required'){
					successFlag = false;
					alert($(element).attr("name")+"不能为空。");
					return false;
				}
				else{
					customerCallService[$(element).attr("name")] =elementValue;
					successFlag = true;
				}
			});
			
			if(!successFlag){
				return;
			}
			
			var url = "./commonCallServiceController.do?callServiceInsert";
			$.ajax({
				url : url,// 请求的action路径
				dataType : "json",
				jsonpCallback:"callback",
				data : customerCallService,
				success : function(data) {
					
					if(data.success){
						location.reload();
					}
					else{
						alert("操作失败，请稍后重试");
					}
					
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
		            console.log(textStatus);
		        }
			});
			
		});
		
		//删除
		
		$(".kfDel").click(function(){
			var id = $(this).data("id");
			
			var customerCallService = {};
			customerCallService.id = id;
			
			var url = "./commonCallServiceController.do?callServiceDel";
			$.ajax({
				url : url,// 请求的action路径
				dataType : "json",
				jsonpCallback:"callback",
				data : customerCallService,
				success : function(data) {
					
					if(data.success){
						location.reload();
					}
					else{
						alert("操作失败，请稍后重试");
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
		            console.log(textStatus);
		        }
			});
		});
		
		//编辑
		$(".kfEdit").click(function(){
			var _self = this;
			var id = $(_self).data("id");
			var accountId = $("#customAdd").find("input[name=accountId]").val();
			
			$("#customAdd").find(".form-control").each(function(index,element){
				$(element).val($(_self).parent().parent().find("td").eq(index+1).text());
			});
			
			$("#customAdd").find("input[name=id]").val(id);
			$("#customAdd").find("input[name=accountId]").val(accountId);
			
			$("#customAdd").modal();
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
							电话客服列表
							<a data-toggle="modal" data-target="#customAdd" href="javascript:;" class="fa fr addBtn" title="添加">
								<i class="fa fa-plus-circle"></i>添加客服
							</a>
						</h4>
						<table class="table table-model-2">
							<tr>
								<th>序号</th>
								<th>姓名</th>
								<th>头像</th>
								<th>电话号码</th>
								<th>手机号码</th>
								<th>服务类型</th>
								<th>分组</th>
								<th>操作</th>
							</tr>
							<c:if test="${pageList.resultList.size()==0}">
								<tr>
									<td align="center" colspan="8">还未添加任何电话客服</td>
								</tr>
							</c:if>
							<c:forEach items="${pageList.resultList}" var="entity" varStatus="status">
								<tr>
									<td>${status.index + 1}</td>
									<td>${entity.name}</td>
									<td>${entity.headImg}</td>
									<td>${entity.mobile}</td>
									<td>${entity.phone}</td>
									<td>${entity.serviceType}</td>
									<td>${entity.group}</td>
									<td align="center">
										<a href="javascript:void(0);" data-id="${entity.id}" class="kfEdit">编辑</a>
										<a href="javascript:void(0);" class="kfDel" data-id="${entity.id}">删除</a>
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
					<h3 id="myModalLabel">客服信息</h3>
				</div>
				<div class="panel-cont">
					<div class="form-group">
						<label class="control-label">姓名：</label> 
						<input type="text" class="form-control" name="name" required="required" maxlength="10">
					</div>
					<div class="form-group">
						<label class="control-label">头像：</label> 
						<input type="text" class="form-control" name="headImg" required="required" maxlength="10">
					</div>
					<div class="form-group">
						<label class="control-label">电话号码：</label> 
						<input type="tel" class="form-control" name="mobile" required="required" maxlength="50">
					</div>
					<div class="form-group">
						<label class="control-label">手机号码：</label> 
						<input type="tel" class="form-control" name="phone" required="required" maxlength="50">
					</div>
					<div class="form-group">
						<label class="control-label">服务类型：</label>
						<!-- <input type="text" class="form-control" name="serviceType" maxlength="10"> -->
						<select class="form-control" required="required" name="serviceType">
							<option value="1">发票类</option>
							<option value="2">纳税申报</option>
							<option value="3">出口退税</option>
							<option value="4">其他</option>
							<option value="5">增值税 （税收优惠、营改增等）</option>
							<option value="6">消费税</option>
							<option value="7">企业所得税（小微企业、税收优惠等）</option>
						</select>
					</div>
					<div class="form-group">
						<label class="control-label">分组：</label>
						<!-- <input type="text" class="form-control" name="group" maxlength="10"> -->
						<select class="form-control" required="required" name="group">
							<option value="1">税收征管类专家顾问团队</option>
							<option value="2">税收政策类专家顾问团队</option>
						</select>
					</div>
					<a href="javascript:void(0);" class="submit-btn submitCustomAdd">保存</a>
					
					<input type="hidden" class="form-control" name="accountId" required="required" value="${accountId}" />
					<input type="hidden" class="form-control" name="id" />
				</div>
			</div>
		</div>
	</div>
	<!-- 添加客服 -->

	<!-- 编辑客服 -->
	<!-- <div class="modal fade" id="customEdit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="myModalLabel">编辑客服</h3>
				</div>
				<div class="panel-cont">
					<input type="hidden" name="customid">
					<div class="form-group">
						<label class="control-label">姓名：</label> <input type="text" class="form-control" name="nickname" maxlength="16">
					</div>
					<div class="form-group">
						<label class="control-label">上传头像</label> <input id="fileupload" type="file" data-url="weixinArticleController.do?upload" onClick="uploadHeadImg(this)"> <input type="hidden" name="headImg" />
						<div id="progress_headImg" class="progress">
							<div class="progress-bar progress-bar-success" style="width: 0%;"></div>
						</div>
					</div>
					<a href="javascript:void(0);" class="submit-btn submitCustomEdit">保存</a>
				</div>
			</div>
		</div>

	</div> -->
	<!-- 编辑客服 -->
</body>
</html>
