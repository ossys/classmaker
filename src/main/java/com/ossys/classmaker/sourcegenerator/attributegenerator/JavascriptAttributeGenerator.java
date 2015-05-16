/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.attributegenerator;

import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator;

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
		DATE
	}
	private PrimitiveType primitiveType = null;
	
	public static enum AttributeType {
		CLASS,
		INTERFACE,
		ARGUMENT
	}
	
	private StringBuilder sb = new StringBuilder();
	
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
	
	public void setType(PrimitiveType primitiveType) {
		this.primitiveType = primitiveType;
	}
	
	public String generate(AttributeType type) {
		if(type == AttributeType.ARGUMENT && this.primitiveType == PrimitiveType.STRING) {
			this.sb.append("'" + this.name + "'");
		} else if(type == AttributeType.ARGUMENT) {
			this.sb.append(this.name);
		}
		
		return this.sb.toString();
	}
	
}
