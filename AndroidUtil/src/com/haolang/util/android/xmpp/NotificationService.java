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

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Service that continues to run in background and respond to the push
 * notification events from the server. This should be registered as service in
 * AndroidManifest.xml.
 */
public class NotificationService extends Service {

	private static final String LOGTAG = LogUtil
			.makeLogTag(NotificationService.class);

	private TelephonyManager telephonyManager;

	private BroadcastReceiver connectivityReceiver;

	private PhoneStateListener phoneStateListener;

	private ExecutorService executorService;

	private TaskSubmitter taskSubmitter;

	private TaskTracker taskTracker;

	private XmppManager xmppManager;

	private SharedPreferences sharedPrefs;

	private String deviceId;

	public NotificationService() {
		connectivityReceiver = new ConnectivityReceiver(this);
		phoneStateListener = new PhoneStateChangeListener(this);
		executorService = Executors.newSingleThreadExecutor();
		taskSubmitter = new TaskSubmitter(this);
		taskTracker = new TaskTracker(this);
	}

	@Override
	public void onCreate() {
		Log.d(LOGTAG, "onCreate()...");
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		sharedPrefs = getSharedPreferences(
				XmppConstants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

		// Get deviceId
		deviceId = telephonyManager.getDeviceId();
		Editor editor = sharedPrefs.edit();

		// If running on an emulator
		if (deviceId == null || deviceId.trim().length() == 0
				|| deviceId.matches("0+")) {
			if (sharedPrefs.contains("EMULATOR_DEVICE_ID")) {
				deviceId = sharedPrefs.getString(
						XmppConstants.EMULATOR_DEVICE_ID, "");
			} else {
				deviceId = (new StringBuilder("EMU")).append(
						(new Random(System.currentTimeMillis())).nextLong())
						.toString();
				editor.putString(XmppConstants.EMULATOR_DEVICE_ID, deviceId);
				editor.commit();
			}
		}
		Log.d(LOGTAG, "deviceId=" + deviceId);

		xmppManager = new XmppManager(this);

		taskSubmitter.submit(new Runnable() {
			public void run() {
				start();
			}
		});
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(LOGTAG, "onStart()...");
	}

	@Override
	public void onDestroy() {
		Log.d(LOGTAG, "onDestroy()...");
		stop();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(LOGTAG, "onBind()...");
		return null;
	}

	@Override
	public void onRebind(Intent intent) {
		Log.d(LOGTAG, "onRebind()...");
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(LOGTAG, "onUnbind()...");
		return true;
	}

	ExecutorService getExecutorService() {
		return executorService;
	}

	TaskSubmitter getTaskSubmitter() {
		return taskSubmitter;
	}

	TaskTracker getTaskTracker() {
		return taskTracker;
	}

	XmppManager getXmppManager() {
		return xmppManager;
	}

	SharedPreferences getSharedPreferences() {
		return sharedPrefs;
	}

	String getDeviceId() {
		return deviceId;
	}

	void connect() {
		Log.d(LOGTAG, "connect()...");
		taskSubmitter.submit(new Runnable() {
			public void run() {
				xmppManager.connect();
			}
		});
	}

	void disconnect() {
		Log.d(LOGTAG, "disconnect()...");
		taskSubmitter.submit(new Runnable() {
			public void run() {
				xmppManager.disconnect();
			}
		});
	}

	private void start() {
		Log.d(LOGTAG, "start()...");
		registerConnectivityReceiver();
	}

	private void registerConnectivityReceiver() {
		telephonyManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(connectivityReceiver, filter);
	}

	private void stop() {
		Log.d(LOGTAG, "stop()...");
		unregisterConnectivityReceiver();
		xmppManager.disconnect();
		executorService.shutdown();
	}

	private void unregisterConnectivityReceiver() {
		telephonyManager.listen(phoneStateListener,
				PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(connectivityReceiver);
	}

	/**
	 * Class for summiting a new runnable task.
	 */
	class TaskSubmitter {

		final NotificationService notificationService;

		TaskSubmitter(NotificationService notificationService) {
			this.notificationService = notificationService;
		}

		Future<?> submit(Runnable task) {
			Future<?> result = null;
			if (!notificationService.executorService.isTerminated()
					&& !notificationService.executorService.isShutdown()
					&& task != null) {
				result = notificationService.executorService.submit(task);
			}
			return result;
		}

	}

	/**
	 * Class for monitoring the running task count.
	 */
	class TaskTracker {

		final NotificationService notificationService;

		int count;

		TaskTracker(NotificationService notificationService) {
			this.notificationService = notificationService;
			this.count = 0;
		}

		void increase() {
			synchronized (notificationService.taskTracker) {
				notificationService.taskTracker.count++;
				Log.d(LOGTAG, "Incremented task count to " + count);
			}
		}

		void decrease() {
			synchronized (notificationService.taskTracker) {
				notificationService.taskTracker.count--;
				Log.d(LOGTAG, "Decremented task count to " + count);
			}
		}

	}

}
