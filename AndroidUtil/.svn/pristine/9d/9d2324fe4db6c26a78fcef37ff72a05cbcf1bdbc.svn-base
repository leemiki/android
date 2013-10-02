package com.haolang.util.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.haolang.util.android.AndroidLog;
import com.haolang.util.java.FileUtil;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * 图片工具类
 * <p>
 * v1.0支持本地图片的获取
 * </p>
 * <p>
 * v1.0支持本地图片的写入
 * </p>
 * <p>
 * v1.1支持两张图片的拼接
 * </p>
 * <p>
 * v1.1支持单张图片的缩放
 * </p>
 * <p>
 * v1.1支持单张图片的裁剪
 * </p>
 * <p>
 * v1.1支持单张图片的旋转
 * </p>
 * 
 * @author yj
 * 
 * @version 1.0 2013.07.18 17:14
 * 
 * @author wjl
 * 
 * @version 1.1 2013.07.22 14:56 添加了一些图片操作功能
 */
public class ImageUtil {
	/**
	 * 拼接图片时的对其方式，中间对齐
	 */
	public static final int CENTER = 0;
	/**
	 * 拼接图片时的对其方式，左边对齐
	 */
	public static final int LEFT = 1;
	/**
	 * 拼接图片时的对其方式，右边对齐
	 */
	public static final int RIGHT = 2;

	/**
	 * 获取本地图片
	 * 
	 * @param imagePath
	 *            图片地址
	 * 
	 * @return Bitmap对象
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static Bitmap getLocalImage(String imagePath) {
		Bitmap bitmap = null;

		if (imagePath != null && !imagePath.equals("")) {
			bitmap = BitmapFactory.decodeFile(imagePath);
		} else {
			throw new IllegalArgumentException("获取本地图片：地址输入有误！");
		}

		return bitmap;
	}

	/**
	 * 将图片保存至本地
	 * 
	 * @param imagePath
	 *            图片地址
	 * @param bitmap
	 *            源图片对象
	 * @param isOverride
	 *            如果图片存在，是否覆盖
	 * 
	 * @return 文件对象（如果图片已存在或者图片写入成功返回 否则返回null)
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static File writeLocalImage(String imagePath, Bitmap bitmap,
			boolean isOverride) {
		File file = null;

		if (imagePath == null || imagePath.equals("")) {
			throw new IllegalArgumentException("保存图片至本地：图片地址输入有误！");
		} else {
			if (bitmap == null) {
				throw new IllegalArgumentException("保存图片至本地：源图片对象输入有误！");
			} else {
				if (!isOverride) {
					file = FileUtil.isExitFile(imagePath);
				}

				if (file == null) {
					file = new File(imagePath);
					CompressFormat format = imagePath.indexOf(".jpg") != -1 ? CompressFormat.JPEG
							: CompressFormat.PNG;
					FileOutputStream fos = null;

					try {
						fos = new FileOutputStream(file);
						bitmap.compress(format, 100, fos);
					} catch (IOException e) {
						file = null;
						e.printStackTrace();
					} finally {
						if (fos != null) {
							try {
								fos.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		return file;
	}

	/**
	 * 拼接两张图片 ，b1在b2上面
	 * 
	 * @param b1
	 *            需要拼接的第一张图片
	 * @param b2
	 *            需要拼接的第二张图片
	 * @param location
	 *            对齐方式，参数定义为CENTER,LEFT,RIGHT
	 * 
	 * @return 返回拼接后的图片
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static Bitmap spellBitmap(Bitmap b1, Bitmap b2, int location) {
		if (b1 == null) {
			throw new IllegalArgumentException("拼接两张图片时传入拼接的第一张图片为null");
		} else if (b2 == null) {
			throw new IllegalArgumentException("拼接两张图片时传入拼接的第二张图片为null");
		} else {
			Bitmap result = null;
			int width = b1.getWidth() > b2.getWidth() ? b1.getWidth() : b2
					.getWidth();
			result = Bitmap.createBitmap(width,
					b1.getHeight() + b2.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(result);

			Point p1 = getPoint(b1, location, width, 0);
			Rect r1 = new Rect(p1.x, p1.y, p1.x + b1.getWidth(), p1.y
					+ b1.getHeight());

			Point p2 = getPoint(b2, location, width, b1.getHeight());
			Rect r2 = new Rect(p2.x, p2.y, p2.x + b2.getWidth(), p2.y
					+ b2.getHeight());

			c.drawBitmap(b1, null, r1, null);
			c.drawBitmap(b2, null, r2, null);
			c.save(Canvas.ALL_SAVE_FLAG);
			c.restore();

			return result;
		}
	}

	/**
	 * 根据对齐方式获得起点坐标
	 * 
	 * @param bitmap
	 *            需要进行定位的图片
	 * @param location
	 *            对齐位置
	 * @param width
	 *            整图的宽度
	 * @param paddingTop
	 *            该图距上边界的距离
	 * 
	 * @return 该图的起点（左上角）坐标
	 */
	private static Point getPoint(Bitmap bitmap, int location, int width,
			int paddingTop) {
		Point point = new Point();
		switch (location) {
		case CENTER:
			point.x = (width - bitmap.getWidth()) / 2;
			break;
		case LEFT:
			point.x = 0;
			break;
		case RIGHT:
			point.x = width - bitmap.getWidth();
			break;
		default:
			break;
		}
		point.y = paddingTop;

		return point;
	}

