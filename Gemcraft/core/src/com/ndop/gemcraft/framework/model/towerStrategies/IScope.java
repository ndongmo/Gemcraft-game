package com.ndop.gemcraft.framework.model.towerStrategies;

/**
 * Stratégie de calcul de la portée de tir d'une tour.
 * @author --
 * @version 1.0
 */
public interface IScope {
	/**
	 * Calcul la portée de tir d'une tour
	 * @param level niveau de la tour
	 * @return portée exprimée en cases.
	 */
	int computeScope(int level);
}
