package com.ndop.gemcraft.framework.model.gemsActions;

import java.util.List;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.IGameInteractions;
import com.ndop.gemcraft.framework.model.MyGridCell;

/**
 * Action basique de la gemme rouge.
 * @author --
 * @version 1.0
 */
public class BasicRedAction extends RedAction{

	@Override
	protected float computeRadius(float nbGem) {
		return nbGem/3;
	}

	@Override
	public void updateAction(int nbGem, boolean isPure, float damage) {
		if(nbGem < 1) return;
			
		float radiusDamage = 0;
		float radius =  computeRadius(nbGem);

		if(isPure){
			radius = radius * 2;
			//radiusDamage = damage;
			radiusDamage = (damage * 75)/100;
		}
		else
			radiusDamage = (damage * 40)/100;

		if((int) radius > 0){
			radiusList.add((int)radius);
			radiusDamageList.add(radiusDamage);
			apply = true;
		}
	}

	@Override
	public void applyToEnemy(Enemy e, IGameInteractions game, float deltaTime) {
		if(apply){
			for(int i=0; i<radiusList.size(); i++){
				float radiusDamage = radiusDamageList.get(i);
				int radius = radiusList.get(i) - 1;
				int index = e.getCellIndex();
				List<MyGridCell> cells = e.getSpawn().getCells();
				
				while(radius >= 0){
					if(radius == 0){
						for(Enemy nei : cells.get(index).getEnemies())
							nei.receiveDamage(radiusDamage);
					}
					else{
						if(index-radius >= 0){
							for(Enemy nei : cells.get(index-radius).getEnemies())
								nei.receiveDamage(radiusDamage);
						}
						if(index+radius < cells.size()){
							for(Enemy nei : cells.get(index+radius).getEnemies())
								nei.receiveDamage(radiusDamage);
						}
					}
					radius--;
				}
			}
			reset();
		}
	}

	@Override
	public void addAction(GemAction action) {
		if(action.apply){
			radiusList.addAll(((RedAction)action).radiusList);
			radiusDamageList.addAll(((RedAction)action).radiusDamageList);
			apply = true;
		}
	}

	@Override
	public void reset() {
		radiusList.clear();
		radiusDamageList.clear();
		apply = false;
	}

}
