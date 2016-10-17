package com.kbrobot.controller.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.wxsendmsg.JwKfaccountAPI;
import org.jeewx.api.wxsendmsg.model.WxKfaccount;
import org.jeewx.api.wxuser.user.JwUserAPI;
import org.jeewx.api.wxuser.user.model.Wxuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.CustomServiceEntity;
import com.kbrobot.utils.WeixinThirdUtil;

import weixin.guanjia.account.entity.WeixinAccountEntity;

@Controller
@RequestMapping("/customServiceController")
public class CustomServiceController {

	@Autowired
	private SystemService systemService;

	WeixinThirdUtil weixinThirdUtilInstance = WeixinThirdUtil.getInstance();

	/**
	 * 客服列表
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "sustomServiceList")
	public ModelAndView sustomServiceList(ModelMap modelMap,HttpServletRequest request){

		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();

		List<CustomServiceEntity> customList = systemService.findByProperty(CustomServiceEntity.class, "accountId", weixinAccountEntity.getId());
		modelMap.put("customList", customList);

		return new ModelAndView("kbrobot/customService");
	}

	/**
	 * 刷新同步客服列表
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "refreshCustomList")
	@ResponseBody
	public AjaxJson refreshCustomList(HttpServletRequest request){
		AjaxJson j = new AjaxJson();

		List<CustomServiceEntity> entityList = new ArrayList<CustomServiceEntity>();
		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();
		List<WxKfaccount> kfAccountList = null;
		try {
			kfAccountList = JwKfaccountAPI.getAllKfaccount(weixinThirdUtilInstance.getAuthorizerAccessToken(weixinAccountEntity.getWeixinAccountId()));

			for(WxKfaccount kfAccount:kfAccountList){
				CustomServiceEntity entity = new CustomServiceEntity();
				/**
				 * 根据客服账号查找是否存在
				 */
				List<CustomServiceEntity> customList = systemService.findByProperty(CustomServiceEntity.class, "kfAccount", kfAccount.getKf_account());

