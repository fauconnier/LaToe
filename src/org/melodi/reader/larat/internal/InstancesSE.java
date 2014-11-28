package org.melodi.reader.larat.internal;

import java.util.ArrayList;

import org.melodi.reader.larat.internal.Items;
import org.melodi.reader.larat.internal.Primer;



public class InstancesSE {
	
	private Primer primer;
	private Items items;
	private String id;
	private String outcome;
	
	public InstancesSE(){
		this.primer = new Primer();
		this.items = new Items();
		this.id = "";
		this.outcome = "";
		
	}
	
	public Primer getPrimer() {
		return primer;
	}
	public void setPrimer(Primer primer) {
		this.primer = primer;
	}
	public Items getItems() {
		return items;
	}
	public void setItems(Items items) {
		this.items = items;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	@Override
	public String toString() {
		return "InstancesSE "+id+ "[\n" + primer.toString() + "\n" + items.toString() + "\noutcome=" + outcome + "]";
	}
	
	public String getSurface(){
		return primer.getSurface() + "\n" + items.getSurface();
		
	}
	
	

	

}
