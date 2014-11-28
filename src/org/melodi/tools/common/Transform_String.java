package org.melodi.tools.common;

public class Transform_String {
	
	
	public Transform_String(){
		
	}
	
	public String toTitle_File(String name_of_file){
		String toReturn = name_of_file.replaceAll("\\\\", "_");
		toReturn = toReturn.replaceAll("\\/","_");
		toReturn = toReturn.replaceAll(" ","_");
		
		return toReturn;
	}

}
