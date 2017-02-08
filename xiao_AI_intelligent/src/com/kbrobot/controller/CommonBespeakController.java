package com.kbrobot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.hibernate.qbc.PageList;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.CustomerBespeakEntity;
import com.kbrobot.entity.CustomerBespeakSetEntity;
import com.kbrobot.utils.CustomServiceUtil;
import com.kbrobot.utils.WeixinThirdUtil;

import weixin.guanjia.account.entity.WeixinAccountEntity;

/**
 * 预约
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/commonBespeakController")
public class CommonBespeakController {
	
	WeixinThirdUtil weixinThirdUtilInstance = WeixinThirdUtil.getInstance();
	
	@Autowired
	private SystemService systemService;
	
	@RequestMapping(params = "bespeakSubmit")
	@ResponseBody
	public void bespeakSubmit(HttpServletRequest request,HttpServletResponse response,CustomerBespeakEntity bespeak) throws IOException{
		
		AjaxJson j = new AjaxJson();
		
		if(bespeak==null){
			j.setSuccess(false);
		}
		else{
			try{
				//保存
				systemService.save(bespeak);
				
				{
					String toUser = bespeak.getTargetOpenid();
					String accountId = bespeak.getAccountId();
					
					WeixinAccountEntity currentWeixinAccountEntity = systemService.get(WeixinAccountEntity.class, accountId);
					String authorizer_access_token = weixinThirdUtilInstance.getAuthorizerAccessToken(currentWeixinAccountEntity.getWeixinAccountId());
					
					//预定客户发送消息
					CustomServiceUtil.sendCustomServiceTextMessage(toUser, authorizer_access_token, "您好，您已经预约成功。");
					
					CustomerBespeakSetEntity customerBespeakSetEntity = systemService.findUniqueByProperty(CustomerBespeakSetEntity.class ,"accountId", accountId);
					
					if(customerBespeakSetEntity!=null){
						//预定提醒管理员
						CustomServiceUtil.sendCustomServiceTextMessage(customerBespeakSetEntity.getOpenId(), authorizer_access_token, "您好，有新客户预约，请及时登陆后台查看。");
					}
				}
				
				j.setSuccess(true);
			}
			catch(Exception e){
				j.setSuccess(false);
			}
		}
		
		response.getWriter().write("callback(" + j.getJsonStr() +")");
	}
	
	@RequestMapping(params = "bespeakEdit")
	@ResponseBody
	public AjaxJson bespeakEdit(ModelMap modelMap,HttpServletRequest request,CustomerBespeakSetEntity customerBespeakSetEntity){
		AjaxJson j = new AjaxJson();
		
		if(customerBespeakSetEntity==null){
			j.setSuccess(false);
		}
		else{
			
			try {
				CustomerBespeakSetEntity oldEntity = systemService.findUniqueByProperty(CustomerBespeakSetEntity.class, "accountId", customerBespeakSetEntity.getAccountId());
				
				if(oldEntity==null){
					oldEntity = new CustomerBespeakSetEntity();
				}
				
				MyBeanUtils.copyBeanNotNull2Bean(customerBespeakSetEntity, oldEntity);
				
				systemService.saveOrUpdate(oldEntity);
				
				j.setSuccess(true);
			}
			catch (Exception e) {
				e.printStackTrace();
				j.setSuccess(false);
			}
			
		}
		
		
		return j;
	}
	
	/**
	 * 分页
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "bespeakList")
	public ModelAndView questionList(HttpServletRequest request,ModelMap modelMap){
		
		
		String weixinAccountId = ResourceUtil.getWeiXinAccountId();
		List<CustomerBespeakEntity> customerBespeakList = new ArrayList<CustomerBespeakEntity>();
		CustomerBespeakSetEntity customerBespeakSetEntity = systemService.findUniqueByProperty(CustomerBespeakSetEntity.class ,"accountId", weixinAccountId);

		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();
		//当前页码
		String curPageNO = request.getParameter("curPageNO");
		if(StringUtil.isEmpty(curPageNO)){
			curPageNO = "1";
		}

		CriteriaQuery cq = new CriteriaQuery(CustomerBespeakEntity.class, Integer.valueOf(curPageNO));
		//搜索参数
		String searchKey = request.getParameter("searchKey");
		String searchParam = request.getParameter("searchParam");

		if(StringUtil.isNotEmpty(searchParam)){
			cq.like(searchKey, searchParam);
		}
		
		request.setAttribute("searchParam", searchParam);
		request.setAttribute("searchKey", searchKey);
		
		cq.eq("accountId", accountId);
		cq.setPageSize(15);
		cq.setMyAction("./commonBespeakController.do?bespeakList");
		//cq.addOrder("createTime", SortDirection.desc);//根据时间顺寻排序
		cq.add();//加载条件
		
		PageList pageList = systemService.getPageList(cq, true);
		customerBespeakList = pageList.getResultList();
		modelMap.put("pageList", pageList);
		
		
		modelMap.put("customerBespeakList", customerBespeakList);
		modelMap.put("customerBespeakSetEntity", customerBespeakSetEntity);
		modelMap.put("weiXinAccount", ResourceUtil.getWeiXinAccount());

		return new ModelAndView("kbrobot/yybsList");
	}
	
	@RequestMapping(params = "bespeakDel")
	@ResponseBody
	public AjaxJson bespeakDel(HttpServletRequest request,CustomerBespeakEntity customerBespeakEntity){
		AjaxJson j = new AjaxJson();
		
		try{
			if(customerBespeakEntity!=null){
				systemService.delete(customerBespeakEntity);
				
				j.setSuccess(true);
			}
			else{
				j.setSuccess(false);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			j.setSuccess(false);
		}
		
		return j;
	}
	
}
