/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.attributegenerator;

import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator;
import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;

/**
 * @author Administrator
 *
 */
public class AttributeGenerator implements AttributeGeneratorInterface {
	StringBuilder sb = new StringBuilder();
	
	protected boolean stc = false;
	protected boolean fin = false;
	protected boolean cnst = false;
	protected boolean is_array = false;
	protected long array_size = 0L;
	
	protected String name = "";
	protected String dflt = null;
	
	public static enum AttributeVisibilityType {
		PRIVATE,
		PROTECTED,
		PUBLIC
	}
	protected AttributeVisibilityType visibilityType = null;
	
	protected NamingSyntaxType namingSyntaxType = NamingSyntaxType.LOWERCASE;
	
	protected AttributeGenerator(String name) {
		this.name = name;
	}
	
	protected AttributeGenerator(String name, NamingSyntaxType type) {
		this.name = name;
		this.namingSyntaxType = type;
	}
	
	public void setVisibility(AttributeVisibilityType visibilityType) {
		this.visibilityType = visibilityType;
	}
	
	public void setStatic() {
		this.stc = true;
	}
	
	public void setFinal() {
		this.fin = true;
	}
	
	public void setConstant() {
		this.cnst = true;
	}
	
	public void setDefault() {
		this.dflt = null;
	}
	
	public void setArray() {
		this.is_array = true;
	}
	
	public void setArraySize(long array_size) {
		this.array_size = array_size;
	}
	
	public void setDefault(boolean dflt) {
		if(dflt) {
			this.dflt = "true";
		} else {
			this.dflt = "false";
		}
	}
	
	public void setDefault(byte dflt) {
		this.dflt = new Byte(dflt).toString();
	}
	
	public void setDefault(char dflt) {
		this.dflt = new Character(dflt).toString();
	}
	
	public void setDefault(short dflt) {
		this.dflt = new Short(dflt).toString();
	}
	
	public void setDefault(int dflt) {
		this.dflt = new Integer(dflt).toString();
	}
	
	public void setDefault(long dflt) {
		this.dflt = new Long(dflt).toString();
	}
	
	public void setDefault(float dflt) {
		this.dflt = new Float(dflt).toString();
	}
	
	public void setDefault(double dflt) {
		this.dflt = new Double(dflt).toString();
	}
	
	public void setDefault(String dflt) {
		this.dflt = dflt;
	}
	
	public boolean hasDefault() {
		if(this.dflt != null && !this.dflt.equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getOriginalName() {
		return this.name;
	}
	
	public String name() {
		return ClassGenerator.getName(this.name, this.namingSyntaxType, false);
	}
	
	public AttributeVisibilityType getVisibilityType() {
		return this.visibilityType;
	}
	
	public boolean isConstant() {
		return this.cnst;
	}
	
	public boolean isStatic() {
		return this.stc;
	}
	
	public void pluralize() {
		this.name = ClassGenerator.pluralize(this.name);
	}

	public static String getAttributeName(String name) {
		return ClassGenerator.getName(name, NamingSyntaxType.LOWERCASE, false);
	}

	public static String getAttributeName(String name, boolean pluralize) {
		return ClassGenerator.getName(name, NamingSyntaxType.LOWERCASE, pluralize);
	}
	
}
