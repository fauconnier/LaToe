package org.melodi.reader.larat.tools;

import org.melodi.reader.larat.controler.LaratControler;
import org.melodi.reader.larat.model.LaratModel;
import org.melodi.reader.larat.view.LaratView;


public class PrinterAnnotation {
	
	String docname;
	int index_doc;
	String annotateur;
	
	public PrinterAnnotation(String annotateur, String docname, int index_doc){
		this.docname=docname;
		this.index_doc=index_doc;
		this.annotateur=annotateur;
	}
	
	public void run(){
		
		// Input
		String doc = docname;
		int index = index_doc;
		String annotateur_toprint = annotateur;
		
		/**
		 *  Model–view–controller
		 */
		LaratModel laratModel = new LaratModel();
		LaratControler laratControler = new LaratControler(laratModel);
		LaratView laratInterface = new LaratView(laratControler);
		laratModel.addObserver(laratInterface);
		
		laratInterface.setTitle(annotateur_toprint + " " + docname + " " + index);
		/**
		 * Do something
		 */
		
		if(annotateur_toprint.equals("consensus")){
			laratModel.openFile("/home/jfaucon/workspace/corpus-LARA/corpus/LARA_corpus/"+doc+".html");
		}
		else{
	//		laratModel.openFile("/home/jfaucon/Thesis/Data/LARAt_Consensus/Consensus/LARA_corpus/"+doc+".html");
			laratModel.openFile("/home/jfaucon/Thesis/Data/LARAt_Corpus/Annotation/"+annotateur_toprint+"/LARA_corpus/"+doc+".html");
		}
		laratModel.selectUnit(index);
	}


}
