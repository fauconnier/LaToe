package org.melodi.tools.fuzzymatcher.server;

import org.melodi.tools.fuzzymatcher.datamodel.Message_Matcher;
import org.melodi.tools.fuzzymatcher.net.java.frej.fuzzy.Fuzzy;


public class FuzzyMatcher_Service {

	String source;
	String pattern;
	int start;
	int end;
	double result;

	public FuzzyMatcher_Service() {

	}
	
	public boolean contains(Message_Matcher currMsg_Matcher){
		
		String source = currMsg_Matcher.getSource();
		String pattern = currMsg_Matcher.getPattern();
		
		// Exact matching
		if (source.contains(pattern)) {
			System.out.println("Exact matching");
			
			this.start = source.indexOf(pattern);
			this.end = start + pattern.length();
			this.result = 1.0;
			currMsg_Matcher.setResults(start, end, result);
			System.out.println("Exactly found : pattern [" + pattern + "] in source [" + source + "]");
			return true;

		} else {

			Fuzzy newFuzzy = new Fuzzy(source.length(),
					pattern.length());

			if (newFuzzy.containsOneOf(source, pattern)) {

				this.start = newFuzzy.getResultStart() - 1;
				this.end = newFuzzy.getResultEnd();
				this.result = newFuzzy.getResult();
				currMsg_Matcher.setResults(start, end, result);
				
				System.out.println("Fuzzy found : pattern [" + pattern + "] in source [" + source + "]");
				return true;
				

			} else {
				System.out.println("Not Found : pattern [" + pattern + "] in source [" + source + "]");
				this.start = -1;
				this.end = -1;
				this.result = -1;
				currMsg_Matcher.setResults(start, end, result);
				return false;
			}
		}
	}

	public boolean contains(String source, String pattern) {

		// Exact matching
		if (source.contains(pattern)) {
			System.out.println("Exact matching");
			
			this.start = source.indexOf(pattern);
			this.end = start + pattern.length();
			this.result = 1.0;
			return true;

		} else {

			Fuzzy newFuzzy = new Fuzzy(source.length(),
					pattern.length());

			if (newFuzzy.containsOneOf(source, pattern)) {
				System.out.println("Fuzzy matching");
				
//				// Fuzzy Matching
//				System.out.println("Size pattern : " + pattern.length());
//				System.out.println("Size document : " + source.length());

				this.start = newFuzzy.getResultStart() - 1;
				this.end = newFuzzy.getResultEnd();
				this.result = newFuzzy.getResult();
				return true;
				

			} else {
				System.out.println("Not Found: [" + pattern + "] in [" + source + "]");
				this.start = -1;
				this.end = -1;
				this.result = -1;
				return false;
			}
		}
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

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

}
