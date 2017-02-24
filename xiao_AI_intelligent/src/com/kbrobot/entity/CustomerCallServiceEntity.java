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
 * @Description: 纳税顾问
 * @author zhangdaihao
 * @date 2017-02-17 14:34:26
 * @version V1.0   
 *
 */
@Entity
@Table(name = "kbrobot_customer_call_service", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class CustomerCallServiceEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**姓名*/
	private java.lang.String name;
	/**手机*/
	private java.lang.String phone;
	/**电话*/
	private java.lang.String mobile;
	/**头像*/
	private java.lang.String headImg;
	/**
	 * 服务类型
	 * 
	 * 1 : 发票类
	 * 2 : 纳税申报
	 * 3 : 出口退税
	 * 4 : 其他
	 * 5 : 增值税 （税收优惠、营改增等）
	 * 6 : 消费税
	 * 7 : 企业所得税（小微企业、税收优惠等）
	 * 
	 * */
	private java.lang.String serviceType;
	/**
	 * 分组
	 * 
	 * 1 : 税收征管类专家顾问团队
	 * 2 : 税收政策类专家顾问团队
	 * 
	 * */
	private java.lang.String group;
	/**微信公众号ID*/
	private java.lang.String accountId;
	
	/**
	 * 点击次数
	 */
	private Integer clickTimes;
	
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
	 *@return: java.lang.String  姓名
	 */
	@Column(name ="`NAME`",nullable=true,length=100)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  姓名
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  手机
	 */
	@Column(name ="PHONE",nullable=true,length=20)
	public java.lang.String getPhone(){
		return this.phone;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  手机
	 */
	public void setPhone(java.lang.String phone){
		this.phone = phone;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电话
	 */
	@Column(name ="MOBILE",nullable=true,length=20)
	public java.lang.String getMobile(){
		return this.mobile;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电话
	 */
	public void setMobile(java.lang.String mobile){
		this.mobile = mobile;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  头像
	 */
	@Column(name ="HEAD_IMG",nullable=true,length=500)
	public java.lang.String getHeadImg(){
		return this.headImg;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  头像
	 */
	public void setHeadImg(java.lang.String headImg){
		this.headImg = headImg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  服务类型
	 */
	@Column(name ="SERVICE_TYPE",nullable=true,length=10)
	public java.lang.String getServiceType(){
		return this.serviceType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  服务类型
	 */
	public void setServiceType(java.lang.String serviceType){
		this.serviceType = serviceType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  分组
	 */
	@Column(name ="`GROUP`",nullable=true,length=10)
	public java.lang.String getGroup(){
		return this.group;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  分组
	 */
	public void setGroup(java.lang.String group){
		this.group = group;
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

	@Column(name ="CLICK_TIMES",nullable=true)
	public Integer getClickTimes() {
		return clickTimes;
	}
	

	public void setClickTimes(Integer clickTimes) {
		this.clickTimes = clickTimes;
	}
	
	
	
}
