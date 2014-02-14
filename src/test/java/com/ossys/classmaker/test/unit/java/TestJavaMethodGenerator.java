package com.ossys.classmaker.test.unit.java;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ossys.classmaker.sourcegenerator.attributegenerator.AttributeGenerator.AttributeVisibilityType;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator.PrimitiveType;
import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;
import com.ossys.classmaker.sourcegenerator.classgenerator.JavaClassGenerator;
import com.ossys.classmaker.sourcegenerator.methodgenerator.JavaMethodGenerator;
import com.ossys.classmaker.sourcegenerator.methodgenerator.JavaMethodGenerator.MethodType;

public class TestJavaMethodGenerator {

	@Test
	public void testClassMethodGeneratorName() {
		JavaMethodGenerator jmg = new JavaMethodGenerator("Test Method");

		String[] parts = jmg.getSource(MethodType.CLASS).split("\n");
		assertEquals("\tpublic void testMethod() { }", parts[0]);
	}

	@Test
	public void testInterfaceMethodGeneratorName() {
		JavaMethodGenerator jmg = new JavaMethodGenerator("Test Method");

		String[] parts = jmg.getSource(MethodType.INTERFACE).split("\n");
		assertEquals("\tpublic void testMethod();", parts[0]);
	}

	@Test
	public void testClassMethodGeneratorComment() {
		JavaMethodGenerator jmg = new JavaMethodGenerator("Test Method");
		jmg.addComment("\t//Test Comment");

		String[] parts = jmg.getSource(MethodType.INTERFACE).split("\n");
		assertEquals("\t//Test Comment", parts[0]);
		assertEquals("\tpublic void testMethod();", parts[1]);
	}

}
