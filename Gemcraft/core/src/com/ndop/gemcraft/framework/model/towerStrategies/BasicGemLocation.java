package com.ndop.gemcraft.framework.model.towerStrategies;

/**
 * Stratégie basique de calcul du nombre d'emplacement de gemme d'une tour.
 * @author --
 * @version 1.0
 */
public class BasicGemLocation implements IGemLocation{

	@Override
	public int computeNbLocation(int level) {
		int loc = (int) Math.floor((Math.sqrt(1+8*level)-1)/2);
		return loc;
	}
}
