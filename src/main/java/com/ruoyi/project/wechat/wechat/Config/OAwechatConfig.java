package com.ruoyi.project.wechat.wechat.Config;

/**
 * 微信支付 
 * 
 * 全局设置
 */

public class OAwechatConfig {
	
	/**
	 * 字符编码
	 */
	public static final String CHARACTER_ENCODING = "UTF-8";
	

	/**
	 * 微信相关URL
	 */
	public static final String WECHAT_GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token"; // 获取access_token的URL
	public static final String WECHAT_GET_JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket"; // 获取jsapi_ticket的URL // 微信扫一扫功能
	public static final String WECHAT_GET_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token"; // 获取openId的URL
	public final static String WECHAT_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder"; // 微信支付URL
//	public final static String WECHAT_SEND_EED_PACK_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers"; // 企业向个人账号付款URL
	public final static String WECHAT_GET_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template"; // 获得模板消息的模板id
	public final static String WECHAT_SEND_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send"; // 发送模板消息
	public final static String WECHAT_DELETE_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/template/del_private_template"; // 删除模板消息
	
	/**
	 * 微信相关配置
	 */
	public final static String WECHAT_PAY_IP = "211.149.163.128"; // 发起支付IP
	public final static String WECHAT_PAY_NOTIFY_URL = "http://www.qyyxx.com/wechatpay/notify.action"; // 微信支付回调接口
	
	/**
	 * 微信模板库的模板编号
	 */
	public final static String WECHAT_TEMPLATE_ID_APPT_SUCCESS = "OPENTM203483517"; // 预约成功通知
	public final static String WECHAT_TEMPLATE_ID_APPT_FAIL = "OPENTM200395767"; // 预约失败通知
	
	/**
	 * 返回状态
	 */
	public final static String WECHAT_PAY_STATE_SUCCESS = "0"; // 成功
	public final static String WECHAT_PAY_STATE_ERROR = "1"; // 失败
	public final static String WECHAT_PAY_STATE_SYSTEM_ERROR = "-1"; // 系统错误
	
}
