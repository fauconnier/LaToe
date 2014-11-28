package org.melodi.reader.service;

import java.awt.Container;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.demo.SampleTreeFactory;
import org.abego.treelayout.demo.TextInBox;
import org.abego.treelayout.demo.TextInBoxNodeExtentProvider;
import org.abego.treelayout.demo.svg.SVGForTextInBoxTree;
import org.abego.treelayout.demo.swing.TextInBoxTreePane;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.melodi.analyser.yatea_client.service.YateaClient;
import org.melodi.objectslogic.Chunk_Lara;
import org.melodi.objectslogic.Document_Lara;
import org.melodi.objectslogic.LayoutAnnotation;
import org.melodi.objectslogic.Term;
import org.melodi.reader.larat.internal.Concept;
import org.melodi.reader.larat.internal.Item;
import org.melodi.reader.larat.internal.Primer;
import org.melodi.reader.larat.internal.Segment;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.tools.common.IO_Service;
import org.melodi.tools.common.Segment_Tool;
import org.melodi.tools.fuzzymatcher.client.FuzzyMatcher_Client;
import org.melodi.tools.fuzzymatcher.datamodel.Message_Matcher;


public class Reader_Service {

	Element racine;
	org.jdom2.Document document;
	int count_unit;
//	int count_relation;
	public int index_char;
	ArrayList<Element> relations_for_end = new ArrayList<Element>();

	/**
	 * Reader_Service proposer d'aficher un document et ses unités. Plusieurs
	 * "frame" : -logical layout (h1,h2,p,item, etc.) -logical tree (dep, sub,
	 * rien) -unit frame
	 */

	private static boolean printFlag = false;

	public Reader_Service() {

	}

	// public void getHTML(Document_Lara currDocument) throws
	// UnsupportedEncodingException, FileNotFoundException{
	//
	// int index_char = 0;
	// String plainText = "";
	//
	//
	// for(Chunk_Lara currChunk : currDocument.getChunk()){
	//
	// //1. getText and Count Char
	// String chunkText = currChunk.getText();
	// chunkText = chunkText.trim();
	// chunkText = chunkText.replace("\n", "");
	// int length = chunkText.length();
	// int begin = index_char;
	// int end = index_char + length;
	// String type = currChunk.getType();
	// plainText += chunkText;
	// currChunk.setBegin(begin);
	// currChunk.setEnd(end);
	// index_char += length;
	//
	// //2. getLayoutAnnotation
	// LayoutAnnotation layoutAnnotation = new LayoutAnnotation();
	// layoutAnnotation.setBegin(begin);
	// layoutAnnotation.setEnd(end);
	// layoutAnnotation.setHtml_type(type);
	// currChunk.setLayoutAnnotation(layoutAnnotation);
	//
	// }
	// // Ecriture du texte
	// IO_Service io_service = new IO_Service();
	// io_service.writeFile(currDocument.getTitle().replaceAll("\\s", "")+".aa",
	// "UTF-8", plainText);
	//
	//
	//
	// // Tentative de ré-écriture HTML
	// String ReadPlainText =
	// io_service.readFile(currDocument.getTitle().replaceAll("\\s", "")+".aa");
	// String dumpHTML = "";
	//
	// System.out.println(currDocument.toString());
	// Chunk_Lara FirstChunk = currDocument.getChunk_id(0);
	// System.out.println("<h1>" + FirstChunk.toString() + "</h1>"); // ROOT
	// writeRecursiveHTML(ReadPlainText, currDocument, FirstChunk);
	//
	// }
	
	
	
	public void writeHTML(String path, String encoding,
			Document_Lara currDocument) {

		if(printFlag){
			System.out.println("writeHTML");
			
		}
		String body = getHTMLBody(currDocument);

		String start = "<!DOCTYPE html>\n<html>\n";
		String head = "<head>\n " + "<meta charset=\"" + encoding + "\"/>\n"
				+ "" + " </head>\n";

		String html = start + head + "<body>\n" + body + "</body>\n</html>\n";
		if(printFlag){
			System.out.println(html);
		}

		IO_Service io_service = new IO_Service();
		io_service.writeFile(
				path + currDocument.getTitle().replaceAll("\\s", "") + ".html",
				encoding, html);
	}

