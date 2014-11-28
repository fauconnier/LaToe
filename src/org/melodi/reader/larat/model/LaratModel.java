package org.melodi.reader.larat.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JTextPane;
import javax.swing.text.Highlighter;
import javax.swing.text.html.HTMLDocument;

import org.melodi.reader.larat.internal.Annotation;
import org.melodi.reader.larat.internal.Cloture;
import org.melodi.reader.larat.internal.Graphical_Component;
import org.melodi.reader.larat.internal.Item;
import org.melodi.reader.larat.internal.Items;
import org.melodi.reader.larat.internal.Primer;
import org.melodi.reader.larat.internal.Segment;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.reader.larat.view.LaratView;

public class LaratModel {

	private LaratView obs;

	/*
	 * Document variables
	 */
	private HTMLDocument currDocument;
	private String currDocName;
	private String currDocPath;
	private Larat_Metadata currDocMetadata;
	private String encoding;

	/*
	 * Units variables
	 */
	private LinkedList<Unit> chainUnits;
	private int currIndexUnit;

	/**
	 * Constructor
	 */
	public LaratModel() {
		chainUnits = new LinkedList<Unit>();
		currIndexUnit = -1;
		encoding = "ISO-8859-1"; //Par défaut, encodage latin-1
	}
	
	public LaratModel(LinkedList<Unit> chainUnits){
		this.chainUnits = chainUnits;
		currIndexUnit = -1;
	}

	public void addObserver(LaratView obs) {
		this.obs = obs;
	}

	public void notifyObserver(HTMLDocument currDocument,
			LinkedList<Unit> chainUnits, int currIndexUnit,
			Larat_Metadata currDocMetadata) {
		if(obs != null)
			obs.update(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}

	public void refresh() {
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}

	public void reset() {
		System.out.println("Reset");
		currDocument = new HTMLDocument();
		chainUnits = new LinkedList<Unit>();
		currIndexUnit = -1;
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}
	
	
	public void selectUnit(Unit currUnit) {
		currIndexUnit = chainUnits.indexOf(currUnit);
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}
	
	public void selectUnit(int currIndexUnit){
		this.currIndexUnit = currIndexUnit;
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}
	
	public void valid(){
		valid("","");
	}
	
	
	public void valid(String comment, String author){
		
		if (chainUnits.size() != 0) {
			Annotation annotation = new Annotation();
			annotation.setAuthor(author);
			annotation.setComment(comment);
			Date current_date = new Date();
			annotation.setDate(current_date);
			annotation.setDocumentName(currDocMetadata.getDocName());
			Unit currUnit = chainUnits.get(currIndexUnit);
			currUnit.setAnnotation(annotation);
			
			if (currDocMetadata.getDocPath() != null ) {
				Larat_inputoutput io_mig = new Larat_inputoutput();
				io_mig.setChain(chainUnits);
				io_mig.setPath(currDocMetadata.getDocPath());
				io_mig.writeThis();
			} else {
			}
			
		}
		else{
			System.err.println("Attention, pas d'écriture car chaîne vide pour " + currDocName);
			
			
		}
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}

	/**
	 * Open a HTML Document and returns number of chars in this doc.
	 * 
	 * @param currPathDocFile
	 */
	public void openFile(String currPathDocFile) {
		System.out.println("Ouverture de " + currPathDocFile);

		// 1. RenderHTML
		JTextPane renderHTMLDocument = new JTextPane();
		renderHTMLDocument.setContentType("text/html");
		Charset charset = Charset.forName(encoding); //ISO-8859-1
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(currPathDocFile), charset));
			renderHTMLDocument.read(in, charset);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderHTMLDocument.setCaretPosition(0);
		currDocument = (HTMLDocument) renderHTMLDocument.getDocument();
		currDocMetadata = new Larat_Metadata();

		// 2. getAnnotation
		Larat_inputoutput io_mig = new Larat_inputoutput();
		io_mig.setPath(currPathDocFile);
		if (io_mig.hisXMLVersion(currPathDocFile)) {
			io_mig.readThis(currPathDocFile);

			chainUnits = io_mig.getChain();
			Iterator it = chainUnits.listIterator();
			currIndexUnit = 0;

		} else {
			System.out.println("initDoc : DocXML non present.");
		}

