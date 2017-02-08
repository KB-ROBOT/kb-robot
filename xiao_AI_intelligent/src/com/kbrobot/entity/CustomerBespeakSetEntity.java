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
 * @Description: 预约设置
 * @author zhangdaihao
 * @date 2017-02-06 15:05:28
 * @version V1.0   
 *
 */
@Entity
@Table(name = "kbrobot_customer_bespeak_set", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class CustomerBespeakSetEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**名字*/
	private java.lang.String name;
	/**管理人员微信ID*/
	private java.lang.String openId;
	/**目标微信公众号ID*/
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
	 *@return: java.lang.String  名字
	 */
	@Column(name ="NAME",nullable=true,length=50)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  名字
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  管理人员微信ID
	 */
	@Column(name ="OPEN_ID",nullable=true,length=100)
	public java.lang.String getOpenId(){
		return this.openId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  管理人员微信ID
	 */
	public void setOpenId(java.lang.String openId){
		this.openId = openId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  目标微信公众号ID
	 */
	@Column(name ="ACCOUNT_ID",nullable=true,length=32)
	public java.lang.String getAccountId(){
		return this.accountId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  目标微信公众号ID
	 */
	public void setAccountId(java.lang.String accountId){
		this.accountId = accountId;
	}
}
