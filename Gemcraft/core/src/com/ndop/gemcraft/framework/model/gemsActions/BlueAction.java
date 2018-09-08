package com.ndop.gemcraft.framework.model.gemsActions;

import java.util.ArrayList;

/**
 * Action de la gemme bleu (ralentissement). 
 * Un ennemi touché voit sa vitesse réduite pendant une seconde.
 * @author --
 * @version 1.0
 */
public abstract class BlueAction extends GemAction{
	/**
	 * Liste des multiplicateurs de vitesse.
	 */
	protected ArrayList<Float> slowDownMult = new ArrayList<Float>();
	/**
	 * Liste des timer.
	 */
	protected ArrayList<Float> timers = new ArrayList<Float>();
	/**
	 * Calcule le multiplicateur de vitesse du projectile.
	 * @param nbGem nombre de gemme
	 * @return multiplicateur de vitesse
	 */
	protected abstract float computeSlowDownMult(int nbGem);
}
