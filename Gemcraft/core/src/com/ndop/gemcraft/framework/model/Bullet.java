package com.ndop.gemcraft.framework.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
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
 * Classe représentant un projectile tiré par une tour.
 * @author --
 * @version 1.0
 */
public class Bullet implements Poolable{
	/**
	 * Position du projectile.
	 */
	private Vector2 position;
	/**
	 * Vitesse du projectile.
	 */
	private Vector2 velocity;
	private float damage;
	private float speed;
	private float manaScalar;
	/**
	 * Etat du projectile.
	 */
	private boolean alive;
	/**
	 * Cible.
	 */
	private Enemy target;
	/**
	 * Actions à infliger par le projectile.
	 */
	private GemAction[] actions = new GemAction[GemColor.values().length];
	
	public Bullet(){
		speed = 200;
		manaScalar = 1.0f/10;
		velocity = new Vector2();
		
		actions[GemColor.RED.ordinal()] = new BasicRedAction();
		actions[GemColor.ORANGE.ordinal()] = new BasicOrangeAction();
		actions[GemColor.YELLOW.ordinal()] = new BasicYellowAction();
		actions[GemColor.GREEN.ordinal()] = new BasicGreenAction();
		actions[GemColor.CYAN.ordinal()] = new BasicCyanAction();
		actions[GemColor.BLUE.ordinal()] = new BasicBlueAction();
		actions[GemColor.MAGENTA.ordinal()] = new BasicMagentaAction();
		actions[GemColor.PURPLE.ordinal()] = new BasicPurpleAction();
	}
	
	public void initBullet(Vector2 position, float damage, Enemy target){
		this.alive = true;
		this.position = position;
		this.damage = damage;
		this.target = target;
	}
	
	public void update(float deltaTime, Player player) {
		Vector2 pos = new Vector2(target.getBound().x+(target.getBound().width/2), target.getBound().y+(target.getBound().height/2));
		velocity.set(pos.x - position.x, pos.y - position.y).nor().scl(speed);
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
	    
	    if(target.isAlive() && target.getBound().contains(position)){
	    	for(int i=0; i<actions.length; i++){
	    		if(!target.getImmunity()[i]){
	    			target.getActions()[i].addAction(actions[i]);
	    		}
	    	}
	    	target.receiveDamage(damage);
	    	player.setMana(player.getMana() + (damage*manaScalar));
	    	alive = false;
	    }
	    else if(! target.isAlive())
	    	alive = false;
	}

	@Override
	public void reset() {
		alive = false;
		for(GemAction a : actions)
			a.reset();
	}

	public Vector2 getPosition() {
		return position;
	}

	public boolean isAlive() {
		return alive;
	}

	public GemAction[] getActions() {
		return actions;
	}
	
}
