package main;

import com.purlogic.classmaker.sourcegenerator.classgenerator.ClassGenerator;

public class RunNamingTests {

	public static void main(String[] args) {
		System.out.println(ClassGenerator.adjectivize("own"));
		System.out.println(ClassGenerator.adjectivize("manage"));
		System.out.println(ClassGenerator.adjectivize("snag"));
		System.out.println(ClassGenerator.adjectivize("complete"));
		System.out.println(ClassGenerator.adjectivize("result"));
	}

}
