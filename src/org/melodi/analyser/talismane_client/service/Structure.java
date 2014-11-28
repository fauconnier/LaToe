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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Structure extends ArrayList<Sentence> implements Serializable{
	
	/**
	 * A simple data structure to use Talismane's default output format.
	 * This format is based on the CoNLL format. 
	 * 
	 * @see <a href="http://nextens.uvt.nl/depparse-wiki/DataFormat">CoNLL format</a>
	 * @author Jean-Philippe Fauconnier
	 */
	public Structure() {
		
	}
	
	public void print(){
		System.out.println(this.stringDump());
	}
	
	public String stringDump(){
		/* to get a string dump of a CoNLL_Structure*/
		String all_sentence = "";
		for(Sentence currSentence : this){
			all_sentence += currSentence.stringDump() + "\n";
		}
		return all_sentence;
	}

}
