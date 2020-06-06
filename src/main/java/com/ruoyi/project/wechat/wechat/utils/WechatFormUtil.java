package com.ruoyi.project.wechat.wechat.utils;

import com.ruoyi.project.wechat.wechat.domain.TemplateData;
import com.ruoyi.project.wechat.wechat.domain.WxMssVo;
import net.sf.json.JSONObject;


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class WechatFormUtil {
	/**
	 * 创建模板消息
	 * @param openId
	 * @param template_id
	 * @param url
	 * @param topcolor
	 * @param carrierName
	 * @param waybillCode
	 * @param waybillDesc
	 * @param waybillMark
	 * @return
	 */

	public static String makeRouteMessage(String openId, String template_id, String form_id, String url, String topcolor, String carrierName, String waybillCode, String waybillDesc, String waybillMark){
		WxMssVo template = new WxMssVo();
		template.setTouser(openId);
		template.setTemplate_id(template_id);
		template.setForm_id(form_id);
		template.setUrl(url);
		template.setTopcolor(topcolor);
		Map<String, TemplateData> data = new HashMap<String, TemplateData>();
		data.put("keyword1", new TemplateData(carrierName+"\n","#ff6600"));
		data.put("keyword2", new TemplateData(waybillCode+"\n","#ff6600"));
		data.put("keyword3", new TemplateData(waybillDesc,"#ff6600"));
		data.put("keyword4", new TemplateData(waybillMark,"#ff6600"));
		template.setData(data);
		JSONObject jsonObject = JSONObject.fromObject(template);
		System.out.println(template);
		return jsonObject+"";
	}
	/**
	 * 发送消息
	 * @param accessToken
	 * @param jsonMsg
	 * @return
	 */
	public static boolean sendTemplateMessage(String accessToken, String jsonMsg){
		System.out.println("消息内容：{"+jsonMsg+"}");
		boolean result = false;
		//请求地址
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN";
		requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
		//发送模板消息
		JSONObject jsonObject = httpRequest(requestUrl, "POST", jsonMsg);
		if(null != jsonObject){
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if(0 == errorCode){
				result = true;
				System.out.println("模板消息发送成功errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
				System.out.println("模板消息发送成功errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
			}else{
				System.out.println("模板消息发送失败errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
				System.out.println("模板消息发送失败errorCode:{"+errorCode+"},errmsg:{"+errorMsg+"}");
			}
		}
		return result;
	}

	public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {

		JSONObject jsonObject = null;

		StringBuffer buffer = new StringBuffer();

		try {

// 创建SSLContext对象，并使用我们指定的信任管理器初始化

			TrustManager[] tm = { new MyX509TrustManager() };

			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");

			sslContext.init(null, tm, new java.security.SecureRandom());

// 从上述SSLContext对象中得到SSLSocketFactory对象

			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);

			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();

			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);

			httpUrlConn.setDoInput(true);

			httpUrlConn.setUseCaches(false);

// 设置请求方式（GET/POST）

			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))

				httpUrlConn.connect();

// 当有数据需要提交时

			if (null != outputStr) {

				OutputStream outputStream = httpUrlConn.getOutputStream();

// 注意编码格式，防止中文乱码

				outputStream.write(outputStr.getBytes("UTF-8"));

				outputStream.close();

			}

// 将返回的输入流转换成字符串

			InputStream inputStream = httpUrlConn.getInputStream();

			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");

			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;

			while ((str = bufferedReader.readLine()) != null) {

				buffer.append(str);

			}

			bufferedReader.close();

			inputStreamReader.close();

// 释放资源

			inputStream.close();

			inputStream = null;

			httpUrlConn.disconnect();

			jsonObject = JSONObject.fromObject(buffer.toString());

		} catch (ConnectException ce) {

			ce.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return jsonObject;

	}


}