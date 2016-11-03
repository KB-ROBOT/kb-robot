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
    <div class="content">
    	<h5 class="title">留言</h5>
    	<form class="comment">
        	<div><label><font>*</font>姓名：</label><input name="name" type="text" maxlength="16"></div>
            <div><label><font>*</font>手机号码：</label><input name="tel" type="text" maxlength="20"></div>
            <div><label><font>*</font>留言：</label><textarea name="comm" maxlength="225"></textarea></div>
            <a href="javascript:checkForm();" class="submit">提交</a>
        </form>
        
    </div>
</div>
<script>
	
	function checkForm(){
		var name = $('input[name=name]').val();
		var tel = $('input[name=tel]').val();
		var comm = $('textarea[name=comm]').val();
		if(name === undefined || name == ''){
			swal("提交失败", "请填写姓名！", "error");
			return;
		}
		if(tel === undefined || tel == ''){
			swal("提交失败", "请填写手机号码！", "error");
			return;
		}
		if(comm === undefined || comm == ''){
			swal("提交失败", "请填写留言内容！", "error");
			return;
		}
		
		swal("提交成功", "感谢您的留言，我们会及时和您联系并按规定处理！", "success");
	}


</script>

</body>
</html>