	public String getHTMLBody(Document_Lara flatDoc) {

		/**
		 * Shift-Reduce
		 */

		String body = "";
		// Algo de shift-reduce

		// ArrayList<String> listRelation_predict = new ArrayList<String>();
		/*
		 * Objets : pile
		 */
		Stack<Chunk_Lara> pile = new Stack<Chunk_Lara>();

		// file
		List<Chunk_Lara> listChunk = flatDoc.getChunk();
		Stack<Chunk_Lara> file = new Stack<Chunk_Lara>();
		file.addAll(listChunk);
		Chunk_Lara root = new Chunk_Lara(0, 0, 0, 0, "root", 0);
		file.add(0, root);

		// System.out.println();
		// System.out.println("STEP 0");

		int index = 0;
		while ((pile.size() != 0) || (file.size() != 0)) {
			// while(index < 10){

			// on défile la file
			Chunk_Lara curr;
			if (file.size() == 0) {
				curr = null;
				// System.out.println("Debug : File vide");
			} else {
				curr = file.get(0);
				// System.out.println("Debug curr="+curr.toString());
			}

			// Si la pile est vide, on met un élément.
			if (pile.size() == 0) {
				// System.out.println("Debug Pile vide");
				pile.push(curr);
				file.remove(0);
			} else {
				// sinon on regarde la relation
				// qu'il peut exister entre le sommet de la pile
				// et la curr.

				if (curr == null) {
					// On dépile le sommet pile et on l'écart
					// System.out.println("Debug On dépile");
					pile.pop();
				} else {
					Chunk_Lara sommet_pile = pile.pop();
					pile.push(sommet_pile);

					int result = select(sommet_pile, curr);

					if (result == 0) {
						// subordination
						// System.out.println("Debug"+curr
						// +"_sub("+sommet_pile+")");

						// On empile curr
						// on descend dans la structure du document
						pile.push(curr);

						// On enlève le premier élément de la file
						file.remove(0);

						/**
						 * Récupération du résultat
						 */
						int id = curr.id;
						String type = curr.type;
						String dependance_relation = "sub";
						String dependance_id = sommet_pile.id + "";
						String dependance_type = sommet_pile.type + "";

						String toReturn = type + "_" + id + "_"
								+ dependance_relation + "(" + dependance_type
								+ ":" + dependance_id + ")";

						body += printSubordinateHTML(curr);

						// listRelation_predict.add(toReturn);

						// PredictLabel
						// curr.setPredictTag(toReturn);
						//
						// curr.setDepId(sommet_pile.id);
						// curr.setDepRel("sub");
						// curr.setDepType(sommet_pile.type);

					} else if (result == 1) {
						// coordination
						// System.out.println("Debug"+curr
						// +"_coord("+sommet_pile+")");

						// On dépile sommet pile et on le remplace
						// par curr
						pile.pop();
						pile.push(curr);

						// On enlève le premier élément de la file
						file.remove(0);

						/**
						 * Récupération du résultat
						 */
						int id = curr.id;
						String type = curr.type;
						String dependance_relation = "coord";
						String dependance_id = sommet_pile.id + "";
						String dependance_type = sommet_pile.type + "";

						body += printCoordinateHTML(curr);

						String toReturn = type + "_" + id + "_"
								+ dependance_relation + "(" + dependance_type
								+ ":" + dependance_id + ")";

						// listRelation_predict.add(toReturn);
						// curr.setPredictTag(toReturn);
						// curr.setDepId(sommet_pile.id);
						// curr.setDepRel("coord");
						// curr.setDepType(sommet_pile.type);
					} else if (result == 2) {

						// On dépile sommet pile et on l'écarte

						body += printNothingHTML(sommet_pile, curr);
						pile.pop();
						// hack : on met curr dans la file
						// System.out.println("Debug Hack : ajout de " + curr +
						// " dans la file");
						// On n'enlève le premier élément de la file
						// file.remove(0);
					} else {
						System.out
								.println("Erreur dans le choix de la relation");
						System.out.println("Relation : " + result);
						System.exit(0);
					}
				}
			}
			// index
			index++;

		}

		return body;

	}

	private static String printNothingHTML(Chunk_Lara sommet_pile,
			Chunk_Lara curr) {

		String level_string = "";
		for (int i = 0; i < sommet_pile.getLevel(); i++) {
			level_string += "\t";
		}

		String toReturn = "";
		String type = sommet_pile.type;
		if (type.equals("item")) {
			toReturn += level_string + "</ul>\n";
		}

		return toReturn;
	}

	private static String printCoordinateHTML(Chunk_Lara curr) {

		int id = curr.id;
		String type = curr.type;
		String dependance_relation = "coord";

		String toReturn = "";

		for (int i = 0; i < curr.getLevel(); i++) {
			toReturn += "\t";
		}
		if (type.equals("item")) {
			type = "li";
		}
		if (type.equals("quote")) {
			type = "blockquote";
		}
		if (type.equals("titleDocument")) {
			type = "h1";
		}
		toReturn += "<" + type + ">" + curr.getText() + "</" + type + ">\n";

		return toReturn;
	}

	private static String printSubordinateHTML(Chunk_Lara curr) {

		int id = curr.id;
		String type = curr.type;
		String dependance_relation = "sub";

		String toReturn = "";

		String level_string = "";
		for (int i = 0; i < curr.getLevel(); i++) {
			level_string += "\t";
		}

		if (type.equals("item")) {
			toReturn += level_string + "<ul>\n";
			type = "li";
		}
		if (type.equals("root")) {
			type = "h1";
		}
		if (type.equals("quote")) {
			type = "blockquote";
		}
		if (type.equals("titleDocument")) {
			type = "h1";
		}
		toReturn += level_string + "<" + type + ">" + curr.getText() + "</"
				+ type + ">\n";
		return toReturn;

	}

	public int select(Chunk_Lara sommet_pile, Chunk_Lara currChunk) {
		// 0 : subordination
		// 1 : coordination
		// 2 : rien

		if (currChunk.getDepId() == sommet_pile.getId()) {
			if (currChunk.getDepRelation().equals("coord")) {
				// System.out.println("Coordination");
				return 1;
			} else {
				// System.out.println("Sub");
				return 0;
			}
		} else {
			// System.out.println("Rien");
			return 2;
		}

	}

	public void writeRecursiveHTML(String ReadPlainText, Document_Lara currDoc,
			Chunk_Lara readChunk) {

		int readBegin = readChunk.getBegin();
		int readEnd = readChunk.getEnd();

		// Type Titre : pas de fermeture
		if (readChunk.getType().contains("h")) {
			System.out.print("<" + readChunk.getType() + ">");
			System.out.print(ReadPlainText.substring(readBegin, readEnd));
			System.out.println("</" + readChunk.getType() + ">");

			// getSub
			if (currDoc.hasDependence(readChunk)) {

				// System.out.println("H and his dependent is " +
				// currDoc.getDependence(readChunk).toString());
				writeRecursiveHTML(ReadPlainText, currDoc,
						currDoc.getDependence(readChunk));
			}

		} else if (readChunk.getType().contains("p")) { // Type Paragraphe
			System.out.print("<" + readChunk.getType() + ">");
			System.out.print(ReadPlainText.substring(readBegin, readEnd));
			System.out.println("</" + readChunk.getType() + ">");

			if (currDoc.hasDependence(readChunk)) {
				writeRecursiveHTML(ReadPlainText, currDoc,
						currDoc.getDependence(readChunk));
			}
		} else if (readChunk.getType().equals("item")
				&& readChunk.getDepRelation().equals("sub")) {

			System.out.println("<ul>");

			System.out.print("<li>");
			System.out.print(ReadPlainText.substring(readBegin, readEnd));
			System.out.print("</li>");

			if (currDoc.hasDependence(readChunk)) {
				writeRecursiveHTML(ReadPlainText, currDoc,
						currDoc.getDependence(readChunk));
			}

			System.out.println("</ul>");
		} else if (readChunk.getType().equals("root")) { // Type Paragraphe

			if (currDoc.hasDependence(readChunk)) {
				// System.out.println("ROOT and his dependent is " +
				// currDoc.getDependence(readChunk).toString());
				writeRecursiveHTML(ReadPlainText, currDoc,
						currDoc.getDependence(readChunk));
			}
		}
	}
	
	
	public void writeSVG(String path, Document_Lara currDocument){
		writeSVG(path, currDocument, 0);
	}

