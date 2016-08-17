package com.kbrobot.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.util.LogUtil;
import org.jeewx.api.third.JwThirdAPI;
import org.jeewx.api.wxbase.wxmedia.JwMediaAPI;
import org.jeewx.api.wxbase.wxmedia.model.WxUpload;
import org.json.JSONException;

import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SynthesizeToUriListener;
import com.kbrobot.utils.WeixinMediaUtil.WeixinMediaType;

public class CustomServiceUtil {

	//private static final String CUSTOM_SERVICE_API_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	/**
	 * 发送客服消息
	 * @param toUser
	 * @param accessToken
	 * @param content
	 * @throws JSONException
	 * @throws IOException
	 */
	public static void sendCustomServiceTextMessage(String toUser,String accessToken,String content) throws JSONException, IOException{
		
		Map<String,Object> obj = new HashMap<String,Object>();
    	obj.put("touser", toUser);
    	obj.put("msgtype", "text");
    	Map<String,Object> msgMap = new HashMap<String,Object>();
    	msgMap.put("content", content);
    	obj.put("text", msgMap);
		//jeewxAPI
		String resultJson = JwThirdAPI.sendMessage(obj, accessToken);
		
		LogUtil.info(resultJson);
	}

	public static void sendCustomServiceVoiceMessage(final String toUser,final String accessToken,String speakStr) throws JSONException{
		//微信请求url
		//final String url = CUSTOM_SERVICE_API_URL.replaceAll("ACCESS_TOKEN", accessToken);
		
		SynthesizeToUriListener synthesizeToUriListener = new SynthesizeToUriListener() {
			@Override
			public void onSynthesizeCompleted(final String uri, SpeechError error) {
				if (error == null) {
					LogUtil.info("*************合成成功*************");
					LogUtil.info("合成音频生成路径：" + uri);
					//转换格式
					try {
						//wav音频路径
						String wavFile = uri.substring(0, uri.lastIndexOf('.'));
						//经过两次格式转换
						String srcFile = wavFile + ".wav";
						String targetFile = wavFile + ".mp3";
						AudioFileFormatUtil.pcm2wav(uri ,srcFile );
						AudioFileFormatUtil.wav2mp3(srcFile, targetFile);
						
						//上传到微信临时素材 mp3格式
						File voiceFile = new File(targetFile);
						LogUtil.info("文件绝对路径：" + voiceFile.getAbsolutePath());
						//上传文件 使用jeewAPI
						//WeixinMediaUtil.uploadMediaFiles(accessToken, mediaFile);
						WxUpload uploadObj = JwMediaAPI.uploadMedia(accessToken, WeixinMediaType.VOICE.toString().toLowerCase(),voiceFile.getPath());
								
						//发送语音消息
						Map<String,Object> obj = new HashMap<String,Object>();
						obj.put("touser", toUser);
						obj.put("msgtype", "voice");
						Map<String,Object> contentObj = new HashMap<String,Object>();
						contentObj.put("media_id", uploadObj.getMedia_id());//获取mediaId
						obj.put("voice", contentObj);
						String resultJson = JwThirdAPI.sendMessage(obj, accessToken); //WeixinUtil.httpsRequest(url, "POST", obj.toString()).toString();
						LogUtil.info(resultJson);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else
					LogUtil.info("*************" + error.getErrorCode()+ "*************");
			}
			
			@Override
			public void onBufferProgress(int progress) {
				LogUtil.info("*************合成进度*************" + progress);
			}
		};
		//语音合成
		SpeechSynthesizerUtil.speechSynthesize(speakStr,synthesizeToUriListener);
	}
}
