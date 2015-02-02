package org.latoe.textobject;

import java.util.ArrayList;

import logicalobjects.Chunk_Lara;
import logicalobjects.Document_Lara;

import org.melodi.reader.larat.internal.Item;
import org.melodi.reader.larat.internal.Items;
import org.melodi.reader.larat.internal.Primer;
import org.melodi.reader.larat.internal.Unit;


public class ES_Detection {
	
	
	static boolean printFlagESMatched = true;
	
	
	
	
	/**
	 * Extraction des ES avec des patrons simples dans un document Lara.
	 * A faire:
	 * - Attention : recouvrement des conditions des patrons. A vérifier!
	 * - Faire en sorte que les règles puissent être exprimées dans un fichier texte
	 * - Etendre les patrons pour les primers avec plusieurs chunks:
	 * h1: titre :h1
	 * p: paragraphe :p
	 * i: item1 :i
	 * i: item2 :i
	 * @param currDocument
	 * @return
	 */
	public static ArrayList<Unit> getES(Document_Lara currDocument) {
		/**
		 * ES detection sur des documents Wikipedia avec des structures énumératives verticales
		 */
		currDocument.setList_of_units(new ArrayList<Unit>( ));
		currDocument.processLayoutAnnotation(); 
		
		int nb_extracted_se = 0;
		
		/**
		 * Parcours des chunks dans le document
		 */
		for (Chunk_Lara currChunk : currDocument.getChunk()) {
			
			/**
			 * Pattern 1 : le chunk courant est = item,
			 * son subordonné est un paragraph 
			 * son suborodnné n'est pas un item 
			 * son id fait qu'il n'est pas à la fin du document
			 * le chunk suivant n'est pas de type paragraphe
			 */

				if (currChunk.getType().equals("item")
						& currChunk.getDepType().equals("p")
						& !currChunk.getDepType().equals("item")
						& (currChunk.getId() < currDocument
								.getIndexOfLastChunck() - 1 && !currDocument
								.getChunk_id(currChunk.getId() + 1).getType()
								.equals("p"))) {
					
					nb_extracted_se++;
					Unit newUnit = new Unit();
					Primer primer = new Primer();

					/**
					 * Primer
					 */
					Chunk_Lara primerChunk = currDocument.getChunk_id(
							currChunk.getDepId());
					primer.getListChunk().add(primerChunk);
					primer.setText(primerChunk.getText());
					primer.setIndice_begin(primerChunk.getLayoutAnnotation().getBegin());
					primer.setIndice_end(primerChunk.getLayoutAnnotation().getEnd());
					newUnit.setPrimer(primer);
					
					/**
					 * Enumeration
					 * 
					 * Chacun des coordoonés est un item
					 * Chacun des subordonnées au coordonné i appartient à l'item i.
					 */
					ArrayList<Chunk_Lara> coordinates = new ArrayList<Chunk_Lara>();
					coordinates.add(currChunk); // ajout du premier chunk courant (1er item)
					ArrayList<Chunk_Lara> coordinates_other = currDocument.getAllCoordinates(currChunk);
					coordinates.addAll(coordinates_other);
					
					
					// ArrayList<Chunk_Lara> contient tous les items
					Items item_list = new Items();
					int index_item = 0;
					for (Chunk_Lara item_niveau_O : coordinates) {
//						System.out.println("Coordinates "+index_item+" : "+item_niveau_O.getText());
						index_item++;
						
						Item currItem = new Item();
						
						ArrayList<Chunk_Lara> item_and_his_subordinates = new ArrayList<Chunk_Lara>();
						item_and_his_subordinates.add(item_niveau_O); // Item de niveau 0
						
						ArrayList<Chunk_Lara> subordinates_of_items = currDocument.getAllSubordinates(item_niveau_O);
						String text_of_item = "";
						int fin_item = 0;
						text_of_item += item_niveau_O.getText() + "\n";
						
						fin_item = item_niveau_O.getLayoutAnnotation().getEnd();
						
						for(Chunk_Lara subordinates : subordinates_of_items){
							
							for (int i = 0; i < subordinates.getLevel(); i++) {
//								System.out.print("\t");
							}
//							System.out.println("Subordinates :" + subordinates.getText());
							text_of_item += subordinates.getText() + "\n";
							fin_item = subordinates.getLayoutAnnotation().getEnd();
						}
						
						// Création du nouvel item
						item_and_his_subordinates.addAll(subordinates_of_items);
						currItem.setListChunk(item_and_his_subordinates);
						currItem.setText(text_of_item);
						currItem.setIndice_begin(item_niveau_O.getLayoutAnnotation().getBegin());
						currItem.setIndice_end(fin_item);
						currItem.setId(index_item+"");
						item_list.add(currItem);
					}
					newUnit.setItems(item_list);
					
					newUnit.setIndice_begin(newUnit.getPrimer().getIndice_begin());
					int nb_item = newUnit.getItems().size();
					int nb_chunk_in_last_item = newUnit.getItems().get(nb_item-1).getListChunk().size();
					int indice_end_last_chunk = newUnit.getItems().get(nb_item-1).getChunk(nb_chunk_in_last_item-1).getLayoutAnnotation().getEnd();
					newUnit.setIndice_end(indice_end_last_chunk);
					
					currDocument.addUnits(newUnit); // Ajout dans le document
					
					if(printFlagESMatched){
						System.out.println("###### ES Pattern 1 ######");
						System.out.println(newUnit.toString());
					}
				} 
				
				/**
				 * Pattern 2 : le chunk courant est = item,
				 * son dépendant est un TITRE (**) 
				 * son dépendant n'est pas un item 
				 * son id fait qu'il n'est pas à la fin du document
				 * le chunk suivant n'est pas de type paragraphe
				 */
				
				else if (currChunk.getType().equals("item")
						& currChunk.getDepType().contains("h")
						& !currChunk.getDepType().equals("item")
						& (currChunk.getId() < currDocument
								.getIndexOfLastChunck() - 1 
						&& !currDocument.getChunk_id(currChunk.getId() + 1).getType()
								.equals("p"))) {
					
					nb_extracted_se++;
					Unit newUnit = new Unit();
					Primer primer = new Primer();

					/**
					 * Primer
					 */
					Chunk_Lara primerChunk = currDocument.getChunk_id(
							currChunk.getDepId());
					primer.getListChunk().add(primerChunk);
					primer.setText(primerChunk.getText());
					primer.setIndice_begin(primerChunk.getLayoutAnnotation().getBegin());
					primer.setIndice_end(primerChunk.getLayoutAnnotation().getEnd());
					newUnit.setPrimer(primer);
					
					/**
					 * Enumeration
					 * Chacun des coordoonés est un item
					 * Chacun des subordonnées au coordonné i appartient à l'item i.
					 */
					ArrayList<Chunk_Lara> coordinates = new ArrayList<Chunk_Lara>();
					coordinates.add(currChunk); // ajout du premier chunk courant (1er item)
					ArrayList<Chunk_Lara> coordinates_other = currDocument.getAllCoordinates(currChunk);
					coordinates.addAll(coordinates_other);
					
					
					Items item_list = new Items();
					int index_item = 0;
					for (Chunk_Lara item_niveau_O : coordinates) {
						index_item++;
						
						
						Item currItem = new Item();
						
						ArrayList<Chunk_Lara> item_and_his_subordinates = new ArrayList<Chunk_Lara>();
						item_and_his_subordinates.add(item_niveau_O); // Item de niveau 0
						
						ArrayList<Chunk_Lara> subordinates_of_items = currDocument.getAllSubordinates(item_niveau_O);
						String text_of_item = "";
						int fin_item = 0;
						fin_item = item_niveau_O.getLayoutAnnotation().getEnd();
						text_of_item += item_niveau_O.getText() + "\n";
						for(Chunk_Lara subordinates : subordinates_of_items){
							
							text_of_item += subordinates.getText() + "\n";
							fin_item = subordinates.getLayoutAnnotation().getEnd();
						}
						item_and_his_subordinates.addAll(subordinates_of_items);
						currItem.setListChunk(item_and_his_subordinates);
						currItem.setText(text_of_item);
						currItem.setIndice_begin(item_niveau_O.getLayoutAnnotation().getBegin());
						currItem.setIndice_end(fin_item);
						currItem.setId(index_item+"");
						item_list.add(currItem);
					}
					newUnit.setItems(item_list);
					
					newUnit.setIndice_begin(newUnit.getPrimer().getIndice_begin());
					int nb_item = newUnit.getItems().size();
					int nb_chunk_in_last_item = newUnit.getItems().get(nb_item-1).getListChunk().size();
					int indice_end_last_chunk = newUnit.getItems().get(nb_item-1).getChunk(nb_chunk_in_last_item-1).getLayoutAnnotation().getEnd();
					newUnit.setIndice_end(indice_end_last_chunk);
					
					currDocument.addUnits(newUnit); // Ajout dans le document
					
					if(printFlagESMatched){
						System.out.println("###### ES Pattern 2 ######");
						System.out.println(newUnit.toString());
					}
				} 
				
				/**
				 * Pattern 3 : le chunk courant est = item,
				 * son dépendant est un TITRE (**) 
				 * son dépendant n'est pas un item 
				 * son id fait qu'il n'est pas à la fin du document
				 * le chunk suivant n'est pas de type paragraphe
				 */
				
				else if (currChunk.getType().equals("item")
						& currChunk.getDepRelation().equals("sub")
						& (currChunk.getId() < currDocument
								.getIndexOfLastChunck() - 1 
						&& !currDocument.getChunk_id(currChunk.getId() + 1).getType()
								.equals("p"))) {
					
					nb_extracted_se++;
					Unit newUnit = new Unit();
					Primer primer = new Primer();

					/**
					 * Primer
					 */
					Chunk_Lara primerChunk = currDocument.getChunk_id(
							currChunk.getDepId());
					primer.getListChunk().add(primerChunk);
					primer.setText(primerChunk.getText());
					primer.setIndice_begin(primerChunk.getLayoutAnnotation().getBegin());
					primer.setIndice_end(primerChunk.getLayoutAnnotation().getEnd());
					newUnit.setPrimer(primer);
					
					/**
					 * Enumeration
					 * Chacun des coordoonés est un item
					 * Chacun des subordonnées au coordonné i appartient à l'item i.
					 */
					ArrayList<Chunk_Lara> coordinates = new ArrayList<Chunk_Lara>();
					coordinates.add(currChunk); // ajout du premier chunk courant (1er item)
					ArrayList<Chunk_Lara> coordinates_other = currDocument.getAllCoordinates(currChunk);
					coordinates.addAll(coordinates_other);
					
					Items item_list = new Items();
					int index_item = 0;
					for (Chunk_Lara item_niveau_O : coordinates) {
						index_item++;
						
						Item currItem = new Item();
						
						ArrayList<Chunk_Lara> item_and_his_subordinates = new ArrayList<Chunk_Lara>();
						item_and_his_subordinates.add(item_niveau_O); // Item de niveau 0
						
						ArrayList<Chunk_Lara> subordinates_of_items = currDocument.getAllSubordinates(item_niveau_O);
						String text_of_item = "";
						int fin_item = 0;
						text_of_item += item_niveau_O.getText() + "\n";
						fin_item = item_niveau_O.getLayoutAnnotation().getEnd();
						for(Chunk_Lara subordinates : subordinates_of_items){
							
							text_of_item += subordinates.getText() + "\n";
							fin_item = subordinates.getLayoutAnnotation().getEnd();
						}
						item_and_his_subordinates.addAll(subordinates_of_items);
						currItem.setListChunk(item_and_his_subordinates);
						currItem.setText(text_of_item);
						currItem.setIndice_begin(item_niveau_O.getLayoutAnnotation().getBegin());
						currItem.setIndice_end(fin_item);
						currItem.setId(index_item+"");
						item_list.add(currItem);
					}
					newUnit.setItems(item_list);
					
					newUnit.setIndice_begin(newUnit.getPrimer().getIndice_begin());
					int nb_item = newUnit.getItems().size();
					int nb_chunk_in_last_item = newUnit.getItems().get(nb_item-1).getListChunk().size();
					int indice_end_last_chunk = newUnit.getItems().get(nb_item-1).getChunk(nb_chunk_in_last_item-1).getLayoutAnnotation().getEnd();
					newUnit.setIndice_end(indice_end_last_chunk);
					
					currDocument.addUnits(newUnit); // Ajout dans le document
					
					if(printFlagESMatched){
						System.out.println("###### ES Pattern 3 ######");
						System.out.println(newUnit.toString());
					}
				} 
			}
		
		System.out.println("SE extraites : " + nb_extracted_se);
		
		return currDocument.getList_of_units();
	}

}
