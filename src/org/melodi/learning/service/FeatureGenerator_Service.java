package org.melodi.learning.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.melodi.reader.larat.internal.Unit;
import org.melodi.tools.dataset.DataSet_Corpora;
import org.melodi.tools.dataset.DataSet_Service;

/**
 * Classe pour manipulation des traits
 * A refaire!
 * @author jfaucon
 *
 */
public class FeatureGenerator_Service extends ArrayList<PairUnit_Features>{
	
	
	
	public FeatureGenerator_Service(){
		
	}
	
	public void writeSVMlight(DataSet_Corpora corpora, String path) throws FileNotFoundException, UnsupportedEncodingException{
		
//		<line> .=. <target> <feature>:<value> ... <feature>:<value> # <info>
//		example : -1 1:0.43 3:0.12 9284:0.2 # abcdef
		
		 PrintWriter writer = new PrintWriter(path, "UTF-8");
		 DataSet_Service dataset_service = new DataSet_Service();
		 HashMap<Integer, String> mapInt_Label = dataset_service.getMapInt_to_Label(corpora);
		 HashMap<String, Integer> mapLabel_Int = dataset_service.getMapLabel_to_Int(corpora);
		 
		 for(PairUnit_Features currPair : this){
			 Vector <String> vector = currPair.getFeatures();
			 Unit currUnit = currPair.getUnit();
			 
			 writer.print(mapLabel_Int.get(currUnit.getY()) + " ");
			 
			 for(String features : vector){
				 writer.print(features + ":1 ");
			 }
			 writer.println(" ");
		 }
		 writer.close();
		
	}
	
	public void writeMaxEnt(String path) throws FileNotFoundException, UnsupportedEncodingException{
		

		 PrintWriter writer = new PrintWriter(path, "UTF-8");
		 
		 for(PairUnit_Features currPair : this){
			 Vector <String> vector = currPair.getFeatures();
			 Unit currUnit = currPair.getUnit();
//			 System.out.println(currUnit.getY());
			 for(String features : vector){
				 writer.print(features + " ");
			 }
			 writer.println(currUnit.getY());
		 }
		 writer.close();
		
	}
	
	public String getFeatureName(String feature){
	    feature = feature.replaceAll("[^A-Za-z]","");
        return feature;
	}
	
	public String getFeatureValue(String feature){
		if(!feature.contains("html")){
		  feature = feature.replaceAll("[A-Za-z]","");
		}
		
		  return feature;
	}
	
	
	public void writeCSV(String path) throws FileNotFoundException, UnsupportedEncodingException{
		
		 PrintWriter writer = new PrintWriter(path, "UTF-8");
		 
		 // first line
		 int index = 0;
		 PairUnit_Features firstLine =  this.get(0);
		 Vector <String> vector_firstLine = firstLine.getFeatures();
		 for(String feature : vector_firstLine){
			 writer.print(this.getFeatureName(feature)+",");
		 }
		 writer.println("Y");
		 
		 
		 for(PairUnit_Features currPair : this){
			 Vector <String> vector = currPair.getFeatures();
			 Unit currUnit = currPair.getUnit();
			 
			 for(String features : vector){
				 writer.print(this.getFeatureValue(features) + ",");
			 }
			 writer.println(currUnit.getY());
		 }
		 
		 writer.close();
	}
	
	
	
	public void analyseFeatures() throws IOException, InterruptedException{
		
		
		Runtime.getRuntime().exec("rm plot.png");
		Thread.sleep(500);
		Runtime.getRuntime().exec("rm features.csv");
		Thread.sleep(500);
		this.writeCSV("features.csv");
		
		PrintWriter writer = new PrintWriter("rscript.R", "UTF-8");
		
		String code = "library(corrgram);\n"
				+ "dm <- read.csv(\"features.csv\");\n"
				+ "\n"
				+ "vec_y <- as.integer(dm$Y)\n"
				+ "dm$Y <- vec_y\n"
				+ "cormatrix<- cor(dm, method='pearson')\n"
				+ "png('plot.png',width = 1500, height = 800, pointsize = 20)\n"
				+ "corrgram(cormatrix,type='cor');\n"
				+ "dev.off()";
		writer.print(code);
		writer.close();
		
		
		Runtime.getRuntime().exec("Rscript rscript.R"); 
		Thread.sleep(3000);
		Runtime.getRuntime().exec("gpicview plot.png"); 
		
		
		
	}
	
	
	
	

}
