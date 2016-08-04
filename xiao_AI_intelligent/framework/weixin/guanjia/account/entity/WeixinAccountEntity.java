package weixin.guanjia.account.entity;

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
 * @Description: 微信公众帐号信息
 * @author zhangdaihao
 * @date 2016-07-29 18:14:28
 * @version V1.0   
 *
 */
@Entity
@Table(name = "weixin_account", schema = "")
@DynamicUpdate(true)
@DynamicInsert(true)
@SuppressWarnings("serial")
public class WeixinAccountEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**公众帐号名称*/
	private java.lang.String accountName;
	/**公众帐号TOKEN*/
	private java.lang.String accountToken;
	/**公众微信号*/
	private java.lang.String accountNumber;
	/**公众号类型
	0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号*/
	private java.lang.String accountType;
	/**电子邮箱*/
	private java.lang.String accountEmail;
	/**公众帐号描述*/
	private java.lang.String accountDesc;
	/**ACCESS_TOKEN*/
	private java.lang.String accountAccesstoken;
	/**公众帐号APPID*/
	private java.lang.String accountAppid;
	/**公众帐号APPSECRET*/
	private java.lang.String accountAppsecret;
	/**addTokenTime*/
	private java.util.Date addTokenTime;
	/**所属系统用户*/
	private java.lang.String userName;
	/**公众原始ID*/
	private java.lang.String weixinAccountId;
	/**微信卡券JS API的临时票据*/
	private java.lang.String apiTicket;
	/**微信卡券JS API的临时票据的获取时间*/
	private java.util.Date apiTicketTime;
	/**jsapi调用接口临时凭证*/
	private java.lang.String jsApiTicket;
	/**jsapi调用接口临时凭证的获取时间*/
	private java.util.Date jsApiTicketTime;
	/**授权方接口调用凭据（在授权的公众号具备API权限时，才有此返回值），也简称为令牌*/
	private java.lang.String authorizerAccessToken;
	/**接口调用凭据刷新令牌（在授权的公众号具备API权限时，才有此返回值），刷新令牌主要用于公众号第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌*/
	private java.lang.String authorizerRefreshToken;
	/**授权方接口调用凭据获取时间*/
	private java.util.Date authorizerAccessTokenTime;
	/**接入类型 0：扫一扫接入。1：配置方式接入*/
	private java.lang.String accountAuthorizeType;
	/**授权方认证类型，，
	 * 0代表微信认证，
	 * 1代表新浪微博认证，
	 * 2代表腾讯微博认证，
	 * 3代表已资质认证通过但还未通过名称认证，
	 * 4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，
	 * 5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证*/
	private java.lang.String verifyTypeInfo;
	/**头像*/
	private java.lang.String headImg;
	/**其他功能开通情况（以json数据存入数据库）*/
	private java.lang.String businessInfo;
	/**二维码*/
	private java.lang.String qrcodeUrl;
	/**公众号授权给开发者的权限集列表（以逗号分隔）*/
	private java.lang.String funcInfo;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name ="ID",nullable=false,length=36)
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公众帐号名称
	 */
	@Column(name ="ACCOUNT_NAME",nullable=true,length=200)
	public java.lang.String getAccountName(){
		return this.accountName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公众帐号名称
	 */
	public void setAccountName(java.lang.String accountName){
		this.accountName = accountName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公众帐号TOKEN
	 */
	@Column(name ="ACCOUNT_TOKEN",nullable=true,length=200)
	public java.lang.String getAccountToken(){
		return this.accountToken;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公众帐号TOKEN
	 */
	public void setAccountToken(java.lang.String accountToken){
		this.accountToken = accountToken;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公众微信号
	 */
	@Column(name ="ACCOUNT_NUMBER",nullable=true,length=200)
	public java.lang.String getAccountNumber(){
		return this.accountNumber;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公众微信号
	 */
	public void setAccountNumber(java.lang.String accountNumber){
		this.accountNumber = accountNumber;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公众号类型
0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
	 */
	@Column(name ="ACCOUNT_TYPE",nullable=true,length=50)
	public java.lang.String getAccountType(){
		return this.accountType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公众号类型
0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号
	 */
	public void setAccountType(java.lang.String accountType){
		this.accountType = accountType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电子邮箱
	 */
	@Column(name ="ACCOUNT_EMAIL",nullable=true,length=200)
	public java.lang.String getAccountEmail(){
		return this.accountEmail;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子邮箱
	 */
	public void setAccountEmail(java.lang.String accountEmail){
		this.accountEmail = accountEmail;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公众帐号描述
	 */
	@Column(name ="ACCOUNT_DESC",nullable=true,length=500)
	public java.lang.String getAccountDesc(){
		return this.accountDesc;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公众帐号描述
	 */
	public void setAccountDesc(java.lang.String accountDesc){
		this.accountDesc = accountDesc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  ACCESS_TOKEN
	 */
	@Column(name ="ACCOUNT_ACCESSTOKEN",nullable=true,length=1000)
	public java.lang.String getAccountAccesstoken(){
		return this.accountAccesstoken;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  ACCESS_TOKEN
	 */
	public void setAccountAccesstoken(java.lang.String accountAccesstoken){
		this.accountAccesstoken = accountAccesstoken;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公众帐号APPID
	 */
	@Column(name ="ACCOUNT_APPID",nullable=true,length=200)
	public java.lang.String getAccountAppid(){
		return this.accountAppid;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公众帐号APPID
	 */
	public void setAccountAppid(java.lang.String accountAppid){
		this.accountAppid = accountAppid;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公众帐号APPSECRET
	 */
	@Column(name ="ACCOUNT_APPSECRET",nullable=true,length=500)
	public java.lang.String getAccountAppsecret(){
		return this.accountAppsecret;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公众帐号APPSECRET
	 */
	public void setAccountAppsecret(java.lang.String accountAppsecret){
		this.accountAppsecret = accountAppsecret;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  addTokenTime
	 */
	@Column(name ="ADD_TOKEN_TIME",nullable=true)
	public java.util.Date getAddTokenTime(){
		return this.addTokenTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  addTokenTime
	 */
	public void setAddTokenTime(java.util.Date addTokenTime){
		this.addTokenTime = addTokenTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属系统用户
	 */
	@Column(name ="USER_NAME",nullable=true,length=50)
	public java.lang.String getUserName(){
		return this.userName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属系统用户
	 */
	public void setUserName(java.lang.String userName){
		this.userName = userName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公众原始ID
	 */
	@Column(name ="WEIXIN_ACCOUNT_ID",nullable=true,length=100)
	public java.lang.String getWeixinAccountId(){
		return this.weixinAccountId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公众原始ID
	 */
	public void setWeixinAccountId(java.lang.String weixinAccountId){
		this.weixinAccountId = weixinAccountId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  微信卡券JS API的临时票据
	 */
	@Column(name ="API_TICKET",nullable=true,length=200)
	public java.lang.String getApiTicket(){
		return this.apiTicket;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  微信卡券JS API的临时票据
	 */
	public void setApiTicket(java.lang.String apiTicket){
		this.apiTicket = apiTicket;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  微信卡券JS API的临时票据的获取时间
	 */
	@Column(name ="API_TICKET_TIME",nullable=true)
	public java.util.Date getApiTicketTime(){
		return this.apiTicketTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  微信卡券JS API的临时票据的获取时间
	 */
	public void setApiTicketTime(java.util.Date apiTicketTime){
		this.apiTicketTime = apiTicketTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  jsapi调用接口临时凭证
	 */
	@Column(name ="JS_API_TICKET",nullable=true,length=200)
	public java.lang.String getJsApiTicket(){
		return this.jsApiTicket;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  jsapi调用接口临时凭证
	 */
	public void setJsApiTicket(java.lang.String jsApiTicket){
		this.jsApiTicket = jsApiTicket;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  jsapi调用接口临时凭证的获取时间
	 */
	@Column(name ="JS_API_TICKET_TIME",nullable=true)
	public java.util.Date getJsApiTicketTime(){
		return this.jsApiTicketTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  jsapi调用接口临时凭证的获取时间
	 */
	public void setJsApiTicketTime(java.util.Date jsApiTicketTime){
		this.jsApiTicketTime = jsApiTicketTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  授权方接口调用凭据（在授权的公众号具备API权限时，才有此返回值），也简称为令牌
	 */
	@Column(name ="AUTHORIZER_ACCESS_TOKEN",nullable=true,length=200)
	public java.lang.String getAuthorizerAccessToken(){
		return this.authorizerAccessToken;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  授权方接口调用凭据（在授权的公众号具备API权限时，才有此返回值），也简称为令牌
	 */
	public void setAuthorizerAccessToken(java.lang.String authorizerAccessToken){
		this.authorizerAccessToken = authorizerAccessToken;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口调用凭据刷新令牌（在授权的公众号具备API权限时，才有此返回值），刷新令牌主要用于公众号第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌
	 */
	@Column(name ="AUTHORIZER_REFRESH_TOKEN",nullable=true,length=200)
	public java.lang.String getAuthorizerRefreshToken(){
		return this.authorizerRefreshToken;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口调用凭据刷新令牌（在授权的公众号具备API权限时，才有此返回值），刷新令牌主要用于公众号第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌
	 */
	public void setAuthorizerRefreshToken(java.lang.String authorizerRefreshToken){
		this.authorizerRefreshToken = authorizerRefreshToken;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  授权方接口调用凭据获取时间
	 */
	@Column(name ="AUTHORIZER_ACCESS_TOKEN_TIME",nullable=true)
	public java.util.Date getAuthorizerAccessTokenTime(){
		return this.authorizerAccessTokenTime;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  授权方接口调用凭据获取时间
	 */
	public void setAuthorizerAccessTokenTime(java.util.Date authorizerAccessTokenTime){
		this.authorizerAccessTokenTime = authorizerAccessTokenTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接入类型 0：扫一扫接入。1：配置方式接入
	 */
	@Column(name ="ACCOUNT_AUTHORIZE_TYPE",nullable=true,length=1)
	public java.lang.String getAccountAuthorizeType(){
		return this.accountAuthorizeType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接入类型 0：扫一扫接入。1：配置方式接入
	 */
	public void setAccountAuthorizeType(java.lang.String accountAuthorizeType){
		this.accountAuthorizeType = accountAuthorizeType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
	 */
	@Column(name ="VERIFY_TYPE_INFO",nullable=true,length=255)
	public java.lang.String getVerifyTypeInfo(){
		return this.verifyTypeInfo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
	 */
	public void setVerifyTypeInfo(java.lang.String verifyTypeInfo){
		this.verifyTypeInfo = verifyTypeInfo;
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
	 *@return: java.lang.String  其他功能开通情况（以json数据存入数据库）
	 */
	@Column(name ="BUSINESS_INFO",nullable=true,length=200)
	public java.lang.String getBusinessInfo(){
		return this.businessInfo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  其他功能开通情况（以json数据存入数据库）
	 */
	public void setBusinessInfo(java.lang.String businessInfo){
		this.businessInfo = businessInfo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  二维码
	 */
	@Column(name ="QRCODE_URL",nullable=true,length=500)
	public java.lang.String getQrcodeUrl(){
		return this.qrcodeUrl;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  二维码
	 */
	public void setQrcodeUrl(java.lang.String qrcodeUrl){
		this.qrcodeUrl = qrcodeUrl;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  公众号授权给开发者的权限集列表（以逗号分隔）
	 */
	@Column(name ="FUNC_INFO",nullable=true,length=500)
	public java.lang.String getFuncInfo(){
		return this.funcInfo;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  公众号授权给开发者的权限集列表（以逗号分隔）
	 */
	public void setFuncInfo(java.lang.String funcInfo){
		this.funcInfo = funcInfo;
	}
}
