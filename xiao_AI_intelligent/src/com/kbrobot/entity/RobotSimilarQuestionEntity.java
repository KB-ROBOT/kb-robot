package com.kbrobot.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**   
 * @Title: Entity
 * @Description: 相似问题
 * @author zhangdaihao
 * @date 2016-08-13 15:22:01
 * @version V1.0   
 *
 */
@Entity
@Table(name = "robot_similar_question", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class RobotSimilarQuestionEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**similarQuestionTitle*/
	private java.lang.String similarQuestionTitle;
	/**questionId*/
	private RobotQuestionEntity question;
	
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
	 *@return: java.lang.String  similarQuestionTitle
	 */
	@Column(name ="SIMILAR_QUESTION_TITLE",nullable=true,length=500)
	public java.lang.String getSimilarQuestionTitle(){
		return this.similarQuestionTitle;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  similarQuestionTitle
	 */
	public void setSimilarQuestionTitle(java.lang.String similarQuestionTitle){
		this.similarQuestionTitle = similarQuestionTitle;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="question_id")
	@JsonBackReference
	public RobotQuestionEntity getQuestion() {
		return question;
	}
	@JsonBackReference
	public void setQuestion(RobotQuestionEntity question) {
		this.question = question;
	}
	
	
	
}
