package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.PropertyConfigurator;
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
import de.tudarmstadt.ukp.wikipedia.datamachine.domain.JWPLDataMachine;

public class LATOE_client {
	
	public LATOE_client(){
		
	}
	
	public Document_Lara runMediaWiki(String path)
			throws UnsupportedEncodingException, FileNotFoundException,
			WikiInitializationException {

		System.out.println("Module 1 : Document Processing MediaWiki text");
		Wikipedia_Service wiki_service = new Wikipedia_Service();
		Document_Lara currDocument = wiki_service.getDocumentFromText(path);

		return currDocument;
	}
	
	
	public Document_Lara runMediaWiki_BDD(String name, String bdd_host, String bdd_name, String bdd_user, String bdd_pwd)
			throws WikiApiException {

		System.out.println("Module 1 : Document Processing MediaWiki BDD");
		Wikipedia_Service wiki_service = new Wikipedia_Service();
		wiki_service.connect(bdd_host, bdd_name, bdd_user, bdd_pwd); // BDD connexion
		Document_Lara currDocument = wiki_service.getDocument(name);

		return currDocument;
	}
	
	
	public Document_Lara runHTML(String path) throws IOException {
	
		System.out.println("Module 1 : Document Processing HTML");
		HTML_Service html_service = new HTML_Service();
		Document_Lara currDocument = html_service.getDocument(path);
	
		return currDocument;
	}

	public Document_Lara runPDF(String path, String inputModel, String pathRules, String onlyRules) throws Exception {
		
		System.out.println("Module 1 : Document Processing PDF");
		Document_Lara currDocument = new Document_Lara();
		PDF_Service pdf_service = new PDF_Service();
		currDocument = pdf_service.getDocument(path, inputModel, pathRules, onlyRules);
	
		return currDocument;
	}

	public void trainModelPDF(String corpus_train, String output_model) throws Exception{
		
		System.out.println("Train model");
		PDF_Service pdf_service = new PDF_Service();
		pdf_service.trainNewModel(corpus_train, output_model);
		
	}
	
	public void deployBDD(String dir, String lang){
		
		System.out.println("Transformation for a new Wikipedia dump");
		String log4jConfPath = "./resources/log4j_database.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		JWPLDataMachine dataMachine = new JWPLDataMachine();
		String[] myargs = new String[4];
		
		if(lang.equals("fr")){
			myargs[0] = "french";
			myargs[1] = "Accueil";
			myargs[2] = "Homonymie";
			myargs[3] = dir;
		}
		else if(lang.equals("en")){
			myargs[0] = "english";
			myargs[1] = "Content";
			myargs[2] = "Disambiguation_pages";
			myargs[3] = dir;
		}
		dataMachine.main(myargs);
	}

	
	
	public void runBlockifyPDF(String path) throws Exception{
		System.out.println("Blockify PDF");
		PDF_Service pdf_service = new PDF_Service();
		pdf_service.blockifyPDF(path);
	}
	
	
	
	
	
	public void extractTextObject(Document_Lara currDocument) throws FileNotFoundException {

		System.out.println("Module 2 : Object Detection");
		HierarchicalStructure_Detection es_detection = new HierarchicalStructure_Detection();
		
		PrintWriter out = new PrintWriter(new File("./output/"+currDocument.getName()+"_objects.txt"));
		
		for(Unit currUnit : es_detection.getES(currDocument)){
			out.print(currUnit.toString());
			out.flush();
		}
		out.close();
	}
	
	public void writeDocument(Document_Lara currDocument) {
		
		/**
		 * Reader
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
