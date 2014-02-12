package main;

import com.ossys.classmaker.sourcegenerator.attributegenerator.CppAttributeGenerator;
import com.ossys.classmaker.sourcegenerator.attributegenerator.AttributeGenerator.AttributeVisibilityType;
import com.ossys.classmaker.sourcegenerator.attributegenerator.CppAttributeGenerator.AttributeSignage;
import com.ossys.classmaker.sourcegenerator.attributegenerator.CppAttributeGenerator.PrimitiveType;
import com.ossys.classmaker.sourcegenerator.classgenerator.ClassGenerator;
import com.ossys.classmaker.sourcegenerator.classgenerator.CppClassGenerator;
import com.ossys.classmaker.sourcegenerator.classgenerator.CppClassGenerator.LibraryType;
import com.ossys.classmaker.sourcegenerator.methodgenerator.CppMethodGenerator;
import com.ossys.classmaker.sourcegenerator.methodgenerator.CppMethodGenerator.ReturnType;
import com.ossys.classmaker.sourcegenerator.methodgenerator.MethodGenerator.MethodVisibilityType;

public class RunCPPGeneration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/***
		 * CPP Class Generation
		 ***/
		CppClassGenerator ccg = new CppClassGenerator("Version Information", "/Users/ccravens/dev/java/AE/results/cpp");

		ccg.addNamespace("QB");
		ccg.addNamespace("Eurex");
		ccg.addNamespace("Model");

		ccg.extendsClass("qmsg");

		ccg.addHeaderLibrary(LibraryType.STANDARD, "Common/QuickFASTPch.h");
		ccg.addHeaderLibrary(LibraryType.STANDARD, "Messages/Message.h");
		ccg.addHeaderLibrary(LibraryType.STANDARD, "Common/StringBuffer.h");
		ccg.addHeaderLibrary(LibraryType.CUSTOM, "qmsg.h");

		CppAttributeGenerator cag = new CppAttributeGenerator("ID",ClassGenerator.NamingSyntaxType.LOWERCASE);
		cag.setVisibility(AttributeVisibilityType.PRIVATE);
		cag.setStatic();
		cag.setGlobal();
		cag.setSignage(AttributeSignage.UNSIGNED);
		cag.setType(PrimitiveType.LONG);
		cag.setDefault(1);
		ccg.addAttribute(cag);
		
		cag = new CppAttributeGenerator("M ID",ClassGenerator.NamingSyntaxType.LOWERCASE);
		cag.setVisibility(AttributeVisibilityType.PRIVATE);
		cag.setSignage(AttributeSignage.UNSIGNED);
		cag.setType(PrimitiveType.LONG);
		ccg.addAttribute(cag);
		
		cag = new CppAttributeGenerator("Vers No",ClassGenerator.NamingSyntaxType.LOWERCASE);
		cag.setVisibility(AttributeVisibilityType.PRIVATE);
		cag.setSignage(AttributeSignage.UNSIGNED);
		cag.setType(PrimitiveType.INT);
		ccg.addAttribute(cag);
		
		cag = new CppAttributeGenerator("Src Id",ClassGenerator.NamingSyntaxType.LOWERCASE);
		cag.setVisibility(AttributeVisibilityType.PRIVATE);
		cag.setSignage(AttributeSignage.UNSIGNED);
		cag.setType(PrimitiveType.INT);
		ccg.addAttribute(cag);
		
		cag = new CppAttributeGenerator("Seq Num",ClassGenerator.NamingSyntaxType.LOWERCASE);
		cag.setVisibility(AttributeVisibilityType.PRIVATE);
		cag.setSignage(AttributeSignage.UNSIGNED);
		cag.setType(PrimitiveType.INT);
		ccg.addAttribute(cag);
		
		cag = new CppAttributeGenerator("Reserved",ClassGenerator.NamingSyntaxType.LOWERCASE);
		cag.setVisibility(AttributeVisibilityType.PRIVATE);
		cag.setType("QuickFAST::StringBuffer");
		ccg.addAttribute(cag);
		
		// TODO: Make it so that creating a constructor does not require passing of the class name
		// to the constructor: CppMethodGenerator cmg = new CppMethodGenerator(); cmg.setConstructor();
		CppMethodGenerator cmg = new CppMethodGenerator("Version Information");
		cmg.setVisibility(MethodVisibilityType.PUBLIC);
		
		cag = new CppAttributeGenerator("Message", ClassGenerator.NamingSyntaxType.LOWERCASE);
		cag.setConstant();
		cag.setType("QuickFAST::Messages::Message");
		cag.setPassByReference();
		cmg.addArgument(cag);
		
		cmg.setConstructor();
		ccg.addMethod(cmg);
		
		cmg = new CppMethodGenerator("Print");
		cmg.setVisibility(MethodVisibilityType.PUBLIC);
		ccg.addMethod(cmg);
		
		cmg = new CppMethodGenerator("");
		cmg.sets("Vers No");
		cmg.setVisibility(MethodVisibilityType.PUBLIC);
		cmg.addCode("\tthis->vers_no = vers_no;\n");
		
		cag = new CppAttributeGenerator("Vers No", ClassGenerator.NamingSyntaxType.LOWERCASE);
		cag.setSignage(AttributeSignage.UNSIGNED);
		cag.setType(PrimitiveType.INT);
		cmg.addArgument(cag);
		
		ccg.addMethod(cmg);
		
		cmg = new CppMethodGenerator("Set Src Id");
		cmg.setVisibility(MethodVisibilityType.PUBLIC);
		
		cag = new CppAttributeGenerator("Src Id", ClassGenerator.NamingSyntaxType.LOWERCASE);
		cag.setSignage(AttributeSignage.UNSIGNED);
		cag.setType(PrimitiveType.INT);
		cmg.addArgument(cag);
		
		ccg.addMethod(cmg);
		
		cmg = new CppMethodGenerator("Set Seq Num");
		cmg.setVisibility(MethodVisibilityType.PUBLIC);
		
		cag = new CppAttributeGenerator("Seq Num", ClassGenerator.NamingSyntaxType.LOWERCASE);
		cag.setSignage(AttributeSignage.UNSIGNED);
		cag.setType(PrimitiveType.INT);
		cmg.addArgument(cag);
		
		ccg.addMethod(cmg);
		
		cmg = new CppMethodGenerator("Get Id");
		cmg.setVisibility(MethodVisibilityType.PUBLIC);
		cmg.setConstant();
		cmg.setReturnSignage(AttributeSignage.UNSIGNED);
		cmg.setReturnType(ReturnType.LONG);
		ccg.addMethod(cmg);
		
		cmg = new CppMethodGenerator("Get Vers No");
		cmg.setVisibility(MethodVisibilityType.PUBLIC);
		cmg.setConstant();
		cmg.setReturnSignage(AttributeSignage.UNSIGNED);
		cmg.setReturnType(ReturnType.INT);
		ccg.addMethod(cmg);
		
		cmg = new CppMethodGenerator("Get Src Id");
		cmg.setVisibility(MethodVisibilityType.PUBLIC);
		cmg.setConstant();
		cmg.setReturnSignage(AttributeSignage.UNSIGNED);
		cmg.setReturnType(ReturnType.INT);
		ccg.addMethod(cmg);
		
		cmg = new CppMethodGenerator("Get Seq Num");
		cmg.setVisibility(MethodVisibilityType.PUBLIC);
		cmg.setConstant();
		cmg.setReturnSignage(AttributeSignage.UNSIGNED);
		cmg.setReturnType(ReturnType.INT);
		ccg.addMethod(cmg);
		
		System.out.println(ccg.getSource());
		
		if(ccg.save()) {
			System.out.println("Successfully Saved CPP Class!");
		} else {
			System.err.println("Error Saving CPP Class");
		}
	}

}
