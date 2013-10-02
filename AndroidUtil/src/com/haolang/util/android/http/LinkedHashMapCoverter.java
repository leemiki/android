package com.haolang.util.android.http;

import java.util.LinkedHashMap;

import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * LinkedHashMap类型转化
 * 
 * @author ghl
 * 
 * @version 1.0
 * 
 * @author yj
 * 
 * @version 1.1 2013.09.05 10:21 修改类权限为包可见
 */
class LinkedHashMapCoverter extends MapConverter {

	public LinkedHashMapCoverter(Mapper mapper) {
		super(mapper);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return LinkedHashMap.class == type;
	}

}
