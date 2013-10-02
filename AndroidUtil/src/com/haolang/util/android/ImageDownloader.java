package com.haolang.util.android;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.haolang.util.java.FileUtil;

/**
 * 图片下载工具类
 * <p>
 * v1.0支持基于http协议下载图片
 * </p>
 * <p>
 * v1.0支持下载成功和失败状态的监控
 * </p>
 * 
 * @author yj
 * 
 * @version 1.0 2013.07.16 10:23
 * @version 1.2 2013.08.21 11:28 修改图片本地缓存
 * @version 1.3 2013.09.10 11.20 修改图片本地缓存路径
 * @version 1.4 2013.09.11 11:32 新增图片覆盖控制
 */
public abstract class ImageDownloader extends
		AsyncTask<String, Integer, SoftReference<Bitmap>> {

	private ImageView image;
	private int waitImage;
	private int failImage;

	private String imageUrl = "";
	private String mark = "";

	/**
	 * @param image
	 *            ImageView控件对象
	 * @param waitImage
	 *            等待图片资源ID
	 * @param failImage
	 *            失败图片资源ID
	 */
	public ImageDownloader(ImageView image, int waitImage, int failImage) {
		this.image = image;
		this.waitImage = waitImage;
		this.failImage = failImage;
	}

	/**
	 * 加载服务端图片
	 * 
	 * @param imageUrl
	 *            远程图片地址
	 * @param mark
	 *            标示符（教师端：teacher 学生端：student）
	 * @param isOverride
	 *            是否覆盖
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public void loadImage(String imageUrl, String mark, boolean isOverride) {
		if (imageUrl != null && !imageUrl.equals("") && mark != null) {
			this.imageUrl = imageUrl;
			this.mark = mark;

			if (isOverride
					|| FileUtil.isExitFile(getImagePath(imageUrl)) == null) {
				execute(imageUrl);
			} else {
				provideImage(readImage(imageUrl));
			}
		} else {
			throw new IllegalArgumentException("加载服务端图片：地址输入有误！");
		}
	}

	@Override
	protected SoftReference<Bitmap> doInBackground(String... params) {
		SoftReference<Bitmap> softBitmap = null;
		Bitmap bitmap = getRemoteImage(params[0]);

		if (params.length > 0 && bitmap != null) {
			softBitmap = new SoftReference<Bitmap>(bitmap);
		}

		return softBitmap;
	}

	@Override
	protected void onPostExecute(SoftReference<Bitmap> softBitmap) {
		super.onPostExecute(softBitmap);
		if (softBitmap != null) {
			provideImage(softBitmap.get());
			writeImage(imageUrl, softBitmap.get());
		} else {
			image.setImageResource(failImage);
			loadImageFailed(failImage);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		image.setImageResource(waitImage);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	/**
	 * 获取下载成功后的Bitmap
	 * 
	 * @param bitmap
	 *            Bitmap对象
	 */
	protected abstract void getImageBitmap(Bitmap bitmap);

	/**
	 * 图片加载失败后调用
	 * 
	 * @param resId
	 *            失败图片资源ID
	 */
	protected void loadImageFailed(int resId) {

	}

	/**
	 * 提供图片
	 * 
	 * @param bitmap
	 *            图片Bitmap对象
	 */
	private void provideImage(Bitmap bitmap) {
		image.setImageBitmap(bitmap);
		getImageBitmap(bitmap);
	}

	/**
	 * 将图片保存至本地（本地不存在）
	 * 
	 * @param imageUrl
	 *            图片请求路径
	 * @param bitmap
	 *            图片Bitmap对象
	 */
	private void writeImage(String imageUrl, Bitmap bitmap) {
		ImageUtil.writeLocalImage(getImagePath(imageUrl), bitmap, true);
	}

	/**
	 * 从本地读取图片
	 * 
	 * @param imageUrl
	 *            图片路径
	 * 
	 * @return Bitmap对象（不存在返回null）
	 */
	private Bitmap readImage(String imageUrl) {
		Bitmap bitmap = ImageUtil.getLocalImage(getImagePath(imageUrl));

		return bitmap;
	}

	/**
	 * 根据图片请求路径生成本地图片
	 * 
	 * @param imageUrl
	 *            图片路径
	 * 
	 * @return 本地生成的图片路径 （如果目录创建成功返回 否则返回空字符串）
	 */
	private String getImagePath(String imageUrl) {
		String tag = mark.equals("student") ? "/StudentTomon" : "/TeacherTomon";
		String dir = imageUrl.substring(0, imageUrl.lastIndexOf("/"));
		dir = dir.substring(dir.lastIndexOf("/"));

		String filePath = Environment.getExternalStorageDirectory() + tag
				+ "/location/img" + dir;
		String folderName = imageUrl.substring(imageUrl.lastIndexOf("/"));

		File path = FileUtil.createDirector(filePath);

		return path != null ? filePath + folderName : "";
	}

	/**
	 * 获取远程图片
	 * 
	 * @param urlStr
	 *            图片路径
	 * 
	 * @return Bitmap对象（如果获取图片成功返回 否则返回null）
	 */
	private Bitmap getRemoteImage(String urlStr) {
		Bitmap bitmap = null;
		URL url;
		HttpURLConnection conn = null;
		InputStream is = null;

		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			is = conn.getInputStream();
			int length = (int) conn.getContentLength();

			if (length != -1) {
				byte[] imgData = new byte[length];
				byte[] temp = new byte[64];
				int readLen = 0;
				int destPos = 0;

				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, imgData, destPos, readLen);
					destPos += readLen;
				}

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPurgeable = true;
				bitmap = BitmapFactory.decodeByteArray(imgData, 0,
						imgData.length, options);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (conn != null) {
				conn.disconnect();
			}
		}

		return bitmap;
	}

}
