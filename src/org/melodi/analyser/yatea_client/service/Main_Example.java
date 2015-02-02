package org.melodi.analyser.yatea_client.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import logicalobjects.Term;

import org.jdom2.JDOMException;
import org.melodi.analyser.talismane_client.service.Structure;
import org.melodi.analyser.talismane_client.service.TalismaneClient;
import org.melodi.analyser.yatea_client.service.YateaClient;

/**
 * Client pour YaTea
 * @author jfaucon
 *
 */
public class Main_Example {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException, JDOMException, ClassNotFoundException {
	
		/* pre-requisite : YaTea must be installed and can be reached with "yatea" command */
		YateaClient yatea_client = new YateaClient();
		TalismaneClient talismane_client = new TalismaneClient();
		
		
		/* A test String */
		String test1 = "Le calcul de la similarité sémantique entre les termes repose sur l'existence "
				+ "et l'utilisation de ressources sémantiques. "
				+ "Cependant de telles ressources, qui consistent souvent à proposer des équivalences "
				+ "entre entités, ou des synonymes, doivent elles-mêmes être d'abord analysées afin de "
				+ "définir des zones de fiabilité où la similarité sémantique est plus forte. "
				+ "Nous proposons une méthode d'acquisition de synonymes élémentaires grâce à l'exploitation "
				+ "des terminologies structurées au travers de l'analyse de la structure syntaxique des termes "
				+ "complexes et de leur compositionnalités. ";
		
		String test2 = "On examine les alternatives d'une production économique "
				+ "de vapeur et de courant pour la sucrerie.";
		
		String test3 = "Un volcan est formé de différentes structures que l'on retrouve en général chez chacun d'eux :"
				+ "une chambre magmatique alimentée par du magma venant du manteau et jouant le rôle de réservoir et de lieu de différenciation du magma. Lorsque celle-ci se vide à la suite d'une éruption, le volcan peut s'affaisser et donner naissance à une caldeira. Les chambres magmatiques se trouvent entre dix et cinquante kilomètres de profondeur dans la lithosphère;"
				+ "une cheminée volcanique qui est le lieu de transit privilégié du magma de la chambre magmatique vers la surface ;"
				+ " un cratère ou une caldeira sommitale où débouche la cheminée volcanique ;"
				+ "une ou plusieurs cheminées volcaniques secondaires partant de la chambre magmatique ou de la cheminée volcanique principale et débouchant en général sur les flancs du volcan, parfois à sa base ; elles peuvent donner naissance à de petits cônes secondaires ;"
				+ "des fissures latérales qui sont des fractures longitudinales dans le flanc du volcan provoquées par son gonflement ou son dégonflement ; elles peuvent permettre l'émission de lave sous la forme d'une éruption fissurale.";

    
		String test4 = "les chambres magmatiques alimentées par du magma venant du manteau et jouant le rôle de réservoir et de chambre magmatique.";
		
		
				
		/* Analysing structure */
		String text_original = test2;
		Structure structure_test = talismane_client.analyse(text_original, false);
		ArrayList<Term> terms = yatea_client.analyse(text_original, structure_test, false);
		
		/* print terms */
		for(Term currTerm : terms){
			System.out.println(currTerm.toString());
		}
		

	}

}
