/*******************************************************************************
 * Copyright (c) 2010 Torsten Zesch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * 
 * Contributors:
 *     Torsten Zesch - initial API and implementation
 ******************************************************************************/
package de.tudarmstadt.ukp.wikipedia.parser.mediawiki;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tudarmstadt.ukp.wikipedia.parser.Link;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Template;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiContentElementParser;

/**
 * This is the TemplateParser for the french language, with special treatment
 * for all the french templates, like "-s (siècle)" and others.
 * 
 * @author CJacobi
 * 
 */
public class FrenchTemplateParser implements MediaWikiTemplateParser {

	private final Log logger = LogFactory.getLog(getClass());

	private final String templatePrefix = "TEMPLATE[";
	private final String templatePostfix = "]";
	private final String parameterDivisor = ", ";
	private final String templateNotImplementedPrefix = "TEMPLATE NOT IMPLEMENTED[";
	private final String templateNotImplementedPostfix = "]";
	private final String emptyLinkText = "[ ]";

	// private MediaWikiContentElementParser parser;
	private List<String> deleteTemplates;
	private List<String> parseTemplates;
	private HashMap<String, String> parsing;

	public FrenchTemplateParser(MediaWikiContentElementParser parser,
			List<String> deleteTemplates, List<String> parseTemplates) {
		this.deleteTemplates = deleteTemplates;
		this.parseTemplates = parseTemplates;
		
		// this.parser = parser;
		
		/**
		 * Adding code
		 */
		String base = "./configuration/MediaWikiParsing/";
		try {
			parsing = getParseText(base	+ "parser_templates.txt", false, true);
		} catch (FileNotFoundException e) {
			System.err.println("Erreur lors de la lecture de parser_templates.fr");
			e.printStackTrace();
		}
	}

	public String configurationInfo() {
		StringBuilder result = new StringBuilder();
		result.append("Standard Template treatment: ShowNameAndParameters");
		result.append("\nDelete Templates: ");
		for (String s : deleteTemplates) {
			result.append("\"" + s + "\" ");
		}
		result.append("\nParse Templates: ");
		for (String s : parseTemplates) {
			result.append("\"" + s + "\" ");
		}
		return result.toString();
	}

