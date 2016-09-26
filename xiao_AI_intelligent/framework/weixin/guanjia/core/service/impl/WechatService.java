package weixin.guanjia.core.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.web.system.service.SystemService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kbrobot.entity.RobotQuestionEntity;
import com.kbrobot.entity.RobotSimilarQuestionEntity;
import com.kbrobot.service.RobotQuestionServiceI;
import com.kbrobot.utils.CustomServiceUtil;
import com.kbrobot.utils.TextCompareUtil;
import com.kbrobot.utils.TuLingUtil;
import com.kbrobot.utils.TuLingUtil.ResultKey;
import com.kbrobot.utils.TuLingUtil.ReturnCode;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.base.entity.Subscribe;
import weixin.guanjia.base.entity.WeixinExpandconfigEntity;
import weixin.guanjia.base.service.SubscribeServiceI;
import weixin.guanjia.base.service.WeixinExpandconfigServiceI;
import weixin.guanjia.core.entity.message.resp.Article;
import weixin.guanjia.core.entity.message.resp.NewsMessageResp;
import weixin.guanjia.core.entity.message.resp.TextMessageResp;
import weixin.guanjia.core.util.MessageUtil;
import weixin.guanjia.menu.entity.MenuEntity;
import weixin.guanjia.message.dao.TextTemplateDao;
import weixin.guanjia.message.entity.AutoResponse;
import weixin.guanjia.message.entity.NewsItem;
import weixin.guanjia.message.entity.ReceiveText;
import weixin.guanjia.message.entity.TextTemplate;
import weixin.guanjia.message.service.AutoResponseServiceI;
import weixin.guanjia.message.service.NewsItemServiceI;
import weixin.guanjia.message.service.ReceiveTextServiceI;
import weixin.guanjia.message.service.TextTemplateServiceI;
import weixin.idea.extend.function.KeyServiceI;
import weixin.util.DateUtils;

@Service("wechatService")
public class WechatService {
	@Autowired
	private TextTemplateDao textTemplateDao;
	@Autowired
	private AutoResponseServiceI autoResponseService;
	@Autowired
	private TextTemplateServiceI textTemplateService;
	/*@Autowired
	private NewsTemplateServiceI newsTemplateService;*/
	@Autowired
	private ReceiveTextServiceI receiveTextService;
	@Autowired
	private NewsItemServiceI newsItemService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private SubscribeServiceI subscribeService;
	@Autowired
	private WeixinExpandconfigServiceI weixinExpandconfigService;
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	@Autowired
	private RobotQuestionServiceI robotQuestionService;
	//当前微信账户
	private WeixinAccountEntity currentWeixinAccount =null;
	
