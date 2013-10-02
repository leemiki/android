package com.haolang.util.java;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具类
 * <p>
 * v1.0支持日期相关属性的获取
 * </p>
 * <p>
 * v1.0支持日期对象格式化为字符串
 * </p>
 * <p>
 * v1.0支持字符串转化为日期对象
 * </p>
 * <p>
 * v1.0支持日期之间的计算
 * </p>
 * 
 * @author yj
 * 
 * @version 1.0 2013.07.16 17:45
 */
public class DateUtil {

	private static class Formats {
		private final DateFormat simple = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		private final DateFormat dtSimple = new SimpleDateFormat("yyyy-MM-dd");

		private final DateFormat dtSimpleChinese = new SimpleDateFormat(
				"yyyy年MM月dd日");

		private final DateFormat dtFullChinese = new SimpleDateFormat(
				"yyyy年MM月dd日 HH时mm分");

		private final DateFormat dtShort = new SimpleDateFormat("yyyyMMdd");

		private final DateFormat hmsFormat = new SimpleDateFormat("HH:mm:ss");

		private final DateFormat gmtFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
	}

	private static final ThreadLocal<Formats> local = new ThreadLocal<Formats>();

	/**
	 * 获取当前日期
	 * 
	 * @return 日期字符串（格式yyyy年MM月dd日HH时mm分）
	 */
	public static final String frontFullChineseDate() {
		String str = getFormats().dtFullChinese.format(Calendar.getInstance()
				.getTime());

		return str;
	}

	/**
	 * 获取当前年份
	 * 
	 * @return 年份
	 */
	public static String getCurrentYear() {
		return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
	}

	/**
	 * 获取当前月份
	 * 
	 * @return 月份（小于10,0前缀）
	 */
	public static final String getCurrentMonth() {
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;
		String sMonth;

		if (month < 10) {
			sMonth = "0" + String.valueOf(month);
		} else {
			sMonth = String.valueOf(month);
		}

		return sMonth;
	}

	/**
	 * 获取日期毫秒数
	 * 
	 * @param strDate
	 *            日期字符串（格式：yyyy-MM-dd）
	 * 
	 * @return 日期毫秒数
	 * 
	 * @throws ParseException
	 *             日期格式解析出错，将报此异常
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static final long string2DateLong(String strDate)
			throws ParseException {
		long time = 0l;

		if (strDate != null && !strDate.equals("")) {
			Date d = string2Date(strDate);
			time = d.getTime();
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return time;
	}

	/**
	 * 获取指定日期所在周的周一日期
	 * 
	 * @param date
	 *            初始Date对象
	 * 
	 * @return 周一的日期字符串（格式：yyyy-MM-dd）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static String getFirstDayOfWeek(Date date) {
		String str = "";

		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int dayofweek = cal.get(Calendar.DAY_OF_WEEK) - 1;

			if (dayofweek == 0) {
				dayofweek = 7;
			}

			cal.add(Calendar.DATE, -dayofweek + 1);
			str = cal.get(1) + "-";

			if (cal.get(2) + 1 < 10) {
				str += "0";
			}

			str = str + (cal.get(2) + 1) + "-";

			if (cal.get(5) < 10) {
				str += "0";
			}

			str += cal.get(5);
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return str;
	}

	/**
	 * 获取指定时间当天的起点时间
	 * 
	 * @param date
	 *            初始Date对象
	 * 
	 * @return 初始化后的日期对象
	 * 
	 * @throws ParseException
	 *             日期格式解析出错，将报此异常
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static Date getDayBegin(Date date) throws ParseException {
		Date init = null;

		if (date != null) {
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			df.setLenient(false);

			String dateString = df.format(date);

			init = df.parse(dateString);
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return init;
	}

	/**
	 * 获取日期格式化类型
	 * 
	 * @return 格式化对象
	 */
	private static final Formats getFormats() {
		Formats f = local.get();

		if (f == null) {
			f = new Formats();
			local.set(f);
		}

		return f;
	}