	public void writeSVG(String path, Document_Lara currDocument, int length) {
		
		if(printFlag){
			System.out.println("writeSVG");
		}

		// get the sample tree
		String treeName = "";
		TreeForTreeLayout<TextInBox> tree = toConstituant(currDocument, length);

		// setup the tree layout configuration
		double gapBetweenLevels = 50;
		double gapBetweenNodes = 10;
		DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<TextInBox>(
				gapBetweenLevels, gapBetweenNodes);

		// create the NodeExtentProvider for TextInBox nodes
		TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();

		// create the layout
		TreeLayout<TextInBox> treeLayout = new TreeLayout<TextInBox>(tree,
				nodeExtentProvider, configuration);

		// Create a panel that draws the nodes and edges and show the panel
		// TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);
		// showInDialog(panel);

		SVGForTextInBoxTree generator = new SVGForTextInBoxTree(treeLayout);

		IO_Service io_service = new IO_Service();
		io_service.writeFile(
				path + currDocument.getTitle().replaceAll("\\s", "") + ".svg",
				"UTF-8", generator.getSVG());

	}

	public static TreeForTreeLayout<TextInBox> toConstituant(
			Document_Lara currDocument, int length) {

		/**
		 * Le passe à un arbre en constituants nécessite de pouvoir nommer les
		 * noeuds de l'arbre.
		 */
		if(printFlag){
			System.out.println(currDocument.getChunk_id(0).getDepId());
		}

		TextInBox root = new TextInBox(currDocument.getChunk_id(0).getText(),
				100, 20);
		DefaultTreeForTreeLayout<TextInBox> tree = new DefaultTreeForTreeLayout<TextInBox>(
				root);

		TreeMap<Integer, TextInBox> hashMap = new TreeMap<Integer, TextInBox>();
		hashMap.put(0, root);

		for (Chunk_Lara currChunk : currDocument.getChunk()) {

			if (!currChunk.getType().equals("root")) {
				// Instanciat d'un TextInBox
				TextInBox currTextInBox = new TextInBox(toPrint(currChunk, length),
						100, 20);
				// Ajout dans la HashMap
				hashMap.put(currChunk.getId(), currTextInBox);

				Chunk_Lara depChunk = currDocument.getChunk_id(currChunk
						.getDepId());
				int id_dep = depChunk.getId();

				if (currChunk.getDepRelation().equals("sub")) {

					if (hashMap.containsKey(id_dep)) {
						TextInBox tt = hashMap.get(id_dep);
						tree.addChild(hashMap.get(id_dep), currTextInBox);
					}

				} else {// =coordinate // Nécessiter de trouver le "père".
					depChunk = currDocument.getParent(currChunk);

					TextInBox tt = hashMap.get(depChunk.getId());
					tree.addChild(tt, currTextInBox);
				}
			}
		}

		return tree;
	}

