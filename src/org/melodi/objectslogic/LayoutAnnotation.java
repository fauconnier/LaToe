package org.melodi.objectslogic;

public class LayoutAnnotation  implements java.io.Serializable{
	
	String html_type;
	int begin;
	int end;
	
	
	public LayoutAnnotation(){
		
	}


	public String getType() {
		return html_type;
	}


	public void setType(String html_type) {
		this.html_type = html_type;
	}


	public int getBegin() {
		return begin;
	}


	public void setBegin(int begin) {
		this.begin = begin;
	}


	public int getEnd() {
		return end;
	}


	public void setEnd(int end) {
		this.end = end;
	}

	
	
}
