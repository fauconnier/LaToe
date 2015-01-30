package org.melodi.tools.tree;

import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.melodi.objectslogic.Chunk_Lara;
import org.melodi.objectslogic.Document_Lara;



public class ShiftReduce_Service {
	
	private static boolean printFlag = false;
	
	public ShiftReduce_Service(){
		
	}
	
	
	public String read_shiftreduce(Document_Lara flatDoc){
		/*
		 * Objets pour l'affichage
		 */
		String toReturn = "";
		int level = -1; // root level
		
		/*
		 * Objets : pile
		 */
		Stack<Chunk_Lara> pile = new Stack<Chunk_Lara>();

		// file
		List<Chunk_Lara> listChunk = flatDoc.getChunk();
		Stack<Chunk_Lara> file = new Stack<Chunk_Lara>();
		file.addAll(listChunk);
		
		// Attention,ici on ajoute une root!
		Chunk_Lara root = new Chunk_Lara(0, 0, 0, 0, "root", 0);
		file.add(0, root);

		int index = 0;
		while ((pile.size() != 0) || (file.size() != 0)) {

			// on défile la file
			Chunk_Lara curr;
			if (file.size() == 0) {
				curr = null;
			} else {
				curr = file.get(0);
			}

			// Si la pile est vide, on met un élément.
			if (pile.size() == 0) {
				pile.push(curr);
				file.remove(0);
			} else {
				// sinon on regarde la relation
				// qu'il peut exister entre le sommet de la pile
				// et la curr.

				if (curr == null) {
					// On dépile le sommet pile et on l'écart
					// System.out.println("Debug On dépile");
					pile.pop();
				} else {
					Chunk_Lara sommet_pile = pile.pop();
					pile.push(sommet_pile);
					int result = select(sommet_pile, curr, 3);

					if (result == 0) {
						level++;
						// subordination
						// System.out.println("Debug"+curr
						// +"_sub("+sommet_pile+")");
						// On empile curr
						// on descend dans la structure du document
						pile.push(curr);

						// On enlève le premier élément de la file
						file.remove(0);

						/**
						 * Récupération du résultat
						 */
						int id = curr.id;
						String type = curr.type;
						String dependance_relation = "sub";
						String dependance_id = sommet_pile.id + "";
						String dependance_type = sommet_pile.type + "";

//						toReturn +=  StringUtils.repeat("\t", level) + type + "_" + id + "_"
//								+ dependance_relation + "(" + dependance_type
//								+ ":" + dependance_id + ")\n";
						
						toReturn +=  StringUtils.repeat("\t", level) + curr.toString() + "\n";

						// listRelation_predict.add(toReturn);

						// PredictLabel
						curr.setPredictTag(toReturn);

						 curr.setDepId(sommet_pile.id);
						 curr.setDepRel("sub");
						 curr.setDepType(sommet_pile.type);
						 

					} else if (result == 1) {
						// coordination
						// System.out.println("Debug"+curr
						// +"_coord("+sommet_pile+")");

						// On dépile sommet pile et on le remplace
						// par curr
						pile.pop();
						pile.push(curr);

						// On enlève le premier élément de la file
						file.remove(0);

						/**
						 * Récupération du résultat
						 */
						int id = curr.id;
						String type = curr.type;
						String dependance_relation = "coord";
						String dependance_id = sommet_pile.id + "";
						String dependance_type = sommet_pile.type + "";

//						toReturn += StringUtils.repeat("\t", level) + type + "_" + id + "_"
//								+ dependance_relation + "(" + dependance_type
//								+ ":" + dependance_id + ")\n";
						
						toReturn +=  StringUtils.repeat("\t", level) + curr.toString() + "\n";

						// listRelation_predict.add(toReturn);
						curr.setPredictTag(toReturn);
						 curr.setDepId(sommet_pile.id);
						 curr.setDepRel("coord");
						 curr.setDepType(sommet_pile.type);
					} else if (result == 2) {

						// On dépile sommet pile et on l'écarte
						pile.pop();
						// hack : on met curr dans la file
						// System.out.println("Debug Hack : ajout de " + curr +
						// " dans la file");
						// On n'enlève le premier élément de la file
						// file.remove(0);
						
						level--;
						
					} else {
						System.out
								.println("Erreur dans le choix de la relation");
						System.out.println("Relation : " + result);
						System.exit(0);
					}
				}
			}
			// index
			index++;
		}

		return toReturn;
	}
	
