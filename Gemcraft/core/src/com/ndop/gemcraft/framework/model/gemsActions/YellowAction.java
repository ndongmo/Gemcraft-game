package com.ndop.gemcraft.framework.model.gemsActions;


/**
 * Action de la gemme jaune (coups critiques). Lorsqu'un tir d'une tour ́equipée d'au moins une gemme jaune inflige des
 * dégâts à un ennemi, il y a une certaine probabilité que la quantité de dégâts soit triplée.
 * @author --
 * @version 1.0
 */
public abstract class YellowAction extends GemAction{
	/**
	 * Dégâts.
	 */
	protected float damage = 0;
	/**
	 * Calcul le pourcentage de multiplication des dégâts.
	 * @param nbGem nombre de gemme.
	 * @return pourcentage de multiplication des dégâts.
	 */
	protected abstract float computeMultDamage(float nbGem);
}
