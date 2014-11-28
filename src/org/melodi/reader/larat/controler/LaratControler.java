package org.melodi.reader.larat.controler;

import java.awt.Component;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import org.melodi.reader.larat.internal.Cloture;
import org.melodi.reader.larat.internal.Graphical_Component;
import org.melodi.reader.larat.internal.Item;
import org.melodi.reader.larat.internal.Primer;
import org.melodi.reader.larat.internal.Segment;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.reader.larat.model.LaratModel;
import org.melodi.reader.larat.view.Larat_CenterPanel;

public class LaratControler {

	protected LaratModel laratModel;
	private File currFileDirectory;
	private String currPathDocFile;

	/*
	 * Variables from models
	 */
	private HTMLDocument currDocument;
	private LinkedList<Unit> chainUnits;
	private int currIndexUnit;

	/*
	 * Variables from Left textPaneSelection
	 */
	public String current_selection_textPane = "";
	public int deb_current_selection_textPane = -1;
	public int fin_current_selection_textPane = -1;

	/*
	 * Variables from Center textPaneSelection
	 */
	public String current_selection_centerPane = "";
	public int start_current_selection_centerPane = -1;
	public int end_current_selection_centerPane = -1;

	/**
	 * Class which controls LaratModel
	 * 
	 * @param abstractModel
	 */
	public LaratControler(LaratModel abstractModel) {
		this.laratModel = abstractModel;
		this.currDocument = new HTMLDocument();
		this.chainUnits = new LinkedList<Unit>();
		this.currIndexUnit = -1;
	}

	public void reset() {
		this.laratModel.reset();
	}

	public void notifyUpdate(HTMLDocument currDocument,
			LinkedList<Unit> chainUnits, int currIndexUnit) {
		this.currDocument = currDocument;
		this.chainUnits = chainUnits;
		this.currIndexUnit = currIndexUnit;
	}

	public void notifyCaretLeftUpdate(String current_selection_textPane,
			int deb_current_selection_textPane,
			int fin_current_selection_textPane) {
		this.current_selection_textPane = current_selection_textPane;
		this.deb_current_selection_textPane = deb_current_selection_textPane;
		this.fin_current_selection_textPane = fin_current_selection_textPane;
	}

	public void notifyCaretCenterUpdate(String current_selection_centerPane,
			int start_current_selection_centerPane,
			int end_current_selection_centerPane) {
		this.current_selection_centerPane = current_selection_centerPane;
		this.start_current_selection_centerPane = start_current_selection_centerPane;
		this.end_current_selection_centerPane = end_current_selection_centerPane;
	}

	private boolean controlStatus() {
		return (currDocument != null && chainUnits != null && currIndexUnit != -1);
	}

	public void actionPerformedOpenFile() {
		System.out.println("actionPerformedOpenFile");

		if (currFileDirectory == null) {
			try {
				currFileDirectory = new File(".").getCanonicalFile();
			} catch (IOException e) {
			}
		}
		JFileChooser dialogue = new JFileChooser(currFileDirectory);
		dialogue.showOpenDialog(null);

		if (dialogue.getSelectedFile() != null) {
			currPathDocFile = "" + dialogue.getSelectedFile();
			currFileDirectory = dialogue.getCurrentDirectory();
			laratModel.openFile(currPathDocFile);
		}
	}

	public void actionPerformedValid(String comment, String annotateur) {
		System.out.println("actionPerformedValid");
		laratModel.valid(comment, annotateur);
	}

	public void actionPerformedQuit() {
		System.exit(0);
	}

