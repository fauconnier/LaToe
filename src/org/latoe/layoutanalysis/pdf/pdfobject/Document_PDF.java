/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.latoe.layoutanalysis.pdf.pdfobject;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.latoe.layoutanalysis.pdf.labelisation.ValueComparatorIntInt;
import org.latoe.layoutanalysis.pdf.labelisation.ValueComparatorStringInt;
import org.latoe.layoutanalysis.pdf.labelisation.Service_CRF;
import org.melodi.learning.iitb.CRF.DataSequence;
import org.melodi.learning.iitb.CRF.SegmentDataSequence;
import org.melodi.learning.iitb.CRF.Segmentation;

/**
 *
 * @author LaurentS
 */
@XStreamAlias("Document")
public class Document_PDF implements SegmentDataSequence, Segmentation{
    /**
     * Les chunk vont de 0 à n
     */
	
    @XStreamImplicit
    public List<Page_PDF> pages;
    
    @XStreamOmitField
    private String fName;
    
    @XStreamOmitField
    int modeTaillePolice;
    
    @XStreamOmitField
    int modeMargeGauche;
    
    @XStreamOmitField
    int modeMargeDroite;
    
    @XStreamOmitField
    float moyMargeHaut;
    
    @XStreamOmitField
    public int largeurMoyennePages, hauteurMoyennePages;
            
    public Document_PDF(){
        this.pages = new ArrayList<>();
    }
    
    // Methode à appeler pour construire les params optionnels des objets du corpus
    public void constructObjects(){
        int sommeH = 0, sommeLa = 0;
        for(Page_PDF p : this.pages){
            p.construct();
            sommeH += p.hauteur;
            sommeLa += p.largeur;
        }
        this.largeurMoyennePages = Math.round(sommeLa / this.pages.size());
        this.hauteurMoyennePages = Math.round(sommeH / this.pages.size());
    }
    
    
    public int getModeTaillePolice(){
    	return modeTaillePolice;
    }
    
    public int getModeMargeDroite(){
    	return modeMargeDroite;
    }
    
    
    public int getModeMargeGauche(){
    	return modeMargeGauche;
    }
    
    public float getMoyMargeHaut(){
        return this.moyMargeHaut;
    }
    
    public void addPage(Page_PDF p){
        this.pages.add(p);
    }
    
    /*---- Get and Set -----*/
    public void setName(String s){
        this.fName = s;
    }
    
    
    public String getName(){
        return this.fName;
    }

	@Override
	public int length() {
		// retourne le nombre total de chunk
		List<Page_PDF> pageList = this.pages;
		int sum = 0;
		for(Page_PDF currPage : pageList){
			for(Chunk_PDF currChunk : currPage.groupes){
				sum++;
			}
		}
		return sum;
	}

	@Override
	public int y(int i) {
		// Renvoie le int_label du i-th chunk
		
		List<Page_PDF> pageList = this.pages;
		int label_int = 99;
		for(Page_PDF currPage : pageList){
			List<Chunk_PDF> chunkList = currPage.groupes;
			for(Chunk_PDF currChunk : chunkList){
				if(currChunk.id==i){
					label_int = currChunk.type_int;
				}
			}
		}
//		System.out.println("Label:"+label+":"+label_int);
		if(label_int == 99){
			System.err.println("Chunk malformé");
		}
		return label_int;
	}

	
	@Override
	public Object x(int i) {
		// retourne le i-th chunk
		List<Page_PDF> pageList = this.pages;
		Chunk_PDF chunkToReturn = new Chunk_PDF(0, 0, 0, 0, "unknown", 99);
		// Un chunk avec 'type_int' de 99 est un chunk malformé
		for(Page_PDF currPage : pageList){
			List<Chunk_PDF> chunkList = currPage.groupes;
			for(Chunk_PDF currChunk : chunkList){
				if(currChunk.id==i){
					chunkToReturn = currChunk;
				}
			}
		}
		return (Object) chunkToReturn;
	}

	@Override
	public void set_y(int i, int label) {
		// Assigne au i-th chunk le int_label 'label'
		List<Page_PDF> pageList = this.pages;
		for(Page_PDF currPage : pageList){
			List<Chunk_PDF> chunkList = currPage.groupes;
			for(Chunk_PDF currChunk : chunkList){
				if(currChunk.id==i){
					currChunk.type_int = label;
				}
			}
		}	
		
	}

	@Override
	public int getSegmentEnd(int segmentStart) {
		
		return 0;
	}

	@Override
	public void setSegment(int segmentStart, int segmentEnd, int y) {
		// TODO Auto-generated method stub
	}

	@Override
	public int numSegments() {
		return this.length();
	}

