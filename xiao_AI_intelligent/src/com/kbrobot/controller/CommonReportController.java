package com.kbrobot.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.CustomerBespeakSetEntity;
import com.kbrobot.entity.CustomerReportEntity;
import com.kbrobot.utils.CustomServiceUtil;
import com.kbrobot.utils.WeixinThirdUtil;

import weixin.guanjia.account.entity.WeixinAccountEntity;

/**
 * 微信公众号举报管理
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/commonReportController")
public class CommonReportController  {

	@Autowired
	private SystemService systemService;

	WeixinThirdUtil weixinThirdUtilInstance = WeixinThirdUtil.getInstance();

	@RequestMapping(params = {"reportPage","reportPage="})
	public ModelAndView goReportPage(ModelMap modelMap,HttpServletRequest request){

		return new ModelAndView("kbrobot/commReportSubmit");
	}

	@RequestMapping(params = "addReportMessage")
	@ResponseBody
	public AjaxJson addReportMessage(HttpServletRequest request){
		AjaxJson j = new AjaxJson();

		String accountId = request.getParameter("accountId");

		String targetOpenid = request.getParameter("targetOpenid");

		if(StringUtil.isEmpty(accountId)){
			j.setMsg("对不起，请求链接不合法。");
			j.setSuccess(false);
			return j;
		}

		String name = request.getParameter("name");
		String mobile = request.getParameter("mobile");
		String content = request.getParameter("content");


		CustomerReportEntity entity = new CustomerReportEntity();
		entity.setAccountId(accountId);
		entity.setName(name);
		entity.setMobile(mobile);
		entity.setContent(content);

		try{
			systemService.save(entity);

			if(StringUtil.isNotEmpty(targetOpenid)){
				
				LogUtil.info(targetOpenid);
				
				WeixinAccountEntity currentWeixinAccountEntity = systemService.get(WeixinAccountEntity.class, accountId);
				String authorizer_access_token = weixinThirdUtilInstance.getAuthorizerAccessToken(currentWeixinAccountEntity.getWeixinAccountId());

				//预定客户发送消息
				//CustomServiceUtil.sendCustomServiceTextMessage(targetOpenid, authorizer_access_token, "您好，您的投诉已受理。");

				CustomerBespeakSetEntity customerBespeakSetEntity = systemService.findUniqueByProperty(CustomerBespeakSetEntity.class ,"accountId", accountId);

				if(customerBespeakSetEntity!=null){

					String messageAdmin = "";

					messageAdmin += "您好，收到新的投诉，请及时登陆后台查看。";
					messageAdmin += "\n姓　　名：" + entity.getName();
					messageAdmin += "\n联系方式：" + entity.getMobile();
					messageAdmin += "\n投诉内容：" + entity.getContent();

					//预定提醒管理员
					CustomServiceUtil.sendCustomServiceTextMessage(customerBespeakSetEntity.getOpenId(), authorizer_access_token, messageAdmin);
				
					LogUtil.info(messageAdmin);
				}

			}


			j.setSuccess(true);
		}

		catch(Exception e){
			j.setMsg("保存异常，请稍后再试");
			j.setSuccess(false);
		}

		return j;
	}

	@RequestMapping(params = "confirmReply")
	@ResponseBody
	public AjaxJson confirmReply(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String id = request.getParameter("id");
		CustomerReportEntity entity = systemService.get(CustomerReportEntity.class, id);
		entity.setIsReply("1");
		systemService.save(entity);
		return j;
	}

	@RequestMapping(params = "reportMessageList")
	public ModelAndView goReportMessageList(ModelMap modelMap,HttpServletRequest request){

		String weixinAccountId = ResourceUtil.getWeiXinAccountId();
		List<CustomerReportEntity> CustomerReportList = systemService.findByProperty(CustomerReportEntity.class, "accountId", weixinAccountId);
		modelMap.put("customerReportList", CustomerReportList);



		return new ModelAndView("kbrobot/consumer-report");
	}


}
