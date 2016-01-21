package com.cn.exceldom.dom;

import java.util.HashMap;
import java.util.Map;

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
public class SpecialTable {
	private Map<String, String> tablename = new HashMap<String, String>();
	private Map<String, String> tablemap = new HashMap<String, String>();
	private String sequenceName = "";
	private String triggerName = "";

	public SpecialTable() {

	}

	public SpecialTable(Map<String, String> tablename,
			Map<String, String> tablemap) {
		this.tablename = tablename;
		this.tablemap = tablemap;
	}

	/**
	 * 拼接建表语句
	 * 
	 * @return
	 */
	public Map<String, String> createTable() {
		Map<String, String> sql = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		StringBuffer alert = new StringBuffer();
		StringBuffer index = new StringBuffer();
		String tableName = getTableName();
		String tableCNName = tablename.get("tableNameCN");
		String seq = tablename.get("sequence");
		String lenth = tablename.get("lenth");
		String model = tablename.get("createdId");
		String trgModel = tablename.get("trigger");
		String createSequence = "";
		String isCreatedDate = "";
		String id = "";
		String createTrigger = "";
		String dropSeq = "";
		String dropTriger = "";
		if (seq.equals("是")) {
			createSequence = "--创建序列\n" + createSequence(seq, tableName, lenth);
			dropSeq = dropSequence(sequenceName);
		}
		if (model.equals("是")) {
			isCreatedDate = "created_date date,\ncreated_user varchar2(200),\nupdated_date date,\nupdated_user varchar2(200)";
			id = "id number(" + lenth.split("\\.")[0] + ") primary key,\n";
		}
		if (trgModel.equals("是") && model.equals("是") && seq.equals("是")) {
			createTrigger = "--创建触发器\n"
					+ createTrigger(tableName, sequenceName, "id");
			dropTriger = dropTrigger(triggerName);
		}
		sb.append("--创建表" + tableCNName + "(" + tableName + ")" + "\n");
		sb.append("create table " + tableName + "\n(\n");
		if (!id.equals("")) {
			sb.append(id);
		}
		for (String key : tablemap.keySet()) {
			String[] fileds = key.split("\\|");
			// 字段名
			String fieldName = fileds[0];
			// 字段类型
			String type = fileds[1];
			String[] value = tablemap.get(key).split("\\|");
			// 描述*
			String descrption = value[0];
			// 是否主键
			String primaryKey = value[1];
			// 是否唯一
			String isOnly = value[2];
			// 是否可为空
			String canNull = value[3];
			// 默认值
			String defultValue = value[4];
			// 是否索引*
			String isFastQuery = value[5];
			if (fieldName.equals("id") && !id.equals("")) {
				continue;
			}
			if (descrption != null) {
				alert.append("comment on column " + tableName + "." + fieldName
						+ " is '" + descrption + "';\n");
			}
			if (primaryKey.equals("是")) {
				sb.append(fieldName + " " + type + " primary key,\n");
			} else {
				sb.append(fieldName + " " + type);
				if (isOnly.equals("是")) {
					sb.append(" unique");
				}
				if (canNull.equals("否")) {
					sb.append(" not null");
				}
				if (!defultValue.equals("无") && !defultValue.equals("")) {
					sb.append(" default " + defultValue);
				}
				sb.append(",\n");
				if (isFastQuery.equals("是") && !isOnly.equals("是")) {
					if (tableName.length() >= 25) {
						index.append("--创建列("
								+ fieldName
								+ ")索引\n create index "
								+ tableName.substring(0,
										tableName.length() - 10) + "_idx_"
								+ fieldName.substring(0, 5) + " on "
								+ tableName + " (" + fieldName + ");");
					} else {
						index.append("--创建列("
								+ fieldName
								+ ")索引\n create index "
								+ tableName
								+ "_idx_"
								+ (fieldName.length() <= 5 ? fieldName
										: fieldName.substring(0, 5)) + " on "
								+ tableName + " (" + fieldName + ");");
					}
				}
			}
		}
		alert.append("comment on table " + tableName + " is '" + tableCNName
				+ "';");
		if (isCreatedDate.equals("")) {
			String createSql = sb.substring(0, sb.toString().length() - 2);
			sql.put("createtable", createSql + "\n);");
		} else {
			sb.append("" + isCreatedDate);
			alert.append("\ncomment on column " + tableName
					+ ".created_date is '创建时间';\ncomment on column "
					+ tableName + ".created_user is '创建人';\ncomment on column "
					+ tableName
					+ ".updated_date is '修改时间';\ncomment on column "
					+ tableName
					+ ".updated_user is '修改人';\ncomment on column "
					+ tableName + ".id is '主键id';\n");
			sql.put("createtable", sb.toString() + "\n);");
		}
		sql.put("createsequence", createSequence);
		sql.put("comment", alert.toString());
		sql.put("index", index.toString());
		sql.put("trigger", createTrigger);
		sql.put("droptable", dropTable(tableName));
		sql.put("dropsequence", dropSeq);
		sql.put("droptrigger", dropTriger);
		sql.put("tablename", tableCNName);
		return sql;
	}

