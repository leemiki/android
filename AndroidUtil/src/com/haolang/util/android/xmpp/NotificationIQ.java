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

import org.jivesoftware.smack.packet.IQ;

/**
 * This class represents a notifcatin IQ packet.
 */
class NotificationIQ extends IQ {

	private String id;

	private String apiKey;

	private String title;

	private String message;

	private String uri;

	@Override
	public String getChildElementXML() {
		StringBuilder buf = new StringBuilder();
		buf.append("<").append("notification").append(" xmlns=\"")
				.append("androidpn:iq:notification").append("\">");
		if (id != null) {
			buf.append("<id>").append(id).append("</id>");
		}
		buf.append("</").append("notification").append("> ");
		return buf.toString();
	}

	String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	String getApiKey() {
		return apiKey;
	}

	void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	String getTitle() {
		return title;
	}

	void setTitle(String title) {
		this.title = title;
	}

	String getMessage() {
		return message;
	}

	void setMessage(String message) {
		this.message = message;
	}

	String getUri() {
		return uri;
	}

	void setUri(String url) {
		this.uri = url;
	}

}
