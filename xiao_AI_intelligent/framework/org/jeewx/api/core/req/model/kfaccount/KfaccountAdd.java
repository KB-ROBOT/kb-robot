package org.jeewx.api.core.req.model.kfaccount;

import org.jeewx.api.core.annotation.ReqType;
import org.jeewx.api.core.req.model.WeixinReqParam;

/**
 * 客服账号添加
 * 
 * @author sfli.sir
 * 
 */
@ReqType("kfaccountAdd")
public class KfaccountAdd extends WeixinReqParam {

	private String kf_account;
	
	private String nickname;
	
	public String getKf_account() {
		return kf_account;
	}

	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}	
	
}
