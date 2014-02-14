Classmaker is a Java library that provides an API for automatically generating code. For example, the following classmaker code:

	JavaClassGenerator jcg = new JavaClassGenerator("my java class", "/home/ccravens/", "com.ossys.myproject");
	
	JavaAttributeGenerator jag = new JavaAttributeGenerator("my class member", NamingSyntaxType.LOWERCASE);
	jag.setType(PrimitiveType.INT);
	jag.setVisibility(AttributeVisibilityType.PUBLIC);
	jag.setDefault(5);
	
	JavaMethodGenerator jmg1 = new JavaMethodGenerator("my class method");
	jmg1.setStatic();
	
	jcg.addAttribute(jag);
	jcg.addMethod(jmg1);
	jcg.save()

Results in the following Java class at /home/ccravens/MyJavaClass.java:

	package com.ossys.myproject;
	
	public class MyJavaClass {
		public int my_class_member = 5;
	
		public static void myClassMethod() { }
	
	}

The end goal of this project is to provide an easy-to-use Java API for creating source code in many different languages (C++, Java, PHP, Node.js, etc...) that is guaranteed valid source code.