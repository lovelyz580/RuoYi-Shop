package com.ruoyi.project.wechat.wechat.pay;

import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 微信支付工具类
 * 
 * 二维码工具
 * 
 * 利用Google的ZXing工具包，生成和解析二维码图片
 */

public class QRCodeUtils {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}

	public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}

	/**
	 * 测试main方法
	 */
//	public static void main(String[] args) throws Exception {
//		int width = 300; // 二维码图片的宽
//		int height = 300; // 二维码图片的高
//		String format = "gif"; // 二维码图片的格式
//		
//		String text = "http://www.baidu.com"; // 二维码数据
//		
//		Hashtable hints = new Hashtable();
//		hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用编码
//		BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
//		File outputFile = new File("d:" + File.separator + "new.gif");
//		QRCodeUtil.writeToFile(bitMatrix, format, outputFile); // 生成二维码
//	}
	
}
