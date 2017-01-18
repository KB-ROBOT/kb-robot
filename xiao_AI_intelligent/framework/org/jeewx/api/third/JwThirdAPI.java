package org.jeewx.api.third;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import weixin.guanjia.core.util.WeixinUtil;
import weixin.p3.oauth2.def.WeiXinOpenConstants;

import org.jeewx.api.core.common.WxstoreUtils;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.handler.impl.WeixinReqDefaultHandler;
import org.jeewx.api.third.model.ApiAuthorizerToken;
import org.jeewx.api.third.model.ApiAuthorizerTokenRet;
import org.jeewx.api.third.model.ApiComponentToken;
import org.jeewx.api.third.model.ApiGetAuthorizer;
import org.jeewx.api.third.model.ApiGetAuthorizerRet;
import org.jeewx.api.third.model.GetPreAuthCodeParam;
import org.jeewx.api.third.model.ReOpenAccessToken;
import org.jeewx.api.wxstore.order.model.OrderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kbrobot.utils.WeixinThirdUtil;

/**
 * 微信--token信息
 * 
 * @author lizr
 * 
 */
public class JwThirdAPI {
	private static Logger logger = LoggerFactory.getLogger(JwThirdAPI.class);

	public static final String SNSAPI_USERINFO = "snsapi_userinfo";
	public static final String SNSAPI_BASE = "snsapi_base";

	//获取预授权码
	private static String api_create_preauthcode_url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";
	private static String api_component_token_url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";

	//OAuth2.0换取code
	private static String oauth2_get_code_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE&component_appid=COMPONENT_APPID#wechat_redirect";
	//OAuth2.0通过code换取access_token
	private static String oauth2_get_access_token_bycode_url = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=APPID&code=CODE&grant_type=authorization_code&component_appid=COMPONENT_APPID&component_access_token=COMPONENT_ACCESS_TOKEN";

