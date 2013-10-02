package com.haolang.util.android.http;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * 数据类型转化
 * 
 * @author ghl
 * 
 * @version 1.0
 * 
 * @author yj
 * 
 * @version 1.1 2013.09.05 10:21 修改类权限为包可见
 */
class DataConverter implements Converter {

	private static DateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class arg0) {
		return Date.class == arg0;
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext arg2) {
		Date calendar = (Date) value;
		writer.setValue(formatter.format(calendar));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext arg1) {
		GregorianCalendar calendar = new GregorianCalendar();

		try {
			calendar.setTime(formatter.parse(reader.getValue()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return calendar.getTime();
	}

}
