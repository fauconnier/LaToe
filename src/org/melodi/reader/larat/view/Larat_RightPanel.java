package org.melodi.reader.larat.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;

import org.melodi.reader.larat.controler.LaratControler;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.reader.larat.model.Larat_Metadata;
import org.melodi.reader.larat.view.listener.Listener_RightButtons;

public class Larat_RightPanel extends JPanel {

	/*
	 * Variables from Model
	 */
	private HTMLDocument currDocument;
	private LinkedList<Unit> chainUnits;
	private int currIndexUnit;
	private Unit currUnit;
	private JLabel annotation_id;
	private JLabel annotation_set_size;
	private Listener_RightButtons rightButtonsListener;

	/**
	 * Pane
	 */
	private JEditorPane commentTextPane;
	private JComboBox fonctionComboBox;

	private JButton previousButton;
	private JButton nextButton;

	/**
	 * Visual axis
	 */
	private JRadioButton verticaleJRadio;
	private JRadioButton horizontaleJRadio;
	private JRadioButton hypertextualJRadio;
	private JRadioButton navigationnelJRadio;

	/**
	 * 
	 * Rheto axis
	 */
	private JRadioButton paradigmatique;
	private JRadioButton syntagmatique;
	private JRadioButton hybride;
	private JRadioButton bivalente;

	/**
	 * Inten. axis
	 */
	private JRadioButton descriptive;
	private JRadioButton narrative;
	private JRadioButton explicative;
	private JRadioButton prescriptive;
	private JRadioButton procedurale;
	private JRadioButton argumentative;
	private JRadioButton autre_intentionnel;

	/**
	 * Sem. axis
	 */
	private JRadioButton isA;
	private JRadioButton partOf;
	private JRadioButton instanceOf;
	private JRadioButton ontologiqueAutre;
	private JRadioButton hyperonymie;
	private JRadioButton meronymie;
	private JRadioButton homonymie;
	private JRadioButton synonymie;
	private JRadioButton multilingue;
	private JRadioButton lexicalAutre;
	private JRadioButton semantiqueAutre;

	// Valid Button
	private JButton valid;

	/**
	 * Contexte
	 */
	private JRadioButton contextuelle;
	private JRadioButton non_contextuelle;

