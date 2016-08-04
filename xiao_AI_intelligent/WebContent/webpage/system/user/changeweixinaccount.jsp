<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>切换微信账号</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
	<t:formvalid formid="formobj" refresh="false" dialog="true" action="userController.do?saveweixinaccount" layout="table">
		<table style="width: 550px" cellpadding="0" cellspacing="1" class="formtable">
			<tbody>
				<c:forEach items="${weixinAccountList}" var="weixinAccount" varStatus="status">
					<a href="javascript:void(0);">
						<tr>
							<td class="value">
								<input type="radio" value="${weixinAccount.id}" name="accountId" />
								<span>${weixinAccount.accountName}</span>
							</td>
						</tr>
					</a>
				</c:forEach>
			</tbody>
		</table>
	</t:formvalid>
</body>
<script type="text/javascript">
	//选中当前风格
	$(function() {
		var val = "${currentWeixinAccount.id}";
		$("input[name='account']").each(function() {
			if ($(this).val() == val) {
				$(this).attr("checked", true);
				return false;
			}
		});
	});
</script>
</html>
