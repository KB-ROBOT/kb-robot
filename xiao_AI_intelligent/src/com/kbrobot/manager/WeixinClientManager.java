package com.kbrobot.manager;

import java.util.HashMap;
import java.util.Map;

import com.kbrobot.entity.system.WeixinConversationClient;

public class WeixinClientManager {
	
	public static WeixinClientManager instance = new WeixinClientManager();
	
	private Map<String,WeixinConversationClient> map = new HashMap<String, WeixinConversationClient>();
	
	public void addWeixinConversationClient(String openId,WeixinConversationClient client){
		map.put(openId, client);
	}
	/**
	 * sessionId
	 */
	public void removeWeixinConversationClient(String openId){
		map.remove(openId);
	}
	/**
	 * 
	 * @param sessionId
	 * @return
	 */
	public WeixinConversationClient getWeixinConversationClient(String openid){
		return map.get(openid);
	}

	/**
	 * 
	 * @return
	 */
	public Map<String,WeixinConversationClient> getAllWeixinConversationClient(){
		return map;
	}
	

}
