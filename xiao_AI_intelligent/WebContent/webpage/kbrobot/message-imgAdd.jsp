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
		//图文消息富文本框绑定
		UE.getEditor('content_0');
		//默认展开第一个
		Location('collapse_0');
		//编辑菜单显示隐藏
		$(".left_list").on("mouseover", "li", function() {
			var editMenu = $(this).find(".yun_display_none");
			editMenu.stop().fadeIn();
		});
		$(".left_list").on("mouseleave", "li", function() {
			var editMenu = $(this).find(".yun_display_none");
			editMenu.stop().fadeOut();
		});

		function imageFileUpload() {
			//文件上传
			$("input[id^='fileupload_']").fileupload({
				dataType : 'json',
				done : function(e, data) {
					/* $("tr:has(td)").remove();
					$.each(data.result, function (index, file) {
					    $("#uploaded-files").append(
					    		$('<tr/>')
					    		.append($('<td/>').text(file.fileName))
					    		.append($('<td/>').text(file.fileSize))
					    		.append($('<td/>').text(file.fileType))
					    		.append($('<td/>').html("<a href='fileUploadController.do?view&index="+index+"'>下载</a>"))
					    		)//end $("#uploaded-files").append()
					});  */

					alert($(this).attr("id"));

					alert(data.toString());

					alert("上传成功!");
				},
				progressall : function(e, data) {
					/* var progress = parseInt(data.loaded / data.total * 100, 10);
					$('#progress .bar').css(
					    'width',
					    progress + '%';
					); */
				},
				dropZone : $('#dropzone')
			//拖拽区域
			});
		}

		//初始化
		$('#addline').click();
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
					<div class="panel-cont">
						<h4 class="title">添加多图文消息</h4>
						<form id="imgTxt">
							<div class="col-sm-12 row-fluid">
								<!--左侧开始-->
								<div class="col-sm-3">
									<div class="col-sm-12 thumbnail">
										<ol class="left_list">
											<li>
												<div class="yun_position_relative">
													<div class="yun_align_new">
														<img id="img1" src="plug-in/kbrobot/images/thumb_index.png">
													</div>
													<div class="yun_display_none">
														<div class="yun_position_absolute_0">
															<a onclick="Location('collapse_0')">
																<i class="fa fa-pencil"></i>
															</a>
														</div>
													</div>
												</div>
												<div class="yun_position_absolute">
													<div class="yun_color_fff">
														<span id="left_title_0" class="yun_img_text_title fonts">主标题</span>
													</div>
												</div>
											</li>
										</ol>
										<button type="button" class="btn btn-success btn-block" id="addline">
											<i class="fa fa-plus"></i> 增加一行
										</button>
									</div>
								</div>
								<!--左侧结束-->
								<!--右侧开始-->
								<div class="col-sm-9">
									<ol class="accordion panel-group right_list" id="accordion2">
										<li class="accordion-group mt10">
											<div class="panel-heading">
												<h4 class="panel-title">
													<a href="#collapse_0" data-parent="#accordion2" data-toggle="collapse" class="collapsed" id="right_title_0" onclick="Location('collapse_0')">主标题</a>
												</h4>
											</div>
											<div class="panel-collapse collapse in" id="collapse_0">
												<div class="panel-body">
													<label>标题</label>
													<input type="text" name="title" id="title1" class="form-control" maxlength="64" onChange="testChange(this,'left_title_0','right_title_0')">
													<label class="m10">作者</label>
													<input type="text" name="author" id="author1" class="form-control" maxlength="8">
													<div class="imgFile_1" style="margin-top: 10px; position: relative;">
														<span class="btn btn-success fileinput-button">
															<i class="fa fa-plus"></i>
															<span>请选择封面图片</span>
															<input id="fileupload_1" type="file" name="file"  data-url="fileUploadController.do?upload">
														</span>
														<p class="clearBoth">大图片建议尺寸：900像素 * 500像素;</p>
                                                        <div class="progress progress-bar-success"></div>
														<div id="files_1" class="files"></div>
													</div>
													<label class="m10">图文内容</label>
													<script id="content_0" type="text/plain" style="height: 200px; width: 100%;" name="content"></script>
												</div>
											</div>
										</li>

									</ol>
									<!--右侧结束-->
									<div class="row">
										<div class="col-lg-5 choose-btn">
											<a>保存</a>
											<a href="message-img.html" class="cancel-btn tab-return">返回</a>
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
