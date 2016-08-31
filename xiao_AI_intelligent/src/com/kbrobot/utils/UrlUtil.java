package com.kbrobot.utils;

public class UrlUtil {
	/**
	 * 获得url参数
	 * @param key
	 * @param url
	 * @return
	 */
	public static String getParam(String key,String url){
		String reg = "((\\?|&)"+key+"=|&)";
		System.out.println("reg：" + reg);
		return url.split(reg).length>1?url.split(reg)[1]:null;
	}
	/**
	 * 设置url参数
	 * @param key
	 * @param value
	 * @param url
	 * @return
	 */
	public static String serParam(String key,String value,String url){
		return url.replaceAll("("+ key+"=)([^&]*)",key + "=" + value);
	}
	/**
	 * 通过url得到json
	 * @param urlStr
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 *//*
	public static JSONObject getJsonObjectByUrl(String urlStr) throws IOException, JSONException{
		//urlStr = URLEncoder.encode(urlStr, "utf-8");
		URL url = new URL(urlStr);
		HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
		httpUrlConn.setDoInput(true);
		httpUrlConn.setRequestMethod("GET");
		httpUrlConn.connect();

		StringBuffer jsonStr = new StringBuffer("");
		BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream(),"utf-8"));

		while (in.ready()) {
			jsonStr.append(String.valueOf((char)in.read()));
		}
		
		String result = jsonStr.toString().replaceAll("\n", "").replaceAll("(\\[(\\s)*\\[(\\s)*\\[)", "{\"root\":[");
		result = result.replaceAll("\n", "").replaceAll("(\\](\\s)*\\](\\s)*\\])", "]}");
		
		return new JSONObject(result);// JSONObject.fromObject("{\"root\":["+result+"]}");
	}*/
}
