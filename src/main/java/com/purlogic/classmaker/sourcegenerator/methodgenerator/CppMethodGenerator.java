package com.purlogic.classmaker.sourcegenerator.methodgenerator;

import java.util.ArrayList;

import com.purlogic.classmaker.sourcegenerator.attributegenerator.CppAttributeGenerator;
import com.purlogic.classmaker.sourcegenerator.attributegenerator.CppAttributeGenerator.AttributeSignage;
import com.purlogic.classmaker.sourcegenerator.attributegenerator.CppAttributeGenerator.AttributeType;
import com.purlogic.classmaker.sourcegenerator.classgenerator.ClassGenerator;
import com.purlogic.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;

public class CppMethodGenerator extends MethodGenerator {
	
	public static enum ReturnType {
		BOOLEAN,
		BYTE,
		SHORT,
		INT,
		LONG,
		FLOAT,
		DOUBLE,
		CHAR,
		STRING
	}
	private ReturnType return_type = null;
	private AttributeSignage return_signage = null;
	
	private String complex_type = null;
	
	public static enum MethodType {
		DEFINITION,
		IMPLEMENTATION
	}
	
	private ArrayList<CppAttributeGenerator> arguments = new ArrayList<CppAttributeGenerator>();

	private boolean destructor = false;
	private boolean virtual = false;
	private boolean constant = false;
	private String namespace = "";
	private String classname = "";
	
	public CppMethodGenerator() {
		super(null);
	}
	
	public CppMethodGenerator(String name) {
		super(name);
	}
	
	public CppMethodGenerator(String name, NamingSyntaxType type) {
		super(name,type);
	}
	
	public void setDestructor() {
		this.destructor = true;
	}
	
	public void setVirtual() {
		this.virtual = true;
	}
	
	public void setConstant() {
		this.constant = true;
	}
	
	public void setReturnSignage(AttributeSignage return_signage) {
		this.return_signage = return_signage;
		this.complex_type = null;
	}
	
	public void setReturnType(ReturnType return_type) {
		this.return_type = return_type;
		this.complex_type = null;
	}
	
	public void setComplexType(String complex_type) {
		this.complex_type = complex_type;
		this.return_type = null;
	}
	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public void setClassname(String classname) {
		this.classname = classname;
	}

	public void addArgument(CppAttributeGenerator argument) {
		this.arguments.add(argument);
	}
	
	public String getSource(MethodType type) {
		this.generate(type);
		return this.sb.toString();
	}
	
	private void generate(MethodType type) {
		this.sb.delete(0, this.sb.length());
		if(type == MethodType.DEFINITION) {
			if((!this.constructor && !this.destructor) &&
				this.return_type == null &&
				this.complex_type == null) {
				this.sb.append("void ");
			} else if(this.return_type != null && !this.constructor) {
				if(this.return_signage != null) {
					switch(this.return_signage) {
						case SIGNED:
							sb.append("signed ");
							break;
						case UNSIGNED:
							sb.append("unsigned ");
							break;
					}
				} else {
					sb.append("");
				}
				
				switch(this.return_type) {
					case BOOLEAN:
						sb.append("bool ");
						break;
					case BYTE:
						sb.append("char ");
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
						sb.append("string ");
						break;
				}
			} else if(this.complex_type != null && !this.constructor) {
				sb.append(this.complex_type + " ");
			}
			
			if(this.virtual) {
				this.sb.append("virtual ");
			}
			if(this.destructor) {
				this.sb.append("~");
			}
			if(this.constructor || this.destructor) {
				this.sb.append(ClassGenerator.getClassName(this.name));
			} else {
				this.sb.append(this.name());
			}
			
			this.sb.append("(");
			int cnt = 0;
			for(CppAttributeGenerator argument : this.arguments) {
				if(cnt > 0) {
					this.sb.append(", ");
				}
				this.sb.append(argument.getSource(AttributeType.ARGUMENT));
				cnt++;
			}
			this.sb.append(")");
			
			if(this.constant) {
				this.sb.append(" const");
			}
			this.sb.append(";\n");
		} else if(type == MethodType.IMPLEMENTATION) {
			if(this.constructor) {
				this.sb.append(this.namespace + ClassGenerator.getName(this.classname, NamingSyntaxType.PASCAL, false) + "::" + ClassGenerator.getClassName(this.name));
			} else if(this.destructor) {
				this.sb.append(this.namespace + ClassGenerator.getName(this.classname, NamingSyntaxType.PASCAL, false) + "::~" + ClassGenerator.getClassName(this.name));
			} else {
				if((!this.constructor && !this.destructor) &&
					this.return_type == null &&
					this.complex_type == null) {
					this.sb.append("void ");
				} else if(this.return_type != null && !this.constructor) {
					if(this.return_signage != null) {
						switch(this.return_signage) {
							case SIGNED:
								sb.append("signed ");
								break;
							case UNSIGNED:
								sb.append("unsigned ");
								break;
						}
					} else {
						sb.append("");
					}
					
					switch(this.return_type) {
						case BOOLEAN:
							sb.append("bool ");
							break;
						case BYTE:
							sb.append("char ");
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
							sb.append("string ");
							break;
					}
				} else if(this.complex_type != null && !this.constructor) {
					sb.append(this.complex_type + " ");
				}
				this.sb.append(this.namespace + ClassGenerator.getName(this.classname, NamingSyntaxType.PASCAL, false) + "::" + this.name());
			}

			this.sb.append("(");
			int cnt = 0;
			for(CppAttributeGenerator argument : this.arguments) {
				if(cnt > 0) {
					this.sb.append(", ");
				}
				this.sb.append(argument.getSource(AttributeType.ARGUMENT));
				cnt++;
			}
			this.sb.append(")");
			
			if(this.constant) {
				this.sb.append(" const");
			}
			
			this.sb.append(" {\n");
			this.sb.append(this.code);
			this.sb.append("}\n\n");
		}
	}
	
}
