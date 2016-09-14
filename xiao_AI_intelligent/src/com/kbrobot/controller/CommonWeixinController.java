package com.kbrobot.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.RobotQuestionEntity;

import weixin.util.DateUtils;

/**
 * 微信公众号公共coltroller
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/commonWeixinController")
public class CommonWeixinController {
	
	@Autowired
	private SystemService systemService;
	
	@RequestMapping(params = {"hotQuestionList","hotQuestionList="})
	public ModelAndView goHotQuestionList(ModelMap modelMap,HttpServletRequest request){
		String accountId = request.getParameter("accountId");
		
		CriteriaQuery cq = new CriteriaQuery(RobotQuestionEntity.class);
		
		cq.eq("accountId", accountId);
		cq.notEq("matchTimes", 0);
		cq.addOrder("matchTimes", SortDirection.desc);//根据匹配数量从大到小排序
		cq.setPageSize(10);
		cq.add();//加载条件
		
		List<RobotQuestionEntity> topQuestionList =  systemService.getListByCriteriaQuery(cq, true);//isPage为true，只提取前几条
		
		modelMap.put("topQuestionList", topQuestionList);
		modelMap.put("dataUpdateDate", DateUtils.date2Str(new Date() , DateUtils.time_sdf));	
		
		return new ModelAndView("kbrobot/commonHotQuestionList");
	}
}
