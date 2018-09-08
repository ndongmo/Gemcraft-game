package com.ndop.gemcraft.framework.factory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.Gem;
import com.ndop.gemcraft.framework.model.GemColor;
import com.ndop.gemcraft.framework.model.Group;
import com.ndop.gemcraft.framework.model.Level;
import com.ndop.gemcraft.framework.model.MyFileFilter;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.Spawn;
import com.ndop.gemcraft.framework.model.Tower;
import com.ndop.gemcraft.framework.model.Trap;
import com.ndop.gemcraft.framework.model.Wave;
import com.ndop.gemcraft.framework.view.MyMap;

/**
 * Factory de l'objet Level. Elle charge les différents fichiers de 
 * description des niveaux et crée les niveaux requis.
 * @author --
 * @version 1.0
 */
public class LevelFactory implements ILevelFactory{
	/**
	 * Liste des noms de fichier des descripteurs de niveau.
	 */
	private ArrayList<String> levelFiles;
	/**
	 * Liste des comparateurs d'ennemis identifiables par leurs noms.
	 */
	private HashMap<String, Comparator<Enemy>> enemyComparators;

	/**
	 * Pool de gestion de la réutilisabilité des ojets Enemy
	 */
	public Pool<Enemy> enemyPool = new Pool<Enemy>(){
		@Override
		protected Enemy newObject() {
			return new Enemy();
		}
	};

	/*
	/**
	 * Pool de gestion de la réutilisabilité des ojets Tower

	public Pool<Tower> towerPool = new Pool<Tower>(){
		@Override
		protected Tower newObject() {
			return new Tower();
		}
	};*/

	/**
	 * Pool de gestion de la réutilisabilité des ojets Gem

	public Pool<Gem> gemPool = new Pool<Gem>(){
		@Override
		protected Gem newObject() {
			return new Gem();
		}
	};*/

	public LevelFactory() {
		levelFiles = new ArrayList<String>();
		this.loadFiles();

		enemyComparators = new HashMap<String, Comparator<Enemy>>();
		enemyComparators.put("life", lifeComparator());
	}

	/**
	 * Vérifie s'il existe un niveau supérieur à celui entré.
	 * @param level le niveau actuel.
	 * @return vrai s'il existe un niveau supérieur, faux sinon.
	 */
	public boolean hasNext(String level){
		int index = levelFiles.indexOf(level);
		return !(index == levelFiles.size()-1 || index < 0);
	}

	/**
	 * Vérifie s'il existe un niveau supérieur à celui entré.
	 * @param level le niveau actuel.
	 * @return vrai s'il existe un niveau supérieur, faux sinon.
	 */
	public boolean hasNext(int level){
		return !(level == levelFiles.size() || level < 0);
	}

	/**
	 * Obtient le prochain niveau à partir du niveau courant.
	 * @param level le niveau courant.
	 * @return le prochain niveau s'il existe, null sinon.1
	 */
	public String getNextLevel(String level){
		if(level.isEmpty() && levelFiles.size() > 0)
			return levelFiles.get(0);
		if(hasNext(level))
			return levelFiles.get(levelFiles.indexOf(level)+1);
		return null;
	}

	/**
	 * Obtient le classement du niveau recherché.
	 * @param fileName le fichier du niveau.
	 * @return int classement
	 */
	public int getLevel(String fileName){
		return levelFiles.indexOf(fileName)+1;
	}

	/**
	 * Charge la liste des noms des fichiers de description.
	 */
	public void loadFiles() {
		FileHandle fh = Gdx.files.local("levels/");
		for(FileHandle lg : fh.list(new MyFileFilter("^((g[0-9]+_\\w+-[1-9]+)|(g[1-9]+_\\w+))\\.xml$"))){
			String fileName = lg.name();
			levelFiles.add(fileName);
		}
		Collections.sort(levelFiles, new Comparator<String>() {
			@Override
			public int compare(String str1, String str2) {
				try{
					str1 = str1.substring(str1.lastIndexOf("_"), str1.lastIndexOf(".xml"));
					str2 = str2.substring(str2.lastIndexOf("_"), str2.lastIndexOf(".xml"));
				}catch(Exception e){}
				return str1.compareTo(str2);
			}
		});
	}

	/**
	 * Charge la liste des noms des fichiers de description et leurs contenus dans une LinkedHashmap.
	 * @return LinkedHashmap <nom,contenu> des fichiers de description.
	 */
	public LinkedHashMap<String, String> loadLevels(){

		LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();

		for(String fileName : levelFiles){
			String description = format(fileName);
			list.put(fileName, description);
		}

		return list;
	}

