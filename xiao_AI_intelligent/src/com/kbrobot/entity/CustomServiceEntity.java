package com.kbrobot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: 客服
 * @author zhangdaihao
 * @date 2016-10-10 11:59:53
 * @version V1.0   
 *
 */
@Entity
@Table(name = "weixin_custom_service", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class CustomServiceEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**完整客服帐号，格式为：帐号前缀@公众号微信号*/
	private java.lang.String kfAccount;
	/**客服头像*/
	private java.lang.String kfHeadimgurl;
	/**客服编号*/
	private java.lang.String kfId;
	/**客服昵称*/
	private java.lang.String kfNick;
	/**如果客服帐号已绑定了客服人员微信号，则此处显示微信号*/
	private java.lang.String kfWx;
	/**如果客服帐号尚未绑定微信号，但是已经发起了一个绑定邀请，则此处显示绑定邀请的微信号*/
	private java.lang.String inviteWx;
	/**如果客服帐号尚未绑定微信号，但是已经发起过一个绑定邀请，邀请的过期时间，为unix 时间戳*/
	private java.lang.String inviteExpireTime;
	/**邀请的状态，有等待确认“waiting”，被拒绝“rejected”，过期“expired”*/
	private java.lang.String inviteStatus;
	/**微信公众号ID*/
	private java.lang.String accountId;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
	 */
	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=32)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  完整客服帐号，格式为：帐号前缀@公众号微信号
	 */
	@Column(name ="KF_ACCOUNT",nullable=true,length=255)
	public java.lang.String getKfAccount(){
		return this.kfAccount;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  完整客服帐号，格式为：帐号前缀@公众号微信号
	 */
	public void setKfAccount(java.lang.String kfAccount){
		this.kfAccount = kfAccount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  客服头像
	 */
	@Column(name ="KF_HEADIMGURL",nullable=true,length=255)
	public java.lang.String getKfHeadimgurl(){
		return this.kfHeadimgurl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  客服头像
	 */
	public void setKfHeadimgurl(java.lang.String kfHeadimgurl){
		this.kfHeadimgurl = kfHeadimgurl;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  客服编号
	 */
	@Column(name ="KF_ID",nullable=true,length=50)
	public java.lang.String getKfId(){
		return this.kfId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  客服编号
	 */
	public void setKfId(java.lang.String kfId){
		this.kfId = kfId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  客服昵称
	 */
	@Column(name ="KF_NICK",nullable=true,length=255)
	public java.lang.String getKfNick(){
		return this.kfNick;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  客服昵称
	 */
	public void setKfNick(java.lang.String kfNick){
		this.kfNick = kfNick;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  如果客服帐号已绑定了客服人员微信号，则此处显示微信号
	 */
	@Column(name ="KF_WX",nullable=true,length=50)
	public java.lang.String getKfWx(){
		return this.kfWx;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  如果客服帐号已绑定了客服人员微信号，则此处显示微信号
	 */
	public void setKfWx(java.lang.String kfWx){
		this.kfWx = kfWx;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  如果客服帐号尚未绑定微信号，但是已经发起了一个绑定邀请，则此处显示绑定邀请的微信号
	 */
	@Column(name ="INVITE_WX",nullable=true,length=50)
	public java.lang.String getInviteWx(){
		return this.inviteWx;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  如果客服帐号尚未绑定微信号，但是已经发起了一个绑定邀请，则此处显示绑定邀请的微信号
	 */
	public void setInviteWx(java.lang.String inviteWx){
		this.inviteWx = inviteWx;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  如果客服帐号尚未绑定微信号，但是已经发起过一个绑定邀请，邀请的过期时间，为unix 时间戳
	 */
	@Column(name ="INVITE_EXPIRE_TIME",nullable=true,length=50)
	public java.lang.String getInviteExpireTime(){
		return this.inviteExpireTime;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  如果客服帐号尚未绑定微信号，但是已经发起过一个绑定邀请，邀请的过期时间，为unix 时间戳
	 */
	public void setInviteExpireTime(java.lang.String inviteExpireTime){
		this.inviteExpireTime = inviteExpireTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  邀请的状态，有等待确认“waiting”，被拒绝“rejected”，过期“expired”
	 */
	@Column(name ="INVITE_STATUS",nullable=true,length=255)
	public java.lang.String getInviteStatus(){
		return this.inviteStatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  邀请的状态，有等待确认“waiting”，被拒绝“rejected”，过期“expired”
	 */
	public void setInviteStatus(java.lang.String inviteStatus){
		this.inviteStatus = inviteStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  微信公众号ID
	 */
	@Column(name ="ACCOUNT_ID",nullable=true,length=255)
	public java.lang.String getAccountId(){
		return this.accountId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  微信公众号ID
	 */
	public void setAccountId(java.lang.String accountId){
		this.accountId = accountId;
	}
}
