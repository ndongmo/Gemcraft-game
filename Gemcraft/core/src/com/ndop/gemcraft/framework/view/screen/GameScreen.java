package com.ndop.gemcraft.framework.view.screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ndop.gemcraft.framework.factory.FactoryException;
import com.ndop.gemcraft.framework.model.Bullet;
import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.Gem;
import com.ndop.gemcraft.framework.model.IGameInteractions;
import com.ndop.gemcraft.framework.model.IUpdate;
import com.ndop.gemcraft.framework.model.Level;
import com.ndop.gemcraft.framework.model.MyGridCell;
import com.ndop.gemcraft.framework.model.Player;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.model.Spawn;
import com.ndop.gemcraft.framework.model.Tower;
import com.ndop.gemcraft.framework.model.Wave;
import com.ndop.gemcraft.framework.model.gemsActions.GemAction;
import com.ndop.gemcraft.framework.view.MyGemcraft;
import com.ndop.gemcraft.framework.view.MyMap;
import com.ndop.gemcraft.framework.view.panel.GameBoardPanel;
import com.ndop.gemcraft.framework.view.panel.GameOverPanel;
import com.ndop.gemcraft.framework.view.panel.GenericPanel;
import com.ndop.gemcraft.framework.view.panel.InventoryPanel;
import com.ndop.gemcraft.framework.view.panel.LoadingPanel;

/**
 * Ecran de jeu.
 * @author --
 * @version 1.0
 */
public class GameScreen extends MyScreen implements IUpdate, IGameInteractions{

	/**
	 * Mode de jeu.
	 * @author --
	 * @version 1.0
	 */
	public enum GameMode{
		FREEMAP, // Mode libre
		CAMPAGN; // Mode campagne
	}

	/**
	 * Différents états du jeu.
	 * @author --
	 * @version 1.0
	 */
	public enum GameState{
		LOADING, // Etat du jeu en chargement
		PAUSED, // Etat du jeu en pause
		RUNNING, // Etat du jeu en cours
		GAMEOVER; // Etat du jeu terminé
	}

	/**
	 * Mode de jeu actuel.
	 */
	private GameMode currentMode;
	/**
	 * Etat courant du jeu.
	 */
	private GameState currentState;
	/**
	 * Niveau actuel.
	 */
	private Level currentLevel;
	/**
	 * Nexus.
	 */
	private Rectangle nexus;
	/**
	 * Multiplicateur de vitesse.
	 */
	private float speedMult;
	/**
	 * Le joueur courant.
	 */
	private Player player;
	/**
	 * Objet actuellement sélectionné
	 */
	private Sprite selectedObject;
	/**
	 * Liste des vagues d'ennemis du niveau courant.
	 */
	private ArrayList<Wave> waves;
	/**
	 * Liste des tours présentes sur la map.
	 */
	private ArrayList<Tower> towers = new ArrayList<Tower>();
	/**
	 * Liste d'ennemis sur la map.
	 */
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	/**
	 * Liste d'ennemis morts sur la map.
	 */
	private ArrayList<Enemy> deadEnemies = new ArrayList<Enemy>();
	/**
	 * Liste des projectiles affichés.
	 */
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	/**
	 * Pool de gestion de la réutilisabilité des projectiles.
	 */
	public Pool<Bullet> bulletPool = new Pool<Bullet>(){
		@Override
		protected Bullet newObject() {
			return new Bullet();
		}
	};
	/**
	 * Conteneur de la carte du niveau.
	 */
	private MyMap theMap;
	/**
	 * Panneau d'affichage des infos du joueur.
	 */
	private GameBoardPanel board;
	/**
	 * panneau d'affichage de l'inventaire.
	 */
	private InventoryPanel inventory;
	/**
	 * Ecran de chargement.
	 */
	private LoadingPanel loadingPane;
	/**
	 * Ecran de fin du jeu.
	 */
	private GameOverPanel overPane;
	/**
	 * Ecran du generique de fin du jeu.
	 */
	private GenericPanel genericPane;
	/**
	 * Temps écoulé depuis le début du jeu.
	 */
	private float elapsedTime;
	/**
	 * Utilisé pour afficher la vie des ennemis
	 */
	private ShapeRenderer shape;
	private Label lb_time;


