/**
 * 
 */
package com.ossys.classmaker.sourcegenerator.classgenerator;

import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.ClassVisibilityType;

/**
 * @author Administrator
 *
 */
public interface ClassGeneratorInterface {
	
	public void addLibrary(String lib);
	
	public void addConstructor();

	public void addAttribute();
	
	public void addMethod();
	
	public void addSubclass();
	
	public String newLine();
	
	public void setVisibility(ClassVisibilityType visibilityType);
	
	public boolean save();
	
}