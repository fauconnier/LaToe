package org.melodi.reader.larat.internal;

import java.util.ArrayList;

public class Circonstant extends ArrayList<Segment>{
	
	@Override
	public String toString() {
		String toReturn = "	#ItemsList# = [\n";
		for (Segment segment : this) {
			toReturn += segment.toString();
		}
		return toReturn + "]";
	}
	
	
	public String getSurface() {
		String toReturn = "";
		for (Segment segment : this) {
			toReturn += segment.getSurface();
		}
		return toReturn;
	}
	
	

}
