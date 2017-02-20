package com.kbrobot.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.hibernate.qbc.PageList;
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

import com.kbrobot.entity.CustomerCallServiceEntity;

/**
 * 纳税顾问
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/commonCallServiceController")
public class CommonCallServiceController {

	@Autowired
	private SystemService systemService;

	@RequestMapping(params = "callServiceList")
	//@ResponseBody
	public ModelAndView callServiceList(ModelMap modelMap,HttpServletRequest request){

		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();
		//当前页码
		String curPageNO = request.getParameter("curPageNO");
		if(StringUtil.isEmpty(curPageNO)){
			curPageNO = "1";
		}

		CriteriaQuery cq = new CriteriaQuery(CustomerCallServiceEntity.class, Integer.valueOf(curPageNO));
		cq.eq("accountId", accountId);
		cq.setPageSize(15);
		cq.setMyAction("./commonCallServiceController.do?callServiceList");
		cq.add();//加载条件
		
		PageList pageList = systemService.getPageList(cq, true);
		modelMap.put("pageList", pageList);
		modelMap.put("accountId", accountId);

		return new ModelAndView("kbrobot/callService");
	}
	
	@RequestMapping(params = "callServiceLook")
	//@ResponseBody
	public ModelAndView callServiceLook(ModelMap modelMap,HttpServletRequest request){

		//获取当前微信账户id
		String accountId =request.getParameter("accountId");
		
		if(accountId==null || accountId.equals("")){
			
		}
		else{
			CriteriaQuery cq = new CriteriaQuery(CustomerCallServiceEntity.class);
			cq.eq("accountId", accountId);
			cq.setPageSize(15);
			cq.setMyAction("./commonCallServiceController.do?callServiceLook");
			cq.add();//加载条件
			
			List<CustomerCallServiceEntity> customerCallServiceList = systemService.getListByCriteriaQuery(cq, false);
			modelMap.put("customerCallServiceList", customerCallServiceList);
		}

		return new ModelAndView("kbrobot/tax-gw");
	}
/*
	@RequestMapping(params = "callServiceLook")
	//@ResponseBody
	public ModelAndView goCallServiceLook(){

		return new ModelAndView("kbrobot/tax-gw");
	}*/

	@RequestMapping(params = "callServiceInsert")
	@ResponseBody
	public AjaxJson callServiceInsert(HttpServletRequest request ,CustomerCallServiceEntity customerCallService ){

		AjaxJson j = new AjaxJson();
		
		

		if(customerCallService!=null){
			
			if(customerCallService.getId()!=null && customerCallService.getId().equals("")){
				customerCallService.setId(null);
			}
			
			try{
				systemService.saveOrUpdate(customerCallService);
				j.setSuccess(true);
			}
			catch(Exception e){
				j.setMsg("发生错误");
				j.setSuccess(false);
			}
			
		}
		else{
			j.setMsg("参数错误");
			j.setSuccess(false);
		}

		return j;
	}
	
	@RequestMapping(params = "callServiceDel")
	@ResponseBody
	public AjaxJson callServiceDel(HttpServletRequest request ,CustomerCallServiceEntity customerCallService ){

		AjaxJson j = new AjaxJson();

		if(customerCallService!=null){
			try{
				systemService.delete(customerCallService);
				j.setSuccess(true);
			}
			catch(Exception e){
				j.setMsg("发生错误");
				j.setSuccess(false);
			}
			
		}
		else{
			j.setMsg("参数错误");
			j.setSuccess(false);
		}

		return j;
	}

}

