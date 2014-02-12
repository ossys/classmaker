package com.ossys.classmaker.modelgenerator.xml;

public class XMLModelGenerator extends ModelGenerator {
	
	private XMLElementGenerator root_element = null;
	
	public XMLModelGenerator(String filename, String path) {
		super(filename,path);
	}
	
	public void setRootElement(XMLElementGenerator root_element) {
		this.root_element = root_element;
	}
	
	public String toString(boolean tabbed) {
		this.sb.delete(0, this.sb.length());
		this.sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		this.sb.append(this.root_element.toString(tabbed));
		return this.sb.toString();
	}
	
	public boolean save(boolean tabbed) {
		this.sb.delete(0, this.sb.length());
		this.sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		this.sb.append(this.root_element.toString(tabbed));
		return super.save();
	}
	
}
