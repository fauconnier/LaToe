///////////////////////////////////////////////////////////////////////////////
//Copyright (C) 2012 Jean-Philippe Fauconnier
//
//This file is part of TalismaneClient.
//
//TalismaneClient is free software: you can redistribute it and/or modify
//it under the terms of the GNU Affero General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//Talismane is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Affero General Public License for more details.
//
//You should have received a copy of the GNU Affero General Public License
//along with Talismane.  If not, see <http://www.gnu.org/licenses/>.
//////////////////////////////////////////////////////////////////////////////
package org.melodi.analyser.talismane_client.service;

import java.io.BufferedReader;


public class SentenceAnalyser extends Thread {
	
	private int id_sentence;
	private String sentence;
	private Structure conll_structure;
	private boolean print;

	/**
	 * Thread class to split each sentence.
	 * 
	 * @param id_sentence
	 * @param sentence
	 * @param conll_structure
	 * @param print
	 * @author Jean-philippe Fauconnier
	 */
	public SentenceAnalyser(int id_sentence, String sentence, Structure conll_structure, boolean print) {
		
		this.id_sentence=id_sentence;
		this.sentence = sentence;
		this.conll_structure = conll_structure;
		this.print=print;
	}

	public void run() {
	
		/* process sentence */
		Sentence conll_sentence = new Sentence(id_sentence);
		 
		String[] token_lines = sentence.split("\n");
	     for (String line : token_lines){
	         Token token = new Token(line.split("\t"));
	         conll_sentence.add(token);
	     }
	     /* print sentence */
	     if(print)
	    	 conll_sentence.print();
	     
	     /* add sentence to CoNNL_Structure */
	     conll_structure.add(conll_sentence);
	}

}
