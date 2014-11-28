/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.latoe.layoutanalysis.pdf.pdfobject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.latoe.layoutanalysis.pdf.labelisation.ValueComparatorStringInt;

/**
 *
 * @author LaurentS
 */
@XStreamAlias("Chunk")
public class Chunk_PDF extends Box_PDF {
    @XStreamAsAttribute
    public String type;
    public int id;
    
    
    @XStreamImplicit
    public List<Word_PDF> mots;
    
    @XStreamOmitField
    public int type_int;
    @XStreamOmitField
    public int modeTaillePolice;
    @XStreamOmitField
    public boolean isFirstOnPage, isLastOnPage;
    @XStreamOmitField
    public float ratio;
    @XStreamOmitField
    public float rapportLongLarg;
    @XStreamOmitField
    public String modeTypePolice;
    
    
    // Pour les test
    public String trueTag;
    public String predictTag;
    
    public Chunk_PDF(int x1, int x2,int y1,int y2, String type, int id){
         super(x1,x2,y1,y2);
         this.type = type;
         this.id=id;
         this.type_int=99; // Un chunk avec un int_type de '99' est un chunk malformé.
         this.mots = new ArrayList<>();
    }
    
    @Override
    public void construct(){
        //Compute box attributes (surface etc)
        super.construct();
        
        //Compute word attributes
        for(Word_PDF w : this.mots){
            w.constructor();
        }
        
        //Default values. Changed when this method is called
        this.isFirstOnPage = false;
        this.isLastOnPage = false;
        
        //Word constructor set the different word font sizes
        this.assignModeTaillePolice();
        this.assignModeTypePolice();
        
        //Compute the ratio 
        //this.ratio = ((this.surface * this.modeTaillePolice) / this.mots.size());
        //Ratio : surface divisée par nombre de mots x taille de la police
//        this.ratio = this.surface / (this.modeTaillePolice * this.mots.size()) ;
        this.ratio = this.surface * (((float)this.modeTaillePolice) / this.mots.size()) ;
//        System.out.println(this.type + "\t"+ Float.toString(this.ratio)+ "\t surface = \t"+this.surface + "\t police= \t" + +this.modeTaillePolice + "\t nb mots\t" +this.mots.size());
        
        
        this.rapportLongLarg = (this.largeur > 0 && this.hauteur > 0) ?(((float)this.largeur) / this.hauteur)*(((float)this.largeur) / this.hauteur) : 0;
//        System.out.println(type + "\t long_larg = \t" + this.rapportLongLarg + "\t raio = \t" + Float.toString(this.ratio));
    }
    
    public void addWord(Word_PDF w){
        this.mots.add(w);
    }
    
    public int getModeTaillePolice(){
    	return modeTaillePolice;
    }
    
    @Override
    public String toString(){
    	
    	String typeReturn = "";
    	if(predictTag == null){
    		typeReturn = type;
    	}else{
    		typeReturn = type + "/" + predictTag;
    	}
    	
    	String returnString = typeReturn + ":";
    	
    	for(Word_PDF currWord : this.mots){
    		returnString += currWord.mot + " ";
    	}
    	
    	return returnString;
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
	
    
    //Parfois le font-size est à null. On l'attribue alors au mode du chunk
    private void corrigerTaillesPoliceNull(){
        for(Word_PDF w : this.mots){
            if(w.fontSize == -1){
                w.fontSize = this.modeTaillePolice;
            }
        }
    }
    
    private void assignModeTaillePolice(){
		HashMap<Integer, Integer> mapPolice = new HashMap<>();
		
		int max = 0, maxKey = -1, nbOccurs;
		for (Word_PDF currWord : this.mots) {
                    if(mapPolice.containsKey(currWord.fontSize)){
                            nbOccurs = mapPolice.get(currWord.fontSize)+1;
                    }
                    else{
                        nbOccurs = 1;
                    }
                    mapPolice.put(currWord.fontSize, nbOccurs);
                    if(max < nbOccurs){
                        max = nbOccurs;
                        maxKey = currWord.fontSize;
                    }
		}
		this.modeTaillePolice = maxKey;
                this.corrigerTaillesPoliceNull();
    }
    
    private void assignModeTypePolice(){
		
		HashMap<String, Integer> mapPolice = new HashMap<>();
                
                for (Word_PDF currWord : this.mots) {
                        if(mapPolice.containsKey(currWord.font)){
                                mapPolice.put(currWord.font, mapPolice.get(currWord.font)+1);
                        }
                        else{
                                mapPolice.put(currWord.font, 1);
                        }
                }
				
		ValueComparatorStringInt comp = new ValueComparatorStringInt(mapPolice);
		TreeMap<String,Integer> sorted_map = new TreeMap<>(comp);
		sorted_map.putAll(mapPolice);
		
		this.modeTypePolice = sorted_map.firstKey();
    }
    
    public boolean isAllinBold(){
        boolean bold = true;
        int i = 0;
        while(bold && i < this.mots.size()){
            bold = this.mots.get(i).isBold();
            i++;
        }
        
        return bold;
    }    
    
    public boolean isAllinItalic(){
        boolean italic = true;
        int i = 0;
        while(italic && i < this.mots.size()){
            italic = this.mots.get(i).isItalic();
            i++;
        }
        return italic;
    }  
    
    public boolean containsWordsinBold(){
        boolean bold = false;
        int i = 0;
        while(!bold && i < this.mots.size()){
            bold = this.mots.get(i).isBold();
            i++;
        }
        
        return bold;
    }
    
    
    public boolean containsWordsinItalic(){
        boolean ita = false;
        int i =0;
        while(!ita && i < this.mots.size()){
            ita = this.mots.get(i).isItalic();
            i++;
        }
        return ita;
    }
}
