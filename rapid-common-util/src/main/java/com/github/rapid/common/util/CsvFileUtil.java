package com.github.rapid.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
/**
 * 读取csv文件格式的工具类
 * @author badqiu
 *
 */
public class CsvFileUtil {
	
	/**
	 * 读取csv格式文件流为 List<Map>返回
	 * 
	 * @param reader 输入流
	 * @param columns 读回来的数据列名，将作为Map.key使用
	 * @param skipLines 跳过的行
	 * @return 
	 * @throws IOException
	 */
	public static List<Map> readCsv2Maps(BufferedReader reader, String columns,int skipLines) throws IOException {
		String line = null;
		int count = 0;
		String[] columnKeys = columns.split("[,\\s]+");
		List<Map> result = new ArrayList<Map>();
		while((line = reader.readLine()) != null) {
			count++;
			if(count <= skipLines)  {
				continue;
			}
			if(isCommentLine(line)) {
				continue;
			}
			Map row = MapUtil.toMap(line.split(","), columnKeys);
			result.add(row);
		}
		return result;
	}
	
	private static boolean isCommentLine(String line) {
		if(line != null && line.trim().startsWith("#")) {
			return true;
		}
		return false;
	}

	public static List<Map> readCsv2Maps(InputStream inputStream,String chartset, String columns,int skipLines) throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream,chartset));
			return readCsv2Maps(reader,columns,skipLines);
		}finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(inputStream);
		}
	}
	
}
