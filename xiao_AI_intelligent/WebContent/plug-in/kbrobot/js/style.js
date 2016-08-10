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

	$('.news span').click(function(){
		$('.news span').removeClass('curre');
		var newtId = $(this).attr('data');
		$('.new-conts .new').hide();
		$('#' + newtId).show();
		$(this).addClass('curre');
	});

	$('.fans-content p.nav-define').click(function(){
		$('.define-none').hide();
		$('.fans-content p.nav-define').removeClass('on');
		var defineId = $(this).attr('data');
		$('.defines .define').hide();
		$('#' + defineId).show();
		$(this).addClass('on');
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

	$("#add_Que_similar").click(function(){
		$("#add_similar").append(function(n){
			return "<div class=\"form-group simi_group\">\
			<label class=\"control-label\">相似问法</label>\
			<div class=\"input-group input-group-minimal\">\
			<input class=\"form-control\" type=\"text\">\
			<span class=\"input-group-btn\">\
			<a href=\"javascript:;\" class=\"btn btn-white del_Que_similar\">\
			<i class=\"fa fa-minus-square-o\"></i>\
			</a>\
			</span>\
			</div>\
			</div>";
		});
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

	//时间控件
	$(".form_datetime").datetimepicker({
		format: "yyyy-MM-DD  hh:ii",
		language: 'zh-CN'
	});



	//点击添加
	/*UM.getEditor('content_1');
	UM.getEditor('content_2');*/
/*	$('#collapse_0').collapse('show');
	var fromurl  = location.href;
	if(!fromurl || fromurl==""){}
	var urlId=fromurl.substring(fromurl.indexOf("=")+1,fromurl.length);
	if(urlId > 0){
		loadInfo(urlId);
	}



	//封面图片
	$('#imgTxt').on('change','.showPiccheckBox',function(){
		if (!$(this).is(':checked')) {
			$(this).siblings().val(0);
		}else{
			$(this).siblings().val(1);
		}
	}); */

	//修改时页面初始化拼接
	/*function loadInfo(id){
		$.ajax({
			type:'get',
			datatype:'json',
			cache:false,//不从缓存中去数据
			url:encodeURI('../../Wxappmsg/findById?id='+id),
			success:
				function(json){
				if(typeof json.result.wxappmsg != 'undefined'){
					var list=json.result.wxappmsg;
					if(list.wxappmsgDetails.length>0){
						$("#img1").attr("src",list.wxappmsgDetails[0].imgUrl);
						$(".imgFile_1").append('<input type="hidden"  value="'+list.wxappmsgDetails[0].imgUrl+'" name="imgURL"/>');
						$("#left_title_0").empty();
						$("#left_title_0").append(list.wxappmsgDetails[0].title);
						$("#right_title_0").empty();
						$("#right_title_0").append(list.wxappmsgDetails[0].title);
						$(":text[id='title1']").prop('value', list.wxappmsgDetails[0].title);
						$(":text[id='author1']").prop('value', list.wxappmsgDetails[0].author);
						$("#digest1").append(list.wxappmsgDetails[0].digest);

						//是否显示封面图片
						var picBox=list.wxappmsgDetails[0].showPic;
						if(picBox==1){
							$("#sourceurl1").siblings().attr('checked',true); 
						}else if(picBox==0){
							$("#sourceurl1").siblings().attr('checked',false);  
						}

						UE.getEditor('content_1').ready( function( ) {
							UE.getEditor('content_1').setContent(list.wxappmsgDetails[0].content);
						});
						if(typeof list.wxappmsgDetails[1] !='undefined'){
							$("#img2").attr("src",list.wxappmsgDetails[1].imgUrl);
							$(".imgFile_2").append('<input type="hidden"  value="'+list.wxappmsgDetails[1].imgUrl+'" name="imgURL"/>');
							$("#left_title_1").empty();
							$("#left_title_1").append(list.wxappmsgDetails[1].title);
							$("#right_title_1").empty();
							$("#right_title_1").append(list.wxappmsgDetails[1].title);
							$(":text[id='title2']").prop('value', list.wxappmsgDetails[1].title);
							$(":text[id='author2']").prop('value', list.wxappmsgDetails[1].author);
							$("#digest2").append(list.wxappmsgDetails[1].digest);
							//是否显示封面图片
							var picBox=list.wxappmsgDetails[1].showPic;
							if(picBox==1){
								$("#sourceurl2").siblings().attr('checked',true); 
							}else if(picBox==0){
								$("#sourceurl2").siblings().attr('checked',true); 
							}

							UE.getEditor('content_2').ready( function( ) {
								UE.getEditor('content_2').setContent(list.wxappmsgDetails[1].content);
							});
						}


						if(list.wxappmsgDetails.length > 2){
							for(var i=1; i<list.wxappmsgDetails.length; i++){
								var s;
								var m;
								//左侧添加
								i = $('.left_list li').length;
								s = '<li id="left_'+i+'" class="yun_position_relative">';
								s += '<div class="row yun_border_bottom_e">';
								s += '<div class="col-sm-8 yun_overflow_hidden fonts" id="left_title_'+i+'">'+list.wxappmsgDetails[i].title+'</div>';
								s += '<div class="col-sm-4">';
								s += '<img id=\"img'+(i+1)+'\" alt="" class="pull-right" src="'+list.wxappmsgDetails[i].imgUrl+'">';
								s += '</div>';
								s += '</div>';
								s += '<div class="yun_display_block">';
								s += '<div class="yun_position_absolute_0">';
								s += '<a onClick="Location(\'collapse_'+i+'\')"><i class="fa fa-pencil"></i></a>&nbsp;';
								s += '<a onClick="delpage(\'left_'+i+'\',\'right_'+i+'\')"><i class="fa fa-trash-o"></i></a>';
								s += '</div>';
								s += '</div>';
								s += '</li>';
								$('.left_list').append(s);
								//右侧添加


								m = '<li class="accordion-group mt10" id="right_'+i+'">';
								m += '<div class="panel-heading">';
								m += ' <h4 class="panel-title"> <a aria-controls="collapseOne" aria-expanded="false" href="#collapse_'+i+'" data-parent="#accordion2" data-toggle="collapse" class="collapsed" id="right_title_'+i+'" onClick="Location(\'collapse_'+i+'\')">'+list.wxappmsgDetails[i].title+'</a></h4>';
								m += '</div>';
								m += '<div id="collapse_'+i+'" class="panel-collapse collapse" aria-expanded="false">';
								m += '<div  class="panel-body">';
								m += '<label>标题</label><input type=\"text\" name=\"title\" id=\"title'+(i+1)+'\" class=\"form-control\" maxlength=\"64\" onChange=\"change(this,\'left_title_'+i+'\',\'right_title_'+i+'\')\" value='+list.wxappmsgDetails[i].title+'>';
								m += '<label class="m10">作者</label><input type=\"text\" name=\"author\" maxlength=\"8\" id=\"author'+(i+1)+'\" class=\"form-control\" value='+list.wxappmsgDetails[i].author+' >';
								m += '<div class=\"imgFile_'+(i+1)+'\"  style=\"margin-top:10px;position:relative;\">'; 
								m += '<span class=\"btn btn-success fileinput-button\"> <i class=\"fa fa-plus\"></i> <span>请选择封面图片</span>';
								if(i>=1){
									m += '</span><p class="clearBoth">小图片建议尺寸：200像素 * 200像素;</p>  ';
								}else{
									m += '</span><p class="clearBoth">大图片建议尺寸：900像素 * 500像素;</p>';
								}
								//根据是否显示判断是否选中checkbox
								var picBox=list.wxappmsgDetails[i].showPic;
								if(picBox==1){
									m += '<p class="help-block"><input type="checkbox"  class="showPiccheckBox" checked >';
								}else if(picBox==0){
									m += '<p class="help-block"><input type="checkbox"  class="showPiccheckBox" >';
								}
								m += '<input id=\"sourceurl'+(i+1)+'\" type="hidden" name="showPic" value="'+picBox+'">&nbsp;封面图片显示在正文中</p>'
								m += '<div id=\"progress_'+(i+1)+'\" class="progress m10" style="margin-bottom:0">';
								m += '<div class=\"progress-bar progress-bar-success\"></div>';
								m += '</div>';
								m += '<div id=\"files_'+(i+1)+'\" class=\"files\"></div>';
								m += '<input type="hidden" value="'+list.wxappmsgDetails[i].imgUrl+'" name="imgURL"/></div>';
								m += '<label class="m10">图文内容</label><textarea style=\"height:200px;width:100%;\"  name=\"content\" id=\"content_'+(i+1)+'\">'+list.wxappmsgDetails[i].content+'</textarea>';
								m += '</div>';
								m += '</div>';
								m += '</li>';
								$('.right_list').append(m);
								UE.getEditor('content_'+(i+1));
							}
						}
					}
				}
			}
		});

	}*/


});



