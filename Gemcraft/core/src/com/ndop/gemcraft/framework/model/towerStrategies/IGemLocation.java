package com.ndop.gemcraft.framework.model.towerStrategies;

/**
 * Stratégie de calcul du nombre d'emplacement de gemme d'une tour.
 * @author --
 * @version 1.0
 */
public interface IGemLocation {
	/**
	 * Calcul le nombre d'emplacement de gemme d'une tour.
	 * @param level niveau de la tour
	 * @return le nombre d'emplacement
	 */
	public int computeNbLocation(int level);
}
