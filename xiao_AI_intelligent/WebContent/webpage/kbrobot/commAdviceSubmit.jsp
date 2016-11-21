<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp" />

<title>提交留言</title>
<link rel="stylesheet" type="text/css" href="plug-in/kbrobot/css/sweet-alert.css">
<link rel="stylesheet" type="text/css" href="plug-in/kbrobot/css/mobile.css">
<script type="text/javascript" src="plug-in/kbrobot/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="plug-in/kbrobot/js/sweet-alert.min.js"></script>
</head>

<body>
	<div class="wrap">
		<h1 class="title"><span>问题建议</span></h1>
		<!-- <div class="content">
			<h5 class="title">问题建议</h5>
			<div class="comment">
				<div>
					<label><font>*</font>姓名：</label><input name="name" type="text" maxlength="16">
				</div>
				<div>
					<label><font>*</font>手机号码：</label><input name="mobile" type="text" maxlength="20">
				</div>
				<div>
					<label><font>*</font>留言：</label>
					<textarea name="content" maxlength="225"></textarea>
				</div>
				<a href="javascript:checkForm();" class="submit">提交</a>
			</div>
		</div> -->
		<div class="content">
			<form class="leave">
				<p class="user">
					<input type="text" placeholder="请输入您的姓名" name="name">
				</p>
				<p class="phone">
					<input type="text" placeholder="请输入联系电话" name="mobile">
				</p>
				<div>
					<textarea placeholder="请输入您的问题或建议" name="content"></textarea>
				</div>
				<h6 class="tip">请详细说明您的问题与建议，便于我们及时准确处理与回复。</h6>
				<a href="javascript:checkForm();" class="submit">提交</a>
			</form>
		</div>
	</div>
	<script>
		function GetQueryString(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if (r != null)
				return unescape(r[2]);
			return null;
		}

		function checkForm() {
			var name = $('input[name=name]').val();
			var mobile = $('input[name=mobile]').val();
			var content = $('textarea[name=content]').val();
			if (name === undefined || name == '') {
				swal("提交失败", "请填写姓名！", "error");
				return;
			}
			if (mobile === undefined || mobile == '') {
				swal("提交失败", "请填写手机号码！", "error");
				return;
			}
			if (content === undefined || content == '') {
				swal("提交失败", "请填写留言内容！", "error");
				return;
			}

			var accountId = GetQueryString('accountId');
			
			accountId = accountId==null?'':accountId;

			var url = "./commonAdviceController.do?addLeaveMessage&accountId=" + accountId;
			$.ajax({
				url : url,// 请求的action路径
				type : 'post',
				dataType : "json",
				data : {
					"name" : name,
					"mobile" : mobile,
					"content" : content
				},
				success : function(data) {
					if (data.success) {
						swal("提交成功", "感谢您的留言，我们会及时和您联系并按规定处理！", "success");
						$('input[name=name]').val("");
						$('input[name=mobile]').val("");
						$('textarea[name=content]').val("");
						setTimeout("location.reload()", 500);
					} else {
						swal("提交失败", data.msg, "error");
					}
				},
				error : function() {// 请求失败处理函数

				},
			});

		}
	</script>

</body>
</html>
