package com.ndop.gemcraft.framework.factory;

import java.util.ArrayList;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.ndop.gemcraft.framework.model.Config;

/**
 * Factory de l'objet Config. Elle crée, modifie, enregistre et retourne la configuration.
 * @author ndongmo
 * @see Config
 */
public class ConfigFactory {
	/**
	 * Nom du fichier de configuration.
	 */
	private static final String CONFIG_FILENAME = "configs/config.json";
	/**
	 * Configuration courante.
	 */
	public Config config;
	/**
	 * Liste des langues gérées par le jeu.
	 */
	public ArrayList<Locale> locales;

	public ConfigFactory() {
		locales = new ArrayList<Locale>();
		this.initConfig();
	}

	/**
	 * Charge les langues gérées par le jeu et le fichier de configuration.
	 * @throws FactoryException
	 */
	private void initConfig() {
		/**
		FileHandle fh = Gdx.files.internal("i18n");
		for(FileHandle lg : fh.list(new MyFileFilter("^.*_[a-z]{2}.properties$"))){
			String fileName = lg.nameWithoutExtension();
			Locale l = new Locale(fileName.substring(fileName.length() - 2));
			locales.add(l);
		}

		if(locales.size() < 1)
			throw new FactoryException("Aucun fichier de langue !");
		*/
		/*
		 * On ajoute manuellement car le listing des fichiers internes n'est pas possible !
		 */
		locales.add(new Locale("fr"));
		locales.add(new Locale("en"));
		
		loadConfig();
	}

	/**
	 * Enregistre la configuration courante.
	 */
	public void saveConfig(){
		Json json = new Json();
		json.toJson(config, Gdx.files.local(CONFIG_FILENAME));
	}
	
	/**
	 * Charge le fichier de configuration, 
	 * si ce dernier n'existe pas, la config par défaut sera chargée.
	 */
	public void loadConfig() {
		FileHandle configFile = Gdx.files.local(CONFIG_FILENAME);
		if(configFile.exists()){
			Json json = new Json();
			config = json.fromJson(Config.class, configFile);
			if(config == null){
				defaultConfig();
				new FactoryException("Erreur lors de la lecture du fichier de configuration !");
			}
		}
		else
			defaultConfig();
	}

	/**
	 * Crée la configuration par défaut.
	 */
	public void defaultConfig(){
		config = new Config();
		config.language = locales.get(0).getLanguage();
	}

	/**
	 * Recherche la langue à partir de son code.
	 * @param code code de la langue.
	 * @return locale
	 */
	public Locale findLocale(String code){
		Locale myLocale = locales.get(0);
		for(Locale loc : locales)
			if(loc.getLanguage().equals(new Locale(code).getLanguage())){
				myLocale = loc;
				break;
			}

		return myLocale;
	}
}