	public void actionPerformedAbout() {
		JDialog myJD = new JDialog();
		JTextPane about = new JTextPane();
		about.setContentType("text/html");
		Charset charset = Charset.forName("ISO-8859-1");

		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					"resources/properties/about.html"), charset));
			about.read(in, charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JFrame about_frame = new JFrame("About");
		about_frame.setSize(800, 600);
		about_frame.setVisible(true);
		about_frame.add(about);
		about_frame.setLocationRelativeTo(null);
	}

	public void actionPerformedNextUnit() {

		if (controlStatus()) {
			Unit currUnit = chainUnits.get(currIndexUnit);
			if (currIndexUnit + 1 < chainUnits.size()) {
				laratModel.selectUnit(chainUnits.get(currIndexUnit + 1));
			} else {
				laratModel.selectUnit(currUnit);
			}
		}
	}

	public void actionPerformedPreviousUnit() {
		if (controlStatus()) {
			Unit currUnit = chainUnits.get(currIndexUnit);
			if (currIndexUnit - 1 >= 0) {
				laratModel.selectUnit(chainUnits.get(currIndexUnit - 1));
			} else {
				laratModel.selectUnit(currUnit);
			}
		}
	}

	public void actionPerformedAddSE() {

		if (!current_selection_textPane.equals("")
				&& deb_current_selection_textPane != -1
				&& fin_current_selection_textPane != -1) {
			System.out.println("actionPerformedAddUnit");
			Unit current_SE = new Unit();
			current_SE.setIndice_begin(deb_current_selection_textPane);
			current_SE.setIndice_end(fin_current_selection_textPane);
			current_SE.setText(current_selection_textPane);

			laratModel.addUnit(current_SE);
		}
	}

	public void actionPerformedDelSE() {
		System.out.println("actionPerformedDelUnit");
		if (currIndexUnit != -1) {
			Unit currUnit = chainUnits.get(currIndexUnit);
			laratModel.removeUnit(currUnit);
		}
	}

	public void actionPerformedAddPrimer() {
		System.out.println("actionPerformedAddPrimer");

		if (controlStatus() && !current_selection_centerPane.equals("")
				&& start_current_selection_centerPane != -1
				&& end_current_selection_centerPane != -1) {
			Primer currPrimer = new Primer();

			currPrimer.setText(current_selection_centerPane);
			currPrimer.setIndice_begin(start_current_selection_centerPane);
			currPrimer.setIndice_end(end_current_selection_centerPane);

			Unit currUnit = chainUnits.get(currIndexUnit);
			laratModel.addPrimer(currUnit, currPrimer);
		}
	}

	public void actionPerformedAddItem() {
		if (controlStatus() && !current_selection_centerPane.equals("")
				&& start_current_selection_centerPane != -1
				&& end_current_selection_centerPane != -1) {
			System.out.println("actionPerformedAddItem");
			Item currItem = new Item();

			currItem.setText(current_selection_centerPane);
			currItem.setIndice_begin(start_current_selection_centerPane);
			currItem.setIndice_end(end_current_selection_centerPane);

			Unit currUnit = chainUnits.get(currIndexUnit);
			laratModel.addItem(currUnit, currItem);
		}
	}

	public void actionPerformedAddClot() {

		if (controlStatus() && !current_selection_centerPane.equals("")
				&& start_current_selection_centerPane != -1
				&& end_current_selection_centerPane != -1) {
			System.out.println("actionPerformedAddClot");

			Cloture currClosure = new Cloture();
			currClosure.setText(current_selection_centerPane);
			currClosure.setIndice_begin(start_current_selection_centerPane);
			currClosure.setIndice_end(end_current_selection_centerPane);

			Unit currUnit = chainUnits.get(currIndexUnit);
			laratModel.addClosure(currUnit, currClosure);
		}
	}

	public void actionPerformedClearThis() {
		if (controlStatus()) {
			System.out.println("actionPerformedClearThis");
			Unit currUnit = chainUnits.get(currIndexUnit);
			laratModel.clearInternalComponents(currUnit);
		}
	}

	public void actionPerformedAddConcept(Larat_CenterPanel centerPanel) {
		// TODO : link with Model
		if (controlStatus() && !current_selection_centerPane.equals("")
				&& start_current_selection_centerPane != -1
				&& end_current_selection_centerPane != -1) {

			System.out.println("actionPerformedAddConcept");

			Unit currUnit = chainUnits.get(currIndexUnit);
			Segment segment = new Segment();
			segment.setText(current_selection_centerPane);

			segment.setIndice_begin(start_current_selection_centerPane);
			segment.setIndice_end(end_current_selection_centerPane);

			// Ou est-ce foutu segment??
			if (whereIsThisSegment(segment).equals("primer")) {
				currUnit.getPrimer().getConcept().add(segment);

			} else if (whereIsThisSegment(segment).equals("item")) {
				// Quel Item
				int item_id = whichItem(segment);
				if (item_id == -1) {
					 JOptionPane.showMessageDialog(centerPanel,"Attention : Le concept que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
					 "Inane warning", JOptionPane.WARNING_MESSAGE);
				} else {
					currUnit.getItem(item_id).getConcept().add(segment);
				}
			} else {
				 JOptionPane.showMessageDialog( centerPanel,
				 "Attention : Le concept que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
				 "Inane warning", JOptionPane.WARNING_MESSAGE);
			}
			laratModel.refresh();
		}
	}

	public void actionPerformedAddCirconstant(Larat_CenterPanel centerPanel) {

		if (controlStatus() && !current_selection_centerPane.equals("")
				&& start_current_selection_centerPane != -1
				&& end_current_selection_centerPane != -1) {
			System.out.println("actionPerformedAddCirconstant");
			Unit currUnit = chainUnits.get(currIndexUnit);

			Segment segment = new Segment();
			segment.setText(current_selection_centerPane);
			segment.setIndice_begin(start_current_selection_centerPane);
			segment.setIndice_end(end_current_selection_centerPane);

			// Ou est-ce foutu segment??
			if (whereIsThisSegment(segment).equals("primer")) {
				currUnit.getPrimer().getCirconstant().add(segment);
			} else if (whereIsThisSegment(segment).equals("item")) {
				// Quel Item
				int item_id = whichItem(segment);
				if (item_id == -1) {
					 JOptionPane
					 .showMessageDialog(
							 centerPanel,
					 "Attention : Le circonstant que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
					 "Inane warning", JOptionPane.WARNING_MESSAGE);
				} else {
					currUnit.getItem(item_id).getCirconstant().add(segment);
				}
			} else {
				 JOptionPane
				 .showMessageDialog(
						 centerPanel,
				 "Attention : Le circonstant que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
				 "Inane warning", JOptionPane.WARNING_MESSAGE);
			}
			laratModel.refresh();
		}
	}

	public void actionPerformedAddMarqRel(Larat_CenterPanel centerPanel) {

		if (controlStatus() && !current_selection_centerPane.equals("")
				&& start_current_selection_centerPane != -1
				&& end_current_selection_centerPane != -1) {
			System.out.println("actionPerformedAddMarqRel");
			Unit currUnit = chainUnits.get(currIndexUnit);
			Segment segment = new Segment();
			segment.setText(current_selection_centerPane);
			segment.setIndice_begin(start_current_selection_centerPane);
			segment.setIndice_end(end_current_selection_centerPane);

			// Ou est-ce foutu segment??
			if (whereIsThisSegment(segment).equals("primer")) {
				currUnit.getPrimer().getMarqueurRel().add(segment);
			} else if (whereIsThisSegment(segment).equals("item")) {
				// Quel Item
				int item_id = whichItem(segment);
				if (item_id == -1) {
					 JOptionPane
					 .showMessageDialog(
							 centerPanel,
					 "Attention : Le marqueur de relation que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
					 "Inane warning", JOptionPane.WARNING_MESSAGE);
				} else {
					currUnit.getItem(item_id).getMarqueurRel().add(segment);
				}
			} else {
				 JOptionPane
				 .showMessageDialog(
						 centerPanel,
				 "Attention : Le marqueur de relation que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
				 "Inane warning", JOptionPane.WARNING_MESSAGE);
			}
			laratModel.refresh();
		}
	}

	
	public void actionPerformedSwitch(Larat_CenterPanel centerPanel) {
		System.out.println("actionPerformedSwitch()");
		centerPanel.setSwitch();
		laratModel.refresh();
	}
	
	public void actionSelectAnnotationAtChar(int pos) {
		Unit newCurrUnit = getUnitFromPosition(pos);
		laratModel.selectUnit(newCurrUnit);
	}

	public void actionSelectSubAnnotationAtChar(int pos) {
		// TODO : link with larat_model
		System.out.println("selectSubAnnotationAtChar : " + pos);

		Graphical_Component myGraphicalCompo = getGraphicalComponentFromPosition(pos);
		if (myGraphicalCompo.getIndice_begin() != 0
				&& myGraphicalCompo.getIndice_end() != 0) {
			laratModel.removeInternalUnits(myGraphicalCompo);
		} else {
			System.out.println("Souris cliquee SEPane: Aucune selection");
			laratModel.refresh();
		}

	}

	public void actionSelectProperty(String property) {
		System.out.println("Property " + property);
		laratModel.addProperty(property);
	}

	private Graphical_Component getGraphicalComponentFromPosition(int pos) {
		Unit currUnit = chainUnits.get(currIndexUnit);

		if (currUnit != null) {
			if (currUnit.getPrimer() != null) {

				// Concept in Primer
				if (currUnit.getPrimer().hasConcept()) {
					int index = 0;
					for (Segment segment : currUnit.getPrimer().getConcept()) {

						if (segment.getIndice_begin() <= pos
								&& segment.getIndice_end() >= pos) {
							return currUnit.getPrimer().getConcept().get(index);
						}
						index++;
					}
				}

				// Circ. in Primer
				if (currUnit.getPrimer() != null) {

					if (currUnit.getPrimer().getCirconstant().size() > 0) {
						int index_circonstant = 0;
						for (Segment segment : currUnit.getPrimer()
								.getCirconstant()) {
							if (segment.getIndice_begin() <= pos
									&& segment.getIndice_end() >= pos) {
								return currUnit.getPrimer().getCirconstant()
										.get(index_circonstant);

							}
							index_circonstant++;
						}
					}
				}

				// Relation Marker in Primer
				if (currUnit.getPrimer() != null) {
					if (currUnit.getPrimer().getMarqueurRel().size() > 0) {
						for (Segment segment : currUnit.getPrimer()
								.getMarqueurRel()) {

							if (segment.getIndice_begin() <= pos
									&& segment.getIndice_end() >= pos) {
								return segment;
							}
						}
					}
				}
				if (currUnit.getPrimer().getIndice_begin() <= pos
						&& currUnit.getPrimer().getIndice_end() >= pos) {
					return currUnit.getPrimer();
				}
			}

			// Concept in items
			if (currUnit.getItems().size() > 0) {
				int index_item = 0;
				for (Item it : currUnit.getItems()) {
					if (it.hasConcept()) {
						int index = 0;
						for (Segment segment : it.getConcept()) {
							if (segment.getIndice_begin() <= pos
									&& segment.getIndice_end() >= pos) {
								return currUnit.getItem(index_item)
										.getConcept().get(index);
							}
							index++;
						}
					}
					index_item++;
				}
			}

			// Circ. in items
			if (currUnit.getItems().size() > 0) {
				int index_item = 0;
				for (Item it : currUnit.getItems()) {
					if (it.hasCirconstant()) {
						int index_segment = 0;
						for (Segment segment : it.getCirconstant()) {
							if (segment.getIndice_begin() <= pos
									&& segment.getIndice_end() >= pos) {
								return currUnit.getItem(index_item)
										.getCirconstant().get(index_segment);
							}
							index_segment++;
						}
					}
					index_item++;
				}
			}

			// Rel. Marker in items
			if (currUnit.getItems().size() > 0) {
				int index_item = 0;
				for (Item it : currUnit.getItems()) {
					if (it.getMarqueurRel().size() > 0) {
						for (Segment segment : it.getMarqueurRel()) {
							if (segment.getIndice_begin() <= pos
									&& segment.getIndice_end() >= pos) {
								return segment;
							}
						}
					}
					index_item++;
				}
			}

			// Item
			if (currUnit.getItems().size() > 0) {
				for (Item it : currUnit.getItems()) {
					if (it.getIndice_begin() <= pos
							&& it.getIndice_end() >= pos) {
						return it;
					}
				}
			}

			// Closire
			if (currUnit.getClot() != null) {
				if (currUnit.getClot().getIndice_begin() <= pos
						&& currUnit.getClot().getIndice_end() >= pos) {
					return currUnit.getClot();
				}
			}
		}

		return new Primer();
	}

	private Unit getUnitFromPosition(int pos) {
		int i = 0;
		int last_Begin = 0;
		int last_End = 0;
		for (Iterator<Unit> it2 = chainUnits.listIterator(0); it2.hasNext(); i++) {
			Unit currUnit = it2.next();

			if ((currUnit.getIndice_begin() <= pos)
					&& (currUnit.getIndice_end() >= pos)) {
				last_Begin = currUnit.getIndice_begin();
				last_End = currUnit.getIndice_end();
			}
		}
		return getUnitFromStartEnd(last_Begin, last_End);
	}

	private Unit getUnitFromStartEnd(int deb, int fin) {
		Unit targetUnit = null;
		int i = 0;
		for (Iterator<Unit> it2 = chainUnits.listIterator(0); it2.hasNext(); i++) {
			Unit currUnit = it2.next();
			if ((currUnit.getIndice_begin() == deb)
					&& (currUnit.getIndice_end() == fin)) {
				targetUnit = currUnit;
			}
		}
		return targetUnit;
	}

	private String whereIsThisSegment(Segment segment) {
		Unit currUnit = chainUnits.get(currIndexUnit);
		if (currUnit.getPrimer() != null
				&& segment.getIndice_begin() >= currUnit.getPrimer()
						.getIndice_begin()
				&& segment.getIndice_end() <= currUnit.getPrimer()
						.getIndice_end()) {
			return "primer";
		} else if (currUnit.getItems().size() > 0
				&& segment.getIndice_begin() >= currUnit.getItem(0)
						.getIndice_begin()
				&& segment.getIndice_end() <= currUnit.getItem(
						currUnit.getItems().size() - 1).getIndice_end()) {
			return "item";
		} else {
			return "error";
		}
	}

	private int whichItem(Segment segment) {
		Unit currUnit = chainUnits.get(currIndexUnit);
		int index = 0;
		for (Item item : currUnit.getItems()) {

			if (segment.getIndice_begin() >= item.getIndice_begin()
					&& segment.getIndice_end() <= item.getIndice_end()) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	public String getEncoding(){
		return laratModel.getEncoding();
	}
}
