package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.latoe.layoutanalysis.html.HTML_Service;
import org.latoe.layoutanalysis.pdf.PDF_Service;
import org.latoe.layoutanalysis.wikipedia.Wikipedia_Service;
import org.latoe.textobject.HierarchicalStructure_Detection;
import org.melodi.objectslogic.Document_Lara;
import org.melodi.reader.larat.controler.LaratControler;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.reader.larat.model.LaratModel;
import org.melodi.reader.larat.view.LaratView;
import org.melodi.reader.service.Reader_Service;

import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;

public class Main_Example {

	public static void main(String[] args) {

		Document_Lara currDocument = new Document_Lara();

		/**
		 * Wikipedia example Need BDD
		 */
		String bdd_host = "localhost";
		String bdd_name = "FRWIKISEPTEMBER";
		String bdd_user = "root";
		String bdd_pwd = "killer";
		try {
			currDocument = runWikipedia("Barrage", bdd_host, bdd_name, bdd_user, bdd_pwd);
		} catch (WikiApiException e) {
			e.printStackTrace();
		}

		/**
		 * Mediawiki example
		 */
		try {
			currDocument = runMediaWiki("./samples/sample.mediawiki");
		} catch (WikiInitializationException | UnsupportedEncodingException
				| FileNotFoundException e1) {
			e1.printStackTrace();
		}

		/**
		 * HTML example
		 */
		try {
			currDocument = runHTML("./samples/sample.html");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * PDF example
		 */
		try {
			currDocument = runPDF("./samples/sample.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}

		/**
		 * Write document
		 */
		writeDocument(currDocument);

		/**
		 * Extract Hierarchical structures
		 */
		HierarchicalStructure_Detection es_detection = new HierarchicalStructure_Detection();
		ArrayList<Unit> units = es_detection.getES(currDocument);

		
	}

	public static Document_Lara runHTML(String path) throws IOException {

		/**
		 * Module 1 : Document Processing : HTML
		 */
		HTML_Service html_service = new HTML_Service();
		Document_Lara currDocument = html_service.getDocument(path);

		return currDocument;
	}

	public static Document_Lara runPDF(String path) throws Exception {

		/**
		 * Module 1 : Document Processing : PDF
		 */
		PDF_Service pdf_service = new PDF_Service();
		
		/*
		 *  Train new Model
		 */
//		pdf_service.trainNewModel("./data/training_label_layout/LING_GEOP/", "./models/crf.bin");
		
		/*
		 *  Prediction
		 */
		Document_Lara currDocument = pdf_service.getDocument(path, "./models/crf.bin");

		return currDocument;
	}

	public static Document_Lara runMediaWiki(String path)
			throws UnsupportedEncodingException, FileNotFoundException,
			WikiInitializationException {

		/**
		 * Module 1 : Document Processing : MediaWiki
		 */
		System.out.println("Module 1 : Document Processing");
		Wikipedia_Service wiki_service = new Wikipedia_Service();
		Document_Lara currDocument = wiki_service.getDocumentFromText(path);

		return currDocument;
	}

	public static Document_Lara runWikipedia(String name, String bdd_host, String bdd_name, String bdd_user, String bdd_pwd)
			throws WikiApiException {

		/**
		 * Module 1 : Document Processing : Wikipedia BDD
		 */
		System.out.println("Module 1 : Document Processing");
		Wikipedia_Service wiki_service = new Wikipedia_Service();
		wiki_service.connect(bdd_host, bdd_name, bdd_user, bdd_pwd); // BDD connexion
		Document_Lara currDocument = wiki_service.getDocument(name);

		return currDocument;
	}


	
	
	public static void extractTextObject(Document_Lara currDocument) {

		/**
		 * Module 2 : Text Object Detection
		 */
		System.out.println("Module 2 : Object Detection");
		HierarchicalStructure_Detection es_detection = new HierarchicalStructure_Detection();
		es_detection.getES(currDocument);

	}

	
	
	
	public static void writeDocument(Document_Lara currDocument) {
		
		/**
		 * Reader
		 * 
		 */
		Reader_Service reader_service = new Reader_Service();

		// HTML and LARAt
		reader_service.writeHTML("./output/", "UTF-8", currDocument);

		// Tree SVG (with max 5 characters in each node)
		reader_service.writeSVG("./output/", currDocument, 5);

		// AA and Glozz
		try {
			reader_service.writeGlozz("./output/", currDocument);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		// Serialise Document
	}

	private static LaratModel startLaratTool() {
		LaratModel laratModel = new LaratModel();
		laratModel.setEncoding("UTF-8");
		LaratControler laratControler = new LaratControler(laratModel);
		LaratView laratInterface = new LaratView(laratControler);
		laratModel.addObserver(laratInterface);
		return laratModel;
	}
}
