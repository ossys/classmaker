/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.methodgenerator;

import java.util.ArrayList;
import java.util.List;

import com.ossys.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavascriptAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.AttributeGenerator.AttributeVisibilityType;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavascriptAttributeGenerator.AttributeType;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavascriptAttributeGenerator.PrimitiveType;
import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator;

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
		ANONYMOUS,
		PROTOTYPED,
		MEMBER,
		CLASS
	}
	
	private List<JavascriptAttributeGenerator> attributes = new ArrayList<JavascriptAttributeGenerator>();
	private List<JavascriptMethodGenerator> methods = new ArrayList<JavascriptMethodGenerator>();
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
	
	public MethodType getType() {
		return this.type;
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
	
	public void addAttribute(JavascriptAttributeGenerator jsag) {
		this.attributes.add(jsag);
	}
	
	public void addMethod(JavascriptMethodGenerator jsmg) {
		jsmg.setTabLevel(this.tab_level+1);
		this.methods.add(jsmg);
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
			s.append(");\n");
		} else if(this.type == MethodType.MEMBER || this.type == MethodType.PROTOTYPED || this.type == MethodType.DECLARED || this.type == MethodType.ANONYMOUS || this.type == MethodType.CLASS) {
			for(int i=0; i<this.tab_level; i++) {
				s.append("\t");
			}
			
			if(this.type == MethodType.MEMBER && this.visibilityType == MethodVisibilityType.PRIVATE) {
				s.append("var " + this.name + " = ");
			} else if(this.type == MethodType.MEMBER && (this.visibilityType == MethodVisibilityType.PUBLIC || this.visibilityType == MethodVisibilityType.PRIVILEGED)) {
				s.append("this." + this.name + " = ");
			}else if(this.type == MethodType.PROTOTYPED) {
				s.append(this.var + ".prototype." + this.name + " = ");
			}
			s.append("function");
			if(this.type == MethodType.DECLARED || this.type == MethodType.CLASS) {
				s.append(" " + this.name);
			}
			s.append("(");
			int cnt = 0;
			for(String arg : this.args) {
				if(cnt > 0) {
					s.append(", ");
				}
				s.append(arg);
				cnt++;
			}
			s.append(") {");
			if(this.type == MethodType.DECLARED || this.type == MethodType.CLASS) {
				s.append("\n");
				if(this.type == MethodType.CLASS) {
					s.append("\tif (!(this instanceof " + this.name + ")) {\n");
					s.append("\t\tthrow new TypeError(\"" + this.name + " constructor cannot be called as a function.\");\n");
					s.append("\t}\n");
				}
			}
			
			for(JavascriptAttributeGenerator jsag : this.attributes) {
				if(!jsag.isStatic()) {
					s.append("\t" + jsag.generate(AttributeType.MEMBER) + "\n");
				}
			}
			
			if(this.code.length() > 0) {
				s.append("\n" + this.code.toString() + "\n");
			}
			
			if(this.type == MethodType.DECLARED || this.type == MethodType.CLASS) {
				for(JavascriptMethodGenerator jsmg : this.methods) {
					if(jsmg.getVisibilityType() == MethodVisibilityType.PRIVATE || jsmg.getVisibilityType() == MethodVisibilityType.PRIVILEGED) {
						s.append(jsmg.generate());
					}
				}
			}

			for(int i=0; i<this.tab_level; i++) {
				s.append("\t");
			}
			s.append("}");
			if(this.type == MethodType.MEMBER || this.type == MethodType.ASSIGNED) {
				s.append(";\n\n");
			}
			
			//Static attributes
			if(this.type == MethodType.DECLARED || this.type == MethodType.CLASS) {
				for(JavascriptAttributeGenerator jsag : this.attributes) {
					if(jsag.isStatic() && jsag.getVisibilityType() == AttributeVisibilityType.PUBLIC) {
						jsag.setParent(this);
						s.append("\n" + jsag.generate(AttributeType.MEMBER));
					}
				}
			}
			
			//Prototyped methods
			if(this.type == MethodType.DECLARED || this.type == MethodType.CLASS) {
				for(JavascriptMethodGenerator jsmg : this.methods) {
					if(jsmg.getVisibilityType() == MethodVisibilityType.PUBLIC && jsmg.getType() == MethodType.PROTOTYPED) {
						s.append(jsmg.generate());
					}
				}
			}
		}
		
		return s.toString();
	}
	
}
