package org.latoe.layoutanalysis.pdf.labelisation.features;

import org.melodi.learning.iitb.CRF.DataSequence;
import org.melodi.learning.iitb.Model.FeatureGenImpl;
import org.melodi.learning.iitb.Model.FeatureImpl;
import org.melodi.learning.iitb.Model.FeatureTypes;
import org.melodi.learning.iitb.Model.WordsInTrain;

public class WordFeatures_Labelisation extends FeatureTypes {
	/**
	 * Check si le token courant est présent dans le dictionnaire.
	 */
	private static final long serialVersionUID = -202366673127245027L;
	protected int stateId;
	int statePos;
	Object token;
	int tokenId;
	protected WordsInTrain dict;
	int _numWordStatePairs;
	public static int RARE_THRESHOLD = 0;
	protected int frequency_cutOff;
	boolean assignStateIds = true;
	int numStates;

	/**
	 * Constructeurs
	 */
	public WordFeatures_Labelisation(FeatureGenImpl m, WordsInTrain d) {
		super(m);
		dict = d;
		frequency_cutOff = RARE_THRESHOLD;
		numStates = m.numStates();
	}

	public WordFeatures_Labelisation(FeatureGenImpl m, WordsInTrain d,
			int freqCuttOff) {
		this(m, d);
		if (freqCuttOff >= 0){
			frequency_cutOff = freqCuttOff;
		}
	}

	public WordFeatures_Labelisation(FeatureGenImpl m, WordsInTrain d,
			int freqCuttOff, boolean assignStateIds) {
		this(m, d);
		if (freqCuttOff >= 0)
			frequency_cutOff = freqCuttOff;
		this.assignStateIds = assignStateIds;
		if (assignStateIds == false){
			numStates = 1;
		}
	}

	public boolean startScanFeaturesAt(DataSequence data, int prevPos, int pos) {
		stateId = -1;
		
		// Regarde si la fréquence du token est supérieure
		// à une certaine fréquence
		if (dict.count(data.x(pos)) > frequency_cutOff) {
			
			// Récupère l'ID du token
			tokenId = dict.getIndex(data.x(pos));
			// Récupère le token
			token = data.x(pos);
			
			// Toujours ture
			if (assignStateIds) {
				// On met l'état de position à -1
				statePos = -1;
				nextStateId();
			} else {
				stateId = 0;
			}
			return true;
		}// Si ce token n'est pas supérieur à cette fréquence
		else{
			return false;
		}
	}
	
	/**
	 * nextStateId()
	 */
	private void nextStateId() {
		stateId = dict.nextStateWithWord(token, stateId);
		statePos++;
	}

	public boolean hasNext() {
		return (stateId != -1);
	}

	public void next(FeatureImpl f) {
		if (assignStateIds) {
			if (featureCollectMode()){
				setFeatureIdentifier(tokenId * numStates + stateId, stateId,
						name() + "(" + dict.getKey(token) + ")", f);
			}
			else{
				setFeatureIdentifier(tokenId * numStates + stateId, stateId,
						null, f);
			}
			f.yend = stateId;
			nextStateId();
			
		} else {
			f.yend = 0;
			if (featureCollectMode()){
				f.strId.name = name() + dict.getKey(token);
			}
			f.strId.id = tokenId;
			stateId = -1;
		}
		f.ystart = -1;
		f.val = 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see iitb.Model.FeatureTypes#maxFeatureId()
	 */
	public int maxFeatureId() {
		return dict.dictionaryLength() * numStates;
	}

	public String name() {
		return "Word";
	}
};
