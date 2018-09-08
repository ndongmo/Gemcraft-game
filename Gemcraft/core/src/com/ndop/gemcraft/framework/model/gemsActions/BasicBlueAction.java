package com.ndop.gemcraft.framework.model.gemsActions;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.GemColor;
import com.ndop.gemcraft.framework.model.IGameInteractions;

/**
 * Action basique de la gemme bleu.
 * @author --
 * @version 1.0
 */
public class BasicBlueAction extends BlueAction{

	@Override
	protected float computeSlowDownMult(int nbGem) {
		return 0.75f * nbGem;
	}

	@Override
	public void updateAction(int nbGem, boolean isPure, float damage) {
		if(nbGem < 1) return;

		float mult = computeSlowDownMult(nbGem);
		float timer = (isPure) ? 3 : 1;
		timers.add(timer);
		slowDownMult.add(mult);
		apply = true;
	}

	@Override
	public void applyToEnemy(Enemy e, IGameInteractions game, float deltaTime) {
		if(apply){
			for(int i=0; i<slowDownMult.size(); i++){
				float speed = slowDownMult.get(i) * e.getRealVelocity();
				float timer = timers.get(i);

				if(timer > 0){
					if(! e.getActions()[GemColor.CYAN.ordinal()].apply && speed < e.getCurrentVelocity())
						e.setCurrentVelocity(speed);
					timer -= deltaTime;
					timers.set(i, timer);
				}
				else {
					slowDownMult.remove(i);
					timers.remove(i);
					i--;
				}
			}

			if(slowDownMult.isEmpty()){
				if(! e.getActions()[GemColor.CYAN.ordinal()].apply)
					e.setCurrentVelocity(e.getRealVelocity());
				reset();
			}
		}
	}

	@Override
	public void addAction(GemAction action) {
		if(action.apply){
			timers.addAll(((BlueAction)action).timers);
			slowDownMult.addAll(((BlueAction)action).slowDownMult);
			apply = true;
		}
	}

	@Override
	public void reset() {
		timers.clear();
		slowDownMult.clear();
		apply = false;
	}

}
