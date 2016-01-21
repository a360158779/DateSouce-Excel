package com.cn.exceldom.util;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * 
 * 
 * <pre>
 * HISTORY
 * ****************************************************************************
 *  ID   DATE            PERSON         REASON
 *  1    2015-12-21		sfit1087         Create
 * ****************************************************************************
 * </pre>
 * 
 * @author sfit1087
 * @since 1.0
 */
public class ExcelUtil {
	/**
	 * 取值
	 * 
	 * @param hssfCell
	 * @return
	 */
	@SuppressWarnings({ "static-access" })
	public static String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
}
