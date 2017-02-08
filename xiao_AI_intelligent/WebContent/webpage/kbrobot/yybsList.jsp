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
		$(".del").click(function(){
			var id = $(this).data("id");
			
			var customerBespeakEntity = {};
			
			customerBespeakEntity["id"] = id;
			
			dialog({
				title : "警告",
				content : "确认删除？",
				ok : function() {
					var url = "./commonBespeakController.do?bespeakDel";
					$.ajax({
						url : url,// 请求的action路径
						type : 'get',
						dataType : "json",
						jsonpCallback:"callback",
						data : customerBespeakEntity,
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
				},
				cancle : function() {
				}
			}).width(320).showModal();
			
			
		});
		
		$(".bespeakEdit").click(function(){
			$("#bespeakEditModal").modal("show");
		});
		
		$(".submit-btn").click(function(){
			
			var customerBespeakSetEntity = {};
			var successFlag = false;
			
			$(this).parent().find(".form-control").each(function(index,element){
				
				var elementVla = $(element).val();
				
				if(elementVla == ''){
					alert($(element).parent().find("label").text() + "不能为空");
					successFlag = false;
					return false;
				}
				
				customerBespeakSetEntity[$(element).attr("name")] = elementVla;
				successFlag = true;
			});
			
			if(!successFlag){
				return ;
			}
			
			
			var url = "./commonBespeakController.do?bespeakEdit";
			$.ajax({
				url : url,// 请求的action路径
				type : 'get',
				dataType : "json",
				jsonpCallback:"callback",
				data : customerBespeakSetEntity,
				success : function(data) {
					
					if(data.success){
						location.reload();
					}
					else{
						alert("操作失败，请稍后重试");
					}
					
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
		            /* alert(XMLHttpRequest.status);
		            alert(XMLHttpRequest.readyState);
		            alert(XMLHttpRequest.responseText);
		            alert(textStatus);
		            alert(errorThrown); */
		            console.log(textStatus);
		        }
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
						<h4 class="title">预约办税</h4>
						<div class="row" style="margin-bottom: 20px;">
							<div class="col-lg-12">
								<div class="col-lg-8">
									<form>
										<div class="col-sm-3">
											起始时间： <input type="text" class="form-control input-append begin_datetime" size="16" style="width: 60%; display: inline-block;">
										</div>
										<div class="col-sm-3">
											结束时间： <input type="text" class="form-control input-append end_datetime" size="16" style="width: 60%; display: inline-block;">
										</div>
										<div class="col-sm-3">
											<div class="btn-group">
												<button type="button" class="btn btn-success visitDataSearch">
													<i class="fa fa-files-o"></i>&nbsp;导出
												</button>
											</div>
										</div>
									</form>
								</div>
								<div class="col-lg-4">
									<form id="search_form" action="commonBespeakController.do?bespeakList" method="POST" class="navbar-left navbar-form" style="margin: 0;">

										<div class="input-group input-group-minimal form-inline" style="width: 410px;">
											<input type="text" name="searchParam" class="form-control pull-right" value="${searchParam}" style="height: auto !important;">
											<span class="input-group-btn yun_btn">
												<div class="form-group" style="background: #ddd;">
													<select name="searchKey" class=" form-control" value="${searchKey}" style="border: none; height: 33px;">
														<option value="yybssbh">纳税识别号</option>
														<option value="yyfwxm">服务项目</option>
													</select>
												</div>
												<button class="btn btn-white dropdown-toggle" type="submit" onclick="submit();" style="margin-left: 3px;">
													<i class="fa fa-search"></i>
												</button>
											</span>

										</div>
									</form>
								</div>
							</div>

							<div class="col-lg-12">
								<div class="col-sm-3">
									<br/>
									当前预约信息接收微信：${customerBespeakSetEntity.name }  (
									<a href="javascript:;" class="bespeakEdit">点此修改</a>
									)
								</div>
							</div>
						</div>
						<div class="row">
							<div class="adviceBox col-lg-12">
								<table class="table table-model-2 table-hover">
									<tbody>
										<tr>
											<th>预约名称</th>
											<th>联系方式</th>
											<th>纳税人识别号</th>
											<th>预约服务项目</th>
											<th>预约日期</th>
											<th>预约时间段</th>
											<th>操作</th>
										</tr>
										<!-- <tr>
											<td>姓名</td>
											<td>12345678901</td>
											<td>0123456789012345</td>
											<td>发票代开</td>
											<td>2017-02-04</td>
											<td>9:00--9:30</td>
											<td>
												<a href="" class="del">删除</a>
											</td>
										</tr> -->
										<c:forEach items="${customerBespeakList}" var="customerBespeak" varStatus="status">
											<tr>
												<td>${customerBespeak.yybsxm }</td>
												<td>${customerBespeak.yybshm }</td>
												<td>${customerBespeak.yybssbh }</td>
												<td>${customerBespeak.yyfwxm }</td>
												<td>${customerBespeak.yyrq }</td>
												<td>${customerBespeak.yybssh }</td>
												<td>
													<a href="javascript:;" data-id="${customerBespeak.id}" class="del">删除</a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
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
	<!-- 修改预约接收微信 -->
	<div class="modal fade" id="bespeakEditModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h3 id="myModalLabel">修改预约提醒微信号</h3>
				</div>
				<div class="panel-cont">
					<form>
						<input type="hidden" name="id">
						<div class="form-group">
							<label class="control-label">名称</label> <input type="text" class="form-control" name="name" maxlength="50">
						</div>
						<div class="form-group">
							<label class="control-label">openId</label>
							<span>(切记不可填错，否则无法正常通知，建议从手机上复制)</span>
							<input type="text" class="form-control" name="openId" maxlength="50">
						</div>
						<div class="form-group">
							<label class="control-label">说明：</label>

							关于openId的获取，请在微信端访问这个链接：
							
							<textarea class="form-control" readonly="readonly" rows="8">https://open.weixin.qq.com/connect/oauth2/authorize?appid=${weiXinAccount.accountAppid}&redirect_uri=http%3a%2f%2frobot.kb-robot.com%2foAuth2JumpController.do%3fredirectTargetUrl%26targetRedirectUrl%3dhttp%253a%252f%252flbgs.kb-robot.com%252fwebpage%252fshowOpenId.html%253ftargetOpenid%253dOPENID&response_type=code&scope=snsapi_base&state=STATE&component_appid=wx520d1bc0926617f0#wechat_redirect</textarea>

						</div>
						<input type="hidden" class="form-control" name="accountId" value="${weiXinAccount.id}">
					</form>
					<a href="javascript:void(0);" class="submit-btn">保存</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
