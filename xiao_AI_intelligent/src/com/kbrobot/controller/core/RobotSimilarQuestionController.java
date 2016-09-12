package com.kbrobot.controller.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kbrobot.entity.RobotSimilarQuestionEntity;
import com.kbrobot.service.RobotSimilarQuestionServiceI;
import com.kbrobot.task.QuestionWordSplitTask;

/**
 * 
 * @author 刘维
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/robotSimilarQuestionController")
public class RobotSimilarQuestionController  extends BaseController {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(RobotQuestionController.class);
	@Autowired
	private SystemService systemService;

	@Autowired
	private RobotSimilarQuestionServiceI robotSimilarQuestionService;
	
	@Autowired
	private QuestionWordSplitTask questionWordSplitTask;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 添加或编辑相似问题
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(RobotSimilarQuestionEntity robotSimilarQuestion, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		
		String accountId = ResourceUtil.getWeiXinAccountId();
		
		if (StringUtil.isNotEmpty(robotSimilarQuestion.getId())) {
			message = "知识库更新成功";
			RobotSimilarQuestionEntity t = robotSimilarQuestionService.get(RobotSimilarQuestionEntity.class, robotSimilarQuestion.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(robotSimilarQuestion, t);
				robotSimilarQuestionService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "知识库更新失败";
			}
		} else {
			message = "知识库添加成功";
			//获取当前微信账户id
			try{
				robotSimilarQuestion.setAccountId(accountId);
				robotSimilarQuestionService.save(robotSimilarQuestion);
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
			catch (Exception e) {
				e.printStackTrace();
				message = "知识库添加失败";
			}
		}
		//添加成功之后执行一次任务
		new Thread(new Runnable() {

			@Override
			public void run() {
				questionWordSplitTask.questionWordSplit();
			}
		}).start();
		
		j.setMsg(message);
		return j;
	}

	/**
	 * 相似问题删除
	 * @param similarQuestion
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(RobotSimilarQuestionEntity similarQuestion, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		similarQuestion = systemService.getEntity(RobotSimilarQuestionEntity.class, similarQuestion.getId());
		message = "相似问题删除成功";
		robotSimilarQuestionService.delete(similarQuestion);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

}
