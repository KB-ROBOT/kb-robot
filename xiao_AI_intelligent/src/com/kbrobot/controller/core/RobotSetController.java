package com.kbrobot.controller.core;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.RobotInfoEntity;

@Controller
@RequestMapping("/robotSetController")
public class RobotSetController {
	
	@Autowired
	private SystemService systemService;
	
	@RequestMapping(params = "goRobotSet")
	public ModelAndView goRobotSet(HttpServletRequest request){
		
		String weixinAccountId = ResourceUtil.getWeiXinAccount().getWeixinAccountId();
		
		/*CriteriaQuery robotInfoQuery = new CriteriaQuery(RobotInfoEntity.class);
		robotInfoQuery.eq("weixinAccountId", weixinAccountId);*/
		
		RobotInfoEntity robotInfoEntity = systemService.findUniqueByProperty(RobotInfoEntity.class, "weixinAccountId", weixinAccountId);
		
		if(robotInfoEntity==null||robotInfoEntity.getId()==null){
			robotInfoEntity = new RobotInfoEntity();
			robotInfoEntity.setWeixinAccountId(weixinAccountId);
			systemService.save(robotInfoEntity);
		}
		
		request.setAttribute("robotInfoEntity", robotInfoEntity);
		
		
		return new ModelAndView("kbrobot/set");
	}
	
	@RequestMapping(params = "saveRobotSet")
	@ResponseBody
	public AjaxJson saveRobotSet(RobotInfoEntity robotInfoEntity){
		
		AjaxJson j = new AjaxJson();
		
		if(robotInfoEntity==null||robotInfoEntity.getId()==null){
			j.setSuccess(false);
			j.setMsg("所填信息为空。");
		}
		else{
			
			RobotInfoEntity oldEntity = systemService.get(RobotInfoEntity.class, robotInfoEntity.getId());
			
			try {
				MyBeanUtils.copyBeanNotNull2Bean(robotInfoEntity, oldEntity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			systemService.saveOrUpdate(oldEntity);
			j.setSuccess(true);
			j.setMsg("修改成功。");
		}
		
		return j;
		
	}
	
	
	
	
}
