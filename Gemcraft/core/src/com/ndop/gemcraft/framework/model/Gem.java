package com.ndop.gemcraft.framework.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ndop.gemcraft.framework.view.MyMap;


/**
 * Classe représentant une gemme
 * @author --
 *
 */
public class Gem extends CraftObject {
	/**
	 * couleur de la gemme.
	 */
	private GemColor gemColor;
	
	public Gem(){
		gemColor = GemColor.BLUE;
	}
	
	public GemColor getGemColor() {
		return gemColor;
	}

	public void setGemColor(GemColor gc) {
		this.gemColor = gc;
		int x = (gemColor.ordinal()) * MyMap.TILE_SIZE;
		int y = 0;
		int w = MyMap.TILE_SIZE;
		int h = MyMap.TILE_SIZE;
		this.setRegion(new TextureRegion(ResourceManager.texture, x, y, w, h));
		this.setSize(MyMap.TILE_SIZE, MyMap.TILE_SIZE);
	}
	
	public String toString(){
		return "Gem : " + gemColor.toString();
	}

	@Override
	public float computeCost(int nbObject) {
		float cost = (float) (10 * Math.pow(1.15, nbObject));
		return cost;
	}
}
