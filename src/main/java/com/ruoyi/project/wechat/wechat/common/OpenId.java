package com.ruoyi.project.wechat.wechat.common;
import com.ruoyi.project.wechat.wechat.Config.OAwechatConfig;
import com.ruoyi.project.wechat.wechat.domain.OpenIdEntity;
import com.ruoyi.project.wechat.wechat.utils.HttpUtils;
import com.ruoyi.project.wechat.wechat.utils.JsonUtils;

import java.util.TreeMap;
import java.util.logging.Logger;


/**
 * openId
 */

public class OpenId {



	/**
	 * 获取openId
	 * 
	 * @param AppID 微信开发应用ID
	 * @param AppSecret 微信开发应用秘钥
	 * @param code 
	 * @return
	 */
	public static OpenIdEntity getOpenId(String AppID, String AppSecret, String code) {
		// TreeMap方式
		// 参数
		TreeMap<String, String> requestMap = new TreeMap<String, String>();
		requestMap.put("appid", AppID);
		requestMap.put("secret", AppSecret);
		requestMap.put("code", code);
		requestMap.put("grant_type", "authorization_code");
		// 输出
		System.out.println("OpenId:requestMap:=============================" + requestMap);
		System.out.println("OpenId:requestMap:=============================" + requestMap);
		
		String responseXml = HttpUtils.HttpsDefaultExecute("GET", OAwechatConfig.WECHAT_GET_OPENID_URL, requestMap, null);
		// 输出
		System.out.println("OpenId:responseXml:=============================" + responseXml);
		System.out.println("OpenId:responseXml:=============================" + responseXml);

		// 将JSON类型的数据转换为实体类
		OpenIdEntity openId = JsonUtils.fromJson(responseXml, OpenIdEntity.class);
		
		// 返回数据
		return openId == null ? null : openId;
	}

}
