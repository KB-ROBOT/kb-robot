package com.kbrobot.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;

/**   
 * @Title: Entity
 * @Description: 语音消息模板
 * @author zhangdaihao
 * @date 2016-09-19 12:10:43
 * @version V1.0   
 *
 */
@Entity
@Table(name = "weixin_voice_template", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class VoiceTemplate implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**addTime*/
	private java.util.Date addTime;
	/**voiceText*/
	private java.lang.String voiceText;
	/**templateName*/
	private java.lang.String templateName;
	/**accountId*/
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  addTime
	 */
	@Column(name ="ADD_TIME",nullable=true)
	public java.util.Date getAddTime(){
		return this.addTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  addTime
	 */
	public void setAddTime(java.util.Date addTime){
		this.addTime = addTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  voiceText
	 */
	@Column(name ="VOICE_TEXT",nullable=true,length=200)
	public java.lang.String getVoiceText(){
		return this.voiceText;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  voiceText
	 */
	public void setVoiceText(java.lang.String voiceText){
		this.voiceText = voiceText;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  templateName
	 */
	@Column(name ="TEMPLATE_NAME",nullable=true,length=255)
	public java.lang.String getTemplateName(){
		return this.templateName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  templateName
	 */
	public void setTemplateName(java.lang.String templateName){
		this.templateName = templateName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  accountId
	 */
	@Column(name ="ACCOUNT_ID",nullable=true,length=32)
	public java.lang.String getAccountId(){
		return this.accountId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  accountId
	 */
	public void setAccountId(java.lang.String accountId){
		this.accountId = accountId;
	}
}
