package org.jeewx.api.wxsendmsg.model;

/**
 * 客服信息
 * @author sfli.sir
 *
 */
public class WxKfaccount {

	private String kf_account;
	private String kf_nick;
	private String kf_id;
	private String kf_headimgurl;
	private String kf_wx;
	
	private String invite_wx;
	private String invite_expire_time;
	private String invite_status;
	
	private String status;
	private String accepted_case;
	
	public String getKf_account() {
		return kf_account;
	}
	
	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}
	
	public String getKf_nick() {
		return kf_nick;
	}
	
	public void setKf_nick(String kf_nick) {
		this.kf_nick = kf_nick;
	}
	
	public String getKf_id() {
		return kf_id;
	}
	
	public void setKf_id(String kf_id) {
		this.kf_id = kf_id;
	}
	
	public String getKf_headimgurl() {
		return kf_headimgurl;
	}
	
	public void setKf_headimgurl(String kf_headimgurl) {
		this.kf_headimgurl = kf_headimgurl;
	}
	
	public String getKf_wx() {
		return kf_wx;
	}
	
	public void setKf_wx(String kf_wx) {
		this.kf_wx = kf_wx;
	}
	
	public String getInvite_wx() {
		return invite_wx;
	}
	
	public void setInvite_wx(String invite_wx) {
		this.invite_wx = invite_wx;
	}
	
	public String getInvite_expire_time() {
		return invite_expire_time;
	}
	
	public void setInvite_expire_time(String invite_expire_time) {
		this.invite_expire_time = invite_expire_time;
	}
	
	public String getInvite_status() {
		return invite_status;
	}
	
	public void setInvite_status(String invite_status) {
		this.invite_status = invite_status;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getAccepted_case() {
		return accepted_case;
	}
	
	public void setAccepted_case(String accepted_case) {
		this.accepted_case = accepted_case;
	}
	
}
