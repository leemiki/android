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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

/**
 * This class is to manage the XMPP connection between client and server.
 */
class XmppManager {

	private static final String LOGTAG = LogUtil.makeLogTag(XmppManager.class);

	private static final String XMPP_RESOURCE_NAME = "AndroidpnClient";

	private Context context;

	private NotificationService.TaskSubmitter taskSubmitter;

	private NotificationService.TaskTracker taskTracker;

	private SharedPreferences sharedPrefs;

	private String xmppHost;

	private int xmppPort;

	private XMPPConnection connection;

	private String username;

	private String password;

	private ConnectionListener connectionListener;

	private PacketListener notificationPacketListener;

	private Handler handler;

	private List<Runnable> taskList;

	private boolean running = false;

	private Future<?> futureTask;

	private Thread reconnection;

	public XmppManager(NotificationService notificationService) {
		context = notificationService;
		taskSubmitter = notificationService.getTaskSubmitter();
		taskTracker = notificationService.getTaskTracker();
		sharedPrefs = notificationService.getSharedPreferences();

		xmppHost = sharedPrefs.getString(XmppConstants.XMPP_HOST, "localhost");
		xmppPort = sharedPrefs.getInt(XmppConstants.XMPP_PORT, 5222);
		username = sharedPrefs.getString(XmppConstants.XMPP_USERNAME, "");
		password = sharedPrefs.getString(XmppConstants.XMPP_PASSWORD, "");

		connectionListener = new PersistentConnectionListener(this);
		notificationPacketListener = new NotificationPacketListener(this);

		handler = new Handler();
		taskList = new ArrayList<Runnable>();
		reconnection = new ReconnectionThread(this);
	}

	Context getContext() {
		return context;
	}

	void connect() {
		Log.d(LOGTAG, "connect()...");
		resetTask();
		submitLoginTask();
	}

	private void resetTask() {
		taskList.clear();
		running = false;
	}

	void disconnect() {
		Log.d(LOGTAG, "disconnect()...");
		terminatePersistentConnection();
	}

	void terminatePersistentConnection() {
		Log.d(LOGTAG, "terminatePersistentConnection()...");
		Runnable runnable = new Runnable() {

			public void run() {
				if (isConnected()) {
					Log.d(LOGTAG, "terminatePersistentConnection()... run()");
					connection.removePacketListener(notificationPacketListener);
					connection.disconnect();
				}
				runTask();
			}

		};
		addTask(runnable);
	}

	XMPPConnection getConnection() {
		return connection;
	}

