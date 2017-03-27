package com.kbrobot.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.entity.RobotSimilarQuestionEntity;
import com.kbrobot.service.RobotQuestionServiceI;
import com.kbrobot.utils.LtpUtil;
import com.kbrobot.utils.TextCompareUtil;

/**
 * 问题接口
 * @author 刘维
 *
 */
@Controller
@RequestMapping(value="questionApiController")
public class QuestionApiController {
	
	/**
	 * 相似度最小值
	 */
	public final static double minScore = 0.60d;

	public final static double niceScore = 0.95d;
	
	@Autowired
	private RobotQuestionServiceI robotQuestionService;
	
	@RequestMapping(params = "query")
	@ResponseBody
	public AjaxJson query(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		
		String queryStr = request.getParameter("queryStr");
		String accountId = request.getParameter("accountId");
		
		if(StringUtil.isEmpty(queryStr) || StringUtil.isEmpty(accountId)){
			j.setMsg("参数错误，请检查。");
			j.setSuccess(false);
		}
		else{
			try {
				queryStr = URLDecoder.decode(queryStr, "UTF-8");
				accountId = URLDecoder.decode(accountId, "UTF-8");
				
				//问题全量查找
				CriteriaQuery cqAllQuestion = new CriteriaQuery(RobotQuestionEntity.class);
				//相似问题查找
				CriteriaQuery cqAllSimilarQuestion = new CriteriaQuery(RobotSimilarQuestionEntity.class);
				cqAllQuestion.eq("accountId", accountId);
				cqAllSimilarQuestion.eq("accountId", accountId);

				cqAllQuestion.addOrder("matchTimes", SortDirection.desc);//根据问题热度倒叙排序
				cqAllQuestion.add();
				cqAllSimilarQuestion.add();
				List<RobotQuestionEntity> filterQuestionList = robotQuestionService.getListByCriteriaQuery(cqAllQuestion, false);

				//相似问题查找填入到主问题中
				List<RobotSimilarQuestionEntity> filterSimilarQuestionList = robotQuestionService.getListByCriteriaQuery(cqAllSimilarQuestion, false);
				for(RobotSimilarQuestionEntity similar:filterSimilarQuestionList){
					RobotQuestionEntity tempQuestion = new RobotQuestionEntity();
					RobotQuestionEntity similarQuestionParent = similar.getQuestion();

					MyBeanUtils.copyBeanNotNull2Bean(similarQuestionParent, tempQuestion);

					tempQuestion.setQuestionTitle(similar.getSimilarQuestionTitle());
					tempQuestion.setWordSplit(similar.getWordSplit());
					filterQuestionList.add(tempQuestion);
				}

				//匹配知识库
				Set<RobotQuestionEntity> matchResult = matchQuestion(filterQuestionList, queryStr);

				//匹配结果为空

				if(matchResult==null||matchResult.isEmpty()){
					CriteriaQuery cqKeyWordQuestion = new CriteriaQuery(RobotQuestionEntity.class);

					queryStr = queryStr.replaceAll("，|。|？|、", "").replaceAll(",|[.]|[?]", "");
					cqKeyWordQuestion.eq("accountId", accountId);
					for(int i=0;i<queryStr.length();i++){
						cqKeyWordQuestion.like("questionTitle", queryStr.charAt(i));
					}
					cqKeyWordQuestion.addOrder("matchTimes", SortDirection.desc);//根据问题热度倒叙排序
					cqKeyWordQuestion.add();

					filterQuestionList = robotQuestionService.getListByCriteriaQuery(cqKeyWordQuestion, false);
					LogUtil.info("keyWord查询到的条数:" + (filterQuestionList==null?0:filterQuestionList.size()));
					
					if(!filterQuestionList.isEmpty()){
						//给匹配结果赋予keyWord搜索结果
						matchResult = new HashSet<RobotQuestionEntity>(filterQuestionList);
					}
				}
				
				
				j.setObj(matchResult);
				
			} catch (Exception e) {
				j.setMsg("发生错误。");
				j.setSuccess(false);
				e.printStackTrace();
			}
		}
		
		return j;
	}
	
	public static Set<RobotQuestionEntity> matchQuestion(List<RobotQuestionEntity> questionList,String content) throws JSONException, IOException{
		double maxScore = 0;

		Set<RobotQuestionEntity> findResultQuestionSet = new HashSet<RobotQuestionEntity>();

		String[] contentWordSplit = LtpUtil.getWordList(content);

		RobotQuestionEntity niceMatchQuestion = null;
		for(RobotQuestionEntity que : questionList){
			//遍历每个问题并得出相似度 getWordSplit是已经分好的词
			double currentScore = TextCompareUtil.getSimilarScore(que.getWordSplit().split(","), contentWordSplit);

			//取得当前最大值
			if(currentScore>maxScore){
				maxScore = currentScore;
				niceMatchQuestion = que;
				//如果相似度大于0.95 则判定为已经找到了答案
				if(maxScore >= niceScore){
					break;
				}
				else if(maxScore >= minScore){
					LogUtil.info("插入推荐问题，分数：" + currentScore +  "   set中：" + findResultQuestionSet.size() + "个");
					findResultQuestionSet.add(que);
				}
			}
		}


		//如果最大分数大于niceScore，则判定为找到了答案
		if(maxScore>=niceScore){
			findResultQuestionSet.clear();
			findResultQuestionSet.add(niceMatchQuestion);
			return findResultQuestionSet;
		}
		//否则就返回一些相似答案
		else if(!findResultQuestionSet.isEmpty()){

			List<RobotQuestionEntity> findResultQuestionList = new ArrayList<RobotQuestionEntity>(findResultQuestionSet);
			//截取问题列表50条
			findResultQuestionList = findResultQuestionList.subList(0, findResultQuestionList.size()>50?50:findResultQuestionList.size());
			Collections.reverse(findResultQuestionList);
			return new HashSet<RobotQuestionEntity>(findResultQuestionList);
		}

		else{
			return null;
		}
	}
}
