/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.methodgenerator;

import java.util.ArrayList;
import java.util.List;

import com.ossys.classmaker.sourcegenerator.attributegenerator.JavascriptAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavascriptAttributeGenerator.AttributeType;

/**
 * @author Administrator
 *
 */
public class JavascriptMethodGenerator extends MethodGenerator {
	
	public static enum MethodType {
		ASSIGNED,
		DECLARED,
		CALLED,
		ANONYMOUS
	}
	
	private StringBuilder code = new StringBuilder();
	private List<String> args = new ArrayList<String>();
	private MethodType type = null;
	private String name = "";
	
	public JavascriptMethodGenerator(MethodType type, String name) {
		super(name);
		this.type = type;
		this.name = name;
	}
	
	public void addArgument(String arg) {
		this.args.add(arg);
	}
	
	public void addArgument(JavascriptAttributeGenerator jsag) {
		this.args.add(jsag.generate(AttributeType.ARGUMENT));
	}
	
	public void addArgument(JavascriptMethodGenerator jsmg) {
		this.args.add(jsmg.generate());
	}
	
	public void addCode(String code) {
		this.code.append(code);
	}
	
	public String generate() {
		StringBuilder s = new StringBuilder();
		
		if(this.type == MethodType.CALLED) {
			s.append(this.name + "(");
			int cnt = 0;
			for(String arg : this.args) {
				if(cnt > 0) {
					s.append(", ");
				}
				s.append(arg);
				cnt++;
			}
			s.append(");\n\n");
		} else if(this.type == MethodType.ANONYMOUS) {
			s.append("function(");
			int cnt = 0;
			for(String arg : this.args) {
				if(cnt > 0) {
					s.append(", ");
				}
				s.append(arg);
				cnt++;
			}
			s.append(") {\n");
			s.append(this.code.toString() + "\n");
			s.append("}");
		}
		
		return s.toString();
	}
	
}
