package com.ndop.gemcraft.framework.model.gemsActions;

/**
 * Action de la gemme cyan (paralysie). Un ennemi touché a une 
 * certaine probabilité d'être paralysé pendant une seconde.
 * @author --
 * @version 1.0
 */
public abstract class CyanAction extends GemAction{
	/**
	 * Timer de paralysie.
	 */
	protected float timer = 0;
	/**
	 * Calcule la probabilité de paralysie.
	 * @return probabilité en pourcentage.
	 */
	protected abstract float computeProbaParalysis(float nbGem);
}
