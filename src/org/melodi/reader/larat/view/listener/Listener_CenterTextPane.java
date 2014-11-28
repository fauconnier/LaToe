package org.melodi.reader.larat.view.listener;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import org.melodi.reader.larat.controler.LaratControler;
import org.melodi.reader.larat.view.CenterTextPane;

public class Listener_CenterTextPane implements CaretListener, MouseListener{
	
	
	LaratControler controler;
	CenterTextPane centerTextPane;
	
	
	public String current_selection_CenterPane;
	public int start_current_selection_CenterPane;
	public int end_current_selection_CenterPane;
	public int last_current_selection_CenterPane;
	
	/**
	 * Listen to events from textPane and notifies those
	 * to controler.
	 * @param controler
	 */
	public Listener_CenterTextPane(CenterTextPane centerTextPane, LaratControler controler){
		this.centerTextPane=centerTextPane;
		this.controler=controler;
		this.current_selection_CenterPane = "";
		this.start_current_selection_CenterPane = -1;
		this.end_current_selection_CenterPane = -1;
		this.last_current_selection_CenterPane = -1;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (e.getClickCount() == 2) {
			Point pt = new Point(e.getX(), e.getY());
			int pos = centerTextPane.viewToModel(pt);
			controler.actionSelectSubAnnotationAtChar(pos);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
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
					String textString = centerTextPane.getText(pos, dif);
					current_selection_CenterPane = textString;
					current_selection_CenterPane.replace("\n", "<br>");
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				start_current_selection_CenterPane = pos;
				end_current_selection_CenterPane = pos + dif;

			} else if (dot > mark) {
				pos = mark;
				dif = dot - mark;
				min = mark;
				max = dot;

				try {
					String textString = centerTextPane.getText(pos, dif);
					current_selection_CenterPane = textString;
					current_selection_CenterPane.replace("\n", "<br>");
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				start_current_selection_CenterPane = pos;
				end_current_selection_CenterPane = pos + dif;

			} else if (dot == mark) {
			}

			last_current_selection_CenterPane = end_current_selection_CenterPane;
			controler.notifyCaretCenterUpdate(current_selection_CenterPane, start_current_selection_CenterPane, end_current_selection_CenterPane);
	}
}
