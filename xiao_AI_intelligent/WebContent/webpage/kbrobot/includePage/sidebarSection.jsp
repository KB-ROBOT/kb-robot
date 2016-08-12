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
				<a href="home.html">
					<i class="fa fa-home"></i>
					<span>首页</span>
				</a>
			</li>
			<li>
				<a href="set.html">
					<i class="fa fa-cog"></i>
					<span>基本设定</span>
				</a>
			</li>
			<Li>
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
			</Li>
			<Li>
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
						<a href="message-voice.html">语音消息</a>
					</li>
				</ul>
			</Li>
			<Li>
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
						<a href="add-question.html">添加问题</a>
					</li>
				</ul>
			</Li>
			<li>
				<a href="work-order.html">
					<i class="fa fa-calendar"></i>
					<span>工单</span>
				</a>
			</li>
			<li>
				<a href="javascript:;" class="submenu">
					<i class="fa fa-cloud"></i>
					<span>数据分析</span>
					<font class="fa fa-angle-right"></font>
				</a>
				<ul>
					<li>
						<a href="visit-data.html">访客次数统计</a>
					</li>
					<li>
						<a href="knowledge-count.html">知识库统计</a>
					</li>
					<li>
						<a href="question-count.html">问题明细统计</a>
					</li>
					<li>
						<a href="knowledge-radar.html">知识雷达</a>
					</li>
					<li>
						<a href="visit-log.html">访客日志</a>
					</li>
				</ul>
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