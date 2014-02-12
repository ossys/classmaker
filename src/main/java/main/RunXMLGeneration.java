package main;

import com.ossys.classmaker.modelgenerator.xml.XMLAttributeGenerator;
import com.ossys.classmaker.modelgenerator.xml.XMLElementGenerator;
import com.ossys.classmaker.modelgenerator.xml.XMLModelGenerator;


public class RunXMLGeneration {
	public static void main(String[] args) {
		/***
		 * XML Generation
		 ***/
		XMLModelGenerator xmg = new XMLModelGenerator("XML Model", "/Users/ccravens/dev/java/AE/results/xml");

		XMLElementGenerator xeg = new XMLElementGenerator("first",null);

		XMLElementGenerator xeg2 = new XMLElementGenerator("second",xeg);
		XMLAttributeGenerator xag = new XMLAttributeGenerator("attr1", "1");
		xeg2.addAttribute(xag);
		xag = null;
		xag = new XMLAttributeGenerator("attr2", "2");
		xeg2.addAttribute(xag);
		xag = null;

		XMLElementGenerator xeg3 = new XMLElementGenerator("third",xeg2);
		xag = new XMLAttributeGenerator("attr1", "1");
		xeg3.addAttribute(xag);
		xag = null;
		xag = new XMLAttributeGenerator("attr2", "2");
		xeg3.addAttribute(xag);

		XMLElementGenerator xeg4 = new XMLElementGenerator("fourth",xeg3);
		xag = new XMLAttributeGenerator("attr1", "1");
		xeg4.addAttribute(xag);
		xag = null;
		xag = new XMLAttributeGenerator("attr2", "2");
		xeg4.addAttribute(xag);

		XMLElementGenerator xeg5 = new XMLElementGenerator("fifth",xeg4);
		xag = new XMLAttributeGenerator("attr1", "1");
		xeg5.addAttribute(xag);
		xag = null;
		xag = new XMLAttributeGenerator("attr2", "2");
		xeg5.addAttribute(xag);

		xmg.setRootElement(xeg);

		System.out.println(xmg.toString(true));

		if(xmg.save()) {
			System.out.println("Successfully Saved XML!");
		} else {
			System.err.println("Error Saving XML");
		}
	}
}
