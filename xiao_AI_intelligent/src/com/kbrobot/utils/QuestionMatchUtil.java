package com.kbrobot.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.entity.RobotSimilarQuestionEntity;

import weixin.guanjia.core.entity.message.resp.Article;
import weixin.guanjia.core.entity.message.resp.BaseMessageResp;
import weixin.guanjia.core.entity.message.resp.NewsMessageResp;
import weixin.guanjia.core.entity.message.resp.TextMessageResp;
import weixin.guanjia.core.util.MessageUtil;

public class QuestionMatchUtil {
	
	public static double minScore = 0.75;
	private static ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");

	public static BaseMessageResp matchQuestion(List<RobotQuestionEntity> questionList,String content,String toUserName,String fromUserName) throws JSONException, IOException{
		double maxScore = 0;
		RobotQuestionEntity goodMatchQuestion = null;
		for(RobotQuestionEntity que : questionList){
			//遍历每个问题并得出相似度
			String title = que.getQuestionTitle();
			double currentScore = TextCompareUtil.getSimilarScore(title, content);
			System.out.println("============相似度：" + currentScore +"\n" + title);

			//取得当前最大值
			if(currentScore>maxScore){
				maxScore = currentScore;
				goodMatchQuestion = que;
				//如果相似度大于0.95 则判定为已经找到了答案
				if(maxScore >= 0.95d){
					break;
				}
			}
			//遍历相似问题进行比较
			List<RobotSimilarQuestionEntity> similarQueList = que.getSimilarQuestionList();
			for(RobotSimilarQuestionEntity simliarQue : similarQueList){
				String similarTile = simliarQue.getSimilarQuestionTitle();

				currentScore = TextCompareUtil.getSimilarScore(similarTile, content);
				System.out.println("=======相似问题比较=====相似度：" + currentScore +"\n" + title);

				//取得当前最大值
				if(currentScore>maxScore){
					maxScore = currentScore;
					goodMatchQuestion = que;
					//如果相似度大于0.95 则判定为已经找到了答案
					if(maxScore >= 0.95d){
						break;
					}
				}
			}

		}
		//假如最大分数大于当前阈值，则视为找到答案
		if(maxScore>=minScore){
			String answerStr = goodMatchQuestion.getQuestionAnswer();
			
			if(answerStr.indexOf("<p>")==0&&answerStr.lastIndexOf("</p>")==(answerStr.length()-4)&&answerStr.length()>7){
				answerStr = answerStr.substring(3, answerStr.length()-4);
			}
			
			if(answerStr.contains("<")||answerStr.contains(">")){ //图文形式
				Article article = new Article();
				article.setTitle(goodMatchQuestion.getQuestionTitle());
				article.setDescription("");
				article.setUrl(bundler.getString("domain")+ "robotQuestionController.do?getQuestionAnswerContent&id=" + goodMatchQuestion.getId() );
				article.setPicUrl("");

				Matcher matcher = Pattern.compile("(http:|https:).*(jpg|jpeg|gif|png)").matcher(answerStr);  
				
				if(matcher.find()){
					article.setPicUrl(matcher.group());
				}
				
				List<Article> articleList = new ArrayList<Article>();
				articleList.add(article);
				
				NewsMessageResp newsResp = new NewsMessageResp();
				newsResp.setCreateTime(new Date().getTime());
				newsResp.setFromUserName(toUserName);
				newsResp.setToUserName(fromUserName);
				newsResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsResp.setArticleCount(1);
				newsResp.setArticles(articleList);
				
				return newsResp;
				
			}
			else{//文本形式
				TextMessageResp textMessageResp = new TextMessageResp();
				textMessageResp.setCreateTime(new Date().getTime());
				textMessageResp.setFromUserName(toUserName);
				textMessageResp.setToUserName(fromUserName);
				textMessageResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
				
				textMessageResp.setContent(answerStr);
				
				return textMessageResp;
			}
			
		}
		else{
			return null;
		}
	}
}
