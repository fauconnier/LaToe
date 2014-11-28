package org.latoe.layoutanalysis.wikipedia;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.melodi.objectslogic.Chunk_Lara;
import org.melodi.objectslogic.Document_Lara;
import org.melodi.tools.common.IO_Service;
import org.melodi.tools.tree.ShiftReduce_Service;

import de.tudarmstadt.ukp.wikipedia.api.Category;
import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiTitleParsingException;
import de.tudarmstadt.ukp.wikipedia.parser.Content;
import de.tudarmstadt.ukp.wikipedia.parser.ContentElement;
import de.tudarmstadt.ukp.wikipedia.parser.NestedListContainer;
import de.tudarmstadt.ukp.wikipedia.parser.NestedListElement;
import de.tudarmstadt.ukp.wikipedia.parser.Paragraph;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import de.tudarmstadt.ukp.wikipedia.parser.Section;
import de.tudarmstadt.ukp.wikipedia.parser.Table;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.FrenchTemplateParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParser;
import de.tudarmstadt.ukp.wikipedia.parser.mediawiki.MediaWikiParserFactory;

public class Wikipedia_Service {
	
	private DatabaseConfiguration dbConfig;
	private Wikipedia wiki;
	private static ArrayList<Chunk_Lara> currListChunk;
	private static boolean printFlag = false;
	
	
	public Wikipedia_Service() throws WikiInitializationException{
	
	}
	
	
	public void connect(String bdd_host, String bdd_name, String bdd_user, String bdd_pwd) throws WikiInitializationException{
		// configure the database connection parameters
		dbConfig = new DatabaseConfiguration();
		dbConfig.setHost(bdd_host);
		dbConfig.setDatabase(bdd_name);
		dbConfig.setUser(bdd_user);
		dbConfig.setPassword(bdd_pwd);
		dbConfig.setLanguage(Language.french);
		wiki = new Wikipedia(dbConfig);
	}
	
	
	public void writeMediaWiki(String path, String encoding, String title_request) throws WikiApiException{
		
		 if (wiki.existsPage(title_request)) {
			 Page page = wiki.getPage(title_request);
			 String text = page.getText();
			 
				IO_Service io_service = new IO_Service();
				io_service.writeFile(
						path + title_request.replaceAll("\\s", "") + ".mediawiki",
						encoding, text);
		 }
		 else {
			 System.out.println("Page " + title_request + " does not exist");
		 }
		
	}
	
	
	public Document_Lara getDocument(Page currPage) throws WikiTitleParsingException{
		return  runDependencyBase(currPage);
	}
	
	
	public Document_Lara getDocumentFromText(String path) throws UnsupportedEncodingException, FileNotFoundException{
		
		/**
		 * ParserFactory
		 */
		IO_Service io_service = new IO_Service();
		String wikiText = io_service.readFile(path);
		
		MediaWikiParserFactory parserFactory = new MediaWikiParserFactory(
				Language.french);
		// Add ParserTemplate
		parserFactory.setTemplateParserClass(FrenchTemplateParser.class);
		MediaWikiParser mediawiki_parser = parserFactory.createParser();

		wikiText = preParseWikiText(wikiText);
		ParsedPage parsedpage = mediawiki_parser.parse(wikiText);

		Document_Lara currDocument = new Document_Lara();
		currDocument.setName("mediawiki" + "");
		currListChunk = new ArrayList<Chunk_Lara>();
		
		Chunk_Lara root_Chunk = new Chunk_Lara(0, 0, 0, 0);
		root_Chunk.setType("root");
		root_Chunk.setDepRel("");
		root_Chunk.setDepId(-1);
		root_Chunk.setText("mediawiki");
		currListChunk.add(root_Chunk);

		for (Content currContent : parsedpage.getSections()) {
			runDependencyRecursive(0, currContent);
		}
		
		int index = 0; 
		for (Chunk_Lara currChunk : currListChunk) {
			currChunk.setId(index);
			index++;
		}
		currDocument.setChunk(currListChunk);

		ShiftReduce_Service shiftReduce_Service = new ShiftReduce_Service();
		currDocument = shiftReduce_Service
				.assign_shiftreduce(currDocument, 0);

		return currDocument;
		
	}
	
	
	public Document_Lara getDocument(String title_request) throws WikiApiException{
		Document_Lara currDocument = new Document_Lara();
		
		 if (wiki.existsPage(title_request)) {
		 Page page = wiki.getPage(title_request);
		
			 currDocument = runDependencyBase(page);
		 }
		 else {
			 System.out.println("Page " + title_request + " does not exist");
		 }
		 
		return currDocument;
	}
	
	
	
