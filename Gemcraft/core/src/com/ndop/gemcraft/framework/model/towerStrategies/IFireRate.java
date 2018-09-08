package com.ndop.gemcraft.framework.model.towerStrategies;

/**
 * Stratégie de calcul du débit de tir d'une tour.
 * @author --
 * @version 1.0
 */
public interface IFireRate {
	/**
	 * Calcul le débit de tir selon le niveau d'une tour.
	 * @param level niveau de la tour
	 * @param nbGem nombre de gemme dans la tour
	 * @param nbLocation nombre d'emplacement de gemme dans la tour
	 * @return le débit de tir.
	 */
	float computeFireRate(int level, int nbGem, int nbLocation);
}
