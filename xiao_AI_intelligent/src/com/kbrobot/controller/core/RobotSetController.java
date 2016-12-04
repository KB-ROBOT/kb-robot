package com.kbrobot.controller.core;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/robotSetController")
public class RobotSetController {
	
	@RequestMapping(params = "goRobotSet")
	public ModelAndView goRobotSet(HttpServletRequest request){
		return new ModelAndView("kbrobot/set");
	}
}
