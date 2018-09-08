package com.ndop.gemcraft.framework.model;

import java.util.ArrayList;

/**
 * Classe représentant un groupe d'ennemis.
 * @author --
 * @version 1.0
 */
public class Group {
	/**
	 * Sentier d'entrée des ennemis du groupe.
	 */
	private Spawn spawn;
	/**
	 * Intervalle d'apparition des ennemis du groupe.
	 */
	private float lapse;
	/**
	 * Temps entre le début de la vague et l'apparition du prochain ennemi.
	 */
	private float timer;
	/**
	 * Liste des ennemis du groupe.
	 */
	private ArrayList<Enemy> enemies;
	
	public Group(Spawn spawn, float lapse, float timer){
		this.spawn = spawn;
		this.lapse = lapse;
		this.timer = timer;
		this.enemies = new ArrayList<Enemy>();
	}
	
	public Enemy getEnemy(float elapsedTime){
		timer += lapse;
		Enemy e = enemies.remove(0);
		e.initPosition();
		return e;
	}
	
	public boolean isAvailable(float elapsedTime){
		return elapsedTime > timer;
	}

	public Spawn getSpawn() {
		return spawn;
	}

	public void setSpawn(Spawn spawn) {
		this.spawn = spawn;
	}

	public float getLapse() {
		return lapse;
	}

	public void setLapse(float lapse) {
		this.lapse = lapse;
	}

	public float getTimer() {
		return timer;
	}

	public void setTimer(float timer) {
		this.timer = timer;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}
	
	public void addEnemy(Enemy e){
		e.setSpawn(spawn);
		enemies.add(e);
	}
	
	public String toString(){
		return "Spawn = ["+spawn+"]; lapse = "+lapse+"; timer = "+timer;
	}
}
