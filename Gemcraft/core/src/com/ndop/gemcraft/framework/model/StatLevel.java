package com.ndop.gemcraft.framework.model;

/**
 * Cette classe enregistre les statistiques d'un joueur pour un niveau donné du jeu.
 * @author --
 * @version 1.0
 */
public class StatLevel implements Comparable<StatLevel>{
	/**
	 * Index du niveau.
	 */
	private int index;
	/**
	 * Nom du niveau.
	 */
	private String name;
	/**
	 * Temps mis par le joueur pour terminer le niveau.
	 */
	private float time;
	
	public StatLevel(int index, String name, float time){
		this.index = index;
		this.name = name;
		this.time = time;
	}
	
	@Override
	public int compareTo(StatLevel sl) {
		if(index == sl.index && name.equals(sl.name))
			return (time > sl.time) ? 1 : (time < sl.time) ? -1 : 0;
		return (index > sl.index) ? 1 : -1;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public float getTime() {
		return time;
	}
	
	public void setTime(float time){
		this.time = time;
	}
	
}
