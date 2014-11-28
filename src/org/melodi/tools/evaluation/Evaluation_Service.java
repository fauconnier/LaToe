package org.melodi.tools.evaluation;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.melodi.reader.larat.internal.Unit;
import org.melodi.tools.dataset.DataSet_Corpora;
import org.melodi.tools.dataset.DataSet_Service;

public class Evaluation_Service {

	public DataSet_Corpora corpora;
	public String name;
	public HashMap<String, Integer> mapLabel_to_Int;
	public HashMap<Integer, String> mapInt_to_Label;

	public Evaluation_Service() {
		this.name = "evaluator";
	}

	public Evaluation_Service(String name) {
		this.name = name;
	}

	public DataSet_Corpora getCorpora() {
		return corpora;
	}

	public void setCorpora(DataSet_Corpora corpora) {
		// Unit : getY(), getPreviousY(), getPredictY();
		this.corpora = corpora;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfusionMatrix() {

		return "";
	}

	public int getTotalObs() {
		return corpora.size();
	}
	
	public ArrayList<Unit> getAllTruePositive(){
		ArrayList<Unit> all_tp = new ArrayList<Unit>();
		
		for (Unit currUnit : corpora) {
			if (currUnit.getY().equals(currUnit.getPredict_y())) {
				all_tp.add(currUnit);
			}
		}
		return all_tp;
	}
	
	public int getAccuracyTp() {
		int total = corpora.size();
		int tp = 0;

		for (Unit currUnit : corpora) {
			if (currUnit.getY().equals(currUnit.getPredict_y())) {
				tp++;
			}
		}
		return tp;
	}

	public Double getAccuracy() {

		int total = corpora.size();
		int tp = 0;

		for (Unit currUnit : corpora) {
			if (currUnit.getY().equals(currUnit.getPredict_y())) {
				tp++;
			}
		}
		double accuracy = (double) ((tp * 10000) / (total)) / 100;
		return accuracy;
	}
	
	public String binaryTP(){
		
		ArrayList<Integer> binaryTP = new ArrayList<Integer>();
		ArrayList<String> identifiant = new ArrayList<String>();
		
		/*
		 * 1. DataSet and Mapping
		 */
		DataSet_Service dataset_service = new DataSet_Service();
		int nlabels = dataset_service.getNumberLabel(corpora);
		HashMap<String, Integer> mapLabel_to_Int = dataset_service
				.getMapLabel_to_Int(corpora);
		HashMap<Integer, String> mapInt_to_Label = dataset_service
				.getMapInt_to_Label(corpora);

		/*
		 * 2. Label creation
		 */
		String fscore_string = "";
		Label_Service label_service = new Label_Service();
		int index_id = 0;
		for (int i = 0; i < nlabels; i++) {
			Label_Instance currLabel = new Label_Instance();
			currLabel.setId_evaluation_service(index_id);
			currLabel.setName(mapInt_to_Label.get(index_id));
			label_service.add(currLabel);
			index_id++;
		}

		/*
		 * 3. Var. TP and others
		 */
		int truePositive[] = new int[nlabels + 1];
		int total_TruePositive_FalsePositive[] = new int[nlabels + 1];
		int total_TruePositive_False_Negative[] = new int[nlabels + 1];
		int confuseMatrix[][] = new int[nlabels][nlabels];

		/*
		 * 4. Count
		 */
		for (Unit currUnit : corpora) {
			identifiant.add(currUnit.getDocument() + ":" + currUnit.getId());
			
			String currTrueLabel = currUnit.getY();
			String currPredictLabel = currUnit.getPredict_y();

			// TruePositive
			if (currTrueLabel.equals(currPredictLabel)) {
				truePositive[mapLabel_to_Int.get(currTrueLabel)]++;
				truePositive[nlabels]++;

				// Ajout dans ArrayList Label
				Label_Instance currLabel = label_service.get(mapLabel_to_Int
						.get(currTrueLabel));
				currLabel.addTruePositive(currUnit);
				
				//
				binaryTP.add(1);

			}
			// FalsePositive
			// FalseNegative
			else {
				binaryTP.add(0);
				for (Label_Instance currLabel : label_service) {

					// False Negative
					if (currLabel.getName().equals(currTrueLabel)
							&& !currLabel.getName().equals(currPredictLabel)) {
						currLabel.addFalseNegative(currUnit);
						
						currLabel.addConfusionFalseNegative(currUnit,currPredictLabel);
					}

					// False Positive
					if (!currLabel.getName().equals(currTrueLabel)
							&& currLabel.getName().equals(currPredictLabel)) {
						currLabel.addFalsePositive(currUnit);
						
						currLabel.addConfusionFalsePositive(currUnit);
					}

				}
			}
		}
		
		// Return results
		String results = "";
		results += "\"id\", \"tp\"\n";
		int count = 0;
		int index = 0;
		for(Integer tpornot : binaryTP){
			results += "\"" + identifiant.get(index) + "\"," + tpornot + "\n";
			index++;
			if(tpornot == 1){
				count++;
			}
		}
		if(count != this.getAccuracyTp()){
			System.err.println("Erreur ic");
			System.exit(0);
		}
		
		return results;
	}
	
	public void writeForWilcoxonTest(String path) throws IOException{
		FileWriter wilcoxonTest = new FileWriter(path);
		wilcoxonTest.append(this.binaryTP());
		wilcoxonTest.flush();
		wilcoxonTest.close();
	}

	public void writeResults(String path) throws IOException {
		FileWriter fscoreWriter = new FileWriter(path);
		fscoreWriter.append(this.getFScore());
		fscoreWriter.flush();
		fscoreWriter.close();

	}
	
	public String getFScore() throws IOException {

		/*
		 * 1. DataSet and Mapping
		 */
		DataSet_Service dataset_service = new DataSet_Service();
		int nlabels = dataset_service.getNumberLabel(corpora);
		HashMap<String, Integer> mapLabel_to_Int = dataset_service
				.getMapLabel_to_Int(corpora);
		HashMap<Integer, String> mapInt_to_Label = dataset_service
				.getMapInt_to_Label(corpora);

		/*
		 * 2. Label creation
		 */
		String fscore_string = "";
		Label_Service label_service = new Label_Service();
		int index_id = 0;
		for (int i = 0; i < nlabels; i++) {
			Label_Instance currLabel = new Label_Instance();
			currLabel.setId_evaluation_service(index_id);
			currLabel.setName(mapInt_to_Label.get(index_id));
			label_service.add(currLabel);
			index_id++;
		}

		/*
		 * 3. Var. TP and others
		 */
		int truePositive[] = new int[nlabels + 1];
		int total_TruePositive_FalsePositive[] = new int[nlabels + 1];
		int total_TruePositive_False_Negative[] = new int[nlabels + 1];
		int confuseMatrix[][] = new int[nlabels][nlabels];

		/*
		 * 4. Count
		 */
		for (Unit currUnit : corpora) {
			String currTrueLabel = currUnit.getY();
			String currPredictLabel = currUnit.getPredict_y();

			// TruePositive
			if (currTrueLabel.equals(currPredictLabel)) {
				truePositive[mapLabel_to_Int.get(currTrueLabel)]++;
				truePositive[nlabels]++;

				// Ajout dans ArrayList Label
				Label_Instance currLabel = label_service.get(mapLabel_to_Int
						.get(currTrueLabel));
				currLabel.addTruePositive(currUnit);

			}
			// FalsePositive
			// FalseNegative
			else {
				for (Label_Instance currLabel : label_service) {

					// False Negative
					if (currLabel.getName().equals(currTrueLabel)
							&& !currLabel.getName().equals(currPredictLabel)) {
						currLabel.addFalseNegative(currUnit);
						
						currLabel.addConfusionFalseNegative(currUnit,currPredictLabel);
					}

					// False Positive
					if (!currLabel.getName().equals(currTrueLabel)
							&& currLabel.getName().equals(currPredictLabel)) {
						currLabel.addFalsePositive(currUnit);
						
						currLabel.addConfusionFalsePositive(currUnit);
					}

				}
			}

			// Total_truePositive_falsePositive (precision)
			total_TruePositive_FalsePositive[mapLabel_to_Int
					.get(currPredictLabel)]++;
			total_TruePositive_FalsePositive[nlabels]++;

			// Total_truePositive_falseNegative (rappel)
			total_TruePositive_False_Negative[mapLabel_to_Int
					.get(currTrueLabel)]++;
			total_TruePositive_False_Negative[nlabels]++;

			confuseMatrix[mapLabel_to_Int.get(currTrueLabel)][mapLabel_to_Int
					.get(currPredictLabel)]++;
		}

		/*
		 * 5. Confuse Matrix
		 */
		boolean confuseSet[] = new boolean[nlabels + 1];
		for (int i = 0; i < confuseSet.length; i++) {
			confuseSet[i] = true;
		}
		if (confuseSet != null) {
			// matconf_string += ("T\\P,");
			for (int i = 0; i < nlabels; i++) {
				if (confuseSet[i]) {
					// matconf_string += mapInt_to_Label.get(i) + ",";
				}
			}
			// matconf_string += ('\n');

			// Lignes suivantes
			for (int i = 0; i < nlabels; i++) {
				if (confuseSet[i]) {
					// Première colonne
					// matconf_string += mapInt_to_Label.get(i) + ",";

					// Colonnes suivantes
					for (int j = 0; j < nlabels; j++) {
						if (confuseSet[j]) {
							// matconf_string += Integer
							// .toString(confuseMatrix[i][j]) + ",";
						}
					}
					// matconf_string += '\n';
				}
			}
		}

		/*
		 * 6. By Classes
		 */

		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat f = new DecimalFormat("##.000", otherSymbols);

		fscore_string += name + ",";
		for (int i = 0; i < nlabels; i++) {
			fscore_string += mapInt_to_Label.get(i) + ",";
		}
		fscore_string += "Total,TP,FP,FN,Precision,Recall,F1";
		fscore_string += '\n';

		double prec, recall;
		for (int i = 0; i < nlabels; i++) {

			Label_Instance currLabel = label_service.get(i);

			prec = (total_TruePositive_FalsePositive[i] == 0) ? 0
					: ((double) (truePositive[i] * 100000 / total_TruePositive_FalsePositive[i])) / 1000;
			recall = (total_TruePositive_False_Negative[i] == 0) ? 0
					: ((double) (truePositive[i] * 100000 / total_TruePositive_False_Negative[i])) / 1000;

			fscore_string += mapInt_to_Label.get(i) + ",";

			// Confuse Map
			int total_nb = 0;
			for (int j = 0; j < nlabels; j++) {
				if (confuseSet[j]) {
					fscore_string += Integer.toString(confuseMatrix[i][j])
							+ ",";
					total_nb += confuseMatrix[i][j];
				}
			}
			fscore_string += total_nb + ",";

			fscore_string += Integer.toString(truePositive[i])
					+ ","
					+ Integer.toString(total_TruePositive_FalsePositive[i]
							- truePositive[i])
					+ ","
					+ Integer.toString(total_TruePositive_False_Negative[i]
							- truePositive[i]) + "," + f.format(prec) + ","
					+ f.format(recall) + ",";

			double f_score_class = 0;
			if (prec != 0 && recall != 0) {
				f_score_class = 2 * prec * recall / (prec + recall);
				f_score_class = (double) Math.round(f_score_class * 1000) / 1000;
			}

			fscore_string += f.format(f_score_class) + ",";
			fscore_string += ('\n');

			currLabel.setPrecision(prec);
			currLabel.setRecall(recall);
			currLabel.setFscore(f_score_class);
		}

		/*
		 * 7. Last line
		 */
		prec = (total_TruePositive_FalsePositive[nlabels] == 0) ? 0
				: ((double) (truePositive[nlabels] * 100000 / total_TruePositive_FalsePositive[nlabels])) / 1000;
		recall = (total_TruePositive_False_Negative[nlabels] == 0) ? 0
				: ((double) (truePositive[nlabels] * 100000 / total_TruePositive_False_Negative[nlabels])) / 1000;

		fscore_string += "Total :,";

		int total_cross_label = 0;
		for (int i = 0; i < nlabels; i++) {
			int total_for_label = 0;
			for (int j = 0; j < nlabels; j++) {
				total_for_label += confuseMatrix[j][i];
			}
			total_cross_label += total_for_label;
			fscore_string += Integer.toString(total_for_label) + ",";
		}
		fscore_string += total_cross_label + ",";

		fscore_string += Integer.toString(truePositive[nlabels])
				+ ","
				+ Integer
						.toString((total_TruePositive_FalsePositive[nlabels] - truePositive[nlabels]))
				+ ","
				+ Integer
						.toString((total_TruePositive_False_Negative[nlabels] - truePositive[nlabels]))
				+ "," + f.format(prec) + "," + f.format(recall) + ","
				+ f.format(2 * prec * recall / (prec + recall)) // Accuracy
				+ "\n";

		return fscore_string;
	}

	public Label_Service getResults(){
		/*
		 * 1. DataSet and Mapping
		 */
		DataSet_Service dataset_service = new DataSet_Service();
		int nlabels = dataset_service.getNumberLabel(corpora);
		HashMap<String, Integer> mapLabel_to_Int = dataset_service
				.getMapLabel_to_Int(corpora);
		HashMap<Integer, String> mapInt_to_Label = dataset_service
				.getMapInt_to_Label(corpora);

		/*
		 * 2. Label creation
		 */
		String fscore_string = "";
		Label_Service label_service = new Label_Service();
		int index_id = 0;
		for (int i = 0; i < nlabels; i++) {
			Label_Instance currLabel = new Label_Instance();
			currLabel.setId_evaluation_service(index_id);
			currLabel.setName(mapInt_to_Label.get(index_id));
			label_service.add(currLabel);
			index_id++;
		}

		/*
		 * 3. Var. TP and others
		 */
		int truePositive[] = new int[nlabels + 1];
		int total_TruePositive_FalsePositive[] = new int[nlabels + 1];
		int total_TruePositive_False_Negative[] = new int[nlabels + 1];
		int confuseMatrix[][] = new int[nlabels][nlabels];

		/*
		 * 4. Count
		 */
		for (Unit currUnit : corpora) {
			String currTrueLabel = currUnit.getY();
			String currPredictLabel = currUnit.getPredict_y();

			// TruePositive
			if (currTrueLabel.equals(currPredictLabel)) {
				truePositive[mapLabel_to_Int.get(currTrueLabel)]++;
				truePositive[nlabels]++;

				// Ajout dans ArrayList Label
				Label_Instance currLabel = label_service.get(mapLabel_to_Int
						.get(currTrueLabel));
				currLabel.addTruePositive(currUnit);

			}
			// FalsePositive
			// FalseNegative
			else {
				for (Label_Instance currLabel : label_service) {

					// False Negative
					if (currLabel.getName().equals(currTrueLabel)
							&& !currLabel.getName().equals(currPredictLabel)) {
						currLabel.addFalseNegative(currUnit);
						
						currLabel.addConfusionFalseNegative(currUnit,currPredictLabel);
					}

					// False Positive
					if (!currLabel.getName().equals(currTrueLabel)
							&& currLabel.getName().equals(currPredictLabel)) {
						currLabel.addFalsePositive(currUnit);
						
						currLabel.addConfusionFalsePositive(currUnit);
					}

				}
			}

			// Total_truePositive_falsePositive (precision)
			total_TruePositive_FalsePositive[mapLabel_to_Int
					.get(currPredictLabel)]++;
			total_TruePositive_FalsePositive[nlabels]++;

			// Total_truePositive_falseNegative (rappel)
			total_TruePositive_False_Negative[mapLabel_to_Int
					.get(currTrueLabel)]++;
			total_TruePositive_False_Negative[nlabels]++;

			confuseMatrix[mapLabel_to_Int.get(currTrueLabel)][mapLabel_to_Int
					.get(currPredictLabel)]++;
		}

		/*
		 * 5. Confuse Matrix
		 */
		boolean confuseSet[] = new boolean[nlabels + 1];
		for (int i = 0; i < confuseSet.length; i++) {
			confuseSet[i] = true;
		}
		if (confuseSet != null) {
			// matconf_string += ("T\\P,");
			for (int i = 0; i < nlabels; i++) {
				if (confuseSet[i]) {
					// matconf_string += mapInt_to_Label.get(i) + ",";
				}
			}
			// matconf_string += ('\n');

			// Lignes suivantes
			for (int i = 0; i < nlabels; i++) {
				if (confuseSet[i]) {
					// Première colonne
					// matconf_string += mapInt_to_Label.get(i) + ",";

					// Colonnes suivantes
					for (int j = 0; j < nlabels; j++) {
						if (confuseSet[j]) {
							// matconf_string += Integer
							// .toString(confuseMatrix[i][j]) + ",";
						}
					}
					// matconf_string += '\n';
				}
			}
		}

		/*
		 * 6. By Classes
		 */

		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat f = new DecimalFormat("##.000", otherSymbols);

		fscore_string += name + ",";
		for (int i = 0; i < nlabels; i++) {
			fscore_string += mapInt_to_Label.get(i) + ",";
		}
		fscore_string += "Total,TP,FP,FN,Precision,Recall,F1";
		fscore_string += '\n';

		double prec, recall;
		for (int i = 0; i < nlabels; i++) {

			Label_Instance currLabel = label_service.get(i);

			prec = (total_TruePositive_FalsePositive[i] == 0) ? 0
					: ((double) (truePositive[i] * 100000 / total_TruePositive_FalsePositive[i])) / 1000;
			recall = (total_TruePositive_False_Negative[i] == 0) ? 0
					: ((double) (truePositive[i] * 100000 / total_TruePositive_False_Negative[i])) / 1000;

			fscore_string += mapInt_to_Label.get(i) + ",";

			// Confuse Map
			int total_nb = 0;
			for (int j = 0; j < nlabels; j++) {
				if (confuseSet[j]) {
					fscore_string += Integer.toString(confuseMatrix[i][j])
							+ ",";
					total_nb += confuseMatrix[i][j];
				}
			}
			fscore_string += total_nb + ",";

			fscore_string += Integer.toString(truePositive[i])
					+ ","
					+ Integer.toString(total_TruePositive_FalsePositive[i]
							- truePositive[i])
					+ ","
					+ Integer.toString(total_TruePositive_False_Negative[i]
							- truePositive[i]) + "," + f.format(prec) + ","
					+ f.format(recall) + ",";

			double f_score_class = 0;
			if (prec != 0 && recall != 0) {
				f_score_class = 2 * prec * recall / (prec + recall);
				f_score_class = (double) Math.round(f_score_class * 1000) / 1000;
			}

			fscore_string += f.format(f_score_class) + ",";
			fscore_string += ('\n');

			currLabel.setPrecision(prec);
			currLabel.setRecall(recall);
			currLabel.setFscore(f_score_class);
		}

		/*
		 * 7. Last line
		 */
		prec = (total_TruePositive_FalsePositive[nlabels] == 0) ? 0
				: ((double) (truePositive[nlabels] * 100000 / total_TruePositive_FalsePositive[nlabels])) / 1000;
		recall = (total_TruePositive_False_Negative[nlabels] == 0) ? 0
				: ((double) (truePositive[nlabels] * 100000 / total_TruePositive_False_Negative[nlabels])) / 1000;

		fscore_string += "Total :,";

		int total_cross_label = 0;
		for (int i = 0; i < nlabels; i++) {
			int total_for_label = 0;
			for (int j = 0; j < nlabels; j++) {
				total_for_label += confuseMatrix[j][i];
			}
			total_cross_label += total_for_label;
			fscore_string += Integer.toString(total_for_label) + ",";
		}
		fscore_string += total_cross_label + ",";

		fscore_string += Integer.toString(truePositive[nlabels])
				+ ","
				+ Integer
						.toString((total_TruePositive_FalsePositive[nlabels] - truePositive[nlabels]))
				+ ","
				+ Integer
						.toString((total_TruePositive_False_Negative[nlabels] - truePositive[nlabels]))
				+ "," + f.format(prec) + "," + f.format(recall) + ","
				+ f.format(2 * prec * recall / (prec + recall)) // Accuracy
				+ "\n";

		return label_service;
	}
	
	

}
