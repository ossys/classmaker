/**
 * 
 */
package com.purlogic.classmaker.sourcegenerator.attributegenerator;

import com.purlogic.classmaker.sourcegenerator.attributegenerator.AttributeGenerator.AttributeVisibilityType;


/**
 * @author Administrator
 *
 */
public interface AttributeGeneratorInterface {
	public void setVisibility(AttributeVisibilityType visibilityType);
	
	public void setDefault(String dflt);
	
	public String toString();
}
