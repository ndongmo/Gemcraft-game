package com.ndop.gemcraft.framework.model;

import java.util.ArrayList;

import org.xguzm.pathfinding.grid.GridCell;

public class MyGridCell extends GridCell{
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private int position = 0;
	
	public MyGridCell(int x, int y, boolean isWalkable){
		super(x, y, isWalkable);
	}
	
	public ArrayList<Enemy> getEnemies(){
		return enemies;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
