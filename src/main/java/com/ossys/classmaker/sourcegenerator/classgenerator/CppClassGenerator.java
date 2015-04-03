package com.ossys.classmaker.sourcegenerator.classgenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ossys.classmaker.dbgenerator.attributegenerator.AttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.CppAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.AttributeGenerator.AttributeVisibilityType;
import com.ossys.classmaker.sourcegenerator.attributegenerator.CppAttributeGenerator.AttributeType;
import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;
import com.ossys.classmaker.sourcegenerator.methodgenerator.CppMethodGenerator;
import com.ossys.classmaker.sourcegenerator.methodgenerator.CppMethodGenerator.InitParam;
import com.ossys.classmaker.sourcegenerator.methodgenerator.CppMethodGenerator.MethodType;
import com.ossys.classmaker.sourcegenerator.methodgenerator.MethodGenerator.MethodVisibilityType;

public class CppClassGenerator extends ClassGenerator {

	public static enum LibraryType {
		STANDARD,
		CUSTOM
	}
	
	StringBuilder sb_h = new StringBuilder();
	StringBuilder sb_i = new StringBuilder();
	
	String root_path = "";
	String include_path = "";
	String include_prepend = "";
	
	private List<CppMethodGenerator> methods = new ArrayList<CppMethodGenerator>();
	private List<CppAttributeGenerator> attributes = new ArrayList<CppAttributeGenerator>();
	private List<String> standard_header_libraries = new ArrayList<String>();
	private List<String> custom_header_libraries = new ArrayList<String>();
	private List<String> standard_implementation_libraries = new ArrayList<String>();
	private List<String> custom_implementation_libraries = new ArrayList<String>();
	private List<String> header_typedefs = new ArrayList<String>();
	private List<String> implementation_typedefs = new ArrayList<String>();
	private List<String> namespaces = new ArrayList<String>();
	private List<String> forward_declarations = new ArrayList<String>();
	private List<Enum> enums = new ArrayList<Enum>();
	
	public CppClassGenerator(String name, String path) {
		super(name,path);
		this.root_path = path;
	}
	
	public void addMethod(CppMethodGenerator cmg) {
		this.methods.add(cmg);
	}
	
	public void addAttribute(CppAttributeGenerator cag) {
		this.attributes.add(cag);
	}
	
	public void addHeaderLibrary(LibraryType libraryType, String library) {
		if(libraryType == LibraryType.STANDARD) {
			this.standard_header_libraries.add(library);
		} else if(libraryType == LibraryType.CUSTOM) {
			this.custom_header_libraries.add(library);
		}
	}
	
	public void addImplementationLibrary(LibraryType libraryType, String library) {
		if(libraryType == LibraryType.STANDARD) {
			this.standard_implementation_libraries.add(library);
		} else if(libraryType == LibraryType.CUSTOM) {
			this.custom_implementation_libraries.add(library);
		}
	}
	
	public void addHeaderLibrary(String library) {
		this.standard_header_libraries.add(library);
	}
	
	public void addImplementationLibrary(String library) {
		this.standard_implementation_libraries.add(library);
	}
	
	public void addNamespace(String namespace) {
		this.namespaces.add(namespace);
	}
	
	public void addHeaderTypedef(String typedef) {
		this.header_typedefs.add(typedef);
	}
	
	public void addImplementationTypedef(String typedef) {
		this.implementation_typedefs.add(typedef);
	}
	
	public void addForwardDeclaration(String forward_declaration) {
		this.forward_declarations.add(forward_declaration);
	}
	
	public void addEnumClass(CppAttributeGenerator cppag, List<String> types) {
		this.enums.add(new Enum(cppag, types));
	}
	
	public String getNamespace() {
		StringBuilder sb = new StringBuilder();
		for(String namespace : this.namespaces) {
			sb.append(namespace + "::");
		}
		return sb.toString();
	}
	
