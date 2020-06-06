package com.ruoyi.project.wechat.wechat.common;



import com.ruoyi.project.wechat.wechat.Config.OAwechatConfig;
import com.ruoyi.project.wechat.wechat.domain.JsapiTicketEntity;
import com.ruoyi.project.wechat.wechat.utils.HttpUtils;
import com.ruoyi.project.wechat.wechat.utils.JsonUtils;

import java.util.TreeMap;

/**
 * 微信相关类
 * 
 * 微信扫一扫功能
 * 
 * 获取jsapi_ticket
 */

public class JsapiTicket {



	/**
	 * 获取jsapi_ticket
	 * 
	 * @param access_token 
	 * @return
	 */
	public static JsapiTicketEntity getJsapiTicket(String access_token) {
		// TreeMap方式
		// 参数
		TreeMap<String, String> requestMap = new TreeMap<String, String>();
		requestMap.put("access_token", access_token);
		requestMap.put("type", "jsapi");
		// 输出
		System.out.println("JsapiTicket:requestMap:=============================" + requestMap);
		System.out.println("JsapiTicket:requestMap:=============================" + requestMap);
				
		String responseXml = HttpUtils.HttpsDefaultExecute("GET", OAwechatConfig.WECHAT_GET_JSAPI_TICKET_URL, requestMap, null);
		// 输出
		System.out.println("JsapiTicket:responseXml:=============================" + responseXml);
		System.out.println("JsapiTicket:responseXml:=============================" + responseXml);

		// 将JSON类型的数据转换为实体类
		JsapiTicketEntity jsapiTicket = JsonUtils.fromJson(responseXml, JsapiTicketEntity.class);
				
		// 返回数据
		return jsapiTicket == null ? null : jsapiTicket;
	}
	
}
