package org.latoe.layoutanalysis.pdf.labelisation.features;

import org.latoe.layoutanalysis.pdf.labelisation.Service_CRF;
import org.melodi.learning.iitb.CRF.DataSequence;
import org.melodi.learning.iitb.Model.FeatureGenImpl;
import org.melodi.learning.iitb.Model.FeatureImpl;
import org.melodi.learning.iitb.Model.FeatureTypes;

public class StartFeatures_Labelisation extends FeatureTypes {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4603152016253011064L;
	int stateId;
	int startStateNum;
	Object fname;

	public StartFeatures_Labelisation(FeatureGenImpl m) {
		super(m);
		fname = "Start";
	}

	public StartFeatures_Labelisation(FeatureGenImpl m, Object name) {
		super(m);
		fname = name;
	}

	public boolean startScanFeaturesAt(DataSequence data, int prevPos, int pos) {
		if (prevPos >= 0) {
			stateId = -1;
			return false;
		} else {
			startStateNum = 0;
			stateId = model.startState(startStateNum);
//			Document d = (Document) data;
//			Chunk test = (Chunk) d.x(pos);
//			System.out.println(test.toString());
			return true;
		}
	}

	public boolean hasNext() {
		return (stateId >= 0);
	}

	public void next(FeatureImpl f) {
		
		// TODO : warging quick fix
//		String mystring = serviceCRF.intMapLabel.get(stateId);
		String mystring = "";
		setFeatureIdentifier(stateId, stateId, fname+"("+mystring+")", f);
		f.yend = stateId;
		f.ystart = -1;
		f.val = 1;
		startStateNum++;
		stateId = model.startState(startStateNum);
	}
};

