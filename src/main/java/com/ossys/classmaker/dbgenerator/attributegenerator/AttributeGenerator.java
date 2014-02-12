package com.ossys.classmaker.dbgenerator.attributegenerator;

import java.util.ArrayList;

public class AttributeGenerator {

	protected String name = "";
	protected boolean nullable = false;
	protected boolean signed = false;
	protected boolean primary_key = false;
	protected boolean auto_increment = false;
	protected double max_length = 0;
	protected long precision = 0;
	protected ArrayList<String> enums = new ArrayList<String>();
	
	public static enum DataType {
		NUM,
		TEXT,
		DATE,
		ENUM,
		BINARY,
		BOOLEAN
	}
	protected DataType dataType = null;
	
	public AttributeGenerator(String name) {
		this.name = AttributeGenerator.getAttributeName(name);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setMaxLength(double max_length) {
		this.max_length = max_length;
	}
	
	public double getMaxLength() {
		return this.max_length;
	}
	
	public void setPrecision(long precision) {
		this.precision = precision;
	}
	
	public long getPrecision() {
		return this.precision;
	}
	
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public DataType getDataType() {
		return this.dataType;
	}
	
	public void addEnum(String e) {
		this.enums.add(e);
	}
	
	public void setNullable() {
		this.nullable = true;
	}
	
	public void setSigned() {
		this.signed = true;
	}
	
	public void setPrimaryKey() {
		this.primary_key = true;
	}
	
	public boolean isPrimaryKey() {
		return this.primary_key;
	}
	
	public void setAutoIncrement() {
		this.auto_increment = true;
	}
	
	public static String getAttributeName(String name) {
		StringBuilder sb = new StringBuilder();
		
		String[] parts = name.split(" ");
		int cnt = 0;
		
		for(String s : parts) {
			if(cnt > 0) {
				sb.append("_");
			}
			sb.append(s.toLowerCase());
			cnt++;
		}
		
		return sb.toString();
	}
	
}
