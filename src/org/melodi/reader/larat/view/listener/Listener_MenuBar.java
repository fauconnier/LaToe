package org.melodi.reader.larat.view.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.melodi.reader.larat.controler.LaratControler;

public class Listener_MenuBar implements ActionListener, MenuListener {
	private LaratControler controler;
	
	/**
	 * Listen to events from menu bar and notifies those
	 * to controler.
	 * @param controler
	 */
	public Listener_MenuBar(LaratControler controler){
		this.controler=controler;
	}
	
	public void actionPerformed(ActionEvent e) {
		String str = ((JMenuItem)e.getSource()).getText();

		if(str.equals("Open a file")){
			this.controler.actionPerformedOpenFile();
		}
		else if(str.equals("Save")){
			controler.actionPerformedValid("","");
		}
		else if(str.equals("Quit")){
			controler.actionPerformedQuit();	
		}
		else if(str.equals("About")){
			controler.actionPerformedAbout();
		}
	}

	@Override
	public void menuCanceled(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuDeselected(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuSelected(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}
}

