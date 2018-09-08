package com.ndop.gemcraft.framework.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.model.Gem;
import com.ndop.gemcraft.framework.model.IGameInteractions;
import com.ndop.gemcraft.framework.model.MyGridCell;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.model.Tower;
import com.ndop.gemcraft.framework.model.Trap;
import com.ndop.gemcraft.framework.view.panel.TowerPanelBox;
import com.ndop.gemcraft.framework.view.screen.GameScreen;

/**
 * Classe contenant la classe d'un niveau. 
 * Elle est chargée d'afficher la carte et de gérer les différents accès à ce dernier.
 * @author --
 * @version 1.0
 */
public class MyMap extends Actor{
	/**
	 * Taille d'une case.
	 */
	public static final int TILE_SIZE = 32;
	/**
	 * carte du niveau courant.
	 */
	private TiledMapTileLayer mapLayer;
	/**
	 * Echelle d'une tile convertie aux dimensions de notre conteneur.
	 */
	private float xScale, yScale;
	private ShapeRenderer shape;
	private Tower[][] towers;
	private MyGridCell[][] cells;
	private boolean gemAdd, towerAdd, trapAdd, over;
	private TextureRegion disabled;
	private GameScreen parent;
	private TowerPanelBox dialogBox;

	public MyMap(GameScreen parent){
		super();
		this.parent = parent;
		this.over = false;
		this.gemAdd = false;
		this.towerAdd = false;
		this.shape = new ShapeRenderer();
		this.shape.setColor(Color.BLACK);
		int x = 11 * MyMap.TILE_SIZE;
		int y = 5 * MyMap.TILE_SIZE;
		int w = MyMap.TILE_SIZE;
		int h = MyMap.TILE_SIZE;
		this.disabled = new TextureRegion(ResourceManager.texture, x, y, w, h);

		this.dialogBox = new TowerPanelBox(parent);
		parent.getStage().addActor(dialogBox);
		dialogBox.setVisible(false);
		this.addListener(new MapListener());
	}
	
	@Override
	public void setBounds(float x, float y, float width, float height){
		super.setBounds(x, y, width, height);
		if(mapLayer != null){
			xScale = width / mapLayer.getWidth();
			yScale = height /mapLayer.getHeight();
			shape.setProjectionMatrix(getStage().getCamera().combined);
		}
	}

	/**
	 * Initialise la carte pour le niveau courant.
	 */
	public void initMap(String mapName){
		mapName = (mapName.endsWith(".tmx")) ? "levels/"+mapName : "levels/"+mapName+".tmx";
		mapLayer = (TiledMapTileLayer) new TmxMapLoader(new LocalFileHandleResolver()).load(mapName).getLayers().get(0);
		xScale = this.getWidth() / mapLayer.getWidth();
		yScale = getHeight()/mapLayer.getHeight();
		towers = new Tower[mapLayer.getWidth()] [mapLayer.getHeight()];
		cells = new MyGridCell[mapLayer.getWidth()][mapLayer.getHeight()];
		
		for(int i=0; i<mapLayer.getWidth(); i++){
			for(int j=0; j<mapLayer.getHeight(); j++){
				TiledMapTile tile = mapLayer.getCell(i, j).getTile();

				if(tile.getProperties().get("path", "0", String.class).equals("1") || 
						tile.getProperties().get("nexus", "0", String.class).equals("1")){
					cells[i][j] = new MyGridCell(i, j, true);
				}
				else
					cells[i][j] = new MyGridCell(i, j, false);
			}
		}
	}
	
	/**
	 * Vérifie si le joueur peut ajouter une tour à une position (x, y).
	 * @param i coordonnée x
	 * @param j coordonnée y
	 * @return vrai si la position accepte la tour, faux sinon
	 */
	private boolean canAddTower(int i, int j){
		boolean b = mapLayer.getCell(i, j).getTile().getProperties().get("buildable", "0", String.class).equals("1");
		return (!towerAdd || (towers[i][j] == null && b));
	}

	/**
	 * Vérifie si le joueur peut ajouter un piège à une position (x, y).
	 * @param i coordonnée x
	 * @param j coordonnée y
	 * @return vrai si la position accepte la tour, faux sinon
	 */
	private boolean canAddTrap(int i, int j){
		boolean b = mapLayer.getCell(i, j).getTile().getProperties().get("path", "0", String.class).equals("1");
		return (!trapAdd || (towers[i][j] == null && b));
	}

	/**
	 * Vérifie si le joueur peut ajouter une gemme às la tour se touvant à la position (x, y).
	 * @param i coordonnée x
	 * @param j coordonnée y
	 * @return vrai si le tile à la position contient une tour qui accepte une gemme, faux sinon
	 */
	private boolean canAddGem(int i, int j){
		return (!gemAdd || (towers[i][j] != null && towers[i][j].canAddGem()));
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha);

