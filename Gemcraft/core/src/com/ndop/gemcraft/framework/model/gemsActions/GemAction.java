package com.ndop.gemcraft.framework.model.gemsActions;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.IGameInteractions;

/**
 * Représente une action d'un gemme, elle visite un ennemi afin de lui appliquer son effet.
 * @author --
 * @version 1.0
 */
public abstract class GemAction {
	
	protected boolean apply = false;
	
	public abstract void updateAction(int nbGem, boolean isPure, float damage);
	public abstract void applyToEnemy(Enemy e, IGameInteractions game, float deltaTime);
	public abstract void addAction(GemAction action);
	public abstract void reset();
}
