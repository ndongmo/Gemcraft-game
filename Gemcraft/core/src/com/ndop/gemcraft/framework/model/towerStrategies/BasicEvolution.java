package com.ndop.gemcraft.framework.model.towerStrategies;

/**
 * Stratégie basique de calcul du coûts d'évolution d'une tour.
 * @author --
 * @version 1.0
 */
public class BasicEvolution implements IEvolution{

	@Override
	public float computeEvolutionCost(int level, int levelSum) {
		float cost = (float) (100 * Math.pow(1.5, level) * (9 + levelSum) / 10);
		return cost;
	}

}
