/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.methodgenerator;

import java.util.ArrayList;
import java.util.List;

import com.ossys.classmaker.sourcegenerator.attributegenerator.JavascriptAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavascriptAttributeGenerator.AttributeType;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavascriptAttributeGenerator.PrimitiveType;

/**
 * @author Administrator
 *
 */
public class JavascriptMethodGenerator extends MethodGenerator {
	
	public static enum MethodType {
		ASSIGNED,
		INSTANTIATED,
		DECLARED,
		CALLED,
		ANONYMOUS
	}
	
	private StringBuilder code = new StringBuilder();
	private List<String> args = new ArrayList<String>();
	private MethodType type = null;
	private String var = null;
	private String name = null;
	private int tab_level = 0;
	
	public JavascriptMethodGenerator(MethodType type, String name) {
		super(name);
		this.type = type;
		this.name = name;
	}
	
	public JavascriptMethodGenerator(MethodType type, String var, String name) {
		super(name);
		this.var = var;
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
	
	public void addTabLevel() {
		this.tab_level++;
	}
	
	public int getTabLevel() {
		return this.tab_level;
	}
	
	public void setTabLevel(int tab_level) {
		this.tab_level = tab_level;
	}
	
	public void addCode(String code) {
		this.code.append(code);
	}
	
	public void addCode(JavascriptMethodGenerator jsmg) {
		jsmg.addTabLevel();
		this.code.append(jsmg.generate());
	}
	
	public String generate() {
		StringBuilder s = new StringBuilder();
		
		if(this.type == MethodType.CALLED || this.type == MethodType.INSTANTIATED || this.type == MethodType.ASSIGNED) {
			for(int i=0; i<this.tab_level; i++) {
				s.append("\t");
			}
			
			if(this.type == MethodType.INSTANTIATED || this.type == MethodType.ASSIGNED) {
				s.append("var " + this.var + " = ");
				if(this.type == MethodType.INSTANTIATED) {
					s.append("new ");
				}
			}
			s.append(this.name + "(");
			
			int cnt = 0;
			for(String arg : this.args) {
				if(cnt > 0) {
					s.append(", ");
				}
				s.append(arg);
				cnt++;
			}
			s.append(");");
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

			for(int i=0; i<this.tab_level; i++) {
				s.append("\t");
			}
			s.append("}");
		}
		
		return s.toString();
	}
	
}
