package com.ossys.classmaker.test.unit.java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ossys.classmaker.sourcegenerator.attributegenerator.AttributeGenerator.AttributeVisibilityType;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator.PrimitiveType;
import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.ClassVisibilityType;
import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;
import com.ossys.classmaker.sourcegenerator.classgenerator.JavaClassGenerator;
import com.ossys.classmaker.sourcegenerator.methodgenerator.JavaMethodGenerator;

public class TestJavaClassGenerator {
	
	private static final String pkg = "com.ossys.test";

	@Test
	public void testJavaClass() {
		JavaClassGenerator jcg = new JavaClassGenerator("Test Class");
		
		String[] parts = jcg.getSource().split("\n");
		assertEquals("public class TestClass {", parts[0]);
		assertEquals("}", parts[2]);
	}

	@Test
	public void testJavaClassPackage() {
		JavaClassGenerator jcg = new JavaClassGenerator("Test Class","",pkg);
		
		String[] parts = jcg.getSource().split("\n");
		assertEquals("package " + pkg + ";", parts[0]);
	}

	@Test
	public void testJavaClassVisibility() {
		JavaClassGenerator jcg1 = new JavaClassGenerator("Test Class","",pkg);
		jcg1.setVisibility(ClassVisibilityType.PRIVATE);
		JavaClassGenerator jcg2 = new JavaClassGenerator("Test Class","",pkg);
		jcg2.setVisibility(ClassVisibilityType.PROTECTED);
		
		String[] parts1 = jcg1.getSource().split("\n");
		assertEquals("private class TestClass {", parts1[2]);
		
		String[] parts2 = jcg2.getSource().split("\n");
		assertEquals("protected class TestClass {", parts2[2]);
	}

	@Test
	public void testJavaClassInheritance() {
		JavaClassGenerator jcg1 = new JavaClassGenerator("Test Class","",pkg);
		jcg1.extendsClass("SuperClass");

		JavaClassGenerator jcg2 = new JavaClassGenerator("Test Class","",pkg);
		jcg2.extendsClass("SuperClass1");
		jcg2.extendsClass("SuperClass2");
		
		String[] parts1 = jcg1.getSource().split("\n");
		assertEquals("public class TestClass extends SuperClass {", parts1[2]);
		
		String[] parts2 = jcg2.getSource().split("\n");
		assertEquals("public class TestClass extends SuperClass1, SuperClass2 {", parts2[2]);
	}

	@Test
	public void testJavaClassAnnotation() {
		JavaClassGenerator jcg = new JavaClassGenerator("Test Class","",pkg);
		jcg.addAnnotation("@Test");
		
		String[] parts = jcg.getSource().split("\n");
		assertEquals("@Test", parts[2]);
	}

	@Test
	public void testJavaClassConstructor() {
		JavaClassGenerator jcg = new JavaClassGenerator("Test Class","",pkg);
		JavaMethodGenerator jmg = new JavaMethodGenerator();
		jmg.setConstructor();
		jcg.addMethod(jmg);

		String[] parts = jcg.getSource().split("\n");
		assertEquals("\tpublic TestClass() { }", parts[4]);
	}
	
	@Test
	public void testJavaClassMethodSetter() {
		JavaClassGenerator jcg = new JavaClassGenerator("Test Class","",pkg);
		JavaMethodGenerator jmg = new JavaMethodGenerator();
		jmg.sets("some var");
		jcg.addMethod(jmg);

		String[] parts = jcg.getSource().split("\n");
		assertEquals("\tpublic void setSomeVar() { }", parts[4]);
	}
	
	@Test
	public void testJavaClassMethodGetter() {
		JavaClassGenerator jcg = new JavaClassGenerator("Test Class","",pkg);
		JavaMethodGenerator jmg = new JavaMethodGenerator();
		jmg.gets("some var");
		jcg.addMethod(jmg);

		String[] parts = jcg.getSource().split("\n");
		assertEquals("\tpublic void getSomeVar() { }", parts[4]);
	}

