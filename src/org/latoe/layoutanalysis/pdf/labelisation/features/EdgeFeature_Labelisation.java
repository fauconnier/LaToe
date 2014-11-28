package org.latoe.layoutanalysis.pdf.labelisation.features;

import java.util.HashMap;

import org.latoe.layoutanalysis.pdf.labelisation.Service_CRF;
import org.melodi.learning.iitb.CRF.DataSequence;
import org.melodi.learning.iitb.Model.Edge;
import org.melodi.learning.iitb.Model.EdgeIterator;
import org.melodi.learning.iitb.Model.FeatureGenImpl;
import org.melodi.learning.iitb.Model.FeatureImpl;
import org.melodi.learning.iitb.Model.FeatureTypes;

public class EdgeFeature_Labelisation extends FeatureTypes {
	/**
		 * 
		 */
	private static final long serialVersionUID = -6098103393225258231L;
	transient protected EdgeIterator edgeIter = null;
	protected int edgeNum;
	transient boolean edgeIsOuter;
	Object labelNames[];
//	public HashMap<Integer, String> intMapLabel;
	float value;

	public EdgeFeature_Labelisation(FeatureGenImpl m, Object labels[],float value) {
		super(m);
		labelNames = labels;
//		this.intMapLabel=serviceCRF.intMapLabel;
		this.value=value;
		
	}

	public EdgeFeature_Labelisation(FeatureGenImpl m,float value) {
		this(m, null,value);
	}

	protected void setEdgeIter() {
		edgeIter = model.edgeIterator();
	}

	public boolean startScanFeaturesAt(DataSequence data, int prevPos, int pos) {
		if (prevPos < 0) {
			edgeNum = model.numEdges();
			return false;
		} else {
			edgeNum = 0;
			if (edgeIter == null) {
				setEdgeIter();
			}
			if (edgeIter != null)
				edgeIter.start();
			return hasNext();
		}
	}

	public boolean hasNext() {
		return (edgeIter != null) && (edgeIter.hasNext());
	}

	public boolean lastEdgeWasOuter() {
		return edgeIsOuter;
	}

	public void next(FeatureImpl f) {
		edgeIsOuter = edgeIter.nextIsOuter();
		Edge e = edgeIter.next();
		Object name = "";
		if (featureCollectMode()) {
			if (labelNames == null) {
				// TODO : quick fix
//				String nom_label = intMapLabel.get(model.label(e.start));
				String nom_label = "n";
				name = "Edge("
//						+ (edgeIsOuter ? ("" + model.label(e.start)+")")
						+ (edgeIsOuter ? ("" + nom_label+")")
								: ("I." + e.start));
			} else {
				name = labelNames[model.label(e.start)];
			}
		}
		if (edgeIsOuter) {
			setFeatureIdentifier(model.label(e.start) * model.numberOfLabels()
					+ model.label(e.end) + model.numEdges(), e.end, name, f);
		} else {
			setFeatureIdentifier(edgeNum, e.end, name, f);
		}
		f.ystart = e.start;
		f.yend = e.end;
		f.val = value;
		edgeNum++;
	}

	public boolean fixedTransitionFeatures() {
		return true;
		/*
		 * ((model.numStartStates()==model.numStates())&&
		 * (model.numEndStates()==model.numEndStates()));
		 */
	}
};
