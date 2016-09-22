package com.kbrobot.task;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kbrobot.entity.system.WeixinConversationClient;
import com.kbrobot.manager.WeixinClientManager;
import com.kbrobot.utils.CustomServiceUtil;
import com.kbrobot.utils.WeixinThirdUtil;

@Component("weixinClientCheckTask")
public class WeixinContextCheckTask {
	
	private Integer timeout = 5;//分钟
	@Autowired
	private SystemService systemService;
	
	public void ClientCheck(){
		Map<String,WeixinConversationClient> allWeixinConversationClient = WeixinClientManager.instance.getAllWeixinConversationClient();
		LogUtil.info("当前客户端个数：" + (allWeixinConversationClient.size()));
		Set<String> keySet =  allWeixinConversationClient.keySet();
		
		Iterator<String> keyItreator =  keySet.iterator();
		
		//遍历
		while(keyItreator.hasNext()){
			String currentKey = keyItreator.next();
			WeixinConversationClient checkClient = allWeixinConversationClient.get(currentKey);
			Long addTime =  checkClient.getUpdateTime().getTime();
			Long nowTime = System.currentTimeMillis();
			String fromUserName = checkClient.getOpenId();
			if((nowTime - addTime)>= timeout*60*1000 ){
				String authorizer_access_token;
				try {
					authorizer_access_token = WeixinThirdUtil.getInstance().getAuthorizerAccessToken(checkClient.getWeixinAccountId());
					CustomServiceUtil.sendCustomServiceTextMessage(fromUserName, authorizer_access_token, "您好，我已经5分钟没有收到您的消息了。不打扰您了，再见。");
					
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				finally {
					
					checkClient.setEndDate(new Date());
					//保存当前会话
					systemService.save(checkClient);
					//移除超时会话端
					WeixinClientManager.instance.removeWeixinConversationClient(currentKey);
					//keyItreator.remove();
				}
			}
		}
	}
}
