package org.melodi.analyser.acabit_client.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import logicalobjects.Term;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.melodi.analyser.talismane_client.service.Sentence;
import org.melodi.analyser.talismane_client.service.Structure;
import org.melodi.analyser.talismane_client.service.Token;
import org.melodi.tools.fuzzymatcher.client.FuzzyMatcher_Client;
import org.melodi.tools.fuzzymatcher.datamodel.Message_Matcher;

/**
 * Client pour ACABIT
 * @author jfaucon
 *
 */
public class ACABITClient {
	
	boolean test = false;
	boolean print;
	String host_fuzzymatcher = "192.168.1.6";
	
	public ACABITClient(){
		
	}
	
	
	public void setHost(String host){
		this.host_fuzzymatcher = host;
	}

	public ArrayList<Term> analyse(String text, Structure currStructure)
			throws UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		return this.analyse(text, currStructure, false, false);
	}

	public ArrayList<Term> analyse(String text, Structure currStructure, boolean statisticsort, boolean print) throws IOException, InterruptedException, ClassNotFoundException {
//		String workingDir = System.getProperty("user.dir");
		
		this.print = print;
		
		String workingDir = "/home/jfaucon/workspace/acabit-client";
		
		/**
		 * Ecriture
		 */
		this.writeACABITFormat(currStructure, workingDir+"/tmp.txt"); /* write */
		File xml = new File(workingDir+"/out.xml");
		xml.delete();
		
		/**
		 *  Analyse avec YaTea
		 */
		Runtime runtime = Runtime.getRuntime();
		
		String [] cmdarray1 = new String [3];
		cmdarray1 [0] = "perl";
		cmdarray1 [1] = "fr_stat.pl";
		cmdarray1 [2] = workingDir+"/tmp.txt";
		
		String [] envp = new String [0];
		File dir = new File( workingDir+"/");
		
		final Process process = runtime.exec(cmdarray1, envp, dir);
		process.waitFor();
		
		
		String [] cmdarray2 = new String [2];
		cmdarray2 [0] = "perl";
		cmdarray2 [1] = "fr_tri.pl";
		
		final Process process2 = runtime.exec(cmdarray2, envp, dir);
		process2.waitFor();
		
		
		/**
		 *  Récupération des éléments
		 */
		ArrayList<Term> candidate_terms = new ArrayList<Term>();
		if(statisticsort){
			File xml_exist = new File(workingDir+"/out.xml");
			FileInputStream fis = new FileInputStream(xml_exist);
			String str = "";
			LineNumberReader l = new LineNumberReader(new BufferedReader(new InputStreamReader(fis)));
			
			int count = 0;
			while ((str=l.readLine())!=null){
				count = l.getLineNumber();
			}
			
			if(count > 0){
				candidate_terms = this.getResults(text, currStructure, workingDir+"/out.xml");
			}
		
		}
		else{
			File xml_exist = new File(workingDir+"/REA/temp2.txt");
			FileInputStream fis = new FileInputStream(xml_exist);
			String str = "";
			LineNumberReader l = new LineNumberReader(new BufferedReader(new InputStreamReader(fis)));
			
			int count = 0;
			while ((str=l.readLine())!=null){
				count = l.getLineNumber();
			}
			
			if(count > 0){
				candidate_terms = this.getResultsSimple(text, currStructure, workingDir+"/REA/temp2.txt");
			}
			
		}
		
		return candidate_terms;
	}
	
	
	private ArrayList<Term> getResultsSimple(String original_text, Structure currStructure, String path) throws IOException, ClassNotFoundException{
		
		ArrayList<Term> candidate_terms = new ArrayList<Term>();
		
		File xml_exist = new File(path);
		FileInputStream fis = new FileInputStream(xml_exist);
		String str = "";
		LineNumberReader l = new LineNumberReader(new BufferedReader(new InputStreamReader(fis)));
		
		while ((str=l.readLine())!=null){
			
			String [] array = str.split("---");
			String candidate = array[1];
			candidate = candidate.trim();
//			System.out.println(candidate);
			
			candidate_terms.addAll(associateStructureAndFlexions(original_text, currStructure,candidate));
			
		}
		
		return candidate_terms;
	}
	
	
	private ArrayList<Term> getResults(String original_text, Structure currStructure, String path) throws IOException, ClassNotFoundException{
		ArrayList<Term> candidate_terms = new ArrayList<Term>();
		
		SAXBuilder sxb = new SAXBuilder();
		String pathXml =  path;
		
		Document document =  new Document();
		try {
			document =  sxb.build(new File(pathXml));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			System.err.println("Error in reading XML");
			e.printStackTrace();
		}

		Element root = document.getRootElement();
		List candidates = root.getChildren("SETCAND");
		Iterator i = candidates.iterator();
		
		// Iteration sur les terms extraits
		while (i.hasNext()) {
			Element setcand = (Element) i.next();
			
			List niv1 =  setcand.getChildren();
			Iterator j = niv1.iterator();
			
			// Iteration de niv1 : CAND 
			while(j.hasNext()){
				Element niv1_element = (Element) j.next();
				
				List niv2 = niv1_element.getChildren();
				Iterator k = niv2.iterator();
				
				while(k.hasNext()){ // Iteration de niv 2 : NA
					Element niv2_element = (Element) k.next();
					
					List niv3 = niv2_element.getChildren();
					Iterator v = niv3.iterator();
					
					while(v.hasNext()){ // MODIF BASE et autre
						Element niv3_element = (Element) v.next();
						
						List niv4 = niv3_element.getChildren();
						Iterator x = niv4.iterator();
						
						while(x.hasNext()){
							Element term = (Element) x.next();
							if(term.getName().equals("TERM")){
								candidate_terms.addAll(associateStructureAndTermLemma(original_text, currStructure,term.getText()));
							}
						}
					}
				}
			}
		}
		
		
		return candidate_terms;
	}
	
	
	private ArrayList<Term> associateStructureAndFlexions(String original_text, Structure currStructure, String term) throws UnknownHostException, ClassNotFoundException, IOException{
		
		ArrayList<Term> subset_candidate = new ArrayList<Term>();
		
//		System.out.println(term);
//		System.out.println(original_text);
		
		FuzzyMatcher_Client fuzzymatcher_client = new FuzzyMatcher_Client();
		fuzzymatcher_client.setHost(host_fuzzymatcher);
		
		 Message_Matcher msg_matcher 
         = fuzzymatcher_client.analyse(original_text, term);
		
		
		if(msg_matcher.getMatching()){
			
			Term newTerm = new Term();
			newTerm.setText(term);
			newTerm.setOrigin("acabit");
			newTerm.setStart(msg_matcher.getStart());;
			newTerm.setEnd(msg_matcher.getEnd());
			subset_candidate.add(newTerm);
			
			if(print){
				System.out.println("ACABIT:" + newTerm.toString());
			}
		}
		else{
			System.err.println("Erreur dans ACABIT no tri " + term);
		}
		
		
		
		return subset_candidate;
	}
	
	private ArrayList<Term> associateStructureAndTermLemma(String original_text, Structure currStructure, String term) throws UnknownHostException, ClassNotFoundException, IOException{
		ArrayList<Term> subset_candidate = new ArrayList<Term>();
		
		
		// TERM : LEMMA LEMMA LEMMA 
		term = term.trim();
		String [] lemma = term.split(" ");
		
		
		
		/**
		 * Problème : ACABIT retourne des lemmes et non la forme retrouvée.
		 * Ici, essai pour associer 
		 */
		ArrayList<ArrayList<Token>> retrieved = new ArrayList<ArrayList<Token>>();
		
		// Parcours de la sentence
		
		for(Sentence currSentence : currStructure){
			int index_token = 0;
			for(Token currToken : currSentence){
				
				boolean flag = false;
				ArrayList<Token> current = new ArrayList<Token>();
				retrieved.add(current);
				
				
				// Matching sur les Lemmes
				for(int j=0;j<lemma.length;j++){
					
					int curr_index = index_token+j;
					if(curr_index < currSentence.size()){
						
						if(currSentence.get(curr_index).getLemma().equals(lemma[j])){
							flag=true;
							
							current.add(currSentence.get(curr_index));
						}
						else{
							flag=false;
							retrieved.remove(current);
						}
						
					}
					
				}
				index_token++;
			}
		}
		
		
		for(ArrayList<Token> token : retrieved){
//			System.out.print("FLAG:");
			
			String text_of_candidate = "";
			for(Token currToken2 : token){
				text_of_candidate += currToken2.getForm() + " ";
			}
			text_of_candidate = text_of_candidate.trim();
			if(print){
				System.out.println("ACABIT:" + text_of_candidate);
			}
			
			Term newTerm = new Term();
			newTerm.setTokens(token);
			newTerm.setText(text_of_candidate);
			newTerm.setOrigin("acabit");
			subset_candidate.add(newTerm);
			
			FuzzyMatcher_Client fuzzymatcher_client = new FuzzyMatcher_Client();
			fuzzymatcher_client.setHost(host_fuzzymatcher);
			
			 Message_Matcher msg_matcher 
	         = fuzzymatcher_client.analyse(original_text, text_of_candidate);
			 
			if(msg_matcher.getMatching()){
				
				int start = msg_matcher.getStart();
				int end = msg_matcher.getEnd();
				
				newTerm.setStart(start);
				newTerm.setEnd(end);
				
			}
			else{
				System.err.println("ERREUR dans ACABIT");
			}
			
		}
		
		
		
		if(subset_candidate.size() == 0){
			System.err.println("Erreur : Term ACABIT non retrouvé " + term);
		}
				
		return subset_candidate;
	}
	
