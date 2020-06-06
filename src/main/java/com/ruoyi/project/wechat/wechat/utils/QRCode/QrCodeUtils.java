package com.ruoyi.project.wechat.wechat.utils.QRCode;

import com.ruoyi.project.wechat.wechat.Config.OAwechatConfig;
import net.sf.json.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @version: V1.0
 * @author: fendo
 * @className: QrCodeUtils
 * @packageName: com.xxx
 * @description: 二维码工具类
 * @data: 2019/03/06
 **/
public class QrCodeUtils {



	/**
	 * 生成二维码（QRCode）
	 * @param userid
	 * @return
	 */
	public static String CreateCode(HttpServletRequest request, String userid, String accessToken){
		String path = null;
		try {
			// 获取推广二维码的URL
			URL url = new URL(OAwechatConfig.WECHAT_GET_EXTENTSION_QRCODE_URL + "?access_token=" + accessToken);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			// POST请求必须设置如下两行数据
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 发送请求参数
			JSONObject paramJson = new JSONObject();
			/*
			 * create by: lovelyz
			 * description:小程序跳转路径
			 * create time: 16:42 2020/6/6
			 * params：[request, userid, accessToken]
			 * @return： java.lang.String
			 */
			
			paramJson.put("path", "pages/home/home?userid=" + userid);
			paramJson.put("width", 430);
			// 输出
			System.out.println("ExtensionQRCode:paramJson:=============================" + paramJson);
			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 获取URLConnection对象对应的输出流
			printWriter.write(paramJson.toString());
			printWriter.flush(); // flush输出流的缓冲
			// 输出
			System.out.println("ExtensionQRCode:printWriter:=============================" + printWriter);
			// 开始获取数据
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			// buff用于存放循环读取的临时数据
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = bis.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			System.out.println(swapStream);

			// 获取图片、裁剪原图
			ByteArrayInputStream inputStream = new ByteArrayInputStream(swapStream.toByteArray());
			BufferedImage image = ImageIO.read(inputStream);
			BufferedImage subImage = image.getSubimage(0, 0, image.getWidth(), (int) (image.getHeight() * 1)); // 裁剪原图  微信返回的是470*535 像素的图片
			BufferedImage inputbig = new BufferedImage(256, 256, BufferedImage.TYPE_INT_BGR);
			Graphics2D g = (Graphics2D) inputbig.getGraphics();
			g.drawImage(subImage, 0, 0, 256, 256, null); // 画图
			g.dispose();
			inputbig.flush();
			// 存放图片
			String pathroot = request.getServletContext().getRealPath("/upload/");
			System.out.println(pathroot);
			path = request.getServletContext().getRealPath("/upload/")+"/"+ userid + ".jpg"; // 服务器地址
			System.out.println(path);
			File file = new File(path);
			ImageIO.write(inputbig, "jpg", file);
			// 关闭
			inputStream.close();
			path = userid + ".jpg";
			System.out.println(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回数据
		return path;
	}

}
