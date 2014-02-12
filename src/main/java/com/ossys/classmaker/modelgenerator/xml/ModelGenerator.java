package com.ossys.classmaker.modelgenerator.xml;

import java.io.FileWriter;
import java.io.IOException;

public class ModelGenerator {

	protected StringBuilder sb = new StringBuilder();
	private String filename = "";
	private String path = "";
	private String file_extension = null;
	
	public ModelGenerator(String filename, String path) {
		this.filename = filename;
		this.path = path;
	}
	
	public String toString(boolean tabbed) {
		return this.sb.toString();
	}
	
	public void setFileExtension(String file_extension) {
		this.file_extension = file_extension;
	}
	
	public boolean save() {
		boolean success = false;
		
		if(this.file_extension == null) {
			this.file_extension = ".xml";
		}
		
		FileWriter out = null;
		try {
			out = new FileWriter(this.path + System.getProperty("file.separator") + this.filename + this.file_extension);
			out.write(this.sb.toString());
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}  finally {
			if(out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return success;
	}
}
