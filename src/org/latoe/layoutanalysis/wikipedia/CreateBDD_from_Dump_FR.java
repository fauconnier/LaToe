package org.latoe.layoutanalysis.wikipedia;

import org.apache.log4j.PropertyConfigurator;

import de.tudarmstadt.ukp.wikipedia.datamachine.domain.*;

public class CreateBDD_from_Dump_FR {

	public static void main(String[] args) {
		
		String log4jConfPath = "./log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		// TODO Auto-generated method stub
		JWPLDataMachine ne = new JWPLDataMachine();
		
		String[] myargs = new String[4];
		myargs[0] = "french";
		myargs[1] = "Accueil";
		myargs[2] = "Homonymie";
		myargs[3] = "/media/dd/wikipedia/20140928/";
		ne.main(myargs);

	}

}