	void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}

	String getUsername() {
		return username;
	}

	void setUsername(String username) {
		this.username = username;
	}

	String getPassword() {
		return password;
	}

	void setPassword(String password) {
		this.password = password;
	}

	ConnectionListener getConnectionListener() {
		return connectionListener;
	}

	PacketListener getNotificationPacketListener() {
		return notificationPacketListener;
	}

	void startReconnectionThread() {
		synchronized (reconnection) {
			if (!reconnection.isAlive()) {
				reconnection.setName("Xmpp Reconnection Thread");
				reconnection.start();
			}
		}
	}

	Handler getHandler() {
		return handler;
	}

	void reregisterAccount() {
		removeAccount();
		submitLoginTask();
		runTask();
	}

	private void removeAccount() {
		Editor editor = sharedPrefs.edit();
		editor.remove(XmppConstants.XMPP_USERNAME);
		editor.remove(XmppConstants.XMPP_PASSWORD);
		editor.commit();
	}

	List<Runnable> getTaskList() {
		return taskList;
	}

	Future<?> getFutureTask() {
		return futureTask;
	}

	void runTask() {
		Log.d(LOGTAG, "runTask()...");
		synchronized (taskList) {
			running = false;
			futureTask = null;
			if (!taskList.isEmpty()) {
				Runnable runnable = (Runnable) taskList.get(0);
				taskList.remove(0);
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			}
		}
		taskTracker.decrease();
		Log.d(LOGTAG, "runTask()...done");
	}

	private String newRandomUUID() {
		String uuidRaw = UUID.randomUUID().toString();
		return uuidRaw.replaceAll("-", "");
	}

	private boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	private boolean isAuthenticated() {
		return connection != null && connection.isConnected()
				&& connection.isAuthenticated();
	}

	private boolean isRegistered() {
		return sharedPrefs.contains(XmppConstants.XMPP_USERNAME)
				&& sharedPrefs.contains(XmppConstants.XMPP_PASSWORD);
	}

	private void submitConnectTask() {
		Log.d(LOGTAG, "submitConnectTask()...");
		addTask(new ConnectTask());
	}

	private void submitRegisterTask() {
		Log.d(LOGTAG, "submitRegisterTask()...");
		submitConnectTask();
		addTask(new RegisterTask());
	}

	private void submitLoginTask() {
		Log.d(LOGTAG, "submitLoginTask()...");
		submitRegisterTask();
		addTask(new LoginTask());
	}

	private void addTask(Runnable runnable) {
		Log.d(LOGTAG, "addTask(runnable)...");
		taskTracker.increase();
		synchronized (taskList) {
			if (taskList.isEmpty() && !running) {
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			} else {
				taskList.add(runnable);
			}
		}
		Log.d(LOGTAG, "addTask(runnable)... done");
	}

	/**
	 * A runnable task to connect the server.
	 */
	private class ConnectTask implements Runnable {

		public void run() {
			Log.i(LOGTAG, "ConnectTask.run()...");

			if (!isConnected()) {
				// Create the configuration for this new connection
				ConnectionConfiguration connConfig = new ConnectionConfiguration(
						xmppHost, xmppPort);
				// connConfig.setSecurityMode(SecurityMode.disabled);
				connConfig.setSecurityMode(SecurityMode.required);
				connConfig.setSASLAuthenticationEnabled(false);
				connConfig.setCompressionEnabled(false);

				XMPPConnection connection = new XMPPConnection(connConfig);
				setConnection(connection);

				try {
					// Connect to the server
					connection.connect();
					Log.i(LOGTAG, "XMPP connected successfully");

					// packet provider
					ProviderManager.getInstance().addIQProvider("notification",
							"androidpn:iq:notification",
							new NotificationIQProvider());

					runTask();
				} catch (XMPPException e) {
					Log.e(LOGTAG, "XMPP connection failed", e);
					startReconnectionThread();
				}
			} else {
				Log.i(LOGTAG, "XMPP connected already");
				runTask();
			}
		}
	}

	/**
	 * A runnable task to register a new user onto the server.
	 */
	private class RegisterTask implements Runnable {

		public void run() {
			Log.i(LOGTAG, "RegisterTask.run()...");

			if (!isRegistered()) {
				final String newUsername = newRandomUUID();
				final String newPassword = newRandomUUID();

				Registration registration = new Registration();
				registration.setType(IQ.Type.SET);
				registration.addAttribute("username", newUsername);
				registration.addAttribute("password", newPassword);

				PacketFilter packetFilter = new AndFilter(new PacketIDFilter(
						registration.getPacketID()), new PacketTypeFilter(
						IQ.class));

				PacketListener packetListener = new PacketListener() {

					public void processPacket(Packet packet) {
						if (packet instanceof IQ) {
							IQ response = (IQ) packet;
							if (response.getType() == IQ.Type.ERROR) {
								if (!response.getError().toString()
										.contains("409")) {
									Log.e(LOGTAG,
											"Unknown error while registering XMPP account! "
													+ response.getError()
															.getCondition());
								}
							} else if (response.getType() == IQ.Type.RESULT) {
								setUsername(newUsername);
								setPassword(newPassword);

								Editor editor = sharedPrefs.edit();
								editor.putString(XmppConstants.XMPP_USERNAME,
										newUsername);
								editor.putString(XmppConstants.XMPP_PASSWORD,
										newPassword);
								editor.commit();
								Log.i(LOGTAG, "Account registered successfully");
								runTask();
							}
						}
					}
				};

				connection.addPacketListener(packetListener, packetFilter);
				connection.sendPacket(registration);
			} else {
				Log.i(LOGTAG, "Account registered already");
				runTask();
			}
		}
	}

	/**
	 * A runnable task to log into the server.
	 */
	private class LoginTask implements Runnable {

		public void run() {
			Log.i(LOGTAG, "LoginTask.run()...");

			if (!isAuthenticated()) {
				Log.d(LOGTAG, "username=" + username);
				Log.d(LOGTAG, "password=" + password);

				try {
					connection.login(username, password, XMPP_RESOURCE_NAME);
					Log.d(LOGTAG, "Loggedn in successfully");

					connection.addConnectionListener(connectionListener);

					PacketFilter packetFilter = new PacketTypeFilter(
							NotificationIQ.class);

					connection.addPacketListener(notificationPacketListener,
							packetFilter);

					runTask();
				} catch (XMPPException e) {
					Log.e(LOGTAG, "LoginTask.run()... xmpp error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: "
							+ e.getMessage());
					
					String INVALID_CREDENTIALS_ERROR_CODE = "401";
					String errorMessage = e.getMessage();
					if (errorMessage != null
							&& errorMessage
									.contains(INVALID_CREDENTIALS_ERROR_CODE)) {
						reregisterAccount();
						return;
					}
					
					startReconnectionThread();
				}
			} else {
				Log.i(LOGTAG, "Logged in already");
				runTask();
			}
		}
	}

}
