package org.melodi.reader.larat.internal;

import java.util.ArrayList;

public class Concept extends ArrayList<Segment>{
	
	@Override
	public String toString() {
		String toReturn = "	#Concept SegmentList# = [\n";
		for (Segment segment : this) {
			toReturn += "(" + segment.getIndice_begin() +")"+ segment.getSurface() + "(" + segment.getIndice_end() + ")";
		}
		return toReturn + "]";
	}
	
	
	public String getSurface() {
		String toReturn = "";
		for (Segment segment : this) {
			toReturn += segment.getSurface()+"||";
		}
		return toReturn;
	}
	
	

}
