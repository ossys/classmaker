/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.attributegenerator;

import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator;

/**
 * @author Administrator
 *
 */
public class CSharpAttributeGenerator extends AttributeGenerator {
	
	public CSharpAttributeGenerator(String name, ClassGenerator.NamingSyntaxType namingSyntaxType) {
		super(name, namingSyntaxType);
	}
	
	private void generate() {
		this.sb = new StringBuilder();
	}
	
}
