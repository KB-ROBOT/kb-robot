package com.kbrobot.controller.core;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/roleManageController")
public class RoleManageController extends BaseController {

	@RequestMapping(params = "roleManage")
	public ModelAndView roleManage(HttpServletRequest request){
		return new ModelAndView("kbrobot/role-manage");
	}
}
