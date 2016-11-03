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
						<h4 class="title">投诉举报</h4>
						<div class="row">
							<div class="col-lg-12">
								<table class="table table-model-2 table-hover">
                            	<tr>
                                	<th>ID</th>
                                    <th>投诉人姓名</th>
                                    <th>投诉人手机号码</th>
                                    <th>被投诉人姓名</th>
                                    <th>被投诉人手机号码</th>
                                    <th>投诉内容</th>
                                    <th>操作</th>
                                </tr>
                                <tr>
                                	<td colspan="7" align="center">当前记录为空</td>
                                </tr>
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


</body>
</html>
