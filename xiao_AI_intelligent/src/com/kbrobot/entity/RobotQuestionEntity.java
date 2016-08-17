package com.kbrobot.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: 知识库
 * @author zhangdaihao
 * @date 2016-08-03 10:48:34
 * @version V1.0   
 *
 */
@Entity
@Table(name = "robot_question", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class RobotQuestionEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**questionTitle*/
	private java.lang.String questionTitle;
	/**questionAnswer*/
	private java.lang.String questionAnswer;
	/**accoundId*/
	private java.lang.String accoundId;
	/**
	 * 相似问题
	 */
	private List<RobotSimilarQuestionEntity> similarQuestionList;
	/**createTime*/
	private java.util.Date createTime;
	/**updateTime*/
	private java.util.Date updateTime;
	/**问题匹配到的次数*/
	private java.lang.Integer matchTimes;
	
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
	 *@return: java.lang.String  questionTitle
	 */
	@Column(name ="QUESTION_TITLE",nullable=true,length=500)
	public java.lang.String getQuestionTitle(){
		return this.questionTitle;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  questionTitle
	 */
	public void setQuestionTitle(java.lang.String questionTitle){
		this.questionTitle = questionTitle;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  questionAnswer
	 */
	@Column(name ="QUESTION_ANSWER",nullable=true,length=65535)
	public java.lang.String getQuestionAnswer(){
		return this.questionAnswer;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  questionAnswer
	 */
	public void setQuestionAnswer(java.lang.String questionAnswer){
		this.questionAnswer = questionAnswer;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  accoundId
	 */
	@Column(name ="ACCOUND_ID",nullable=true,length=36)
	public java.lang.String getAccoundId(){
		return this.accoundId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  accoundId
	 */
	public void setAccoundId(java.lang.String accoundId){
		this.accoundId = accoundId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  createTime
	 */
	@Column(name ="CREATE_TIME",nullable=true)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  createTime
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  updateTime
	 */
	@Column(name ="UPDATE_TIME",nullable=true)
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  updateTime
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  问题匹配到的次数
	 */
	@Column(name ="MATCH_TIMES",nullable=true,precision=10,scale=0)
	public java.lang.Integer getMatchTimes(){
		return this.matchTimes;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  问题匹配到的次数
	 */
	public void setMatchTimes(java.lang.Integer matchTimes){
		this.matchTimes = matchTimes;
	}
	
	/**
	 * 相似问题
	 * @return
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="question_id")
	public List<RobotSimilarQuestionEntity> getSimilarQuestionList() {
		return similarQuestionList;
	}
	public void setSimilarQuestionList(List<RobotSimilarQuestionEntity> similarQuestionList) {
		this.similarQuestionList = similarQuestionList;
	}
	
	
	
}
