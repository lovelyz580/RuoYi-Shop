package com.ruoyi.project.wechat.wechat.domain;

/**
 * 微信相关类
 * 
 * 模板消息
 * 
 * 发送模板消息返回的结果对象
 */

public class TemplateSendEntity {

	/**
	 * 获取到的错误码
	 */
	private int errcode;

	/**
	 * 获取到的错误信息
	 */
	private String errmsg;

	/**
	 * 获取到的msgid
	 */
	private long msgid;

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

	public long getMsgid() {
		return msgid;
	}

	public void setMsgid(long msgid) {
		this.msgid = msgid;
	}

}
