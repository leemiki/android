package com.haolang.util.android.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.haolang.util.android.AndroidLog;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.DefaultMapper;

/**
 * 服务端通信基类
 * 
 * @author hph
 * 
 * @version 1.0
 * 
 * @author yj
 * 
 * @version 1.1 2013.07.19 16:34 重构
 * @version 1.2 2013.08.21 11:22 去除请求单机版
 * @version 1.3 2013.09.03 13:49 添加请求头
 * @version 1.4 2013.09.05 10:22 修改类、方法权限为包可见
 */
class HLHttpRequest {

	private static String TAG = "HLHttpRequest";
	private static XStream stream;
	static {
		stream = new XStream();
		stream.registerConverter(new DataConverter());
		stream.alias("linked-hash-map", LinkedHashMap.class);
		stream.registerConverter(new LinkedHashMapCoverter(new DefaultMapper(
				HLHttpRequest.class.getClassLoader())));
	}

	/**
	 * HTTP请求类型
	 * 
	 * @author hph
	 * 
	 */
	static enum Method {
		Get, Post
	}

	private static HLHttpConfig globalHttpConfig = new HLHttpConfig();
	private HttpUriRequest httpUriRequest;
	private IHttpHandler handler;
	private HLHttpConfig httpConfig = null;

	static HLHttpConfig getGlobalHttpConfig() {
		return globalHttpConfig;
	}

	HLHttpConfig getHttpConfig() {
		return (httpConfig == null) ? getGlobalHttpConfig() : httpConfig;
	}

	void setHttpConfig(HLHttpConfig httpConfig) {
		this.httpConfig = httpConfig;
	}

	/**
	 * 私有化构造函数
	 */
	private HLHttpRequest(HttpUriRequest httpUriRequest, IHttpHandler handler) {
		if (httpUriRequest == null || handler == null) {
			throw new NullPointerException(
					"HLHttpRequest`s constructor can`t receive null paramters");
		}
		this.httpUriRequest = httpUriRequest;
		this.handler = handler;
	}

	/**
	 * 本函数提供向服务器发送Http请求并且做响应
	 * 
	 * @param url
	 *            传输路径
	 * @param httpConfig
	 *            配置参数
	 * @param param
	 *            传递到服务器的数据
	 * @param handler
	 *            服务器返回数据的响应类
	 * 
	 * @return 返回一个HLHttpRequest实例，此实例已经开始向服务器发送数据
	 * 
	 * @throws NullPointerException
	 *             请求路径为null时，抛出此异常
	 */
	static HLHttpRequest createPostRequest(String url, HLHttpConfig httpConfig,
			HLPara param, IHttpHandler handler) {
		if (url == null) {
			throw new NullPointerException(
					"HLHttpRequest.request cant receive null url");
		}

		URI uri = null;
		try {
			uri = new URI(url);
			uri.toURL();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HLPara _param;

		_param = param != null ? param : HLPara.newInstance();

		HttpUriRequest uriRequest = getPostUriRequest(uri, _param);
		uriRequest.addHeader("hl_data_format", "format_xml");

		AndroidLog.i(TAG, "url = " + url);
		AndroidLog.i(TAG, "params: " + _param.toString());

		HLHttpRequest httpRequest = new HLHttpRequest(uriRequest, handler);
		httpRequest.setHttpConfig(httpConfig);

		return httpRequest;
	}

	/**
	 * 生成Post方式的Http请求的参数
	 * 
	 * @param uri
	 *            地址
	 * @param para
	 *            传递给服务器的参数
	 * 
	 * @return 请求对象
	 * 
	 * @throws NullPointException
	 *             uri为null时，抛出此异常
	 */
	private static HttpUriRequest getPostUriRequest(URI uri, HLPara para) {
		if (uri == null) {
			throw new NullPointerException(
					"HLHttpParam.toPostParam get null parameter");
		}
		MultipartEntity mulentity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		try {
			String xml = para.toXml();
			if (getGlobalHttpConfig().isDataCompressed()) {
				xml = para.compressXml(xml);
			}
			mulentity.addPart(
					"paraString",
					new StringBody(xml, Charset.forName(getGlobalHttpConfig()
							.getCharSet())));
		} catch (UnsupportedEncodingException e) {
			AndroidLog.e(TAG, e.getMessage());
		}
		List<File> files = para.getFiles();
		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			mulentity.addPart("file", new FileBody(file));
		}

		HttpPost postResult = new HttpPost(uri);
		postResult.setHeader("Charset", getGlobalHttpConfig().getCharSet());
		postResult.setEntity(mulentity);

		return postResult;
	}

