package com.github.rapid.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.github.rapid.common.beanutils.PropertyUtils;
import com.github.rapid.common.util.DateConvertUtil;
/**
 * 生成excel工具类
 * 
 * @author badqiu
 *
 */
public class ExcelGenerator {

	private HSSFWorkbook workbook = new HSSFWorkbook();
			
	private int headColCount = 0;
	private int dataColCount = 0;
	
	private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss"; //for date or timestamp
	private String dateFormat = "yyyy-MM-dd"; // for sql date
	private String timeFormat = "HH:mm:ss"; // for sql time
	
	private String numberFormat = "####.##";
	private String trueValue = "是";
	private String falseValue = "否";

	
	public void setDateTimeFormat(String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public void setNumberFormat(String numberFormat) {
		this.numberFormat = numberFormat;
	}

	public void setTrueValue(String trueValue) {
		this.trueValue = trueValue;
	}

	public void setFalseValue(String falseValue) {
		this.falseValue = falseValue;
	}
	
	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	/**
	 * 生成excel
	 * 
	 * @param sheetName 表格名称
	 * @param datas 数据行
	 * @param headMap 表头, key为data的属性，value为中文描述
	 * @param output 
	 */
	public void execute(String sheetName,List<? extends Object> datas,LinkedHashMap<String,String> headMap,OutputStream output) {
		try {
			createSheetRows(sheetName,datas, headMap);
			workbook.write(output);
		}catch(Exception e) {
			throw new RuntimeException("generate excel error",e);
		}
	}

	public HSSFSheet createSheetRows(String sheetName,List<? extends Object> datas,LinkedHashMap<String, String> headMap)  {
		HSSFSheet sheet = StringUtils.isBlank(sheetName) ? workbook.createSheet() : workbook.createSheet(sheetName);
		createHeadRow(headMap, sheet);
		createDataRows(datas, headMap, sheet);
		return sheet;
	}

	private void createDataRows(List datas,LinkedHashMap<String, String> headColumn, HSSFSheet sheet)  {
		for (int i = 0; i < datas.size(); i++) {
			Object data = datas.get(i);
			int rownum = i + 1;
			final HSSFRow row = sheet.createRow(rownum);
			headColumn.forEach((key,v) -> {
				try {
					Object cellValue = org.apache.commons.beanutils.PropertyUtils.getProperty(data, key);
					HSSFCell cell = row.createCell(dataColCount);
					setCellValue(cell,cellValue);
//					cell.setCellValue(formatCellValue(cellValue));
					dataColCount++;
				}catch(Exception e) {
					throw new RuntimeException("error on get property:"+key,e);
				}
			});
			dataColCount = 0;
		}
	}
	
	private void setCellValue(HSSFCell cell, Object cellValue) {
		if(cellValue instanceof Number) {
			DecimalFormat df = new DecimalFormat(numberFormat);
			String strVal = df.format((Number)cellValue);
			cell.setCellValue(Double.parseDouble(strVal));
		}else {
			String strVal = formatCellValue(cellValue);
			cell.setCellValue(strVal);
		}
	}

	private String formatCellValue(Object cellValue) {
		if(cellValue == null) return "";
		
		if(cellValue instanceof Date) {
			if(cellValue instanceof java.sql.Date) {
				return DateConvertUtil.format((Date)cellValue, dateFormat);
			}else if(cellValue instanceof java.sql.Time) {
				return DateConvertUtil.format((Date)cellValue, timeFormat);
			}
			return DateConvertUtil.format((Date)cellValue, dateTimeFormat);
		}else if (cellValue instanceof Number) {
			DecimalFormat df = new DecimalFormat(numberFormat);
			return df.format((Number)cellValue);
		}else if (cellValue instanceof Boolean) {
			Boolean b = (Boolean)cellValue;
			return b ? trueValue : falseValue;
		}
		
		return String.valueOf(cellValue);
	}
	
	private void createHeadRow(LinkedHashMap<String, String> headColumn,HSSFSheet sheet) {
		HSSFCellStyle style = createHeadCellStyle();
		HSSFRow row = sheet.createRow(0);
		
		headColumn.forEach((k,v) -> {
			HSSFCell cell = row.createCell(headColCount);         
			cell.setCellValue(v);                  
			cell.setCellStyle(style); 
			headColCount++;
		});
	}

	private HSSFCellStyle createHeadCellStyle() {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  //居中
		HSSFFont font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);
		return style;
	}


}
