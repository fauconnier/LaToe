package org.melodi.reader.larat.internal;

import java.util.ArrayList;

import org.melodi.analyser.talismane_client.service.Structure;
import org.melodi.objectslogic.Chunk_Lara;
import org.melodi.reader.larat.internal.Concept;

public class Primer extends ArrayList<String> implements java.io.Serializable, Graphical_Component{
	
	
	/**
	 * Chacune des unités : 
	 * item, primer, cloture
	 * peut être composées de plusieurs chunks.
	 * 
	 * Attention, il n'y a pas toujours bijection
	 * complète entre les éléments de la SE et ses chunks.
	 * 
	 * Il faut distinguer "amorce_layout" et "amorce_se".
	 * La plus simple à trouver est l'amorce "layout", c'est-à-dire
	 * l'amorce composée de chunk.
	 * 
	 * La vraie amorce, au sens linguistique, "amorce_se"
	 * peut être un sous-ensemble de amorce_layout.
	 * Et c'est celle là qu'il faut analyser.
	 * 
	 * Chaque chunk a une structure.
	 * Chaque structure est composée d'une sentence.
	 * Chaque sentence est composée de tokens.
	 */
	
	/**
	 * Machine Learning : EKAW
	 */
	Structure structure;
	
	
	ArrayList<Chunk_Lara> listChunk; // Problème avec les ES horizontale
	
	int id_se;
	int indice_begin;
	int indice_end;
	
	/**
	 * Unités
	 */
	private Concept concept;
	private Circonstant circonstant;
	private MarqueurRelation marqueurRel;
	
	public Primer(){
		this.concept = new Concept();
		this.circonstant = new Circonstant();
		this.marqueurRel = new MarqueurRelation();
		this.structure =  new Structure();
		this.listChunk = new ArrayList<Chunk_Lara>();
	}
	
	public void setMarqueurRel(MarqueurRelation marqueurRel){
		this.marqueurRel = marqueurRel;
	}
	
	public MarqueurRelation getMarqueurRel(){
		return this.marqueurRel;
	}
	
	public void setCirconstant(Circonstant circonstant){
		this.circonstant = circonstant;
	}
	
	public Circonstant getCirconstant(){
		return this.circonstant;
	}
	
	public boolean hasCirconstant(){
		return this.circonstant.size() > 0;
	}
	
	public boolean hasConcept(){
		return this.concept.size() > 0;
	}
	
	public boolean hasMarqRel(){
		return this.marqueurRel.size() > 0;
	}
	
	public void setConcept(Concept concept){
		this.concept = concept;
	}
	
	public Concept getConcept(){
		return this.concept;
	}

	@Override
	public String toString() {
		String toReturn = "	#Primer# = [";
		for (String sequence : this) {
			toReturn += sequence.toString();
		}
		return toReturn + "	]\n";
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
	
	public void setStructure(Structure structure){
		this.structure=structure;
	}
	
	public Structure getStructure(){
		return this.structure;
	}

	public ArrayList<Chunk_Lara> getListChunk() {
		return listChunk;
	}

	public void setListChunk(ArrayList<Chunk_Lara> listChunk) {
		this.listChunk = listChunk;
	}
	
	

}
