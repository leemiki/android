package com.haolang.util.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.haolang.util.java.FileUtil;

import android.os.AsyncTask;

/**
 * 文件下载工具类
 * <p>
 * v1.0支持基于http协议下载文件
 * </p>
 * <p>
 * v1.0支持下载相关参数（单位数据量、连接超时时间...）的设置
 * </p>
 * <p>
 * v1.0支持各状态（开始下载、下载中、下载成功、下载失败、下载出错）的监控
 * </p>
 * 
 * @author hph
 * 
 * @version 1.0
 * 
 * @author yj
 * 
 * @version 1.1 2013.07.19 15:48 重构
 */
public abstract class HttpDownloader {

	private static final int DEFAULTNOTICEBITSIZE = 256;// 单位数据量的默认大小
	private static final int DEFAULTCONNECTIMEOUT = 5000;// 连接超时时间的默认大小

	private int noticeBitSize = HttpDownloader.DEFAULTNOTICEBITSIZE;// 单位数据量
	private int connectTimeout = HttpDownloader.DEFAULTCONNECTIMEOUT;// 连接超时时间

	public static final String SUFFIX = ".temp";

	private DownloadTask downloadTask;// 下载实际执行类

	/**
	 * 设置单位数据量大小，单位数据量的定义参见类的注释。<br />
	 * 本值不宜过大，推荐在1024以内
	 * 
	 * @param size
	 *            新的大小，单位为B
	 * 
	 * @throws IllegalArgumentException
	 *             如果size为非正数，则抛出此异常
	 */
	public void setNoticeBitSize(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("无法设置非正的单位数据量");
		}

