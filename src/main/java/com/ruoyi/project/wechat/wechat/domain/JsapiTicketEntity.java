package com.ruoyi.project.wechat.wechat.domain;

/**
 * 微信相关类
 * 
 * 微信扫一扫功能
 * 
 * 获取jsapi_ticket返回的结果对象
 */

public class JsapiTicketEntity {

	/**
	 * 获取到的错误码
	 */
	private int errcode;

	/**
	 * 获取到的错误信息
	 */
	private String errmsg;

	/**
	 * 获取到的jsapi_ticket
	 */
	private String ticket;

	/**
	 * 凭证有效时间，单位：秒
	 */
	private int expires_in;

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

}
