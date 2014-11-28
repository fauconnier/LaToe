/** Segment.java
 * 
 * @author Sunita Sarawagi
 * @since 1.0
 * @version 1.3
 */
package org.melodi.learning.iitb.Segment;

import java.io.File;
import java.io.FileInputStream;
import java.util.StringTokenizer;
import java.util.Vector;

import org.melodi.learning.iitb.BSegment.BFeatureGenImpl;
import org.melodi.learning.iitb.BSegmentCRF.BSegmentCRF;
import org.melodi.learning.iitb.CRF.CRF;
import org.melodi.learning.iitb.CRF.FeatureGenerator;
import org.melodi.learning.iitb.CRF.NestedCRF;
import org.melodi.learning.iitb.CRF.SegmentDataSequence;
import org.melodi.learning.iitb.CRF.Segmentation;
import org.melodi.learning.iitb.CRF.Util;
import org.melodi.learning.iitb.Model.FeatureGenImpl;
import org.melodi.learning.iitb.Model.NestedFeatureGenImpl;
import org.melodi.learning.iitb.Utils.Options;

public class Segment {
	String inName;
	String outDir;
	String baseDir = "";
	int nlabels;

	String delimit = " \t"; // used to define token boundaries
	String tagDelimit = "|"; // separator between tokens and tag number
	String impDelimit = ""; // delimiters to be retained for tagging
	String groupDelimit = null;

	boolean confuseSet[] = null;
	boolean validate = false;
	String mapTagString = null;
	String smoothType = "";

	String modelArgs = "";
	String featureArgs = "";
	String modelGraphType = "naive";

	LabelMap labelMap;
	Options options;

	CRF crfModel;
	FeatureGenImpl featureGen;

	public FeatureGenerator featureGenerator() {
		return featureGen;
	}

