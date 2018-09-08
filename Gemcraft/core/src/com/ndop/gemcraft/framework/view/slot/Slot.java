package com.ndop.gemcraft.framework.view.slot;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ndop.gemcraft.framework.view.panel.MyPanel;

/**
 * Classe représentant un slot de l'inventaire.
 * @author --
 * @version 1.0
 * @param <T>
 */
public abstract class Slot<T> extends Actor{
	/**
	 * Objet stocké.
	 */
	protected T object;
	/**
	 * Inventaire.
	 */
	protected MyPanel panel;
	
	public Slot(T object, MyPanel panel){
		super();
		this.object = object;
		this.panel = panel;
	}
	
	public T getObject(){
		return object;
	}
}
