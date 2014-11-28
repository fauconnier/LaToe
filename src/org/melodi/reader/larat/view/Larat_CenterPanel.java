package org.melodi.reader.larat.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.html.HTMLDocument;

import org.melodi.reader.larat.controler.LaratControler;
import org.melodi.reader.larat.internal.Graphical_Component;
import org.melodi.reader.larat.internal.Item;
import org.melodi.reader.larat.internal.Segment;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.reader.larat.model.Larat_Metadata;
import org.melodi.reader.larat.view.listener.Listener_CenterButtons;
import org.melodi.reader.larat.view.listener.Listener_CenterTextPane;


public class Larat_CenterPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/*
	 * Variables from Model
	 */
	private LaratControler controler;
	private HTMLDocument currDocument;
	private LinkedList<Unit> chainUnits;
	private int currIndexUnit;
	private Unit currUnit;
	private Larat_Metadata currDocMetadata;
	
	/*
	 * Text Panel
	 */
	private CenterTextPane centerTextPane;
	private  HashMap<Unit, CenterTextPane> hashSE_Doc;
	private HashMap<Unit, Highlighter> hashSE_highlighter;
	private DefaultHighlighter.HighlightPainter currentSelectedSEPane;
	private DefaultHighlighter.HighlightPainter primerSelected;
	private DefaultHighlighter.HighlightPainter itemSelected;
	private DefaultHighlighter.HighlightPainter circonstantSelected;
	private DefaultHighlighter.HighlightPainter contextSpatioSelected;
	private DefaultHighlighter.HighlightPainter conceptSelected;
	private DefaultHighlighter.HighlightPainter clotSelected;
	private DefaultHighlighter.HighlightPainter marqRelSelected;
	
	/*
	 * Buttons
	 */
	 /* Selection Panel */
	private JButton addButton;
	private JButton clearButton;

	 /* Internal Components Panel */
	private JButton addPrimer;
	private JButton addItem;
	private JButton addContextSpatio;
	private JButton addContextTempo;
	private JButton clearThis;
	private JButton clotIcon;
	
	 /* Internal Units panel */
	private JButton addConcept;
	private JButton addCirconstant;
	private JButton addMarqRel;
	private JButton switchView;
	private boolean switch_view = false;

	 /* Status panel */
	private JLabel primer_info;
	private JLabel nbrItems_info;
	private JLabel axe_visuel_info;
	private JLabel axe_rheto_info;
	private JLabel axe_intentionnel_info;
	private JLabel axe_sem_info;
	
	public Larat_CenterPanel(LaratControler controler) {

		this.controler = controler;
		
		// 1. TextPane and Listener initialization
		Listener_CenterButtons centerButtonsListener = new Listener_CenterButtons(this,controler);
		centerTextPane = new CenterTextPane();
		Listener_CenterTextPane centerTextPaneListener = new Listener_CenterTextPane(centerTextPane, controler);
		centerTextPane.addCaretListener(centerTextPaneListener);
		centerTextPane.addMouseListener(centerTextPaneListener);
		
		// 2. Add graphicals params
		this.setLayout(new BorderLayout());
		centerTextPane.setContentType("text/html");
		centerTextPane.setEditable(false);
		JScrollPane editorScrollPane = new JScrollPane(centerTextPane);
		editorScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel centerUp = new JPanel(new BorderLayout());
		centerUp.add(editorScrollPane);

		
		// 3. Add buttons : Selection Panel
		JPanel seletionPanel = new JPanel(new GridBagLayout());
		ImageIcon plusIcon = new ImageIcon("resources/images/plus.png");
		addButton = new JButton(plusIcon);
		addButton.setVerticalTextPosition(AbstractButton.CENTER);
		addButton.setHorizontalTextPosition(AbstractButton.RIGHT); // aka LEFT,
		addButton.setMnemonic(KeyEvent.VK_D);
		addButton.setActionCommand("addUnit");

		ImageIcon clearIcon = new ImageIcon("resources/images/croix.png");
		clearButton = new JButton(clearIcon);
		clearButton.setHorizontalTextPosition(AbstractButton.LEFT);
		clearButton.setMnemonic(KeyEvent.VK_E);
		clearButton.setActionCommand("delUnit");
		clearButton.setEnabled(true);

		clearButton.addActionListener(centerButtonsListener);
		addButton.addActionListener(centerButtonsListener);
		
		JButton emptyButton = new JButton();
		emptyButton.setVisible(false);
		seletionPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Selection"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		addButtonGrid(seletionPanel, addButton, clearButton,  emptyButton,  emptyButton);
		
		
		// 4. Add buttons : Internal components panel
		ImageIcon primerIcon = new ImageIcon("resources/images/plus3.png");
		addPrimer = new JButton(primerIcon);
		addPrimer.setVerticalTextPosition(AbstractButton.CENTER);
		addPrimer.setHorizontalTextPosition(AbstractButton.RIGHT); // aka LEFT,
		addPrimer.setMnemonic(KeyEvent.VK_D);
		addPrimer.setActionCommand("addPrimer");

		ImageIcon itemIcon = new ImageIcon("resources/images/plus4.png");
		addItem = new JButton(itemIcon);
		addItem.setVerticalTextPosition(AbstractButton.CENTER);
		addItem.setHorizontalTextPosition(AbstractButton.RIGHT); // aka LEFT,
		addItem.setMnemonic(KeyEvent.VK_D);
		addItem.setActionCommand("addItem");

		ImageIcon clearItemPrimerIcon = new ImageIcon(
				"resources/images/clear2.png");
		clearThis = new JButton(clearItemPrimerIcon);
		clearThis.setVerticalTextPosition(AbstractButton.CENTER);
		clearThis.setHorizontalTextPosition(AbstractButton.RIGHT); // aka LEFT,
		clearThis.setMnemonic(KeyEvent.VK_D);
		clearThis.setActionCommand("clearAll");

		ImageIcon imageIcon = new ImageIcon("resources/images/clot.png");
		clotIcon = new JButton(imageIcon);
		clotIcon.setVerticalTextPosition(AbstractButton.CENTER);
		clotIcon.setHorizontalTextPosition(AbstractButton.RIGHT); // aka LEFT,
		clotIcon.setMnemonic(KeyEvent.VK_D);
		clotIcon.setActionCommand("cloture");

		addPrimer.addActionListener(centerButtonsListener);
		addItem.addActionListener(centerButtonsListener);
		clearThis.addActionListener(centerButtonsListener);
		clotIcon.addActionListener(centerButtonsListener);

		JPanel internalComponentsPanel = new JPanel(new GridBagLayout());
		internalComponentsPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Internal components"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		addButtonGrid(internalComponentsPanel, addPrimer, addItem, clearThis, clotIcon);
		
		
		// 5. Add buttons : Internal units
		ImageIcon conceptIcon = new ImageIcon("resources/images/addConcept.png");
		addConcept = new JButton(conceptIcon);
		addConcept.setVerticalTextPosition(AbstractButton.CENTER);
		addConcept.setHorizontalTextPosition(AbstractButton.RIGHT); // aka LEFT,
		addConcept.setMnemonic(KeyEvent.VK_D);
		addConcept.setActionCommand("addConcept");

		ImageIcon circonstantIcon = new ImageIcon("resources/images/addCirc.png");
		addCirconstant = new JButton(circonstantIcon);
		addCirconstant.setVerticalTextPosition(AbstractButton.CENTER);
		addCirconstant.setHorizontalTextPosition(AbstractButton.RIGHT); // aka LEFT,
		addCirconstant.setMnemonic(KeyEvent.VK_D);
		addCirconstant.setActionCommand("addCirconstant");

		ImageIcon switchIcon = new ImageIcon(
				"resources/images/switch.png");
		switchView = new JButton(switchIcon);
		switchView.setVerticalTextPosition(AbstractButton.CENTER);
		switchView.setHorizontalTextPosition(AbstractButton.RIGHT); // aka LEFT,
		switchView.setMnemonic(KeyEvent.VK_D);
		switchView.setActionCommand("switch");

		ImageIcon marqueurIcon = new ImageIcon("resources/images/addMarq.png");
		addMarqRel = new JButton(marqueurIcon);
		addMarqRel.setVerticalTextPosition(AbstractButton.CENTER);
		addMarqRel.setHorizontalTextPosition(AbstractButton.RIGHT); // aka LEFT,
		addMarqRel.setMnemonic(KeyEvent.VK_D);
		addMarqRel.setActionCommand("addMarqRel");

		addConcept.addActionListener(centerButtonsListener);
		addCirconstant.addActionListener(centerButtonsListener);
		switchView.addActionListener(centerButtonsListener);
		addMarqRel.addActionListener(centerButtonsListener);

		JPanel internalUnitsPanel = new JPanel(new GridBagLayout());
		internalUnitsPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Internal Units"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		addButtonGrid(internalUnitsPanel, addConcept, addCirconstant, switchView, addMarqRel);
		
		// 6. Status panel
		JPanel statusPanel = new JPanel(new GridLayout(0, 1));
		statusPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Status"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		addInformationToStatusPanel(statusPanel);
		
		
		// 7. Put together [TopPanel and BottomPanel]
		JPanel centerMiddle = new JPanel(new BorderLayout());
		JPanel topButtonPanel = new JPanel(new GridLayout(1, 0));
		topButtonPanel.add(seletionPanel);
		topButtonPanel.add(internalComponentsPanel);
		centerMiddle.add(topButtonPanel, BorderLayout.CENTER);
		
		JPanel bottomButtonPanel = new JPanel(new GridLayout());
		bottomButtonPanel.add(statusPanel);
		bottomButtonPanel.add(internalUnitsPanel);
		centerMiddle.add(bottomButtonPanel, BorderLayout.SOUTH);
		
		JSplitPane jsp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				centerUp, centerMiddle);
		jsp1.setOneTouchExpandable(true);

		this.add(jsp1);
		int iSliderPos = 99;
		((JSplitPane) jsp1).setDividerSize(6);
		((JSplitPane) jsp1).setContinuousLayout(true);
		((JSplitPane) jsp1).setResizeWeight((iSliderPos * 0.01));
		((JSplitPane) jsp1).setDividerLocation((iSliderPos * 0.01));
		
		// 8. CenterTextPane
		hashSE_Doc = new HashMap<Unit, CenterTextPane>();
		hashSE_highlighter = new HashMap<Unit, Highlighter>();
		currentSelectedSEPane = new DefaultHighlighter.DefaultHighlightPainter(
				Color.RED);
		primerSelected = new DefaultHighlighter.DefaultHighlightPainter(
				new Color(255, 228, 122)); // Jaune clair
		itemSelected = new DefaultHighlighter.DefaultHighlightPainter(
				new Color(122, 215, 255)); // Bleu clair
		conceptSelected = new DefaultHighlighter.DefaultHighlightPainter(
				new Color(179, 255, 0)); // Vert clair

		circonstantSelected = new DefaultHighlighter.DefaultHighlightPainter(
				new Color(204, 102, 204)); // Violet

		marqRelSelected = new DefaultHighlighter.DefaultHighlightPainter(
				new Color(255, 255, 0)); // Violet

		clotSelected = new DefaultHighlighter.DefaultHighlightPainter(
				new Color(255, 122, 215)); // Rose

	}
	
	public void update(HTMLDocument currDocument, LinkedList<Unit> chainUnits,
			int currIndexUnit, Larat_Metadata currDocMetadata) {

		// 1. Update model variables
		this.currDocument = currDocument;
		this.chainUnits = chainUnits;
		this.currIndexUnit = currIndexUnit;
		if(currIndexUnit != -1){
			this.currUnit = chainUnits.get(currIndexUnit);
		}else{
			this.currUnit = null;
		}
		this.currDocMetadata = currDocMetadata;

		// 2. Update CenterTextPane
		updateCenterTextPane();
		
		// 3. Update Status Panel
		updateStatusPanel();
		
	}
	
	
	private void updateCenterTextPane(){
		//TODO : clean code
		
		if ((!hashSE_Doc.containsKey(currUnit)) && (currUnit != null)) {
			CenterTextPane toTest = new CenterTextPane();
			toTest.setContentType("text/html");

//			Charset charset = Charset.forName("ISO-8859-1");
			Charset charset = Charset.forName(controler.getEncoding());
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(
							new FileInputStream(currDocMetadata.getDocPath()), charset));
					toTest.read(in, charset);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	

			pruneHTMLDocumentTree((HTMLDocument) toTest.getDocument());
			centerTextPane.setText(toTest.getText());
			centerTextPane.setCaretPosition(0);
			hashSE_Doc.put(currUnit, toTest);

			modifyHighLighterSEPane();

		} else if (currUnit != null) {
			JTextPane toSet = hashSE_Doc.get(currUnit);

			centerTextPane.setText(toSet.getText());

			// TODO : Work on caret update for long units.
//			// Last
//			if (last_current_selection_SEPane > 0
//					&& last_current_selection_SEPane < centerTextPane.getDocument()
//							.getLength()) {
//				centerTextPane.setCaretPosition(last_current_selection_SEPane);
//			} else {
//				centerTextPane.setCaretPosition(0);
//			}
			modifyHighLighterSEPane();

		} else {
			centerTextPane.setText("<html><body></body></html>");
		}
	}

	private void updateStatusPanel(){
		
		if(currUnit != null){
			if (currUnit.getPrimer() != null) {
				primer_info.setText("Amorce : 1");
			} else {
				primer_info.setText("Amorce : ");
			}
	
			if (currUnit.getItems().size() > 0) {
				nbrItems_info.setText("Items : "
						+ currUnit.getItems().size());
			} else {
				nbrItems_info.setText("Items : ");
			}
		}
		else{
			primer_info.setText("Amorce : ");
			nbrItems_info.setText("Items : ");
		}

		// TODO : don't link with Right Panel, but check in Unit
//		if (flag_visuel) {
//			currUnit.axe_visuel_info.setText("Axe visuel : 1");
//		} else {
//			currUnit.axe_visuel_info.setText("Axe visuel : ");
//		}
//
//		if (flag_rhetorique) {
//			currUnit.axe_rheto_info.setText("Axe rhéto : 1");
//		} else {
//			currUnit.axe_rheto_info.setText("Axe rhéto : ");
//		}
//
//		if (flag_intentionnel) {
//			currUnit.axe_intentionnel_info.setText("Axe inten : 1");
//		} else {
//			currUnit.axe_intentionnel_info.setText("Axe inten :");
//		}
//
//		if (flag_semantique) {
//			currUnit.axe_sem_info.setText("Axe sem : 1");
//		} else {
//			currUnit.axe_sem_info.setText("Axe sem :");
//		}
	}
	
	private void modifyHighLighterSEPane() {
		Highlighter highlighter = centerTextPane.getHighlighter();
		highlighter.removeAllHighlights();

		// Primer + concept + circonstant + relation Marker
		if (currUnit.getPrimer() != null) {
			if (currUnit.getPrimer().getConcept().size() > 0) {
				for (Segment segment : currUnit.getPrimer().getConcept()) {
					setHighLighter(segment, highlighter, conceptSelected);
				}
			}

			if (currUnit.getPrimer().getCirconstant().size() > 0) {
				for (Segment segment : currUnit.getPrimer().getCirconstant()) {
					setHighLighter(segment, highlighter, circonstantSelected);
				}
			}

			if (currUnit.getPrimer().getMarqueurRel().size() > 0) {
				for (Segment segment : currUnit.getPrimer().getMarqueurRel()) {
					setHighLighter(segment, highlighter, marqRelSelected);
				}
			}
			if (!switch_view) {
				setHighLighter(currUnit.getPrimer(), highlighter,
						primerSelected);
			}
		}
		// Items + Concept + circonstant + relation markers
		if (currUnit.getItems().size() > 0) {
			int index = 0;
			for (Item it : currUnit.getItems()) {

				if (it.getConcept().size() > 0) {
					for (Segment segment : currUnit.getItem(index)
							.getConcept()) {
						setHighLighter(segment, highlighter, conceptSelected);
					}
				}

				if (it.getCirconstant().size() > 0) {
					for (Segment segment : currUnit.getItem(index)
							.getCirconstant()) {
						setHighLighter(segment, highlighter,
								circonstantSelected);
					}
				}

				if (it.getMarqueurRel().size() > 0) {
					for (Segment segment : currUnit.getItem(index)
							.getMarqueurRel()) {
						setHighLighter(segment, highlighter, marqRelSelected);
					}
				}
				index++;
			}
			for (Item it : currUnit.getItems()) {
				if (!switch_view) {
					setHighLighter(it, highlighter, itemSelected);
				}
			}
		}
		// Closure
		if (currUnit.getClot() != null) {
			if (!switch_view) {
				setHighLighter(currUnit.getClot(), highlighter, clotSelected);
			}
		}
	}
	
	private void pruneHTMLDocumentTree(HTMLDocument doc) {
		// 0 -- 99[100 - 150]151 -- Fin
		// 200

		// Debut -- deb_current_selectin-1
		// Longueur : deb_current_selection-1 - Debut

		// fin_current_selection+1 -- Fin du document
		// Longeur : Fin - fin_current_selection+1

		int longeur2 = (doc.getLength()) - currUnit.getIndice_end();
		try {
			doc.remove(currUnit.getIndice_end(), longeur2);
			doc.remove(0, currUnit.getIndice_begin());

		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}

	}

	private static void addButtonGrid(JPanel mainFrame, JButton button1,
			JButton button2, JButton button3, JButton button4) {

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets.top = 10;
		gbc.insets.right = 10;

		/* First button. */
		gbc.gridx = 0;
		gbc.gridy = 0;
		mainFrame.add(button1, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		
		/* Second button */
		gbc.gridx = 1;
		gbc.gridy = 0;
		mainFrame.add(button2, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		
		/* Third button */
		gbc.gridx = 0;
		gbc.gridy = 1;
		mainFrame.add(button3, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		
		/* last button */
		gbc.gridx = 1;
		gbc.gridy = 1;
		mainFrame.add(button4, gbc);
	}
	
	
	private void addInformationToStatusPanel(JPanel statusPanel){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER; 
		gbc.insets = new Insets(10, 5, 0, 0);
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		
		primer_info = new JLabel("Primer :");
		statusPanel.add(primer_info, gbc);
		
		nbrItems_info = new JLabel("Items :");
		statusPanel.add(nbrItems_info, gbc);
		
		axe_visuel_info = new JLabel("Visual axis :");
		statusPanel.add(axe_visuel_info, gbc);
		
		axe_rheto_info= new JLabel("Rheto axis :");
		statusPanel.add(axe_rheto_info, gbc);
		
		axe_intentionnel_info= new JLabel("Inten axis :");
		statusPanel.add(axe_intentionnel_info, gbc);
		
		axe_sem_info = new JLabel("Sem axis :");
		statusPanel.add(axe_sem_info, gbc);
	}
	
	public void setSwitch(){
		if (switch_view) {
			switch_view = false;
		} else {
			switch_view = true;
		}
	}
	
	private Highlighter setHighLighter(Graphical_Component toSet,
			Highlighter highlighter, DefaultHighlighter.HighlightPainter painter) {

		try {
			highlighter.addHighlight(toSet.getIndice_begin(),
					toSet.getIndice_end(), painter);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return highlighter;
	}
	
}
