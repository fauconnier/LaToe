package org.latoe.layoutanalysis.pdf.labelisation;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


import org.latoe.layoutanalysis.pdf.labelisation.features.FeatureGenerator_Labelisation;
import org.latoe.layoutanalysis.pdf.pdfobject.Chunk_PDF;
import org.latoe.layoutanalysis.pdf.pdfobject.Corpus_PDF;
import org.latoe.layoutanalysis.pdf.pdfobject.Document_PDF;
import org.latoe.layoutanalysis.pdf.pdfobject.Page_PDF;
import org.melodi.learning.iitb.CRF.CRF;
import org.melodi.learning.iitb.CRF.Evaluator;
import org.melodi.learning.iitb.CRF.Util;
import org.melodi.learning.iitb.MaxentClassifier.DataRecord;
import org.melodi.learning.iitb.MaxentClassifier.DataSet;
import org.melodi.learning.iitb.Model.FeatureGenImpl;
import org.melodi.learning.iitb.Segment.AlphaNumericPreprocessor;
import org.melodi.learning.iitb.Segment.DataCruncher;
import org.melodi.learning.iitb.Segment.LabelMap;
import org.melodi.learning.iitb.Segment.TrainData;
import org.melodi.learning.iitb.Utils.Options;

public class Service_CRF implements java.io.Serializable{
	
	/**
	 * Classe TALN 2014:
	 * TODO : A refactoriser!
	 */

	/*
	 * Debug Mode
	 */
	boolean debugMode = true;
	boolean confusionMatrix = false;
	boolean oneToOne=false;
    boolean verbose = true;

	/*
	 * Params nlabels = nb de labels modelGraphType = type de modèle de
	 * dépendances options = ensemble d'options
	 */
	int nlabels;
	String modelGraphType;
	Options options;
	String modelArgs = "";
	String featureArgs = "";

	/*
	 * Labels
	 */
	public HashMap<String, Integer> labelMapInt;
	public HashMap<Integer, String> intMapLabel;
	public HashMap<String,String> labelsToMerge;

	/*
	 * Model & Features 
	 */
	
	CRF crfModel;
	FeatureGenerator_Labelisation featureGen;
	String[] argv;

	/*
	 * Evaluation
	 */
	boolean confuseSet[] = null;
	boolean validate = false;

