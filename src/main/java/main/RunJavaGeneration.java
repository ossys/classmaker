package main;

import com.purlogic.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator;
import com.purlogic.classmaker.sourcegenerator.attributegenerator.JavaAttributeGenerator.PrimitiveType;
import com.purlogic.classmaker.sourcegenerator.classgenerator.ClassGenerator;
import com.purlogic.classmaker.sourcegenerator.classgenerator.JavaClassGenerator;
import com.purlogic.classmaker.sourcegenerator.classgenerator.JavaInterfaceGenerator;
import com.purlogic.classmaker.sourcegenerator.methodgenerator.JavaMethodGenerator;

public class RunJavaGeneration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/***
		 * Java Class Generation
		 ***/
		JavaClassGenerator jcg = new JavaClassGenerator("Java Class", "/Users/ccravens/dev/java/AE/results/java", "java");
		
		JavaMethodGenerator jmg = new JavaMethodGenerator("test method");
		jmg.addCode("\t\treturn;");
		
		jcg.addMethod(jmg);
		System.out.println(jcg.getSource());
		
		if(jcg.save()) {
			System.out.println("Successfully Saved Java Class!");
		} else {
			System.err.println("Error Saving Java Class");
		}

		
		/***
		 * Java Interface Generation
		 ***/
		JavaInterfaceGenerator jig = new JavaInterfaceGenerator("Test Interface", "/Users/ccravens/dev/java/AE/results/java", "java");
		jig.addLibrary("com.package.library");
		jig.extendsClass("ExtendedClass1");
		jig.extendsClass("ExtendedClass2");
		
		JavaAttributeGenerator jag = new JavaAttributeGenerator("Attribute", ClassGenerator.NamingSyntaxType.LOWERCASE);
		jag.setConstant();
		jag.setType(PrimitiveType.DOUBLE);
		jig.addAttribute(jag);
		
		jmg = new JavaMethodGenerator("Set Something");
		jig.addMethod(jmg);
		
		System.out.println(jig.getSource());
		
		if(jig.save()) {
			System.out.println("Successfully Saved Java Interface!");
		} else {
			System.err.println("Error Saving Saving Java Interface");
		}
	}

}
