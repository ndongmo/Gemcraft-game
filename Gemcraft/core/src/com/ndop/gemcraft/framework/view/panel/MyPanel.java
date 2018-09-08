package com.ndop.gemcraft.framework.view.panel;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ndop.gemcraft.framework.view.screen.MyScreen;

/**
 * Classe générique des différents panneaux d'affichage du jeu.
 * Elle herite de Table et est insérée dans la table principale.
 * @author --
 * @version 1.0
 */
public abstract class MyPanel extends Table{
	/**
	 * L'écran dans lequel le panneau est contenu.
	 */
	public final MyScreen parent;
	
	public MyPanel(MyScreen parent){
		super();
		this.parent = parent;
		//this.setDebug(true);
		this.top().left().pad(10);
		this.initPanel();
	}
	
	/**
	 * Initialise les différents composants du panneau.
	 */
	public abstract void initPanel();
	
	/**
	 * Signal le panneau d'un click sur sa zone clickable.
	 */
	public void updatePanel(Actor actor){};
}
