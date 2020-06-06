package com.ruoyi.project.wechat.wechat.domain;

/**
 * 微信相关类
 * 
 * 模板消息
 * 
 * 获取template_id返回的结果对象
 */

public class TemplateEntity {

	/**
	 * 获取到的错误码
	 */
	private int errcode;

	/**
	 * 获取到的错误信息
	 */
	private String errmsg;

	/**
	 * 获取到的template_id
	 */
	private String template_id;

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

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

}
