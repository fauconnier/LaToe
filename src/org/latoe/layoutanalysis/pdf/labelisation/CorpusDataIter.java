package org.latoe.layoutanalysis.pdf.labelisation;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;

import org.latoe.layoutanalysis.pdf.*;
import org.latoe.layoutanalysis.pdf.pdfobject.Corpus_PDF;
import org.latoe.layoutanalysis.pdf.pdfobject.Document_PDF;
import org.melodi.learning.iitb.CRF.*;
import org.melodi.learning.iitb.MaxentClassifier.DataRecord;
import org.melodi.learning.iitb.Segment.TrainRecord;

/**
 * 
 * @author jfaucon
 * Chaque CorpusDataIter est un itérateur sur le corpus
 * Le corpus est constitué de 'Document'
 * 'Document' est consitué de segments (chunk)
 */

public class CorpusDataIter implements DataIter {
	List<Document_PDF> docList;
	int currPos = 0;

	
	 public CorpusDataIter(Corpus_PDF corpus) {
		 this.docList = corpus.getListdoc();
		 
	}
	

	public void startScan() {
		// Spécifie à quel document on commence dans le corpus
		currPos = 0;
	}

	public boolean hasNext() {
		// Spécifie si il y a un document suivant
		return (currPos < docList.size());
	}

	public DataSequence next() {
		// récupére le document suivant
		currPos++;
		return docList.get(currPos-1);
		
	}
	

}
