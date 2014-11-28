package org.latoe.layoutanalysis.pdf.labelisation.features;

import java.util.HashMap;

import org.melodi.learning.iitb.Model.FeatureGenImpl;
import org.melodi.learning.iitb.Model.Model;

public class FeatureGenerator_Labelisation extends FeatureGenImpl implements java.io.Serializable {

	private static final long serialVersionUID = 3986800700251134782L;

	public HashMap<Integer, String> intMapLabel;

	// Utilisé pour enlever le "bruit" dans les marges inter chunk (marges
	// "haute")
	// seuil de détection pour savoir si la marge d'un chunk est inférieur à la
	// marge du doc
	public static float seuil = 0.10F;

	public FeatureGenerator_Labelisation(Model m, int numLabels,
			boolean addFeatureNow) throws Exception {
		super(m, numLabels, addFeatureNow);
	}

	public FeatureGenerator_Labelisation(String modelGraphType, int nlabels)
			throws Exception {
		super(modelGraphType, nlabels);
	}

	@Override
	protected void addFeatures() {
		/**
		 * TRANSITIONS Features pour les transitions (arcs du graphe) - Dans
		 * "./Output/features.txt" Edge(pos_courante)->next_position
		 */
		addFeature(new EdgeFeature_Labelisation(this, 1));

		/**
		 * POSITIONS Features de position (au sein de la séquence)
		 * -StartFeatures : renvoie vrai si le label commence une séquence
		 * -EndFeatures : renvoie vrai si le label commence une séquence
		 */
		addFeature(new StartFeatures_Labelisation(this));
		addFeature(new EndFeatures_Labelisation(this));

		/**
		 * DICTIONNAIRE Features par dico : - UnknowFeature - WordFeatures :
		 * Features avec Regex : - FeatureTypesEachlabel
		 * 
		 * Utiles pour travailler sur des mots, cependant, ici on aimerait
		 * travailler sur des propriétés visuelles.
		 */
		// dict = new WordsInTrain();
		// addFeature(new UnknownFeature_Labelisation(this, dict));
		// addFeature(new WordFeatures_Labelisation(this, dict));
		// addFeature(new FeatureTypesEachLabel(this, new ConcatRegexFeatures(
		// this, 0, 0)));
		// addFeature(new KnownInOtherState(this, dict));

		/**
		 * Features pour la Police
		 */
		addFeature(new Feature_MemeTaillePoliceMode(this));
		addFeature(new Feature_PlusTaillePoliceMode(this));
		addFeature(new Feature_MoinsTaillePoliceMode(this));
		addFeature(new Feature_ContrasteTaillePolice(this));
		addFeature(new Feature_ContrasteTypePolice(this));

		/**
		 * Features de typographie
		 */
		addFeature(new Feature_ContientNumerotationFirstWord(this));
		addFeature(new Feature_SymboleFirstWord(this));
		addFeature(new Feature_AllBold(this));
		addFeature(new Feature_AllItalic(this));
		addFeature(new Feature_ContientMotsEnGras(this));
		addFeature(new Feature_ContientMotsEnItalique(this));
		// ici j'ai découpe les distributions en quartiles sur le corpus de DEV
		// (geop+ling)
		addFeature(new Feature_RatioPoliceRelativeNbMots(this, Float.MIN_VALUE,
				0));
		addFeature(new Feature_RatioPoliceRelativeNbMots(this, 0, 1));
		addFeature(new Feature_RatioPoliceRelativeNbMots(this, 1, 5));
		addFeature(new Feature_RatioPoliceRelativeNbMots(this, 5, 10));
		addFeature(new Feature_RatioPoliceRelativeNbMots(this, 10, 15));
		addFeature(new Feature_RatioPoliceRelativeNbMots(this, 15,
				Float.MAX_VALUE));

		/**
		 * Marges
		 */
		addFeature(new Feature_PlusMargeAGaucheMode(this));
		addFeature(new Feature_MargeGaucheDroite(this));
		addFeature(new Feature_PlusMargeADroiteMode(this, 1));

		/*
		 * // /!\ /!\ Pas l'air de marcher avec la marge haute moyenne...
		 * (crf_model : p = 0.0) /!\ /!\ addFeature(new
		 * Feature_MoinsMargeHautMoy(this)); addFeature(new
		 * Feature_PlusMargeHautMoy(this)); addFeature(new
		 * Feature_MemeMargeHautMoy(this));
		 */

		/**
		 * Ratio
		 */
		// addFeature(new Feature_RatioLl_sur_NbMots(this));
		// NB : borne inférieure exclue (pour virer les quelques zéros qui sont
		// des erreurs dans les longueurs/hauteurs de chunk
		// NB2 : ici j'ai découpe les distributions en quartiles sur le corpus
		// de DEV (geop+ling)
		addFeature(new Feature_RatioLargHautComprisEntre(this, 0F, 19F));
		addFeature(new Feature_RatioLargHautComprisEntre(this, 19F, 155F));
		addFeature(new Feature_RatioLargHautComprisEntre(this, 155F, 467F));
		addFeature(new Feature_RatioLargHautComprisEntre(this, 567F,
				Float.MAX_VALUE));

		addFeature(new Feature_RatioSurfaceComprisEntre(this, 0F, 2200F));
		addFeature(new Feature_RatioSurfaceComprisEntre(this, 2200F, 3060F));
		addFeature(new Feature_RatioSurfaceComprisEntre(this, 3060F, 4542F));
		addFeature(new Feature_RatioSurfaceComprisEntre(this, 4542F,
				Float.MAX_VALUE));

		/*
		 * Emplacement sur la page de chaque chunk : dans quel partie de la page
		 * se situe-t-il ? (on divise la hauteur de la page en 5)
		 */
		addFeature(new Feature_PositionVerticaleCompriseEntre(this, 1));
		/*
		 * addFeature(new Feature_PositionVerticaleCompriseEntre(this, 2));
		 * addFeature(new Feature_PositionVerticaleCompriseEntre(this, 3));
		 * addFeature(new Feature_PositionVerticaleCompriseEntre(this, 4));
		 */
		addFeature(new Feature_PositionVerticaleCompriseEntre(this, 5));

	}

}
