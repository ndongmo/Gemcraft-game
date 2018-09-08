package com.ndop.gemcraft.framework.view;

import com.badlogic.gdx.Game;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.view.screen.MainMenuScreen;

/**
 * Classe d'initialisation du jeu. 
 * @author --
 * @version 1.0
 */
public class MyGemcraft extends Game {

	public void create () {
		ResourceManager.load();
		this.setScreen(new MainMenuScreen(this));
	}
	
	public void dispose(){
		ResourceManager.dispose();
	}
}
