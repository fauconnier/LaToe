package org.latoe.layoutanalysis.wikipedia;

import org.apache.log4j.PropertyConfigurator;

import de.tudarmstadt.ukp.wikipedia.datamachine.domain.*;

public class CreateBDD_from_Dump_EN {

	public static void main(String[] args) {
		
		String log4jConfPath = "./log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		// TODO Auto-generated method stub
		JWPLDataMachine ne = new JWPLDataMachine();
		
		String[] myargs = new String[4];
		myargs[0] = "english";
		myargs[1] = "Content";
		myargs[2] = "Disambiguation_pages";
		myargs[3] = "/media/dd/wikipedia/20140903/";
		ne.main(myargs);

	}

}
