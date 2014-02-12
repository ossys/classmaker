/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.methodgenerator;

import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator;
import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;

/**
 * @author Administrator
 *
 */
public class MethodGenerator implements MethodGeneratorInterface {
	protected StringBuilder sb = new StringBuilder();
	protected StringBuilder comment = new StringBuilder();
	protected StringBuilder code = new StringBuilder();

	protected String name = "";
	protected String verb = null;
	protected String adjective = null;
	
	protected boolean constructor = false;
	protected boolean getter = false;
	protected boolean setter = false;
	protected boolean empty = false;
	protected boolean stc = false;
	protected boolean fin = false;
	
	
	public static enum MethodVisibilityType {
		PRIVATE,
		PROTECTED,
		PUBLIC
	}
	protected MethodVisibilityType visibilityType = null;
	
	protected NamingSyntaxType namingSyntaxType = NamingSyntaxType.CAMELCASE;
	
	public MethodGenerator(String name) {
		this.name = name;
	}
	
	public MethodGenerator(String name, NamingSyntaxType type) {
		this.name = name;
		this.namingSyntaxType = type;
	}
	
	public void setVisibility(MethodVisibilityType visibilityType) {
		this.visibilityType = visibilityType;
	}
	
	public void setVerb(String verb) {
		this.verb = verb;
	}
	
	public void setStatic() {
		this.stc = true;
	}
	
	public void setFinal() {
		this.fin = true;
	}
	
	public void setConstructor() {
		this.constructor = true;
	}
	
	public void setEmpty() {
		this.empty = true;
	}
	
	public boolean isConstructor() {
		return this.constructor;
	}
	
	public boolean isSetter() {
		return this.setter;
	}
	
	public boolean isGetter() {
		return this.getter;
	}
	
	public void addCode(String code) {
		this.code.append(code);
	}
	
	public void addComment(String comment) {
		this.comment.append(comment);
	}
	
	public void sets(String attribute) {
		if(!this.constructor) {
			this.setter = true;
			this.name = "Set " + attribute;
		}
	}
	
	public void gets(String attribute) {
		if(!this.constructor) {
			this.getter = true;
			this.name = "Get " + attribute;
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String name() {
		return ClassGenerator.getName(this.name, this.namingSyntaxType, false);
	}
	
	public MethodVisibilityType getVisibilityType() {
		return this.visibilityType;
	}
	
	public void pluralize() {
		this.name = ClassGenerator.pluralize(this.name);
	}

	public static String getMethodName(String name) {
		return ClassGenerator.getName(name, NamingSyntaxType.CAMELCASE, false);
	}

	public static String getPluralizedMethodName(String name) {
		return ClassGenerator.getName(name, NamingSyntaxType.CAMELCASE, true);
	}
	
}
