package com.ndop.gemcraft.framework.model.gemsActions;


/**
 * Action de la gemme orange. Draine la mana de l'ennemi qui recoit le projectile.
 * @author --
 * @version 1.0
 */
public abstract class OrangeAction extends GemAction{
	/**
	 * Mana drainée.
	 */
	protected float drainedMana = 0;
	
	/**
	 * Calcul de la mana drainée.
	 * @param dégâts génèrés par le projectile.
	 */
	protected abstract float computeDrainedMana(float damage);
}
