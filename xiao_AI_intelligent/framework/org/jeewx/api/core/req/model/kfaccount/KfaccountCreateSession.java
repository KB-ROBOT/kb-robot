package org.jeewx.api.core.req.model.kfaccount;

import org.jeewx.api.core.annotation.ReqType;
import org.jeewx.api.core.req.model.WeixinReqParam;

/**
 * 
 * @author lw
 *
 */
@ReqType("createSession")
public class KfaccountCreateSession extends WeixinReqParam {
	String kf_account;
	String openid;
	String text;
	public String getKf_account() {
		return kf_account;
	}
	
	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}
	
	public String getOpenid() {
		return openid;
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	
}