		float x = this.getX(), y = this.getY();

		for(int i=0; i<mapLayer.getWidth(); i++){
			for(int j=0; j<mapLayer.getHeight(); j++){
				TiledMapTile tile = mapLayer.getCell(i, j).getTile();

				if(towers[i][j] != null){
					towers[i][j].setBounds(x, y, xScale, yScale);
					towers[i][j].draw(batch, parentAlpha);
				}
				else
					batch.draw(tile.getTextureRegion(), x, y, xScale, yScale);

				if(! canAddTower(i, j) || ! canAddTrap(i, j) || ! canAddGem(i, j)){
					batch.draw(disabled, x, y, xScale, yScale+1);
				}

				y += yScale;
			}
			x += xScale;
			y = getY();
		}

		batch.end();
		shape.begin(ShapeType.Line);
		shape.rect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		shape.end();
		batch.begin();
	}

	/**
	 * Obtient la tour à la position du curseur.
	 * @return la tour s'il y a une tour à la position du curseur, null sinon.
	 */
	private Tower getMousePos(){
		Vector3 point = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		parent.getStage().getCamera().unproject(point);
		point.set(point.x-this.getX(), point.y-this.getY(), 0);

		int a = (int)((point.x+(xScale/2))/xScale);
		int b = (int)(point.y/yScale);

		if(a < towers.length && b < towers[0].length)
			return towers[a][b];
		return null;
	}

	private class MapListener extends ClickListener{
		@Override
		public void clicked(InputEvent event, float x, float y) {
			Sprite sprite = parent.getSelectedObject();
			parent.hideDialogBox();
			over = true;
			int i = (int)((x+(xScale/2))/xScale);
			int j = (int)(y/yScale);

			if(sprite != null){
				boolean add = false;
				if(sprite instanceof Gem && canAddGem(i, j)){
					gemAdd = false;
					towers[i][j].addGem((Gem)sprite);
					parent.setSelectedObject(null);
					add = true;
				}
				else if(sprite instanceof Trap){
					if(canAddTrap(i, j)){
						trapAdd = false;
						towers[i][j] = (Trap)sprite;
						towers[i][j].updateClosestCells(cells[i][j], (IGameInteractions)parent);
						parent.getTowers().add((Trap)sprite);
						parent.setSelectedObject(null);
						add = true;
					}
				}
				else if(sprite instanceof Tower && canAddTower(i, j)){
					towerAdd = false;
					towers[i][j] = (Tower)sprite;
					towers[i][j].updateClosestCells(cells[i][j], (IGameInteractions)parent);
					parent.getTowers().add((Tower)sprite);
					parent.setSelectedObject(null);
					add = true;
				}
				
				if(add) ResourceManager.playSound(SoundEffect.DROP);
				else ResourceManager.playSound(SoundEffect.ERROR);
			}
			else if(towers[i][j] != null){
				parent.setSelectedObject(towers[i][j]);
				parent.getTowers().remove(towers[i][j]);
				if(towers[i][j] instanceof Trap)
					trapAdd = true;
				else
					towerAdd = true;

				towers[i][j] = null;
				ResourceManager.playSound(SoundEffect.PICK);
			}
		}
		
		@Override
		public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
			over = true;
			updateState();
		}
		
		@Override
		public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
			if(toActor != MyMap.this){
				gemAdd = false;
				trapAdd = false;
				towerAdd = false;
				over = false;
			}
		}
	}

	public void drawDialogBox(){
		Tower tower = null;
		if(dialogBox.isVisible())
			dialogBox.setVisible(false);
		else if(over && !gemAdd && !towerAdd && !trapAdd && (tower = getMousePos()) != null){
			Vector3 point = new Vector3(Gdx.input.getX()-this.getX(), Gdx.input.getY()+(yScale)-this.getY(), 0);
			parent.getStage().getCamera().unproject(point);
			dialogBox.init(tower);
			dialogBox.setVisible(true);
			dialogBox.setPosition(point.x, point.y);			
		}
	}

	/**
	 * Met à jour l'état du curseur sur la map.
	 */
	public void updateState(){
		Sprite sprite = parent.getSelectedObject();
		if(sprite != null){
			if(sprite instanceof Gem)
				gemAdd = true;
			else if(sprite instanceof Trap)
				trapAdd = true;
			else if(sprite instanceof Tower)
				towerAdd = true;
		}
		else{
			gemAdd = false;
			trapAdd = false;
			towerAdd = false;
		}
	}

	public MyGridCell[][] getCells(){
		return cells;
	}

	public TowerPanelBox getDialogBox(){
		return dialogBox;
	}

	public Vector2 scale(){
		return new Vector2(xScale, yScale);
	}

	public float getXscale(){
		return xScale;
	}

	public float getYscale(){
		return yScale;
	}
}
