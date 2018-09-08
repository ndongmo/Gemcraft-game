package com.ndop.gemcraft.framework.model.towerStrategies;

public class BasicBuyable implements IBuyable{

	@Override
	public float computeCost(int nbTower) {
		float cost = (float) (100 * Math.pow(1.15, nbTower));
		return cost;
	}

}
