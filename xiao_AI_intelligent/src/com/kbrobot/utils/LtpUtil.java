package com.kbrobot.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		str = str.replaceAll("，|。|？|、|！|“|”", "");
		str = str.replaceAll("[.]|[?]", "");
		return  new JSONObject(WeixinUtil.httpRequest(URL.replaceAll("INPUT_TEXT", URLEncoder.encode(str.replaceAll(" ", ""), "utf-8")), "GET", null).toString());
	}
	/**
	 * 获取分词list
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static String[] getWordList(JSONObject json) throws JSONException{

		JSONArray jsonArray = json.getJSONArray("root");
		String[] resultArray = new String[]{};
		List<String> resultList = new ArrayList<String>();
		for(int i=0;i<jsonArray.length();i++){
			JSONArray contArray = jsonArray.getJSONArray(i);
			for(int j=0;j<contArray.length();j++){
				resultList.add(contArray.getJSONObject(j).optString("cont"));
			}
		}
		return resultList.toArray(resultArray);
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
		return getWordList(getLTPResultByStr(text));
	}
	
	
	/**
	 * 获取分词结果
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static String getWordSplit(JSONObject json) throws JSONException{
		
		JSONArray jsonArray = json.getJSONArray("root");
		String result = "";
		for(int i=0;i<jsonArray.length();i++){
			JSONArray contArray = jsonArray.getJSONArray(i);
			for(int j=0;j<contArray.length();j++){
				result += contArray.getJSONObject(j).optString("cont").trim() + ",";
			}
		}
		return result;
	}
	
	public static String getWordSplit(String text) throws JSONException, UnsupportedEncodingException, IOException{
		return getWordSplit(getLTPResultByStr(text));
	}
	
	/**
	 * 关键词提取 HED & HED.arg[A1]
	 * @param json
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException
	 */
	
	public static String[] getKeyWordArray(String text) throws UnsupportedEncodingException, JSONException, IOException{
		return getKeyWordArray(getLTPResultByStr(text));
	}
	public static String[] getKeyWordArray(JSONObject json) throws JSONException{
		JSONArray jsonArray = json.getJSONArray("root");
		String[] resultArray = new String[]{};
		
		Set<String> resultSet = new HashSet<String>();

		for(int i=0;i<jsonArray.length();i++){
			
			JSONArray sendArray = jsonArray.getJSONArray(i);//每一句
			for(int j=0;j<sendArray.length();j++){
				String wordrelate =  sendArray.getJSONObject(j).optString("relate");
				if("SBV".equals(wordrelate)){
					String posStr = sendArray.getJSONObject(j).optString("pos");
					//if(posStr.equalsIgnoreCase("n")){//如果是名词则加入关键词中
						resultSet.add(sendArray.getJSONObject(j).optString("cont").trim());
						System.out.println("SBV.n" + sendArray.getJSONObject(j).optString("cont"));
					//}
				}
			}
		}
		
		if(resultSet.size()<=1){
			
			for(int i=0;i<jsonArray.length();i++){
				JSONArray sendArray = jsonArray.getJSONArray(i);//每一句
				for(int j=0;j<sendArray.length();j++){
					String wordrelate =  sendArray.getJSONObject(j).optString("relate");
					if("HED".equals(wordrelate)){
						resultSet.add(sendArray.getJSONObject(j).optString("cont").trim());
						System.out.println("HED" + sendArray.getJSONObject(j).optString("cont"));
					}
				}
			}
		}
		
		return resultSet.toArray(resultArray);
	}

}
