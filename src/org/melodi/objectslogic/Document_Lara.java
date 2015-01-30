package org.melodi.objectslogic;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.melodi.analyser.talismane_client.service.Sentence;
import org.melodi.analyser.talismane_client.service.Structure;
import org.melodi.analyser.talismane_client.service.TalismaneClient;
import org.melodi.analyser.talismane_client.service.Token;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.tools.common.IO_Service;
import org.melodi.tools.common.Segment_Tool;
import org.melodi.tools.tree.ShiftReduce_Service;



/*
 * Représentation de tous les chunks du document
 */



public class Document_Lara  implements java.io.Serializable{
	
	public List<Chunk_Lara> chunks;
	
    public String fName;
    
    public int index_char;
    
    public ArrayList<Unit> list_of_units;
    
    public String text;
    
    public Document_Lara(){
    	list_of_units = new ArrayList<Unit>();
    }
    
    private static ArrayList<Chunk_Lara> subordonates;
    private static ArrayList<Chunk_Lara> coordinates;

	public Document_Lara(List<Chunk_Lara> listChunk){
		this.chunks = listChunk;
	}
	
	public Chunk_Lara getChunk_id(int id){
		Chunk_Lara toReturn = new Chunk_Lara(0,0,0,0);
		for(Chunk_Lara currChunk : chunks){
			if(currChunk.id == id){
				toReturn = currChunk;
			}
		}
		return toReturn;
	}

	public List<Chunk_Lara> getChunk(){
		return chunks;
	}
	
	public void setChunk(List<Chunk_Lara> currList){
		this.chunks=currList;
	}
	
	public void setName(String fname){
		this.fName=fname;
	}
	
	public String getName(){
        return this.fName;
    }
    
    public String getTitle(){
    	return this.fName;
    }
    
    public void setTitle(String title){
    	this.fName=title;
    }
    
    public int getLengthChar(){
    	int index_char = 0;
		String plainText = "";

		for (Chunk_Lara currChunk : this.getChunk()) {

			// 1. getText and Count Char
			String chunkText = currChunk.getText();
			chunkText = chunkText.trim();
			chunkText = chunkText.replace("\n", "");
			int length = chunkText.length();
			int begin = index_char;
			int end = index_char + length;
			String type = currChunk.getType();
			plainText += chunkText;
			currChunk.setBegin(begin);
			currChunk.setEnd(end);
			index_char += length;
		}
		return index_char;
    }
    
    public int getIndexOfLastChunck(){
    	int index = 0;
    	for(Chunk_Lara tmpChunk : this.getChunk()){
    		index++;
    	}
    	return index;
    }
    
    
    
    
    /*
     * Représentation HTML pour LARAt
     */
    

    
    public ArrayList<Unit> getList_of_units() {
		return list_of_units;
	}

	public void setList_of_units(ArrayList<Unit> list_of_units) {
		this.list_of_units = list_of_units;
	}

	
	public void addUnits(Unit currUnit){
		this.list_of_units.add(currUnit);
	}
	/*
     * Document Tree
     */
    public boolean hasDependence(Chunk_Lara currChunk){
    	int id = currChunk.getId();
    	
    	boolean flag = false;
    	
    	
    	for(Chunk_Lara tmpChunk : this.getChunk()){
    		if(tmpChunk.getDepId() == id){
    			flag = true;
    		}
    	}
    	return flag;
    }
    
    public Chunk_Lara getDependence(Chunk_Lara currChunk){
    	
    	int id = currChunk.getId();
    	Chunk_Lara returnChunk = new Chunk_Lara(0, 0, 0, 0);
    	returnChunk.setType("nothing");
    	
//    	System.out.println("Recherche du dépendant pour " + currChunk.toString());
    	
     	for(Chunk_Lara tmpChunk : this.getChunk()){
    		if(tmpChunk.getDepId() == id && tmpChunk.getId() != id){ 
    			// Un Chunk ne peut pas être dépendant de lui-même.
    			return tmpChunk;
    		}
    	}
     	
     	return returnChunk;
    }
    
    public Chunk_Lara getLastDependence(Chunk_Lara currChunk){
    	
    	int id = currChunk.getId();
    	Chunk_Lara returnChunk = new Chunk_Lara(0, 0, 0, 0);
    	returnChunk.setType("nothing");
    	
     	for(Chunk_Lara tmpChunk : this.getChunk()){
    		if(tmpChunk.getDepId() == id){
    			returnChunk = tmpChunk;
    		}
    	}
     	
     	return returnChunk;
    }
    
