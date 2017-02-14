package com.kbrobot.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.hibernate.qbc.PageList;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExcelTitle;
import org.jeecgframework.web.cgform.service.excel.ExcelTempletService;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.CustomerBespeakEntity;
import com.kbrobot.entity.CustomerBespeakSetEntity;
import com.kbrobot.utils.CustomServiceUtil;
import com.kbrobot.utils.WeixinThirdUtil;

import weixin.guanjia.account.entity.WeixinAccountEntity;

/**
 * 预约
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/commonBespeakController")
public class CommonBespeakController {

	private static Logger logger = LoggerFactory.getLogger(CommonBespeakController.class);

	WeixinThirdUtil weixinThirdUtilInstance = WeixinThirdUtil.getInstance();

	@Autowired
	private SystemService systemService;

	@RequestMapping(params = "bespeakSubmit")
	@ResponseBody
	public void bespeakSubmit(HttpServletRequest request,HttpServletResponse response,CustomerBespeakEntity bespeak) throws IOException{

		AjaxJson j = new AjaxJson();

		if(bespeak==null){
			j.setSuccess(false);
		}
		else{
			try{
				//保存
				systemService.save(bespeak);

				{
					String toUser = bespeak.getTargetOpenid();
					String accountId = bespeak.getAccountId();

					WeixinAccountEntity currentWeixinAccountEntity = systemService.get(WeixinAccountEntity.class, accountId);
					String authorizer_access_token = weixinThirdUtilInstance.getAuthorizerAccessToken(currentWeixinAccountEntity.getWeixinAccountId());

					//预定客户发送消息
					CustomServiceUtil.sendCustomServiceTextMessage(toUser, authorizer_access_token, "您好，您已经预约成功。");
					/*IndustryTemplateMessageSend industryTemplateMessageSend = new IndustryTemplateMessageSend();
					industryTemplateMessageSend.setAccess_token(authorizer_access_token);
					industryTemplateMessageSend.setTemplate_id("4m3vrpiSA-CPyL9YqHw2jKDlZSX6Sz65SoMKvA9BV1s");
					industryTemplateMessageSend.setTouser(toUser);
					industryTemplateMessageSend.setUrl("www.kb-robot.com");
					industryTemplateMessageSend.setTopcolor("#ffAADD");
					TemplateMessage data = new TemplateMessage();
					TemplateData first = new TemplateData();
					first.setColor("#173177");
					first.setValue("恭喜你预定成功！");

					TemplateData keynote1= new TemplateData();
					keynote1.setColor("#173177");
					keynote1.setValue("巧克22力");

					TemplateData keynote2= new TemplateData();
					keynote2.setColor("39.8元");
					keynote2.setValue("恭喜你购买成功！");

					TemplateData keynote3= new TemplateData();
					keynote3.setColor("#173177");
					keynote3.setValue("2014年9月16日");

					TemplateData remark= new TemplateData();
					remark.setColor("#173177");
					remark.setValue("欢迎再次购买！");
					data.setFirst(first);
					data.setKeynote1(keynote1);
					data.setKeynote2(keynote2);
					data.setKeynote3(keynote3);
					data.setRemark(remark);
					industryTemplateMessageSend.setData(data);

					s  = JwTemplateMessageAPI.sendTemplateMsg(industryTemplateMessageSend);



					String result = JwTemplateMessageAPI.setIndustry(authorizer_access_token, "	21", "	41");
					logger.debug(result);*/


					CustomerBespeakSetEntity customerBespeakSetEntity = systemService.findUniqueByProperty(CustomerBespeakSetEntity.class ,"accountId", accountId);

					if(customerBespeakSetEntity!=null){

						String messageAdmin = "";

						messageAdmin += "您好，有新客户预约，请及时登陆后台查看。";
						messageAdmin += "\n姓　　名：" + bespeak.getYybsxm();
						messageAdmin += "\n联系方式：" + bespeak.getYybshm();
						messageAdmin += "\n预约项目：" + bespeak.getYyfwxm();
						messageAdmin += "\n预约日期：" + bespeak.getYyrq();
						messageAdmin += "\n预约时间段：" + bespeak.getYybssh();

						//预定提醒管理员
						CustomServiceUtil.sendCustomServiceTextMessage(customerBespeakSetEntity.getOpenId(), authorizer_access_token, messageAdmin);
					}
				}

				j.setSuccess(true);
			}
			catch(Exception e){
				j.setSuccess(false);
			}
		}

		response.getWriter().write("callback(" + j.getJsonStr() +")");
	}

	@RequestMapping(params = "bespeakEdit")
	@ResponseBody
	public AjaxJson bespeakEdit(ModelMap modelMap,HttpServletRequest request,CustomerBespeakSetEntity customerBespeakSetEntity){
		AjaxJson j = new AjaxJson();

		if(customerBespeakSetEntity==null){
			j.setSuccess(false);
		}
		else{

			try {
				CustomerBespeakSetEntity oldEntity = systemService.findUniqueByProperty(CustomerBespeakSetEntity.class, "accountId", customerBespeakSetEntity.getAccountId());

				if(oldEntity==null){
					oldEntity = new CustomerBespeakSetEntity();
				}

				MyBeanUtils.copyBeanNotNull2Bean(customerBespeakSetEntity, oldEntity);

				systemService.saveOrUpdate(oldEntity);

				j.setSuccess(true);
			}
			catch (Exception e) {
				e.printStackTrace();
				j.setSuccess(false);
			}

		}


		return j;
	}

	/**
	 * 分页
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "bespeakList")
	public ModelAndView questionList(HttpServletRequest request,ModelMap modelMap){


		String weixinAccountId = ResourceUtil.getWeiXinAccountId();
		List<CustomerBespeakEntity> customerBespeakList = new ArrayList<CustomerBespeakEntity>();
		CustomerBespeakSetEntity customerBespeakSetEntity = systemService.findUniqueByProperty(CustomerBespeakSetEntity.class ,"accountId", weixinAccountId);

		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();
		//当前页码
		String curPageNO = request.getParameter("curPageNO");
		if(StringUtil.isEmpty(curPageNO)){
			curPageNO = "1";
		}

		CriteriaQuery cq = new CriteriaQuery(CustomerBespeakEntity.class, Integer.valueOf(curPageNO));
		//搜索参数
		String searchKey = request.getParameter("searchKey");
		String searchParam = request.getParameter("searchParam");

		if(StringUtil.isNotEmpty(searchParam)){
			cq.like(searchKey, searchParam);
		}

		request.setAttribute("searchParam", searchParam);
		request.setAttribute("searchKey", searchKey);

		cq.eq("accountId", accountId);
		cq.setPageSize(15);
		cq.setMyAction("./commonBespeakController.do?bespeakList");
		//cq.addOrder("createTime", SortDirection.desc);//根据时间顺寻排序
		cq.add();//加载条件

		PageList pageList = systemService.getPageList(cq, true);
		customerBespeakList = pageList.getResultList();
		modelMap.put("pageList", pageList);


		modelMap.put("customerBespeakList", customerBespeakList);
		modelMap.put("customerBespeakSetEntity", customerBespeakSetEntity);
		modelMap.put("weiXinAccount", ResourceUtil.getWeiXinAccount());

		return new ModelAndView("kbrobot/yybsList");
	}

	@RequestMapping(params = "bespeakDel")
	@ResponseBody
	public AjaxJson bespeakDel(HttpServletRequest request,CustomerBespeakEntity customerBespeakEntity){
		AjaxJson j = new AjaxJson();

		try{
			if(customerBespeakEntity!=null){
				systemService.delete(customerBespeakEntity);

				j.setSuccess(true);
			}
			else{
				j.setSuccess(false);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			j.setSuccess(false);
		}

		return j;
	}

	/**
	 * 导出
	 * 
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("all")
	@RequestMapping(params = "exportXls")
	public void exportXls(HttpServletRequest request,HttpServletResponse response) {

		String codedFileName = "文件";
		String sheetName="导出信息";
		
		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();
		
		List<CustomerBespeakEntity> customerBespeakList = null;

		String searchKey = request.getParameter("searchKey");
		searchKey = searchKey==null?"":searchKey;
		String searchParam = request.getParameter("searchParam");
		searchParam = searchParam==null?"":searchParam;
		
		CriteriaQuery cq = new CriteriaQuery(CustomerBespeakEntity.class);
		
		cq.eq("accountId", accountId);
		cq.setMyAction("./commonBespeakController.do?bespeakList");
		cq.add();//加载条件
		
		PageList pageList = systemService.getPageList(cq, true);
		customerBespeakList = pageList.getResultList();
		
		//导出文件名称 form表单中文名-v版本号.xsl
		codedFileName = sheetName+"_"+searchKey+"-v";
		// 生成提示信息，
		response.setContentType("application/vnd.ms-excel");

		OutputStream fOut = null;
		try {

			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				response.setHeader("content-disposition",
						"attachment;filename="
								+ java.net.URLEncoder.encode(codedFileName,
										"UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"),
						"ISO8859-1");
				response.setHeader("content-disposition",
						"attachment;filename=" + newtitle + ".xls");
			}
			// 产生工作簿对象
			HSSFWorkbook workbook = null;
			
			ExcelTitle excelTitle = new ExcelTitle("导出",null,"export");
			workbook = ExcelExportUtil.exportExcel(excelTitle, CustomerBespeakEntity.class, customerBespeakList);
			
			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (UnsupportedEncodingException e1) {

		} catch (Exception e) {

		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (IOException e) {

			}
		}


	}


}
