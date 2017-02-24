package com.kbrobot.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.dao.impl.CommonDao;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.hibernate.qbc.PageList;
import org.jeecgframework.core.common.model.common.UploadFile;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.web.system.pojo.base.TSDocument;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.CustomerFileEntity;

import weixin.util.DateUtils;

/**
 * 文件下载
 * ppt与table
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/commonFileDownloadController")
public class CommonFileDownloadController {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private CommonDao commonDao;

	@RequestMapping(params = "powerPointManageList")
	public ModelAndView goPowerPointManagePage(ModelMap modelMap,HttpServletRequest request){

		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();
		//当前页码
		String curPageNO = request.getParameter("curPageNO");
		if(StringUtil.isEmpty(curPageNO)){
			curPageNO = "1";
		}

		CriteriaQuery cq = new CriteriaQuery(CustomerFileEntity.class, Integer.valueOf(curPageNO));
		cq.eq("accountId", accountId);
		cq.eq("fileType", "powerPoint");
		cq.setPageSize(15);
		cq.setMyAction("./commonFileDownloadController.do?powerPointManageList");
		cq.add();//加载条件

		PageList pageList = systemService.getPageList(cq, true);
		modelMap.put("pageList", pageList);
		modelMap.put("accountId", accountId);

		return new ModelAndView("kbrobot/CommonPowerPointManage");
	}

	@RequestMapping(params = "excelManageList")
	public ModelAndView excelManageList(ModelMap modelMap,HttpServletRequest request){

		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();
		//当前页码
		String curPageNO = request.getParameter("curPageNO");
		if(StringUtil.isEmpty(curPageNO)){
			curPageNO = "1";
		}

		CriteriaQuery cq = new CriteriaQuery(CustomerFileEntity.class, Integer.valueOf(curPageNO));
		cq.eq("accountId", accountId);
		cq.eq("fileType", "excel");
		cq.setPageSize(15);
		cq.setMyAction("./commonFileDownloadController.do?excelManageList");
		cq.add();//加载条件

		PageList pageList = systemService.getPageList(cq, true);
		modelMap.put("pageList", pageList);
		modelMap.put("accountId", accountId);

		return new ModelAndView("kbrobot/CommonExcelManage");
	}

	@RequestMapping(params = "uploadFile")
	@ResponseBody
	public AjaxJson uploadFile(HttpServletRequest request ){

		AjaxJson j = new AjaxJson();

		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();

		String fileType = request.getParameter("fileType");

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
		uploadFile.setCusPath(fileType);
		uploadFile.setByteField(null);
		//uploadFile.setSwfpath("swfpath"); //不需要swf预览
		try {
			//限制上传格式
			if("excel".equals(fileType)){
				document = systemService.uploadFile(uploadFile,new String[]{"xls","xlsx"});
			}
			else{
				document = systemService.uploadFile(uploadFile,new String[]{"ppt","pptx","doc","docx"});
			}

			attributes.put("filePath", document.getRealpath());
			attributes.put("fileKey", document.getId());
			attributes.put("name", document.getAttachmenttitle());
			//attributes.put("viewhref", "commonController.do?openViewFile&fileid=" + document.getId());
			attributes.put("delurl", "commonController.do?delObjFile&fileKey=" + document.getId());

			//保存
			CustomerFileEntity fileEntity = new CustomerFileEntity();
			fileEntity.setCreateDate(new Date());
			fileEntity.setFileName(document.getAttachmenttitle());
			fileEntity.setFileType(fileType);
			fileEntity.setFilePath(document.getRealpath());
			fileEntity.setFileKey(document.getId());
			fileEntity.setDelUrl("commonController.do?delObjFile&fileKey=" + document.getId());
			fileEntity.setAccountId(accountId);

			systemService.saveOrUpdate(fileEntity);

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

	@RequestMapping(params = "delFile")
	@ResponseBody
	public AjaxJson delFile(HttpServletRequest request , CustomerFileEntity fileEntity){
		AjaxJson j = new AjaxJson();

		if(fileEntity!=null){
			try{
				systemService.delete(fileEntity);
				j.setSuccess(true);
			}
			catch(Exception e){
				j.setMsg("发生错误");
				j.setSuccess(false);
			}

		}
		else{
			j.setMsg("参数错误");
			j.setSuccess(false);
		}

		return j;
	}


	@RequestMapping(params = "downloadExcelPage")
	public ModelAndView downloadExcelPage(HttpServletRequest request,ModelMap modelMap){

		//获取当前微信账户id
		String accountId = request.getParameter("accountId");
		
		//当前页码
		String curPageNO = request.getParameter("curPageNO");
		if(StringUtil.isEmpty(curPageNO)){
			curPageNO = "1";
		} 

		CriteriaQuery cq = new CriteriaQuery(CustomerFileEntity.class, Integer.valueOf(curPageNO));
		cq.eq("accountId", accountId);
		cq.eq("fileType", "excel");
		cq.setPageSize(15);
		cq.setMyAction("./commonFileDownloadController.do?downloadExcelPage");
		cq.add();//加载条件

		PageList pageList = systemService.getPageList(cq, true);
		modelMap.put("pageList", pageList);
		modelMap.put("accountId", accountId);

		return new ModelAndView("kbrobot/download-bg");
	}
	
	@RequestMapping(params = "downloadPowerPointPage")
	public ModelAndView downloadPowerPointPage(HttpServletRequest request,ModelMap modelMap){

		//获取当前微信账户id
		String accountId = request.getParameter("accountId");
		
		//当前页码
		String curPageNO = request.getParameter("curPageNO");
		if(StringUtil.isEmpty(curPageNO)){
			curPageNO = "1";
		}

		CriteriaQuery cq = new CriteriaQuery(CustomerFileEntity.class, Integer.valueOf(curPageNO));
		cq.eq("accountId", accountId);
		cq.eq("fileType", "powerPoint");
		cq.setPageSize(15);
		cq.setMyAction("./commonFileDownloadController.do?downloadExcelPage");
		cq.add();//加载条件

		PageList pageList = systemService.getPageList(cq, true);
		modelMap.put("pageList", pageList);
		modelMap.put("accountId", accountId);

		return new ModelAndView("kbrobot/download-kj");
	}
	
	@RequestMapping(params = "commonDownloadFile")
	public void commonDownloadFile(HttpServletRequest request,HttpServletResponse response){
		
		String fileKey = request.getParameter("fileKey");
		
		TSDocument tsDocument = systemService.getEntity(TSDocument.class, fileKey);
		
		UploadFile uploadFile = new UploadFile(request,response);
		uploadFile.setRealPath(tsDocument.getRealpath());
		uploadFile.setView(false);
		uploadFile.setExtend(tsDocument.getExtend());
		
		//commonDao 
		try {
			commonDao.viewOrDownloadFile(uploadFile).getOutputStream().flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
	}

}

