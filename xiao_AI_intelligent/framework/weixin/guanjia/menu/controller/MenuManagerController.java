package weixin.guanjia.menu.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.ComboTree;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.model.json.TreeGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.tag.vo.easyui.TreeGridModel;
import org.jeecgframework.web.system.service.SystemService;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.core.req.model.menu.config.CustomWeixinButtonConfig;
import org.jeewx.api.core.req.model.menu.config.WeixinButtonExtend;
import org.jeewx.api.wxmenu.JwMenuAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kbrobot.entity.VoiceTemplate;
import com.kbrobot.utils.WeixinThirdUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.guanjia.core.entity.common.Button;
import weixin.guanjia.core.entity.common.CommonButton;
import weixin.guanjia.core.entity.common.ComplexButton;
import weixin.guanjia.core.entity.common.Menu;
import weixin.guanjia.core.entity.common.ViewButton;
import weixin.guanjia.core.util.WeixinUtil;
import weixin.guanjia.menu.entity.MenuEntity;
import weixin.guanjia.menu.service.WeixinMenuServiceI;
import weixin.guanjia.message.entity.NewsTemplate;
import weixin.guanjia.message.entity.TextTemplate;
import weixin.guanjia.message.service.TextTemplateServiceI;

/**
 * 微信自定义菜单
 * 
 */
@Scope("prototype")
@Controller
@RequestMapping("/menuManagerController")
public class MenuManagerController {
	@Autowired
	private SystemService systemService;
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	@Autowired
	private WeixinMenuServiceI weixinMenuService;
	/*	@Autowired
	private WeixinExpandconfigServiceI weixinExpandconfigService;*/
	@Autowired
	private TextTemplateServiceI textTemplateService;
	private String message;

	WeixinThirdUtil weixinThirdUtilInstance = WeixinThirdUtil.getInstance();

