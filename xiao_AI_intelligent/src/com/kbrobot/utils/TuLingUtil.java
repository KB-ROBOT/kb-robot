package com.kbrobot.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.util.LogUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weixin.guanjia.core.entity.message.resp.Article;
import weixin.guanjia.core.util.WeixinUtil;

public class TuLingUtil {
	private static String API_KEY = "37c8bd4757c66bf58f4090a8eee2772d";
	private static String API_URL = "http://www.tuling123.com/openapi/api";

	public enum ReturnCode {
		TEXT("100000"),
		LINK("200000"),
		NEWS("302000"),
		COOK_BOOK("308000"),
		ERROR("0");

		private String code ;

		ReturnCode(String code){
			this.code = code;
		}

		public String getCode(){
			return this.code;
		}

	};
	public enum ResultKey{
		resultType,text,url,list
	}

	public static Map<ResultKey,Object> getResultBySpeakStr(String speakStr,String userId) throws JSONException{

		JSONObject obj = new JSONObject();
		obj.put("key", API_KEY);	
		obj.put("info", speakStr==null?"":speakStr.trim());
		obj.put("userid", userId);
		LogUtil.info(obj.toString());
		String resultJson = WeixinUtil.httpRequest(API_URL, "POST", obj.toString()).toString();

		Map<ResultKey,Object> resultMap = new HashMap<ResultKey,Object>();
		JSONObject jsonObj = new JSONObject(resultJson);

		System.out.println(jsonObj.toString());

		String code = jsonObj.optString("code").trim();

		EnumSet<ReturnCode> codeSet = EnumSet.allOf(ReturnCode.class);

		List<Article> articleList = null;
		JSONArray newList = null;

		for(ReturnCode returnCode : codeSet){

			if(returnCode.code.equals(code)){
				switch(returnCode){
				case TEXT://文本类
					resultMap.put(ResultKey.resultType, ReturnCode.TEXT);
					resultMap.put(ResultKey.text, jsonObj.optString("text"));
					break;
				case LINK://链接类
					resultMap.put(ResultKey.resultType, ReturnCode.LINK);
					resultMap.put(ResultKey.text, jsonObj.optString("text"));
					resultMap.put(ResultKey.url, jsonObj.optString("url"));
					break;
				case NEWS://新闻类
					resultMap.put(ResultKey.resultType, ReturnCode.NEWS);
					resultMap.put(ResultKey.text, jsonObj.optString("text"));
					articleList = new ArrayList<Article>();
					newList =  jsonObj.getJSONArray("list");
					for(int i=0;i<newList.length();i++){
						Article article = new Article();

						JSONObject listObj =  newList.getJSONObject(i);
						article.setUrl(listObj.getString("detailurl"));
						article.setTitle(listObj.getString("article"));
						article.setDescription(listObj.getString("source"));
						article.setPicUrl(listObj.getString("icon"));
						articleList.add(article);
					}
					resultMap.put(ResultKey.list, articleList);
					break;
				case COOK_BOOK:
					resultMap.put(ResultKey.resultType, ReturnCode.COOK_BOOK);
					resultMap.put(ResultKey.text, jsonObj.optString("text"));
					articleList = new ArrayList<Article>();
					newList =  jsonObj.getJSONArray("list");

					for(int i=0;i<newList.length();i++){
						Article article = new Article();

						JSONObject listObj =  newList.getJSONObject(i);
						article.setUrl(listObj.getString("detailurl"));
						article.setTitle(listObj.getString("name"));
						article.setDescription(listObj.getString("info"));
						article.setPicUrl(listObj.getString("icon"));
						articleList.add(article);
					}
					resultMap.put(ResultKey.list, articleList);
					break;
				default:
					resultMap.put(ResultKey.resultType, ReturnCode.ERROR);
					resultMap.put(ResultKey.text, jsonObj.optString("text"));
					break;
				}
				if(resultMap.get("resultType")!=null){
					break;
				}
			}
		}
		return resultMap;
	}
}