	public Document_Lara assign_shiftreduce(Document_Lara flatDoc, int method) {

		// boolean method
		// 0. grammaire
		// 1. model train
		// 2. model test

		// Algo de shift-reduce

		// ArrayList<String> listRelation_predict = new ArrayList<String>();
		/*
		 * Objets : pile
		 */
		Stack<Chunk_Lara> pile = new Stack<Chunk_Lara>();

		// file
		List<Chunk_Lara> listChunk = flatDoc.getChunk();
		Stack<Chunk_Lara> file = new Stack<Chunk_Lara>();
		file.addAll(listChunk);
		Chunk_Lara root = new Chunk_Lara(0, 0, 0, 0, "root", 0);
		file.add(0, root);

		// System.out.println();
		// System.out.println("STEP 0");

		int index = 0;
		while ((pile.size() != 0) || (file.size() != 0)) {
			// while(index < 10){

			// on défile la file
			Chunk_Lara curr;
			if (file.size() == 0) {
				curr = null;
				// System.out.println("Debug : File vide");
			} else {
				curr = file.get(0);
				// System.out.println("Debug curr="+curr.toString());
			}

			// Si la pile est vide, on met un élément.
			if (pile.size() == 0) {
				// System.out.println("Debug Pile vide");
				pile.push(curr);
				file.remove(0);
			} else {
				// sinon on regarde la relation
				// qu'il peut exister entre le sommet de la pile
				// et la curr.

				if (curr == null) {
					// On dépile le sommet pile et on l'écart
					// System.out.println("Debug On dépile");
					pile.pop();
				} else {
					Chunk_Lara sommet_pile = pile.pop();
					pile.push(sommet_pile);
					int result = select(sommet_pile, curr, method);

					if (result == 0) {
						// subordination
						// System.out.println("Debug"+curr
						// +"_sub("+sommet_pile+")");

						// On empile curr
						// on descend dans la structure du document
						pile.push(curr);

						// On enlève le premier élément de la file
						file.remove(0);

						/**
						 * Récupération du résultat
						 */

						/**
						 * Récupération du résultat
						 */
						int id = curr.id;
						String type = curr.type;
						String dependance_relation = "sub";
						String dependance_id = sommet_pile.id + "";
						String dependance_type = sommet_pile.type + "";

						String toReturn = type + "_" + id + "_"
								+ dependance_relation + "(" + dependance_type
								+ ":" + dependance_id + ")";

						// listRelation_predict.add(toReturn);

						// PredictLabel
						curr.setPredictTag(toReturn);

						 curr.setDepId(sommet_pile.id);
						 curr.setDepRel("sub");
						 curr.setDepType(sommet_pile.type);

					} else if (result == 1) {
						// coordination
						// System.out.println("Debug"+curr
						// +"_coord("+sommet_pile+")");

						// On dépile sommet pile et on le remplace
						// par curr
						pile.pop();
						pile.push(curr);

						// On enlève le premier élément de la file
						file.remove(0);

						/**
						 * Récupération du résultat
						 */
						int id = curr.id;
						String type = curr.type;
						String dependance_relation = "coord";
						String dependance_id = sommet_pile.id + "";
						String dependance_type = sommet_pile.type + "";

						String toReturn = type + "_" + id + "_"
								+ dependance_relation + "(" + dependance_type
								+ ":" + dependance_id + ")";

						// listRelation_predict.add(toReturn);
						curr.setPredictTag(toReturn);
						 curr.setDepId(sommet_pile.id);
						 curr.setDepRel("coord");
						 curr.setDepType(sommet_pile.type);
					} else if (result == 2) {

						// On dépile sommet pile et on l'écarte
						pile.pop();
						// hack : on met curr dans la file
						// System.out.println("Debug Hack : ajout de " + curr +
						// " dans la file");
						// On n'enlève le premier élément de la file
						// file.remove(0);
					} else {
						System.out
								.println("Erreur dans le choix de la relation");
						System.out.println("Relation : " + result);
						System.exit(0);
					}
				}
			}
			// index
			index++;

			// STEP
			// System.out.println("");
			// System.out.println("STEP_"+index);
			// System.out.println("Pile:"+pile.toString());
			// System.out.println("File:"+file.toString());
		}

		// FlatDocument depFlatDocument = new FlatDocument();

		return flatDoc;
	}
	
	
	public static int select(Chunk_Lara sommet_pile, Chunk_Lara curr, int method) {
		// boolean method
		// 0. grammaire
		// 1. model train
		// 2. model test
		// 3. bonne réponse du corpus

		int result = 999;

		if (method == 0) {
			result = grammaire_(sommet_pile, curr);
		} else if (method == 1) {
			result = (int) (Math.random() * (1 - 0 + 1)) + 0;
		} else if (method == 2) {
//			result = apply_model(sommet_pile, curr);
		} else if (method == 3) {
			result = good_answer(sommet_pile, curr);
		} else if (method == 4) {
			result = only_level_(sommet_pile, curr);
		}
		else {
			System.out.println("Erreur de boolean method dans Select");
			System.exit(0);
		}

		// 0 : subordination
		// 1 : coordination
		// 2 : rien
		
		
		if(printFlag){
			if(result == 0){
				System.out.println("subordination : " + sommet_pile.toString() + " : " + curr.toString());
			}
			else if(result == 1){
				System.out.println("coordination : " + sommet_pile.toString() + " : " + curr.toString());
			}
			else{
				System.out.println("rien : " + sommet_pile.toString() + " : " + curr.toString());
			}
		}
		
		return result;
	}
	
	

