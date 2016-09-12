package com.kbrobot.task;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.hibernate.qbc.PageList;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kbrobot.controller.core.RobotQuestionController;
import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.entity.RobotSimilarQuestionEntity;
import com.kbrobot.service.RobotQuestionServiceI;
import com.kbrobot.utils.LtpUtil;

@Component("questionWordSplitTask")
public class QuestionWordSplitTask {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RobotQuestionController.class);

	@Autowired
	private RobotQuestionServiceI robotQuestionService;
	
	/**
	 * 1.秒（0~59）
	 * 2.分钟（0~59）
	 * 3.小时（0~23）
	 * 4.天（月）（0~31，但是你需要考虑你月的天数）
	 * 5.月（0~11）
	 * 6.天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
	 * 7.年份（1970－2099）
	 */
	public void questionWordSplit(){
		logger.info("知识库分词定时任务开始");
		Long start = System.currentTimeMillis();
		
		CriteriaQuery cq = new CriteriaQuery(RobotQuestionEntity.class);
		cq.isNull("wordSplit");
		cq.add();
		List<RobotQuestionEntity> questionResult = robotQuestionService.getListByCriteriaQuery(cq, false);
		
		/**
		 * 主问题
		 */
		for(int i=0;i<questionResult.size();i++){
			try {
				RobotQuestionEntity entity = questionResult.get(i);
				entity.setWordSplit(LtpUtil.getWordSplit(entity.getQuestionTitle()));
				
				questionResult.set(i, entity);
				
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		
		robotQuestionService.batchUpdate(questionResult);
		
		/**
		 * 相似问题
		 */
		cq = new CriteriaQuery(RobotSimilarQuestionEntity.class);
		cq.isNull("wordSplit");
		cq.add();
		
		List<RobotSimilarQuestionEntity> similarQuestionResult = robotQuestionService.getListByCriteriaQuery(cq, false);
		
		for(int i=0;i<similarQuestionResult.size();i++){
			RobotSimilarQuestionEntity similarEntity = similarQuestionResult.get(i);
			try {
				similarEntity.setWordSplit(LtpUtil.getWordSplit(similarEntity.getSimilarQuestionTitle()));
				similarQuestionResult.set(i, similarEntity);
				
			}
			catch (JSONException | IOException e) {
				e.printStackTrace();
				continue;
			}
			
		}
		
		robotQuestionService.batchUpdate(similarQuestionResult);
		
		Long end = System.currentTimeMillis();
		
		logger.info("用时：" + ((end - start)/1000.0) + "秒 ");
		
		logger.info("questionResult:" + questionResult.size() + "  similarQuestionResult:" + similarQuestionResult.size());
		
		
	}
}
