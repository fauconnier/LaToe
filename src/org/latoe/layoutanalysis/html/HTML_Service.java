package org.latoe.layoutanalysis.html;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import logicalobjects.Chunk_Lara;
import logicalobjects.Document_Lara;

import org.htmlcleaner.AttributeTransformationPatternImpl;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CleanerTransformations;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;
import org.melodi.tools.tree.ShiftReduce_Service;
import org.mozilla.universalchardet.UniversalDetector;

public class HTML_Service {

	private static ArrayList<Chunk_Lara> currListChunk;
	private static boolean printFlag = false;

	public static void main(String[] args) throws IOException {

		/**
		 * En 3 étapes : 0. Cleaner le document avec JTidy 1. Avoir la structure
		 * -> toDep avec JSoup 2. Récupérer les annotations LARAt -> toDep
		 */

		
		/**
		 * Etape 1 : HTML -> toDep
		 */
		HTML_Service test_html_service = new HTML_Service();
		Document_Lara test_document = test_html_service.getDocument(
				"input/melodi.html");

		System.out.println(test_document.toString());

		// Reader_Service myReader = new Reader_Service();
		// myReader.writeHTML("output/", "UTF-8", test_document);
		// myReader.writeSVG("output/", test_document, 15);

	}

	public HTML_Service() {

	}

	public Document_Lara getDocument(String path)
			throws IOException {

		Document_Lara currDocument = new Document_Lara();
		currListChunk = new ArrayList<Chunk_Lara>();

		
		String encoding = this.getEncoding(path);
		this.cleanHTML(path, path+"_clean", encoding);
		
		
		/*
		 * Run Dependency
		 */
		String title_html = runDependencyBase(path+"_clean", "UTF-8");

		// PréProcess
		// 1. Enlever les balises vides
		// 2. if item vide suivi d'un P, remonter le P et le mettre à l'item.
		ArrayList<Chunk_Lara> newArray = new ArrayList<Chunk_Lara>();
		Chunk_Lara root_Chunk = new Chunk_Lara(0, 0, 0, 0);
		root_Chunk.setType("root");
		root_Chunk.setDepRel("");
		root_Chunk.setDepId(-1);
		root_Chunk.setText(title_html);
		newArray.add(root_Chunk);
		currDocument.setName(title_html);

		/**
		 * Preprocess.
		 * 
		 */
		for (int i = 0; i < currListChunk.size(); i++) {

			Chunk_Lara curr = currListChunk.get(i);
			// printLevel(curr);

			// // Si le courant = p
			// // si on est dans le document
			// // si le -1 est un item
			// // si le -1 n'a pas de texte
			// // =>
			// // alors courant = item
			// // alors courant = level + haut
			if (curr.getType().equals("p") && i > 0
					&& currListChunk.get(i - 1).getType().equals("item")
					&& currListChunk.get(i - 1).getText().trim().length() == 0) {
				curr.setType("item");
//				curr.setLevel(currListChunk.get(i - 1).getLevel() - 1);
				curr.setLevel(currListChunk.get(i - 1).getLevel()); //Même level que l'item précédent
				 
//				System.out.println(curr);
			}

			if (curr.getText().trim().length() > 1) {
				newArray.add(curr);
			}

		}

		// Numérotation des chunks
		int index = 0; // Les INde des document comment à 1, car 0=root
		for (Chunk_Lara currChunk : newArray) {
			currChunk.setId(index);
			index++;
		}

		currDocument.setChunk(newArray);
		
		// DEBUG
		for(Chunk_Lara currChunk : currDocument.getChunk()){
			System.out.println(currChunk.getLevel() + "_" + currChunk);
		}
		

		ShiftReduce_Service shiftReduce_Service = new ShiftReduce_Service();
		currDocument = shiftReduce_Service.assign_shiftreduce(currDocument, 0);

		return currDocument;
	}

	public static String runDependencyBase(String path, String encoding)
			throws IOException {
		/**
		 * Balises prises en compte : h*,p,ul,ol,li
		 * 
		 * encodage : ISO-8859-1
		 */
		File input = new File(path);
		org.jsoup.nodes.Document doc = Jsoup.parse(input, encoding, ""
				+ path);

		String title_html = doc.getElementsByTag("title").text();

		/**
		 * Deux techniques possibles 1. Passer par traverse 2. Le fair à
		 * l'ancienne Objectif : Avoir un arrayList de chunk estampilé par
		 * h*,p,item, etc. + level Okay pour le level
		 * 
		 * Le problème avec la deuxième, c'est qu'on aura toujours l'arbre. Le
		 * problème avec la première, c'est que je ne gère pas le parcours.
		 * 
		 * Contraintes sur les chunks. Un chunk a un seul level. Difficulté ici
		 * par rapport à WikipediaParser est que les terminaux ne sont pas égaux
		 * aux chunks.
		 */

		doc.body().traverse(new NodeVisitor() {
			@Override
			public void head(Node node, int depth) {

				String currNodeName = node.nodeName();

				if (currNodeName.equals("p")) {
					runDependencyRecursive_NewChunk(depth, node, "p");
				} else if (currNodeName.contains("h")) {
					runDependencyRecursive_NewChunk(depth, node, currNodeName);
				} else if (currNodeName.equals("li")) {
					runDependencyRecursive_NewChunk(depth, node, "item");
				} else if (currNodeName.equals("blockquote")) {
					runDependencyRecursive_NewChunk(depth, node, "quote");
				}

			}

			@Override
			public void tail(Node node, int depth) {
				// Do Nothing
			}
		});

		return title_html;
	}

