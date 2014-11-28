package org.melodi.reader.larat.tools;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.html.HTMLDocument;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.melodi.reader.larat.internal.Annotation;
import org.melodi.reader.larat.internal.Cloture;
import org.melodi.reader.larat.internal.Graphical_Component;
import org.melodi.reader.larat.internal.Item;
import org.melodi.reader.larat.internal.Items;
import org.melodi.reader.larat.internal.Primer;
import org.melodi.reader.larat.internal.Segment;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.reader.larat.model.Larat_inputoutput;
import org.melodi.reader.larat.view.Larat_CenterPanel;
import org.melodi.reader.larat.view.Larat_LeftPanel;
import org.melodi.reader.larat.view.Larat_RightPanel;

public class Larat_ParentPanel {

//	private static final long serialVersionUID = 1L;
//
//	boolean mode_v2 = true;
//	String mode_state = "2";
//
//	/**
//		 * 
//		 */
//	File repertoireCourant;
//
//	/**
//	 * Axe visuel
//	 */
//	final String axe_visuel_horizontal = "Horizontale";
//	final String axe_visuel_vertical = "Verticale";
//
//	final String axe_visuel_nav_hyp_Hypertext = "Hypertextuelle";
//	final String axe_visuel_nav_hyp_Navig = "Navigationnelle";
//	final String axe_visuel_nav_hyp_Nothing = "Nothing";
//
//	/**
//	 * Axe rhétorique
//	 */
//	final String axe_rhetorique_paradigmatique = "paradigmatique";
//	final String axe_rhetorique_syntagmatique = "syntagmatique";
//	final String axe_rhetorique_hybride = "hybride";
//	final String axe_rhetorique_bivalente = "bivalente";
//
//	/**
//	 * Axe intentionnel
//	 */
//	final String axe_intentionnel_descriptive = "descriptive";
//	final String axe_intentionnel_narrative = "narrative";
//	final String axe_intentionnel_explicative = "explicative";
//	final String axe_intentionnel_prescriptive = "prescriptive";
//	final String axe_intentionnel_procedurale = "procedurale";
//	final String axe_intentionnel_argumentative = "argumentative";
//	final String axe_intentionnel_autreIntentionnel = "autre_intentionnel";
//
//	/**
//	 * Axe sémantique
//	 */
//	final String axe_semantique_isA = "isA";
//	final String axe_semantique_partOf = "partOf";
//	final String axe_semantique_instanceOf = "instanceOf";
//	final String axe_semantique_autreOntologique = "autre_ontologique";
//	final String axe_semantique_hyperonymie = "hyperonymie";
//	final String axe_semantique_meronymie = "meronymie";
//	final String axe_semantique_homonymie = "homonymie";
//	final String axe_semantique_synonymie = "synonymie";
//	final String axe_semantique_multilingue = "multilingue";
//	final String axe_semantique_autreLexical = "autre_lexical";
//	final String axe_semantique_autreSemantique = "autre_semantique";
//
//	/*
//	 * Axe sémantique context
//	 */
//	final String axe_semantique_contextuelle = "contextuelle";
//	final String axe_semantique_non_contextuelle = "non_contextuelle";
//
//	/**
//	 * Logger
//	 */
//	private static Logger logger = Logger.getLogger(Larat_ParentPanel.class);
//
//	/**
//	 * Document
//	 */
//	public String document_path;
//	public String document_name;
//	public HTMLDocument current_document;
//
//	/**
//	 * States
//	 */
//	boolean stateTreatCurentSE = false;
//	boolean stateWaitForNewSE = false;
//	boolean stateWaitForNewDoc = true;
//	boolean stateRecSE = false;
//
//	/**
//	 * Panel MIG
//	 */
//	Larat_LeftPanel leftPane;
//	Larat_CenterPanel centerPane;
//	Larat_RightPanel rightPane;
//
//	/**
//	 * Pane
//	 */
//	public JTextPane textPane;
//	public JTextPane SEPane;
//	public JTextPane alignPane;
//
//	/**
//	 * Current Chain_SE
//	 */
//	public LinkedList<Unit> chain_SE;
//	public ListIterator<Unit> it;
//	public HashMap<Unit, JTextPane> hashSE_Doc;
//	public HashMap<Unit, Highlighter> hashSE_highlighter;
//
//	/**
//	 * Current SE
//	 */
//	public Unit current_SE;
//	public Items current_SE_items;
//	public Primer current_SE_primer;
//
//	/**
//	 * TextPane
//	 */
//	public String current_selection_textPane;
//	public int deb_current_selection_textPane;
//	public int fin_current_selection_textPane;
//
//	/**
//	 * SEPane
//	 */
//	public String current_selection_SEPane;
//	public int deb_current_selection_SEPane;
//	public int fin_current_selection_SEPane;
//	public int last_current_selection_SEPane;
//
//	/**
//	 * HighLighter
//	 */
//	/*
//	 * TextPane
//	 */
//	DefaultHighlighter currentHighLighter;
//	DefaultHighlighter.HighlightPainter pastSelected;
//	DefaultHighlighter.HighlightPainter currentSelectedRec;
//	DefaultHighlighter.HighlightPainter currentSelectedAdd;
//	/*
//	 * SEPane
//	 */
//	DefaultHighlighter.HighlightPainter currentSelectedSEPane;
//	DefaultHighlighter.HighlightPainter primerSelected;
//	DefaultHighlighter.HighlightPainter itemSelected;
//	DefaultHighlighter.HighlightPainter circonstantSelected;
//	DefaultHighlighter.HighlightPainter contextSpatioSelected;
//	DefaultHighlighter.HighlightPainter conceptSelected;
//	DefaultHighlighter.HighlightPainter clotSelected;
//	DefaultHighlighter.HighlightPainter marqRelSelected;
//
//	boolean switch_view = false;
//
//	/*
//	 * Labels en tout genre
//	 */
//	JLabel SE_id_in_chain;
//	JLabel chain_SE_size;
//
//	public Larat_ParentPanel() {
//
//		/**
//		 * IO
//		 */
//
//		/**
//		 * Logger
//		 */
//		PropertyConfigurator.configure("resources/properties/log4j.properties");
//		logger.info("Demarrage de l'application");
//
//		/**
//		 * SE
//		 */
//		// cf. initDoc();
//		SE_id_in_chain = new JLabel("0");
//		chain_SE_size = new JLabel("0");
//		chain_SE = new LinkedList<Unit>();
//		it = chain_SE.listIterator();
//
//		/**
//		 * Panels
//		 */
//		// JTextPane
//		SEPane = new JTextPane();
//		SEPane.setContentType("text/html");
//		textPane = new JTextPane();
//		alignPane = new JTextPane();
//
//		// Mig_JPanel
//		// setLayout(new GridLayout(1, 3));
//		this.leftPane = new Larat_LeftPanel(textPane);
//		this.centerPane = new Larat_CenterPanel(this, SEPane);
//		this.rightPane = new Larat_RightPanel(this, SE_id_in_chain,
//				chain_SE_size);
//
//		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
//				leftPane, centerPane);
//		splitPane.setOneTouchExpandable(true);
//		splitPane.setResizeWeight(0.90);
//
//		add(splitPane);
//
//		JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
//				splitPane, rightPane);
//		splitPane2.setOneTouchExpandable(true);
//		splitPane2.setResizeWeight(0.999);
//
//		add(splitPane2);
//
//		/**
//		 * TextPane
//		 */
//		pastSelected = new DefaultHighlighter.DefaultHighlightPainter(
//				Color.LIGHT_GRAY);
//		currentSelectedAdd = new DefaultHighlighter.DefaultHighlightPainter(
//				Color.GREEN);
//		currentSelectedRec = new DefaultHighlighter.DefaultHighlightPainter(
//				Color.RED);
//
//		hashSE_Doc = new HashMap<Unit, JTextPane>();
//		hashSE_highlighter = new HashMap<Unit, Highlighter>();
//
//		/**
//		 * SEPane
//		 */
//		currentSelectedSEPane = new DefaultHighlighter.DefaultHighlightPainter(
//				Color.RED);
//		primerSelected = new DefaultHighlighter.DefaultHighlightPainter(
//				new Color(255, 228, 122)); // Jaune clair
//		itemSelected = new DefaultHighlighter.DefaultHighlightPainter(
//				new Color(122, 215, 255)); // Bleu clair
//		conceptSelected = new DefaultHighlighter.DefaultHighlightPainter(
//				new Color(179, 255, 0)); // Vert clair
//
//		circonstantSelected = new DefaultHighlighter.DefaultHighlightPainter(
//				new Color(204, 102, 204)); // Violet
//
//		marqRelSelected = new DefaultHighlighter.DefaultHighlightPainter(
//				new Color(255, 255, 0)); // Violet
//
//		clotSelected = new DefaultHighlighter.DefaultHighlightPainter(
//				new Color(255, 122, 215)); // Rose
//
//		/**
//		 * State
//		 */
//		setStateWaitForNewDoc();
//
//	}
//
//	public int initDoc() {
//		logger.info("initDoc : Ouverture de " + document_path);
//		textPane.setContentType("text/html");
//
//		// textPane.getDocument().putProperty( "Ignore-Charset", "true" );
//		// textPane.getDocument().putProperty("IgnoreCharsetDirective",
//		// Boolean.TRUE);
//
//		Charset charset = Charset.forName("ISO-8859-1");
//		try {
//			BufferedReader in = new BufferedReader(new InputStreamReader(
//					new FileInputStream(document_path), charset));
//			textPane.read(in, charset);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		// textPane.setText(Tools.getText(document_path));
//		textPane.setCaretPosition(0);
//
//		logger.info("initDoc : Document de longeur :"
//				+ textPane.getDocument().getLength());
//
//		this.current_document = (HTMLDocument) textPane.getDocument();
//		// Changement graphique
//		textPane.getHighlighter().removeAllHighlights();
//
//		Larat_inputoutput io_mig = new Larat_inputoutput();
//		io_mig.setPath(document_path);
//		if (io_mig.hisXMLVersion(document_path)) {
//			io_mig.readThis(document_path);
//			logger.info("initDoc : DocXML present ... Chargement de "
//					+ io_mig.getNewPath());
//			chain_SE = io_mig.getChain();
//			it = chain_SE.listIterator();
//			current_SE = chain_SE.get(0);
//
//			hashSE_Doc = new HashMap<Unit, JTextPane>();
//			hashSE_highlighter = new HashMap<Unit, Highlighter>();
//
//			refreshSEPane();
//			refreshTextPane();
//		} else {
//			logger.info("initDoc : DocXML non present.");
//			// Internal SE
//			chain_SE = new LinkedList<Unit>();
//			it = chain_SE.listIterator();
//
//			SE_id_in_chain.setText("0");
//			chain_SE_size.setText("0");
//			current_SE = null;
//
//			current_SE_items = null;
//			current_SE_primer = null;
//
//			hashSE_Doc = new HashMap<Unit, JTextPane>();
//			hashSE_highlighter = new HashMap<Unit, Highlighter>();
//
//			refreshSEPane();
//		}
//
//		// Récupartion du nom de document
//		if (File.separator.equals("\\")) {
//			System.out.println("Windows");
//			String str[] = document_path.split("\\\\");
//			document_name = str[str.length - 1];
//			logger.info("initDoc : Split. Name :" + document_name);
//		} else {
//			String str[] = document_path.split(File.separator);
//			document_name = str[str.length - 1];
//			logger.info("initDoc : Split. Name :" + document_name);
//		}
//
//		setStateWaitForNewSE();
//		init();
//		return textPane.getDocument().getLength();
//	}
//
//	public void refreshSimpleAnnotation() {
//
//		if (current_SE != null) {
//			rightPane.valid.setEnabled(true);
//
//			boolean flag_visuel = false;
//			boolean flag_rhetorique = false;
//			boolean flag_intentionnel = false;
//			boolean flag_semantique = false;
//
//			if (rightPane.horizontaleJRadio.isSelected()
//					|| rightPane.verticaleJRadio.isSelected()
//					|| rightPane.navigationnelJRadio.isSelected()
//					|| rightPane.hypertextualJRadio.isSelected()) {
//				flag_visuel = true;
//			}
//
//			if (rightPane.paradigmatique.isSelected()
//					|| rightPane.syntagmatique.isSelected()
//					|| rightPane.hybride.isSelected()
//					|| rightPane.bivalente.isSelected()) {
//				flag_rhetorique = true;
//			}
//
//			if (rightPane.descriptive.isSelected()
//					|| rightPane.narrative.isSelected()
//					|| rightPane.explicative.isSelected()
//					|| rightPane.prescriptive.isSelected()
//					|| rightPane.procedurale.isSelected()
//					|| rightPane.argumentative.isSelected()
//					|| rightPane.autre_intentionnel.isSelected()) {
//				flag_intentionnel = true;
//			}
//
//			if (rightPane.isA.isSelected() || rightPane.partOf.isSelected()
//					|| rightPane.instanceOf.isSelected()
//					|| rightPane.ontologiqueAutre.isSelected() ||
//
//					rightPane.hyperonymie.isSelected()
//					|| rightPane.meronymie.isSelected()
//					|| rightPane.homonymie.isSelected()
//					|| rightPane.synonymie.isSelected()
//					|| rightPane.multilingue.isSelected()
//					|| rightPane.lexicalAutre.isSelected()
//					|| rightPane.semantiqueAutre.isSelected()) {
//				flag_semantique = true;
//			}
//			
//			
//
//			if (current_SE.getPrimer() != null) {
//				centerPane.primer_info.setText("Amorce : 1");
//			} else {
//				centerPane.primer_info.setText("Amorce : ");
//			}
//
//			if (current_SE.getItems().size() > 0) {
//				centerPane.nbrItems_info.setText("Items : "
//						+ current_SE.getItems().size());
//			} else {
//				centerPane.nbrItems_info.setText("Items : ");
//			}
//
//			if (flag_visuel) {
//				centerPane.axe_visuel_info.setText("Axe visuel : 1");
//			} else {
//				centerPane.axe_visuel_info.setText("Axe visuel : ");
//			}
//
//			if (flag_rhetorique) {
//				centerPane.axe_rheto_info.setText("Axe rhéto : 1");
//			} else {
//				centerPane.axe_rheto_info.setText("Axe rhéto : ");
//			}
//
//			if (flag_intentionnel) {
//				centerPane.axe_intentionnel_info.setText("Axe inten : 1");
//			} else {
//				centerPane.axe_intentionnel_info.setText("Axe inten :");
//			}
//
//			if (flag_semantique) {
//				centerPane.axe_sem_info.setText("Axe sem : 1");
//			} else {
//				centerPane.axe_sem_info.setText("Axe sem :");
//			}
//
//		}
//
//	}
//
//	public void refreshAnnotation() {
//
//		rightPane.valid.setEnabled(true);
//
//		boolean flag_rhetorique = true;
//		boolean flag_intentionnel = false;
//		boolean flag_semantique = true;
//		boolean flag_semantique2 = true;
//
//		if (current_SE.getAnnotation() != null) {
//
//			rightPane.commentTextPane.setText(current_SE.getAnnotation()
//					.getComment());
//			rightPane.fonctionComboBox.setSelectedItem(current_SE
//					.getAnnotation().getAuthor());
//
//			if (current_SE.getAxe_visuel().equals("Horizontale")) {
//				rightPane.horizontaleJRadio.setSelected(true);
//				rightPane.verticaleJRadio.setSelected(false);
//			} else if (current_SE.getAxe_visuel().equals("Verticale")) {
//				rightPane.horizontaleJRadio.setSelected(false);
//				rightPane.verticaleJRadio.setSelected(true);
//			} else {
//				rightPane.horizontaleJRadio.setSelected(false);
//				rightPane.verticaleJRadio.setSelected(false);
//			}
//
//			if (current_SE.getAxe_visuel_nav_hyp().equals("Hypertextuelle")) {
//				rightPane.hypertextualJRadio.setSelected(true);
//				rightPane.navigationnelJRadio.setSelected(false);
//			} else if (current_SE.getAxe_visuel_nav_hyp().equals(
//					"Navigationnelle")) {
//				rightPane.hypertextualJRadio.setSelected(false);
//				rightPane.navigationnelJRadio.setSelected(true);
//			} else {
//				rightPane.hypertextualJRadio.setSelected(false);
//				rightPane.navigationnelJRadio.setSelected(false);
//			}
//
//			// Axe rhétorique
//			if (current_SE.getAxe_rhetorique().equals("paradigmatique")) {
//				rightPane.paradigmatique.setSelected(true);
//				rightPane.syntagmatique.setSelected(false);
//				rightPane.hybride.setSelected(false);
//				rightPane.bivalente.setSelected(false);
//			} else if (current_SE.getAxe_rhetorique().equals("syntagmatique")) {
//				rightPane.paradigmatique.setSelected(false);
//				rightPane.syntagmatique.setSelected(true);
//				rightPane.hybride.setSelected(false);
//				rightPane.bivalente.setSelected(false);
//			} else if (current_SE.getAxe_rhetorique().equals("hybride")) {
//				rightPane.paradigmatique.setSelected(false);
//				rightPane.syntagmatique.setSelected(false);
//				rightPane.hybride.setSelected(true);
//				rightPane.bivalente.setSelected(false);
//
//			} else if (current_SE.getAxe_rhetorique().equals("bivalente")) {
//				rightPane.paradigmatique.setSelected(false);
//				rightPane.syntagmatique.setSelected(false);
//				rightPane.hybride.setSelected(false);
//				rightPane.bivalente.setSelected(true);
//			} else {
//				rightPane.paradigmatique.setSelected(false);
//				rightPane.syntagmatique.setSelected(false);
//				rightPane.hybride.setSelected(false);
//				rightPane.bivalente.setSelected(false);
//				flag_rhetorique = false;
//			}
//
//			rightPane.descriptive.setSelected(false);
//			rightPane.narrative.setSelected(false);
//			rightPane.explicative.setSelected(false);
//			rightPane.prescriptive.setSelected(false);
//			rightPane.procedurale.setSelected(false);
//			rightPane.argumentative.setSelected(false);
//			rightPane.autre_intentionnel.setSelected(false);
//
//			for (String value : current_SE.getAxe_intentionnel()) {
//
//				if (value.equals("descriptive")) {
//					rightPane.descriptive.setSelected(true);
//					flag_intentionnel = true;
//				}
//				if (value.equals("narrative")) {
//					rightPane.narrative.setSelected(true);
//					flag_intentionnel = true;
//				}
//				if (value.equals("explicative")) {
//					rightPane.explicative.setSelected(true);
//					flag_intentionnel = true;
//				}
//				if (value.equals("prescriptive")) {
//					rightPane.prescriptive.setSelected(true);
//					flag_intentionnel = true;
//				}
//				if (value.equals("procedurale")) {
//					rightPane.procedurale.setSelected(true);
//					flag_intentionnel = true;
//				}
//				if (value.equals("argumentative")) {
//					rightPane.argumentative.setSelected(true);
//					flag_intentionnel = true;
//				}
//				if (value.equals("autre_intentionnel")) {
//					rightPane.autre_intentionnel.setSelected(true);
//					flag_intentionnel = true;
//				}
//			}
//
//			// Axe sémantique
//			// final String axe_semantique_isA = "isA";
//			// final String axe_semantique_partOf = "partOf";
//			// final String axe_semantique_instanceOf = "instanceOf";
//			// final String axe_semantique_autreOntologique =
//			// "autre_ontologique";
//			// final String axe_semantique_hyperonymie = "hyperonymie";
//			// final String axe_semantique_meronymie = "meronymie";
//			// final String axe_semantique_homonymie = "homonymie";
//			// final String axe_semantique_synonymie = "synonymie";
//			// final String axe_semantique_multilingue = "multilingue";
//			// final String axe_semantique_autreLexical = "autre_lexical";
//			// final String axe_semantique_autreSemantique = "autre_semantique";
//
//			// Par défaut, on enlève
//			rightPane.isA.setSelected(false);
//			rightPane.partOf.setSelected(false);
//			rightPane.instanceOf.setSelected(false);
//			rightPane.ontologiqueAutre.setSelected(false);
//
//			rightPane.hyperonymie.setSelected(false);
//			rightPane.meronymie.setSelected(false);
//			rightPane.homonymie.setSelected(false);
//			rightPane.synonymie.setSelected(false);
//			rightPane.multilingue.setSelected(false);
//			rightPane.lexicalAutre.setSelected(false);
//			rightPane.semantiqueAutre.setSelected(false);
//
//			if (current_SE.getAxe_semantique().equals("isA")) {
//				rightPane.isA.setSelected(true);
//			} else if (current_SE.getAxe_semantique().equals("partOf")) {
//				rightPane.partOf.setSelected(true);
//			} else if (current_SE.getAxe_semantique().equals("instanceOf")) {
//				rightPane.instanceOf.setSelected(true);
//			} else if (current_SE.getAxe_semantique().equals(
//					"autre_ontologique")) {
//				rightPane.ontologiqueAutre.setSelected(true);
//			} else if (current_SE.getAxe_semantique().equals("hyperonymie")) {
//				rightPane.hyperonymie.setSelected(true);
//			} else if (current_SE.getAxe_semantique().equals("meronymie")) {
//				rightPane.meronymie.setSelected(true);
//			} else if (current_SE.getAxe_semantique().equals("homonymie")) {
//				rightPane.homonymie.setSelected(true);
//			} else if (current_SE.getAxe_semantique().equals("synonymie")) {
//				rightPane.synonymie.setSelected(true);
//			} else if (current_SE.getAxe_semantique().equals("multilingue")) {
//				rightPane.multilingue.setSelected(true);
//			} else if (current_SE.getAxe_semantique().equals("autre_lexical")) {
//				rightPane.lexicalAutre.setSelected(true);
//			} else if (current_SE.getAxe_semantique()
//					.equals("autre_semantique")) {
//				rightPane.semantiqueAutre.setSelected(true);
//			} else {
//				flag_semantique = false;
//			}
//
//			if (current_SE.getAxe_semantiqueContext().equals("contextuelle")) {
//				rightPane.contextuelle.setSelected(true);
//				rightPane.non_contextuelle.setSelected(false);
//			} else {
//				rightPane.contextuelle.setSelected(false);
//				rightPane.non_contextuelle.setSelected(true);
//				flag_semantique2 = false;
//			}
//
//		}
//
//		// Bouton valid
//		if (flag_intentionnel && flag_rhetorique && flag_semantique
//				&& flag_semantique2) {
//			rightPane.valid.setEnabled(true);
//		} else {
//			rightPane.valid.setEnabled(false);
//		}
//
//		// else {
//		// centerPane.hypertextualJRadio.setSelected(false);
//		// centerPane.navigationnelJRadio.setSelected(false);
//		// centerPane.horizontaleJRadio.setSelected(false);
//		// centerPane.verticaleJRadio.setSelected(false);
//		// }
//	}
//
//	public void init() {
//
//		if (current_SE != null) {
//			// Re-ranking des SE selon la linéarité du texte
//			Collections.sort(chain_SE);
//
//			logger.info("initSE : CurrentSE : " + current_SE.getId() + "/"
//					+ chain_SE.size());
//
//			// Annotation
//			refreshAnnotation();
//
//			// Decider en fonction de l'annotation quel bouton allumer
//
//			// On met le TextPane sur la position de la SE
//			textPane.setCaretPosition(current_SE.getIndice_begin());
//
//			// On affiche le document nécessaire dans SEPane
//			refreshSEPane();
//
//			// On affiche len écessaire dans TextPane
//			refreshTextPane();
//
//			// On met à jour les infos sur la chaîne
//			showIndexLinkedList();
//			showSizeLinkedList();
//
//			// On vérifie si il y a des SE avant et après
//			rightPane.previousButton.setEnabled(toolsGetInfoPrevious());
//			rightPane.nextButton.setEnabled(toolsGetInfoNext());
//
//			// Changement d'état, on attend une nouvelle SE.
//			setStateWaitForNewSE();
//
//		} else {
//			logger.info("initSE : Pas de SE courante");
//			setStateWaitForNewSE();
//
//			showSizeLinkedList();
//			showDumperLinkedList();
//		}
//	}
//
//	public void refreshTextPane() {
//		logger.info("refreshTextPane : application des HighLigthers");
//		textPane.getHighlighter().removeAllHighlights();
//
//		if (!chain_SE.isEmpty() && chain_SE.contains(current_SE)) {
//			int index = chain_SE.indexOf(current_SE);
//			int i = 0;
//			setHighLighterForm(chain_SE.get(index).getIndice_begin(), chain_SE
//					.get(index).getIndice_end(), textPane, currentSelectedRec);
//			for (Iterator<Unit> it2 = chain_SE.listIterator(0); it2.hasNext(); i++) {
//				Unit mySE = it2.next();
//
//				if (i != index) {
//					setHighLighterForm(mySE.getIndice_begin(),
//							mySE.getIndice_end(), textPane, pastSelected);
//				}
//			}
//
//		} else if ((!chain_SE.contains(current_SE) && (current_SE != null) && (!chain_SE
//				.isEmpty()))) {
//			// Dans ce cas-ci, curret_SE est en instance d'addition à la
//			// chain_SE
//			// mais il y a déjà d'autres SE dans la chaine
//			setHighLighterForm(current_SE.getIndice_begin(),
//					current_SE.getIndice_end(), textPane, currentSelectedAdd);
//			int i = 0;
//			for (Iterator<Unit> it2 = chain_SE.listIterator(0); it2.hasNext(); i++) {
//				Unit mySE = it2.next();
//				setHighLighterForm(mySE.getIndice_begin(),
//						mySE.getIndice_end(), textPane, pastSelected);
//			}
//		} else if ((current_SE != null) && (!chain_SE.contains(current_SE))) {
//			setHighLighterForm(current_SE.getIndice_begin(),
//					current_SE.getIndice_end(), textPane, currentSelectedAdd);
//		}
//	}
//
//	public void refreshSEPane() {
//
//		// Si hashSE_Doc ne contient pas la SE et que la SE est différente de
//		// null
//		if ((!hashSE_Doc.containsKey(current_SE)) && (current_SE != null)) {
//			logger.info("refreshSEPane : Creation d'un nouveay HtmlDoc from "
//					+ document_path);
//
//			JTextPane toTest = new JTextPane();
//			toTest.setContentType("text/html");
//
//			Charset charset = Charset.forName("ISO-8859-1");
//			try {
//				BufferedReader in = new BufferedReader(new InputStreamReader(
//						new FileInputStream(document_path), charset));
//				toTest.read(in, charset);
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			toolsPruneTree((HTMLDocument) toTest.getDocument());
//			SEPane.setText(toTest.getText());
//			SEPane.setCaretPosition(0);
//			hashSE_Doc.put(current_SE, toTest);
//
//			modifyHighLighterSEPane();
//
//		} else if (current_SE != null) {
//			logger.info("refreshSEPane : Affichage du HTML document pour la SE ["
//					+ current_SE.getIndice_begin()
//					+ ","
//					+ current_SE.getIndice_end() + "]");
//
//			JTextPane toSet = hashSE_Doc.get(current_SE);
//
//			SEPane.setText(toSet.getText());
//
//			// Last
//			if (last_current_selection_SEPane > 0
//					&& last_current_selection_SEPane < SEPane.getDocument()
//							.getLength()) {
//				SEPane.setCaretPosition(last_current_selection_SEPane);
//			} else {
//				SEPane.setCaretPosition(0);
//			}
//			modifyHighLighterSEPane();
//
//		} else {
//			logger.info("refreshSEPane : Aucun affichage");
//			SEPane.setText("<html><body></body></html>");
//		}
//	}
//
//	public void ModifyHighLighterTextPane() {
//
//		setHighLighterFormPastSE();
//		removeHighLighterFormSE(current_SE, textPane);
//	}
//
//	public void actionPerformed(ActionEvent e) {
//
//		/*
//		 * Previous & Next Button
//		 */
//		if ("previous".equals(e.getActionCommand())) {
//			actionPerformedPrevious();
//		} else if ("next".equals(e.getActionCommand())) {
//			actionPerformedNext();
//		}
//
//		/*
//		 * Center Buttons
//		 */
//		if ("addSE".equals(e.getActionCommand())) {
//			actionPerformedAddSE();
//		} else if ("clear".equals(e.getActionCommand())) {
//			actionPerformedClearSE();
//		} else if ("rec".equals(e.getActionCommand())) {
//			actionPerformedRecSE();
//		} else if ("del".equals(e.getActionCommand())) {
//			actionPerformedDelSE();
//		}
//
//		/*
//		 * Boutons Sélection Button
//		 */
//		if ("addPrimer".equals(e.getActionCommand())) {
//			actionPerformedAddPrimer();
//		} else if ("addItem".equals(e.getActionCommand())) {
//			actionPerformedAddItem();
//		} else if ("clearAll".equals(e.getActionCommand())) {
//			actionPerformedClearThis();
//		} else if ("valid".equals(e.getActionCommand())) {
//			actionPerformedValid();
//		} else if ("cloture".equals(e.getActionCommand())) {
//			actionPerformedAddClot();
//		}
//
//		/*
//		 * Boutons Concepts/Circonstant/Marqueurs
//		 */
//		if ("addConcept".equals(e.getActionCommand())) {
//			actionPerformedAddConcept();
//		} else if ("addCirconstant".equals(e.getActionCommand())) {
//			actionPerformedAddCirconstant();
//		} else if ("switch".equals(e.getActionCommand())) {
//			actionPerformedSwitch();
//		} else if ("addMarqRel".equals(e.getActionCommand())) {
//			actionPerformedAddMarqRel();
//		}
//
//		/*
//		 * JRadioButton vertical vs horizon
//		 */
//		if ("vertical".equals(e.getActionCommand())) {
//			rightPane.verticaleJRadio.setSelected(true);
//			rightPane.horizontaleJRadio.setSelected(false);
//		} else if ("horizon".equals(e.getActionCommand())) {
//			rightPane.verticaleJRadio.setSelected(false);
//			rightPane.horizontaleJRadio.setSelected(true);
//		} else if ("hypertextual".equals(e.getActionCommand())) {
//		} else if ("navigationnelle".equals(e.getActionCommand())) {
//		}
//
//		/*
//		 * RightPane - Intentionnel
//		 */
//		if ("descriptive".equals(e.getActionCommand())) {
//		} else if ("narrative".equals(e.getActionCommand())) {
//		} else if ("explicative".equals(e.getActionCommand())) {
//		} else if ("prescriptive".equals(e.getActionCommand())) {
//		} else if ("procedurale".equals(e.getActionCommand())) {
//		} else if ("argumentative".equals(e.getActionCommand())) {
//		} else if ("autre_intentionnel".equals(e.getActionCommand())) {
//		}
//
//		/*
//		 * Axe Sémantique
//		 */
//		if ("isA".equals(e.getActionCommand())) {
//			rightPane.isA.setSelected(true);
//			rightPane.partOf.setSelected(false);
//			rightPane.instanceOf.setSelected(false);
//			rightPane.ontologiqueAutre.setSelected(false);
//			rightPane.hyperonymie.setSelected(false);
//			rightPane.meronymie.setSelected(false);
//			rightPane.homonymie.setSelected(false);
//			rightPane.synonymie.setSelected(false);
//			rightPane.multilingue.setSelected(false);
//			rightPane.lexicalAutre.setSelected(false);
//			rightPane.semantiqueAutre.setSelected(false);
//
//		} else if ("partOf".equals(e.getActionCommand())) {
//			rightPane.isA.setSelected(false);
//			rightPane.partOf.setSelected(true);
//			rightPane.instanceOf.setSelected(false);
//			rightPane.ontologiqueAutre.setSelected(false);
//			rightPane.hyperonymie.setSelected(false);
//			rightPane.meronymie.setSelected(false);
//			rightPane.homonymie.setSelected(false);
//			rightPane.synonymie.setSelected(false);
//			rightPane.multilingue.setSelected(false);
//			rightPane.lexicalAutre.setSelected(false);
//			rightPane.semantiqueAutre.setSelected(false);
//		} else if ("instanceOf".equals(e.getActionCommand())) {
//			rightPane.isA.setSelected(false);
//			rightPane.partOf.setSelected(false);
//			rightPane.instanceOf.setSelected(true);
//			rightPane.ontologiqueAutre.setSelected(false);
//			rightPane.hyperonymie.setSelected(false);
//			rightPane.meronymie.setSelected(false);
//			rightPane.homonymie.setSelected(false);
//			rightPane.synonymie.setSelected(false);
//			rightPane.multilingue.setSelected(false);
//			rightPane.lexicalAutre.setSelected(false);
//			rightPane.semantiqueAutre.setSelected(false);
//		} else if ("ontologiqueAutre".equals(e.getActionCommand())) {
//			rightPane.isA.setSelected(false);
//			rightPane.partOf.setSelected(false);
//			rightPane.instanceOf.setSelected(false);
//			rightPane.ontologiqueAutre.setSelected(true);
//			rightPane.hyperonymie.setSelected(false);
//			rightPane.meronymie.setSelected(false);
//			rightPane.homonymie.setSelected(false);
//			rightPane.synonymie.setSelected(false);
//			rightPane.multilingue.setSelected(false);
//			rightPane.lexicalAutre.setSelected(false);
//			rightPane.semantiqueAutre.setSelected(false);
//		} else if ("hyperonymie".equals(e.getActionCommand())) {
//			rightPane.isA.setSelected(false);
//			rightPane.partOf.setSelected(false);
//			rightPane.instanceOf.setSelected(false);
//			rightPane.ontologiqueAutre.setSelected(false);
//			rightPane.hyperonymie.setSelected(true);
//			rightPane.meronymie.setSelected(false);
//			rightPane.homonymie.setSelected(false);
//			rightPane.synonymie.setSelected(false);
//			rightPane.multilingue.setSelected(false);
//			rightPane.lexicalAutre.setSelected(false);
//			rightPane.semantiqueAutre.setSelected(false);
//		} else if ("meronymie".equals(e.getActionCommand())) {
//			rightPane.isA.setSelected(false);
//			rightPane.partOf.setSelected(false);
//			rightPane.instanceOf.setSelected(false);
//			rightPane.ontologiqueAutre.setSelected(false);
//			rightPane.hyperonymie.setSelected(false);
//			rightPane.meronymie.setSelected(true);
//			rightPane.homonymie.setSelected(false);
//			rightPane.synonymie.setSelected(false);
//			rightPane.multilingue.setSelected(false);
//			rightPane.lexicalAutre.setSelected(false);
//			rightPane.semantiqueAutre.setSelected(false);
//		} else if ("homonymie".equals(e.getActionCommand())) {
//			rightPane.isA.setSelected(false);
//			rightPane.partOf.setSelected(false);
//			rightPane.instanceOf.setSelected(false);
//			rightPane.ontologiqueAutre.setSelected(false);
//			rightPane.hyperonymie.setSelected(false);
//			rightPane.meronymie.setSelected(false);
//			rightPane.homonymie.setSelected(true);
//			rightPane.synonymie.setSelected(false);
//			rightPane.multilingue.setSelected(false);
//			rightPane.lexicalAutre.setSelected(false);
//			rightPane.semantiqueAutre.setSelected(false);
//		} else if ("synonymie".equals(e.getActionCommand())) {
//			rightPane.isA.setSelected(false);
//			rightPane.partOf.setSelected(false);
//			rightPane.instanceOf.setSelected(false);
//			rightPane.ontologiqueAutre.setSelected(false);
//			rightPane.hyperonymie.setSelected(false);
//			rightPane.meronymie.setSelected(false);
//			rightPane.homonymie.setSelected(false);
//			rightPane.synonymie.setSelected(true);
//			rightPane.multilingue.setSelected(false);
//			rightPane.lexicalAutre.setSelected(false);
//			rightPane.semantiqueAutre.setSelected(false);
//		} else if ("multilingue".equals(e.getActionCommand())) {
//			rightPane.isA.setSelected(false);
//			rightPane.partOf.setSelected(false);
//			rightPane.instanceOf.setSelected(false);
//			rightPane.ontologiqueAutre.setSelected(false);
//			rightPane.hyperonymie.setSelected(false);
//			rightPane.meronymie.setSelected(false);
//			rightPane.homonymie.setSelected(false);
//			rightPane.synonymie.setSelected(false);
//			rightPane.multilingue.setSelected(true);
//			rightPane.lexicalAutre.setSelected(false);
//			rightPane.semantiqueAutre.setSelected(false);
//		} else if ("lexicalAutre".equals(e.getActionCommand())) {
//			rightPane.isA.setSelected(false);
//			rightPane.partOf.setSelected(false);
//			rightPane.instanceOf.setSelected(false);
//			rightPane.ontologiqueAutre.setSelected(false);
//			rightPane.hyperonymie.setSelected(false);
//			rightPane.meronymie.setSelected(false);
//			rightPane.homonymie.setSelected(false);
//			rightPane.synonymie.setSelected(false);
//			rightPane.multilingue.setSelected(false);
//			rightPane.lexicalAutre.setSelected(true);
//			rightPane.semantiqueAutre.setSelected(false);
//		} else if ("semantiqueAutre".equals(e.getActionCommand())) {
//			rightPane.isA.setSelected(false);
//			rightPane.partOf.setSelected(false);
//			rightPane.instanceOf.setSelected(false);
//			rightPane.ontologiqueAutre.setSelected(false);
//			rightPane.hyperonymie.setSelected(false);
//			rightPane.meronymie.setSelected(false);
//			rightPane.homonymie.setSelected(false);
//			rightPane.synonymie.setSelected(false);
//			rightPane.multilingue.setSelected(false);
//			rightPane.lexicalAutre.setSelected(false);
//			rightPane.semantiqueAutre.setSelected(true);
//		}
//
//		/*
//		 * Contextuel
//		 */
//		if ("contextuelle".equals(e.getActionCommand())) {
//			rightPane.contextuelle.setSelected(true);
//			rightPane.non_contextuelle.setSelected(false);
//
//		} else if ("non_contextuelle".equals(e.getActionCommand())) {
//			rightPane.contextuelle.setSelected(false);
//			rightPane.non_contextuelle.setSelected(true);
//		}
//
//		refreshSimpleAnnotation();
//
//	}
//
//	@Override
//	public void caretUpdate(CaretEvent e) {
//
//		if (e.getSource().equals(textPane)) {
//
//			textPane = (JTextPane) e.getSource();
//			// htmlDoc = (HTMLDocument) textPane.getDocument();
//
//			int dot = e.getDot();
//			int mark = e.getMark();
//			int pos = 0;
//			int dif = 0;
//			int min = 0;
//			int max = 0;
//
//			if (dot < mark) {
//				pos = dot;
//				dif = mark - dot;
//				min = dot;
//				max = mark;
//				try {
//					String textString = textPane.getText(pos, dif);
//					current_selection_textPane = textString;
//					current_selection_textPane.replace("\n", "<br>");
//				} catch (BadLocationException e1) {
//					e1.printStackTrace();
//					logger.error(
//							"Threw a BadLocationException in MIG_JPanel_Parent::caretUpdate, full stack trace follows:",
//							e1);
//
//				}
//
//				deb_current_selection_textPane = pos;
//				fin_current_selection_textPane = pos + dif;
//
//			} else if (dot > mark) {
//				pos = mark;
//				dif = dot - mark;
//				min = mark;
//				max = dot;
//
//				try {
//					String textString = textPane.getText(pos, dif);
//					current_selection_textPane = textString;
//					current_selection_textPane.replace("\n", "<br>");
//				} catch (BadLocationException e1) {
//					e1.printStackTrace();
//					logger.error(
//							"Threw a BadLocationException in MIG_JPanel_Parent::caretUpdate, full stack trace follows:",
//							e1);
//
//				}
//
//				deb_current_selection_textPane = pos;
//				fin_current_selection_textPane = pos + dif;
//
//			} else if (dot == mark) {
//			}
//		} else if (e.getSource().equals(SEPane)) {
//			SEPane = (JTextPane) e.getSource();
//			int dot = e.getDot();
//			int mark = e.getMark();
//			int pos = 0;
//			int dif = 0;
//			int min = 0;
//			int max = 0;
//
//			if (dot < mark) {
//				pos = dot;
//				dif = mark - dot;
//				min = dot;
//				max = mark;
//				try {
//					String textString = textPane.getText(pos, dif);
//					current_selection_textPane = textString;
//					current_selection_textPane.replace("\n", "<br>");
//				} catch (BadLocationException e1) {
//					e1.printStackTrace();
//					logger.error(
//							"Threw a BadLocationException in MIG_JPanel_Parent::caretUpdate, full stack trace follows:",
//							e1);
//
//				}
//				deb_current_selection_SEPane = pos;
//				fin_current_selection_SEPane = pos + dif;
//
//			} else if (dot > mark) {
//				pos = mark;
//				dif = dot - mark;
//				min = mark;
//				max = dot;
//
//				try {
//					String textString = textPane.getText(pos, dif);
//					current_selection_textPane = textString;
//					current_selection_textPane.replace("\n", "<br>");
//				} catch (BadLocationException e1) {
//					e1.printStackTrace();
//					logger.error(
//							"Threw a BadLocationException in MIG_JPanel_Parent::caretUpdate, full stack trace follows:",
//							e1);
//
//				}
//				deb_current_selection_SEPane = pos;
//				fin_current_selection_SEPane = pos + dif;
//
//			} else if (dot == mark) {
//			}
//
//			last_current_selection_SEPane = fin_current_selection_SEPane;
//		}
//	}
//
//	public void setStateTreatCurentSE() {
//		logger.info("setStateTreatCurrentSE");
//		stateTreatCurentSE = true;
//		stateWaitForNewSE = false;
//		stateRecSE = false;
//		stateWaitForNewDoc = false;
//		/**
//		 * Select SE
//		 */
//		// On a une SE dans le centerPane
//		centerPane.addButton.setEnabled(false);
//		// La SE courante n'est pas encore instanciée
//		// Il est encore possible de rejeter le texte
//		centerPane.clearButton.setEnabled(true);
//		// Si la SE courante n'est pas encore enregistrée
//		// Il est possible de l'enregistrer
//		centerPane.recButton.setEnabled(true);
//		if (chain_SE.contains(current_SE)) {
//			centerPane.delButton.setEnabled(true);
//		}
//		// Si il n'y a pas de SE ou qu'elle est en cours de traitement
//		// on ne peut pas la supprimer
//		else {
//			centerPane.delButton.setEnabled(false);
//		}
//
//		/**
//		 * Internal SE
//		 */
//		if (chain_SE.contains(current_SE) && !mode_v2) {
//			centerPane.addPrimer.setEnabled(true);
//			centerPane.addItem.setEnabled(true);
//			centerPane.clearThis.setEnabled(true);
//			rightPane.valid.setEnabled(true);
//			centerPane.clotIcon.setEnabled(true);
//		} else {
//			centerPane.addPrimer.setEnabled(false);
//			centerPane.addItem.setEnabled(false);
//			centerPane.clearThis.setEnabled(false);
//			rightPane.valid.setEnabled(false);
//			centerPane.clotIcon.setEnabled(false);
//		}
//		// centerPane.addConcept.setEnabled(true);
//		// centerPane.addContextTempo.setEnabled(true);
//		// centerPane.addContextSpatio.setEnabled(true);
//
//		centerPane.addCirconstant.setEnabled(true);
//		centerPane.addConcept.setEnabled(true);
//		centerPane.switchView.setEnabled(true);
//		centerPane.addMarqRel.setEnabled(true);
//	}
//
//	public void setStateWaitForNewDoc() {
//		logger.info("setStateWaitForNewDoc");
//		stateWaitForNewDoc = true;
//		stateWaitForNewSE = false;
//		stateTreatCurentSE = false;
//		stateRecSE = false;
//
//		/**
//		 * Select SE
//		 */
//		// On attend une nouvelle SE avec le bouton "Add"
//		centerPane.addButton.setEnabled(false);
//		// Si la SE courante est instanciée
//		// Ou si il n'y a pas de SE
//		// alors, on ne peut pas la "clear".
//		centerPane.clearButton.setEnabled(false);
//		// Si la SE courante est "enregistrée",
//		// Si il n'y a pas de SE
//		// alors, on ne peut pas l'enregistrer à nouveau
//		centerPane.recButton.setEnabled(false);
//		// Si il y a une SE en cours de visionnage
//		// et qu'elle appartient à la liste
//		// On peut la supprimer
//
//		if (chain_SE.contains(current_SE)) {
//			centerPane.delButton.setEnabled(true);
//			rightPane.valid.setEnabled(true);
//		}
//		// Si il n'y a pas de SE ou qu'elle est en cours de traitement
//		// on ne peut pas la supprimer
//		else {
//			centerPane.delButton.setEnabled(false);
//			rightPane.valid.setEnabled(false);
//		}
//
//		/**
//		 * Internal SE
//		 */
//		if (chain_SE.contains(current_SE)) {
//			centerPane.addPrimer.setEnabled(true);
//			centerPane.addItem.setEnabled(true);
//			centerPane.clearThis.setEnabled(true);
//			centerPane.clotIcon.setEnabled(true);
//		} else {
//			centerPane.addPrimer.setEnabled(false);
//			centerPane.addItem.setEnabled(false);
//			centerPane.clearThis.setEnabled(false);
//			centerPane.clotIcon.setEnabled(false);
//		}
//
//		centerPane.addCirconstant.setEnabled(false);
//		centerPane.addConcept.setEnabled(false);
//		centerPane.switchView.setEnabled(false);
//		centerPane.addMarqRel.setEnabled(false);
//
//	}
//
//	public void setStateWaitForNewSE() {
//		logger.info("setStateWaitForNewSE");
//		stateWaitForNewDoc = false;
//		stateWaitForNewSE = true;
//		stateTreatCurentSE = false;
//		stateRecSE = false;
//
//		/**
//		 * Select SE
//		 */
//		// On attend une nouvelle SE avec le bouton "Add"
//		// centerPane.addButton.setEnabled(true);
//		// V2
//		if (mode_v2) {
//			centerPane.addButton.setEnabled(false);
//			centerPane.clearButton.setEnabled(false);
//			centerPane.recButton.setEnabled(false);
//		} else {
//			centerPane.addButton.setEnabled(true);
//			centerPane.clearButton.setEnabled(true);
//			centerPane.recButton.setEnabled(true);
//		}
//		// Si la SE courante est instanciée
//		// Ou si il n'y a pas de SE
//		// alors, on ne peut pas la "clear".
//		// Si la SE courante est "enregistrée",
//		// Si il n'y a pas de SE
//		// alors, on ne peut pas l'enregistrer à nouveau
//		// Si il y a une SE en cours de visionnage
//		// et qu'elle appartient à la liste
//		// On peut la supprimer
//		if (chain_SE.contains(current_SE) && !mode_v2) {
//			centerPane.delButton.setEnabled(true);
//			// V2
//			centerPane.addButton.setEnabled(true);
//		} else {
//			centerPane.addButton.setEnabled(false);
//			centerPane.delButton.setEnabled(false);
//		}
//		// Si il n'y a pas de SE ou qu'elle est en cours de traitement
//		// on ne peut pas la supprimer
//
//		/**
//		 * Internal SE
//		 */
//		if (chain_SE.contains(current_SE) && !mode_v2) {
//			centerPane.addPrimer.setEnabled(true);
//			centerPane.addItem.setEnabled(true);
//			centerPane.clearThis.setEnabled(true);
//			rightPane.valid.setEnabled(true);
//			centerPane.clotIcon.setEnabled(true);
//		} else {
//			centerPane.addPrimer.setEnabled(false);
//			centerPane.addItem.setEnabled(false);
//			centerPane.clearThis.setEnabled(false);
//			rightPane.valid.setEnabled(false);
//			centerPane.clotIcon.setEnabled(false);
//		}
//		// centerPane.addContextTempo.setEnabled(true);
//		// centerPane.addContextSpatio.setEnabled(true);
//
//		centerPane.addCirconstant.setEnabled(true);
//		centerPane.addConcept.setEnabled(true);
//		centerPane.switchView.setEnabled(true);
//		centerPane.addMarqRel.setEnabled(true);
//
//	}
//
//	public void actionPerformedAddSE() {
//		logger.info("actionPerformedAdd");
//
//		if (!stateTreatCurentSE) {
//			current_SE = new Unit();
//			current_SE.setIndice_begin(deb_current_selection_textPane);
//			current_SE.setIndice_end(fin_current_selection_textPane);
//			current_SE.setText(current_selection_textPane);
//			rightPane.commentTextPane.setText("");
//
//			rightPane.verticaleJRadio.setSelected(false);
//			rightPane.horizontaleJRadio.setSelected(false);
//			rightPane.navigationnelJRadio.setSelected(false);
//			rightPane.hypertextualJRadio.setSelected(false);
//
//			refreshTextPane();
//			refreshSEPane();
//
//			setStateTreatCurentSE();
//		} else {
//			logger.info("actionPerformedAdd : abort");
//		}
//	}
//
//	public void actionPerformedClearSE() {
//		// removeHighLighterFormSE(current_SE, textPane);
//		current_SE = null;
//		// SEPane.setText("");
//		refreshSEPane();
//		setStateWaitForNewSE();
//	}
//
//	public void actionPerformedRecSE() {
//		logger.info("actionPerformedRec");
//
//		chain_SE.add(current_SE);
//
//		// modifyHighLighterForm(current_SE, textPane, currentSelectedRec);
//
//		setStateWaitForNewSE();
//		init();
//	}
//
//	public void actionPerformedDelSE() {
//		logger.info("actionPerformedDelSE");
//
//		int index = chain_SE.indexOf(current_SE);
//		rightPane.annotation_id.setText("" + index);
//
//		// Il y a une SE avant
//		// on retourne en arrière
//		if (toolsGetInfoPrevious()) {
//			logger.info("actionPerformedDelSE: passage SE précédente");
//			Unit last_currentSE = actionPerformedPrevious();
//			toolsRemoveSE(last_currentSE);
//			// chain_SE.remove(last_currentSE);
//			// hashSE_Doc.remove(last_currentSE);
//			// removeHighLighterFormSE(last_currentSE);
//		}
//		// Il y a une SE après mais pas avant
//		// on va de l'avant ("The Life is Hard, but this is the Life")
//		else if (toolsGetInfoNext()) {
//			logger.info("actionPerformedDelSE: passage SE suivante");
//			Unit last_currentSE = actionPerformedNext();
//			// chain_SE.remove(last_currentSE);
//			// removeHighLighterFormSE(last_currentSE);
//			toolsRemoveSE(last_currentSE);
//		}
//		// Il y a plus rien, men!
//		// init pas Possible car ... il y a rien
//		else {
//			logger.info("actionPerformedDelSE: plus de SE");
//			// On passe à l'état on attend un truc
//			toolsRemoveSE(current_SE);
//			// chain_SE.remove(current_SE);
//			// hashSE_Doc.remove(current_SE);
//			// removeHighLighterFormSE(current_SE);
//			current_SE = null;
//			refreshTextPane();
//			// SEPane.setText("");
//			refreshSEPane();
//			setStateWaitForNewSE();
//		}
//
//		init();
//	}
//
//	public Unit actionPerformedPrevious() {
//		logger.info("actionPerformedPrevious");
//		// Passe à la SE précédente
//		// current_SE = précédente
//		// return = SE à partir de laquelle on va à la précédente
//
//		int index = 0;
//		Unit last_current = null;
//		if (current_SE != null) {
//			last_current = current_SE;
//			index = chain_SE.indexOf(current_SE);
//		} else {
//			index = chain_SE.size();
//		}
//
//		// Si on est en train de traiter la SE
//		if (stateTreatCurentSE) {
//			actionPerformedClearSE();
//
//			rightPane.annotation_id.setText("" + index);
//			current_SE = chain_SE.get(chain_SE.size() - 1);
//		} else {
//			if (index != 0) {
//				rightPane.annotation_id.setText("" + index);
//				current_SE = chain_SE.get(index - 1);
//			}
//		}
//
//		init();
//		return last_current;
//
//	}
//
//	public Unit actionPerformedNext() {
//		logger.info("actionPerformedNext");
//		// Passe à la SE suivante
//		// current_SE = la suivante
//		// return = SE à partir de laquelle on va à la suivante
//		Unit last_current = current_SE;
//		int index = chain_SE.indexOf(current_SE);
//		rightPane.annotation_id.setText("" + index);
//		current_SE = chain_SE.get(index + 1);
//
//		if (stateTreatCurentSE) {
//			actionPerformedClearSE();
//		}
//
//		init();
//		return last_current;
//	}
//
//	public void actionPerformedAddPrimer() {
//		logger.info("actionPerformedAddPrimer [" + deb_current_selection_SEPane
//				+ "," + fin_current_selection_SEPane + "]");
//
//		Primer myNewPrimer = new Primer();
//		int length = fin_current_selection_SEPane
//				- deb_current_selection_SEPane;
//		try {
//			myNewPrimer.add(SEPane
//					.getText(deb_current_selection_SEPane, length));
//		} catch (BadLocationException e) {
//			e.printStackTrace();
//			logger.error(
//					"Threw a BadLocationException in MIG_JPanel_Parent::actionPerformedAddPrimer, full stack trace follows:",
//					e);
//
//		}
//		myNewPrimer.setIndice_begin(deb_current_selection_SEPane);
//		myNewPrimer.setIndice_end(fin_current_selection_SEPane);
//		current_SE.setPrimer(myNewPrimer);
//		refreshSEPane();
//	}
//
//	public void actionPerformedAddItem() {
//		logger.info("actionPerformedAddItem [" + deb_current_selection_SEPane
//				+ "," + fin_current_selection_SEPane + "]");
//
//		Item myNewItem = new Item();
//		int length = fin_current_selection_SEPane
//				- deb_current_selection_SEPane;
//		try {
//			myNewItem.add(SEPane.getText(deb_current_selection_SEPane, length));
//		} catch (BadLocationException e) {
//			e.printStackTrace();
//			logger.error(
//					"Threw a BadLocationException in MIG_JPanel_Parent::actionPerformedAddItem, full stack trace follows:",
//					e);
//
//		}
//		myNewItem.setIndice_begin(deb_current_selection_SEPane);
//		myNewItem.setIndice_end(fin_current_selection_SEPane);
//		current_SE.addItem(myNewItem);
//		refreshSEPane();
//	}
//
//	public void actionPerformedAddClot() {
//		logger.info("actionPerformedAddItem [" + deb_current_selection_SEPane
//				+ "," + fin_current_selection_SEPane + "]");
//
//		Cloture myNewItem = new Cloture();
//		int length = fin_current_selection_SEPane
//				- deb_current_selection_SEPane;
//		try {
//			myNewItem.setText(SEPane.getText(deb_current_selection_SEPane,
//					length));
//		} catch (BadLocationException e) {
//			e.printStackTrace();
//			logger.error(
//					"Threw a BadLocationException in MIG_JPanel_Parent::actionPerformedAddItem, full stack trace follows:",
//					e);
//
//		}
//		myNewItem.setIndice_begin(deb_current_selection_SEPane);
//		myNewItem.setIndice_end(fin_current_selection_SEPane);
//		current_SE.setClot(myNewItem);
//		refreshSEPane();
//
//	}
//
//	public void actionPerformedClearThis() {
//		logger.info("actionPerformed ClearSE");
//
//		current_SE.setPrimer(null);
//		current_SE_primer = null;
//		Items newList = new Items();
//		current_SE.setClot(null);
//		current_SE.setItems(newList);
//
//		init();
//	}
//
//	public void actionPerformedValidAxeRhetorique() {
//
//		if (rightPane.paradigmatique.isSelected()) {
//			current_SE.setAxe_rhetorique(axe_rhetorique_paradigmatique);
//
//		} else if (rightPane.syntagmatique.isSelected()) {
//			current_SE.setAxe_rhetorique(axe_rhetorique_syntagmatique);
//
//		} else if (rightPane.hybride.isSelected()) {
//			current_SE.setAxe_rhetorique(axe_rhetorique_hybride);
//
//		} else if (rightPane.bivalente.isSelected()) {
//			current_SE.setAxe_rhetorique(axe_rhetorique_bivalente);
//		}
//	}
//
//	public void actionPerformedValidAxeIntentionnel() {
//
//		current_SE.getAxe_intentionnel().clear();
//
//		if (rightPane.descriptive.isSelected()) {
//			current_SE.addAxe_intentionnel(axe_intentionnel_descriptive);
//		}
//
//		if (rightPane.narrative.isSelected()) {
//			current_SE.addAxe_intentionnel(axe_intentionnel_narrative);
//		}
//
//		if (rightPane.explicative.isSelected()) {
//			current_SE.addAxe_intentionnel(axe_intentionnel_explicative);
//		}
//
//		if (rightPane.prescriptive.isSelected()) {
//			current_SE.addAxe_intentionnel(axe_intentionnel_prescriptive);
//		}
//
//		if (rightPane.procedurale.isSelected()) {
//			current_SE.addAxe_intentionnel(axe_intentionnel_procedurale);
//		}
//
//		if (rightPane.argumentative.isSelected()) {
//			current_SE.addAxe_intentionnel(axe_intentionnel_argumentative);
//		}
//		if (rightPane.autre_intentionnel.isSelected()) {
//			current_SE.addAxe_intentionnel(axe_intentionnel_autreIntentionnel);
//		}
//	}
//
//	public void actionPerformedValidAxeSemantique() {
//
//		if (rightPane.isA.isSelected()) {
//			current_SE.setAxe_semantique(axe_semantique_isA);
//
//		} else if (rightPane.partOf.isSelected()) {
//			current_SE.setAxe_semantique(axe_semantique_partOf);
//
//		} else if (rightPane.instanceOf.isSelected()) {
//			current_SE.setAxe_semantique(axe_semantique_instanceOf);
//		} else if (rightPane.ontologiqueAutre.isSelected()) {
//			current_SE.setAxe_semantique(axe_semantique_autreOntologique);
//		} else if (rightPane.hyperonymie.isSelected()) {
//			current_SE.setAxe_semantique(axe_semantique_hyperonymie);
//		} else if (rightPane.meronymie.isSelected()) {
//			current_SE.setAxe_semantique(axe_semantique_meronymie);
//		} else if (rightPane.homonymie.isSelected()) {
//			current_SE.setAxe_semantique(axe_semantique_homonymie);
//		} else if (rightPane.synonymie.isSelected()) {
//			current_SE.setAxe_semantique(axe_semantique_synonymie);
//		} else if (rightPane.multilingue.isSelected()) {
//			current_SE.setAxe_semantique(axe_semantique_multilingue);
//		} else if (rightPane.lexicalAutre.isSelected()) {
//			current_SE.setAxe_semantique(axe_semantique_autreLexical);
//		} else if (rightPane.semantiqueAutre.isSelected()) {
//			current_SE.setAxe_semantique(axe_semantique_autreSemantique);
//		}
//
//		/*
//		 * Contextuel
//		 */
//		if (rightPane.contextuelle.isSelected()) {
//			current_SE.setAxe_semantiqueCircon(axe_semantique_contextuelle);
//
//		} else if (rightPane.non_contextuelle.isSelected()) {
//			current_SE.setAxe_semantiqueCircon(axe_semantique_non_contextuelle);
//		}
//	}
//	
//
//	public void actionPerformedValid() {
//		logger.info("actionPerformed Valid");
//		// refreshAnnotation();
//
//		if (chain_SE.size() != 0) {
//			Annotation annotation = new Annotation();
//			annotation.setAuthor(""
//					+ rightPane.fonctionComboBox.getSelectedItem());
//			annotation.setComment(rightPane.commentTextPane.getText());
//			Date current_date = new Date();
//			annotation.setDate(current_date);
//			annotation.setDocumentName(document_name);
//			current_SE.setAnnotation(annotation);
//
//			/*
//			 * Axe visuel (horizontale vs. verticale)
//			 */
//			if (rightPane.verticaleJRadio.isSelected()) {
//				current_SE.setAxe_visuel(axe_visuel_vertical);
//			} else if (rightPane.horizontaleJRadio.isSelected()) {
//				current_SE.setAxe_visuel(axe_visuel_horizontal);
//			} else {
//				current_SE.setAxe_visuel("Nothing");
//			}
//
//			/*
//			 * Axe visuel (navigationnelle vs. hypertextuelle)
//			 */
//			if (rightPane.hypertextualJRadio.isSelected()) {
//				current_SE.setAxe_visuel_nav_hyp(axe_visuel_nav_hyp_Hypertext);
//			} else if (rightPane.navigationnelJRadio.isSelected()) {
//				current_SE.setAxe_visuel_nav_hyp(axe_visuel_nav_hyp_Navig);
//			} else {
//				// Choix par défaut
//				current_SE.setAxe_visuel_nav_hyp(axe_visuel_nav_hyp_Nothing);
//			}
//
//			/**
//			 * Axe rhétorique
//			 */
//			actionPerformedValidAxeRhetorique();
//
//			/**
//			 * Axe intentionnel
//			 */
//			actionPerformedValidAxeIntentionnel();
//
//			/**
//			 * Axe sémantique
//			 */
//			actionPerformedValidAxeSemantique();
//
//			// Si c'est okay et que la chaîne de SE n'est pas vide
//			// et que on a bien un document_path, on balance.
//			if (document_path != null & chain_SE.size() > 0) {
//				Larat_inputoutput io_mig = new Larat_inputoutput();
//				io_mig.setChain(chain_SE);
//				io_mig.setPath(document_path);
//				io_mig.writeThis();
//			} else {
//				logger.info("actionPerformed Valid : chaine vide ou pas de document");
//			}
//		} else {
//			if (document_path != null) {
//				// JOptionPane
//				// .showMessageDialog(
//				// annotationSE_jframe,
//				// "Attention : il n'est pas certain que le fichier XML lié à "
//				// + document_path
//				// +
//				// " soit vide.\nSi vous voulez supprimer toutes les annotations, faites-le à la main.",
//				// "Inane warning", JOptionPane.WARNING_MESSAGE);
//			} else {
//				// JOptionPane.showMessageDialog(annotationSE_jframe,
//				// "Attention : Rien à enregistrer.", "Inane warning",
//				// JOptionPane.WARNING_MESSAGE);
//			}
//			// IO_MIG io_mig = new IO_MIG();
//			// io_mig.removeThisXML(document_path);
//		}
//	}
//
//	public void actionPerformedOpenFileAutomaticaly(String path, String repPath) {
//		logger.info("actionPerformedOpenFileAutomaticaly : " + path);
//		document_path = path;
//		repertoireCourant = new File(repPath);
//	}
//
//	public void actionPerformedAddConcept() {
//		logger.info("actionPerformedAddConcept ["
//				+ deb_current_selection_SEPane + ","
//				+ fin_current_selection_SEPane + "]");
//
//		// Vérifier où se trouve le segment et, en fonction,
//		// l'ajouter soit au Primer, soit à l'item correspondant.
//
//		Segment segment = new Segment();
//		int length = fin_current_selection_SEPane
//				- deb_current_selection_SEPane;
//		try {
//			segment.setText(SEPane
//					.getText(deb_current_selection_SEPane, length));
//		} catch (BadLocationException e) {
//			e.printStackTrace();
//			logger.error(
//					"Threw a BadLocationException in MIG_JPanel_Parent::actionPerformedAddItem, full stack trace follows:",
//					e);
//		}
//		segment.setIndice_begin(deb_current_selection_SEPane);
//		segment.setIndice_end(fin_current_selection_SEPane);
//
//		// Ou est-ce foutu segment??
//		if (whereIsThisSegment(segment).equals("primer")) {
//			current_SE.getPrimer().getConcept().add(segment);
//			logger.info("actionPerformedAddConcept au Primer ["
//					+ deb_current_selection_SEPane + ","
//					+ fin_current_selection_SEPane + "] - \""
//					+ segment.getText() + "\"");
//		} else if (whereIsThisSegment(segment).equals("item")) {
//			// Quel Item
//			int item_id = whichItem(segment);
//			if (item_id == -1) {
//				// JOptionPane
//				// .showMessageDialog(
//				// annotationSE_jframe,
//				// "Attention : Le concept que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
//				// "Inane warning", JOptionPane.WARNING_MESSAGE);
//			} else {
//				current_SE.getItem(item_id).getConcept().add(segment);
//				logger.info("actionPerformedAddConcept à l'item " + item_id
//						+ " [" + deb_current_selection_SEPane + ","
//						+ fin_current_selection_SEPane + "] - \""
//						+ segment.getText() + "\"");
//			}
//		} else {
//			// JOptionPane
//			// .showMessageDialog(
//			// annotationSE_jframe,
//			// "Attention : Le concept que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
//			// "Inane warning", JOptionPane.WARNING_MESSAGE);
//		}
//
//		refreshSEPane();
//	}
//
//	public int whichItem(Segment segment) {
//
//		int index = 0;
//		for (Item item : current_SE.getItems()) {
//
//			if (segment.getIndice_begin() >= item.getIndice_begin()
//					&& segment.getIndice_end() <= item.getIndice_end()) {
//				return index;
//			}
//			index++;
//		}
//		return -1;
//	}
//
//	public String whereIsThisSegment(Segment segment) {
//		if (current_SE.getPrimer() != null
//				&& segment.getIndice_begin() >= current_SE.getPrimer()
//						.getIndice_begin()
//				&& segment.getIndice_end() <= current_SE.getPrimer()
//						.getIndice_end()) {
//			return "primer";
//		} else if (current_SE.getItems().size() > 0
//				&& segment.getIndice_begin() >= current_SE.getItem(0)
//						.getIndice_begin()
//				&& segment.getIndice_end() <= current_SE.getItem(
//						current_SE.getItems().size() - 1).getIndice_end()) {
//			return "item";
//		} else {
//			return "error";
//		}
//	}
//
//	public void actionPerformedAddCirconstant() {
//		logger.info("actionPerformedAddCirconstant ["
//				+ deb_current_selection_SEPane + ","
//				+ fin_current_selection_SEPane + "]");
//
//		// Vérifier où se trouve le segment et, en fonction,
//		// l'ajouter soit au Primer, soit à l'item correspondant.
//
//		Segment segment = new Segment();
//		int length = fin_current_selection_SEPane
//				- deb_current_selection_SEPane;
//		try {
//			segment.setText(SEPane
//					.getText(deb_current_selection_SEPane, length));
//		} catch (BadLocationException e) {
//			e.printStackTrace();
//			logger.error(
//					"Threw a BadLocationException in MIG_JPanel_Parent::actionPerformedAddItem, full stack trace follows:",
//					e);
//		}
//		segment.setIndice_begin(deb_current_selection_SEPane);
//		segment.setIndice_end(fin_current_selection_SEPane);
//
//		// Ou est-ce foutu segment??
//		if (whereIsThisSegment(segment).equals("primer")) {
//			current_SE.getPrimer().getCirconstant().add(segment);
//			logger.info("actionPerformedAddCirconstant au Primer ["
//					+ deb_current_selection_SEPane + ","
//					+ fin_current_selection_SEPane + "] - \""
//					+ segment.getText() + "\"");
//		} else if (whereIsThisSegment(segment).equals("item")) {
//			// Quel Item
//			int item_id = whichItem(segment);
//			if (item_id == -1) {
//				// JOptionPane
//				// .showMessageDialog(
//				// annotationSE_jframe,
//				// "Attention : Le circonstant que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
//				// "Inane warning", JOptionPane.WARNING_MESSAGE);
//			} else {
//				current_SE.getItem(item_id).getCirconstant().add(segment);
//				logger.info("actionPerformedAddCirconstant à l'item " + item_id
//						+ " [" + deb_current_selection_SEPane + ","
//						+ fin_current_selection_SEPane + "] - \""
//						+ segment.getText() + "\"");
//			}
//		} else {
//			// JOptionPane
//			// .showMessageDialog(
//			// annotationSE_jframe,
//			// "Attention : Le circonstant que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
//			// "Inane warning", JOptionPane.WARNING_MESSAGE);
//		}
//
//		refreshSEPane();
//	}
//
//	public void actionPerformedSwitch() {
//
//		if (switch_view) {
//			switch_view = false;
//		} else {
//			switch_view = true;
//		}
//		refreshSEPane();
//	}
//
//	public void actionPerformedAddMarqRel() {
//		logger.info("actionPerformedAddMarqueurRelation ["
//				+ deb_current_selection_SEPane + ","
//				+ fin_current_selection_SEPane + "]");
//
//		// Vérifier où se trouve le segment et, en fonction,
//		// l'ajouter soit au Primer, soit à l'item correspondant.
//
//		Segment segment = new Segment();
//		int length = fin_current_selection_SEPane
//				- deb_current_selection_SEPane;
//		try {
//			segment.setText(SEPane
//					.getText(deb_current_selection_SEPane, length));
//		} catch (BadLocationException e) {
//			e.printStackTrace();
//			logger.error(
//					"Threw a BadLocationException in MIG_JPanel_Parent::actionPerformedAddItem, full stack trace follows:",
//					e);
//		}
//		segment.setIndice_begin(deb_current_selection_SEPane);
//		segment.setIndice_end(fin_current_selection_SEPane);
//
//		// Ou est-ce foutu segment??
//		if (whereIsThisSegment(segment).equals("primer")) {
//			current_SE.getPrimer().getMarqueurRel().add(segment);
//			logger.info("actionPerformedAddMarqueurRelation au Primer ["
//					+ deb_current_selection_SEPane + ","
//					+ fin_current_selection_SEPane + "] - \""
//					+ segment.getText() + "\"");
//		} else if (whereIsThisSegment(segment).equals("item")) {
//			// Quel Item
//			int item_id = whichItem(segment);
//			if (item_id == -1) {
//				// JOptionPane
//				// .showMessageDialog(
//				// annotationSE_jframe,
//				// "Attention : Le marqueur de relation que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
//				// "Inane warning", JOptionPane.WARNING_MESSAGE);
//			} else {
//				current_SE.getItem(item_id).getMarqueurRel().add(segment);
//				logger.info("actionPerformedAddMarqueurRelation à l'item "
//						+ item_id + " [" + deb_current_selection_SEPane + ","
//						+ fin_current_selection_SEPane + "] - \""
//						+ segment.getText() + "\"");
//			}
//		} else {
//			// JOptionPane
//			// .showMessageDialog(
//			// annotationSE_jframe,
//			// "Attention : Le marqueur de relation que vous avez sélectionné \nn'appartient pas au primer ou à un item unique.",
//			// "Inane warning", JOptionPane.WARNING_MESSAGE);
//		}
//
//		refreshSEPane();
//	}
//
//	public void modifyHighLighterSEPane() {
//		// Modifie le HighLighter rattaché à chaque SE
//		logger.info("modifyHighLighterComponents : Modification du HighLighter de la SE courante");
//		// Highlighter myHighLigher = hashSE_highlighter.get(current_SE);
//
//		Highlighter highlighter = SEPane.getHighlighter();
//		highlighter.removeAllHighlights();
//
//		// Primer + concept + circonstant + MarqRel
//		if (current_SE.getPrimer() != null) {
//			if (current_SE.getPrimer().getConcept().size() > 0) {
//				for (Segment segment : current_SE.getPrimer().getConcept()) {
//					setHighLighter(segment, highlighter, conceptSelected);
//				}
//			}
//
//			if (current_SE.getPrimer().getCirconstant().size() > 0) {
//				for (Segment segment : current_SE.getPrimer().getCirconstant()) {
//					setHighLighter(segment, highlighter, circonstantSelected);
//				}
//			}
//
//			if (current_SE.getPrimer().getMarqueurRel().size() > 0) {
//				for (Segment segment : current_SE.getPrimer().getMarqueurRel()) {
//					setHighLighter(segment, highlighter, marqRelSelected);
//				}
//			}
//
//			if (!switch_view) {
//				setHighLighter(current_SE.getPrimer(), highlighter,
//						primerSelected);
//			}
//		}
//
//		// Items + Concept + circonstant + marqueurRel
//		if (current_SE.getItems().size() > 0) {
//
//			int index = 0;
//			for (Item it : current_SE.getItems()) {
//
//				if (it.getConcept().size() > 0) {
//					for (Segment segment : current_SE.getItem(index)
//							.getConcept()) {
//						setHighLighter(segment, highlighter, conceptSelected);
//					}
//				}
//
//				if (it.getCirconstant().size() > 0) {
//					for (Segment segment : current_SE.getItem(index)
//							.getCirconstant()) {
//						setHighLighter(segment, highlighter,
//								circonstantSelected);
//					}
//				}
//
//				if (it.getMarqueurRel().size() > 0) {
//					for (Segment segment : current_SE.getItem(index)
//							.getMarqueurRel()) {
//						setHighLighter(segment, highlighter, marqRelSelected);
//					}
//				}
//				index++;
//			}
//
//			for (Item it : current_SE.getItems()) {
//				if (!switch_view) {
//					setHighLighter(it, highlighter, itemSelected);
//				}
//			}
//		}
//
//		// Cloture
//		if (current_SE.getClot() != null) {
//			if (!switch_view) {
//				setHighLighter(current_SE.getClot(), highlighter, clotSelected);
//			}
//		}
//
//	}
//
//	public Highlighter setHighLighter(Graphical_Component toSet,
//			Highlighter highlighter, DefaultHighlighter.HighlightPainter painter) {
//		logger.info("set HighLighter : [" + toSet.getIndice_begin() + ","
//				+ toSet.getIndice_end() + "]");
//
//		try {
//			highlighter.addHighlight(toSet.getIndice_begin(),
//					toSet.getIndice_end(), painter);
//		} catch (BadLocationException e) {
//			e.printStackTrace();
//			logger.error(
//					"Threw a BadException in MIG_JPanel_Parent::setHighLighter, full stack trace follows:",
//					e);
//		}
//		return highlighter;
//	}
//
//	public void modifyHighLighterForm(Graphical_Component toModify,
//			JTextPane editorPane, DefaultHighlighter.HighlightPainter painter) {
//		/**
//		 * Change le HighLighter d'un Graphical Component dans un JEditorPane
//		 */
//		logger.info("modification HighLighterForm : ["
//				+ toModify.getIndice_begin() + "," + toModify.getIndice_end()
//				+ "] in a EditorPane ");
//
//		Highlighter highlighter = editorPane.getHighlighter();
//		Highlighter.Highlight[] highlightTab = highlighter.getHighlights();
//		int index = 0;
//		int toRemove_index = 0;
//		for (Highlighter.Highlight myEl : highlightTab) {
//			if (myEl.getStartOffset() == toModify.getIndice_begin()) {
//				if (myEl.getEndOffset() == toModify.getIndice_end()) {
//					toRemove_index = index;
//				}
//			}
//			index++;
//		}
//		editorPane.getHighlighter().removeHighlight(
//				highlightTab[toRemove_index]);
//		setHighLighterForm(toModify.getIndice_begin(),
//				toModify.getIndice_end(), editorPane, painter);
//	}
//
//	public void removeHighLighterForm(int begin, int end, JTextPane editorPane) {
//		/**
//		 * Remove
//		 */
//		logger.info("remove HighLighterForm : [" + begin + "," + end
//				+ "] in a EditorPane ");
//
//		Highlighter highlighter = editorPane.getHighlighter();
//		Highlighter.Highlight[] highlightTab = highlighter.getHighlights();
//
//		int index = 0;
//		int toRemove_index = 0;
//		for (Highlighter.Highlight myEl : highlightTab) {
//			if (myEl.getStartOffset() == begin) {
//				if (myEl.getEndOffset() == end) {
//					toRemove_index = index;
//				}
//			}
//			index++;
//		}
//		editorPane.getHighlighter().removeHighlight(
//				highlightTab[toRemove_index]);
//	}
//
//	public void removeHighLighterFormSE(Graphical_Component toDel,
//			JTextPane editorPane) {
//		logger.info("remove HighLighterFormSE : [" + toDel.getIndice_begin()
//				+ "," + toDel.getIndice_end() + "] in a EditorPane ");
//		Highlighter highlighter = editorPane.getHighlighter();
//		Highlighter.Highlight[] highlightTab = highlighter.getHighlights();
//		int index = 0;
//		int toRemove_index = 0;
//		for (Highlighter.Highlight myEl : highlightTab) {
//			if (myEl.getStartOffset() == toDel.getIndice_begin()) {
//				if (myEl.getEndOffset() == toDel.getIndice_end()) {
//					toRemove_index = index;
//				}
//			}
//			index++;
//		}
//		editorPane.getHighlighter().removeHighlight(
//				highlightTab[toRemove_index]);
//		editorPane.requestFocusInWindow();
//		editorPane.setCaretPosition(toDel.getIndice_begin());
//	}
//
//	public void setHighLighterFormPastSE() {
//		// Met en gris les SE précédemment sélectionnée
//		logger.info("setHighLighterForm : SE autre que courante en gris");
//		if (!chain_SE.isEmpty()) {
//			int index = chain_SE.indexOf(current_SE);
//			int i = 0;
//			for (Iterator<Unit> it2 = chain_SE.listIterator(0); it2.hasNext(); i++) {
//				Unit mySE = it2.next();
//
//				// CurrentSE en RED
//				if (i == index) {
//					// modifyHighLighterForm(mySE, textPane,
//					// currentSelectedRec);
//					// Le reste en GRIS
//				} else {
//					modifyHighLighterForm(mySE, textPane, pastSelected);
//				}
//			}
//		}
//	}
//
//	public void setHighLighterForm(int begin, int end, JTextPane editorPane,
//			DefaultHighlighter.HighlightPainter painter) {
//		try {
//
//			logger.info("setHighLighterForm : [" + begin + "," + end
//					+ "] in a  EditorPane ");
//
//			Highlighter highlighter = editorPane.getHighlighter();
//			highlighter.addHighlight(begin, end, painter);
//		} catch (BadLocationException e2) {
//			e2.printStackTrace();
//			logger.error(
//					"Threw a BadLocationException in MIG_JPanel_Parent::setHighLighterForm, full stack trace follows:",
//					e2);
//
//		}
//	}
//
//	public void showIndexLinkedList() {
//		// On récupére l'index courant
//		int index = chain_SE.indexOf(current_SE);
//		rightPane.annotation_id.setText("" + index);
//		logger.info("Chain_SE id : " + index);
//
//	}
//
//	public void showSizeLinkedList() {
//		if (!chain_SE.isEmpty()) {
//			rightPane.chain_SE_size.setText("" + chain_SE.size());
//			logger.info("Chain_SE size : " + chain_SE.size());
//		} else {
//			rightPane.chain_SE_size.setText("0");
//			logger.info("Chain_SE size : 0");
//		}
//	}
//
//
//	public void toolsPruneTree(HTMLDocument doc) {
//
//		// Element e = doc.getDefaultRootElement();
//		// e = e.getElement(0);
//
//		logger.info("toolsPruneTree SE from  [" + current_SE.getIndice_begin()
//				+ "," + current_SE.getIndice_end() + "] for " + document_path);
//		logger.info("toolsPruneTree SE : DocumentL : " + doc.getLength());
//		// 0 -- 99[100 - 150]151 -- Fin
//		// 200
//
//		// Debut -- deb_current_selectin-1
//		// Longueur : deb_current_selection-1 - Debut
//
//		// fin_current_selection+1 -- Fin du document
//		// Longeur : Fin - fin_current_selection+1
//
//		int longeur2 = (doc.getLength()) - current_SE.getIndice_end();
//		try {
//			doc.remove(current_SE.getIndice_end(), longeur2);
//			doc.remove(0, current_SE.getIndice_begin());
//
//		} catch (BadLocationException e1) {
//			e1.printStackTrace();
//			logger.error(
//					"Threw a BadLocationException in MIG_JPanel_Parent::toolsPruneTree, full stack trace follows:",
//					e1);
//		}
//
//	}
//
//	public Unit toolsGetSEFromPos(int deb, int fin) {
//		// TODO Clean code
//		Unit getSEFromPos = null;
//		int i = 0;
//		for (Iterator<Unit> it2 = chain_SE.listIterator(0); it2.hasNext(); i++) {
//			Unit mySE = it2.next();
//			if ((mySE.getIndice_begin() == deb)
//					&& (mySE.getIndice_end() == fin)) {
//				getSEFromPos = mySE;
//			}
//		}
//		return getSEFromPos;
//	}
//
//	public Graphical_Component toolsGetGraphicalComponentFromSouris(int pos) {
//		logger.info("toolsGetGraphicalComponent From : " + pos);
//
//		if (current_SE != null) {
//			if (current_SE.getPrimer() != null) {
//
//				// Concept du Primer
//				if (current_SE.getPrimer().hasConcept()) {
//					int index = 0;
//					for (Segment segment : current_SE.getPrimer().getConcept()) {
//
//						if (segment.getIndice_begin() <= pos
//								&& segment.getIndice_end() >= pos) {
//
//							logger.info("toolsGetGraphicalComponent : retour Concept dans le Primer");
//							return current_SE.getPrimer().getConcept()
//									.get(index);
//
//						}
//						index++;
//					}
//				}
//
//				// Circonstant du primer
//				if (current_SE.getPrimer() != null) {
//
//					if (current_SE.getPrimer().getCirconstant().size() > 0) {
//						int index_circonstant = 0;
//						for (Segment segment : current_SE.getPrimer()
//								.getCirconstant()) {
//							if (segment.getIndice_begin() <= pos
//									&& segment.getIndice_end() >= pos) {
//
//								logger.info("toolsGetGraphicalComponent : retour Circonstant dans le Primer");
//								return current_SE.getPrimer().getCirconstant()
//										.get(index_circonstant);
//
//							}
//							index_circonstant++;
//						}
//
//					}
//				}
//
//				// MarqueurRel Primer
//				if (current_SE.getPrimer() != null) {
//
//					if (current_SE.getPrimer().getMarqueurRel().size() > 0) {
//
//						for (Segment segment : current_SE.getPrimer()
//								.getMarqueurRel()) {
//
//							if (segment.getIndice_begin() <= pos
//									&& segment.getIndice_end() >= pos) {
//
//								logger.info("toolsGetGraphicalComponent : retour MarqueurRel dans Primer");
//								return segment;
//							}
//						}
//					}
//				}
//
//				// Primer
//				if (current_SE.getPrimer().getIndice_begin() <= pos
//						&& current_SE.getPrimer().getIndice_end() >= pos) {
//					logger.info("toolsGetGraphicalComponent : retour Primer");
//					return current_SE.getPrimer();
//				}
//			}
//
//			// Concept des Items
//			if (current_SE.getItems().size() > 0) {
//				int index_item = 0;
//				for (Item it : current_SE.getItems()) {
//
//					if (it.hasConcept()) {
//						int index = 0;
//						for (Segment segment : it.getConcept()) {
//							if (segment.getIndice_begin() <= pos
//									&& segment.getIndice_end() >= pos) {
//
//								logger.info("toolsGetGraphicalComponent : retour Concept dans l'item "
//										+ index_item);
//								return current_SE.getItem(index_item)
//										.getConcept().get(index);
//							}
//							index++;
//						}
//					}
//					index_item++;
//				}
//			}
//
//			// Circonstant Items
//			if (current_SE.getItems().size() > 0) {
//
//				int index_item = 0;
//				for (Item it : current_SE.getItems()) {
//
//					if (it.hasCirconstant()) {
//
//						int index_segment = 0;
//						for (Segment segment : it.getCirconstant()) {
//							if (segment.getIndice_begin() <= pos
//									&& segment.getIndice_end() >= pos) {
//
//								logger.info("toolsGetGraphicalComponent : retour Circonstant dans l'item "
//										+ index_item);
//								return current_SE.getItem(index_item)
//										.getCirconstant().get(index_segment);
//
//							}
//							index_segment++;
//						}
//					}
//					index_item++;
//				}
//			}
//
//			// MarqRel dans l'Item
//			if (current_SE.getItems().size() > 0) {
//
//				int index_item = 0;
//				for (Item it : current_SE.getItems()) {
//
//					if (it.getMarqueurRel().size() > 0) {
//
//						for (Segment segment : it.getMarqueurRel()) {
//
//							if (segment.getIndice_begin() <= pos
//									&& segment.getIndice_end() >= pos) {
//								logger.info("toolsGetGraphicalComponent : retour MarqRel dans l'item "
//										+ index_item);
//								return segment;
//							}
//						}
//					}
//					index_item++;
//				}
//			}
//
//			// Item
//			if (current_SE.getItems().size() > 0) {
//				for (Item it : current_SE.getItems()) {
//					if (it.getIndice_begin() <= pos
//							&& it.getIndice_end() >= pos) {
//						logger.info("toolsGetGraphicalComponent : retour item");
//						return it;
//					}
//				}
//			}
//
//			// Cloture
//			if (current_SE.getClot() != null) {
//
//				if (current_SE.getClot().getIndice_begin() <= pos
//						&& current_SE.getClot().getIndice_end() >= pos) {
//					logger.info("toolsGetGraphicalComponent : retour Primer");
//					return current_SE.getClot();
//				}
//			}
//		}
//		return new Primer();
//	}
//
//	public Unit toolsGetSEFromSouris(int pos) {
//		logger.info("toolsSE From : " + pos);
//
//		// Todo : Gérer les SE imbriquées
//		// Stratégie : Prendre celle dans le Begin est l'inférieur plus proche.
//
//		int i = 0;
//		int last_Begin = 0;
//		int last_End = 0;
//		for (Iterator<Unit> it2 = chain_SE.listIterator(0); it2.hasNext(); i++) {
//			Unit mySE = it2.next();
//
//			if ((mySE.getIndice_begin() <= pos)
//					&& (mySE.getIndice_end() >= pos)) {
//				last_Begin = mySE.getIndice_begin();
//				last_End = mySE.getIndice_end();
//			}
//		}
//		return toolsGetSEFromPos(last_Begin, last_End);
//	}
//
//	public void toolsRemoveSE(Unit thisSE) {
//		logger.info("toolsRemoveSE : [" + thisSE.getIndice_begin() + ","
//				+ thisSE.getIndice_end() + "]");
//		chain_SE.remove(thisSE);
//		hashSE_Doc.remove(thisSE);
//	}
//
//	public boolean toolsGetInfoPrevious() {
//		// Vérifie s'il y a quelque chose avant
//		// On suppose que current_SE est instanciée
//		logger.info("toolsGetInfoPrevious");
//		int index = chain_SE.indexOf(current_SE);
//		rightPane.annotation_id.setText("" + index);
//		if (index > 0) {
//			it = chain_SE.listIterator(index);
//			if (it.hasPrevious()) {
//				return true;
//			}
//		} else {
//			return false;
//		}
//		return false;
//	}
//
//	public boolean toolsGetInfoNext() {
//		logger.info("toolsGetInfoNext");
//		// Vérifie s'il y a quelque chose après
//		// On suppose que current_SE est instanciée
//		int index = chain_SE.indexOf(current_SE);
//		rightPane.annotation_id.setText("" + index);
//
//		if (index < (chain_SE.size() - 1)) {
//			it = chain_SE.listIterator(index);
//			it.next();
//			if (it.hasNext()) {
//				return true;
//			}
//		} else {
//			return false;
//		}
//		return false;
//	}
//
//	@Override
//	public void mouseEntered(MouseEvent e) {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void mouseExited(MouseEvent arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void mousePressed(MouseEvent e) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void mouseReleased(MouseEvent arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void mouseClicked(MouseEvent e) {
//		// TODO Auto-generated method stub
//
//		if (e.getClickCount() == 2) {
//			if (e.getSource().equals(textPane)) {
//				logger.info("Souris cliquee : TextPane");
//				// JEditorPane editor = (JEditorPane) e.getSource();
//				Point pt = new Point(e.getX(), e.getY());
//				int pos = textPane.viewToModel(pt);
//
//				Unit toTest = toolsGetSEFromSouris(pos);
//				if (toTest != null) {
//					if (!chain_SE.contains(current_SE) && (current_SE != null)) {
//						removeHighLighterFormSE(current_SE, textPane);
//					}
//					current_SE = toTest;
//					logger.info("Souris cliquée TextPane: Sélection SE");
//					init();
//				} else {
//					logger.info("Souris cliquée TextPane: Aucune sélection");
//				}
//			} else if (e.getSource().equals(SEPane)) {
//				logger.info("Souris cliquee : SEPane");
//				Point pt = new Point(e.getX(), e.getY());
//				int pos = SEPane.viewToModel(pt);
//
//				Graphical_Component myGraphicalCompo = toolsGetGraphicalComponentFromSouris(pos);
//				if (myGraphicalCompo.getIndice_begin() != 0
//						&& myGraphicalCompo.getIndice_end() != 0) {
//					logger.info("Souris cliquee SEPane: Graphical Component sélectionné");
//					// modifyHighLighterForm(myGraphicalCompo, SEPane,
//					// currentSelectedSEPane);
//					removeItemPrimer(myGraphicalCompo);
//					refreshSEPane();
//				} else {
//					logger.info("Souris cliquee SEPane: Aucune selection");
//				}
//
//			}
//		}
//	}
//
//	public void removeItemPrimer(Graphical_Component gc) {
//
//		// Suppression du concept Primer
//		if (current_SE.getPrimer() != null
//				&& current_SE.getPrimer().hasConcept()) {
//
//			if (!current_SE.getPrimer().getConcept().isEmpty()) {
//
//				int index = 0;
//				int final_del = -1;
//				for (Segment segment : current_SE.getPrimer().getConcept()) {
//					if (segment.getIndice_begin() == gc.getIndice_begin()
//							&& segment.getIndice_end() == gc.getIndice_end()) {
//
//						logger.info("removeSegment : suppression du concept dans le primer");
//						final_del = index;
//
//					}
//					index++;
//				}
//				if (final_del != -1) {
//					current_SE.getPrimer().getConcept().remove(final_del);
//				}
//			}
//		}
//
//		// Suppression du concept Items
//		if (current_SE.getItems() != null) {
//
//			int index_item = 0;
//			int index_item_del = 0;
//			int index_segment_del = -1;
//			for (Item item : current_SE.getItems()) {
//
//				int index_segment = 0;
//				for (Segment segment : current_SE.getItem(index_item)
//						.getConcept()) {
//					if (segment.getIndice_begin() == gc.getIndice_begin()
//							&& segment.getIndice_end() == gc.getIndice_end()) {
//
//						index_item_del = index_item;
//						index_segment_del = index_segment;
//
//					}
//					index_segment++;
//				}
//
//				index_item++;
//			}
//
//			if (index_segment_del != -1) {
//				logger.info("removeSegment : suppression du concept "
//						+ index_segment_del + " dans l'item " + index_item_del);
//				current_SE.getItem(index_item_del).getConcept()
//						.remove(index_segment_del);
//			}
//		}
//
//		// Suppression des circonstant dans Primer
//		if (current_SE.getPrimer() != null
//				&& current_SE.getPrimer().hasCirconstant()) {
//			if (!current_SE.getPrimer().getCirconstant().isEmpty()) {
//				int final_del = -1;
//				int index_segment = 0;
//				for (Segment segment : current_SE.getPrimer().getCirconstant()) {
//					if (segment.getIndice_begin() == gc.getIndice_begin()
//							&& segment.getIndice_end() == gc.getIndice_end()) {
//						final_del = index_segment;
//					}
//					index_segment++;
//				}
//				if (final_del != -1) {
//					logger.info("removeSegment : suppression du circonstant dans le Primer");
//					current_SE.getPrimer().getCirconstant().remove(final_del);
//				}
//			}
//		}
//
//		// Suppression des circonstant dans l'item
//		if (current_SE.getItems() != null) {
//			int index_item = 0;
//			int index_item_del = 0;
//			int index_segment_del = -1;
//			for (Item item : current_SE.getItems()) {
//
//				int index_segment = 0;
//				for (Segment segment : current_SE.getItem(index_item)
//						.getCirconstant()) {
//					if (segment.getIndice_begin() == gc.getIndice_begin()
//							&& segment.getIndice_end() == gc.getIndice_end()) {
//						index_item_del = index_item;
//						index_segment_del = index_segment;
//					}
//					index_segment++;
//				}
//				index_item++;
//			}
//			if (index_segment_del != -1) {
//				logger.info("removeSegment : suppression du circonstant "
//						+ index_segment_del + " dans l'item " + index_item_del);
//				current_SE.getItem(index_item_del).getCirconstant()
//						.remove(index_segment_del);
//			}
//		}
//
//		// Suppression du MarqRel dans le primer
//		if (current_SE.getPrimer() != null
//				&& current_SE.getPrimer().getMarqueurRel().size() > 0) {
//			if (!current_SE.getPrimer().getMarqueurRel().isEmpty()) {
//				int final_del = -1;
//				int index_segment = 0;
//				for (Segment segment : current_SE.getPrimer().getMarqueurRel()) {
//					if (segment.getIndice_begin() == gc.getIndice_begin()
//							&& segment.getIndice_end() == gc.getIndice_end()) {
//						final_del = index_segment;
//					}
//					index_segment++;
//				}
//				if (final_del != -1) {
//					logger.info("removeSegment : suppression du circonstant dans le Primer");
//					current_SE.getPrimer().getMarqueurRel().remove(final_del);
//				}
//			}
//		}
//
//		// Suppression du MarqRel dans l'item
//		if (current_SE.getItems() != null) {
//			int index_item = 0;
//			int index_item_del = 0;
//			int index_segment_del = -1;
//			for (Item item : current_SE.getItems()) {
//
//				int index_segment = 0;
//				for (Segment segment : current_SE.getItem(index_item)
//						.getMarqueurRel()) {
//					if (segment.getIndice_begin() == gc.getIndice_begin()
//							&& segment.getIndice_end() == gc.getIndice_end()) {
//						index_item_del = index_item;
//						index_segment_del = index_segment;
//					}
//					index_segment++;
//				}
//				index_item++;
//			}
//			if (index_segment_del != -1) {
//				logger.info("removeSegment : suppression du circonstant "
//						+ index_segment_del + " dans l'item " + index_item_del);
//				current_SE.getItem(index_item_del).getMarqueurRel()
//						.remove(index_segment_del);
//			}
//		}
//	}
//
//	public void removeXML() {
//		Larat_inputoutput io_mig = new Larat_inputoutput();
//		io_mig.setPath(document_path);
//		if (io_mig.hisXMLVersion(document_path)) {
//			logger.info("removeXML : suppression du XML pour " + document_path);
//			io_mig.removeThisXML(document_path);
//		} else {
//			logger.info("removeXML : pas de suppression du XML pour "
//					+ document_path);
//		}
//	}
//
//	@Override
//	public void menuSelected(MenuEvent e) {
//	}
//
//	@Override
//	public void menuCanceled(MenuEvent e) {
//	}
//
//	@Override
//	public void menuDeselected(MenuEvent e) {
//	}

}
