package com.github.rapid.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;

import io.swagger.annotations.ApiModelProperty;
import jakarta.servlet.http.HttpServletResponse;


public class EasyExcelUtil {
	
	private static ExcelTypeEnum excelType = ExcelTypeEnum.XLS;
	private static boolean swaggerDocApiAvaiable = false;
	static {
		try {
			Class.forName("io.swagger.annotations.ApiModelProperty");
			swaggerDocApiAvaiable = true;
		} catch (ClassNotFoundException e) {
			//ignore
		}
	}
	
	public static <T> void  writeExcel2Response(HttpServletResponse response,List<T> list,Class<T> head)  {
		LinkedHashMap<String, String> headMap = getHeadMapByClass(head);
		writeExcel2Response(response,list,head.getSimpleName(),headMap);
	}

	public static <T> void  writeExcel2Response(HttpServletResponse response,List<T> list,String filenamePrefix,LinkedHashMap<String, String> head)  {
		setResponseHeaders(response, filenamePrefix);
		
		OutputStream outputStream = null;
		try {
	        outputStream = response.getOutputStream();
			buildExcelWriterSheetBuilder(outputStream,head).doWrite(list);
		}catch(IOException e) {
			throw new RuntimeException("writeExcel2Response error,head:"+head,e);
		}finally {
			IOUtils.closeQuietly(outputStream);
		}
	}
	
	private static <T> void setResponseHeaders(HttpServletResponse response, String filenamePrefix) {
		String date = DateConvertUtil.format(new Date(), "yyyyMMdd_HHmmss");
		String fileExt = excelType.name().toLowerCase();
		String finalFileName = filenamePrefix + "_" + date + "." + fileExt;
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + finalFileName);
	}
	
	public static <T> ExcelWriterSheetBuilder buildExcelWriterSheetBuilder(OutputStream outputStream,LinkedHashMap<String, String> head) {
		List<List<String>> headList = getEasyExcelHeadByMap(head);
		return EasyExcel
			.write(outputStream)
			.head(headList)
			.inMemory(true)
			.autoTrim(true)
	        .excelType(excelType)
	        .sheet("sheet1");
	}
	
	
	public static List<List<String>> getEasyExcelHeadByMap(Map<String, String> apiModelProperties) {
		List<List<String>> head = new ArrayList<List<String>>();
		apiModelProperties.forEach((key,value) -> {
			head.add(Arrays.asList(value));
		});
		return head;
	}
	
	public static LinkedHashMap<String,String> getHeadMapByClass(Class<?> clazz) {
		LinkedHashMap<String, String> apiModelProperties = new LinkedHashMap<String, String>();  
        // 遍历TaskLog类的所有字段  
        Field[] fields = clazz.getDeclaredFields();  
        for (Field field : fields) {  
        	if(Modifier.isStatic(field.getModifiers())) {
        		continue;
        	}
        	
        	String fieldName = field.getName();
            String fieldDesc = getFieldDesc(field, fieldName);
            apiModelProperties.put(fieldName, fieldDesc);  
        } 
        return apiModelProperties;
	}

	private static String getFieldDesc(Field field, String fieldDefaultDesc) {
		
		String fieldDesc = fieldDefaultDesc;
		
		if(swaggerDocApiAvaiable) {
			ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);  
			if (apiModelProperty != null) {  
				String tempFieldDesc = apiModelProperty.value();
				if(StringUtils.isNotBlank(tempFieldDesc)) {
					fieldDesc = tempFieldDesc;
				}
			}
		}
		
		ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);  
		if(excelProperty != null && ArrayUtils.isNotEmpty(excelProperty.value())) {
			String tempFieldDesc = excelProperty.value()[0];
			if(StringUtils.isNotBlank(tempFieldDesc)) {
				fieldDesc = tempFieldDesc;
			}
		}
		
		return fieldDesc;
	}
	
}
