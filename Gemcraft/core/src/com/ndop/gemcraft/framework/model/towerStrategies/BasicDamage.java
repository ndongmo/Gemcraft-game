package com.ndop.gemcraft.framework.model.towerStrategies;

/**
 * Stratégie basique de calcul des dégats d'une tour.
 * @author --
 * @version 1.0
 */
public class BasicDamage implements IDamage{

	@Override
	public float computeDamage(int level) {
		float damage = (float) (50 * Math.pow(1.5, (double)level));
		return damage;
	}

}
