package com.ndop.gemcraft.framework.model.towerStrategies;

/**
 * Stratégie de calcul des dégats d'une tour.
 * @author --
 * @version 1.0
 */
public interface IDamage {
	/**
	 * Calcul les dégats causés par un tir d'une tour.
	 * @param level niveau de la tour
	 * @return dégats
	 */
	float computeDamage(int level);
}
