package com.ndop.gemcraft.framework.model.towerStrategies;

/**
 * Stratégie de calcul du coûts d'une tour.
 * @author --
 * @version 1.0
 */
public interface IBuyable {
	/**
	 * Calcul du coûts d'une tour.
	 * @param nbTower nombre de tour que possède le joueur.
	 * @return prix d'une tour.
	 */
	float computeCost(int nbTower);
}
