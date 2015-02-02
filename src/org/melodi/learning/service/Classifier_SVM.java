package org.melodi.learning.service;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.melodi.learning.rjava.Rengine_Service;
import org.melodi.tools.dataset.DataSet_Corpora;
import org.melodi.tools.evaluation.Evaluation_Service;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.Rengine;

import au.com.bytecode.opencsv.CSVReader;
import opennlp.model.AbstractModel;


/**
 * A refaire! 
 * Ne pas utiliser R en production.
 * Facile pour expé, mais trop lent et sûrement non compatible avec Windows.
 * @author jfaucon
 *
 */
public class Classifier_SVM {
	
	String path_object_R = "";
	Rengine re;
	
	
	public Classifier_SVM(Rengine_Service re_s){
		path_object_R = "./tmp/model_svm.RData";
		this.re = re_s.getRengine();
	}
	
	public Classifier_SVM train(FeatureGenerator_Service featureGen) throws FileNotFoundException, UnsupportedEncodingException{
		HashMap<String,String> params = new HashMap<String,String>();
		return train(featureGen,params);
	}
	
	public Classifier_SVM train(FeatureGenerator_Service featureGen, HashMap<String,String> params) throws FileNotFoundException, UnsupportedEncodingException{
		
		featureGen.writeCSV("./tmp/dataset_train.csv");
		
		// Gérer les params
		String parameters = this.generatesParams(params);
		
        //Data
        re.eval("library('e1071')");
        re.eval("x <- read.csv(\"./tmp/dataset_train.csv\")");
        re.eval("y <- as.factor(x$Y)");
        re.eval("x <- subset(x, select=-Y)");
        
        //Train
        re.eval("cat('Training SVM','\n')");
        System.out.println("Parameters:"+parameters);
        re.eval("model_svm <- svm(x,y"+parameters+")");
        re.eval("save(model_svm,file='"+path_object_R+"')");
        
		return this;
	}
	
	public Evaluation_Service predict(DataSet_Corpora corpora, FeatureGenerator_Service featureGen) throws IOException{
		
		System.out.println("Predict SVM ...");
		Evaluation_Service evaluator = new Evaluation_Service();
		evaluator.setName("evaluator");
		evaluator.setCorpora(corpora);
		
		featureGen.writeCSV("./tmp/dataset_test.csv");
		
		re.eval("x_test <- read.csv(\"./tmp/dataset_test.csv\")");
		re.eval("x_test <- subset(x_test, select=-Y)");
		re.eval("load(file='"+path_object_R+"')"); // model_svm
		re.eval("predict_y <- predict(model_svm,x_test)");
		re.eval("write.csv(predict_y,file=\"./tmp/predicted_y.csv\")");
		
		
		
	    CSVReader reader = new CSVReader(new FileReader("./tmp/predicted_y.csv"));
	    String [] nextLine;
	    boolean first_line=true;
	    int index = 0;
	    while ((nextLine = reader.readNext()) != null) {
	        // nextLine[] is an array of values from the line
	    	if(!first_line){
	    		first_line = false;
	    	}
	    	else{
	    		
	    		if(!nextLine[0].equals("")){// lecture ligne vide
	    		String predict_y = nextLine[1];
	    		int index_data = Integer.parseInt(nextLine[0]);
	    		featureGen.get(index_data-1).getUnit().setPredict_y(predict_y);
	    		}
	    	}
	    }
	    
		
//		for(PairUnit_Features currPair : featureGen){
//			this.predict(currPair);
//		}
		return evaluator;
		
	}
	
	
	public String generatesParams(HashMap<String,String> params){
		Set cles = params.keySet();
		Iterator it = cles.iterator();
		String R_format = "";
		while (it.hasNext()){
		   Object cle = it.next(); // tu peux typer plus finement ici
		   Object valeur = params.get(cle); // tu peux typer plus finement ici
		   if(!valeur.equals("NULL")){
			   R_format += "," + cle + "=" + valeur+"";
		   }
		}
		return R_format;
	}
	
	
	
	
	public void predict(PairUnit_Features currPair){
		
		String predicates = "";
		for(String currString : currPair.getFeatures()){
			predicates += currString + ",";
		}
		
		System.out.println(predicates);
		
		String[] contexts = predicates.split(" ");
		
		
		
//		double[] ocs = model.eval(contexts);
//		currPair.getUnit().setPredict_y(model.getBestOutcome(ocs));
	}
	
	
	
	
}