	public GameScreen(MyGemcraft game, String level, Player player, GameMode mode) {
		super(game);

		this.player = player;
		player.setBoard(board);

		currentLevel = new Level();
		currentLevel.setFileName(level);
		shape = new ShapeRenderer();
		nexus = new Rectangle();
		currentMode = mode;

		loadLevel();
	}

	@Override
	public void initScreen() {
		theMap = new MyMap(this);
		board = new GameBoardPanel(this);
		inventory = new InventoryPanel(this);
		overPane = new GameOverPanel(this);
		loadingPane = new LoadingPanel(this);
		genericPane = new GenericPanel(this);
		lb_time = new Label("", ResourceManager.skin);

		currentState = GameState.LOADING;

		VerticalGroup vg = new VerticalGroup();
		vg.addActor(lb_time);
		vg.addActor(inventory);

		table.add(board).fillX().expandX().colspan(2).row();
		table.add(vg).width(inventory.getWidth());
		table.add(theMap).fill().left().expand().pad(10).row();

		table.add(loadingPane).left().size(0, 0).row();
		table.add(overPane).left().size(0, 0).row();
		table.add(genericPane).left().size(0, 0);

		hidePane(false);
		overPane.setVisible(false);
		genericPane.setVisible(false);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(new StretchViewport(width, height));
		stage.getViewport().update(width, height, true);
		shape.setProjectionMatrix(stage.getCamera().combined);

		table.clear();
		VerticalGroup vg = new VerticalGroup();
		vg.addActor(lb_time);
		vg.addActor(inventory);
		table.add(board).fillX().expandX().height(board.HEIGHT).colspan(2).row();
		table.add(vg).width(inventory.getWidth());
		table.add(theMap).expand().fill().pad(10).row();

		table.add(loadingPane).left().size(0, 0).row();
		table.add(overPane).left().size(0, 0).row();
		table.add(genericPane).left().size(0, 0);

		float w = width - inventory.getWidth()-20;
		float h = height - board.HEIGHT - 20;

		theMap.setBounds(inventory.getWidth()+10, 10, w, h);
		theMap.setOrigin(inventory.getWidth()+10, 10);
		initNexus();

		for(Tower tower : towers){
			tower.updateShape(stage.getCamera().combined);
		}
	}

