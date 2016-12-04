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
        	<div class="tabs col-sm-6">
            	<div class="tabs-nav">
                	<ul class="clearfix">
                        <li data="cont1" class="curr">机器人设定</li>
                        <li data="cont2">企业基本信息设定</li>
                        <li data="cont3">欢迎词</li>
                        <li data="cont4">未知说辞</li>
					</ul>
                </div>
                <div class="conts">
                	<div class="cont" id="cont1" style="position: relative; display: block;">
                    	<form>
                        	<div class="form-group">
                                <label class="control-label">机器人名称</label>
                                <input type="text" class="form-control" name="robotName">
                            </div>
                        </form>
                    </div>
                    <div class="cont" id="cont2" style="display:none;">
                    	<form>
                        	<div class="form-group">
                                <label class="control-label">企业名称</label>
                                <input type="text" class="form-control" name="robotName">
                            </div>
                            <div class="form-group">
                                <label class="control-label">企业网址</label>
                                <input type="text" class="form-control" name="robotName">
                            </div>
                        </form>
                    </div>
                    <div class="cont" id="cont3" style="display:none;">
                    	<form>
                        	<div class="form-group">
                                <label class="control-label">机器人欢迎词</label>
                                <input type="text" class="form-control" name="robotName">
                            </div>
                        </form>
                    </div>
                    <div class="cont" id="cont4" style="display:none;">
                    	<form>
                        	<div class="form-group">
                                <label class="control-label">未知说辞</label>
                                <input type="text" class="form-control" name="robotName">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
</div>
</body>
</html>
