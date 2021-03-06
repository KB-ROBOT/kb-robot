<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="sidebar-menu toggle-others fixed">
	<div class="sidebar-menu-inner">
		<div class="header">
			<a href="">
				<img src="./plug-in/kbrobot/images/logo.png">
			</a>
		</div>
		<ul id="main-menu" class="main-menu">
			<li class="active">
				<a href="loginController.do?login">
					<i class="fa fa-home"></i>
					<span>首页</span>
				</a>
			</li>
			<li>
				<a href="./robotSetController.do?goRobotSet">
					<i class="fa fa-cog"></i>
					<span>基本设定</span>
				</a>
			</li>
			<li>
				<a href="javascript:;" class="submenu">
					<i class="fa fa-link"></i>
					<span>机器人接入</span>
					<font class="fa fa-angle-right"></font>
				</a>
				<ul>
					<li>
						<a href="./robotBindController.do?goweixinbind">微信接入</a>
					</li>
					<li>
						<a href="./robotBindController.do?gowebbind">网页接入</a>
					</li>
				</ul>
			</li>
			<li>
				<a href="javascript:;" class="submenu">
					<i class="fa fa-tasks"></i>
					<span>素材管理</span>
					<font class="fa fa-angle-right"></font>
				</a>
				<ul>
					<li>
						<a href="textTemplateController.do?textList">文本消息</a>
					</li>
					<li>
						<a href="newsTemplateController.do?newsList">图文消息</a>
					</li>
					<li>
						<a href="voiceTemplateController.do?voiceList">语音消息</a>
					</li>
				</ul>
			</li>
			<li>
				<a href="javascript:;" class="submenu">
					<i class="fa fa-book"></i>
					<span>知识库管理</span>
					<font class="fa fa-angle-right"></font>
				</a>
				<ul>
					<li>
						<a href="robotQuestionController.do?questionList">问答总览</a>
					</li>
					<li>
						<a href="robotQuestionController.do?goQuestionAdd">添加问题</a>
					</li>
					<li>
						<a href="robotQuestionController.do?goQuestionContextAdd">添加多轮问题</a>
					</li>
				</ul>
			</li>
			<!-- <li>
				<a href="work-order.html">
					<i class="fa fa-calendar"></i>
					<span>工单</span>
				</a>
			</li> -->
			<li>
				<a href="javascript:;" class="submenu">
					<i class="fa fa-cloud"></i>
					<span>数据分析</span>
					<font class="fa fa-angle-right"></font>
				</a>
				<ul>
					<li>
						<a href="dataReportController.do?report-visit-data">访客统计</a>
					</li>
					<li>
						<a href="dataReportController.do?report-question-data">问题热度统计</a>
					</li>
					<li>
						<a href="dataReportController.do?report-match-data">匹配率统计</a>
					</li>
					<%-- <li>
						<a href="knowledge-radar.html">知识雷达</a>
					</li> --%>

				</ul>
			</li>
            <li>
				<a href="javascript:;" class="submenu">
					<i class="fa fa-group"></i>
					<span>人工客服</span>
					<font class="fa fa-angle-right"></font>
				</a>
				<ul>
					<li>
						<a href="customServiceController.do?sustomServiceList">微信客服</a>
					</li>
					<li>
						<a href="commonCallServiceController.do?callServiceList">电话客服</a>
					</li>
					<li>
						<a href="commonCallServiceController.do?goCallServiceCountPage">客服统计</a>
					</li>
				</ul>
			</li>
            
			<li>
				<a href="javascript:;" class="submenu">
					<i class="fa fa-comment-o"></i>
					<span>问题与建议</span>
					<font class="fa fa-angle-right"></font>
				</a>
				<ul>
					<li>
						<a href="commonAdviceController.do?leaveMessageList">留言板</a>
					</li>
					<li>
						<a href="commonReportController.do?reportMessageList">投诉举报</a>
					</li>
				</ul>
			</li>
			<li>
				<a href="javascript:;" class="submenu">
					<i class="fa fa-file"></i>
					<span>文件管理</span>
					<font class="fa fa-angle-right"></font>
				</a>
				<ul>
					<li>
						<a href="commonFileDownloadController.do?powerPointManageList">课件管理</a>
					</li>
					<li>
						<a href="commonFileDownloadController.do?excelManageList">表格管理</a>
					</li>
				</ul>
			</li>
			<li>
				<a href="roleManageController.do?roleManage">
					<i class="fa fa-group"></i>
					<span>权限管理</span>
				</a>
			</li>
			<li>
				<a href="dataReportController.do?report-visit-log">
					<i class="fa fa-list-alt"></i>
					<span>系统日志</span>
				</a>
			</li>
            <li>
				<a href="./commonBespeakController.do?bespeakList">
					<i class="fa fa-bell-o"></i>
					<span>预约办税</span>
				</a>
			</li>
			<li>
				<a href="help.html">
					<i class="fa fa-wrench"></i>
					<span>帮助中心</span>
				</a>
			</li>
		</ul>
	</div>
</div>