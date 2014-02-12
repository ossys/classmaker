package com.ossys.classmaker.dbgenerator.tablemaker;

import java.util.ArrayList;

public class MySqlForeignKeyGenerator {

	private StringBuilder sb = new StringBuilder();
	private String name = "";
	private String schema_name = "";
	private String referring_table_name = "";
	private String referred_table_name = "";
	private ArrayList<String> referring_attribute_names = new ArrayList<String>();
	private ArrayList<String> referred_attribute_names = new ArrayList<String>();
	
	public static enum ForeignKeyConstraintType {
		NO_ACTION,
		CASCADE,
		SET_NULL,
		RESTRICT
	}
	protected ForeignKeyConstraintType onUpdate = null;
	protected ForeignKeyConstraintType onDelete = null;
	
	public MySqlForeignKeyGenerator(String name, String schema_name) {
		this.name = name;
		this.schema_name = schema_name;
	}
	
	public void setReferringTableName(String referring_table_name) {
		this.referring_table_name = referring_table_name;
	}
	
	public void setReferredTableName(String referred_table_name) {
		this.referred_table_name = referred_table_name;
	}
	
	public void addReferringAttributeName(String referring_attribute_name) {
		this.referring_attribute_names.add(referring_attribute_name);
	}
	
	public void addReferredAttributeName(String referred_attribute_name) {
		this.referred_attribute_names.add(referred_attribute_name);
	}
	
	public void setOnDelete(ForeignKeyConstraintType onDelete) {
		this.onDelete = onDelete;
	}
	
	public void setOnUpdate(ForeignKeyConstraintType onUpdate) {
		this.onUpdate = onUpdate;
	}
	
	public String getDDL() {
		this.sb.append("ALTER TABLE `" + this.schema_name + "`.`" + this.referring_table_name + "` ADD CONSTRAINT `" + this.name + "` ");
		this.sb.append("FOREIGN KEY `" + this.name + "`(");
		int cnt = 0;
		for(String referring_attribute_name : this.referring_attribute_names) {
			if(cnt>0) {
				this.sb.append(",");
			}
			this.sb.append("`" + referring_attribute_name + "`");
			cnt++;
		}
		this.sb.append(") REFERENCES `" + this.referred_table_name + "`(");
		cnt = 0;
		for(String referred_attribute_name : this.referred_attribute_names) {
			if(cnt>0) {
				this.sb.append(",");
			}
			this.sb.append("`" + referred_attribute_name + "`");
			cnt++;
		}
		this.sb.append(") ");
		
		this.sb.append("ON DELETE ");
		switch(this.onDelete) {
			case CASCADE:
				this.sb.append("CASCADE ");
				break;
			case NO_ACTION:
				this.sb.append("NO ACTION ");
				break;
			case SET_NULL:
				this.sb.append("SET NULL ");
				break;
			case RESTRICT:
				this.sb.append("RESTRICT ");
				break;
		}
		
		this.sb.append("ON UPDATE ");
		switch(this.onUpdate) {
			case CASCADE:
				this.sb.append("CASCADE");
				break;
			case NO_ACTION:
				this.sb.append("NO ACTION");
				break;
			case SET_NULL:
				this.sb.append("SET NULL");
				break;
			case RESTRICT:
				this.sb.append("RESTRICT");
				break;
		}
		
		return this.sb.toString();
	}
	
}
