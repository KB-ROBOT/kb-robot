package com.kbrobot.utils;

import org.json.JSONException;
import org.json.JSONObject;

import weixin.guanjia.core.util.WeixinUtil;

/**
 * 
 * @author lw
 * 布丁聊天库
 *
 */
public class RooboUtil {
	private static String ROOBO_URL = "http://ros-ai03.roobo.net/v1/query";
	private static String ROOBO_TOKEN = "8a883532b7bdf9fa5cdfaf0311c00d72";
	private static String ROOBO_AGENT_ID = "77";
	
	/**
	 * post body：json格式
{
    “query”: “你好”,
    “sessionId”:”123456”,
    “token”:”8a883532b7bdf9fa5cdfaf0311c00d72”,
    “agentId”:”77”
}
	 * @param speak
	 * @param sessionId
	 * @return
	 * @throws JSONException 
	 */
	public static String getRooboResultBySpeakStr(String speak,String sessionId){
		JSONObject obj = new JSONObject();
		String resultStr = "";
		try {
			obj.put("query", speak);
			obj.put("sessionId", sessionId);
			obj.put("token", ROOBO_TOKEN);
			obj.put("agentId", ROOBO_AGENT_ID);
			
			String resultJson = WeixinUtil.httpRequest(ROOBO_URL, "POST", obj.toString()).toString();
			System.out.println(resultJson);
			JSONObject jsonResult = new JSONObject(resultJson);
			resultStr = jsonResult.getJSONObject("result").optString("hint");
		} catch (JSONException e) {
			e.printStackTrace();
			resultStr = "别问我，我也不知道。";
		}
		resultStr = resultStr.replaceAll("布丁", "小AI");
		return resultStr;
	}
}
