package com.ndop.gemcraft.framework.model;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * Interface d'interaction du jeu avec les objets dynamiques.
 * @author --
 * @version 1.0
 */
public interface IGameInteractions {
	/**
	 * Ajoute un projectile dans la liste des projectiles affichés.
	 * @param bullet projectile
	 */
	public void addBullet(Bullet bullet);
	/**
	 * Obtient la liste des ennemis sur la carte.
	 * @return
	 */
	public ArrayList<Enemy> getEnemies();
	/**
	 * Obtient la liste des sentiers sur la carte.
	 * @return
	 */
	public ArrayList<Spawn> getSpawns();
	/**
	 * Obtient le joueur en cours.
	 * @return
	 */
	public Player getPlayer();
	/**
	 * Le conteneur du nexus.
	 * @return
	 */
	public Rectangle getNexus();
	/**
	 * Obtient le gestionnaire des projectile.
	 * @return
	 */
	public Pool<Bullet> getBulletPool();
}
