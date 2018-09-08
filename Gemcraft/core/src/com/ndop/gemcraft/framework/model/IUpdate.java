package com.ndop.gemcraft.framework.model;

/**
 * Interface de mise à jour des objets dynamique du jeu.
 * @author --
 * @version 1.0
 */
public interface IUpdate {
	/**
	 * Met à jour un objet.
	 * @param deltaTime temps écoulé depuis la dernière mise à jour.
	 */
	public void update(float deltaTime);
}
