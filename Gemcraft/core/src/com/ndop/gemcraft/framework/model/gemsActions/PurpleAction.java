package com.ndop.gemcraft.framework.model.gemsActions;

import java.util.ArrayList;

/**
 * Action de la gemme magenta (chaîne).
 * Un ennemi touché voit tous les dégats qu'il recoit augmenter pendant une seconde.
 * @author --
 * @version 1.0
 */
public abstract class PurpleAction extends GemAction{
	/**
	 * Liste des multiplicateurs de vulnérabilité.
	 */
	protected ArrayList<Float> vulnerabilityMult = new ArrayList<Float>();
	/**
	 * Liste des timer.
	 */
	protected ArrayList<Float> timers = new ArrayList<Float>();
	/**
	 * Calcule le multiplicateur de vitesse du projectile.
	 * @param nbGem nombre de gemme
	 * @return multiplicateur de vitesse
	 */
	protected abstract float computeVulnerabilityMult(int nbGem);
}
