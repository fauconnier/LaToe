package org.melodi.tools.evaluation;

import java.util.ArrayList;

public class Label_Service extends ArrayList<Label_Instance>{

	
	public Label_Instance getResultsFor(String label){
		
		Label_Instance label_instance = new Label_Instance();
		
		for(Label_Instance currLabel : this){
			if(currLabel.getName().equals(label)){
				label_instance = currLabel;
			}
		}
		return label_instance;
	}
}
