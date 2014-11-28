package org.melodi.reader.larat.internal;

public class Segment implements java.io.Serializable, Graphical_Component{
	
	String text;
	String id;
	int indice_begin;
	int indice_end;
	
	public Segment(){
		
	}
	
	public Segment(String text){
		this.text=text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getSurface(){
		return text;
	}
	
	public int getIndice_begin() {
		return indice_begin;
	}

	public void setIndice_begin(int indice_begin) {
		this.indice_begin = indice_begin;
	}

	public int getIndice_end() {
		return indice_end;
	}

	public void setIndice_end(int indice_end) {
		this.indice_end = indice_end;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
