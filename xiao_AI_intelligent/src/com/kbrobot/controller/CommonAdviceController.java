package com.kbrobot.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.CustomerAdviceEntity;

/**
 * 微信公众号留言管理
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/commonAdviceController")
public class CommonAdviceController {
	
	@Autowired
	private SystemService systemService;
	
	@RequestMapping(params = {"leaveMessagePage","leaveMessagePage="})
	public ModelAndView goLeaveMessagePage(ModelMap modelMap,HttpServletRequest request){
		
		return new ModelAndView("kbrobot/commAdviceSubmit");
	}
	
	@RequestMapping(params = "addLeaveMessage")
	@ResponseBody
	public AjaxJson addLeaveMessage(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		
		String accountId = request.getParameter("accountId");
		
		if(StringUtil.isEmpty(accountId)){
			j.setMsg("对不起，请求链接不合法。");
			j.setSuccess(false);
			return j;
		}
		
		String name = request.getParameter("name");
		String mobile = request.getParameter("mobile");
		String content = request.getParameter("content");
		
		
		CustomerAdviceEntity entity = new CustomerAdviceEntity();
		entity.setAccountId(accountId);
		entity.setName(name);
		entity.setMobile(mobile);
		entity.setContent(content);
		
		try{
			systemService.save(entity);
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
		CustomerAdviceEntity entity = systemService.get(CustomerAdviceEntity.class, id);
		entity.setIsReply("1");
		systemService.save(entity);
		return j;
	}
	
	@RequestMapping(params = "leaveMessageList")
	public ModelAndView goLeaveMessageList(ModelMap modelMap,HttpServletRequest request){
		
		String weixinAccountId = ResourceUtil.getWeiXinAccountId();
		List<CustomerAdviceEntity> CustomerAdviceList = systemService.findByProperty(CustomerAdviceEntity.class, "accountId", weixinAccountId);
		modelMap.put("customerAdviceList", CustomerAdviceList);
		
		
		
		return new ModelAndView("kbrobot/consumer-advice");
	}
}
