package com.ndop.gemcraft.framework.model;

import com.ndop.gemcraft.framework.model.gemsActions.BasicBlueAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicCyanAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicGreenAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicMagentaAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicOrangeAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicPurpleAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicRedAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicYellowAction;
import com.ndop.gemcraft.framework.model.gemsActions.GemAction;


/**
 * Classe représentant un piège du jeu. Le piège peut être déposé sur un sentier.
 * @author --
 * @version 1.0
 */
public class Trap extends Tower{
	/**
	 * Cellule courante où se trouve la tour.
	 */
	protected MyGridCell cell;
	
	public Trap(){
		super(7, 5);
	}
	
	public void updateClosestCells(MyGridCell cell, IGameInteractions game){
		this.cell = cell;
	}
	
	public void update(float deltaTime, IGameInteractions game) {
		currentSleepTime -= deltaTime;
		waitTime -= deltaTime;
		
		if(! isAsleep() && waitTime <= 0 && gems.size() > 0 && cell.getEnemies().size() > 0){
			float damage = this.damage.computeDamage(level) / 5;
			
			GemAction[] actions = getActions();
			
			for(int i=0; i<actions.length; i++){
				GemAction a = actions[i];
				a.updateAction(nbGems[i], isPure, damage);
			}
			
			if(isPure && nbGems[GemColor.CYAN.ordinal()] > 1)
				damage = 0;
			
			for(Enemy e : cell.getEnemies()){
				for(int i=0; i<actions.length; i++){
		    		if(!e.getImmunity()[i]){
		    			e.getActions()[i].addAction(actions[i]);
		    			e.receiveDamage(damage);
		    		}
		    	}
			}
			
			waitTime = fireRate.computeFireRate(level, gems.size(), gemLoc.computeNbLocation(level)) ;
		}
	}
	
	public Tower createNew(){
		return ResourceManager.levelFactory.createTrap();
	}
	
	private GemAction[] getActions(){
		GemAction[] actions = new GemAction[GemColor.values().length];
		actions[GemColor.RED.ordinal()] = new BasicRedAction();
		actions[GemColor.ORANGE.ordinal()] = new BasicOrangeAction();
		actions[GemColor.YELLOW.ordinal()] = new BasicYellowAction();
		actions[GemColor.GREEN.ordinal()] = new BasicGreenAction();
		actions[GemColor.CYAN.ordinal()] = new BasicCyanAction();
		actions[GemColor.BLUE.ordinal()] = new BasicBlueAction();
		actions[GemColor.MAGENTA.ordinal()] = new BasicMagentaAction();
		actions[GemColor.PURPLE.ordinal()] = new BasicPurpleAction();
		return actions;
	}
	
	@Override
	public float computeCost(int nbObject) {
		float cost = (float) (100 * Math.pow(1.23, nbObject));
		return cost;
	}
}
