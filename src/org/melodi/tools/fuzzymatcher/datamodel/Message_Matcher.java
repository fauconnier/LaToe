package org.melodi.tools.fuzzymatcher.datamodel;

import java.io.Serializable;

public class Message_Matcher implements Serializable{
	
	String source;
	String pattern;

	int start;
	int end;
	double result;
	boolean matching = false;
	
	public Message_Matcher(){
		this.source = "";
		this.pattern = "";
		this.start = -1;
		this.end = -1;
	}
	
	public Message_Matcher(String source, String pattern) {
		this.source = source;
		this.pattern = pattern;
		this.start = -1;
		this.end = -1;
	}
	
	public void setResults(int start, int end, double result){
		this.start = start;
		this.end = end;
		this.result = result;
	}
	
	public int getStart(){
		return this.start;
	}
	
	public int getEnd(){
		return this.end;
	}
	
	public void setStart(int start){
		this.start = start;
	}
	
	public void setEnd(int end){
		this.end = end;
	}
	
	public double getResult(){
		return this.result;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String toString(){
		return source + "\n" + pattern;
	}
	
	public String getMatchedSource(){
		String toReturn = "";
		if(this.start != -1 && this.end != -1){
			toReturn = this.source.substring(start,end);
		}
		return toReturn;
	}
	
	public boolean getMatching(){
		if( this.start == -1 || this.end == -1){
			return false;
		}else{
			return true;
		}
	}

}
