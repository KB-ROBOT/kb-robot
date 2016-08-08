<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>凯博机器人后台管理系统</title>
<jsp:include page="./includePage/linkSource.jsp"></jsp:include>
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
        	<div class="panel col-sm-6">
            	<div class="panel-cont">
                	<h4 class="title">文本消息列表</h4>
                    <table class="table table-model-2">
                    	<tr>
                        	<th>标题</th>
                            <th>作者</th>
                            <th>摘要</th>
                            <th>操作</th>
                        </tr>
                        <tr>
                        	<td>1111</td>
                            <td>auth</td>
                            <td width="50%">111111</td>
                            <td width="10%">
                            	<a href="javascript:;" class="fa fa-pencil" title="编辑" style="margin-right:5px;"></a>
                                <a href="javascript:;" class="fa fa-trash-o" title="编辑"></a>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
    
</div>

<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/bootstrap.js"></script>
<script type="text/ecmascript" src="js/jquery-ui.js"></script>
<script type="text/ecmascript" src="js/jquery-ui.min.js"></script>
<script type="text/ecmascript" src="js/bootstrap-datetimepicker.js"></script>
<script type="text/ecmascript" src="js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/ecmascript" src="js/style.js"></script>
</body>
</html>