	/**
	 * Main
	 * 
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String argv[]) throws Exception {

		argv = new String[3];
		argv[0] = "calc";
		argv[1] = "-f";
		argv[2] = "./lib/CRF/samples/us50.conf";

		if (argv.length < 3) {
			System.out
					.println("Usage: java iitb.Segment.Segment <train|test|calc|all> -f <conf-file>");
			return;
		}
		Segment segment = new Segment();
		segment.parseConf(argv);

		if (argv[0].toLowerCase().equals("all")) {
			segment.train();
			segment.doTest();
			segment.calc();
		}
		if (argv[0].toLowerCase().equals("train")) {
			segment.train();
		}
		if (argv[0].toLowerCase().equals("test")) {
			segment.test();
		}
		if (argv[0].toLowerCase().equals("calc")) {
			segment.calc();
		}
	}

	public void train() throws Exception {
		DataCruncher.createRaw(baseDir + "/data/" + inName + "/" + inName
				+ ".train", tagDelimit);
		File dir = new File(baseDir + "/learntModels/" + outDir);
		dir.mkdirs();

		TrainData trainData = DataCruncher.readTagged(nlabels, baseDir
				+ "/data/" + inName + "/" + inName + ".train", baseDir
				+ "/data/" + inName + "/" + inName + ".train", delimit,
				tagDelimit, impDelimit, labelMap);
		AlphaNumericPreprocessor.preprocess(trainData, nlabels);

		allocModel();
		featureGen.train(trainData);

		double featureWts[] = crfModel.train(trainData);
		if (options.getInt("debugLvl") > 1) {
			Util.printDbg("Training done");
		}

		crfModel.write(baseDir + "/learntModels/" + outDir + "/crf");
		featureGen.write(baseDir + "/learntModels/" + outDir + "/features");

		// Debug Mode
		if (options.getInt("debugLvl") > 1) {
			Util.printDbg("Writing model to " + baseDir + "/learntModels/"
					+ outDir + "/crf");
		}

		// ShowModel
		if (options.getProperty("showModel") != null) {
			featureGen.displayModel(featureWts);
		}
	}

	public void test() throws Exception {
		allocModel();
		featureGen.read(baseDir + "/learntModels/" + outDir + "/features");
		crfModel.read(baseDir + "/learntModels/" + outDir + "/crf");
		doTest();
	}

	public void doTest() throws Exception {

		// Création du répertoire de sortie
		File dir = new File(baseDir + "/out/" + outDir);
		dir.mkdirs();

		// Récupération des données de test
		TestData testData = new TestData(baseDir + "/data/" + inName + "/"
				+ inName + ".test", delimit, impDelimit, groupDelimit);
		TestDataWrite testDatawrite_var = new TestDataWrite(baseDir + "/out/"
				+ outDir + "/" + inName + ".test", baseDir + "/data/" + inName
				+ "/" + inName + ".test", delimit, tagDelimit, impDelimit,
				labelMap);

		String collect[] = new String[nlabels];

		TestRecord testRecord = new TestRecord(collect);

		for (String seq[] = testData.nextRecord(); seq != null; seq = testData
				.nextRecord()) {

			testRecord.init(seq);

			if (options.getInt("debugLvl") > 1) {
				Util.printDbg("Invoking segment on " + seq);
			}

			int path[] = segmentMethod(testRecord, testData.groupedTokens(),
					collect);
			testDatawrite_var.writeRecord(path, testRecord.length());
		}
		testDatawrite_var.close();
	}

	/**
	 * segmentMethod
	 * 
	 * @param testRecord
	 * @param groupedToks
	 * @param collect
	 * @return
	 */
	public int[] segmentMethod(TestRecord testRecord, int[] groupedToks,
			String collect[]) {
		
		
		for (int i = 0; i < testRecord.length(); i++){
			testRecord.seq[i] = AlphaNumericPreprocessor
					.preprocess(testRecord.seq[i]);
		}
		crfModel.apply(testRecord);
		featureGen.mapStatesToLabels(testRecord);
		
		
		int path[] = testRecord.path;
		
		// On vide collect [collection qui contient les lables]
		for (int i = 0; i < nlabels; i++){
			collect[i] = null;
		}
		// On parcours la séquence de test
		for (int i = 0; i < testRecord.length(); i++) {
//			 System.out.println(testRecord.seq[i] + " " + path[i]);
			int snew = path[i];
			if (snew >= 0) {
				if (collect[snew] == null) {
					collect[snew] = testRecord.seq[i];
				} else {
					collect[snew] = collect[snew] + " " + testRecord.seq[i];
				}
			}
		}
		return path;
	}

	
	
	
	public void calc() throws Exception {
		Vector<String[]> s = new Vector<String[]>();
		TrainData tdManuel = DataCruncher.readTagged(nlabels, baseDir + "/data/"
				+ inName + "/" + inName + ".test", baseDir + "/data/" + inName
				+ "/" + inName + ".test", delimit, tagDelimit, impDelimit,
				labelMap);
		
		TrainData tdPredict = DataCruncher.readTagged(nlabels, baseDir + "/out/"
				+ outDir + "/" + inName + ".test", baseDir + "/data/" + inName
				+ "/" + inName + ".test", delimit, tagDelimit, impDelimit,
				labelMap);
		
		DataCruncher.readRaw(s, baseDir + "/data/" + inName + "/" + inName
				+ ".test", "", "");
		
		int len = tdPredict.size();
		
		int truePos[] = new int[nlabels + 1];
		int totalMarkedPos[] = new int[nlabels + 1];
		int totalPos[] = new int[nlabels + 1];
		int confuseMatrix[][] = new int[nlabels][nlabels];
		boolean printDetails = (options.getInt("debugLvl") > 0);
		
		// Sanity Check
		if (tdPredict.size() != tdManuel.size()) {
			// Sanity Check
			System.out.println("Length Mismatch - Raw: " + len + " Auto: "
					+ tdPredict.size() + " Man: " + tdManuel.size());
		}

		//Parcours de la longueur du DataSet prédit
		for (int i = 0; i < len; i++) {
			String raw[] = (String[]) (s.get(i));
			
			// Récupération des records
			TrainRecord trMan = tdManuel.nextRecord();
			TrainRecord trAuto = tdPredict.nextRecord();
			
			// Récupération de tous les labels
			int labelTrue[] = allLabels(trMan);
			int labelPredict[] = allLabels(trAuto);

			// Sanity Check
			if (labelTrue.length != labelPredict.length) {
				// Sanity Check
				System.out.println("Length Mismatch - Manual: "
						+ labelTrue.length + " Auto: " + labelPredict.length);
				// continue;
			}
			
			// remove invalid tagging.
			boolean invalidMatch = false;
			
			int longeurSequence = labelTrue.length;
			
			for (int j = 0; j < longeurSequence; j++) {
				if (printDetails)
					System.err.println(labelTrue[j] + " " + labelPredict[j]);
				if (labelPredict[j] < 0) {
					invalidMatch = true;
					break;
				}
			}
			if (invalidMatch) {
				if (printDetails)
					System.err.println("No valid path");
				continue;
			}
			// Calcul des labels correctement prédits
			int correctTokens = 0;
			for (int j = 0; j < longeurSequence; j++) {
				totalMarkedPos[labelPredict[j]]++;
				totalMarkedPos[nlabels]++;
				totalPos[labelTrue[j]]++;
				totalPos[nlabels]++;
				confuseMatrix[labelTrue[j]][labelPredict[j]]++;
				if (labelPredict[j] == labelTrue[j]) {
					correctTokens++;
					truePos[labelTrue[j]]++;
					truePos[nlabels]++;
				}
			}
			if (printDetails){
				System.err.println("Stats: " + correctTokens + " " + (longeurSequence));
			}
			int rlen = raw.length;
			
			for (int j = 0; j < rlen; j++) {
				if (printDetails){
					System.err.print(raw[j] + " ");
				}
			}
			
			if (printDetails){
				System.err.println();
			}
			
			for (int j = 0; j < nlabels; j++) {
				String mstr = "";
				for (int k = 0; k < trMan.numSegments(j); k++)
					mstr += arrayToString(trMan.tokens(j, k));
				String astr = "";
				for (int k = 0; k < trAuto.numSegments(j); k++)
					astr += arrayToString(trAuto.tokens(j, k));

				if (!mstr.equalsIgnoreCase(astr))
					if (printDetails)
						System.err.print("W");
				if (printDetails)
					System.err.println(j + ": " + mstr + " : " + astr);
			}
			
			if (printDetails)
				System.err.println();
		}

		
		
		// Création de la matrice de confusion
		if (confuseSet != null) {
			System.out.println("Confusion Matrix:");
			System.out.print("T\\P");
			for (int i = 0; i < nlabels; i++) {
				if (confuseSet[i]) {
					System.out.print("\t" + (i));
				}
			}
			System.out.println();
			for (int i = 0; i < nlabels; i++) {
				if (confuseSet[i]) {
					System.out.print(i);
					for (int j = 0; j < nlabels; j++) {
						if (confuseSet[j]) {
							System.out.print("\t" + confuseMatrix[i][j]);
						}
					}
					System.out.println();
				}
			}
		}
		
		// Affichage des statistiques
		System.out.println("\n\nFScore:");
		System.out.println();
		System.out.println("Label\tTrue+\tMarked+\tActual+\tPrec.\tRecall\tF1");
		double prec, recall;
		for (int i = 0; i < nlabels; i++) {
			prec = (totalMarkedPos[i] == 0) ? 0
					: ((double) (truePos[i] * 100000 / totalMarkedPos[i])) / 1000;
			recall = (totalPos[i] == 0) ? 0
					: ((double) (truePos[i] * 100000 / totalPos[i])) / 1000;
			System.out.println((i) + ":\t" + truePos[i] + "\t"
					+ totalMarkedPos[i] + "\t" + totalPos[i] + "\t" + prec
					+ "\t" + recall + "\t" + 2 * prec * recall
					/ (prec + recall));
		}
		System.out
				.println("---------------------------------------------------------");
		prec = (totalMarkedPos[nlabels] == 0) ? 0
				: ((double) (truePos[nlabels] * 100000 / totalMarkedPos[nlabels])) / 1000;
		recall = (totalPos[nlabels] == 0) ? 0
				: ((double) (truePos[nlabels] * 100000 / totalPos[nlabels])) / 1000;
		System.out.println("Ov:\t" + truePos[nlabels] + "\t"
				+ totalMarkedPos[nlabels] + "\t" + totalPos[nlabels] + "\t"
				+ prec + "\t" + recall + "\t" + 2 * prec * recall
				/ (prec + recall));

	}

