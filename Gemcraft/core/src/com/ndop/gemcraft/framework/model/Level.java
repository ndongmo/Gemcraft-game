package com.ndop.gemcraft.framework.model;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;

/**
 * Classe représentant un niveau du jeu.
 * @author --
 * @version 1.0
 */
public class Level { 
	/**
	 * Nom du fichier et nom du niveau.
	 */
	private String fileName, name;
	/**
	 * Vie et mana initial du joueur.
	 */
	private float life, mana;
	/**
	 * nexus.
	 */
	private Rectangle nexus;
	/**
	 * nom de la carte.
	 */
	private String mapName;
	/**
	 * liste des gemmes initialles du joueur.
	 */
	private ArrayList<Gem> gems;
	/**
	 * Liste des vagues d'ennemis du niveau.
	 */
	private ArrayList<Wave> waves;
	/**
	 * Liste des sentiers du niveau.
	 */
	private ArrayList<Spawn> spawns;
	/**
	 * Liste des tours du niveau.
	 */
	private ArrayList<Tower> towers;
	
	public Level(){
		gems = new ArrayList<Gem>();
		waves = new ArrayList<Wave>();
		spawns = new ArrayList<Spawn>();
		towers = new ArrayList<Tower>();
		nexus = new Rectangle();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getLife() {
		return life;
	}

	public void setLife(float life) {
		this.life = life;
	}

	public float getMana() {
		return mana;
	}

	public void setMana(float mana) {
		this.mana = mana;
	}

	public Rectangle getNexus() {
		return nexus;
	}

	public void setNexus(Rectangle nexus) {
		this.nexus = nexus;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public ArrayList<Gem> getGems() {
		return gems;
	}

	public void addGem(Gem gem) {
		this.gems.add(gem);
	}
	
	public void addTower(Tower tower) {
		this.towers.add(tower);
	}

	public ArrayList<Wave> getWaves() {
		return waves;
	}

	public void addWave(Wave wave) {
		this.waves.add(wave);
	}

	public ArrayList<Spawn> getSpawns() {
		return spawns;
	}

	public void addSpawn(Spawn spawn) {
		this.spawns.add(spawn);
	}

	public ArrayList<Tower> getTowers() {
		return towers;
	}

	public void setTowers(ArrayList<Tower> towers) {
		this.towers = towers;
	}

}
