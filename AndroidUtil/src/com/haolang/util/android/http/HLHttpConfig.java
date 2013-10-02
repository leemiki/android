package com.haolang.util.android.http;

/**
 * HTTP通信配置
 * 
 * @author hph
 * 
 * @version 1.0
 */
public final class HLHttpConfig {

	private static final int DEFAULTCONNECTTIMEOUT = 15000;// 默认连接超时时间
	private static final int DEFAULTSOTIMEOUT = 15000;// 默认获取数据超时时间

	private int connectTimeout = DEFAULTCONNECTTIMEOUT;// 连接超时时间
	private int soTimeout = DEFAULTSOTIMEOUT;// 获取数据超时时间
	private String charSet = "utf-8";
	private boolean dataCompressed = true;

	/**
	 * 构造函数，设置连接超时时间和获取数据超时时间为默认值
	 */
	public HLHttpConfig() {

	}

	/**
	 * 构造函数，可以设置连接超时时间
	 * 
	 * @param connectionTimeout
	 *            连接超时时间
	 *            
	 * @throws IllegalArgumentException
	 *             当connectionTimeout < 0时，抛出此异常
	 */
	public HLHttpConfig(int connectionTimeout) {
		this.setConnectionTimeout(connectionTimeout);
	}

	/**
	 * 构造函数，可以设置连接超时时间和获取数据超时时间
	 * 
	 * @param connectionTimeout
	 *            连接超时时间
	 * @param soTimeout
	 *            数据获取超时时间
	 *            
	 * @throws IllegalArgumentException
	 *             当connectionTimeout < 0或者soTimeout < 0时，抛出此异常
	 */
	public HLHttpConfig(int connectionTimeout, int soTimeout) {
		this.setConnectionTimeout(connectionTimeout);
		this.setSoTimeout(soTimeout);
	}

	/**
	 * 设置与服务器的连接时的超时时间
	 * 
	 * @param t
	 *            连接超时时间（默认15s）
	 *            
	 * @return 当前类自身，不会为null
	 * 
	 * @throws IllegalArgumentException
	 *             当t < 0 时会抛出此异常
	 */
	public HLHttpConfig setConnectionTimeout(int t) {
		if (t < 0) {
			throw new IllegalArgumentException();
		} else {
			this.connectTimeout = t;
			return this;
		}
	}

	/**
	 * 设置向服务器获取数据时的超时时间
	 * 
	 * @param t
	 *            获取数据超时时间（默认15s）
	 *            
	 * @return 当前类自身，不会为null
	 * 
	 * @throws IllegalArgumentException
	 *             当t < 0 时会抛出此异常
	 */
	public HLHttpConfig setSoTimeout(int t) {
		if (t < 0) {
			throw new IllegalArgumentException();
		} else {
			this.soTimeout = t;
			return this;
		}
	}

	public HLHttpConfig setCharSet(String charSet) {
		if (charSet != null && charSet.trim().length() > 0) {
			this.charSet = charSet;
		}
		return this;
	}

	/**
	 * 获得连接超时设置
	 * 
	 * @return 连接超时时间（以毫秒为单位）
	 */
	public int getConnectionTimeout() {
		return this.connectTimeout;
	}

	/**
	 * 获得服务器获取数据超时时间
	 * 
	 * @return 获取数据超时时间（以毫秒为单位）
	 */
	public int getSoTimeout() {
		return this.soTimeout;
	}

	public String getCharSet() {
		return this.charSet;
	}

	public boolean isDataCompressed() {
		return dataCompressed;
	}

	public void setDataCompressed(boolean dataCompressed) {
		this.dataCompressed = dataCompressed;
	}

}
