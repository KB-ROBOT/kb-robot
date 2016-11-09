package com.kbrobot.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.dom4j.DocumentException;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.web.rest.entity.WeixinOpenAccountEntity;
import org.jeecgframework.web.system.service.SystemService;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.mp.aes.AesException;
import org.jeewx.api.mp.aes.WXBizMsgCrypt;
import org.jeewx.api.third.JwThirdAPI;
import org.jeewx.api.third.model.ApiAuthorizerToken;
import org.jeewx.api.third.model.ApiAuthorizerTokenRet;
import org.jeewx.api.third.model.ApiComponentToken;
import org.jeewx.api.wxsendmsg.JwKfaccountAPI;
import org.jeewx.api.wxsendmsg.model.WxKfaccount;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.entity.VoiceTemplate;
import com.kbrobot.entity.system.WeixinConversationContent;
import com.kbrobot.service.RobotQuestionServiceI;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.core.entity.message.resp.Article;
import weixin.guanjia.core.entity.message.resp.BaseMessageResp;
import weixin.guanjia.core.entity.message.resp.NewsMessageResp;
import weixin.guanjia.core.entity.message.resp.TextMessageResp;
import weixin.guanjia.core.util.MessageUtil;
import weixin.guanjia.message.entity.NewsItem;
import weixin.guanjia.message.entity.TextTemplate;

@Controller
public class WeixinThirdUtil {

	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private RobotQuestionServiceI robotQuestionService;
	

	public final static String APPID = "wx520d1bc0926617f0";

	//AppID
	public final static String COMPONENT_APPID = "wx520d1bc0926617f0";
	//AppSecret
	public final static String COMPONENT_APPSECRET = "40b98977374d5f4fba07082a3a596532";
	//公众号消息加解密Key
	public final static String COMPONENT_ENCODINGAESKEY = "66LuuoMFadsH2OYlpTh4JvmKVI3QBMqucO9bW7yYI0n";
	//公众号消息校验Token
	public final static String COMPONENT_TOKEN = "kbrobot";

	WXBizMsgCrypt weixinMsgCrypt = null;

	ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");

	private static WeixinThirdUtil instance;

	private WeixinThirdUtil(){

	}

	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {
		instance = this;
		// 初使化时将已静态化的Service实例化
		instance.weixinAccountService = this.weixinAccountService;
		instance.systemService = this.systemService;
		instance.robotQuestionService = this.robotQuestionService;
	}

	public static WeixinThirdUtil getInstance(){
		if(instance == null){
			synchronized(WeixinThirdUtil.class){
				if(instance == null){
					instance = new WeixinThirdUtil();
				}
			}
		}
		return instance;
	}

	/**
	 * 获取已经保存的WeixinOpenAccountEntity(component_verify_ticket、component_access_token) 
	 * @param appid
	 * @return
	 */
	public WeixinOpenAccountEntity getWeixinOpenAccount(){
		WeixinOpenAccountEntity  entity = null;
		List<WeixinOpenAccountEntity> ls = systemService.findByProperty(WeixinOpenAccountEntity.class, "appid", APPID);
		if(ls!=null && ls.size()!=0){
			entity = ls.get(0);
		}
		return entity;
	}
	/**
	 * 取得componentAccessToken
	 * @throws WexinReqException 
	 */
	public String getComponentAccessToken() throws WexinReqException{
		//取得数据库当前entity
		WeixinOpenAccountEntity  entity = getWeixinOpenAccount();//weixin open account
		String componentAccessToken = entity.getComponentAccessToken();
		Date end = new java.util.Date();
		Date start = entity.getGetAccessTokenTime();
		start = start==null?new Date():start;


		/*
		 * 判断当前componentAccessToken是否存在,并且判断是否超过2小时
		 */
		//如果存在并且没有超时
		//提前刷新 1.83约等于1小时50分钟
		System.out.println((end.getTime() - start.getTime()) / 1000.0f / 3600.0f);
		if (StringUtils.isNotEmpty(componentAccessToken)&&!((end.getTime() - start.getTime()) / 1000.0f / 3600.0f >= 1.83f)) {
			return entity.getComponentAccessToken();
		}
		//不存在或者失效 重新获取
		else{
			LogUtil.info("重新获取 componentAccessToken");
			ApiComponentToken apiComponentToken = new ApiComponentToken();
			apiComponentToken.setComponent_appid(COMPONENT_APPID);
			apiComponentToken.setComponent_appsecret(COMPONENT_APPSECRET);
			apiComponentToken.setComponent_verify_ticket(entity.getComponentVerifyTicket());
			//jeewxAPI
			String component_access_token;
			component_access_token = JwThirdAPI.getAccessToken(apiComponentToken);
			if (null != component_access_token) {
				//设置componentAccessToken
				entity.setComponentAccessToken(component_access_token);
				entity.setGetAccessTokenTime(new Date());
				systemService.saveOrUpdate(entity);
			}
			return entity.getComponentAccessToken();
		}
	}

