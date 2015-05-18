/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.classgenerator;

import java.util.ArrayList;
import java.util.List;

import com.ossys.classmaker.sourcegenerator.methodgenerator.JavascriptMethodGenerator;

/**
 * @author Administrator
 *
 */
public class JavascriptClassGenerator extends ClassGenerator {

	private int methods_cnt = 0;
	private List<List<String>> libraries = new ArrayList<List<String>>();
	private StringBuilder code = new StringBuilder();
	private String exports = null;
	
	public JavascriptClassGenerator(String name) {
		super(name);
		this.sb = new StringBuilder();
	}
	
	public JavascriptClassGenerator(String name, String path) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".js");
		this.sb = new StringBuilder();
	}
	
	public JavascriptClassGenerator(String name, String path, NamingSyntaxType type) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".js", type);
		this.sb = new StringBuilder();
	}
	
	public String getSource() {
		this.generate();
		return this.sb.toString();
	}
	
	public void addMethod(JavascriptMethodGenerator jsmg) {
		if(this.methods_cnt > 0) {
			this.code.append("\n\n");
		}
		this.methods_cnt++;
		this.code.append(jsmg.generate());
	}
	
	public void addLibrary(String var, String lib) {
		List<String> library = new ArrayList<String>();
		library.add(var);
		library.add(lib);
		this.libraries.add(library);
	}
	
	public void addCode(String code) {
		this.code.append(code);
	}
	
	public void exports(String export) {
		this.exports = export;
	}
	
	private void generate() {
		
		for(List<String> library : this.libraries) {
			if(library.get(0) == null || library.get(0).equals("")) {
				this.sb.append("require('" + library.get(1) + "');\n");
			} else {
				this.sb.append("var " + library.get(0) + " = require('" + library.get(1) + "');\n");
			}
		}
		
		if(this.libraries.size() > 0) {
			this.sb.append("\n");
		}
		
		this.sb.append(this.code.toString());
		
		if(this.exports != null) {
			this.sb.append("\nmodule.exports = " + this.exports + ";");
		}
	}
	
	public boolean save() {
		boolean success = false;
		
		this.generate();
		
		if(super.save()) {
			success = true;
		}
		
		return success;
	}
	
}
