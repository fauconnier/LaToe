package org.melodi.tools.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.melodi.reader.larat.internal.Unit;

public class Label_Instance {
	
	public String name = "";
	public int id_evaluation_service;
	
	ArrayList<Unit> true_positive_array;
	ArrayList<Unit> false_negative_array;
	ArrayList<Unit> false_positive_array;
	
	HashMap<String,ArrayList<Unit>> confusionSet_false_negative;
	HashMap<String,ArrayList<Unit>> confusionSet_false_positive;
	
	double fscore;
	double recall;
	double precision;
	
	
	public Label_Instance(){
		this.true_positive_array = new ArrayList<Unit>();
		this.false_negative_array = new ArrayList<Unit>();
		this.false_positive_array = new ArrayList<Unit>();
		this.confusionSet_false_negative = new HashMap<String,ArrayList<Unit>>();
		this.confusionSet_false_positive = new HashMap<String,ArrayList<Unit>>();
	}
	
	public Label_Instance(String name){
		super();
		this.name=name;
	}
	
	public int getId_evaluation_service() {
		return id_evaluation_service;
	}

	public void setId_evaluation_service(int id_evaluation_service) {
		this.id_evaluation_service = id_evaluation_service;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public double getFscore() {
		return fscore;
	}

	public void setFscore(double fscore) {
		this.fscore = fscore;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public void addTruePositive(Unit currUnit){
		this.true_positive_array.add(currUnit);
	}
	
	public void addFalseNegative(Unit currUnit){
		this.false_negative_array.add(currUnit);
	}
	
	public void addFalsePositive(Unit currUnit){
		this.false_positive_array.add(currUnit);
	}

	public ArrayList<Unit> getTruePositive(){
		return true_positive_array;
	}
	
	public ArrayList<Unit> getFalseNegative(){
		return false_negative_array;
	}
	
	public ArrayList<Unit> getFalsePositive(){
		return false_positive_array;
	}
	
	public ArrayList<Unit> getFalseNegativeFor(String label){
		return confusionSet_false_negative.get(label);
	}
	
	public ArrayList<Unit> getFalsePositiveFor(String label){
		return confusionSet_false_positive.get(label);
	}
	
	public void addConfusionFalseNegative(Unit currUnit, String label){
		// 1. vérifier si le label de currUnit existe dans
		// la hash
		if(confusionSet_false_negative.containsKey(label)){
			ArrayList<Unit> currArray = confusionSet_false_negative.get(label);
			currArray.add(currUnit);
		}
		else{
			ArrayList<Unit> currArray = new ArrayList<Unit>();
			currArray.add(currUnit);
			confusionSet_false_negative.put(label, currArray);
		}
	}
	
	public 	HashMap<String,ArrayList<Unit>> getConfusionFalseNegative(){
		return this.confusionSet_false_negative;
	}
	
	
	public void addConfusionFalsePositive(Unit currUnit){
		// 1. vérifier si le label de currUnit existe dans
		// la hash
		if(confusionSet_false_positive.containsKey(currUnit.getY())){
			ArrayList<Unit> currArray = confusionSet_false_positive.get(currUnit.getY());
			currArray.add(currUnit);
		}
		else{
			ArrayList<Unit> currArray = new ArrayList<Unit>();
			currArray.add(currUnit);
			confusionSet_false_positive.put(currUnit.getY(), currArray);
		}
	}
	
	
	public 	HashMap<String,ArrayList<Unit>> getConfusionFalsePositive(){
		return this.confusionSet_false_positive;
	}
	
	public void printDetails(){
		Label_Instance currLabel = this;
		System.out.println(currLabel.getName());
		System.out.println("Fscore: " + currLabel.getFscore());
		System.out.println("Recall: " + currLabel.getRecall());
		System.out.println("Precision: " + currLabel.getPrecision());
		System.out.println("TP: " + currLabel.getTruePositive().size());
		System.out.println("FN: " + currLabel.getFalseNegative().size());
		System.out.println("FP: " + currLabel.getFalsePositive().size());
		
		System.out.println("False Negative Confusion");
		HashMap<String,ArrayList<Unit>> confusionset_false_negative = currLabel.getConfusionFalseNegative();
		for(Entry<String,ArrayList<Unit>> entry : confusionset_false_negative.entrySet()) {
		    String conf_label = entry.getKey();
		    ArrayList<Unit> valeur = entry.getValue();
		    System.out.println("Confusion_label: "+conf_label+"-"+valeur.size());
		    // traitements
		}
		
		
		System.out.println("False Positive Confusion");
		HashMap<String,ArrayList<Unit>> confusionset_false_positive = currLabel.getConfusionFalsePositive();
		for(Entry<String,ArrayList<Unit>> entry : confusionset_false_positive.entrySet()) {
		    String conf_label = entry.getKey();
		    ArrayList<Unit> valeur = entry.getValue();
		    System.out.println("Confusion_label: "+conf_label+"-"+valeur.size());
		    // traitements
		}
	}

}
