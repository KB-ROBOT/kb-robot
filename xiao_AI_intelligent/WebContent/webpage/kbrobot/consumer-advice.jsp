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

				<div class="panel col-sm-12">
					<div class="panel-cont">
						<h4 class="title">问题建议</h4>
						<div class="row">
							<div class="col-lg-12">
								<table class="table table-model-2 table-hover">
                            	<tr>
                                	<th>序号</th>
                                    <th>姓名</th>
                                    <th>联系方式</th>
                                    <th>操作</th>
                                </tr>
                               <!--<tr>
                                	<td colspan="4" align="center">当前记录为空</td>
                                </tr>-->

                                <tr>
                                	<td>1</td>
                                    <td>李四</td>
                                    <td>11111111111</td>
                                    <td><button data-toggle="modal" data-target="#advice_modal">查看</button><a href="" class="select">回复</a></td>
                                </tr>
                                <tr>
                                	<td>2</td>
                                    <td>张三</td>
                                    <td>11111111111</td>
                                    <td><button data-toggle="modal" data-target="#advice_modal">查看</button><a href="" class="uncheck">回复</a></td>
                                </tr>
                            </table>
							</div>
						</div>
						<!-- 页码 -->
						<div class="row">${questionPageList.pager.getToolsBarByUrl()}</div>
						<!-- 页码 -->
					</div>
				</div>
			</div>
		</div>

	</div>

<!--留言内容-->
<div id="advice_modal" class="modal fade">
	<div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" 
               data-dismiss="modal" aria-hidden="true">
                  &times;
            </button>
            <h4 class="modal-title" id="myModalLabel">
               留言内容
            </h4>
         </div>
         <div class="modal-body">
            <p>留言内容内容内容内容</p>
         </div>
         <div class="modal-footer">
         	<button type="button" class="btn btn-primary">
               确定
            </button>
            <button type="button" class="btn btn-default" 
               data-dismiss="modal">取消
            </button>
            
         </div>
      </div></div>
</div>
<!--留言内容结束-->
</body>
</html>
