package com.ossys.classmaker.dbgenerator.attributegenerator;

public class MySQLAttributeGenerator extends AttributeGenerator {
 
	public MySQLAttributeGenerator(String name) {
		super(name);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("`" + this.name + "`");
		
		switch(this.dataType) {
			case NUM:
				if(this.precision == 0) {
					if(this.signed && this.max_length > 2147483647L) {
						sb.append(" BIGINT(" + new Double(this.max_length).toString().length() + ") SIGNED");
					} else if(!this.signed && this.max_length > 4294967295L) {
						sb.append(" BIGINT(" + new Double(this.max_length).toString().length() + ") UNSIGNED");
					} else if(this.signed && this.max_length > 8388607) {
						sb.append(" INT(" + new Double(this.max_length).toString().length() + ") SIGNED");
					} else if(!this.signed && this.max_length > 16777215) {
						sb.append(" INT(" + new Double(this.max_length).toString().length() + ") UNSIGNED");
					} else if(this.signed && this.max_length > 32767) {
						sb.append(" MEDIUMINT(" + new Double(this.max_length).toString().length() + ") SIGNED");
					} else if(!this.signed && this.max_length > 65535) {
						sb.append(" MEDIUMINT(" + new Double(this.max_length).toString().length() + ") UNSIGNED");
					} else if(this.signed && this.max_length > 127) {
						sb.append(" SMALLINT(" + new Double(this.max_length).toString().length() + ") SIGNED");
					} else if(!this.signed && this.max_length > 255) {
						sb.append(" SMALLINT(" + new Double(this.max_length).toString().length() + ") UNSIGNED");
					} else if(this.signed) {
						sb.append(" TINYINT(" + new Double(this.max_length).toString().length() + ") SIGNED");
					} else if(!this.signed) {
						sb.append(" TINYINT(" + new Double(this.max_length).toString().length() + ") UNSIGNED");
					}
				} else {
					if(this.signed) {
						sb.append(" DOUBLE(" + this.precision + "," + new Double(this.max_length).toString().length() + ") UNSIGNED");
					} else if(!this.signed) {
						sb.append(" DOUBLE(" + this.precision + "," + new Double(this.max_length).toString().length() + ") UNSIGNED");
					}
				}
				break;
				
			case TEXT:
				if(this.max_length > 16777215) {
					sb.append(" LONGTEXT");
				} else if(this.max_length > 65535) {
					sb.append(" MEDIUMTEXT");
				} else if(this.max_length > 255) {
					sb.append(" TEXT(" + this.max_length + ")");
				} else if(this.max_length > 1) {
					sb.append(" VARCHAR(" + this.max_length + ")");
				} else if(this.max_length == 1) {
					sb.append(" VARCHAR(1)");
				}
				break;
				
			case ENUM:
				sb.append(" ENUM(");
				int cnt=0;
				for(String s : this.enums) {
					if(cnt>0) {
						sb.append(",");
					}
					sb.append("'" + AttributeGenerator.getAttributeName(s) + "'");
					cnt++;
				}
				sb.append(")");
				break;
				
			case DATE:
				sb.append(" DATETIME");
				break;
				
			case BINARY:
				break;
			
			case BOOLEAN:
				sb.append(" TINYINT(1) UNSIGNED");
				break;
		}
		
		if(!this.nullable) {
			sb.append(" NOT NULL");
		}
		
		if(this.auto_increment) {
			sb.append(" AUTO_INCREMENT");
		}
		
		return sb.toString();
	}
	
}
