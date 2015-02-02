package org.melodi.analyser;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import logicalobjects.Chunk_Lara;
import logicalobjects.Document_Lara;
import logicalobjects.Term;

import org.latoe.textobject.TextPattern_Detection;
import org.melodi.analyser.acabit_client.service.ACABITClient;
import org.melodi.analyser.talismane_client.service.Structure;
import org.melodi.analyser.talismane_client.service.TalismaneClient;
import org.melodi.analyser.yatea_client.service.YateaClient;
import org.melodi.reader.larat.internal.Item;
import org.melodi.reader.larat.internal.Unit;

public class Analyzer_Service {
	
	static boolean printStep = false;
	static boolean printTermino = false;
	static String host_fuzzymatcher = "localhost";
	static String host_talismane = "localhost";
	
	
	public static void addStructureToAllChunk(Document_Lara currDocument) throws UnknownHostException, IOException, InterruptedException{
		
		
		TalismaneClient talismane_client = new TalismaneClient();
		talismane_client.setHost(host_talismane);
		
		for(Chunk_Lara currChunk : currDocument.getChunk()){
			
			Structure currStructure = talismane_client.analyse(currChunk.getText(),false);
			currChunk.setStructure(currStructure);
		}
	}
	
	
	public static void addWordToChunk(Chunk_Lara currChunk) throws UnknownHostException, ClassNotFoundException, IOException{
		
		if(printStep)
		System.out.println("Analyse UniqWord ...");
		TextPattern_Detection pattern_detection = new TextPattern_Detection();
		
		Structure currStructure = currChunk.getStructure();
		ArrayList<Term> candidates = pattern_detection.word(currChunk.getText(),currStructure);
		ArrayList<Term> tmp_ArrayList = currChunk.getTerms();
		tmp_ArrayList.addAll(candidates);
	}
	
	public static void addWordToChunk(Unit currUnit) throws UnknownHostException, ClassNotFoundException, IOException{
		
		if(printStep)
		System.out.println("Analyse UniqWord ...");
		TextPattern_Detection pattern_detection = new TextPattern_Detection();
		
		if(currUnit.getPrimer() == null || currUnit.getPrimer().getListChunk().size() == 0){
			System.err.println("Warning : currUnit has no chunk");
		}
		
		for(Chunk_Lara currChunk : currUnit.getPrimer().getListChunk()){
			Structure currStructure = currChunk.getStructure();
			ArrayList<Term> candidates = pattern_detection.word(currChunk.getText(),currStructure);
			ArrayList<Term> tmp_ArrayList = currChunk.getTerms();
			tmp_ArrayList.addAll(candidates);
		}
		for(Item currItem : currUnit.getItems()){
			for(Chunk_Lara currChunk : currItem.getListChunk()){
				Structure currStructure = currChunk.getStructure();
				ArrayList<Term> candidates = pattern_detection.word(currChunk.getText(), currStructure);
				ArrayList<Term> tmp_ArrayList = currChunk.getTerms();
				tmp_ArrayList.addAll(candidates);
			}
		}
		
	}
	
	public static void addStructureToChunk(Unit currUnit) throws UnknownHostException, IOException, InterruptedException{
		
		TalismaneClient talismane_client = new TalismaneClient();
		talismane_client.setHost(host_talismane);
		
		if(printStep)
		System.out.println("Analyse Talismane ...");
		
		if(currUnit.getPrimer() == null || currUnit.getPrimer().getListChunk().size() == 0){
			System.err.println("Warning : currUnit has no chunk");
		}
		
		
		for(Chunk_Lara currChunk : currUnit.getPrimer().getListChunk()){
			Structure currStructure = talismane_client.analyse(currChunk.getText(),false);
			currChunk.setStructure(currStructure);
		}
		
		for(Item currItem : currUnit.getItems()){
			for(Chunk_Lara currChunk : currItem.getListChunk()){
				Structure currStructure = talismane_client.analyse(currChunk.getText(),false);
				currChunk.setStructure(currStructure);
			}
		}
		
		
	}
	
