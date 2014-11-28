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

public class Feature_PlusMargeHautMoy extends FeatureTypes {
	String fname;
	int stateId;
	int numStates;
	String nameFeatures;
	float value;

	public Feature_PlusMargeHautMoy(FeatureGenImpl m) {
		super(m);
		numStates = m.numStates();
		fname = "PlusMargeHaut";
		this.value=value;
	}

	
	@Override
	public boolean startScanFeaturesAt(DataSequence data, int prevPos, int pos) {
		Document_PDF d = (Document_PDF) data;
                
		Chunk_PDF prevChunk = (Chunk_PDF) d.x(prevPos);
		Chunk_PDF currChunk = (Chunk_PDF) d.x(pos);
		
                if(!currChunk.isFirstOnPage){
                    float margeHauteDocu = d.getMoyMargeHaut();
                    float margeHauteChunk = currChunk.y1 - prevChunk.y2;

                    // Renvoie true si la marge haute du chunk avec le précédent est inférieure
                    // à la marge haute mode du document. On ne prend pas en compte les chunks avec 
                    // une marge haute négative !(ex : objets spatialisés gauche/droite type tableau ou header multiples)
                    if (margeHauteChunk > (margeHauteDocu * (1 + FeatureGenerator_Labelisation.seuil))) {
                                stateId = 0;
                                return true;

                    } 
                }

                stateId = numStates;
                return false;
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
		f.val = value;
		stateId++;
	}

}

