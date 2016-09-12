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
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

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
@ExcelTarget(id="robotQuestionEntity")
public class RobotQuestionEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**questionTitle*/
	@Excel(exportName="questionTitle",orderNum="1")
	private java.lang.String questionTitle;
	/**questionAnswer*/
	@Excel(exportName="questionAnswer",orderNum="1")
	private java.lang.String questionAnswer;
	/**accountId*/
	private java.lang.String accountId;
	/**wordSplit*/
	private String wordSplit;
	
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
	 *@return: java.lang.String  accountId
	 */
	@Column(name ="ACCOUNT_ID",nullable=true,length=36)
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
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY ,mappedBy="question")
	//@JoinColumn(name="question_id")
	public List<RobotSimilarQuestionEntity> getSimilarQuestionList() {
		return similarQuestionList;
	}
	public void setSimilarQuestionList(List<RobotSimilarQuestionEntity> similarQuestionList) {
		this.similarQuestionList = similarQuestionList;
	}
	
	@Column(name ="WORD_SPLIT",nullable=true,length=1500)
	public String getWordSplit() {
		return wordSplit;
	}
	public void setWordSplit(String wordSplit) {
		this.wordSplit = wordSplit;
	}
}
