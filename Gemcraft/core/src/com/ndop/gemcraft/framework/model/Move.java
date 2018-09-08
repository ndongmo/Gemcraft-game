package com.ndop.gemcraft.framework.model;

/**
 * Différents mouvements
 * @author --
 * @version 1.0
 */
public enum Move {

	DOWN(0, -1),
	LEFT(-1, 0),
	RIGHT(1, 0),
	UP(0, 1);
	
	public int x, y;
	
	Move(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public static Move getMove(int x, int y){
		if(x > 0)
			return RIGHT;
		else if( x < 0)
			return LEFT;
		else if( y > 0)
			return UP;
		else if( y < 0)
			return DOWN;
		return LEFT;
	}
}
