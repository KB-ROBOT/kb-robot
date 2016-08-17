package com.kbrobot.utils;

import java.io.IOException;
import java.net.URLEncoder;

import org.jeecgframework.core.util.ResourceUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weixin.guanjia.core.util.WeixinUtil;

/**
 * 讯飞语言云工具
 * @author 刘维
 *
 */
public class LtpUtil{
	public static final String LTP_API_KEY = ResourceUtil.getConfigByName("xunfei_ltp_api_key");
	//调用方式为GET
	public static final String URL = "http://ltpapi.voicecloud.cn/analysis/?api_key="+LTP_API_KEY+"&text=INPUT_TEXT&pattern=all&format=json";
	/**
	 * 得到json数据
	 * @param str
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject getLTPResultByStr(String str) throws IOException, JSONException{
		return  new JSONObject(WeixinUtil.httpRequest(URL.replaceAll("INPUT_TEXT", str), "GET", null).toString());
	}
	/**
	 * 获取分词list
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static String[] getWordList(JSONObject json) throws JSONException{

		JSONArray jsonArray = json.getJSONArray("root");
		String[] resultArray = new String[jsonArray.length()];
		for(int i=0;i<jsonArray.length();i++){
			resultArray[i] = jsonArray.getJSONObject(i).optString("cont");
		}
		return resultArray;
	}
	/**
	 * 直接通过字符串获得分词list
	 * @param text
	 * 
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public static String[] getWordList(String text) throws JSONException, IOException{
		//
		return getWordList(getLTPResultByStr(URLEncoder.encode(text, "utf-8")));
	}

}
