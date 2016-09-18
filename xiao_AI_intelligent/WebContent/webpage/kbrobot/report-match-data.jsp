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

		// 访客数量初始化echarts实例
		var matchDataPie = echarts.init($("#martch_data_pie")[0]);
		// 访客地域初始化echarts实例
		//var matchDataLine = echarts.init($("#martch_data_line")[0]);
		//搜索
		$(".marchDataSearch").click(function() {
			//获取开始和结束时间
			startTime = $(".begin_datetime").val();
			endTime = $(".end_datetime").val();
			//loading动画
			matchDataPie.showLoading();
			//matchDataLine.showLoading();

			//异步加载
			var url = "./dataReportController.do?show-match-data";
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
					matchDataPie.hideLoading();
					//matchDataLine.hideLoading();

					if (data.success) {
						var matchDataPieOption = {
							title : {
								text : '问题匹配率统计',
								x : 'left'
							},
							tooltip : {
								trigger : 'item',
								formatter : "{a} <br/>{b} : {c} ({d}%)"
							},
							legend : {
								orient : 'horizontal',
								left : 'center',
								data : [ '未知问题', '直接匹配', '引导匹配','引导回复']
							},
							series : [ {
								name : '匹配统计',
								type : 'pie',
								radius : '75%',
								center : [ '50%', '60%' ],
								data : data.attributes.resultData,
								itemStyle : {
									emphasis : {
										shadowBlur : 10,
										shadowOffsetX : 0,
										shadowColor : 'rgba(0, 0, 0, 0.5)'
									}
								}
							} ]
						};

						matchDataPie.setOption(matchDataPieOption);

					} else {
						dialog({
							content : data.msg,
							title : "错误消息"
						}).show();
					}
				}
			});
		});

		//数据初始化
		$(".marchDataSearch").click();
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
						<h4 class="title">匹配率统计</h4>
						<div class="panel-body">
							<div class="col-lg-12">
								<form>
									<div class="col-sm-3">
										起始时间： <input type="text" class="form-control input-append begin_datetime" size="16" style="width: 60%; display: inline-block;">
									</div>
									<div class="col-sm-3">
										结束时间： <input type="text" class="form-control input-append end_datetime" size="16" style="width: 60%; display: inline-block;">
									</div>
									<div class="col-sm-3">
										<div class="btn-group">
											<button type="button" class="btn btn-success marchDataSearch">
												<i class="fa fa-search "></i>&nbsp;查询
											</button>
										</div>
									</div>
								</form>
								<div class="col-lg-12" style="margin-top: 50px;">
									<div id="martch_data_pie" style="width: 100%; height: 500px;"></div>
								</div>

								<!-- <div class="col-lg-12" style="margin-bottom: 50px;">
									<div id="martch_data_line" style="width: 100%; height: 500px;"></div>
								</div> -->
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
</body>
</html>
