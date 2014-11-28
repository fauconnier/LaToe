package org.melodi.reader.larat.tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.melodi.reader.larat.internal.Unit;
import org.melodi.reader.larat.model.Larat_inputoutput;

import au.com.bytecode.opencsv.*;

public class WriteCSV {
	
	
	public static void run(String [] args){
		
		// Un fichier de tableau par document d'annotation
		String doc_julien = "./annotation/Phase_2/Julien/LARA_corpus/Menhir.html";
		String doc_sophie = "./annotation/Phase_2/Sophie/LARA_xml/Menhir.xml";
		String doc_m_jp = "./annotation/Phase_2/M_Jp/LARA_xml/Menhir.xml";
		
		// Ouvrir les fichiers d'annotation [okay]
		Larat_inputoutput io_julien = new Larat_inputoutput();
		io_julien.readThis(doc_julien);
		LinkedList<Unit>	chain_julien = io_julien.getChain();
		
		Larat_inputoutput io_sophie = new Larat_inputoutput();
		io_sophie.readThis(doc_sophie);
		LinkedList<Unit> chain_sophie = io_sophie.getChain();
		
		Larat_inputoutput io_m_jp = new Larat_inputoutput();
		io_m_jp.readThis(doc_m_jp);
		LinkedList<Unit> chain_m_jp = io_m_jp.getChain();
		
		
		 // Préparation du fichier .CSV
		try {
			CSVWriter writer = new CSVWriter(new FileWriter("yourfile.csv"),
					'\t');
			// feed in your array (or convert your data to an array)
			String[] entries = "##Axe rhétorique###Axe intentionnel###Axe sémantique###Contexte/Non-Contextuel###Concept amorce###Concept(s) items###Circonstant amorce###Circonstant(s) items".split("#");
			writer.writeNext(entries);
			entries="".split("#");
			writer.writeNext(entries);
			writer.writeNext(entries);
			
			
			for(int i=0;i < chain_m_jp.size(); i++){
				Unit se_julien = chain_julien.get(i);
				Unit se_sophie = chain_sophie.get(i);
				Unit se_m_jp = chain_m_jp.get(i);
				
				String se_julien_rhetorique = axeRehtorique(se_julien);
				String se_sophie_rhetorique = axeRehtorique(se_sophie);
				String se_m_jp_rhetorique = axeRehtorique(se_m_jp);

				String [] tab = new String[100];
				tab[1] = se_julien_rhetorique;
				tab[2] = se_sophie_rhetorique;
				tab[3] = se_m_jp_rhetorique;
				
				writer.writeNext(tab);
				
				
				
				
			}
			
			// SE
			
			
			
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static String axeRehtorique(Unit currentSE){
		
		String axe_rethorique_return = "";
		if(currentSE.getAxe_rhetorique().equals("")){
			axe_rethorique_return  = "?";
		}
		else{
			axe_rethorique_return  = currentSE.getAxe_rhetorique();
		}
		
		return axe_rethorique_return;
	}
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		run(args);
		

	}

}
