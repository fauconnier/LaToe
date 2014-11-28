package org.melodi.reader.service;

import java.util.regex.Matcher;

import org.melodi.reader.larat.controler.LaratControler;
import org.melodi.reader.larat.model.LaratModel;
import org.melodi.reader.larat.view.LaratView;

public class Reader_Larat {
	String docname;
	int index_doc;
	
	
	public Reader_Larat(String docname, int index_doc){
		this.docname=docname;
		this.index_doc=index_doc;
	}
	
public void run(){
		
		// Input
		String doc = docname;
		int index = index_doc;
		
		/**
		 *  Model–view–controller
		 */
		LaratModel laratModel = new LaratModel();
		LaratControler laratControler = new LaratControler(laratModel);
		LaratView laratInterface = new LaratView(laratControler);
		laratModel.addObserver(laratInterface);
		
		/**
		 * Do something
		 */
		laratModel.openFile("./data/LARA_corpus/"+doc+".html");
		laratModel.selectUnit(index);
	}
	
	
	public static void main(String[] args) {
		
		String docname = "";
		int index = 99;
		
		if(args.length == 0){
			printUsage();
		}
		
		for (String currArgs : args) {
			if (currArgs.matches("doc=(.)*")) {
				docname = currArgs.substring(4, currArgs.length());
			} else if (currArgs.matches("id=([0-9])*")) {
				index = Integer
						.parseInt(currArgs.substring(3, currArgs.length()));
			} else {
				printUsage();
			}
		}
		
		Reader_Larat currReader = new Reader_Larat(docname, index);
		currReader.run();
	}
	
	
	static void printUsage() {
		/* usage */
		System.err.println("Usage:\njava -jar reader-service-1.x.x.jar "
				+ "doc=[docname.html] id=[0-9]");
		System.exit(1);
	}

}
