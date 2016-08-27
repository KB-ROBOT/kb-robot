package com.kbrobot.controller.core;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.controller.BaseController;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kbrobot.utils.LtpUtil;
import com.kbrobot.utils.TuLingUtil;
/**
 * 语义理解
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/ltpController")
public class LtpController extends BaseController {
	
	@RequestMapping(params = "ltptest")
	public String ltpTest(HttpServletRequest request) throws IOException, JSONException{
		System.out.println("ltptest");
		
		String speakStr = request.getParameter("speakStr");

		JSONObject jsonObj = LtpUtil.getLTPResultByStr(speakStr==null||speakStr.equals("")?"空":speakStr);
		
		request.setAttribute("ltpResult", TuLingUtil.getResultBySpeakStr(speakStr,"123456") + "<br/>" + jsonObj.toString());
		System.out.println(speakStr);
		
		return "kbrobot/ltp";
	}
	
	
}
