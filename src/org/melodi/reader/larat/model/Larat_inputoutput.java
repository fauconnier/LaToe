package org.melodi.reader.larat.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.*;
import org.melodi.analyser.talismane_client.service.Sentence;
import org.melodi.analyser.talismane_client.service.Structure;
import org.melodi.analyser.talismane_client.service.Token;
import org.melodi.reader.larat.internal.Annotation;
import org.melodi.reader.larat.internal.Circonstant;
import org.melodi.reader.larat.internal.Cloture;
import org.melodi.reader.larat.internal.Concept;
import org.melodi.reader.larat.internal.Item;
import org.melodi.reader.larat.internal.Items;
import org.melodi.reader.larat.internal.MarqueurRelation;
import org.melodi.reader.larat.internal.Primer;
import org.melodi.reader.larat.internal.Segment;
import org.melodi.reader.larat.internal.Unit;


public class Larat_inputoutput {

	String path;
	String new_path;
	LinkedList<Unit> chain_SE;
	String annotateur;

	static Date aujourdhui = new Date();
	DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
			DateFormat.SHORT, DateFormat.SHORT);
	public static SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/dd/yyyy HH:mm");

	Element racine;
	org.jdom2.Document document;

	/**
	 * Logger
	 */
	// private static Logger logger = Logger.getLogger(Larat_ParentPanel.class);

	public Larat_inputoutput() {
		racine = new Element("root");
		document = new Document(racine);

		PropertyConfigurator
				.configure("/home/jfaucon/workspace/larat/resources/properties/log4j.properties");
		System.out.println("Demarrage de l'application");
	}

	public Larat_inputoutput(String path, LinkedList<Unit> chain_SE) {
		racine = new Element("root");
		document = new Document(racine);
		this.path = path;
		this.chain_SE = chain_SE;
		this.new_path = getNewPath();

	}

	public void removeThisXML(String path) {
		this.path = path;
		new_path = this.getNewPath();

		if (!new_path.contains("html")) {
			System.out.println("IO_MIG_removeThisXML : suppresion de "
					+ new_path);
			File toDel = new File(new_path);
			if (toDel.exists()) {
				toDel.delete();
			}
		}
	}

	public void readThisXML(String xml_path) {

		SAXBuilder sxb = new SAXBuilder();
		new_path = xml_path;
		// System.out.println(new_path);
		try {
			// On crée un nouveau document JDOM avec en argument le fichier XML
			// Le parsing est terminé ;)
			document = sxb.build(new File(new_path));
		} catch (Exception e) {
		}

		racine = document.getRootElement();

		this.recordSE();

		for (Unit mySE : chain_SE) {

			// System.out.println(mySE.toString());

			getPrimerForThisSE(mySE);
			getPrimerSyntax(mySE);
			getItemsForThisSE(mySE);
			getItemsSyntax(mySE);
			getClotForThisSE(mySE);
			getAxeVisuelForThisSE(mySE);

			getConceptsForThisSE(mySE);
			getCirconstantsForThisSE(mySE);
			getMarqRelsForThisSE(mySE);

			getAxeRhetoForThisSE(mySE);
			getAxeIntentForThisSE(mySE);
			getAxeSemanForThisSE(mySE);
		}

	}

	public void readThis(String path) {

		// On crée une instance de SAXBuilder
		this.path = path;
		SAXBuilder sxb = new SAXBuilder();
		new_path = this.getNewPath();
		// System.out.println(new_path);
		try {
			// On crée un nouveau document JDOM avec en argument le fichier XML
			// Le parsing est terminé ;)
			document = sxb.build(new File(new_path));
		} catch (Exception e) {
		}

		racine = document.getRootElement();

		this.recordSE();

		for (Unit mySE : chain_SE) {
			// System.out.println(mySE.toString());

			getPrimerForThisSE(mySE);
			getPrimerSyntax(mySE);
			getItemsForThisSE(mySE);
			getItemsSyntax(mySE);
			getClotForThisSE(mySE);
			getAxeVisuelForThisSE(mySE);

			getConceptsForThisSE(mySE);
			getCirconstantsForThisSE(mySE);
			getMarqRelsForThisSE(mySE);

			getAxeRhetoForThisSE(mySE);
			getAxeIntentForThisSE(mySE);
			getAxeSemanForThisSE(mySE);
		}

	}

	public void getConceptsPrimer(Unit currentSE) {
		List listAnnotation = racine.getChildren("annotation");

		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("concept")
						&& courant.getChild("SEval")
								.getAttributeValue("localisation")
								.equals("primer")
						&& courant.getChild("SEval").getAttributeValue("idSE")
								.equals("" + currentSE.getId())) {

					// Concept lié au Primer
					Concept concept = new Concept();

					// Récupération des SPANS
					List itemList = courant.getChild("SEval").getChildren(
							"span");
					Iterator v = itemList.iterator();
					while (v.hasNext()) {
						Element span = (Element) v.next();

						Segment segment = new Segment();
						// System.out.println(span.getAttributeValue("id"));

						segment.setId(span.getAttributeValue("id"));
						segment.setIndice_begin(Integer.parseInt(span
								.getAttributeValue("begin")));
						segment.setIndice_end(Integer.parseInt(span
								.getAttributeValue("end")));

						concept.add(segment);

					}
					// Récupération des textes
					for (Segment segment : concept) {

						List itemList2 = courant.getChild("SEval").getChildren(
								"text");
						Iterator w = itemList2.iterator();
						while (w.hasNext()) {
							Element text = (Element) w.next();
							// System.out.println("b"+text.getAttributeValue("id"));

							if (text.getAttributeValue("id").equals(
									"" + segment.getId())) {
								// System.out.println(text.getText());
								segment.setText(text.getText());
							}
						}
					}
					currentSE.getPrimer().setConcept(concept);
				}
			}
		}
	}

	public void getConceptItems(Item item, Unit currentSE) {

		List listAnnotation = racine.getChildren("annotation");

		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("concept")
						&& courant.getChild("SEval")
								.getAttributeValue("localisation")
								.equals("item")
						&& courant.getChild("SEval").getAttributeValue("idSE")
								.equals("" + currentSE.getId())
						&& courant.getChild("SEval")
								.getAttributeValue("idItem")
								.equals("" + item.getId())) {

					// Concept lié à l'item courant
					Concept concept = new Concept();

					// Récupération des SPANS
					List itemList = courant.getChild("SEval").getChildren(
							"span");
					Iterator v = itemList.iterator();
					while (v.hasNext()) {
						Element span = (Element) v.next();

						Segment segment = new Segment();
						// System.out.println(span.getAttributeValue("id"));

						segment.setId(span.getAttributeValue("id"));
						segment.setIndice_begin(Integer.parseInt(span
								.getAttributeValue("begin")));
						segment.setIndice_end(Integer.parseInt(span
								.getAttributeValue("end")));

						concept.add(segment);

					}
					// Récupération des textes
					for (Segment segment : concept) {

						List itemList2 = courant.getChild("SEval").getChildren(
								"text");
						Iterator w = itemList2.iterator();
						while (w.hasNext()) {
							Element text = (Element) w.next();
							// System.out.println("b"+text.getAttributeValue("id"));

							if (text.getAttributeValue("id").equals(
									"" + segment.getId())) {
								// System.out.println(text.getText());
								segment.setText(text.getText());
							}
						}
					}
					currentSE.getItem(Integer.parseInt(item.getId()))
							.setConcept(concept);
				}
			}
		}

	}

	public void getConceptsForThisSE(Unit currentSE) {

		// Primer
		getConceptsPrimer(currentSE);

		// Item
		for (Item item : currentSE.getItems()) {
			getConceptItems(item, currentSE);
		}

	}

	public void getCirconstantPrimer(Unit currentSE) {
		List listAnnotation = racine.getChildren("annotation");

		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("circonstant")
						&& courant.getChild("SEval")
								.getAttributeValue("localisation")
								.equals("primer")
						&& courant.getChild("SEval").getAttributeValue("idSE")
								.equals("" + currentSE.getId())) {

					// Concept lié au Primer
					Circonstant circonstant = new Circonstant();

					// Récupération des SPANS
					List itemList = courant.getChild("SEval").getChildren(
							"span");
					Iterator v = itemList.iterator();
					while (v.hasNext()) {
						Element span = (Element) v.next();

						Segment segment = new Segment();
						// System.out.println(span.getAttributeValue("id"));

						segment.setId(span.getAttributeValue("id"));
						segment.setIndice_begin(Integer.parseInt(span
								.getAttributeValue("begin")));
						segment.setIndice_end(Integer.parseInt(span
								.getAttributeValue("end")));

						circonstant.add(segment);

					}
					// Récupération des textes
					for (Segment segment : circonstant) {

						List itemList2 = courant.getChild("SEval").getChildren(
								"text");
						Iterator w = itemList2.iterator();
						while (w.hasNext()) {
							Element text = (Element) w.next();
							// System.out.println("b"+text.getAttributeValue("id"));

							if (text.getAttributeValue("id").equals(
									"" + segment.getId())) {
								// System.out.println(text.getText());
								segment.setText(text.getText());
							}
						}
					}
					currentSE.getPrimer().setCirconstant(circonstant);
				}
			}
		}
	}

	public void getCirconstantItems(Item item, Unit currentSE) {

		List listAnnotation = racine.getChildren("annotation");

		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("circonstant")
						&& courant.getChild("SEval")
								.getAttributeValue("localisation")
								.equals("item")
						&& courant.getChild("SEval").getAttributeValue("idSE")
								.equals("" + currentSE.getId())
						&& courant.getChild("SEval")
								.getAttributeValue("idItem")
								.equals("" + item.getId())) {

					// Concept lié à l'item courant
					Circonstant circonstant = new Circonstant();

					// Récupération des SPANS
					List itemList = courant.getChild("SEval").getChildren(
							"span");
					Iterator v = itemList.iterator();
					while (v.hasNext()) {
						Element span = (Element) v.next();

						Segment segment = new Segment();
						// System.out.println(span.getAttributeValue("id"));

						segment.setId(span.getAttributeValue("id"));
						segment.setIndice_begin(Integer.parseInt(span
								.getAttributeValue("begin")));
						segment.setIndice_end(Integer.parseInt(span
								.getAttributeValue("end")));

						circonstant.add(segment);

					}
					// Récupération des textes
					for (Segment segment : circonstant) {

						List itemList2 = courant.getChild("SEval").getChildren(
								"text");
						Iterator w = itemList2.iterator();
						while (w.hasNext()) {
							Element text = (Element) w.next();
							// System.out.println("b"+text.getAttributeValue("id"));

							if (text.getAttributeValue("id").equals(
									"" + segment.getId())) {
								// System.out.println(text.getText());
								segment.setText(text.getText());
							}
						}
					}
					currentSE.getItem(Integer.parseInt(item.getId()))
							.setCirconstant(circonstant);
				}
			}
		}

	}

	public void getCirconstantsForThisSE(Unit currentSE) {
		// Primer
		getCirconstantPrimer(currentSE);

		// Item
		for (Item item : currentSE.getItems()) {
			getCirconstantItems(item, currentSE);
		}
	}

	public void getMarqRelsPrimer(Unit currentSE) {

		List listAnnotation = racine.getChildren("annotation");

		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("marq_relation")
						&& courant.getChild("SEval")
								.getAttributeValue("localisation")
								.equals("primer")
						&& courant.getChild("SEval").getAttributeValue("idSE")
								.equals("" + currentSE.getId())) {

					// Concept lié au Primer
					MarqueurRelation marqRel = new MarqueurRelation();

					// Récupération des SPANS
					List itemList = courant.getChild("SEval").getChildren(
							"span");
					Iterator v = itemList.iterator();
					while (v.hasNext()) {
						Element span = (Element) v.next();

						Segment segment = new Segment();
						// System.out.println(span.getAttributeValue("id"));

						segment.setId(span.getAttributeValue("id"));
						segment.setIndice_begin(Integer.parseInt(span
								.getAttributeValue("begin")));
						segment.setIndice_end(Integer.parseInt(span
								.getAttributeValue("end")));

						marqRel.add(segment);

					}
					// Récupération des textes
					for (Segment segment : marqRel) {

						List itemList2 = courant.getChild("SEval").getChildren(
								"text");
						Iterator w = itemList2.iterator();
						while (w.hasNext()) {
							Element text = (Element) w.next();
							// System.out.println("b"+text.getAttributeValue("id"));

							if (text.getAttributeValue("id").equals(
									"" + segment.getId())) {
								// System.out.println(text.getText());
								segment.setText(text.getText());
							}
						}
					}
					currentSE.getPrimer().setMarqueurRel(marqRel);
				}
			}
		}

	}

	public void getMarqRelsItem(Item item, Unit currentSE) {

		List listAnnotation = racine.getChildren("annotation");

		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("marq_relation")
						&& courant.getChild("SEval")
								.getAttributeValue("localisation")
								.equals("item")
						&& courant.getChild("SEval").getAttributeValue("idSE")
								.equals("" + currentSE.getId())
						&& courant.getChild("SEval")
								.getAttributeValue("idItem")
								.equals("" + item.getId())) {

					// Concept lié à l'item courant
					MarqueurRelation marqRel = new MarqueurRelation();

					// Récupération des SPANS
					List itemList = courant.getChild("SEval").getChildren(
							"span");
					Iterator v = itemList.iterator();
					while (v.hasNext()) {
						Element span = (Element) v.next();

						Segment segment = new Segment();
						// System.out.println(span.getAttributeValue("id"));

						segment.setId(span.getAttributeValue("id"));
						segment.setIndice_begin(Integer.parseInt(span
								.getAttributeValue("begin")));
						segment.setIndice_end(Integer.parseInt(span
								.getAttributeValue("end")));

						marqRel.add(segment);

					}
					// Récupération des textes
					for (Segment segment : marqRel) {

						List itemList2 = courant.getChild("SEval").getChildren(
								"text");
						Iterator w = itemList2.iterator();
						while (w.hasNext()) {
							Element text = (Element) w.next();
							// System.out.println("b"+text.getAttributeValue("id"));

							if (text.getAttributeValue("id").equals(
									"" + segment.getId())) {
								// System.out.println(text.getText());
								segment.setText(text.getText());
							}
						}
					}
					currentSE.getItem(Integer.parseInt(item.getId()))
							.setMarqueurRel(marqRel);
				}
			}
		}

	}

	public void getMarqRelsForThisSE(Unit currentSE) {
		// Primer
		getMarqRelsPrimer(currentSE);

		// Item
		for (Item item : currentSE.getItems()) {
			getMarqRelsItem(item, currentSE);
		}
	}

	public void getAxeRhetoForThisSE(Unit currentSE) {
		List listAnnotation = racine.getChildren("annotation");
		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("axe_rhetorique")
						&& courant.getChild("SEval").getAttributeValue("idSE")
								.equals("" + currentSE.getId())) {

					if (courant.getChild("SEval").getChild("paradigmatique")
							.getAttributeValue("value").equals("1")) {
						currentSE.setAxe_rhetorique("paradigmatique");

					} else if (courant.getChild("SEval")
							.getChild("syntagmatique")
							.getAttributeValue("value").equals("1")) {
						currentSE.setAxe_rhetorique("syntagmatique");

					} else if (courant.getChild("SEval").getChild("hybride")
							.getAttributeValue("value").equals("1")) {
						currentSE.setAxe_rhetorique("hybride");

					} else if (courant.getChild("SEval").getChild("bivalente")
							.getAttributeValue("value").equals("1")) {
						currentSE.setAxe_rhetorique("bivalente");
					}
				}
			}
		}
	}

	public void getAxeIntentForThisSE(Unit currentSE) {

		List listAnnotation = racine.getChildren("annotation");
		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("axe_intentionnel")
						&& courant.getChild("SEval").getAttributeValue("idSE")
								.equals("" + currentSE.getId())) {

					if (courant.getChild("SEval").getChild("descriptive")
							.getAttributeValue("value").equals("1")) {
						currentSE.addAxe_intentionnel("descriptive");
					}
					if (courant.getChild("SEval").getChild("narrative")
							.getAttributeValue("value").equals("1")) {
						currentSE.addAxe_intentionnel("narrative");
					}
					if (courant.getChild("SEval").getChild("explicative")
							.getAttributeValue("value").equals("1")) {
						currentSE.addAxe_intentionnel("explicative");
					}
					if (courant.getChild("SEval").getChild("prescriptive")
							.getAttributeValue("value").equals("1")) {
						currentSE.addAxe_intentionnel("prescriptive");
					}
					if (courant.getChild("SEval").getChild("procedurale")
							.getAttributeValue("value").equals("1")) {
						currentSE.addAxe_intentionnel("procedurale");
					}
					if (courant.getChild("SEval").getChild("argumentative")
							.getAttributeValue("value").equals("1")) {
						currentSE.addAxe_intentionnel("argumentative");
					}
					if (courant.getChild("SEval")
							.getChild("intentionnel_autre")
							.getAttributeValue("value").equals("1")) {
						currentSE.addAxe_intentionnel("autre_intentionnel");
					}
				}
			}
		}

	}

	public void getAxeSemanForThisSE(Unit currentSE) {
		List listAnnotation = racine.getChildren("annotation");
		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("axe_semantique")
						&& courant.getChild("SEval").getAttributeValue("idSE")
								.equals("" + currentSE.getId())) {

					// 3 feuilles et plusieurs children
					if (courant.getChild("SEval").getChild("visee_ontologique")
							.getAttributeValue("value").equals("1")) {

						if (courant.getChild("SEval")
								.getChild("visee_ontologique").getChild("isA")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_semantique("isA");
						} else if (courant.getChild("SEval")
								.getChild("visee_ontologique")
								.getChild("partOf").getAttributeValue("value")
								.equals("1")) {
							currentSE.setAxe_semantique("partOf");
						} else if (courant.getChild("SEval")
								.getChild("visee_ontologique")
								.getChild("instanceOf")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_semantique("instanceOf");
						} else if (courant.getChild("SEval")
								.getChild("visee_ontologique")
								.getChild("ontologique_autre")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_semantique("autre_ontologique");
						}

					} else if (courant.getChild("SEval")
							.getChild("metalinguistique")
							.getAttributeValue("value").equals("1")) {

						if (courant.getChild("SEval")
								.getChild("metalinguistique")
								.getChild("hyperonymie")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_semantique("hyperonymie");
						} else if (courant.getChild("SEval")
								.getChild("metalinguistique")
								.getChild("meronymie")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_semantique("meronymie");
						} else if (courant.getChild("SEval")
								.getChild("metalinguistique")
								.getChild("homonymie")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_semantique("homonymie");
						} else if (courant.getChild("SEval")
								.getChild("metalinguistique")
								.getChild("synonymie")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_semantique("synonymie");
						} else if (courant.getChild("SEval")
								.getChild("metalinguistique")
								.getChild("multilingue")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_semantique("multilingue");
						} else if (courant.getChild("SEval")
								.getChild("metalinguistique")
								.getChild("lexical_autre")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_semantique("autre_lexical");
						}

					} else if (courant.getChild("SEval")
							.getChild("semantique_autre")
							.getAttributeValue("value").equals("1")) {
						currentSE.setAxe_semantique("autre_semantique");
					}

					if (courant.getChild("SEval").getChild("contextuelle")
							.getAttributeValue("value").equals("1")) {
						currentSE.setAxe_semantiqueCircon("contextuelle");
					} else {
						currentSE.setAxe_semantiqueCircon("non_contextuelle");
					}

				}
			}
		}
	}

	public void getAxeVisuelForThisSE(Unit currentSE) {

		List listAnnotation = racine.getChildren("annotation");
		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("axe_visuel")) {

					if (courant.getChild("SEval").getAttributeValue("idSE")
							.equals("" + currentSE.getId())) {

						if (courant.getChild("SEval").getChild("horizontale")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_visuel("Horizontale");
						} else if (courant.getChild("SEval")
								.getChild("verticale")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_visuel("Verticale");
						} else {
							currentSE.setAxe_visuel("Nothing");
						}

						if (courant.getChild("SEval")
								.getChild("navigationnelle")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_visuel_nav_hyp("Navigationnelle");
						} else if (courant.getChild("SEval")
								.getChild("hypertextuelle")
								.getAttributeValue("value").equals("1")) {
							currentSE.setAxe_visuel_nav_hyp("Hypertextuelle");
						} else {
							currentSE.setAxe_visuel_nav_hyp("Nothing");
						}

					}

				}
			}
		}
	}

	public void getClotForThisSE(Unit currentSE) {

		List listAnnotation = racine.getChildren("annotation");

		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("cloture")) {

					if (courant.getChild("SEval").getAttributeValue("idSE")
							.equals("" + currentSE.getId())) {

						Cloture newPrimer = new Cloture();

						newPrimer.setText(courant.getChild("SEval")
								.getChild("text").getText());

						newPrimer.setIndice_begin(Integer.parseInt(courant
								.getChild("SEval").getChild("span")
								.getAttributeValue("begin")));

						newPrimer.setIndice_end(Integer.parseInt(courant
								.getChild("SEval").getChild("span")
								.getAttributeValue("end")));

						currentSE.setClot(newPrimer);
					}

				}
			}
		}

	}
	
	public void getPrimerSyntax(Unit currentSE){
		
		List listAnnotation = racine.getChildren("annotation");

		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			
			
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {
				
				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("primer_syntax")) {

				
					if (courant.getChild("SEval").getAttributeValue("idSE")
							.equals("" + currentSE.getId())) {
						
						
						Structure myStruc = new Structure();
						
						Element seVal = courant.getChild("SEval");
						
						// Parcours des Sentence
						List listSentence = seVal.getChildren("sentence");
						Iterator it_sentence = listSentence.iterator();
						while(it_sentence.hasNext()){
							
							Sentence curr_sentence = new Sentence();
							
							Element curr_sentence_xml = (Element) it_sentence.next();
							
							int id_sentence = Integer.parseInt(curr_sentence_xml.getAttributeValue("id"));
							curr_sentence.setId_sentence(id_sentence);
							
							// Parcours des mots
							List listWord = curr_sentence_xml.getChildren("word");
							Iterator it_word = listWord.iterator();
							
							while(it_word.hasNext()){
								Element word = (Element) it_word.next();
								
								Token currToken = new Token();
								
								String lem = word.getAttributeValue("lem");
								currToken.setLemma(lem);
								
								String postag = word.getAttributeValue("postag");
								currToken.setCppostag(postag);
								
								String feats = word.getAttributeValue("morpho");
								currToken.setFeats(feats);
								
								int head = Integer.parseInt(word.getAttributeValue("head"));
								currToken.setHead(head);
								
								String pdep = word.getAttributeValue("dep");
								currToken.setDeprel(pdep);
								
								int id = Integer.parseInt(word.getAttributeValue("id"));
								currToken.setId_token(id);
								
								String form = word.getText();
								currToken.setForm(form);
								
								
								curr_sentence.add(currToken);
							}
							
							
							
							myStruc.add(curr_sentence);
						}
						
						currentSE.getPrimer().setStructure(myStruc);
					}
					
				}
			}
		}
	}

	public void getPrimerForThisSE(Unit currentSE) {

		List listAnnotation = racine.getChildren("annotation");

		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("primer")) {

					if (courant.getChild("SEval").getAttributeValue("idSE")
							.equals("" + currentSE.getId())) {

						Primer newPrimer = new Primer();

						newPrimer.setText(courant.getChild("SEval")
								.getChild("text").getText());

						newPrimer.setIndice_begin(Integer.parseInt(courant
								.getChild("SEval").getChild("span")
								.getAttributeValue("begin")));

						newPrimer.setIndice_end(Integer.parseInt(courant
								.getChild("SEval").getChild("span")
								.getAttributeValue("end")));

						currentSE.setPrimer(newPrimer);
					}

				}
			}
		}

	}
	
	public void getItemsSyntax(Unit currentSE){
		List listAnnotation = racine.getChildren("annotation");

		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("items_syntax")) {

					if (courant.getChild("SEval").getAttributeValue("idSE")
							.equals("" + currentSE.getId())) {

						Element seval = courant.getChild("SEval");
						
						// Récupération des items de la SE
						Items listItm = currentSE.getItems();
						
						// Récupération des items dans le XML
						List itemList = courant.getChild("SEval").getChildren(
								"item");
						Iterator item_iterator = itemList.iterator();
						
						while (item_iterator.hasNext()) {
							Structure myStructure = new Structure();
							Element item_xml = (Element) item_iterator.next();
							
							int id_item = Integer.parseInt(item_xml.getAttributeValue("id"));
							
							// Parcours des Sentence
							List listSentence = item_xml.getChildren("sentence");
							Iterator it_sentence = listSentence.iterator();
							
							
							
							while(it_sentence.hasNext()){
								
								Sentence curr_sentence = new Sentence();
								
								Element curr_sentence_xml = (Element) it_sentence.next();
								
								int id_sentence = Integer.parseInt(curr_sentence_xml.getAttributeValue("id"));
								curr_sentence.setId_sentence(id_sentence);
								
								// Parcours des mots
								List listWord = curr_sentence_xml.getChildren("word");
								Iterator it_word = listWord.iterator();
								
								while(it_word.hasNext()){
									Element word = (Element) it_word.next();
									
									Token currToken = new Token();
									
									String lem = word.getAttributeValue("lem");
									currToken.setLemma(lem);
									
									String postag = word.getAttributeValue("postag");
									currToken.setCppostag(postag);
									
									String feats = word.getAttributeValue("morpho");
									currToken.setFeats(feats);
									
									int head = Integer.parseInt(word.getAttributeValue("head"));
									currToken.setHead(head);
									
									String pdep = word.getAttributeValue("dep");
									currToken.setDeprel(pdep);
									
									int id = Integer.parseInt(word.getAttributeValue("id"));
									currToken.setId_token(id);
									
									String form = word.getText();
									currToken.setForm(form);
									
									
									curr_sentence.add(currToken);
								}
								myStructure.add(curr_sentence);
							}
							// Ajout de la structure à l'Item
							listItm.get(id_item).setStructure(myStructure);
							
						}
					}

				}
			}
		}
		
		
	}

	public void getItemsForThisSE(Unit currentSE) {
		List listAnnotation = racine.getChildren("annotation");

		Iterator i = listAnnotation.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("2")) {

				if (courant.getChild("SEval").getAttributeValue("type")
						.equals("items")) {

					if (courant.getChild("SEval").getAttributeValue("idSE")
							.equals("" + currentSE.getId())) {

						Items newListItems = new Items();

						// Récupération des SPANS
						List itemList = courant.getChild("SEval").getChildren(
								"span");
						Iterator v = itemList.iterator();
						while (v.hasNext()) {
							Element span = (Element) v.next();

							Item newItem = new Item();
							// System.out.println(span.getAttributeValue("id"));

							newItem.setId(span.getAttributeValue("id"));
							newItem.setIndice_begin(Integer.parseInt(span
									.getAttributeValue("begin")));
							newItem.setIndice_end(Integer.parseInt(span
									.getAttributeValue("end")));

							newListItems.add(newItem);

						}
						// Récupération des textes
						for (Item it : newListItems) {

							it.getId();

							List itemList2 = courant.getChild("SEval")
									.getChildren("text");
							Iterator w = itemList2.iterator();
							while (w.hasNext()) {
								Element text = (Element) w.next();
								// System.out.println("b"+text.getAttributeValue("id"));

								if (text.getAttributeValue("id").equals(
										"" + it.getId())) {
									// System.out.println(text.getText());
									it.setText(text.getText());
								}

							}

						}

						currentSE.setItems(newListItems);

					}

				}
			}
		}
	}

	public void recordSE() {
		List listEtudiants = racine.getChildren("annotation");

		chain_SE = new LinkedList<Unit>();

		// Méthode : Pour chaque SE, on essaye de reconstruire ses informations

		// On crée un Iterator sur notre liste
		Iterator i = listEtudiants.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();

			// Si c'est une annotation
			if (courant.getAttributeValue("type").equals("1")) {
				Unit newSE = new Unit();
				Annotation newAnnotation = new Annotation();

				/**
				 * METADATA
				 */
				// id in Metadata
				newAnnotation.setId(Integer.parseInt(courant
						.getChild("metadata").getChild("id").getText()));

				// Auteur
				newAnnotation.setAuthor(courant.getChild("metadata")
						.getChild("auteur").getText());

				// Date
				try {
					newAnnotation.setDate(stringToDate(courant
							.getChild("metadata").getChild("date").getText()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Document
				newAnnotation.setDocumentName(courant.getChild("metadata")
						.getChild("document").getText());

				// Comment
				newAnnotation.setComment(courant.getChild("comment").getText());

				/**
				 * SE
				 */
				newSE.setId(Integer.parseInt(courant.getChild("SE")
						.getChild("id").getText()));

				newSE.setText(courant.getChild("SE").getChild("text").getText());

				newSE.setIndice_begin(Integer.parseInt(courant.getChild("SE")
						.getChild("span").getAttributeValue("begin")));

				newSE.setIndice_end(Integer.parseInt(courant.getChild("SE")
						.getChild("span").getAttributeValue("end")));

				// put together
				newSE.setAnnotation(newAnnotation);
				chain_SE.add(newSE);
			}
		}
	}

	public static Date stringToDate(String sDate) throws ParseException {
		return formatter.parse(sDate);
	}

	public void afficheALL() {
		// On crée une List contenant tous les noeuds "etudiant" de l'Element
		// racine
		List listEtudiants = racine.getChildren("annotation");

		// On crée un Iterator sur notre liste
		Iterator i = listEtudiants.iterator();
		while (i.hasNext()) {
			// On recrée l'Element courant à chaque tour de boucle afin de
			// pouvoir utiliser les méthodes propres aux Element comme :
			// sélectionner un nœud fils, modifier du texte, etc...
			Element courant = (Element) i.next();
			// On affiche le nom de l’élément courant
			// System.out.println(courant.getChild("metadata").getChild("id")
			// .getText());
		}
	}

	public String getAnnotateur() {
		return annotateur;
	}

	public void setAnnotateur(String annotateur) {
		this.annotateur = annotateur;
	}

	public void setPath(String path) {
		this.path = path;
		this.new_path = getNewPath();
	}

	public String getPath() {
		return this.path;
	}

	public void setChain(LinkedList<Unit> chain_SE) {
		this.chain_SE = chain_SE;
	}

	public LinkedList<Unit> getChain() {
		return this.chain_SE;
	}

	public boolean writeThis() {

		// Reorganise
		organizeItems();
		organizeSE();

		// Annotation type 1
		int num_annotation = 0;
		for (Unit currentSE : chain_SE) {

			// System.out.println(currentSE.toString());

			// For retrieve ID
			if (currentSE.getAnnotation() != null) {
				currentSE.setId(num_annotation);

				Element annotation = new Element("annotation");
				Attribute classe = new Attribute("type", "1");
				// Attribute id_annot = new Attribute("id",""+num_annotation);
				annotation.setAttribute(classe);
				// annotation.setAttribute(id_annot);

				Element meta = metaData(currentSE, num_annotation);
				Element SE = annotationSE(currentSE, num_annotation);
				Element comment = comment(currentSE);

				racine.addContent(annotation);
				annotation.addContent(meta);
				annotation.addContent(SE);
				annotation.addContent(comment);

				num_annotation++;
			}
		}

		/**
		 * Annotation de Type 2
		 */

		// Type 2 - Primer/Item/Cloture analyse lexicale
		for (Unit currentSE : chain_SE) {

			if (currentSE.getAnnotation() != null) {

				// Primer
				if (currentSE.getPrimer() != null) {
					racine.addContent(writeAnnotSyntaxPrimer(currentSE));
					// writeAnnotSyntaxPrimer(currentSE);
				}

				// Items
				if (currentSE.getItems().size() > 0) {
					racine.addContent(writeAnnotSyntaxItem(currentSE));
				}
				
				// Cloture
				if (currentSE.getClot() != null) {
					racine.addContent(writeAnnotCloture(currentSE));
				}
			}

		}

		// Type 2 - Primer/Item/Cloture
		for (Unit currentSE : chain_SE) {

			// Primer - Item - Cloture
			if (currentSE.getAnnotation() != null) {
				// Primer
				if (currentSE.getPrimer() != null) {
					racine.addContent(writeAnnotPrimer(currentSE));
				}

				// Items
				if (currentSE.getItems().size() > 0) {

					racine.addContent(writeAnnotItem(currentSE.getItems(),
							currentSE));
				}
				// Cloture
				if (currentSE.getClot() != null) {
					racine.addContent(writeAnnotCloture(currentSE));
				}

			}

		}

		// Type 2 - Concept/Circonstant/MarqRel
		for (Unit currentSE : chain_SE) {

			// Concepts
			if (currentSE.getAnnotation() != null) {
				writeConcepts(currentSE);
			}

			if (currentSE.getAnnotation() != null) {
				writeCirconstants(currentSE);
			}

			if (currentSE.getAnnotation() != null) {
				writeMarqRel(currentSE);
			}
		}

		// Type 2 - Axe en tout genre
		for (Unit currentSE : chain_SE) {

			// Axe visuel
			if (currentSE.getAnnotation() != null) {
				racine.addContent(writeAxeVisuel(currentSE));
			}

			// Axe rhétorique
			if (currentSE.getAnnotation() != null) {
				racine.addContent(writeAxeRhetorique(currentSE));
			}

			// Axe intentionnel
			if (currentSE.getAnnotation() != null) {
				racine.addContent(writeAxeIntentionnel(currentSE));
			}

			// Axe Sémantique
			if (currentSE.getAnnotation() != null) {
				racine.addContent(writeAxeSemantique(currentSE));
			}

		}

		// affiche();
		enregistre(new_path);
		return true;
	}

	public Element writeAxeVisuel(Unit currentSE) {

		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "axe_visuel");
		SEval.setAttribute(type);
		Attribute idSE = new Attribute("idSE", "" + num_annotation);
		SEval.setAttribute(idSE);

		// Horizontale
		Element horizontal = new Element("horizontale");
		if (currentSE.getAxe_visuel().equals("Horizontale")) {
			Attribute positif = new Attribute("value", "1");
			horizontal.setAttribute(positif);
		} else {
			Attribute positif = new Attribute("value", "0");
			horizontal.setAttribute(positif);
		}

		// Verticale
		Element vertical = new Element("verticale");
		if (currentSE.getAxe_visuel().equals("Verticale")) {
			Attribute positif = new Attribute("value", "1");
			vertical.setAttribute(positif);
		} else {
			Attribute positif = new Attribute("value", "0");
			vertical.setAttribute(positif);
		}

		Element navigationnelle = new Element("navigationnelle");
		if (currentSE.getAxe_visuel_nav_hyp().equals("Navigationnelle")) {
			Attribute positif = new Attribute("value", "1");
			navigationnelle.setAttribute(positif);
		} else {
			Attribute positif = new Attribute("value", "0");
			navigationnelle.setAttribute(positif);
		}

		Element hypertextuelle = new Element("hypertextuelle");
		if (currentSE.getAxe_visuel_nav_hyp().equals("Hypertextuelle")) {
			Attribute positif = new Attribute("value", "1");
			hypertextuelle.setAttribute(positif);
		} else {
			Attribute positif = new Attribute("value", "0");
			hypertextuelle.setAttribute(positif);
		}

		SEval.addContent(horizontal);
		SEval.addContent(vertical);
		SEval.addContent(navigationnelle);
		SEval.addContent(hypertextuelle);

		annotation.addContent(SEval);

		return annotation;
	}

	public Element writeAxeRhetorique(Unit currentSE) {
		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "axe_rhetorique");
		SEval.setAttribute(type);
		Attribute idSE = new Attribute("idSE", "" + num_annotation);
		SEval.setAttribute(idSE);

		// Paradigmatique
		Element paradigmatique = new Element("paradigmatique");
		if (currentSE.getAxe_rhetorique().equals("paradigmatique")) {
			Attribute positif = new Attribute("value", "1");
			paradigmatique.setAttribute(positif);
		} else {
			Attribute negatif = new Attribute("value", "0");
			paradigmatique.setAttribute(negatif);
		}

		// Syntagmatique
		Element syntagmatique = new Element("syntagmatique");
		if (currentSE.getAxe_rhetorique().equals("syntagmatique")) {
			Attribute positif = new Attribute("value", "1");
			syntagmatique.setAttribute(positif);
		} else {
			Attribute negatif = new Attribute("value", "0");
			syntagmatique.setAttribute(negatif);
		}

		// hybride
		Element hybride = new Element("hybride");
		if (currentSE.getAxe_rhetorique().equals("hybride")) {
			Attribute positif = new Attribute("value", "1");
			hybride.setAttribute(positif);
		} else {
			Attribute negatif = new Attribute("value", "0");
			hybride.setAttribute(negatif);
		}

		// Bivalente
		Element bivalente = new Element("bivalente");
		if (currentSE.getAxe_rhetorique().equals("bivalente")) {
			Attribute positif = new Attribute("value", "1");
			bivalente.setAttribute(positif);
		} else {
			Attribute negatif = new Attribute("value", "0");
			bivalente.setAttribute(negatif);
		}

		SEval.addContent(paradigmatique);
		SEval.addContent(syntagmatique);
		SEval.addContent(hybride);
		SEval.addContent(bivalente);

		annotation.addContent(SEval);

		return annotation;
	}

	public Element writeAxeIntentionnel(Unit currentSE) {
		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "axe_intentionnel");
		SEval.setAttribute(type);
		Attribute idSE = new Attribute("idSE", "" + num_annotation);
		SEval.setAttribute(idSE);

		// descriptive
		Element descriptive = new Element("descriptive");
		Element narrative = new Element("narrative");
		Element explicative = new Element("explicative");
		Element prescriptive = new Element("prescriptive");
		Element procedurale = new Element("procedurale");
		Element argumentative = new Element("argumentative");
		Element autreIntentionnel = new Element("intentionnel_autre");

		Attribute positif = new Attribute("value", "1");
		descriptive.setAttribute(new Attribute("value", "0"));
		narrative.setAttribute(new Attribute("value", "0"));
		explicative.setAttribute(new Attribute("value", "0"));
		prescriptive.setAttribute(new Attribute("value", "0"));
		procedurale.setAttribute(new Attribute("value", "0"));
		argumentative.setAttribute(new Attribute("value", "0"));
		autreIntentionnel.setAttribute(new Attribute("value", "0"));

		for (String value : currentSE.getAxe_intentionnel()) {

			if (value.equals("descriptive")) {
				descriptive.removeAttribute("value");
				descriptive.setAttribute(new Attribute("value", "1"));
			} else if (value.equals("narrative")) {
				narrative.removeAttribute("value");
				narrative.setAttribute(new Attribute("value", "1"));
			} else if (value.equals("explicative")) {
				explicative.removeAttribute("value");
				explicative.setAttribute(new Attribute("value", "1"));
			} else if (value.equals("prescriptive")) {
				prescriptive.removeAttribute("value");
				prescriptive.setAttribute(new Attribute("value", "1"));
			} else if (value.equals("procedurale")) {
				procedurale.removeAttribute("value");
				procedurale.setAttribute(new Attribute("value", "1"));
			} else if (value.equals("argumentative")) {
				argumentative.removeAttribute("value");
				argumentative.setAttribute(new Attribute("value", "1"));
			} else if (value.equals("autre_intentionnel")) {
				autreIntentionnel.removeAttribute("value");
				autreIntentionnel.setAttribute(new Attribute("value", "1"));
			}

		}

		SEval.addContent(descriptive);
		SEval.addContent(narrative);
		SEval.addContent(explicative);
		SEval.addContent(prescriptive);
		SEval.addContent(procedurale);
		SEval.addContent(argumentative);
		SEval.addContent(autreIntentionnel);

		annotation.addContent(SEval);

		return annotation;
	}

	public Element writeAxeSemantique(Unit currentSE) {
		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "axe_semantique");
		SEval.setAttribute(type);
		Attribute idSE = new Attribute("idSE", "" + num_annotation);
		SEval.setAttribute(idSE);

		// 3 booléens
		boolean visee_onto = false;
		boolean metaling = false;
		boolean sem_autre = false;

		// isA
		Element isA = new Element("isA");
		if (currentSE.getAxe_semantique().equals("isA")) {
			Attribute positif = new Attribute("value", "1");
			isA.setAttribute(positif);
			visee_onto = true;
		} else {
			Attribute negatif = new Attribute("value", "0");
			isA.setAttribute(negatif);
		}

		// partOf
		Element partOf = new Element("partOf");
		if (currentSE.getAxe_semantique().equals("partOf")) {
			Attribute positif = new Attribute("value", "1");
			partOf.setAttribute(positif);
			visee_onto = true;
		} else {
			Attribute negatif = new Attribute("value", "0");
			partOf.setAttribute(negatif);
		}

		// instanceOf
		Element instanceOf = new Element("instanceOf");
		if (currentSE.getAxe_semantique().equals("instanceOf")) {
			Attribute positif = new Attribute("value", "1");
			instanceOf.setAttribute(positif);
			visee_onto = true;
		} else {
			Attribute negatif = new Attribute("value", "0");
			instanceOf.setAttribute(negatif);
		}

		// autre_ontologique
		Element autreOntologique = new Element("ontologique_autre");
		if (currentSE.getAxe_semantique().equals("autre_ontologique")) {
			Attribute positif = new Attribute("value", "1");
			autreOntologique.setAttribute(positif);
			visee_onto = true;
		} else {
			Attribute negatif = new Attribute("value", "0");
			autreOntologique.setAttribute(negatif);
		}

		// Hyperonymie
		Element hyperonymie = new Element("hyperonymie");
		if (currentSE.getAxe_semantique().equals("hyperonymie")) {
			Attribute positif = new Attribute("value", "1");
			hyperonymie.setAttribute(positif);
			metaling = true;
		} else {
			Attribute negatif = new Attribute("value", "0");
			hyperonymie.setAttribute(negatif);
		}

		// meronymie
		Element meronymie = new Element("meronymie");
		if (currentSE.getAxe_semantique().equals("meronymie")) {
			Attribute positif = new Attribute("value", "1");
			meronymie.setAttribute(positif);
			metaling = true;
		} else {
			Attribute negatif = new Attribute("value", "0");
			meronymie.setAttribute(negatif);
		}

		// homonymie
		Element homonymie = new Element("homonymie");
		if (currentSE.getAxe_semantique().equals("homonymie")) {
			Attribute positif = new Attribute("value", "1");
			homonymie.setAttribute(positif);
			metaling = true;
		} else {
			Attribute negatif = new Attribute("value", "0");
			homonymie.setAttribute(negatif);
		}

		// synonymie
		Element synonymie = new Element("synonymie");
		if (currentSE.getAxe_semantique().equals("synonymie")) {
			Attribute positif = new Attribute("value", "1");
			synonymie.setAttribute(positif);
			metaling = true;
		} else {
			Attribute negatif = new Attribute("value", "0");
			synonymie.setAttribute(negatif);
		}

		// multilingue
		Element multilingue = new Element("multilingue");
		if (currentSE.getAxe_semantique().equals("multilingue")) {
			Attribute positif = new Attribute("value", "1");
			multilingue.setAttribute(positif);
			metaling = true;
		} else {
			Attribute negatif = new Attribute("value", "0");
			multilingue.setAttribute(negatif);
		}

		// autre_lexical
		Element autreLexical = new Element("lexical_autre");
		if (currentSE.getAxe_semantique().equals("autre_lexical")) {
			Attribute positif = new Attribute("value", "1");
			autreLexical.setAttribute(positif);
			metaling = true;
		} else {
			Attribute negatif = new Attribute("value", "0");
			autreLexical.setAttribute(negatif);
		}

		// autre_semantique
		Element autreSemantique = new Element("semantique_autre");
		if (currentSE.getAxe_semantique().equals("autre_semantique")) {
			Attribute positif = new Attribute("value", "1");
			autreSemantique.setAttribute(positif);
			sem_autre = true;

		} else {
			Attribute negatif = new Attribute("value", "0");
			autreSemantique.setAttribute(negatif);
		}

		// contextuelle ou pas
		Element contextuelle = new Element("contextuelle");
		if (currentSE.getAxe_semantiqueContext().equals("contextuelle")) {
			Attribute positif = new Attribute("value", "1");
			contextuelle.setAttribute(positif);
		} else {
			Attribute negatif = new Attribute("value", "0");
			contextuelle.setAttribute(negatif);
		}

		// Mise en forme - test
		if (sem_autre && visee_onto) {
			System.out.println("writeAxeSemantique : Annotation inconsistante");
			System.out.println("Erreur sem_autre et visee_onto");
		} else if (sem_autre && metaling) {
			System.out.println("writeAxeSemantique : Annotation inconsistante");
			System.out.println("Erreur sem autre et metaling");
		} else if (visee_onto && metaling) {
			System.out.println("writeAxeSemantique : Annotation inconsistante");
			System.out.println("Erreur visée onto et metaling");
		}

		Element visee_ontologique = new Element("visee_ontologique");
		if (visee_onto) {
			Attribute positif = new Attribute("value", "1");
			visee_ontologique.setAttribute(positif);
		} else {
			Attribute negatif = new Attribute("value", "0");
			visee_ontologique.setAttribute(negatif);
		}

		Element metalinguistique = new Element("metalinguistique");
		if (metaling) {
			Attribute positif = new Attribute("value", "1");
			metalinguistique.setAttribute(positif);
		} else {
			Attribute negatif = new Attribute("value", "0");
			metalinguistique.setAttribute(negatif);
		}

		visee_ontologique.addContent(isA);
		visee_ontologique.addContent(partOf);
		visee_ontologique.addContent(instanceOf);
		visee_ontologique.addContent(autreOntologique);
		SEval.addContent(visee_ontologique);

		metalinguistique.addContent(hyperonymie);
		metalinguistique.addContent(meronymie);
		metalinguistique.addContent(homonymie);
		metalinguistique.addContent(synonymie);
		metalinguistique.addContent(multilingue);
		metalinguistique.addContent(autreLexical);
		SEval.addContent(metalinguistique);

		SEval.addContent(autreSemantique);
		SEval.addContent(contextuelle);

		annotation.addContent(SEval);

		return annotation;
	}

	public void writeMarqRelPrimer(Unit currentSE) {

		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "marq_relation");
		Attribute localisation = new Attribute("localisation", "primer");
		Attribute idItem = new Attribute("idItem", "-1");
		Attribute idSE = new Attribute("idSE", "" + num_annotation);

		SEval.setAttribute(type);
		SEval.setAttribute(idSE);
		SEval.setAttribute(localisation);
		SEval.setAttribute(idItem);

		// <spanSE begin =" ? " end =" ?"
		// idSegment="identificateur du 1er segment  au concept"/>
		// <segment idSegment="identificateur du segment  au concept "> texte
		// relatif au 1er segment  au
		// concept </segment>

		int count_segment = 0;
		for (Segment segment : currentSE.getPrimer().getMarqueurRel()) {
			Element span = new Element("span");

			Attribute span_deb = new Attribute("begin", ""
					+ segment.getIndice_begin());
			Attribute span_fin = new Attribute("end", ""
					+ segment.getIndice_end());
			span.setAttribute(span_deb);
			span.setAttribute(span_fin);
			Attribute id_item = new Attribute("id", "" + count_segment);
			span.setAttribute(id_item);
			SEval.addContent(span);

			Element text = new Element("text");
			text.setText(segment.getText());
			Attribute id_item2 = new Attribute("id", "" + count_segment);
			text.setAttribute(id_item2);

			SEval.addContent(text);
			count_segment++;
		}
		annotation.addContent(SEval);
		racine.addContent(annotation);

	}

	public void writeMarqRelItem(Unit currentSE, Item item) {

		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "marq_relation");
		Attribute localisation = new Attribute("localisation", "item");
		Attribute idItem = new Attribute("idItem", item.getId());
		Attribute idSE = new Attribute("idSE", "" + num_annotation);

		SEval.setAttribute(type);
		SEval.setAttribute(idSE);
		SEval.setAttribute(localisation);
		SEval.setAttribute(idItem);

		// <spanSE begin =" ? " end =" ?"
		// idSegment="identificateur du 1er segment  au concept"/>
		// <segment idSegment="identificateur du segment  au concept "> texte
		// relatif au 1er segment  au
		// concept </segment>

		int count_segment = 0;

		for (Segment segment : item.getMarqueurRel()) {
			Element span = new Element("span");

			Attribute span_deb = new Attribute("begin", ""
					+ segment.getIndice_begin());
			Attribute span_fin = new Attribute("end", ""
					+ segment.getIndice_end());
			span.setAttribute(span_deb);
			span.setAttribute(span_fin);
			Attribute id_item = new Attribute("id", "" + count_segment);
			span.setAttribute(id_item);
			SEval.addContent(span);

			Element text = new Element("text");
			text.setText(segment.getText());
			Attribute id_item2 = new Attribute("id", "" + count_segment);
			text.setAttribute(id_item2);

			SEval.addContent(text);
			count_segment++;
		}
		annotation.addContent(SEval);
		racine.addContent(annotation);

	}

	public void writeMarqRel(Unit currentSE) {

		if (currentSE.getPrimer() != null && currentSE.getPrimer().hasMarqRel()) {
			writeMarqRelPrimer(currentSE);
		} else if (currentSE.getItems().size() > 0) {
			for (Item item : currentSE.getItems()) {
				if (item.hasMarqRel()) {
					writeMarqRelItem(currentSE, item);
				}
			}
		} else {
			System.out.println("no Concept");
		}

	}

	public void writeCirconstants(Unit currentSE) {
		if (currentSE.getPrimer() != null
				&& currentSE.getPrimer().hasCirconstant()) {
			writeCirconstantPrimer(currentSE);
		}
		if (currentSE.getItems().size() > 0) {
			for (Item item : currentSE.getItems()) {
				if (item.hasCirconstant()) {
					writeCirconstantItem(currentSE, item);
				}
			}
		} else {
			System.out.println("no Circonstant");
		}
	}

	public void writeConcepts(Unit currentSE) {

		// Il y a un CONCEPT dans le PRIMER
		// Il y a un CONCEPT dans chacun des items

		if (currentSE.getPrimer() != null && currentSE.getPrimer().hasConcept()) {
			writeConceptPrimer(currentSE);
		}
		if (currentSE.getItems().size() > 0) {
			for (Item item : currentSE.getItems()) {
				if (item.hasConcept()) {
					writeConceptItem(currentSE, item);
				}
			}
		} else {
			System.out.println("no Concept");
		}

		// <SEval type = "concept" idSE="identificateur de la SE" localisation
		// ="primer ou item" idItem = "identificateur

	}

	public void writeCirconstantPrimer(Unit currentSE) {
		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "circonstant");
		Attribute localisation = new Attribute("localisation", "primer");
		Attribute idItem = new Attribute("idItem", "-1");
		Attribute idSE = new Attribute("idSE", "" + num_annotation);

		SEval.setAttribute(type);
		SEval.setAttribute(idSE);
		SEval.setAttribute(localisation);
		SEval.setAttribute(idItem);

		// <spanSE begin =" ? " end =" ?"
		// idSegment="identificateur du 1er segment  au concept"/>
		// <segment idSegment="identificateur du segment  au concept "> texte
		// relatif au 1er segment  au
		// concept </segment>

		int count_segment = 0;
		for (Segment segment : currentSE.getPrimer().getCirconstant()) {
			Element span = new Element("span");

			Attribute span_deb = new Attribute("begin", ""
					+ segment.getIndice_begin());
			Attribute span_fin = new Attribute("end", ""
					+ segment.getIndice_end());
			span.setAttribute(span_deb);
			span.setAttribute(span_fin);
			Attribute id_item = new Attribute("id", "" + count_segment);
			span.setAttribute(id_item);
			SEval.addContent(span);

			Element text = new Element("text");
			text.setText(segment.getText());
			Attribute id_item2 = new Attribute("id", "" + count_segment);
			text.setAttribute(id_item2);

			SEval.addContent(text);
			count_segment++;

		}
		annotation.addContent(SEval);
		racine.addContent(annotation);
	}

	public void writeCirconstantItem(Unit currentSE, Item item) {
		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "circonstant");
		Attribute localisation = new Attribute("localisation", "item");
		Attribute idItem = new Attribute("idItem", item.getId());
		Attribute idSE = new Attribute("idSE", "" + num_annotation);

		SEval.setAttribute(type);
		SEval.setAttribute(idSE);
		SEval.setAttribute(localisation);
		SEval.setAttribute(idItem);

		// <spanSE begin =" ? " end =" ?"
		// idSegment="identificateur du 1er segment  au concept"/>
		// <segment idSegment="identificateur du segment  au concept "> texte
		// relatif au 1er segment  au
		// concept </segment>

		int count_segment = 0;
		for (Segment segment : item.getCirconstant()) {
			Element span = new Element("span");

			Attribute span_deb = new Attribute("begin", ""
					+ segment.getIndice_begin());
			Attribute span_fin = new Attribute("end", ""
					+ segment.getIndice_end());
			span.setAttribute(span_deb);
			span.setAttribute(span_fin);
			Attribute id_item = new Attribute("id", "" + count_segment);
			span.setAttribute(id_item);
			SEval.addContent(span);

			Element text = new Element("text");
			text.setText(segment.getText());
			Attribute id_item2 = new Attribute("id", "" + count_segment);
			text.setAttribute(id_item2);

			SEval.addContent(text);
			count_segment++;

		}
		annotation.addContent(SEval);
		racine.addContent(annotation);
	}

	public void writeConceptPrimer(Unit currentSE) {
		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "concept");
		Attribute localisation = new Attribute("localisation", "primer");
		Attribute idItem = new Attribute("idItem", "-1");
		Attribute idSE = new Attribute("idSE", "" + num_annotation);

		SEval.setAttribute(type);
		SEval.setAttribute(idSE);
		SEval.setAttribute(localisation);
		SEval.setAttribute(idItem);

		// <spanSE begin =" ? " end =" ?"
		// idSegment="identificateur du 1er segment  au concept"/>
		// <segment idSegment="identificateur du segment  au concept "> texte
		// relatif au 1er segment  au
		// concept </segment>

		int count_segment = 0;
		for (Segment segment : currentSE.getPrimer().getConcept()) {
			Element span = new Element("span");

			Attribute span_deb = new Attribute("begin", ""
					+ segment.getIndice_begin());
			Attribute span_fin = new Attribute("end", ""
					+ segment.getIndice_end());
			span.setAttribute(span_deb);
			span.setAttribute(span_fin);
			Attribute id_item = new Attribute("id", "" + count_segment);
			span.setAttribute(id_item);
			SEval.addContent(span);

			Element text = new Element("text");
			text.setText(segment.getText());
			Attribute id_item2 = new Attribute("id", "" + count_segment);
			text.setAttribute(id_item2);

			SEval.addContent(text);
			count_segment++;
		}
		annotation.addContent(SEval);
		racine.addContent(annotation);

	}

	public void writeConceptItem(Unit currentSE, Item item) {

		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "concept");
		Attribute localisation = new Attribute("localisation", "item");
		Attribute idItem = new Attribute("idItem", "" + item.getId());
		Attribute idSE = new Attribute("idSE", "" + num_annotation);

		SEval.setAttribute(type);
		SEval.setAttribute(idSE);
		SEval.setAttribute(localisation);
		SEval.setAttribute(idItem);

		int count_segment = 0;
		for (Segment segment : item.getConcept()) {

			Element span = new Element("span");

			// Span deb et fin
			Attribute span_deb = new Attribute("begin", ""
					+ segment.getIndice_begin());
			Attribute span_fin = new Attribute("end", ""
					+ segment.getIndice_end());
			span.setAttribute(span_deb);
			span.setAttribute(span_fin);

			// id pour le span
			Attribute id_item = new Attribute("id", "" + count_segment);
			span.setAttribute(id_item);
			SEval.addContent(span);

			// Text
			Element text = new Element("text");
			text.setText(segment.getText());
			Attribute id_item2 = new Attribute("id", "" + count_segment);
			text.setAttribute(id_item2);

			SEval.addContent(text);
			count_segment++;

		}
		annotation.addContent(SEval);
		racine.addContent(annotation);
	}

	public Element writeAnnotSyntaxItem(Unit currentSE) {

		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "items_syntax");
		SEval.setAttribute(type);
		Attribute idSE = new Attribute("idSE", "" + num_annotation);
		SEval.setAttribute(idSE);

		int count = 0;
		for (Item it : currentSE.getItems()) {
			Element currItem = new Element("item");
			Attribute currItem_attribute = new Attribute("id",""+count);
			currItem.setAttribute(currItem_attribute);

			Structure talismane_client_structure = it.getStructure();

			int nb_sentence = 1;
			for (Sentence currSentence : talismane_client_structure) {
				Element new_sentence = new Element("sentence");
				Attribute id_sentence = new Attribute("id", "" + nb_sentence);
				new_sentence.setAttribute(id_sentence);
				nb_sentence++;

				int nb_word = 1;
				for (Token currToken : currSentence) {
					Element new_word = new Element("word");
					Attribute id_word = new Attribute("id", "" + nb_word);
					new_word.setAttribute(id_word);
					nb_word++;

					// Lem
					Attribute lem = new Attribute("lem", ""
							+ currToken.getLemma());
					new_word.setAttribute(lem);

					// postag
					Attribute postag = new Attribute("postag", ""
							+ currToken.getCppostag());
					new_word.setAttribute(postag);

					// morpho
					Attribute morpho = new Attribute("morpho", ""
							+ currToken.getFeats());
					new_word.setAttribute(morpho);

					// head
					Attribute head = new Attribute("head", ""
							+ currToken.getHead());
					new_word.setAttribute(head);

					// dep
					Attribute dep = new Attribute("dep", ""
							+ currToken.getDeprel());
					new_word.setAttribute(dep);

					new_word.setText(currToken.getForm());

					new_sentence.addContent(new_word);
				}

				currItem.addContent(new_sentence);
			}
			
			SEval.addContent(currItem);
			count++;
		}
		annotation.addContent(SEval);

		XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
		String s = outp.outputString(annotation);
		System.out.println(s);

		
		return annotation;
	}

	public Element writeAnnotItem(Items itemsList, Unit currentSE) {

		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "items");
		SEval.setAttribute(type);
		Attribute idSE = new Attribute("idSE", "" + num_annotation);
		SEval.setAttribute(idSE);

		int count = 0;
		for (Item it : itemsList) {

			Element span = new Element("span");
			Attribute span_deb = new Attribute("begin", ""
					+ it.getIndice_begin());
			Attribute span_fin = new Attribute("end", "" + it.getIndice_end());
			span.setAttribute(span_deb);
			span.setAttribute(span_fin);
			Attribute id_item = new Attribute("id", "" + count);
			span.setAttribute(id_item);
			SEval.addContent(span);

			Element text = new Element("text");
			text.setText(it.getText());
			Attribute id_item2 = new Attribute("id", "" + count);
			text.setAttribute(id_item2);

			SEval.addContent(text);
			count++;
		}
		annotation.addContent(SEval);

		// Element comment = comment(currentSE);
		// annotation.addContent(comment);

		return annotation;
	}

	public Element writeAnnotCloture(Unit currentSE) {
		// Gestion de la clôture

		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "cloture");
		SEval.setAttribute(type);
		Attribute idSE = new Attribute("idSE", "" + num_annotation);
		SEval.setAttribute(idSE);

		// Span
		Element span = new Element("span");
		Attribute span_deb = new Attribute("begin", ""
				+ currentSE.getClot().getIndice_begin());
		Attribute span_fin = new Attribute("end", ""
				+ currentSE.getClot().getIndice_end());
		span.setAttribute(span_deb);
		span.setAttribute(span_fin);
		SEval.addContent(span);

		Element text = new Element("text");
		text.setText(currentSE.getClot().getText());
		SEval.addContent(text);
		annotation.addContent(SEval);

		return annotation;
	}

	public Element writeAnnotSyntaxPrimer(Unit currentSE) {

		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "primer_syntax");
		SEval.setAttribute(type);
		Attribute idSE = new Attribute("idSE", "" + num_annotation);
		SEval.setAttribute(idSE);

		Structure talismane_client_structure = currentSE.getPrimer()
				.getStructure();

		System.out.println("SIZE : " + talismane_client_structure.size());

		int nb_sentence = 1;
		for (Sentence currSentence : talismane_client_structure) {
			Element new_sentence = new Element("sentence");
			Attribute id_sentence = new Attribute("id", "" + nb_sentence);
			new_sentence.setAttribute(id_sentence);
			nb_sentence++;

			int nb_word = 1;
			for (Token currToken : currSentence) {
				Element new_word = new Element("word");
				Attribute id_word = new Attribute("id", "" + nb_word);
				new_word.setAttribute(id_word);
				nb_word++;

				// Lem
				Attribute lem = new Attribute("lem", "" + currToken.getLemma());
				new_word.setAttribute(lem);

				// postag
				Attribute postag = new Attribute("postag", ""
						+ currToken.getCppostag());
				new_word.setAttribute(postag);

				// morpho
				Attribute morpho = new Attribute("morpho", ""
						+ currToken.getFeats());
				new_word.setAttribute(morpho);

				// head
				Attribute head = new Attribute("head", "" + currToken.getHead());
				new_word.setAttribute(head);

				// dep
				Attribute dep = new Attribute("dep", "" + currToken.getDeprel());
				new_word.setAttribute(dep);

				new_word.setText(currToken.getForm());

				new_sentence.addContent(new_word);
			}

			SEval.addContent(new_sentence);
		}

		annotation.addContent(SEval);

		XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
		String s = outp.outputString(annotation);
		System.out.println(s);

		return annotation;
	}

	public Element writeAnnotPrimer(Unit currentSE) {
		// Gestion du primer

		Element annotation = new Element("annotation");
		Attribute classe = new Attribute("type", "2");
		annotation.setAttribute(classe);

		int num_annotation = currentSE.getId();

		Element metaData = metaData(currentSE, num_annotation);
		annotation.addContent(metaData);

		Element SEval = new Element("SEval");
		Attribute type = new Attribute("type", "primer");
		SEval.setAttribute(type);
		Attribute idSE = new Attribute("idSE", "" + num_annotation);
		SEval.setAttribute(idSE);

		// Span
		Element span = new Element("span");
		Attribute span_deb = new Attribute("begin", ""
				+ currentSE.getPrimer().getIndice_begin());
		Attribute span_fin = new Attribute("end", ""
				+ currentSE.getPrimer().getIndice_end());
		span.setAttribute(span_deb);
		span.setAttribute(span_fin);
		SEval.addContent(span);

		Element text = new Element("text");
		text.setText(currentSE.getPrimer().getText());
		SEval.addContent(text);
		annotation.addContent(SEval);

		// Element comment = comment(currentSE);
		// annotation.addContent(comment);

		return annotation;
	}

	public Element metaData(Unit currentSE, int num_annotation) {

		Element metaData = new Element("metadata");

		Element id = new Element("id");
		id.setText("" + num_annotation);
		metaData.addContent(id);

		Element author = new Element("auteur");
		author.setText(currentSE.getAnnotation().getAuthor());
		metaData.addContent(author);

		Element date = new Element("date");
		date.setText(shortDateFormat
				.format(currentSE.getAnnotation().getDate()));
		metaData.addContent(date);

		Element doc_name = new Element("document");
		doc_name.setText(currentSE.getAnnotation().getDocumentName());
		metaData.addContent(doc_name);

		return metaData;
	}

	public Element annotationSE(Unit currentSE, int num_annotation) {

		Element se = new Element("SE");

		// Id
		Element id = new Element("id");
		id.setText("" + num_annotation);
		se.addContent(id);

		// Text
		Element text = new Element("text");
		text.setText(currentSE.getText());
		se.addContent(text);

		// Span
		Element span = new Element("span");
		Attribute span_deb = new Attribute("begin", ""
				+ currentSE.getIndice_begin());
		Attribute span_fin = new Attribute("end", ""
				+ currentSE.getIndice_end());
		span.setAttribute(span_deb);
		span.setAttribute(span_fin);
		se.addContent(span);

		return se;
	}

	public Element comment(Unit currentSE) {
		Element commentNode = new Element("comment");
		commentNode.setText(currentSE.getAnnotation().getComment());

		return commentNode;
	}

	public void affiche() {
		try {
			// On utilise ici un affichage classique avec getPrettyFormat()
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(document, System.out);
		} catch (java.io.IOException e) {
		}
	}

	public void enregistre(String fichier) {

		try {
			// On utilise ici un affichage classique avec getPrettyFormat()
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			// Remarquez qu'il suffit simplement de créer une instance de
			// FileOutputStream
			// avec en argument le nom du fichier pour effectuer la
			// sérialisation.
			System.out.println("IO_MIG_enregistre : écriture dans " + fichier);
			sortie.output(document, new FileOutputStream(fichier));
		} catch (java.io.IOException e) {
		}
	}

	// TODO
	public void organizeSegment() {

		for (Unit currentSE : chain_SE) {

			for (Segment segment : currentSE.getPrimer().getConcept()) {

			}

			for (Segment segment : currentSE.getPrimer().getCirconstant()) {

			}

			for (Segment segment : currentSE.getPrimer().getMarqueurRel()) {

			}

		}

	}

	public void organizeItems() {

		for (Unit mySe : chain_SE) {
			Items myListItems = mySe.getItems();
			Collections.sort(myListItems);
		}
	}

	public void organizeSE() {
		Collections.sort(chain_SE);
	}

	public String getNewPath() {
		String new_path = path;
		
//		System.out.println("DEBUG : " + path);

		if (File.separator.equals("\\")) {
			
//			System.out.println("DEBUG : Windows");
			new_path =  new_path.replace("LARA_corpus","LARA_xml");
			
//			new_path = new_path.replaceAll("corpus\\\\((.)*)\\.html",
//					"xml\\\\$1\\.xml");
		} else {
//			new_path = new_path.replaceAll("corpus" + File.separator
//					+ "((.*))\\.html", "xml" + File.separator + "$1\\.xml");
//			System.out.println("DEBUG : Linux");
			new_path = new_path.replace("LARA_corpus","LARA_xml");
		}
		new_path = new_path.replaceAll("\\.html", "\\.xml");

		System.out
				.println("IO_MIG_getNewPath : chemin pour le XML " + new_path);
		return new_path;
	}

	public boolean hisXMLVersion(String path) {

		this.path = path;
		new_path = this.getNewPath();

		if (new_path.contains(".xml")) {
			File f1 = new File(new_path);

			return f1.exists();
		} else {
			return false;
		}
	}

}