	@Override
	public int segmentLabel(int segmentNum) {
		// retourne le label du segment 'segmentNum'
		
		return this.y(segmentNum);
	}

	@Override
	public int segmentStart(int segmentNum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int segmentEnd(int segmentNum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSegmentId(int offset) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String toString(){
		return this.getName();
	}
	
	
	
	public void assignModeMargeDroite(){
		// Parcourt tout le document et assigne le mode
		// de la valeur de la marge de gauche (x2)
                // fonction de comptage 
		HashMap<Integer, Integer> mapMargeDroite = new HashMap<Integer, Integer>();
		int tempInt;
		
		List<Page_PDF> pageList = this.pages;
		for (Page_PDF currPage : pageList) {
			
			if(mapMargeDroite.containsKey(currPage.x2)){
				mapMargeDroite.put(currPage.x2, mapMargeDroite.get(currPage.x2)+1);
			}
			else{
				mapMargeDroite.put(currPage.x2,1);
			}
		}
		
		ValueComparatorIntInt comp = new ValueComparatorIntInt(mapMargeDroite);
		TreeMap<Integer,Integer> sorted_map = new TreeMap<Integer, Integer>(comp);
		sorted_map.putAll(mapMargeDroite);
		
//		System.out.println("MargeGauche:"+mapMargeHaut);
		
		modeMargeDroite = sorted_map.firstKey();
	}
	
	
	public void assignModeMargeGauche(){
		// Parcourt tout le document et assigne le mode
		// de la valeur de la marge de gauche (x1)
            
		HashMap<Integer, Integer> mapMargeGauche = new HashMap<Integer, Integer>();
		int tempInt;
		
		List<Page_PDF> pageList = this.pages;
		for (Page_PDF currPage : pageList) {
			
			if(mapMargeGauche.containsKey(currPage.x1)){
				mapMargeGauche.put(currPage.x1, mapMargeGauche.get(currPage.x1)+1);
			}
			else{
				mapMargeGauche.put(currPage.x1,1);
			}
		}
		
		ValueComparatorIntInt comp = new ValueComparatorIntInt(mapMargeGauche);
		TreeMap<Integer,Integer> sorted_map = new TreeMap<Integer, Integer>(comp);
		sorted_map.putAll(mapMargeGauche);
		
//		System.out.println("MargeGauche:"+mapMargeHaut);
		
		modeMargeGauche = sorted_map.firstKey();
	}
        
        public  void assignMoyMargeHaut(){
            	// Parcourt tout le document et assigne le mode
		// de la valeur de la marge du haut de chaque bloc.
                // les blocs en début de page ne sont pas considérés comme ayant une marge haute
                //.. on considère que c'est une contrainte du support initial
            
		int sommeMargeHaut = 0;
                int cptChunk = 0;
		for (Page_PDF currPage : this.pages) {
                    
                    // premier chunk de chaque page : pas de marge du haut
                    for(int indexChunk = 1; indexChunk < currPage.groupes.size(); indexChunk++){
                        Chunk_PDF currChunk = currPage.groupes.get(indexChunk);
                        Chunk_PDF prevChunk = currPage.groupes.get(indexChunk -1);
                        
                        int margeHaut = currChunk.y1 - prevChunk.y2;
                        // on ne prend pas en compte les marges négatives (= tableaux ou spatialisation de texte, ou erreurs)
                        if(margeHaut > 0 ){
                            sommeMargeHaut += margeHaut; 
                            cptChunk ++;
                        }
                    }
                    
			
		}
		this.moyMargeHaut = sommeMargeHaut/cptChunk;
        }
	
	public void assignMoyMargeBas(){
                /*Pas beaucoup de sens de faire margeHaut et marge Bas :
                    la marge haute d'un chunk est la marge basse d'un autre...
            
            */
        }
        
	public void assignModeTaillePolice() {
                HashMap<Integer, Integer> mapPolice = new HashMap<>();
		
		int max = 0, maxKey = -1, nbOccurs;
		for (Page_PDF currPage : this.pages) {
                    for(Chunk_PDF currChunk : currPage.groupes){
                        if(mapPolice.containsKey(currChunk.modeTaillePolice)){
                                nbOccurs = mapPolice.get(currChunk.modeTaillePolice)+1;
                        }
                        else{
                            nbOccurs = 1;
                        }
                        mapPolice.put(currChunk.modeTaillePolice, nbOccurs);
                        if(max < nbOccurs){
                            max = nbOccurs;
                            maxKey = currChunk.modeTaillePolice;
                        }
                    }
		}
                
		this.modeTaillePolice = maxKey;
	}
}
