package org.latoe.layoutanalysis.pdf.labelisation.features;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.latoe.layoutanalysis.pdf.labelisation.ValueComparatorStringInt;
import org.latoe.layoutanalysis.pdf.pdfobject.Chunk_PDF;
import org.latoe.layoutanalysis.pdf.pdfobject.Document_PDF;
import org.latoe.layoutanalysis.pdf.pdfobject.Word_PDF;
import org.melodi.learning.iitb.CRF.DataSequence;
import org.melodi.learning.iitb.Model.FeatureGenImpl;
import org.melodi.learning.iitb.Model.FeatureImpl;
import org.melodi.learning.iitb.Model.FeatureTypes;

public class Feature_ContientNumerotationFirstWord extends FeatureTypes {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String fname;

	int stateId;
	int numStates;
	String nameFeatures;

	public Feature_ContientNumerotationFirstWord(FeatureGenImpl m) {
		super(m);
		numStates = m.numStates();
		fname = "CommenceParNumero";
	}

	@Override
	public boolean startScanFeaturesAt(DataSequence data, int prevPos, int pos) {
		// System.err.print("startScanFeaturesAt()   ");
		Document_PDF d = (Document_PDF) data;
		Chunk_PDF currChunk = (Chunk_PDF) d.x(pos);


		if (contientNumerotationFirstWord(currChunk)) {

			stateId = 0;
			return true;
		

		} else {
			stateId = numStates;
			return false;
		}
	}
	
	static boolean contientNumerotationFirstWord(Chunk_PDF currChunk){
		
		List<Word_PDF> wordList = currChunk.mots;
		String toMatch = wordList.get(0).mot;
		
                // matches letters and digits followed by . or - or a space, for any repetition
                // ex : works for 1. 1) 1 1.a a.1 A.1.b.2...
                //return toMatch.matches("(\\w[\\.\\-\\s\\)])+.*");
                
                //nouvelle version : prend en compte les chiffres romains
		return toMatch.toUpperCase().matches("((\\w|((XC|XL|L?X{0,3})(IX|IV|V?I{0,3})))[\\,\\.\\-\\s\\)])+.*");
	}

	
	

	public void processDoc(Document_PDF d) {

	}

	@Override
	public boolean hasNext() {
		return (stateId < numStates);
	}

	@Override
	public void next(FeatureImpl f) {
		// On créé un nouveau feature "vide".
		// Ce nouveau feature vide va être créé
		// pour chacun des labels
		// et chacun avec une valeur de 1.0 au début
		setFeatureIdentifier(stateId, stateId, fname, f);

		f.yend = stateId;
		f.ystart = -1;
		f.val = 1;
		stateId++;
	}

}