	public String coreService(HttpServletRequest request) {
		String respMessage = null;
		
		try {
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			String msgId = requestMap.get("MsgId");
			//消息内容
			String content = requestMap.get("Content");
			LogUtil.info("------------微信客户端发送请求---------------------   |   fromUserName:"+fromUserName+"   |   ToUserName:"+toUserName+"   |   msgType:"+msgType+"   |   msgId:"+msgId+"   |   content:"+content);
			//根据微信ID,获取配置的全局的数据权限ID
			LogUtil.info("-toUserName--------"+toUserName);
			//获取当前微信账户
			currentWeixinAccount =  weixinAccountService.findByToUsername(toUserName);
			
			String sys_accountId = currentWeixinAccount.getId();
			
			//获取当前accessToken  根据accountId（当前微信号）
			String accessToken = weixinAccountService.getAccessToken(sys_accountId);
			LogUtil.info("-sys_accountId--------"+sys_accountId);
			ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");
			// 默认回复此文本消息
			TextMessageResp textMessage = new TextMessageResp();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			// 默认返回的文本消息内容
			textMessage.setContent("请求处理异常，请稍候尝试！");
			// 将文本消息对象转换成xml字符串
			respMessage = MessageUtil.textMessageToXml(textMessage);

			//【微信触发类型】文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				LogUtil.info("------------微信客户端发送请求------------------【微信触发类型】文本消息---");
				respMessage = doTextResponse(content,toUserName,textMessage,bundler,sys_accountId,respMessage,fromUserName,request,msgId,msgType,accessToken);
			}
			//【微信触发类型】图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				textMessage.setContent("您发送的是图片消息！");
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			//【微信触发类型】地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				textMessage.setContent("您发送的是地理位置消息！");
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			//【微信触发类型】链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				textMessage.setContent("您发送的是链接消息！");
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			//【微信触发类型】音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				//获取语音识别结果(需要在微信公众号的后台打开开关)
				String voiceResult = "";
				if((voiceResult=requestMap.get("Recognition"))!=null){
					respMessage = doTextResponse(voiceResult,toUserName,textMessage,bundler,sys_accountId,respMessage,fromUserName,request,msgId,msgType,accessToken);
				}
			}
			//【微信触发类型】事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				LogUtil.info("------------微信客户端发送请求------------------【微信触发类型】事件推送---");
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					respMessage = doDingYueEventResponse(requestMap, textMessage, bundler, respMessage, toUserName, fromUserName, sys_accountId,accessToken);
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
					System.out.println("又有人取消订阅了。");
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					respMessage = doMyMenuEvent(requestMap, textMessage, bundler, respMessage, toUserName, fromUserName, sys_accountId, request);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respMessage;
	}

	/**
	 * Q译通使用指南
	 * 
	 * @return
	 */
	public static String getTranslateUsage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("微译使用指南").append("\n\n");
		buffer.append("微译为用户提供专业的多语言翻译服务，目前支持以下翻译方向：").append("\n");
		buffer.append("    中 -> 英").append("\n");
		buffer.append("    英 -> 中").append("\n");
		buffer.append("    日 -> 中").append("\n\n");
		buffer.append("使用示例：").append("\n");
		buffer.append("    翻译我是中国人").append("\n");
		buffer.append("    翻译dream").append("\n");
		buffer.append("    翻译さようなら").append("\n\n");
		buffer.append("回复“?”显示主菜单");
		return buffer.toString();
	}

	/**
	 * 遍历关键字管理中是否存在用户输入的关键字信息
	 * 
	 * @param content
	 * @return
	 */
	private AutoResponse findKey(String content, String toUsername) {
		LogUtil.info("---------sys_accountId--------"+toUsername+"|");
		//获取全局的数据权限ID
		if(currentWeixinAccount==null){
			currentWeixinAccount = weixinAccountService.findByToUsername(toUsername);
		}
		String sys_accountId = currentWeixinAccount.getId();
		LogUtil.info("---------sys_accountId--------"+sys_accountId);
		// 获取关键字管理的列表，匹配后返回信息
		List<AutoResponse> autoResponses = autoResponseService.findByProperty(AutoResponse.class, "accountId", sys_accountId);
		LogUtil.info("---------sys_accountId----关键字查询结果条数：----"+autoResponses!=null?autoResponses.size():0);
		for (AutoResponse r : autoResponses) {
			// 如果包含关键字
			String kw = r.getKeyWord();
			String[] allkw = kw.split(",");
			for (String k : allkw) {
				if (k.equals(content)) {
					LogUtil.info("---------sys_accountId----查询结果----"+r);
					return r;
				}
			}
		}
		return null;
	}

	/**
	 * 针对文本消息
	 * @param content
	 * @param toUserName
	 * @param textMessage
	 * @param bundler
	 * @param sys_accountId
	 * @param respMessage
	 * @param fromUserName
	 * @param request
	 * @throws Exception 
	 */
	String doTextResponse(String content,
							String toUserName,
							TextMessageResp textMessage,
							ResourceBundle bundler,
							String sys_accountId,
							String respMessage,
							String fromUserName,
							HttpServletRequest request,
							String msgId,
							String msgType,
							String accessToken) throws Exception{
		
		//=================================================================================================================
		// 保存接收到的信息
		ReceiveText receiveText = new ReceiveText();
		receiveText.setContent(content);
		Timestamp temp = Timestamp.valueOf(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		receiveText.setCreateTime(temp);
		receiveText.setFromUserName(fromUserName);
		receiveText.setToUserName(toUserName);
		receiveText.setMsgId(msgId);
		receiveText.setMsgType(msgType);
		receiveText.setResponse("0");
		receiveText.setAccountId(toUserName);
		this.receiveTextService.save(receiveText);
		//=================================================================================================================
		//Step.1 判断关键字信息中是否管理该文本内容。有的话优先采用数据库中的回复
		LogUtil.info("------------微信客户端发送请求--------------Step.1 判断关键字信息中是否管理该文本内容。有的话优先采用数据库中的回复---");
		AutoResponse autoResponse = findKey(content, toUserName);
		// 根据系统配置的关键字信息，返回对应的消息
		if (autoResponse != null) {
			String resMsgType = autoResponse.getMsgType();
			if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(resMsgType)) {//文本
				//根据返回消息key，获取对应的文本消息返回给微信客户端
				TextTemplate textTemplate = textTemplateDao.getTextTemplate(sys_accountId, autoResponse.getTemplateName());
				//return textTemplate.getContent();
				textMessage.setContent(textTemplate.getContent());
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			else if (MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(resMsgType)) {//图文
				List<NewsItem> newsList = this.newsItemService.findByProperty(NewsItem.class,"newsTemplate.id", autoResponse.getTemplateId());
				//NewsTemplate newsTemplate = newsTemplateService.getEntity(NewsTemplate.class, autoResponse.getResContent());
				List<Article> articleList = new ArrayList<Article>();
				for (NewsItem news : newsList) {
					Article article = new Article();
					article.setTitle(news.getTitle());

					article.setPicUrl(bundler.getString("domain") + "/"+ news.getImagePath());
					String url = "";
					if (oConvertUtils.isEmpty(news.getUrl())) {
						url = bundler.getString("domain")+ "/newsItemController.do?goContent&id="+ news.getId();
					}
					else {
						url = news.getUrl();
					}
					article.setUrl(url);
					article.setDescription(news.getDescription());
					articleList.add(article);
				}
				NewsMessageResp newsResp = new NewsMessageResp();
				newsResp.setCreateTime(new Date().getTime());
				newsResp.setFromUserName(toUserName);
				newsResp.setToUserName(fromUserName);
				newsResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsResp.setArticleCount(newsList.size());
				newsResp.setArticles(articleList);
				respMessage = MessageUtil.newsMessageToXml(newsResp);
			}
		}
		else {
			// Step.2  通过微信扩展接口（支持二次开发，例如：翻译，天气）
			//LogUtil.info("------------微信客户端发送请求--------------Step.2  通过微信扩展接口（支持二次开发，例如：翻译，天气）---");
			//List<WeixinExpandconfigEntity> weixinExpandconfigEntityLst = weixinExpandconfigService.findByQueryString("FROM WeixinExpandconfigEntity");
			boolean findflag = false;// 是否找到关键字信息
			/*if (weixinExpandconfigEntityLst.size() != 0) {
				for (WeixinExpandconfigEntity wec : weixinExpandconfigEntityLst) {
					// 如果已经找到关键字并处理业务，结束循环。
					if (findflag) {
						break;// 如果找到结束循环
					}
					String[] keys = wec.getKeyword().split(",");
					for (String k : keys) {
						if (content.indexOf(k) != -1) {
							String className = wec.getClassname();
							KeyServiceI keyService = (KeyServiceI) Class.forName(className).newInstance();
							respMessage = keyService.excute(content,textMessage, request);
							findflag = true;// 改变标识，已经找到关键字并处理业务，结束循环。
							break;// 当前关键字信息处理完毕，结束当前循环
						}
					}
				}
			}*/
//==================================================1.获得accountId并发送客服消息===========================================================================
			
			// currentWeixinAccount.getAccountaccesstoken();
			LogUtil.info("accessToken:"+accessToken);
			//如果是语音信息则 发送一条客服消息
			if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)){
				CustomServiceUtil.sendCustomServiceTextMessage(fromUserName, accessToken, "语音解析结果：" + content);
			}
			
			String speakStr = "";
//==================================================2.根据消息内容查找回答（1.匹配知识库  [无果]---> 2.图灵智能回答）====================================================================
			//@知识库
			List<RobotQuestionEntity> questionList = robotQuestionService.findByProperty(RobotQuestionEntity.class, "accountId", sys_accountId);
			double maxScore = 0;
			for(RobotQuestionEntity que : questionList){
				//遍历每个问题并得出相似度
				String title = que.getQuestionTitle();
				double currentScore = TextCompareUtil.getSimilarScore(title, content);
				System.out.println("============相似度：" + currentScore +"\n" + title);
				
				//取得当前最大值
				if(currentScore>maxScore){
					maxScore = currentScore;
					textMessage.setContent("问题："+que.getQuestionTitle() +"\n"+ que.getQuestionAnswer());
					/*
					 * 设置合成语音字符串
					 */
					speakStr = que.getQuestionAnswer();
					respMessage = MessageUtil.textMessageToXml(textMessage);
					//如果相似度大于0.95 则判定为已经找到了答案
					if(maxScore >= 0.95d){
						findflag = true;
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
						textMessage.setContent("问题："+que.getQuestionTitle() +"\n"+ que.getQuestionAnswer());
						/*
						 * 设置合成语音字符串
						 */
						speakStr = que.getQuestionAnswer();
						respMessage = MessageUtil.textMessageToXml(textMessage);
						//如果相似度大于0.95 则判定为已经找到了答案
						if(maxScore >= 0.95d){
							findflag = true;
							break;
						}
					}
				}
				
			}
			//假如最大分数大于当前阈值，则视为找到答案
			if(maxScore>=0.75d){
				findflag = true;
			}
			
			
			//@图灵
			if(!findflag){//如果没有匹配到知识库
				Map<ResultKey,Object> result = TuLingUtil.getResultBySpeakStr(content,fromUserName);//把询问人的id作为会话ID
				if(result.get(ResultKey.resultType)==ReturnCode.TEXT){//文本
					String resultText = result.get(ResultKey.text).toString();
					textMessage.setContent(resultText);
					
					 //设置合成语音字符串
					 
					speakStr = resultText;
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				else if(result.get(ResultKey.resultType)==ReturnCode.LINK){//链接类型
					String link = "<a href='"+result.get(ResultKey.url).toString()+"'>"+result.get(ResultKey.text).toString()+"</a>";
					textMessage.setContent(link);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				else if(result.get(ResultKey.resultType)==ReturnCode.NEWS){//新闻类型
					@SuppressWarnings("unchecked")
					List<Article> articleList = (List<Article>) result.get(ResultKey.list);
					NewsMessageResp newsResp = new NewsMessageResp();
					newsResp.setCreateTime(new Date().getTime());
					newsResp.setFromUserName(toUserName);
					newsResp.setToUserName(fromUserName);
					newsResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
					newsResp.setArticleCount(articleList.size());
					newsResp.setArticles(articleList);
					respMessage = MessageUtil.newsMessageToXml(newsResp);
				}
				else if(result.get(ResultKey.resultType)==ReturnCode.ERROR){
					textMessage.setContent(result.get(ResultKey.text).toString());
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
			}
//==================================================3.发送语音消息=============================================
			//发送语音
			if(!"".equals(speakStr)){
				CustomServiceUtil.sendCustomServiceVoiceMessage(fromUserName, accessToken,speakStr );
			}
			
		}
		return respMessage;
	}

	/**
	 * 针对事件消息
	 * @param requestMap
	 * @param textMessage
	 * @param bundler
	 * @param respMessage
	 * @param toUserName
	 * @param fromUserName
	 * @throws JSONException 
	 * @throws InterruptedException 
	 */
	String doDingYueEventResponse(Map<String, String> requestMap,TextMessageResp textMessage ,ResourceBundle bundler,String respMessage
			,String toUserName,String fromUserName,String sys_accountId,String accessToken) throws JSONException, InterruptedException{
		//respContent = "谢谢您的关注！回复\"?\"进入主菜单。";
		//查找关注时回复语
		List<Subscribe> lst = subscribeService.findByProperty(Subscribe.class, "accountid", sys_accountId);
		if (lst.size() != 0) {
			Subscribe subscribe = lst.get(0);//获取到关注是回复语
			String type = subscribe.getMsgType();
			if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(type)) {//文本消息
				TextTemplate textTemplate = this.textTemplateService.getEntity(TextTemplate.class, subscribe.getTemplateId());
				String content = textTemplate.getContent();
				CustomServiceUtil.sendCustomServiceVoiceMessage(fromUserName, accessToken,content );
				textMessage.setContent(content);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			} else if (MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(type)) {//图文消息
				List<NewsItem> newsList = this.newsItemService.findByProperty(NewsItem.class,"newsTemplate.id", subscribe.getTemplateId());
				List<Article> articleList = new ArrayList<Article>();
				//NewsTemplate newsTemplate = newsTemplateService.getEntity(NewsTemplate.class, subscribe.getTemplateId());
				for (NewsItem news : newsList) {
					Article article = new Article();
					article.setTitle(news.getTitle());
					article.setPicUrl(bundler.getString("domain")+ "/" + news.getImagePath());
					String url = "";
					if (oConvertUtils.isEmpty(news.getUrl())) {
						url = bundler.getString("domain")+ "/newsItemController.do?goContent&id="+ news.getId();
					} else {
						url = news.getUrl();
					}
					article.setUrl(url);
					article.setDescription(news.getDescription());
					articleList.add(article);
				}
				NewsMessageResp newsResp = new NewsMessageResp();
				newsResp.setCreateTime(new Date().getTime());
				newsResp.setFromUserName(toUserName);
				newsResp.setToUserName(fromUserName);
				newsResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsResp.setArticleCount(newsList.size());
				newsResp.setArticles(articleList);
				respMessage = MessageUtil.newsMessageToXml(newsResp);
			}
		}
		return respMessage;
	}

	/**
	 * 
	 * @param requestMap
	 * @param textMessage
	 * @param bundler
	 * @param respMessage
	 * @param toUserName
	 * @param fromUserName
	 * @param sys_accountId
	 * @param request
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	String doMyMenuEvent(Map<String, String> requestMap,TextMessageResp textMessage ,ResourceBundle bundler,String respMessage
			,String toUserName,String fromUserName,String sys_accountId,HttpServletRequest request) throws Exception{
		String key = requestMap.get("EventKey");
		//自定义菜单CLICK类型
		MenuEntity menuEntity = this.systemService.findUniqueByProperty(MenuEntity.class, "menuKey",key);
		if (menuEntity != null&& oConvertUtils.isNotEmpty(menuEntity.getTemplateId())) {
			String type = menuEntity.getMsgType();
			if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(type)) {
				TextTemplate textTemplate = this.textTemplateService.getEntity(TextTemplate.class, menuEntity.getTemplateId());
				String content = textTemplate.getContent();
				textMessage.setContent(content);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			} else if (MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(type)) {
				List<NewsItem> newsList = this.newsItemService.findByProperty(NewsItem.class,"newsTemplate.id", menuEntity.getTemplateId());
				List<Article> articleList = new ArrayList<Article>();
				//NewsTemplate newsTemplate = newsTemplateService.getEntity(NewsTemplate.class, menuEntity.getTemplateId());
				for (NewsItem news : newsList) {
					Article article = new Article();
					article.setTitle(news.getTitle());
					article.setPicUrl(bundler.getString("domain")+ "/" + news.getImagePath());
					String url = "";
					if (oConvertUtils.isEmpty(news.getUrl())) {
						url = bundler.getString("domain")+ "/newsItemController.do?goContent&id="+ news.getId();
					} else {
						url = news.getContent();
					}
					article.setUrl(url);
					article.setDescription(news.getContent());
					articleList.add(article);
				}
				NewsMessageResp newsResp = new NewsMessageResp();
				newsResp.setCreateTime(new Date().getTime());
				newsResp.setFromUserName(toUserName);
				newsResp.setToUserName(fromUserName);
				newsResp.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsResp.setArticleCount(newsList.size());
				newsResp.setArticles(articleList);
				respMessage = MessageUtil
						.newsMessageToXml(newsResp);
			} else if ("expand".equals(type)) {
				WeixinExpandconfigEntity expandconfigEntity = weixinExpandconfigService.getEntity(WeixinExpandconfigEntity.class,menuEntity.getTemplateId());
				String className = expandconfigEntity.getClassname();
				KeyServiceI keyService = (KeyServiceI) Class.forName(className).newInstance();
				respMessage = keyService.excute("", textMessage,request);

			}
		}
		return respMessage;
	}

	/**
	 * 欢迎语
	 * @return
	 */
	/*public static String getMainMenu() {
		// 复杂字符串文本读取，采用文件方式存储
		String html = new FreemarkerHelper().parseTemplate("/weixin/welcome.ftl", null);
		return html;
	}*/
}
