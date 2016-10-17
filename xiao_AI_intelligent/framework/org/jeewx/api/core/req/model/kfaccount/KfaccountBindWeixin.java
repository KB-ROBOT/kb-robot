package org.jeewx.api.core.req.model.kfaccount;

import org.jeewx.api.core.annotation.ReqType;
import org.jeewx.api.core.req.model.WeixinReqParam;

/**
 * 
 * @author lw
 *
 */
@ReqType("inviteworker")
public class KfaccountBindWeixin extends WeixinReqParam {
	
	String kf_account;
	String invite_wx;
	public String getKf_account() {
		return kf_account;
	}
	
	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}
	
	public String getInvite_wx() {
		return invite_wx;
	}
	
	public void setInvite_wx(String invite_wx) {
		this.invite_wx = invite_wx;
	}
	
	
	
}
