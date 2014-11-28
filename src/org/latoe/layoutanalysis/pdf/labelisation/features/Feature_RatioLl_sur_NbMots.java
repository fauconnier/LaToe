package org.latoe.layoutanalysis.pdf.labelisation.features;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.latoe.layoutanalysis.pdf.pdfobject.Chunk_PDF;
import org.latoe.layoutanalysis.pdf.pdfobject.Document_PDF;
import org.latoe.layoutanalysis.pdf.pdfobject.Page_PDF;
import org.melodi.learning.iitb.CRF.DataSequence;
import org.melodi.learning.iitb.Model.FeatureGenImpl;
import org.melodi.learning.iitb.Model.FeatureImpl;
import org.melodi.learning.iitb.Model.FeatureTypes;

public class Feature_RatioLl_sur_NbMots extends FeatureTypes {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String fname;

	int stateId;
	int numStates;
	String nameFeatures;
	PrintWriter out;

	public Feature_RatioLl_sur_NbMots(FeatureGenImpl m) {
		super(m);
		numStates = m.numStates();
		fname = "MargeGauche";
		try {
			this.out=new PrintWriter(new FileOutputStream("./data/Output/Ratio_police.csv"));
			out.println("\"ratio\"" + "," + "\"type\"");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean startScanFeaturesAt(DataSequence data, int prevPos, int pos) {
		// System.err.print("startScanFeaturesAt()   ");
		Document_PDF d = (Document_PDF) data;
		Chunk_PDF currChunk = (Chunk_PDF) d.x(pos);


		if (ratio(currChunk, d)) {

			stateId = 0;
			return true;
		

		} else {
			stateId = numStates;
			return false;
		}
	}
	
	public boolean ratio(Chunk_PDF chunkToTest, Document_PDF d){
		boolean flag = false;
		
		
		List<Page_PDF> pageList = d.pages;
		for(Page_PDF currPage : pageList){
			for(Chunk_PDF currChunk : currPage.groupes){
				
				// Ratio : L*l / (nb mots* mode_taille_police);
				//L : x2 - x1
				//l : y2 - y1
				int Longueur = currChunk.x2 - currChunk.x1;
				int largeur = currChunk.y2 - currChunk.y1;
				int nb_mots = currChunk.mots.size();
				int taille_police = currChunk.getModeTaillePolice();
				int ratio = ( ((Longueur*largeur) * taille_police) / (nb_mots)) ;
				
				System.out.println("\""+ratio + "\"" + "," + "\""+ currChunk.type + "\"");
			}
		}
		
		
		return flag;
	}

	@Override
	public boolean hasNext() {
		return (stateId < numStates);
	}

	@Override
	public void next(FeatureImpl f) {
		// On créé un nouveau feature "vide".
		// Ce nouveau feature vide va être créé
		// pour chacun des labels
		// et chacun avec une valeur de 1.0 au début
		setFeatureIdentifier(stateId, stateId, fname, f);

		f.yend = stateId;
		f.ystart = -1;
		f.val = 1;
		stateId++;
	}

}