    public boolean hasSubordinate(Chunk_Lara currChunk){
    	int id = currChunk.getId();
    	
    	boolean flag = false;
    	
    	
    	for(Chunk_Lara tmpChunk : this.getChunk()){
    		if(tmpChunk.getDepId() == id && tmpChunk.getDepRelation().equals("sub")){
    			flag = true;
    		}
    	}
    	return flag;
    }
    
    public boolean hasCoordinate(Chunk_Lara currChunk){
    	int id = currChunk.getId();
    	
    	boolean flag = false;
    	
    	
    	for(Chunk_Lara tmpChunk : this.getChunk()){
    		if(tmpChunk.getDepId() == id && tmpChunk.getDepRelation().equals("coord")){
    			flag = true;
    		}
    	}
    	return flag;
    }
    
    
    public Chunk_Lara getParent(Chunk_Lara currChunk){
    	int id = currChunk.getId();
    	Chunk_Lara returnChunk = new Chunk_Lara(0, 0, 0, 0);
    	returnChunk.setType("nothing");
    	
    	returnChunk = getParentRecursive(currChunk);
    	
    	return returnChunk;
    }
    
    public Chunk_Lara getParentRecursive(Chunk_Lara currChunk){
    	
    	if(currChunk.getDepRelation().equals("sub")){
    		return this.getChunk_id(currChunk.getDepId());
    	}
    	else{
    		
    		Chunk_Lara newChunk = this.getChunk_id(currChunk.getDepId());
    		return getParentRecursive(newChunk);
    	}
    }
    
    public ArrayList<Chunk_Lara> getAllCoordinates(Chunk_Lara currChunk){
    	int id = currChunk.getId();
    	
    	coordinates = new ArrayList<Chunk_Lara>();
    	for(Chunk_Lara tmpChunk : this.getChunk()){
    		if(tmpChunk.getDepId() == id && tmpChunk.getDepRelation().equals("coord")){
    			coordinates.add(tmpChunk);
    			getAllCoordinatesRecursive(tmpChunk);
    		}
    	}
    	
    	return coordinates;
    }
    
    public void getAllCoordinatesRecursive(Chunk_Lara currChunk){
    	int id = currChunk.getId();
    	
    	for(Chunk_Lara tmpChunk : this.getChunk()){
    		if(tmpChunk.getDepId() == id && tmpChunk.getDepRelation().equals("coord")){
    			coordinates.add(tmpChunk);
    			getAllCoordinatesRecursive(tmpChunk);
    		}
    	}
    }
    
    public ArrayList<Chunk_Lara> getAllSubordinates(Chunk_Lara currChunk){
    	int id = currChunk.getId();
    	
    	subordonates = new ArrayList<Chunk_Lara>();
    	
    	for(Chunk_Lara tmpChunk : this.getChunk()){
    		if(tmpChunk.getDepId() == id && tmpChunk.getDepRelation().equals("sub")){
    			subordonates.add(tmpChunk);
    			getAllSubordonatesRecursive(tmpChunk, this);
    		}
    	}
    	
    	return subordonates;
    }
    
    
    public String toString(){
    	
    	/**
    	 * Imprime un document indépendamment des levels des chunks
    	 * mais en respectant les dépendances mises en place.
    	 * 
    	 * Parcours de l'arbre en shift-reduce
    	 */
    	
    	ShiftReduce_Service shiftreduce_service = new ShiftReduce_Service();
    	  	
    	return shiftreduce_service.read_shiftreduce(this);
    }