public void writeACABITFormat(Structure currStructure, String path) throws IOException{
		
		File outFile = new File(path);
		Writer destFile = null;
		
		destFile = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "UTF-8"));
		
		String toWrite = "";
		
		toWrite += "<record>\n";
		toWrite += "<AN>\n";
		toWrite += "92/CAR/92 -/- 0014602/CAR/0014602\n";
		toWrite += "</AN>\n";
		
		toWrite += "<AB>\n";
		int ph_nb = 1;
		for(Sentence currSentence : currStructure){
			
			toWrite += "<ph_nb="+ph_nb+">";
			for(Token currToken : currSentence){
				
//				if(this.mapping(currToken).getCppostag().equals("")){
//					System.out.print(currToken.getForm());
//					System.out.print("\t");
//					System.out.print(currToken.getCppostag());
//					System.out.print("\t");
//					System.out.print(currToken.getFeats());
//					System.out.print("\t");
//					System.out.print(currToken.getLemma());
//					System.out.print("\n");
//					System.out.print(this.mapping(currToken));
//					System.out.println("");
//					
//					System.err.println("Erreur ACABIT : postag non reconnu");
//				}
				
				
				// Crade
				String original = this.mapping(currToken);
				String currPostag = currToken.getCppostag();
						
				String posmorpho = "";
				if(currPostag.equals("SBC") || currPostag.equals("DTN") || currPostag.equals("ADJ")){
					String feats = currToken.getFeats();
					String nombre = "_";
					if(feats.contains("n=s")){
						nombre = "s";
					}else if(feats.contains("n=p")){
						nombre = "p";
					}
					String genre = "_";
					if(feats.contains("n=m")){
						genre = "m";
					}else if(feats.contains("n=f")){
						genre = "f";
					}
					
					posmorpho = currPostag+":"+genre+":"+nombre;
				}else if(currPostag.equals("ADV")){
					posmorpho = currPostag;
				}
				else if(currPostag.equals("PREP")){
					posmorpho = currPostag;
				}
				else if(currPostag.equals("COO")){
					posmorpho = currPostag;
				}
				else if(currPostag.equals("PRV")){
					
					String feats = currToken.getFeats();
					String nombre = "_";
					if(feats.contains("n=s")){
						nombre = "s";
					}else if(feats.contains("n=p")){
						nombre = "p";
					}
					String genre = "_";
					if(feats.contains("n=m")){
						genre = "m";
					}else if(feats.contains("n=f")){
						genre = "f";
					}
					String personne = "_";
					if(feats.contains("p=3")){
						personne = "3p";
					}else if(feats.contains("n=p")){
						personne = "_";
					}
					
					posmorpho = currPostag + ":" + personne + ":_:" + nombre + ":" + genre;
					
					
				}
				else{
					posmorpho = currPostag + " " + currToken.getFeats();
				}
				
				
				toWrite += currToken.getForm() + "/" + posmorpho + "/" + currToken.getLemma() + " ";
				if(test){
					toWrite += "\n";
				}
				
				// IMPORTANT  : replace original token
				currToken.setCppostag(original);
				
			}
			ph_nb ++;
			toWrite += "</ph>\n";
		}
		toWrite += "</AB>\n";
		toWrite += "</record>\n";
		
		destFile.write(toWrite);
		destFile.close();
		
	}
	
	public String mapping(Token currToken){
		
		String postag = currToken.getCppostag();
		String original = postag;
		String lemma = currToken.getLemma();
		String feats = currToken.getFeats();
		
		String postag_treeTagger = "";
				
		
		if(postag.equals("DET")) {
//			if(lemma.equals("le") || lemma.equals("la") || lemma.equals("les")  ){
//				postag_treeTagger = "DET:ART";
//			}
//			else 
			if( lemma.equals("leur") || lemma.equals("ton") || lemma.equals("ta") || lemma.equals("tes") || lemma.equals("leurs")
					|| lemma.equals("mon") || lemma.equals("ma") || lemma.equals("mes")){
				currToken.setCppostag("DTN");
			}
			else{
				
				if(currToken.getLemma().equals("l'")){
					currToken.setLemma("le");
				}
				if(currToken.getLemma().equals("d'")){
					currToken.setLemma("de");
				}
				currToken.setCppostag("DTN");
			}
		}
		else if(postag.equals("NC")){
			currToken.setCppostag("SBC");
		}
		else if(postag.equals("P")){
			currToken.setCppostag("PREP");
		}
		else if(postag.equals("CC")){
			currToken.setCppostag("COO");
		}
		else if(postag.equals("PONCT")){
			currToken.setCppostag("PUN");
		}
		else if(postag.equals("PRO")){
			currToken.setCppostag("PRO");
		}
		else if(postag.equals("P+D")){
			currToken.setCppostag("DTC");
		}
		else if(postag.equals("ADV")){
			currToken.setCppostag("ADV");
		}
		else if(postag.equals("ADJ")){
			currToken.setCppostag("ADJ");
		}
		else if(postag.equals("VPP")){
			currToken.setCppostag("VER:pper");
		}
		else if(postag.equals("CLS")){
			currToken.setCppostag("PRV");
		}
		else if(postag.equals("VINF")){
			currToken.setCppostag("VNCFF");
		}
		else if(postag.equals("NPP")){
			currToken.setCppostag("SBP");
		}
		else if(postag.equals("V")){
			if(feats.contains("|t=P")){
				currToken.setCppostag("VER:pres");
			}
		}
		else if(postag.equals("PROREL")){
			currToken.setCppostag("PRO:REL");
		}
		else{
//			System.err.println("Erreur dans TreeTaggerMapping pour " + currToken.getCppostag());
		}
		
		if(currToken.getLemma().equals("_")){
			currToken.setLemma(currToken.getForm());
		}
		
		
		return original;
	}

}
