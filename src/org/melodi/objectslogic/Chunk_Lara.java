/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.melodi.objectslogic;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.latoe.layoutanalysis.pdf.labelisation.ValueComparatorStringInt;
import org.melodi.analyser.talismane_client.service.Structure;


public class Chunk_Lara  implements java.io.Serializable{
	
	
	
    public int x1,x2,y1,y2;
    public String type;
    
    
    int begin;
    int end;
    
    public ArrayList<Word_Lara> mots;
    
    public String text;
    
    public int type_int;
    public int modeTaillePolice;
  
	public int id;
	
	public int level;
	
	public LayoutAnnotation layoutAnnotation;

    
    // Tag supplémentaire
    public String previousTag;
    
    // Pour les test
    public String trueTag;
    public String predictTag;
    
    // - relations -
    int dependance_id = 999;
    String dependance_type = "";
    String dependance_relation = "";
    
    // Structure
	Structure structure;
	ArrayList<Term> candidates_terms;
	
	
	// 
	
    
    
    public Chunk_Lara(int x1, int y1,int x2,int y2, String type, int id){
    	this.x1=x1;
    	this.y1=y1;
    	this.x2=x2;
    	this.y2=y2;
    	this.type = type;
    	this.id=id;
    	this.candidates_terms = new ArrayList<Term>();
    	
    }
    
    public Chunk_Lara(int x1, int y1,int x2,int y2){
//    	super(x1,x2,y1,y2);
    	this.mots = new ArrayList<>();
    	this.candidates_terms = new ArrayList<Term>();
    }
    
    
    public void setX1(int x1){
    	this.x1=x1;
    }
    
    public void setY1(int y1){
    	this.y1=y1;
    }
    
    public void setX2(int x2){
    	this.x2=x2;
    }
    
    public void setY2(int y2){
    	this.y2=y2;
    }
    
    public int getDepId(){
    	return dependance_id;
    }
  
    
    public String getDepType(){
    	return dependance_type;
    }
    public String getDepRelation(){
    	return dependance_relation;
    }
    
    public void setDepId(int id){
    	this.dependance_id=id;
    }
    public void setDepType(String type){
    	this.dependance_type=type;
    }
    public void setDepRel(String rel){
    	this.dependance_relation=rel;
    }
    
    public void setPreviousTag(String previousTag){
    	this.previousTag=previousTag;
    }
    
    public String getPreviousTag(){
    	return previousTag;
    }
    
    public void addWord(Word_Lara w){
        this.mots.add(w);
    }
    
    public int getModeTaillePolice(){
    	return modeTaillePolice;
    }
    
    public String getTrueTag() {
		return trueTag;
	}

	public void setTrueTag(String trueTag) {
		this.trueTag = trueTag;
	}

	public String getPredictTag() {
		return predictTag;
	}

	public void setPredictTag(String predictTag) {
		this.predictTag = predictTag;
	}
	
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
    public String toString(){
    	
    	if(dependance_relation == null ){
    		return type + "_" + id;
    	}
    	else if(type.equals("root")){
    		return "root" + "_" + id ;
    	}
    	else{
    		return type+"_" + id + "_"+ dependance_relation +"("+dependance_type +":"+dependance_id+")"
    				+ " " + this.getText();
    	}
    }
	
	
	
	
	
    
	public Structure getStructure() {
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	public LayoutAnnotation getLayoutAnnotation() {
		return layoutAnnotation;
	}

	public void setLayoutAnnotation(LayoutAnnotation layoutAnnotation) {
		this.layoutAnnotation = layoutAnnotation;
	}
	
	
	

	public ArrayList<Term> getTerms() {
		return candidates_terms;
	}

	public void setTerms(ArrayList<Term> candidates_terms) {
		this.candidates_terms = candidates_terms;
	}
	
	public void addTerm(Term currTerm){
		this.candidates_terms.add(currTerm);
	}
	
	public Term getTerm(int id){
		return this.candidates_terms.get(id);
	}
	
	

	public void assignModeTaillePolice(){
		HashMap<String, Integer> mapPolice = new HashMap<String, Integer>();
		String tempString;
		
		Pattern p = Pattern.compile("([0-9]{1,2})(pt)");
		
		List<Word_Lara> wordList = this.mots;
		
		for (Word_Lara currWord : wordList) {
			String toMatch = currWord.style;
			Matcher m = p.matcher(toMatch);
			while(m.find()){
//				System.out.println(m.group(1));
				tempString = m.group(1);
				
				if(mapPolice.containsKey(tempString)){
					mapPolice.put(tempString, mapPolice.get(tempString)+1);
				}
				else{
					mapPolice.put(tempString, 1);
				}
				
			}
		}
		ValueComparatorStringInt comp = new ValueComparatorStringInt(mapPolice);
		TreeMap<String,Integer> sorted_map = new TreeMap<String, Integer>(comp);
		sorted_map.putAll(mapPolice);
//		System.out.println(d.getName());
//		System.out.println("results:"+sorted_map);
		
		modeTaillePolice = Integer.parseInt(sorted_map.firstKey());
		
	}
}
