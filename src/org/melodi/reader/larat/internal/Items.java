package org.melodi.reader.larat.internal;

import java.util.ArrayList;

public class Items extends ArrayList<Item> implements java.io.Serializable{
	
	@Override
	public String toString() {
		String toReturn = "	#ItemsList# = [\n";
		for (Item item : this) {
			toReturn += item.toString();
		}
		return toReturn + "]";
	}
	
	
	public String getSurface() {
		String toReturn = "";
		for (Item item : this) {
			toReturn += item.getSurface();
		}
		return toReturn;
	}

}
