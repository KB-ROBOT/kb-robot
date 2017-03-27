package com.kbrobot.controller.core;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.hibernate.qbc.PageList;
import org.jeecgframework.core.common.model.common.UploadFile;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSDocument;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.service.RobotQuestionServiceI;
import com.kbrobot.task.QuestionWordSplitTask;

import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.util.DateUtils;

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

		CriteriaQuery cq = new CriteriaQuery(RobotQuestionEntity.class, Integer.valueOf(curPageNO));
		//搜索参数
		String searchKey = request.getParameter("searchKey");
		String searchParam = request.getParameter("searchParam");
		String questionTitle = request.getParameter("questionTitle");
		String questionAnswer = request.getParameter("questionAnswer");
		if(StringUtil.isNotEmpty(searchKey)){
			cq.like(searchKey, searchParam);
			request.setAttribute("searchParam", searchParam);
			request.setAttribute("searchKey", searchKey);
		}
		else{
			if(StringUtil.isNotEmpty(questionTitle)){
				cq.like("questionTitle", questionTitle);
				request.setAttribute("searchParam", questionTitle);
				request.setAttribute("searchKey", "questionTitle");
			}
			else if(StringUtil.isNotEmpty(questionAnswer)){
				cq.like("questionAnswer", questionAnswer);
				request.setAttribute("searchParam", questionAnswer);
				request.setAttribute("searchKey", "questionAnswer");
			}
		}

		cq.eq("accountId", accountId);
		cq.setPageSize(15);
		cq.setMyAction("./robotQuestionController.do?questionList");
		cq.addOrder("createTime", SortDirection.desc);//根据时间顺寻排序
		cq.add();//加载条件
		PageList questionPageList = systemService.getPageList(cq, true);

		request.setAttribute("questionPageList", questionPageList);


		return new ModelAndView("kbrobot/overview-Q&A");
	}
	
	/**
	 * 分页
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "questionQuery")
	public ModelAndView questionQuery(HttpServletRequest request){

		//获取当前微信账户id
		String accountId = request.getParameter("accountId");
		request.setAttribute("accountId", accountId);
		
		//当前页码
		String curPageNO = request.getParameter("curPageNO");
		if(StringUtil.isEmpty(curPageNO)){
			curPageNO = "1";
		}

		CriteriaQuery cq = new CriteriaQuery(RobotQuestionEntity.class, Integer.valueOf(curPageNO));
		//搜索参数
		String searchKey = request.getParameter("searchKey");
		String searchParam = request.getParameter("searchParam");
		String questionTitle = request.getParameter("questionTitle");
		String questionAnswer = request.getParameter("questionAnswer");
		
		if(StringUtil.isNotEmpty(accountId)&&(StringUtil.isNotEmpty(searchParam)||StringUtil.isNotEmpty(questionTitle)||StringUtil.isNotEmpty(questionAnswer))){
			if(StringUtil.isNotEmpty(searchKey)){
				request.setAttribute("searchKey", searchKey);
			}
			else{
				if(StringUtil.isNotEmpty(questionTitle)){
					searchKey = "questionTitle";
					request.setAttribute("searchKey", "questionTitle");
				}
				else if(StringUtil.isNotEmpty(questionAnswer)){
					searchKey = "questionAnswer";
					request.setAttribute("searchKey", "questionAnswer");
				}
				searchParam = StringUtil.isNotEmpty(questionTitle)?questionTitle:questionAnswer;
			}
			
			request.setAttribute("searchParam", searchParam);
			String[] searchParamArray = searchParam.split(" ");
			//遍历参数
			for(String param : searchParamArray){
				cq.like(searchKey, param);
			}

			cq.eq("accountId", accountId);
			cq.setPageSize(15);
			cq.setMyAction("./robotQuestionController.do?questionQuery");
			cq.addOrder("createTime", SortDirection.desc);//根据时间顺寻排序
			cq.add();//加载条件
			PageList questionPageList = systemService.getPageList(cq, true);
			request.setAttribute("questionPageList", questionPageList);
		}
		return new ModelAndView("kbrobot/question-query");
	}

	/**
	 * 查看某条知识库的内容
	 * @param robotQuestion
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getQuestionDetail")
	@ResponseBody
	public AjaxJson getQuestionDetail(RobotQuestionEntity robotQuestion, HttpServletRequest request){
		AjaxJson j = new AjaxJson();

		if(StringUtil.isNotEmpty(robotQuestion.getId())){
			RobotQuestionEntity entiry = robotQuestionService.get(RobotQuestionEntity.class, robotQuestion.getId());
			if(entiry!=null){
				j.setSuccess(true);
				j.setObj(entiry);
			}
			else{
				this.message = "未找到该问题";
				j.setMsg(message);
				j.setSuccess(false);
			}

		}
		else{
			this.message = "参数有误";
			j.setMsg(message);
			j.setSuccess(false);
		}

		return j;
	}


	/**
	 * 知识库列表 页面跳转
	 * 
	 * @return
	 *//*
	@RequestMapping(params = "robotQuestion")
	public ModelAndView robotQuestion(HttpServletRequest request) {
		return new ModelAndView("com/buss/com.kbrobot.entity/robotQuestionList");
	}*/

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
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(RobotQuestionEntity robotQuestion, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(robotQuestion.getId())) {
			message = "知识库更新成功";
			RobotQuestionEntity t = robotQuestionService.get(RobotQuestionEntity.class, robotQuestion.getId());
			t.setUpdateTime(new Date());//设置更新时间
			try {
				MyBeanUtils.copyBeanNotNull2Bean(robotQuestion, t);
				t.setWordSplit(null);//更新后清空分词
				robotQuestionService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "知识库更新失败";
			}
		} else {
			message = "知识库添加成功";
			//获取当前微信账户id
			String accountId = ResourceUtil.getWeiXinAccountId();
			robotQuestion.setAccountId(accountId);
			robotQuestion.setCreateTime(new Date());
			robotQuestion.setUpdateTime(new Date());
			robotQuestionService.save(robotQuestion);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		//添加或更新完成之后立即执行分词定时任务
		splitWord();

		j.setMsg(message);
		return j;
	}
	
	@RequestMapping(params = "saveContextQuestion")
	@ResponseBody
	public AjaxJson saveContextQuestion(@RequestBody RobotQuestionEntity[] robotQuestionList) {
		AjaxJson j = new AjaxJson();
		
		if(robotQuestionList!=null&&robotQuestionList.length!=0){
			
			//获取当前微信账户id
			String accountId = ResourceUtil.getWeiXinAccountId();
			
			for(RobotQuestionEntity robotQuestion : robotQuestionList){
				robotQuestion.setAccountId(accountId);
				robotQuestion.setCreateTime(new Date());
				robotQuestion.setUpdateTime(new Date());
				robotQuestionService.save(robotQuestion);
			}
			message = "知识库添加成功";
		
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			
			//添加或更新完成之后立即执行分词定时任务
			splitWord();
		}
		else{
			message = "添加失败，问题不能为空";
			j.setSuccess(false);
		}
		

		j.setMsg(message);
		return j;
	}
	
	
	

	/**
	 * 知识库添加
	 * @return
	 */
	@RequestMapping(params = "goQuestionAdd")
	public ModelAndView goQuestionAdd(){
		return new ModelAndView("kbrobot/question-add");
	}
	
	@RequestMapping(params = "goQuestionContextAdd")
	public ModelAndView goQuestionContextAdd(){
		return new ModelAndView("kbrobot/question-add-context");
	}

	/**
	 * 取得答案的主体内容
	 * @return
	 */
	@RequestMapping(params = {"getQuestionAnswerContent","getQuestionAnswerContent="})
	public ModelAndView getQuestionAnswerContent(RobotQuestionEntity robotQuestion,HttpServletRequest request){
		RobotQuestionEntity entity = robotQuestionService.get(RobotQuestionEntity.class, robotQuestion.getId());
		request.setAttribute("question", entity);

		return new ModelAndView("kbrobot/questionAnswerContent");

	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "uploadImportQuestion")
	@ResponseBody
	public AjaxJson uploadImportQuestion(MultipartHttpServletRequest request, HttpServletResponse response){
		AjaxJson j = new AjaxJson();
		Map<String, Object> attributes = new HashMap<String, Object>();

		TSTypegroup tsTypegroup=systemService.getTypeGroup("fieltype","文档分类");
		TSType tsType = systemService.getType("files","附件", tsTypegroup);
		String fileKey = oConvertUtils.getString(request.getParameter("fileKey"));// 文件ID
		String documentTitle = oConvertUtils.getString(request.getParameter("documentTitle"));// 文件标题
		TSDocument document = new TSDocument();
		if (StringUtil.isNotEmpty(fileKey)) {
			document.setId(fileKey);
			document = systemService.getEntity(TSDocument.class, fileKey);
			document.setDocumentTitle(documentTitle);
		}
		document.setSubclassname(MyClassLoader.getPackPath(document));
		document.setCreatedate(DateUtils.gettimestamp());
		document.setTSType(tsType);

		UploadFile uploadFile = new UploadFile(request, document);
		uploadFile.setCusPath("temp");
		uploadFile.setByteField(null);
		//uploadFile.setSwfpath("swfpath"); //不需要swf预览
		try {
			//限制上传格式
			document = systemService.uploadFile(uploadFile,new String[]{"xls","xlsx"});
			Collection<?> importList = new ArrayList<RobotQuestionEntity>();
			if(StringUtil.isNotEmpty(document.getRealpath())){
				ImportParams importParams = new ImportParams();
				importList = ExcelImportUtil.importExcel(new File(request.getServletContext().getRealPath("/") + document.getRealpath()),RobotQuestionEntity.class, importParams);
				//importList = (List<RobotQuestionEntity>)importList;

				if(importList.size()>300){
					throw new Exception("导入条数超出最大限制");
				}
			}

			attributes.put("filePath", document.getRealpath());
			attributes.put("fileKey", document.getId());
			attributes.put("name", document.getAttachmenttitle());
			//attributes.put("viewhref", "commonController.do?openViewFile&fileid=" + document.getId());
			attributes.put("delurl", "commonController.do?delObjFile&fileKey=" + document.getId());
			j.setMsg("上传成功。共："+importList.size()+"条");
			j.setAttributes(attributes);
			j.setSuccess(true);
		}
		catch (Exception e ) {
			//e.printStackTrace();
			j.setMsg(e.getMessage());
			j.setSuccess(false);
		}

		return j;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(params = "importQuestion")
	@ResponseBody
	public AjaxJson importQuestion(HttpServletRequest request, HttpServletResponse response){
		AjaxJson j = new AjaxJson();

		String importFilePath = request.getParameter("importFilePath");
		String fileAbsolutePath = request.getServletContext().getRealPath("/") + importFilePath;
		Collection<?> importList = new ArrayList<RobotQuestionEntity>();
		ImportParams importParams = new ImportParams();
		importList = ExcelImportUtil.importExcel(new File(fileAbsolutePath),RobotQuestionEntity.class, importParams);

		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();

		for(Object question : importList){
			RobotQuestionEntity que = (RobotQuestionEntity)question;
			que.setAccountId(accountId);
			que.setCreateTime(new Date());
			que.setUpdateTime(new Date());
			String questionAnswer = que.getQuestionAnswer();
			questionAnswer = questionAnswer.replaceAll("\n", "<br/>").replaceAll(" ", "&nbsp;");
			que.setQuestionAnswer(questionAnswer);
		}

		robotQuestionService.batchSave((List<RobotQuestionEntity>)importList);

		//导入完成之后立即执行分词定时任务
		splitWord();

		return j;
	}

	public void splitWord(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				questionWordSplitTask.questionWordSplit();
			}
		}).start();
	}

}
