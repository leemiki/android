package com.haolang.util.android.http;

import com.haolang.util.android.HardwareUtil;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 服务端通信工具类
 * <p>
 * v1.0支持服务端Post请求处理
 * </p>
 * <p>
 * v1.0支持请求响应各状态（成功、失败、超时、错误）的监控
 * </p>
 * <p>
 * v1.0支持请求响应中自动加载遮罩层
 * </p>
 * 
 * @author yj
 * 
 * @version 1.0 2013.07.18 22:48
 * @version 1.1 2013.08.03 14:43 去除DefaultHandler通信处理基类
 * @version 1.2 2013.08.21 11:29 去除请求单机版
 * @version 1.3 2013.09.18 07.11 添加请求超时WIFI网络判断
 */
public abstract class AndroidHttpRequest {

	private static final int FILL_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
	private static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
	private Context context;

	/**
	 * 向服务端发送请求
	 * 
	 * @param context
	 *            上下文对象
	 * @param url
	 *            请求路径
	 * @param config
	 *            http传输配置（采用默认配置传null）
	 * @param params
	 *            请求参数（参照HLPara用法，不需要传null）
	 * @param group
	 *            显示遮罩层的父容器（不需要传null）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public final void basicRequest(Context context, String url,
			HLHttpConfig config, HLPara params, ViewGroup group) {
		if (HardwareUtil.isWifiConnect(context)) {
			if (context == null) {
				throw new IllegalArgumentException("发送请求：上下文对象输入有误！");
			} else {
				if (url == null || url.equals("")) {
					throw new IllegalArgumentException("发送请求：请求地址输入有误！");
				} else {
					this.context = context;

					addFade(group);
					HLHttpConfig c = config == null ? new HLHttpConfig()
							: config;

					HLHttpRequest.createPostRequest(url, c, params,
							new RequestHandler(group)).sendRequest();
				}
			}
		} else {
			Toast.makeText(context, "WIFI未连接，请检查网络!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * 请求回调类
	 * 
	 * @author yj
	 * 
	 * @version 1.0 2013.07.18 22:48
	 */
	final class RequestHandler implements IHttpHandler {
		private ViewGroup group;

		public RequestHandler(ViewGroup group) {
			this.group = group;
		}

		@Override
		public void onSuccess(HLResult result) {
			removeFade(group);
			sucessHandler(result);
		}

		@Override
		public void onFailed(String errMsg) {
			removeFade(group);
			failedHandler();

			Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onTimeout(String errMsg) {
			removeFade(group);
			timeoutHandler();

			Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(String errMsg) {
			removeFade(group);
			errorHandler();

			Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 添加遮罩层
	 * 
	 * @param group
	 *            外部父容器
	 */
	private void addFade(ViewGroup group) {
		if (group != null) {
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					FILL_PARENT, FILL_PARENT);
			group.addView(createFade(), params);
		}
	}

	/**
	 * 移除遮罩层
	 * 
	 * @param group
	 *            外部父容器
	 */
	private void removeFade(ViewGroup group) {
		if (group != null) {
			int index = group.getChildCount() - 1;
			group.removeViewAt(index);
		}
	}

	/**
	 * 创建遮罩层
	 * 
	 * @return 遮罩层布局
	 */
	private RelativeLayout createFade() {
		RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		rParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

		RelativeLayout rLayout = new RelativeLayout(context);
		rLayout.setBackgroundColor(Color.TRANSPARENT);

		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				WRAP_CONTENT, WRAP_CONTENT);
		lParams.rightMargin = 10;

		LinearLayout lLayout = new LinearLayout(context);
		lLayout.setOrientation(LinearLayout.HORIZONTAL);
		lLayout.setGravity(Gravity.CENTER_VERTICAL);

		ProgressBar progress = new ProgressBar(context);
		lLayout.addView(progress, lParams);

		TextView mark_tv = new TextView(context);
		mark_tv.setText("数据加载中...");
		mark_tv.setTextColor(Color.BLACK);
		mark_tv.setTextSize(20f);
		lLayout.addView(mark_tv);

		rLayout.addView(lLayout, rParams);

		rLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});

		return rLayout;
	}

	/**
	 * 请求成功，获取数据成功回调函数
	 * 
	 * @param result
	 *            服务器端返回的结果集对象（需要根据实际情况进行类型强转）
	 */
	protected abstract void sucessHandler(HLResult result);

	/**
	 * 请求成功，获取数据失败回调函数
	 */
	protected abstract void failedHandler();

	/**
	 * 请求超时回调函数
	 */
	protected void timeoutHandler() {

	}

	/**
	 * 本地未知异常回调函数
	 */
	protected void errorHandler() {

	}

}