	/**
	 * Constructeur
	 */
	public Service_CRF(String configuration) {

		// Instanciation pour processLabel()
		labelMapInt = new HashMap<String, Integer>();
		intMapLabel = new HashMap<Integer, String>();
		
		// Instanciation et assignation pour mergeLabel()
		this.labelsToMerge = new HashMap<String,String>();
//		labelsToMerge.put("footnote_end_se", "footnote_end");
		//labelsToMerge.put("footnote_end", "footnote");
		//labelsToMerge.put("h2", "h1");
		//labelsToMerge.put("h3", "h1");
//		labelsToMerge.put("h4", "h3");
		
		labelsToMerge.put("footnote_end", "item");
		labelsToMerge.put("footnote", "byline");
		labelsToMerge.put("footnote_end_se", "item");
		labelsToMerge.put("p_item", "p");
		labelsToMerge.put("item_quote", "item");
		labelsToMerge.put("byline_end", "byline");
		labelsToMerge.put(" nameAuthor", "byline");
		labelsToMerge.put("bibl_head", "h1");
		labelsToMerge.put("head_bibl", "h1");
		labelsToMerge.put("bibl", "item");
		labelsToMerge.put("nameAuthor", "byline");
//		labelsToMerge.put("byline", "other");
//		labelsToMerge.put("header", "other");
//		labelsToMerge.put("footer", "other");
		
		
		 argv = new String[3];
		 argv[0] = "";// A laisser libre pour une utilisation en Shell Command
		 argv[1] = "-f";
		 argv[2] = "./configuration/PDFParsing/crf.conf";
		 argv[2] = configuration;
		 
		 try {
			this.parseConf(argv);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
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
	 * Parse fichier.conf
	 * 
	 * @throws Exception
	 */
	public void processArgs() throws Exception {
		String value = null;
		// Matrice confusion sur les classes int_1, int_2, .., int_n
		if ((value = options.getProperty("confusion")) != null) {
			confusionMatrix = true;
		}
		if((value=options.getProperty("oneToOne")) != null){
			if(value.equals("1")){
				oneToOne=true;
			}
		}
		if((value=options.getProperty("verbose")) != null){
			if(value.equals("1")){
				verbose=true;
			}
		}
		if ((value = options.getMandatoryProperty("modelGraph")) != null) {
			modelGraphType = value;
		}
		if ((value = options.getProperty("debugMode")) != null) {
			if (value.equals("1")) {
				debugMode = true;
			}
		}
		if ((value = options.getProperty("validate")) != null) {
			validate = true;
		}
		if ((value = options.getProperty("model-args")) != null) {
			modelArgs = value;
		}
		if ((value = options.getProperty("feature-args")) != null) {
			featureArgs = value;
		}
	}

	/**
	 * train()
	 * 
	 * @param corpus_dev
         * @param corpus_evaluator
         * @param outputFolder
	 * @throws Exception
	 *             Entraînement du modèle sur un corpus d'entraînement (Training
	 *             Set)
	 */
	public void train(Corpus_PDF corpus_dev, String outputFolder) throws Exception {
		if (debugMode) {
			System.out.println("\nDémarrage de l'entraînement ... \n");
		}

		/**
		 * Etapes dans l'entraînement d'un modèle : 
		 * 0. Traduire 'Document' en 'FlatDocument' + pre-process
		 * 1. ProcessLabel 2. Instance FeatureGenerator (FeatureGenImpl) avec params : modelGraph, nombre de labels 
		 * 3. Instance de CRF avec params : FeatureGen.nb d'états, FeatureGen, options 
		 * 4. Lire les instances du Training Set (corpus_dev) avec utilisation d'un DataIter
		 * 5. FeatureGenImpl.train() : créer un dictionnaire à la volée pour les fréquences avec params : DataIter
		 * 6. CRF.train() : maximiser le log-likelihood de la somme des p(y|x) (optimisation : LBFGS)
		 * 7. Ecriture des Features dans ./data/Output/features.txt 
		 * 8. Ecriture du modèle dans ./data/Output/crf_Model.txt
		 * 9. Ecrirure de la learninc curve dans ./data/Ouptut/learning_curve.csv
		 */

		/*
		 * TODO 0. Document -> FlatDocument
		 * 
		 * Note : 
		 * a) Il est nécessaire de traduire un document avec des pages en
		 * liste simple de Chunk, sans notion de page. 
		 * b) Ceci permettra de rassembler les paragraphes coupés en deux entre deux pages. 
		 * c) Si on  décide d'enlever les footer/header/foot_notes à l'aide
		 * d'heuristiques, c'est ici qu'il faut le faire. 
		 * d) Si on décide de "merger" des labels (e.g : p_item et p), 
		 * c'est aussi ici qu'il faut le faire.
		 * 
		 * Attention à bien updater les int 'id' des chunks après toute
		 * modification. Les id de chunk, contrairement à ce qui est dans le
		 * corpus, vont de 0 à n.
		 * 
		 * 
		 * Pas encore implémenté.
		 */
		
		mergeLabel(corpus_dev);
//		mergeLabel(corpus_evaluator);
		processInformationAboutDocChunk(corpus_dev);
//		processInformationAboutDocChunk(corpus_evaluator);
		
		
		/*
		 * 1. processLabel()
		 * 
		 * Note : processLabel() regarde le nombre de labels différents qui
		 * existent dans le corpus et associe à chacun d'eux un 'int'
		 * (nécessaire pour le CRF). Il est possible de retrouver l'association
		 * label-int, int-label dans les HashMap labelMapInt et intMapLabel.
		 * 
		 * Pour la prédiction, ce sont ces mêmes labels qui seront utilisés (et
		 * non ceux trouvés dans corpus_test). L'hypothèse est que corpus_dev
		 * est assez grand pour que la probabilité d'avoir tous les labels du
		 * corpus dans corpus_dev soit grande.
		 * 
		 * Par conséquent : 
		 * -si corpus_test présente un nouveau label non vu dans corpus_dev, 
		 * ce sera obligatoirement une erreur de classification.
		 */
		processLabel(corpus_dev);

		
		/*
		 * 2. FeatureGenerator avec params : modelGraph, nombre de labels
		 * 
		 * Note : 
		 * C'est au travers de FeatureGenerator que l'on va implémenter
		 * nos features. 
		 * 
		 * Voir classe "FeatureGenerator_Labelisation" pour définir/modifier
		 * les features.
		 * 
		 * Ici : 
		 * Une séquence est un document 
		 * Un segment est un chunk
		 */
		featureGen = new FeatureGenerator_Labelisation(modelGraphType, nlabels);
		
		
		/*
		 * 3. CRF avec params : FeatureGen.nb d'états, FeatureGen, options
		 * 
		 * options : 
		 * - maxIters : nombre total d'itérations pour la maximisation du log-likelihood 
		 * - debugLvl : booléen si debug ou non. 
		 * - etc. (voir JavaDoc)
		 */
		crfModel = new CRF(featureGen.numStates(), featureGen, options);

		/*
		 * 4. Training Set (corpus_dev) avec utilisation d'un DataIter
		 * 
		 * Note : 
		 * Un DataIter est un Itérateur sur un DataSet. 
		 * Un DataSet est un ensemble de 'Document' (ou 'FlatDocument') 
		 * DataIter utilise, en interne, DataSequence. 
		 * DataSequence représente un document, càd une
		 * séquence de segment/chunk Un segment est un chunk
		 */
		CorpusDataIter corpus_dev_iter = new CorpusDataIter(corpus_dev);

		/*
		 * 5. FeatureGenImpl.train()
		 * 
		 * Note : 
		 * Création d'un dictionnaire comptant les fréquences de chacun
		 * des features. Ce calcul des fréquences se fait "absolument" (au
		 * travers de tout le corpus), ou "relativement" (à un moment donné dans
		 * le parcours de la séquence).
		 * 
		 * Params : DataIter
		 */
		featureGen.train(corpus_dev_iter);

		
		/*
		 * 6. CRF.train()
		 * 
		 * Note : 
		 * A partir des informations contenues dans FeatureGenImpl (avec le dico), 
		 * CRF.train() applique une méthode d'optimisation (LBFGS)
		 * pour maximiser le log-likelihood de la 
		 * distribution conditionnelle p(y|x)
		 * 
		 * Ajout d'un objet Evaluator pour avoir l'accuracy à chaque itération
		 * dans la maximisation du log-likelihood.
		 */
//		this.addIntLabels(corpus_evaluator);
//		InternalEvaluator evaluator = new InternalEvaluator(featureGen, options, corpus_evaluator);
		double [] lambda = crfModel.train(corpus_dev_iter);

		/*
		 * 7. Ecriture des Features dans ./data/Output/features.txt 
		 * 8. Ecriture du modèle dans ./data/Output/crf_Model.txt
		 * 9. Ecrtire de la learning curve
		 */
//		featureGen.write(outputFolder + featureGen_output);
//		crfModel.write(outputFolder + crfModel_output);
//		evaluator.writeHistory(outputFolder + "learning_curve.csv");

	}

	/**
	 * test()
	 * 
	 * @param corpus_test
         * @param outputFolder
	 * @throws IOException
	 * Test du modèle sur un corpus de test.
	 * Retourne un double qui est l'accuracy du test.
	 */
	public ArrayList<Chunk_PDF> predict(Corpus_PDF corpus_test, String outputFolder) throws IOException {
		if (debugMode) {
			System.out.println("\nDémarrage de la prédiction test ...\n ");
		}
//                FileWriter matConfWriter = new FileWriter(outputFolder + "confusion_mat.csv");
//                FileWriter statsWriter = new FileWriter(outputFolder + "overall_stats.csv");
		
		/**
		 * Etapes dans la prédiction test d'un modèle 
		 * 0. Traduire 'Document' en 'FlatDocument' + pre-process
		 * 1. addIntLabels : on transforme les labels en int selon
		 * l'association définie lors de l'entraînement 
		 * 2. On définit un nouvel DataIter sur le corpus_test 
		 * 3. CRF.apply() : le CRF prédit les labels des 'segments' 
		 * (chunk) au sein des documents de corpus_test 
		 * 4. On évalue la performance.
		 */
		
		/*
		 * 0. Traduire 'Document' en 'FlatDocument' + pre-process
		 */
		mergeLabel(corpus_test);
		processInformationAboutDocChunk(corpus_test);

		/*
		 * 1. addIntLabels : labels -> int
		 */
		this.addIntLabels(corpus_test);

		/*
		 * 2. DataIter sur corpus_test
		 */
		CorpusDataIter corpus_test_iter = new CorpusDataIter(corpus_test);

		/*
		 * 3. prédiction : CRF.apply(Document)
		 */
		ArrayList<Integer> trueLabels = new ArrayList<Integer>();
		ArrayList<Integer> predictLabels = new ArrayList<Integer>();
		ArrayList<Chunk_PDF> listPredictChunk = new ArrayList<Chunk_PDF>();
		
		while (corpus_test_iter.hasNext()) {
			Document_PDF d = (Document_PDF) corpus_test_iter.next();

			for (Page_PDF currP : d.pages) {
				for (Chunk_PDF currChunk : currP.groupes) {
//	ATTENTION				currChunk.setTrueTag(currChunk.type_int+"");
					currChunk.setTrueTag(currChunk.type);
					trueLabels.add(currChunk.type_int);
				}
			}

			
			crfModel.apply(d);
			featureGen.mapStatesToLabels(d);

			for (Page_PDF currP : d.pages) {
				for (Chunk_PDF currChunk : currP.groupes) {
					predictLabels.add(currChunk.type_int);
//	ATTENTION				currChunk.setPredictTag(currChunk.type_int+"");
					int predictInt = currChunk.type_int;
					
					String predictLab = intMapLabel.get(predictInt);
					currChunk.setPredictTag(predictLab);
					listPredictChunk.add(currChunk);
				}
			}
		}
		// Vérification naïve :
		if (trueLabels.size() != predictLabels.size()) {
			System.err.println("Erreur dans la prédiction");
		}

		return listPredictChunk;

	}
	
	/**
	 * mergeLabel()
	 * @param corpus
	 * 
	 * Fusionne les labels selon HashMap<String, String> labelsToMerge 
	 */
	public void mergeLabel(Corpus_PDF corpus){
		
		for (Document_PDF currDocument : corpus.getListdoc()) {
			List<Page_PDF> pageList = currDocument.pages;
			for (Page_PDF currPage : pageList) {
				List<Chunk_PDF> chunkList = currPage.groupes;
				for (Chunk_PDF currChunk : chunkList) {
					if(labelsToMerge.containsKey(currChunk.type)){
						currChunk.type = labelsToMerge.get(currChunk.type);
					}
					else{
//						System.out.println("non contenu pour:" + currChunk.type);
					}
				}
			}
		}
	}
	
	
	/**
	 * processInformationAboutDocChunk()
	 * 
	 * Pour chacun des documents et des chunk du corpus
	 * assigne des informations tels que le mode de la taille
	 * de police, les marges, etc.
	 * 
	 * Idem pour les chunks.
	 * 
	 * Toutes ces informations vont être utilisées dans les features.
	 * Elles sont calculés ici pour éviter un coût computationel trop
	 * élevé lors du calcul des features.
	 */
	public void processInformationAboutDocChunk(Corpus_PDF corpus){
		
		// Assigne le mode pour le document
		for (Document_PDF currDocument : corpus.getListdoc()) {
			currDocument.assignModeTaillePolice();
			currDocument.assignModeMargeGauche();
			currDocument.assignModeMargeDroite();
                        currDocument.assignMoyMargeHaut();
		}
		
	}

	/**
	 * processLabel()
	 * 
	 * @param corpus_dev
	 * @return Assigne un int à chaque label trouvé dans le corpus de dev Et
	 *         permet de retrouver à quel label est associé chaque int au
	 *         travers de : labelMapInt au travers de : intMapLabel
	 */
	public void processLabel(Corpus_PDF corpus_dev) {

		// liste de labels
		ArrayList<String> labelList = new ArrayList<String>();

		// Parcours des documents du corpus et ajout des labels trouvés dans
		// labelList
		for (Document_PDF currDocument : corpus_dev.getListdoc()) {
			List<Page_PDF> pageList = currDocument.pages;
			for (Page_PDF currPage : pageList) {
				List<Chunk_PDF> chunkList = currPage.groupes;
				for (Chunk_PDF currChunk : chunkList) {
					labelList.add(currChunk.type);
				}
			}
		}

		// Suppression des doublons dans labelList
		List<String> labelList_uniq = new ArrayList<String>(
				new HashSet<String>(labelList));
		// Sort alphabétique de la liste de labels
		Collections.sort(labelList_uniq);

		// Remplissage des maps d'association label-int, int-label
		// Les labels commencent à '0'.
		int int_label = 0;
		for (String currLabel : labelList_uniq) {
			labelMapInt.put(currLabel, int_label);
			intMapLabel.put(int_label, currLabel);
			int_label++;
		}

		// Lecture de la map d'association label-int
		if (debugMode) {
			System.out.println("# Association labelMapInt : ");
			System.out.println("# Size : " + labelMapInt.size());
			for (String label : labelList_uniq) {
				System.out.println(label + "\t" + labelMapInt.get(label));
			}
			System.out.println();
		}

		// Assignation de ces labels 'int' dans le corpus
		this.addIntLabels(corpus_dev);

		// Assignation du nombre total de labels
		nlabels = labelMapInt.size();
	}

	/**
	 * addIntLabels()
	 * 
	 * @param corpus
	 *            Ajoute aux chunks du corpus donné en params le int_label
	 *            associé à son label (string). Utilisation d'une map
	 *            d'association, calculée dans processLabel()
	 */
	public void addIntLabels(Corpus_PDF corpus) {

		int nb_chunk = 0;
		for (Document_PDF currDocument : corpus.getListdoc()) {
			List<Page_PDF> pageList = currDocument.pages;
			for (Page_PDF currPage : pageList) {
				List<Chunk_PDF> chunkList = currPage.groupes;
				for (Chunk_PDF currChunk : chunkList) {
					String label = currChunk.type;
					
					// Si le label a été vu dans le corpus_dev
					// alors on lui assigne le 'int' associé.
					// Si label non vu, alors on lui assigne par défaut "other"
					// Par défaut le label est 99, càd, malformé.
					int label_int=99;
					if(labelMapInt.containsKey(label)){
						label_int = labelMapInt.get(label);
					}
					else{
//						label_int = labelMapInt.get("other");
						label_int = 99;
					}
					currChunk.type_int = label_int;
					nb_chunk++;
				}
			}
		}
		if (debugMode) {
			System.out
					.println("# addIntLabels : assignation des int_labels dans le corpus");
			System.out.println("# nb de chunk traités : " + nb_chunk);
			System.out.println();
		}
	}


}
