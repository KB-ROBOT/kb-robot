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
 * @Description: 用户地理位置
 * @author zhangdaihao
 * @date 2016-09-12 17:56:40
 * @version V1.0   
 *
 */
@Entity
@Table(name = "weixin_user_location", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class WeixinUserLocationEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**经度*/
	private java.lang.String longitude;
	/**纬度*/
	private java.lang.String latitude;
	/**精确度*/
	private java.lang.String locationPrecision;
	/**目标公众号原始ID*/
	private java.lang.String weixinAccountId;
	/**客户openid*/
	private java.lang.String openId;
	/**创建时间*/
	private java.util.Date createTime;
	
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
	 *@return: java.lang.String  经度
	 */
	@Column(name ="LONGITUDE",nullable=true,length=50)
	public java.lang.String getLongitude(){
		return this.longitude;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  经度
	 */
	public void setLongitude(java.lang.String longitude){
		this.longitude = longitude;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  纬度
	 */
	@Column(name ="LATITUDE",nullable=true,length=50)
	public java.lang.String getLatitude(){
		return this.latitude;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  纬度
	 */
	public void setLatitude(java.lang.String latitude){
		this.latitude = latitude;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  精确度
	 */
	@Column(name ="LOCATION_PRECISION",nullable=true,length=50)
	public java.lang.String getLocationPrecision(){
		return this.locationPrecision;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  精确度
	 */
	public void setLocationPrecision(java.lang.String locationPrecision){
		this.locationPrecision = locationPrecision;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  目标公众号原始ID
	 */
	@Column(name ="WEIXIN_ACCOUNT_ID",nullable=true,length=255)
	public java.lang.String getWeixinAccountId(){
		return this.weixinAccountId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  目标公众号原始ID
	 */
	public void setWeixinAccountId(java.lang.String weixinAccountId){
		this.weixinAccountId = weixinAccountId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  客户openid
	 */
	@Column(name ="OPEN_ID",nullable=true,length=255)
	public java.lang.String getOpenId(){
		return this.openId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  客户openid
	 */
	public void setOpenId(java.lang.String openId){
		this.openId = openId;
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
}
