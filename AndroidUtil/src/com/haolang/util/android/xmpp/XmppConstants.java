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

/**
 * XMPP协议消息推送常量类
 * 
 * @author yj
 * 
 * @version 1.0 2013.09.12 23:55
 */
public class XmppConstants {

	/**
	 * 本地SharedPreference文件名
	 */
	public static final String SHARED_PREFERENCE_NAME = "client_preferences";

	/**
	 * 外部传入Activity包名 KEY
	 */
	public static final String ACTIVITY_PACKAGE_NAME = "ACTIVITY_PACKAGE_NAME";

	/**
	 * 外部传入Activity类名 KEY
	 */
	public static final String ACTIVITY_CLASS_NAME = "ACTIVITY_CLASS_NAME";

	/**
	 * 版本 KEY
	 */
	public static final String API_KEY = "API_KEY";

	/**
	 * IP地址 KEY
	 */
	public static final String XMPP_HOST = "XMPP_HOST";

	/**
	 * IP端口 KEY
	 */
	public static final String XMPP_PORT = "XMPP_PORT";

	/**
	 * 用户名 KEY
	 */
	public static final String XMPP_USERNAME = "XMPP_USERNAME";

	/**
	 * 密码 KEY
	 */
	public static final String XMPP_PASSWORD = "XMPP_PASSWORD";

	/**
	 * 运行设备ID KEY
	 */
	public static final String EMULATOR_DEVICE_ID = "EMULATOR_DEVICE_ID";

	/**
	 * 消息通知ID KEY
	 */
	public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

	/**
	 * 消息通知版本 KEY
	 */
	public static final String NOTIFICATION_API_KEY = "NOTIFICATION_API_KEY";

	/**
	 * 消息通知标题 KEY
	 */
	public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";

	/**
	 * 消息通知内容 KEY
	 */
	public static final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";

	/**
	 * 消息通知附件URI KEY
	 */
	public static final String NOTIFICATION_URI = "NOTIFICATION_URI";

	/**
	 * 消息通知广播接收器
	 */
	public static final String ACTION_SHOW_NOTIFICATION = "org.androidpn.client.SHOW_NOTIFICATION";

}