	/**
	 * 取得 authorizerAccessToken
	 * @param currentWeixinAccount
	 * @return
	 * @throws WexinReqException 
	 */
	public String getAuthorizerAccessToken(String toUserName) throws WexinReqException{

		WeixinAccountEntity  currentWeixinAccount =  weixinAccountService.findByToUsername(toUserName);

		//如果找不到这个微信账户 则返回""
		if(currentWeixinAccount==null||StringUtil.isEmpty(currentWeixinAccount.getId())){
			return "";
		}

		String authorizerAccessToken =currentWeixinAccount.getAuthorizerAccessToken();
		Date end = new java.util.Date();
		Date start = currentWeixinAccount.getAuthorizerAccessTokenTime();
		start = start==null?new Date():start;
		//如果存在authorizerAccessToken并且没有失效
		System.out.println((end.getTime() - start.getTime()) / 1000.0f / 3600.0f);
		if(StringUtils.isNotEmpty(authorizerAccessToken)&&!((end.getTime() - start.getTime()) / 1000.0f / 3600.0f >= 1.83f)){
			return authorizerAccessToken;
		}
		//不存在或者失效 重新获取 authorizerAccessToken
		else{
			LogUtil.info("重新获取 authorizerAccessToken");
			//apiAuthorizerToken
			ApiAuthorizerToken apiAuthorizerToken = new ApiAuthorizerToken();
			apiAuthorizerToken.setComponent_appid(COMPONENT_APPID);
			//授权方appid
			apiAuthorizerToken.setAuthorizer_appid(currentWeixinAccount.getAccountAppid());
			//授权方的刷新令牌
			apiAuthorizerToken.setAuthorizer_refresh_token(currentWeixinAccount.getAuthorizerRefreshToken());

			//刷新令牌返回结果
			ApiAuthorizerTokenRet apiAuthorizerTokenRet = JwThirdAPI.apiAuthorizerToken(apiAuthorizerToken, getComponentAccessToken());

			//更新authorizer_access_token与authorizer_refresh_token
			currentWeixinAccount.setAuthorizerAccessToken(apiAuthorizerTokenRet.getAuthorizer_access_token());
			currentWeixinAccount.setAuthorizerAccessTokenTime(new Date());
			currentWeixinAccount.setAuthorizerRefreshToken(apiAuthorizerTokenRet.getAuthorizer_refresh_token());
			weixinAccountService.saveOrUpdate(currentWeixinAccount);

			return currentWeixinAccount.getAuthorizerAccessToken();
		}
	}
	/**
	 * 微信消息加密
	 * @param replyMessageStr
	 * @param createTime
	 * @return
	 * @throws AesException 
	 */
	/*public String 1encryptReplyMessage(String replyMessageStr,Long createTime){
		String result = "";
		try {
			WXBizMsgCrypt pc = new WXBizMsgCrypt(COMPONENT_TOKEN, COMPONENT_ENCODINGAESKEY, COMPONENT_APPID);
			result =  pc.encryptMsg(replyMessageStr, createTime.toString(), "easemob");
		} catch (AesException e) {
			e.printStackTrace();
		}

		return result;
	}*/

	public WXBizMsgCrypt getWeixinMsgCrypt() throws AesException {
		if(weixinMsgCrypt==null){
			weixinMsgCrypt = new WXBizMsgCrypt(COMPONENT_TOKEN, COMPONENT_ENCODINGAESKEY, COMPONENT_APPID);
		}
		return weixinMsgCrypt;
	}


