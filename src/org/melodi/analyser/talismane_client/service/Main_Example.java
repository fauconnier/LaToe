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

import java.io.IOException;
import java.net.UnknownHostException;

import org.melodi.analyser.talismane_client.service.TalismaneClient;

public class Main_Example {
	
	/**
	 * Example in a java environment.
	 * 
	 * @param args
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InterruptedException
	 * @author Jean-Philippe Fauconnier
	 */
	public static void main(String[] args) throws UnknownHostException,
			IOException, InterruptedException {

		/* default values : localhost, port 7272 and UTF-8 */
		TalismaneClient talismane_client = new TalismaneClient();
		
		/* ping server */
		boolean isAlive = talismane_client.pingServer();
		
		/* analyse a String */
		talismane_client.analyse("John a poussé Marie.");

		/* analyse a file */
		String text = talismane_client
				.readFile("./samples/stendhal_sample_1.txt");
		Structure stendhal_conll = talismane_client.analyse(text);
		
		/* print first lemma of each sentence */
		for(Sentence currSentence : stendhal_conll){
			Token currToken = currSentence.get(0);
			System.out.println(currToken.getLemma());
		}
		
		/* analyse a text without print output during the analysis */
		Structure struct_connll = talismane_client.analyse("Marie est tombée.",false);
		Sentence sentence = struct_connll.get(0);
		sentence.print();
		
		/* close server */
		talismane_client.closeServer();
		
	}

}
