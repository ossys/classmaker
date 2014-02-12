package com.ossys.classmaker.dbgenerator.tablemaker;

import java.util.ArrayList;

import com.ossys.classmaker.dbgenerator.attributegenerator.MySQLAttributeGenerator;

public class MySQLTableGenerator extends TableGenerator {
	
	private String schema = "";
	private int auto_increment = 0;
	private ArrayList<MySQLAttributeGenerator> attributes = new ArrayList<MySQLAttributeGenerator>();
	private ArrayList<MySqlConstraint> constraints = new ArrayList<MySqlConstraint>();
	
	public static enum EngineType {
		INNODB,
		MYISAM,
		MEMORY,
		MERGE,
		ARCHIVE,
		FEDERATED,
		NDBCLUSTER,
		CSV,
		BLACKHOLE
	}
	private EngineType engine = null;
	
	public static enum CharsetType {
		LATIN1,
		UTF8
	}
	private CharsetType charset = null;

	public static enum IndexType {
		INDEX,
		PRIMARY,
		UNIQUE,
		FULLTEXT,
		SPATIAL
	}
	
	public MySQLTableGenerator(String name, String schema) {
		super(name);
		this.schema = TableGenerator.getSchemaName(schema);
		this.engine = EngineType.INNODB;
		this.charset = CharsetType.LATIN1;
	}
	
	public void dropIfExists() {
		this.drop = true;
	}
	
	public void setEngineType(EngineType engine) {
		this.engine = engine;
	}
	
	public EngineType getEngineType() {
		return this.engine;
	}
	
	public void setCharsetType(CharsetType charset) {
		this.charset = charset;
	}
	
	public CharsetType getCharsetType() {
		return this.charset;
	}
	
	public void setAutoIncrement(int auto_increment) {
		this.auto_increment = auto_increment;
	}
	
	public void addAttribute(MySQLAttributeGenerator mag) {
		this.attributes.add(mag);
		
		this.addAttribute();
	}
	
	public void addConstraint(MySqlConstraint constraint) {
		this.constraints.add(constraint);
	}
	
	public String getDDL() {
		if(this.drop) {
			this.sb.append("DROP TABLE IF EXISTS `" + this.schema + "`.`" + this.name + "`;\n");
		}
		
		this.sb.append("CREATE TABLE `" + this.schema + "`.`" + this.name + "` (\n");
		
		// Attributes
		int cnt=0;
		for(MySQLAttributeGenerator mag : this.attributes) {
			if(cnt>0) {
				this.sb.append(",\n");
			}
			
			this.sb.append("\t" + mag.toString());
			
			cnt++;
		}
		
		for(MySqlConstraint constraint : this.constraints) {
			this.sb.append(",\n\t" + constraint.toString());
		}
		
		for(MySQLAttributeGenerator mag : this.attributes) {
			if(mag.isPrimaryKey()) {
				this.sb.append(",\n\tPRIMARY KEY(`" + mag.getName() + "`)");
			}
		}
		
		this.sb.append("\n) ENGINE=");
		switch(this.engine) {
			case INNODB:
				this.sb.append("InnoDB");
				break;
			case MYISAM:
				this.sb.append("");
				break;
			case MEMORY:
				this.sb.append("");
				break;
			case MERGE:
				this.sb.append("");
				break;
			case ARCHIVE:
				this.sb.append("");
				break;
			case FEDERATED:
				this.sb.append("");
				break;
			case NDBCLUSTER:
				this.sb.append(""); 
				break;
			case CSV:
				this.sb.append(""); 
				break;
			case BLACKHOLE:
				this.sb.append("");
				break;
		}
		
		this.sb.append(" AUTO_INCREMENT=" + this.auto_increment);
		
		this.sb.append(" DEFAULT CHARSET=");
		switch(this.charset) {
			case LATIN1:
				sb.append("latin1");
				break;
			case UTF8:
				sb.append("utf-8");
				break;
		}
		
		if(this.comment != null) {
			this.sb.append(" COMMENT='" + this.comment + "'");
		}
		
		return this.sb.toString();
	}
	
}
