package org.melodi.reader.larat.internal;

import java.util.ArrayList;

import org.melodi.analyser.talismane_client.service.Structure;
import org.melodi.objectslogic.Chunk_Lara;


public class Item extends ArrayList<String> implements java.io.Serializable, Graphical_Component, Comparable {
	
	
	
	/**
	 * Machine Learning : EKAW
	 */
	Structure structure;
	
	/**
	 * Thèse
	 */
	ArrayList<Chunk_Lara> listChunk;
	
	String id;
	int indice_begin;
	int indice_end;
	
	/**
	 * Unités
	 */
	private Concept concept;
	private Circonstant circonstant;
	private MarqueurRelation marqueurRel;
	
	public Item(){
		this.concept = new Concept();
		this.circonstant = new Circonstant();
		this.marqueurRel = new MarqueurRelation();
		this.structure =  new Structure();
		this.listChunk = new ArrayList<Chunk_Lara>();
	}
	
	public void setMarqueurRel(MarqueurRelation marqueurRel){
		this.marqueurRel = marqueurRel;
	}
	
	public boolean hasMarqRel(){
		return this.marqueurRel.size() > 0;
	}
	
	public MarqueurRelation getMarqueurRel(){
		return this.marqueurRel;
	}
	
	public boolean hasCirconstant(){
		return this.circonstant.size() > 0;
	}
	
	public boolean hasConcept(){
		return this.concept.size() > 0;
	}
	
	public void setCirconstant(Circonstant circonstant){
		this.circonstant = circonstant;
	}
	
	public Circonstant getCirconstant(){
		return this.circonstant;
	}
	
	public void setConcept(Concept concept){
		this.concept = concept;
	}
	
	public Concept getConcept(){
		return this.concept;
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

	public String getText() {
		return this.get(0);
	}

	public void setText(String toadd){
		this.add(toadd);
	}
	
	@Override
	public String toString() {
		String toReturn = "	Item " + id + " = [";
		for (String sequence : this) {
			toReturn += sequence.toString();
		}
		return toReturn + "	]\n";
	}

	public String getSurface() {
		String toReturn = "";
		for (String sequence : this) {
			toReturn += sequence.toString();
		}
		return toReturn + "\n";
	}

	public int compareTo(Item o2) {

		if (this.getIndice_begin() > o2.getIndice_begin()) {
			return 1;
		} else {

			return 0;
		}
	}
	
	
	public Chunk_Lara getChunk(int id){
		return listChunk.get(id);
	}

	public ArrayList<Chunk_Lara> getListChunk() {
		return listChunk;
	}

	public void setListChunk(ArrayList<Chunk_Lara> listChunk) {
		this.listChunk = listChunk;
	}

	@Override
	public int compareTo(Object arg0) {
		Item b = (Item) arg0;
		return this.getIndice_begin() - b.getIndice_begin() ;
	}

	public void setStructure(Structure structure){
		this.structure=structure;
	}
	
	public Structure getStructure(){
		return this.structure;
	}
	
}
