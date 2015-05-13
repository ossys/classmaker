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
	
	public JavascriptAttributeGenerator(String name, ClassGenerator.NamingSyntaxType namingSyntaxType) {
		super(name, namingSyntaxType);
	}
	
	private void generate() {
		this.sb = new StringBuilder();
	}
	
}
