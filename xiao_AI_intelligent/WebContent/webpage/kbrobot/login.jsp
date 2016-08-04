<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>凯博机器人后台管理系统</title>
<jsp:include page="./includePage/linkSource.jsp"></jsp:include>
<!-- 验证码 -->
<script src="http://static.geetest.com/static/tools/gt.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		
		//设置cookie
		function setCookie(){
			$(":input").each(function() {
				 $.cookie(this.name, $("#"+this.name).val(), "/",24);
				 $.cookie("COOKIE_NAME","true", "/",24);
			});
		}
		//UM测试
		//UM.getEditor('content_1');
		
		var captchaIsOk = false;

		//获得验证码 
		var handler = function(captchaObj) {
			// 将验证码加到id为captcha的元素里
			captchaObj.appendTo("#captcha-box");
			$("#userName,#password").focus(function() {
				captchaObj.refresh();
				$("#loginMessage").text("");
				captchaIsOk = false;
			});
			//点击登陆事件
			$(".btn-login").click(function() {
				var validate = captchaObj.getValidate();
				if(validate){
					var actionurl=$('form').attr('action');//提交路径
					var checkurl=$('form').attr('check');//验证路径
					var formData = new Object();
					var data=$(":input").each(function() {
						 formData[this.name] =$("#"+this.name ).val();
					});
					
					formData["geetest_challenge"] = validate.geetest_challenge;
					formData["geetest_validate"] = validate.geetest_validate;
					formData["geetest_seccode"] = validate.geetest_seccode;
					$.ajax({
						async : false,
						cache : false,
						type : 'POST',
						url : checkurl,// 请求的action路径
						data : formData,
						error : function() {// 请求失败处理函数
							
						},
						success : function(data) {
							var d = $.parseJSON(data);
							if (d.success) {
								setCookie();
								setTimeout("window.location.href='"+actionurl+"'", 1000);
							} else {
								$("#loginMessage").append(d.msg);
							}
						}
					});
				}
				else{
					$("#loginMessage").append("请完成验证码验证");
				}
			});
			
			//验证码成功验证事件
			captchaObj.onSuccess(function() {
				$("#loginMessage").text("");
				$("#loginForm").find(function() {
					var userName = $("#userName").val();
					var pwd = $("#password").val();

					if (userName != '' && pwd != '') {
						//$("#loginForm").submit();
						captchaIsOk = true;
					} else {
						if (userName == '') {
							$("#loginMessage").append("*登录名不能为空<br/>");
						}
						if (pwd == '') {
							$("#loginMessage").append("*密码不能为空");
						}
					}
				});
			});
		};

		//验证码初始化
		var actionUrl = "./loginController.do?initRandomCode";
		$.ajax({
			// 获取id，challenge，success（是否启用failback）
			url : actionUrl,
			type : "post",
			dataType : "json", // 使用json格式
			success : function(data) {

				if (data.success) {
					//initResult 初始化返回字符串
					var dataJson = $.parseJSON(data.attributes.initResult);
					// 使用initGeetest接口
					// 参数1：配置参数，与创建Geetest实例时接受的参数一致
					// 参数2：回调，回调的第一个参数验证码对象，之后可以使用它做appendTo之类的事件
					initGeetest({
						gt : dataJson.gt,
						challenge : dataJson.challenge,
						product : "float", // 验证码形式
						offline : !dataJson.success
					}, handler);

				} else {
					/*  $.dialog.confirm("数据库无数据,是否初始化数据?", function(){
							window.location = "init.jsp";
						}, function(){
							//
						});  */
				}
			},
			error : function(data) {
				alert("获取验证码失败！");
			}
		});
		
	});
</script>
</head>

<body class="login">
	<div class="login-main">
		<div class="col-lg-2">
			<a href="http://www.kb-robot.com">
				<img src="./plug-in/kbrobot/images/logo.png">
			</a>
		</div>
		<div class="login-form">
			<form id="loginForm" action="loginController.do?login" check="loginController.do?checkuser" method="post">
				<div>
					<input class="userName" name="userName" type="text" id="userName" title="用户名" placeholder="用户名" iscookie="true" nullmsg="用户名" />
				</div>
				<div>
					<input class="password" name="password" type="password" id="password" title="密码" placeholder="密码" nullmsg="密码" />
					<a href="javascript:void(0);">
						<img src="./plug-in/kbrobot/images/btn-login.png" class="btn-login">
					</a>
				</div>
				<label id="loginMessage" class="c-label label_RFnkmh" style="color: red;"></label>

				<div id="captcha-box"></div>
				<p>
					还没有账号，
					<a href="./loginController.do?goRegister">立即注册</a>
				</p>

			</form>
		</div>
	</div>
</body>
</html>
