package com.kbrobot.utils;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import weixin.guanjia.core.util.WeixinUtil;

public class BaiduMapUtil {
	
	private static final String AK = "AQSirPwSmkNWv513ncysOLlxCe5HkA7U";
	
	private static final String Geocoding_API_URL = "http://api.map.baidu.com/geocoder/v2/?ak=AK&location=LOCATION&output=json&pois=1";
	
	/**
	 * 经纬度转地址 
	 * @param lat 纬度
	 * @param lng 经度
	 * @return
	 * @throws JSONException 
	 * @throws UnsupportedEncodingException 
	 */
	public static String location2Address(String lng,String lat){
		String url = Geocoding_API_URL;
		url = url.replaceAll("AK", AK);
		url = url.replaceAll("LOCATION", lat + "," + lng);
		
		JSONObject jsonResult =  new JSONObject();
		try {
			jsonResult = new JSONObject(WeixinUtil.httpRequest(url, "GET", null).toString());
			JSONObject result = jsonResult.getJSONObject("result");
			JSONObject addressComponent = result.getJSONObject("addressComponent");
			
			String province = addressComponent.optString("province");
			String city = addressComponent.optString("city");
			String district = addressComponent.optString("district");
			
			return province+" "+city + " "+ district;
		} catch (JSONException e) {
			e.printStackTrace();
			
			return "转换出错。错误码：" + jsonResult.optString("status");
		}
		
		
	}
	
}