		// 3. getName of Document
		if (File.separator.equals("\\")) {// Windows
			System.out.println("Windows");
			String str[] = currPathDocFile.split("\\\\");
			currDocName = str[str.length - 1];
		} else {// Unix, MacOS
			String str[] = currPathDocFile.split(File.separator);
			currDocName = str[str.length - 1];
		}
		System.out.println("Name " + currDocName);
		currDocMetadata.setDocName(currDocName);
		currDocMetadata.setDocPath(currPathDocFile);

		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}

	public void addUnit(Unit currUnit) {
		chainUnits.add(currUnit);
		Collections.sort(chainUnits);
		currIndexUnit = chainUnits.indexOf(currUnit);
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}
	
	public Unit getUnit(int currIndexUnit){
		return chainUnits.get(currIndexUnit);
	}
	
	public LinkedList<Unit> getAllUnits(){
		return chainUnits;
	}
	
	public void setAllUnits(LinkedList<Unit> currChain){
		this.chainUnits = currChain;
	}
	
	public void resetAllUnits(){
		this.chainUnits = new LinkedList<Unit>();
	}

	public void removeUnit(Unit currUnit) {
		currIndexUnit = chainUnits.indexOf(currUnit);

		Unit newCurrUnit = new Unit();
		if (currIndexUnit - 1 >= 0) {
			newCurrUnit = chainUnits.get(currIndexUnit - 1);
			chainUnits.remove(currUnit);
			currIndexUnit = chainUnits.indexOf(newCurrUnit);
		} else if (currIndexUnit + 1 < chainUnits.size()) {
			newCurrUnit = chainUnits.get(currIndexUnit + 1);
			chainUnits.remove(currUnit);
			currIndexUnit = chainUnits.indexOf(newCurrUnit);
		} else {
			chainUnits.remove(currUnit);
			currIndexUnit = -1;
		}
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}

	public void addPrimer(Unit currUnit, Primer currPrimer) {
		currUnit.setPrimer(currPrimer);
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}

	public void addItem(Unit currUnit, Item item) {
		currUnit.addItem(item);
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}

	public void addClosure(Unit currUnit, Cloture currClosure) {
		currUnit.setClot(currClosure);
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}

	public void clearInternalComponents(Unit currUnit) {
		currUnit.setPrimer(null);
		Items emptyList = new Items();
		currUnit.setItems(emptyList);
		currUnit.setClot(null);
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}

	public void removeInternalUnits(Graphical_Component gc) {
		// TODO : clean code and change name

		Unit currUnit = chainUnits.get(currIndexUnit);
		// Suppression du concept Primer
		if (currUnit.getPrimer() != null && currUnit.getPrimer().hasConcept()) {

			if (!currUnit.getPrimer().getConcept().isEmpty()) {

				int index = 0;
				int final_del = -1;
				for (Segment segment : currUnit.getPrimer().getConcept()) {
					if (segment.getIndice_begin() == gc.getIndice_begin()
							&& segment.getIndice_end() == gc.getIndice_end()) {
						final_del = index;
					}
					index++;
				}
				if (final_del != -1) {
					currUnit.getPrimer().getConcept().remove(final_del);
				}
			}
		}

		// Suppression du concept Items
		if (currUnit.getItems() != null) {
			int index_item = 0;
			int index_item_del = 0;
			int index_segment_del = -1;
			for (Item item : currUnit.getItems()) {
				int index_segment = 0;
				for (Segment segment : currUnit.getItem(index_item)
						.getConcept()) {
					if (segment.getIndice_begin() == gc.getIndice_begin()
							&& segment.getIndice_end() == gc.getIndice_end()) {

						index_item_del = index_item;
						index_segment_del = index_segment;
					}
					index_segment++;
				}
				index_item++;
			}

			if (index_segment_del != -1) {
				currUnit.getItem(index_item_del).getConcept()
						.remove(index_segment_del);
			}
		}

		// Suppression des circonstant dans Primer
		if (currUnit.getPrimer() != null
				&& currUnit.getPrimer().hasCirconstant()) {
			if (!currUnit.getPrimer().getCirconstant().isEmpty()) {
				int final_del = -1;
				int index_segment = 0;
				for (Segment segment : currUnit.getPrimer().getCirconstant()) {
					if (segment.getIndice_begin() == gc.getIndice_begin()
							&& segment.getIndice_end() == gc.getIndice_end()) {
						final_del = index_segment;
					}
					index_segment++;
				}
				if (final_del != -1) {
					currUnit.getPrimer().getCirconstant().remove(final_del);
				}
			}
		}

		// Suppression des circonstant dans l'item
		if (currUnit.getItems() != null) {
			int index_item = 0;
			int index_item_del = 0;
			int index_segment_del = -1;
			for (Item item : currUnit.getItems()) {

				int index_segment = 0;
				for (Segment segment : currUnit.getItem(index_item)
						.getCirconstant()) {
					if (segment.getIndice_begin() == gc.getIndice_begin()
							&& segment.getIndice_end() == gc.getIndice_end()) {
						index_item_del = index_item;
						index_segment_del = index_segment;
					}
					index_segment++;
				}
				index_item++;
			}
			if (index_segment_del != -1) {
				currUnit.getItem(index_item_del).getCirconstant()
						.remove(index_segment_del);
			}
		}

		// Suppression du MarqRel dans le primer
		if (currUnit.getPrimer() != null
				&& currUnit.getPrimer().getMarqueurRel().size() > 0) {
			if (!currUnit.getPrimer().getMarqueurRel().isEmpty()) {
				int final_del = -1;
				int index_segment = 0;
				for (Segment segment : currUnit.getPrimer().getMarqueurRel()) {
					if (segment.getIndice_begin() == gc.getIndice_begin()
							&& segment.getIndice_end() == gc.getIndice_end()) {
						final_del = index_segment;
					}
					index_segment++;
				}
				if (final_del != -1) {
					currUnit.getPrimer().getMarqueurRel().remove(final_del);
				}
			}
		}
		// Suppression du MarqRel dans l'item
		if (currUnit.getItems() != null) {
			int index_item = 0;
			int index_item_del = 0;
			int index_segment_del = -1;
			for (Item item : currUnit.getItems()) {
				int index_segment = 0;
				for (Segment segment : currUnit.getItem(index_item)
						.getMarqueurRel()) {
					if (segment.getIndice_begin() == gc.getIndice_begin()
							&& segment.getIndice_end() == gc.getIndice_end()) {
						index_item_del = index_item;
						index_segment_del = index_segment;
					}
					index_segment++;
				}
				index_item++;
			}
			if (index_segment_del != -1) {
				currUnit.getItem(index_item_del).getMarqueurRel()
						.remove(index_segment_del);
			}
		}
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}

	public void addProperty(String property) {
		// TODO : do a generic annotation tool
		
		if(currIndexUnit != -1){
		Unit currUnit = chainUnits.get(currIndexUnit);
		
		// 1. Visual Axis
		if (property.equals("Horizontale")) {
			currUnit.setAxe_visuel(property);
		}
		if (property.equals("Verticale")) {
			currUnit.setAxe_visuel(property);
		}

		if (property.equals("Hypertextuelle")) {
			currUnit.setAxe_visuel_nav_hyp(property);
		}
		if (property.equals("Navigationnelle")) {
			currUnit.setAxe_visuel_nav_hyp(property);
		}

		// 2. Rheto Axis
		if (property.equals("paradigmatique")) {
			currUnit.setAxe_rhetorique(property);
		}
		if (property.equals("syntagmatique")) {
			currUnit.setAxe_rhetorique(property);
		}
		if (property.equals("hybride")) {
			currUnit.setAxe_rhetorique(property);
		}
		if (property.equals("bivalente")) {
			currUnit.setAxe_rhetorique(property);
		}

		// 3. Intentionnel Axis

		if (property.equals("descriptive")) {
			currUnit.addAxe_intentionnel(property);
		}
		if (property.equals("narrative")) {
			currUnit.addAxe_intentionnel(property);
		}
		if (property.equals("explicative")) {
			currUnit.addAxe_intentionnel(property);
		}
		if (property.equals("prescriptive")) {
			currUnit.addAxe_intentionnel(property);
		}
		if (property.equals("procedurale")) {
			currUnit.addAxe_intentionnel(property);
		}
		if (property.equals("argumentative")) {
			currUnit.addAxe_intentionnel(property);
		}
		if (property.equals("autre_intentionnel")) {
			currUnit.addAxe_intentionnel(property);
		}

		// 4. Sem axis
		if (property.equals("isA")) {
			currUnit.setAxe_semantique(property);
		} 
		if (property.equals("partOf")) {
			currUnit.setAxe_semantique(property);
		}
		if (property.equals("instanceOf")) {
			currUnit.setAxe_semantique(property);
		} 
		if (property.equals("autre_ontologique")) {
			currUnit.setAxe_semantique(property);
		} 
		if (property.equals("hyperonymie")) {
			currUnit.setAxe_semantique(property);
		} 
		if (property.equals("meronymie")) {
			currUnit.setAxe_semantique(property);
		} 
		if (property.equals("homonymie")) {
			currUnit.setAxe_semantique(property);
		} 
		if (property.equals("synonymie")) {
			currUnit.setAxe_semantique(property);
		} 
		if (property.equals("multilingue")) {
			currUnit.setAxe_semantique(property);
		} 
		if (property.equals("autre_lexical")) {
			currUnit.setAxe_semantique(property);
		} 
		if (property.equals("autre_semantique")) {
			currUnit.setAxe_semantique(property);
		}

		// Contextual
		if (property.equals("contextuelle")) {
			currUnit.setAxe_semantiqueCircon(property);
		} 
		if (property.equals("non_contextuelle")) {
			currUnit.setAxe_semantiqueCircon(property);
		}
		}
		
		notifyObserver(currDocument, chainUnits, currIndexUnit, currDocMetadata);
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public HTMLDocument getDocumentHTML() {
		return currDocument;
	}

	public void setDocumentHTML(HTMLDocument currDocument) {
		this.currDocument = currDocument;
	}
	
	
	

}
