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
	private List<List<String>> post_export_libraries = new ArrayList<List<String>>();
	private MethodType type = null;
	private String var = null;
	private String name = null;
	private String forward_declaration = null;
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
		if(jsmg.getType() != MethodType.PROTOTYPED && !jsmg.isStatic()) {
			jsmg.setTabLevel(this.tab_level+1);
		}
		this.methods.add(jsmg);
	}
	
	public void setForwardDeclaration(String forward_declaration) {
		this.forward_declaration = forward_declaration;
	}
	
	public void addPostExportLibrary(String var, String lib) {
		List<String> library = new ArrayList<String>();
		library.add(var);
		library.add(lib);
		this.post_export_libraries.add(library);
	}
	
	public String generate() {
		StringBuilder s = new StringBuilder();
		
		if(this.type == MethodType.CALLED || this.type == MethodType.INSTANTIATED || this.type == MethodType.ASSIGNED) {
			for(int i=0; i<this.tab_level; i++) {
				s.append("\t");
			}
			
			if(this.type == MethodType.INSTANTIATED || this.type == MethodType.ASSIGNED) {
				if(this.isStatic()) {
					s.append(this.var + " = ");
				} else if(this.type == MethodType.ASSIGNED && this.visibilityType == MethodVisibilityType.PUBLIC) {
					s.append("this." + this.var + " = ");
				} else {
					s.append("var " + this.var + " = ");
				}
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
			s.append(")");
			if(!this.isStatic()) {
				s.append(";\n");
			}
		} else if(this.type == MethodType.MEMBER || this.type == MethodType.PROTOTYPED || this.type == MethodType.DECLARED || this.type == MethodType.ANONYMOUS || this.type == MethodType.CLASS) {
			for(int i=0; i<this.tab_level; i++) {
				s.append("\t");
			}
			
			if(this.type == MethodType.MEMBER && this.visibilityType == MethodVisibilityType.PRIVATE) {
				s.append("var " + this.name + " = ");
			} else if(this.type == MethodType.MEMBER && (this.visibilityType == MethodVisibilityType.PUBLIC || this.visibilityType == MethodVisibilityType.PRIVILEGED)) {
				s.append("this." + this.name + " = ");
			} else if(this.type == MethodType.PROTOTYPED) {
				s.append(this.var + ".prototype." + this.name + " = ");
			} else if(this.type == MethodType.CLASS && this.isStatic()) {
				s.append(this.var + "." + this.name + " = ");
			}
			s.append("function");
			if(this.type == MethodType.DECLARED || (this.type == MethodType.CLASS && !this.isStatic())) {
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
			if(this.type == MethodType.DECLARED || (this.type == MethodType.CLASS && !this.isStatic())) {
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
			
			if(this.type == MethodType.MEMBER || this.type == MethodType.ASSIGNED || this.type == MethodType.DECLARED) {
				s.append(";");
				if(this.forward_declaration == null) {
					s.append("\n\n");
				}
			}
			
			// Forward Declaration
			if(this.forward_declaration != null) {
				s.append("\n\nmodule.exports = " + this.forward_declaration + ";\n\n");
			}

			// Libraries to require() AFTER module.export, for cyclic dependencies
			for(List<String> post_export_library : this.post_export_libraries) {
				if(post_export_library.get(0) == null || post_export_library.get(0).equals("")) {
					if(post_export_library.get(1) == null || post_export_library.get(1).equals("")) {
						s.append("\n");
					} else {
						s.append("require('" + post_export_library.get(1) + "');\n");
					}
				} else {
					s.append("var " + post_export_library.get(0) + " = require('" + post_export_library.get(1) + "');\n");
				}
			}
			if(this.post_export_libraries.size() > 0) {
				s.append("\n");
			}
			
			//Prototyped methods
			if(this.type == MethodType.DECLARED || this.type == MethodType.CLASS) {
				for(JavascriptMethodGenerator jsmg : this.methods) {
					if(jsmg.getVisibilityType() == MethodVisibilityType.PUBLIC && jsmg.getType() == MethodType.PROTOTYPED) {
						s.append(jsmg.generate() + ";\n\n");
					}
				}
			}
			
			//Static attributes / enums
			if(this.type == MethodType.DECLARED || this.type == MethodType.CLASS) {
				for(JavascriptAttributeGenerator jsag : this.attributes) {
					if(jsag.isStatic() && jsag.getVisibilityType() == AttributeVisibilityType.PUBLIC) {
						jsag.setParent(this);
						s.append(jsag.generate(AttributeType.MEMBER) + "\n\n");
					}
				}
			}
			
			//Static methods
			if(this.type == MethodType.DECLARED || this.type == MethodType.CLASS) {
				for(JavascriptMethodGenerator jsmg : this.methods) {
					if(jsmg.isStatic()) {
						s.append(jsmg.generate() + ";\n\n");
					}
				}
			}
		}
		
		return s.toString();
	}
	
}
