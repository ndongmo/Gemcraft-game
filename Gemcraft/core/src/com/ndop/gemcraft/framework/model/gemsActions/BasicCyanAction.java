package com.ndop.gemcraft.framework.model.gemsActions;

import java.util.Random;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.GemColor;
import com.ndop.gemcraft.framework.model.IGameInteractions;

/**
 * Action basique de la gemme cyan.
 * @author --
 * @version 1.0
 */
public class BasicCyanAction extends CyanAction{

	@Override
	protected float computeProbaParalysis(float nbGem) {
		return 100 * Math.min(1, (float)(0.08 + (0.06 * nbGem)));
	}

	@Override
	public void updateAction(int nbGem, boolean isPure, float damage) {
		if(nbGem < 1) return;

		if(isPure){
			timer = nbGem/2;
			apply = true;
		}
		else{
			Random random = new Random();
			float proba = computeProbaParalysis(nbGem);
			if(proba >= random.nextInt(100)){
				timer = 1;
				apply = true;
			}
		}
	}

	@Override
	public void applyToEnemy(Enemy e, IGameInteractions game, float deltaTime) {
		if(apply){
			if(timer > 0){
				e.setCurrentVelocity(0);
				timer -= deltaTime;
			}
			else {
				timer = 0;
				if(! e.getActions()[GemColor.BLUE.ordinal()].apply)
					e.setCurrentVelocity(e.getRealVelocity());
				reset();
			}
		}
	}

	@Override
	public void addAction(GemAction action) {
		if(action.apply){
			timer = ((CyanAction)action).timer;
			apply = true;
		}
	}

	@Override
	public void reset() {
		timer = 0;
		apply = false;
	}

}
