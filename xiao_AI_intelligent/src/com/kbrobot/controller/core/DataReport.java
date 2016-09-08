package com.kbrobot.controller.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.system.WeixinConversationClient;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.util.DateUtils;

@Controller
@RequestMapping("/dataReportController")
public class DataReport {
	
	@Autowired
	private SystemService systemService;
	
	//一天毫秒数
	private Long oneDayTime = 24*60*60*1000L;
	
	/**
	 * 访客统计
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "report-visit-data")
	public ModelAndView goVisitData(ModelMap modelMap,HttpServletRequest request){
		
		
		return new ModelAndView("kbrobot/report-visit-data");
	}
	
	@RequestMapping(params = "show-visit-data")
	@ResponseBody
	public AjaxJson showVisitData(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		
		Calendar calendarInstance = Calendar.getInstance();
		
		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();
		
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");

		//2016-09-01 11:00
		Date startTime = null;
		Date endTime = null;
		if(StringUtil.isNotEmpty(startTimeStr)&&StringUtil.isNotEmpty(endTimeStr)){
			startTime = DateUtils.str2Date(startTimeStr, DateUtils.time_sdf);
			endTime = DateUtils.str2Date(endTimeStr, DateUtils.time_sdf);
		}
		else{
			startTime = new Date(System.currentTimeMillis() - 7*oneDayTime);
			endTime = new Date();
		}
		 
		
		CriteriaQuery cq = new CriteriaQuery(WeixinConversationClient.class);
		
		cq.eq("weixinAccountId", weixinAccountEntity.getWeixinAccountId());
		cq.gt("addDate", startTime);
		cq.lt("addDate", endTime);
		cq.addOrder("addDate",SortDirection.asc );
		cq.add();
		//查询出来结果
		List<WeixinConversationClient> questionList =  systemService.getListByCriteriaQuery(cq, false);
		
		Iterator<WeixinConversationClient> questionIterator =  null;
		
		
		//得出时间距离
		Long timeDistance = endTime.getTime() - startTime.getTime();
		Map<String, Object> attributes = new HashMap<String, Object>();
		
		//x轴显示的数据
		List<String> xAxisData = new ArrayList<String>();
		//数据
		List<Integer> seriesData = new ArrayList<Integer>();
		
		//设置时间
		calendarInstance.setTime(startTime);
		
		
		//小于一天 			每小时
		if(timeDistance < 1*oneDayTime){
			int hoursDistance =  (int) Math.ceil(timeDistance/(oneDayTime/24));
			//设置单位时间最大值
			calendarInstance.set(Calendar.MINUTE, calendarInstance.getActualMaximum(Calendar.MINUTE));
			calendarInstance.set(Calendar.SECOND, calendarInstance.getActualMaximum(Calendar.SECOND));
			
			for(int hours=0;hours <= hoursDistance;hours++){
				
				Integer splitData = 0;
				//获取Date类型时间
				Date splitDate = calendarInstance.getTime();
				questionIterator =  questionList.iterator();
				while(questionIterator.hasNext()){
					WeixinConversationClient client = questionIterator.next();
					Date clientAddDate = client.getAddDate();
					if(splitDate.after(clientAddDate)){
						splitData++;
						questionIterator.remove();
					}
				}
				seriesData.add(splitData);
				xAxisData.add(DateUtils.date2Str(splitDate , DateUtils.time_sdf));
				calendarInstance.set(Calendar.HOUR_OF_DAY, calendarInstance.get(Calendar.HOUR_OF_DAY) + 1 );
			}
		}
		//小于60天 - 大于一天  每天
		else if(timeDistance < 60*oneDayTime){
			//112800000/112800000
			int dayDistance = (int) Math.ceil(timeDistance/oneDayTime);
			calendarInstance.set(Calendar.HOUR_OF_DAY, calendarInstance.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendarInstance.set(Calendar.MINUTE, calendarInstance.getActualMaximum(Calendar.MINUTE));
			calendarInstance.set(Calendar.SECOND, calendarInstance.getActualMaximum(Calendar.SECOND));
			
			for(int day=0;day <= dayDistance;day++){
				
				Integer splitData = 0;
				//获取Date类型时间
				Date splitDate = calendarInstance.getTime();
				questionIterator =  questionList.iterator();
				while(questionIterator.hasNext()){
					WeixinConversationClient client = questionIterator.next();
					Date clientAddDate = client.getAddDate();
					if(splitDate.after(clientAddDate)){
						splitData++;
						questionIterator.remove();
					}
				}
				seriesData.add(splitData);
				xAxisData.add(DateUtils.date2Str(splitDate, DateUtils.date_sdf));
				calendarInstance.set(Calendar.DAY_OF_MONTH, calendarInstance.get(Calendar.DAY_OF_MONTH) + 1 );
			}
		}
		//大于60天 每个月
		else {
			int monthDistance = (int) Math.ceil(timeDistance/(oneDayTime*30));
			calendarInstance.set(Calendar.DAY_OF_MONTH, calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendarInstance.set(Calendar.HOUR_OF_DAY, calendarInstance.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendarInstance.set(Calendar.MINUTE, calendarInstance.getActualMaximum(Calendar.MINUTE));
			calendarInstance.set(Calendar.SECOND, calendarInstance.getActualMaximum(Calendar.SECOND));
			for(int month=0;month <= monthDistance;month++){
				
				Integer splitData = 0;
				//获取Date类型时间
				Date splitDate = calendarInstance.getTime();
				questionIterator =  questionList.iterator();
				while(questionIterator.hasNext()){
					WeixinConversationClient client = questionIterator.next();
					Date clientAddDate = client.getAddDate();
					if(splitDate.after(clientAddDate)){
						splitData++;
						questionIterator.remove();
					}
				}
				seriesData.add(splitData);
				xAxisData.add(DateUtils.date2Str(splitDate, DateUtils.getSDFormat("yyyy-MM")));
				calendarInstance.set(Calendar.MONTH, calendarInstance.get(Calendar.MONTH) + 1 );
			}
		}
		attributes.put("xAxisData", xAxisData);
		attributes.put("seriesData", seriesData);
		j.setAttributes(attributes);
		
		return j;
	}
	
	
	
	/**
	 * 问题热度统计
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "report-question-data")
	public ModelAndView goQuestionCount(ModelMap modelMap,HttpServletRequest request){
		
		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();
		return new ModelAndView("kbrobot/report-question-data");
	}
	
	/**
	 * 匹配率统计
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "report-martch-data")
	public ModelAndView goKnowledgeCount(ModelMap modelMap,HttpServletRequest request){
		
		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();
		return new ModelAndView("kbrobot/report-martch-data");
	}
	
	
	/**
	 * 访客日志
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "report-visit-log")
	public ModelAndView goVisitLog(ModelMap modelMap,HttpServletRequest request){
		
		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();
		
		
		
		return new ModelAndView("kbrobot/report-visit-log");
	}
}


