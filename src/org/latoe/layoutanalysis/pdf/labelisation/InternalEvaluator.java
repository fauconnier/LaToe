package org.latoe.layoutanalysis.pdf.labelisation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.latoe.layoutanalysis.pdf.labelisation.features.FeatureGenerator_Labelisation;
import org.latoe.layoutanalysis.pdf.pdfobject.Chunk_PDF;
import org.latoe.layoutanalysis.pdf.pdfobject.Corpus_PDF;
import org.latoe.layoutanalysis.pdf.pdfobject.Document_PDF;
import org.latoe.layoutanalysis.pdf.pdfobject.Page_PDF;
import org.melodi.learning.iitb.CRF.CRF;
import org.melodi.learning.iitb.CRF.Evaluator;
import org.melodi.learning.iitb.Utils.Options;

public class InternalEvaluator implements Evaluator {
	int index;
	FeatureGenerator_Labelisation fgen;
	Options options;
	Corpus_PDF corpus_test;
	CRF crfModel;
	int nlabels;
	ArrayList<Integer> trueLabels;
	HashMap<Integer,Double> historyAccuracy;
	ArrayList<Integer> historyIteration;

	public InternalEvaluator(FeatureGenerator_Labelisation fgen,
			Options options, Corpus_PDF corpus_test) {
		this.index = 0;
		this.fgen = fgen;
		this.nlabels = fgen.numStates();
		this.options = options;
		this.corpus_test = corpus_test;
		this.trueLabels = new ArrayList<Integer>();
		
		CorpusDataIter corpus_test_iter = new CorpusDataIter(corpus_test);
		while (corpus_test_iter.hasNext()) {
			Document_PDF d = (Document_PDF) corpus_test_iter.next();

			for (Page_PDF currP : d.pages) {
				for (Chunk_PDF currChunk : currP.groupes) {
					trueLabels.add(currChunk.type_int);
				}
			}
		}
		this.historyAccuracy = new HashMap<Integer,Double>();
		this.historyIteration = new ArrayList<Integer>();

	}

	public boolean evaluate(double[] lambda, int iteration) {
		// TODO Auto-generated method stub

		CRF crfModel = new CRF(fgen.numStates(), fgen, options);
		crfModel.setInitTrainWeights(lambda);
		this.crfModel = crfModel;

		double accuracy = 0;
		try {
			accuracy = this.test();
			System.out.println("Accuracy : " + accuracy);
			historyAccuracy.put(iteration,accuracy);
			historyIteration.add(iteration);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		for(double currD : lambda){
//			System.out.print(currD + " ");
//		}
//		System.out.println("");

		return true;
	}

	@Override
	public boolean evaluate() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * test()
	 * 
	 * @param corpus_test
	 * @throws IOException
	 *             Test du modèle sur un corpus de test. Retourne un double qui
	 *             est l'accuracy du test.
	 */
	public double test() throws IOException {

		/**
		 * Etapes dans la prédiction test d'un modèle 1. addIntLabels : on
		 * transforme les labels en int selon l'association définie lors de
		 * l'entraînement 2. On définit un nouvel DataIter sur le corpus_test 3.
		 * CRF.apply() : le CRF prédit les labels des 'segments' (chunk) au sein
		 * des documents de corpus_test 4. On évalue la performance.
		 */

		CorpusDataIter corpus_test_iter = new CorpusDataIter(corpus_test);
		
		/*
		 * 3. prédiction : CRF.apply(Document)
		 */
//		ArrayList<Integer> trueLabels = new ArrayList<Integer>();
		ArrayList<Integer> predictLabels = new ArrayList<Integer>();
		while (corpus_test_iter.hasNext()) {
			Document_PDF d = (Document_PDF) corpus_test_iter.next();

			double double_score = crfModel.apply(d);
			fgen.mapStatesToLabels(d);

			for (Page_PDF currP : d.pages) {
				for (Chunk_PDF currChunk : currP.groupes) {
					predictLabels.add(currChunk.type_int);
				}
			}
		}
		// Vérification naïve :
		if (trueLabels.size() != predictLabels.size()) {
			System.err.println("Erreur dans la prédiction");
		}

		int index = 0;
		int total_positive = 0;
		for (int currLabelTrue : trueLabels) {
			int currLabelPredict = predictLabels.get(index);
			
			if(currLabelPredict == currLabelTrue){
				total_positive++;
			}

			index++;
		}

//		System.out.println("totalPos="+total_positive);
//		System.out.println("total="+trueLabels.size());
		// retourne F1 total, càd accuracy
//		return (((total_positive*100000)/(trueLabels.size()*100000))*100);
		return ((double) (total_positive * 100000 / trueLabels.size())) / 1000;

	}
	
	public void writeHistory(String fileName){
		System.out.println("Ecriture de la learning curve");
        PrintWriter out;
		try {
			out = new PrintWriter(new FileOutputStream(fileName));
			out.println("\"iteration\",\"accuracy\"");
			
			for(Integer myIteration : historyIteration){
				Double accuracy = historyAccuracy.get(myIteration);
				out.println("\""+ myIteration + "\",\""+accuracy+"\"");
			}
			
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean evaluate(double[] lambda) {
		// TODO Auto-generated method stub
		return false;
	}

}
