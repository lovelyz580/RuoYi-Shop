package com.ruoyi.project.wechat.wechat.utils;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 微信工具类
 * 
 * HTTP 自定义信任管理器
 * 
 * 负责发起get、post请求并获取返回数据
 */

public class MyX509TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

	}

	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

}
