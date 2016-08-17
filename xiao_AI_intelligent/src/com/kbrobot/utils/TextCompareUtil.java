package com.kbrobot.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONException;

public class TextCompareUtil {
	
	/*public static void main(String args[]){
		String str1 = "计算机是什么";
		String str2 = "什么是呵呵";
		
		try {
			System.out.println(getSimilarScore(str1,str2));
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 比较两个文本的相似度
	 * @param text1
	 * @param text2
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static double getSimilarScore(String text1,String text2) throws JSONException, IOException{

		Map<String, Integer> tfMap1 = getTFMap(text1);
		Map<String, Integer> tfMap2 = getTFMap(text2);

		Map<String, MutablePair<Integer, Integer>> tfsForward = new HashMap<String, MutablePair<Integer, Integer>>();
		//计算第二遍
		Map<String, MutablePair<Integer, Integer>> tfsBackward = new HashMap<String, MutablePair<Integer, Integer>>();

		//计算相似度
		for (String key : tfMap1.keySet()) {
			//forward
			MutablePair<Integer, Integer> pairForward = new MutablePair<Integer, Integer>(tfMap1.get(key), 0);  
			tfsForward.put(key, pairForward);
		}
		for (String key : tfMap2.keySet()) {
			//froward
			MutablePair<Integer, Integer> pairForward = tfsForward.get(key);
			if (null == pairForward) {
				pairForward = new MutablePair<Integer, Integer>(0, tfMap2.get(key));
			}
			else{
				pairForward.setRight(tfMap2.get(key));
			}
			
			//backward
			MutablePair<Integer, Integer> pairBackward = new MutablePair<Integer, Integer>(tfMap2.get(key), 0);  
			tfsBackward.put(key, pairBackward);
			
		}
		for(String key : tfMap1.keySet()){
			//backward
			MutablePair<Integer, Integer> pairBackward = tfsBackward.get(key);
			if (null == pairBackward) {
				pairBackward = new MutablePair<Integer, Integer>(0, tfMap1.get(key));
			}
			else{
				pairBackward.setRight(tfMap1.get(key));
			}
		}
		
		double compareResult1 = caclIDF(tfsForward);
		double compareResult2 = caclIDF(tfsBackward);
		
		return (compareResult1 + compareResult2)/2.0;
	}

	/**
	 * 获得词频映射
	 * @param str
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	private static Map<String, Integer> getTFMap(String text) throws IOException, JSONException {  
		Map<String, Integer> map = new HashMap<String, Integer>();  
		String[] wordList = LtpUtil.getWordList(text);
		for(String word:wordList){
			//得出当前词是否所占词频
			Integer count = map.get(word);
			if (null == count) {
				count = word.length();
			}
			else {
				count = count + word.length();//以词汇的长度作为判断的参数（长词得分高，短词得分低）
			}
			map.put(word, count);
		}
		return map;
	}
	/**
	 * 计算两个文本的余弦值
	 * @param tf
	 * @return
	 */
	private static double caclIDF(Map<String, MutablePair<Integer, Integer>> tf) {  
		double d = 0;
		if (MapUtils.isEmpty(tf)) {
			return d;  
		}  
		double denominator = 0;  
		double sqdoc1 = 0;  
		double sqdoc2 = 0;  
		Pair<Integer, Integer> count = null;
		for (String key : tf.keySet()) {
			count = tf.get(key);  
			denominator += count.getLeft() * count.getRight();
			sqdoc1 += count.getLeft() * count.getLeft();
			sqdoc2 += count.getRight() * count.getRight();
		}
		double under = (Math.sqrt(sqdoc1) * Math.sqrt(sqdoc2));
		if(under==0){
			return 0;
		}
		d = denominator / under;
		return d;  
	}
}
