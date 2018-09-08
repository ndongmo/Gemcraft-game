package com.ndop.gemcraft.framework.model;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.ndop.gemcraft.framework.model.gemsActions.GemAction;
import com.ndop.gemcraft.framework.model.towerStrategies.BasicDamage;
import com.ndop.gemcraft.framework.model.towerStrategies.BasicEvolution;
import com.ndop.gemcraft.framework.model.towerStrategies.BasicFireRate;
import com.ndop.gemcraft.framework.model.towerStrategies.BasicGemLocation;
import com.ndop.gemcraft.framework.model.towerStrategies.BasicScope;
import com.ndop.gemcraft.framework.model.towerStrategies.IDamage;
import com.ndop.gemcraft.framework.model.towerStrategies.IEvolution;
import com.ndop.gemcraft.framework.model.towerStrategies.IFireRate;
import com.ndop.gemcraft.framework.model.towerStrategies.IGemLocation;
import com.ndop.gemcraft.framework.model.towerStrategies.IScope;
import com.ndop.gemcraft.framework.model.towerStrategies.ITargetTracker;
import com.ndop.gemcraft.framework.model.towerStrategies.NexusClosestTargetTracker;
import com.ndop.gemcraft.framework.view.MyMap;

/**
 * Classe représentant un objet tour du jeu. 
 * Elle implémente l'interface Poolable qui permet la réutilisabilité de l'objet.
 * @author --
 * @version 1.0
 */
public class Tower extends CraftObject {
	/**
	 * Le niveau de la tour.
	 */
	protected int level;
	
	protected boolean isPure;
	/**
	 * Liste des gemmes que contient la tour.
	 */
	protected ArrayList<Gem> gems = new ArrayList<Gem>();
	/**
	 * Temps de sommeil entre chaque upgrade.
	 */
	protected float currentSleepTime;
	/**
	 * Temps d'attente après un tir.
	 */
	protected float waitTime;
	/**
	 * Temps par défaut de sommeil entre chaque action.
	 */
	protected final float DEFAULT_SLEEP_TIME = 5;
	/**
	 * Tableau qui conserve le nombre de gemme de chaque couleur.
	 */
	protected int[] nbGems = new int[GemColor.values().length];
	/**
	 * Stratégie de calcul de tir.
	 */
	protected IFireRate fireRate;
	/**
	 * Stratégie de calcul du nombre d'emplacement.
	 */
	protected IGemLocation gemLoc;
	/**
	 * Stratégie de calcul de la portée de tir.
	 */
	protected IScope scope;
	/**
	 * Stratégie de calcul des dégats d'un tir.
	 */
	protected IDamage damage;
	/**
	 * Stratégie de calcul du coût d'évolution.
	 */
	protected IEvolution evolution;
	/**
	 * Stratégie de choix de l'ennemi à attaquer.
	 */
	protected ITargetTracker tracker;
	/**
	 * Couche pour marquer l'état de sommeil de la tour.
	 */
	protected ShapeRenderer sleepShape;
	/**
	 * Liste des cellules à portée de la tour.
	 */
	protected ArrayList<MyGridCell> closestCells = new ArrayList<MyGridCell>();
	
	public Tower(){
		this(0, 5);
	}
	
