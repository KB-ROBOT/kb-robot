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
 * @Description: 用户预约
 * @author zhangdaihao
 * @date 2017-02-07 13:59:53
 * @version V1.0   
 *
 */
@Entity
@Table(name = "kbrobot_customer_bespeak", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class CustomerBespeakEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**预约办税人姓名*/
	private java.lang.String yybsxm;
	/**联系方式（号码）*/
	private java.lang.String yybshm;
	/**纳税人识别号*/
	private java.lang.String yybssbh;
	/**预约服务项目*/
	private java.lang.String yyfwxm;
	/**预约日期*/
	private java.lang.String yyrq;
	/**预约时间段*/
	private java.lang.String yybssh;
	/**微信公众号ID*/
	private java.lang.String accountId;
	/**用户OpenID*/
	private java.lang.String targetOpenid;
	
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
	 *@return: java.lang.String  预约办税人姓名
	 */
	@Column(name ="YYBSXM",nullable=true,length=10)
	public java.lang.String getYybsxm(){
		return this.yybsxm;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  预约办税人姓名
	 */
	public void setYybsxm(java.lang.String yybsxm){
		this.yybsxm = yybsxm;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  联系方式（号码）
	 */
	@Column(name ="YYBSHM",nullable=true,length=32)
	public java.lang.String getYybshm(){
		return this.yybshm;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  联系方式（号码）
	 */
	public void setYybshm(java.lang.String yybshm){
		this.yybshm = yybshm;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  纳税人识别号
	 */
	@Column(name ="YYBSSBH",nullable=true,length=32)
	public java.lang.String getYybssbh(){
		return this.yybssbh;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  纳税人识别号
	 */
	public void setYybssbh(java.lang.String yybssbh){
		this.yybssbh = yybssbh;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  预约服务项目
	 */
	@Column(name ="YYFWXM",nullable=true,length=32)
	public java.lang.String getYyfwxm(){
		return this.yyfwxm;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  预约服务项目
	 */
	public void setYyfwxm(java.lang.String yyfwxm){
		this.yyfwxm = yyfwxm;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  预约日期
	 */
	@Column(name ="YYRQ",nullable=true,length=32)
	public java.lang.String getYyrq(){
		return this.yyrq;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  预约日期
	 */
	public void setYyrq(java.lang.String yyrq){
		this.yyrq = yyrq;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  预约时间段
	 */
	@Column(name ="YYBSSH",nullable=true,length=32)
	public java.lang.String getYybssh(){
		return this.yybssh;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  预约时间段
	 */
	public void setYybssh(java.lang.String yybssh){
		this.yybssh = yybssh;
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
	 *@return: java.lang.String  用户OpenID
	 */
	@Column(name ="TARGET_OPENID",nullable=true,length=100)
	public java.lang.String getTargetOpenid(){
		return this.targetOpenid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  用户OpenID
	 */
	public void setTargetOpenid(java.lang.String targetOpenid){
		this.targetOpenid = targetOpenid;
	}
}
