package org.jeecgframework.web.rest.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.util.DataUtils;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.web.rest.entity.WeixinOpenAccountEntity;
import org.jeecgframework.web.system.service.SystemService;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.mp.aes.AesException;
import org.jeewx.api.mp.aes.WXBizMsgCrypt;
import org.jeewx.api.third.JwThirdAPI;
import org.jeewx.api.third.model.ApiGetAuthorizer;
import org.jeewx.api.third.model.ApiGetAuthorizerRet;
import org.jeewx.api.third.model.ApiGetAuthorizerRetAuthortionFunc;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.entity.system.WeixinConversationClient;
import com.kbrobot.entity.system.WeixinConversationContent;
import com.kbrobot.manager.WeixinClientManager;
import com.kbrobot.service.RobotQuestionServiceI;
import com.kbrobot.utils.CustomServiceUtil;
import com.kbrobot.utils.LtpUtil;
import com.kbrobot.utils.QuestionMatchUtil;
import com.kbrobot.utils.TuLingUtil;
import com.kbrobot.utils.TuLingUtil.ResultKey;
import com.kbrobot.utils.TuLingUtil.ReturnCode;
import com.kbrobot.utils.WeixinThirdUtil;

import net.sf.json.JSONObject;
import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.base.entity.Subscribe;
import weixin.guanjia.core.entity.message.resp.BaseMessageResp;
import weixin.guanjia.core.entity.message.resp.TextMessageResp;
import weixin.guanjia.core.util.MessageUtil;
import weixin.guanjia.menu.entity.MenuEntity;

/**
 * 微信公众账号第三方平台全网发布源码（java）
 * @author： jeewx开源社区
 * @网址：www.jeewx.com
 * @论坛：www.jeecg.org
 * @date 20150801
 */
@Controller
@RequestMapping("/openwx")
public class OpenwxController {	
	/*
	 * weixin_open_account 表中的主键标识
	 * weixin_open_account表存储10分钟更新一次的component_verify_ticket，
	 * 以及2个小时刷新一次的component_access_token
	 */
	//private final String APPID = "wx520d1bc0926617f0";

	/**
	 * 微信全网测试账号
	 */

	/*//AppID
	private final static String COMPONENT_APPID = "wx520d1bc0926617f0";
	//AppSecret
	private final String COMPONENT_APPSECRET = "40b98977374d5f4fba07082a3a596532";
	//公众号消息加解密Key
	private final static String COMPONENT_ENCODINGAESKEY = "66LuuoMFadsH2OYlpTh4JvmKVI3QBMqucO9bW7yYI0n";
	//公众号消息校验Token
	private final static String COMPONENT_TOKEN = "kbrobot";*/
	@Autowired
	private SystemService systemService;
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	@Autowired
	private RobotQuestionServiceI robotQuestionService;

	WeixinThirdUtil weixinThirdUtilInstance = WeixinThirdUtil.getInstance();

	ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");

	long startTime = System.currentTimeMillis();

	long endTime = System.currentTimeMillis();






	/**
	 * 第一步：授权事件接收，接收一些绑定事件和ticket、component_verify_ticket 返回success
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws AesException
	 * @throws DocumentException
	 */
	@RequestMapping(value = "/event/authorize")
	public void acceptAuthorizeEvent(HttpServletRequest request, HttpServletResponse response) throws IOException, AesException, DocumentException {
		LogUtil.info("微信第三方平台---------微信推送Ticket消息10分钟一次-----------"+ DataUtils.getDataString(DataUtils.yyyymmddhhmmss));
		processAuthorizeEvent(request);
		/**
		 * 微信服务器发送给服务自身的事件推送（如取消授权通知，Ticket推送等）。
		 * 此时，消息XML体中没有ToUserName字段，而是AppId字段，即公众号服务的AppId。\
		 * 这种系统事件推送通知（现在包括推送component_verify_ticket协议和推送取消授权通知），
		 * 服务开发者收到后也需进行解密，接收到后只需直接返回字符串“success”
		 */
		weixinThirdUtilInstance.output(response, "success");
	}

