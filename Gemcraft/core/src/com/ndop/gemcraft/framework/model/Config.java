package com.ndop.gemcraft.framework.model;


/**
 * Classe de gestion des différents paramètres du jeu.
 * @author --
 * @version 1.0
 */
public class Config {

	/**
	 * Langue.
	 */
	public String language;
	public float soundVolume;
	public float musicVolume;
	
	public Config(){
		language = "";
		soundVolume = 0.5f;
		musicVolume = 0.5f;
	}
}
