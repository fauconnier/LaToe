package org.melodi.learning.service;

import java.util.Vector;

import org.melodi.reader.larat.internal.Unit;


public class PairUnit_Features {
	
	Vector<String> features;
	Unit currUnit;
	
	public PairUnit_Features(){
		
	}

	public PairUnit_Features(Unit currUnit, Vector<String> features){
		this.currUnit=currUnit;
		this.features=features;
	}
	
	public Vector<String> getFeatures() {
		return features;
	}

	public void setFeatures(Vector<String> features) {
		this.features = features;
	}

	public Unit getUnit() {
		return currUnit;
	}

	public void setUnit(Unit currUnit) {
		this.currUnit = currUnit;
	}
	
	

}
