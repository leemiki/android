package com.haolang.util.android.http;

/**
 * 通信处理接口
 * 
 * @author yj
 * 
 * @version 1.0 2013.07.19 16:09
 * @version 1.1 2013.09.12 19:23 去掉接口函数多余的修饰符
 */
public interface IHttpHandler {

	/**
	 * 请求成功，获取数据成功回调函数
	 * 
	 * @param p
	 *            请求数据
	 */
	void onSuccess(HLResult p);

	/**
	 * 请求成功，获取数据失败回调函数
	 * 
	 * @param errMsg
	 *            错误信息
	 */
	void onFailed(String errMsg);

	/**
	 * 请求超时回调函数
	 * 
	 * @param errMsg
	 *            错误信息
	 */
	void onTimeout(String errMsg);

	/**
	 * 本地未知异常回调函数
	 * 
	 * @param errMsg
	 *            错误信息
	 */
	void onError(String errMsg);

}
