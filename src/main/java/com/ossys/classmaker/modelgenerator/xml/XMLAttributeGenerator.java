package com.ossys.classmaker.modelgenerator.xml;

public class XMLAttributeGenerator {

	private String name = "";
	private String value = "";
	
	public XMLAttributeGenerator(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String toString() {
		return this.name + "=\"" + this.value + "\"";
	}
	
}
