package main;

import com.purlogic.classmaker.sourcegenerator.classgenerator.PhpClassGenerator;
import com.purlogic.classmaker.sourcegenerator.methodgenerator.PhpMethodGenerator;

public class RunPHPGeneration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PhpClassGenerator pcg = new PhpClassGenerator("Php Class", "/Users/ccravens/dev/java/AE/results/php");
		
		PhpMethodGenerator pmg = new PhpMethodGenerator("Test Method");
		pmg.addCode("\t\t//return;");
		
		pcg.addMethod(pmg);
		System.out.println(pcg.getSource());
		
		if(pcg.save()) {
			System.out.println("Successfully Saved PHP Class!");
		} else {
			System.err.println("Error Saving PHP Class");
		}
	}

}
