package com.ndop.gemcraft.framework.factory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ndop.gemcraft.framework.model.Player;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.StatLevel;

/**
 * Factory de l'objet Player. Elle charge les sauvegardes des joueurs.
 * Elle est aussi chargée de la comparaison des joueurs selon le niveau ou le jeu dans sa globalité.
 * @author --
 * @version 1.0
 */
public class PlayerFactory {
	/**
	 * Nom du fichier de sauvegarde.
	 */
	private static final String PLAYER_FILENAME = "configs/players.xml";
	/**
	 * Les milleurs scores.
	 */
	public static final int BEST_PLAYER = 5;
	
	private ArrayList<Player> players;

	public PlayerFactory(){
		players = new ArrayList<Player>();
		loadPlayers();
	}

	/**
	 * Charge la liste des sauvegardes.
	 */
	public void loadPlayers(){
		FileHandle fh = Gdx.files.local(PLAYER_FILENAME);

		if(!fh.exists()){
			return;
		}

		XmlReader reader = new XmlReader();
		Element root;

		try {
			root = reader.parse(fh);

			for(Element node : root.getChildrenByName("player")){
				Player player = new Player(node.get("name"), node.getInt("currentLevel", 1));
				ArrayList<StatLevel> stats = new ArrayList<StatLevel>();
				player.setStats(stats);
				players.add(player);

				for(Element child : node.getChildrenByName("stat")){
					int index = child.getInt("index");
					float time = child.getFloat("time");
					String name = child.get("name");
					StatLevel stat = new StatLevel(index, name, time);
					stats.add(stat);
				}
			}
		} catch (Exception e) {
			new FactoryException("Erreur lors de la lecture du fichier de sauvegarde !", e);
		}
	}

	/**
	 * Sauvegarde des statistiques d'un joueur.
	 * @param player joueur en cours de sauvegarde.
	 */
	public void save(){
		FileWriter writer = null;
		XmlWriter xml;

		try {
			writer = new FileWriter(PLAYER_FILENAME);
			xml = new XmlWriter(writer);

			xml.element("players");
			for(Player p : players){
				xml.element("player").attribute("name", p.getName()).attribute("currentLevel", p.getCurrentLevel());
				for(StatLevel stat : p.getStats()){
					xml.element("stat").attribute("index", stat.getIndex())
					.attribute("name", stat.getName()).attribute("time", stat.getTime());
					xml.pop();
				}
				xml.pop();
			}
			xml.pop();

		} catch (IOException e) {
			new FactoryException("Erreur lors de l'ecriture du fichier de sauvegarde !", e);
		}finally{
			try {
				if(writer != null) writer.close();
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Teste l'existence de la sauvegarde d'un joueur.
	 * @param player
	 * @return vrai si elle existe, faux sinon.
	 */
	public boolean exists(Player player){
		return players.contains(player);
	}
	
	public Player getPlayer(String name, int level){
		Player player = new Player(name, level);
		if(players.contains(player))
			player = players.get(players.indexOf(player));
		return player;
	}
	
	public void addPlayer(Player player){
		players.add(player);
	}
	
	/**
	 * 
	 * @param level
	 * @return
	 */
	public ArrayList<Player> getLevelBestPlayers(final int level){
		ArrayList<Player> tmp = new ArrayList<Player>();
				
		Collections.sort(players, new Comparator<Player>(){
			@Override
			public int compare(Player p0, Player p1) {
				if(p0.getStats().size() >= level && p1.getStats().size() >= level){
					float t0 = p0.getStats().get(level-1).getTime();
					float t1 = p1.getStats().get(level-1).getTime();
					return (t0 < t1) ? -1 : 1;
				}
				return (p0.getStats().size() > p1.getStats().size()) ? -1 : 1;
			}
		});
		
		for(int i=0, j=0; i<players.size() && j<BEST_PLAYER; i++, j++){
			Player p = players.get(i);
			if(p.getStats().size() < level)
				break;
			tmp.add(p);
		}
		
		return tmp;
	}
	
	/**
	 * 
	 * @param level
	 * @return
	 */
	public ArrayList<Player> getGameBestPlayers(){
		ArrayList<Player> tmp = new ArrayList<Player>();
		final int level = ResourceManager.levelFactory.getLevelFiles().size();
		
		Collections.sort(players, new Comparator<Player>(){
			@Override
			public int compare(Player p0, Player p1) {
				if(p0.getStats().size() >= level  && p1.getStats().size() >= level){
					float t0 = p0.getAllTime();
					float t1 = p1.getAllTime();
					return (t0 < t1) ? -1 : 1;
				}
				return (p0.getStats().size() > p1.getStats().size()) ? -1 : 1;
			}
		});
		
		for(int i=0, j=0; i<players.size() && j<BEST_PLAYER; i++, j++){
			Player p = players.get(i);
			if(p.getStats().size() < level)
				break;
			tmp.add(p);
		}
		
		return tmp;
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}

}
