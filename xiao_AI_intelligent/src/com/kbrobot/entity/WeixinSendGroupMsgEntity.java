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
 * @Description: 群发消息
 * @author zhangdaihao
 * @date 2016-09-21 16:52:14
 * @version V1.0   
 *
 */
@Entity
@Table(name = "weixin_sendgroup_msg", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class WeixinSendGroupMsgEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**群发消息名称*/
	private java.lang.String groupMsgName;
	/**群发消息描述*/
	private java.lang.String groupMsgDesc;
	/**微信公众号ID*/
	private java.lang.String accountId;
	/**模板ID*/
	private java.lang.String templateId;
	/**消息类型*/
	private java.lang.String msgType;
	/**消息发送任务的ID*/
	private java.lang.String msgId;
	/**群发消息结果状态*/
	private java.lang.String status;
	/**发送的总粉丝数量*/
	private java.lang.Integer totalCount;
	/**过滤后，准备发送的粉丝数*/
	private java.lang.Integer filterCount;
	/**发送成功的粉丝数*/
	private java.lang.Integer sendCount;
	/**发送失败的粉丝数*/
	private java.lang.Integer errorCount;
	/**创建时间*/
	private java.util.Date createTime;
	/**发送时间*/
	private java.util.Date sendTime;
	
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
	 *@return: java.lang.String  群发消息名称
	 */
	@Column(name ="GROUP_MSG_NAME",nullable=true,length=60)
	public java.lang.String getGroupMsgName(){
		return this.groupMsgName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  群发消息名称
	 */
	public void setGroupMsgName(java.lang.String groupMsgName){
		this.groupMsgName = groupMsgName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  群发消息描述
	 */
	@Column(name ="GROUP_MSG_DESC",nullable=true,length=255)
	public java.lang.String getGroupMsgDesc(){
		return this.groupMsgDesc;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  群发消息描述
	 */
	public void setGroupMsgDesc(java.lang.String groupMsgDesc){
		this.groupMsgDesc = groupMsgDesc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  微信公众号ID
	 */
	@Column(name ="ACCOUNT_ID",nullable=true,length=32)
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  模板ID
	 */
	@Column(name ="TEMPLATE_ID",nullable=true,length=32)
	public java.lang.String getTemplateId(){
		return this.templateId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  模板ID
	 */
	public void setTemplateId(java.lang.String templateId){
		this.templateId = templateId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  消息类别
	 */
	@Column(name ="MSG_TYPE",nullable=true,length=10)
	public java.lang.String getMsgType(){
		return this.msgType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  消息类别
	 */
	public void setMsgType(java.lang.String msgType){
		this.msgType = msgType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  消息发送任务的ID
	 */
	@Column(name ="MSG_ID",nullable=true,length=32)
	public java.lang.String getMsgId(){
		return this.msgId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  消息发送任务的ID
	 */
	public void setMsgId(java.lang.String msgId){
		this.msgId = msgId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  群发消息结果状态
	 */
	@Column(name ="STATUS",nullable=true,length=32)
	public java.lang.String getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  群发消息结果状态
	 */
	public void setStatus(java.lang.String status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  发送的总粉丝数量
	 */
	@Column(name ="TOTAL_COUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getTotalCount(){
		return this.totalCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  发送的总粉丝数量
	 */
	public void setTotalCount(java.lang.Integer totalCount){
		this.totalCount = totalCount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  过滤后，准备发送的粉丝数
	 */
	@Column(name ="FILTER_COUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getFilterCount(){
		return this.filterCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  过滤后，准备发送的粉丝数
	 */
	public void setFilterCount(java.lang.Integer filterCount){
		this.filterCount = filterCount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  发送成功的粉丝数
	 */
	@Column(name ="SEND_COUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getSendCount(){
		return this.sendCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  发送成功的粉丝数
	 */
	public void setSendCount(java.lang.Integer sendCount){
		this.sendCount = sendCount;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  发送是被的粉丝数
	 */
	@Column(name ="ERROR_COUNT",nullable=true,precision=10,scale=0)
	public java.lang.Integer getErrorCount(){
		return this.errorCount;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  发送是被的粉丝数
	 */
	public void setErrorCount(java.lang.Integer errorCount){
		this.errorCount = errorCount;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME",nullable=true)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  发送时间
	 */
	@Column(name ="SEND_TIME",nullable=true)
	public java.util.Date getSendTime(){
		return this.sendTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  发送时间
	 */
	public void setSendTime(java.util.Date sendTime){
		this.sendTime = sendTime;
	}
}
