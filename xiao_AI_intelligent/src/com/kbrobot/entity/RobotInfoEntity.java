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
 * @Description: 机器人设定
 * @author zhangdaihao
 * @date 2016-09-28 15:33:45
 * @version V1.0   
 *
 */
@Entity
@Table(name = "robot_info", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class RobotInfoEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**微信公众号原始ID*/
	private java.lang.String weixinAccountId;
	/**电话号码*/
	private java.lang.String phoneNumber;
	/**提醒语句*/
	private java.lang.String remind;
	
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
	 *@return: java.lang.String  微信公众号ID
	 */
	@Column(name ="WEIXIN_ACCOUNT_ID",nullable=true,length=32)
	public java.lang.String getWeixinAccountId(){
		return this.weixinAccountId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  微信公众号原始ID
	 */
	public void setWeixinAccountId(java.lang.String weixinAccountId){
		this.weixinAccountId = weixinAccountId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电话号码
	 */
	@Column(name ="PHONE_NUMBER",nullable=true,length=32)
	public java.lang.String getPhoneNumber(){
		return this.phoneNumber;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电话号码
	 */
	public void setPhoneNumber(java.lang.String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  提醒语句
	 */
	@Column(name ="REMIND",nullable=true,length=32)
	public java.lang.String getRemind(){
		return this.remind;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  提醒语句
	 */
	public void setRemind(java.lang.String remind){
		this.remind = remind;
	}
}