	/**
	 * 删除表的语句
	 * 
	 * @param tablename
	 * @return
	 */
	public String dropTable(String tablename) {
		return "--删除表" + tablename + "\ndrop table " + tablename + ";";
	}

	/**
	 * 删除序列
	 * 
	 * @param seq
	 * @return
	 */
	public String dropSequence(String seq) {
		return "--删除序列" + seq + "\ndrop sequence " + seq + ";";
	}

	/**
	 * 删除触发器
	 * 
	 * @param trigger
	 * @return
	 */
	public String dropTrigger(String trigger) {
		return "--删除触发器" + trigger + "\ndrop trigger " + trigger + ";";
	}

	/**
	 * 得到表名
	 * 
	 * @return
	 */
	public String getTableName() {
		return tablename.get("tableNameEN");
	}

	/**
	 * 创建序列
	 * 
	 * @param seq
	 * @param tableName
	 * @param lenth
	 * @return
	 */
	public String createSequence(String seq, String tableName, String lenth) {
		if (seq.equals("是")) {
			StringBuffer sb = new StringBuffer();
			if (tableName.length() > 20) {
				sb.append("create sequence " + tableName.substring(0, 20)
						+ "_seq \nminvalue 1 \nmaxvalue " + appendSeq(lenth)
						+ "\nstart with 1 \nincrement by 1\ncache 10;");
				sequenceName = tableName.substring(0, 20) + "_seq";
			} else {
				sb.append("create sequence " + tableName
						+ "_seq \nminvalue 1 \nmaxvalue " + appendSeq(lenth)
						+ "\nstart with 1 \nincrement by 1\ncache 10;");
				sequenceName = tableName + "_seq";
			}

			return sb.toString();
		} else {
			return "";
		}
	}

	/**
	 * 建立序列触发器
	 * 
	 * @param trigger
	 * @param tableName
	 * @param seqName
	 * @return
	 */
	public String createTrigger(String tableName, String seqName,
			String fieldValue) {
		StringBuffer sql = new StringBuffer();
		sql.append("create or replace trigger ");
		if (tableName.length() > 20) {
			sql.append(tableName.subSequence(0, 20) + "_seq_trg \n");
			sql.append("  before insert on " + tableName + "\n");
			sql.append("  for each row\n");
			sql.append("begin\n");
			sql.append("select " + seqName + ".nextval into :new.id from "
					+ tableName + ";\n");
			sql.append("end " + tableName.subSequence(0, 20) + "_seq_trg \n");
			triggerName = tableName.subSequence(0, 20) + "_seq_trg";
		} else {
			sql.append(tableName + "_seq_trg \n");
			sql.append("  before insert on " + tableName + "\n");
			sql.append("  for each row\n");
			sql.append("begin\n");
			sql.append("select " + seqName + ".nextval into :new." + fieldValue
					+ " from " + tableName + ";\n");
			sql.append("end " + tableName + "_seq_trg;\n");
			triggerName = tableName + "_seq_trg";
		}
		sql.append("/");
		return sql.toString();
	}

	/**
	 * 初始化序列长度
	 * 
	 * @param lenth
	 * @return
	 */
	public String appendSeq(String lenth) {
		StringBuffer s = new StringBuffer();
		if (lenth.contains(".")) {
			for (int i = 0; i < Integer.parseInt(lenth.split("\\.")[0]); i++) {
				s.append("9");
			}
		} else {
			for (int i = 0; i < Integer.parseInt(lenth); i++) {
				s.append("9");
			}
		}
		return s.toString();
	}
}
