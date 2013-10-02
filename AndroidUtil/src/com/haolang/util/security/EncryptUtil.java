package com.haolang.util.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 数据加密工具类
 * <p>
 * v1.0支持对字符串进行MD5加密
 * </p>
 * <p>
 * v1.0支持对文件进行MD5加密
 * </p>
 * 
 * @author yj
 * 
 * @version 1.0 2013.07.11 21:08
 */
public class EncryptUtil {

	private static char hexdigits[] = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param source
	 *            源字符串
	 * 
	 * @return 加密后的字符串（如果字符串加密成功返回 否则返回空字符串）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static String MD5CrypticString(String source) {
		String str = "";

		if (source != null && !source.equals("")) {
			MessageDigest md5 = null;

			try {
				md5 = MessageDigest.getInstance("MD5");

				char[] charArray = source.toCharArray();
				int len = charArray.length;
				byte[] byteArray = new byte[len];

				for (int i = 0; i < len; i++) {
					byteArray[i] = (byte) charArray[i];
				}

				str = byteToHexString(md5.digest(byteArray));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("MD5加密字符串：源字符串输入有误！");
		}

		return str;
	}

	/**
	 * 对文件进行MD5加密
	 * 
	 * @param file
	 *            源文件
	 * 
	 * @return 加密后的字符串（如果文件加密成功返回 否则返回空字符串）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static String MD5CrypticFile(File file) {
		String str = "";

		if (file != null) {
			FileInputStream fis = null;
			MessageDigest md5 = null;

			try {
				fis = new FileInputStream(file);
				md5 = MessageDigest.getInstance("MD5");

				int length = -1;
				byte[] buffer = new byte[1024];

				while ((length = fis.read(buffer)) != -1) {
					md5.update(buffer, 0, length);
				}

				str = byteToHexString(md5.digest());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new IllegalArgumentException("MD5加密文件：源文件输入有误！");
		}

		return str;
	}

	/**
	 * 字节数组转换成十六进制字符串
	 * 
	 * @param tmp
	 *            字节数组
	 * 
	 * @return 十六进制字符串
	 */
	private static String byteToHexString(byte[] tmp) {
		String s = "";
		char str[] = new char[16 * 2];

		int k = 0;
		for (int i = 0; i < 16; i++) {
			byte byte0 = tmp[i];
			str[k++] = hexdigits[byte0 >>> 4 & 0xf];
			str[k++] = hexdigits[byte0 & 0xf];
		}

		s = new String(str);

		return s;
	}

	public static void main(String[] args) {
		System.out.println(EncryptUtil.MD5CrypticString("O(∩_∩)O哈哈~"));

		File file = new File(
				"C://Documents and Settings//Administrator//桌面//2001.jpg");
		System.out.println(EncryptUtil.MD5CrypticFile(file));
	}

}
