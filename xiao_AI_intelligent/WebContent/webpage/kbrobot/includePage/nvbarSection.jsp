
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- 弹出框样式 -->
<style type="text/css">
	.choose-box{}
	.choose-box li{float:left;padding:1px;margin:0 4px;line-height:28px;}
	.choose-box li a{display:inline-block;background-color: #fff;white-space: nowrap;width: auto!important;min-width: 10px;padding: 0 9px;border: 1px solid #b8b7bd;color: #000;text-decoration: none;}
	.choose-box li:hover a,.choose-box li.on a{border:2px solid #FF9500;margin:-1px;}
</style>

<script type="text/javascript">

	function weixinChange() {
		
		var d = dialog({
			title : '请选择已经绑定的公众号',
			lock : true,
			background : '#000', // 背景色
			ok : function() {
				//绑定微信公众号
				
				var accountId = $(".on a").attr("accountId");
				if(accountId!=null&&accountId!=''&&accountId!=undefined){
					//保存设置
					saveChange(accountId);
				}
			}
		});
		
		d.showModal();
		//请求url
		var actionUrl = "./userController.do?changewexinaccount";
		$.ajax({
			// 获取id，challenge，success（是否启用failback）
			url : actionUrl,
			type : "post",
			dataType : "json", // 使用json格式
			success : function(data) {

				if (data.success) {
					//msg保存的是验证码信息
					var weixinList = data.attributes.weixinAccountList;
					var dialogContent = '<div class="col-lg-12">' +
                							'<ul class="choose-box">';

					if (weixinList.length == 0) {
						dialogContent = "你还没有绑定微信公众号."
					}
					else{
						for (var i = 0; i < weixinList.length; i++) {
							dialogContent += '<li><a href="javascript:void(0);" accountId="'+weixinList[i].id+'">'+weixinList[i].accountName+'</a></li>';
						}
						dialogContent +=	'</ul>'+
			                		'</div>';
					}
					d.content(dialogContent);
					//绑定click事件
					$(".choose-box li").click(function(){
						$('.choose-box li').removeClass('on');
						$(this).addClass('on');	
					});
				}
				else {
					
				}
			},
			error : function(data) {
				
			}
		});
	}
	
	function saveChange(weixinId){
		var saveChangeUrl = "userController.do?saveweixinaccount";
		$.ajax({
			// 获取id，challenge，success（是否启用failback）
			url : saveChangeUrl,
			type : "post",
			data:{"accountId":weixinId},
			dataType : "json", // 使用json格式
			success : function(data) {
				var d = dialog({
					ok : function() {
						location.reload();
					}
				});
				if (data.success) {
					d.content("切换成功");
					d.show();
				}
				else {
					d.content("切换失败，请稍后再试");
					d.show();
					setTimeout(d.remove(),1000);
				}
			},
			error : function(data) {
				dialog({
					content:"出错了，请稍后再试",
					ok : function() {
						location.reload();
					}
				}).show();
			}
		});
	}
</script>
<div class="navbar user-info-navbar">
	<h1 class="fl"></h1>
	<ul class="fr user-info-menu">
		<li>
			<a href="">
				<i style="background: url(plug-in/kbrobot/images/robot.png) center center no-repeat;"></i>我的机器人
			</a>
		</li>
		<li>
			<a href="">
				<i style="background: url(plug-in/kbrobot/images/qq.png) center center no-repeat;"></i>联系客服
			</a>
		</li>
		<li>
			<a href="">
				<i style="background: url(plug-in/kbrobot/images/wx.png) center center no-repeat;"></i>当前公众号：${sessionScope.WEIXIN_ACCOUNT.accountName}
			</a>
		</li>
		<li class="dropdown">
			<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown">
				<i style="background: url(plug-in/kbrobot/images/user.png) center center no-repeat;"></i>${sessionScope.sessionLoginUser.get(0).TSUser.userName }(${sessionScope.sessionLoginUser.get(0).TSRole.roleName})
				<span class="caret"></span>

			</a>
			<ul class="dropdown-menu" role="menu">
				<li>
					<a href="">
						<i class="fa fa-key"></i>修改密码
					</a>
				</li>
				<li>
					<a href="">
						<i class="fa fa-edit"></i>账户信息
					</a>
				</li>
				<li>
					<a href="javascript:weixinChange();">
						<i class="fa fa-exchange"></i>切换公众号
					</a>
				</li>
				<li>
					<a href="./loginController.do?logout">
						<i class="fa fa-power-off"></i>退出
					</a>
				</li>
			</ul>
		</li>
	</ul>
</div>