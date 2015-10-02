/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.methodgenerator;

import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;

/**
 * @author Administrator
 *
 */

public class RubyMethodGenerator extends MethodGenerator {
	
	public RubyMethodGenerator() {
		super(null);
	}
	
	public RubyMethodGenerator(String name) {
		super(name);
	}
	
	public RubyMethodGenerator(String name, NamingSyntaxType type) {
		super(name,type);
	}
	
	private void generate() {
		this.sb = new StringBuilder();
	}
	
}
