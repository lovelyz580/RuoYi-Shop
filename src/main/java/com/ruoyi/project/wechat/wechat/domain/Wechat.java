package com.ruoyi.project.wechat.wechat.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 微信相关数据表 pflm_wechat
 * 
 * @author lovelyz
 * @date 2020-06-06
 */
public class Wechat extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** ID */
	private Integer iD;
	/** 类型 */
	private String type;
	/** appid */
	private String appId;
	/** 密钥 */
	private String appSecret;
	/** 商户号id */
	private String mCHID;
	/** 微信支付api密钥 */
	private String aPI;
	/** token */
	private String accessToken;
	/** JsapiTicket */
	private String jsapiTicket;
	/** token有效期 */
	private Date tokenBuildTime;
	/** session_key */
	private String sessionKey;
	/** session有效期 */
	private Date sessionBuildTime;
	/** 支付IP */
	private String payIp;
	/** 支付回调地址 */
	private String payNotifyUrl;

	public void setID(Integer iD) 
	{
		this.iD = iD;
	}

	public Integer getID() 
	{
		return iD;
	}
	public void setType(String type) 
	{
		this.type = type;
	}

	public String getType() 
	{
		return type;
	}
	public void setAppId(String appId) 
	{
		this.appId = appId;
	}

	public String getAppId() 
	{
		return appId;
	}
	public void setAppSecret(String appSecret) 
	{
		this.appSecret = appSecret;
	}

	public String getAppSecret() 
	{
		return appSecret;
	}
	public void setMCHID(String mCHID) 
	{
		this.mCHID = mCHID;
	}

	public String getMCHID() 
	{
		return mCHID;
	}
	public void setAPI(String aPI) 
	{
		this.aPI = aPI;
	}

	public String getAPI() 
	{
		return aPI;
	}
	public void setAccessToken(String accessToken) 
	{
		this.accessToken = accessToken;
	}

	public String getAccessToken() 
	{
		return accessToken;
	}
	public void setJsapiTicket(String jsapiTicket) 
	{
		this.jsapiTicket = jsapiTicket;
	}

	public String getJsapiTicket() 
	{
		return jsapiTicket;
	}
	public void setTokenBuildTime(Date tokenBuildTime) 
	{
		this.tokenBuildTime = tokenBuildTime;
	}

	public Date getTokenBuildTime() 
	{
		return tokenBuildTime;
	}
	public void setSessionKey(String sessionKey) 
	{
		this.sessionKey = sessionKey;
	}

	public String getSessionKey() 
	{
		return sessionKey;
	}
	public void setSessionBuildTime(Date sessionBuildTime) 
	{
		this.sessionBuildTime = sessionBuildTime;
	}

	public Date getSessionBuildTime() 
	{
		return sessionBuildTime;
	}
	public void setPayIp(String payIp) 
	{
		this.payIp = payIp;
	}

	public String getPayIp() 
	{
		return payIp;
	}
	public void setPayNotifyUrl(String payNotifyUrl) 
	{
		this.payNotifyUrl = payNotifyUrl;
	}

	public String getPayNotifyUrl() 
	{
		return payNotifyUrl;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("iD", getID())
            .append("type", getType())
            .append("appId", getAppId())
            .append("appSecret", getAppSecret())
            .append("mCHID", getMCHID())
            .append("aPI", getAPI())
            .append("accessToken", getAccessToken())
            .append("jsapiTicket", getJsapiTicket())
            .append("tokenBuildTime", getTokenBuildTime())
            .append("sessionKey", getSessionKey())
            .append("sessionBuildTime", getSessionBuildTime())
            .append("payIp", getPayIp())
            .append("payNotifyUrl", getPayNotifyUrl())
            .toString();
    }
}
