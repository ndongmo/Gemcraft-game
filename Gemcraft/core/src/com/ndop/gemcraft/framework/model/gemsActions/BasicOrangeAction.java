package com.ndop.gemcraft.framework.model.gemsActions;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.IGameInteractions;
import com.ndop.gemcraft.framework.model.Player;

/**
 * Action basique de la gemme orange.
 * @author --
 * @version 1.0
 */
public class BasicOrangeAction extends OrangeAction{

	@Override
	protected float computeDrainedMana(float damage) {
		return damage/10;
	}

	@Override
	public void updateAction(int nbGem, boolean isPure, float damage) {
		if(nbGem < 1) return;
		
		float bonus = computeDrainedMana(damage);
		bonus *= (isPure) ? 5 : nbGem;
		drainedMana = bonus;
		apply = true;
	}

	@Override
	public void applyToEnemy(Enemy e, IGameInteractions game, float deltaTime) {
		if(apply){
			Player player = game.getPlayer();
			player.setMana(player.getMana() + drainedMana);
			reset();
		}
	}

	@Override
	public void addAction(GemAction action) {
		if(action.apply){
			drainedMana += ((OrangeAction)action).drainedMana;
			apply = true;
		}
	}

	@Override
	public void reset() {
		drainedMana = 0;
		apply = false;
	}

}
