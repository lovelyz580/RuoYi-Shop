package com.ruoyi.project.wechat.wechat.common;



import com.ruoyi.project.wechat.wechat.Config.OAwechatConfig;
import com.ruoyi.project.wechat.wechat.domain.AccessTokenEntity;
import com.ruoyi.project.wechat.wechat.utils.HttpUtils;
import com.ruoyi.project.wechat.wechat.utils.JsonUtils;

import java.util.TreeMap;

/**
 * 微信相关类
 * 
 * 获取access_token
 */

public class AccessToken {


	/**
	 * 获取access_token
	 * 
	 * @param AppID 微信开发应用ID
	 * @param AppSecret 微信开发应用秘钥
	 * @return
	 */
	public static AccessTokenEntity getAccessToken(String AppID, String AppSecret) {
		// TreeMap方式
		// 参数
		TreeMap<String, String> requestMap = new TreeMap<String, String>();
		requestMap.put("appid", AppID);
		requestMap.put("secret", AppSecret);
		requestMap.put("grant_type", "client_credential");
		// 输出
		System.out.println("AccessToken:requestMap:=============================" + requestMap);
		System.out.println("AccessToken:requestMap:=============================" + requestMap);
				
		String responseXml = HttpUtils.HttpsDefaultExecute("GET", OAwechatConfig.WECHAT_GET_ACCESS_TOKEN_URL, requestMap, null);
		// 输出
		System.out.println("AccessToken:responseXml:=============================" + responseXml);
		System.out.println("AccessToken:responseXml:=============================" + responseXml);

		// 将JSON类型的数据转换为实体类
		AccessTokenEntity accessToken = JsonUtils.fromJson(responseXml, AccessTokenEntity.class);
				
		// 返回数据
		return accessToken == null ? null : accessToken;
	}
	
}
