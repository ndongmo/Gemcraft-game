package com.ndop.gemcraft.framework.model.gemsActions;

import java.util.ArrayList;

/**
 * Action de la gemme magenta (chaîne). Un ennemi touché a une 
 * certaine probabilité pour qu'un ennemi proche soit également touché.
 * @author --
 * @version 1.0
 */
public abstract class MagentaAction extends GemAction{
	/**
	 * Liste des dégâts de chaine.
	 */
	protected ArrayList<Float> damages = new ArrayList<Float>();
	/**
	 * Calcule la probabilité des dégâts de chaine.
	 * @param nbGem nombre de gemme
	 * @return probabilité en pourcentage
	 */
	protected abstract float computeStringProba(int nbGem);
}
