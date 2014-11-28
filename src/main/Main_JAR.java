package main;

import java.io.IOException;
import java.net.UnknownHostException;
import org.melodi.objectslogic.Document_Lara;


public class Main_JAR {

	/**
	 * Main class for jar. Use 'ant dist' to build a new jar.
	 * 
	 * @param args
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InterruptedException
	 * @author Jean-Philippe Fauconnier
	 */

	public static void main(String[] args) {
		
		/* default values */
		String bdd_host = "localhost";
		String bdd_name = "name";
		String bdd_user = "user";
		String bdd_pwd = "pwd";
		String format = "";
		String path = "";

		/* dirty quick solution to parse args */
		for (String currArgs : args) {
			if (currArgs.matches("format=(.)*")) {
				format = currArgs.substring(7, currArgs.length());
			} else if (currArgs.matches("file=(.)*")) {
				path = currArgs.substring(5, currArgs.length());
			} else {
				printUsage();
			}
		}

		if (args.length == 0) {
			printUsage();
		} else {
			try {
				run(format, path, bdd_host, bdd_name, bdd_user, bdd_pwd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	

	static void run(String format, String path, String bdd_host, String bdd_name, String bdd_user, String bdd_pwd) throws Exception {

		LATOE_client latoe_client = new LATOE_client();
		Document_Lara currDocument = new Document_Lara();

		if (format.equals("wiki-text")) {
			currDocument = latoe_client.runMediaWiki(path);
		} else if (format.equals("wiki-bdd")) {
			currDocument = latoe_client.runMediaWiki_BDD(path, bdd_host, bdd_name, bdd_user, bdd_pwd);
		} else if (format.equals("pdf")) {
			currDocument = latoe_client.runPDF(path);
		}else if(format.equals("html")){
			currDocument = latoe_client.runHTML(path);
		}
		if(currDocument.getChunk().size() > 0){
			latoe_client.extractTextObject(currDocument);
			latoe_client.writeDocument(currDocument); 
		}
		
	}

	static void printUsage() {
		/* usage */
		System.err.println("Usage:\njava -jar latoe-0.x.x.jar "
				+ "format=[*wiki-text|wiki-bdd|html|pdf] file=[path or name in bdd] "
				+ "\n\t* indicates default values for args.");
		System.exit(1);
	}
}
