package org.jeecgframework.web.rest.entity;

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
 * @Description: 第三方平台component_verify_ticket与component_access_token
 * @author zhangdaihao
 * @date 2016-07-29 14:25:46
 * @version V1.0   
 *
 */
@Entity
@Table(name = "weixin_open_account", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class WeixinOpenAccountEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**appid*/
	private java.lang.String appid;
	/**第三方平台推送 : ticket*/
	private java.lang.String componentVerifyTicket;
	/**取得ticket的时间*/
	private java.util.Date getTicketTime;
	/**componentAccessToken*/
	private java.lang.String componentAccessToken;
	/**取得accessToken的时间*/
	private java.util.Date getAccessTokenTime;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=36)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  appid
	 */
	@Column(name ="APPID",nullable=true,length=200)
	public java.lang.String getAppid(){
		return this.appid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  appid
	 */
	public void setAppid(java.lang.String appid){
		this.appid = appid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  第三方平台推送 : ticket
	 */
	@Column(name ="COMPONENT_VERIFY_TICKET",nullable=true,length=200)
	public java.lang.String getComponentVerifyTicket(){
		return this.componentVerifyTicket;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  第三方平台推送 : ticket
	 */
	public void setComponentVerifyTicket(java.lang.String componentVerifyTicket){
		this.componentVerifyTicket = componentVerifyTicket;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  getTicketTime
	 */
	@Column(name ="GET_TICKET_TIME",nullable=true)
	public java.util.Date getGetTicketTime(){
		return this.getTicketTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  getTicketTime
	 */
	public void setGetTicketTime(java.util.Date getTicketTime){
		this.getTicketTime = getTicketTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  componentAccessToken
	 */
	@Column(name ="COMPONENT_ACCESS_TOKEN",nullable=true,length=200)
	public java.lang.String getComponentAccessToken(){
		return this.componentAccessToken;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  componentAccessToken
	 */
	public void setComponentAccessToken(java.lang.String componentAccessToken){
		this.componentAccessToken = componentAccessToken;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  getAccessTokenTime
	 */
	@Column(name ="GET_ACCESS_TOKEN_TIME",nullable=true)
	public java.util.Date getGetAccessTokenTime(){
		return this.getAccessTokenTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  getAccessTokenTime
	 */
	public void setGetAccessTokenTime(java.util.Date getAccessTokenTime){
		this.getAccessTokenTime = getAccessTokenTime;
	}
}