	@Test
	public void testJavaClassSubclass() {
		JavaClassGenerator jcg1 = new JavaClassGenerator("Test Class","",pkg);
		JavaClassGenerator jcg2 = new JavaClassGenerator("Test Subclass","",pkg);
		jcg1.addSubclass(jcg2);
		
		String[] parts = jcg1.getSource().split("\n");
		assertEquals("public class TestSubclass {", parts[4]);
		assertEquals("", parts[5]);
		assertEquals("}", parts[6]);
		assertTrue(jcg2.getSubclass());
	}

	@Test
	public void testJavaClassStaticCodeblock() {
		JavaClassGenerator jcg = new JavaClassGenerator("Test Class","",pkg);
		StringBuilder sb = new StringBuilder();
		sb.append("\t\tint i=0;\n");
		sb.append("\t\ti++;\n");
		sb.append("\t\ti--;");
		jcg.addStaticCodeBlock(sb.toString());

		String[] parts = jcg.getSource().split("\n");
		assertEquals("\tstatic {", parts[3]);
		assertEquals("\t\tint i=0;", parts[4]);
		assertEquals("\t\ti++;", parts[5]);
		assertEquals("\t\ti--;", parts[6]);
		assertEquals("\t}", parts[7]);
	}

	@Test
	public void testJavaClassEnumVariable() {
		JavaClassGenerator jcg1 = new JavaClassGenerator("Test Class","",pkg);
		JavaAttributeGenerator jag1 = new JavaAttributeGenerator("var", NamingSyntaxType.LOWERCASE);
		List<String> list1 = new ArrayList<String>();
		list1.add("type 1");
		list1.add("type 2");
		list1.add("type 3");
		jcg1.addEnumClass(jag1, list1);
		
		String[] parts1 = jcg1.getSource().split("\n");
		assertEquals("\tprivate enum Var {", parts1[3]);
		assertEquals("\t\tTYPE_1,", parts1[4]);
		assertEquals("\t\tTYPE_2,", parts1[5]);
		assertEquals("\t\tTYPE_3", parts1[6]);
		assertEquals("\t}", parts1[7]);
		
		JavaClassGenerator jcg2 = new JavaClassGenerator("Test Class","",pkg);
		JavaAttributeGenerator jag2 = new JavaAttributeGenerator("var", NamingSyntaxType.LOWERCASE);
		jag2.setVisibility(AttributeVisibilityType.PRIVATE);
		List<String> list2 = new ArrayList<String>();
		list2.add("type 1");
		list2.add("type 2");
		list2.add("type 3");
		jcg2.addEnumClass(jag2, list2);
		
		String[] parts2 = jcg2.getSource().split("\n");
		assertEquals("\tprivate enum Var {", parts2[3]);
		assertEquals("\t\tTYPE_1,", parts2[4]);
		assertEquals("\t\tTYPE_2,", parts2[5]);
		assertEquals("\t\tTYPE_3", parts2[6]);
		assertEquals("\t}", parts2[7]);
		
		JavaClassGenerator jcg3 = new JavaClassGenerator("Test Class","",pkg);
		JavaAttributeGenerator jag3 = new JavaAttributeGenerator("var", NamingSyntaxType.LOWERCASE);
		jag3.setVisibility(AttributeVisibilityType.PROTECTED);
		List<String> list3 = new ArrayList<String>();
		list3.add("type 1");
		list3.add("type 2");
		list3.add("type 3");
		jcg3.addEnumClass(jag3, list3);
		
		String[] parts3 = jcg3.getSource().split("\n");
		assertEquals("\tprotected enum Var {", parts3[3]);
		assertEquals("\t\tTYPE_1,", parts3[4]);
		assertEquals("\t\tTYPE_2,", parts3[5]);
		assertEquals("\t\tTYPE_3", parts3[6]);
		assertEquals("\t}", parts3[7]);
		
		JavaClassGenerator jcg4 = new JavaClassGenerator("Test Class","",pkg);
		JavaAttributeGenerator jag4 = new JavaAttributeGenerator("var", NamingSyntaxType.LOWERCASE);
		jag4.setVisibility(AttributeVisibilityType.PUBLIC);
		List<String> list4 = new ArrayList<String>();
		list4.add("type 1");
		list4.add("type 2");
		list4.add("type 3");
		jcg4.addEnumClass(jag4, list4);
		
		String[] parts4 = jcg4.getSource().split("\n");
		assertEquals("\tpublic enum Var {", parts4[3]);
		assertEquals("\t\tTYPE_1,", parts4[4]);
		assertEquals("\t\tTYPE_2,", parts4[5]);
		assertEquals("\t\tTYPE_3", parts4[6]);
		assertEquals("\t}", parts4[7]);

		JavaClassGenerator jcg5 = new JavaClassGenerator("Test Class","",pkg);
		JavaAttributeGenerator jag5 = new JavaAttributeGenerator("var", NamingSyntaxType.LOWERCASE);
		jag5.setVisibility(AttributeVisibilityType.PUBLIC);
		jag5.setStatic();
		List<String> list5 = new ArrayList<String>();
		list5.add("type 1");
		list5.add("type 2");
		list5.add("type 3");
		jcg5.addEnumClass(jag5, list5);
		
		String[] parts5 = jcg5.getSource().split("\n");
		assertEquals("\tpublic static enum Var {", parts5[3]);
		assertEquals("\t\tTYPE_1,", parts5[4]);
		assertEquals("\t\tTYPE_2,", parts5[5]);
		assertEquals("\t\tTYPE_3", parts5[6]);
		assertEquals("\t}", parts5[7]);
	}

