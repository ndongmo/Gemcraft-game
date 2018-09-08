package com.ndop.gemcraft.framework.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe représentant une vague d'ennemis groupés.
 * @author --
 * @version 1.0
 */
public class Wave {

	/**
	 * Description de la vague.
	 */
	private String description;
	/**
	 * Temps entre le début du niveau et l'entrée de la vague courante. 
	 */
	private float delay;
	/**
	 * Groupe d'ennemis.
	 */
	private ArrayList<Group> groups;

	public Wave(String description, float delay){
		this.delay = delay;
		this.description = description;
		this.groups = new ArrayList<Group>();
	}

	public boolean isAvailable(float elapsedTime){
		return elapsedTime > delay;
	}

	public ArrayList<Enemy> getEnemies(float elapsedTime){
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		if(isAvailable(elapsedTime)){
			Iterator<Group> it = groups.iterator();
			while(it.hasNext()){
				Group group = it.next();
				
				if(group.getEnemies().isEmpty()){
					it.remove();
				}
				else if(group.isAvailable(elapsedTime)){
					enemies.add(group.getEnemy(elapsedTime));
				}
			}
		}
		return enemies;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getDelay() {
		return delay;
	}

	public void setDelay(float delay) {
		this.delay = delay;
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public void addGroup(Group group) {
		this.groups.add(group);
	}

}