	HLHttpRequest sendRequest() {
		task = new RequestTask();
		task.execute();

		return this;
	}

	private RequestTask task;

	/**
	 * 尝试结束任务
	 */
	void cancel() {
		if (task != null) {
			task.cancel(true);
		}
	}

	private static enum ResultCode {
		NOTDONE, SUCCESS, FAILED, TIMEOUT, ERROR
	}

	private class RequestTask extends AsyncTask<HLHttpRequest, Object, String> {
		HLHttpRequest.ResultCode resultCode;
		int statusCode;
		String errMsg = "";

		@Override
		protected String doInBackground(HLHttpRequest... params) {
			BasicHttpParams httpParams = new BasicHttpParams();// 设定超时参数
			HttpConnectionParams.setConnectionTimeout(httpParams,
					getHttpConfig().getConnectionTimeout());
			HttpConnectionParams.setSoTimeout(httpParams, getHttpConfig()
					.getSoTimeout());

			HttpClient client = new DefaultHttpClient(httpParams);
			String result = null;

			// 执行服务器请求
			HttpResponse response;
			resultCode = HLHttpRequest.ResultCode.NOTDONE;
			statusCode = 0;

			try {
				response = client.execute(httpUriRequest);
				statusCode = response.getStatusLine().getStatusCode();

				if (statusCode == HttpStatus.SC_OK) {
					resultCode = HLHttpRequest.ResultCode.SUCCESS;
					result = EntityUtils.toString(response.getEntity(),
							getHttpConfig().getCharSet());
					result = result.replace(
							"com.haolang.adframe.request.HLResult",
							HLResult.class.getCanonicalName());

					AndroidLog.i(HLHttpRequest.TAG, "statusCode: " + statusCode
							+ ", result: " + result);
				} else {
					resultCode = HLHttpRequest.ResultCode.FAILED;
					errMsg = "服务器响应失败";

					AndroidLog.i(HLHttpRequest.TAG, "statusCode: " + statusCode
							+ ", result: "
							+ response.getStatusLine().getReasonPhrase());
					AndroidLog.e(HLHttpRequest.TAG, errMsg);
				}
			} catch (ClientProtocolException e) {
				resultCode = HLHttpRequest.ResultCode.TIMEOUT;
				errMsg = "服务器响应超时";

				AndroidLog.e(HLHttpRequest.TAG, errMsg);
				e.printStackTrace();
			} catch (IOException e) {
				resultCode = HLHttpRequest.ResultCode.ERROR;
				errMsg = "本地未知异常";

				AndroidLog.e(HLHttpRequest.TAG, errMsg);
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			AndroidLog.i(HLHttpRequest.TAG, "请求完毕");

			if (this.resultCode == HLHttpRequest.ResultCode.SUCCESS) {
				provideResult(result);
			} else if (this.resultCode == HLHttpRequest.ResultCode.FAILED) {
				handler.onFailed(errMsg);
			} else if (this.resultCode == HLHttpRequest.ResultCode.TIMEOUT) {
				handler.onTimeout(errMsg);
			} else if (this.resultCode == HLHttpRequest.ResultCode.ERROR) {
				handler.onError(errMsg);
			}

			super.onPostExecute(result);
		}
	}

	/**
	 * 提供请求数据
	 * 
	 * @param result
	 *            请求数据
	 */
	private void provideResult(String result) {
		HLResult rsl = null;

		if (ResponseFilter.isNeedFilter(result)) {
			rsl = new HLResult();
			rsl.setSucceed(false);
			rsl.setMsg(ResponseFilter.replaceResponse(result));
		} else {
			rsl = (HLResult) HLHttpRequest.stream.fromXML(result);
		}

		handler.onSuccess(rsl);
	}

}
