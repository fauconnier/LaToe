package org.melodi.reader.larat.view.listener;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import org.melodi.reader.larat.controler.LaratControler;
import org.melodi.reader.larat.internal.Graphical_Component;
import org.melodi.reader.larat.internal.Unit;
import org.melodi.reader.larat.view.LeftTextPane;

public class Listener_LeftTextPane implements CaretListener, MouseListener {
	private LaratControler controler;
	private LeftTextPane textPane;

	public String current_selection_textPane;
	public int deb_current_selection_textPane;
	public int fin_current_selection_textPane;

	/**
	 * Listen to events from textPane and notifies those to controler.
	 * 
	 * @param controler
	 */
	public Listener_LeftTextPane(LeftTextPane  textPane, LaratControler controler) {
		this.textPane = textPane;
		this.controler = controler;
		this.current_selection_textPane = "";
		this.deb_current_selection_textPane = -1;
		this.fin_current_selection_textPane = -1;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			Point pt = new Point(e.getX(), e.getY());
			int pos = textPane.viewToModel(pt);
			controler.actionSelectAnnotationAtChar(pos);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void caretUpdate(CaretEvent e) {

		int dot = e.getDot();
		int mark = e.getMark();
		int pos = 0;
		int dif = 0;
		int min = 0;
		int max = 0;

		if (dot < mark) {
			pos = dot;
			dif = mark - dot;
			min = dot;
			max = mark;
			try {
				String textString = textPane.getText(pos, dif);
				current_selection_textPane = textString;
				current_selection_textPane.replace("\n", "<br>");
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			deb_current_selection_textPane = pos;
			fin_current_selection_textPane = pos + dif;

		} else if (dot > mark) {
			pos = mark;
			dif = dot - mark;
			min = mark;
			max = dot;
			try {
				String textString = textPane.getText(pos, dif);
				current_selection_textPane = textString;
				current_selection_textPane.replace("\n", "<br>");
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			deb_current_selection_textPane = pos;
			fin_current_selection_textPane = pos + dif;
		} 
		controler.notifyCaretLeftUpdate(current_selection_textPane, deb_current_selection_textPane, fin_current_selection_textPane);
	}

}
