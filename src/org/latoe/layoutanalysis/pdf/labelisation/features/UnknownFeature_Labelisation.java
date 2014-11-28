package org.latoe.layoutanalysis.pdf.labelisation.features;

import org.melodi.learning.iitb.CRF.DataSequence;
import org.melodi.learning.iitb.Model.FeatureGenImpl;
import org.melodi.learning.iitb.Model.FeatureImpl;
import org.melodi.learning.iitb.Model.FeatureTypes;
import org.melodi.learning.iitb.Model.WordFeatures;
import org.melodi.learning.iitb.Model.WordsInTrain;

public class UnknownFeature_Labelisation extends FeatureTypes {
	private static final long serialVersionUID = 6122L;
	int stateId;
	WordsInTrain dict;
	int numStates;
	String nameFeatures;
	
	/**
	 * Feature qui tente de capturer les "mots" du corpus 
	 */

	public UnknownFeature_Labelisation(FeatureGenImpl m, WordsInTrain d) {
		this(m, d, true);
	}

	public UnknownFeature_Labelisation(FeatureGenImpl m, WordsInTrain d, boolean assignStateIds) {
		super(m);
		nameFeatures="Unknown";
		dict = d;
		if (assignStateIds)
			numStates = m.numStates();
		else
			numStates = 1;
	}

	public boolean startScanFeaturesAt(DataSequence data, int prevPos, int pos) {
		if (dict.count(data.x(pos)) > WordFeatures.RARE_THRESHOLD + 1) {
			stateId = numStates;
			return false;
		} else {
//			System.out.println("RARE_THRESHOLD:"+WordFeatures.RARE_THRESHOLD);
			stateId = 0;
			return true;
		}
	}

	public boolean hasNext() {
		return (stateId < numStates);
	}

	public void next(FeatureImpl f) {
		setFeatureIdentifier(stateId, stateId, "Unknown", f);
		f.yend = stateId;
		f.ystart = -1;
		f.val = 1;
		stateId++;
	}

	public int maxFeatureId() {
		return numStates;
	}
};
