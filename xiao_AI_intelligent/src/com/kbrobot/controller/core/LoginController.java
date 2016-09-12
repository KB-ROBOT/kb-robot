package com.kbrobot.controller.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.extend.datasource.DataSourceContextHolder;
import org.jeecgframework.core.extend.datasource.DataSourceType;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.IpUtil;
import org.jeecgframework.core.util.ListtoMenu;
import org.jeecgframework.core.util.NumberComparator;
import org.jeecgframework.core.util.PasswordUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.web.system.manager.ClientManager;
import org.jeecgframework.web.system.pojo.base.Client;
import org.jeecgframework.web.system.pojo.base.TSConfig;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSFunction;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSRoleFunction;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.geetest.sdk.GeetestConfig;
import com.geetest.sdk.GeetestLib;

import weixin.guanjia.account.entity.WeixinAccountEntity;
import weixin.guanjia.account.service.WeixinAccountServiceI;
import weixin.util.WeiXinConstants;

/**
 * 登陆初始化控制器
 * @author 张代浩
 * 
 */
@Scope("prototype")
@Controller
@RequestMapping("/loginController")
public class LoginController extends BaseController{
	private Logger log = Logger.getLogger(LoginController.class);
	private SystemService systemService;
	@Autowired
	private WeixinAccountServiceI weixinAccountService;
	private UserService userService;
	private String message = null;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	@Autowired
	public void setUserService(UserService userService) {

		this.userService = userService;
	}

	/**
	 * 验证码初始化
	 * @param req
	 * @param res
	 * @return
	 */
	@RequestMapping(params = "initRandomCode")
	@ResponseBody
	public AjaxJson initRandomCode(HttpServletRequest req,HttpServletResponse res){
		AjaxJson j = new AjaxJson();

		GeetestLib gtSdk = new GeetestLib(GeetestConfig.getCaptcha_id(), GeetestConfig.getPrivate_key());

		//进行验证预处理
		int gtServerStatus = gtSdk.preProcess();
		//将服务器状态设置到session中
		HttpSession session = ContextHolderUtils.getSession();
		session.setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
		String result = gtSdk.getResponseStr();
		Map<String,Object> attr = new HashMap<String, Object>();
		if(!StringUtil.isEmpty(result)){
			j.setSuccess(true);
			attr.put("initResult", result);
			j.setAttributes(attr);
		}
		else{
			j.setSuccess(false);
			attr.put("msg", result);
			j.setAttributes(attr);
		}
		return j;
	}

