<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp" />

<title>纳税顾问</title>
<link rel="stylesheet" type="text/css" href="plug-in/kbrobot/css/sweet-alert.css">
<link rel="stylesheet" type="text/css" href="plug-in/kbrobot/css/mobile.css">
<script type="text/javascript" src="plug-in/kbrobot/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="plug-in/kbrobot/js/sweet-alert.min.js"></script>

<script type="text/javascript">

	$(document).ready(function(){
		
		$(".mobile,.phone").click(function(){
			//alert($(this).attr("href"));
			//console.log($(this).data("id"));
			
			var id = $(this).data("id");
			var customerCallService = {};
			customerCallService.id = id;
			
			var url = "./commonCallServiceController.do?clickTimesCount";
			$.ajax({
				url : url,// 请求的action路径
				dataType : "json",
				jsonpCallback:"callback",
				data : customerCallService,
				success : function(data) {
					
					if(data.success){
						///location.reload();
					}
					else{
						alert("操作失败，请稍后重试");
					}
				},
				error:function(XMLHttpRequest, textStatus, errorThrown){
		            console.log(textStatus);
		        }
			});
		});
		
	});
	
	
</script>

</head>

<body>
	<div class="wrap">
		<h1 class="title">
			<!-- <img src="images/title.png"> -->
			纳税顾问
		</h1>
		<p class="preface">为广大纳税人成立了专家顾问咨询团队，纳税人有相关纳税业务不熟悉的，需要咨询、需要排忧解难的，可以根据业务类别细分找的纳税顾问，进行纳税业务上的咨询。</p>
		<div class="content">
			<div class="inforBox">
				<p class="issueTit">税收征管类专家顾问团队</p>
				<ul>
					<li>
						<a class="inactive inforNav">
							发票类<i></i>
						</a>
						<ul class="inforDiv">
							<c:forEach items="${customerCallServiceList}" var="customerCallService" varStatus="status">
								<c:if test="${ customerCallService.group == 1 }">
									<c:if test="${customerCallService.serviceType == 1 }">
										<li>
											<span class="auth">
												<c:if test="${customerCallService.headImg == 1}">
													<img src="images/man.jpg">
												</c:if>
												<c:if test="${customerCallService.headImg == 2}">
													<img src="images/women.jpg">
												</c:if>
											</span>
											<div class="tel">
												<h2>${ customerCallService.name }</h2>
												<p>
													<img src="images/call.png">
													<a class="mobile" data-id="${ customerCallService.id }" href="tel:${ customerCallService.mobile }">${ customerCallService.mobile }</a>
												</p>
												<p>
													<img src="images/phone.png">
													<a class="phone" data-id="${ customerCallService.id }"  href="tel:${ customerCallService.phone }">${ customerCallService.phone }</a>
												</p>
											</div>
										</li>
									</c:if>
								</c:if>
							</c:forEach>
						</ul>
					</li>

					<li>
						<a class="inactive inforNav">
							纳税申报<i></i>
						</a>
						<ul class="inforDiv">
							<c:forEach items="${customerCallServiceList}" var="customerCallService" varStatus="status">
								<c:if test="${ customerCallService.group == 1 }">
									<c:if test="${customerCallService.serviceType == 2 }">
										<li>
											<span class="auth">
												<c:if test="${customerCallService.headImg == 1}">
													<img src="images/man.jpg">
												</c:if>
												<c:if test="${customerCallService.headImg == 2}">
													<img src="images/women.jpg">
												</c:if>
											</span>
											<div class="tel">
												<h2>${ customerCallService.name }</h2>
												<p>
													<img src="images/call.png">
													<a class="mobile" data-id="${ customerCallService.id }" href="tel:${ customerCallService.mobile }">${ customerCallService.mobile }</a>
												</p>
												<p>
													<img src="images/phone.png">
													<a class="phone" data-id="${ customerCallService.id }"  href="tel:${ customerCallService.phone }">${ customerCallService.phone }</a>
												</p>
											</div>
										</li>
									</c:if>
								</c:if>
							</c:forEach>
						</ul>
					</li>

					<li>
						<a class="inactive inforNav">
							出口退税<i></i>
						</a>
						<ul class="inforDiv">
							<c:forEach items="${customerCallServiceList}" var="customerCallService" varStatus="status">
								<c:if test="${ customerCallService.group == 1 }">
									<c:if test="${customerCallService.serviceType == 3 }">
										<li>
											<span class="auth">
												<c:if test="${customerCallService.headImg == 1}">
													<img src="images/man.jpg">
												</c:if>
												<c:if test="${customerCallService.headImg == 2}">
													<img src="images/women.jpg">
												</c:if>
											</span>
											<div class="tel">
												<h2>${ customerCallService.name }</h2>
												<p>
													<img src="images/call.png">
													<a class="mobile" data-id="${ customerCallService.id }" href="tel:${ customerCallService.mobile }">${ customerCallService.mobile }</a>
												</p>
												<p>
													<img src="images/phone.png">
													<a class="phone" data-id="${ customerCallService.id }"  href="tel:${ customerCallService.phone }">${ customerCallService.phone }</a>
												</p>
											</div>
										</li>
									</c:if>
								</c:if>
							</c:forEach>
						</ul>
					</li>

					<li>
						<a class="inactive inforNav">
							其他<i></i>
						</a>
						<ul class="inforDiv">
							<c:forEach items="${customerCallServiceList}" var="customerCallService" varStatus="status">
								<c:if test="${ customerCallService.group == 1 }">
									<c:if test="${customerCallService.serviceType == 4 }">
										<li>
											<span class="auth">
												<c:if test="${customerCallService.headImg == 1}">
													<img src="images/man.jpg">
												</c:if>
												<c:if test="${customerCallService.headImg == 2}">
													<img src="images/women.jpg">
												</c:if>
											</span>
											<div class="tel">
												<h2>${ customerCallService.name }</h2>
												<p>
													<img src="images/call.png">
													<a class="mobile" data-id="${ customerCallService.id }" href="tel:${ customerCallService.mobile }">${ customerCallService.mobile }</a>
												</p>
												<p>
													<img src="images/phone.png">
													<a class="phone" data-id="${ customerCallService.id }"  href="tel:${ customerCallService.phone }">${ customerCallService.phone }</a>
												</p>
											</div>
										</li>
									</c:if>
								</c:if>
							</c:forEach>
						</ul>
					</li>

				</ul>

				<p class="issueTit">税收政策类专家顾问团队</p>
				<ul>
					<li>
						<a class="inactive inforNav">
							增值税
							<span>（税收优惠、营改增等）</span>
							<i></i>
						</a>
						<ul class="inforDiv">
							<c:forEach items="${customerCallServiceList}" var="customerCallService" varStatus="status">
								<c:if test="${ customerCallService.group == 2 }">
									<c:if test="${customerCallService.serviceType == 5 }">
										<li>
											<span class="auth">
												<c:if test="${customerCallService.headImg == 1}">
													<img src="images/man.jpg">
												</c:if>
												<c:if test="${customerCallService.headImg == 2}">
													<img src="images/women.jpg">
												</c:if>
											</span>
											<div class="tel">
												<h2>${ customerCallService.name }</h2>
												<p>
													<img src="images/call.png">
													<a class="mobile" data-id="${ customerCallService.id }" href="tel:${ customerCallService.mobile }">${ customerCallService.mobile }</a>
												</p>
												<p>
													<img src="images/phone.png">
													<a class="phone" data-id="${ customerCallService.id }"  href="tel:${ customerCallService.phone }">${ customerCallService.phone }</a>
												</p>
											</div>
										</li>
									</c:if>
								</c:if>
							</c:forEach>
						</ul>
					</li>

					<li>
						<a class="inactive inforNav">
							消费税<i></i>
						</a>
						<ul class="inforDiv">
							<c:forEach items="${customerCallServiceList}" var="customerCallService" varStatus="status">
								<c:if test="${ customerCallService.group == 2 }">
									<c:if test="${customerCallService.serviceType == 6 }">
										<li>
											<span class="auth">
												<c:if test="${customerCallService.headImg == 1}">
													<img src="images/man.jpg">
												</c:if>
												<c:if test="${customerCallService.headImg == 2}">
													<img src="images/women.jpg">
												</c:if>
											</span>
											<div class="tel">
												<h2>${ customerCallService.name }</h2>
												<p>
													<img src="images/call.png">
													<a class="mobile" data-id="${ customerCallService.id }" href="tel:${ customerCallService.mobile }">${ customerCallService.mobile }</a>
												</p>
												<p>
													<img src="images/phone.png">
													<a class="phone" data-id="${ customerCallService.id }"  href="tel:${ customerCallService.phone }">${ customerCallService.phone }</a>
												</p>
											</div>
										</li>
									</c:if>
								</c:if>
							</c:forEach>
						</ul>
					</li>

					<li>
						<a href="#" class="inactive inforNav">
							企业所得税
							<span>（小微企业、税收优惠等）</span>
							<i></i>
						</a>
						<ul class="inforDiv">
							<c:forEach items="${customerCallServiceList}" var="customerCallService" varStatus="status">
								<c:if test="${ customerCallService.group == 2 }">
									<c:if test="${customerCallService.serviceType == 7 }">
										<li>
											<span class="auth">
												<c:if test="${customerCallService.headImg == 1}">
													<img src="images/man.jpg">
												</c:if>
												<c:if test="${customerCallService.headImg == 2}">
													<img src="images/women.jpg">
												</c:if>
											</span>
											<div class="tel">
												<h2>${ customerCallService.name }</h2>
												<p>
													<img src="images/call.png">
													<a class="mobile" data-id="${ customerCallService.id }" href="tel:${ customerCallService.mobile }">${ customerCallService.mobile }</a>
												</p>
												<p>
													<img src="images/phone.png">
													<a class="phone" data-id="${ customerCallService.id }"  href="tel:${ customerCallService.phone }">${ customerCallService.phone }</a>
												</p>
											</div>
										</li>
									</c:if>
								</c:if>
							</c:forEach>
						</ul>
					</li>

				</ul>

				<%-- <p class="issueTit">税收征管类专家顾问团队</p>
				<ul>
					<c:forEach items="${customerCallServiceList}" var="customerCallService" varStatus="status">
						<c:if test="${customerCallService.group == 1 }">
							<li>
								<a class="inactive inforNav">
									发票类<i></i>
								</a>
								<ul class="inforDiv">
									<li>
										<span class="auth">
											<img src="images/women.jpg">
										</span>
										<div class="tel">
											<h2>${customerCallService.name}</h2>
											<p>
												<img src="images/call.png">
												<a href="tel:${customerCallService.mobile}">${customerCallService.mobile}</a>
											</p>
											<p>
												<img src="images/phone.png">
												<a href="tel:${customerCallService.phone}">${customerCallService.phone}</a>
											</p>
										</div>
									</li>
								</ul>
							</li>
						</c:if>
					</c:forEach>
					
				</ul> --%>
			</div>

		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.inactive').click(function() {
				if ($(this).siblings('ul').css('display') == 'none') {
					$(this).parent('li').siblings('li').removeClass('inactives');
					$(this).addClass('inactives');
					$(this).siblings('ul').slideDown(100).children('li');
					if ($(this).parents('li').siblings('li').children('ul').css('display') == 'block') {
						$(this).parents('li').siblings('li').children('ul').parent('li').children('a').removeClass('inactives');
						$(this).parents('li').siblings('li').children('ul').slideUp(100);

					}
				} else {
					//控制自身变成+号
					$(this).removeClass('inactives');
					//控制自身菜单下子菜单隐藏
					$(this).siblings('ul').slideUp(100);
					//控制自身子菜单变成+号
					$(this).siblings('ul').children('li').children('ul').parent('li').children('a').addClass('inactives');
					//控制自身菜单下子菜单隐藏
					$(this).siblings('ul').children('li').children('ul').slideUp(100);

					//控制同级菜单只保持一个是展开的（-号显示）
					$(this).siblings('ul').children('li').children('a').removeClass('inactives');
				}
			})
		});
	</script>

</body>
</html>
