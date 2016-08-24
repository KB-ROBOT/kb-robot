package com.kbrobot.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.entity.RobotSimilarQuestionEntity;
import com.kbrobot.utils.QuestionMatchUtil;

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
		
		List<RobotQuestionEntity> questionList = getList();
		
		questionList = questionList.subList(0, 32);
		
		Long start = System.currentTimeMillis();
		
		QuestionMatchUtil.matchQuestion(questionList, "你好", "to", "from");
		
		Long end = System.currentTimeMillis();
		
		System.out.println("用时：" + (end - start )/1000.0  +"秒");
		
	}
	
	public static List<RobotQuestionEntity> getList() throws IOException{
		List<RobotQuestionEntity> questionList = new ArrayList<RobotQuestionEntity>();
		
		
		File file = new File("./questionList.txt");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		
		while(reader.ready()){
			RobotQuestionEntity entity = new RobotQuestionEntity();
			entity.setQuestionTitle(reader.readLine());
			entity.setSimilarQuestionList(new ArrayList<RobotSimilarQuestionEntity>());
			questionList.add(entity);
		}
		
		reader.close();
		
		return questionList;
		
		//System.out.println(fil.getAbsolutePath());
	}
}
