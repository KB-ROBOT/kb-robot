package com.kbrobot.controller.core;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.hibernate.qbc.PageList;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.service.RobotQuestionServiceI;

/**   
 * @Title: Controller
 * @Description: 知识库
 * @author 刘维
 * @date 2016-08-03 10:48:34
 * @version V1.0   
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/robotQuestionController")
public class RobotQuestionController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RobotQuestionController.class);

	@Autowired
	private RobotQuestionServiceI robotQuestionService;
	@Autowired
	private SystemService systemService;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 分页
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "questionList")
	public ModelAndView questionList(HttpServletRequest request){
		
		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();
		//当前页码
		String curPageNO = request.getParameter("curPageNO");
		if(StringUtil.isEmpty(curPageNO)){
			curPageNO = "1";
		}
		//
		//request.getParameter("")
		
		CriteriaQuery cq = new CriteriaQuery(RobotQuestionEntity.class, Integer.valueOf(curPageNO));
		cq.eq("accoundId", accountId);
		//cq.like("", "");
		cq.setPageSize(3);
		cq.setMyAction("./robotQuestionController.do?questionList");
		PageList questionPageList = systemService.getPageList(cq, true);
		
		//List<RobotQuestionEntity> questionList = robotQuestionService.findByProperty(RobotQuestionEntity.class, "accoundId", accountId);
		request.setAttribute("questionPageList", questionPageList);
		
		return new ModelAndView("kbrobot/overview-Q&A");
	}
	


	/**
	 * 知识库列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "robotQuestion")
	public ModelAndView robotQuestion(HttpServletRequest request) {
		return new ModelAndView("com/buss/com.kbrobot.entity/robotQuestionList");
	}

	/**
	 * 删除知识库
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(RobotQuestionEntity robotQuestion, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		robotQuestion = systemService.getEntity(RobotQuestionEntity.class, robotQuestion.getId());
		message = "知识库删除成功";
		robotQuestionService.delete(robotQuestion);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加知识库
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(RobotQuestionEntity robotQuestion, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(robotQuestion.getId())) {
			message = "知识库更新成功";
			RobotQuestionEntity t = robotQuestionService.get(RobotQuestionEntity.class, robotQuestion.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(robotQuestion, t);
				robotQuestionService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "知识库更新失败";
			}
		} else {
			message = "知识库添加成功";
			robotQuestionService.save(robotQuestion);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 知识库列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(RobotQuestionEntity robotQuestion, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(robotQuestion.getId())) {
			robotQuestion = robotQuestionService.getEntity(RobotQuestionEntity.class, robotQuestion.getId());
			req.setAttribute("robotQuestionPage", robotQuestion);
		}
		return new ModelAndView("com/buss/com.kbrobot.entity/robotQuestion");
	}
}
