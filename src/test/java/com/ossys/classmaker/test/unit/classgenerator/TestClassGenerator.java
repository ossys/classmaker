package com.ossys.classmaker.test.unit.classgenerator;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator;
import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator.NamingSyntaxType;

public class TestClassGenerator {

	@Test
	public void testClassName() {
		assertEquals("Test", ClassGenerator.getClassName("Test"));
		assertEquals("TestName", ClassGenerator.getClassName("Test Name"));
		assertEquals("Test", ClassGenerator.getClassName("TEST"));
		assertEquals("TestName", ClassGenerator.getClassName("TEST NAME"));
		assertEquals("Test", ClassGenerator.getClassName("test"));
		assertEquals("TestName", ClassGenerator.getClassName("test name"));
	}
	
	@Test
	public void testPluralClassName() {
		assertEquals("Tests", ClassGenerator.getPluralizedClassName("Test"));
		assertEquals("Assemblies", ClassGenerator.getPluralizedClassName("Assembly"));
		assertEquals("Classes", ClassGenerator.getPluralizedClassName("Class"));
	}
	
	@Test
	public void testPluralize() {
		assertEquals("tests", ClassGenerator.pluralize("test"));
		assertEquals("TESTS", ClassGenerator.pluralize("TEST"));
		assertEquals("assemblies", ClassGenerator.pluralize("assembly"));
		assertEquals("ASSEMBLIES", ClassGenerator.pluralize("ASSEMBLY"));
		assertEquals("Classes", ClassGenerator.pluralize("Class"));
		assertEquals("CLASSES", ClassGenerator.pluralize("CLASS"));
	}
	
	@Test
	public void testClassGeneratorName() {
		ClassGenerator cg1 = new ClassGenerator("test");
		assertEquals("Test", cg1.name());
		
		ClassGenerator cg2 = new ClassGenerator("test another");
		assertEquals("TestAnother", cg2.name());
	}
	
	@Test
	public void testClassGeneratorNamingSyntax() {
		ClassGenerator cg1 = new ClassGenerator("test class","",NamingSyntaxType.LOWERCASE);
		assertEquals("test_class", cg1.name());

		ClassGenerator cg2 = new ClassGenerator("test class","",NamingSyntaxType.CAMELCASE);
		assertEquals("testClass", cg2.name());

		ClassGenerator cg3 = new ClassGenerator("test class","",NamingSyntaxType.PASCAL);
		assertEquals("TestClass", cg3.name());

		ClassGenerator cg4 = new ClassGenerator("test class","",NamingSyntaxType.UPPERCASE);
		assertEquals("TEST_CLASS", cg4.name());
	}
	
	@Test
	public void testClassGeneratorOriginalName() {
		ClassGenerator cg1 = new ClassGenerator("test","",NamingSyntaxType.LOWERCASE);
		assertEquals("test", cg1.getOriginalName());
		
		ClassGenerator cg2 = new ClassGenerator("test class","",NamingSyntaxType.LOWERCASE);
		assertEquals("test class", cg2.getOriginalName());
		
		ClassGenerator cg3 = new ClassGenerator("Test Class","",NamingSyntaxType.LOWERCASE);
		assertEquals("Test Class", cg3.getOriginalName());
		
		ClassGenerator cg4 = new ClassGenerator("TEST CLASS","",NamingSyntaxType.LOWERCASE);
		assertEquals("TEST CLASS", cg4.getOriginalName());
	}
	
	@Test
	public void testClassGetName() {
		assertEquals("test_class", ClassGenerator.getName("test class", NamingSyntaxType.LOWERCASE, false));
		assertEquals("testClass", ClassGenerator.getName("test class", NamingSyntaxType.CAMELCASE, false));
		assertEquals("TestClass", ClassGenerator.getName("test class", NamingSyntaxType.PASCAL, false));
		assertEquals("TEST_CLASS", ClassGenerator.getName("test class", NamingSyntaxType.UPPERCASE, false));
	}
	
	@Test
	public void testClassGetNamePlural() {
		assertEquals("test_tests", ClassGenerator.getName("test test", NamingSyntaxType.LOWERCASE, true));
		assertEquals("testTests", ClassGenerator.getName("test test", NamingSyntaxType.CAMELCASE, true));
		assertEquals("TestTests", ClassGenerator.getName("test test", NamingSyntaxType.PASCAL, true));
		assertEquals("TEST_TESTS", ClassGenerator.getName("test test", NamingSyntaxType.UPPERCASE, true));
		
		assertEquals("test_assemblies", ClassGenerator.getName("test assembly", NamingSyntaxType.LOWERCASE, true));
		assertEquals("testAssemblies", ClassGenerator.getName("test assembly", NamingSyntaxType.CAMELCASE, true));
		assertEquals("TestAssemblies", ClassGenerator.getName("test assembly", NamingSyntaxType.PASCAL, true));
		assertEquals("TEST_ASSEMBLIES", ClassGenerator.getName("test assembly", NamingSyntaxType.UPPERCASE, true));
		
		assertEquals("test_classes", ClassGenerator.getName("test class", NamingSyntaxType.LOWERCASE, true));
		assertEquals("testClasses", ClassGenerator.getName("test class", NamingSyntaxType.CAMELCASE, true));
		assertEquals("TestClasses", ClassGenerator.getName("test class", NamingSyntaxType.PASCAL, true));
		assertEquals("TEST_CLASSES", ClassGenerator.getName("test class", NamingSyntaxType.UPPERCASE, true));
	}
	
	@Test
	public void testClassGetNameNull() {
		assertEquals("", ClassGenerator.getName(null, NamingSyntaxType.UPPERCASE, true));
	}

}