	static int good_answer(Chunk_Lara sommet_pile, Chunk_Lara curr) {
		// 0 : subordination
		// 1 : coordination
		// 2 : rien

		// Si curr dépend de sommet_pile
		// il y a une relation
		if (curr.getDepId() == sommet_pile.id) {

			if (curr.getDepRelation().equals("sub")) {
				return 0;
			} else if (curr.getDepRelation().equals("coord")) {
				return 1;
			} else {
				System.out.println("Il y a un problem dans good_answer");
				return 99;
			}

		} else {
			// Sinon il n'y a pas de relation
			return 2;
		}

	}
	
	public static int only_level_(Chunk_Lara sommet_pile, Chunk_Lara curr) {
		
		// 0 : subordination
		// 1 : coordination
		// 2 : rien
		String type_sommet_pile = sommet_pile.type;
		String type_curr = curr.type;
		
		// root
		if (type_sommet_pile.equals("root")) {
			return 0;
		}

		if (type_sommet_pile.equals(type_curr) & sommet_pile.getLevel() == curr.getLevel() ) {
			return grammaire_(sommet_pile, curr); //S'ils sont de même niveau on utilise la grammaire.
		} 
		else if (type_sommet_pile.equals(type_curr) & sommet_pile.getLevel() < curr.getLevel() ) {
			return 0; // Ajout
		}else {
			return 2;
		}
		
	}
	
