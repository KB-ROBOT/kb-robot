package com.kbrobot.controller;

import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	public ModelAndView goTaxAdviserPage(){
		
		return new ModelAndView("kbrobot/callService");
	}
	
	@RequestMapping(params = "callServiceLook")
	//@ResponseBody
	public ModelAndView goCallServiceLook(){
		
		return new ModelAndView("kbrobot/tax-gw");
	}
	
	
	
}
