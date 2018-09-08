package com.ndop.gemcraft.framework.model.gemsActions;

import java.util.Random;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.IGameInteractions;

/**
 * Action basique de la gemme jaune.
 * @author --
 * @version 1.0
 */
public class BasicYellowAction extends YellowAction{

	@Override
	protected float computeMultDamage(float nbGem) {
		return 100 * (Math.min(1,  (10 * nbGem)/100));
	}

	@Override
	public void updateAction(int nbGem, boolean isPure, float damage) {
		if(nbGem < 1) return;

		Random random = new Random();
		float proba = computeMultDamage(nbGem);
		float value = random.nextInt(100);
		if(value <= proba){
			damage *= (isPure)? 4 : 2;
			apply = true;
		}
	}

	@Override
	public void applyToEnemy(Enemy e, IGameInteractions game, float deltaTime) {
		if(apply){
			e.receiveDamage(damage);
			reset();
		}
	}

	@Override
	public void addAction(GemAction action) {
		if(action.apply){
			damage += ((YellowAction)action).damage;
			apply = true;
		}
	}

	@Override
	public void reset() {
		damage = 0;
		apply = false;
	}

}
