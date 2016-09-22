package com.kbrobot.controller.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.web.system.service.SystemService;
import org.jeewx.api.third.JwThirdAPI;
import org.jeewx.api.wxbase.wxmedia.JwMediaAPI;
import org.jeewx.api.wxbase.wxmedia.model.WxUpload;
import org.jeewx.api.wxsendmsg.JwSendMessageAPI;
import org.jeewx.api.wxsendmsg.model.SendMessageResponse;
import org.jeewx.api.wxsendmsg.model.WxArticle;
import org.jeewx.api.wxsendmsg.model.WxMedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SynthesizeToUriListener;
import com.kbrobot.entity.VoiceTemplate;
import com.kbrobot.entity.WeixinSendGroupMsgEntity;
import com.kbrobot.utils.AudioFileFormatUtil;
import com.kbrobot.utils.SpeechSynthesizerUtil;
import com.kbrobot.utils.WeixinThirdUtil;
import com.kbrobot.utils.WeixinMediaUtil.WeixinMediaType;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.message.entity.NewsItem;
import weixin.guanjia.message.entity.NewsTemplate;
import weixin.guanjia.message.entity.TextTemplate;

/**
 * 
 * @author 刘维
 *
 */
@Controller
@RequestMapping("/weixinSendMsgAllController")
public class WeixinSendGroupMsgController extends BaseController {
	@Autowired
	private SystemService systemService;

	private String message;

	WeixinThirdUtil weixinThirdUtilInstance = WeixinThirdUtil.getInstance();

	ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");

	//群发消息返回值
	private SendMessageResponse sendMessageResponse = new SendMessageResponse();

	private Object lock = new Object();

	@RequestMapping(params = "addSendMsgAll")
	@ResponseBody
	public AjaxJson addSendMsgAll(WeixinSendGroupMsgEntity  sendGroupMsg,HttpServletRequest request){

		AjaxJson j = new AjaxJson();
		message = "知识库添加成功";
		//获取当前微信账户id
		String accountId = ResourceUtil.getWeiXinAccountId();
		sendGroupMsg.setAccountId(accountId);
		sendGroupMsg.setCreateTime(new Date());
		sendGroupMsg.setStatus("wait_send");
		systemService.save(sendGroupMsg);
		systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);

		return j;
	}

	@RequestMapping(params = "groupMsgSend")
	@ResponseBody
	public AjaxJson groupMsgSend(HttpServletRequest request){
		AjaxJson j = new AjaxJson();

		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();

		String id = request.getParameter("id");

		WeixinSendGroupMsgEntity weixinSendGroupMsg = systemService.get(WeixinSendGroupMsgEntity.class, id);

		String msgType = weixinSendGroupMsg.getMsgType();
		String templateId = weixinSendGroupMsg.getTemplateId();


		try{
			//获取accessToken
			String authorizerAccessToken = weixinThirdUtilInstance.getAuthorizerAccessToken(weixinAccountEntity.getWeixinAccountId());

			if("text".equals(msgType)){//文本
				TextTemplate textTemplate = systemService.get(TextTemplate.class, templateId);

				sendMessageResponse = JwSendMessageAPI.sendMessageToGroupOrAllWithText(authorizerAccessToken,true,null,textTemplate.getContent());
			}
			else if("news".equals(msgType)){//图文
				NewsTemplate newsTemplate = systemService.get(NewsTemplate.class, templateId);

				List<NewsItem> itemList = newsTemplate.getNewsItemList();
				List<WxArticle> weixinArticleList = new ArrayList<WxArticle>();
				for (NewsItem news : itemList) {
					WxArticle wxArticle = new WxArticle();
					wxArticle.setTitle(news.getTitle());
					wxArticle.setAuthor(news.getAuthor());

					String filePath = bundler.getString("fileRootPath") + File.separatorChar + news.getImagePath();
					wxArticle.setFilePath(filePath.substring(0, filePath.lastIndexOf("/")));
					wxArticle.setFileName(filePath.substring(filePath.lastIndexOf("/")+1));
					String url = "";
					if (oConvertUtils.isEmpty(news.getUrl())) {
						url = bundler.getString("domain")+ "/newsItemController.do?goContent&id=" + news.getId();
					}
					else {
						url = news.getUrl();
					}
					wxArticle.setContent(news.getContent());
					wxArticle.setContent_source_url(url);
					wxArticle.setDigest(news.getDescription());
					weixinArticleList.add(wxArticle);
				}

				sendMessageResponse = JwSendMessageAPI.sendMessageToGroupOrAllWithArticles(authorizerAccessToken,true,null,weixinArticleList);
			}
			else if("voice".equals(msgType)){//语音
				VoiceTemplate voiceTemplate = systemService.get(VoiceTemplate.class, templateId);

				final String accessTokenFinal = authorizerAccessToken;

				//同步线程 sendMessageResponse 变量 

				SynthesizeToUriListener synthesizeToUriListener = new SynthesizeToUriListener() {
					@Override
					public void onSynthesizeCompleted(final String uri, SpeechError error) {
						synchronized (lock) {
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
									String absolutePath = voiceFile.getAbsolutePath();
									LogUtil.info("文件绝对路径：" + absolutePath);
									//上传文件 使用jeewAPI
									//WxUpload uploadObj = JwMediaAPI.uploadMedia(accessTokenFinal, WeixinMediaType.VOICE.toString().toLowerCase(),voiceFile.getPath());
									WxMedia wxMedia = new WxMedia();
									wxMedia.setType("voice");
									wxMedia.setFilePath(absolutePath.substring(0, absolutePath.lastIndexOf(File.separator)) + File.separator);
									wxMedia.setFileName(absolutePath.substring(absolutePath.lastIndexOf(File.separator)+1));
									
									sendMessageResponse = JwSendMessageAPI.sendMessageToGroupOrAllWithMedia(accessTokenFinal, true, null, wxMedia);
								
								}
								catch (Exception e) {
									e.printStackTrace();
									message = e.getMessage();
								}
							}
							else{
								LogUtil.info("*************" + error.getErrorCode()+ "*************");
							}
							lock.notify();//释放线程锁
						}
					}

					@Override
					public void onBufferProgress(int progress) {
						LogUtil.info("*************合成进度*************" + progress);
					}
				};

				//语音合成
				SpeechSynthesizerUtil.speechSynthesize(voiceTemplate.getVoiceText(),synthesizeToUriListener);
			}

			synchronized (lock) {
				if("voice".equals(msgType)){
					lock.wait();//线程锁
				}
				
				String mediaId = sendMessageResponse.getMsg_id();
				if(StringUtil.isNotEmpty(mediaId)){
					weixinSendGroupMsg.setMsgId(mediaId);
					weixinSendGroupMsg.setSendTime(new Date());
					systemService.saveOrUpdate(weixinSendGroupMsg);
					this.message = "推送成功，推送会有一定的时间，请耐心等待。<br/>已经推送的消息只能在半个小时内删除（已经推送成功的用户无法删除）";
				}
				else{
					this.message = "推送失败，请检查相关权限接口";
				}
			}
		}
		catch(Exception e){
			this.message = e.getMessage();
		}

		j.setMsg(this.message);

		return j;
	}




}
