package com.ossys.classmaker.modelgenerator.xml;

import java.util.ArrayList;

public class XMLElementGenerator {
	
	private String name = "";
	private StringBuilder text = new StringBuilder();
	private ArrayList<XMLElementGenerator> elements = new ArrayList<XMLElementGenerator>();
	private ArrayList<XMLAttributeGenerator> attributes = new ArrayList<XMLAttributeGenerator>();
	private boolean commented = false;
	private int level = 0;
	
	public XMLElementGenerator(String name, XMLElementGenerator parent) {
		this.name = name;
		if(parent != null) {
			parent.addElement(this);
		}
	}
	
	private void setLevel(int level) {
		this.level = level;
	}
	
	private void addElement(XMLElementGenerator element) {
		element.setLevel(this.level+1);
		this.elements.add(element);
	}
	
	public XMLElementGenerator addAttribute(XMLAttributeGenerator attribute) {
		this.attributes.add(attribute);
		return this;
	}
	
	public XMLElementGenerator addText(String text) {
		this.text.append(text);
		return this;
	}
	
	public XMLElementGenerator comment() {
		this.commented = true;
		return this;
	}
	
	public String toString(boolean tabbed) {
		StringBuilder sb = new StringBuilder();
		
		if(this.elements.size() == 0) {
			if(tabbed) {
				sb.append("\n");
				
				for(int i=0; i< this.level; i++) {
					sb.append("\t");
				}
			}
			
			if(this.commented) {
				sb.append("<!--");
			}
			
			sb.append("<" + this.name);
			
			for(XMLAttributeGenerator attribute : this.attributes) {
				sb.append(" " + attribute.toString());
			}
			
			if(this.text.length() > 0) {
				sb.append(">" + this.text.toString() + "</" + this.name + ">");
			} else {
				sb.append(" />");
			}
			
			if(this.commented) {
				sb.append("-->");
			}
		} else {
			if(tabbed) {
				sb.append("\n");
				
				for(int i=0; i< this.level; i++) {
					sb.append("\t");
				}
			}
			
			if(this.commented) {
				sb.append("<!--");
			}
			
			sb.append("<" + this.name);
			
			for(XMLAttributeGenerator attribute : this.attributes) {
				sb.append(" " + attribute.toString());
			}
			
			sb.append(">");
			
			sb.append(this.text.toString());
			
			for(XMLElementGenerator element : this.elements) {
				sb.append(element.toString(tabbed));
			}
			
			if(tabbed) {
				sb.append("\n");
				
				for(int i=0; i< this.level; i++) {
					sb.append("\t");
				}
			}
			
			sb.append("</" + this.name + ">");
			
			if(this.commented) {
				sb.append("-->");
			}
		}
		
		return sb.toString();
	}
	
}