				if(!customList.isEmpty()){
					CustomServiceEntity existCustom = customList.get(0);

					existCustom.setKfId(kfAccount.getKf_id());
					existCustom.setKfAccount(kfAccount.getKf_account());
					existCustom.setKfHeadimgurl(kfAccount.getKf_headimgurl());
					existCustom.setKfNick(kfAccount.getKf_nick());

					existCustom.setKfWx(kfAccount.getKf_wx());

					existCustom.setInviteWx(kfAccount.getInvite_wx());
					existCustom.setInviteStatus(kfAccount.getInvite_status());
					existCustom.setInviteExpireTime(kfAccount.getInvite_expire_time());
				}
				else{
					/**
					 * 必有值
					 */
					entity.setKfId(kfAccount.getKf_id());
					entity.setKfAccount(kfAccount.getKf_account());
					entity.setKfHeadimgurl(kfAccount.getKf_headimgurl());
					entity.setKfNick(kfAccount.getKf_nick());

					if(StringUtil.isNotEmpty(kfAccount.getKf_wx())){
						/**
						 * 绑定成功后的参数
						 */
						entity.setKfWx(kfAccount.getKf_wx());
					}
					else if(StringUtil.isNotEmpty(kfAccount.getInvite_wx())){
						/**
						 * 发送绑定之后才会有
						 */
						entity.setInviteWx(kfAccount.getInvite_wx());
						entity.setInviteStatus(kfAccount.getInvite_status());
						entity.setInviteExpireTime(kfAccount.getInvite_expire_time());
					}
					/**
					 * 最后设置所属公众号
					 */
					entity.setAccountId(weixinAccountEntity.getId());

					entityList.add(entity);
				}
			}

			/**
			 * 同步完成，批量存储
			 */
			systemService.batchSave(entityList);

			j.setSuccess(true);
		}
		catch (WexinReqException e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}

	@RequestMapping(params = "bindWeixinAccount")
	@ResponseBody
	public AjaxJson bindWeixinAccount(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String kfAccount = request.getParameter("kfAccount");
		String inviteWx = request.getParameter("inviteWx");

		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();
		try {
			JwKfaccountAPI.bindWeixinAccount(weixinThirdUtilInstance.getAuthorizerAccessToken(weixinAccountEntity.getWeixinAccountId()), kfAccount, inviteWx);
			j.setSuccess(true);
		}
		catch (WexinReqException e) {
			e.printStackTrace();
			String errorMsg = e.getMessage();

			if(errorMsg.contains("65400")){
				j.setMsg("API不可用");
			}
			else if(errorMsg.contains("65401")){
				j.setMsg("无效客服帐号");
			}
			else if(errorMsg.contains("65407")){
				j.setMsg("邀请对象已经是本公众号客服");
			}
			else if(errorMsg.contains("65408")){
				j.setMsg("本公众号已发送邀请给该微信号");
			}
			else if(errorMsg.contains("65409")){
				j.setMsg("无效的微信号");
			}
			else if(errorMsg.contains("65410")){
				j.setMsg("邀请对象绑定公众号客服数量达到上限（目前每个微信号最多可以绑定5个公众号客服帐号）");
			}
			else if(errorMsg.contains("65411")){
				j.setMsg("该帐号已经有一个等待确认的邀请，不能重复邀请");
			}
			else if(errorMsg.contains("65412")){
				j.setMsg("该帐号已经绑定微信号，不能进行邀请");
			}
			j.setSuccess(false);
		}

		return j;
	}

	@RequestMapping(params = "sustomServiceAdd")
	@ResponseBody
	public AjaxJson sustomServiceAdd(HttpServletRequest request){
		AjaxJson j = new AjaxJson();

		String nickname = request.getParameter("nickname");
		String accountNum = request.getParameter("accountNum");
		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();

		String kf_account = accountNum+"@"+weixinAccountEntity.getAccountNumber();
		try {
			JwKfaccountAPI.addKfacount(weixinThirdUtilInstance.getAuthorizerAccessToken(weixinAccountEntity.getWeixinAccountId()), kf_account, nickname);

			j.setSuccess(true);
		}
		catch (WexinReqException e) {
			e.printStackTrace();
			String errorMsg = e.getMessage();
			if(errorMsg.contains("65400")){
				j.setMsg("API不可用");
			}
			else if(errorMsg.contains("65403")){
				j.setMsg("客服昵称不合法");
			}
			else if(errorMsg.contains("65404")){
				j.setMsg("客服帐号不合法");
			}
			else if(errorMsg.contains("65405")){
				j.setMsg("帐号数目已达到上限，不能继续添加");
			}
			else if(errorMsg.contains("65406")){
				j.setMsg("已经存在的客服帐号");
			}
			j.setSuccess(false);
		}

		return j;
	}

	@RequestMapping(params = "sustomServiceDel")
	@ResponseBody
	public AjaxJson sustomServiceDel(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();

		String customId = request.getParameter("customId");
		CustomServiceEntity delEntity = systemService.get(CustomServiceEntity.class, customId);
		try {
			JwKfaccountAPI.deleteKfaccount(weixinThirdUtilInstance.getAuthorizerAccessToken(weixinAccountEntity.getWeixinAccountId()), delEntity.getKfAccount());
			systemService.delete(delEntity);
			j.setSuccess(true);
		}
		catch (WexinReqException e) {
			e.printStackTrace();
			String errorMsg = e.getMessage();
			if(errorMsg.contains("65400")){
				j.setMsg("API不可用");
			}
			else if(errorMsg.contains("65401")){
				j.setMsg("无效客服帐号");
			}
			j.setSuccess(false);
		}

		return j;
	}

	@RequestMapping(params = "sustomServiceEdit")
	@ResponseBody
	public AjaxJson sustomServiceEdit(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();

		String customId = request.getParameter("customid");
		String nickname = request.getParameter("nickname");
		String headImg = request.getParameter("headImg");
		headImg = ResourceUtil.getConfigByName("fileRootPath") + headImg;//图片完整路径
		
		CustomServiceEntity editEntity = systemService.get(CustomServiceEntity.class, customId);
		try {
			//JwKfaccountAPI.deleteKfaccount(weixinThirdUtilInstance.getAuthorizerAccessToken(weixinAccountEntity.getWeixinAccountId()), delEntity.getKfAccount());
			//systemService.delete(delEntity);
			
			JwKfaccountAPI.uploadKfaccountHeadimg(weixinThirdUtilInstance.getAuthorizerAccessToken(weixinAccountEntity.getWeixinAccountId()), editEntity.getKfAccount(), headImg);
			JwKfaccountAPI.modifyKfaccount(weixinThirdUtilInstance.getAuthorizerAccessToken(weixinAccountEntity.getWeixinAccountId()), editEntity.getKfAccount(), nickname);
			
			j.setSuccess(true);
		}
		catch (WexinReqException e) {
			e.printStackTrace();
			String errorMsg = e.getMessage();

			if(errorMsg.contains("65400")){
				j.setMsg("API不可用");
			}
			else if(errorMsg.contains("65401")){
				j.setMsg("无效客服帐号");
			}
			else if(errorMsg.contains("65403")){
				j.setMsg("客服昵称不合法");
			}
			else if(errorMsg.contains("40005")){
				j.setMsg("不支持的媒体类型");
			}
			else if(errorMsg.contains("40009")){
				j.setMsg("图片太大了");
			}
			j.setSuccess(false);
		}

		return j;
	}

	/*@RequestMapping(params = "checkOpenId")
	@ResponseBody
	public AjaxJson checkOpenId(HttpServletRequest request){
		AjaxJson j = new AjaxJson();

		String openId = request.getParameter("openId");
		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();
		try {

			Wxuser wxUser = JwUserAPI.getWxuser(weixinThirdUtilInstance.getAuthorizerAccessToken(weixinAccountEntity.getWeixinAccountId()), openId);

			Map<String,Object> attr = new HashMap<String,Object>();
			attr.put("wxUser", wxUser);
			j.setAttributes(attr);
			j.setSuccess(true);
		}
		catch (WexinReqException e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
			j.setSuccess(false);
		}
		return j;
	}*/

}
