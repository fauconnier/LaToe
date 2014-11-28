package org.melodi.reader.larat.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.html.HTMLDocument;

import org.melodi.reader.larat.controler.LaratControler;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.reader.larat.model.Larat_Metadata;
import org.melodi.reader.larat.view.listener.Listener_CenterTextPane;
import org.melodi.reader.larat.view.listener.Listener_LeftTextPane;
import org.melodi.reader.larat.view.listener.Listener_MenuBar;

public class LaratView extends JFrame {

	private LaratControler controler;
	private JPanel larat_ParentPanel;
	private String title_head = "LARAt : R1.1.6b";
	
	/*
	 * Variables from models
	 */
	private HTMLDocument currDocument;
	private LinkedList<Unit> chainUnits;
	private int currIndexUnit;
	private Larat_Metadata currDocMetadata;
	
	/*
	 * Panels and TextPane
	 */
	private Larat_LeftPanel leftPanel;
	private Larat_CenterPanel centerPanel;
	private Larat_RightPanel rightPanel;

	/**
	 * Constructor
	 */
	public LaratView(LaratControler controler) {
		// 1. Initialize controler and model variables
		this.controler = controler;
		this.currDocument = new HTMLDocument();
		this.chainUnits = new LinkedList<Unit>();
		this.currIndexUnit = -1;
		controler.notifyUpdate(currDocument,chainUnits,currIndexUnit);
		
		// 2. Configure view
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		this.setSize(1280, 800);
		this.setTitle(title_head);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(true);
		this.addMenu();
		
		// 3. Add Panels
		this.initComposant();
		this.setContentPane(larat_ParentPanel);
		
		// 4. Visible
		this.setVisible(true);
	}
	
	private void initComposant(){
		
		larat_ParentPanel =  new JPanel();
		larat_ParentPanel.setLayout(new GridLayout(1, 3));
		
		// 1. leftPanel
		leftPanel = new Larat_LeftPanel(controler);
		
		// 2. centerPanel
		centerPanel = new Larat_CenterPanel(controler);
		
		// 3. rightPanel
		rightPanel = new Larat_RightPanel(controler);

		// 4. Merge leftPanel and centerPanel
		JSplitPane left_center_splitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				leftPanel, centerPanel);
		left_center_splitPanel.setOneTouchExpandable(true);
		left_center_splitPanel.setResizeWeight(0.90);
		larat_ParentPanel.add(left_center_splitPanel);

		// 5. Add rightPanel to splitPane
		JSplitPane all_panels_splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				left_center_splitPanel, rightPanel);
		all_panels_splitPane.setOneTouchExpandable(true);
		all_panels_splitPane.setResizeWeight(0.999);

		larat_ParentPanel.add(all_panels_splitPane);
	}

	
	/**
	 * Update View from Larat_Model
	 * @param Document doc, Linked<SE> SE, int currentSE
	 */
	public void update(HTMLDocument currDocument, LinkedList<Unit> chainUnits, int currIndexUnit, Larat_Metadata currDocMetadata) {
	
		// 1. Update model variables and notify controler
		this.currDocument = currDocument;
		this.chainUnits = chainUnits;
		this.currIndexUnit = currIndexUnit;
		this.currDocMetadata = currDocMetadata;
		controler.notifyUpdate(currDocument,chainUnits,currIndexUnit);
		
		
		// 2. Update Panels and TextPane
		leftPanel.update(currDocument, chainUnits, currIndexUnit,currDocMetadata);
		centerPanel.update(currDocument,chainUnits,currIndexUnit,currDocMetadata);
		rightPanel.update(currDocument, chainUnits, currIndexUnit,currDocMetadata);
	}
	

	
	
	

	/**
	 * Add menu bar to LaratView
	 */
	private void addMenu() {
		
		// 1. menuBar and Listener
		JMenuBar menuBar = new JMenuBar();
		Listener_MenuBar menuListener = new Listener_MenuBar(controler);
		
		// 2. Menu 'File'
		JMenu menuFile = new JMenu("File");
		menuFile.addMenuListener(menuListener);
		menuBar.add(menuFile);

		JMenuItem itemOpenFile = new JMenuItem("Open a file", KeyEvent.VK_O);
		itemOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.ALT_MASK));
		itemOpenFile.addActionListener(menuListener);
		menuFile.add(itemOpenFile);

		JMenuItem itemSaveFile = new JMenuItem("Save", KeyEvent.VK_S);
		itemSaveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.ALT_MASK));
		itemSaveFile.addActionListener(menuListener);
		menuFile.add(itemSaveFile);
		
		menuFile.addSeparator();

		JMenuItem itemQuit = new JMenuItem("Quit", KeyEvent.VK_Q);
		itemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.ALT_MASK));
		menuFile.add(itemQuit);
		itemQuit.addActionListener(menuListener);

		// 3. Menu "Credits"
		JMenu menuCredit = new JMenu("Credits");
		menuCredit.setMnemonic(KeyEvent.VK_N);
		menuBar.add(menuCredit);

		JMenuItem itemAbout = new JMenuItem("About", KeyEvent.VK_T);
		itemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.ALT_MASK));
		menuCredit.add(itemAbout);
		itemAbout.addActionListener(menuListener);

		// 4. Add Menu bar to LaratView
		this.setJMenuBar(menuBar);
	}



}
