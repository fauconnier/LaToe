package main;

import java.io.IOException;
import java.net.UnknownHostException;

import logicalobjects.Document_Lara;


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
		
		
		/* default values for bdd */
		String bdd_host = "localhost";
		String bdd_name = "name";
		String bdd_user = "user";
		String bdd_pwd = "pwd";
	
		/* values for deploy bdd */
		String dir = "";
		String lang= "fr";
		
		/* default values */
		String format = "wiki-text";
		String path = "";
		String command = "analyse";
		String outputModel="./models/newmodel.bin";
		String inputModel="./models/crf.bin";
		String pathRules = "./configuration/PDFRules/rules.drl";
		Boolean onlyRules = false;

		/* dirty quick solution to parse args */
		for (String currArgs : args) {
			if (currArgs.matches("format=(.)*")) {
				format = currArgs.substring(7, currArgs.length());
			} else if(currArgs.matches("command=(.)*")){
				command = currArgs.substring(8, currArgs.length());
			} else if (currArgs.matches("file=(.)*")) {
				path = currArgs.substring(5, currArgs.length());
			} else if (currArgs.matches("dir=(.)*")) {
				dir = currArgs.substring(4, currArgs.length());
			}  else if (currArgs.matches("lang=(.)*")) {
				lang = currArgs.substring(5, currArgs.length());
			} else if(currArgs.matches("outputModel=(.)*")){
				outputModel = currArgs.substring(12, currArgs.length());
			} else if(currArgs.matches("inputModel=(.)*")){
				inputModel = currArgs.substring(11, currArgs.length());
			}else if(currArgs.matches("pathRules=(.)*")){
				pathRules = currArgs.substring(10, currArgs.length());
			} else if(currArgs.matches("onlyRules=(.)*")){
				String onlyRulesString = currArgs.substring(10, currArgs.length());
				if(onlyRulesString.contains("TRUE")){
					onlyRules = true;
				}
			}
			
			else {
				printUsage();
			}
		}
		

		
		if (args.length == 0) {
			printUsage();
		} else {
			try {
				if(command.equals("analyse")){
					runAnalyse(format, path, bdd_host, bdd_name, bdd_user, bdd_pwd, inputModel, pathRules, onlyRules);
				}
				else if(command.equals("deploy-bdd")){
					runDeployBDD(dir, lang);
				}
				else if(command.equals("blockify-pdf")){
					runBlockifyPDF(dir);
				}
				else if(command.equals("train-pdf")){
					runTrainPDF(dir, outputModel);
				}
				else{
					printUsage();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	static void runBlockifyPDF(String path) throws Exception{
		LATOE_client latoe_client = new LATOE_client();
		latoe_client.runBlockifyPDF(path);
	}
	
	static void runTrainPDF(String path, String outputModel) throws Exception{
		LATOE_client latoe_client = new LATOE_client();
		latoe_client.trainModelPDF(path, outputModel);
	}
	

	static void runAnalyse(String format, String path, String bdd_host, String bdd_name, String bdd_user, String bdd_pwd, String inputModel, String pathRules, Boolean onlyRules) throws Exception {

		LATOE_client latoe_client = new LATOE_client();
		Document_Lara currDocument = new Document_Lara();

		if (format.equals("wiki-text")) {
			currDocument = latoe_client.runMediaWiki(path);
		} else if (format.equals("wiki-bdd")) {
			currDocument = latoe_client.runMediaWiki_BDD(path, bdd_host, bdd_name, bdd_user, bdd_pwd);
		} else if (format.equals("pdf")) {
			currDocument = latoe_client.runPDF(path, inputModel, pathRules, onlyRules);
		}else if(format.equals("html")){
			currDocument = latoe_client.runHTML(path);
		}
		if(currDocument.getChunk().size() > 0){
			latoe_client.extractTextObject(currDocument);
			latoe_client.writeDocument(currDocument); 
		}
	}
	
	
	static void runDeployBDD(String dir, String lang){
		LATOE_client latoe_client = new LATOE_client();
		latoe_client.deployBDD(dir, lang);
	}

	
	static void printUsage() {
		/* usage */
		System.err.println("Usage:\njava -jar LaToe-0.x.x.jar "
				+ "command=[*analyse|deploy-bdd|train-pdf]\n"
				
				+ "\nif command=analyse\n"
				+ "format=[*wiki-text|wiki-bdd|html|pdf] file=[path or name in bdd]\n"
				
				+ "\nif command=deploy-bdd\n"
				+ "dir=[path to dir] lang=[*fr|en]\n" 
				
				+ "\nif command=blockify-pdf\n"
				+ "dir=[path to directory of pdf]\n" 
				
				+ "\nif command=train-pdf\n"
				+ "train=[path to training set] outputModel=[path for new model]\n" 
				
				+ "\n\t* indicates default values for args.");
		System.exit(1);
	}
}
