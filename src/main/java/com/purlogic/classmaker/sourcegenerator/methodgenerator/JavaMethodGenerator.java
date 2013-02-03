/**
 * 
 */
package com.purlogic.classmaker.sourcegenerator.methodgenerator;

import java.util.ArrayList;
import java.util.List;

import com.purlogic.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator;
import com.purlogic.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator.AttributeType;
import com.purlogic.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;

/**
 * @author Administrator
 *
 */
public class JavaMethodGenerator extends MethodGenerator {
	
	public static enum ReturnType {
		BOOLEAN,
		BYTE,
		SHORT,
		INT,
		LONG,
		FLOAT,
		DOUBLE,
		CHAR,
		STRING,
		VOID
	}
	private ReturnType returnType = null;

	public static enum MethodType {
		CLASS,
		INTERFACE
	}
	
	private String complexType = null;
	
	private List<JavaAttributeGenerator> arguments = new ArrayList<JavaAttributeGenerator>();
	private List<String> annotations = new ArrayList<String>();
	private List<String> exceptions = new ArrayList<String>();
	
	public JavaMethodGenerator() {
		super(null);
	}
	
	public JavaMethodGenerator(String name) {
		super(name);
	}
	
	public JavaMethodGenerator(String name, NamingSyntaxType type) {
		super(name,type);
	}
	
	public void setReturnType(ReturnType returnType) {
		this.returnType = returnType;
		this.complexType = null;
	}
	
	public void setReturnType(String complexType) {
		this.complexType = complexType;
		this.returnType = null;
	}

	public void addArgument(JavaAttributeGenerator argument) {
		this.arguments.add(argument);
	}
	
	public void addAnnotation(String annotation) {
		this.annotations.add(annotation);
	}
	
	public void addThrownException(String exception) {
		this.exceptions.add(exception);
	}
	
	public String getSource(MethodType type) {
		this.generate(type);
		return this.sb.toString();
	}
	
	private void generate(MethodType type) {
		this.sb = new StringBuilder();
		
		if(this.comment != null && this.comment.length() > 0) {
			this.sb.append(this.comment + "\n");
		}
		
		for(String annotation : this.annotations) {
			this.sb.append("\t" + annotation + "\n");
		}
		
		if(this.constructor) {
			if(this.visibilityType == MethodVisibilityType.PROTECTED) {
				this.sb.append("\tprotected ");
			} else {
				this.sb.append("\tpublic ");
			}
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
			
			if(this.returnType == ReturnType.VOID || (this.returnType == null && this.complexType == null)) {
				sb.append("void ");
			} else if(this.returnType != null && !this.constructor) {
				switch(this.returnType) {
					case BOOLEAN:
						sb.append("boolean ");
						break;
					case BYTE:
						sb.append("byte ");
						break;
					case SHORT:
						sb.append("short ");
						break;
					case INT:
						sb.append("int ");
						break;
					case LONG:
						sb.append("long ");
						break;
					case FLOAT:
						sb.append("float ");
						break;
					case DOUBLE:
						sb.append("double ");
						break;
					case CHAR:
						sb.append("char ");
						break;
					case STRING:
						sb.append("String ");
						break;
					case VOID:
						break;
					default:
						break;
				}
			} else if(this.complexType != null && !this.constructor) {
				sb.append(this.complexType + " ");
			}
		}
		if(this.constructor) {
			this.sb.append(this.name + "(");
		} else {
			this.sb.append(this.name() + "(");
		}
		
		int cnt = 0;
		for(JavaAttributeGenerator argument : this.arguments) {
			if(cnt > 0) {
				this.sb.append(", ");
			}
			this.sb.append(argument.getSource(AttributeType.ARGUMENT));
			cnt++;
		}
		
		if(type == MethodType.CLASS) {
			this.sb.append(") ");
		} else if(type == MethodType.INTERFACE) {
			this.sb.append(")");
		}
		
		if(this.exceptions.size() > 0) {
			this.sb.append("throws ");
			cnt = 0;
			for(String exception : this.exceptions) {
				if(cnt > 0) {
					this.sb.append(",");
				}
				this.sb.append(exception);
				cnt++;
			}
			if(type == MethodType.CLASS) {
				this.sb.append(" {");
			} else if(type == MethodType.INTERFACE) {
				this.sb.append(";");
			}
		} else {
			if(type == MethodType.CLASS) {
				this.sb.append("{");
			} else if(type == MethodType.INTERFACE) {
				this.sb.append(";\n");
			}
		}
		
		if(type == MethodType.CLASS) {
			if(!this.code.toString().equalsIgnoreCase("")) {
				this.sb.append("\n" + this.code.toString() + "\n\t");
			} else {
				this.sb.append(" ");
			}
			
			this.sb.append("}\n");
		}
	}
	
}
