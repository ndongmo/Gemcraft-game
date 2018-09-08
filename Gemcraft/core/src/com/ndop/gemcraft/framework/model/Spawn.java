package com.ndop.gemcraft.framework.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * Classe représentant un sentier, point d'entrée des enemies sur la carte.
 * @author --
 * @version 1.0
 */
public class Spawn {
	/**
	 * Nom du sentier.
	 */
	private String name;
	/**
	 * Origine du sentier sur la carte.
	 */
	private Vector2 origin;
	
	private List<MyGridCell> cells = new ArrayList<MyGridCell>();
	
	public Spawn(String name, Vector2 origin){
		this.name = name;
		this.origin = origin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector2 getOrigin() {
		return origin;
	}

	public void setOrigin(Vector2 origin) {
		this.origin = origin;
	}
	
	public List<MyGridCell> getCells() {
		return cells;
	}

	public void setCells(List<MyGridCell> cells) {
		this.cells.clear();
		for(int i=0; i<cells.size(); i++){
			MyGridCell cell = cells.get(i);
			cell.setPosition(i);
			this.cells.add(cell);
		}
	}

	public boolean equals(Object spawn){
		return name.equals(((Spawn)spawn).name);
	}
	
	public String toString(){
		return "Nom = "+name+"; vector = "+origin;
	}
	
}