	/**
	 * 对图片进行缩放
	 * 
	 * @param bitmap
	 *            需要缩放的源图片
	 * @param scale
	 *            缩放的比例
	 * 
	 * @return 缩放后返回的结果图片
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap, float scale) {
		if (bitmap == null) {
			throw new IllegalArgumentException("对图片进行缩放时传入进行缩放的源图片为null");
		} else {
			Bitmap result = null;
			Matrix matrix = new Matrix();
			matrix.setScale(scale, scale);
			result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);

			return result;
		}
	}

	/**
	 * 对图片进行截取，当截取的边界超出图片的边界时，已图片边界为准
	 * 
	 * @param bitmap
	 *            需要截取的源图片
	 * @param rect
	 *            截取的位置 ，相对于图片的位置
	 * 
	 * @return 返回截取结果的图片
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static Bitmap cutBitmap(Bitmap bitmap, Rect rect) {
		if (bitmap == null) {
			throw new IllegalArgumentException("对图片进行截取时截取图片时的源图片为null");
		} else if (rect == null) {
			throw new IllegalArgumentException("对图片进行截取时截取区域为null");
		} else {
			rect = checkRect(bitmap, rect);

			int width = rect.right - rect.left;
			int height = rect.bottom - rect.top;
			Bitmap result = Bitmap.createBitmap(bitmap, rect.left, rect.top,
					width, height);

			return result;
		}
	}

	/**
	 * 检查裁剪区域是否超过了图片区域，超过部分以图片区域为准
	 * 
	 * @param bitmap
	 *            需要裁剪的图片
	 * @param rect
	 *            计算前需要裁剪的区域
	 * 
	 * @return 计算后需要裁剪的区域
	 */
	private static Rect checkRect(Bitmap bitmap, Rect rect) {
		if (rect.left < 0) {
			AndroidLog.i("ImageUtil", "裁剪区域左边界超出图片边界，以图片边界为准");
			rect.left = 0;
		}
		if (rect.top < 0) {
			AndroidLog.i("ImageUtil", "裁剪区域上边界超出图片边界，以图片边界为准");
			rect.top = 0;
		}
		if (rect.right > bitmap.getWidth()) {
			AndroidLog.i("ImageUtil", "裁剪区域右边界超出图片边界，以图片边界为准");
			rect.right = bitmap.getWidth();
		}
		if (rect.bottom > bitmap.getHeight()) {
			AndroidLog.i("ImageUtil", "裁剪区域下边界超出图片边界，以图片边界为准");
			rect.bottom = bitmap.getHeight();
		}

		return rect;
	}

	/**
	 * 对图片进行旋转
	 * 
	 * @param bitmap
	 *            需要选择的源图片
	 * @param angle
	 *            旋转的角度的度数float类型，（单位是°）
	 * @param point
	 *            选择的中心点
	 * 
	 * @return 返回旋转后的图片
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, float angle, Point point) {
		if (bitmap == null) {
			throw new IllegalArgumentException("对图片进行旋转时选择图片时的源图片为null");
		} else {
			Matrix matrix = new Matrix();
			angle = (float) (angle / 90 * Math.PI / 2);
			matrix.setRotate(angle, point.x, point.y);
			Bitmap result = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			return result;
		}
	}

	/**
	 * 缩放图片，使其适应目标宽高，若图片未超过目标框的宽高，则返回源图片
	 * 
	 * @param bitmap
	 *            需要适应的图片
	 * @param width
	 *            图片需要适应的宽度
	 * @param height
	 *            图片需要适应的高度
	 * 
	 * @return 返回进行适应处理之后的图片
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或参数不合法，将报此异常
	 */
	public static Bitmap fitBitmap(Bitmap bitmap, int width, int height) {
		if (bitmap == null) {
			throw new IllegalArgumentException("缩放图片，使其适应目标宽高时的源图片为null");
		} else if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("缩放图片，使其适应目标宽高时适应的宽高不合法");
		} else {
			float scale = checkBitmap(bitmap, width, height);
			if (scale == 0) {
				return bitmap;
			} else {
				return scaleBitmap(bitmap, scale);
			}
		}
	}

	/**
	 * 计算缩放比例
	 * 
	 * @param bitmap
	 *            需要缩放的图片
	 * @param width
	 *            需要适应的宽度
	 * @param height
	 *            需要适应的高度
	 * 
	 * @return 返回需要缩放的比例
	 */
	private static float checkBitmap(Bitmap bitmap, float width, float height) {
		float bWidth = bitmap.getWidth();
		float bHeight = bitmap.getHeight();
		if (bWidth < width && bHeight < height) {
			return 0;
		} else {
			float bScale = bWidth / bHeight;
			float scale = width / height;
			if (bScale > scale) {
				return width / bWidth;
			} else {
				return height / bHeight;
			}
		}
	}

}
