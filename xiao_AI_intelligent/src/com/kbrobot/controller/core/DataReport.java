package com.kbrobot.controller.core;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.hibernate.qbc.PageList;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.service.SystemService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.entity.WeixinUserLocationEntity;
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

		//会话人数
		CriteriaQuery cqConversion = new CriteriaQuery(WeixinConversationClient.class);

		//地理位置坐标
		CriteriaQuery cqLocation = new CriteriaQuery(WeixinUserLocationEntity.class);

		cqConversion.eq("weixinAccountId", weixinAccountEntity.getWeixinAccountId());
		cqLocation.eq("weixinAccountId", weixinAccountEntity.getWeixinAccountId());

		cqConversion.ge("addDate", startTime);
		cqConversion.le("addDate", endTime);

		cqLocation.ge("createTime", startTime);
		cqLocation.le("createTime", endTime);

		cqConversion.addOrder("addDate",SortDirection.asc );

		cqLocation.addOrder("createTime", SortDirection.asc);

		cqConversion.add();
		cqLocation.add();
		//查询会话
		List<WeixinConversationClient> questionList =  systemService.getListByCriteriaQuery(cqConversion, false);

		//查询地域分布
		List<WeixinUserLocationEntity> locationList =  systemService.getListByCriteriaQuery(cqLocation, false);

		List<Map<String,Object>> locationData = new ArrayList<Map<String,Object>>();

		for(WeixinUserLocationEntity location:locationList){
			Map<String,Object> locationMap = new HashMap<String,Object>();
			locationMap.put("name", location.getAddress());
			locationMap.put("value", new Double[]{Double.valueOf(location.getLongitude()),Double.valueOf(location.getLatitude()),50.0});
			locationData.add(locationMap);
		}


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
				calendarInstance.add(Calendar.HOUR_OF_DAY, 1);
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
				calendarInstance.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		//大于60天 每个月
		else {
			int monthDistance = (int) Math.ceil(timeDistance/(oneDayTime*30));

			calendarInstance.set(Calendar.HOUR_OF_DAY, calendarInstance.getActualMaximum(Calendar.HOUR_OF_DAY));
			calendarInstance.set(Calendar.MINUTE, calendarInstance.getActualMaximum(Calendar.MINUTE));
			calendarInstance.set(Calendar.SECOND, calendarInstance.getActualMaximum(Calendar.SECOND));
			for(int month=0;month <= monthDistance;month++){
				//每个月重新设置一下天数
				calendarInstance.set(Calendar.DAY_OF_MONTH, calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH));

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


				//使用add方法进行月份的 +1 使用set方法会有可能
				calendarInstance.add(Calendar.MONTH, 1);

			}
		}
		attributes.put("xAxisData", xAxisData);
		attributes.put("seriesData", seriesData);

		attributes.put("locationData", locationData);

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

		CriteriaQuery cq = new CriteriaQuery(RobotQuestionEntity.class);
		//公众号数据库ID
		cq.eq("accountId", weixinAccountEntity.getId());
		cq.notEq("matchTimes", 0);
		cq.addOrder("matchTimes", SortDirection.desc);//根据匹配数量从大到小排序
		cq.setPageSize(30);
		cq.add();//加载条件

		List<RobotQuestionEntity> topQuestionList =  systemService.getListByCriteriaQuery(cq, true);//isPage为true，只提取前几条

		modelMap.put("topQuestionList", topQuestionList);

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
	 * @throws JSONException 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(params = "report-visit-log")
	public ModelAndView goVisitLog(ModelMap modelMap,HttpServletRequest request) {

		//当前微信账户
		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();
		//当前页码
		String curPageNO = request.getParameter("curPageNO");
		if(StringUtil.isEmpty(curPageNO)){
			curPageNO = "1";
		}
		//时间段
		String startTimeStr = request.getParameter("startTime");
		String endTimeStr = request.getParameter("endTime");

		CriteriaQuery cqConversation = new CriteriaQuery(WeixinConversationClient.class,Integer.valueOf(curPageNO));
		//公众号原始ID
		cqConversation.eq("weixinAccountId", weixinAccountEntity.getWeixinAccountId());
		
		Date startTime = null;
		Date endTime = null;
		if(StringUtil.isNotEmpty(startTimeStr)&&StringUtil.isNotEmpty(endTimeStr)){
			startTime = DateUtils.str2Date(startTimeStr, DateUtils.time_sdf);
			endTime = DateUtils.str2Date(endTimeStr, DateUtils.time_sdf);
			//时间段
			cqConversation.ge("addDate", startTime);
			cqConversation.le("addDate", endTime);
		}
		
		cqConversation.setPageSize(15);
		cqConversation.setMyAction("./dataReportController.do?report-visit-log");
		cqConversation.addOrder("addDate", SortDirection.desc);//根据时间顺寻排序
		cqConversation.add();
		//查出分页结果
		PageList pageResult = systemService.getPageList(cqConversation, true);
		//结果集
		List<WeixinConversationClient> conversationList = pageResult.getResultList();
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		for(WeixinConversationClient conversation : conversationList){
			//访客的openId
			String openId = conversation.getOpenId();
			//开始和结束会话的接收消息时间
			Date conversationStartDate = conversation.getAddDate(); 
			Date conversationEndDate = conversation.getEndDate();
			//得出时间距离
			Long timeDistance = conversationEndDate.getTime() -  conversationStartDate.getTime();
			int minute = (int) (timeDistance/1000/60);
			int stayTime = minute==0?1:minute;
			
			String conversationStartTime = DateUtils.date2Str(conversationStartDate, DateUtils.datetimeFormat);
			String conversationEndTime = DateUtils.date2Str(conversationEndDate, DateUtils.datetimeFormat);
			
			//会话条数
			String sql= "";
			sql = "SELECT COUNT(*) FROM weixin_conversation_content";
			sql += " WHERE ";
			sql += "to_username = '"+weixinAccountEntity.getWeixinAccountId()+"'";
			sql += " AND ";
			sql += "from_username = '"+openId+"'";
			sql += " AND ";
			sql += "receive_time >= '"+ conversationStartTime +"'";
			sql += " AND ";
			sql += "receive_time <= '" + conversationEndTime +"'";
			Long conversationCount = systemService.getCountForJdbcParam(sql, null);
			
			//获取用户地理位置
			//方法：用户开始会话时间（conversationStartDate）前后十分钟里 通过openId与weixinAccountId找出唯一结果
			Calendar calendarInstance = Calendar.getInstance();
			calendarInstance.setTime(conversationStartDate);
			
			calendarInstance.add(Calendar.MINUTE, -5);//五分钟前
			Date locationTimeStart = calendarInstance.getTime();
			calendarInstance.add(Calendar.MINUTE, 10);//五分钟后
			Date locationTimeEnd = calendarInstance.getTime();
			
			CriteriaQuery cqLocation = new CriteriaQuery(WeixinUserLocationEntity.class);
			
			cqLocation.eq("openId", openId);
			cqLocation.eq("weixinAccountId", weixinAccountEntity.getWeixinAccountId());
			cqLocation.ge("createTime", locationTimeStart);
			cqLocation.le("createTime", locationTimeEnd);
			cqLocation.add();
			List<WeixinUserLocationEntity> locationList = systemService.getListByCriteriaQuery(cqLocation, false);
			
			String conversationAddress = "";
			if(!locationList.isEmpty()){
				conversationAddress = locationList.get(0).getAddress();
			}
			else{
				conversationAddress = "-";
			}
			
			//将值放入map中
			Map<String,Object> conversationMap = new HashMap<String,Object>();
			conversationMap.put("visitType", "微信客户（ID="+openId+"）");
			conversationMap.put("conversationCount", conversationCount);
			conversationMap.put("stayTime",stayTime);
			conversationMap.put("conversationStartTime",conversationStartTime );
			conversationMap.put("conversationEndTime", conversationEndTime);
			conversationMap.put("conversationAddress", conversationAddress);
			conversationMap.put("conversationId", conversation.getId());
			
			resultList.add(conversationMap);
		}
		
		//分页
		modelMap.put("pageTools", pageResult.getPager().getToolsBarByUrl());
		modelMap.put("resultList", resultList);

		return new ModelAndView("kbrobot/report-visit-log");
	}
}


