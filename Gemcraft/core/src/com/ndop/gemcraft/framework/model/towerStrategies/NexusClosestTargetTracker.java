package com.ndop.gemcraft.framework.model.towerStrategies;

import java.util.ArrayList;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.MyGridCell;
import com.ndop.gemcraft.framework.model.ResourceManager;

/**
 * Stratégie de choix du prochain ennemi à attaquer d'une tour. 
 * Cette stratégie recherche l'ennemi le plus proche du nexus.
 * @author --
 * @version 1.0
 */
public class NexusClosestTargetTracker extends ITargetTracker{

	@Override
	public Enemy trackTarget(ArrayList<MyGridCell> cells) {
		Enemy e = null;
		int closest = -1;

		for(MyGridCell cell : cells){
			if(cell.getEnemies().size() > 0 && cell.getPosition() > closest){
				e = cell.getEnemies().get(0);
				closest = cell.getPosition();
			}
		}

		return e;
	}

	@Override
	public String toString(){
		return ResourceManager.bundle.get("ennemiProcheNexus");
	}

}