	/**
	 * 检查用户名称
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "checkuser")
	@ResponseBody
	public AjaxJson checkuser(TSUser user, HttpServletRequest req) {

		HttpSession session = ContextHolderUtils.getSession();
		DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_jeecg);

		AjaxJson j = new AjaxJson();

		//验证码校验
		String challenge = req.getParameter(GeetestLib.fn_geetest_challenge);
		String validate = req.getParameter(GeetestLib.fn_geetest_validate);
		String seccode = req.getParameter(GeetestLib.fn_geetest_seccode);
		GeetestLib gtSdk = new GeetestLib(GeetestConfig.getCaptcha_id(), GeetestConfig.getPrivate_key());
		int gt_server_status_code = (Integer)session.getAttribute(gtSdk.gtServerStatusSessionKey);
		int gtResult = 0;//1表示验证成功0表示验证失败
		if (gt_server_status_code == 1) {
			//gt-server正常，向gt-server进行二次验证
			gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode);
			System.out.println(gtResult);
		} else {
			// gt-server非正常情况下，进行failback模式验证
			gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
		}
		
		if(gtResult!=1){
			j.setSuccess(false);
			j.setMsg("验证码验证失败！");
			return j;
		}
		

		int users = userService.getList(TSUser.class).size();

		if (users == 0) {
			j.setMsg("a");
			j.setSuccess(false);
		} else {
			System.out.println("....name..."+user.getUserName()+"...password..."+user.getPassword());
			TSUser u = userService.checkUserExits(user);
			if(u == null) {
				j.setMsg("用户名或密码错误!");
				j.setSuccess(false);
				return j;
			}
			TSUser u2 = userService.getEntity(TSUser.class, u.getId());
			if (u != null&&u2.getStatus()!=0) {

				message = "用户: " + user.getUserName() + "["+ u.getTSDepart().getDepartname() + "]" + "登录成功";
				Client client = new Client();
				client.setIp(IpUtil.getIpAddr(req));
				client.setLogindatetime(new Date());
				client.setUser(u);
				//登陆之后添加session
				ClientManager.getInstance().addClinet(session.getId(), client);
				// 添加登陆日志
				systemService.addLog(message, Globals.Log_Type_LOGIN,Globals.Log_Leavel_INFO);

			} else {
				j.setMsg("用户名或密码错误!");
				j.setSuccess(false);
			}
		}
		return j;
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(params = "login")
	public String login(ModelMap modelMap,HttpServletRequest request) {
		DataSourceContextHolder.setDataSourceType(DataSourceType.dataSource_jeecg);
		TSUser user = ResourceUtil.getSessionUserName();
		String roles = "";
		if (user != null) {
			//首先判断session是否有当前微信账号
			WeixinAccountEntity  weixinAccountEntity = (WeixinAccountEntity) request.getSession().getAttribute(WeiXinConstants.WEIXIN_ACCOUNT);
			if(weixinAccountEntity==null){
				weixinAccountEntity = weixinAccountService.findLoginWeixinAccount();
				if(weixinAccountEntity==null||weixinAccountEntity.getId().equals("-1")){
					//把当前微信账号信息放在session里面
					weixinAccountEntity.setAccountName("[未绑定公众号]");
					request.getSession().setAttribute(WeiXinConstants.WEIXIN_ACCOUNT, weixinAccountEntity);
				}
				else{
					//把当前微信账号信息放在session里面
					request.getSession().setAttribute(WeiXinConstants.WEIXIN_ACCOUNT, weixinAccountEntity);
				}
				
			}

			//初始化当前用户信息
			List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
			for (TSRoleUser ru : rUsers) {
				TSRole role = ru.getTSRole();
				roles += role.getRoleName() + ",";
			}
			if (roles.length() > 0) {
				roles = roles.substring(0, roles.length() - 1);
			}
			/*modelMap.put("roleName", roles);
			modelMap.put("userName", user.getUserName());*/
			request.getSession().setAttribute("CKFinder_UserRole", "admin");
			request.getSession().setAttribute("sessionLoginUser", rUsers);
			
			if(user.getUserName().equals("admin")){
				return "main/main";
			}
			
			//访问数据统计
			String sql = "";
			/**
			 * 昨天总访问次数
			 */
			sql = "SELECT COUNT(*) FROM weixin_conversation_client";
			sql += " WHERE ";
			sql += "weixin_account_Id = '"+weixinAccountEntity.getWeixinAccountId()+"'";
			sql += " AND ";
			sql += "add_date > DATE(CURDATE()-1)";
			sql += " AND ";
			sql += "add_date < CURDATE()";
			
			Long lastDayVisitNum =  systemService.getCountForJdbcParam(sql, null);
			
			
			/**
			 * 昨日机器人咨询数量
			 */
			sql = "SELECT COUNT(*) FROM weixin_conversation_client";
			sql += " WHERE ";
			sql += "weixin_account_Id = '"+weixinAccountEntity.getWeixinAccountId()+"'";
			sql += " AND ";
			sql += "add_date > DATE(CURDATE()-1)";
			sql += " AND ";
			sql += "add_date < CURDATE()";
			Long lastDayRobotAskNum =  systemService.getCountForJdbcParam(sql, null);
			
