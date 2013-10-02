package com.haolang.util.android;

import android.util.Log;

/**
 * Log日志工具类	
 * <p>
 * v1.0支持android自带log机制
 * </p>
 * <p>
 * v1.0支持各log等级的null值处理
 * </p>
 * 
 * @author mb
 * 
 * @version 1.0
 * 
 * @author yj
 * 
 * @version 1.1 2013.07.19 16:06 重构
 */
public class AndroidLog {
	
	private static int logLevel = 0;
	
    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;
    
    /**
     * Log nothing.
     */
    public static final int NOLOG = 9;
	
	/**
	 * set log level. Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, Log.ERROR
	 * @param level
	 */
	public static void setLogLevel(int level) {
		logLevel = level;
	}
	
	public static int getLogLevel() {
		return logLevel;
	}
	
    /**
     * Send a {@link #VERBOSE} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg) {
    	msg = nullToString(msg);
    	
    	return logLevel>VERBOSE ? 0 : Log.v(tag, msg);
    }
    
    /**
     * Send a {@link #VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int v(String tag, String msg, Throwable tr) {
    	msg = nullToString(msg);
    	
    	return logLevel>VERBOSE ? 0 : Log.v(tag, msg, tr);
    }


    /**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
    	msg = nullToString(msg);
    	
    	return logLevel>DEBUG ? 0 : Log.d(tag, msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int d(String tag, String msg, Throwable tr) {
    	msg = nullToString(msg);
    	
    	return logLevel>DEBUG ? 0 : Log.d(tag, msg, tr);
    }
    
    /**
     * Send an {@link #INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) {
    	msg = nullToString(msg);
    	
    	return logLevel>DEBUG ? 0 : Log.i(tag, msg);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int i(String tag, String msg, Throwable tr) {
    	msg = nullToString(msg);
    	
    	return logLevel>DEBUG ? 0 : Log.i(tag, msg, tr);
    }
    
    /**
     * Send a {@link #WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
    	msg = nullToString(msg);
    	
    	return logLevel>DEBUG ? 0 : Log.w(tag, msg);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int w(String tag, String msg, Throwable tr) {
    	msg = nullToString(msg);
    	
    	return logLevel>DEBUG ? 0 : Log.w(tag, msg, tr);
    }
    
    /**
     * Send an {@link #ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
    	msg = nullToString(msg);
    	
    	return logLevel>ERROR ? 0 : Log.e(tag, msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static int e(String tag, String msg, Throwable tr) {
    	msg = nullToString(msg);
    	
    	return logLevel>ERROR ? 0 : Log.e(tag, msg, tr);
    }    
    
    private static String nullToString(String msg) {
    	return msg==null ? "null" : msg;
    }
    
}
