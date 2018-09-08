package com.ndop.gemcraft.framework.view;

public interface IGameBoard {
	/**
	 * Met à jour la barre de progression de la vie.
	 * @param life la vie courante du joueur.
	 */
	public void updateLife(float life);
	/**
	 * Met à jour la barre de progression de la mana.
	 * @param mana la mana courante du joueur.
	 */
	public void updateMana(float mana);
}
