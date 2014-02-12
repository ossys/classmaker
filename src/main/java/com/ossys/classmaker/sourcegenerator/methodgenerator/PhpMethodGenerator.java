/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.methodgenerator;

import java.util.ArrayList;

import com.ossys.classmaker.sourcegenerator.attributegenerator.PhpAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.PhpAttributeGenerator.AttributeType;
import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;

/**
 * @author Administrator
 *
 */
public class PhpMethodGenerator extends MethodGenerator {

	private ArrayList<PhpAttributeGenerator> arguments = new ArrayList<PhpAttributeGenerator>();

	public PhpMethodGenerator() {
		super(null);
	}

	public PhpMethodGenerator(String name) {
		super(name);
	}

	public PhpMethodGenerator(String name, NamingSyntaxType type) {
		super(name,type);
	}

	public void addArgument(PhpAttributeGenerator argument) {
		this.arguments.add(argument);
	}
	
	public String getSource() {
		this.generate();
		return this.sb.toString();
	}
	
	private void generate() {
		this.sb = new StringBuilder();
		
		this.sb.append(this.comment);
		
		if(this.constructor) {
			if(this.visibilityType == MethodVisibilityType.PROTECTED) {
				this.sb.append("\tprotected ");
			} else {
				this.sb.append("\tpublic ");
			}
			
			this.name = "__construct";
		} else {
			if(this.visibilityType == MethodVisibilityType.PRIVATE) {
				this.sb.append("\tprivate ");
			} else if(this.visibilityType == MethodVisibilityType.PROTECTED) {
				this.sb.append("\tprotected ");
			} else {
				this.sb.append("\tpublic ");
			}
			
			if(this.stc) {
				this.sb.append("static ");
			}
			
			if(this.fin) {
				this.sb.append("final ");
			}
		}
		this.sb.append("function " + this.name() + "(");
		
		int cnt = 0;
		for(PhpAttributeGenerator argument : this.arguments) {
			if(cnt > 0) {
				this.sb.append(", ");
			}
			this.sb.append(argument.getSource(AttributeType.ARGUMENT));
			cnt++;
		}
		
		if(this.empty) {
			this.sb.append(") { }\n\n");
		} else {
			this.sb.append(") {\n");
			
			this.sb.append(this.code.toString() + "\n");
			
			this.sb.append("\t}\n\n");
		}
	}
}
