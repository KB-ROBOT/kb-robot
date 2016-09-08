//账户中心
$(document).ready(function () {
	$('.dropdown-toggle').dropdown();

	//左侧菜单栏	
	$(function animate(){
		$(".submenu").click(function(){
			var $parent = $(this);
			if($parent.hasClass('close_bg')){
				$parent.next().slideUp(function(){
					$parent.removeClass('close_bg');
				});
				$(this).children('font').removeClass('fa-angle-down');
				$(this).children('font').addClass("fa-angle-right");

			} else {
				$parent.next().slideDown(function(){
					$parent.addClass('close_bg');
				});
				$(this).children('font').removeClass('fa-angle-right');
				$(this).children('font').addClass('fa-angle-down');
			}
		});
	});


	//文本返回
	$('.tab-pane').click(function(){
		$(this).parent().hide().siblings('.news').show();
	});
	$('.tab-return').click(function(ev){

		$(this).parent().parent().parent().parent().hide().siblings('.choose-btn').show();
	});
	//url返回
	$('.tab-url').click(function(){
		$(this).parent().hide().siblings('.urls').show();
	});
	$('.url-return').click(function(ev){

		$(this).parent().parent().hide().siblings('.choose-btn').show();
	});
	
	
	//tab选项卡
	$('.tabs li').click(function(){
		$('.tabs li').removeClass('curr');
		var contId = $(this).attr('data');
		$('.conts .cont').hide();
		$('#' + contId).show();
		$(this).addClass('curr');
	});

	//滑块

	$(function() {
		$( "#mLeft,#mBottom" ).slider({
			range: "min",
			value: 50,
			min: 0,
			max: 100,
			slide: function( event, ui ) {
				$(this).find("#amount" ).html(ui.value );
			}
		});
		$( "#amount" ).html( $("#mLeft" ).slider( "value" ) );
		$( "#amount" ).html( $("#mBottom" ).slider( "value" ) );
	});


	//清空列表
	$('.clearBtn').click(function () {
		$('#clearModal').modal('show');
	});

	//添加相似问题
	$(function animate(){
		$(".similarBtn").click(function(){
			var $parent = $(this);
			if($parent.hasClass('close_bg')){
				$parent.siblings('.add-similar').slideUp(function(){
					$parent.removeClass('close_bg');
				});
			} else {
				$parent.siblings('.add-similar').slideDown(function(){
					$parent.addClass('close_bg');
				});  
			}
		});
	});
	
	
});



