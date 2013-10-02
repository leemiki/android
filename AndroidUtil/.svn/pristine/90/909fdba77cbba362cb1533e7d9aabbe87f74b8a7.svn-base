package com.haolang.util.android.http;

/**
 * 请求名本地映射过滤
 * 
 * @author mb
 * 
 * @version 1.0 2012-7-11 11:31:18
 * 
 * @author yj
 * 
 * @version 1.1 2013.09.05 10:21 修改类权限为包可见
 */
class ResponseFilter {

	private static String FLAG_NO_ACTION_MAPPED = "There is no Action mapped";

	static boolean isNeedFilter(String response) {
		return !(response == null || response.trim() == "" || response
				.indexOf("HLResult") > -1);
	}

	static String replaceResponse(String response) {
		if (!isNeedFilter(response)) {
			return response;
		}

		if (response.indexOf("There is no Action mapped") > 0) {
			response = repleaceNoMapedAction(response);
		}

		return response;
	}

	private static String repleaceNoMapedAction(String response) {
		try {
			int s = response.indexOf(FLAG_NO_ACTION_MAPPED);
			int e = response.indexOf("</");

			response = response.substring(s);
			response = response.substring(0, e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

}
