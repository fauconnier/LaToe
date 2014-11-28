package org.melodi.learning.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.melodi.tools.dataset.DataSet_Corpora;
import org.melodi.tools.evaluation.Evaluation_Service;

import opennlp.maxent.BasicEventStream;
import opennlp.maxent.GIS;
import opennlp.maxent.PlainTextByLineDataStream;
import opennlp.model.AbstractModel;
import opennlp.model.EventStream;

public class Classifier_MaxEnt {

	AbstractModel model;
	public static double SMOOTHING_OBSERVATION = 0.1;

	public Classifier_MaxEnt() {
	}
	
	
	
	public AbstractModel train(FeatureGenerator_Service featureGen, int iteration, int cutoff, boolean USE_SMOOTHING, double value_smooth) throws IOException{
		this.SMOOTHING_OBSERVATION = value_smooth;
		return this.train(featureGen,iteration,cutoff,USE_SMOOTHING);
	}
	
	
	public AbstractModel train(FeatureGenerator_Service featureGen, int iteration, int cutoff) throws IOException{
		return this.train(featureGen,iteration,cutoff,false);
	}
	
	
	public AbstractModel train(FeatureGenerator_Service featureGen, int iteration) throws IOException{
		return this.train(featureGen,iteration,0,false);
	}
	
	
	
	public AbstractModel train(FeatureGenerator_Service featureGen) throws IOException{
		return this.train(featureGen,100,0,false);
	}
	
	public AbstractModel train(FeatureGenerator_Service featureGen, int iteration, int  cutoff, boolean USE_SMOOTHING) throws IOException {

		// TODO : c'est crade de passer par un fichier texte.
		featureGen.writeMaxEnt("dataset_train.txt");
		FileReader datafr = new FileReader(new File("dataset_train.txt"));
		
		EventStream es = new BasicEventStream(new PlainTextByLineDataStream(
				datafr));
		GIS.SMOOTHING_OBSERVATION = SMOOTHING_OBSERVATION;
		model = GIS.trainModel(es, iteration, cutoff, USE_SMOOTHING, true );
		
		// Data Structures
		// datastructure[0] = model parameters
		// datastructure[1] = java.util.Map (mapping model predicate to unique integers)
		// datastructure[2] = java.lang.String[] names of outcomes
		// datastructure[3] = java.lang.integer : value of models correction constante
		// datastructure[4] = java.lang.Double : value of models correction paramter

		return model;
	}
	

	public Evaluation_Service predict(AbstractModel model, DataSet_Corpora corpora, FeatureGenerator_Service featureGen) {
		Evaluation_Service evaluator = new Evaluation_Service();
		evaluator.setName("evaluator");
		evaluator.setCorpora(corpora);
		for(PairUnit_Features currPair : featureGen){
			predict(model, currPair);
		}
		return evaluator;
	}

	public void predict(AbstractModel model, PairUnit_Features currPair){
		
		String predicates = "";
		for(String currString : currPair.getFeatures()){
			predicates += currString + " ";
		}
		
		String[] contexts = predicates.split(" ");
		double[] ocs = model.eval(contexts);
		currPair.getUnit().setPredict_y(model.getBestOutcome(ocs));
//		System.out.println("For context: " + predicates+ "\n" + model.getAllOutcomes(ocs) + "\n");
	}
}
