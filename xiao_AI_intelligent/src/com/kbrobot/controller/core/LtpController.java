package com.kbrobot.controller.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.web.system.service.SystemService;
import org.jeewx.api.core.exception.WexinReqException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.utils.Sign;
import com.kbrobot.utils.WeixinThirdUtil;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
/**
 * 语义理解
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/ltpController")
public class LtpController extends BaseController {
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	
	@RequestMapping(params = "ltptest")
	public String ltpTest(HttpServletRequest request) throws IOException, JSONException{
		System.out.println("ltptest");
		
		
		/*String speakStr = request.getParameter("speakStr");

		JSONObject jsonObj = LtpUtil.getLTPResultByStr(speakStr==null||speakStr.equals("")?"空":speakStr);
		
		request.setAttribute("ltpResult", TuLingUtil.getResultBySpeakStr(speakStr,"123456") + "<br/>" + jsonObj.toString());
		System.out.println(speakStr);
		
		return "kbrobot/ltp";*/
		
		List<RobotQuestionEntity> xtxgsQuestionList = new ArrayList<RobotQuestionEntity>();
		
		List<RobotQuestionEntity> frgsQuestionList = systemService.findByProperty(RobotQuestionEntity.class,"accountId" , "8af5366957044e1f015708e276a20037");
		
		for(RobotQuestionEntity ques : frgsQuestionList){
			RobotQuestionEntity xtxgsQuestion = new RobotQuestionEntity();
			xtxgsQuestion.setAccountId("8af53669597324bf0159812dfa770117");
			xtxgsQuestion.setCreateTime(new Date());
			xtxgsQuestion.setUpdateTime(new Date());
			xtxgsQuestion.setQuestionTitle(ques.getQuestionTitle());
			xtxgsQuestion.setQuestionAnswer(ques.getQuestionAnswer());
			xtxgsQuestion.setWordSplit(ques.getWordSplit());
			xtxgsQuestion.setMatchTimes(0);
			
			xtxgsQuestionList.add(xtxgsQuestion);
		}
		
		//systemService.batchSave(xtxgsQuestionList);
		
		return "kbrobot/ltp";
	}
	
	@RequestMapping(params = "jsApiTicketTest")
	@ResponseBody
	public void getJsApiTicket(HttpServletRequest request,HttpServletResponse response) throws WexinReqException, IOException{
		AjaxJson j = new AjaxJson();
		
		String toUserName = request.getParameter("toUserName");
		String url = request.getParameter("url");
		
		WeixinThirdUtil.getInstance().getAuthorizerAccessToken(toUserName);
		
		WeixinAccountEntity  currentWeixinAccount =  weixinAccountService.findByToUsername(toUserName);
		
		j.setObj(Sign.sign(currentWeixinAccount.getJsApiTicket(), url));
		
		
		response.getWriter().write("callback(" + j.getJsonStr() +")");
		//return j;
	}
	public void test(){}
}