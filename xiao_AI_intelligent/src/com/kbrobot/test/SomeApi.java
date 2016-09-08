package com.kbrobot.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.entity.RobotSimilarQuestionEntity;
import com.kbrobot.utils.LtpUtil;
import com.kbrobot.utils.QuestionMatchUtil;
import com.kbrobot.utils.TextCompareUtil;
import com.kbrobot.utils.WeixinThirdUtil;

import weixin.guanjia.core.entity.message.resp.BaseMessageResp;

public class SomeApi {
	public void Test(){
		//MapUtils
		//MutablePair
	}
	
	public static void main(String[] args) throws Exception{
		//System.out.println(PasswordUtil.encrypt("admin", "462195349zxc", PasswordUtil.getStaticSalt()));
		//5e2b1a8da7705446
		//c44b01947c9e6e3f
		//String[] heh = LtpUtil.getWordList("纳税人于2016年5月1日以后取得的不动产，适用进项税额分期抵扣时的“第二年”怎么理解？是否指自然年度？");
		//System.out.println(heh); 
		
		/*Long start = System.currentTimeMillis();
		List<RobotQuestionEntity> questionList = getList().subList(0, 5);
		Long end = System.currentTimeMillis();
		System.out.println("获取list用时：" + (end - start )/1000.0  +"秒");
		
		QuestionMatchUtil.matchResultConvert(questionList, "", "");*/
		
		System.out.println(TextCompareUtil.getSimilarScore("印花税","印花税优惠备案"));
		
	}
	
	public static List<RobotQuestionEntity> getList() throws IOException, JSONException{
		List<RobotQuestionEntity> questionList = new ArrayList<RobotQuestionEntity>();
		
		File file = new File("./questionList.txt");
		
		System.out.println(file.getAbsolutePath());
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		while(reader.ready()){
			RobotQuestionEntity entity = new RobotQuestionEntity();
			entity.setQuestionTitle(reader.readLine());
			entity.setSimilarQuestionList(new ArrayList<RobotSimilarQuestionEntity>());
			
			//entity.setWordSplit(LtpUtil.getWordSplit(entity.getQuestionTitle()));
			
			questionList.add(entity);
			/*String question = reader.readLine();
			String[] keyWord = LtpUtil.getKeyWordArray(question);
			
			for(String key: keyWord){
				System.out.print(key + ",");
			}
			System.out.println("\n" + question);*/
		}
		
		reader.close();
		
		System.out.println(questionList.size() + "个问题测试");
		return questionList;
		
		//System.out.println(fil.getAbsolutePath());
	}
}