	/**
	 * Récupère le contenu du fichier niveau et retourne 
	 * sa description sous forme d'une chaine de caractère.
	 * @param fileName nom du fichier.
	 * @return description du fichier niveau.
	 */
	public String format(String fileName){

		FileHandle fh = Gdx.files.local("levels/"+fileName);
		XmlReader reader = new XmlReader();
		String descr = "";
		Element root, node;

		try {
			root = reader.parse(fh);
			descr += ResourceManager.bundle.get("nom")+root.get("name")+"\n"+ResourceManager.bundle.get("vieIni")+
					root.get("life")+"\n"+ResourceManager.bundle.get("manaIni")+root.get("mana")+"\n";

			if((node = root.getChildByName("spawns")) != null){
				descr += ResourceManager.bundle.get("nbSentier")+node.getChildCount()+"\n";
			}

			if((node = root.getChildByName("towers")) != null){
				descr += ResourceManager.bundle.get("nbTour")+node.get("quantity")+"\n";
			}

			if((node = root.getChildByName("traps")) != null){
				descr += ResourceManager.bundle.get("nbPiege")+node.get("quantity")+"\n";
			}

			if((node = root.getChildByName("gems")) != null){
				descr += ResourceManager.bundle.get("gemmeIni")+"\n";
				for(Element child : node.getChildrenByName("gem"))
					descr += "    "+ResourceManager.bundle.get("gemmeType")+child.get("color")+";    "+
							ResourceManager.bundle.get("quantite")+child.get("quantity")+"\n";
			}

			if((node = root.getChildByName("enemytypes")) != null){
				descr += ResourceManager.bundle.get("enemyType")+"\n";
				for(Element child : node.getChildrenByName("type"))
					descr += "    "+ResourceManager.bundle.get("nom")+child.get("name")+";    "+
							ResourceManager.bundle.get("immunite")+child.get("immunity")+";    "+
							ResourceManager.bundle.get("vie")+child.get("life")+";    "+
							ResourceManager.bundle.get("mana")+child.get("mana")+"\n";
			}

			if((node = root.getChildByName("waves")) != null){
				descr += ResourceManager.bundle.get("vagues")+node.getChildCount()+"\n";
				for(Element child : node.getChildrenByName("wave")){
					descr += "    "+ResourceManager.bundle.get("delai")+child.get("delay")+";    "+
							ResourceManager.bundle.get("nbGroupe")+child.getChildCount()+"\n";
				}
			}

		} catch (IOException e) {
			new FactoryException("Le fichier '"+fileName+"' est mal formaté (xml) !", e);
		}catch(GdxRuntimeException gex){
			new FactoryException("Le fichier '"+fileName+"' est mal formaté, attribut obligatoire manquant !", gex);
		}catch(Exception ex){
			new FactoryException("Le fichier '"+fileName+"' est mal formaté !", ex);
		}

		return descr;
	}

	/**
	 * Obtient le nom du niveau à partir de son index.
	 * @param index du niveau
	 * @return nom du niveau à l'index donné.
	 */
	public String getLevelName(int index){
		String name = "";
		String fileName = getFileName(index);

		if(!fileName.isEmpty()){
			FileHandle fh = Gdx.files.local("levels/"+fileName);
			XmlReader reader = new XmlReader();
			try{
				name = reader.parse(fh).get("name");
			}catch(Exception e){}
		}

		return name;
	}

	public String getFileName(int index){
		String fileName = "";
		if(levelFiles.size() > index-1){
			fileName = levelFiles.get(index-1);
		}
		return fileName;
	}

