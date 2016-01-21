package com.cn.exceldom.dom;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.cn.exceldom.util.ExcelUtil;

/**
 * 描述：
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
public class DomTable {

	/**
	 * 当row=3时取表名 先取表名
	 * 
	 * @param row
	 * @return
	 */
	public static Map<String, String> domFirstRow(Row row) {
		Map<String, String> map = new HashMap<String, String>();
		Cell cell = row.getCell(1);
		Cell cell2 = row.getCell(2);
		if (cell != null) {
			String tableNameEN = ExcelUtil.getValue((HSSFCell) cell);
			String tableNameCN = ExcelUtil.getValue((HSSFCell) cell2);
			map.put("tableNameEN", tableNameEN.toLowerCase());
			map.put("tableNameCN", tableNameCN);
		}
		return map;
	}

	/**
	 * 当row=4时取是否自动建立序列的值
	 * 
	 * @param row
	 * @return
	 */
	public static Map<String, String> domSecondRow(Row row) {
		Map<String, String> map = new HashMap<String, String>();
		Cell cell = row.getCell(1);
		if (cell != null) {
			String createSeq = ExcelUtil.getValue((HSSFCell) cell);
			map.put("sequence", createSeq);
		}
		return map;
	}

	/**
	 * 当row=5时取是否是否需要id触发器
	 * 
	 * @param row
	 * @return
	 */
	public static Map<String, String> domThirdRow(Row row) {
		Map<String, String> map = new HashMap<String, String>();
		Cell cell = row.getCell(1);
		if (cell != null) {
			String createTrigger = ExcelUtil.getValue((HSSFCell) cell);
			map.put("trigger", createTrigger);
		}
		return map;
	}

	/**
	 * 当row=6时取是否是否需要id
	 * 
	 * @param row
	 * @return
	 */
	public static Map<String, String> domForthRow(Row row) {
		Map<String, String> map = new HashMap<String, String>();
		Cell cell = row.getCell(1);
		if (cell != null) {
			String isCreated = ExcelUtil.getValue((HSSFCell) cell);
			map.put("createdId", isCreated);
		}
		return map;
	}

	/**
	 * 当row=7时取是否是否需要id
	 * 
	 * @param row
	 * @return
	 */
	public static Map<String, String> domFifthRow(Row row) {
		Map<String, String> map = new HashMap<String, String>();
		Cell cell = row.getCell(1);
		if (cell != null) {
			String lenth = ExcelUtil.getValue((HSSFCell) cell);
			map.put("lenth", lenth);
		}
		return map;
	}

	/**
	 * 当row=9时开始循环取表
	 * 
	 * @param row
	 * @return
	 */
	public static Map<String, String> domTableAll(Row row) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < row.getLastCellNum(); i++) {
			// 字段名
			String fieldName = ExcelUtil.getValue((HSSFCell) row.getCell(0));
			// 字段类型
			String type = ExcelUtil.getValue((HSSFCell) row.getCell(1));
			// 描述
			String descrption = (row.getCell(2) == null ? "无" : ExcelUtil
					.getValue((HSSFCell) row.getCell(2)));
			// 是否主键
			String primaryKey = (row.getCell(3) == null ? "否" : ExcelUtil
					.getValue((HSSFCell) row.getCell(3)));
			// 是否唯一
			String isOnly = (row.getCell(4) == null ? "否" : ExcelUtil
					.getValue((HSSFCell) row.getCell(4)));
			// 是否可为空
			String canNull = (row.getCell(5) == null ? "是" : ExcelUtil
					.getValue((HSSFCell) row.getCell(5)));
			// 默认值
			String defultValue = (row.getCell(6) == null ? "无" : ExcelUtil
					.getValue((HSSFCell) row.getCell(6)));
			// 是否索引
			String isFastQuery = (row.getCell(7) == null ? "否" : ExcelUtil
					.getValue((HSSFCell) row.getCell(7)));
			map.put(fieldName.toLowerCase() + "|" + type, descrption + "|"
					+ primaryKey + "|" + isOnly + "|" + canNull + "|"
					+ defultValue + "|" + isFastQuery);
		}
		return map;
	}
}
