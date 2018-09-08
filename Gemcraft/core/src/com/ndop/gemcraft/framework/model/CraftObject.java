package com.ndop.gemcraft.framework.model;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Objet de craft du jeu.
 * @author --
 * @version 1.0
 */
public abstract class CraftObject extends Sprite{
	
	/**
	 * Calcule le prix de l'objet en fonction du nombre d'objet du même type que possède le joueur.
	 * @param nbObject nombre d'objet du même type
	 * @return prix de l'objet
	 */
	public abstract float computeCost(int nbObject);

}
