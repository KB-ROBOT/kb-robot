package com.kbrobot.entity.system;

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
 * @Description: 微信会话内容
 * @author zhangdaihao
 * @date 2016-09-02 17:22:12
 * @version V1.0   
 *
 */
@Entity
@Table(name = "weixin_conversation_content", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class WeixinConversationContent implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**toUsername*/
	private java.lang.String toUsername;
	/**fromUsername*/
	private java.lang.String fromUsername;
	/**会话人昵称*/
	private java.lang.String nickName;
	/**msgId*/
	private java.lang.String msgId;
	/**会话内容*/
	private java.lang.String msgContent;
	/**msgType*/
	private java.lang.String msgType;
	/**responseContent*/
	private java.lang.String responseContent;
	/**responseType*/
	private java.lang.String responseType;
	/**接收时间*/
	private java.util.Date receiveTime;
	/**回复时间*/
	private java.util.Date replyTime;
	
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
	 *@return: java.lang.String  toUsername
	 */
	@Column(name ="TO_USERNAME",nullable=true,length=255)
	public java.lang.String getToUsername(){
		return this.toUsername;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  toUsername
	 */
	public void setToUsername(java.lang.String toUsername){
		this.toUsername = toUsername;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  fromUsername
	 */
	@Column(name ="FROM_USERNAME",nullable=true,length=255)
	public java.lang.String getFromUsername(){
		return this.fromUsername;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  fromUsername
	 */
	public void setFromUsername(java.lang.String fromUsername){
		this.fromUsername = fromUsername;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  会话人昵称
	 */
	@Column(name ="NICK_NAME",nullable=true,length=200)
	public java.lang.String getNickName(){
		return this.nickName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  会话人昵称
	 */
	public void setNickName(java.lang.String nickName){
		this.nickName = nickName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  msgId
	 */
	@Column(name ="MSG_ID",nullable=true,length=255)
	public java.lang.String getMsgId(){
		return this.msgId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  msgId
	 */
	public void setMsgId(java.lang.String msgId){
		this.msgId = msgId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  会话内容
	 */
	@Column(name ="MSG_CONTENT",nullable=true,length=255)
	public java.lang.String getMsgContent(){
		return this.msgContent;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  会话内容
	 */
	public void setMsgContent(java.lang.String msgContent){
		this.msgContent = msgContent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  msgType
	 */
	@Column(name ="MSG_TYPE",nullable=true,length=255)
	public java.lang.String getMsgType(){
		return this.msgType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  msgType
	 */
	public void setMsgType(java.lang.String msgType){
		this.msgType = msgType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  responseContent
	 */
	@Column(name ="RESPONSE_CONTENT",nullable=true,length=255)
	public java.lang.String getResponseContent(){
		return this.responseContent;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  responseContent
	 */
	public void setResponseContent(java.lang.String responseContent){
		this.responseContent = responseContent;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  responseType
	 */
	@Column(name ="RESPONSE_TYPE",nullable=true,length=255)
	public java.lang.String getResponseType(){
		return this.responseType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  responseType
	 */
	public void setResponseType(java.lang.String responseType){
		this.responseType = responseType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  接收时间
	 */
	@Column(name ="RECEIVE_TIME",nullable=true)
	public java.util.Date getReceiveTime(){
		return this.receiveTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  接收时间
	 */
	public void setReceiveTime(java.util.Date receiveTime){
		this.receiveTime = receiveTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  回复时间
	 */
	@Column(name ="REPLY_TIME",nullable=true)
	public java.util.Date getReplyTime(){
		return this.replyTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  回复时间
	 */
	public void setReplyTime(java.util.Date replyTime){
		this.replyTime = replyTime;
	}
}
