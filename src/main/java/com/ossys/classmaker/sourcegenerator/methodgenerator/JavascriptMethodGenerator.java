/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.methodgenerator;

import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;

/**
 * @author Administrator
 *
 */
public class JavascriptMethodGenerator extends MethodGenerator {
	
	public JavascriptMethodGenerator() {
		super(null);
	}
	
	public JavascriptMethodGenerator(String name) {
		super(name);
	}
	
	public JavascriptMethodGenerator(String name, NamingSyntaxType type) {
		super(name,type);
	}
	
	private void generate() {
		this.sb = new StringBuilder();
	}
	
}