	/**
	 * Charge un niveau à partir de son fichier de description.
	 * @param fileName fichier de description du niveau.
	 * @return level le niveau chargé.
	 */
	public Level loadLevel(String fileName) throws FactoryException {
		Level level = new Level();

		FileHandle fh = Gdx.files.local("levels/"+fileName);
		XmlReader reader = new XmlReader();
		HashMap<Enemy.EnemyType, Enemy> enemyTypes = new HashMap<Enemy.EnemyType, Enemy>();
		Element root, node;
		level.setFileName(fileName);

		try {
			root = reader.parse(fh);
			level.setName(root.get("name"));
			level.setMapName(root.get("map"));
			level.setLife(root.getFloat("life"));
			level.setMana(root.getFloat("mana"));

			Rectangle nexus = new Rectangle(root.getInt("nexus_x"), root.getInt("nexus_y"), MyMap.TILE_SIZE, MyMap.TILE_SIZE);
			level.setNexus(nexus);

			if((node = root.getChildByName("gems")) != null){
				for(Element child : node.getChildrenByName("gem"))
					for(int i=0; i<child.getInt("quantity"); i++)
						level.addGem(createGem(child.get("color")));
			}

			if((node = root.getChildByName("towers")) != null){
				for(int i=0; i<node.getInt("quantity"); i++)
					level.addTower(createTower());
			}

			if((node = root.getChildByName("traps")) != null){
				for(int i=0; i<node.getInt("quantity"); i++)
					level.addTower(createTrap());
			}

			if((node = root.getChildByName("spawns")) != null){
				for(Element child : node.getChildrenByName("spawn"))
					level.addSpawn(createSpawn(child.get("name"), child.getInt("x"), child.getInt("y")));
			}

			if((node = root.getChildByName("enemytypes")) != null){
				for(Element child : node.getChildrenByName("type")){
					enemyTypes.put(getEnemyType(child.get("name")), createEnemy(child.get("name"), child.get("immunity"), 
							child.getFloat("speed"), child.getFloat("size"), child.get("sprite"), 
							child.getFloat("life"), child.getFloat("mana")));
				}
			}

			if((node = root.getChildByName("waves")) != null){
				float delay = 0;
				if(node.getChildCount() < 1)
					throw new FactoryException("Un niveau doit contenir au moins une vague !");

				for(Element child : node.getChildrenByName("wave")){
					delay += child.getFloat("delay");
					if(child.getChildCount() < 1)
						throw new FactoryException("Une vague doit contenir au moins un groupe !");

					Wave wave = new Wave("", delay);
					for(Element groupEle : child.getChildrenByName("group")){
						if(groupEle.getChildCount() < 1)
							throw new FactoryException("Un groupe doit contenir au moins un enemie !");

						Spawn spawn = level.getSpawns().get(level.getSpawns().indexOf(new Spawn(groupEle.get("spawn"),null)));
						Group group = new Group(spawn, groupEle.getFloat("lapse"), delay+groupEle.getFloat("timer"));

						for(Element enemyEle : groupEle.getChildrenByName("ennemy")){
							Enemy e = null;
							for(int i=0; i<enemyEle.getInt("quantity"); i++){
								if(e != null){
									Enemy e1 = enemyPool.obtain();
									e1.setEnemy(e);
									group.addEnemy(e1);
								}
								else{
									e = enemyPool.obtain();
									e.setEnemy(enemyTypes.get(getEnemyType(enemyEle.get("type"))));

									if(enemyEle.get("speed", null) != null){
										e.setCurrentVelocity(enemyEle.getFloat("speed"));
										e.setRealVelocity(enemyEle.getFloat("speed"));
									}

									if(enemyEle.get("life", null) != null){
										e.setLife(enemyEle.getFloat("life"));
									}

									if(enemyEle.get("mana", null) != null){
										e.setLife(enemyEle.getFloat("mana"));
									}

									group.addEnemy(e);
								}
							}
						}
						if(groupEle.get("order", null) != null && enemyComparators.containsKey(groupEle.get("order"))){
							Comparator<Enemy> comparator = enemyComparators.get(groupEle.get("order"));
							Collections.sort(group.getEnemies(), comparator);
						}
						else
							Collections.shuffle(group.getEnemies());

						wave.addGroup(group);
					}
					level.addWave(wave);
				}
			}

		} catch (IOException e) {
			throw new FactoryException("Le fichier '"+fileName+"' est mal formaté (xml) !", e);
		}catch(GdxRuntimeException gex){
			throw new FactoryException("Le fichier '"+fileName+"' est mal formaté, attribut obligatoire manquant !", gex);
		}catch(Exception ex){
			System.out.println(Thread.currentThread().getStackTrace()[2].getLineNumber());
			throw new FactoryException("Le fichier '"+fileName+"' est mal formaté !", ex);
		}

		return level;
	}

	public ArrayList<String> getLevelFiles(){
		return levelFiles;
	}

	public Enemy.EnemyType getEnemyType(String mytype){
		Enemy.EnemyType type = null;
		try{type = Enemy.EnemyType.valueOf(mytype.toUpperCase());}
		catch(Exception ex){type = Enemy.EnemyType.UNDEAD;}
		return type;
	}

	@Override
	public Gem createGem(String gc) throws FactoryException {
		Gem g = new Gem();
		g.setGemColor(GemColor.valueOf(gc.toUpperCase()));
		return g;
	}

	@Override
	public Gem createGem(GemColor gc) {
		Gem g = new Gem();
		g.setGemColor(gc);
		return g;
	}

	@Override
	public Spawn createSpawn(String name, int x, int y) {
		return new Spawn(name,new Vector2(x, y));
	}

	@Override
	public Enemy createEnemy(String name, String immunity, float speed,
			float size, String sprite, float life, float mana) throws FactoryException {

		Enemy e = enemyPool.obtain();
		e.setType(getEnemyType(name));
		e.setLife(life);
		e.setMana(mana);
		e.setRealVelocity(speed);
		e.setCurrentVelocity(speed);
		e.setSize(size);
		for(String gc : immunity.split(",")){
			if(!gc.equals("none"))
				e.setImmunity(GemColor.valueOf(gc.toUpperCase()), true);
		}

		return e;
	}

	@Override
	public Trap createTrap() {
		return new Trap();
	}

	@Override
	public Tower createTower() {
		return new Tower();
	}

	@Override
	public Comparator<Enemy> lifeComparator() {
		return new Comparator<Enemy>(){
			@Override
			public int compare(Enemy e0, Enemy e1) {
				return (e0.getLife() > e1.getLife())?1:-1;
			}
		};
	}

}
