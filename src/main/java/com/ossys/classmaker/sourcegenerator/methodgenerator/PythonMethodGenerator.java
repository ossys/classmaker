/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.methodgenerator;

import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;

/**
 * @author Administrator
 *
 */
public class PythonMethodGenerator extends MethodGenerator {
	
	public PythonMethodGenerator() {
		super(null);
	}
	
	public PythonMethodGenerator(String name) {
		super(name);
	}
	
	public PythonMethodGenerator(String name, NamingSyntaxType type) {
		super(name,type);
	}
	
	private void generate() {
		this.sb = new StringBuilder();
	}
	
}
