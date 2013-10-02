package com.haolang.util.android.http;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 请求参数封装类
 * 
 * @author mb
 * 
 * @version 1.0
 * 
 * @author  yj
 * 
 * @version 1.1 2013.09.11 20:21 修改添加文件参数方法名
 */
public final class HLPara {

	private static XStream stream = null;
	public static final String ALIAS = "HLPara";

	private Map<String, Object> para;

	private List<File> fileList;

	static {
		stream = new XStream(new DomDriver());
		stream.alias(ALIAS, HLPara.class);
	}

	public Map<String, Object> getPara() {
		return para;
	}

	public void setPara(Map<String, Object> para) {
		this.para = para;
	}

	private HLPara() {
		this.para = new HashMap<String, Object>();
		this.fileList = new ArrayList<File>();
	}

	public static HLPara newInstance() {
		HLPara hlaPara = new HLPara();
		return hlaPara;
	}

	public HLPara addPara(String key, Object value) {
		para.put(key, value);
		return this;
	}

	public HLPara addParaFile(String key, File file) {
		fileList.add(file);
		return this;
	}

	public String toXml() {
		return stream.toXML(this);
	}

	public String compressXml(String xml) {
		String fileNode_s = "<fileList>";
		String fileNode_e = "</fileList>";
		String fileNode_c = "<fileList/>";

		Pattern p = Pattern.compile("\\s{2,}|\t|\r|\n");
		Matcher m = p.matcher(xml);
		xml = m.replaceAll("");

		int f_s = xml.indexOf(fileNode_s);
		int f_e = xml.indexOf(fileNode_e);

		if (f_s > -1 && f_e > -1) {
			String fileNode = xml.substring(f_s, f_e + fileNode_e.length());
			xml.replace(fileNode, fileNode_c);
		}

		return xml;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, Object> entry : this.para.entrySet()) {
			if (entry.getValue() == null) {
				sb.append(entry.getKey()).append(": null, ");
			} else {
				sb.append(entry.getKey()).append(": ").append(entry.getValue())
						.append(", ");
			}
		}

		for (File file : fileList) {
			sb.append("File: ").append(file.getName()).append(", ");
		}

		return sb.toString();
	}

	public List<File> getFiles() {
		return fileList;
	}

}
