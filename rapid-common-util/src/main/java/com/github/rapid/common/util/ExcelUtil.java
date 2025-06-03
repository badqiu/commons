package com.github.rapid.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * 读取excel文件工具类，
 * 读取时支持注释，以#号开头，会忽略该行数据
 * 
 * @author badqiu
 *
 */
public class ExcelUtil {

	public static List<Map> readExcelData(InputStream input,String columns,int skipLines)throws Exception{ 
		try {
	        List<List<String>> list = readExcelData(input);
			return toMaps(columns, skipLines, list);
		}finally {
			IOUtils.closeQuietly(input);
		}
    }

	private static List<Map> toMaps(String columns, int skipLines, List<List<String>> list) {
		int count = 0;
		String[] columnKeys = columns.split("[,\\s]+");
		List<Map> resultMaps = new ArrayList<Map>();
		for(List<String> row : list){
			count++;
			if(count <= skipLines)  {
				continue;
			}
			Map map = MapUtil.toMap(row.toArray(new String[row.size()]), columnKeys);
			resultMaps.add(map);
		}
		return resultMaps;
	}
	
	public static List<List<String>> readExcelData(InputStream input)throws Exception{
		Workbook workBook = newWebbook(input);
		
        try {
	        int numberOfSheets = workBook.getNumberOfSheets();  
	        
	        List<List<String>> resultList = new ArrayList<List<String>>(); 
	        for(int sheetNum = 0; sheetNum < numberOfSheets; sheetNum++){  
	            Sheet sheet = workBook.getSheetAt(sheetNum);  
	            resultList.addAll(readSheet(sheet));  
	        }
	        return resultList;  
        }finally {
        	workBook.close();
        }
    }

	private static Workbook newWebbook(InputStream input) throws IOException {
		Workbook workBook = null;
		byte[] inputBytes = IOUtils.toByteArray(input);
		try {
			workBook = new HSSFWorkbook(new ByteArrayInputStream(inputBytes));  
		}catch(Exception e) {
			workBook = new XSSFWorkbook(new ByteArrayInputStream(inputBytes));
		}
		return workBook;
	}

	private static List<List<String>> readSheet(Sheet sheet) {
		List<List<String>> resultList = new ArrayList<List<String>>();  
		for(int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++){  
			Row row = sheet.getRow(i);  
			
			if(isCommentLine(row)) {
				continue;
			}
			
			List<String> listRow = readCells(row);
		    resultList.add(listRow);
		}
		return resultList;
	}

	private static List<String> readCells(Row row) {
		List<String> listRow = new ArrayList<String>();
		for(int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++){  
		    Cell cell = row.getCell(j);
		    String stringCellValue = getStringCellValue(cell);
			listRow.add(stringCellValue);  
		}
		return listRow;
	}

	private static boolean isCommentLine(Row row) {
		String firstCell = getStringCellValue(row.getCell(row.getFirstCellNum()));
		if(firstCell != null && firstCell.trim().startsWith("#")) {
			return true;
		}
		return false;
	}

	private static String getStringCellValue(Cell cell) {
		Object cellValue = getCellValue(cell);
		if(cellValue instanceof Number) {
			DecimalFormat numberFormat = new DecimalFormat("######.####");
			return numberFormat.format(cellValue);
		}
		return cellValue == null ? null : String.valueOf(cellValue);
	}

	private static Object getCellValue(Cell cell) {
		if(cell == null) return null;
		return cell.toString();
//		switch (cell.getCellType()) {
//		case Cell.:
//			return cell.getStringCellValue();
//		case Cell.CELL_TYPE_NUMERIC:
//			return cell.getNumericCellValue();
//		case Cell.CELL_TYPE_BLANK:
//			return null;
//		case Cell.CELL_TYPE_BOOLEAN:
//			return cell.getBooleanCellValue();
//		case Cell.CELL_TYPE_ERROR:
////			return cell.getErrorCellValue();
//			return null;
//		case Cell.CELL_TYPE_FORMULA:
//			try {
//				return cell.getNumericCellValue();
//			}catch(Exception e) {
//				return null;
//			}
//		}
//		return cell.toString();
	}  
	
}
