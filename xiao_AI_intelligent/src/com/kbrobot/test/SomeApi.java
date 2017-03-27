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

		//System.out.println(TextCompareUtil.getSimilarScore("印花税","印花税优惠备案"));
		/*String token = "07dQtIsd-UQKI5InBLY7vXiuNhBq5m-L-7xLOquUbQGWJKBBcwSvRHTeTMdJCcxJ4URGcD88CUCsDo1pVDuC_e3MG2JGLlxNBaEEAwlSODUi8G9shT0j7r_9Fhm9CghWJABeAIDSJK";
		JSONObject json =  WeixinUtil.httpsRequest("https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=" + token, "POST", "{\"msg_id\": \"1000000001\"}");
		System.out.println(json.toString());*/

		//String token = "s8-bBROYZlsIkWqedvL-BOqciPBBJeNTUgdQmVqeP80drZEfN5LWGda0p3HqG0n5CDg-r1ZHZ4QTB4EgP7K2UYK95EKF8gmz0w7F0lAaR3bfUF-uB6VaSSRbqbY2OYaFJJAhADDFFW";
		/*String addKfResult = JwKfaccountAPI.addKfacount(token, "kbrobot@kbznyj", "小凯");
		System.out.println(addKfResult);*/

		/*List<WxKfaccount> kfList = JwKfaccountAPI.getAllKfaccount(token);
		System.out.println(kfList);*/

		/**
		 * 相似度测试
		 */
		/*SemanticSimilarity semanticSimilarity = SemanticSimilarity.getInstance();
		File file = new File("./questionListFull.txt");
		//File file = new File("./questionList.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String ask = "开票的系统都可以开具哪些类型的发票";
		while(reader.ready()){
			String question = reader.readLine();
			String[] askArray = LtpUtil.getWordList(ask);
			String[] questionArray = LtpUtil.getWordList(question);
			long time = System.currentTimeMillis();
			//double score = TextSimilarityUtil.getSimilarityNotSplit(ask, question);
			//double score = TextCompareUtil.getSimilarScore(askArray, questionArray);
			//double score = semanticSimilarity.getSimilarity(askArray, questionArray);
			double score = TextSimilarityUtil.getSimilaritySplit(askArray, questionArray);
			time = System.currentTimeMillis() - time;
			if(score >= 0.5){
				System.out.println(question + " -- " + score);
			}
			System.out.println(question + " -- " + score + "  --- time :" + time);
		}
		reader.close();*/
		
		/*Map<String,Object> obj = new HashMap<String,Object>();
		obj.put("kf_account", "xiaokai@kbznyj");
		obj.put("invite_wx", "booltrue000");
		String token = "cd1dm8IM29XPvEUGFaB43RnK9XxxKnaoAPurVTBV34htBPliVkoLa81d1hSrxFZ3jL3DC43Iop2TVECxossnbKfeBjeZgG3bC0UALKn7PhWCp4UqV9m5ZdXTYdxDIb4ENGTcAGDRKX";
		String url = "https://api.weixin.qq.com/customservice/kfaccount/inviteworker?access_token=" + token;
		JSONObject json = WxstoreUtils.httpRequest(url, "POST", JSONObject.fromObject(obj).toString());
		System.out.println(json.toString());*/
		
		/*String result = RooboUtil.getRooboResultBySpeakStr("我想听故事", "avdfagG");
		System.out.println(result);*/
		
		//System.out.println(TuLingUtil.getResultBySpeakStr("我想听歌", "avdfagG").get(ResultKey.resultType).toString());;
		
		/*SpeechUtility.createUtility("appid=" + SpeechSynthesizerUtil.APPID);
		SpeechSynthesizerUtil.speechSynthesize("你好，我叫小艾", new SynthesizeToUriListener() {
			
			@Override
			public void onSynthesizeCompleted(String uri, SpeechError arg1) {
				System.out.println(uri);
				String wavFile = uri.substring(0, uri.lastIndexOf('.'));
				//经过两次格式转换
				String srcFile = wavFile + ".wav";
				String targetFile = wavFile + ".mp3";
				try {
					AudioFileFormatUtil.pcm2wav(uri ,srcFile );
					AudioFileFormatUtil.wav2mp3(srcFile, targetFile);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println(arg1);
			}
			
			@Override
			public void onBufferProgress(int arg0) {
				System.out.println(arg0);
			}
		});*/

		/*File file = new File("./33.mp3");
		System.out.println(file.getAbsolutePath());*/
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
