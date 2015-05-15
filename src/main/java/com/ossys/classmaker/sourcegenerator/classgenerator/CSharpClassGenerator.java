/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.classgenerator;

import java.util.ArrayList;
import java.util.List;

import com.ossys.classmaker.sourcegenerator.methodgenerator.JavascriptMethodGenerator;

/**
 * @author Administrator
 *
 */
public class CSharpClassGenerator extends ClassGenerator {

	private List<JavascriptMethodGenerator> methods = new ArrayList<JavascriptMethodGenerator>();
	
	public CSharpClassGenerator(String name) {
		super(name);
	}
	
	public CSharpClassGenerator(String name, String path) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".js");
	}
	
	public CSharpClassGenerator(String name, String path, NamingSyntaxType type) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".js", type);
	}
	
	public void addMethod(JavascriptMethodGenerator method) {
		this.methods.add(method);
	}
	
	public String getSource() {
		this.generate();
		return this.sb.toString();
	}
	
	private void generate() {
		this.sb = new StringBuilder();
	}
	
	public boolean save() {
		boolean success = false;
		
		this.generate();
		
		if(super.save()) {
			success = true;
		}
		
		return success;
	}
	
}
