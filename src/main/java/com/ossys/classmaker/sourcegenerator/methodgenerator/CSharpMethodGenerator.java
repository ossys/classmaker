/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.methodgenerator;

import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;

/**
 * @author Administrator
 *
 */
public class CSharpMethodGenerator extends MethodGenerator {
	
	public CSharpMethodGenerator() {
		super(null);
	}
	
	public CSharpMethodGenerator(String name) {
		super(name);
	}
	
	public CSharpMethodGenerator(String name, NamingSyntaxType type) {
		super(name,type);
	}
	
	private void generate() {
		this.sb = new StringBuilder();
	}
	
}
