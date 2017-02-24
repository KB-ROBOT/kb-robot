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
		//开始和结束时间
		var startTime = null;
		var endTime = null;

		//开始时间
		$(".begin_datetime").datetimepicker({
			todayBtn : "linked",
			autoclose : true,
			todayHighlight : true,
			endDate : new Date(),
			format : "yyyy-mm-dd hh:ii",
			language : 'zh-CN'
		}).on('changeDate', function(e) {
			startTime = e.date;
			$('.end_datetime').datetimepicker("setStartDate", startTime);
		});

		//结束时间
		$(".end_datetime").datetimepicker({
			todayBtn : "linked",
			autoclose : true,
			todayHighlight : true,
			endDate : new Date(),
			format : "yyyy-mm-dd hh:ii",
			language : 'zh-CN'
		}).on('changeDate', function(e) {
			endTime = e.date;
			$('.begin_datetime').datetimepicker("setEndDate", endTime);
		});

		// 客服点击初始化echarts实例
		var callServiceCountChart = echarts.init($("#callServiceCount")[0]);

		$(".callServiceCountSearch").on("click", function() {
			//获取开始和结束时间
			startTime = $(".begin_datetime").val();
			endTime = $(".end_datetime").val();
			//loading动画
			callServiceCountChart.showLoading();

			//异步加载
			var url = "./commonCallServiceController.do?callServiceCount";
			$.ajax({
				url : url,// 请求的action路径
				dataType : "json",
				type : "post",
				data : {
					"startTime" : startTime,
					"endTime" : endTime
				},
				success : function(data) {
					//隐藏loading动画
					callServiceCountChart.hideLoading();

					if (data.success) {
						// 指定图表的配置项和数据
						var option = {
							title : {
								text : '电话客服统计',
								subtext : '名字后两个数字分别表示"分组"和"类型"'
							},
							tooltip : {
								trigger : 'axis',
								axisPointer : {
									type : 'shadow'
								}
							},
							legend : {
								//data : [ '2011年']
							},
							grid : {
								left : '3%',
								right : '4%',
								bottom : '3%',
								containLabel : true
							},
							xAxis : {
								type : 'value',
								boundaryGap : [ 0, 1 ]
							},
							yAxis : {
								type : 'category',
								data : data.attributes.nameArray
							},
							series : [ {
								name : '纳税人点击次数',
								type : 'bar',
								data : data.attributes.clickTimesArray
							}]
						};

						callServiceCountChart.setOption(option);

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

		//数据初始化
		$(".callServiceCountSearch").click();

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
						<h4 class="title">电话客服统计</h4>
						<div class="panel-body">
							<div class="col-lg-12" style="margin-bottom: 20px;">
								<form>
									<div class="col-sm-3">
										<div class="btn-group">
											<button type="button" class="btn btn-success callServiceCountSearch">
												<i class="fa fa-refresh"></i>&nbsp;刷新
											</button>
										</div>
									</div>
								</form>
							</div>
							<div class="col-lg-12" style="margin-bottom: 50px;">
								<div id="callServiceCount" style="width: 100%; height: 500px;"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
