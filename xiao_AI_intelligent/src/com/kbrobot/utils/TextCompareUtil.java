package com.kbrobot.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONException;

public class TextCompareUtil {
	
	public static void main(String args[]){
		String str1 = "计算机是啥";
		String str2 = "什么是计算机";
		
		try {
			System.out.println(getSimilarScore(str1,str2));
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 比较两个文本的相似度
	 * @param text1	
	 * @param text2
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static double getSimilarScore(String text1,String text2) throws JSONException, IOException{
		String[] textArray1 = LtpUtil.getWordList(text1==null?"":text1);
		String[] textArray2 = LtpUtil.getWordList(text2==null?"":text2);
		
		return getSimilarScore(textArray1,textArray2);
	}
	
	public static double getSimilarScore(String[] textArray1,String[] textArray2) throws JSONException, IOException{
		//短句
		Map<String, Integer> tfMap1 = getTFMap(textArray1.length<textArray2.length?textArray1:textArray2);
		//长句
		Map<String, Integer> tfMap2 = getTFMap(textArray1.length<textArray2.length?textArray2:textArray1);

		Map<String, MutablePair<Integer, Integer>> tfsForward = new HashMap<String, MutablePair<Integer, Integer>>();

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
			
			tfsForward.put(key, pairForward);
		}
		
		double compareResult1 = caclIDF(tfsForward);
		
		return compareResult1 ;
	}

	/**
	 * 获得词频映射
	 * @param str
	 * @return
	 * @throws IOException
	 * @throws JSONException 
	 */
	private static Map<String, Integer> getTFMap(String[] wordList) throws IOException, JSONException {  
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		if(wordList==null){
			wordList = new String[]{};
		}
		
		for(String word:wordList){
			//得出当前词是否所占词频
			Integer count = map.get(word);
			if (null == count) {
				count = 1;
			}
			else {
				count += 1;//以词汇的长度作为判断的参数（长词得分高，短词得分低）
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
