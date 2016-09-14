<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>凯博机器人后台管理系统</title>
<jsp:include page="./includePage/linkSource.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8" src="plug-in/echarts/china.js"></script>

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
		var visitDataChart = echarts.init($("#visit_data")[0]);
		
		// 访客地域初始化echarts实例
		var areaDataChart = echarts.init($("#area_data")[0]);
		
		
		$(".visitDataSearch").on("click",function(){
			//获取开始和结束时间
			startTime = $(".begin_datetime").val();
		 	endTime = $(".end_datetime").val();
			//loading动画
			visitDataChart.showLoading();
			areaDataChart.showLoading();
			
			//异步加载
			var url = "./dataReportController.do?show-visit-data";
			$.ajax({
				url : url,// 请求的action路径
				dataType : "json",
				type : "post",
				data : {"startTime":startTime,"endTime":endTime},
				success : function(data) {
					//隐藏loading动画
					visitDataChart.hideLoading();
					areaDataChart.hideLoading();
					
					if (data.success) {
						// 指定图表的配置项和数据
						var option = {
							title : {
								text : '访客时间段统计'
							},
							tooltip : {
								trigger : 'axis'
							},
							legend : {
								data : ['访客次数']
							},
							toolbox : {
								feature : {
									saveAsImage : {},
									restore: {}
								}
							},
							xAxis : {
								type : 'category',
								boundaryGap : false,
								data : data.attributes.xAxisData
							},
							yAxis : {
								type : 'value',
								boundaryGap : false,
								minInterval: 1
								
							},
							dataZoom : [ { // 这个dataZoom组件，默认控制x轴。
								type : 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
								start : 0, // 左边在 10% 的位置。
								end : 35
							// 右边在 60% 的位置。
							} ],
							series : [ {
								name : '访客次数',
								type : 'line',
								data : data.attributes.seriesData,
								markLine: {
					                silent: true,
					                data: [{
					                    yAxis: 5
					                }, {
					                    yAxis: 10
					                }, {
					                    yAxis: 30
					                }, {
					                    yAxis: 100
					                }, {
					                    yAxis: 200
					                }, {
					                    yAxis: 500
					                }, {
					                    yAxis: 1000
					                }, {
					                    yAxis: 1800
					                }]
					            },
								itemStyle : {
									normal : {
										color : "#35a2d8"
									}
								}
							} ]
						};
						
						var areaDataChartOption = {
							    title: {
							        text: '访客地域统计',
							        subtext: '测试数据'
							    },
							    geo: {
							        map: 'china',
							        label: {
							            emphasis: {
							                show: false
							            }
							        },
							        roam: true,
							        zoom: 1.2,
							        scaleLimit:{
							        	min:1.2
							        },
							        itemStyle: {
							            normal: {
							                areaColor: '#fff',
							                borderColor: '#000'
							            },
							            emphasis: {
							                areaColor: '#eee'
							            }
							        }
							    },
							    series: [
							        {
							            name: '访客数',
							            type: 'effectScatter',
							            coordinateSystem: 'geo',
							            data: data.attributes.locationData ,//[{value:[112.909912,28.207537,50]},{value:[112.909912,28.207537,50]}],
							            symbolSize: function (val) {
							                return val[2] / 10;
							            },
							            label: {
							                normal: {
							                    formatter: '{b}',
							                    position: 'right',
							                    show: false
							                },
							                emphasis: {
							                    show: true
							                }
							            },
							            itemStyle: {
							                normal: {
							                    color: '#35a2d8'
							                }
							            }
							        }
							    ]
							};
						
						// 使用刚指定的配置项和数据显示图表。
						areaDataChart.setOption(areaDataChartOption);
						visitDataChart.setOption(option); 

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
		$(".visitDataSearch").click();

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

			<div class="section">
				<div class="row">
					<div class="panel col-sm-12">
						<div class="panel-cont">
							<h4 class="title">访客次数统计</h4>
							<div class="panel-body">
								<div class="col-lg-12" style="margin-bottom: 20px;">
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
													<i class="fa fa-search"></i>&nbsp;查询
												</button>
											</div>
										</div>
									</form>
								</div>
								<div class="col-lg-12" style="margin-bottom: 50px;">
									<div id="visit_data" style="width: 100%; height: 500px;"></div>
								</div>

								<div class="col-lg-12" style="margin-bottom: 50px;">
									<div id="area_data" style="width: 100%; height: 800px;"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
