package com.ndop.gemcraft.framework.model.towerStrategies;

import java.util.ArrayList;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.MyGridCell;
import com.ndop.gemcraft.framework.model.ResourceManager;

/**
 * Stratégie de choix du prochain ennemi à attaquer d'une tour. 
 * Cette stratégie recherche l'ennemi ayant le plus d'énergie.
 * @version 1.0
 */
public class MostEnergyTargetTracker extends ITargetTracker{

	@Override
	public Enemy trackTarget(ArrayList<MyGridCell> cells) {
		Enemy e = null;
		
		for(MyGridCell cell : cells){
			for(Enemy current : cell.getEnemies()){
				if(e == null || e.getMana() < current.getMana())
					e = current;
			}
		}
		
		return e;
	}
	
	@Override
	public String toString(){
		return ResourceManager.bundle.get("ennemiPlusEnergie");
	}

}