	/**
	 * 日期对象转化为字符串
	 * 
	 * @param date
	 *            日期对象
	 * 
	 * @return 日期字符串（格式：yyyyMMdd）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static final String shortDate(Date date) {
		String str = "";

		if (date != null) {
			str = getFormats().dtShort.format(date);
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return str;
	}

	/**
	 * 日期对象转化为字符串
	 * 
	 * @param date
	 *            日期对象
	 * 
	 * @return 日期字符串（格式：yyyy-MM-dd）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static final String dtSimpleFormat(Date date) {
		String str = "";

		if (date != null) {
			str = getFormats().dtSimple.format(date);
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return str;
	}

	/**
	 * 日期对象转化为字符串
	 * 
	 * @param date
	 *            日期对象
	 * 
	 * @return 时间字符串（格式：HH:mm:ss）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static final String hmsFormat(Date date) {
		String str = "";

		if (date != null) {
			str = getFormats().hmsFormat.format(date);
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return str;
	}

	/**
	 * 日期对象转化为字符串
	 * 
	 * @param date
	 *            日期对象
	 * 
	 * @return 日期字符串（格式：yyyy-MM-dd HH:mm:ss）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static final String simpleFormat(Date date) {
		String str = "";

		if (date != null) {
			str = getFormats().simple.format(date);
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return str;
	}

	/**
	 * 日期对象转化为字符串
	 * 
	 * @param date
	 *            日期对象
	 * 
	 * @return 日期字符串（格式yyyy年MM月dd日）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static final String dtSimpleChineseFormat(Date date) {
		String str = "";

		if (date != null) {
			str = getFormats().dtSimpleChinese.format(date.getTime());
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return str;
	}

	/**
	 * 日期对象转化为字符串
	 * 
	 * @param date
	 *            日期对象
	 * 
	 * @return 日期字符串（格式yyyy年MM月dd日HH时mm分）
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static final String frontFullChineseDate(Date date) {
		String str = "";

		if (date != null) {
			str = getFormats().dtFullChinese.format(date.getTime());
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return str;
	}

	/**
	 * 将格林威治时间格式的字符串转化为日期对象
	 * 
	 * @param strDate
	 *            日期字符串
	 * 
	 * @return 日期对象
	 * 
	 * @throws ParseException
	 *             日期格式解析出错，将报此异常
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static final Date gmtFormatStringDate(String strDate)
			throws ParseException {
		Date date = null;

		if (strDate != null && !strDate.equals("")) {
			date = getFormats().gmtFormat.parse(strDate);
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return date;
	}

	/**
	 * 字符串转化为日期对象
	 * 
	 * @param strDate
	 *            日期字符串（格式：yyyy-MM-dd）
	 * 
	 * @return 日期对象
	 * 
	 * @throws ParseException
	 *             日期格式解析出错，将报此异常
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static final Date string2Date(String strDate) throws ParseException {
		Date date = null;

		if (strDate != null && !strDate.equals("")) {
			date = getFormats().dtSimple.parse(strDate);
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return date;
	}

	/**
	 * 字符串转化为日期对象
	 * 
	 * @param strDate
	 *            日期字符串（格式：yyyy-MM-dd HH:mm:ss）
	 * 
	 * @return 日期对象
	 * 
	 * @throws ParseException
	 *             日期格式解析出错，将报此异常
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static final Date string2DateTime(String strDate)
			throws ParseException {
		Date date = null;

		if (strDate != null && !strDate.equals("")) {
			date = getFormats().simple.parse(strDate);
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return date;
	}

	/**
	 * 字符串转化为日期对象
	 * 
	 * @param strDate
	 *            日期字符串（格式：yyyy年MM月dd日）
	 * 
	 * @return 日期对象
	 * 
	 * @throws ParseException
	 *             日期格式解析出错，将报此异常
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static final Date chineseString2Date(String strDate)
			throws ParseException {
		Date date = null;

		if (strDate != null && !strDate.equals("")) {
			date = getFormats().dtSimpleChinese.parse(strDate);
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return date;
	}

	/**
	 * 获取两日期相差的天数
	 * 
	 * @param startDate
	 *            初始Date对象
	 * @param endDate
	 *            结束Date对象
	 * 
	 * @return 相差天数
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static int getBetweenDate(Date startDate, Date endDate) {
		int day = 0;

		if (startDate != null && endDate != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(endDate);
			int end = c.get(Calendar.YEAR) * 365 + c.get(Calendar.DAY_OF_YEAR);
			c.setTime(startDate);
			day = end
					- (c.get(Calendar.YEAR) * 365 + c.get(Calendar.DAY_OF_YEAR));
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return day;
	}

	/**
	 * 获取月份相差后的第一天的日期
	 * 
	 * @param dt
	 *            初始Date对象
	 * @param mon
	 *            月分增益
	 * 
	 * @return 月第一天的日期对象
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static final Date getMonthFirstDay(Date dt, int mon) {
		Date date = null;

		if (dt != null && mon != 0) {
			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			c.add(Calendar.MONDAY, mon);
			c.set(Calendar.DAY_OF_MONTH, 1);
			date = c.getTime();
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return date;
	}

	/**
	 * 获取月份相差后的日期
	 * 
	 * @param dt
	 *            初始Date对象
	 * @param idiff
	 *            月份增益
	 * 
	 * @return 相差后的日期对象
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static final Date getDiffMon(Date dt, int idiff) {
		Date date = null;

		if (dt != null && idiff != 0) {
			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			c.add(Calendar.MONTH, idiff);
			date = c.getTime();
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return date;
	}

	/**
	 * 获取天数相差后的日期
	 * 
	 * @param dt
	 *            初始Date对象
	 * @param idiff
	 *            天数增益
	 * 
	 * @return 相差后的日期对象
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static final Date getDiffDate(Date dt, int idiff) {
		Date date = null;

		if (dt != null && idiff != 0) {
			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			c.add(Calendar.DATE, idiff);
			date = c.getTime();
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return date;
	}

	/**
	 * 获取分钟相差后的日期
	 * 
	 * @param dt
	 *            初始Date对象
	 * @param delayMinutes
	 *            分钟增益
	 * 
	 * @return 相差后的日期对象
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static Date getDiffTime(Date dt, int delayMinutes) {
		Date date = null;

		if (dt != null && delayMinutes != 0) {
			Calendar c = Calendar.getInstance();
			c.setTime(dt);
			c.add(Calendar.MINUTE, delayMinutes);
			date = c.getTime();
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return date;
	}

	/**
	 * 获取日期相差的时间
	 * 
	 * @param strDate
	 *            日期字符串
	 * @param date
	 *            Date对象
	 * 
	 * @return 相差时间字符串（格式：xx天xx小时xx分钟xx秒）
	 * 
	 * @throws ParseException
	 *             日期格式解析出错，将报此异常
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static String dateDiffDate(String strDate, Date date)
			throws ParseException {
		String str = "";

		if (strDate != null && !strDate.equals("") && date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			java.util.Date newDate = df.parse(strDate);

			long l = date.getTime() - newDate.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

			str = day + "天" + hour + "小时" + min + "分" + s + "秒";
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return str;
	}

	/**
	 * 获取日期相差的小时
	 * 
	 * @param strDate
	 *            日期字符串
	 * @param date
	 *            Date对象
	 * 
	 * @return 相差的小时
	 * 
	 * @throws ParseException
	 *             日期格式解析出错，将报此异常
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public static long dateDiffHour(String strDate, Date date)
			throws ParseException {
		long time = 0l;

		if (strDate != null && !strDate.equals("") && date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			Date newDate = df.parse(strDate);

			long l = date.getTime() - newDate.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);

			time = day * 24 + hour;
		} else {
			throw new IllegalArgumentException("输入日期参数有误！");
		}

		return time;
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] argus) throws ParseException {
		System.out.println(frontFullChineseDate());
		System.out.println(getCurrentYear());
		System.out.println(getCurrentMonth());
		System.out.println(string2DateLong("2013-07-15"));
		System.out.println(getFirstDayOfWeek(new Date()));
		System.out.println(getDayBegin(new Date()));

		System.out.println(shortDate(new Date()));
		System.out.println(dtSimpleFormat(new Date()));
		System.out.println(hmsFormat(new Date()));
		System.out.println(simpleFormat(new Date()));
		System.out.println(dtSimpleChineseFormat(new Date()));
		System.out.println(frontFullChineseDate(new Date()));

		System.out.println(string2Date("2013-03-04"));
		System.out.println(string2DateTime("2013-07-16 15:30:23"));
		System.out.println(chineseString2Date("2007年6月4日"));

		System.out.println(getBetweenDate(new Date(113, 05, 05), new Date()));
		System.out.println(getMonthFirstDay(new Date(), 3));
		System.out.println(getDiffMon(new Date(), -3));
		System.out.println(getDiffDate(new Date(), 5));
		System.out.println(getDiffTime(new Date(), 30));
		System.out.println(dateDiffDate("2007-06-04 15:30:23", new Date()));
		System.out.println(dateDiffHour("2007-06-04 15:30:23", new Date()));
	}

}
