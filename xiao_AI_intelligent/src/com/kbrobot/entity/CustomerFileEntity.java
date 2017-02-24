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
 * @Description: 文件
 * @author zhangdaihao
 * @date 2017-02-23 09:44:46
 * @version V1.0   
 *
 */
@Entity
@Table(name = "kbrobot_customer_file", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class CustomerFileEntity implements java.io.Serializable {
	/**id*/
	private java.lang.String id;
	/**文件名*/
	private java.lang.String fileName;
	/**文件类型*/
	private java.lang.String fileType;
	/**文件存储路径*/
	private java.lang.String filePath;
	/**TSDocumentID */
	private java.lang.String fileKey;
	/**文件删除路径*/
	private java.lang.String delUrl;
	/**创建日期*/
	private java.util.Date createDate;
	/**微信公众号ID*/
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
	 *@return: java.lang.String  文件名
	 */
	@Column(name ="FILE_NAME",nullable=true,length=1000)
	public java.lang.String getFileName(){
		return this.fileName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  文件名
	 */
	public void setFileName(java.lang.String fileName){
		this.fileName = fileName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  文件类型
	 */
	@Column(name ="FILE_TYPE",nullable=true,length=10)
	public java.lang.String getFileType(){
		return this.fileType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  文件类型
	 */
	public void setFileType(java.lang.String fileType){
		this.fileType = fileType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  文件存储路径
	 */
	@Column(name ="FILE_PATH",nullable=true,length=1000)
	public java.lang.String getFilePath(){
		return this.filePath;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  文件存储路径
	 */
	public void setFilePath(java.lang.String filePath){
		this.filePath = filePath;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  TSDocumentID 
	 */
	@Column(name ="FILE_KEY",nullable=true,length=32)
	public java.lang.String getFileKey(){
		return this.fileKey;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  TSDocumentID 
	 */
	public void setFileKey(java.lang.String fileKey){
		this.fileKey = fileKey;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  文件删除路径
	 */
	@Column(name ="DEL_URL",nullable=true,length=1000)
	public java.lang.String getDelUrl(){
		return this.delUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  文件删除路径
	 */
	public void setDelUrl(java.lang.String delUrl){
		this.delUrl = delUrl;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
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
}