	public Larat_RightPanel(LaratControler controler) {

		// Init.
		commentTextPane = new JEditorPane();
		commentTextPane.setText("");
		fonctionComboBox = new JComboBox(new String[] { "A1", "A2", "A3" });
		this.rightButtonsListener = new Listener_RightButtons (commentTextPane, fonctionComboBox, controler);
		annotation_id = new JLabel();
		annotation_set_size = new JLabel();

		// 1. Typology Panel
		this.setLayout(new GridLayout(0, 1));
		JPanel typologyPane = new JPanel(new GridLayout(0, 1));
		typologyPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// 1.1 Visual Axis
		JPanel axe_visuel = new JPanel(new GridLayout(0, 1));
		verticaleJRadio = new JRadioButton("verticale");
		verticaleJRadio.setMnemonic(KeyEvent.VK_C);
		verticaleJRadio.setSelected(false);
		verticaleJRadio.addActionListener(rightButtonsListener);
		verticaleJRadio.setActionCommand("Verticale");

		horizontaleJRadio = new JRadioButton("horizontale");
		horizontaleJRadio.setMnemonic(KeyEvent.VK_G);
		horizontaleJRadio.setSelected(false);
		horizontaleJRadio.addActionListener(rightButtonsListener);
		horizontaleJRadio.setActionCommand("Horizontale");

		hypertextualJRadio = new JRadioButton("hypertextuelle");
		hypertextualJRadio.setMnemonic(KeyEvent.VK_G);
		hypertextualJRadio.setSelected(false);
		hypertextualJRadio.addActionListener(rightButtonsListener);
		hypertextualJRadio.setActionCommand("Hypertextuelle");

		navigationnelJRadio = new JRadioButton("navigationnelle");
		navigationnelJRadio.setMnemonic(KeyEvent.VK_G);
		navigationnelJRadio.setSelected(false);
		navigationnelJRadio.addActionListener(rightButtonsListener);
		navigationnelJRadio.setActionCommand("Navigationnelle");

		axe_visuel.add(verticaleJRadio);
		axe_visuel.add(horizontaleJRadio);
		axe_visuel.add(hypertextualJRadio);
		axe_visuel.add(navigationnelJRadio);
		axe_visuel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Axe visuel"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// 1.2 Rheto axis
		JPanel axe_rhetorique = new JPanel(new GridLayout(0, 1));
		axe_rhetorique.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Axe rhétorique"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		paradigmatique = new JRadioButton("Paradigmatique");
		paradigmatique.setMnemonic(KeyEvent.VK_C);
		paradigmatique.setSelected(false);
		paradigmatique.addActionListener(rightButtonsListener);
		paradigmatique.setActionCommand("paradigmatique");

		syntagmatique = new JRadioButton("Syntagmatique");
		syntagmatique.setMnemonic(KeyEvent.VK_C);
		syntagmatique.setSelected(false);
		syntagmatique.addActionListener(rightButtonsListener);
		syntagmatique.setActionCommand("syntagmatique");

		hybride = new JRadioButton("Hybride");
		hybride.setMnemonic(KeyEvent.VK_C);
		hybride.setSelected(false);
		hybride.addActionListener(rightButtonsListener);
		hybride.setActionCommand("hybride");

		bivalente = new JRadioButton("Bivalente");
		bivalente.setMnemonic(KeyEvent.VK_C);
		bivalente.setSelected(false);
		bivalente.addActionListener(rightButtonsListener);
		bivalente.setActionCommand("bivalente");

		axe_rhetorique.add(paradigmatique);
		axe_rhetorique.add(syntagmatique);
		axe_rhetorique.add(hybride);
		axe_rhetorique.add(bivalente);

		// 1.3 Inten. axis
		JPanel axe_intentionnel = new JPanel(new GridLayout(0, 2));
		axe_intentionnel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Axe intentionnel"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		descriptive = new JRadioButton("descriptive");
		descriptive.setMnemonic(KeyEvent.VK_C);
		descriptive.setSelected(false);
		descriptive.addActionListener(rightButtonsListener);
		descriptive.setActionCommand("descriptive");

		narrative = new JRadioButton("narrative");
		narrative.setMnemonic(KeyEvent.VK_C);
		narrative.setSelected(false);
		narrative.addActionListener(rightButtonsListener);
		narrative.setActionCommand("narrative");

		explicative = new JRadioButton("explicative ");
		explicative.setMnemonic(KeyEvent.VK_C);
		explicative.setSelected(false);
		explicative.addActionListener(rightButtonsListener);
		explicative.setActionCommand("explicative");

		prescriptive = new JRadioButton("prescriptive");
		prescriptive.setMnemonic(KeyEvent.VK_C);
		prescriptive.setSelected(false);
		prescriptive.addActionListener(rightButtonsListener);
		prescriptive.setActionCommand("prescriptive");

		procedurale = new JRadioButton("procedurale");
		procedurale.setMnemonic(KeyEvent.VK_C);
		procedurale.setSelected(false);
		procedurale.addActionListener(rightButtonsListener);
		procedurale.setActionCommand("procedurale");

		argumentative = new JRadioButton("argumentative");
		argumentative.setMnemonic(KeyEvent.VK_C);
		argumentative.setSelected(false);
		argumentative.addActionListener(rightButtonsListener);
		argumentative.setActionCommand("argumentative");

		autre_intentionnel = new JRadioButton("autre intent.");
		autre_intentionnel.setMnemonic(KeyEvent.VK_C);
		autre_intentionnel.setSelected(false);
		autre_intentionnel.addActionListener(rightButtonsListener);
		autre_intentionnel.setActionCommand("autre_intentionnel");

		axe_intentionnel.add(descriptive);
		axe_intentionnel.add(narrative);
		axe_intentionnel.add(explicative);
		axe_intentionnel.add(prescriptive);
		axe_intentionnel.add(procedurale);
		axe_intentionnel.add(argumentative);
		axe_intentionnel.add(autre_intentionnel);

		JPanel container = new JPanel(new GridLayout(1, 1));
		JPanel container2 = new JPanel(new GridLayout(0, 1));

		container.add(axe_visuel);
		container.add(axe_rhetorique);
		container2.add(container);
		container2.add(axe_intentionnel);
		typologyPane.add(container2);

		// 1.4 Sem axis
		JPanel axe_semantique = new JPanel(new GridLayout(0, 2));
		axe_semantique.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Axe semantique"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// a. Visée_ontologique
		JPanel visee_ontologique = new JPanel(new GridLayout(0, 1));
		visee_ontologique.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("A visée ontologique"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		isA = new JRadioButton("isA");
		isA.setMnemonic(KeyEvent.VK_C);
		isA.setSelected(false);
		isA.addActionListener(rightButtonsListener);
		isA.setActionCommand("isA");

		partOf = new JRadioButton("partOf");
		partOf.setMnemonic(KeyEvent.VK_C);
		partOf.setSelected(false);
		partOf.addActionListener(rightButtonsListener);
		partOf.setActionCommand("partOf");

		instanceOf = new JRadioButton("instanceOf");
		instanceOf.setMnemonic(KeyEvent.VK_C);
		instanceOf.setSelected(false);
		instanceOf.addActionListener(rightButtonsListener);
		instanceOf.setActionCommand("instanceOf");

		ontologiqueAutre = new JRadioButton("ontologiqueAutre");
		ontologiqueAutre.setMnemonic(KeyEvent.VK_C);
		ontologiqueAutre.setSelected(false);
		ontologiqueAutre.addActionListener(rightButtonsListener);
		ontologiqueAutre.setActionCommand("autre_ontologique");

		visee_ontologique.add(isA);
		visee_ontologique.add(partOf);
		visee_ontologique.add(instanceOf);
		visee_ontologique.add(ontologiqueAutre);

		// b. MétaLinguistique
		JPanel metalinguistique = new JPanel(new GridLayout(0, 1));
		metalinguistique.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Metalinguistique"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		hyperonymie = new JRadioButton("hyperonymie");
		hyperonymie.setMnemonic(KeyEvent.VK_C);
		hyperonymie.setSelected(false);
		hyperonymie.addActionListener(rightButtonsListener);
		hyperonymie.setActionCommand("hyperonymie");

		meronymie = new JRadioButton("meronymie");
		meronymie.setMnemonic(KeyEvent.VK_C);
		meronymie.setSelected(false);
		meronymie.addActionListener(rightButtonsListener);
		meronymie.setActionCommand("meronymie");

		homonymie = new JRadioButton("homonymie");
		homonymie.setMnemonic(KeyEvent.VK_C);
		homonymie.setSelected(false);
		homonymie.addActionListener(rightButtonsListener);
		homonymie.setActionCommand("homonymie");

		synonymie = new JRadioButton("synonymie");
		synonymie.setMnemonic(KeyEvent.VK_C);
		synonymie.setSelected(false);
		synonymie.addActionListener(rightButtonsListener);
		synonymie.setActionCommand("synonymie");

		multilingue = new JRadioButton("multilingue");
		multilingue.setMnemonic(KeyEvent.VK_C);
		multilingue.setSelected(false);
		multilingue.addActionListener(rightButtonsListener);
		multilingue.setActionCommand("multilingue");

		lexicalAutre = new JRadioButton("lexicalAutre");
		lexicalAutre.setMnemonic(KeyEvent.VK_C);
		lexicalAutre.setSelected(false);
		lexicalAutre.addActionListener(rightButtonsListener);
		lexicalAutre.setActionCommand("autre_lexical");

		metalinguistique.add(hyperonymie);
		metalinguistique.add(meronymie);
		metalinguistique.add(homonymie);
		metalinguistique.add(synonymie);
		metalinguistique.add(multilingue);
		metalinguistique.add(lexicalAutre);

		axe_semantique.add(visee_ontologique);
		axe_semantique.add(metalinguistique);
		typologyPane.add(axe_semantique);

		// c. Autre sémantique
		JPanel autre_semantique = new JPanel(new GridLayout(0, 1));
		autre_semantique.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Autre semantique"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		semantiqueAutre = new JRadioButton("semantiqueAutre");
		semantiqueAutre.setMnemonic(KeyEvent.VK_C);
		semantiqueAutre.setSelected(false);
		semantiqueAutre.addActionListener(rightButtonsListener);
		semantiqueAutre.setActionCommand("autre_semantique");

		autre_semantique.add(semantiqueAutre);
		axe_semantique.add(autre_semantique);

		// d. Contextuelle
		JPanel contextuel = new JPanel(new GridLayout(0, 1));
		contextuel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Contexte"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		contextuelle = new JRadioButton("contextuelle");
		contextuelle.setMnemonic(KeyEvent.VK_C);
		contextuelle.setSelected(false);
		contextuelle.addActionListener(rightButtonsListener);
		contextuelle.setActionCommand("contextuelle");
		non_contextuelle = new JRadioButton("non contextuelle");
		non_contextuelle.setMnemonic(KeyEvent.VK_C);
		non_contextuelle.setSelected(false);
		non_contextuelle.addActionListener(rightButtonsListener);
		non_contextuelle.setActionCommand("non_contextuelle");

		contextuel.add(contextuelle);
		contextuel.add(non_contextuelle);
		axe_semantique.add(contextuel);

		// 2. Comment, Validation and Control Panels
		JPanel centerMiddle = new JPanel(new BorderLayout());
		JPanel middleValid_Comment = new JPanel(new GridLayout());

		// 2.1 Validation Panel
		JPanel validPane = new JPanel(new GridBagLayout());
		ImageIcon validIcon = new ImageIcon("resources/images/v.png");
		valid = new JButton(validIcon);
		valid.setVerticalTextPosition(AbstractButton.CENTER);
		valid.setHorizontalTextPosition(AbstractButton.RIGHT); // aka LEFT, for
		valid.setMnemonic(KeyEvent.VK_D);
		valid.setActionCommand("valid");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		valid.addActionListener(rightButtonsListener);
		validPane.add(valid, gbc);

		// 2.2 Comment Panel
		JPanel commentPane = new JPanel(new GridLayout(0, 1));
		JScrollPane paneScrollPane = new JScrollPane(commentTextPane);
		paneScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(10, 10));
		paneScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Comment"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		commentPane.add(paneScrollPane);
		validPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Validation"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		middleValid_Comment.add(commentPane);
		middleValid_Comment.add(validPane);
		centerMiddle.add(middleValid_Comment);

		// 2.3 Control Panel
		JPanel centerBottom = new JPanel(new GridLayout(0, 1));
		JPanel information = new JPanel(new GridBagLayout());
		setDetailsPanel(information);
		JPanel bottom = new JPanel(new GridLayout(0, 1));
		bottom.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Control"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		bottom.add(information);
		centerBottom.add(bottom, BorderLayout.CENTER);

		// 3. Put together and add
		JPanel allPanels = new JPanel(new GridLayout(0, 1));
		allPanels.add(centerMiddle);
		allPanels.add(centerBottom);
		typologyPane.add(allPanels);
		this.add(typologyPane);

	}