	/**
	 * 工具：回复微信服务器"消息"
	 * @param response
	 * @param returnvaleue
	 */
	public void output(HttpServletResponse response,String returnvaleue){
		try {
			response.setContentType("text/plain;charset=utf-8");
			PrintWriter pw = response.getWriter();
			pw.append(returnvaleue);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 回复微信服务器"文本消息"
	 * @param request
	 * @param response
	 * @param content
	 * @param toUserName
	 * @param fromUserName
	 * @throws AesException 
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void replyTextMessage(HttpServletRequest request, HttpServletResponse response,String content,String toUserName,String fromUserName) throws AesException{
		TextMessageResp textMessageResp = new TextMessageResp();
		textMessageResp.setFromUserName(toUserName);
		textMessageResp.setToUserName(fromUserName);
		textMessageResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		textMessageResp.setContent(content);
		
		replyTextMessage(request,response,textMessageResp);
	}
	public void replyTextMessage(HttpServletRequest request, HttpServletResponse response, TextMessageResp textMessageResp) throws AesException{
		Long createTime = Calendar.getInstance().getTimeInMillis() / 1000;
		textMessageResp.setCreateTime(createTime);
		String replyMessageStr =  MessageUtil.textMessageToXml(textMessageResp);
		String returnvaleue = "";
		returnvaleue = getWeixinMsgCrypt().encryptMsg(replyMessageStr,createTime.toString(), request.getParameter("nonce"));
		output(response, returnvaleue);
	}


	/**
	 * 回复微信服务器"图文消息"
	 * @param request
	 * @param response
	 * @param newsList
	 * @param toUserName
	 * @param fromUserName
	 * @throws AesException 
	 */
	public void replyNewsMessage(HttpServletRequest request, HttpServletResponse response,List<Article> articleList,String toUserName,String fromUserName) throws AesException{
		NewsMessageResp newsResp = new NewsMessageResp();
		newsResp.setToUserName(fromUserName);
		newsResp.setFromUserName(toUserName);
		newsResp.setArticles(articleList);
		newsResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
		replyNewsMessage(request,response,newsResp);
	}
	public void replyNewsMessage(HttpServletRequest request, HttpServletResponse response,NewsMessageResp newsResp) throws AesException{
		Long createTime = Calendar.getInstance().getTimeInMillis() / 1000;
		newsResp.setCreateTime(createTime);
		newsResp.setArticleCount(newsResp.getArticles().size());
		newsResp.setArticles(newsResp.getArticles());
		String replyMessageStr = MessageUtil.newsMessageToXml(newsResp);
		String returnvaleue = "";
		returnvaleue = getWeixinMsgCrypt().encryptMsg(replyMessageStr,createTime.toString(),request.getParameter("nonce"));
		output(response, returnvaleue);
	}

	/**
	 * 将匹配的结果回复给用户
	 * @param baseMsgResp
	 * @param request
	 * @param response
	 * @throws AesException
	 */
	public WeixinConversationContent replyMatchResult(BaseMessageResp baseMsgResp,HttpServletRequest request, HttpServletResponse response) throws AesException{
		String matchMsgType = baseMsgResp.getMsgType();
		WeixinConversationContent resultConversation = new WeixinConversationContent();
		resultConversation.setResponseType(matchMsgType);
		if(MessageUtil.RESP_MESSAGE_TYPE_TEXT.equals(matchMsgType)){
			TextMessageResp textMessageResp = ((TextMessageResp)baseMsgResp);
			/*
			 * 回复文本消息
			 */
			replyTextMessage(request, response, textMessageResp);
		
			resultConversation.setResponseContent(textMessageResp.getContent());
		}
		else if(MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(matchMsgType)){
			NewsMessageResp newsMessageResp = ((NewsMessageResp)baseMsgResp);
			List<Article> articleList = newsMessageResp.getArticles();
			/*
			 * 回复图文消息
			 */
			replyNewsMessage(request, response,newsMessageResp);
			
			if(!articleList.isEmpty()){
				resultConversation.setResponseContent(articleList.get(0).getUrl());
			}
			else{
				resultConversation.setResponseContent("图文消息为空。");
			}
		}
		else if(MessageUtil.REQ_MESSAGE_TYPE_TRANSFER_CUSTOMER_SERVICE.equals(matchMsgType)){
			Long createTime = Calendar.getInstance().getTimeInMillis() / 1000;
			baseMsgResp.setCreateTime(createTime);
			String replyMessageStr = MessageUtil.baseMessageToXml(baseMsgResp);
			String returnvaleue = "";
			returnvaleue = getWeixinMsgCrypt().encryptMsg(replyMessageStr,createTime.toString(),request.getParameter("nonce"));
			output(response, returnvaleue);
		}
		
		
		return resultConversation;
	}
	
	/**
	 * 回复事件消息
	 * @param request
	 * @param response
	 * @param toUserName
	 * @param fromUserName
	 * @param msgType
	 * @param templateId
	 * @throws AesException
	 * @throws WexinReqException 
	 * @throws JSONException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public void replyEventMessage(HttpServletRequest request, HttpServletResponse response,String toUserName, String fromUserName,String msgType,String templateId) throws AesException, JSONException, WexinReqException, InterruptedException, IOException{
		
		if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {//文本消息
			TextTemplate textTemplate = this.systemService.getEntity(TextTemplate.class, templateId);
			String content = textTemplate.getContent();
			
			replyTextMessage(request, response, content,toUserName,fromUserName);

		}
		else if (MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(msgType)) {//图文消息
			List<NewsItem> newsList = this.systemService.findByProperty(NewsItem.class,"newsTemplate.id", templateId);
			List<Article> articleList = new ArrayList<Article>();
			for (NewsItem news : newsList) {
				Article article = new Article();
				article.setTitle(news.getTitle());
				article.setPicUrl(bundler.getString("domain")+ "/" + news.getImagePath());
				String url = "";
				if (oConvertUtils.isEmpty(news.getUrl())) {
					url = bundler.getString("domain")+ "/newsItemController.do?goContent&id=" + news.getId();
				}
				else {
					url = news.getUrl();
				}
				article.setUrl(url);
				article.setDescription(news.getDescription());
				articleList.add(article);
			}

			replyNewsMessage(request, response,articleList,toUserName,fromUserName);
		}
		else if(MessageUtil.REQ_MESSAGE_TYPE_VOICE.equals(msgType)){//语音消息
			VoiceTemplate textTemplate = this.systemService.getEntity(VoiceTemplate.class, templateId);
			String content = textTemplate.getVoiceText();
			output(response, "");
			CustomServiceUtil.sendCustomServiceVoiceMessage(fromUserName, getAuthorizerAccessToken(toUserName), content);
		}
		else if(MessageUtil.REQ_MESSAGE_TYPE_CUSTOMERSERVICE.equalsIgnoreCase(msgType)){//转接人工
			
			WeixinAccountEntity currentWeixinAccount = weixinAccountService.findByToUsername(toUserName);
			String authorizerAccessToken = getAuthorizerAccessToken(toUserName);
			String responseText = transferCustomerService(authorizerAccessToken, fromUserName, currentWeixinAccount.getId());
			CustomServiceUtil.sendCustomServiceTextMessage(fromUserName, authorizerAccessToken, responseText);
		}

	}
	
	/**
	 * 判断是否能转化数字Integer
	 * @param str
	 * @return
	 */
	public Integer convertString2Number(String str){
		
		str = str.replaceAll("，|。|？|、", "");
		str = str.replaceAll(",|[.]|[?]", "");
		
		try{
			return Integer.valueOf(str);
		}
		catch(Exception e){
			
			if(str.equals("一")){
				return 1;
			}
			else if(str.equals("二")){
				return 2;
			}
			else if(str.equals("三")){
				return 3;
			}
			else if(str.equals("四")){
				return 4;
			}
			else if(str.equals("五")){
				return 5;
			}
			else if(str.equals("六")){
				return 6;
			}
			else if(str.equals("七")){
				return 7;
			}
			else if(str.equals("八")){
				return 8;
			}
			else if(str.equals("九")){
				return 9;
			}
			else if(str.equals("零")){
				return 0;
			}
			else{
				return -1;
			}
			
		}
	}
	
	public void updateQuestionMatchTimes(RobotQuestionEntity entity){
		robotQuestionService.updateRobotQuestionMatchTimes(entity.getId());
	}
	
	
	public String transferCustomerService(String authorizer_access_token,String fromUserName,String accountId){
		
		String responseText = "";
		
		try{
			List<WxKfaccount> wxKfaccountList = JwKfaccountAPI.getAllOnlineKfaccount(authorizer_access_token);
			if(wxKfaccountList.isEmpty()){
				
				String leaveMessageUrl = "<a href=\"" + bundler.getString("domain") + "/commonAdviceController.do?leaveMessagePage&accountId="+accountId+"\">点此链接</a>";
				responseText ="抱歉！现在人工客服不在线。请您稍后再联系或"+leaveMessageUrl+"留言，我们客服人员会在第一时间联系你。";
			}
			else{

				WxKfaccount wxKfaccount = wxKfaccountList.get(RandomUtils.nextInt(wxKfaccountList.size()));
				JwKfaccountAPI.createSession(authorizer_access_token, wxKfaccount.getKf_account(), fromUserName,"有新的客户接入。");
				responseText = "您好！已经成功转接到人工客服。您可以通过语音、文字、图片或者短视频等方式向我们的人工客服反馈您的问题了。";
			}
		}
		catch(Exception e){
			e.printStackTrace();
			String errorMsg = e.getMessage();
			if(errorMsg.contains("65400")){
				responseText = "不好意思，本公众号还未开通人工客服功能。";
			}
			else{
				responseText = "接入失败，请稍后再试。";
			}
		}
		
		return responseText;
	}

}