	private static void showInDialog(JComponent panel) {
		JDialog dialog = new JDialog();
		Container contentPane = dialog.getContentPane();
		((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(
				10, 10, 10, 10));
		contentPane.add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	public static String toPrint(Chunk_Lara currChunk, int length) {

		/*
		 * Impression pour la console ou arbre, etc.
		 */
		String toReturn = "";
		toReturn += "[" + currChunk.getType() + "] ";
		
		
		if(currChunk.getText().length() >= length){
			toReturn += " " + currChunk.getText().substring(0, length);
		}
		else{
			toReturn += " " + currChunk.getText();
		}

//		if (currChunk.getText().length() > 10) {
//			toReturn += " " + currChunk.getText().substring(0, 10);
//		} else if (currChunk.getText().length() > 5) {
//			toReturn += " " + currChunk.getText().substring(0, 5);
//		} else if (currChunk.getText().length() == 0) {
//			toReturn += "NULL";
//		}

		return toReturn;
	}

	public void writeGlozz(String path, Document_Lara currDocument)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		
		if(printFlag){
			System.out.println("writeGlozz");
		}
		
		count_unit = 0;
//		count_relation = 0;
		writeGlozzAC(path, currDocument);

		String hashCode = getHashCode(path
				+ currDocument.getTitle().replaceAll("\\s", "") + ".ac");
//		System.out.println("HASHCODE:" + hashCode);
		

		writeGlozzAA(path, currDocument, hashCode);
	}

	public void writeGlozzAC(String path, Document_Lara currDocument) {

		index_char = 0;
		String plainText = "";
		
		if(printFlag){
			System.out.println("writeGlozzAC");
		}

		for (Chunk_Lara currChunk : currDocument.getChunk()) {
			// 1. getText and Count Char
			String chunkText = currChunk.getText();
			chunkText = chunkText.trim();
			chunkText = chunkText.replace("\n", ""); // replaceALL?
//			chunkText = chunkText.replace("\r", "");
//			chunkText = chunkText.replace("\t", "");
//			chunkText = chunkText.replace("\b", "");
//			chunkText = chunkText.replace("\f", "");
//			chunkText = chunkText.replace("\'", "");
//			chunkText = chunkText.replace("\"", "");
//			chunkText = chunkText.replace("\\", "");
			
			int length = chunkText.length();
			int begin = index_char;
			int end = index_char + length;
			String type = currChunk.getType();
			plainText += chunkText;
			currChunk.setBegin(begin);
			currChunk.setEnd(end);
			index_char += length;

			// 2. getLayoutAnnotation
			LayoutAnnotation layoutAnnotation = new LayoutAnnotation();
			layoutAnnotation.setBegin(begin);
			layoutAnnotation.setEnd(end);
			layoutAnnotation.setType(type);
			currChunk.setLayoutAnnotation(layoutAnnotation);

		}
		if(printFlag){
			System.out.println("Number of chars (java) : " +  index_char );
		}
		
		// Sanity Check
		for (Chunk_Lara currChunk : currDocument.getChunk()) {
			
			if(currChunk.getLayoutAnnotation().getBegin() > index_char){
				System.err.println("Erreur ici");
			}
			if(currChunk.getLayoutAnnotation().getEnd() > index_char){
				System.err.println("Erreur ici");
			}
		}
		
		
		// Ecriture du texte
		IO_Service io_service = new IO_Service();
		io_service.writeFile(
				path + currDocument.getTitle().replaceAll("\\s", "") + ".ac",
				"UTF-8", plainText);
	}

	public void writeGlozzAA(String path, Document_Lara currDocument,
			String hashCode) throws FileNotFoundException, IOException, ClassNotFoundException {

		if(printFlag){
			System.out.println("writeGlozzAA");
		}
		
		racine = new Element("annotations");
		document = new Document(racine);

		// hashCode
		Element hashCodeElement = new Element("metadata");
		Attribute corpusHash = new Attribute("corpusHashcode", hashCode);
		hashCodeElement.setAttribute(corpusHash);
		racine.addContent(hashCodeElement);

		// Pile pour les files
		Stack<LayoutAnnotation> pile_Layout = new Stack<LayoutAnnotation>();
		ArrayList<LayoutAnnotation> get_Layout = new ArrayList<LayoutAnnotation>();
		
		// Pile pour les items étendus (qui ont des éléments subordonnés)
		Stack<LayoutAnnotation>  pile_item_extend = new Stack<LayoutAnnotation>();
		ArrayList<LayoutAnnotation> get_item_extend = new ArrayList<LayoutAnnotation>();
		
		/**
		 * Shift-Reduce
		 */
		String body = "";
		// Algo de shift-reduce

		// ArrayList<String> listRelation_predict = new ArrayList<String>();
		/*
		 * Objets : pile
		 */
		Stack<Chunk_Lara> pile = new Stack<Chunk_Lara>();

		// file
		List<Chunk_Lara> listChunk = currDocument.getChunk();
		Stack<Chunk_Lara> file = new Stack<Chunk_Lara>();
		file.addAll(listChunk);
		Chunk_Lara root = new Chunk_Lara(0, 0, 0, 0, "root", 0);
		file.add(0, root);

		// System.out.println();
		// System.out.println("STEP 0");

		int index = 0;
		while ((pile.size() != 0) || (file.size() != 0)) {
			// on défile la file
			Chunk_Lara curr;
			if (file.size() == 0) {
				curr = null;
			} else {
				curr = file.get(0);
			}

			// Si la pile est vide, on met un élément.
			if (pile.size() == 0) {
				// System.out.println("Debug Pile vide");
				pile.push(curr);
				file.remove(0);
			} else {
				// sinon on regarde la relation
				// qu'il peut exister entre le sommet de la pile
				// et la curr.

				if (curr == null) {
					// On dépile le sommet pile et on l'écart
					// System.out.println("Debug On dépile");
					pile.pop();
				} else {
					Chunk_Lara sommet_pile = pile.pop();
					pile.push(sommet_pile);

					int result = select(sommet_pile, curr);
					
					if (result == 0) {
						// subordination
						// System.out.println("Debug"+curr
						// +"_sub("+sommet_pile+")");

						// On empile curr
						// on descend dans la structure du document
						pile.push(curr);

						// On enlève le premier élément de la file
						file.remove(0);

						/**
						 * Récupération du résultat
						 */
						int id = curr.id;
						String type = curr.type;
						String dependance_relation = "sub";
						String dependance_id = sommet_pile.id + "";
						String dependance_type = sommet_pile.type + "";

						String toReturn = type + "_" + id + "_"
								+ dependance_relation + "(" + dependance_type
								+ ":" + dependance_id + ")";

						// body += printSubordinateHTML(curr);d

						if(!curr.getType().equals("item")){
							//Si l'élément n'est pas un item subordonné.
							Element returnElement = printUnit(curr); // Impression de l'unité
							racine.addContent(returnElement);
						}
						else if(curr.getType().equals("item") && !currDocument.hasSubordinate(curr)){
							// Si l'élément courant est un item mais qu'il n'a pas de subordonné
							// on fait comme les autres.
							Element returnElement = printUnit(curr); // Impression de l'unité
							racine.addContent(returnElement);
						}
						else if(curr.getType().equals("item") && currDocument.hasSubordinate(curr)){
							// Si l'élément est un item mais qu'il a des éléments subordonnés
							// il faut étendre l'impression de son unité.
							
							// Cette condition devra aussi se retrouver dans les coordonnés.
							
							LayoutAnnotation item_extend = new LayoutAnnotation();
							item_extend.setType("listItem");
							item_extend.setBegin(curr.getLayoutAnnotation().getBegin());
							pile_item_extend.push(item_extend);
						}

						if (curr.getType().equals("item")) {
							LayoutAnnotation list = new LayoutAnnotation();
							list.setBegin(curr.getLayoutAnnotation().getBegin());
							list.setType("list");

							pile_Layout.push(list);
						}

						// listRelation_predict.add(toReturn);

						// PredictLabel
						// curr.setPredictTag(toReturn);
						//
						// curr.setDepId(sommet_pile.id);
						// curr.setDepRel("sub");
						// curr.setDepType(sommet_pile.type);

					} else if (result == 1) {
						// coordination
						// System.out.println("Debug"+curr
						// +"_coord("+sommet_pile+")");

						// On dépile sommet pile et on le remplace
						// par curr
						pile.pop();
						pile.push(curr);

						// On enlève le premier élément de la file
						file.remove(0);

						/**
						 * Récupération du résultat
						 */
						int id = curr.id;
						String type = curr.type;
						String dependance_relation = "coord";
						String dependance_id = sommet_pile.id + "";
						String dependance_type = sommet_pile.type + "";
						
						if(!curr.getType().equals("item")){
							//Si l'élément n'est pas un item subordonné.
							Element returnElement = printUnit(curr); // Impression de l'unité
							racine.addContent(returnElement);
						}
						else if(curr.getType().equals("item") && !currDocument.hasSubordinate(curr)){
							// Si l'élément courant est un item mais qu'il n'a pas de subordonné
							// on fait comme les autres.
							Element returnElement = printUnit(curr); // Impression de l'unité
							racine.addContent(returnElement);
						}
						else if(curr.getType().equals("item") && currDocument.hasSubordinate(curr)){
							// Si l'élément est un item mais qu'il a des éléments subordonnés
							// il faut étendre l'impression de son unité.
							
							// Cette condition devra aussi se retrouver dans les coordonnés.
							LayoutAnnotation item_extend = new LayoutAnnotation();
							item_extend.setType("listItem");
							item_extend.setBegin(curr.getLayoutAnnotation().getBegin());
							pile_item_extend.push(item_extend);
						}

						String toReturn = type + "_" + id + "_"
								+ dependance_relation + "(" + dependance_type
								+ ":" + dependance_id + ")";

						// listRelation_predict.add(toReturn);
						// curr.setPredictTag(toReturn);
						// curr.setDepId(sommet_pile.id);
						// curr.setDepRel("coord");
						// curr.setDepType(sommet_pile.type);
					} else if (result == 2) {

						// Gestion des listes
						if (sommet_pile.getType().equals("item")) {
							LayoutAnnotation list = pile_Layout.pop();
							list.setEnd(sommet_pile.getLayoutAnnotation()
									.getEnd());
							get_Layout.add(list);
						}
						
						// Gestion des items étendus
						if(!pile_item_extend.isEmpty()){
							LayoutAnnotation item_extend = pile_item_extend.pop();
							item_extend.setEnd(sommet_pile.getLayoutAnnotation().getEnd());
							get_item_extend.add(item_extend);
						}

						// On dépile sommet pile et on l'écarte

						// body += printNothingHTML(sommet_pile, curr);d
						pile.pop();
						// hack : on met curr dans la file
						// System.out.println("Debug Hack : ajout de " + curr +
						// " dans la file");
						// On n'enlève le premier élément de la file
						// file.remove(0);
					} else {
						System.out
								.println("Erreur dans le choix de la relation");
						System.out.println("Relation : " + result);
						System.exit(0);
					}
				}
			}
			// index
			index++;

		}

		/**
		 * Les Listes
		 */
		for (LayoutAnnotation currLayout : get_Layout) {
			// System.out.println("");
			// System.out.println(currLayout.getBegin());
			// System.out.println(currLayout.getEnd());

			racine.addContent(printList(currLayout));
		}
		if (!pile_Layout.isEmpty()) {
			
			if(printFlag){
				System.out.println("Pile à files non fermées.");
			}
			// Hypothèse que c'est une ou plusieurs listes qui termine le
			// document

			Iterator<LayoutAnnotation> iter = pile_Layout.iterator();
			while (iter.hasNext()) {
				if(printFlag)
					System.out.println("Une liste fermée");
				LayoutAnnotation currLayoutAnnotation = (LayoutAnnotation) iter
						.next();

				int end = currDocument.getLengthChar();

				if(printFlag){
				System.out.println("DEB :" + currLayoutAnnotation.getBegin());
				System.out.println("END :" + end);
				}
				currLayoutAnnotation.setEnd(end);
				racine.addContent(printList(currLayoutAnnotation));
			}
		}
		
		/**
		 * Les items étendus
		 */
		for(LayoutAnnotation item_extend : get_item_extend){
			
			Chunk_Lara temporaire = new Chunk_Lara(0, 0, 0, 0);
			temporaire.setType("listItem");
			temporaire.setLayoutAnnotation(item_extend);
			
			racine.addContent(printUnit(temporaire));
		}
		
		/**
		 * Objets dans le document
		 */
		ArrayList<Unit> list_of_Units = currDocument.getList_of_units();
		for(Unit currUnit : list_of_Units){
			
			String axe_semantique = currUnit.getAxe_semantique();
			String axe_verticale = currUnit.getAxe_visuel();
			
			// Verticale et Taxonomique
			if ((axe_semantique.equals("isA") || axe_semantique
					.equals("instanceOf") || axe_semantique.equals("partOf") ) 
					&& axe_verticale.equals("Verticale")) {
				writeGlozzAA_Units(currUnit);
			}
		}
		
		
		/** 
		 * Les termes
		 */
		// Term dans tout le document
		// TODO A REMETTRE!!
//		for(Chunk_Lara currChunk : currDocument.getChunk()){
//			if(currChunk.getTerms() != null){
//				ArrayList<Term> candidate_terms = currChunk.getTerms();
//				for(Term candidate : candidate_terms){
//					printTerms(currChunk, candidate);
//				}
//			}
//		}
		
		
		// Aout des Relations
		
		for(Element relation : relations_for_end){
			racine.addContent(relation);
		}

		// Ecriture

		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		// Remarquez qu'il suffit simplement de créer une instance de
		// FileOutputStream
		// avec en argument le nom du fichier pour effectuer la
		// sérialisation.
		
		if(printFlag){
		System.out.println("Ecriture dans " + path
				+ currDocument.getTitle().replaceAll("\\s", "")+".aa");
		}
		sortie.output(document, new FileOutputStream(path
				+ currDocument.getTitle().replaceAll("\\s", "") + ".aa"));
		


	}

	public void writeGlozzAA_Units(Unit currUnit) throws UnknownHostException, ClassNotFoundException, IOException{
			
		
		
		String axe_semantique = currUnit.getAxe_semantique();
		String axe_verticale = currUnit.getAxe_visuel();
		
		// Verticale et Taxonomique
		if ((axe_semantique.equals("isA") || axe_semantique
				.equals("instanceOf") || axe_semantique.equals("partOf") ) 
				&& axe_verticale.equals("Verticale")) {
			
			
			/**
			 * SE générale
			 */
			Chunk_Lara temporaire = new Chunk_Lara(0,0,0,0);
			temporaire.setType("ES");
			LayoutAnnotation temp_LA = new LayoutAnnotation();
			temp_LA.setBegin(currUnit.getIndice_begin());
			temp_LA.setEnd(currUnit.getIndice_end());
			temporaire.setLayoutAnnotation(temp_LA);
			racine.addContent(printUnit(temporaire));
			
			
			
			/**
			 * Primer
			 */
			if(!currUnit.getPrimer().getText().equals("null")){
				
				Chunk_Lara temp_primer = new Chunk_Lara(0,0,0,0);
				temp_primer.setType("amorce");
				LayoutAnnotation temp_primer_LA = new LayoutAnnotation();
				temp_primer_LA.setBegin(currUnit.getPrimer().getIndice_begin());
				temp_primer_LA.setEnd(currUnit.getPrimer().getIndice_end());
				temp_primer.setLayoutAnnotation(temp_primer_LA);
				racine.addContent(printUnit(temp_primer));
			}
			
			
			
			/**
			 * Item
			 */
			for(Item currItem : currUnit.getItems()){
				Chunk_Lara temp_primer = new Chunk_Lara(0,0,0,0);
				temp_primer.setType("itemSE");
				LayoutAnnotation temp_primer_LA = new LayoutAnnotation();
				
				temp_primer_LA.setBegin(currItem.getIndice_begin());
				temp_primer_LA.setEnd(currItem.getIndice_end());
				temp_primer.setLayoutAnnotation(temp_primer_LA);
				racine.addContent(printUnit(temp_primer));
			}
			
			
			printConcept(currUnit);
			
			
			
	//		<characterisation>
	//		<type>SE</type>
	//		<featureSet>
	//		<feature name="type">SE</feature>
	//		</featureSet>
	//		</characterisation>
	//		<positioning>
	//		<embedded-unit id="coder2_1280392629065" />
	//		<embedded-unit id="coder2_1280392580115" />
	//		<embedded-unit id="coder2_1280392651802" />
	//		<embedded-unit id="coder2_1280392560306" />
	//		<embedded-unit id="coder2_1280392595925" />
	//		<embedded-unit id="coder2_1280392671810" />
	//		<embedded-unit id="coder2_1280392610202" />
	//		<embedded-unit id="coder2_1280392687210" />
	//		</positioning>
	//		</schema>
	//		<schema id="coder2_1280393117624">
	//		<metadata>
	//		<author>coder2</author>
	//		<creation-date>1280393117624</creation-date>
	//		<lastModifier>n/a</lastModifier>
	//		<lastModificationDate>0</lastModificationDate>
	//		</metadata>
	//		<characterisation>
			
			
			/** 
			 * Terminology Extraction (Term)
			 */
			
	////		Prune Term
	//		for(Chunk_Lara currChunk : currUnit.getPrimer().getListChunk()){
	//			
	//			if(currChunk.getTerms() != null){
	//				ArrayList<Term> candidate_terms = currChunk.getTerms();
	//				for(Term candidate : candidate_terms){
	//					
	//					System.out.println(candidate);
	//					
	//					
	//				}
	//			}
	//			
	//		}
	//		
	//		
	//		
			
		}
			
			
		}

	public Element printList(LayoutAnnotation currListLayout) {

		Chunk_Lara temporaire = new Chunk_Lara(0, 0, 0, 0);
		temporaire.setType("list");
		temporaire.setLayoutAnnotation(currListLayout);

		return printUnit(temporaire);
	}
	
	
	
	
	
	public void printConcept(Unit currUnit) throws UnknownHostException, ClassNotFoundException, IOException{
		/**
		 * Concept Primer
		 */
		
		// Modification du code 16 novembre
		// Affichage des relations dans Glozz pour annotation/correction manuelle après.
		
		// System.out.println(currUnit.getAxe_semantique());
		String axe_semantique = currUnit.getAxe_semantique();
		String axe_verticale = currUnit.getAxe_visuel();
		
		// Verticale et Taxonomique
		if ((axe_semantique.equals("isA") || axe_semantique
				.equals("instanceOf") || axe_semantique.equals("partOf") ) 
				&& axe_verticale.equals("Verticale")) {
			
			
			Primer primer = currUnit.getPrimer();
			
			ArrayList<String> id_concept_primer = new ArrayList<String>();
			ArrayList<String> string_concept_primer = new ArrayList<String>();
			
			ArrayList<String> id_concept_item = new ArrayList<String>();
			ArrayList<String> string_concept_item = new ArrayList<String>();
			
			for(Segment currSegment : primer.getConcept()){
				
				FuzzyMatcher_Client fuzzy_matcher_client = new FuzzyMatcher_Client();
				fuzzy_matcher_client.setHost("localhost");
				
				ArrayList<Chunk_Lara> chunk_list = currUnit.getPrimer().getListChunk();

				
				boolean flag = false;
				for(Chunk_Lara currChunk : chunk_list){
				
					Message_Matcher msg_matcher = fuzzy_matcher_client.analyse(currChunk.getText(), currSegment.getText());
				
					if(msg_matcher.getMatching()){
						Term newTerm = new Term();
						newTerm.setText(currSegment.getText());
						newTerm.setOrigin("concept");
						newTerm.setStart(msg_matcher.getStart());
						newTerm.setEnd(msg_matcher.getEnd());
						String id = printTerms(currChunk, newTerm);
						
						id_concept_primer.add(id);
						string_concept_primer.add(newTerm.getText());
						
						flag=true;
						
					}
				}
				
				if(flag == false){
					System.err.println("Concept primer non retrouvé pour " + currSegment.getText());
				}
				
			}
			
			
			
			
			/**
			 * Item
			 */
			for(Item currItem : currUnit.getItems()){
				
			Concept currConcept = currItem.getConcept();
				
				for(Segment currSegment : currConcept){
//					System.out.println(currSegment.getIndice_begin() + " " + currSegment.getIndice_end());
				
//					System.out.println("Segment item:"+currSegment.getText());
					
					FuzzyMatcher_Client fuzzy_matcher_client = new FuzzyMatcher_Client();
					fuzzy_matcher_client.setHost("localhost");
					
					ArrayList<Chunk_Lara> chunk_list = currItem.getListChunk();

					boolean flag = false;
					for(Chunk_Lara currChunk : chunk_list){
					
						Message_Matcher msg_matcher = fuzzy_matcher_client.analyse(currChunk.getText(), currSegment.getText());
					
						if(msg_matcher.getMatching()){
							Term newTerm = new Term();
							newTerm.setText(currSegment.getText());
							newTerm.setOrigin("concept");
							newTerm.setStart(msg_matcher.getStart());
							newTerm.setEnd(msg_matcher.getEnd());
							String id = printTerms(currChunk, newTerm);
							id_concept_item.add(id);
							string_concept_item.add(currSegment.getText());
							
							flag = true;
						}
						
					}
					
					if(flag == false){
						System.err.println("Concept item non retrouvé pour " + currSegment.getText());
					}
				}
			}
			
			
			
			// Impression des relations
			printRelations(id_concept_primer,string_concept_primer,id_concept_item,string_concept_item,axe_semantique);
			
			
		}
		
		
	}
	
	
	public void printRelations(ArrayList<String> id_primer, ArrayList<String> string_concept_primer, ArrayList<String> id_item, ArrayList<String> string_concept_item, String relation){
		
//		System.out.println("Size id_primer:"+id_primer.size());
//		System.out.println("Size id_primer_string:"+string_concept_primer.size());
//		System.out.println("Size id_item:"+id_item.size());
//		System.out.println("Size id_item_string:"+string_concept_item.size());
		
		
		for(int i=0;i<id_primer.size();i++){
			for(int j=0;j<id_item.size();j++){
				System.out.println("Print Relation : " + string_concept_primer.get(i) + "[" + id_primer.get(i) +"]" + "_" + string_concept_item.get(j) + "[" + id_item.get(j) +"]");
				print1Relation(id_primer.get(i),id_item.get(j),relation);
			}
		}
		
	}
	
	
	public void print1Relation(String id_concept_primer, String id_concept_item, String axe_semantique){
		
		
//		<relation id="jfaucon_1415997601663">
//		<metadata>
//		<author>jfaucon</author>
//		<creation-date>1415997601663</creation-date>
//		<lastModifier>n/a</lastModifier>
//		<lastModificationDate>0</lastModificationDate>
//		</metadata>
//		<characterisation>
//		<type>isA</type>
//		<featureSet/>
//		</characterisation>
//		<positioning>
//		<term id="layout_370"/>
//		<term id="layout_345"/>
//		</positioning>
//		</relation>
		
		Element currElementUnit = null;

		currElementUnit = new Element("relation");
		String time = new Date().getTime() + "";
		Attribute id = new Attribute("id", "layout_" + count_unit);
		currElementUnit.setAttribute(id);

		/**
		 * Metadata
		 */
		Element currElementMetadata = new Element("metadata");
		currElementUnit.addContent(currElementMetadata);

		// Author
		Element currElementAuthor = new Element("author");
		currElementAuthor.setText("layout");
		currElementMetadata.addContent(currElementAuthor);

		// creation-date
		Element currElementCreationDate = new Element("creation-date");
		currElementCreationDate.setText("" + count_unit);
		currElementMetadata.addContent(currElementCreationDate);

		// lastModifier
		Element currElementLastModifier = new Element("lastModifier");
		currElementLastModifier.setText("n/a");
		currElementMetadata.addContent(currElementLastModifier);

		// lastModificationDate
		Element currElementLastModificationDate = new Element(
				"lastModificationDate");
		currElementLastModificationDate.setText("0");
		currElementMetadata.addContent(currElementLastModificationDate);

		/**
		 * Characterization
		 */
		Element currElementCharacterisation = new Element("characterisation");
		currElementUnit.addContent(currElementCharacterisation);

		Element currElementType = new Element("type");
		String type = converseToSchemaGlozz(axe_semantique);
		currElementType.setText(type);
		currElementCharacterisation.addContent(currElementType);

		Element currElementFeaturesSet = new Element("featureSet");
		currElementCharacterisation.addContent(currElementFeaturesSet);

		/**
		 * positioning
		 */
		Element currElementPositioning = new Element("positioning");
		currElementUnit.addContent(currElementPositioning);

		// terme 1 : primer
		Element currElementStart = new Element("term");
		Attribute item_1 = new Attribute("id",id_concept_item);
		currElementStart.setAttribute(item_1);
		currElementPositioning.addContent(currElementStart);
		

		// term 2 : item
		Element currElementEnd = new Element("term");
		Attribute primer_1 = new Attribute("id",id_concept_primer);
		currElementEnd.setAttribute(primer_1);
		currElementPositioning.addContent(currElementEnd);

		relations_for_end.add(currElementUnit);

		count_unit++;
//		racine.addContent(currElementUnit);
		
	}
	
	
	
	public String printTerms(Chunk_Lara currChunk, Term candidate_term){
		
		String id = "";
		int start_chunk = currChunk.getLayoutAnnotation().getBegin();
		int end_chunk = currChunk.getLayoutAnnotation().getEnd();
		
        Chunk_Lara temporaire = new Chunk_Lara(0,0,0,0);
		temporaire.setType(candidate_term.getOrigin());
		LayoutAnnotation temp_LA = new LayoutAnnotation();
		temp_LA.setBegin(currChunk.getLayoutAnnotation().getBegin() + candidate_term.getStart());
		temp_LA.setEnd(currChunk.getLayoutAnnotation().getBegin() + candidate_term.getEnd());
		temporaire.setLayoutAnnotation(temp_LA);
		
		id = "layout_" + count_unit;
		racine.addContent(printUnit(temporaire));
		return id;
	}
	

	public Element printUnit(Chunk_Lara currChunk) {

		Element currElementUnit = null;

		currElementUnit = new Element("unit");
		String time = new Date().getTime() + "";
		Attribute id = new Attribute("id", "layout_" + count_unit);
		currElementUnit.setAttribute(id);
		/**
		 * Metadata
		 */
		Element currElementMetadata = new Element("metadata");
		currElementUnit.addContent(currElementMetadata);

		// Author
		Element currElementAuthor = new Element("author");
		currElementAuthor.setText("layout");
		currElementMetadata.addContent(currElementAuthor);

		// creation-date
		Element currElementCreationDate = new Element("creation-date");
		currElementCreationDate.setText("" + count_unit);
		currElementMetadata.addContent(currElementCreationDate);

		// lastModifier
		Element currElementLastModifier = new Element("lastModifier");
		currElementLastModifier.setText("n/a");
		currElementMetadata.addContent(currElementLastModifier);

		// lastModificationDate
		Element currElementLastModificationDate = new Element(
				"lastModificationDate");
		currElementLastModificationDate.setText("0");
		currElementMetadata.addContent(currElementLastModificationDate);

		/**
		 * Characterization
		 */
		Element currElementCharacterisation = new Element("characterisation");
		currElementUnit.addContent(currElementCharacterisation);

		Element currElementType = new Element("type");
		String type = converseToSchemaGlozz(currChunk.getType());
		currElementType.setText(type);
		currElementCharacterisation.addContent(currElementType);

		Element currElementFeaturesSet = new Element("featureSet");
		currElementCharacterisation.addContent(currElementFeaturesSet);

		/**
		 * positioning
		 */
		Element currElementPositioning = new Element("positioning");
		currElementUnit.addContent(currElementPositioning);

		// start
		Element currElementStart = new Element("start");
		currElementPositioning.addContent(currElementStart);

		Element singlePosition = new Element("singlePosition");
		int deb = currChunk.getLayoutAnnotation().getBegin();
//		System.out.println("Longueur : " +index_char);
//		System.out.println("Deb : " + deb);
		if(deb > index_char){
			System.err.println("ERREUR dans printUnit");
		}
		Attribute index = new Attribute("index", deb + "");
		singlePosition.setAttribute(index);
		currElementStart.addContent(singlePosition);

		// end
		Element currElementEnd = new Element("end");
		currElementPositioning.addContent(currElementEnd);

		Element singlePosition2 = new Element("singlePosition");
		int fin = currChunk.getLayoutAnnotation().getEnd();
//		System.out.println("Fin : " + fin);
		if(fin > index_char){
			System.err.println("ERREUR dans printUnit");
		}
		Attribute index2 = new Attribute("index", fin + "");
		singlePosition2.setAttribute(index2);
		currElementEnd.addContent(singlePosition2);

		
		count_unit++;
		return currElementUnit;

	}

	public String converseToSchemaGlozz(String type) {

		String toReturn = "";

		if (type.equals("h1")) {
			toReturn = "title";
		} else if (type.equals("root")) {
			toReturn = "title";
		} else if (type.equals("h2")) {
			toReturn = "sectionTitle";
		} else if (type.equals("h3")) {
			toReturn = "subSectionTitle";
		} else if (type.equals("h4")) {
			toReturn = "subSubSectionTitle";
		} else if (type.equals("h5")) {
			toReturn = "subSubSubSectionTitle";
		} 
		// H6 Alfred Hitchcock
		else if (type.equals("p")) {
			toReturn = "paragraph";
		} else if (type.equals("item")) {
			toReturn = "listItem";
		} else if (type.equals("itemSE")) {
			toReturn = "item";
		} 
		else {
			toReturn = type;
		}

		return toReturn;
	}

	public String getHashCode(String path) throws IOException {

		String hash = "";
		FileInputStream s = null;
		try {
			s = new FileInputStream(path);
			int length = s.available();
			hash = "" + length + "-";
			long code = 1;
			for (int i = 0; i < length; i++) {
				int n = s.read();
				if (n != 0) {
					code *= n;
					code = code % 99999999;
				}
//				 System.out.println("code="+code);
			}
			hash += code;
		} finally {

		}
		return hash;
	}
}
