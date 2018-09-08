package com.ndop.gemcraft.framework.model.towerStrategies;

import java.util.ArrayList;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.MyGridCell;
import com.ndop.gemcraft.framework.model.ResourceManager;

/**
 * Stratégie de choix du prochain ennemi à attaquer d'une tour. 
 * Cette stratégie recherche l'ennemi ayant le plus d'ennemis proches.
 * @author --
 * @version 1.0
 */
public class EnemiesClosestTargetTracker extends ITargetTracker {

	@Override
	public Enemy trackTarget(ArrayList<MyGridCell> cells) {
		Enemy e = null;
		int max = 0;
		
		for(MyGridCell cell : cells){
			if(cell.getEnemies().size() > max){
				e = cell.getEnemies().get(0);
			}
		}
		
		return e;
	}
	
	@Override
	public String toString(){
		return ResourceManager.bundle.get("ennemiPlusEnnemieProche");
	}

}
