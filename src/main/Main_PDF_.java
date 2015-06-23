package main;

import org.melodi.reader.service.Writer_Service;

import logicalobjects.Document_Lara;

public class Main_PDF_ {
	
	public static void main(String [] args) throws Exception{
		
		LATOE_client latoe_client = new LATOE_client();
		Document_Lara currDocument = latoe_client.runPDF("./data/atala/taln-2015-long-001.pdf", "./models/crf.bin", "", false);
		
		System.out.println(currDocument.toString());
		
		Writer_Service reader_service = new Writer_Service();
		reader_service.writeHTML("./output/", "UTF-8", currDocument);
	}

}