	/**
	 * 第二步：处理授权事件的推送 
	 * @param request
	 * @throws IOException
	 * @throws AesException
	 * @throws DocumentException
	 */
	public void processAuthorizeEvent(HttpServletRequest request) throws IOException, DocumentException, AesException {
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");
		String signature = request.getParameter("signature");
		String msgSignature = request.getParameter("msg_signature");

		if (!StringUtils.isNotBlank(msgSignature)){
			LogUtil.info("msgSignature is blank");
		}

		boolean isValid = checkSignature(WeixinThirdUtil.COMPONENT_TOKEN, signature, timestamp, nonce);
		if (isValid) {
			StringBuilder sb = new StringBuilder();
			BufferedReader in = request.getReader();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			String xml = sb.toString();
			LogUtil.info("第三方平台全网发布-----------------------原始 Xml="+xml);
			String encodingAesKey = WeixinThirdUtil.COMPONENT_ENCODINGAESKEY;// 第三方平台组件加密密钥
			String appId = getAuthorizerAppidFromXml(xml);// 此时加密的xml数据中ToUserName(推送消息可能是appid)是非加密的，解析xml获取即可
			LogUtil.info("第三方平台全网发布-------------appid----------getAuthorizerAppidFromXml(xml)-----------appId="+appId);
			WXBizMsgCrypt pc = new WXBizMsgCrypt(WeixinThirdUtil.COMPONENT_TOKEN, encodingAesKey, WeixinThirdUtil.COMPONENT_APPID);
			xml = pc.decryptMsg(msgSignature, timestamp, nonce, xml);
			LogUtil.info("第三方平台全网发布-----------------------解密后 Xml="+xml);
			//保存component_verify_ticket
			processAuthorizationEvent(xml);
		}
	}

