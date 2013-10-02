package com.haolang.util.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.xerial.snappy.SnappyInputStream;
import org.xerial.snappy.SnappyOutputStream;

import com.haolang.util.android.AndroidLog;

/**
 * 文件工具类
 * <p>
 * v1.0支持目录/文件的创建、删除、重命名、存在判断
 * </p>
 * <p>
 * v1.0支持目录/文件大小的获取
 * </p>
 * <p>
 * v1.0支持字符串源文件的读取、写入
 * </p>
 * <p>
 * v1.0支持单文件的拷贝/剪切、及拷贝/剪切过程中的修改
 * </p>
 * <p>
 * v1.0支持单层文件夹批量文件的拷贝 暂不支持多层文件夹
 * </p>
 * <p>
 * v1.0支持文件的压缩/解压缩（snappy算法）
 * </p>
 * 
 * @author yj
 * 
 * @version 1.0 2013.07.11 14:36
 */
public class FileUtil {

	private static IUpdateFile updateFile;

	public static interface IUpdateFile {
		String update(String str);
	}

	public static void setUpdateFile(IUpdateFile updateFile) {
		FileUtil.updateFile = updateFile;
	}

	/**
	 * 创建目录
	 * 
	 * @param pathName
	 *            目录路径
	 * 
	 * @return 文件对象（如果目录已存在或者创建目录成功返回 否则返回null）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static File createDirector(String pathName) {
		File dir = null;

		if (pathName != null && !pathName.equals("")) {
			dir = FileUtil.isExitFile(pathName);

			if (dir == null) {
				dir = new File(pathName);

				if (!dir.mkdirs()) {
					dir = null;
				}
			} else {
				AndroidLog.w("创建目录", "目录已存在");
			}
		} else {
			throw new IllegalArgumentException("创建目录：路径输入有误！");
		}

		return dir;
	}

	/**
	 * 创建文件（如果目录不存在将自动创建目录）
	 * 
	 * @param pathName
	 *            文件全路径
	 * 
	 * @return 文件对象（如果文件已存在或者创建文件成功返回 否则返回null）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 * @throws IOException
	 *             文件路径错误导致创建文件失败，将报此异常
	 */
	public static File createFile(String pathName) throws IOException {
		File file = null;

		if (pathName != null && !pathName.equals("")) {
			File dir = FileUtil.createDirector(pathName.substring(0,
					pathName.lastIndexOf("/")));

			if (dir != null) {
				file = FileUtil.isExitFile(pathName);

				if (file == null) {
					file = new File(pathName);

					if (!file.createNewFile()) {
						file = null;
					}
				} else {
					AndroidLog.w("创建文件", "文件已存在");
				}
			}
		} else {
			throw new IllegalArgumentException("创建文件：路径输入有误！");
		}

		return file;
	}

