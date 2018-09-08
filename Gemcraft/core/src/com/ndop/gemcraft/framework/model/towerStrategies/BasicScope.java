package com.ndop.gemcraft.framework.model.towerStrategies;

/**
* Stratégie basique de calcul de la portée de tir d'une tour.
* @author --
* @version 1.0
*/
public class BasicScope implements IScope{

	@Override
	public int computeScope(int level) {
		int scope = (int) (1.5 + Math.log(level));
		return scope;
	}
	
}
