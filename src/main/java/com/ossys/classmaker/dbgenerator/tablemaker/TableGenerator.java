package com.ossys.classmaker.dbgenerator.tablemaker;

public class TableGenerator {
	protected StringBuilder sb = null;

	protected boolean drop = false;
	protected String name = "";
	protected String comment = null;
	
	protected int num_attributes = 0;
	
	public TableGenerator(String name) {
		this.sb = new StringBuilder();
		
		this.name = TableGenerator.getTableName(name);
	}
	
	public static String getTableName(String name) {
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
	
	public static String getSchemaName(String name) {
		StringBuilder sb = new StringBuilder();
		
		String[] parts = name.split(" ");
		
		for(String s : parts) {
			sb.append(s.toLowerCase());
		}
		
		return sb.toString();
	}
	
	public void addAttribute() {
		this.num_attributes++;
	}
	
	public void dropTable() {
		this.drop = true;
	}
	
}