	public static Document_Lara runDependencyBase(Page page)
			throws WikiTitleParsingException {
		/**
		 * ParserFactory
		 */
		MediaWikiParserFactory parserFactory = new MediaWikiParserFactory(
				Language.french);
		// Add ParserTemplate
		parserFactory.setTemplateParserClass(FrenchTemplateParser.class);
		MediaWikiParser mediawiki_parser = parserFactory.createParser();

		String wikiText = page.getText();
		wikiText = preParseWikiText(wikiText);
		ParsedPage parsedpage = mediawiki_parser.parse(wikiText);

		Document_Lara currDocument = new Document_Lara();
		currDocument.setName(page.getTitle() + "");
		currListChunk = new ArrayList<Chunk_Lara>();
		
		Chunk_Lara root_Chunk = new Chunk_Lara(0, 0, 0, 0);
		root_Chunk.setType("root");
		root_Chunk.setDepRel("");
		root_Chunk.setDepId(-1);
		root_Chunk.setText(page.getTitle()+"");
		currListChunk.add(root_Chunk);

		for (Content currContent : parsedpage.getSections()) {
			runDependencyRecursive(0, currContent);
		}
		int index = 0; 
		for (Chunk_Lara currChunk : currListChunk) {
			currChunk.setId(index);
			index++;
		}

		currDocument.setChunk(currListChunk);

		ShiftReduce_Service shiftReduce_Service = new ShiftReduce_Service();
		currDocument = shiftReduce_Service
				.assign_shiftreduce(currDocument, 0);

		return currDocument;
	}

	public static void runDependencyRecursive(int level, Content currContent) {

		// Section
		if (currContent instanceof Section) {
			Section currSection = (Section) currContent;

			if (currSection.getTitleElement() == null
					&& currSection.getLevel() == 2) {

				printLevel(level, "Section [" + currSection.getLevel()
						+ "] vide");
				
			} else if (currSection.getTitleElement() == null) {

				printLevel(level, "Section [" + currSection.getLevel()
						+ "] vide");
			} else {

					printLevel(level, "Section [" + currSection.getLevel()
							+ "] " + currSection.getTitleElement().getText());

				Chunk_Lara currSectionChunk = new Chunk_Lara(0, 0, 0, 0);
				currSectionChunk.setType("h" + currSection.getLevel());
				currSectionChunk.setText(currSection.getTitleElement()
						.getText());
				currSectionChunk.setLevel(level);
				currListChunk.add(currSectionChunk);
			}

			int descent_level = level + 1;
			for (Content newContent : currSection.getContentList()) {
				runDependencyRecursive(descent_level, newContent);
			}
		}
		// NestedList Container <List Debut>
		else if (currContent instanceof NestedListContainer) {
			NestedListContainer currNestedListContainer = (NestedListContainer) currContent;
			printLevel(level, "[-list-]");
			int descent_level = level + 1;
			for (Content newContent : currNestedListContainer.getNestedLists()) {
				runDependencyRecursive(descent_level, newContent);
			}
		}
		// Paragraph
		else if (currContent instanceof Paragraph) {
			// Dernier étage
			Paragraph currParagraph = (Paragraph) currContent;
			printLevel(level, "[p] " + printContentElement(currParagraph));

			Chunk_Lara currParagraphChunk = new Chunk_Lara(0, 0, 0, 0);
			currParagraphChunk.setType("p");
			currParagraphChunk.setLevel(level);
			currParagraphChunk.setText(printContentElement(currParagraph));
			currListChunk.add(currParagraphChunk);
		}
		// Item
		else if (currContent instanceof NestedListElement) {
			// Last step
			NestedListElement currContentNestedListElement = (NestedListElement) currContent;
			printLevel(level, "* "
					+ printContentElement(currContentNestedListElement));

			Chunk_Lara currItemChunk = new Chunk_Lara(0, 0, 0, 0);
			currItemChunk.setType("item");
			currItemChunk.setLevel(level);
			currItemChunk
					.setText(printContentElement(currContentNestedListElement));
			currListChunk.add(currItemChunk);
		}
		// Table
		else if (currContent instanceof Table) {
			// Do NOTHING for Table
		} else {
			// printLevel(level,"["+currContent.getClass()+"]");
		}

	}

	public static String preParseWikiText(String wikiText) {

		// Remove "Fichier::
		String str[] = wikiText.split("\n");
		String returnWiki = "";
		for (int i = 0; i < str.length; i++) {
				returnWiki += str[i] + "\n";
		}
		
		returnWiki = returnWiki.replaceAll("\\[\\[Fichier:(.)*\\]\\]","");
		
		
		// remplace <ref  name="10.1371/journal.ppat.1000212"/>
		returnWiki = returnWiki.replaceAll("<ref name=\"(.)*\"\\/>","");
		
		// remplace "<ref name=((.)*)> smdkfjs fsdmklsdf j<ref name=\\1\\/>"
		returnWiki = returnWiki.replaceAll("<ref name=((.)*)>(.)*<ref name=\\1\\/>","");
		
		// remplace <ref> par {{ref|
		// remplace </ref> par }}
		returnWiki = returnWiki.replaceAll("<ref", "{{ref| ");
		returnWiki= returnWiki.replaceAll("<\\/ref>", "}}");
		
		
		// remplace <gallery> {{gallery|
		// remplace </gallery> par }}
		returnWiki = returnWiki.replaceAll("<gallery>", "{{gallery|");
		returnWiki= returnWiki.replaceAll("<\\/gallery>", "}}");
		
		// remplace formatnum: by formatnum,
		returnWiki = returnWiki.replaceAll("formatnum:","formatnum|");
		returnWiki = returnWiki.replaceAll("Formatnum:","Formatnum|");
		
		return returnWiki;
	}



	public static String printContentElement(ContentElement currContentElement) {
		String content = currContentElement.getText();
		content = content.trim();

		return content;
	}

	public static void printLevel(int level, String text) {
		if (printFlag) {
			for (int i = 0; i < level; i++) {
				System.out.print("\t");
			}
			System.out.println(text);
		}
	}

	
	
	
	

}
