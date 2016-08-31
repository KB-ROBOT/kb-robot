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
import com.kbrobot.entity.system.WeixinClient;
import com.kbrobot.manager.WeixinClientManager;

import weixin.guanjia.core.entity.message.resp.Article;
import weixin.guanjia.core.entity.message.resp.BaseMessageResp;
import weixin.guanjia.core.entity.message.resp.NewsMessageResp;
import weixin.guanjia.core.entity.message.resp.TextMessageResp;
import weixin.guanjia.core.util.MessageUtil;

public class QuestionMatchUtil {
	
	/**
	 * 相似度最小值
	 */
	public static double minScore = 0.75;
	/**
	 * 匹配到最多的问题数量
	 */
	public static Integer maxMacthNum = 5;
	/**
	 * 
	 */
	private static ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");
	
	/**
	 * 空答案
	 */
	private static String emptyAnswer = "此答案为空";

	/**
	 * 
	 * @param questionList
	 * @param content
	 * @param toUserName
	 * @param fromUserName
	 * @return 匹配问题结果
	 * @throws JSONException
	 * @throws IOException
	 */
	public static List<RobotQuestionEntity> matchQuestion(List<RobotQuestionEntity> questionList,String content) throws JSONException, IOException{
		double maxScore = 0;

		List<RobotQuestionEntity> findResultQuestionList = new ArrayList<RobotQuestionEntity>();

		String[] contentWordSplit = LtpUtil.getWordList(content);

		RobotQuestionEntity goodMatchQuestion = null;
		for(RobotQuestionEntity que : questionList){
			//遍历每个问题并得出相似度 getWordSplit是已经分好的词
			double currentScore = TextCompareUtil.getSimilarScore(que.getWordSplit().split(","), contentWordSplit);

			//取得当前最大值
			if(currentScore>maxScore){
				maxScore = currentScore;
				goodMatchQuestion = que;
				//如果相似度大于0.95 则判定为已经找到了答案
				if(maxScore >= 0.95d){
					findResultQuestionList.add(que);
					continue;
				}
			}
			//遍历相似问题进行比较 getWordSplit是已经分好的词
			List<RobotSimilarQuestionEntity> similarQueList = que.getSimilarQuestionList();
			for(RobotSimilarQuestionEntity simliarQue : similarQueList){
				currentScore = TextCompareUtil.getSimilarScore(simliarQue.getWordSplit().split(","), contentWordSplit);
				//取得当前最大值
				if(currentScore>maxScore){
					maxScore = currentScore;
					goodMatchQuestion = que;
					//如果相似度大于0.95 则判定为已经找到了答案
					if(maxScore >= 0.95d){
						findResultQuestionList.add(que);
					}
				}
			}
			
			if(findResultQuestionList.size()>=maxMacthNum){
				break;
			}
		}

		//如果匹配答案结果不为空
		if(!findResultQuestionList.isEmpty()){
			return findResultQuestionList;
		}
		//匹配结果为空 则判断最大分数是否大于阈值
		else if(maxScore>=minScore){
			findResultQuestionList.add(goodMatchQuestion);
			return findResultQuestionList;
		}
		else{
			return null;
		}

	}
	
	
	/**
	 * 匹配结果转换
	 * @param selectQuestion
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static BaseMessageResp matchResultConvert(RobotQuestionEntity selectQuestion,String toUserName,String fromUserName){
		List<RobotQuestionEntity> matchResult = new ArrayList<RobotQuestionEntity>();
		matchResult.add(selectQuestion);
		return matchResultConvert(matchResult,toUserName,fromUserName);
	}
	/**
	 * 匹配结果转换
	 * @param matchResult
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static BaseMessageResp matchResultConvert(List<RobotQuestionEntity> matchResult,String toUserName,String fromUserName){
		String answerContent = "";//答案内容
		RobotQuestionEntity selectQueston = null;//选中的问题
		
		WeixinClientManager weixinClientManager = WeixinClientManager.instance;
		
		if(matchResult.size()>1){
			//获取对应的微信client 
			WeixinClient currentClient =  weixinClientManager.getWeixinClient(fromUserName+":"+toUserName);
			//设置问题列表
			currentClient.setLastQuestionList(matchResult);
			answerContent += "您是否在关心下列问题:\n";
			for(int i=0;i<matchResult.size();i++){
				answerContent += ( (i+1) + "."+matchResult.get(i).getQuestionTitle()+"\n");
			}
			answerContent += "请回复相应序号选择问题。";
			
		}
		else{
			selectQueston = matchResult.get(0);
			answerContent = selectQueston.getQuestionAnswer();
		}
		
		answerContent = answerContent==null||answerContent.equals("")?emptyAnswer:answerContent;
		
		//答案处理
		if(answerContent.indexOf("<p>")==0&&answerContent.lastIndexOf("</p>")==(answerContent.length()-4)&&answerContent.length()>7){
			answerContent = answerContent.substring(3, answerContent.length()-4);
		}
		
		if(answerContent.contains("<")||answerContent.contains(">")||(answerContent.length()>=200&&matchResult.size()<=1)){ //图文形式
			Article article = new Article();
			article.setTitle(selectQueston.getQuestionTitle());
			article.setDescription("");
			article.setUrl(bundler.getString("domain")+ "/robotQuestionController.do?getQuestionAnswerContent&id=" + selectQueston.getId() );
			//设置图片
			Matcher matcher = Pattern.compile("(http://|https://)[^\\s|^\"]*(.jpg|.png|.jpeg|.bmp|.gif)").matcher(answerContent);
			if(matcher.find()){
				article.setPicUrl(matcher.group());
			}
			else{
				article.setPicUrl("");
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

			textMessageResp.setContent(answerContent);

			return textMessageResp;
		}
	}


}
