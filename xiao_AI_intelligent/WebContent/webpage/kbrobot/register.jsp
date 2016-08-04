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
		
		var ok1 = false;
		var ok2 = false;
		var ok3 = false;
		var ok4 = false;
		// 验证用户名
		$('input[name="userName"]').focus(function() {
			$(this).next().text('用户名应该为4-12位之间').removeClass('state1').addClass('state2');
		}).blur(function() {
			if ($(this).val().length >= 4 && $(this).val().length <= 12 && $(this).val() != '') {
				$(this).next().text('输入成功').removeClass('state1').addClass('state4');
				ok1 = true;
			} else {
				$(this).next().text('用户名应该为3-6位之间').removeClass('state1').addClass('state3');
			}
		});

		//验证密码
		$('input[name="password"]').focus(function() {
			$(this).next().text('密码应该为6-20位之间').removeClass('state1').addClass('state2');
		}).blur(function() {
			if ($(this).val().length >= 6 && $(this).val().length <= 20 && $(this).val() != '') {
				$(this).next().text('输入成功').removeClass('state1').addClass('state4');
				ok2 = true;
			} else {
				$(this).next().text('密码应该为6-20位之间').removeClass('state1').addClass('state3');
			}

		});

		//验证确认密码
		$('input[name="repass"]').focus(function() {
			$(this).next().text('确认密码与密码一致').removeClass('state1').addClass('state2');
		}).blur(function() {
			if ($(this).val().length >= 6 && $(this).val().length <= 20 && $(this).val() != '' && $(this).val() == $('input[name="password"]').val()) {
				$(this).next().text('输入成功').removeClass('state1').addClass('state4');
				ok3 = true;
			} else {
				$(this).next().text('确认密码与密码不一致').removeClass('state1').addClass('state3');
			}
		});

		//验证邮箱
		$('input[name="email"]').focus(function() {
			$(this).next().text('请输入正确的EMAIL格式').removeClass('state1').addClass('state2');
		}).blur(function() {
			if ($(this).val().search(/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/) == -1) {
				$(this).next().text('请输入正确的EMAIL格式').removeClass('state1').addClass('state3');
			} else {
				$(this).next().text('输入成功').removeClass('state1').addClass('state4');
				ok4 = true;
			}
		});

		//提交按钮,所有验证通过方可提交
		
		$(".reg-btn").click(function(){
			if (ok1 && ok2 && ok3 && ok4){
				var actionurl=$('form').attr('action');//提交路径
				var formData = new Object();
				var data=$(":input[type='text'],:input[type='password']").each(function() {
					 formData[this.name] =$("#"+this.name ).val();
				});
				
				$.ajax({
					type : 'POST',
					url : actionurl,// 请求的action路径
					data : formData,
					dataType : "json", // 使用json格式
					error : function() {// 请求失败处理函数
						alert("请求失败！");
					},
					success : function(data) {
						if (data.success) {
							setTimeout("window.location.href='.'", 1000);
						} else {
							$("#loginMessage").append(data.msg);
						}
					}
				});
			}
			else{
				$("#loginMessage").text("请填写相关信息");
			}
		});
	});
</script>
</head>

<body class="login">
	<div class="login-main">
		<div class="col-lg-2">
			<a href="">
				<img src="./plug-in/kbrobot/images/logo.png">
			</a>
		</div>
		<div class="login-form">
			<form action="./loginController.do?register" method="post">
				<div class="post-reg">
					<input placeholder="用户名" name="userName" type="text" id="userName" maxlength="12">
					<span class='state1'>请输入用户名</span>
				</div>

				<div class="post-reg">
					<input type="password" placeholder="输入密码" name="password" id="password" maxlength="20">
					<span class='state1'>请输入密码</span>
				</div>
				<div class="post-reg">
					<input type="password" placeholder="确认密码" name="repass" maxlength="20">
					<span class='state1'>请输入确认密码</span>
				</div>
				<div class="post-reg">
					<input type="text" placeholder="邮箱" name="email" id="email">
					<span class='state1'>请输入邮箱</span>
				</div>
				<label id="loginMessage" class="c-label label_RFnkmh" style="color: red;"></label>
				<p>
					<input type="checkbox"  >同意网站
					<a href="">协议</a>
					内容
				</p>
				<a href="javascript:void(0);" class="reg-btn">确定</a>
				<p>
					已有账号，
					<a href=".">立即登录</a>
				</p>
			</form>
		</div>
	</div>

	<script>
		
	</script>
</body>
</html>