	public static String runDependencyRecursive_OnlyText(int level,
			Node currNode, String parent) {

		String text_of_chunk = "";
		level++;

		for (Node curr : currNode.childNodes()) {
			// TextNode de niveau 1

			// System.out.println(curr.nodeName());

			// a - b et i sont des terminaux.
			if (curr instanceof TextNode) {
				TextNode currTextNode = (TextNode) curr;
				printLevel(level, " [" + level + "] " + currTextNode.text());
				text_of_chunk += currTextNode.text();
			}
			// bold, link, italic, etc.
			text_of_chunk += runDependencyRecursive_OnlyText(level, curr,
					"test");
		}

		if (text_of_chunk.length() == 0) {
			// System.out.println("Erreur");
			text_of_chunk = "";
		}

		return text_of_chunk;

	}

	public static String runDependencyRecursive_NewChunk(int level,
			Node currNode, String parent) {

		Chunk_Lara currChunk = new Chunk_Lara(0, 0, 0, 0);
		currChunk.setLevel(level);
		currChunk.setType(parent);

		String text_of_chunk = "";

		level++;
		for (Node curr : currNode.childNodes()) {

			// a - b et i sont des terminaux.
			if (!curr.nodeName().equals("b") && !curr.nodeName().equals("i")
					&& !curr.nodeName().equals("a")
					&& !curr.nodeName().equals("strong")
					&& !curr.nodeName().equals("em")
					&& !curr.nodeName().equals("div")
					&& !curr.nodeName().equals("font")
					&& !curr.nodeName().equals("span")
					&& !curr.nodeName().equals("u")) {

				if (curr instanceof TextNode) {
					TextNode currTextNode = (TextNode) curr;
					printLevel(level, " [" + level + "] " + currTextNode.text());
					text_of_chunk += currTextNode.text();
				}
				// // bold, link, italic, etc.
				// text_of_chunk += runDependencyRecursive_OnlyText(level, curr,
				// "test");
			} else {
				// node = a, b or a
				text_of_chunk += runDependencyRecursive_OnlyText(level, curr,
						"test");

				// TextNode currTextNode = (TextNode) curr;

			}
		}

		if (text_of_chunk.length() == 0) {
			// System.out.println("Erreur");
			text_of_chunk = "";
		}

		currChunk.setText(text_of_chunk);
		currListChunk.add(currChunk);

		return text_of_chunk;
	}

	public static void printLevel(Chunk_Lara currChunk) {

		for (int i = 0; i < currChunk.getLevel(); i++) {
			System.out.print("\t");
		}
		System.out.println(currChunk.getType() + " " + currChunk.getText());
	}

	public static void printLevel(int level, String text) {

		if (printFlag) {
			for (int i = 0; i < level; i++) {
				System.out.print("\t");
			}
			System.out.println(text);
		}
	}

	public String getEncoding(String path) throws IOException {
	
		byte[] buf = new byte[4096];
		java.io.FileInputStream fis = new java.io.FileInputStream(path);
	
		// (1)
		UniversalDetector detector = new UniversalDetector(null);
	
		// (2)
		int nread;
		while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}
		// (3)
		detector.dataEnd();
	
		// (4)
		String encoding = detector.getDetectedCharset();
		if (encoding != null) {
//			System.out.println("Encoding = " + encoding);
		} else {
			encoding = "ISO-8859-1";
			System.err.println("No encoding detected by default " + encoding);
		}
	
		// (5)
		detector.reset();
	
		return encoding;
	
	}

	public void cleanHTML(String path, String out, String encoding) throws IOException {
	
		HtmlCleaner cleaner = new HtmlCleaner();
	
		CleanerProperties props = cleaner.getProperties();
	
		CleanerTransformations transformations = new CleanerTransformations();
	
		AttributeTransformationPatternImpl attPattern = new AttributeTransformationPatternImpl(
				Pattern.compile("^\\s*class", Pattern.CASE_INSENSITIVE), null,
				null);
		transformations.addGlobalTransformation(attPattern);
	
		AttributeTransformationPatternImpl attPattern2 = new AttributeTransformationPatternImpl(
				Pattern.compile("^\\s*id", Pattern.CASE_INSENSITIVE), null,
				null);
		transformations.addGlobalTransformation(attPattern2);
	
		props.setCleanerTransformations(transformations);
	
		// set some properties to non-default values
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(false);
		props.setOmitComments(true);
		props.setPruneTags("script,style,img,form");
		
		
	
		// do parsing
		TagNode tagNode = new HtmlCleaner(props)
				.clean(new File(path), encoding);
	
		tagNode.removeAttribute("class");
	
		// serialize to xml file
		new PrettyHtmlSerializer(props).writeToFile(tagNode,
				out, "utf-8");
	
	}

}
