/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.classgenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 *
 */
public class ClassGenerator implements ClassGeneratorInterface {
	StringBuilder sb = new StringBuilder();
	
	protected String name = "";
	protected String extension = "";
	protected String path = null;
	protected boolean stc = false;
	protected ArrayList<String> libraries = new ArrayList<String>();
	protected ArrayList<String> extended_classes = new ArrayList<String>();
	protected StringBuilder comment = new StringBuilder();
	
	protected int num_constructors = 0;
	protected int num_attributes = 0;
	protected int num_methods = 0;
	protected int num_subclasses = 0;
	
	public static enum ClassVisibilityType {
		PRIVATE,
		PROTECTED,
		PUBLIC
	}
	
	public static enum NamingSyntaxType {
		LOWERCASE,
		UPPERCASE,
		CAMELCASE,
		PASCAL
	}
	protected NamingSyntaxType namingSyntaxType = NamingSyntaxType.PASCAL;
	
	protected ClassVisibilityType visibilityType = null;
	
	public ClassGenerator(String name) {
		this.name = name;
	}
	
	public ClassGenerator(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	public ClassGenerator(String name, String path, NamingSyntaxType type) {
		this.name = name;
		this.path = path;
		this.namingSyntaxType = type;
	}
	
	public void addLibrary(String library) {
		if(library == null) {
			this.libraries.add(null);
		} else if(!this.libraries.contains(library)) {
			this.libraries.add(library);
		}
	}
	
	public void addComment(String comment) {
		this.comment.append(comment);
	}
	
	public void extendsClass(String extended_class) {
		this.extended_classes.add(extended_class);
	}
	
	public void addConstructor() {
		this.num_constructors++;
	}

	public void addAttribute() {
		this.num_attributes++;
	}
	
	public void addMethod() {
		this.num_methods++;
	}
	
	public void addSubclass() {
		this.num_subclasses++;
	}
	
	public void addExtendedClass(String extended_class) {
		this.extended_classes.add(extended_class);
	}
	
	public String newLine() {
		return "\n";
	}
	
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public void setStatic() {
		this.stc = true;
	}
	
	public boolean isStatic() {
		return this.stc;
	}
	
	public void setVisibility(ClassVisibilityType visibilityType) {
		this.visibilityType = visibilityType;
	}
	
	public ClassVisibilityType getVisibility() {
		return this.visibilityType;
	}
	
	public String getOriginalName() {
		return this.name;
	}
	
	public String name() {
		return ClassGenerator.getName(this.name, this.namingSyntaxType, false);
	}
	
	public boolean exists() {
		return new File(this.path).exists();
	}
	
	public int getLinesOfCode() {
		Matcher m = Pattern.compile("^(?![ \\s]*\\r?\\n|import|package|[ \\s]*}\\r?\\n|[ \\s]*//|[ \\s]*/\\*|[ \\s]*\\*).*\\r?\\n").matcher(sb.toString());
		int cnt = 0;
		while(m.find()) {
			cnt++;
		}
		return cnt;
	}
	
	public boolean save(boolean overwrite) {
		boolean exists = new File(this.path).exists();
		if(!exists) {
			return save();
		} else if(exists && overwrite) {
			return save();
		} else {
			return false;
		}
	}

	public boolean save() {
		boolean success = false;
		
		FileWriter out = null;
		try {
			out = new FileWriter(this.path);
			out.write(this.sb.toString());
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}  finally {
			if(out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return success;
	}
	
	public static String getName(String name, ClassGenerator.NamingSyntaxType type, boolean plural) {
		StringBuilder sb = new StringBuilder();
		
		name = name.replaceAll("[^A-Za-z0-9\\s_]", "");
		
		if(name != null && name.length() > 0) {
			String[] parts = name.split(" ");
			int cnt = 0;
			
			switch(type) {
				case LOWERCASE:
					for(String s : parts) {
						if(cnt > 0) {
							sb.append("_");
						}
						sb.append(s.toLowerCase());
						cnt++;
					}
					break;
				case UPPERCASE:
					for(String s : parts) {
						if(cnt > 0) {
							sb.append("_");
						}
						sb.append(s.toUpperCase());
						cnt++;
					}
					break;
				case CAMELCASE:
					for(String s : parts) {
						if(cnt > 0) {
							sb.append(s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase());
						} else {
							sb.append(s.toLowerCase());
						}
						cnt++;
					}
					break;
				case PASCAL:
					for(String s : parts) {
						sb.append(s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase());
					}
					break;
			}
			
			if(plural) {
				if(name.substring(name.length()-1).equalsIgnoreCase("y")) {
					if(type == NamingSyntaxType.UPPERCASE) {
						sb.deleteCharAt(sb.length()-1);
						sb.append("IES");
					} else {
						sb.deleteCharAt(sb.length()-1);
						sb.append("ies");
					}
				} else if(name.substring(name.length()-1).equalsIgnoreCase("s")) {
					if(type == NamingSyntaxType.UPPERCASE) {
						sb.append("ES");
					} else {
						sb.append("es");
					}
				} else {
					if(type == NamingSyntaxType.UPPERCASE) {
						sb.append("S");
					} else {
						sb.append("s");
					}
				}
			}
			
		}
		
		return sb.toString();
	}
	
	public static String pluralize(String name) {
		if(name.substring(name.length()-1).equals("y")) {
			name = name.substring(0, name.length()-1) + "ies";
		} else if(name.substring(name.length()-1).equals("Y")) {
			name = name.substring(0, name.length()-1) + "IES";
		} else if(name.substring(name.length()-1).equals("s")) {
			name = name.substring(0, name.length()) + "es";
		} else if(name.substring(name.length()-1).equals("S")) {
			name = name.substring(0, name.length()) + "ES";
		} else if(Character.isUpperCase(name.charAt(name.length()-1))) {
			name = name.substring(0, name.length()) + "S";
		} else {
			name = name.substring(0, name.length()) + "s";
		}
		return name;
	}

	public static String getClassName(String name) {
		return ClassGenerator.getName(name, NamingSyntaxType.PASCAL, false);
	}

	public static String getPluralizedClassName(String name) {
		return ClassGenerator.getName(name, NamingSyntaxType.PASCAL, true);
	}
	
}
