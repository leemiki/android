package com.haolang.util.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 硬件信息类
 * <p>
 * 支持各网络连接状态的获取（WIFI、MOBILE、BLUETOOTH）
 * </p>
 * 
 * @author yj
 * 
 * @version 1.0 2013.09.06 16:27
 */
public class HardwareUtil {

	/**
	 * 判断网络连接状态
	 * 
	 * @param context
	 *            上下文
	 * 
	 * @return 连接状态（如果已连接返回true 否则返回false）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static boolean isNetConnect(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("网络连接判断：上下文对象输入有误 ！");
		}

		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();

		return info != null && info.isConnected();
	}

	/**
	 * 判断WIFI连接状态
	 * 
	 * @param context
	 *            上下文
	 * 
	 * @return 连接状态（如果已连接返回true 否则返回false）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static boolean isWifiConnect(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("WIFI网络连接判断：上下文对象输入有误！");
		}

		return isTypeConnect(context, ConnectivityManager.TYPE_WIFI);
	}

	/**
	 * 判断移动网络连接状态
	 * 
	 * @param context
	 *            上下文
	 * 
	 * @return 连接状态（如果已连接返回true 否则返回false）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static boolean isMobilConnect(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("移动网络连接判断：上下文对象输入有误！");
		}

		return isTypeConnect(context, ConnectivityManager.TYPE_MOBILE);
	}

	/**
	 * 判断蓝牙网络连接状态
	 * 
	 * @param context
	 *            上下文
	 * 
	 * @return 连接状态（如果已连接返回true 否则返回false）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static boolean isBlueConnect(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("蓝牙网络连接判断：上下文对象输入有误！");
		}

		return isTypeConnect(context, ConnectivityManager.TYPE_BLUETOOTH);
	}

	/**
	 * 判断各类型网络连接状态
	 * 
	 * @param context
	 *            上下文呢
	 * @param type
	 *            网络类型
	 * 
	 * @return 连接状态（如果已连接返回true 否则返回false）
	 */
	private static boolean isTypeConnect(Context context, int type) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = manager.getNetworkInfo(type);

		return info != null && info.isConnected();
	}

}
