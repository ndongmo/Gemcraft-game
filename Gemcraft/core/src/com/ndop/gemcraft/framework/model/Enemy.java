package com.ndop.gemcraft.framework.model;

import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.ndop.gemcraft.framework.model.gemsActions.BasicBlueAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicCyanAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicGreenAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicMagentaAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicOrangeAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicPurpleAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicRedAction;
import com.ndop.gemcraft.framework.model.gemsActions.BasicYellowAction;
import com.ndop.gemcraft.framework.model.gemsActions.GemAction;
import com.ndop.gemcraft.framework.view.MyMap;

/**
 * Classe représentant un ennemi dans le jeu.
 * @author --
 * @version 1.0
 */
public class Enemy implements Poolable, IUpdate{
	/**
	 * Différnts types d'ennemi.
	 * @author --
	 *
	 */
	public enum EnemyType{
		FLY,
		FAST,
		BOSS,
		STRONG,
		MARTIAN,
		UNDEAD,
		VAMPIRE;
	}
	/**
	 * Nombre de colonnes et lignes de la texture d'animation.
	 */
	private static final int FRAME_COLS = 3, FRAME_ROWS = 4;
	/**
	 * Durré de l'animation de mort.
	 */
	private static final int DEADTIME = 500;
	/**
	 * Temps depuis la mort de l'enemi.
	 */
	private float currentDeadTime;
	/**
	 * Nom de l'ennemi.
	 */
	private EnemyType type;
	/**
	 * Mana et vie de l'ennemi.
	 */
	private float mana, initLife, life;
	/**
	 * Taille de l'ennemi.
	 */
	private float size;
	/**
	 * Contour.
	 */
	private Rectangle bound = new Rectangle();
	/**
	 * Vitesse réelle ou initiale de l'ennemi.
	 */
	private float realVelocity;
	/**
	 * Tableau représentant les immunités de l'ennemi aux différents gemmes.
	 */
	private boolean[] immunity = new boolean[GemColor.values().length];
	/**
	 * Liste des actions des projectiles recus.
	 */
	private GemAction[] actions = new GemAction[GemColor.values().length];
	/**
	 * Vitesse actuelle de l'ennemi.
	 */
	private float currentVelocity;
	/**
	 * Position précédente sur la carte.
	 */
	private Vector2 previousPos;
	/**
	 * Sentier que va suivre l'ennemi.
	 */
	private Spawn spawn;
	/**
	 * Animation de déplacement.
	 */
	private Animation walk[];
	/**
	 * Animation de d'explosion.
	 */
	private Animation explode;
	/**
	 * Index de la case courante où se trouve l'ennemi sur le sentier.
	 */
	private int cellIndex;
	/**
	 * position dans la case courante. gridMovePos représente le mouvement dans le sens de la prochaine
	 *  case où se dirige l'ennemi et randomMovePos représente le mouvement dans le sens perpendiculaire à gridMovePos.
	 */
	private float gridMovePos, randomMovePos;
	/**
	 * Mouvement courant de l'ennemi.
	 */
	private Move currentMove;
	/**
	 * Dégâts courant.
	 */
	private float damage;
	private Random random = new Random();
	
	public Enemy(){
		reset();
	}
	/**
	 * Initialise un ennemi à partir des paramètres d'un autre.
	 * @param e ennemi à copier
	 */
	public void setEnemy(Enemy e){
		mana = e.mana;
		initLife = e.life;
		life = e.life;
		size = e.size;
		type = e.type;
		realVelocity = e.realVelocity;
		currentDeadTime = e.currentDeadTime;
		currentVelocity = e.currentVelocity;
		previousPos = e.previousPos;
		for(int i=0; i<e.immunity.length; i++){
			immunity[i] = e.immunity[i];
		}
		
		initActions();
		
		Texture texture = ResourceManager.texture;
        TextureRegion[][] tmp = TextureRegion.split(texture, MyMap.TILE_SIZE, MyMap.TILE_SIZE);
        walk = new Animation[Move.values().length];
        
        int initRow = 1, initCol = type.ordinal()*FRAME_COLS;
        for (int i = initRow; i < FRAME_ROWS+initRow; i++) {
        	TextureRegion[] frames = new TextureRegion[FRAME_COLS];
        	int index = 0;
            for (int j = initCol; j < FRAME_COLS+initCol; j++) {
            	frames[index++] = tmp[i][j];
            }
            walk[i-initRow] = new Animation(0.15f, frames);
        }
        
        TextureRegion[] frames = new TextureRegion[5];
        for (int j = 0; j < 5; j++) {
        	frames[j] = tmp[6][j];
        }
        explode = new Animation(0.15f, frames);
	}
	
	/**
	 * Incrémente les dégâts recus.
	 * @param damage
	 */
	public void receiveDamage(float damage){
		this.damage += damage;
	}
	
	/**
	 * Encaisse les dégâts recus.
	 */
	public void processDamage(){
		life -= damage;
		damage = 0;
	}
	
	@Override
	public void reset() {
		type = EnemyType.FLY;
		mana = 0;
		life = 0;
		size = 0;
		damage = 0;
		initLife = 0;
		currentVelocity = realVelocity = 0;
		currentDeadTime = DEADTIME;
		previousPos = new Vector2();
		bound.set(0, 0, 0, 0);
		Arrays.fill(immunity, false);
		initActions();
	}
	