	public ResolvedTemplate parseTemplate(Template t, ParsedPage pp) {

		final String templateName = t.getName();
		// Show Name and Parameters as Standart treatment.
		ResolvedTemplate result = new ResolvedTemplate(t);
		result.setPreParseReplacement(ResolvedTemplate.TEMPLATESPACER);
		StringBuilder sb = new StringBuilder();
		sb.append(templatePrefix);
		sb.append(t.getName() + parameterDivisor);
		for (String s : t.getParameters()) {
			sb.append(s + parameterDivisor);
		}
		sb.delete(sb.length() - parameterDivisor.length(), sb.length());
		sb.append(templatePostfix);
		result.setPostParseReplacement(sb.toString());

		result.setParsedObject(t);

		/**
		 * 1 DeleteTempaltes : On enlève tout
		 */
		for (String s : deleteTemplates) {
			s = s.toLowerCase();
			String templateName_lower = templateName.toLowerCase();
			
			if (templateName_lower.matches(s)) {
				result.setPostParseReplacement("");
				result.setParsedObject(null);
				return result;
			}
		}
		
		
		/**
		 * 2. Parse Template
		 */
//		System.out.println(templateName);
		String templateName_lower = templateName.toLowerCase();
		
		for(String s : parseTemplates){
			s = s.toLowerCase();
			if(templateName_lower.matches(s)){
				
				List<String> templateParameters = t.getParameters();
				
				/**
				 * Template to Parse French
				 */
				if(parsing.containsKey(templateName)){
					String value = parsing.get(templateName);
//					System.out.println("TemplateName:"+templateName);
//					System.out.println("TemplateValue:"+value);
					
					String [] args = value.split(" ");
					
					String remplacement = "";
					
					
					for(int i=0;i< args.length;i++){
						String currArg = args[i];
						
						if(currArg.equals("all")){
							// On prend ce qui est écrit dans le texte
							for(int j=0;j<templateParameters.size();j++){
								remplacement += templateParameters.get(j) + " ";
							}
							if(remplacement.equals("")){
								remplacement = templateName;
							}
//							System.out.println("Remplacement:"+remplacement);
						}
						// simple index (e.g: TEMPLATE 1 2 4  5)
						else if(currArg.matches("[0-9]+")){
							int index_params = Integer.parseInt(currArg);
							
							if(index_params < templateParameters.size()){
								remplacement += templateParameters.get(index_params) + " ";
							}
						}
						else{
							
							// On prend ce qui est écrit dans parser_templates.txt
							remplacement += currArg + " ";
						}
					
					}
					
//					System.out.println(templateName_lower);
//					System.out.println("Remplacement:"+remplacement);
//					System.out.println();
					
					result.setPostParseReplacement(remplacement);
					result.setParsedObject(null);
					return result;
				}
			}
		}
		
		
		/**
		 * 3. Non delete and non parsé
		 */
		
//		System.out.println("#########"+templateName);
		String remplacement = "";
		List<String> templateParameters = t.getParameters();
		for(int j=0;j<templateParameters.size();j++){
			remplacement += templateParameters.get(j) + " ";
		}
		if(remplacement.equals("")){
			remplacement = templateName;
		}
		result.setPostParseReplacement(remplacement);
		result.setParsedObject(null);
		return result;
		
		
//		/**
//		 * 2 ParseTemplate
//		 */
//
//		for (String s : parseTemplates) {
//
//			s = s.toLowerCase();
//			String templateName_lower = templateName.toLowerCase();
//			
//			List<String> templateParameters = t.getParameters();
//
//			if (s.equals(templateName_lower)) {
//				logger.info("ParseTemplate: " + templateName);
//
//				/**
//				 * Template to Parse French
//				 */
//				if(parsing.containsKey(templateName)){
//					String value = parsing.get(templateName);
////					System.out.println("TemplateName:"+templateName);
////					System.out.println("TemplateValue:"+value);
//					
//					String [] args = value.split(" ");
//					
//					String remplacement = "";
//					
//					
//					for(int i=0;i< args.length;i++){
//						String currArg = args[i];
//						
//						
//						if(currArg.equals("all")){
//							// On prend ce qui est écrit dans le texte
//							for(int j=0;j<templateParameters.size();j++){
//								remplacement += templateParameters.get(j) + " ";
//							}
//							if(remplacement.equals("")){
//								remplacement = templateName;
//							}
////							System.out.println("Remplacement:"+remplacement);
//						}
//						// simple index (e.g: TEMPLATE 1 2 4  5)
//						else if(currArg.matches("[0-9]+")){
//							int index_params = Integer.parseInt(currArg);
//							
//							if(index_params < templateParameters.size()){
//								remplacement += templateParameters.get(index_params) + " ";
//							}
//						}
//						else if(currArg.matches("[a-z]*")){
//							
//							// On prend ce qui est écrit dans parser_templates.txt
//							remplacement += currArg + " ";
//						}
//					
//					}
//					
//					result.setPostParseReplacement(remplacement);
//					result.setParsedObject(null);
//					return result;
//				}
////				if (templateName.equals("lang")) {
////
////					result.setPostParseReplacement(templateParameters.get(1));
////					result.setParsedObject(null);
////					return result;
////
////				} else if (templateName.equals("s-")) {
////					result.setPostParseReplacement(templateParameters.get(0)
////							+ " siècle");
////					result.setParsedObject(null);
////					return result;
////				} 
//				
////				else if (templateName.equals("Xe siècle")) {
////					result.setPostParseReplacement("Xe siècle");
////					result.setParsedObject(null);
////					return result;
////				} else if (templateName.equals("XIe siècle")) {
////					result.setPostParseReplacement("XIe siècle");
////					result.setParsedObject(null);
////					return result;
////				} else if (templateName.equals("XIIe_siècle")) {
////					result.setPostParseReplacement("XIIe siècle");
////					result.setParsedObject(null);
////					return result;
////				} else if (templateName.equals("VXIIIe_siècle")) {
////					result.setPostParseReplacement("XIIIe siècle");
////					result.setParsedObject(null);
////					return result;
////				} else if (templateName.equals("XIVe_siècle")) {
////					result.setPostParseReplacement("XIVe siècle");
////					result.setParsedObject(null);
////					return result;
////				} else if (templateName.equals("XVe_siècle")) {
////					result.setPostParseReplacement("XVe siècle");
////					result.setParsedObject(null);
////					return result;
////				} else if (templateName.equals("XVIe_siècle")) {
////					result.setPostParseReplacement("XVI siècle");
////					result.setParsedObject(null);
////					return result;
////				} else if (templateName.equals("XVIIe_siècle")) {
////					result.setPostParseReplacement("XVIIe siècle");
////					result.setParsedObject(null);
////					return result;
////				} else if (templateName.equals("XVIIIe_siècle")) {
////					result.setPostParseReplacement("XVIIIe siècle");
////					result.setParsedObject(null);
////					return result;
////				} else if (templateName.equals("XIXe_siècle")) {
////					result.setPostParseReplacement("XIX siècle");
////					result.setParsedObject(null);
////					return result;
////				} else if (templateName.equals("XXe_siècle")) {
////					result.setPostParseReplacement("XX siècle");
////					result.setParsedObject(null);
////					return result;
////				}
//			}
//			
////			else{
////				System.out.println("NO MATCH:"+s);
////				System.out.println();
////				
////				String remplacement = "";
////				for(int j=0;j<templateParameters.size();j++){
////					remplacement += templateParameters.get(j) + " ";
////				}
////				if(remplacement.equals("")){
////					remplacement = templateName;
////				}
////				
////				result.setPostParseReplacement(remplacement);
////				result.setParsedObject(null);
////				return result;
////			}
//		}
	}

	public HashMap<String, String> getParseText(String path, boolean print,
			boolean normalize) throws FileNotFoundException {

		HashMap<String, String> parsing = new HashMap<String, String>();

		String filePath = path;
		Scanner scanner = new Scanner(new File(filePath));

		// On boucle sur chaque champ detecté
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			line = line.trim();

			if (!line.startsWith("#") && line.length() > 0) {

				String name = line.replaceAll("\\[(.*)\\]", "");
				String value = line.substring(name.length());
				value = value.replaceAll("\\[","");
				value = value.replaceAll("\\]","");

				if (print) {
					System.out.println(name + "\t" + value);
				}
				parsing.put(name, value);

				if (normalize) {
					if (print) {
						System.out.println(name.toUpperCase() + "\t" + value);
						System.out.println(name.toLowerCase() + "\t" + value);
					}
					parsing.put(name.toUpperCase(), value);
					parsing.put(name.toLowerCase(), value);
				}
			}
			// faites ici votre traitement
		}

		scanner.close();

		return parsing;
	}
}