	public String arrayToString(Object[] ar) {
		String st = "";
		for (int i = 0; i < ar.length; i++)
			st += (ar[i] + " ");
		return st;
	}

	public int[] allLabels(TrainRecord tr) {
		int[] labs = new int[tr.length()];
		for (int i = 0; i < labs.length; i++)
			labs[i] = tr.y(i);
		return labs;
	}

	/**
	 * parseConf
	 * 
	 * @param argv
	 * @throws Exception
	 */
	public void parseConf(String argv[]) throws Exception {
		options = new Options();
		if ((argv.length >= 2) && argv[1].toLowerCase().equals("-f")) {
			options.load(new FileInputStream(argv[2]));
		}
		options.add(3, argv);
		processArgs();
	}

	/**
	 * processArgs Appelé de parseConf
	 * 
	 * @throws Exception
	 */
	public void processArgs() throws Exception {
		String value = null;
		if ((value = options.getMandatoryProperty("numlabels")) != null) {
			nlabels = Integer.parseInt(value);
		}
		if ((value = options.getProperty("binary")) != null) {
			nlabels = 2;
			labelMap = new BinaryLabelMap(options.getInt("binary"));
		} else {
			labelMap = new LabelMap();
		}
		if ((value = options.getMandatoryProperty("inname")) != null) {
			inName = new String(value);
		}
		if ((value = options.getMandatoryProperty("outdir")) != null) {
			outDir = new String(value);
		}
		if ((value = options.getProperty("basedir")) != null) {
			baseDir = new String(value);
		}
		if ((value = options.getProperty("tagdelimit")) != null) {
			tagDelimit = new String(value);
		}
		// delimiters that will be ignored.
		if ((value = options.getProperty("delimit")) != null) {
			delimit = new String(value);
		}
		if ((value = options.getProperty("impdelimit")) != null) {
			impDelimit = new String(value);
		}
		if ((value = options.getProperty("groupdelimit")) != null) {
			groupDelimit = value;
		}
		if ((value = options.getProperty("confusion")) != null) {
			StringTokenizer confuse = new StringTokenizer(value, ", ");
			int confuseSize = confuse.countTokens();
			confuseSet = new boolean[nlabels + 1];
			for (int i = 0; i < confuseSize; i++) {
				confuseSet[Integer.parseInt(confuse.nextToken())] = true;
			}
		}
		if ((value = options.getProperty("map-tags")) != null) {
			mapTagString = value;
		}
		if ((value = options.getProperty("validate")) != null) {
			validate = true;
		}
		if ((value = options.getProperty("model-args")) != null) {
			modelArgs = value;
			// System.out.println(modelArgs);
		}
		if ((value = options.getProperty("feature-args")) != null) {
			featureArgs = value;
		}
		if ((value = options.getProperty("modelGraph")) != null) {
			modelGraphType = value;
		}
		// System.out.println(options.toString());
	}

	/**
	 * allocModel()
	 * 
	 * @throws Exception
	 */
	public void allocModel() throws Exception {
		// add any code related to dependency/consistency amongst paramter
		// values here..
		System.out.println("Model=" + modelGraphType.toString());
		featureGen = new FeatureGenImpl(modelGraphType, nlabels);
		crfModel = new CRF(featureGen.numStates(), featureGen, options);
	}

};
