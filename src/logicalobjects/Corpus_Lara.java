package logicalobjects;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.util.Iterator;

import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.thoughtworks.xstream.XStream;

/**
*
* @author LaurentS
*/

public class Corpus_Lara  implements java.io.Serializable {
	String trainingDir;
	List<Document_Lara> listDoc;
	static org.jdom.Document document;
	static Element racine;

	public Corpus_Lara() {
		listDoc = new ArrayList<Document_Lara>();
	}
	
	public void add(Document_Lara currDocument){
		listDoc.add(currDocument);
	}
	
	
	public void writeCorpus(String dir){
		
		for(Document_Lara currDocument : this.getDocuments()){
			
			
			
			
		}
	}

	public Corpus_Lara(String path) {
		this.trainingDir = path;
	}

	public void loadCorpus() {
		File folder = new File(trainingDir);
		XStream xstream = new XStream();
		xstream = processAnnotations(xstream);
		List<Document_Lara> myCorpus = new ArrayList<>();
		Document_Lara d;

		// Parcours des fichiers XML du dossier 'folder'
		for (final File fileEntry : folder.listFiles()) {
			String fName = fileEntry.getName();
			int i = fName.lastIndexOf('.');

//			System.out.println(fileEntry.toString());
			Document_Lara myDoc = getXMLDOC(fileEntry.toString());
			
//			System.out.println(myDoc.toString());
			
			for(Chunk_Lara currChunk : myDoc.chunks){
//				System.out.print(currChunk.toString());
//				System.out.println(currChunk.id);
//				System.out.println(currChunk.x1);
//				System.out.println(currChunk.y1);
//				System.out.println(currChunk.x2);
//				System.out.println(currChunk.y2);
			}
			myCorpus.add(myDoc);
//			System.out.println(myDoc);
			

		}
		listDoc = myCorpus;
		// this.processId();

	}
	

	
	

	public Document_Lara getXMLDOC(String file) {

		// On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();

		try {
			// On crée un nouveau document JDOM avec en argument le fichier XML
			// Le parsing est terminé ;)
			document = sxb.build(new File(file));
		} catch (Exception e) {
		}
		racine = document.getRootElement();
		
		ArrayList<Chunk_Lara> result = new ArrayList<Chunk_Lara>();

		// On crée une List contenant tous les noeuds "etudiant" de l'Element
		// racine
		List listChunk_xml = racine.getChildren("Chunk");
		// On crée un Iterator sur notre liste
		Iterator i = listChunk_xml.iterator();
		
		
		int index = 0;
		while (i.hasNext()) {
			// On recrée l'Element courant à chaque tour de boucle afin de
			// pouvoir utiliser les méthodes propres aux Element comme :
			// sélectionner un nœud fils, modifier du texte, etc...
			Element courant = (Element) i.next();
			
			Chunk_Lara currChunk = new Chunk_Lara(0,0,0,0);
			
			
			try {
				int x1 = courant.getAttribute("x1").getIntValue();
				int y1 = courant.getAttribute("y1").getIntValue();
				int x2 = courant.getAttribute("x2").getIntValue();
				int y2 = courant.getAttribute("y2").getIntValue();
				
				currChunk.setX1(x1);
				currChunk.setY1(y1);
				currChunk.setX2(x2);
				currChunk.setY2(y2);
				
				int depId = courant.getAttribute("depId").getIntValue();
				String depType = courant.getAttribute("depType").getValue();
				String depRel = courant.getAttribute("depRel").getValue();
				currChunk.setDepId(depId);
				currChunk.setDepType(depType);
				currChunk.setDepRel(depRel);
				
				String type = courant.getAttribute("type").getValue();
				currChunk.type=type;
				
				int id = courant.getAttribute("id").getIntValue();
				currChunk.id=id;
				

				
				
			} catch (DataConversionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			
			List listWord_xml = courant.getChildren("Word");
			Iterator j = listWord_xml.iterator();
			
			while (j.hasNext()) {
				Element courant_word = (Element) j.next();
				
//				System.out.println(courant_word.getText());
				Word_Lara currWord = new Word_Lara();
				currWord.mot = courant_word.getText();
				
				try {
					int x1 = courant_word.getAttribute("x1").getIntValue();
					int y1 = courant_word.getAttribute("y1").getIntValue();
					int x2 = courant_word.getAttribute("x2").getIntValue();
					int y2 = courant_word.getAttribute("y2").getIntValue();
					
					currWord.x1 = x1;
					currWord.y1 = y1;
					currWord.x2 = x2;
					currWord.y2 = y2;
					
					String font = courant_word.getAttribute("font").getValue();
					currWord.font=font;
					
					String style = courant_word.getAttribute("style").getValue();
					currWord.style = style;
					
					
				} catch (DataConversionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currChunk.addWord(currWord);
				
			}
			
			
			result.add(currChunk);
			// On affiche le nom de l’élément courant
//			System.out.println(courant.getChild("nom").getText());
			

		}
		
		Document_Lara myDoc = new Document_Lara(result);
		myDoc.fName = file;

		return myDoc;
	}

	/**
	 * Ajout d'ID aux chunks id : [0,n]
	 */
	public void processId() {
		// for(FlatDocument d : listDoc){
		//
		// int chunk_id = 0;
		//
		// for(Page currPage : d.pages){
		// for(Chunk currChunk : currPage.groupes){
		// currChunk.id = chunk_id;
		// chunk_id++;
		// }
		// }
		// }
	}

	public List<Document_Lara> getDocuments() {
		return listDoc;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	private static XStream processAnnotations(XStream xstr) {
		xstr.processAnnotations(Chunk_Lara.class);
		xstr.processAnnotations(Word_Lara.class);
		xstr.processAnnotations(Box_Lara.class);
		xstr.processAnnotations(Document_Lara.class);

		return xstr;
	}

	static void afficheALL() {
		// On crée une List contenant tous les noeuds "etudiant" de l'Element
		// racine
		List listEtudiants = racine.getChildren("Chunk");

		// On crée un Iterator sur notre liste
		Iterator i = listEtudiants.iterator();
		while (i.hasNext()) {
			// On recrée l'Element courant à chaque tour de boucle afin de
			// pouvoir utiliser les méthodes propres aux Element comme :
			// sélectionner un nœud fils, modifier du texte, etc...
			Element courant = (Element) i.next();
			// On affiche le nom de l’élément courant
			System.out.println(courant.getChild("nom").getText());
		}
	}

}
