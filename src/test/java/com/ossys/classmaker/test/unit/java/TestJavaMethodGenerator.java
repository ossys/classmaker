package com.ossys.classmaker.test.unit.java;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ossys.classmaker.sourcegenerator.methodgenerator.JavaMethodGenerator;
import com.ossys.classmaker.sourcegenerator.methodgenerator.JavaMethodGenerator.MethodType;

public class TestJavaMethodGenerator {

	@Test
	public void testClassMethodGeneratorName() {
		JavaMethodGenerator jmg = new JavaMethodGenerator("Test Method");

		String[] parts = jmg.getSource(MethodType.CLASS).split("\n");
		assertEquals("\tpublic void testMethod() { }", parts[0]);
	}

}
