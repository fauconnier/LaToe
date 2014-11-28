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

public class Sentence extends ArrayList<Token> implements Serializable{
	
	private int id_sentence;

	/**
	 * A simple data structure to represent a sentence in CoNLL Format.
	 * This format is based on the CoNLL format. 
	 * 
	 * @see <a href="http://nextens.uvt.nl/depparse-wiki/DataFormat">CoNLL format</a>
	 * @author Jean-Philippe Fauconnier
	 */
	public Sentence(int id_sentence) {
		this.id_sentence = id_sentence;
	}
	
	public Sentence(){
		
	}
	
	public void print(){
		System.out.println(this.stringDump());
	}
	
	public String stringDump(){
		/* to get a string dump of a CoNLL_Sentence*/
		String all_token = "-------"+ this.getId_sentence()+"-------\n";
		for(Token currToken : this){
			all_token += currToken.stringDump() + "\n";
		}
		return all_token;
	}

	/*
	 * Getters and Setters 
	 */
	public int getId_sentence() {
		return id_sentence;
	}

	public void setId_sentence(int id_sentence) {
		this.id_sentence = id_sentence;
	}

	

}