	@Test
	public void testJavaClassLibrary() {
		JavaClassGenerator jcg = new JavaClassGenerator("Test Class","",pkg);
		jcg.addLibrary("com.ossys.lib1");
		jcg.addLibrary("com.ossys.lib2");
		jcg.addLibrary(null);
		jcg.addLibrary("com.ossys.lib3");

		String[] parts = jcg.getSource().split("\n");
		assertEquals("import com.ossys.lib1;", parts[2]);
		assertEquals("import com.ossys.lib2;", parts[3]);
		assertEquals("", parts[4]);
		assertEquals("import com.ossys.lib3;", parts[5]);
	}

	@Test
	public void testJavaClassVariable() {
		JavaClassGenerator jcg = new JavaClassGenerator("Test Class","",pkg);
		JavaAttributeGenerator jag1 = new JavaAttributeGenerator("var1", NamingSyntaxType.LOWERCASE);
		jag1.setType(PrimitiveType.INT);
		jag1.setDefault(5);
		
		JavaAttributeGenerator jag2 = new JavaAttributeGenerator("var2", NamingSyntaxType.LOWERCASE);
		jag2.setType(PrimitiveType.FLOAT);
		jag2.setDefault(2);

		jcg.addAttribute(jag1);
		jcg.addAttribute(null);
		jcg.addAttribute(jag2);
		
		String[] parts = jcg.getSource().split("\n");
		assertEquals("\tpublic int var1 = 5;", parts[3]);
		assertEquals("", parts[4]);
		assertEquals("\tpublic float var2 = 2F;", parts[5]);
	}

	@Test
	public void testJavaClassMethod() {
		JavaClassGenerator jcg = new JavaClassGenerator("Test Class","",pkg);
		JavaMethodGenerator jmg = new JavaMethodGenerator("Method Name");
		jcg.addMethod(jmg);
		
		String[] parts = jcg.getSource().split("\n");
		assertEquals("\tpublic void methodName() { }", parts[4]);
	}

	@Test
	public void testJavaClassMethodAnnotation() {
		JavaClassGenerator jcg = new JavaClassGenerator("Test Class","",pkg);
		JavaMethodGenerator jmg = new JavaMethodGenerator("Method Name");
		jcg.addMethod(jmg);
		
		String[] parts = jcg.getSource().split("\n");
		assertEquals("\tpublic void methodName() { }", parts[4]);
	}

}
