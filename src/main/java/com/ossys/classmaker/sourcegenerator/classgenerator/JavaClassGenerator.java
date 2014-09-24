/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.classgenerator;

import java.util.ArrayList;
import java.util.List;

import com.ossys.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator.AttributeType;
import com.ossys.classmaker.sourcegenerator.methodgenerator.JavaMethodGenerator;
import com.ossys.classmaker.sourcegenerator.methodgenerator.JavaMethodGenerator.MethodType;

/**
 * @author Administrator
 *
 */
public class JavaClassGenerator extends ClassGenerator {
	private String pkg = null;
	private boolean subclass = false;
	
	private List<JavaAttributeGenerator> attributes = new ArrayList<JavaAttributeGenerator>();
	private List<String> annotations = new ArrayList<String>();
	private List<JavaMethodGenerator> methods = new ArrayList<JavaMethodGenerator>();
	private List<JavaClassGenerator> subclasses = new ArrayList<JavaClassGenerator>();
	private List<Enum> enums = new ArrayList<Enum>();
	private List<String> static_code_blocks = new ArrayList<String>();
	
	public JavaClassGenerator(String name) {
		super(name);
	}
	
	public JavaClassGenerator(String name, String path) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".java");
	}
	
	public JavaClassGenerator(String name, String path, NamingSyntaxType type) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".java",type);
	}
	
	public JavaClassGenerator(String name, String path, NamingSyntaxType type, String pkg) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".java", type);
		this.pkg = pkg;
	}
	
	public JavaClassGenerator(String name, String path, String pkg) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".java");
		this.pkg = pkg;
	}
	
	public void addAttribute(JavaAttributeGenerator jag) {
		super.addAttribute();
		this.attributes.add(jag);
	}
	
	public void addAnnotation(String annotation) {
		this.annotations.add(annotation);
	}
	
	public void addMethod(JavaMethodGenerator method) {
		this.methods.add(method);
	}
	
	public void addSubclass(JavaClassGenerator jcg) {
		super.addSubclass();
		this.subclasses.add(jcg);
	}
	
	public void addEnumClass(JavaAttributeGenerator jag, List<String> types) {
		this.enums.add(new Enum(jag, types));
	}
	
	public void addStaticCodeBlock(String static_code_block) {
		this.static_code_blocks.add(static_code_block);
	}
	
	public String getSource() {
		this.generate();
		return this.sb.toString();
	}
	
	public void setSubclass(boolean subclass) {
		this.subclass = subclass;
	}
	
	public boolean getSubclass() {
		return this.subclass;
	}
	
	public String getPackage() {
		return this.pkg;
	}
	
	private void generate() {
		this.sb = new StringBuilder();

		// Setting class package
		if(this.pkg != null && !this.subclass) {
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
		
		for(String annotation : this.annotations) {
			this.sb.append(annotation + "\n");
		}
		
		// Defining Class
		if(this.visibilityType == ClassVisibilityType.PRIVATE) {
			this.sb.append("private ");
		} else if(this.visibilityType == ClassVisibilityType.PROTECTED) {
			this.sb.append("protected ");
		} else {
			this.sb.append("public ");
		}
		// Defining Class
		this.sb.append("class " + this.name());
		
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
				this.sb.append(jag.getSource(AttributeType.CLASS));
			} else {
				this.sb.append("\n");
			}
		}
		
		// Static Code Blocks
		for(String static_code_block : this.static_code_blocks) {
			this.sb.append("\tstatic {\n");
			this.sb.append(static_code_block);
			this.sb.append("\n\t}\n");
		}
		
		//Enums
		for(Enum e : this.enums) {
			this.sb.append(e.toString());
		}
		
		this.sb.append("\n");
		
		// Constructors
		for(JavaMethodGenerator jmg : this.methods) {
			if(jmg.isConstructor()) {
				jmg.setName(this.name());
				this.sb.append(jmg.getSource(MethodType.CLASS));
				this.sb.append("\n");
			}
		}
		
		// Other Methods
		for(JavaMethodGenerator jmg : this.methods) {
			if(!jmg.isConstructor() && !jmg.isSetter() && !jmg.isGetter()) {
				this.sb.append(jmg.getSource(MethodType.CLASS));
				this.sb.append("\n");
			}
		}
		
		// Setters
		for(JavaMethodGenerator jmg : this.methods) {
			if(jmg.isSetter()) {
				this.sb.append(jmg.getSource(MethodType.CLASS));
				this.sb.append("\n");
			}
		}
		
		// Getters
		for(JavaMethodGenerator jmg : this.methods) {
			if(jmg.isGetter()) {
				this.sb.append(jmg.getSource(MethodType.CLASS));
				this.sb.append("\n");
			}
		}
		
		// Subclasses
		for(JavaClassGenerator jcg : this.subclasses) {
			jcg.setSubclass(true);
			this.sb.append(jcg.getSource() + "\n");
		}
		
		// End Class
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
	
	private class Enum {
		private JavaAttributeGenerator jag = null;
		private List<String> types = new ArrayList<String>();
		
		public Enum(JavaAttributeGenerator jag, List<String> types) {
			this.jag = jag;
			this.types = types;
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			sb.append("\t");
			if(jag.getVisibilityType() != null) {
				switch(jag.getVisibilityType()) {
					case PRIVATE:
						sb.append("private");
						break;
					case PROTECTED:
						sb.append("protected");
						break;
					case PUBLIC:
						sb.append("public");
						break;
				}
			} else {
				sb.append("private");
			}
			
			if(jag.isStatic()) {
				sb.append(" static");
			}
			sb.append(" enum " + ClassGenerator.getName(jag.getOriginalName(), NamingSyntaxType.PASCAL, false) + " {");
			int cnt = 0;
			for(String type : this.types) {
				if(cnt > 0) {
					sb.append(",");
				}
				sb.append(ClassGenerator.getName("\n\t\t" + type, ClassGenerator.NamingSyntaxType.UPPERCASE, false));
				cnt++;
			}
			
			//TODO: This should not be hard-coded into the Enum, but instead needs to be added as an attribute via API
			sb.append(";\n\n\t\tpublic static final " + ClassGenerator.getName(jag.getOriginalName(), NamingSyntaxType.PASCAL, false) + "[] value = " + ClassGenerator.getName(jag.getOriginalName(), NamingSyntaxType.PASCAL, false) + ".values();\n\t}\n");
			
			return sb.toString();
		}
	}
	
}
