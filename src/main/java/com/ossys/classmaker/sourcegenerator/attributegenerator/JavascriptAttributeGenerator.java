/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.attributegenerator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator;
import com.ossys.classmaker.sourcegenerator.methodgenerator.JavascriptMethodGenerator;
import com.ossys.classmaker.sourcegenerator.methodgenerator.JavascriptMethodGenerator.MethodType;

/**
 * @author Administrator
 *
 */
public class JavascriptAttributeGenerator extends AttributeGenerator {
	
	public static enum PrimitiveType {
		BOOLEAN,
		BYTE,
		NUM,
		CHAR,
		STRING,
		ENUM,
		DATE,
		OBJECT
	}
	private PrimitiveType primitiveType = null;
	
	public static enum AttributeType {
		MEMBER,
		ARGUMENT
	}
	
	private int tab_level = 0;
	private boolean tabbed = false;
	private StringBuilder sb = new StringBuilder();
	private Map<String, String> values = new LinkedHashMap<String, String>();
	
	public JavascriptAttributeGenerator(String name) {
		super(name);
	}
	
	public JavascriptAttributeGenerator(long name) {
		super(String.valueOf(name));
	}
	
	public JavascriptAttributeGenerator(double name) {
		super(String.valueOf(name));
	}
	
	public JavascriptAttributeGenerator(String name, ClassGenerator.NamingSyntaxType namingSyntaxType) {
		super(name, namingSyntaxType);
	}
	
	public void setTabbed(boolean tabbed) {
		this.tabbed = tabbed;
	}
	
	public void setTabLevel(int tab_level) {
		this.tab_level = tab_level;
	}
	
	public int getTabLevel() {
		return this.tab_level;
	}
	
	public void setType(PrimitiveType primitiveType) {
		this.primitiveType = primitiveType;
	}
	
	public PrimitiveType getType() {
		return this.primitiveType;
	}
	
	public void add(String name, String value) {
		if(this.primitiveType == PrimitiveType.OBJECT) {
			this.values.put(name, value);
		}
	}
	
	public void add(String name, long value) {
		if(this.primitiveType == PrimitiveType.OBJECT) {
			this.values.put(name, String.valueOf(value));
		}
	}
	
	public void add(String name, double value) {
		if(this.primitiveType == PrimitiveType.OBJECT) {
			this.values.put(name, String.valueOf(value));
		}
	}
	
	public void add(String name, JavascriptAttributeGenerator value) {
		if(this.primitiveType == PrimitiveType.OBJECT) {
			this.values.put(name, value.generate(AttributeType.MEMBER));
		}
	}
	
	public String generate(AttributeType type) {
		if(this.name != null && type == AttributeType.ARGUMENT && this.primitiveType == PrimitiveType.STRING) {
			this.sb.append("'" + this.name + "'");
		} else if(this.name != null && type == AttributeType.ARGUMENT) {
			this.sb.append(this.name);
		} else if((type == AttributeType.MEMBER || type == AttributeType.ARGUMENT) && this.primitiveType == PrimitiveType.OBJECT) {
			this.sb.append("{");
			if(this.tabbed) {
				this.sb.append("\n");
			} else {
				this.sb.append(" ");
			}
			
			int cnt=0;
		    Iterator<Map.Entry<String, String>> it = values.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, String> pair = (Map.Entry<String, String>)it.next();
		        if(cnt > 0) {
		        	this.sb.append(",");
					if(this.tabbed) {
						this.sb.append("\n");
					} else {
						this.sb.append(" ");
					}
		        }
		        for(int i=0; i < this.tab_level; i++) {
		        	this.sb.append("\t");
		        }
		        this.sb.append(pair.getKey() + " : " + pair.getValue());
				cnt++;
		    }
		    if(this.tabbed) {
		    	this.sb.append("\n");
		    } else {
		    	this.sb.append(" ");
		    }
		    for(int i=0; i<this.tab_level-1; i++) {
		    	this.sb.append("\t");
		    }
		    this.sb.append("}");
		}
		
		String ret = this.sb.toString();
		this.sb.delete(0, this.sb.length()-1);
		return ret;
	}
	
}
