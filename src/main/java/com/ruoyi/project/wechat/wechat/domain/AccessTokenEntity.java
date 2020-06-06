package com.ruoyi.project.wechat.wechat.domain;

/**
 * 微信相关类
 * 
 * 获取access_token返回的结果对象
 */

public class AccessTokenEntity {
	
	/**
	 * 获取到的access_token
	 */
	private String access_token;
	
	/**
	 * 凭证有效时间，单位：秒
	 */
	private int expires_in;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

}