	/**
	 * 第三步：保存Ticket
	 * @param xml
	 */
	void processAuthorizationEvent(String xml){
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement();
			String ticket = rootElt.elementText("ComponentVerifyTicket");
			if(oConvertUtils.isNotEmpty(ticket)){
				LogUtil.info("8、推送component_verify_ticket协议-----------ticket = "+ticket);
				saveComponentVerifyTicket(ticket);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 第四步：一键授权功能 （跳转到一键绑定界面）
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws AesException
	 * @throws DocumentException
	 */
	@RequestMapping(value = "/goAuthor")
	public void goAuthor(HttpServletRequest request, HttpServletResponse response) throws IOException, AesException, DocumentException {
		try {
			String component_access_token = weixinThirdUtilInstance.getComponentAccessToken();
			//预授权码
			String preAuthCode = JwThirdAPI.getPreAuthCode(WeixinThirdUtil.COMPONENT_APPID, component_access_token);
			String url = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid="+WeixinThirdUtil.COMPONENT_APPID+"&pre_auth_code="+preAuthCode+"&redirect_uri="+ResourceUtil.getConfigByName("domain")+"/openwx/authorCallback.do?sysUserName="+ResourceUtil.getSessionUserName().getUserName();
			response.sendRedirect(url);
		}
		catch (WexinReqException e) {
			e.printStackTrace();
			weixinThirdUtilInstance.output(response, "访问出错，请10分钟后再试！");
		}
	}

	/**
	 * 第五步：授权回调，并保存账号
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws AesException
	 * @throws DocumentException
	 * @throws WexinReqException 
	 */
	@RequestMapping(value = "/authorCallback")
	public ModelAndView authorCallback(HttpServletRequest request, HttpServletResponse response) throws IOException{

		try{
			String authorization_code = request.getParameter("auth_code");
			//String expires_in = request.getParameter("expires_in");//authorization_code的过期时间
			String sysUserName = request.getParameter("sysUserName");
			String component_access_token = weixinThirdUtilInstance.getComponentAccessToken();
			/*
			 * 使用授权码换取公众号的接口调用凭据和授权信息
			 */
			JSONObject result = JwThirdAPI.getApiQueryAuthInfo(WeixinThirdUtil.COMPONENT_APPID, authorization_code, component_access_token);
			//先获取到authorization_info
			JSONObject authorization_info = result.getJSONObject("authorization_info");
			//授权方appid
			String authorizer_appid = authorization_info.optString("authorizer_appid");
			//授权方接口调用凭据
			String authorizer_access_token = authorization_info.optString("authorizer_access_token");
			//接口调用凭据刷新令牌
			String authorizer_refresh_token = authorization_info.optString("authorizer_refresh_token");

			/*
			 * 获取授权方的公众号帐号基本信息
			 */
			ApiGetAuthorizer apiGetAuthorizer = new ApiGetAuthorizer();
			apiGetAuthorizer.setAuthorizer_appid(authorizer_appid);
			apiGetAuthorizer.setComponent_appid(WeixinThirdUtil.COMPONENT_APPID);
			ApiGetAuthorizerRet authorizerInfo = JwThirdAPI.apiGetAuthorizerInfo(apiGetAuthorizer, component_access_token);
			/*
			 * 把接入的微信公众号插入数据库
			 */
			WeixinAccountEntity weixinAccountEntity = null;

			/*
			 * 查找当前接入微信是否已经存在
			 */
			WeixinAccountEntity exitWeixinAccount = weixinAccountService.findUniqueByProperty(WeixinAccountEntity.class, "weixinAccountId", authorizerInfo.getAuthorizer_info().getUser_name());
			if(exitWeixinAccount!=null){
				weixinAccountEntity = exitWeixinAccount;
			}
			else{
				weixinAccountEntity = new WeixinAccountEntity();
			}
			//appid
			weixinAccountEntity.setAccountAppid(authorizerInfo.getAuthorization_info().getAuthorizer_appid());
			//公众号名称
			weixinAccountEntity.setAccountName(authorizerInfo.getAuthorizer_info().getNick_name());
			//账号type公众号类型 0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
			weixinAccountEntity.setAccountType(authorizerInfo.getAuthorizer_info().getService_type_info().getId().toString());
			//公众号所属用户
			weixinAccountEntity.setUserName(sysUserName);
			//公众号原始ID
			weixinAccountEntity.setWeixinAccountId(authorizerInfo.getAuthorizer_info().getUser_name());
			//授权方接口调用凭据
			weixinAccountEntity.setAuthorizerAccessToken(authorizer_access_token);
			//授权方接口调用凭据获取时间
			weixinAccountEntity.setAuthorizerAccessTokenTime(new Date());
			//接口调用凭据刷新令牌
			weixinAccountEntity.setAuthorizerRefreshToken(authorizer_refresh_token);
			//接入类型 0：扫一扫接入。1：配置方式接入
			weixinAccountEntity.setAccountAuthorizeType("0");
			//授权方认证类型，
			//-1代表未认证，
			//0代表微信认证，
			//1代表新浪微博认证，
			//2代表腾讯微博认证，
			//3代表已资质认证通过但还未通过名称认证，
			//4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，
			//5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
			weixinAccountEntity.setVerifyTypeInfo(authorizerInfo.getAuthorizer_info().getVerify_type_info().getId().toString());
			//头像
			weixinAccountEntity.setHeadImg(authorizerInfo.getAuthorizer_info().getHead_img());
			//二维码
			weixinAccountEntity.setQrcodeUrl(authorizerInfo.getAuthorizer_info().getQrcode_url());
			//授权方公众号所设置的微信号
			weixinAccountEntity.setAccountNumber(authorizerInfo.getAuthorizer_info().getAlias());
			//公众号授权给开发者的权限集列表
			//List<ApiGetAuthorizerRetAuthortionFunc> funcInfoList =  authorizerInfo.getAuthorization_info().getFunc_info();
			ApiGetAuthorizerRetAuthortionFunc[] funcInfoList =  authorizerInfo.getAuthorization_info().getFunc_info();
			String funcInfoStr = "";
			for(int i=0;i<funcInfoList.length;i+=1){
				funcInfoStr += funcInfoList[i].getFuncscope_category().getId().toString();
				funcInfoStr += ",";
			}
			funcInfoStr = funcInfoStr.substring(0, funcInfoStr.lastIndexOf(','));
			weixinAccountEntity.setFuncInfo(funcInfoStr);




			/*
			 * 保存或更新接入的公众号
			 */
			weixinAccountService.saveOrUpdate(weixinAccountEntity);

			return new ModelAndView(new RedirectView(ResourceUtil.getConfigByName("domain") + "/robotBindController.do?bindweixinsuccess"));
		}
		catch(Exception e){
			return new ModelAndView(new RedirectView(ResourceUtil.getConfigByName("domain") + "/robotBindController.do?bindweixinerror"));
		}

	}

	/**
	 * 公众号消息与事件接收URL
	 */
	@RequestMapping(value = "{appid}/callback")
	public void acceptMessageAndEvent(HttpServletRequest request, HttpServletResponse response) throws IOException, AesException, DocumentException, WexinReqException, JSONException {
		startTime = System.currentTimeMillis();

		String msgSignature = request.getParameter("msg_signature");
		if (!StringUtils.isNotBlank(msgSignature))
			return;// 微信推送给第三方开放平台的消息一定是加过密的，无消息加密无法解密消息

		StringBuilder sb = new StringBuilder();
		BufferedReader in = request.getReader();
		String line;
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}
		in.close();

		String xml = sb.toString();
		Document doc = DocumentHelper.parseText(xml);
		Element rootElt = doc.getRootElement();
		String toUserName = rootElt.elementText("ToUserName");


		//获取 authorizer_access_token
		String authorizer_access_token = weixinThirdUtilInstance.getAuthorizerAccessToken(toUserName);

		LogUtil.info("消息反馈---------------APPID="+ WeixinThirdUtil.APPID +"------------------------toUserName="+toUserName);
		checkWeixinAllNetworkCheck(request,response,xml,authorizer_access_token);
	}

	/**
	 * 保存componentVerifyTicket
	 * @param componentVerifyTicket
	 */
	void saveComponentVerifyTicket(String componentVerifyTicket){
		WeixinOpenAccountEntity  entity = weixinThirdUtilInstance.getWeixinOpenAccount();
		entity = entity==null?new WeixinOpenAccountEntity():entity;
		entity.setComponentVerifyTicket(componentVerifyTicket);
		entity.setAppid(WeixinThirdUtil.APPID);
		entity.setGetTicketTime(new Date());
		systemService.saveOrUpdate(entity);
	}

	/**
	 * 获取授权的Appid
	 * @param xml
	 * @return
	 */
	String getAuthorizerAppidFromXml(String xml) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xml);
			Element rootElt = doc.getRootElement();
			String toUserName = rootElt.elementText("ToUserName");
			return toUserName;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}


	public void checkWeixinAllNetworkCheck(HttpServletRequest request, HttpServletResponse response,String xml,String authorizer_access_token) throws DocumentException, IOException, AesException, JSONException, WexinReqException{
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");
		String msgSignature = request.getParameter("msg_signature");

		xml = weixinThirdUtilInstance.getWeixinMsgCrypt().decryptMsg(msgSignature, timestamp, nonce, xml);

		Document doc = DocumentHelper.parseText(xml);
		Element rootElt = doc.getRootElement();
		String msgType = rootElt.elementText("MsgType");
		String toUserName = rootElt.elementText("ToUserName");
		String fromUserName = rootElt.elementText("FromUserName");



		String content = null;
		String msgId = null;

		//如果不是event消息，则获取msgId	
		if(!"event".equals(msgType)){
			msgId = rootElt.elementText("MsgId");
		}

		//LogUtil.info("---全网发布接入检测--step.1-----------msgType="+msgType+"-----------------toUserName="+toUserName+"-----------------fromUserName="+fromUserName);
		LogUtil.info("---微信消息或事件推送-----------xml=\n"+xml);
		if("event".equals(msgType)){
			LogUtil.info("--- ----------事件消息--------");
			processEventMessage(request,response,rootElt,toUserName,fromUserName,authorizer_access_token);
		}
		else if("text".equals(msgType)){
			LogUtil.info("--- ----------文本消息--------");
			content = rootElt.elementText("Content");
			processTextMessage(request,response,content,toUserName,fromUserName,msgType,msgId,Long.valueOf(timestamp),authorizer_access_token);
		}
		else if("voice".equals(msgType)){
			//Recognition 语音解析结果
			content = rootElt.elementText("Recognition");
			if(StringUtil.isEmpty(content)){
				weixinThirdUtilInstance.replyTextMessage(request,response,"网络有点不好哦~\n试试打字吧",toUserName,fromUserName);
			}
			else{
				processTextMessage(request,response,content,toUserName,fromUserName,msgType,msgId,Long.valueOf(timestamp),authorizer_access_token);
			}
		}
	}

	/**
	 * 处理事件消息
	 * @param request
	 * @param response
	 * @param rootElt
	 * @param toUserName
	 * @param fromUserName
	 * @param authorizer_access_token
	 * @throws AesException
	 */
	public void processEventMessage(HttpServletRequest request, HttpServletResponse response,Element rootElt,String toUserName, String fromUserName,String authorizer_access_token) throws AesException{
		String eventType = rootElt.elementText("Event");

		WeixinAccountEntity  currentWeixinAccount =  weixinAccountService.findByToUsername(toUserName);
		/*
		 *  订阅
		 */
		if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
			Subscribe subscribe = systemService.findUniqueByProperty(Subscribe.class, "accountid", currentWeixinAccount.getId());
			if(subscribe!=null){
				String type = subscribe.getMsgType();
				String templateId = subscribe.getTemplateId();
				weixinThirdUtilInstance.replyEventMessage(request,response,toUserName,fromUserName,type,templateId);
			}
		}
		/*
		 *  取消订阅
		 */
		else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
			//取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
			LogUtil.info("又有人取消订阅了。 公众号：" + currentWeixinAccount.getAccountName());
		}
		/*
		 *  菜单 click类型
		 */
		else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
			String eventKey = rootElt.elementText("EventKey");

			//事件KEY值，与自定义菜单接口中KEY值对应
			MenuEntity clickMenu = this.systemService.findUniqueByProperty(MenuEntity.class, "menuKey", eventKey);

			String templateId = clickMenu.getTemplateId();
			String msgType = clickMenu.getMsgType();

			weixinThirdUtilInstance.replyEventMessage(request,response,toUserName,fromUserName,msgType,templateId);
		}
		/*
		 *  获取地理位置
		 */
		else if(eventType.equals(MessageUtil.EVENT_TYPE_LOCATION)){
			//地理位置纬度
			String latitude =  rootElt.elementText("Latitude");
			//地理位置经度
			String longitude = rootElt.elementText("Longitude");
			//地理位置精度
			String Precision = rootElt.elementText("Precision");
			
			LogUtil.info("经度：" + longitude + "\n纬度：" + latitude + "\n精确度" + Precision);
			
			weixinThirdUtilInstance.output(response, "success");
		}
	}

	/**
	 * 处理用户发来的信息
	 * @param request
	 * @param response
	 * @param content
	 * @param toUserName
	 * @param fromUserName
	 * @param msgType
	 * @param msgId
	 * @param authorizer_access_token
	 * @throws IOException
	 * @throws DocumentException
	 * @throws JSONException
	 * @throws WexinReqException
	 * @throws AesException
	 */
	public void processTextMessage(HttpServletRequest request, 
			HttpServletResponse response,
			String content,
			String toUserName, 
			String fromUserName,
			String msgType,
			String msgId,
			Long timestamp,
			String authorizer_access_token) throws IOException, DocumentException, JSONException, WexinReqException, AesException{


		//保存接受到的信息，并保存会话实例（如果是新会话）
		WeixinConversationContent weixinConversationContent = new WeixinConversationContent();
		weixinConversationContent.setMsgContent(content);
		//Timestamp temp = Timestamp.valueOf(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		weixinConversationContent.setReceiveTime(new Date());
		weixinConversationContent.setFromUsername(fromUserName);
		weixinConversationContent.setToUsername(toUserName);
		weixinConversationContent.setMsgId(msgId);
		weixinConversationContent.setMsgType(msgType);

		String receivedMsgId = this.systemService.save(weixinConversationContent).toString();

		//获取微信客户队列实例
		WeixinClientManager weixinClientManager =  WeixinClientManager.instance;
		//获取到当前微信客户的上下文 key为：用户openid + 公众号原始id
		WeixinConversationClient currentClient =  weixinClientManager.getWeixinConversationClient(fromUserName+":"+toUserName);
		//如果不存在 添加进入当前微信客户队列
		if(currentClient==null){
			currentClient = new WeixinConversationClient();
			currentClient.setAddDate(new Date());
			currentClient.setOpenId(fromUserName);
			currentClient.setWeixinAccountId(toUserName);
			currentClient.setStartMessageId(receivedMsgId);
			weixinClientManager.addWeixinConversationClient(fromUserName+":"+toUserName, currentClient);
		}
		currentClient.setEndMessageId(receivedMsgId);
		currentClient.updateAddDate();


		if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)){
			CustomServiceUtil.sendCustomServiceTextMessage(fromUserName, authorizer_access_token, "听到你说：" + new String(content));
		}

		WeixinConversationContent returnConversationContent = null;

		//如果正在会话中（正在回答问题列表）
		int questionIndex = -1;
		if(currentClient.questionListIsNotEmpty()&&( questionIndex = weixinThirdUtilInstance.convertString2Number(content.trim() ))!=-1){

			//找出对应的问题
			//int questionIndex = Integer.valueOf(content) - 1;
			RobotQuestionEntity selectQuestion =  currentClient.getLastQuestionList().get(questionIndex - 1);
			//根据找到的问题 转换成  MessageResp
			BaseMessageResp baseMsgResp = QuestionMatchUtil.matchResultConvert(selectQuestion, toUserName,fromUserName);
			returnConversationContent = weixinThirdUtilInstance.replyMatchResult(baseMsgResp, request, response);

			//清空问题列表
			currentClient.clearAllQuestion();

		}
		//否则正常逻辑处理消息
		else{
			WeixinAccountEntity  currentWeixinAccount =  weixinAccountService.findByToUsername(toUserName);
			//关键词提取
			String[] likeStringList =  LtpUtil.getKeyWordArray(content);
			if(likeStringList==null){
				likeStringList = new String[]{};
			}

			System.out.println("关键词长度：" + likeStringList.length);

			//问题过滤（太多会造成超时）
			CriteriaQuery cq = new CriteriaQuery(RobotQuestionEntity.class);
			cq.eq("accoundId", currentWeixinAccount.getId());
			//添加关键词like条件
			for(String keyWord: likeStringList){
				cq.like("questionTitle", keyWord);
				LogUtil.info("提取出来的keyWord:" + keyWord);
			}
			cq.add();
			List<RobotQuestionEntity> filterQuestionList = robotQuestionService.getListByCriteriaQuery(cq, false);
			LogUtil.info("查询到的条数:" + (filterQuestionList==null?0:filterQuestionList.size()));

			//匹配知识库
			List<RobotQuestionEntity> matchResult = QuestionMatchUtil.matchQuestion(filterQuestionList, content);
			//若匹配不为空
			if(!filterQuestionList.isEmpty()){
				if(matchResult==null||matchResult.isEmpty()){
					//根据找到的问题 转换成  MessageResp
					matchResult = filterQuestionList.subList(0, filterQuestionList.size()>=5?5:filterQuestionList.size());
				}
				BaseMessageResp baseMsgResp = QuestionMatchUtil.matchResultConvert(matchResult, toUserName,fromUserName);
				returnConversationContent = weixinThirdUtilInstance.replyMatchResult(baseMsgResp, request, response);

			}
			//若为空则调用图灵api
			else{
				TextMessageResp textMessageResp = new TextMessageResp();
				Map<ResultKey,Object> tulingResult = TuLingUtil.getResultBySpeakStr(content,fromUserName);//把询问人的id作为会话ID
				if(tulingResult.get(ResultKey.resultType)==ReturnCode.TEXT){//文本
					String resultText = tulingResult.get(ResultKey.text).toString();//文本回复
					textMessageResp.setCreateTime(new Date().getTime());
					textMessageResp.setFromUserName(toUserName);
					textMessageResp.setToUserName(fromUserName);
					textMessageResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
					textMessageResp.setContent(resultText);
				}

				//图灵 如果是语音 则返回语音
				if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)){
					CustomServiceUtil.sendCustomServiceVoiceMessage(fromUserName, authorizer_access_token, textMessageResp.getContent());

					returnConversationContent = new WeixinConversationContent();
					returnConversationContent.setResponseContent(textMessageResp.getContent());
					returnConversationContent.setResponseType(MessageUtil.REQ_MESSAGE_TYPE_VOICE);
				}
				else{
					//weixinThirdUtilInstance.replyTextMessage(request, response, resultText,toUserName,fromUserName);
					returnConversationContent = weixinThirdUtilInstance.replyMatchResult(textMessageResp, request, response);
				}
				/*else if(tulingResult.get(ResultKey.resultType)==ReturnCode.LINK){//链接类型
					String link = "<a href='"+tulingResult.get(ResultKey.url).toString()+"'>"+tulingResult.get(ResultKey.text).toString()+"</a>";
					resultText = link;
				}
				else if(tulingResult.get(ResultKey.resultType)==ReturnCode.NEWS){//新闻类型
					@SuppressWarnings("unchecked")
					List<Article> articleList = (List<Article>) tulingResult.get(ResultKey.list);
					//图文回复
					weixinThirdUtilInstance.replyNewsMessage(request, response, articleList,toUserName,fromUserName);
					resultText = "";
				}
				else if(tulingResult.get(ResultKey.resultType)==ReturnCode.ERROR){
					resultText = tulingResult.get(ResultKey.text).toString();
				}*/
				//如果返回消息不为空则返回文本
				/*if(StringUtil.isNotEmpty(resultText)){

				}*/
			}
		}

		weixinConversationContent.setResponseContent(returnConversationContent.getResponseContent());
		weixinConversationContent.setResponseType(returnConversationContent.getResponseType());
		weixinConversationContent.setReplyTime(new Date());
		this.systemService.saveOrUpdate(weixinConversationContent);

	}


	/**
	 * 判断是否加密
	 * @param token
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String token,String signature,String timestamp,String nonce){
		System.out.println("###token:"+token+";signature:"+signature+";timestamp:"+timestamp+"nonce:"+nonce);
		boolean flag = false;
		if(signature!=null && !signature.equals("") && timestamp!=null && !timestamp.equals("") && nonce!=null && !nonce.equals("")){
			String sha1 = "";
			String[] ss = new String[] { token, timestamp, nonce }; 
			Arrays.sort(ss);  
			for (String s : ss) {  
				sha1 += s;  
			}  
			sha1 = AddSHA1.SHA1(sha1);

			if (sha1.equals(signature)){
				flag = true;
			}
		}
		return flag;
	}
}


class AddSHA1 {
	public static String SHA1(String inStr) {
		MessageDigest md = null;
		String outStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");     //选择SHA-1，也可以选择MD5
			byte[] digest = md.digest(inStr.getBytes());       //返回的是byet[]，要转化为String存储比较方便
			outStr = bytetoString(digest);
		}
		catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return outStr;
	}
	public static String bytetoString(byte[] digest) {
		String str = "";
		String tempStr = "";

		for (int i = 0; i < digest.length; i++) {
			tempStr = (Integer.toHexString(digest[i] & 0xff));
			if (tempStr.length() == 1) {
				str = str + "0" + tempStr;
			}
			else {
				str = str + tempStr;
			}
		}
		return str.toLowerCase();
	}
}
