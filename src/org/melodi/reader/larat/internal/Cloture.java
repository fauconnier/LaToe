package org.melodi.reader.larat.internal;

import java.util.ArrayList;

public class Cloture extends ArrayList<String> implements java.io.Serializable, Graphical_Component{
	
	int id_se;
	int indice_begin;
	int indice_end;
	

	@Override
	public String toString() {
		String toReturn = "	#Primer# = [\n";
		for (String sequence : this) {
			toReturn += sequence.toString();
		}
		return toReturn + "	]";
	}
	
	public String getText(){
		return this.get(0);
	}
	
	public void setText(String primer_string){
		this.add(primer_string);
	}
	
	public String getSurface(){
		String toReturn = "";
		for (String sequence : this) {
			toReturn += sequence.toString();
		}
		return toReturn;
	}

	public int getId_se() {
		return id_se;
	}

	public void setId_se(int id_se) {
		this.id_se = id_se;
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

}
