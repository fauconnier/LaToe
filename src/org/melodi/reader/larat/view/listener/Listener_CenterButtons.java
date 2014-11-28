package org.melodi.reader.larat.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.melodi.reader.larat.controler.LaratControler;
import org.melodi.reader.larat.view.Larat_CenterPanel;

public class Listener_CenterButtons implements ActionListener{
	LaratControler controler;
	Larat_CenterPanel centerPanel;
	
	public Listener_CenterButtons(Larat_CenterPanel centerPanel, LaratControler controler){
		this.controler=controler;
		this.centerPanel=centerPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	
		/*
		 * Center Buttons
		 */
		if ("addUnit".equals(e.getActionCommand())) {
			controler.actionPerformedAddSE();
		} else if ("delUnit".equals(e.getActionCommand())) {
			controler.actionPerformedDelSE();
		}

		/*
		 * Boutons Sélection Button
		 */
		if ("addPrimer".equals(e.getActionCommand())) {
			controler.actionPerformedAddPrimer();
		} else if ("addItem".equals(e.getActionCommand())) {
			controler.actionPerformedAddItem();
		} else if ("clearAll".equals(e.getActionCommand())) {
			controler.actionPerformedClearThis();
		} else if ("cloture".equals(e.getActionCommand())) {
			controler.actionPerformedAddClot();
		}

		/*
		 * Boutons Concepts/Circonstant/Marqueurs
		 */
		if ("addConcept".equals(e.getActionCommand())) {
			controler.actionPerformedAddConcept(centerPanel);
		} else if ("addCirconstant".equals(e.getActionCommand())) {
			controler.actionPerformedAddCirconstant(centerPanel);
		} else if ("addMarqRel".equals(e.getActionCommand())) {
			controler.actionPerformedAddMarqRel(centerPanel);
		} else if ("switch".equals(e.getActionCommand())) {
			controler.actionPerformedSwitch(centerPanel);
		}
	}

}