	public void update(HTMLDocument currDocument, LinkedList<Unit> chainUnits,
			int currIndexUnit, Larat_Metadata currDocMetadata) {

		// 1. Update model variables
		this.currDocument = currDocument;
		this.chainUnits = chainUnits;
		this.currIndexUnit = currIndexUnit;

		if (currIndexUnit != -1) {
			this.currUnit = chainUnits.get(currIndexUnit);

			// 2. Update panel
			this.annotation_id.setText(currIndexUnit + "");
			this.annotation_set_size.setText(chainUnits.size() + "");
			selectedFalseAll();

			updateAxis();
			updateComment();
		} else {
			this.annotation_id.setText("");
			this.annotation_set_size.setText("");
			selectedFalseAll();
		}

	}

	private void updateComment() {
		commentTextPane.setText(currUnit.getAnnotation().getComment());
	}

	private void updateAxis() {

		// 1. Visual Axis
		if (currUnit.getAxe_visuel().equals("Horizontale")) {
			horizontaleJRadio.setSelected(true);
		}
		if (currUnit.getAxe_visuel().equals("Verticale")) {
			verticaleJRadio.setSelected(true);
		}

		if (currUnit.getAxe_visuel_nav_hyp().equals("Hypertextuelle")) {
			hypertextualJRadio.setSelected(true);
		}
		if (currUnit.getAxe_visuel_nav_hyp().equals("Navigationnelle")) {
			navigationnelJRadio.setSelected(true);
		}

		// 2. Rheto Axis
		if (currUnit.getAxe_rhetorique().equals("paradigmatique")) {
			paradigmatique.setSelected(true);
		}
		if (currUnit.getAxe_rhetorique().equals("syntagmatique")) {
			syntagmatique.setSelected(true);
		}
		if (currUnit.getAxe_rhetorique().equals("hybride")) {
			hybride.setSelected(true);
		}
		if (currUnit.getAxe_rhetorique().equals("bivalente")) {
			bivalente.setSelected(true);
		}

		// 3. Intentionnel Axis
		for (String value : currUnit.getAxe_intentionnel()) {
			if (value.equals("descriptive")) {
				descriptive.setSelected(true);
			}
			if (value.equals("narrative")) {
				narrative.setSelected(true);
			}
			if (value.equals("explicative")) {
				explicative.setSelected(true);
			}
			if (value.equals("prescriptive")) {
				prescriptive.setSelected(true);
			}
			if (value.equals("procedurale")) {
				procedurale.setSelected(true);
			}
			if (value.equals("argumentative")) {
				argumentative.setSelected(true);
			}
			if (value.equals("autre_intentionnel")) {
				autre_intentionnel.setSelected(true);
			}
		}

		// 4. Sem axis
		if (currUnit.getAxe_semantique().equals("isA")) {
			isA.setSelected(true);
		} else if (currUnit.getAxe_semantique().equals("partOf")) {
			partOf.setSelected(true);
		} else if (currUnit.getAxe_semantique().equals("instanceOf")) {
			instanceOf.setSelected(true);
		} else if (currUnit.getAxe_semantique().equals("autre_ontologique")) {
			ontologiqueAutre.setSelected(true);
		} else if (currUnit.getAxe_semantique().equals("hyperonymie")) {
			hyperonymie.setSelected(true);
		} else if (currUnit.getAxe_semantique().equals("meronymie")) {
			meronymie.setSelected(true);
		} else if (currUnit.getAxe_semantique().equals("homonymie")) {
			homonymie.setSelected(true);
		} else if (currUnit.getAxe_semantique().equals("synonymie")) {
			synonymie.setSelected(true);
		} else if (currUnit.getAxe_semantique().equals("multilingue")) {
			multilingue.setSelected(true);
		} else if (currUnit.getAxe_semantique().equals("autre_lexical")) {
			lexicalAutre.setSelected(true);
		} else if (currUnit.getAxe_semantique().equals("autre_semantique")) {
			semantiqueAutre.setSelected(true);
		}

		// Contextual
		if (currUnit.getAxe_semantiqueContext().equals("contextuelle")) {
			contextuelle.setSelected(true);
		} else {
			non_contextuelle.setSelected(true);
		}

	}

