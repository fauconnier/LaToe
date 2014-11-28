package org.melodi.reader.larat.tools;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import org.melodi.reader.larat.internal.Unit;

import au.com.bytecode.opencsv.CSVWriter;

public class PlotAnnotation extends JFrame {

//	TreeMap<Integer, Integer> pairs;
//	LinkedList<Unit> a2_SE;
//	LinkedList<Unit> a3_SE;
//	String doc;
//
//	ArrayList<Integer> a2_notselected;
//	ArrayList<Integer> a3_notselected;
//
//	int max;
//	int length_line;
//	int y_a2;
//	int y_a3;
//	int margin_line;
//
//	int larg_rect;
//
//	boolean visible;
//
//	RunAndWait a1_mig;
//	RunAndWait a2_mig;
//
//	String tmp_annotateur;
//	int tmp_curr_index_se;
//
//	JTable pairs_table;
//	JTextField pairs_field;
//	JFrame jframe_table;
//	JTextArea label1;
//
//	public PlotAnnotation(TreeMap pairs, LinkedList<Unit> a2_SE,
//			LinkedList<Unit> a3_SE, String doc, boolean visible) {
//		super("Simple Adjudication Tool : " + doc);
//		this.pairs = pairs;
//		this.a2_SE = a2_SE;
//		this.a3_SE = a3_SE;
//
//		this.length_line = 1500;
//		this.y_a2 = 200;
//		this.y_a3 = 400;
//		this.margin_line = 25;
//
//		this.larg_rect = 20;
//		this.doc = doc;
//		this.visible = visible;
//
//		this.a1_mig = new RunAndWait(doc,"Phase_1", "Julien", 0);
//		this.a2_mig = new RunAndWait(doc,"Phase_1","Sophie", 0);
//
//		tmp_annotateur = "";
//		tmp_curr_index_se = -1;
//
//		this.runJFramePairs();
//
//	}
//
//	public void runJFramePairs() {
//
//		// Jframe
//		jframe_table = new JFrame("Pairs");
//		jframe_table.setSize(300, 300);
//		jframe_table.setBackground(Color.WHITE);
//		jframe_table.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		this.label1 = new JTextArea();
//		String toPrint = "";
//		// dessiner les lignes
//		Set cles = pairs.keySet();
//		Iterator it = cles.iterator();
//		while (it.hasNext()) {
//			int cle = (Integer) it.next(); // tu peux typer plus finement ici
//			int valeur = (Integer) pairs.get(cle); // tu peux typer plus
//													// finement ici
//			System.out.println("idDocA2= " + cle + "\tidDocA3=" + valeur);
//			toPrint = toPrint + cle + "\t" + valeur + "\n";
//		}
//		System.out.println("Nb de pairs : " + pairs.size());
//		label1.setText(toPrint);
//
//		JScrollPane scrollPane = new JScrollPane(label1);
//		scrollPane
//				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		scrollPane
//				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//
//		jframe_table.add(scrollPane, BorderLayout.CENTER);
//
//		this.pairs_field = new JTextField();
//
//		this.pairs_field.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println(pairs_field.getText());
//				updateJFramePairs(pairs_field.getText());
//				pairs_field.setText("");
//				// myTextArea.append(pairs_field.getText() + "\n");
//			}
//		});
//
//		jframe_table.add(this.pairs_field, BorderLayout.AFTER_LAST_LINE);
//
//		jframe_table.setVisible(true);
//
//	}
//
//	public void updateJFramePairs(String cmd) {
//
//		// 1. Interpréter la commande
//		if (!cmd.equals("")) {
//
//			// Enlever les espaces et les retours à la ligne.
//			cmd.replaceAll("\\s+", "");
//
//			if (cmd.contains("p")) {
//				// Nouvelle paire
//
//				// Match de deux nombres séparés par un "-"
//				Pattern p = Pattern.compile("([0-9]+)-([0-9]+)");
//				Matcher m = p.matcher(cmd);
//
//				if (m.find()) {
//					// Création de la paire
//					String first_pair = m.group(1);
//					String second_pair = m.group(2);
//
//					System.err.println(first_pair + "\t" + second_pair);
//					int first = Integer.parseInt(first_pair);
//					int second = Integer.parseInt(second_pair);
//					pairs.put(first, second);
//				} else {
//					System.err.println("Rien matché");
//				}
//
//			} else if (cmd.contains("e")) {
//
//				// Match de deux nombres séparés par un "-"
//				Pattern p = Pattern.compile("([0-9]+)e");
//				Matcher m = p.matcher(cmd);
//
//				if (m.find()) {
//					// Création de la paire
//					String first_pair = m.group(1);
//
//					int first = Integer.parseInt(first_pair);
//					pairs.remove(first);
//
//				} else {
//					System.err.println("Rien matché");
//				}
//				
//
//			}
//
//		}
//
//		// 2. Update JTextField
//		Set cles = pairs.keySet();
//		Iterator it = cles.iterator();
//		String toPrint = "";
//		while (it.hasNext()) {
//			int cle = (Integer) it.next(); // tu peux typer plus finement ici
//			int valeur = (Integer) pairs.get(cle); // tu peux typer plus
//													// finement ici
//			// System.out.println("idDocA2= " + cle + "\tidDocA3="+valeur);
//			toPrint = toPrint + cle + "\t" + valeur + "\n";
//		}
//		label1.setText(toPrint);
//
//		this.update(this.getGraphics());
//
//		// 3. Ecrire le CSV.
//		try {
//			writeCSV();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			System.err.println("Erreur dans l'écriture");
//			System.exit(0);
//			e.printStackTrace();
//		}
//
//	}
//
//	public void writeCSV() throws IOException {
//		String path = "data/csv_phase1/pairs_review/" + doc + "_pairs_review.csv";
//
////		CSVWriter writer = new CSVWriter(new FileWriter(path), ',');
////		
////		String header = "document#idSE_docA2#idSE_docA3";
////		String[] entries2 = header.split("#");
////		writer.writeNext(entries2);
////		
////		Set cles = pairs.keySet();
////		Iterator it = cles.iterator();
////		String toPrint = "";
////		while (it.hasNext()) {
////			int cle = (Integer) it.next(); 
////			int valeur = (Integer) pairs.get(cle); 
////			String tabToPrint = doc + "#" + cle + "#" + valeur;
////			String[] entries = tabToPrint.split("#");
////			writer.writeNext(entries);
////		}
////		
////		writer.close();
//
//	}
//
//	public void plot() {
//
//		this.setSize(1600, 600);
//		this.setBackground(Color.WHITE);
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//		this.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//
//				drawSEUnique(e.getX(), e.getY());
//			}
//		});
//
//		this.setVisible(true);
//	}
//
//	public void close() {
//
//		setVisible(false);
//		dispose();
//	}
//
//	public void paint(Graphics g) {
//		g.clearRect(1, 1, 1600, 800);
//		this.drawDocName();
//		this.drawLine();
//		this.drawPairs();
//		this.drawSE();
//		try {
//			writeCSV();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			System.err.println("Erreur dans l'écriture");
//			System.exit(0);
//			e.printStackTrace();
//		}
//	}
//
//	public void drawSEUnique(int x, int y) {
//		System.out.printf("Mouse clicked:\tx= %d\ty= %d\n", x, y);
//
//		// 1. Récupérer la SE correspondante.
//		Unit currSE = returnSE(x, y);
//
//		// 2. l'Afficher à la manière de LARAt
//		if (currSE != null & tmp_annotateur.equals("Julien")) {
//			a1_mig.refresh(tmp_curr_index_se);
//		}
//		if (currSE != null & tmp_annotateur.equals("Sophie")) {
//			a2_mig.refresh(tmp_curr_index_se);
//		}
//
//		// 3. afficher les paires dans l'interface.
//		this.update(this.getGraphics());
//		this.paint(this.getGraphics());
//
//	}
//
//	public Unit returnSE(int x, int y) {
//
//		Unit toReturn = new Unit();
//		toReturn.setId(100);
//		for (Unit currSE : a2_SE) {
//
//			int start_currSE = currSE.getIndice_begin();
//			int end_currSE = currSE.getIndice_end();
//
//			double tmp_x1 = (double) start_currSE / max;
//			tmp_x1 = tmp_x1 * length_line;
//			int x1_norm = (int) Math.round(tmp_x1);
//
//			int tmp_long = end_currSE - start_currSE;
//			double tmp_long_norm = (double) tmp_long / max;
//			tmp_long_norm = tmp_long_norm * length_line;
//			int long_norm_round = (int) Math.round(tmp_long_norm);
//
//			int x1_rect = x1_norm;
//			int y1_rect = y_a2 - (larg_rect / 2);
//			int long_rect = long_norm_round;
//			int height_rect = larg_rect;
//
//			if (x >= x1_rect && y >= y1_rect && x <= x1_rect + long_rect
//					&& y <= y1_rect + larg_rect) {
//				toReturn = currSE;
//				tmp_annotateur = "Julien";
//				tmp_curr_index_se = currSE.getId();
//			}
//		}
//
//		for (Unit currSE : a3_SE) {
//
//			int start_currSE = currSE.getIndice_begin();
//			int end_currSE = currSE.getIndice_end();
//
//			double tmp_x1 = (double) start_currSE / max;
//			tmp_x1 = tmp_x1 * length_line;
//			int x1_norm = (int) Math.round(tmp_x1);
//
//			int tmp_long = end_currSE - start_currSE;
//			double tmp_long_norm = (double) tmp_long / max;
//			tmp_long_norm = tmp_long_norm * length_line;
//			int long_norm_round = (int) Math.round(tmp_long_norm);
//
//			int x1_rect = x1_norm;
//			int y1_rect = y_a3 - (larg_rect / 2);
//			int long_rect = long_norm_round;
//			int height_rect = larg_rect;
//
//			if (x >= x1_rect && y >= y1_rect && x <= x1_rect + long_rect
//					&& y <= y1_rect + larg_rect) {
//				toReturn = currSE;
//				tmp_annotateur = "Sophie";
//				tmp_curr_index_se = currSE.getId();
//			}
//		}
//
//		return toReturn;
//	}
//
//	public void drawSE() {
//		Graphics g = this.getGraphics();
//
//		// A2
//		for (Unit currSE : a2_SE) {
//
//			int start_currSE = currSE.getIndice_begin();
//			int end_currSE = currSE.getIndice_end();
//
//			// x1
//			// System.out.println("x1_rect_origin : " + start_currSE);
//			double tmp_x1 = (double) start_currSE / max;
//			tmp_x1 = tmp_x1 * length_line;
//			int x1_norm = (int) Math.round(tmp_x1);
//			// System.out.println("x1_rect_normal : " + calculate);
//			// System.out.println("x1_rect_int : " + final_v);
//
//			// longeur :
//			int tmp_long = end_currSE - start_currSE;
//			double tmp_long_norm = (double) tmp_long / max;
//			tmp_long_norm = tmp_long_norm * length_line;
//			// System.out.println("Longeur origin : " + tmp_long);
//			// System.out.println("Longeur norm : " + tmp_long_norm);
//			int long_norm_round = (int) Math.round(tmp_long_norm);
//			// System.out.println("Longueur norm rounded : " + long_norm_round);
//
//			int x1_rect = x1_norm;
//			int y1_rect = y_a2 - (larg_rect / 2);
//			int long_rect = long_norm_round;
//			int height_rect = larg_rect;
//
//			boolean flag = false;
//			if (a2_notselected.contains(currSE.getId())) {
//				g.setColor(Color.red);
//				g.fillRect(x1_rect, y1_rect, long_rect, height_rect);
//				g.setColor(Color.black);
//				flag = true;
//			} else {
//				if (currSE.getAxe_visuel().contains("Horizontal")) {
//					g.setColor(Color.orange);
//					g.drawRect(x1_rect + 1, y1_rect + 1, long_rect, height_rect);
//					g.setColor(Color.black);
//				} else {
//					g.setColor(Color.gray);
//					g.drawRect(x1_rect + 1, y1_rect + 1, long_rect, height_rect);
//					g.setColor(Color.black);
//				}
//			}
//
//			int string_x = x1_rect;
//			int string_y = y_a2 - larg_rect;
//			if (!flag) {
//				g.drawString(currSE.getId() + "", string_x, string_y);
//			} else {
//				g.drawString(currSE.getId() + "", string_x, string_y - 20);
//			}
//		}
//
//		// A3
//		for (Unit currSE : a3_SE) {
//
//			int start_currSE = currSE.getIndice_begin();
//			int end_currSE = currSE.getIndice_end();
//
//			// x1
//			// System.out.println("x1_rect_origin : " + start_currSE);
//			double tmp_x1 = (double) start_currSE / max;
//			tmp_x1 = tmp_x1 * length_line;
//			int x1_norm = (int) Math.round(tmp_x1);
//			// System.out.println("x1_rect_normal : " + calculate);
//			// System.out.println("x1_rect_int : " + final_v);
//
//			// longeur :
//			int tmp_long = end_currSE - start_currSE;
//			double tmp_long_norm = (double) tmp_long / max;
//			tmp_long_norm = tmp_long_norm * length_line;
//			// System.out.println("Longeur origin : " + tmp_long);
//			// System.out.println("Longeur norm : " + tmp_long_norm);
//			int long_norm_round = (int) Math.round(tmp_long_norm);
//			// System.out.println("Longueur norm rounded : " + long_norm_round);
//
//			int x1_rect = x1_norm;
//			int y1_rect = y_a3 - (larg_rect / 2);
//			int long_rect = long_norm_round;
//			int height_rect = larg_rect;
//
//			// Rectangle
//			boolean flag = false;
//			if (a3_notselected.contains(currSE.getId())) {
//				g.setColor(Color.red);
//				g.fillRect(x1_rect, y1_rect, long_rect, height_rect);
//				g.setColor(Color.black);
//				flag = true;
//			} else {
//				if (currSE.getAxe_visuel().contains("Horizontal")) {
//					g.setColor(Color.orange);
//					g.drawRect(x1_rect + 1, y1_rect + 1, long_rect, height_rect);
//					g.setColor(Color.black);
//				} else {
//					g.setColor(Color.gray);
//					g.drawRect(x1_rect + 1, y1_rect + 1, long_rect, height_rect);
//					g.setColor(Color.black);
//				}
//
//			}
//
//			// id String
//			int string_x = x1_rect;
//			int string_y = y_a3 + larg_rect;
//			if (!flag) {
//				g.drawString(currSE.getId() + "", string_x, string_y);
//			} else {
//				g.drawString(currSE.getId() + "", string_x, string_y + 20);
//			}
//		}
//
//	}
//
//	public void drawLine() {
//		Graphics g = this.getGraphics();
//
//		// Max end_SE
//		int max = 0;
//		for (Unit currSE : a2_SE) {
//			if (currSE.getIndice_end() > max) {
//				max = currSE.getIndice_end();
//			}
//		}
//		for (Unit currSE : a3_SE) {
//			if (currSE.getIndice_end() > max) {
//				max = currSE.getIndice_end();
//			}
//		}
//		max = max + 100;
//		System.out.println("Max doc= " + max);
//		this.max = max;
//
//		// NOrmalize
//		// [ x / max ] * 1000
//
//		g.drawLine(margin_line, y_a2, length_line, y_a2);
//		g.drawLine(margin_line, y_a3, length_line, y_a3);
//
//		g.drawString("A1", margin_line, y_a2 - (2 * larg_rect));
//		g.drawString("A2", margin_line, y_a3 + (2 * larg_rect));
//	}
//
//	public void drawDocName() {
//		Graphics g = this.getGraphics();
//
//		g.drawString(doc, length_line / 2, 100);
//
//	}
//
//	public void drawPairs() {
//
//		// dessiner les lignes
//		Set cles = pairs.keySet();
//		Iterator it = cles.iterator();
//		while (it.hasNext()) {
//			int cle = (Integer) it.next(); // tu peux typer plus finement ici
//			int valeur = (Integer) pairs.get(cle); // tu peux typer plus
//													// finement ici
//			System.out.println("idDocA2= " + cle + "\tidDocA3=" + valeur);
//			this.connectPairs(cle, valeur);
//		}
//		System.out.println("Nb de pairs : " + pairs.size());
//
//		// Récupérer les SE non sélectionnées
//		this.a2_notselected = new ArrayList<Integer>();
//		this.a3_notselected = new ArrayList<Integer>();
//
//		// A2
//		for (Unit currSE : a2_SE) {
//			if (!pairs.containsKey(currSE.getId())) {
//				a2_notselected.add(currSE.getId());
//			}
//		}
//		// A3
//		for (Unit currSE : a3_SE) {
//			if (!pairs.containsValue(currSE.getId())) {
//				a3_notselected.add(currSE.getId());
//			}
//		}
//
//	}
//
//	public void connectPairs(Integer idDocA2, Integer idDocA3) {
//		Graphics g = this.getGraphics();
//
//		int index_a2 = getIndex(a2_SE, idDocA2);
//		int index_a3 = getIndex(a3_SE, idDocA3);
//
//		Unit se_a2 = a2_SE.get(index_a2);
//		Unit se_a3 = a3_SE.get(index_a3);
//
//		// Milieu se A2
//		int x1 = getMiddleX(se_a2, y_a2);
//		int y1 = getMiddleY(se_a2, y_a2);
//
//		// Mileu se A3
//		int x2 = getMiddleX(se_a3, y_a3);
//		int y2 = getMiddleY(se_a3, y_a3);
//
//		g.drawLine(x1, y1, x2, y2);
//
//	}
//
//	public int getMiddleX(Unit currSE, int y_a) {
//
//		double tmp_x1 = (double) currSE.getIndice_begin() / max;
//		tmp_x1 = tmp_x1 * length_line;
//		int x1_norm = (int) Math.round(tmp_x1);
//
//		return x1_norm + 2;
//	}
//
//	public int getMiddleY(Unit currSE, int y_a) {
//
//		int y = y_a;
//		return y;
//	}
//
//	public static int getIndex(LinkedList<Unit> list_SE, int id_doc) {
//		// Il est possible que cette classe soit redondante par rapport
//		// aux identifiants, mais dans le doute,..
//		int index = 0;
//		int index_sortie = -1;
//		for (Unit currSE : list_SE) {
//			if (currSE.getId() == id_doc) {
//				index_sortie = index;
//			}
//			index++;
//		}
//		return index_sortie;
//	}
//
//	public void write(String path_img) {
//		try {
//			Graphics g = this.getGraphics();
//			Robot robot = new Robot();
//			this.paint(g);
//			g.dispose();
//			BufferedImage bi = robot.createScreenCapture(new Rectangle(1600,
//					600));
//			ImageIO.write(bi, "png", new File(path_img));
//
//		} catch (IOException | AWTException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

}
