package org.melodi.reader.larat.main;

import org.melodi.reader.larat.controler.LaratControler;
import org.melodi.reader.larat.model.LaratModel;
import org.melodi.reader.larat.view.LaratView;

public class Larat_Main {

	public static void main(String[] args) {
		
		/**
		 *  Model–view–controller
		 */
		LaratModel laratModel = new LaratModel();
//		laratModel.setEncoding("UTF-8");
		
		LaratControler laratControler = new LaratControler(laratModel);
		LaratView laratInterface = new LaratView(laratControler);
		laratModel.addObserver(laratInterface);
		
		
	}

}