	private void selectedFalseAll() {

		// 1. Visual Axis
		horizontaleJRadio.setSelected(false);
		verticaleJRadio.setSelected(false);
		hypertextualJRadio.setSelected(false);
		navigationnelJRadio.setSelected(false);

		// 2. Retho axis
		paradigmatique.setSelected(false);
		syntagmatique.setSelected(false);
		hybride.setSelected(false);
		bivalente.setSelected(false);

		// 3. Intent. axis
		descriptive.setSelected(false);
		narrative.setSelected(false);
		explicative.setSelected(false);
		prescriptive.setSelected(false);
		procedurale.setSelected(false);
		argumentative.setSelected(false);
		autre_intentionnel.setSelected(false);

		// 4. Sem axis
		isA.setSelected(false);
		partOf.setSelected(false);
		instanceOf.setSelected(false);
		ontologiqueAutre.setSelected(false);
		hyperonymie.setSelected(false);
		meronymie.setSelected(false);
		homonymie.setSelected(false);
		synonymie.setSelected(false);
		multilingue.setSelected(false);
		lexicalAutre.setSelected(false);
		semantiqueAutre.setSelected(false);

		// Contextual or not
		contextuelle.setSelected(false);
		non_contextuelle.setSelected(false);

		// Comment pane
		commentTextPane.setText("");
	}

