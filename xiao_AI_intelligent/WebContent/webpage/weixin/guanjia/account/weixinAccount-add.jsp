<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>微信公众帐号信息</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
<script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
<script type="text/javascript">
	//编写自定义JS代码
</script>
</head>
<body>
	<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="weixinAccountController.do?doAdd" tiptype="1">
		<input id="id" name="id" type="hidden" value="${weixinAccountPage.id }">
		<input id="addTokenTime" name="addTokenTime" type="hidden" />
		<input id="accountaccesstoken" name="accountaccesstoken" type="hidden" />
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td align="right">
					<label class="Validform_label"> 公众帐号名称: </label>
				</td>
				<td class="value">
					<input id="accountName" name="accountName" type="text" style="width: 150px" class="inputxt" datatype="*">
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">公众帐号名称</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label"> 公众帐号TOKEN: </label>
				</td>
				<td class="value">
					<input id="accountToken" name="accountToken" type="text" style="width: 150px" class="inputxt" datatype="*">
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">公众帐号TOKEN</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label"> 公众微信号: </label>
				</td>
				<td class="value">
					<input id="accountNumber" name="accountNumber" type="text" style="width: 150px" class="inputxt">
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">公众微信号</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label"> 原始ID: </label>
				</td>
				<td class="value">
					<input id="weixinAccountId" name="weixinAccountId" type="text" style="width: 150px" class="inputxt" datatype="*">
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">原始ID</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label"> 公众号类型: </label>
				</td>
				<td class="value">
					<t:dictSelect field="accountType" typeGroupCode="weixintype" hasLabel="false" defaultVal=""></t:dictSelect>
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">公众号类型</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label"> 电子邮箱: </label>
				</td>
				<td class="value">
					<input id="accountEmail" name="accountEmail" type="text" style="width: 150px" class="inputxt">
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">电子邮箱</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label"> 公众帐号描述: </label>
				</td>
				<td class="value">
					<input id="accountDesc" name="accountDesc" type="text" style="width: 150px" class="inputxt">
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">公众帐号描述</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label"> 公众帐号APPID: </label>
				</td>
				<td class="value">
					<input id="accountAppid" name="accountAppid" type="text" style="width: 150px" class="inputxt" datatype="*">
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">公众帐号APPID</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label"> 公众帐号APPSECRET: </label>
				</td>
				<td class="value">
					<input id="accountAppsecret" name="accountAppsecret" type="text" style="width: 150px" class="inputxt" datatype="*">
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">公众帐号APPSECRET</label>
				</td>
			</tr>
		</table>
	</t:formvalid>
</body>
<script src="webpage/weixin/guanjia/account/weixinAccount.js"></script>