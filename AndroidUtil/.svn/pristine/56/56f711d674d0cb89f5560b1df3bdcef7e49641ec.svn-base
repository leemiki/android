package com.haolang.util.java;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.haolang.util.android.http.AndroidHttpRequest;
import com.haolang.util.android.http.HLPara;
import com.haolang.util.android.http.HLResult;

/**
 * 异常处理工具类
 * <p>
 * v1.0异常模板的构建
 * </p>
 * <p>
 * v1.1支持异常字典的解析/读取（提供了从本地文件读取和从res/xml中读取两种方式）
 * </p>
 * <p>
 * v1.1支持异常信息的获取
 * </p>
 * <p>
 * v1.2支持全局异常捕获/上传
 * </p>
 * <p>
 * v1.3支持发布模式和Debug模式
 * </p>
 * 
 * @author yj
 * 
 * @version 1.0 2013.07.26 16:45
 * @version 1.4 2013.09.27 17:38 修改异常文件存放位置及读取方式
 * 
 * @author wjl
 * 
 * @version 1.1 2013.7.30 14:23 增加了pull解析异常配置文件
 * @version 1.2 2013.8.8 14:42 增加了异常处理
 * @version 1.3 2013.9.2 10:39 新增读取异常字典文件方式的选择
 */
public abstract class ExceptionUtil extends Application implements
		UncaughtExceptionHandler {

	public static final int TYPE_FILE = 0;
	public static final int TYPE_ASSET = 1;

	private String netPath = "";

	/**
	 * 是否开发版本标志，true为开发版本，不做全局异常捕获，false为上线版，进行全局异常捕获、上传
	 */
	private static boolean debug = true;

	/**
	 * 系统自带的异常处理机制
	 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	/**
	 * 异常文件缓存
	 */
	private static Map<String, String> exceptionMap;

	static {
		exceptionMap = new HashMap<String, String>();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		String fileName = setFilePath();
		int type = setType();
		if (type == TYPE_ASSET) {
			parseExceptionXML(getApplicationContext(), fileName);
		} else if (type == TYPE_FILE) {
			parseExceptionXML(fileName);
		} else {
			throw new IllegalArgumentException(
					"不支持的工作模式 设置工作模式函数（setType（））返回的模式值有误，请用在ExceptionUtil中定义的值");
		}

		if (!debug) {
			init();
		}
	}

	/**
	 * 通过文件路径解析异常配置文件
	 * 
	 * @param pathName
	 *            文件路径
	 * 
	 * @return 是否解析成功（解析成功返回true 否则返回false）
	 */
	private static boolean parseExceptionXML(String pathName) {
		if (pathName == null || pathName.equals("")) {
			throw new IllegalArgumentException("通过文件路径解析异常配置文件路径名为null或为空");
		}

		FileInputStream fis = null;
		boolean isSuccess = false;
		try {
			fis = new FileInputStream(pathName);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(fis, "utf-8");
			parserToMap(parser);
			isSuccess = true;
		} catch (XmlPullParserException e) {
			isSuccess = false;
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			isSuccess = false;
			e.printStackTrace();
		} catch (IOException e) {
			isSuccess = false;
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return isSuccess;
	}

	/**
	 * 解析android的res/xml目录下的异常配置文件
	 * 
	 * @param context
	 *            android上下文环境
	 * @param fileName
	 *            异常文件名
	 * 
	 * @return 是否解析成功（解析成功返回true 否则返回false）
	 */
	private static boolean parseExceptionXML(Context context, String fileName) {

		if (context == null) {
			throw new IllegalArgumentException(
					"解析assents目录下的异常配置文件时传入的上下文环境为null");
		}
		if (fileName == null || fileName.equals("")) {
			throw new IllegalArgumentException(
					"解析assents目录下的异常配置文件时传入文件名为null或为空");
		}

		boolean isSuccess = true;
		Resources resource = context.getResources();
		try {
			int resId = resource.getIdentifier(fileName, "xml",
					context.getPackageName());
			parserToMap(resource.getXml(resId));
		} catch (IOException e) {
			isSuccess = false;
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			isSuccess = false;
			e.printStackTrace();
		}

		return isSuccess;
	}

	/**
	 * 将xml文件解析到Map中去
	 * 
	 * @param parser
	 *            pull解析器
	 * 
	 * @throws XmlPullParserException
	 *             抛出的xml解析异常
	 * 
	 * @throws IOException
	 *             抛出的IO异常
	 */
	private static void parserToMap(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		if (parser == null) {
			throw new IllegalArgumentException("解析xml到map中时xml解析器对象为空");
		}

		int type = parser.getEventType();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_DOCUMENT:
				Log.i("XmlPullParser", "开始解析XML文件");
				break;
			case XmlPullParser.END_DOCUMENT:
				Log.i("XmlPullParser", "结束解析XML文件");
				break;
			case XmlPullParser.START_TAG:

				String nodeName = parser.getName();
				if (nodeName.equals("node")) {
					String id = parser.getAttributeValue(null, "id");
					String description = parser.getAttributeValue(null, "desc");
					exceptionMap.put(id, description);
				} else if (nodeName.equals("flag")) {
					debug = Boolean.valueOf(parser.getAttributeValue(null,
							"debug"));
				}

				break;
			case XmlPullParser.END_TAG:
				break;
			}

			type = parser.next();
		}

	}

	/**
	 * 获取异常信息
	 * 
	 * @param index
	 *            异常序号
	 * 
	 * @return 异常信息
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数不合法，将报此异常
	 */
	public static String getExceptionDesc(String index) {
		if (!exceptionMap.containsKey(index)) {
			throw new IllegalArgumentException("在异常信息表中没有" + index + "对应的异常");
		}

		return exceptionMap.get(index);
	}

	/**
	 * 初始化,注册Context对象,
	 * 获取系统默认的UncaughtException处理器,同时设置设置ExceptionUtil类为程序的默认处理器
	 */
	private void init() {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		if (!handleException(ex)) { // 如果定义的异常处理没有成功则交给系统自带的处理机制进行处理
			if (mDefaultHandler != null) { // 如果不存在系统自带的处理，则不处理
				mDefaultHandler.uncaughtException(thread, ex);
			} else {
				closeApp();
			}
		} else {
			closeApp();
		}

	}

	/**
	 * 
	 * 关闭应用程序
	 * 
	 */
	private void closeApp() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			Log.e("EXCEPTIONUTIL", "Error : ", e);
		}

		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 *            异常类
	 * 
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		ex.printStackTrace();

		uploadError(ex.getLocalizedMessage());

		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(getApplicationContext(), "应用出现异常",
						Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();

		return true;
	}

	/**
	 * 上传异常信息
	 * 
	 * @param error
	 *            异常信息
	 */
	private void uploadError(String error) {
		if (error == null) {
			throw new IllegalArgumentException("上传异常信息是传入的异常信息为null");
		}

		ErrorRequest mErrorRequest = new ErrorRequest();
		HLPara params = HLPara.newInstance().addPara("error", error);
		mErrorRequest.basicRequest(this, netPath
				+ "/tomon/uploadErrorMessage.do", null, params, null);
	}

	class ErrorRequest extends AndroidHttpRequest {

		@Override
		protected void sucessHandler(HLResult result) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (result.isSucceed()) {
				Toast.makeText(getApplicationContext(), "错误信息已上传，将尽快为您解决！",
						Toast.LENGTH_SHORT).show();
			} else {
				Log.e("ErrorRequest", map.get("e").toString());
			}
		}

		@Override
		protected void failedHandler() {

		}
	}

	/**
	 * 设置异常文件读取方式，TYPE_FILE 本地异常配置文件；TYPE_ASSET res/xml异常配置文件
	 * 
	 * @return 异常文件读取方式
	 */
	protected abstract int setType();

	/**
	 * 设置异常文件路径/文件名，TYPE_FILE 返回文件路径；TYPE_ASSET 返回文件名
	 * 
	 * @return 异常文件路径
	 */
	protected abstract String setFilePath();

}