		noticeBitSize = size;
	}

	/**
	 * 返回单位数据量，单位数据量的定义参见类的注释。
	 * 
	 * @return 下载数据最小通知大小
	 */
	public int getNoticeBitSize() {
		return noticeBitSize;
	}

	/**
	 * 设置连接超时时间，该时间默认为5000毫秒
	 * 
	 * @param timeout
	 *            超时时间，单位为毫秒
	 * 
	 * @throws IllegalArgumentException
	 *             当timeout小于或者等于0时抛出此异常
	 */
	public void setConnectTimeout(int timeout) {
		if (timeout <= 0) {
			throw new IllegalArgumentException("无法设置非正的超时时间");
		}

		connectTimeout = timeout;
	}

	/**
	 * 返回超时时间
	 * 
	 * @return 超时时间
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * 启动下载的方法，系统会尝试下载远程文件，然后以覆盖方式保存到本地。<br/>
	 * 当正式下载前，onStart方法将被调用，如果下载完成，onEnd方法将被调用。<br/>
	 * 在下载中，每下载单位数据量的数据，onWorking方法将被调用，单位数据量的定义参见类的注释。<br/>
	 * 如果下载或者保存时发生错误，onError方法将被调用。<br/>
	 * 
	 * @param fileUrl
	 *            远程文件地址
	 * @param path
	 *            下载后保存的路径
	 * 
	 * @throws NullPointerException
	 *             如果url或者path至少有一个参数为null，则抛出此异常
	 * @throws MalformedURLException
	 *             如果url指定了未知协议
	 * @throws IOException
	 *             在系统打开连接错误的情况下，抛出此异常
	 */
	public void start(String fileUrl, String path)
			throws MalformedURLException, IOException {
		if (fileUrl == null || path == null) {
			throw new NullPointerException("下载文件函数传入参数为空");// NullPointerException来源
		}

		URL url = new URL(fileUrl);// MalformedURLException来源

		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setConnectTimeout(connectTimeout);

		// 尝试获得远程文件输入流，然后尝试创建本地文件，两者如果至少其一失败则抛出IOException
		downloadTask = new DownloadTask(urlConn, path);

		downloadTask.execute();
	}

	/**
	 * 尝试根据path创建文件
	 * 
	 * @param path
	 *            文件路径
	 * 
	 * @return 如果文件创建成功或者已经存在，则返回其对应输出流，否则返回null
	 * 
	 * @throws IOException
	 *             文件路径错误导致获取文件输出流失败，抛出此异常
	 */
	private OutputStream getFileStream(String path) throws IOException {
		FileOutputStream fos = null;
		File file = FileUtil.createFile(path);

		if (file != null) {
			fos = new FileOutputStream(file);
		}

		return fos;
	}

	/**
	 * 停止下载文件，在本函数调用过程中发生异常，onError函数不会被调用<br/>
	 * 在停止下载后，onStop函数将被调用，并会尝试删除已经下载的文件。
	 */
	public void stop() {
		downloadTask.stop();
	}

	/**
	 * 在正式下载前将被调用的方法。<br/>
	 * 本函数的执行线程和类的创建线程一致
	 * 
	 * @param filesize
	 *            总的文件大小，单位为B
	 */
	protected abstract void onStart(int filesize);

	/**
	 * 每下载单位数据量的数据，本函数将被调用，以便对外通知已经下载的数据大小，单位数据量的定义参见类的注释。<br/>
	 * 本函数的执行线程和类的创建线程一致
	 * 
	 * @param size
	 *            已经下载的数据大小，单位为B
	 */
	protected abstract void onWorking(int size);

	/**
	 * 在下载完成后，本函数将被调用 本函数的执行线程和类的创建线程一致
	 * 
	 * @param size
	 *            已经下载的数据大小，单位为B
	 */
	protected abstract void onEnd(int size);

	/**
	 * 下载或者保存时发生错误，则本函数将被调用。<br/>
	 * 但是在调用stop函数以停止下载的过程中发生错误，本函数不会被调用<br/>
	 * 在启动下载前本函数也不会被调用<br />
	 * 本函数的执行线程和类的创建线程一致
	 * 
	 * @param size
	 *            已经下载的数据大小，单位为B
	 */
	protected abstract void onError(int size);

	/**
	 * 停止下载文件后，本函数将被调用。 本函数的执行线程和类的创建线程一致
	 * 
	 * @param size
	 *            已经下载的数据大小，单位为B
	 */
	protected abstract void onStop(int size);

	/**
	 * 下载类
	 * 
	 * @author hph
	 * 
	 */
	private class DownloadTask extends AsyncTask<String, Integer, SizeData> {
		private boolean alive = true;// 本变量如果为true则代表本类继续工作

		private InputStream inputStream;// 输入流
		private HttpURLConnection urlConn;// 网络连接
		private OutputStream outputStream;// 输出流
		private String filePath;// 输出流
		private String tempPath;// 临时路径

		public DownloadTask(HttpURLConnection urlConn, String filePath) {
			this.urlConn = urlConn;
			this.filePath = filePath;
			this.tempPath = filePath.substring(0, filePath.lastIndexOf("."))
					+ SUFFIX;
		}

		/**
		 * 停止下载
		 */
		private void stop() {
			alive = false;
		}

		@Override
		protected SizeData doInBackground(String... params) {
			int totalLength = urlConn.getContentLength();
			onStart(Math.max(0, totalLength));// 发出通知，已经准备开始

			SizeData result = null;
			int downloadSize = 0;
			try {
				inputStream = urlConn.getInputStream();
			} catch (IOException e1) {
				e1.printStackTrace();
				result = SizeData.createErrorData(downloadSize);
			}

			if (inputStream != null) {
				try {
					outputStream = getFileStream(tempPath);
				} catch (IOException e1) {
					e1.printStackTrace();
					result = SizeData.createErrorData(downloadSize);
				}

				byte[] buf = new byte[noticeBitSize];
				try {
					while (alive) {
						int count = inputStream.read(buf);
						if (count == -1) {
							result = SizeData.createRightData(downloadSize);
							break;
						} else {
							outputStream.write(buf, 0, count);
							downloadSize += count;

							onReceiveSuccess(downloadSize);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					result = SizeData.createErrorData(downloadSize);
				}

				if (!alive) {
					FileUtil.deleteFile(tempPath);
					result = SizeData.createOtherData(downloadSize);
				}

				try {
					outputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						outputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return result;
		}

		private void onReceiveSuccess(Integer size) {
			this.publishProgress(size);
		}

		@Override
		protected void onPostExecute(SizeData result) {
			if (result.isError == null) {
				onStop(result.size);
			} else {
				if (result.isError) {
					onError(result.size);
				} else {
					FileUtil.renameFile(tempPath, filePath);
					onEnd(result.size);
				}
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			onWorking(values[0]);
		}
	}

	/**
	 * 本类记录下载量数据，但是区分正确/错误
	 * 
	 * @author hph
	 */
	private static class SizeData {
		private Boolean isError;
		private Integer size;

		private static SizeData createErrorData(Integer size) {
			AndroidLog.i("下载数据", "错误");

			return new SizeData(true, size);
		}

		private static SizeData createRightData(Integer size) {
			AndroidLog.i("下载数据", "正确");

			return new SizeData(false, size);
		}

		private static SizeData createOtherData(Integer size) {
			AndroidLog.i("下载数据", "其他");

			return new SizeData(null, size);
		}

		private SizeData(Boolean isError, Integer size) {
			this.isError = isError;
			this.size = size;
		}
	}

}
