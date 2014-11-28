package org.melodi.reader.larat.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;

import org.melodi.reader.larat.controler.LaratControler;

public class Listener_RightButtons implements ActionListener{
	LaratControler controler;
	private JEditorPane commentTextPane;
	private JComboBox fonctionComboBox;
	
	 public Listener_RightButtons(JEditorPane commentTextPane, JComboBox fonctionComboBox, LaratControler controler){
		 this.controler=controler;
		 this.commentTextPane = commentTextPane;
		 this.fonctionComboBox = fonctionComboBox;
	 }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if("next".equals(e.getActionCommand())) {
			controler.actionPerformedNextUnit();
		}
		else if("previous".equals(e.getActionCommand())) {
			controler.actionPerformedPreviousUnit();
		}
		else if("valid".equals(e.getActionCommand())) {
			
			String comment = commentTextPane.getText();
			String annotateur = fonctionComboBox.getSelectedItem()+"";
			controler.actionPerformedValid(comment, annotateur);
			
		}
		else{
			controler.actionSelectProperty(e.getActionCommand());
		}
		
	}
	 
	 

}