			/**
			 * 昨日人工客服咨询量
			 */
			Long lastDayArtificialVisitNum =  lastDayVisitNum - lastDayRobotAskNum;
			
			/**
			 * 昨日新增知识数量
			 */
			sql = "SELECT COUNT(*) FROM robot_question";
			sql += " WHERE ";
			sql += "ACCOUNT_ID = '"+weixinAccountEntity.getId()+"'";
			sql += " AND ";
			sql += "CREATE_TIME > DATE(CURDATE()-1)";
			sql += " AND ";
			sql += "CREATE_TIME < CURDATE()";
			
			Long lastDayAddQuestionNum =  systemService.getCountForJdbcParam(sql, null);
			
			
			modelMap.put("lastDayVisitNum", lastDayVisitNum);
			modelMap.put("lastDayRobotAskNum", lastDayRobotAskNum);
			modelMap.put("lastDayArtificialVisitNum", lastDayArtificialVisitNum);
			modelMap.put("lastDayAddQuestionNum", lastDayAddQuestionNum);
			
			
			return "kbrobot/home";

		} else {
			return "kbrobot/login";
		}

	}

	/**
	 * 退出系统
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "logout")
	public ModelAndView logout(HttpServletRequest request) {
		HttpSession session = ContextHolderUtils.getSession();
		TSUser user = ResourceUtil.getSessionUserName();
		systemService.addLog("用户" + user.getUserName() + "已退出",
				Globals.Log_Type_EXIT, Globals.Log_Leavel_INFO);
		ClientManager.getInstance().removeClinet(session.getId());
		session.removeAttribute(Globals.USER_SESSION);
		session.removeAttribute(WeiXinConstants.WEIXIN_ACCOUNT);
		ModelAndView modelAndView = new ModelAndView(new RedirectView("loginController.do?login"));
		return modelAndView;
	}


	/**
	 * 跳转到注册
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goRegister")
	public ModelAndView goRegister(HttpServletRequest request){
		TSUser loginUser = ResourceUtil.getSessionUserName();
		if(loginUser!=null){
			//跳转到注册先退出登陆
			logout(request);
		}
		ModelAndView modelAndView = new ModelAndView("kbrobot/register");
		return modelAndView;
	}

	/**
	 * 注册
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(params = "register")
	@ResponseBody
	public AjaxJson registerUser(HttpServletRequest request,TSUser user){
		AjaxJson j = new AjaxJson();
		TSUser loginUser = ResourceUtil.getSessionUserName();
		if(loginUser!=null){
			j.setSuccess(false);
			j.setMsg("请先退出当前用户");
			return j;
		}

		if(user!=null){
			List<TSUser> userList = userService.findByProperty(TSUser.class, "userName", user.getUserName());
			if(userList!=null&&userList.size()>0){
				j.setSuccess(false);
				j.setMsg("用户名已经存在");
			}
			else{
				TSRole tsRole =this.systemService.findUniqueByProperty(TSRole.class,"roleCode", "manager");
				String roleid = tsRole.getId();
				TSRole role = systemService.getEntity(TSRole.class,roleid);
				//设置用户状态
				user.setStatus(Globals.User_Normal);
				//保存加密后的密码
				user.setPassword(PasswordUtil.encrypt(user.getUserName(), user.getPassword(), PasswordUtil.getStaticSalt()));
				//设置用户组(默认设置为普通用户)
				TSDepart depart = new TSDepart();
				depart.setId("4028838e564556cc015645582ce20002");
				user.setTSDepart(depart);
				//1 普通用户
				user.setType("1");
				userService.save(user);

				//保存权限
				TSRoleUser rUser = new TSRoleUser();
				rUser.setTSRole(role);
				rUser.setTSUser(user);
				systemService.save(rUser);

				systemService.addLog("用户: " + user.getUserName() + "添加成功", Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
				j.setSuccess(true);
			}
		}
		else{
			j.setSuccess(false);
			j.setMsg("用户名或密码为空");
		}
		return j;
	}
	
	
	
	
	
	
	
	

	/**
	 * 菜单跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "left")
	public ModelAndView left(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		HttpSession session = ContextHolderUtils.getSession();
		ModelAndView modelAndView = new ModelAndView();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			modelAndView.setView(new RedirectView("loginController.do?login"));
		}else{
			List<TSConfig> configs = userService.loadAll(TSConfig.class);
			for (TSConfig tsConfig : configs) {
				request.setAttribute(tsConfig.getCode(), tsConfig.getContents());
			}
			modelAndView.setViewName("main/left");
			request.setAttribute("menuMap", getFunctionMap(user));
		}
		return modelAndView;
	}

	/**
	 * 获取权限的map
	 * 
	 * @param user
	 * @return
	 */
	private Map<Integer, List<TSFunction>> getFunctionMap(TSUser user) {
		Map<Integer, List<TSFunction>> functionMap = new HashMap<Integer, List<TSFunction>>();
		Map<String, TSFunction> loginActionlist = getUserFunction(user);
		if (loginActionlist.size() > 0) {
			Collection<TSFunction> allFunctions = loginActionlist.values();
			for (TSFunction function : allFunctions) {
				if (!functionMap.containsKey(function.getFunctionLevel() + 0)) {
					functionMap.put(function.getFunctionLevel() + 0,
							new ArrayList<TSFunction>());
				}
				functionMap.get(function.getFunctionLevel() + 0).add(function);
			}
			// 菜单栏排序
			Collection<List<TSFunction>> c = functionMap.values();
			for (List<TSFunction> list : c) {
				Collections.sort(list, new NumberComparator());
			}
		}
		return functionMap;
	}

	/**
	 * 获取用户菜单列表
	 * 
	 * @param user
	 * @return
	 */
	private Map<String, TSFunction> getUserFunction(TSUser user) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		if (client.getFunctions() == null || client.getFunctions().size() == 0) {
			Map<String, TSFunction> loginActionlist = new HashMap<String, TSFunction>();
			List<TSRoleUser> rUsers = systemService.findByProperty(
					TSRoleUser.class, "TSUser.id", user.getId());
			for (TSRoleUser ru : rUsers) {
				TSRole role = ru.getTSRole();
				List<TSRoleFunction> roleFunctionList = systemService
						.findByProperty(TSRoleFunction.class, "TSRole.id",
								role.getId());
				for (TSRoleFunction roleFunction : roleFunctionList) {
					TSFunction function = roleFunction.getTSFunction();
					loginActionlist.put(function.getId(), function);
				}
			}
			client.setFunctions(loginActionlist);
		}
		return client.getFunctions();
	}

	/**
	 * 首页跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "home")
	public ModelAndView home(HttpServletRequest request) {
		return new ModelAndView("main/home");
	}
	/**
	 * 无权限页面提示跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "noAuth")
	public ModelAndView noAuth(HttpServletRequest request) {
		return new ModelAndView("common/noAuth");
	}
	/**
	 * @Title: top
	 * @Description: bootstrap头部菜单请求
	 * @param request
	 * @return ModelAndView
	 * @throws
	 */
	@RequestMapping(params = "top")
	public ModelAndView top(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		HttpSession session = ContextHolderUtils.getSession();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(
					new RedirectView("loginController.do?login"));
		}
		request.setAttribute("menuMap", getFunctionMap(user));
		List<TSConfig> configs = userService.loadAll(TSConfig.class);
		for (TSConfig tsConfig : configs) {
			request.setAttribute(tsConfig.getCode(), tsConfig.getContents());
		}
		return new ModelAndView("main/bootstrap_top");
	}
	/**
	 * @Title: top
	 * @author gaofeng
	 * @Description: shortcut头部菜单请求
	 * @param request
	 * @return ModelAndView
	 * @throws
	 */
	@RequestMapping(params = "shortcut_top")
	public ModelAndView shortcut_top(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		HttpSession session = ContextHolderUtils.getSession();
		// 登陆者的权限
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(
					new RedirectView("loginController.do?login"));
		}
		request.setAttribute("menuMap", getFunctionMap(user));
		List<TSConfig> configs = userService.loadAll(TSConfig.class);
		for (TSConfig tsConfig : configs) {
			request.setAttribute(tsConfig.getCode(), tsConfig.getContents());
		}
		return new ModelAndView("main/shortcut_top");
	}

	/**
	 * @Title: top
	 * @author:gaofeng
	 * @Description: shortcut头部菜单一级菜单列表，并将其用ajax传到页面，实现动态控制一级菜单列表
	 * @return AjaxJson
	 * @throws
	 */
	@RequestMapping(params = "primaryMenu")
	@ResponseBody
	public String getPrimaryMenu() {
		List<TSFunction> primaryMenu = getFunctionMap(ResourceUtil.getSessionUserName()).get(0);
		String floor = "";
		for (TSFunction function : primaryMenu) {
			if(function.getFunctionLevel() == 0){

				if("Online 开发".equals(function.getFunctionName())){

					floor += " <li><img class='imag1' src='plug-in/login/images/online.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/online_up.png' style='display: none;' />" + " </li> ";
				}else if("统计查询".equals(function.getFunctionName())){

					floor += " <li><img class='imag1' src='plug-in/login/images/guanli.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/guanli_up.png' style='display: none;' />" + " </li> ";
				}else if("系统管理".equals(function.getFunctionName())){

					floor += " <li><img class='imag1' src='plug-in/login/images/xtgl.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/xtgl_up.png' style='display: none;' />" + " </li> ";
				}else if("常用示例".equals(function.getFunctionName())){

					floor += " <li><img class='imag1' src='plug-in/login/images/cysl.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/cysl_up.png' style='display: none;' />" + " </li> ";
				}else if("系统监控".equals(function.getFunctionName())){

					floor += " <li><img class='imag1' src='plug-in/login/images/xtjk.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/xtjk_up.png' style='display: none;' />" + " </li> ";
				}else{
					//其他的为默认通用的图片模式
					String s = "";
					if(function.getFunctionName().length()>=5 && function.getFunctionName().length()<7){
						s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>"+ function.getFunctionName() +"</span></div>";
					}else if(function.getFunctionName().length()<5){
						s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>"+ function.getFunctionName() +"</div>";
					}else if(function.getFunctionName().length()>=7){
						s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'><span style='letter-spacing:-1px;'>"+ function.getFunctionName().substring(0, 6) +"</span></div>";
					}
					floor += " <li style='position: relative;'><img class='imag1' src='plug-in/login/images/default.png' /> "
							+ " <img class='imag2' src='plug-in/login/images/default_up.png' style='display: none;' />"
							+ s +"</li> ";
				}
			}
		}

		return floor;
	}


	/**
	 * 返回数据
	 */
	@RequestMapping(params = "getPrimaryMenuForWebos")
	@ResponseBody
	public AjaxJson getPrimaryMenuForWebos() {
		AjaxJson j = new AjaxJson();
		//将菜单加载到Session，用户只在登录的时候加载一次
		Object getPrimaryMenuForWebos =  ContextHolderUtils.getSession().getAttribute("getPrimaryMenuForWebos");
		if(oConvertUtils.isNotEmpty(getPrimaryMenuForWebos)){
			j.setMsg(getPrimaryMenuForWebos.toString());
		}else{
			String PMenu = ListtoMenu.getWebosMenu(getFunctionMap(ResourceUtil.getSessionUserName()));
			ContextHolderUtils.getSession().setAttribute("getPrimaryMenuForWebos", PMenu);
			j.setMsg(PMenu);
		}
		return j;
	}
}
