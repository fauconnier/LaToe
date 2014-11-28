package org.latoe.layoutanalysis.pdf.labelisation.features;

import org.melodi.learning.iitb.CRF.DataSequence;
import org.melodi.learning.iitb.CRF.SegmentDataSequence;
import org.melodi.learning.iitb.Model.FeatureGenImpl;
import org.melodi.learning.iitb.Model.FeatureImpl;
import org.melodi.learning.iitb.Model.FeatureTypes;

public class FeatureTypesEachLabel_Labelisation extends FeatureTypes {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6540153966191141827L;
	protected FeatureTypes single;
	int numStates;
	int stateId;
	FeatureImpl featureImpl;
	boolean optimize = false;

	public FeatureTypesEachLabel_Labelisation(FeatureGenImpl fgen,
			FeatureTypes single) {
		super(fgen);
		numStates = model.numStates();
		this.single = single;
		featureImpl = new FeatureImpl();
		thisTypeId = single.thisTypeId;
	}

	protected void nextFeature() {
		single.next(featureImpl);
	}

	boolean advance() {
		stateId++;
		if (stateId < numStates)
			return true;
		if (single.hasNext()) {
			nextFeature();
			stateId = 0;
		}
		return stateId < numStates;
	}

	public boolean startScanFeaturesAt(org.melodi.learning.iitb.CRF.DataSequence data, int prevPos,
			int pos) {
		stateId = numStates;
		single.startScanFeaturesAt(data, prevPos, pos);
		return advance();
	}

	public boolean startScanFeaturesAt(org.melodi.learning.iitb.CRF.DataSequence data, int pos) {
		stateId = numStates;
		single.startScanFeaturesAt(data, pos);
		return advance();
	}

	public boolean hasNext() {
		return (stateId < numStates);
	}

	public void next(org.melodi.learning.iitb.Model.FeatureImpl f) {
		f.copy(featureImpl);
		nextCopyDone(f);
	}

	protected void nextCopyDone(org.melodi.learning.iitb.Model.FeatureImpl f) {
		f.yend = stateId;
		single.setFeatureIdentifier(f.strId.id * numStates + stateId, stateId,
				f.strId.name, f);
		advance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iitb.Model.FeatureTypes#requiresTraining()
	 */
	public boolean requiresTraining() {
		return single.requiresTraining();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iitb.Model.FeatureTypes#train(iitb.CRF.DataSequence, int)
	 */
	public void train(DataSequence data, int pos) {
		single.train(data, pos);
	}

	public void train(SegmentDataSequence sequence, int segStart, int segEnd) {
		single.train(sequence, segStart, segEnd);
	}

	public boolean fixedTransitionFeatures() {
		return single.fixedTransitionFeatures();
	}

	public boolean needsCaching() {
		return single.needsCaching();
	}
};
