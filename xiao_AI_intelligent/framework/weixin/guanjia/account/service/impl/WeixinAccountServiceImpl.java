package weixin.guanjia.account.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.LogAnnotation;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeewx.api.core.exception.WexinReqException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.core.util.WeixinUtil;
import weixin.p3.oauth2.def.WeiXinOpenConstants;

@Service("weixinAccountService")
@Transactional
public class WeixinAccountServiceImpl extends CommonServiceImpl implements
		WeixinAccountServiceI {

	@Override
	public <T> void delete(T entity) {
		super.delete(entity);
		// 执行删除操作配置的sql增强
		this.doDelSql((WeixinAccountEntity) entity);
	}

	@Override
	public <T> Serializable save(T entity) {
		Serializable t = super.save(entity);
		// 执行新增操作配置的sql增强
		this.doAddSql((WeixinAccountEntity) entity);
		return t;
	}

	@Override
	public <T> void saveOrUpdate(T entity) {
		super.saveOrUpdate(entity);
		// 执行更新操作配置的sql增强
		this.doUpdateSql((WeixinAccountEntity) entity);
	}

	/**
	 * 默认按钮-sql增强-新增操作
	 * 
	 * @param id
	 * @return
	 */
	public boolean doAddSql(WeixinAccountEntity t) {
		return true;
	}

	/**
	 * 默认按钮-sql增强-更新操作
	 * 
	 * @param id
	 * @return
	 */
	public boolean doUpdateSql(WeixinAccountEntity t) {
		return true;
	}

	/**
	 * 默认按钮-sql增强-删除操作
	 * 
	 * @param id
	 * @return
	 */
	public boolean doDelSql(WeixinAccountEntity t) {
		return true;
	}

	public String getAccessToken() {
		String token = "";
		WeixinAccountEntity account = findLoginWeixinAccount();
		token = account.getAccountAccesstoken();
		if (token != null && !"".equals(token)) {
			// 判断有效时间 是否超过2小时
			java.util.Date end = new java.util.Date();
			java.util.Date start = new java.util.Date(account.getAddTokenTime()
					.getTime());
			if ((end.getTime() - start.getTime()) / 1000 / 3600 >= 2) {
				// 失效 重新获取
				String requestUrl = WeixinUtil.access_token_url.replace(
						"APPID", account.getAccountAppid()).replace(
						"APPSECRET", account.getAccountAppsecret());
				JSONObject jsonObject = WeixinUtil.httpsRequest(requestUrl,
						"GET", null);
				if (null != jsonObject) {
					try {
						token = jsonObject.getString("access_token");
						// 重置token
						account.setAccountAccesstoken(token);
						// 重置事件
						account.setAddTokenTime(new Date());
						this.saveOrUpdate(account);
					} catch (Exception e) {
						token = null;
						// 获取token失败
						String wrongMessage = "获取token失败 errcode:{} errmsg:{}"
								+ jsonObject.getInt("errcode")
								+ jsonObject.getString("errmsg");
					}
				}
			} else {
				return account.getAccountAccesstoken();
			}
		}
		else {
			if(StringUtil.isNotEmpty(account.getAccountAppsecret())){
				String requestUrl = WeixinUtil.access_token_url.replace("APPID",account.getAccountAppid()).replace("APPSECRET", account.getAccountAppsecret());
				JSONObject jsonObject = WeixinUtil.httpsRequest(requestUrl, "GET", null);
				if (null != jsonObject) {
					try {
						token = jsonObject.getString("access_token");
						// 重置token
						account.setAccountAccesstoken(token);
						// 重置事件
						account.setAddTokenTime(new Date());
						this.saveOrUpdate(account);
					} catch (Exception e) {
						token = null;
						// 获取token失败
						String wrongMessage = "获取token失败 errcode:{} errmsg:{}"
								+ jsonObject.getInt("errcode")
								+ jsonObject.getString("errmsg");
					}
				}
			}
		}
		return token;
	}
	
	
	public String getAccessToken(String accountId) {
		
		WeixinAccountEntity weixinAccountEntity = this.findUniqueByProperty(WeixinAccountEntity.class, "id", accountId);
		String token = weixinAccountEntity.getAccountAccesstoken();
		if (token != null && !"".equals(token)) {
			// 判断有效时间 是否超过2小时
			java.util.Date end = new java.util.Date();
			java.util.Date start = new java.util.Date(weixinAccountEntity.getAddTokenTime().getTime());
			if ((end.getTime() - start.getTime()) / 1000.0f / 3600.0f >= 1.83f) {//提前刷新 1.83约等于1小时50分钟
				// 失效 重新获取
				String requestUrl = WeixinUtil.access_token_url.replace(
						"APPID", weixinAccountEntity.getAccountAppid()).replace(
						"APPSECRET", weixinAccountEntity.getAccountAppsecret());
				JSONObject jsonObject = WeixinUtil.httpsRequest(requestUrl,
						"GET", null);
				if (null != jsonObject) {
					try {
						token = jsonObject.getString("access_token");
						// 重置token
						weixinAccountEntity.setAccountAccesstoken(token);
						// 重置事件
						weixinAccountEntity.setAddTokenTime(new Date());
						this.saveOrUpdate(weixinAccountEntity);
					} catch (Exception e) {
						token = null;
						// 获取token失败
						String wrongMessage = "获取token失败 errcode:{} errmsg:{}"
								+ jsonObject.getInt("errcode")
								+ jsonObject.getString("errmsg");
						LogUtil.info(wrongMessage);
					}
				}
			} else {
				return weixinAccountEntity.getAccountAccesstoken();
			}
		} else {
			String requestUrl = WeixinUtil.access_token_url.replace("APPID",
					weixinAccountEntity.getAccountAppid()).replace("APPSECRET",
							weixinAccountEntity.getAccountAppsecret());
			JSONObject jsonObject = WeixinUtil.httpsRequest(requestUrl, "GET",null);
			if (null != jsonObject) {
				try {
					token = jsonObject.getString("access_token");
					// 重置token
					weixinAccountEntity.setAccountAccesstoken(token);
					// 重置事件
					weixinAccountEntity.setAddTokenTime(new Date());
					this.saveOrUpdate(weixinAccountEntity);
				} catch (Exception e) {
					token = null;
					// 获取token失败
					String wrongMessage = "获取token失败 errcode:{} errmsg:{}"
							+ jsonObject.getInt("errcode")
							+ jsonObject.getString("errmsg");
					LogUtil.info(wrongMessage);
				}
			}
		}
		return token;
	}

	public WeixinAccountEntity findLoginWeixinAccount() {
		TSUser user = ResourceUtil.getSessionUserName();
		List<WeixinAccountEntity> acclst = this.findByProperty(WeixinAccountEntity.class, "userName", user.getUserName());
		//返回查询到的第一个微信账户
		WeixinAccountEntity weixinAccountEntity = acclst.size() != 0 ? acclst.get(0) : null;
		if (weixinAccountEntity != null) {
			return weixinAccountEntity;
		} else {
			weixinAccountEntity = new WeixinAccountEntity();
			// 返回个临时对象，防止空指针
			weixinAccountEntity.setWeixinAccountId("-1");
			weixinAccountEntity.setId("-1");
			return weixinAccountEntity;
		}
	}

	@Override
	public List<WeixinAccountEntity> findByUsername(String username) {
		List<WeixinAccountEntity> acclst = this.findByProperty(WeixinAccountEntity.class, "userName", username);
		return acclst;
	}

	@Override
	public WeixinAccountEntity findByToUsername(String toUserName) {
		return this.findUniqueByProperty(WeixinAccountEntity.class, "weixinAccountId", toUserName);
	}

	/**
	 * 替换sql中的变量
	 * 
	 * @param sql
	 * @return
	 */
	public String replaceVal(String sql, WeixinAccountEntity t) {
		sql = sql.replace("#{id}", String.valueOf(t.getId()));
		sql = sql.replace("#{accountname}", String.valueOf(t.getAccountName()));
		sql = sql.replace("#{accounttoken}",
				String.valueOf(t.getAccountToken()));
		sql = sql.replace("#{accountnumber}",
				String.valueOf(t.getAccountNumber()));
		sql = sql.replace("#{accounttype}", String.valueOf(t.getAccountType()));
		sql = sql.replace("#{accountemail}",
				String.valueOf(t.getAccountEmail()));
		sql = sql.replace("#{accountdesc}", String.valueOf(t.getAccountDesc()));
		sql = sql.replace("#{accountappid}",
				String.valueOf(t.getAccountAppid()));
		sql = sql.replace("#{accountappsecret}",
				String.valueOf(t.getAccountAppsecret()));
		sql = sql.replace("#{accountaccesstoken}",
				String.valueOf(t.getAccountAccesstoken()));
		sql = sql.replace("#{addtoekntime}",
				String.valueOf(t.getAddTokenTime()));
		sql = sql.replace("#{UUID}", UUID.randomUUID().toString());
		return sql;
	}
	
	/**
	 * 通过微信原始ID，获取系统微信公众账号配置信息
	 * @param weixinId
	 * @return
	 */
	public WeixinAccountEntity getWeixinAccountByWeixinOldId(String weixinId){
		if(oConvertUtils.isEmpty(weixinId)){
			return null;
		}
		List<WeixinAccountEntity> weixinAccounts = this.findByProperty(WeixinAccountEntity.class, "weixin_accountid", weixinId);
		if(weixinAccounts!=null){
			return weixinAccounts.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 重置 AccessToken
	 * @return
	 * @throws WexinReqException 
	 */
	@LogAnnotation(operateDescribe="重置Token",operateFuncNm="resetAccessToken",operateModelNm="AjaxJson")
	public AjaxJson resetAccessToken(String accountid) throws WexinReqException {
		AjaxJson json = new AjaxJson();
		String token = "";
		Date getAccessTokenDate = new Date();
		WeixinAccountEntity account  = this.get(WeixinAccountEntity.class, accountid);
		token = account.getAccountAccesstoken();
		String requestUrl = WeixinUtil.access_token_url.replace("APPID",account.getAccountAppid()).replace("APPSECRET",account.getAccountAppsecret());
		JSONObject jsonObject = WeixinUtil.httpsRequest(requestUrl, "GET",null);
		
		if (null != jsonObject) {
			if (jsonObject.has("errcode") && jsonObject.getInt("errcode") != 0) {
				//update-begin----author:scott---------date:20150719-------for:提示信息优化----------------------
				String errormsg = "很抱歉，系统异常，请联系管理员!";
				if(jsonObject.containsKey("errcode")){
					errormsg = errormsg + "　错误码:"+jsonObject.get("errcode");
				}
				json.setMsg(errormsg);
				//update-end----author:scott---------date:20150719-------for:提示信息优化----------------------
				json.setSuccess(false);
				return json;
			}
			try {
				token = jsonObject.getString("access_token");
				// 重置token
				account.setAccountAccesstoken(token);
				// 重置事件
				account.setAddTokenTime(getAccessTokenDate);
				
				//--update-begin---author：scott-------date:20151026--------for:重置Token扩展支持Apiticket、jsapi_ticket-------------------------
				try {
					//[2].获取api凭证
//					GetticketRtn getticketRtn = JwQrcodeAPI.doGetticket(token);
//					if (null != getticketRtn) {
//						try {
//							// 重置token
//							account.setApiticket(getticketRtn.getTicket());
//							// 重置事件
//							account.setApiticketttime(getAccessTokenDate);
//							LogUtil.info("---------定时任务重置超过2小时失效token------------------"+"获取Apiticket成功");
//						} catch (Exception e) {
//							// 获取api凭证失败
//							String wrongMessage = "获取api凭证失败 errcode:{"+ getticketRtn.getErrcode()+"} errmsg:{"+getticketRtn.getErrmsg()+"}";
//							LogUtil.info(wrongMessage);
//						}
//					}
				} catch (Exception e) {
					LogUtil.info("---------------------定时任务异常--【获取api凭证】--------------"+e.toString());
				}
				//[3].获取jsapi凭证
				try {
					String jsapiticket = null;
					String jsapi_ticket_url = WeiXinOpenConstants.JSAPI_TICKET_URL.replace("ACCESS_TOKEN", token);
					JSONObject jsapi_ticket_json = WeixinUtil.httpsRequest(jsapi_ticket_url, "GET", null);
					if (null != jsapi_ticket_json) {
						try {
							jsapiticket = jsapi_ticket_json.getString("ticket");
							// 重置token
							account.setJsApiTicket(jsapiticket);
							// 重置事件
							account.setJsApiTicketTime(getAccessTokenDate);
							LogUtil.info("---------定时任务重置超过2小时失效token------------------"+"获取Jsapiticket成功");
						} catch (Exception e) {
							//获取jsapi凭证失败
							String wrongMessage = "获取jsapi凭证失败 errcode:{"+ (jsonObject.containsKey("errcode")?jsonObject.get("errcode"):"") +"} errmsg:{"+ (jsonObject.containsKey("errmsg")?jsonObject.getString("errmsg"):"") +"}";
							LogUtil.info(wrongMessage);
						}
					}
				} catch (Exception e) {
					LogUtil.info("---------------------定时任务异常--【获取jsapi凭证】--------------"+e.toString());
				}
				//--update-end---author：scott-------date:20151026--------for:重置Token扩展支持Apiticket、jsapi_ticket-------------------------
				this.saveOrUpdate(account);
			} catch (Exception e) {
				token = null;
				// 获取token失败
				String wrongMessage = "获取token失败 errcode:{ "+ jsonObject.getInt("errcode")+" } errmsg:{ "+ jsonObject.getString("errmsg") +" }";
				json.setMsg(wrongMessage);
				json.setSuccess(false);
				return json;
			}
		}
		json.setSuccess(true);
		return json;
	}
}