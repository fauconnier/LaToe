package org.latoe.textobject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.melodi.analyser.talismane_client.service.Sentence;
import org.melodi.analyser.talismane_client.service.Structure;
import org.melodi.analyser.talismane_client.service.Token;
import org.melodi.objectslogic.Chunk_Lara;
import org.melodi.objectslogic.Term;
import org.melodi.tools.fuzzymatcher.client.FuzzyMatcher_Client;
import org.melodi.tools.fuzzymatcher.datamodel.Message_Matcher;

public class TextPattern_Detection {
	
	
	public TextPattern_Detection(){
		
	}
	
	public ArrayList<Term> word(String original_text, Structure currStructure) throws UnknownHostException, ClassNotFoundException, IOException{
		ArrayList<Term> newArrayTerm = new ArrayList<Term>();
		
		
		
		for(Sentence currSentence : currStructure){
			//* Pattern tel que */
			
			String lemma = "";
			String postag = "";
			String dep = "";
			for(Token currToken : currSentence){
				lemma += currToken.getLemma() + " ";
				dep += currToken.getDeprel() + " ";
				postag += currToken.getCppostag() + " ";
			}
			
			for(Token currToken : currSentence){
				
				if(currToken.getCppostag().equals("NC") || currToken.getCppostag().equals("NPP")){
					Term newTerm = new Term();
					newTerm.setText(currToken.getForm());
					
					
					FuzzyMatcher_Client fuzzymatcher_client = new FuzzyMatcher_Client();
					fuzzymatcher_client.setHost("localhost");
					
					
					
					// Oublie message matcher
					
					
					 Message_Matcher msg_matcher 
			         = fuzzymatcher_client.analyse(original_text, currToken.getForm());
					 
						if(msg_matcher.getMatching()){
							
							newTerm.setOrigin("uniq");
							newTerm.setStart(msg_matcher.getStart());;
							newTerm.setEnd(msg_matcher.getEnd());
							
							newArrayTerm.add(newTerm);
						}
						else{
							System.err.println("Erreur dans Uniq " + currToken.getForm());
						}
				}
			}
			
		}
		
		return newArrayTerm;
	}
	
	public ArrayList<String> apply_Pattern(Chunk_Lara currChunk) throws UnknownHostException, ClassNotFoundException, IOException{
		ArrayList<String> matched_string = new ArrayList<String>();
		
		
		for(Sentence currSentence : currChunk.getStructure()){
			
			//* Pattern tel que */
			
			String lemma = "";
			String dep = "";
			for(Token currToken : currSentence){
				lemma += currToken.getLemma() + " ";
				dep += currToken.getDeprel() + " ";
			}
			
//			for(Token currToken2 : currSentence){
//					int id_dep = currToken2.getId_token();
//					if(currSentence.get(id_dep).getLemma().equals("être") ){
//						
//						String toReturn = "";
//						for(Token currToken3 : currSentence){
//							if(currToken3.getId_token() > id_dep){
//								toReturn += currToken3.getForm() + " " ;
//							}
//						}
//						System.out.println(currToken2.getForm() + " " + toReturn);
//					}
//				}
//			}
			
			if(lemma.contains("tel que")){
				System.out.println(currSentence.stringDump());
			}
			if(lemma.contains("être une sorte de")){
				System.out.println(currSentence.stringDump());
			}
			if(lemma.contains("être un type de")){
				System.out.println(currSentence.stringDump());
			}
			if(lemma.contains("incluant")){
				System.out.println(currSentence.stringDump());
			}
			if(lemma.contains("et autre")){
				System.out.println(currSentence.stringDump());
			}
		}
		
		return matched_string;
	}

}
