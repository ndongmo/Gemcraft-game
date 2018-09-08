package com.ndop.gemcraft.framework.model.gemsActions;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.IGameInteractions;

/**
* Action basique de la gemme violette.
* @author --
* @version 1.0
*/
public class BasicPurpleAction extends PurpleAction{

	@Override
	protected float computeVulnerabilityMult(int nbGem) {
		return (float) Math.pow(1.5, nbGem);
	}

	@Override
	public void updateAction(int nbGem, boolean isPure, float damage) {
		if(nbGem < 1) return;
		
		float vulnerability = computeVulnerabilityMult(nbGem);
		float timer = (isPure) ? 3 : 1;
		
		vulnerabilityMult.add(vulnerability);
		timers.add(timer);
		apply = true;
	}

	@Override
	public void applyToEnemy(Enemy e, IGameInteractions game, float deltaTime) {
		if(apply){
			float initDamage = e.getDamage();
			float damage = initDamage;
			
			for(int i=0; i<vulnerabilityMult.size(); i++){
				float vulnerability = vulnerabilityMult.get(i);
				float timer = timers.get(i);
				
				if(timer > 0){
					damage += initDamage * (vulnerability - 1);
					timer -= deltaTime;
					timers.set(i, timer);
				}
				else {
					vulnerabilityMult.remove(i);
					timers.remove(i);
					i--;
				}
			}
			
			e.setDamage(damage);

			if(vulnerabilityMult.isEmpty()){
				apply = false;
			}
		}
	}

	@Override
	public void addAction(GemAction action) {
		if(action.apply){
			timers.addAll(((PurpleAction)action).timers);
			vulnerabilityMult.addAll(((PurpleAction)action).vulnerabilityMult);
			apply = true;
		}
	}

	@Override
	public void reset() {
		timers.clear();
		vulnerabilityMult.clear();
		apply = false;
	}

}
