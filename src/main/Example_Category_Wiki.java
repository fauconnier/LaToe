package main;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import logicalobjects.Document_Lara;

import org.latoe.layoutanalysis.wikipedia.Wikipedia_Service;

import de.tudarmstadt.ukp.wikipedia.api.Category;
import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiPageNotFoundException;

public class Example_Category_Wiki {

	
	public static void main(String[] args) throws WikiApiException, IOException, InterruptedException {
		
		/**
		 * Classe d'exemple pour extraire des articles liés à un catégorie Wikipédia
		 */
		
        // configure the database connection parameters
        DatabaseConfiguration dbConfig = new DatabaseConfiguration();
        dbConfig.setHost("localhost");
        dbConfig.setDatabase("FRWIKISEPTEMBER");
        dbConfig.setUser("");
        dbConfig.setPassword("");
        dbConfig.setLanguage(Language.french);

        

        // Create a new German wikipedia.
        Wikipedia wiki = new Wikipedia(dbConfig);
        Wikipedia_Service wikipedia_service = new Wikipedia_Service();
        // Get the category "Säugetiere" (mammals)
        String title = "Vin";
        Category cat;
        try {
            cat = wiki.getCategory(title);
        } catch (WikiPageNotFoundException e) {
            throw new WikiApiException("Category " + title + " does not exist");
        }
        
        ArrayList<String> already_seen = new ArrayList<String>();
        
        Set<String> vin_category_articles = new TreeSet<String>();
        for (Page p : cat.getArticles()) {
        	
        	if(!already_seen.contains(p.getTitle().getPlainTitle())){
        		System.out.println("Add " + p.getTitle().getPlainTitle());
        		vin_category_articles.add(p.getTitle().getPlainTitle());
        		already_seen.add(p.getTitle().getPlainTitle());
        	}
        }
        
        // Get the pages categorized under each subcategory of "Towns in Germany".
        for (Category townCategory : cat.getDescendants()) {
            for (Page p : townCategory.getArticles()) {
            	
            	if(!already_seen.contains(p.getTitle().getPlainTitle())){
            		System.out.println("Add + " + p.getTitle().getPlainTitle());
                    vin_category_articles.add(p.getTitle().getPlainTitle());
                	already_seen.add(p.getTitle().getPlainTitle());
            	}
            }
        }
        
        
        PrintWriter pw = new PrintWriter(new FileWriter("/media/dd/dump_simple.txt"));
        int index=1;
        for(String page_name :  vin_category_articles){
        	
        	Page currPage = wiki.getPage(page_name);
        	System.out.println(index + "/"+ vin_category_articles.size() + " " + currPage.getTitle());
        	index++;
         	Document_Lara currDocument = wikipedia_service.getDocument(currPage);
         	
         	String plainText = currDocument.getTextWithMFM(false);
         	pw.write(plainText + " ");
         	pw.flush();
        }
        pw.close();

	}
}
