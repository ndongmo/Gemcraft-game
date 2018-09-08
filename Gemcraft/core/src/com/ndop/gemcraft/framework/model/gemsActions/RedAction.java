package com.ndop.gemcraft.framework.model.gemsActions;

import java.util.ArrayList;

/**
 * Action de la gemme rouge. Applique les dégâts sur les ennemis contenus dans son rayon.
 * @author --
 * @version 1.0
 */
public abstract class RedAction extends GemAction{
	/**
	 * Liste des rayons d'effet.
	 */
	protected ArrayList<Integer> radiusList = new ArrayList<Integer>();
	/**
	 * Liste des dégât de zone.
	 */
	protected ArrayList<Float> radiusDamageList = new ArrayList<Float>();
	/**
	 * Calcul du rayon.
	 * @param nbGem le nombre de gemme de la tour qui génère le projectile.
	 */
	protected abstract float computeRadius(float nbGem);
}
