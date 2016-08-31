package com.kbrobot.task;

import java.util.Map;
import java.util.Set;

import org.jeecgframework.core.util.LogUtil;
import org.springframework.stereotype.Component;

import com.kbrobot.entity.system.WeixinClient;
import com.kbrobot.manager.WeixinClientManager;
import com.kbrobot.utils.CustomServiceUtil;
import com.kbrobot.utils.WeixinThirdUtil;

@Component("weixinClientCheckTask")
public class WeixinContextCheckTask {
	
	private Integer timeout = 5;//分钟
	
	public void ClientCheck(){
		Map<String,WeixinClient> allWeixinClient = WeixinClientManager.instance.getAllWeixinClient();
		LogUtil.info("当前客户端个数：" + (allWeixinClient.size()));
		Set<String> keySet =  allWeixinClient.keySet();
		//遍历
		for(String key:keySet){
			WeixinClient client = allWeixinClient.get(key);
			Long addTime =  client.getAddDateTime().getTime();
			Long nowTime = System.currentTimeMillis();
			String fromUserName = key.split(":")[0];
			if((nowTime - addTime)>= timeout*60*1000 ){
				String authorizer_access_token;
				try {
					authorizer_access_token = WeixinThirdUtil.getInstance().getAuthorizerAccessToken(key.split(":")[1]);
					CustomServiceUtil.sendCustomServiceTextMessage(fromUserName, authorizer_access_token, "您好，我已经5分钟没有收到您的消息了。不打扰您了，再见。");
					
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				finally {
					WeixinClientManager.instance.removeWeixinClinet(key);
				}
			}
		}
	}
}
