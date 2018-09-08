package com.ndop.gemcraft.framework.model.gemsActions;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.IGameInteractions;

/**
 * Action basique de la gemme verte.
 * @author --
 * @version 1.0
 */
public class BasicGreenAction extends GreenAction{

	@Override
	public void updateAction(int nbGem, boolean isPure, float damage) {
		if(nbGem < 1) return;

		float poison = (isPure) ? damage : (damage*2)/10;
		damages.add(poison);
		timers.add(5f);
		apply = true;
	}

	@Override
	public void applyToEnemy(Enemy e, IGameInteractions game, float deltaTime) {
		if(apply){
			for(int i=0; i<damages.size(); i++){
				float damage = damages.get(i);
				float timer = timers.get(i);

				if(timer > 0){
					e.receiveDamage(damage);
					timer -= deltaTime;
					timers.set(i, timer);
				}
				else {
					damages.remove(i);
					timers.remove(i);
					i--;
				}
			}
			if(damages.isEmpty()){
				apply = false;
			}
		}
	}

	@Override
	public void addAction(GemAction action) {
		if(action.apply){
			damages.addAll(((GreenAction)action).damages);
			timers.addAll(((GreenAction)action).timers);
			apply = true;
		}
	}

	@Override
	public void reset() {
		damages.clear();
		timers.clear();
		apply = false;
	}
}
