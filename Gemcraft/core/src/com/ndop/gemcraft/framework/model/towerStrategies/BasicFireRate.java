package com.ndop.gemcraft.framework.model.towerStrategies;

/**
 * Stratégie basique de calcul du débit de tir d'une tour.
 * @author --
 * @version 1.0
 */
public class BasicFireRate implements IFireRate{

	@Override
	public float computeFireRate(int level, int nbGem, int nbLocation) {
		float rate = (nbGem == 0) ? 0 : (0.6f + ((0.1f * level * nbGem) / nbLocation));
		return rate;
	}
}
