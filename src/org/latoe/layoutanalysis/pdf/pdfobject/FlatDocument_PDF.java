package org.latoe.layoutanalysis.pdf.pdfobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/*
 * Représentation de tous les chunks du document
 */
public class FlatDocument_PDF {
	List<Chunk_PDF> internalList;

	public FlatDocument_PDF(){
		this.internalList = new ArrayList<Chunk_PDF>();
	}
	
	public void addPages(List<Page_PDF> listPage){
		for(Page_PDF currP : listPage){
			List<Chunk_PDF> listChunk = currP.groupes;
			this.addChunkList(listChunk);
		}
	}
	
	public void addChunkList(List<Chunk_PDF> listChunkToAdd){
		internalList.addAll(0, listChunkToAdd);
	}

	public List<Chunk_PDF> getListChunk(){
		return internalList;
	}
	

}
