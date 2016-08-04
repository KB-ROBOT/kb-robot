package com.kbrobot.entity.ui;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

public class DictionaryTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4371014632896731394L;
	/**
	 * 字段值
	 */
	public String fieldValue ;
	/**
	 * 字典名称
	 */
	public String dictionary;
	
	@Autowired
	private static SystemService systemService;
	
	@Override
	public int doEndTag() throws JspException {
		
		try {
			JspWriter out = this.pageContext.getOut();
			String text = "";
			String value = "";
			//自定义字典
			if(dictionary.contains(",")){
				String[] dic = dictionary.split(",");
				
				String sql = "select " + dic[1] + " as field," + dic[2] + " as text from " + dic[0];
				systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
				List<Map<String, Object>> list = systemService.findForJdbc(sql);
				for (Map<String, Object> map : list){
					value = map.get("field").toString().trim();
					if(value.equals(fieldValue)){
						text = map.get("text").toString().trim();
					}
				}
			}
			//t_s_type预设字典
			else{
				List<TSType> typeList = TSTypegroup.allTypes.get(dictionary.toLowerCase());
				if (typeList != null && !typeList.isEmpty()) {
					for (TSType type : typeList) {
						value = type.getTypecode();
						if(value.equals(fieldValue)){
							text += type.getTypename();
						}
					}
				}
			}
			out.print(text);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
		
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public String getDictionary() {
		return dictionary;
	}
	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}
}
