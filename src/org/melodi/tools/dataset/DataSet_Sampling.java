package org.melodi.tools.dataset;

import java.util.ArrayList;


public class DataSet_Sampling extends ArrayList<DataSet_Corpora> implements java.io.Serializable{
	
	public DataSet_Sampling(){
		
	}
	
	public DataSet_Corpora getRandomCorpora(){
		int nb_element = this.size();
		int get_nb_element = nb_element -1;
		int random = 0 + (int)(Math.random() * ((get_nb_element - 0) + 1));
		
		return this.get(random);
	}
	
	public int getTotalSize(){
		int total = 0;
		for(DataSet_Corpora currCorpora : this){
			total += currCorpora.size();
		}
		return total;
	}
	

}