	public static int grammaire_(Chunk_Lara sommet_pile, Chunk_Lara curr) {

		// 0 : subordination
		// 1 : coordination
		// 2 : rien

		String type_sommet_pile = sommet_pile.type;
		String type_curr = curr.type;
		
		
	
		
		// root
		if (type_sommet_pile.equals("root")) {
			return 0;
		}

		
		/**
		 * Vraie grammaire
		 */

		// p_item en p
		// item_quote en quote

		// h1
		else if (type_sommet_pile.equals("h1") && type_curr.equals("byline")) {
			return 0;
		} else if (type_sommet_pile.equals("h1") && type_curr.equals("h2")) {
			return 0;
		} else if (type_sommet_pile.equals("h1") && type_curr.equals("h3")) {
			return 0;
		} else if (type_sommet_pile.equals("h1") && type_curr.equals("h4")) {
			return 0;
		} else if (type_sommet_pile.equals("h1") && type_curr.equals("p")) {
			return 0;
		} else if (type_sommet_pile.equals("h1") && type_curr.equals("p_item")) {
			return 0;
		} else if (type_sommet_pile.equals("h1") && type_curr.equals("item")) {
			return 0;
		} else if (type_sommet_pile.equals("h1")
				&& type_curr.equals("item_quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h1") && type_curr.equals("quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h1") && type_curr.equals("bibl")) {
			return 0;
		}

		// h2
		else if (type_sommet_pile.equals("h2") && type_curr.equals("byline")) {
			return 0;
		} else if (type_sommet_pile.equals("h2") && type_curr.equals("h3")) {
			return 0;
		} else if (type_sommet_pile.equals("h2") && type_curr.equals("h4")) {
			return 0;
		} else if (type_sommet_pile.equals("h2") && type_curr.equals("p")) {
			return 0;
		} else if (type_sommet_pile.equals("h2") && type_curr.equals("p_item")) {
			return 0;
		} else if (type_sommet_pile.equals("h2") && type_curr.equals("item")) {
			return 0;
		} else if (type_sommet_pile.equals("h2")
				&& type_curr.equals("item_quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h2") && type_curr.equals("quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h2") && type_curr.equals("bibl")) {
			return 0;
		}

		// h3
		else if (type_sommet_pile.equals("h3") && type_curr.equals("byline")) {
			return 0;
		} else if (type_sommet_pile.equals("h3") && type_curr.equals("h4")) {
			return 0;
		} else if (type_sommet_pile.equals("h3") && type_curr.equals("p")) {
			return 0;
		} else if (type_sommet_pile.equals("h3") && type_curr.equals("p_item")) {
			return 0;
		} else if (type_sommet_pile.equals("h3") && type_curr.equals("item")) {
			return 0;
		} else if (type_sommet_pile.equals("h3")
				&& type_curr.equals("item_quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h3") && type_curr.equals("quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h3") && type_curr.equals("bibl")) {
			return 0;
		}

		// h4
		else if (type_sommet_pile.equals("h4") && type_curr.equals("byline")) {
			return 0;
		} else if (type_sommet_pile.equals("h4") && type_curr.equals("p")) {
			return 0;
		} else if (type_sommet_pile.equals("h4") && type_curr.equals("p_item")) {
			return 0;
		} else if (type_sommet_pile.equals("h4") && type_curr.equals("item")) {
			return 0;
		} else if (type_sommet_pile.equals("h4")
				&& type_curr.equals("item_quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h4") && type_curr.equals("quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h4") && type_curr.equals("bibl")) {
			return 0;
		}
		
		// h4
		else if (type_sommet_pile.equals("h5") && type_curr.equals("byline")) {
			return 0;
		} else if (type_sommet_pile.equals("h5") && type_curr.equals("p")) {
			return 0;
		} else if (type_sommet_pile.equals("h5") && type_curr.equals("p_item")) {
			return 0;
		} else if (type_sommet_pile.equals("h5") && type_curr.equals("item")) {
			return 0;
		} else if (type_sommet_pile.equals("h5")
				&& type_curr.equals("item_quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h5") && type_curr.equals("quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h5") && type_curr.equals("bibl")) {
			return 0;
		}
		
		// h4
		else if (type_sommet_pile.equals("h6") && type_curr.equals("byline")) {
			return 0;
		} else if (type_sommet_pile.equals("h6") && type_curr.equals("p")) {
			return 0;
		} else if (type_sommet_pile.equals("h6") && type_curr.equals("p_item")) {
			return 0;
		} else if (type_sommet_pile.equals("h6") && type_curr.equals("item")) {
			return 0;
		} else if (type_sommet_pile.equals("h6")
				&& type_curr.equals("item_quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h6") && type_curr.equals("quote")) {
			return 0;
		} else if (type_sommet_pile.equals("h6") && type_curr.equals("bibl")) {
			return 0;
		}
		
		

		// p
		else if (type_sommet_pile.equals("p") && type_curr.equals("item")) {
			return 0;
		} else if (type_sommet_pile.equals("p") && type_curr.equals("p_item")) {
			return 0;
		} else if (type_sommet_pile.equals("p")
				&& type_curr.equals("item_quote")) {
			return 0;
		} else if (type_sommet_pile.equals("p") && type_curr.equals("quote")) {
			return 0;
		} else if (type_sommet_pile.equals("p") && type_curr.equals("bibl")) {
			return 0;
		}

		// item and quote
		else if (type_sommet_pile.equals("item") && type_curr.equals("quote")) {
			return 0;
		}
		else if (type_sommet_pile.equals("item") && type_curr.contains("h")) {
			return 2;
		}
		else if (type_sommet_pile.equals("p_item")
				&& type_curr.equals("quote")) {
			return 0;
		}

		// item vs item_quote
		// un item subordonne un item_quote. Cela va être très utile pour le
		// corpus LING
		else if (type_sommet_pile.equals("item")
				&& type_curr.equals("item_quote")) {
			return 0;
		}

		// coordination
		// ATTENTION : Ajout du level < accessible avec Wikipedia
		else if (type_sommet_pile.equals(type_curr) & sommet_pile.getLevel() == curr.getLevel() ) {
			return 1;
		} 
		else if (type_sommet_pile.equals(type_curr) & sommet_pile.getLevel() < curr.getLevel() ) {
			return 0; // Ajout
		}else {
			return 2;
		}
	}

}
