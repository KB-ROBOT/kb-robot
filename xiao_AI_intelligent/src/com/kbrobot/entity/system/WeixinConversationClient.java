package com.kbrobot.entity.system;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.kbrobot.entity.RobotQuestionEntity;

/**   
 * @Title: Entity
 * @Description: 微信会话实例
 * @author zhangdaihao
 * @date 2016-09-02 17:27:24
 * @version V1.0   
 *
 */
@Entity
@Table(name = "weixin_conversation_client", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class WeixinConversationClient implements java.io.Serializable {
	/**会话用户id*/
	private java.lang.String id;
	/**openId*/
	private java.lang.String openId;
	/**会话目标公众号原始ID*/
	private java.lang.String weixinAccountId;
	/**startMessageId*/
	private java.lang.String startMessageId;
	/**endMessageId*/
	private java.lang.String endMessageId;
	/**addDate*/
	private java.util.Date addDate;
	/**endDate*/
	private java.util.Date endDate;
	
	//加入会话时间
	@Transient
	private Date updateTime;
	
	/**
	 * 最后的问题列表
	 */
	private List<RobotQuestionEntity> lastQuestionList;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  会话用户id
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
	 *@param: java.lang.String  会话用户id
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  openId
	 */
	@Column(name ="OPEN_ID",nullable=false,length=255)
	public java.lang.String getOpenId(){
		return this.openId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  openId
	 */
	public void setOpenId(java.lang.String openId){
		this.openId = openId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  会话目标公众号原始ID
	 */
	@Column(name ="WEIXIN_ACCOUNT_ID",nullable=false,length=255)
	public java.lang.String getWeixinAccountId(){
		return this.weixinAccountId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  会话目标公众号原始ID
	 */
	public void setWeixinAccountId(java.lang.String weixinAccountId){
		this.weixinAccountId = weixinAccountId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  startMessageId
	 */
	@Column(name ="START_MESSAGE_ID",nullable=false,length=32)
	public java.lang.String getStartMessageId(){
		return this.startMessageId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  startMessageId
	 */
	public void setStartMessageId(java.lang.String startMessageId){
		this.startMessageId = startMessageId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  endMessageId
	 */
	@Column(name ="END_MESSAGE_ID",nullable=false,length=32)
	public java.lang.String getEndMessageId(){
		return this.endMessageId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  endMessageId
	 */
	public void setEndMessageId(java.lang.String endMessageId){
		this.endMessageId = endMessageId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  addDate
	 */
	@Column(name ="ADD_DATE",nullable=true)
	public java.util.Date getAddDate(){
		return this.addDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  addDate
	 */
	public void setAddDate(java.util.Date addDate){
		this.addDate = addDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  endDate
	 */
	@Column(name ="END_DATE",nullable=true)
	public java.util.Date getEndDate(){
		return this.endDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  endDate
	 */
	public void setEndDate(java.util.Date endDate){
		this.endDate = endDate;
	}
	
	@Transient
	public List<RobotQuestionEntity> getLastQuestionList() {
		return lastQuestionList;
	}
	public void setLastQuestionList(List<RobotQuestionEntity> lastQuestionList) {
		this.lastQuestionList = lastQuestionList;
	}
	
	//辅助方法
	public boolean questionListIsEmpty(){
		return lastQuestionList==null||lastQuestionList.isEmpty();
	}
	
	public boolean questionListIsNotEmpty(){
		return !questionListIsEmpty();
	}
	
	public void clearAllQuestion(){
		this.lastQuestionList.clear();
	}
	
	public void updateAddTime(){
		this.updateTime = new Date();
	}
	@Transient
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
