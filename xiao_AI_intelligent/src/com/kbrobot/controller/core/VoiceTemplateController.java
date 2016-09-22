package com.kbrobot.controller.core;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.VoiceTemplate;

/**
 * 语义理解
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/voiceTemplateController")
public class VoiceTemplateController extends BaseController {
	
	@Autowired
	private SystemService systemService;
	
	private String message;
	
	/**
	 * 后台文本消息
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "voiceList")
	public ModelAndView getTestList(ModelMap modelMap,HttpServletRequest request) {
		
		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();
		List<VoiceTemplate> voiceTemplateList =   systemService.findByProperty(VoiceTemplate.class, "accountId", accountId);
		modelMap.put("voiceTemplateList", voiceTemplateList);
		
		return new ModelAndView("kbrobot/message-voiceList");
	}
	
	/**
	 * 保存文本模板修改
	 * @param textTemplate
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "doSave")
	@ResponseBody
	public AjaxJson doSave(VoiceTemplate voiceTemplate, HttpServletRequest req) {

		AjaxJson j = new AjaxJson();
		String id = voiceTemplate.getId();
		if (StringUtil.isNotEmpty(id)) {
			VoiceTemplate tempAutoResponse = this.systemService.getEntity(VoiceTemplate.class, voiceTemplate.getId());
			this.message = "修改关文本模板成功！";
			try {
				MyBeanUtils.copyBeanNotNull2Bean(voiceTemplate, tempAutoResponse);
				this.systemService.saveOrUpdate(tempAutoResponse);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			String accountId = ResourceUtil.getWeiXinAccountId();
			if (!"-1".equals(accountId)) {
				voiceTemplate.setAccountId(accountId);
				voiceTemplate.setAddTime(new Date());
				this.systemService.save(voiceTemplate);
			} else {
				j.setSuccess(false);
				j.setMsg("请添加一个公众帐号。");
			}
		}
		return j;
	}
	
	
	/**
	 * 删除信息
	 * @param textTemplate
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(VoiceTemplate voiceTemplate, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		voiceTemplate = this.systemService.getEntity(VoiceTemplate.class, voiceTemplate.getId());
		this.systemService.delete(voiceTemplate);

		message = "删除数据成功！";
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(this.message);
		return j;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "goEdit")
	@ResponseBody
	public AjaxJson goEdit(HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		
		String id = req.getParameter("id");
		req.setAttribute("id", id);
		if (StringUtil.isNotEmpty(id)) {
			VoiceTemplate voiceTemplate = this.systemService.getEntity(VoiceTemplate.class, id);
			j.setObj(voiceTemplate);
			j.setSuccess(true);
		}
		else{
			j.setMsg("未找到该素材！");
			j.setSuccess(false);
		}
		return j;
	}
	
}