	/**
	 * 添加菜单
	 * @param menuEntity
	 * @param req
	 * @param fatherName
	 * @return
	 */
	@RequestMapping(params = "su")
	@ResponseBody
	public AjaxJson su(MenuEntity menuEntity, HttpServletRequest req,String fatherName) {
		AjaxJson j = new AjaxJson();
		//String id = oConvertUtils.getString(req.getParameter("id"));

		if (StringUtil.isNotEmpty(menuEntity.getId())) {

			MenuEntity tempMenu = this.systemService.getEntity(MenuEntity.class, menuEntity.getId());

			this.message = "更新" + tempMenu.getName() + "的菜单信息信息成功！";
			try {
				MyBeanUtils.copyBeanNotNull2Bean(menuEntity, tempMenu);
				this.weixinMenuService.saveOrUpdate(tempMenu);
				systemService.addLog(message, Globals.Log_Type_UPDATE,Globals.Log_Leavel_INFO);

			} catch (Exception e) {
				this.message = "更新" + tempMenu.getName() + "的菜单信息信息成功！";
				systemService.addLog(message, Globals.Log_Type_UPDATE,Globals.Log_Leavel_INFO);
				e.printStackTrace();
			}
		}
		else {
			//String fatherId = req.getParameter("fatherId");
			if (StringUtil.isNotEmpty(fatherName)) {
				MenuEntity fatherMenu = this.systemService.getEntity(MenuEntity.class, fatherName);
				menuEntity.setMenuEntity(fatherMenu);
			}
			String accountId = ResourceUtil.getWeiXinAccountId();
			if (!"-1".equals(accountId)) {
				String menuId = this.weixinMenuService.save(menuEntity).toString();
				menuEntity.setId(menuId);
				menuEntity.setMenuKey(menuId);
				this.weixinMenuService.saveOrUpdate(menuEntity);

				this.message = "添加" + menuEntity.getName() + "的信息成功！";

				j.setSuccess(true);
				j.setMsg(this.message);
			} else {
				j.setSuccess(false);
				j.setMsg("请添加一个公众帐号。");
			}

			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		return j;
	}

	@RequestMapping(params = "menuEnetityDetail")
	@ResponseBody
	public AjaxJson getMenuEnetityDetail(MenuEntity menuEntity, HttpServletRequest req){
		AjaxJson j = new AjaxJson();
		Map<String,Object> menuMap = new HashMap<String,Object>();

		if(StringUtil.isNotEmpty(menuEntity.getId())){
			menuEntity = this.systemService.getEntity(MenuEntity.class,menuEntity.getId());
			if (menuEntity.getMenuEntity() != null&& menuEntity.getMenuEntity().getId() != null) {
				menuMap.put("fatherId", menuEntity.getMenuEntity().getId());
				menuMap.put("fatherName", menuEntity.getMenuEntity().getName());
			}

			String type = menuEntity.getType();

			menuMap.put("name", menuEntity.getName());
			menuMap.put("type", type);
			menuMap.put("menuKey", menuEntity.getMenuKey());
			menuMap.put("url", menuEntity.getUrl());
			menuMap.put("orders", menuEntity.getOrders());
			menuMap.put("templateId", menuEntity.getTemplateId());
			menuMap.put("msgType", menuEntity.getMsgType());

			if("click".equals(type)){
				if("text".equals(menuEntity.getMsgType())){
					menuMap.put("templateObject", systemService.get(TextTemplate.class, menuEntity.getTemplateId()));
				}
				else if("news".equals(menuEntity.getMsgType())){
					menuMap.put("templateObject",  systemService.get(NewsTemplate.class, menuEntity.getTemplateId()));
				}
				else if("voice".equals(menuEntity.getMsgType())){
					menuMap.put("templateObject",  systemService.get(VoiceTemplate.class, menuEntity.getTemplateId()));
				}
			}
			else if("view".equals(type)){
				//url
			}

			j.setAttributes(menuMap);
			j.setSuccess(true);
		}
		else{
			this.message = "参数有误";
			j.setMsg(message);
			j.setSuccess(false);
		}

		return j;
	}
	/**
	 * 删除菜单
	 * @param menuEntity
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(MenuEntity menuEntity, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		menuEntity = this.systemService.getEntity(MenuEntity.class, menuEntity.getId());

		//找到所有子节点
		List<MenuEntity> chileMenuList = this.systemService.findByProperty(MenuEntity.class, "menuEntity.id", menuEntity.getId());
		chileMenuList.add(menuEntity);
		//父节点、子节点删除
		systemService.deleteAllEntitie(chileMenuList);


		message = "删除" + menuEntity.getName() + "菜单信息数据";
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		j.setMsg(this.message);
		return j;
	}

	/**
	 * 刷新已有菜单
	 * @param request
	 * @return
	 * @throws WexinReqException 
	 */
	/*@RequestMapping(params = "refreshMenu")
	@ResponseBody
	public AjaxJson refreshMenu( HttpServletRequest request) throws WexinReqException{
		AjaxJson j = new AjaxJson();

		List<MenuEntity> menuList = new ArrayList<MenuEntity>();

		WeixinAccountEntity weixinAccountEntity = ResourceUtil.getWeiXinAccount();
		CustomWeixinButtonConfig customWeixinButtonConfig = JwMenuAPI.getAllMenuConfigure(weixinThirdUtilInstance.getAuthorizerAccessToken(weixinAccountEntity.getWeixinAccountId()));

		if("1".equals(customWeixinButtonConfig.getIs_menu_open())){
			List<WeixinButtonExtend> weixinButtonExtendList = customWeixinButtonConfig.getSelfmenu_info();

			for(WeixinButtonExtend fatherButtonExtend : weixinButtonExtendList){//第一层button
				MenuEntity fatherMenu = new MenuEntity();

				List<WeixinButtonExtend> sub_button = fatherButtonExtend.getSub_button();
				if(sub_button==null||sub_button.isEmpty()){//sub_button为空，没有子菜单
					String type = fatherButtonExtend.getType();
					fatherMenu.setType(type);
					fatherMenu.setName(fatherButtonExtend.getName());

					if("text".equalsIgnoreCase(type)){
						//保存文字到value
					}
					else if("img".equalsIgnoreCase(type)||"voice".equalsIgnoreCase(type)){
						//保存mediaID到value
						fatherMenu.setType("media_id");
					}
					else if("video".equalsIgnoreCase(type)){
						//保存视频下载链接到value
					}
					else if("news".equalsIgnoreCase(type)){
						//保存图文消息到news_info，同时保存mediaID到value
					}
					else if("view".equalsIgnoreCase(type)){
						//保存链接到url
						
					}
					else {
						//保存值到key
						
					}


				}
				else{//sub_button不为空，有子菜单
					
					List<MenuEntity> childMenuList = new ArrayList<MenuEntity>();
					
					for(WeixinButtonExtend childButonExtend : sub_button ){
						MenuEntity childMenu = new MenuEntity();
						
						String type = childButonExtend.getType();
						fatherMenu.setType(type);
						fatherMenu.setName(childButonExtend.getName());

						if("text".equalsIgnoreCase(type)){
							//保存文字到value
						}
						else if("img".equalsIgnoreCase(type)||"voice".equalsIgnoreCase(type)){
							//保存mediaID到value
							
						}
						else if("video".equalsIgnoreCase(type)){
							//保存视频下载链接到value
						}
						else if("news".equalsIgnoreCase(type)){
							//保存图文消息到news_info，同时保存mediaID到value
						}
						else if("view".equalsIgnoreCase(type)){
							//保存链接到url
							
						}
						//使用API设置的自定义菜单 click等等
						else {
							//保存值到key
							
						}
						
						childMenuList.add(childMenu);
					}
					
					//添加子菜单
					fatherMenu.setMenuList(childMenuList);
				}
				
				//添加父级菜单
				menuList.add(fatherMenu);
			}
		}
		else{
			j.setSuccess(false);
			j.setMsg("该公众号暂未开启菜单");
		}


		return j;
	}*/






	@RequestMapping(params = "list")
	public ModelAndView list() {
		return new ModelAndView("weixin/guanjia/menu/menulist");
	}

	@RequestMapping(params = "getSubMenu")
	public void getSubMenu(HttpServletRequest request,
			HttpServletResponse response) {
		String accountid = ResourceUtil.getWeiXinAccountId();
		//String msgType = request.getParameter("msgType");
		String resMsg = "";
		JsonConfig config = new JsonConfig();
		config.setJsonPropertyFilter(new PropertyFilter(){  
			public boolean apply(Object source, String name, Object value) {  
				if(name.equals("menuList")) { //要过滤的areas ，Map对象中的  
					return true;  
				} else {  
					return false;  
				}  
			}
		});
		List<MenuEntity> textList = this.weixinMenuService
				.findByQueryString("from MenuEntity t  where t.accountId = '"
						+  accountid+ "'");
		JSONArray json = JSONArray.fromObject(textList,config);
		resMsg = json.toString();

		try {
			response.setCharacterEncoding("utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(resMsg);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@RequestMapping(params = "gettemplate")
	public void gettemplate(HttpServletRequest request, HttpServletResponse response) {
		String accountid = ResourceUtil.getWeiXinAccountId();
		String msgType = request.getParameter("msgType");
		String resMsg = "";
		if ("text".equals(msgType)) {
			List<TextTemplate> textList = this.weixinMenuService.findByQueryString("from TextTemplate t where t.accountId = '"
					+  accountid+ "'");
			JSONArray json = JSONArray.fromObject(textList);
			resMsg = json.toString();
		} else if ("news".equals(msgType)) {
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "newsItemList" });
			List<NewsTemplate> newsList = this.weixinMenuService.findByQueryString("from NewsTemplate t where t.accountId = '"
					+ accountid + "'");
			JSONArray json = JSONArray.fromObject(newsList, jsonConfig);
			resMsg = json.toString();
		}else if("expand".equals(msgType)){

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "newsItemList" });
			List<NewsTemplate> newsList = this.weixinMenuService.findByQueryString("from WeixinExpandconfigEntity t where t.accountid = '"
					+ accountid + "'");
			JSONArray json = JSONArray.fromObject(newsList, jsonConfig);
			resMsg = json.toString();

		}
		try {
			response.setCharacterEncoding("utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(resMsg);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public List<TreeGrid> datagrid(TreeGrid treegrid,
			HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {

		CriteriaQuery cq = new CriteriaQuery(MenuEntity.class);
		cq.eq("accountId", ResourceUtil.getWeiXinAccountId());
		if (treegrid.getId() != null) {
			cq.eq("menuEntity.id", treegrid.getId());
		} else {

			cq.isNull("menuEntity");
		}

		cq.addOrder("orders", SortDirection.asc);
		cq.add();

		List<MenuEntity> menuList = systemService.getListByCriteriaQuery(cq,
				false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		TreeGridModel treeGridModel = new TreeGridModel();
		// treeGridModel.setIcon("orders");
		treeGridModel.setTextField("name");
		treeGridModel.setParentText("url");
		treeGridModel.setOrder("orders");
		treeGridModel.setSrc("type");
		treeGridModel.setIdField("id");
		treeGridModel.setChildList("menuList");
		// 添加排序字段
		treeGrids = systemService.treegrid(menuList, treeGridModel);
		return treeGrids;
	}

	@RequestMapping(params = "jumpSuView")
	public ModelAndView jumpSuView(MenuEntity menuEntity, HttpServletRequest req) {

		org.jeecgframework.core.util.LogUtil.info("...menuEntity.getId()..." + menuEntity.getId());
		if (StringUtil.isNotEmpty(menuEntity.getId())) {
			menuEntity = this.systemService.getEntity(MenuEntity.class,menuEntity.getId());
			if (menuEntity.getMenuEntity() != null&& menuEntity.getMenuEntity().getId() != null) {
				req.setAttribute("fatherId", menuEntity.getMenuEntity().getId());
				req.setAttribute("fatherName", menuEntity.getMenuEntity().getName());
			}
			req.setAttribute("name", menuEntity.getName());
			req.setAttribute("type", menuEntity.getType());
			req.setAttribute("menuKey", menuEntity.getMenuKey());
			req.setAttribute("url", menuEntity.getUrl());
			req.setAttribute("orders", menuEntity.getOrders());
			req.setAttribute("templateId", menuEntity.getTemplateId());
			req.setAttribute("msgType", menuEntity.getMsgType());
		}
		String fatherId = req.getParameter("fatherId");
		if (StringUtil.isNotEmpty(fatherId)) {
			MenuEntity fatherMenuEntity = this.systemService.getEntity(MenuEntity.class, fatherId);
			req.setAttribute("fatherId", fatherId);
			req.setAttribute("fatherName", fatherMenuEntity.getName());
			org.jeecgframework.core.util.LogUtil.info(".....fatherName...."+ fatherMenuEntity.getName());
		}
		return new ModelAndView("weixin/guanjia/menu/menuinfo");
	}

	@RequestMapping(params = "jumpselect")
	public ModelAndView jumpselect() {
		return new ModelAndView("");
	}


	@RequestMapping(params = "sameMenu")
	@ResponseBody
	public AjaxJson sameMenu(MenuEntity menuEntity, HttpServletRequest req) throws WexinReqException {
		AjaxJson j = new AjaxJson();
		String hql = "from MenuEntity where fatherid is null and accountId = '" + ResourceUtil.getWeiXinAccountId() + "'  order by  orders asc";

		List<MenuEntity> menuList = this.systemService.findByQueryString(hql);
		org.jeecgframework.core.util.LogUtil.info(".....一级菜单的个数是....." + menuList.size());
		Menu menu = new Menu();
		Button firstArr[] = new Button[menuList.size()];
		for (int a = 0; a < menuList.size(); a++) {
			MenuEntity entity = menuList.get(a);
			String hqls = "from MenuEntity where fatherid = '" + entity.getId() + "' and accountId = '" + ResourceUtil.getWeiXinAccountId() + "'  order by  orders asc";
			List<MenuEntity> childList = this.systemService.findByQueryString(hqls);
			org.jeecgframework.core.util.LogUtil.info("....二级菜单的大小....." + childList.size());
			if (childList.size() == 0) {
				if("view".equals(entity.getType())){
					ViewButton viewButton = new ViewButton();
					viewButton.setName(entity.getName());
					viewButton.setType(entity.getType());
					viewButton.setUrl(entity.getUrl());
					firstArr[a] = viewButton;
				}else if("click".equals(entity.getType())){
					CommonButton cb = new CommonButton();
					cb.setKey(entity.getMenuKey());
					cb.setName(entity.getName());
					cb.setType(entity.getType());
					firstArr[a] = cb;
				}
			}
			else {
				ComplexButton complexButton = new ComplexButton();
				complexButton.setName(entity.getName());

				Button[] secondARR = new Button[childList.size()];
				for (int i = 0; i < childList.size(); i++) {
					MenuEntity children = childList.get(i);
					String type = children.getType();
					if ("view".equals(type)) {
						ViewButton viewButton = new ViewButton();
						viewButton.setName(children.getName());
						viewButton.setType(children.getType());
						viewButton.setUrl(children.getUrl());
						secondARR[i] = viewButton;

					} else if ("click".equals(type)) {

						CommonButton cb1 = new CommonButton();
						cb1.setName(children.getName());
						cb1.setType(children.getType());
						cb1.setKey(children.getMenuKey());
						secondARR[i] = cb1;
					}
				}
				complexButton.setSub_button(secondARR);
				firstArr[a] = complexButton;
			}
		}
		menu.setButton(firstArr);
		JSONObject jsonMenu = JSONObject.fromObject(menu);
		String accessToken = weixinAccountService.getAccessToken();

		if(StringUtil.isEmpty(accessToken)){
			//可能是第三方接入
			WeixinAccountEntity weixinEntity = ResourceUtil.getWeiXinAccount();
			accessToken = WeixinThirdUtil.getInstance().getAuthorizerAccessToken(weixinEntity.getWeixinAccountId());
		}


		String url = WeixinUtil.menu_create_url.replace("ACCESS_TOKEN", accessToken);
		JSONObject jsonObject= new JSONObject();
		try {
			jsonObject = WeixinUtil.httpsRequest(url, "POST", jsonMenu.toString());
			LogUtil.info(jsonObject);
			if(jsonObject!=null){
				if (0 == jsonObject.getInt("errcode")) {
					message = "同步菜单信息数据成功！";
				}
				else {
					message = "同步菜单信息数据失败！错误码为："+jsonObject.getInt("errcode")+"错误信息为："+jsonObject.getString("errmsg");
				}
			}else{
				message = "同步菜单信息数据失败！同步自定义菜单URL地址不正确。";
			}
		} catch (Exception e) {
			message = "同步菜单信息数据失败！";
		}finally{
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			j.setMsg(this.message);
		}
		return j;
	}

	@RequestMapping(params = "treeMenu")
	@ResponseBody
	public List<TreeGrid> treeMenu(HttpServletRequest request, ComboTree comboTree) {
		CriteriaQuery cq = new CriteriaQuery(MenuEntity.class);
		if (StringUtil.isNotEmpty(comboTree.getId())) {
			cq.eq("menuEntity.id", comboTree.getId());
		}
		if (StringUtil.isEmpty(comboTree.getId())) {
			cq.isNull("menuEntity.id");
		}
		cq.eq("accountId", ResourceUtil.getWeiXinAccountId());
		cq.add();
		List<MenuEntity> menuList = weixinMenuService.getListByCriteriaQuery(cq, false);

		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();
		TreeGridModel treeGridModel = new TreeGridModel();
		treeGridModel.setTextField("name");
		treeGridModel.setParentText("menuEntity_name");
		treeGridModel.setParentId("menuEntity_id");
		treeGridModel.setSrc("url");
		treeGridModel.setIdField("id");
		treeGridModel.setChildList("menuList");
		treeGrids = systemService.treegrid(menuList, treeGridModel);
		return treeGrids;
	}



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}