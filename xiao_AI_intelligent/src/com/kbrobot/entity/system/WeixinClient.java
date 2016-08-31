package com.kbrobot.entity.system;

import java.util.Date;
import java.util.List;

import com.kbrobot.entity.RobotQuestionEntity;

public class WeixinClient {
	
	/**
	 * 用户id
	 */
	private String openId;
	
	/**
	 *开始会话时间
	 */
	private Date addDateTime;
	
	/**
	 * 最后的问题列表
	 */
	private List<RobotQuestionEntity> lastQuestionList;
	
	/**
	 * 答案列表
	 */
/*	private String[] answerArray;*/

	public String getOpenId() {
		return openId;
	}
	

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	

	public Date getAddDateTime() {
		return addDateTime;
	}
	

	public void setAddDateTime(Date addDateTime) {
		this.addDateTime = addDateTime;
	}
	

	public List<RobotQuestionEntity> getLastQuestionList() {
		return lastQuestionList;
	}
	

	public void setLastQuestionList(List<RobotQuestionEntity> lastQuestionList) {
		this.lastQuestionList = lastQuestionList;
	}
	

	/*public String[] getAnswerArray() {
		return answerArray;
	}
	

	public void setAnswerArray(String[] answerArray) {
		this.answerArray = answerArray;
	}*/
	
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
		this.addDateTime = new Date();
	}
	
}
