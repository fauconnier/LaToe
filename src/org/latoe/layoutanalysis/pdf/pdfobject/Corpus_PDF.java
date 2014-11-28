package org.latoe.layoutanalysis.pdf.pdfobject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

public class Corpus_PDF {

	String trainingDir;
	List<Document_PDF> listDoc;

	/**
	 * @param args
	 *            the command line arguments
	 */
	private static XStream processAnnotations(XStream xstr) {
		xstr.processAnnotations(Chunk_PDF.class);
		xstr.processAnnotations(Word_PDF.class);
		xstr.processAnnotations(Box_PDF.class);
		xstr.processAnnotations(Page_PDF.class);
		xstr.processAnnotations(Document_PDF.class);

		return xstr;
	}

	public Corpus_PDF() {
	}

	public void loadCorpus(String path) {

		File file = new File(path);

		if (file.isDirectory()) {

			File folder = file;
			XStream xstream = new XStream();
			xstream = processAnnotations(xstream);
			List<Document_PDF> myCorpus = new ArrayList<>();
			Document_PDF d;

			// Parcours des fichiers XML du dossier 'folder'
			for (final File fileEntry : folder.listFiles()) {
				String fName = fileEntry.getName();
				int i = fName.lastIndexOf('.');
				if (i > 0 && fName.substring(i + 1).equals("xml")) {
					d = (Document_PDF) xstream.fromXML(fileEntry);
					d.setName(fName);
					d.constructObjects();
					myCorpus.add(d);
				}
			}
			listDoc = myCorpus;
			this.processId();
		} else if (file.isFile()) {

			XStream xstream = new XStream();
			xstream = processAnnotations(xstream);
			List<Document_PDF> myCorpus = new ArrayList<>();
			Document_PDF d;

			String fName = file.getName();
			d = (Document_PDF) xstream.fromXML(file);
			d.setName(fName);
			d.constructObjects();
			myCorpus.add(d);
			
			listDoc = myCorpus;
			this.processId();
		}
	}

	/**
	 * Ajout d'ID aux chunks id : [0,n]
	 */
	public void processId() {
		for (Document_PDF d : listDoc) {

			int chunk_id = 0;

			for (Page_PDF currPage : d.pages) {
				for (Chunk_PDF currChunk : currPage.groupes) {
					currChunk.id = chunk_id;
					chunk_id++;
				}
			}

		}
	}

	/**
	 * Retourne la liste de 'Document'
	 * 
	 * @return
	 */
	public List<Document_PDF> getListdoc() {
		return listDoc;
	}

	/*
	 * Fonction d'affichage : affiche ou retourne une string avec la liste des
	 * docs du corpus
	 */
	public void printDocList() {
		for (Document_PDF d : listDoc) {
			System.out.println(d.getName());
		}
	}

	public String docListToString() {
		String str = "\n";
		for (Document_PDF d : listDoc) {
			str += d.getName() + "\n";
		}
		return str;
	}

}
