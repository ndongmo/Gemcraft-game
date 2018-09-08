package com.ndop.gemcraft.framework.model;

import com.badlogic.gdx.graphics.Color;


/**
 * Enumération des couleurs de gemmes. Retourne la couleur de la gemme désirée.
 * @author --
 * @version 1.0
 */
public enum GemColor {
	
	RED(255, 0, 0),
	ORANGE(255, 165, 0),
	YELLOW(255, 255, 0),
	GREEN(0, 255, 0),
	CYAN(0, 255, 255),
	BLUE(0, 0, 255),
	MAGENTA(255, 0, 255),
	PURPLE(128, 0, 128);
	
	public Color color;
	
	GemColor(float r, float g, float b){
		color = new Color(r, g, b, 1);
	}
}
