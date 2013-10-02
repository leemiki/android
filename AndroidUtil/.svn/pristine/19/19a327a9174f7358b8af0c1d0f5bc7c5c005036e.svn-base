/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haolang.util.android.xmpp;

import java.util.Properties;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * 基于XMPP协议的消息推送
 * <p>
 * v1.0支持接收服务端即时消息推送
 * </p>
 * 
 * @author yj
 * 
 * @version 1.0 2013.09.12 23:37
 */
public abstract class AndroidXmppPush {

	private static final String LOGTAG = LogUtil
			.makeLogTag(AndroidXmppPush.class);

	private Context context;

	private Intent intent;

	private SharedPreferences sharedPrefs;

	private String apiKey;

	private BroadcastReceiver notificationReceiver;

	public AndroidXmppPush(Context context) {
		this.context = context;

		if (context instanceof Activity) {
			String serviceName = getNotifyService();

			if (serviceName == null || serviceName.equals("")) {
				throw new IllegalArgumentException("消息推送服务：服务名输入有误！");
			} else {
				notificationReceiver = getNotifyReceiver();
				if (notificationReceiver == null) {
					throw new IllegalArgumentException("消息推送服务：消息广播接收器输入有误！");
				} else {
					intent = new Intent(getNotifyService());
					getProperties((Activity) context);
				}
			}
		}
	}

	private void getProperties(Activity activity) {
		String packageName = activity.getPackageName();
		String className = activity.getClass().getName();

		Properties props = loadProperties();
		apiKey = props.getProperty("apiKey", "");
		String xmppHost = props.getProperty("xmppHost", "127.0.0.1");
		String xmppPort = props.getProperty("xmppPort", "5222");
		Log.i(LOGTAG, "apiKey=" + apiKey);
		Log.i(LOGTAG, "xmppHost=" + xmppHost);
		Log.i(LOGTAG, "xmppPort=" + xmppPort);

		sharedPrefs = context.getSharedPreferences(
				XmppConstants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putString(XmppConstants.API_KEY, apiKey);
		editor.putString(XmppConstants.XMPP_HOST, xmppHost);
		editor.putInt(XmppConstants.XMPP_PORT, Integer.parseInt(xmppPort));
		editor.putString(XmppConstants.ACTIVITY_PACKAGE_NAME, packageName);
		editor.putString(XmppConstants.ACTIVITY_CLASS_NAME, className);
		editor.commit();
	}

	private Properties loadProperties() {
		Properties props = new Properties();
		String propName = getPropFileName();

		if (propName == null || propName.equals("")) {
			throw new IllegalArgumentException("读取属性文件：文件名输入有误！");
		} else {
			try {
				int id = context.getResources().getIdentifier(propName, "raw",
						context.getPackageName());
				props.load(context.getResources().openRawResource(id));
			} catch (Exception e) {
				Log.e(LOGTAG, "Could not find the properties file.", e);
				e.printStackTrace();
			}
		}

		return props;
	}

	/**
	 * 启动消息推送服务
	 */
	public void startService() {
		Thread serviceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				IntentFilter filter = new IntentFilter();
				filter.addAction(XmppConstants.ACTION_SHOW_NOTIFICATION);
				context.registerReceiver(notificationReceiver, filter);
				context.startService(intent);
			}
		});
		serviceThread.start();
	}

	/**
	 * 停止消息推送服务
	 */
	public void stopService() {
		context.unregisterReceiver(notificationReceiver);
		context.stopService(intent);
	}

	/**
	 * 获取版本key
	 * 
	 * @return 版本key
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * 获取自定义消息推送服务名
	 * 
	 * @return 服务名
	 */
	public abstract String getNotifyService();

	/**
	 * 获取自定义属性文件名
	 * 
	 * @return 文件名
	 */
	public abstract String getPropFileName();

	/**
	 * 获取消息广播接收器
	 * 
	 * @return 广播接收器对象
	 */
	public abstract BroadcastReceiver getNotifyReceiver();

}