	public static void addTermsToChunk_ACABIT(Chunk_Lara currChunk) throws ClassNotFoundException, IOException, InterruptedException{
		ACABITClient acabit_client = new ACABITClient();
		acabit_client.setHost(Analyzer_Service.host_fuzzymatcher);
		if(printStep)
		System.out.println("Analyse ACABIT ...");
		
	
		Structure currStructure = currChunk.getStructure();
		ArrayList<Term> candidates = acabit_client.analyse(currChunk.getText(),currStructure,false,printTermino);
		ArrayList<Term> tmp_ArrayList = currChunk.getTerms();
		tmp_ArrayList.addAll(candidates);
	}
	
	public static void addTermsToChunk_YaTeA(Chunk_Lara currChunk) throws ClassNotFoundException, IOException, InterruptedException{
		
		YateaClient yatea_client = new YateaClient();
		yatea_client.setHost(Analyzer_Service.host_fuzzymatcher);
		
		if(printStep)
		System.out.println("Analyse YaTeA ...");
		
		Structure currStructure = currChunk.getStructure();
		ArrayList<Term> candidates = yatea_client.analyse(currChunk.getText(),currStructure,printTermino);
		ArrayList<Term> tmp_ArrayList = currChunk.getTerms();
		tmp_ArrayList.addAll(candidates);
		
	}

	public static void addTermsToChunk_ACABIT(Unit currUnit) throws IOException, InterruptedException, ClassNotFoundException{
		
		ACABITClient acabit_client = new ACABITClient();
		acabit_client.setHost(Analyzer_Service.host_fuzzymatcher);
		if(printStep)
		System.out.println("Analyse ACABIT ...");
		
		if(currUnit.getPrimer() == null || currUnit.getPrimer().getListChunk().size() == 0){
			System.err.println("Warning : currUnit has no chunk");
		}
		
		for(Chunk_Lara currChunk : currUnit.getPrimer().getListChunk()){
			Structure currStructure = currChunk.getStructure();
			ArrayList<Term> candidates = acabit_client.analyse(currChunk.getText(),currStructure,false,printTermino);
			ArrayList<Term> tmp_ArrayList = currChunk.getTerms();
			tmp_ArrayList.addAll(candidates);
		}
		for(Item currItem : currUnit.getItems()){
			for(Chunk_Lara currChunk : currItem.getListChunk()){
				Structure currStructure = currChunk.getStructure();
				ArrayList<Term> candidates = acabit_client.analyse(currChunk.getText(), currStructure,false,printTermino);
				ArrayList<Term> tmp_ArrayList = currChunk.getTerms();
				tmp_ArrayList.addAll(candidates);
			}
		}
		
		
	}
	
	public static void addTermsToChunk_YaTeA(Unit currUnit) throws UnknownHostException, IOException, InterruptedException, ClassNotFoundException{
		
		YateaClient yatea_client = new YateaClient();
		yatea_client.setHost(Analyzer_Service.host_fuzzymatcher);
		
		if(currUnit.getPrimer() == null || currUnit.getPrimer().getListChunk().size() == 0){
			System.err.println("Warning : currUnit has no chunk");
		}
		if(printStep)
		System.out.println("Analyse YaTeA ...");
		
		for(Chunk_Lara currChunk : currUnit.getPrimer().getListChunk()){
			Structure currStructure = currChunk.getStructure();
			ArrayList<Term> candidates = yatea_client.analyse(currChunk.getText(),currStructure,printTermino);
			ArrayList<Term> tmp_ArrayList = currChunk.getTerms();
			tmp_ArrayList.addAll(candidates);
		}
		for(Item currItem : currUnit.getItems()){
			for(Chunk_Lara currChunk : currItem.getListChunk()){
				Structure currStructure = currChunk.getStructure();
				ArrayList<Term> candidates = yatea_client.analyse(currChunk.getText(),currStructure,printTermino);
				ArrayList<Term> tmp_ArrayList = currChunk.getTerms();
				tmp_ArrayList.addAll(candidates);
			}
		}
	}

	public static String getHost_fuzzymatcher() {
		return host_fuzzymatcher;
	}

	public static void setHost_fuzzymatcher(String host_fuzzymatcher) {
		Analyzer_Service.host_fuzzymatcher = host_fuzzymatcher;
	}

	public static String getHost_talismane() {
		return host_talismane;
	}

	public static void setHost_talismane(String host_talismane) {
		Analyzer_Service.host_talismane = host_talismane;
	}
	
	
	
	

}
