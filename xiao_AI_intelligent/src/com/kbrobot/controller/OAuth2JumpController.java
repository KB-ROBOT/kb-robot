package com.kbrobot.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.third.JwThirdAPI;
import org.jeewx.api.third.model.ReOpenAccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kbrobot.utils.WeixinThirdUtil;

/**
 * OAuth2.0跳转
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/oAuth2JumpController")
public class OAuth2JumpController {

	@RequestMapping(params = {"redirectTargetUrl","redirectTargetUrl="})
	public String jumpTargetRedirectUrl(HttpServletRequest request) throws UnsupportedEncodingException{

		String code = request.getParameter("code");
		code = code==null?"":code;
		
		String appid = request.getParameter("appid");
		appid = appid==null?"":appid;

		String targetRedirectUri = request.getParameter("targetRedirectUrl");
		
		ReOpenAccessToken reOpenAccessTokenEntity = null;
		try {

			reOpenAccessTokenEntity = JwThirdAPI.getOAuth2ComponentAccessTokenByCode(appid, code, WeixinThirdUtil.COMPONENT_APPID, WeixinThirdUtil.getInstance().getComponentAccessToken());
		}
		catch (WexinReqException e) {
			reOpenAccessTokenEntity = new ReOpenAccessToken();
			e.printStackTrace();
		}
		
		String targetOpenId = reOpenAccessTokenEntity.getOpenid();
		targetOpenId = targetOpenId==null?"":targetOpenId;
		
		return "redirect:"+ URLDecoder.decode(targetRedirectUri,"UTF-8").replaceAll("OPENID", targetOpenId);
	}
	
	@RequestMapping(params = "generateOAuth2Url")
	@ResponseBody
	public AjaxJson generateOAuth2Url(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		
		String redirectUri = request.getParameter("redirectUrl");
		
		try {
			redirectUri = URLDecoder.decode(redirectUri,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String appid = request.getParameter("appid");
		String oauth2Url = JwThirdAPI.getOAuth2ComponentUrl(redirectUri, appid, JwThirdAPI.SNSAPI_BASE);
		
		j.setObj(oauth2Url);
		
		return j;
	}
}
