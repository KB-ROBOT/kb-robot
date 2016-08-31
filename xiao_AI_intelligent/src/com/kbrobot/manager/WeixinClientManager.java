package com.kbrobot.manager;

import java.util.HashMap;
import java.util.Map;

import com.kbrobot.entity.system.WeixinClient;

public class WeixinClientManager {
	
	public static WeixinClientManager instance = new WeixinClientManager();
	
	private Map<String,WeixinClient> map = new HashMap<String, WeixinClient>();
	
	public void addWeixinClinet(String openId,WeixinClient client){
		map.put(openId, client);
	}
	/**
	 * sessionId
	 */
	public void removeWeixinClinet(String openId){
		map.remove(openId);
	}
	/**
	 * 
	 * @param sessionId
	 * @return
	 */
	public WeixinClient getWeixinClient(String openid){
		return map.get(openid);
	}

	/**
	 * 
	 * @return
	 */
	public Map<String,WeixinClient> getAllWeixinClient(){
		return map;
	}
	

}
