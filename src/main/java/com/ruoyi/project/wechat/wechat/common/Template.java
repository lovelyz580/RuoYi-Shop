package com.ruoyi.project.wechat.wechat.common;



import com.ruoyi.project.wechat.wechat.Config.OAwechatConfig;
import com.ruoyi.project.wechat.wechat.domain.TemplateDeleteEntity;
import com.ruoyi.project.wechat.wechat.domain.TemplateEntity;
import com.ruoyi.project.wechat.wechat.domain.TemplateSendEntity;
import com.ruoyi.project.wechat.wechat.utils.HttpUtils;
import com.ruoyi.project.wechat.wechat.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 发送模板消息
 */

public class Template {



	/**
	 * 根据模板库的模板编号获得我的模板消息的模板id
	 * 
	 * @param access_token
	 * @param template_id_short
	 * @return
	 */
	public static TemplateEntity getTemplate(String access_token, String template_id_short) {
		// TreeMap方式
		// 参数 access_token
		TreeMap<String, String> requestMap = new TreeMap<String, String>();
		requestMap.put("access_token", access_token);
		
		// TreeMap方式
		// 参数
		TreeMap<String, String> requesData = new TreeMap<String, String>();
		requesData.put("template_id_short", template_id_short);
		// 将Map转换为String
		String requesDataString = JsonUtils.toJson(requesData);
		
		// 输出
		System.out.println("Template:getTemplate:requestMap:=============================" + requestMap);
		System.out.println("Template:getTemplate:requestMap:=============================" + requestMap);
		System.out.println("Template:getTemplate:requestData:=============================" + requesDataString);
		System.out.println("Template:getTemplate:requestData:=============================" + requesDataString);

		String responseXml = HttpUtils.HttpsDefaultExecute("POST", OAwechatConfig.WECHAT_GET_TEMPLATE_URL, requestMap, requesDataString);
		// 输出
		System.out.println("Template:getTemplate:responseXml:=============================" + responseXml);
		System.out.println("Template:getTemplate:responseXml:=============================" + responseXml);

		// 将JSON类型的数据转换为实体类
		TemplateEntity template = JsonUtils.fromJson(responseXml, TemplateEntity.class);

		// 返回数据
		return template == null ? null : template;
	}

	/**
	 * 发送模板消息
	 * 
	 * @param access_token
	 * @param openId
	 * @param template_id
	 * @param title
	 * @param dataList
	 * @param remark
	 * @return
	 */
	public static TemplateSendEntity sendTemplate(String access_token, String openId, String template_id, String title, List<String> dataList, String remark) {
		// TreeMap方式
		// 参数 access_token
		TreeMap<String, String> requestMap = new TreeMap<>();
		requestMap.put("access_token", access_token);
		// 输出
		System.out.println("Template:sendTemplate:requestMap:=============================" + requestMap);
		System.out.println("Template:sendTemplate:requestMap:=============================" + requestMap);
		
		// 参数
		TreeMap<String, Object> requesData = new TreeMap<>();
		requesData.put("touser", openId);
		requesData.put("template_id", template_id);
		requesData.put("url", "www.baidu.com");//可变更
		
		// 模板消息Map
		Map<String, Object> templateData = new TreeMap<>();
		
		// 模板消息标题
		Map<String, Object> titleMap = new HashMap<>();
		titleMap.put("value", title);
		titleMap.put("color", "#173177"); // 文字颜色
		// 将模板消息标题添加到模板消息Map中
		templateData.put("first", titleMap);
		
		// 模板消息数据
		for (int i = 1; i < dataList.size() + 1; i++) {
			Map<String, Object> keywordMap = new HashMap<>();
			keywordMap.put("value", dataList.get(i - 1));
			keywordMap.put("color", "#173177");
			// 将模板消息数据添加到模板消息Map中
			templateData.put("keyword" + i, keywordMap);
		}
		
		// 模板消息备注
		Map<String, Object> remarkMap = new HashMap<>();
		remarkMap.put("value", remark);
		remarkMap.put("color", "#173177");
		// 将模板消息备注添加到模板消息Map中
		templateData.put("remark", remarkMap);
		
		// 将模板消息Map添加到参数中
		requesData.put("data", templateData);
		
		// 将Map转换为String
		String requesDataString = JsonUtils.toJson(requesData);
		
		// 输出
		System.out.println("Template:sendTemplate:requestMap:=============================" + requestMap);
		System.out.println("Template:sendTemplate:requestMap:=============================" + requestMap);
		System.out.println("Template:sendTemplate:requestData:=============================" + requesDataString);
		System.out.println("Template:sendTemplate:requestData:=============================" + requesDataString);

		String responseXml = HttpUtils.HttpsDefaultExecute("POST", OAwechatConfig.WECHAT_SEND_TEMPLATE_URL, requestMap, requesDataString);
		// 输出
		System.out.println("Template:sendTemplate:responseXml:=============================" + responseXml);
		System.out.println("Template:sendTemplate:responseXml:=============================" + responseXml);
				
		// 将JSON类型的数据转换为实体类
		TemplateSendEntity templateSend = JsonUtils.fromJson(responseXml, TemplateSendEntity.class);

		// 返回数据
		return templateSend == null ? null : templateSend;
	}
	
	/**
	 * 根据我的模板消息的模板id删除模板
	 * 
	 * @param access_token
	 * @param template_id
	 * @return
	 */
	public static TemplateDeleteEntity deleteTemplate(String access_token, String template_id) {
		// TreeMap方式
		// 参数 access_token
		TreeMap<String, String> requestMap = new TreeMap<String, String>();
		requestMap.put("access_token", access_token);
		
		// TreeMap方式
		// 参数
		TreeMap<String, String> requesData = new TreeMap<String, String>();
		requesData.put("template_id", template_id);
		// 将Map转换为String
		String requesDataString = JsonUtils.toJson(requesData);
		
		// 输出
		System.out.println("Template:deleteTemplate:requestMap:=============================" + requestMap);
		System.out.println("Template:deleteTemplate:requestMap:=============================" + requestMap);
		System.out.println("Template:deleteTemplate:requestData:=============================" + requesDataString);
		System.out.println("Template:deleteTemplate:requestData:=============================" + requesDataString);

		String responseXml = HttpUtils.HttpsDefaultExecute("POST", OAwechatConfig.WECHAT_DELETE_TEMPLATE_URL, requestMap, requesDataString);
		// 输出
		System.out.println("Template:deleteTemplate:responseXml:=============================" + responseXml);
		System.out.println("Template:deleteTemplate:responseXml:=============================" + responseXml);

		// 将JSON类型的数据转换为实体类
		TemplateDeleteEntity templateDelete = JsonUtils.fromJson(responseXml, TemplateDeleteEntity.class);

		// 返回数据
		return templateDelete == null ? null : templateDelete;
	}
	
}
