package com.ndop.gemcraft.framework.model;

import java.util.ArrayList;

import com.ndop.gemcraft.framework.view.IGameBoard;


/**
 * Classe représentant un joueur.
 * @author --
 * @version 1.0
 */
public class Player {
	/**
	 * Vie et mana du joueur.
	 */
	private float life, mana;
	/**
	 * Niveau actuel du joueur.
	 */
	private int currentLevel;
	/**
	 * Nom du joueur.
	 */
	private String name;
	/**
	 * Liste des statistiques du joueur.
	 */
	private ArrayList<StatLevel> stats;
	/**
	 * Panneau d'affichage des paramàtres.
	 */
	private IGameBoard board;
	
	public Player(IGameBoard board){
		this.board = board;
		this.name = "";
		this.currentLevel = 1;
		this.stats = new ArrayList<StatLevel>();
	}
	
	public Player(String name, int curretLevel){
		this.name = name;
		this.currentLevel = curretLevel;
		this.stats = new ArrayList<StatLevel>();
	}
	
	public void initPlayer(float life, float mana){
		this.setLife(life);
		this.setMana(mana);
	}
	
	public void updateLevel(float time){
		if(stats.size() >= currentLevel){
			if(stats.get(currentLevel-1).getTime() > time)
				stats.get(currentLevel-1).setTime(time);
		}
		else
			stats.add(new StatLevel(currentLevel, name, time));
		
		if(ResourceManager.levelFactory.hasNext(currentLevel))
			currentLevel++;
		else
			currentLevel = 1;	
	}
	
	public float getAllTime(){
		float time = 0;
		for(StatLevel stat : stats)
			time += stat.getTime();
		return time;
	}

	public String getName() {
		return name;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public void setStats(ArrayList<StatLevel> stats) {
		this.stats = stats;
	}

	public ArrayList<StatLevel> getStats() {
		return stats;
	}

	public void setBoard(IGameBoard board) {
		this.board = board;
	}

	public float getLife() {
		return life;
	}

	public void setLife(float life) {
		this.life = life;
		this.board.updateLife(life);
	}

	public float getMana() {
		return mana;
	}

	public void setMana(float mana) {
		this.mana = mana;
		this.board.updateMana(mana);
	}
	
	public boolean isAlive(){
		return life > 0;
	}
	
	public boolean equals(Object o){
		Player p = (Player)o;
		return name.equals(p.name);
	}
	
	public String toString(){
		return name+"["+stats.size()+"]";
	}

}
