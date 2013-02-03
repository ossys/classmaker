package com.purlogic.classmaker.sourcegenerator.classgenerator;

import java.util.ArrayList;

import com.purlogic.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator;
import com.purlogic.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator.AttributeType;
import com.purlogic.classmaker.sourcegenerator.methodgenerator.JavaMethodGenerator;
import com.purlogic.classmaker.sourcegenerator.methodgenerator.JavaMethodGenerator.MethodType;

public class JavaInterfaceGenerator extends ClassGenerator {
	private String pkg = null;

	private ArrayList<JavaAttributeGenerator> attributes = new ArrayList<JavaAttributeGenerator>();
	private ArrayList<JavaMethodGenerator> methods = new ArrayList<JavaMethodGenerator>();
	
	public JavaInterfaceGenerator(String name) {
		super(name);
	}
	
	public JavaInterfaceGenerator(String name, String path) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".java");
	}
	
	public JavaInterfaceGenerator(String name, String path, NamingSyntaxType type) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".java",type);
	}
	
	public JavaInterfaceGenerator(String name, String path, NamingSyntaxType type, String pkg) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".java", type);
		
		this.pkg = pkg;
	}
	
	public JavaInterfaceGenerator(String name, String path, String pkg) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".java");
		
		this.pkg = pkg;
	}

	public void addMethod(JavaMethodGenerator method) {
		this.methods.add(method);
	}
	
	public void addAttribute(JavaAttributeGenerator jag) {
		super.addAttribute();
		if(jag.isConstant()) {
			this.attributes.add(jag);
		}
	}
	
	public String getSource() {
		this.generate();
		return this.sb.toString();
	}
	
	private void generate() {
		this.sb = new StringBuilder();
		
		// Setting class package
		if(this.pkg != null) {
			if(!this.pkg.equalsIgnoreCase("")) {
				this.sb.append("package " + this.pkg + ";\n\n");
			}
		}
		
		// Importing libraries
		for(String library : this.libraries) {
			if(library == null) {
				this.sb.append("\n");
			} else {
				this.sb.append("import " + library + ";\n");
			}
		}
		
		if(this.libraries.size() > 0) {
			this.sb.append("\n");
		}
		
		// Defining Class
		if(this.visibilityType == ClassVisibilityType.PRIVATE) {
			this.sb.append("private ");
		} else if(this.visibilityType == ClassVisibilityType.PROTECTED) {
			this.sb.append("protected ");
		} else {
			this.sb.append("public ");
		}
		
		// Defining Interface
		this.sb.append("interface " + this.name());
		
		// Extended Classes
		if(this.extended_classes.size() > 0) {
			this.sb.append(" extends ");
		}
		int cnt = 0;
		for(String extended_class : this.extended_classes) {
			if(cnt > 0) {
				this.sb.append(", ");
			}
			this.sb.append(extended_class);
			cnt++;
		}
		
		this.sb.append(" {\n");
		
		// Attributes
		for(JavaAttributeGenerator jag : this.attributes) {
			if(jag != null) {
				this.sb.append(jag.getSource(AttributeType.INTERFACE));
			} else {
				this.sb.append("\n");
			}
		}
		
		// Other Methods
		for(JavaMethodGenerator jmg : this.methods) {
			if(!jmg.isConstructor() && !jmg.isSetter() && !jmg.isGetter()) {
				this.sb.append(jmg.getSource(MethodType.INTERFACE));
				this.sb.append("\n");
			}
		}
		
		// End Interface
		this.sb.append("}");
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
