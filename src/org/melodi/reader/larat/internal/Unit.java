package org.melodi.reader.larat.internal;

import java.util.ArrayList;

public class Unit implements java.io.Serializable, Graphical_Component, Comparable{

	
	/**
	 * Alignement des primers/items : concepts
	 */
	String document;
	
	/**
	 * MachineLearing EKAW
	 */
	String y;
	String last_y;
	String predict_y;
	String predict_onto_y;
	
	/**
	 * Caractérisation : Axe
	 */
	String axe_visuel;
	String axe_visuel_nav_hyp;
	
	String axe_rhetorique;
	ArrayList<String> axe_intentionnel;
	String axe_semantique;
	String axe_semantique_context;
	Cloture clot;
	
	
	/**
	 * Objets internes
	 */
	
	private String text;
	private int indice_begin;
	private int indice_end;
	private String comment;
	

	/**
	 * Unités
	 */
	private Primer primer;
	private Items items;
	private int id;
	private int old_id;
	private Annotation annot;
	

	// Un concept est associé au Primer et aux Items
	
	public Unit(){
		Items myInternalItems = new Items();
		items=myInternalItems;
		axe_intentionnel = new ArrayList<String>();
		
		axe_rhetorique = "";
		axe_semantique = "";
		axe_visuel = "";
		axe_visuel_nav_hyp = "";
		axe_semantique_context = "";
		annot = new Annotation();
	
	}
	
	public void setY(String y){
		this.y = y;
	}
	
	public String getY(){
		return this.y;
	}
	
	public void setLastY(String last_y){
		this.last_y = last_y;
	}
	
	public String getLastY(){
		return this.last_y;
	}
	
	public void addAxe_intentionnel(String axe_intentionnel){
		this.axe_intentionnel.add(axe_intentionnel);
	}
	
	public ArrayList<String> getAxe_intentionnel(){
		return this.axe_intentionnel;
	}
	
	
	public String getAxe_semantiqueContext(){
		return this.axe_semantique_context;
	}
	
	public void setAxe_semantiqueCircon(String axe_semantique_context){
		this.axe_semantique_context = axe_semantique_context;
	}
	
	public String getAxe_semantique(){
		return this.axe_semantique;
	}
	
	public void setAxe_semantique(String axe_semantique){
		this.axe_semantique = axe_semantique;
	}
	
	public String getAxe_rhetorique(){
		return this.axe_rhetorique;
	}
	
	public void setAxe_rhetorique(String axe_rethorique){
		this.axe_rhetorique = axe_rethorique;
	}
	
	public String getAxe_visuel() {
		return axe_visuel;
	}

	public void setAxe_visuel(String axe_visuel) {
		this.axe_visuel = axe_visuel;
	}

	public String getAxe_visuel_nav_hyp() {
		return axe_visuel_nav_hyp;
	}

	public void setAxe_visuel_nav_hyp(String axe_visuel_nav_hyp) {
		this.axe_visuel_nav_hyp = axe_visuel_nav_hyp;
	}

	
	public void setAnnotation(Annotation annot){
		this.annot=annot;
	}
	public Annotation getAnnotation(){
		return this.annot;
	}
	
	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
	
	public int getIdOld() {
		return old_id;
	}

	public void setIdOld(int id) {
		this.old_id = old_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Primer getPrimer() {
		
		if(primer == null){
			Primer tmp_primer = new Primer();
			tmp_primer.setText("null");
			return tmp_primer;
		}
		else{
		return primer;
		}
	}

	public void setPrimer(Primer primer) {
		this.primer = primer;
	}

	public Items getItems() {
		return items;
	}
	
	public Item getItem(int index){
		return items.get(index);
	}

	public void setItems(Items items) {
		this.items = items;
	}
	
	public void addItem(Item item){
		this.items.add(item);
	}
	
	public int sizeItems(){
		return this.items.size();
	}

	public String toString(){
		String retur ="SE :";

		if(text != null){
			retur = retur + text + "\n";
		}
		
		if(primer !=null){
			retur = retur + primer.toString();
		}
		if(items.size() > 0){
			for(Item ite : items){
				retur = retur +  ite.toString();
			}
		}
		return retur;
	}
	
	public Cloture getClot() {
		return clot;
	}

	public void setClot(Cloture clot) {
		this.clot = clot;
	}
	
	public String getDocument(){
		return document;
	}
	
	public void setDocument(String doc){
		this.document=doc;
	}

	public String getPredict_y() {
		return predict_y;
	}

	public void setPredict_y(String predict_y) {
		this.predict_y = predict_y;
	}

	
	
	public String getPredict_onto_y() {
		return predict_onto_y;
	}

	public void setPredict_onto_y(String predict_onto_y) {
		this.predict_onto_y = predict_onto_y;
	}

	@Override
	public int compareTo(Object arg0) {
		Unit b = (Unit) arg0;
		return this.getIndice_begin() - b.getIndice_begin() ;
	}

	
}
