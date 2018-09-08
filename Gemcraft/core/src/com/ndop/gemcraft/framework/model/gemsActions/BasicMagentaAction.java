package com.ndop.gemcraft.framework.model.gemsActions;

import java.util.ArrayList;
import java.util.Random;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.IGameInteractions;

/**
* Action basique de la gemme magenta.
* @author --
* @version 1.0
*/
public class BasicMagentaAction extends MagentaAction{

	@Override
	protected float computeStringProba(int nbGem) {
		return 8 * nbGem;
	}

	@Override
	public void updateAction(int nbGem, boolean isPure, float damage) {
		if(nbGem < 1) return;
		
		Random random = new Random();
		float proba = computeStringProba(nbGem);
		if(proba >= random.nextInt(100)){
			damage *= (isPure) ? 0.99f : 0.8f;
			damages.add(damage);
			apply =true;
		}
	}

	@Override
	public void applyToEnemy(Enemy e, IGameInteractions game, float deltaTime) {
		if(apply){
			ArrayList<Enemy> enemies = e.getSpawn().getCells().get(e.getCellIndex()).getEnemies();
			
			if(enemies.size() > 1){
				Random random = new Random();
				for(float damage : damages){
					int index = e.getCellIndex();
					while(index == e.getCellIndex())
						index = random.nextInt(enemies.size());
					enemies.get(index).receiveDamage(damage);
				}
				damages.clear();
			}
			apply = false;
		}
	}

	@Override
	public void addAction(GemAction action) {
		if(action.apply){
			damages.addAll(((MagentaAction)action).damages);
			apply = true;
		}
	}

	@Override
	public void reset() {
		damages.clear();
		apply = false;
	}

}