	private void setDetailsPanel(JPanel mainFrame) {
		// TODO Clean code
		ImageIcon leftButtonIcon = new ImageIcon("resources/images/left.gif");
		ImageIcon rightButtonIcon = new ImageIcon("resources/images/right.gif");
		previousButton = new JButton("previous", leftButtonIcon);
		previousButton.setVerticalTextPosition(AbstractButton.CENTER);
		previousButton.setHorizontalTextPosition(AbstractButton.RIGHT);
		previousButton.setMnemonic(KeyEvent.VK_D);
		previousButton.setActionCommand("previous");
		previousButton.setEnabled(true);

		nextButton = new JButton("next", rightButtonIcon);
		nextButton.setHorizontalTextPosition(AbstractButton.LEFT);
		nextButton.setMnemonic(KeyEvent.VK_E);
		nextButton.setActionCommand("next");
		nextButton.setEnabled(true);
		previousButton.addActionListener(rightButtonsListener);
		nextButton.addActionListener(rightButtonsListener);

		previousButton.setToolTipText("Retourner à la SE précédente.");
		nextButton.setToolTipText("Avancer à la SE suivante.");

		JLabel nomLabel = new JLabel("ID SE :");
		JLabel prenomLabel = new JLabel("Nb. SE :");
		JLabel fonctionLabel = new JLabel("Opérateur :");

		JSeparator separator = new JSeparator();

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(10, 5, 0, 0);
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 4;

		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.insets = new Insets(5, 5, 0, 0);

		gbc.gridx = gbc.gridy = gbc.gridwidth = gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		gbc.insets = new Insets(0, 5, 0, 0);
		mainFrame.add(nomLabel, gbc);
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(3, 5, 0, 5);
		gbc.anchor = GridBagConstraints.BASELINE;

		mainFrame.add(annotation_id, gbc);

		gbc.gridx = gbc.gridwidth = gbc.gridheight = 1;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		gbc.insets = new Insets(0, 5, 0, 0);
		mainFrame.add(prenomLabel, gbc);

		gbc.gridx = gbc.gridy = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		gbc.insets = new Insets(3, 5, 0, 5);
		mainFrame.add(annotation_set_size, gbc);

		gbc.gridx = gbc.gridwidth = gbc.gridheight = 1;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		mainFrame.add(fonctionLabel, gbc);

		gbc.gridx = 2;
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.weightx = 1.;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.BASELINE;
		mainFrame.add(fonctionComboBox, gbc);

		gbc.gridx = 4;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.; // supprimons le poids.
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		gbc.insets = new Insets(0, 3, 0, 5);

		gbc.gridy = 4;
		gbc.gridx = gbc.gridwidth = gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		gbc.insets = new Insets(5, 5, 0, 0);

		gbc.gridx = 2;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;
		gbc.insets = new Insets(0, 2, 0, 0);

		gbc.gridx = 3;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;

		gbc.gridy = 5;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(3, 5, 0, 5);
		mainFrame.add(separator, gbc);

		gbc.gridy = 6;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weighty = 1.;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
		mainFrame.add(nextButton, gbc);

		gbc.gridy = 6;
		gbc.gridx = 0;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.weighty = 1.;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.BASELINE_LEADING;

		mainFrame.add(previousButton, gbc);

	}
}