	/**
	 * Charge le nouveau niveau dans un thread.
	 */
	private void loadLevel(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						try {		
							loadingPane.initLoading(currentLevel.getFileName());

							towers.clear();
							bullets.clear();

							currentLevel = ResourceManager.levelFactory.loadLevel(currentLevel.getFileName());
							board.init(currentLevel.getLife(), currentLevel.getMana(), currentLevel.getName(), player.getName());
							inventory.initInventory(currentLevel.getGems(), currentLevel.getTowers());

							theMap.initMap(currentLevel.getMapName());
							buildEnemyPath(theMap.getCells());

							selectedObject = null;
							ResourceManager.setDefaultCursor();

							player.initPlayer(currentLevel.getLife(), currentLevel.getMana());

							initNexus();

							waves = currentLevel.getWaves();

							loadingPane.setLoad(true);

						} catch (FactoryException e) {
							currentState = GameState.GAMEOVER;
							over();
						}
					}
				});
			}
		}).start();
		ResourceManager.playRandom();
	}

	public void start(){
		hidePane(true);
		currentState = GameState.RUNNING;
		speedMult = 1;
		elapsedTime = 0;
	}

	public void load(){
		hidePane(false);
		overPane.setVisible(false);
		genericPane.setVisible(false);

		currentState = GameState.LOADING;
		currentLevel.setFileName(ResourceManager.levelFactory.getNextLevel(currentLevel.getFileName()));

		loadLevel();
	}

	public void over(){
		ResourceManager.stopMusic();

		hidePane(false);
		loadingPane.setVisible(false);
		genericPane.setVisible(false);

		currentState = GameState.GAMEOVER;
		overPane.initLevelOver(elapsedTime);
		if(GameMode.CAMPAGN == currentMode)
			ResourceManager.playerFactory.save();
	}
	
	public void win(){
		ResourceManager.playGeneric();

		hidePane(false);
		loadingPane.setVisible(false);
		overPane.setVisible(false);
	}

	@Override
	public void update(float deltaTime){
		deltaTime *= speedMult;
		elapsedTime += deltaTime;
		lb_time.setText(convert(elapsedTime));


		if(Gdx.input.isButtonPressed(Buttons.RIGHT) && !theMap.getDialogBox().isVisible()){
			if(selectedObject != null){
				selectedObject.setSize(MyMap.TILE_SIZE, MyMap.TILE_SIZE);

				if(selectedObject instanceof Tower)
					inventory.addTower((Tower)selectedObject);
				else
					inventory.addGem((Gem)selectedObject);
				selectedObject = null;

				theMap.updateState();
				ResourceManager.setDefaultCursor();
			}
			else
				theMap.drawDialogBox();
		}

		for(Tower tower : towers){
			tower.update(deltaTime, this);
		}

		Iterator<Bullet> itBullet = bullets.iterator();
		while(itBullet.hasNext()){
			Bullet bullet = itBullet.next();
			bullet.update(deltaTime, player);
			if(!bullet.isAlive()){
				bulletPool.free(bullet);
				itBullet.remove();
			}
		}

		Iterator<Enemy> itEnemy = enemies.iterator();
		while(itEnemy.hasNext()){
			Enemy e = itEnemy.next();
			e.update(deltaTime);

			for(GemAction a : e.getActions()){
				a.applyToEnemy(e, this, deltaTime);
			}

			e.processDamage();

			if(e.getBound().overlaps(nexus)){
				ResourceManager.playSound(SoundEffect.DAMAGE);
				player.setLife(player.getLife()- e.getLife());
				e.setLife(0);
			}

			if(!e.isAlive()){
				ResourceManager.playSound(SoundEffect.EXPLOSION);
				e.dead();
				deadEnemies.add(e);
				itEnemy.remove();
			}
		}

		Iterator<Enemy> itDeadEnemy = deadEnemies.iterator();
		while(itDeadEnemy.hasNext()){
			Enemy e = itDeadEnemy.next();
			if(e.canDispose()){
				ResourceManager.levelFactory.enemyPool.free(e);
				itDeadEnemy.remove();
			}
		}

		Iterator<Wave> itWave = waves.iterator();
		while(itWave.hasNext()){
			Wave wave = itWave.next();
			if(wave.getGroups().isEmpty()){
				itWave.remove();
				continue;
			}
			ArrayList<Enemy> list = wave.getEnemies(elapsedTime);
			if(!list.isEmpty()){
				enemies.addAll(list);
			}
		}

		if((waves.isEmpty() && enemies.isEmpty() && deadEnemies.isEmpty() && bullets.isEmpty()) || !player.isAlive()){
			currentState = GameState.GAMEOVER;
			over();
		}

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); 


		if(currentState == GameState.RUNNING)
			update(Gdx.graphics.getDeltaTime());

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		if(currentState == GameState.RUNNING || currentState == GameState.PAUSED){
			stage.getBatch().begin();
			for(Enemy e : enemies){
				float x = theMap.getX() + (e.getX() * theMap.getXscale()) + (e.getgridMovePosX() * theMap.getXscale());
				float y = theMap.getY() + (e.getY() * theMap.getYscale()) + (e.getgridMovePosY() * theMap.getXscale());
				float w =  e.getSize() * theMap.getXscale();
				float h =  e.getSize() * theMap.getXscale();

				e.setBound(x, y, w, h);
				stage.getBatch().draw(e.getSprite(elapsedTime), x, y, w, h);
			}

			for(Enemy e : deadEnemies){
				float x = e.getBound().x;
				float y = e.getBound().y;
				float w =  e.getBound().width;
				float h =  e.getBound().height;
				stage.getBatch().draw(e.getSprite(elapsedTime), x, y, w, h);
			}
			stage.getBatch().end();

			shape.begin(ShapeType.Filled);
			shape.setColor(Color.GREEN);
			for(Enemy e : enemies){
				float x = e.getBound().x;
				float y = e.getBound().y;
				float w =  e.getBound().width;
				float h =  e.getBound().height;
				float mana = (e.getLife() * w)/e.getInitLife();

				shape.rect(x, y+h+2, mana, 1);
			}
			shape.setColor(Color.RED);
			for(Bullet bullet : bullets){
				shape.circle(bullet.getPosition().x, bullet.getPosition().y, 3);
			}
			shape.end();

			if(selectedObject != null){
				Vector3 point = new Vector3(Gdx.input.getX(), Gdx.input.getY()+(theMap.getYscale()/2), 0);
				stage.getCamera().unproject(point);
				selectedObject.setBounds(point.x, point.y, theMap.getXscale(), theMap.getYscale());
				stage.getBatch().begin();
				if(theMap.getDialogBox().isVisible())
					theMap.getDialogBox().draw(stage.getBatch(), 1);
				selectedObject.draw(stage.getBatch(), 0.6f);
				stage.getBatch().end();
			}
		}
	}

	private void buildEnemyPath(MyGridCell[][] cells){
		GridFinderOptions opt = new GridFinderOptions();
		opt.allowDiagonal = false;
		NavigationGrid<MyGridCell> navGrid = new NavigationGrid<MyGridCell>(cells, false);
		AStarGridFinder<MyGridCell> finder = new AStarGridFinder<MyGridCell>(MyGridCell.class, opt);

		for(Spawn spawn : currentLevel.getSpawns()){
			List<MyGridCell> pathToEnd = finder.findPath((int)spawn.getOrigin().x, (int)spawn.getOrigin().y,
					(int)currentLevel.getNexus().x, (int)currentLevel.getNexus().y, navGrid);
			spawn.setCells(pathToEnd);
		}
	}

	public GameMode getMode(){
		return currentMode;
	}

	public GameState getState(){
		return currentState;
	}

	public Player getPlayer(){
		return player;
	}

	public Level getCurrentLevel(){
		return currentLevel;
	}

	public Sprite getSelectedObject(){
		return selectedObject;
	}

	public ArrayList<Tower> getTowers(){
		return towers;
	}

	public void setSelectedObject(Sprite current){
		if(current instanceof Tower)
			((Tower)current).updateShape(stage.getCamera().combined);
		selectedObject = current;
	}

	public InventoryPanel getInventoryPanel(){
		return inventory;
	}

	public void hideDialogBox(){
		theMap.getDialogBox().setVisible(false);
		if(currentState == GameState.PAUSED)
			board.play();
	}

	public static String convert(float time){
		String min = ((int)(time / 60))+"";
		String sec = ((int)(time % 60))+"";
		if(sec.length() < 2) sec = "0"+sec;
		if(min.length() < 2) min = "0"+min;
		return min+" : "+sec;
	}

	public int getSumlLevel(){
		int sum = 0;
		for(Tower t : towers)
			sum += t.getLevel();

		return sum;
	}

	public void initNexus(){
		float x = theMap.getX() + (currentLevel.getNexus().getX() * theMap.getXscale());
		float y = theMap.getY() + (currentLevel.getNexus().getY() * theMap.getYscale());
		float w =  theMap.getXscale();
		float h =  theMap.getYscale();

		nexus.set(x, y, w, h);
	}

	public void hidePane(boolean b){
		theMap.setVisible(b);
		board.setVisible(b);
		inventory.setVisible(b);
		overPane.setVisible(!b);
		loadingPane.setVisible(!b);
		genericPane.setVisible(!b);
		lb_time.setVisible(b);
	}

	public void stop(){
		game.setScreen(new MainMenuScreen(game));
	}

	public void setState(GameState state){
		currentState = state;
	}
	public void pause(){
		board.pause();
	}
	public void play(){
		speedMult = 1;
		currentState = GameState.RUNNING;
	}
	public void acc(){
		speedMult = 5;
		currentState = GameState.RUNNING;
	}

	@Override
	public void addBullet(Bullet bullet) {
		bullets.add(bullet);
	}

	@Override
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	@Override
	public ArrayList<Spawn> getSpawns() {
		return currentLevel.getSpawns();
	}

	@Override
	public Pool<Bullet> getBulletPool() {
		return bulletPool;
	}

	@Override
	public Rectangle getNexus() {
		return nexus;
	}

}
