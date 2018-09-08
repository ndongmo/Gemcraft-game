package com.ndop.gemcraft.framework.model.towerStrategies;

/**
 * Stratégie de calcul du coûts d'évolution d'une tour.
 * @author --
 * @version 1.0
 */
public interface IEvolution {
	/**
	 * Calcul du coûts de l'évolution d'une tour.
	 * @param level niveau de la tour
	 * @param levelSum somme des différents niveaux ?
	 * @return coût de l'évolution
	 */
	float computeEvolutionCost(int level, int levelSum);
}
