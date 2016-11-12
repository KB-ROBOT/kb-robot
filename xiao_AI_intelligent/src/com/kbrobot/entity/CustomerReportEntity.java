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
 * @Description: 投诉与举报
 * @author zhangdaihao
 * @date 2016-11-03 14:19:36
 * @version V1.0   
 *
 */
@Entity
@Table(name = "kbrobot_customer_report", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class CustomerReportEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**提交者姓名*/
	private java.lang.String name;
	/**提交者联系方式*/
	private java.lang.String mobile;
	/**提交的内容*/
	private java.lang.String content;
	/**公众号ID*/
	private java.lang.String accountId;
	/**是否已读*/
	private java.lang.String isReader;
	/**是否已回复*/
	private java.lang.String isReply;
	
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
	 *@return: java.lang.String  提交者姓名
	 */
	@Column(name ="NAME",nullable=true,length=50)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  提交者姓名
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  提交者联系方式
	 */
	@Column(name ="MOBILE",nullable=true,length=20)
	public java.lang.String getMobile(){
		return this.mobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  提交者联系方式
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  提交的内容
	 */
	@Column(name ="CONTENT",nullable=true,length=255)
	public java.lang.String getContent(){
		return this.content;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  提交的内容
	 */
	public void setContent(java.lang.String content){
		this.content = content;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公众号ID
	 */
	@Column(name ="ACCOUNT_ID",nullable=true,length=32)
	public java.lang.String getAccountId(){
		return this.accountId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公众号ID
	 */
	public void setAccountId(java.lang.String accountId){
		this.accountId = accountId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否已读
	 */
	@Column(name ="IS_READER",nullable=true,length=1)
	public java.lang.String getIsReader(){
		return this.isReader;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否已读
	 */
	public void setIsReader(java.lang.String isReader){
		this.isReader = isReader;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否已回复
	 */
	@Column(name ="IS_REPLY",nullable=true,length=1)
	public java.lang.String getIsReply(){
		return this.isReply;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否已回复
	 */
	public void setIsReply(java.lang.String isReply){
		this.isReply = isReply;
	}
}
