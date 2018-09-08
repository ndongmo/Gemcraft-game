package com.ndop.gemcraft.framework.factory;

import java.util.Comparator;

import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.Gem;
import com.ndop.gemcraft.framework.model.GemColor;
import com.ndop.gemcraft.framework.model.Spawn;
import com.ndop.gemcraft.framework.model.Tower;
import com.ndop.gemcraft.framework.model.Trap;

/**
 * Interface de création des différents éléments du jeu.
 * @author --
 * @version 1.0
 */
public interface ILevelFactory {
	/**
	 * Création d'une gemme.
	 * @param gc couleur de la gemme.
	 * @return nouvelle gemme créée.
	 * @throws FactoryException
	 */
	public Gem createGem(String gc) throws FactoryException;
	public Gem createGem(GemColor gc);
	/**
	 * Création d'un sentier.
	 * @param name nom du sentier.
	 * @param x coordonnée x.
	 * @param y  coordonnée y.
	 * @return le sentier créé
	 */
	public Spawn createSpawn(String name, int x, int y);
	/**
	 * Création d'un piège.
	 * @return le piège créé.
	 */
	public Trap createTrap();
	/**
	 * Création d'une tour.
	 * @return la tour créée.
	 */
	public Tower createTower();
	/**
	 * Création d'un ennemi.
	 * @param name nom de l'ennemi.
	 * @param immunity liste des immunités sous forme de chaine de caractère de l'ennemi.
	 * @param speed vitesse de l'ennemi.
	 * @param size taille de l'ennemi.
	 * @param sprite image correspondant à l'ennemi.
	 * @param life points de vie de l'ennemi.
	 * @param mana points de mana de l'ennemi.
	 * @return l'enemie créé.
	 * @throws FactoryException
	 */
	public Enemy createEnemy(String name, String immunity, float speed, 
			float size, String sprite, float life, float mana) throws FactoryException;	
	/**
	 * Obtient un comparateur d'ennemis à partir des points de vie.
	 * @return le comparateur en question.
	 */
	public Comparator<Enemy> lifeComparator();
}