	private String getNamespaceTabs() {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<this.namespaces.size();i++) {
			sb.append("\t");
		}
		return sb.toString();
	}
	
	public String getSource() {
		this.generate();
		return this.sb_h.toString() + this.sb_i.toString();
	}
	
	public boolean hasMembersOfVisibilityType(AttributeVisibilityType type) {
		for(CppAttributeGenerator attribute : this.attributes) {
			if(attribute.getVisibilityType() == type) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<CppAttributeGenerator> getAttributesOfVisibilityType(AttributeVisibilityType type) {
		ArrayList<CppAttributeGenerator> attributes = new ArrayList<CppAttributeGenerator>();
		for(CppAttributeGenerator attribute : this.attributes) {
			if(attribute.getVisibilityType() == type) {
				attributes.add(attribute);
			}
		}
		return attributes;
	}
	
	public boolean hasMethodsOfVisibilityType(MethodVisibilityType type) {
		for(CppMethodGenerator method : this.methods) {
			if(method.getVisibilityType() == type) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<CppMethodGenerator> getMethodsOfVisibilityType(MethodVisibilityType type) {
		ArrayList<CppMethodGenerator> methods = new ArrayList<CppMethodGenerator>();
		for(CppMethodGenerator method : this.methods) {
			if(method.getVisibilityType() == type) {
				methods.add(method);
			}
		}
		return methods;
	}
	
	public boolean hasDefaultConstructor() {
		for(CppMethodGenerator cppmg : this.methods) {
			if(cppmg.isConstructor() && cppmg.getArguments().size() == 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasDestructor() {
		for(CppMethodGenerator cppmg : this.methods) {
			if(cppmg.isDestructor()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isDefaultConstructor(CppMethodGenerator cppmg) {
		if(cppmg.isConstructor() && cppmg.getArguments().size() == 0) {
			return true;
		}
		return false;
	}
	
	public void setIncludePath(String include_path) {
		this.include_path = include_path;
	}
	
	public void setIncludePrepend(String include_prepend) {
		this.include_prepend = include_prepend;
	}
	
	public String getHeaderSource() {
		this.generate();
		return this.sb_h.toString();
	}
	
	public String getImplementationSource() {
		this.generate();
		return this.sb_i.toString();
	}
	
	private void generate() {
		this.sb_h = new StringBuilder();
		this.sb_i = new StringBuilder();
		
		//Creating Default Constructor Method
		CppMethodGenerator constructor = new CppMethodGenerator(this.name);
		constructor.setVisibility(MethodVisibilityType.PUBLIC);
		constructor.setConstructor();
		constructor.setNamespace(this.getNamespace());
		constructor.setClassname(this.name);
		
		//Creating Default Destructor Method
		CppMethodGenerator destructor = new CppMethodGenerator(this.name);
		destructor.setVisibility(MethodVisibilityType.PUBLIC);
		destructor.setVirtual();
		destructor.setDestructor();
		destructor.setNamespace(this.getNamespace());
		destructor.setClassname(this.name);
		
		//Header File
		this.sb_h.append("#ifndef _MODEL_" + AttributeGenerator.getAttributeName(this.name).toUpperCase() + "_H_\n");
		this.sb_h.append("#define _MODEL_" + AttributeGenerator.getAttributeName(this.name).toUpperCase() + "_H_\n\n");


		for(String standard_library : this.standard_header_libraries) {
			if(standard_library == null) {
				this.sb_h.append("\n");
			} else {
				this.sb_h.append("#include <" + standard_library + ">\n");
			}
		}
		for(String custom_library : this.custom_header_libraries) {
			if(custom_library == null) {
				this.sb_h.append("\n");
			} else {
				this.sb_h.append("#include \"" + custom_library + "\"\n");
			}
		}
		if(this.standard_header_libraries.size() > 0 || this.custom_header_libraries.size() > 0) {
			this.sb_h.append("\n");
		}
		
		for(String forward_declaration : this.forward_declarations) {
			this.sb_h.append(forward_declaration + "\n");
		}
		if(this.forward_declarations.size() > 0) {
			this.sb_h.append("\n");
		}
		
		// Enum definitions
		for(Enum e : this.enums) {
			this.sb_h.append(e.toString());
		}
		
		// Defining Namespaces
		int ns_cnt=0;
		for(String namespace : this.namespaces) {
			for(int i=0;i<ns_cnt;i++) {
				this.sb_h.append("\t");
			}
			this.sb_h.append("namespace " + namespace + " {\n");
			ns_cnt++;
		}
		if(this.namespaces.size() > 0) {
			this.sb_h.append("\n");
		}
		// Begin Class Definition
		this.sb_h.append(this.getNamespaceTabs() + "class " + ClassGenerator.getClassName(this.name));
		if(this.extended_classes.size() > 0) {
			this.sb_h.append(" : ");
			int cnt=0;
			for(String extended_class : this.extended_classes) {
				if(cnt > 0) {
					this.sb_h.append(", ");
				}
				this.sb_h.append("public " + extended_class);
				cnt++;
			}
		}
		this.sb_h.append(" {\n\n");
		
		// Adding private attributes and methods
		if( this.hasMethodsOfVisibilityType(MethodVisibilityType.PRIVATE) ||
			this.hasMembersOfVisibilityType(AttributeVisibilityType.PRIVATE)) {
			
			this.sb_h.append(this.getNamespaceTabs() + "\tprivate:\n");
			
			boolean newline = true;
			for(CppAttributeGenerator attribute : this.getAttributesOfVisibilityType(AttributeVisibilityType.PRIVATE)) {
				this.sb_h.append(this.getNamespaceTabs() + "\t\t" + attribute.getSource(AttributeType.DEFINITION));
				newline = true;
			}
			if(newline) {
				this.sb_h.append("\n");
				newline = false;
			}

			// Get Non-Default Constructors
			for(CppMethodGenerator method : this.getMethodsOfVisibilityType(MethodVisibilityType.PRIVATE)) {
				if(method.isConstructor()) {
					this.sb_h.append(this.getNamespaceTabs() + "\t\t" + method.getSource(MethodType.DEFINITION));
					newline = true;
				}
			}
			if(newline) {
				this.sb_h.append("\n");
				newline = false;
			}

			// Get Remaining Methods
			for(CppMethodGenerator method : this.getMethodsOfVisibilityType(MethodVisibilityType.PRIVATE)) {
				if(!method.isConstructor()) {
					this.sb_h.append(this.getNamespaceTabs() + "\t\t" + method.getSource(MethodType.DEFINITION));
					newline = true;
				}
			}
			if(newline) {
				this.sb_h.append("\n");
				newline = false;
			}
		}

		// Adding protected attributes and methods
		if( this.hasMethodsOfVisibilityType(MethodVisibilityType.PROTECTED) ||
			this.hasMembersOfVisibilityType(AttributeVisibilityType.PROTECTED)) {
			
			this.sb_h.append(this.getNamespaceTabs() + "\tprotected:\n");
			
			boolean newline = false;
			for(CppAttributeGenerator attribute : this.getAttributesOfVisibilityType(AttributeVisibilityType.PROTECTED)) {
				this.sb_h.append(this.getNamespaceTabs() + "\t\t" + attribute.getSource(AttributeType.DEFINITION));
				newline = true;
			}
			if(newline) {
				this.sb_h.append("\n");
				newline = false;
			}

			// Get Custom Constructors
			for(CppMethodGenerator method : this.getMethodsOfVisibilityType(MethodVisibilityType.PROTECTED)) {
				if(method.isConstructor()) {
					this.sb_h.append(this.getNamespaceTabs() + "\t\t" + method.getSource(MethodType.DEFINITION));
					this.sb_h.append("\n");
					newline = true;
				}
			}
			if(newline) {
				this.sb_h.append("\n");
				newline = false;
			}

			// Get Remaining Methods
			for(CppMethodGenerator method : this.getMethodsOfVisibilityType(MethodVisibilityType.PROTECTED)) {
				if(!method.isConstructor()) {
					this.sb_h.append(this.getNamespaceTabs() + "\t\t" + method.getSource(MethodType.DEFINITION));
					newline = true;
				}
			}
			if(newline) {
				this.sb_h.append("\n");
				newline = false;
			}
		}
		
		// Adding public attributes and methods
		if( this.hasMethodsOfVisibilityType(MethodVisibilityType.PUBLIC) ||
			this.hasMembersOfVisibilityType(AttributeVisibilityType.PUBLIC) ||
			(!this.hasDestructor())) {
			
			this.sb_h.append(this.getNamespaceTabs() + "\tpublic:\n");
			
			boolean newline = false;
			for(CppAttributeGenerator attribute : this.getAttributesOfVisibilityType(AttributeVisibilityType.PUBLIC)) {
				this.sb_h.append(this.getNamespaceTabs() + "\t\t" + attribute.getSource(AttributeType.DEFINITION));
				newline = true;
			}
			if(newline) {
				this.sb_h.append("\n");
				newline = false;
			}

			// Adding Default Constructor to Header
			if(!this.hasDefaultConstructor()) {
				this.sb_h.append(this.getNamespaceTabs() + "\t\t" + constructor.getSource(MethodType.DEFINITION));
				this.sb_h.append("\n");
			}
			// Get Custom Constructors
			for(CppMethodGenerator method : this.getMethodsOfVisibilityType(MethodVisibilityType.PUBLIC)) {
				if(method.isConstructor()) {
					this.sb_h.append(this.getNamespaceTabs() + "\t\t" + method.getSource(MethodType.DEFINITION));
					this.sb_h.append("\n");
				}
			}
			
			// Adding Destructor to Header
			if(!this.hasDestructor()) {
				this.sb_h.append(this.getNamespaceTabs() + "\t\t" + destructor.getSource(MethodType.DEFINITION));
				this.sb_h.append("\n");
			} else {
				for(CppMethodGenerator method : this.getMethodsOfVisibilityType(MethodVisibilityType.PUBLIC)) {
					if(method.isDestructor()) {
						this.sb_h.append(this.getNamespaceTabs() + "\t\t" + method.getSource(MethodType.DEFINITION));
						this.sb_h.append("\n");
					}
				}
			}
			
			this.sb_h.append("\n");
			
			// Get Remaining Methods
			for(CppMethodGenerator method : this.getMethodsOfVisibilityType(MethodVisibilityType.PUBLIC)) {
				if(!method.isConstructor() && !method.isDestructor()) {
					this.sb_h.append(this.getNamespaceTabs() + "\t\t" + method.getSource(MethodType.DEFINITION));
					this.sb_h.append("\n");
					newline = true;
				}
			}
			if(newline) {
				this.sb_h.append("\n");
				newline = false;
			}
		}
		
		this.sb_h.append(this.getNamespaceTabs() + "};\n\n");
		// End Class Definition
		
		
		for(; ns_cnt>0;ns_cnt--) {
			for(int i=1;i<ns_cnt;i++) {
				this.sb_h.append("\t");
			}
			this.sb_h.append("}\n");
		}
		
		this.sb_h.append("#endif /* _" + AttributeGenerator.getAttributeName(this.name).toUpperCase() + "_H_ */\n");
		
		
		
		
		
		
		
		//CPP File
		//Include Header File
		this.sb_i.append("#include \"" + this.include_prepend + "/" + ClassGenerator.getClassName(this.name) + ".h\"\n\n");
		
		for(CppAttributeGenerator attribute : CppAttributeGenerator.getGlobals()) {
			attribute.setNamespace(this.getNamespace());
			attribute.setClassname(this.name);
			this.sb_i.append(attribute.getSource(AttributeType.GLOBAL));
		}
		if(CppAttributeGenerator.getGlobals().size() > 0) {
			this.sb_i.append("\n");
		}

		for(String standard_library : this.standard_implementation_libraries) {
			if(standard_library == null) {
				this.sb_i.append("\n");
			} else {
				this.sb_i.append("#include <" + standard_library + ">\n");
			}
		}
		if(this.standard_implementation_libraries.size() > 0) {
			this.sb_i.append("\n");
		}
		for(String custom_library : this.custom_implementation_libraries) {
			if(custom_library == null) {
				this.sb_i.append("\n");
			} else {
				this.sb_i.append("#include \"" + custom_library + "\"\n");
			}
		}
		if(this.custom_implementation_libraries.size() > 0) {
			this.sb_i.append("\n");
		}
		
		for(String implementation_typedef : this.implementation_typedefs) {
			this.sb_i.append("typedef " + implementation_typedef + ";\n");
		}
		if(this.implementation_typedefs.size() > 0) {
			this.sb_i.append("\n");
		}
		
		boolean newline = false;
		for(CppAttributeGenerator a : this.attributes) {
			if(a.isStatic() && a.isConstant() && a.hasDefault()) {
				this.sb_i.append(a.getSource(AttributeType.CLASS));
				newline = true;
			}
		}
		if(newline) {
			this.sb_i.append("\n");
		}
		
		if(!this.hasDefaultConstructor()) {
			// Add non-const attributes with default values to the default param for default constructor
			for(CppAttributeGenerator a : this.attributes) {
				if(!a.isConstant() && a.hasDefault()) {
					constructor.addInitParam(new CppMethodGenerator.InitParam(a.name(), a.getDefault()));
				}
			}
			this.sb_i.append(constructor.getSource(MethodType.IMPLEMENTATION));
		}
		
		for(CppMethodGenerator method : this.methods) {
			if(method.isConstructor()) {
				// Add non-const attributes with default values to the default param for all other constructors
				for(CppAttributeGenerator a : this.attributes) {
					if(!a.isConstant() && a.hasDefault()) {
						method.addInitParam(new CppMethodGenerator.InitParam(a.name(), a.getDefault()));
					}
				}
				method.setNamespace(this.getNamespace());
				method.setClassname(this.name);
				this.sb_i.append(method.getSource(MethodType.IMPLEMENTATION));
			}
		}
		
		if(!this.hasDestructor()) {
			this.sb_i.append(destructor.getSource(MethodType.IMPLEMENTATION));
		}
		
		for(CppMethodGenerator method : this.methods) {
			if(!method.isConstructor()) {
				method.setNamespace(this.getNamespace());
				method.setClassname(this.name);
				this.sb_i.append(method.getSource(MethodType.IMPLEMENTATION));
			}
		}
	}
	
	public boolean save() {
		this.generate();

		this.sb.delete(0, this.sb.length());
		this.sb.append(this.sb_i);
		this.path = this.root_path + System.getProperty("file.separator") + ClassGenerator.getName(this.name,NamingSyntaxType.PASCAL, false) + ".cpp";
		
		if(super.save()) {
			if(this.include_path == null || this.include_path.equals("")) {
				this.include_path = this.root_path + System.getProperty("file.separator") + "include";
				File file = new File(this.include_path);
				if(!file.exists()) {
					file.mkdir();
				}
			}
			
			this.sb.delete(0, this.sb.length());
			this.sb.append(this.sb_h);
			this.path = this.include_path + System.getProperty("file.separator") + ClassGenerator.getName(this.name,NamingSyntaxType.PASCAL, false) + ".h";
			
			if(super.save()) {
				this.sb.delete(0, this.sb.length());
			} else {
				return false;
			}
		} else {
			return false;
		}
		
		return true;
	}
	
	
	
	
	
	

	
	private class Enum {
		private CppAttributeGenerator cppag = null;
		private List<String> types = new ArrayList<String>();
		
		public Enum(CppAttributeGenerator cppag, List<String> types) {
			this.cppag = cppag;
			this.types = types;
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			sb.append("namespace " + ClassGenerator.getClassName(cppag.getOriginalName()) + " {\n");
			sb.append("\tenum Enum {");
			int cnt = 0;
			for(String type : this.types) {
				if(cnt > 0) {
					sb.append(",");
				}
				sb.append(ClassGenerator.getName("\n\t\t" + type, ClassGenerator.NamingSyntaxType.UPPERCASE, false));
				cnt++;
			}
			sb.append("\n\t};\n");
			sb.append("}\n\n");
			
			return sb.toString();
		}
	}
	
	
	
	
	
	
	
	
}
