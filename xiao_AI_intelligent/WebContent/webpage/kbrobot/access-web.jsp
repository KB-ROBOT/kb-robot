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
				<div class="col-sm-3"></div>
				<div class="panel col-sm-6">
					<h4 class="title">网页接入</h4>
					<div class="panel-cont robot-icon">
						<h4>选择在线图标</h4>
						<div class="col-lg-12" style="float: none; overflow: hidden;">

							<form style="margin-top: 20px;">
								<div class="col-lg-3">
									<input type="radio" id="radio1" name="icon"><label for="radio1"><b>机器人客服</b></label>
								</div>
								<div class="col-lg-4">
									<input type="radio" id="radio2" name="icon" checked="checked"><label for="radio2"><img src="images/chatp3.png"></label>
								</div>
								<div class="col-lg-5">
									<input type="radio" id="radio3" name="icon"><label for="radio3"><img src="images/chatp6.png"></label>
								</div>
							</form>
						</div>
					</div>
					<div class="panel-cont robot-icon">
						<h4>设定图标位置</h4>
						<div class="col-lg-12" style="float: none; overflow: hidden; margin-top: 20px;">
							<form>
								图标放置位置： <input type="radio" id="position1" name="position"><label for="position1">左侧</label> <input type="radio" id="position2" name="position" checked="checked"><label for="position2">右侧</label>
								<p>距离右侧：（px）</p>
								<div id="mLeft" class="ui-slider ui-corner-all ui-slider-horizontal ui-widget ui-widget-content">
									<div class="ui-slider-range ui-corner-all ui-widget-header ui-slider-range-min"></div>
									<span tabindex="0" class="ui-slider-handle ui-corner-all ui-state-default">
										<b id="amount">50</b>
									</span>
								</div>
								<p>距离底部：（px）</p>
								<div id="mBottom" class="ui-slider ui-corner-all ui-slider-horizontal ui-widget ui-widget-content">
									<div class="ui-slider-range ui-corner-all ui-widget-header ui-slider-range-min"></div>
									<span tabindex="0" class="ui-slider-handle ui-corner-all ui-state-default">
										<b id="amount">50</b>
									</span>
								</div>
							</form>
						</div>
					</div>
					<div class="col-lg-12 choose-btn">
						<a href="">生成代码</a>
					</div>
				</div>
			</div>
		</div>

	</div>

	<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
	<script type="text/ecmascript" src="js/jquery-ui.js"></script>
	<script type="text/ecmascript" src="js/jquery-ui.min.js"></script>
	<script type="text/ecmascript" src="js/bootstrap-datetimepicker.js"></script>
	<script type="text/ecmascript" src="js/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/ecmascript" src="js/style.js"></script>

</body>
</html>
