package com.ndop.gemcraft.framework.model.towerStrategies;

import java.util.ArrayList;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.MyGridCell;
import com.ndop.gemcraft.framework.model.ResourceManager;

/**
 * Stratégie de choix du prochain ennemi à attaquer d'une tour. 
 * Cette stratégie recherche l'ennemi le plus rapide.
 * @version 1.0
 */
public class FastestTargetTracker extends ITargetTracker{

	@Override
	public Enemy trackTarget(ArrayList<MyGridCell> cells) {
		Enemy e = null;
		
		for(MyGridCell cell : cells){
			for(Enemy current : cell.getEnemies()){
				if(e == null || e.getRealVelocity() < current.getRealVelocity())
					e = current;
			}
		}
		
		return e;
	}

	@Override
	public String toString(){
		return ResourceManager.bundle.get("ennemiPlusRapide");
	}

}
