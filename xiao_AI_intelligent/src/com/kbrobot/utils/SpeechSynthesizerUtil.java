package com.kbrobot.utils;

import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.ResourceUtil;

import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SynthesizeToUriListener;

public class SpeechSynthesizerUtil {
	public static final String APPID = ResourceUtil.getConfigByName("xunfei_ltp_api_key");

	public static Object speechLock = new Object();

	/**
	 * 语音合成并存储音频文件
	 * @param speakStr
	 * @return
	 * @throws InterruptedException 
	 */
	public static void speechSynthesize(String speakStr,SynthesizeToUriListener synthesizeToUriListener) throws InterruptedException{

		String xunfeiFilePath = ResourceUtil.getConfigByName("xunfei_voice_file_path");
		//生成不重复文件名
		String fileName = "voiceFile_" + System.currentTimeMillis() ;

		SpeechSynthesizer speechSynthesizer = SpeechSynthesizer.getSynthesizer();

		if(speechSynthesizer==null){
			speechSynthesizer = SpeechSynthesizer.createSynthesizer();
		}
		synchronized (speechLock) {
			if(speechSynthesizer.isSpeaking()){
				LogUtil.info("语音正在合成，等待中...");
				speechLock.wait();
			}
			LogUtil.info("====没有线程在合成，开始合成====");
			// 设置发音人
			speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoqi");
			// 设置语速，范围0~100
			speechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
			// 设置语调，范围0~100
			speechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
			// 设置音量，范围0~100
			speechSynthesizer.setParameter(SpeechConstant.VOLUME, "50");
			// 设置合成音频保存位置（可自定义保存位置），默认保存在“./iflytek.pcm”
			speechSynthesizer.synthesizeToUri(speakStr, xunfeiFilePath + fileName + ".pcm",synthesizeToUriListener);
		}



	}
}