/** AlphaNumericPreprocessor.java
 * 
 * @author Sunita Sarawagi
 * @version 1.3
 */

package org.melodi.learning.iitb.Segment;

/**
 * 
 * @author Sunita Sarawagi
 * 
 */

public class AlphaNumericPreprocessor extends Preprocessor {

	public static String DIGIT = new String("DIGIT");

	public int getCode() {
		return 1;
	}

	public static String preprocess(String s) {
		if (isNumber(s)) {
			return DIGIT;
		}

		return s;
	}

	//TODO: Unnecessary constructor? Empty and everything else is static (except getCode()).
	public AlphaNumericPreprocessor() {}

	/**
	 * Checks, if the String encodes a natural number (int).
	 * @param s The String to check.
	 * @return true, if the String encodes a natural number, false otherwise.
	 */
	public static boolean isNumber(String s) {
		try {
			Integer.valueOf(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * preprocess
	 * @param tokens
	 * @param numLabels
	 * @return
	 * Parcourt tous les tokens.
	 */
	public static TrainData preprocess(TrainData tokens, int numLabels) {
		//Parcours tous les exemples du Data Set 
		//Ici, c'est une tache de segmentation
		//Donc chaque séquence = suite de tokenS
		
		for (tokens.startScan(); tokens.hasMoreRecords();) {
			
			TrainRecord tr = tokens.nextRecord();
			
			for (int s = 0; s < tr.numSegments(); s++) {
				String[] words = tr.tokens(s);
				for (int j = 0; j < words.length; j++) {
					words[j] = preprocess(words[j]);
				}
			}
		}
		return tokens;
	}
};
