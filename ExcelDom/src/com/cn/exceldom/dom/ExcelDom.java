package com.cn.exceldom.dom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

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
public class ExcelDom {
	private static Map<String, String> finalMap = new HashMap<String, String>();
	private static String rootPath = "";

	public static void setRootPath() throws UnsupportedEncodingException {
		String rootPath2 = ExcelDom.class
				.getResource("/")
				.getFile()
				.toString()
				.substring(
						1,
						ExcelDom.class.getResource("/").getFile().toString()
								.indexOf("ExcelDom"));
		rootPath = rootPath2;
	}

	static JOptionPane pane = new JOptionPane();

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try {
			setRootPath();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			readExcel();
			String fileName = "";
			String fileString = "";
			for (String key : finalMap.keySet()) {
				if (key.equals("createtable")) {
					fileName = "V0.1-01__新建" + finalMap.get("tablename") + "表";
					fileString = finalMap.get(key) + "\n"
							+ finalMap.get("comment") + "\n"
							+ finalMap.get("index");
					newFile(rootPath + "/SQL Script/", fileName, fileString);
				} else if (key.equals("createsequence")
						&& finalMap.get(key) != null
						&& !finalMap.get(key).equals("")) {
					fileName = "V0.1-02__新建" + finalMap.get("tablename")
							+ "表id序列";
					fileString = finalMap.get("createsequence");
					newFile(rootPath + "/SQL Script/", fileName, fileString);
				} else if (key.equals("trigger") && finalMap.get(key) != null
						&& !finalMap.get(key).equals("")) {
					fileName = "V0.1-03__新建" + finalMap.get("tablename")
							+ "表id触发器";
					fileString = finalMap.get("trigger");
					newFile(rootPath + "/SQL Script/", fileName, fileString);
				} else if (key.equals("droptable")) {
					fileName = "V0.1-00__rollback";
					fileString = finalMap.get("droptable")
							+ "\n"
							+ (finalMap.get("dropsequence") == null
									|| finalMap.get("dropsequence").equals("") ? ""
									: finalMap.get("dropsequence"))
							+ "\n"
							+ (finalMap.get("droptrigger") == null
									|| finalMap.get("droptrigger").equals("") ? ""
									: finalMap.get("droptrigger"));
					newFile(rootPath + "/SQL Script/", fileName, fileString);
				}
			}
			pane.showMessageDialog(pane, "生成脚本完成!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pane.showMessageDialog(pane, e.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pane.showMessageDialog(pane, e.toString());
		}
	}

	private static Map<String, String> tablename = new HashMap<String, String>();
	private static Map<String, String> tablemap = new HashMap<String, String>();

	public static void readExcel() throws IOException {
		InputStream is = null;
		HSSFWorkbook hssfWorkbook = null;
		is = new FileInputStream(rootPath + "excl.xls");
		hssfWorkbook = new HSSFWorkbook(is);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
		for (int i = 3; i <= hssfSheet.getLastRowNum(); i++) {
			Row row = hssfSheet.getRow(i);
			if (row == null) {
				continue;
			}
			if (i == 3) {
				tablename.putAll(DomTable.domFirstRow(row));
			}
			if (i == 4) {
				tablename.putAll(DomTable.domSecondRow(row));
			}
			if (i == 5) {
				tablename.putAll(DomTable.domThirdRow(row));
			}
			if (i == 6) {
				tablename.putAll(DomTable.domForthRow(row));
			}
			if (i == 7) {
				tablename.putAll(DomTable.domFifthRow(row));
			}
			if (i >= 9) {
				tablemap.putAll(DomTable.domTableAll(row));
			}
		}
		SpecialTable specialTable = new SpecialTable(tablename, tablemap);
		finalMap = specialTable.createTable();
		if (is != null) {
			is.close();
		}
		if (hssfWorkbook != null) {
			hssfWorkbook.close();
		}
	}

	/**
	 * 将字符串写人文件
	 * 
	 * @param filePath
	 * @param fileName
	 * @param FileString
	 * @throws IOException
	 */
	public static void newFile(String filePath, String fileName,
			String fileString) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(filePath + fileName + ".sql"));
			fos.write(fileString.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
