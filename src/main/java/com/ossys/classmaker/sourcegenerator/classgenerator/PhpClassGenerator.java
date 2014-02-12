/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.classgenerator;

import java.util.ArrayList;

import com.ossys.classmaker.sourcegenerator.attributegenerator.PhpAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.PhpAttributeGenerator.AttributeType;
import com.ossys.classmaker.sourcegenerator.methodgenerator.PhpMethodGenerator;

/**
 * @author Administrator
 *
 */
public class PhpClassGenerator extends ClassGenerator {
	
	private ArrayList<PhpAttributeGenerator> attributes = null;
	private ArrayList<PhpMethodGenerator> methods = null;
	private ArrayList<PhpClassGenerator> subclasses = null;

	public PhpClassGenerator(String name) {
		super(name);
		
		this.attributes = new ArrayList<PhpAttributeGenerator>();
		this.methods = new ArrayList<PhpMethodGenerator>();
		this.subclasses = new ArrayList<PhpClassGenerator>();
	}

	public PhpClassGenerator(String name, String path) {
		super(name,path + System.getProperty("file.separator") + ClassGenerator.getClassName(name) + ".php");
		
		this.attributes = new ArrayList<PhpAttributeGenerator>();
		this.methods = new ArrayList<PhpMethodGenerator>();
		this.subclasses = new ArrayList<PhpClassGenerator>();
	}
	
	public void extendsClass(String extended_class) {
		if(this.extended_classes.size() > 0) {
			this.extended_classes.set(0, extended_class);
		} else {
			super.extendsClass(extended_class);
		}
	}
	
	public void addAttribute(PhpAttributeGenerator pag) {
		super.addAttribute();
		this.attributes.add(pag);
	}
	
	public void addMethod(PhpMethodGenerator pmg) {
		super.addMethod();
		this.methods.add(pmg);
	}
	
	public void addSubclass(PhpClassGenerator pcg) {
		super.addSubclass();
		this.subclasses.add(pcg);
	}
	
	public String generateAsSubclass() {
		
		// Defining Class
		this.sb.append("class " + this.name() + " {\n");
		
		for(PhpAttributeGenerator pag : this.attributes) {
			if(pag != null) {
				this.sb.append(pag.getSource(AttributeType.DEFINITION));
			} else {
				this.sb.append("\n");
			}
		}
		
		this.sb.append("\n");
		
		// Single Constructor
		for(PhpMethodGenerator pmg : this.methods) {
			if(pmg.isConstructor()) {
				this.sb.append(pmg.getSource());
				break;
			}
		}
		
		// Other Methods
		for(PhpMethodGenerator pmg : this.methods) {
			if(!pmg.isConstructor() && !pmg.isSetter() && !pmg.isGetter()) {
				this.sb.append(pmg.getSource());
			}
		}
		
		// Getters
		for(PhpMethodGenerator pmg : this.methods) {
			if(pmg.isGetter()) {
				this.sb.append(pmg.getSource());
			}
		}
		
		// Setters
		for(PhpMethodGenerator pmg : this.methods) {
			if(pmg.isSetter()) {
				this.sb.append(pmg.getSource());
			}
		}
		
		// End Class
		this.sb.append("}\n\n");
		
		return this.sb.toString();
	}
	
	public String getSource() {
		this.generate();
		return this.sb.toString();
	}
	
	private void generate() {
		this.sb = new StringBuilder();
		
		if(this.path != null) {
			this.sb.append("<?php\n");
			
			// Importing libraries
			for(String library : this.libraries) {
				if(library == null) {
					this.sb.append("\n");
				} else {
					this.sb.append("require_once(dirname(__FILE__) . '/" + library + "');\n");
				}
			}
			
			if(this.libraries.size() > 0) {
				this.sb.append("\n");
			}
			
			// Comment
			this.sb.append(this.comment.toString());
			
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
			
			for(PhpAttributeGenerator pag : this.attributes) {
				if(pag != null) {
					this.sb.append(pag.getSource(AttributeType.DEFINITION));
				} else {
					this.sb.append("\n");
				}
			}
			
			this.sb.append("\n");
			
			// Single Constructor
			for(PhpMethodGenerator pmg : this.methods) {
				if(pmg.isConstructor()) {
					this.sb.append(pmg.getSource());
					break;
				}
			}
			
			// Other Methods
			for(PhpMethodGenerator pmg : this.methods) {
				if(!pmg.isConstructor() && !pmg.isSetter() && !pmg.isGetter()) {
					this.sb.append(pmg.getSource());
				}
			}
			
			// Getters
			for(PhpMethodGenerator pmg : this.methods) {
				if(pmg.isGetter()) {
					this.sb.append(pmg.getSource());
				}
			}
			
			// Setters
			for(PhpMethodGenerator pmg : this.methods) {
				if(pmg.isSetter()) {
					this.sb.append(pmg.getSource());
				}
			}
			
			// End Class
			this.sb.append("}\n\n");
			
			// Subclasses
			for(PhpClassGenerator pcg : this.subclasses) {
				this.sb.append(pcg.generateAsSubclass());
			}
			
			this.sb.append("?>");
			
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
