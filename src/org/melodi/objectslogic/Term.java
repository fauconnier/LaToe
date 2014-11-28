package org.melodi.objectslogic;

import java.util.ArrayList;

import org.melodi.analyser.talismane_client.service.Token;


public class Term implements java.io.Serializable{
	
	private int start;
	private int end;
	private String text;
	private int sentence;
	ArrayList<Token> tokens;
	private String origin;
	
	
	public Term(){
		this.tokens = new ArrayList<Token>();
	}


	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getEnd() {
		return end;
	}


	public void setEnd(int end) {
		this.end = end;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public int getSentence() {
		return sentence;
	}


	public void setSentence(int sentence) {
		this.sentence = sentence;
	}
	


	public ArrayList<Token> getTokens() {
		return tokens;
	}


	public void setTokens(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}
	
	public void addToken(Token currToken){
		this.tokens.add(currToken);
	}

	
	

	public String getOrigin() {
		return origin;
	}


	public void setOrigin(String origin) {
		this.origin = origin;
	}


	public String toString(){
		String toReturn ="";
		toReturn += this.getText() + "[" + this.getStart() + "," + this.getEnd() + "]";
		return toReturn;
	}
	
	

}
