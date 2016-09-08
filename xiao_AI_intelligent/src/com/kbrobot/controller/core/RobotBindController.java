package com.kbrobot.controller.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeewx.api.core.exception.WexinReqException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.utils.WeixinThirdUtil;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.base.entity.Subscribe;
import weixin.guanjia.base.service.SubscribeServiceI;
import weixin.guanjia.menu.entity.MenuEntity;
import weixin.guanjia.menu.service.WeixinMenuServiceI;
import weixin.guanjia.message.entity.AutoResponse;
import weixin.guanjia.message.entity.NewsTemplate;
import weixin.guanjia.message.entity.TextTemplate;
import weixin.guanjia.message.service.AutoResponseServiceI;
import weixin.guanjia.message.service.NewsTemplateServiceI;
import weixin.guanjia.message.service.TextTemplateServiceI;

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
	@Autowired
	private WeixinMenuServiceI weixinMenuService;//微信菜单

	@Autowired
	private TextTemplateServiceI textTemplateService;//文本素材
	@Autowired
	private NewsTemplateServiceI newsTemplateService;//图文素材


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

		//判断是否已经授权
		
		for(WeixinAccountEntity bindAccount:weixinAccountList){
			try {
				WeixinThirdUtil.getInstance().getAuthorizerAccessToken(bindAccount.getWeixinAccountId());
			} catch (WexinReqException e) {

				String msg = e.getMessage();
				if(msg.contains("61003")){//该公众号已经取消了授权
					int index = weixinAccountList.indexOf(bindAccount);
					bindAccount.setAccountAuthorizeType("2");
					weixinAccountList.set(index, bindAccount);
				}
			}
		}


		modelMap.put("weixinAccountList", weixinAccountList);

		/*
		 * 当前微信账号基本设置
		 */
		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();
		//关注回复消息
		List<Subscribe> subscribeList = subscribeService.findByProperty(Subscribe.class, "accountid", accountId);

		//关键字
		List<AutoResponse> autoResponseList = autoResponseService.findByProperty(AutoResponse.class, "accountId", accountId);
		//菜单
		List<MenuEntity> menuList = this.weixinMenuService.findByProperty(MenuEntity.class,"accountId",accountId);

		List<MenuEntity> fatherMenuList = new ArrayList<MenuEntity>();

		for(MenuEntity menu : menuList){
			if(menu.getMenuEntity()==null){
				fatherMenuList.add(menu);
			}
		}

		modelMap.put("subscribeList", subscribeList);
		modelMap.put("autoResponseList", autoResponseList);
		modelMap.put("menuList", menuList);
		modelMap.put("fatherMenuList", fatherMenuList);

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

	/**
	 * 素材查找
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "mediaSourceFind")
	@ResponseBody
	public AjaxJson mediaSourceFind(HttpServletRequest request){
		AjaxJson j = new AjaxJson();

		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();

		List<TextTemplate> textTemplateList =  textTemplateService.findByProperty(TextTemplate.class, "accountId", accountId);

		List<NewsTemplate> newsTemplateList = newsTemplateService.findByProperty(NewsTemplate.class, "accountId", accountId);

		//select newsTemplate.tempateName as templateName from weixin_texttemplate newsTemplate where newsTemplate.accountId =
		/*List<TextTemplate> textTemplateList =  textTemplateService.findByQueryString("select tempateName from weixin_texttemplate where accountId ='"+accountId+"'");

		List<NewsTemplate> newsTemplateList = newsTemplateService.findByQueryString("select tempateName from weixin_newstemplate where accountId ='"+accountId+"'");
		 */
		/*for(NewsTemplate newsTemplate : newsTemplateList){
			newsTemplate.setNewsItemList(null);
		}*/

		Map<String,Object> listMap = new HashMap<String,Object>();
		listMap.put("textTemplateList", textTemplateList);
		listMap.put("newsTemplateList", newsTemplateList);

		j.setAttributes(listMap);
		j.setSuccess(true);

		return j;
	}

	@RequestMapping(params = "bindweixinsuccess")
	public ModelAndView bindSuccess(){
		return new ModelAndView("kbrobot/auth-success");
	}

	@RequestMapping(params = "bindweixinerror")
	public ModelAndView bindError(){
		return new ModelAndView("kbrobot/auth-error");
	}
}
