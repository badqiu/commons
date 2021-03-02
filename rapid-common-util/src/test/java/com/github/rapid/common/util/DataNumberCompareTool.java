package com.github.rapid.common.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

public class DataNumberCompareTool {

	public Map parseCsvDataLine(String line,int groupByKeyNumber) {
		String[] array = line.split("[\\s,]+");
		Map map = new LinkedHashMap(array.length * 2);
		for(int i = 0; i < array.length; i++) {
			if(i + 1 > groupByKeyNumber) {
				map.put(mapKey(i), Double.parseDouble(array[i]));
			}else {
				map.put(mapKey(i), array[i]);
			}
		}
		return map;
	}

	private String mapKey(int i) {
		return "f"+i;
	}
	
	public List<Map> parseMultiLineCsvDataLine(String content,int groupByKeyNumber) {
		List<String> lines = toLines(content);
		List<Map> results = new ArrayList<Map>();
		for(String line : lines) {
			if(StringUtils.isBlank(line)) continue;
			
			Map map = parseCsvDataLine(line,groupByKeyNumber);
			results.add(map);
		}
		return results;
	}

	private List<String> toLines(String content) {
		try {
			return IOUtils.readLines(new StringReader(content));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Map<String,Map<String, NumberCompareResult>> compareTwoList(List<Map> rows1, List<Map> rows2,int groupByKeyNumber) {
		Map m1 = toMap(rows1,groupByKeyNumber);
		Map m2 = toMap(rows2,groupByKeyNumber);
		
		Map<String,Map<String, NumberCompareResult>> result = new LinkedHashMap();
		m1.forEach((k,v) -> {
			Map otherRow = (Map)m2.get(k);
			Map<String, NumberCompareResult> compareTowMap = compareTowMap((Map)v,otherRow,groupByKeyNumber);
			result.put((String)k, compareTowMap);
			
//			System.out.print(k+"   =  ");
//			System.out.print(compareTowMap);
//			System.out.println();
		});
		return result;
	}
	
	private Map<String,NumberCompareResult> compareTowMap(Map map1, Map map2,int groupByKeyNumber) {
		if(map1 == null || map2 == null) {
			return null;
		}
		Map result = new LinkedHashMap();
		for(int i = groupByKeyNumber; i < map1.size(); i++) {
			String valueKey = mapKey(i);
			Double v1 = (Double)map1.get(valueKey);
			Double v2 = (Double)map2.get(valueKey);
			NumberCompareResult r = new NumberCompareResult(v1,v2);
			result.put(valueKey, r);
		}
		return result;
	}

	private void compareTwoNumber(Double v1, Double v2) {
		
	}

	private Map<String,Map> toMap(List<Map> rows, int groupByKeyNumber) {
		Map result = new LinkedHashMap();
		for(Map row : rows) {
			List keys = new ArrayList<String>();
			for(int i = 0; i < groupByKeyNumber; i++) {
				keys.add(row.get(mapKey(i)));
			}
			result.put(StringUtils.join(keys, "__"), row);
		}
		return result;
	}



	@Test
	public void testParser() {
		int groupByKeyNumber = 1;
		List<Map> rows1 = parseMultiLineCsvDataLine("android 1 1\nios 2 2",groupByKeyNumber);
		List<Map> rows2 = parseMultiLineCsvDataLine("android 1 1.1\nios 2 2.1",groupByKeyNumber);
		printMap(compareTwoList(rows1,rows2,groupByKeyNumber));
//		printRows(rows1);
//		printRows(rows2);
	}

	@Test
	public void testParserFile() throws Exception {
		int groupByKeyNumber = 2;
		List<Map> rows1 = parseMultiLineCsvDataLine(readFileToString("classpath:compare_test/left.txt"),groupByKeyNumber);
		List<Map> rows2 = parseMultiLineCsvDataLine(readFileToString("classpath:compare_test/right.txt"),groupByKeyNumber);
		printCompareResult(compareTwoList(rows1,rows2,groupByKeyNumber));
//		printRows(rows1);
//		printRows(rows2);
	}

	private String readFileToString(String location) throws IOException, FileNotFoundException {
		return FileUtils.readFileToString(ResourceUtils.getFile(location));
	}
	
	private void printCompareResult(Map<String,Map<String, NumberCompareResult>> map) {
		map.forEach((k,v) -> {
			Collection<NumberCompareResult> values = v == null ? null : v.values();
			System.out.println(StringUtils.leftPad(k,50)+" = "+values);
		});
	}
	
	private void printMap(Map map) {
		map.forEach((k,v) -> {
			System.out.println(k+"\t="+v);
		});
	}
	
	private void printRows(List rows) {
		for(Object row : rows) {
			System.out.println(row);
		}
	}
	
	static class NumberCompareResult {
		Double left;
		Double right;
		
		public NumberCompareResult(Double left, Double right) {
			super();
			this.left = left;
			this.right = right;
		}

		public Double getGap() {
			if(left == null || right == null) {
				return null;
			}
			return left - right;
		}
		
		public Double getGapPercent() {
			if(left == null || right == null) {
				return null;
			}
			Double result =  getGap() / left;
			return result * 100;
		}
		
		public static String formatNumber(Number n) {
			DecimalFormat df = new DecimalFormat("##.##");
			return df.format(n);
		}
		
		public String toString() {
			String prefix = "left:"+formatNumber(left)+" right:"+formatNumber(right)+" gap:"+formatNumber(getGap());
			return StringUtils.rightPad(prefix,60)+"gapPercent:"+formatNumber(getGapPercent())+"%";
		}
	}
	
}