	private static String api_query_auth_url = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=xxxx";
	//客服接口地址
	public static String send_message_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	//4、获取（刷新）授权公众号的令牌
	private static String api_authorizer_token_url = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=COMPONENT_ACCESS_TOKEN";
	//5、获取授权方的账户信息
	private static String api_get_authorizer_info_url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=COMPONENT_ACCESS_TOKEN";
	//6、获取授权方的选项设置信息
	private static String api_get_authorizer_option_url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_option?component_access_token=COMPONENT_ACCESS_TOKEN";
	//7、设置授权方的选项信息
	private static String api_set_authorizer_option_url = "https://api.weixin.qq.com/cgi-bin/component/api_set_authorizer_option?component_access_token=COMPONENT_ACCESS_TOKEN";
	/**
	 * 1、获取第三方平台access_token
	 * @param appid
	 * @param appscret
	 * @return kY9Y9rfdcr8AEtYZ9gPaRUjIAuJBvXO5ZOnbv2PYFxox__uSUQcqOnaGYN1xc4N1rI7NDCaPm_0ysFYjRVnPwCJHE7v7uF_l1hI6qi6QBsA
	 * @throws WexinReqException
	 */
	public static String getAccessToken(ApiComponentToken apiComponentToken) throws WexinReqException{
		String component_access_token = "";
		String requestUrl = api_component_token_url;
		JSONObject obj = JSONObject.fromObject(apiComponentToken);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", obj.toString());
		if (result.has("errcode")) {
			logger.error("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
			throw new WexinReqException("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
		} else {
			component_access_token = result.getString("component_access_token");
		}
		return component_access_token;
	}

	/**
	 * 2、获取预授权码
	 * @param appid
	 * @param appscret
	 * @return kY9Y9rfdcr8AEtYZ9gPaRUjIAuJBvXO5ZOnbv2PYFxox__uSUQcqOnaGYN1xc4N1rI7NDCaPm_0ysFYjRVnPwCJHE7v7uF_l1hI6qi6QBsA
	 * @throws WexinReqException
	 */
	public static String getPreAuthCode(String component_appid, String component_access_token) throws WexinReqException{
		String pre_auth_code = "";
		String requestUrl = api_create_preauthcode_url.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		GetPreAuthCodeParam getPreAuthCodeParam = new GetPreAuthCodeParam();
		getPreAuthCodeParam.setComponent_appid(component_appid);
		JSONObject obj = JSONObject.fromObject(getPreAuthCodeParam);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", obj.toString());
		if (result.has("errcode")) {
			logger.error("获取权限令牌信息！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
			throw new WexinReqException("获取权限令牌信息！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
		} else {
			pre_auth_code = result.getString("pre_auth_code");
		}
		return pre_auth_code;
	}

	/**
	 * 3、使用授权码换取公众号的授权信息
	 * @param appid
	 * @param appscret
	 * @return kY9Y9rfdcr8AEtYZ9gPaRUjIAuJBvXO5ZOnbv2PYFxox__uSUQcqOnaGYN1xc4N1rI7NDCaPm_0ysFYjRVnPwCJHE7v7uF_l1hI6qi6QBsA
	 * @throws WexinReqException
	 */
	public static JSONObject getApiQueryAuthInfo(String component_appid,String authorization_code,String component_access_token) throws WexinReqException{
		String requestUrl = api_query_auth_url.replace("xxxx", component_access_token);
		Map<String,String> mp = new HashMap<String,String>();
		mp.put("component_appid", component_appid);
		mp.put("authorization_code", authorization_code);
		JSONObject obj = JSONObject.fromObject(mp);
		System.out.println("-------------------3、使用授权码换取公众号的授权信息---requestUrl------------------------"+requestUrl);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl,"POST", obj.toString());
		if (result.has("errcode")) {
			logger.error("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
			throw new WexinReqException("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
		}
		return result;
	}


	/**
	 * 4、获取（刷新）授权公众号的令牌
	 * @param apiAuthorizerToken
	 * @param component_access_token
	 */
	public static ApiAuthorizerTokenRet apiAuthorizerToken(ApiAuthorizerToken apiAuthorizerToken,String component_access_token) throws WexinReqException{
		String requestUrl = api_authorizer_token_url.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		JSONObject param = JSONObject.fromObject(apiAuthorizerToken);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl,"POST", param.toString());
		//JSONObject result = WeixinUtil.httpsRequest(requestUrl,"POST", param.toString());
		ApiAuthorizerTokenRet apiAuthorizerTokenRet = (ApiAuthorizerTokenRet)JSONObject.toBean(result, ApiAuthorizerTokenRet.class);
		if (result.has("errcode")) {
			logger.error("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
			throw new WexinReqException("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
		}
		return apiAuthorizerTokenRet;
	}
	/**
	 * 5、获取授权方的账户信息
	 */
	public static ApiGetAuthorizerRet apiGetAuthorizerInfo(ApiGetAuthorizer apiGetAuthorizer,String component_access_token) throws WexinReqException{
		String requestUrl = api_get_authorizer_info_url.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		JSONObject param = JSONObject.fromObject(apiGetAuthorizer);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl,"POST", param.toString());
		ApiGetAuthorizerRet apiGetAuthorizerRet = (ApiGetAuthorizerRet)JSONObject.toBean(result, ApiGetAuthorizerRet.class);
		if (result.has("errcode")) {
			logger.error("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
			throw new WexinReqException("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
		}
		return apiGetAuthorizerRet;
	}

	/**
	 * 6、获取授权方的选项设置信息
	 */
	public static AuthorizerOptionRet apiGetAuthorizerOption(AuthorizerOption authorizerOption,String component_access_token) throws WexinReqException{
		String requestUrl = api_get_authorizer_option_url.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		JSONObject param = JSONObject.fromObject(authorizerOption);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl,"POST", param.toString());
		AuthorizerOptionRet authorizerOptionRet = (AuthorizerOptionRet)JSONObject.toBean(result, AuthorizerOptionRet.class);
		return authorizerOptionRet;
	}
	/**
	 * 7、设置授权方的选项信息
	 */
	public static AuthorizerSetOptionRet apiSetAuthorizerOption(AuthorizerSetOption authorizerSetOption,String component_access_token) throws WexinReqException{
		String requestUrl = api_set_authorizer_option_url.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
		JSONObject param = JSONObject.fromObject(authorizerSetOption);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl,"POST", param.toString());
		AuthorizerSetOptionRet authorizerSetOptionRet = (AuthorizerSetOptionRet)JSONObject.toBean(result, AuthorizerSetOptionRet.class);
		return authorizerSetOptionRet;
	}
	/**
	 * 微信主动推送给第三方服务器
	 * 8、推送component_verify_ticket协议
	 * 9、推送取消授权通知
	 */


	/**
	 * OAuth2换取code
	 */
	public static String getOAuth2ComponentUrl(String targetUrl, String appid, String scope){
		String shareurl = "";
		String encodeTargetURL = "";

		try {
			encodeTargetURL = URLEncoder.encode(targetUrl, "UTF-8");
		} catch (UnsupportedEncodingException arg5) {
			arg5.printStackTrace();
		}

		shareurl = oauth2_get_code_url.replace("COMPONENT_APPID", WeixinThirdUtil.COMPONENT_APPID)
										.replace("APPID", appid)
										.replace("REDIRECT_URI", encodeTargetURL)
										.replace("SCOPE", scope)
										;
		return shareurl;
	}

	/**
	 * OAuth2 Code换取第三方平台access_token
	 * @param appid
	 * @param appscret
	 * @return kY9Y9rfdcr8AEtYZ9gPaRUjIAuJBvXO5ZOnbv2PYFxox__uSUQcqOnaGYN1xc4N1rI7NDCaPm_0ysFYjRVnPwCJHE7v7uF_l1hI6qi6QBsA
	 * @throws WexinReqException
	 */
	public static ReOpenAccessToken getOAuth2ComponentAccessTokenByCode(String appid,String code,String component_appid,String component_access_token) throws WexinReqException{
		String requestUrl = oauth2_get_access_token_bycode_url.replace("COMPONENT_APPID", component_appid).replace("COMPONENT_ACCESS_TOKEN", component_access_token).replace("CODE", code).replace("APPID", appid);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl,"GET", null);
		ReOpenAccessToken reOpenAccessToken = (ReOpenAccessToken)JSONObject.toBean(result, ReOpenAccessToken.class);
		if (result.has("errcode")) {
			logger.error("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
			throw new WexinReqException("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
		}
		return reOpenAccessToken;
	}

	


	/**
	 * 发送客服消息
	 * @param obj
	 * @param ACCESS_TOKEN
	 * @return
	 */
	public static String sendMessage(Map<String,Object> obj,String ACCESS_TOKEN){
		JSONObject json = JSONObject.fromObject(obj);
		System.out.println("--------发送客服消息---------json-----"+json.toString());
		// 调用接口获取access_token
		String url = send_message_url.replace("ACCESS_TOKEN",ACCESS_TOKEN);
		JSONObject jsonObject = WxstoreUtils.httpRequest(url, "POST", json.toString());
		return jsonObject.toString();
	}


	public static void main(String[] args){

		try {
			//String s = JwThirdAPI.getPreAuthCode("wx5412820bba6f6bd6","unisk");

			ApiComponentToken apiComponentToken = new ApiComponentToken();
			//apiComponentToken.setComponent_appid("wx5412820bba6f6bd6");
			apiComponentToken.setComponent_appid("??");
			apiComponentToken.setComponent_appsecret("???");
			apiComponentToken.setComponent_verify_ticket(null);
			String s = JwThirdAPI.getAccessToken(apiComponentToken);
			System.out.println(s);
		} catch (WexinReqException e) {
			e.printStackTrace();
		}
	}
}
