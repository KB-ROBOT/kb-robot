package org.jeewx.api.core.req.model.kfaccount;

import org.jeewx.api.core.annotation.ReqType;
import org.jeewx.api.core.req.model.WeixinReqParam;

/**
 * 取多媒体文件
 * 
 * @author sfli.sir
 * 
 */
@ReqType("kfaccountDel")
public class KfaccountDel extends WeixinReqParam {

	private String kf_account;
	
	public String getKf_account() {
		return kf_account;
	}

	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}
}
