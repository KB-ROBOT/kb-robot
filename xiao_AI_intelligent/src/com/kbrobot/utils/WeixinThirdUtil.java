package com.kbrobot.utils;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.rest.entity.WeixinOpenAccountEntity;
import org.jeecgframework.web.system.service.SystemService;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.mp.aes.AesException;
import org.jeewx.api.mp.aes.WXBizMsgCrypt;
import org.jeewx.api.third.JwThirdAPI;
import org.jeewx.api.third.model.ApiAuthorizerToken;
import org.jeewx.api.third.model.ApiAuthorizerTokenRet;
import org.jeewx.api.third.model.ApiComponentToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;

@Controller
public class WeixinThirdUtil {

	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	@Autowired
	private SystemService systemService;

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

}