	public Tower(int xPos, int yPos){
		super();
		int x = xPos * MyMap.TILE_SIZE;
		int y = yPos * MyMap.TILE_SIZE;
		int w = MyMap.TILE_SIZE;
		int h = MyMap.TILE_SIZE;
		this.setRegion(new TextureRegion(ResourceManager.texture, x, y, w, h));
		this.sleepShape = new ShapeRenderer();
		
		level = 1;
		currentSleepTime = 0;
		waitTime = 0;
		fireRate = new BasicFireRate();
		gemLoc = new BasicGemLocation();
		damage = new BasicDamage();
		evolution = new BasicEvolution();
		scope = new BasicScope();
		tracker = new NexusClosestTargetTracker();
		setSize(w, h);
		
		Arrays.fill(nbGems, 0);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<Gem> getGems() {
		return gems;
	}

	public void addGem(Gem gem) {
		nbGems[gem.getGemColor().ordinal()]++;
		gems.add(gem);
		currentSleepTime = DEFAULT_SLEEP_TIME;
		setPurity();
	}
	
	public void remove(Gem gem){
		nbGems[gem.getGemColor().ordinal()]--;
		gems.remove(gem);
		setPurity();
	}

	public float getCurrentSleepTime() {
		return currentSleepTime;
	}
	
	public void updateClosestCells(MyGridCell cell, IGameInteractions game){
		int sc = scope.computeScope(level);
		closestCells.clear();

		for(Spawn s : game.getSpawns()){
			for(int i=0; i<s.getCells().size(); i++){
				Vector2 tile = new Vector2(s.getCells().get(i).x, s.getCells().get(i).y);
				
				if(cell.x-sc <= tile.x && tile.x <= cell.x+sc && cell.y-sc <= tile.y && tile.y <= cell.y+sc){
					closestCells.add(s.getCells().get(i));
				}
			}
		}
	}

	public void setCurrentSleepTime(float currentSleepTime) {
		this.currentSleepTime = currentSleepTime;
	}
	
	public ITargetTracker getTracker(){
		return tracker;
	}
	
	public void setTracker(ITargetTracker tracker){
		this.tracker = tracker;
	}
	
	public float getEvolutionCost(int levelsSum){
		return evolution.computeEvolutionCost(level, levelsSum);
	}
	
	public int getNbLocations(){
		return gemLoc.computeNbLocation(level);
	}
	
	public float getFireRate(){
		return fireRate.computeFireRate(level, gems.size(), getNbLocations());
	}
	
	public float getDamage(){
		return damage.computeDamage(level);
	}
	
	public int getScope(){
		return scope.computeScope(level);
	}
	
	public Tower createNew(){
		return ResourceManager.levelFactory.createTower();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
		if(gems.size() > 0){
			Gem gem = gems.get(gems.size()-1);
			gem.setPosition(this.getX(), this.getY());
			gem.draw(batch, parentAlpha-0.2f);
		}
		
		if(isAsleep()){
			batch.end();
			float degrees = (currentSleepTime * 360)/DEFAULT_SLEEP_TIME;
			float radius = this.getWidth()/2;
			sleepShape.begin(ShapeType.Filled);
			sleepShape.setColor(gems.get(gems.size()-1).getGemColor().color);
			sleepShape.arc(this.getX()+radius, this.getY()+radius+3, radius-3, 0, degrees);
			sleepShape.end();
			batch.begin();
		}
	}
	
	@Override
	public void setSize(float width, float height){
		super.setSize(width, height);
		if(gems.size() > 0){
			Gem gem = gems.get(gems.size()-1);
			gem.setSize(width, height);
		}
	}
	
	@Override
	public void setBounds(float x, float y, float width, float height){
		super.setBounds(x, y, width, height);
		if(gems.size() > 0){
			Gem gem = gems.get(gems.size()-1);
			gem.setBounds(x, y, width, height);
		}
	}
	
	public void update(float deltaTime, IGameInteractions game) {
		currentSleepTime -= deltaTime;
		waitTime -= deltaTime;
		
		if(! isAsleep() && waitTime <= 0 && gems.size() > 0 && game.getEnemies().size() > 0){
			Enemy target = tracker.trackTarget(closestCells);
			if(target == null) return;
			
			Bullet bullet = game.getBulletPool().obtain();
			Vector2 pos = new Vector2(this.getX()+(this.getWidth()/2), this.getY()+(this.getHeight()/2));
			float damage = this.damage.computeDamage(level);
			
			for(int i=0; i<bullet.getActions().length; i++){
				GemAction a = bullet.getActions()[i];
				a.updateAction(nbGems[i], isPure, damage);
			}
			
			if(isPure && nbGems[GemColor.CYAN.ordinal()] > 1)
				damage = 0;
			
			bullet.initBullet(pos, damage, target);
			game.addBullet(bullet);
			
			waitTime = fireRate.computeFireRate(level, gems.size(), gemLoc.computeNbLocation(level));
		}
	}
	
	public boolean isAsleep(){
		return currentSleepTime > 0;
	}
	
	public void updateShape(Matrix4 mat){
		sleepShape.setProjectionMatrix(mat);
	}
	
	public boolean canAddGem(){
		return getNbLocations() > gems.size();
	}
	
	public void upgrade(){
		level++;
	}
	
	protected void setPurity(){
		boolean pure = false;
		int nb = 0;
		for(int nbGem : nbGems){
			if(nbGem > 0){
				if(nbGem > 2) pure = true;
				nb++;
				if(nb > 2) break;
			}
		}
		
		isPure = (pure && nb == 1);
	}

	@Override
	public float computeCost(int nbObject) {
		float cost = (float) (100 * Math.pow(1.17, nbObject));
		return cost;
	}
	
}
