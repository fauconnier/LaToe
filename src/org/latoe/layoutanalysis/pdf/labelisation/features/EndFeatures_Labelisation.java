package org.latoe.layoutanalysis.pdf.labelisation.features;

import org.melodi.learning.iitb.CRF.DataSequence;
import org.melodi.learning.iitb.Model.FeatureGenImpl;
import org.melodi.learning.iitb.Model.FeatureImpl;
import org.melodi.learning.iitb.Model.FeatureTypes;

public class EndFeatures_Labelisation extends FeatureTypes {


	int stateId;
	int endStateNum;
	Object fname;

	public EndFeatures_Labelisation(FeatureGenImpl m) {
		super(m);
		fname = "End";
	}

	public EndFeatures_Labelisation(FeatureGenImpl m, Object fname) {
		super(m);
		this.fname = fname;
	}

	public boolean startScanFeaturesAt(DataSequence data, int prevPos, int pos) {
		if (pos < data.length() - 1) {
			stateId = -1;
			return false;
		} else {
			endStateNum = 0;
			stateId = model.endState(endStateNum);
			return true;
		}
	}

	public boolean hasNext() {
		return (stateId >= 0);
	}

	public void next(FeatureImpl f) {
		setFeatureIdentifier(stateId, stateId, fname, f);
		f.yend = stateId;
		f.ystart = -1;
		f.val = 1;
		endStateNum++;
		stateId = model.endState(endStateNum);
	}
};
