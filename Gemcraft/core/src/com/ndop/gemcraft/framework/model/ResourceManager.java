package com.ndop.gemcraft.framework.model;

import java.util.Locale;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader.I18NBundleParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.ndop.gemcraft.framework.factory.ConfigFactory;
import com.ndop.gemcraft.framework.factory.LevelFactory;
import com.ndop.gemcraft.framework.factory.PlayerFactory;

public class ResourceManager {

	/**
	 * Gestionaire des Assets.
	 */
	public static AssetManager manager = new AssetManager();
	/**
	 * Factory de configuration des paramètres du jeu.
	 */
	public static ConfigFactory configFactory = new ConfigFactory(); 
	/**
	 * Factory de configuration des paramètres du jeu.
	 */
	public static LevelFactory levelFactory = new LevelFactory(); 
	/**
	 * Factory de gestion des joueurs.
	 */
	public static PlayerFactory playerFactory = new PlayerFactory(); 
	/**
	 * Texture contenant les différentes images du jeu.
	 */
	public static Texture texture;
	/**
	 * Texture d'affichage de l'image du joueur.
	 */
	public static Texture playerTexture;
	/**
	 * Thème principal du jeu.
	 */
	public static Skin skin;
	/**
	 * Gestionnaire de langue.
	 */
	public static I18NBundle bundle;
	/**
	 * Cursseur par défaut du jeu.
	 */
	public static Cursor defaultCursor;
	/**
	 * Liste des sons du jeu.
	 */
	public static Sound[] sounds = new Sound[11];
	/**
	 * Musique en cours.
	 */
	private static Music currentMusic;

	/**
	 * Charge les ressources du jeu.
	 */
	public static void load(){
		I18NBundleParameter b_param = new I18NBundleParameter(new Locale(configFactory.config.language));

		manager.load("data/uiskin.json", Skin.class);
		manager.load("images/tileset.png", Texture.class);
		manager.load("images/player.png", Texture.class);
		manager.load("i18n/gemcraft", I18NBundle.class, b_param);
		manager.load("data/cursor_pointer1.png", Pixmap.class);
		manager.load("data/cursor_hand1.png", Pixmap.class);
		manager.load("sounds/click.mp3", Sound.class);
		manager.load("sounds/coin.ogg", Sound.class);
		manager.load("sounds/pick.ogg", Sound.class);
		manager.load("sounds/drop.ogg", Sound.class);
		manager.load("sounds/grow.ogg", Sound.class);
		manager.load("sounds/error.ogg", Sound.class);
		manager.load("sounds/damage.ogg", Sound.class);
		manager.load("sounds/explosion.ogg", Sound.class);
		manager.load("sounds/shot.ogg", Sound.class);
		manager.load("sounds/dead.mp3", Sound.class);
		manager.load("sounds/success.mp3", Sound.class);

		manager.finishLoading();

		defaultCursor = Gdx.graphics.newCursor(manager.get("data/cursor_pointer1.png", Pixmap.class), 0, 0);
		Gdx.graphics.setCursor(defaultCursor);
		bundle = manager.get("i18n/gemcraft");
		skin = manager.get("data/uiskin.json", Skin.class);
		texture = manager.get("images/tileset.png", Texture.class);
		playerTexture = manager.get("images/player.png", Texture.class);
		sounds[0] = manager.get("sounds/click.mp3", Sound.class);
		sounds[1] = manager.get("sounds/coin.ogg", Sound.class);
		sounds[2] = manager.get("sounds/pick.ogg", Sound.class);
		sounds[3] = manager.get("sounds/drop.ogg", Sound.class);
		sounds[4] = manager.get("sounds/grow.ogg", Sound.class);
		sounds[5] = manager.get("sounds/error.ogg", Sound.class);
		sounds[6] = manager.get("sounds/damage.ogg", Sound.class);
		sounds[7] = manager.get("sounds/explosion.ogg", Sound.class);
		sounds[8] = manager.get("sounds/shot.ogg", Sound.class);
		sounds[9] = manager.get("sounds/dead.mp3", Sound.class);
		sounds[10] = manager.get("sounds/success.mp3", Sound.class);
	}

	public static void playTheme(){
		play("prologue.mp3");
	}
	public static void playGeneric(){
		play("generic.mp3");
	}

	public static void playRandom(){
		Random r = new Random();
		int index = r.nextInt(14);
		play("music_"+index+".mp3");
	}

	public static void pausedMusic(){
		if(currentMusic != null) currentMusic.pause();
	}

	public static void stopMusic(){
		if(currentMusic != null) currentMusic.stop();
	}
	
	public static void play(String music){
		if(currentMusic != null){ 
			if(currentMusic.isPlaying()) 
				currentMusic.stop();
			currentMusic.dispose();
		}
		manager.load("musics/"+music, Music.class);
		manager.finishLoadingAsset("musics/"+music);
		currentMusic = manager.get("musics/"+music, Music.class);
		currentMusic.setLooping(true);
		currentMusic.setVolume(configFactory.config.musicVolume);
		currentMusic.play();
	}
	
	public static void playSound(SoundEffect sound){
		sounds[sound.ordinal()].play(configFactory.config.soundVolume);
	}

	/**
	 * Met à jour la configuration actuelle.
	 */
	public static void updateConfig(){
		I18NBundleParameter b_param = new I18NBundleParameter(new Locale(configFactory.config.language));
		manager.load("i18n/gemcraft", I18NBundle.class, b_param);
		manager.finishLoadingAsset("i18n/gemcraft");
		bundle = manager.get("i18n/gemcraft");
		if(currentMusic != null) currentMusic.setVolume(configFactory.config.musicVolume);
	}

	public static void updateConfig(String directory){
		I18NBundleParameter b_param = new I18NBundleParameter(new Locale(configFactory.config.language));
		manager.load(directory+"i18n/gemcraft", I18NBundle.class, b_param);
		manager.finishLoadingAsset(directory+"i18n/gemcraft");
		bundle = manager.get(directory+"i18n/gemcraft");
	}

	/**
	 * Change le curseur du jeu.
	 */
	public static void setCursor(String str, int xhotspot, int yhotspot){
		Pixmap p = manager.get(str, Pixmap.class);
		if(p != null){
			Cursor cursor = Gdx.graphics.newCursor(p, xhotspot, yhotspot);
			if(cursor != null)
				Gdx.graphics.setCursor(cursor);
		}
	}

	/**
	 * Change le curseur à celui par défaut du jeu.
	 */
	public static void setDefaultCursor(){
		Gdx.graphics.setCursor(defaultCursor);
	}

	/**
	 * Libère les ressources du jeu.
	 */
	public static void dispose(){
		manager.dispose();
	}
}
