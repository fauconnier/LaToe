package org.melodi.tools.dataset;

import java.util.ArrayList;

import org.melodi.reader.larat.internal.Unit;


public class DataSet_Corpora extends ArrayList<Unit>{
	
	String name;
	String path;
	
	public DataSet_Corpora(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	

}
