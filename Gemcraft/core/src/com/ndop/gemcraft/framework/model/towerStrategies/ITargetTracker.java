package com.ndop.gemcraft.framework.model.towerStrategies;

import java.util.ArrayList;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.MyGridCell;

/**
 * Stratégie de choix du prochain ennemi à attaquer d'une tour.
 * @author --
 * @version 1.0
 */
public abstract class ITargetTracker {
	/**
	 * Obtient le prochain ennemi à attaquer.
	 * @param scope porté de la tour
	 * @return Enemy l'ennemi choisi
	 */
	public abstract Enemy trackTarget(ArrayList<MyGridCell> cells);
}
