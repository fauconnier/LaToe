package org.melodi.reader.larat.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;

import org.jdom2.Text;
import org.melodi.reader.larat.controler.LaratControler;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.reader.larat.model.Larat_Metadata;
import org.melodi.reader.larat.view.highlighter.RectHighlightPainter;
import org.melodi.reader.larat.view.highlighter.RectanglePainter;
import org.melodi.reader.larat.view.listener.Listener_LeftTextPane;

public class Larat_LeftPanel extends JPanel {

	public LeftTextPane leftTextPane;
	private static final long serialVersionUID = 1L;

	DefaultHighlighter.HighlightPainter pastSelected;
	DefaultHighlighter.HighlightPainter currentSelected;

	/*
	 * Variables from Moddel
	 */
	String docName;
	HTMLDocument currDocument;
	LinkedList<Unit> chainUnits;
	int currIndexUnit;
	Larat_Metadata currDocMetadata;

	public Larat_LeftPanel(LaratControler controler) {

		// 1. TextPane and Listener initialization
		this.leftTextPane = new LeftTextPane();
		Listener_LeftTextPane leftTextPaneListener = new Listener_LeftTextPane(
				leftTextPane, controler);
		leftTextPane.addCaretListener(leftTextPaneListener);
		leftTextPane.addMouseListener(leftTextPaneListener);

		// 2. Add graphicals params
		this.setLayout(new BorderLayout());
		leftTextPane.setFont(new Font("Serif", Font.PLAIN, 12));
		leftTextPane.setEditable(false);
		JScrollPane areaScrollPane = new JScrollPane(leftTextPane);
		areaScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(5000, 5000));
		areaScrollPane.setMinimumSize(new Dimension(400, 400));

		docName = "Text";
		areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(docName),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)),
				areaScrollPane.getBorder()));
		this.add(areaScrollPane, BorderLayout.CENTER);

		// 3. Units markers
		currentSelected = new DefaultHighlighter.DefaultHighlightPainter(
				Color.RED);
		pastSelected = new DefaultHighlighter.DefaultHighlightPainter(
				Color.LIGHT_GRAY);
	}

	public void update(HTMLDocument currDocument, LinkedList<Unit> chainUnits,
			int currIndexUnit, Larat_Metadata currDocMetadata) {

		// 1. Update model variables
		if (currDocument != null) {
			this.currDocument = currDocument;
			this.chainUnits = chainUnits;
			this.currIndexUnit = currIndexUnit;
			this.currDocMetadata = currDocMetadata;
			this.docName = currDocMetadata.getDocName();

			// 2. Print Document
			leftTextPane.setContentType("text/html");
			leftTextPane.setDocument(currDocument);
			if (currIndexUnit != -1) {
				leftTextPane.setCaretPosition(chainUnits.get(currIndexUnit)
						.getIndice_begin());
			}

			// 3. Highlight units
			highlightUnits(chainUnits, currIndexUnit);
		}
	}

	private void highlightUnits(LinkedList<Unit> chainUnits, int currIndexUnit) {
		// TODO Clean code

		// 1. remove HighLighters in leftTextPane
		leftTextPane.getHighlighter().removeAllHighlights();

		// 2. Highlight units
		if (!chainUnits.isEmpty() && currIndexUnit != -1) {
			Unit currUnit = chainUnits.get(currIndexUnit);
			int index = chainUnits.indexOf(currUnit);
			int i = 0;
			setHighLighterForm(chainUnits.get(index).getIndice_begin(),
					chainUnits.get(index).getIndice_end(), leftTextPane,
					currentSelected);
			for (Iterator<Unit> it2 = chainUnits.listIterator(0); it2.hasNext(); i++) {
				Unit mySE = it2.next();
				if (i != index) {
					setHighLighterForm(mySE.getIndice_begin(),
							mySE.getIndice_end(), leftTextPane, pastSelected);
				}
			}
		}
	}

	private void setHighLighterForm(int begin, int end, JTextPane editorPane,
			DefaultHighlighter.HighlightPainter painter) {

		try {
			Highlighter highlighter = editorPane.getHighlighter();
			highlighter.addHighlight(begin, end, painter);

		} catch (BadLocationException e2) {
			e2.printStackTrace();
		}
	}

}
