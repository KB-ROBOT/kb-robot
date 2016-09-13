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
	//文件上传
	function imageChange(liSeq) {
		$("#fileupload_" + liSeq).fileupload({
			dataType : 'json',
			acceptFileTypes : /(\.|\/)(gif|jpe?g|png)$/i,
			maxFileSize : 2000000, // 2 MB
			done : function(e, data) {
				$("#img_" + liSeq).attr("src", data.result.attributes.url);
				$("#left_del_" + liSeq).data("delurl", data.result.attributes.delurl);
				$("#imgFile_" + liSeq).append("<input type='hidden'  value='"+data.result.attributes.url+"' name='imagePath'/>");
				//alert($(this).attr("id"));
				//alert("上传成功!");
			},
			progressall : function(e, data) {
				var progress = parseInt(data.loaded / data.total * 100, 10);
				$("#progress_" + liSeq + " .progress-bar").css('width', progress + '%');
			},
			dropZone : $('#dropzone')
		//拖拽区域
		});
	}

	function delpage(clickElement) {

		//删除图片
		var url = $(clickElement).data("delurl");
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			url : url,// 请求的action路径
			error : function() {// 请求失败处理函数

			},
			success : function(data) {
				//alert("删除图片成功！");
			}
		});
		$(clickElement).data("delurl", "#");

		var length = $('.left_list li').length;
		var seq = $(clickElement).data('seq');
		if (seq == 0) {
			$("#img_0").attr("src", "plug-in/kbrobot/images/thumb_index.png");

		}
		if (length > 1) {
			$('#left_' + seq).remove();
			$('#right_' + seq).remove();
			$('#right_' + seq).removeClass('accordion-group');
		}
	}
	//点击修改
	function Location(id) {
		$('#' + id).collapse('show');
		$('#' + id).parent().siblings().children('.collapse').removeClass("in");
		$('#' + id).parent().siblings().children('.collapse').attr("style", "height:0;");
	}

	$(document).ready(function() {
		//图文消息富文本框绑定
		
		$("script[id^=content_]").each(function(index,element){
			UE.getEditor($(element).attr("id"));
		});
		
		//编辑菜单显示隐藏
		$(".left_list").on("mouseover", "li", function() {
			var editMenu = $(this).find(".yun_display_none");
			editMenu.stop().fadeIn();
		});
		$(".left_list").on("mouseleave", "li", function() {
			var editMenu = $(this).find(".yun_display_none");
			editMenu.stop().fadeOut();
		});

		var li_TnpLength = 20;
		var i = 0;

		//点击添加
		$('#addline').click(function(e) {
			var length = $('.left_list li').length;
			if (length >= 10) {
				toastr.error("多图文最多由十个图文组成", opts);
				return;
			}
			var s = '';
			var m = '', b = '';
			//左侧添加
			i++;
			s = '<li id="left_'+i+'" class="yun_position_relative">';
			s += '<div class="row yun_border_bottom_e">';
			s += '<div class="col-sm-8 yun_overflow_hidden fonts" id="left_title_'+i+'">标题' + i + '</div>';
			s += '<div class="col-sm-4">';
			s += '<img id=\"img_'+i+'\" class=\"pull-right\" src=\"plug-in/kbrobot/images/thumb.png\">';
			s += '</div>';
			s += '</div>';
			s += '<div class="yun_display_none">';
			s += '<div class="yun_position_absolute_'+i+'">';
			s += '<a onClick="Location(\'collapse_' + i + '\')"><i class="fa fa-pencil"></i></a>&nbsp;';
			s += '<a id=\"left_del_' + i + '\" data-delurl="#" data-seq=\"' + i + '\" onClick=\"delpage(this)\" ><i class="fa fa-trash-o"></i></a>';
			s += '</div>';
			s += '</div>';
			s += '</li>';
			$('.left_list').append(s);

			//右侧添加
			m = '<li class="accordion-group mt10" id="right_'+i+'">';
			m += '<div class="panel-heading">';
			m += ' <h4 class="panel-title"><a aria-controls="collapseOne" aria-expanded="false" href="#collapse_' + i + '" data-parent="#accordionRight" data-toggle="collapse" class="collapsed" id="right_title_' + i + '" onClick="Location(\'collapse_' + i + '\')">标题' + i + '</a></h4>';
			m += '</div>';
			m += '<div id="collapse_'+i+'" class="panel-collapse collapse" aria-expanded="false">';
			m += '<div class="panel-body"><label>标题</label>';
			m += '<input type=\"text\" name=\"title\" id=\"title_' + i + '\" class=\"form-control\" maxlength=\"64\" onChange=\"testChange(this,\'left_title_' + i + '\',\'right_title_' + i + '\')\">';
			m += '<label class="m10">作者</label><input type=\"text\" name=\"author\" id=\"author_'+i+'\" class=\"form-control\" maxlength=\"8\" >';
			m += '<label class="m10">描述</label>';
			m += '<input type="text" name="description" id="description_0" class="form-control" maxlength="64">';
			m += '<div id=\"imgFile_'+i+'\"  style=\"margin-top:10px;position:relative;\">';
			m += '<span class=\"btn btn-success fileinput-button\"> <i class=\"fa fa-plus\"></i> <span>请选择封面图片</span>';
			m += '<input id=\"fileupload_' + i + '\" type=\"file\" name=\"file\" data-url=\"weixinArticleController.do?upload\" onClick=\"imageChange(' + i + ')"\>';
			m += '</span><p class="clearBoth">小图片建议尺寸：200像素 * 200像素;</p>  ';
			m += '<div id=\"progress_'+i+'\" class=\"progress\"><div class=\"progress-bar progress-bar-success\" style=\"width: 0%;\"></div></div>';
			m += '</div>';
			m += '<div id=\"files_'+i+'\" class=\"files\"></div></div>';
			m += '<label class="m10">图文内容</label>';
			m +='\<script id="content_'+i+'" type="text/plain" style="height: 200px; width: 100%;" name="content"\>'
			//m += 'UE.getEditor("content_"'+i+')';
			m += '\</script\>';
			m += '</div>';
			m += '</div>';
			m += '</li>';
			$('.right_list').append(m);
			//打开折叠
			Location('collapse_' + i);
			//图文消息富文本框绑定
			UE.getEditor('content_' + i);
		});
		
		
		if($("#title_0").val()==''){
			//初始化
			$('#addline').click();
		}
		//默认展开第一个
		Location('collapse_0');
		//保存
		$(".imgNewsSave").click(function() {

			var newsTemplate = {};
			var isSingleNews = true;
			var isEachEnd = true;
			//循环读取每一个item
			$("#accordionRight li").each(function(index, element) {

				var currentUE = UE.getEditor('content_' + index);

				var title = $(element).find("input[name=title]").val();
				var author = $(element).find("input[name=author]").val();
				var imagePath = $(element).find("input[name=imagePath]").val();
				var content = currentUE.getContent();//获取编辑器内容 //$(element).find("input[name=content]").val();
				var description = $(element).find("input[name=description]").val();

				if (title == undefined || title == '') {
					Location('collapse_' + index);
					$(element).find("input[name=title]").focus();
					return isEachEnd = false;
				}
				if (author == undefined || author == '') {
					Location('collapse_' + index);
					$(element).find("input[name=author]").focus();
					return isEachEnd = false;
				}
				if (imagePath == undefined || imagePath == '') {
					Location('collapse_' + index);
					$(element).find("input[name=imagePath]").focus();
					return isEachEnd = false;
				}
				if (!currentUE.hasContents()) {
					Location('collapse_' + index);
					currentUE.focus();
					return isEachEnd = false;
				}
				if (description == undefined || description == '') {
					Location('collapse_' + index);
					$(element).find("input[name=description]").focus();
					return isEachEnd = false;
				}

				newsTemplate["newsItemList[" + index + "].title"] = title;
				newsTemplate["newsItemList[" + index + "].author"] = author;
				newsTemplate["newsItemList[" + index + "].imagePath"] = imagePath;
				newsTemplate["newsItemList[" + index + "].content"] = content;
				newsTemplate["newsItemList[" + index + "].description"] = description;
				newsTemplate["newsItemList[" + index + "].orders"] = index;

				if (index == 0) {
					newsTemplate["templateName"] = title;
				}
				if (index > 1) {
					isSingleNews = false;
				}

			});
			//如果遍历完了则进行保存
			if (isEachEnd) {
				if (isSingleNews) {
					newsTemplate["type"] = "multipleNews";
				} else {
					newsTemplate["type"] = "singleNews";
				}
				//保存
				var url = "./newsTemplateController.do?doSave";
				//$.ajaxSettings.traditional = true; 
				$.ajax({
					url : url,// 请求的action路径
					type: 'POST',
					dataType : "json",
					data : newsTemplate,// {"newsTemplate":newsTemplate},
					success : function(data) {
						if (data.success) {
							setTimeout("location.href='newsTemplateController.do?newsList'", 100);
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
					<div class="panel-cont">
						<h4 class="title">添加多图文消息</h4>
						<form id="imgTxt">
							<div class="col-sm-12 row-fluid">
								<!--左侧开始-->
								<div class="col-sm-3">
									<div class="col-sm-12 thumbnail">
										<ol class="left_list">

											<c:if test="${newsTemplate!=null}">
												<c:forEach items="${newsTemplate.newsItemList}" var="newsItem" begin="0" end="0" varStatus="status">
													<li>
														<div class="yun_position_relative">
															<div class="yun_align_new">
																<img id="img_${status.index}" src="${newsItem.imagePath}">
															</div>
															<div class="yun_display_none">
																<div class="yun_position_absolute_${status.index}">
																	<a onclick="Location('collapse_${status.index}')">
																		<i class="fa fa-pencil"></i>
																	</a>
																	<a id="left_del_${status.index}" data-delurl="#" data-seq="${status.index}" onclick="delpage(this)">
																		<i class="fa fa-trash-o"></i>
																	</a>
																</div>
															</div>
														</div>
														<div class="yun_position_absolute">
															<div class="yun_color_fff">
																<span id="left_title_${status.index}" class="yun_img_text_title fonts">${newsItem.title}</span>
															</div>
														</div>
													</li>
												</c:forEach>
												<c:forEach items="${newsTemplate.newsItemList}" var="newsItem" begin="1" varStatus="status">
													<li id="left_${status.index}" class="yun_position_relative">
														<div class="row yun_border_bottom_e">
															<div class="col-sm-8 yun_overflow_hidden fonts" id="left_title_${status.index}">${newsItem.title }</div>
															<div class="col-sm-4">
																<img id="img_${status.index}" class="pull-right" src="${newsItem.imagePath }">
															</div>
														</div>
														<div class="yun_display_none" style="display: block; opacity: 0;">
															<div class="yun_position_absolute_${status.index}">
																<a onclick="Location('collapse_${status.index}')">
																	<i class="fa fa-pencil"></i>
																</a>
																&nbsp;
																<a id="left_del_${status.index}" data-delurl="#" data-seq="${status.index}" onclick="delpage(this)">
																	<i class="fa fa-trash-o"></i>
																</a>
															</div>
														</div>
													</li>
												</c:forEach>
											</c:if>
											<c:if test="${newsTemplate==null}">
												<li>
													<div class="yun_position_relative">
														<div class="yun_align_new">
															<img id="img_0" src="plug-in/kbrobot/images/thumb_index.png">
														</div>
														<div class="yun_display_none">
															<div class="yun_position_absolute_0">
																<a onclick="Location('collapse_0')">
																	<i class="fa fa-pencil"></i>
																</a>
																<a id="left_del_0" data-delurl="#" data-seq="0" onclick="delpage(this)">
																	<i class="fa fa-trash-o"></i>
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
											</c:if>

										</ol>
										<button type="button" class="btn btn-success btn-block" id="addline">
											<i class="fa fa-plus"></i> 增加一行
										</button>
									</div>
								</div>
								<!--左侧结束-->
								<!--右侧开始-->
								<div class="col-sm-9">
									<ol class="accordion panel-group right_list" id="accordionRight">
										<c:if test="${newsTemplate!=null}">
											<c:forEach items="${newsTemplate.newsItemList}" var="newsItem" varStatus="status">
												<li class="accordion-group mt10">
													<div class="panel-heading">
														<h4 class="panel-title">
															<a href="#collapse_${status.index}" data-parent="#accordionRight" data-toggle="collapse" class="collapsed" id="right_title_${status.index}" onclick="Location('collapse_${status.index}')"> ${newsItem.title} </a>
														</h4>
													</div>
													<div class="panel-collapse collapse in" id="collapse_${status.index}">
														<div class="panel-body">
															<label>标题</label> <input type="text" name="title" id="title_${status.index}" class="form-control" maxlength="64" value="${newsItem.title}" onChange="testChange(this,'left_title_${status.index}','right_title_${status.index}')"> <label class="m10">作者</label> <input type="text" name="author" id="author_${status.index}" class="form-control" maxlength="8" value="${newsItem.author }"> <label class="m10">描述</label> <input type="text" name="description" id="description_${status.index}" class="form-control" maxlength="64" value="${newsItem.description}">
															<div id="imgFile_${status.index}" style="margin-top: 10px; position: relative;">
																<span class="btn btn-success fileinput-button">
																	<i class="fa fa-plus"></i>
																	<span>请选择封面图片</span>
																	<input id="fileupload_${status.index}" type="file" name="file" value="" onClick="imageChange(${status.index})" data-url="weixinArticleController.do?upload">
																</span>
																<p class="clearBoth">大图片建议尺寸：900像素 * 500像素;</p>
																<div id="progress_${status.index}" class="progress">
																	<div class="progress-bar progress-bar-success" style="width: 0%;"></div>
																</div>
																<div id="files_${status.index}" class="files"></div>
																<input type="hidden" value="${newsItem.imagePath}" name="imagePath">
															</div>
															<label class="m10">图文内容</label>
															<script id="content_${status.index}" type="text/plain" style="height: 200px; width: 100%;" name="content">
																${newsItem.content}
															</script>
														</div>
													</div>
												</li>
											</c:forEach>
										</c:if>
										<c:if test="${newsTemplate==null}">
											<li class="accordion-group mt10">
												<div class="panel-heading">
													<h4 class="panel-title">
														<a href="#collapse_0" data-parent="#accordionRight" data-toggle="collapse" class="collapsed" id="right_title_0" onclick="Location('collapse_0')">主标题</a>
													</h4>
												</div>
												<div class="panel-collapse collapse in" id="collapse_0">
													<div class="panel-body">
														<label>标题</label> <input type="text" name="title" id="title_0" class="form-control" maxlength="64" onChange="testChange(this,'left_title_0','right_title_0')"> <label class="m10">作者</label> <input type="text" name="author" id="author_0" class="form-control" maxlength="8"> <label class="m10">描述</label> <input type="text" name="description" id="description_0" class="form-control" maxlength="64">
														<div id="imgFile_0" style="margin-top: 10px; position: relative;">
															<span class="btn btn-success fileinput-button">
																<i class="fa fa-plus"></i>
																<span>请选择封面图片</span>
																<input id="fileupload_0" type="file" name="file" onClick="imageChange(0)" data-url="weixinArticleController.do?upload">
															</span>
															<p class="clearBoth">大图片建议尺寸：900像素 * 500像素;</p>
															<div id="progress_0" class="progress">
																<div class="progress-bar progress-bar-success" style="width: 0%;"></div>
															</div>
															<div id="files_0" class="files"></div>
														</div>
														<label class="m10">图文内容</label>
														<script id="content_0" type="text/plain" style="height: 200px; width: 100%;" name="content"></script>
													</div>
												</div>
											</li>
										</c:if>

									</ol>
									<!--右侧结束-->
									<div class="row">
										<div class="col-lg-5 choose-btn">
											<a class="imgNewsSave">保存</a>
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