	@Override
	public void update(float deltaTime) {
		float gridMove = currentVelocity * deltaTime;
		gridMovePos += gridMove;
		if(gridMovePos > 1){
			if(cellIndex+1 < spawn.getCells().size()){
				spawn.getCells().get(cellIndex).getEnemies().remove(this);
				cellIndex++;
				spawn.getCells().get(cellIndex).getEnemies().add(this);
			}
			gridMovePos = gridMovePos - 1;
			nextMove();
		}
		
		
		float tmp = randomMovePos + ((random.nextInt(3)-1) * gridMove);
		if(tmp < 1 && tmp > 0)
			randomMovePos = tmp;
	}
	
	/**
	 * Initialise la position de l'ennemi.
	 */
	public void initPosition(){
		cellIndex = 0;
		gridMovePos = 0;
		randomMovePos = random.nextInt(3) * ((1-size)/2);
		randomMovePos += (randomMovePos == 0)?((1-size)/2): -((1-size)/2);
		spawn.getCells().get(cellIndex).getEnemies().add(this);
		nextMove();
	}
	
	public void dead(){
		spawn.getCells().get(cellIndex).getEnemies().remove(this);
	}
	
	/**
	 * Initialise les actions des gemmes.
	 */
	private void initActions(){
		actions[GemColor.RED.ordinal()] = new BasicRedAction();
		actions[GemColor.ORANGE.ordinal()] = new BasicOrangeAction();
		actions[GemColor.YELLOW.ordinal()] = new BasicYellowAction();
		actions[GemColor.GREEN.ordinal()] = new BasicGreenAction();
		actions[GemColor.CYAN.ordinal()] = new BasicCyanAction();
		actions[GemColor.BLUE.ordinal()] = new BasicBlueAction();
		actions[GemColor.MAGENTA.ordinal()] = new BasicMagentaAction();
		actions[GemColor.PURPLE.ordinal()] = new BasicPurpleAction();
	}
	
	/**
	 * Calcul le prochain mouvement.
	 */
	private void nextMove(){
		Move tmpMove = currentMove;
		
		if(cellIndex+1 < spawn.getCells().size()){
			int x = spawn.getCells().get(cellIndex+1).x - spawn.getCells().get(cellIndex).x;
			int y = spawn.getCells().get(cellIndex+1).y - spawn.getCells().get(cellIndex).y;
			currentMove = Move.getMove(x, y);
		}
		else
			currentMove = Move.LEFT;
		
		// on permutte les mouvements dans les deux direction pour conserver la cohérence dans le déplacement 
		if(tmpMove != currentMove){
			float tmpValue = randomMovePos;
			randomMovePos = gridMovePos;
			gridMovePos = tmpValue;
		}
	}
	
	/**
	 * Récupère la texture du prochain mouvement.
	 * @param elapsedTime temps écoulé.
	 * @return image du prochain mouvement.
	 */
	public TextureRegion getSprite(float elapsedTime){
		if(isAlive())
			return walk[currentMove.ordinal()].getKeyFrame(elapsedTime * currentVelocity, true);
		else{
			 currentDeadTime -= elapsedTime;
			 return explode.getKeyFrame(elapsedTime, true);
		}
	}
	
	public float getgridMovePosX(){
		return (gridMovePos * currentMove.x) + (randomMovePos * Math.abs(currentMove.y));
	}
	
	public float getgridMovePosY(){
		return (gridMovePos * currentMove.y) + (randomMovePos * Math.abs(currentMove.x));
	}
	
	public int getX(){
		return spawn.getCells().get(cellIndex).x;
	}
	
	public int getY(){
		return spawn.getCells().get(cellIndex).y;
	}
	
	public boolean isAlive(){
		return life > 0;
	}

	public float getMana() {
		return mana;
	}

	public void setMana(float mana) {
		this.mana = mana;
	}

	public EnemyType getName() {
		return type;
	}

	public void setType(EnemyType type) {
		this.type = type;
	}

	public float getLife() {
		return life;
	}

	public void setLife(float life) {
		this.life = life;
	}
	
	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float getRealVelocity() {
		return realVelocity;
	}

	public void setRealVelocity(float realVelocity) {
		this.realVelocity = realVelocity;
	}

	public boolean[] getImmunity() {
		return immunity;
	}

	public void setImmunity(GemColor gc, boolean value) {
		this.immunity[gc.ordinal()] = value;
	}

	public float getCurrentVelocity() {
		return currentVelocity;
	}

	public void setCurrentVelocity(float currentVelocity) {
		this.currentVelocity = currentVelocity;
	}

	public void setSpawn(Spawn spawn) {
		this.spawn = spawn;
	}
	
	public Spawn getSpawn(){
		return spawn;
	}
	public int getCellIndex() {
		return cellIndex;
	}
	
	public float getDamage() {
		return damage;
	}
	public void setDamage(float damage) {
		this.damage = damage;
	}
	
	public GemAction[] getActions(){
		return actions;
	}
	public float getInitLife() {
		return initLife;
	}
	public Rectangle getBound() {
		return bound;
	}
	public void setBound(float x, float y, float w, float h) {
		this.bound.set(x, y, w, h);
	}
	public boolean canDispose(){
		return  currentDeadTime <= 0;
	}
		
}