	/**
	 * 删除目录/文件
	 * 
	 * @param pathName
	 *            目录/文件全路径
	 * 
	 * @return 是否成功标示符（如果删除成功返回true 否则返回false）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static boolean deleteFile(String pathName) {
		boolean isSuccess = false;

		if (pathName != null && !pathName.equals("")) {
			File file = FileUtil.isExitFile(pathName);

			if (file != null) {
				isSuccess = file.delete();
			}
		} else {
			throw new IllegalArgumentException("删除目录/文件：路径输入有误！");
		}

		return isSuccess;
	}

	/**
	 * 重命名目录/文件
	 * 
	 * @param pathName
	 *            目录/文件全路径
	 * @param newName
	 *            新目录/文件全路径
	 * 
	 * @return 文件对象（如果重命名成功返回 否则返回null）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static File renameFile(String pathName, String newName) {
		File file = null;

		if (pathName == null || pathName.equals("")) {
			throw new IllegalArgumentException("重命名目录/文件：源路径输入有误！");
		} else {
			if (newName == null || newName.equals("")) {
				throw new IllegalArgumentException("重命名目录/文件：新路径输入有误！");
			} else {
				file = FileUtil.isExitFile(pathName);

				if (file != null) {
					File newFile = FileUtil.isExitFile(newName);

					if (newFile == null) {
						newFile = new File(newName);
						file = file.renameTo(newFile) ? newFile : null;
					} else {
						file = null;
					}
				}
			}
		}

		return file;
	}

	/**
	 * 判断目录/文件是否存在
	 * 
	 * @param pathName
	 *            目录/文件全路径
	 * 
	 * @return 文件对象（如果存在返回 否则返回null）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static File isExitFile(String pathName) {
		File file = null;

		if (pathName != null && !pathName.equals("")) {
			file = new File(pathName);

			if (!file.exists()) {
				file = null;
			}
		} else {
			throw new IllegalArgumentException("判断存在目录/文件：路径输入有误！");
		}

		return file;
	}

	/**
	 * 获取本地文件大小
	 * 
	 * @param pathName
	 *            目录/文件全路径
	 * 
	 * @return 文件大小
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static long getFileSize(String pathName) {
		long size = 0l;

		if (pathName != null && !pathName.equals("")) {
			File file = FileUtil.isExitFile(pathName);

			if (file != null) {
				size = file.length();
			}
		} else {
			throw new IllegalArgumentException("获取目录/文件大小：路径输入有误！");
		}

		return size;
	}

	/**
	 * 读取本地源文件
	 * 
	 * @param pathName
	 *            文件路径
	 * 
	 * @return 字符串数据（如果读取成功返回 否则返回空字符串）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static String readString(String pathName) {
		String result = "";

		if (pathName != null && !pathName.equals("")) {
			FileReader fr = null;
			BufferedReader br = null;
			String temp = "";
			File file = new File(pathName);

			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				temp = br.readLine();

				while (temp != null) {
					result += temp;
					temp = br.readLine();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fr != null) {
						fr.close();
					}

					if (br != null) {
						br.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new IllegalArgumentException("读取本地文件：路径输入有误！");
		}

		return result;
	}

	/**
	 * 将字符串数据写入本地源文件
	 * 
	 * @param pathName
	 *            文件路径
	 * @param result
	 *            字符串源数据
	 * @param isAppend
	 *            是否追加
	 * 
	 * @return 文件对象（如果写入成功返回 否则返回null）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static File writeString(String pathName, String result,
			boolean isAppend) {
		File file = null;

		if (pathName == null || pathName.equals("")) {
			throw new IllegalArgumentException("字符串写入本地文件：路径输入有误！");
		} else {
			if (result == null || result.equals("")) {
				throw new IllegalArgumentException("字符串写入本地文件：源数据输入有误！");
			} else {
				FileWriter fw = null;
				PrintWriter pw = null;
				file = new File(pathName);

				try {
					fw = new FileWriter(file, isAppend);
					pw = new PrintWriter(fw, true);
					pw.print(result);
				} catch (FileNotFoundException e) {
					file = null;
					e.printStackTrace();
				} catch (IOException e) {
					file = null;
					e.printStackTrace();
				} finally {
					if (fw != null) {
						try {
							fw.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (pw != null) {
						pw.close();
					}
				}
			}
		}

		return file;
	}

	/**
	 * 文件批量拷贝/剪切（暂不支持多层文件夹）
	 * 
	 * @param sourcePath
	 *            源文件夹路径
	 * @param newPath
	 *            新文件夹路径
	 * @param isByte
	 *            是否采用字节流处理（true：字节流 false：字符流）
	 * @param isCopy
	 *            是否拷贝/剪切（true：拷贝，false：剪切）
	 * 
	 * @return 是否成功标示符（如果批量拷贝/剪切成功返回true 否则返回false）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static boolean batchCopyFile(String sourcePath, String newPath,
			boolean isByte, boolean isCopy) {
		boolean isSuccess = false;

		if (sourcePath == null || sourcePath.equals("")) {
			throw new IllegalArgumentException("文件批量"
					+ (isCopy ? "拷贝：" : "剪切：") + "源文件夹路径输入有误！");
		} else {
			if (newPath == null || newPath.equals("")) {
				throw new IllegalArgumentException("文件批量"
						+ (isCopy ? "拷贝：" : "剪切：") + "新文件夹路径输入有误！");
			} else {
				File dir = new File(sourcePath);
				File[] files = dir.listFiles();

				if (files != null) {
					int len = files.length;
					File file = null;
					File newFile = null;

					if (FileUtil.createDirector(newPath) != null) {
						for (int i = 0; i < len; i++) {
							file = files[i];

							newFile = new File(newPath + "\\" + file.getName());
							isSuccess = isByte ? copyFile1(file, newFile,
									isCopy) : copyFile2(file, newFile, isCopy);

							if (!isSuccess) {
								break;
							}
						}

						if (isSuccess && !isCopy) {
							FileUtil.deleteFile(sourcePath);
						}
					}
				}
			}
		}

		return isSuccess;
	}

	/**
	 * 单文件拷贝/剪切（字节流）
	 * 
	 * @param file
	 *            源文件
	 * @param newFile
	 *            新文件
	 * @param isCopy
	 *            是否拷贝/剪切（true：拷贝，false：剪切）
	 * 
	 * @return 是否成功标示符（如果拷贝/剪切成功返回true 否则返回false）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static boolean copyFile1(File file, File newFile, boolean isCopy) {
		boolean isSuccess = false;

		if (file == null) {
			throw new IllegalArgumentException("文件" + (isCopy ? "拷贝：" : "剪切：")
					+ "源文件输入有误！");
		} else {
			if (newFile == null) {
				throw new IllegalArgumentException("文件"
						+ (isCopy ? "拷贝：" : "剪切：") + "新文件输入有误！");
			} else {
				FileInputStream fis = null;
				FileOutputStream fos = null;
				byte[] buffer = new byte[1024];
				int len = 0;

				try {
					fis = new FileInputStream(file);
					fos = new FileOutputStream(newFile);

					while ((len = fis.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
						fos.flush();
					}

					isSuccess = true;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (fis != null) {
							fis.close();

							if (isSuccess && !isCopy) {
								FileUtil.deleteFile(file.getCanonicalPath());
							}
						}

						if (fos != null) {
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return isSuccess;
	}

	/**
	 * 单文件拷贝/剪切（字符流）
	 * 
	 * <p>
	 * 提供对外接口函数update 允许拷贝过程中修改 文件内容 详情参考IUpdateFile
	 * </p>
	 * 
	 * @param file
	 *            源文件
	 * @param newFile
	 *            新文件
	 * @param isCopy
	 *            是否拷贝/剪切（true：拷贝，false：剪切）
	 * 
	 * @return 是否成功标示符（如果拷贝/剪切成功返回true 否则返回false）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static boolean copyFile2(File file, File newFile, boolean isCopy) {
		boolean isSuccess = false;

		if (file == null) {
			throw new IllegalArgumentException("文件" + (isCopy ? "拷贝：" : "剪切：")
					+ "源文件输入有误！");
		} else {
			if (newFile == null) {
				throw new IllegalArgumentException("文件"
						+ (isCopy ? "拷贝：" : "剪切：") + "新文件输入有误！");
			} else {
				FileReader fr = null;
				BufferedReader br = null;
				PrintWriter pw = null;
				String str = "";

				try {
					fr = new FileReader(file);
					br = new BufferedReader(fr);
					pw = new PrintWriter(newFile);

					while ((str = br.readLine()) != null) {
						pw.print(updateFile != null ? updateFile.update(str)
								: str + "\n");
						pw.flush();
					}

					isSuccess = true;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (fr != null) {
							fr.close();
						}

						if (br != null) {
							br.close();

							if (isSuccess && !isCopy) {
								FileUtil.deleteFile(file.getCanonicalPath());
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (pw != null) {
						pw.close();
					}
				}
			}
		}

		return isSuccess;
	}

	/**
	 * Snappy算法压缩文件
	 * 
	 * @param file
	 *            源文件
	 * @param newFile
	 *            压缩文件
	 * 
	 * @return 是否压缩成功 （如果压缩成功返回true 否则返回false）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static boolean compress(File file, File newFile) {
		boolean isSuccess = false;

		if (file == null) {
			throw new IllegalArgumentException("压缩文件：源文件输入有误！");
		} else {
			if (newFile == null) {
				throw new IllegalArgumentException("压缩文件：新文件输入有误！");
			} else {
				FileInputStream fis = null;
				SnappyOutputStream sos = null;
				byte[] buffer = new byte[1024];
				int len = 0;

				try {
					fis = new FileInputStream(file);
					sos = new SnappyOutputStream(new FileOutputStream(newFile));

					while ((len = fis.read(buffer)) != -1) {
						sos.write(buffer, 0, len);
						sos.flush();
					}

					isSuccess = true;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (fis != null) {
							fis.close();
						}

						if (sos != null) {
							sos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return isSuccess;
	}

	/**
	 * Snappy算法解压文件
	 * 
	 * @param file
	 *            源文件
	 * @param newFile
	 *            解压文件
	 * 
	 * @return 是否解压成功 （如果解压成功返回true 否则返回false）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static boolean decompress(File file, File newFile) {
		boolean isSuccess = false;

		if (file == null) {
			throw new IllegalArgumentException("解压文件：源文件输入有误！");
		} else {
			if (newFile == null) {
				throw new IllegalArgumentException("解压文件：新文件输入有误！");
			} else {
				SnappyInputStream sis = null;
				FileOutputStream fos = null;

				try {
					sis = new SnappyInputStream(new FileInputStream(file));
					fos = new FileOutputStream(newFile);
					byte[] buffer = new byte[1024];
					int len = 0;

					while ((len = sis.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
						fos.flush();
					}

					isSuccess = true;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (sis != null) {
							sis.close();
						}

						if (fos != null) {
							fos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return isSuccess;
	}

	public static void main(String args[]) throws IOException {
		String path = "C://Documents and Settings//Administrator//桌面//新建文件.txt";
		String newPath = "C://Documents and Settings//Administrator//桌面//android工具.txt";
		String compressPath = "C://Documents and Settings//Administrator//桌面//android工具.zip";

//		System.out.println(FileUtil.createFile(path));
		System.out.println(FileUtil.renameFile(path, newPath));
//		System.out.println(FileUtil.readString(newPath));
//		System.out.println(FileUtil.writeString(newPath, "O(∩_∩)O哈哈", true));
//		System.out.println(FileUtil.deleteFile(newPath));
//		System.out.println(batchCopyFile(path, newPath, true, true));

		System.out.println(compress(new File(newPath), new File(compressPath)));
//		System.out.println(decompress(new File(compressPath), new File(path)));
	}

}
