package com.purlogic.classmaker.dbgenerator.tablemaker;

import java.util.ArrayList;

import com.purlogic.classmaker.dbgenerator.tablemaker.MySQLTableGenerator.IndexType;
import com.purlogic.classmaker.sourcegenerator.classgenerator.ClassGenerator;

public class MySqlConstraint {
	
	StringBuilder sb = new StringBuilder();
	String name = "";
	IndexType type = null;
	ArrayList<String> attributes = new ArrayList<String>();
	
	public MySqlConstraint(String name, IndexType type) {
		this.name = name;
		this.type = type;
	}
	
	public void addAttribute(String attribute) {
		this.attributes.add(attribute);
	}
	
	public String toString() {
		switch(this.type) {
			case UNIQUE:
				if(this.name.length() > 64) {
					this.name = name.substring(0, 63);
				}
				this.sb.append("UNIQUE KEY `" + com.purlogic.classmaker.sourcegenerator.classgenerator.ClassGenerator.getName(this.name, ClassGenerator.NamingSyntaxType.UPPERCASE, false) + "` (");
				int cnt = 0;
				for(String attr : this.attributes) {
					if(cnt > 0) {
						this.sb.append(",");
					}
					this.sb.append("`" + com.purlogic.classmaker.sourcegenerator.classgenerator.ClassGenerator.getName(attr, ClassGenerator.NamingSyntaxType.LOWERCASE, false) + "`");
					cnt++;
				}
				this.sb.append(")");
				break;
			case FULLTEXT:
				break;
			case INDEX:
				break;
			case PRIMARY:
				break;
			case SPATIAL:
				break;
		}
		
		return this.sb.toString();
	}
}
