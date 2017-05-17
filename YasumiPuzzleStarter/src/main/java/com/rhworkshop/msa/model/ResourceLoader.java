package com.rhworkshop.msa.model;


import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceLoader {
	static public ResourceBundle bundle = null;
	
	//method to return properties
	public static String getString(String key) {
		String value = null;
		try {
		    value = getResourceBundle().getString(key);
		} catch (MissingResourceException e) {
		    System.out.println("java.util.MissingResourceException: Couldn't find value for: " + key);
		}
		if(value == null) {
	    	value = "Could not find resource: " + key + "  ";
		}
		return value;
	}
	
	//method to handle the properties
	public static ResourceBundle getResourceBundle() {
		if(bundle == null) {
			bundle = ResourceBundle.getBundle("resources.block");
		}
		return bundle;
	}
	
}
