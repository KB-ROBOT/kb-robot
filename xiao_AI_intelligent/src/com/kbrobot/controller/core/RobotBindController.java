package com.kbrobot.controller.core;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.base.entity.Subscribe;
import weixin.guanjia.base.service.SubscribeServiceI;
import weixin.guanjia.message.entity.AutoResponse;
import weixin.guanjia.message.service.AutoResponseServiceI;

/**
 * 
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/robotBindController")
public class RobotBindController extends BaseController  {
	
	@Autowired
	private WeixinAccountServiceI weixinAccountService;//微信账户
	@Autowired
	private SubscribeServiceI subscribeService;//关注事件
	@Autowired
	private AutoResponseServiceI autoResponseService;//关键字回复
	
	/**
	 * 微信绑定
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goweixinbind")
	public ModelAndView goWeixinBind(ModelMap modelMap,HttpServletRequest request){
		/*
		 * 初始化数据
		 * 
		 */
		//已经绑定的微信账号
		TSUser user = ResourceUtil.getSessionUserName();
		List<WeixinAccountEntity> weixinAccountList = weixinAccountService.findByUsername(user.getUserName());
		modelMap.put("weixinAccountList", weixinAccountList);
		
		/*
		 * 当前微信账号基本设置
		 */
		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();
		//关注回复消息
		List<Subscribe> subscribeList = subscribeService.findByProperty(Subscribe.class, "accountid", accountId);
		//关键字
		List<AutoResponse> autoResponses = autoResponseService.findByProperty(AutoResponse.class, "accountId", accountId);
		
		
		ModelAndView modelAndView = new ModelAndView("kbrobot/access-wx");
		return modelAndView;
	}
	/**
	 * 网页绑定
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "gowebbind")
	public ModelAndView goWebBind(HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView("kbrobot/access-web");
		return modelAndView;
	}
}