	static void getAllSubordonatesRecursive(Chunk_Lara currChunk, Document_Lara currDocument){
    	
    	int id = currChunk.getId();
    	
    	for(Chunk_Lara tmpChunk : currDocument.getChunk()){
    		if(tmpChunk.getDepId() == id){
    			subordonates.add(tmpChunk);
    			getAllSubordonatesRecursive(tmpChunk, currDocument);
    		}
    	}
    }
	
	
	public void processLayoutAnnotation(){

			index_char = 0;
			String plainText = "";
			

			for (Chunk_Lara currChunk : this.getChunk()) {
				// 1. getText and Count Char
				String chunkText = currChunk.getText();
				chunkText = chunkText.trim();
				chunkText = chunkText.replace("\n", "");
				
				int length = chunkText.length();
				int begin = index_char;
				int end = index_char + length;
				String type = currChunk.getType();
				plainText += chunkText;
				currChunk.setBegin(begin);
				currChunk.setEnd(end);
				index_char += length;

				// 2. getLayoutAnnotation
				LayoutAnnotation layoutAnnotation = new LayoutAnnotation();
				layoutAnnotation.setBegin(begin);
				layoutAnnotation.setEnd(end);
				layoutAnnotation.setType(type);
				currChunk.setLayoutAnnotation(layoutAnnotation);

			}
			
			// Sanity Check
			for (Chunk_Lara currChunk : this.getChunk()) {
				
				if(currChunk.getLayoutAnnotation().getBegin() > index_char){
					System.err.println("Erreur ici");
				}
				if(currChunk.getLayoutAnnotation().getEnd() > index_char){
					System.err.println("Erreur ici");
				}
			}
			
	}
	
	
	public ArrayList<Chunk_Lara> getChunkBetween(int start, int end){
		
		ArrayList<Chunk_Lara> overlapped_chunk = new ArrayList<Chunk_Lara>();
		
		Segment_Tool segment_tool = new Segment_Tool();
		
		for(Chunk_Lara currChunk : this.getChunk()){
			
			if(segment_tool.haveOverlap(start,end, currChunk.getLayoutAnnotation().getBegin(),currChunk.getLayoutAnnotation().getEnd())){
				overlapped_chunk.add(currChunk);
			}
			
		}
		
		
		
		return overlapped_chunk;
	}
	
	public String getTextWithMFM(boolean talismane) throws UnknownHostException, IOException, InterruptedException{
		
		String plainText = "";
		for (Chunk_Lara currChunk : this.getChunk()) {
			
			String chunkText = currChunk.getText();
			chunkText = chunkText.replace("\n", " ");
			plainText += chunkText + " ";
		}
		
//		plainText = plainText.toLowerCase();
//		plainText = plainText.replaceAll("[^a-zA-Z0-9áàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ\\s']", "").replaceAll("\\s+", " ");
	
		if(talismane){
		TalismaneClient talismane_client = new TalismaneClient();
		Structure currStructure = talismane_client.analyse(plainText,false);
		
		String newString = "";
		for(Sentence currSentence : currStructure){
			for(Token currToken : currSentence){
				if(currToken.getCppostag().equals("NC") ||  currToken.getCppostag().equals("NPP")){
					if(currToken.getLemma().equals("_")){
						newString += currToken.getForm() + " ";
					}
					else{
						newString += currToken.getLemma() + " ";
					}
				}
			}
		}
		newString = newString.toLowerCase();
		newString = newString.replaceAll("[^a-zA-ZáàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ\\s']", "").replaceAll("\\s+", " ");
		plainText = newString;
		}
		else{
			plainText = plainText.toLowerCase();
		
			plainText = plainText.replaceAll(" [a-z]'"," ");
			plainText = plainText.replaceAll("[^a-zA-ZáàâäãåçéèêëíìîïñóòôöõúùûüýÿæœÁÀÂÄÃÅÇÉÈÊËÍÌÎÏÑÓÒÔÖÕÚÙÛÜÝŸÆŒ\\s']", "").replaceAll("\\s+", " ");
		}
		
		
		return plainText;
	}
	
	
	public String getText(){
		

			index_char = 0;
			String plainText = "";
			
			for (Chunk_Lara currChunk : this.getChunk()) {
				// 1. getText and Count Char
				String chunkText = currChunk.getText();
				chunkText = chunkText.trim();
				chunkText = chunkText.replace("\n", "");
				
				int length = chunkText.length();
				int begin = index_char;
				int end = index_char + length;
				String type = currChunk.getType();
				plainText += chunkText;
				currChunk.setBegin(begin);
				currChunk.setEnd(end);
				index_char += length;

				// 2. getLayoutAnnotation
				LayoutAnnotation layoutAnnotation = new LayoutAnnotation();
				layoutAnnotation.setBegin(begin);
				layoutAnnotation.setEnd(end);
				layoutAnnotation.setType(type);
				currChunk.setLayoutAnnotation(layoutAnnotation);

			}
			
			// Sanity Check
			for (Chunk_Lara currChunk : this.getChunk()) {
				
				if(currChunk.getLayoutAnnotation().getBegin() > index_char){
					System.err.println("Erreur ici");
				}
				if(currChunk.getLayoutAnnotation().getEnd() > index_char){
					System.err.println("Erreur ici");
				}
			}
			
		return plainText;
	}
}
