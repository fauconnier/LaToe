package org.melodi.tools.dataset;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import logicalobjects.Document_Lara;

import org.melodi.reader.larat.internal.Items;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.reader.larat.model.LaratModel;

import au.com.bytecode.opencsv.CSVReader;

public class DataSet_Service {

	public DataSet_Service() {

	}

	/**
	 * 
	 * @param corpuspath
	 *            : /home/.../corp/../LARA_corpus
	 * @param file
	 *            : file with name of file in corpus
	 * @param label
	 *            : file of label y
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public DataSet_Corpora getCorpora(String corpuspath, String file,
			String label) throws NumberFormatException, IOException {
		DataSet_Corpora corpora = new DataSet_Corpora();

		InputStream ips_2 = new FileInputStream(file);
		InputStreamReader ipsr_2 = new InputStreamReader(ips_2);
		BufferedReader br = new BufferedReader(ipsr_2);
		String docname_;

		while ((docname_ = br.readLine()) != null) {

			LaratModel larat_model = new LaratModel();
			larat_model.openFile(corpuspath + "/" + docname_);
			LinkedList<Unit> chainUnits = larat_model.getAllUnits();

			for (Unit currUnit : chainUnits) {
				if (matchCSV(currUnit, docname_, label)) {
					currUnit.setDocument(docname_);
					corpora.add(currUnit);
				}
			}

		}
		br.close();

		corpora.setName("corpus_all");
		corpora.setPath(corpuspath);

		return corpora;
	}
	
	
	public DataSet_Corpora getCorpora(String corpuspath, String file) throws NumberFormatException, IOException {
		DataSet_Corpora corpora = new DataSet_Corpora();

		InputStream ips_2 = new FileInputStream(file);
		InputStreamReader ipsr_2 = new InputStreamReader(ips_2);
		BufferedReader br = new BufferedReader(ipsr_2);
		String docname_;
		
		int unite_lue = 0;
		ArrayList<String> sortie = new ArrayList<String>();
		while ((docname_ = br.readLine()) != null) {

			LaratModel larat_model = new LaratModel();
			larat_model.openFile(corpuspath + "/" + docname_);
			LinkedList<Unit> chainUnits = larat_model.getAllUnits();

			unite_lue += chainUnits.size();
			
			sortie.add(docname_+","+chainUnits.size());
			
			for (Unit currUnit : chainUnits) {
					currUnit.setY(currUnit.getAxe_semantique());
					currUnit.setDocument(docname_);
					corpora.add(currUnit);
			}

		}
		br.close();

		corpora.setName("corpus_all");
		corpora.setPath(corpuspath);

		System.out.println("Size lue : " +  unite_lue);
		
		for(String cc : sortie){
			System.out.println(cc);
		}
		
		return corpora;
	}

	
	public void writeCorpora(String corpuspath,  String file, DataSet_Corpora corpus) throws IOException{
		
		InputStream ips_2 = new FileInputStream(file);
		InputStreamReader ipsr_2 = new InputStreamReader(ips_2);
		BufferedReader br = new BufferedReader(ipsr_2);
		String docname_;

		int unite_ecrite = 0;
		ArrayList<String> ecriture = new ArrayList<String>();
		while ((docname_ = br.readLine()) != null) {
			
			System.out.println(docname_);
			LaratModel larat_model = new LaratModel();
			larat_model.openFile(corpuspath + "/" + docname_);
			larat_model.resetAllUnits();
			
			LinkedList<Unit> currChain = getAllUnitsForADocument(docname_,corpus);
			unite_ecrite += currChain.size();
			
				ecriture.add(docname_ + "," + currChain.size()+"");
				larat_model.setAllUnits(currChain);
				larat_model.valid("Consensus","Reference");//écriture
			
		}
		
		System.out.println("Size ecrit : " + unite_ecrite);
		
		for(String cc : ecriture){
			System.out.println(cc);
		}
	}
	
	private LinkedList<Unit> getAllUnitsForADocument(String document, DataSet_Corpora corpus){
		
		LinkedList<Unit> currChain = new LinkedList<>();
		
		for(Unit currUnit : corpus){
			if(currUnit.getDocument().equals(document)){
				currChain.add(currUnit);
			}
		}
		return currChain;
	}
	
	
	
	public void writeIdentifiant(DataSet_Corpora corpora, String path) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(path, "UTF-8");
		
		writer.println("docname,id");
		
		for(Unit currUnit : corpora){
			writer.println(currUnit.getDocument() + "," + currUnit.getId());
		}
		
		writer.close();
	}
	
	
	public DataSet_Corpora preprocess(DataSet_Corpora corpora) {

		// y -> last_y
		for (Unit currUnit : corpora) {
			currUnit.setLastY(currUnit.getY());
		}
		return corpora;
	}

	public DataSet_Corpora map(DataSet_Corpora corpora, String y,
			String substitution) {
		System.out.println("Replace " + y + " by " + substitution);
		for (Unit currUnit : corpora) {
			if (currUnit.getY().equals(y)) {
				currUnit.setY(substitution);
			}
		}
		return corpora;
	}

	public DataSet_Corpora nmap(DataSet_Corpora corpora, String y,
			String substitution) {
		System.out.println("Replace non-" + y + " by " + substitution);
		for (Unit currUnit : corpora) {
			if (!currUnit.getY().equals(y)) {
				currUnit.setY(substitution);
			}
		}
		return corpora;
	}
	
	public DataSet_Corpora remove(DataSet_Corpora corpora, String label){
		System.out.println("Remove " + label);
		DataSet_Corpora newcorpora = new DataSet_Corpora();
		for(Unit currUnit : corpora){
			if(!currUnit.getY().equals(label)){
				newcorpora.add(currUnit);
			}
		}
		return newcorpora;
	}
	
	public void serializeSampling(DataSet_Sampling sampling, String path) throws IOException{
		
		System.out.println("Serialize corpora to " + path);
		ObjectOutputStream out_train = new ObjectOutputStream(
				new FileOutputStream(path));
		out_train.writeObject(sampling);
		out_train.close();
		
	}

	public void serializeCorpora(DataSet_Corpora corpora, String path)
			throws FileNotFoundException, IOException {

		System.out.println("Serialize corpora to " + path);
		ObjectOutputStream out_train = new ObjectOutputStream(
				new FileOutputStream(path));
		out_train.writeObject(corpora);
		out_train.close();
	}
	
	public void serializeDocument(Document_Lara currDocument, String path) throws IOException{
		
		System.out.println("Serialize document to " + path);
		ObjectOutputStream out_train = new ObjectOutputStream(
				new FileOutputStream(path));
		out_train.writeObject(currDocument);
		out_train.close();
	}
	
	public Document_Lara deserializeDocument(String path) throws FileNotFoundException, IOException, ClassNotFoundException{
		
		System.out.println("Deserialize document from " + path);
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
		Document_Lara currDocument = (Document_Lara) in.readObject();
		in.close();
		return currDocument;
	}


	
	public DataSet_Sampling deserializeSampling(String path) throws FileNotFoundException, IOException, ClassNotFoundException{
		
		System.out.println("Deserialize corpora from " + path);
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
		DataSet_Sampling sampling = (DataSet_Sampling) in.readObject();
		in.close();
		return sampling;
	}

	public DataSet_Corpora deserializeCorpora(String path)
			throws FileNotFoundException, IOException, ClassNotFoundException {

		System.out.println("Deserialize corpora from " + path);
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
		DataSet_Corpora corpora = (DataSet_Corpora) in.readObject();
		in.close();
		return corpora;
	}
	
	public DataSet_Corpora resampleDataSameDistrib(DataSet_Corpora corpora) throws IOException, ClassNotFoundException{
		
		// 1. getFreq and Label
		HashMap<String, Integer> freq_label = this.getFrequencies(corpora,
				false);
		List<String> keys = new ArrayList(freq_label.keySet());
		Collections.sort(keys); // sort
		
		// 2. Methode facile c'est on double tout.
		DataSet_Corpora new_corpora = new DataSet_Corpora();
		for(Unit currUnit : corpora){
			
			 // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(currUnit);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            Unit newUnit = (Unit) in.readObject();
            
            // Inverse Item
            Items newUnit_Items = newUnit.getItems();
            newUnit_Items.set(0, newUnit_Items.get(1));
			
			
//			// 4. Verify
//			System.out.println("Old item 0 : " + currUnit.getItem(0).getSurface());
//			System.out.println("New item 0 : " + newUnit.getItem(0).getSurface());
            
            // Add Item
            
            new_corpora.add(currUnit);
            new_corpora.add(newUnit);
            
            if(currUnit.getItems().size() > 2){
                // Make an input stream from the byte array and read
                // a copy of the object back in.
                ObjectInputStream in2 = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
                Unit newUnit2 = (Unit) in2.readObject();
                
                // Inverse Item
                Items newUnit_Items2 = newUnit2.getItems();
                newUnit_Items2.set(0, newUnit_Items2.get(2));
                
                new_corpora.add(newUnit2);
            }else{
            	new_corpora.add(currUnit);
            }
		}
		return new_corpora;
	}
	
	
	
	public DataSet_Corpora resampleDataSupressUniform(DataSet_Corpora corpora){
		// Attend un corpus avec 2 classes
		
		// 1. getFreq and Label
		HashMap<String, Integer> freq_label = this.getFrequencies(corpora,
				false);
		List<String> keys = new ArrayList(freq_label.keySet());
		Collections.sort(keys); // sort
		
		if(keys.size() > 2){
			System.err.println("Erreur");
			System.exit(1);
		}
		
		// 2. Get the minimum
		int min = 100000; // TODO c'est dégeulasse ça!
		for(String currKey : keys){
			int currentFreq = freq_label.get(currKey);
			if(currentFreq < min){
				min = currentFreq;
			}
		}
		System.out.println("Min : " + min);
		
		// 3. reduce the majority
		String label_to_remove = "";
		int total = 0;
		for(String currKey : keys){
			
			int currentFreq = freq_label.get(currKey);
			if(currentFreq != min){
				label_to_remove = currKey;
				total = currentFreq;
			}
		}
		int nb_to_remove = total - min;
		corpora = this.removeXOfLabel(corpora, nb_to_remove, label_to_remove);
		
		
		return corpora;
	}
	
	public DataSet_Corpora removeXOfLabel(DataSet_Corpora corpora, int nb, String label){
		
		DataSet_Corpora new_corpora = new DataSet_Corpora();
		
		int current_removed = 0;
		for(Unit currUnit : corpora){
			if(currUnit.getY().equals(label) & current_removed < nb){
				current_removed++;
			}
			else{
				new_corpora.add(currUnit);
			}
		}
		return new_corpora;
	}

	public DataSet_Sampling sampleData_10CV(DataSet_Corpora corpora) {

		// 1. create 10 corpora
		int nb_fold = 10;
		DataSet_Sampling cv_fold = new DataSet_Sampling();
		for (int i = 0; i < nb_fold; i++) {
			DataSet_Corpora currFold = new DataSet_Corpora();
			int id_fold = i + 1;
			currFold.setName("fold" + id_fold);
			cv_fold.add(currFold);
		}

		// 2. getFreq and Label
		HashMap<String, Integer> freq_label = this.getFrequencies(corpora,
				false);
		List<String> keys = new ArrayList(freq_label.keySet());
		Collections.sort(keys); // sort

		// 3. Size corpora
		double total_nb = corpora.size();
		Double size_fold = total_nb / nb_fold;

		// 4. Relative freq
		for (String currKey : keys) {
			ArrayList<Unit> currArrayLabelY = getAllUnitY(corpora, currKey);
			double size_label = currArrayLabelY.size();
			Double relative_freq = size_label / total_nb;
			double print_relative_freq = Math.round(relative_freq * 100.0) / 100.0;
			// System.out.println("FreqRel :" + currKey + "\t" +
			// print_relative_freq );

			// 5. get Numbers of Y in datasets
			double nb_in_unique_fold = relative_freq * size_fold;

			// 6. Unsort all unit Y
			Collections.shuffle(currArrayLabelY);

			// 7. put unit in dataset
			// TODO : improve that

			// Parcours des folds
			int deb_index_currArrayLabelY = 0;
			int nb_put = 0;
			for (int id_fold = 0; id_fold < nb_fold; id_fold++) {

				// Trouver la fin de l'index
				int fin_index_currArrayLabelY = deb_index_currArrayLabelY
						+ (int) nb_in_unique_fold;

				// Récupérer le fold courant
				DataSet_Corpora currFold = cv_fold.get(id_fold);

				int curr_index = 0;
				for (Unit currUnit : currArrayLabelY) {

					if (curr_index >= deb_index_currArrayLabelY
							&& curr_index < fin_index_currArrayLabelY) {
						currFold.add(currUnit);
						nb_put++;
					}
					curr_index++;
				}
				deb_index_currArrayLabelY = fin_index_currArrayLabelY;
			}
			
			
			// Eparpillement pour les dernières
			if(nb_put < currArrayLabelY.size()){
				
				int index = 0;
				for(Unit currUnit : currArrayLabelY){
					
					if(index >= nb_put){
						// choisir aléatoirement un fold
						DataSet_Corpora currFold = cv_fold.getRandomCorpora();
						currFold.add(currUnit);
					}
					
					index++;
				}
				
			}
			
			

			// int index = 0;
			// for (Unit currUnit : currArrayLabelY) {
			//
			//
			//
			// if (index < nb_in_train) {
			// train.add(currUnit);
			// } else if ((index >= nb_in_train)
			// && (index < (nb_in_train + nb_in_valid))) {
			// valid.add(currUnit);
			// } else {
			// test.add(currUnit);
			// }
			// index++;
			// }
		}

		// 8. Return results
		return cv_fold;

	}
	
	public DataSet_Sampling sampleData_TwoSet(DataSet_Corpora corpora, int percent_a, int percent_b){
		
		// 1. create 10 corpora
		int nb_fold = 2;
		DataSet_Sampling binary_fold = new DataSet_Sampling();
		for (int i = 0; i < nb_fold; i++) {
			DataSet_Corpora currFold = new DataSet_Corpora();
			int id_fold = i + 1;
			currFold.setName("fold" + id_fold);
			binary_fold.add(currFold);
		}

		// 2. getFreq and Label
		HashMap<String, Integer> freq_label = this.getFrequencies(corpora,
				false);
		List<String> keys = new ArrayList(freq_label.keySet());
		Collections.sort(keys); // sort

		// 3. Size corpora
		double total_nb = corpora.size();
		// Sanity Check
		if(percent_a + percent_b != 100){
			System.err.println("Error in size binary fold.");
		}
		
		Double percent_A_nor = (double) percent_a / 100;
		Double size_a = total_nb * percent_A_nor;
		Double size_b = total_nb * (percent_b/100);

		
		// 4. Relative freq
		for (String currKey : keys) {
			ArrayList<Unit> currArrayLabelY = getAllUnitY(corpora, currKey);
			double size_label = currArrayLabelY.size();
			Double relative_freq = size_label / total_nb;
			double print_relative_freq = Math.round(relative_freq * 100.0) / 100.0;
			// System.out.println("FreqRel :" + currKey + "\t" +
			// print_relative_freq );

			// 5. get Numbers of Y in datasets
			double nb_in_A = relative_freq * size_a;
			double nb_in_B = relative_freq * size_b;

			// System.out.println("Size in Train : " + nb_in_train);
			// System.out.println("Size in valid : " + nb_in_valid);
			// System.out.println("Size in test : " + nb_in_test);

			// 6. Unsort all unit Y
			Collections.shuffle(currArrayLabelY);

			// 7. put unit in dataset
			// TODO : improve that
			int index = 0;
			for (Unit currUnit : currArrayLabelY) {
				if (index < nb_in_A) {
					binary_fold.get(0).add(currUnit);
				} else {
					binary_fold.get(1).add(currUnit);
				}
				index++;
			}
		}

			
		return binary_fold;
	}

	public DataSet_Sampling sampleData_HoldOut(DataSet_Corpora corpora) {

		// 1. create Corpora Train/Valid/Test : 60, 20, 20
		DataSet_Corpora train = new DataSet_Corpora();
		train.setName("train");
		DataSet_Corpora valid = new DataSet_Corpora();
		valid.setName("valid");
		DataSet_Corpora test = new DataSet_Corpora();
		test.setName("test");

		// 2. getFreq and Label
		HashMap<String, Integer> freq_label = this.getFrequencies(corpora,
				false);
		List<String> keys = new ArrayList(freq_label.keySet());
		Collections.sort(keys); // sort

		// 3. Size corpora
		double total_nb = corpora.size();
		Double train_size = total_nb * 0.60;
		Double valid_size = total_nb * 0.20;
		Double test_size = total_nb * 0.20;

		// System.out.println("Size train : " + train_size);
		// System.out.println("Size valid : " + valid_size);
		// System.out.println("Size test : " + test_size);

		// 4. Relative freq
		for (String currKey : keys) {
			ArrayList<Unit> currArrayLabelY = getAllUnitY(corpora, currKey);
			double size_label = currArrayLabelY.size();
			Double relative_freq = size_label / total_nb;
			double print_relative_freq = Math.round(relative_freq * 100.0) / 100.0;
			// System.out.println("FreqRel :" + currKey + "\t" +
			// print_relative_freq );

			// 5. get Numbers of Y in datasets
			double nb_in_train = relative_freq * train_size;
			double nb_in_valid = relative_freq * valid_size;
			double nb_in_test = relative_freq * test_size;

			// System.out.println("Size in Train : " + nb_in_train);
			// System.out.println("Size in valid : " + nb_in_valid);
			// System.out.println("Size in test : " + nb_in_test);

			// 6. Unsort all unit Y
			Collections.shuffle(currArrayLabelY);

			// 7. put unit in dataset
			// TODO : improve that
			int index = 0;
			for (Unit currUnit : currArrayLabelY) {

				if (index < nb_in_train) {
					train.add(currUnit);
				} else if ((index >= nb_in_train)
						&& (index < (nb_in_train + nb_in_valid))) {
					valid.add(currUnit);
				} else {
					test.add(currUnit);
				}
				index++;
			}
		}

		// 8. Return results
		DataSet_Sampling holdout_sampling = new DataSet_Sampling();
		holdout_sampling.add(train);
		holdout_sampling.add(valid);
		holdout_sampling.add(test);

		return holdout_sampling;
	}

	public ArrayList<Unit> getAllUnitY(DataSet_Corpora corpora, String label) {

		ArrayList<Unit> currArray = new ArrayList<Unit>();

		for (Unit currUnit : corpora) {
			if (currUnit.getY().equals(label)) {
				currArray.add(currUnit);
			}
		}
		return currArray;
	}

	public HashMap<Integer, String> getMapInt_to_Label(DataSet_Corpora corpora) {
		ArrayList<String> labels = this.getLabels(corpora);
		HashMap<Integer, String> mapInt_to_Label = new HashMap<Integer, String>();

		int index = 0;
		for (String currLabel : labels) {
			mapInt_to_Label.put(index, currLabel);
			index++;
		}
		return mapInt_to_Label;
	}

	public HashMap<String, Integer> getMapLabel_to_Int(DataSet_Corpora corpora) {
		ArrayList<String> labels = this.getLabels(corpora);
		HashMap<String, Integer> mapLabel_to_Int = new HashMap<String, Integer>();

		int index = 0;
		for (String currLabel : labels) {
			mapLabel_to_Int.put(currLabel, index);
			index++;
		}
		return mapLabel_to_Int;
	}

	public ArrayList<String> getLabels(DataSet_Corpora corpora) {
		HashMap<String, Integer> freq = this.getFrequencies(corpora, false);
		ArrayList<String> labels = new ArrayList(freq.keySet());
		Collections.sort(labels);
		return labels;
	}

	public int getNumberLabel(DataSet_Corpora corpora) {
		HashMap<String, Integer> freq = this.getFrequencies(corpora, false);
		return freq.size();
	}

	public HashMap<String, Integer> getFrequencies(DataSet_Corpora corpora,
			boolean print) {

		if (print)
			System.out.println("Stats for corpora for " + corpora.getName());

		// Count : number of Y
		HashMap<String, Integer> freq = new HashMap<String, Integer>();
		for (Unit currUnit : corpora) {

			if (freq.containsKey(currUnit.getY())) {
				int nb = freq.get(currUnit.getY());
				nb++;
				freq.put(currUnit.getY(), nb);
			} else {
				freq.put(currUnit.getY(), 1);
			}
		}

		// Print stats
		List<String> keys = new ArrayList(freq.keySet());
		Collections.sort(keys);

		int total = 0;
		for (String currKey : keys) {
			double nb = freq.get(currKey);
			double total_nb = corpora.size();
			double relative_freq = nb / total_nb;
			double print_relative_freq = Math.round(relative_freq * 100.0) / 100.0;

			if (print)
				System.out.println(currKey + "\t" + freq.get(currKey) + " ("
						+ print_relative_freq + ")");
			if (print)
				total += freq.get(currKey);
		}
		if (print)
			System.out.println("Size : " + total);

		return freq;
	}

	private static boolean matchCSV(Unit currUnit, String docname_unit,
			String label_y_path) throws NumberFormatException, IOException {
		CSVReader reader = new CSVReader(new FileReader(label_y_path));
		String[] nextLine;

		boolean flag_first_line = false;
		while ((nextLine = reader.readNext()) != null) {

			if (flag_first_line) {// First line
				String docname_csv = nextLine[0];

				int id_doc_csv = Integer.parseInt(nextLine[1]);

				String y = "";
				if (nextLine.length > 2) {
					y = nextLine[2];
				} else {
					y = "notse";
				}

				if ((currUnit.getId() == id_doc_csv)
						&& (docname_unit.equals(docname_csv))) {
					currUnit.setY(y);
					return true;
				}
			} else {
				flag_first_line = true;
			}
		}
		return false;
	}

}
