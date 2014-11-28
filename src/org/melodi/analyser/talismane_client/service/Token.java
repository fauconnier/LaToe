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

public class Token implements Serializable {

	private int id_token; // id of token in sentence (starting at 1)
	private String form; // word form
	private String lemma; // lemma
	private String cppostag; // Coarse-grained part-of-speech
	private String postag; // Fine-graind part-of-speech
	private String feats; // Unordered syntactic/morphological features
	private int head; // head of current token
	private String deprel; // dependency relation
	private int phead; // projective head
	private String pdeprel; // dependency relation of phead
	private char space_char = '\t';

	/**
	 * A simple data structure to represent a token in CoNLL Format. See
	 * specifications of <a
	 * href="http://nextens.uvt.nl/depparse-wiki/DataFormat">CoNLL format</a>
	 * 
	 * @author Jean-Philippe Fauconnier
	 */

	public Token(String[] line) {

		if(line.length == 10){
			this.id_token = Integer.parseInt(line[0]);
			this.form = line[1];
			this.lemma = line[2];
			this.cppostag = line[3];
			this.postag = line[4];
			this.feats = line[5];
			this.head = Integer.parseInt(line[6]);
			this.deprel = line[7];
			this.phead = Integer.parseInt(line[8]);
			this.pdeprel = line[9];
		}else{
			System.err.println("Invalid args for CoNNL_Token");
		}
	}
	
	public Token(){
		
	}
	
	
	public void print(){
		System.out.println(this.stringDump());
	}

	
	public String stringDump() {
		// TODO : clean code
		/* to get a string dump of a CoNLL_Token*/
		 return "" + id_token + space_char + form + space_char + lemma + space_char + cppostag + space_char +
		 postag + space_char + feats + space_char + head + space_char + deprel + space_char + phead + space_char
		 + pdeprel;
	}
	
	/*
	 * Getters and Setters
	 */
	public int getId_token() {
		return id_token;
	}

	public void setId_token(int id) {
		this.id_token = id;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getCppostag() {
		return cppostag;
	}

	public void setCppostag(String cppostag) {
		this.cppostag = cppostag;
	}

	public String getPostag() {
		return postag;
	}

	public void setPostag(String postag) {
		this.postag = postag;
	}

	public String getFeats() {
		return feats;
	}

	public void setFeats(String feats) {
		this.feats = feats;
	}

	public int getHead() {
		return head;
	}

	public void setHead(int head) {
		this.head = head;
	}

	public String getDeprel() {
		return deprel;
	}

	public void setDeprel(String deprel) {
		this.deprel = deprel;
	}

	public int getPhead() {
		return phead;
	}

	public void setPhead(int phead) {
		this.phead = phead;
	}

	public String getPdeprel() {
		return pdeprel;
	}

	public void setPdeprel(String pdeprel) {
		this.pdeprel = pdeprel;
	}

}
