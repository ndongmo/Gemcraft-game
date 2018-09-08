package com.ndop.gemcraft.framework.view.panel;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ndop.gemcraft.framework.model.Gem;
import com.ndop.gemcraft.framework.model.GemColor;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.Tower;
import com.ndop.gemcraft.framework.model.Trap;
import com.ndop.gemcraft.framework.view.MyMap;
import com.ndop.gemcraft.framework.view.screen.MyScreen;
import com.ndop.gemcraft.framework.view.slot.GemShopSlot;
import com.ndop.gemcraft.framework.view.slot.GemSlot;
import com.ndop.gemcraft.framework.view.slot.TowerShopSlot;
import com.ndop.gemcraft.framework.view.slot.TowerSlot;

/**
 * Panneau d'affichage de l'inventaire du joueur et des objets vendus.
 * @author --
 * @version 1.0
 */
public class InventoryPanel extends MyPanel{
	/**
	 * Nombre de colonne de l'inventaire.
	 */
	private static final int MARGIN = 10;
	/**
	 * Nombre de colonne de l'inventaire.
	 */
	private static final int COLUMNS = 5;

	/**
	 * Taille des différents box de l'inventaire.
	 */
	private static final int WIDTH = MyMap.TILE_SIZE * COLUMNS + (MARGIN*2)+20;
	private static final int GEMSHOP_HEIGHT = (MyMap.TILE_SIZE * 2) + MARGIN ;
	private static final int TOWERSHOP_HEIGHT = (MyMap.TILE_SIZE * 1) + MARGIN;
	private static final int GEM_HEIGHT = (MyMap.TILE_SIZE * 4) + MARGIN;
	private static final int TOWER_HEIGHT = (MyMap.TILE_SIZE * 2) + MARGIN;
	/**
	 * Table des gemmes vendues.
	 */
	private Table tb_gemShop;
	/**
	 * Table des tours vendues
	 */
	private Table tb_towerShop;
	/**
	 * Table des gemmes possedées par le joueur.
	 */
	private Table tb_gemOwner;
	/**
	 * Table des tours possedées par le joueur.
	 */
	private Table tb_towerOwner;
	/**
	 * Liste des gemmes de l'inventaire du joueur.
	 */
	private ArrayList<GemSlot> gemSlots = new ArrayList<GemSlot>();
	/**
	 * Liste des tours de l'inventaire du joueur.
	 */
	private ArrayList<TowerSlot> towerSlots = new ArrayList<TowerSlot>();

	public InventoryPanel(MyScreen parent) {
		super(parent);
	}

	@Override
	public void initPanel() {
		//this.setDebug(true);

		tb_gemShop = new Table();
		tb_towerShop = new Table();
		tb_gemOwner = new Table();
		tb_towerOwner = new Table();

		tb_gemShop.setBackground(ResourceManager.skin.getDrawable("default-scroll"));
		tb_towerShop.setBackground(ResourceManager.skin.getDrawable("default-scroll"));
		tb_gemOwner.setBackground(ResourceManager.skin.getDrawable("default-scroll"));
		tb_towerOwner.setBackground(ResourceManager.skin.getDrawable("default-scroll"));

		ScrollPane sc_gemOwner = new ScrollPane(tb_gemOwner, ResourceManager.skin,"mine");
		sc_gemOwner.setOverscroll(false, false);
		sc_gemOwner.setFadeScrollBars(false);
		sc_gemOwner.setScrollingDisabled(true, false);

		ScrollPane sc_towerOwner = new ScrollPane(tb_towerOwner, ResourceManager.skin,"mine");
		sc_towerOwner.setOverscroll(false, false);
		sc_towerOwner.setFadeScrollBars(false);
		sc_towerOwner.setScrollingDisabled(true, false);

		this.add(tb_towerShop).fill().expand().size(WIDTH, TOWERSHOP_HEIGHT).padBottom(MARGIN).row();
		this.add(tb_gemShop).fill().expand().size(WIDTH, GEMSHOP_HEIGHT).padBottom(MARGIN).row();
		this.add(sc_towerOwner).fill().expand().size(WIDTH, TOWER_HEIGHT).padBottom(MARGIN).row();
		this.add(sc_gemOwner).fill().expand().size(WIDTH, GEM_HEIGHT);

		Tower tower = ResourceManager.levelFactory.createTower();
		Trap trap = ResourceManager.levelFactory.createTrap();
		TowerShopSlot tss = new TowerShopSlot(tower, this);
		TowerShopSlot trss = new TowerShopSlot(trap, this);
		tb_towerShop.add(tss).size(MyMap.TILE_SIZE, MyMap.TILE_SIZE);
		tb_towerShop.add(trss).size(MyMap.TILE_SIZE, MyMap.TILE_SIZE);

		for(int i=0; i<GemColor.values().length; i++){
			if(i != 0 && i % COLUMNS == 0)
				tb_gemShop.row();

			GemColor gc = GemColor.values()[i];
			Gem gem = ResourceManager.levelFactory.createGem(gc);
			GemShopSlot slot = new GemShopSlot(gem, this);
			tb_gemShop.add(slot).size(MyMap.TILE_SIZE, MyMap.TILE_SIZE);
		}
	}

	/**
	 * Initialise l'inventaire pour le niveau courant.
	 * @param gems liste des gemmes initiales du niveau.
	 * @param towers liste des gemmes initiales du niveau.
	 */
	public void initInventory(ArrayList<Gem> gems, ArrayList<Tower> towers){
		towerSlots.clear();
		gemSlots.clear();
		tb_towerOwner.clearChildren();
		tb_gemOwner.clearChildren();

		for(Tower tower : towers)
			addTower(tower);

		for(Gem gem : gems)
			addGem(gem);
	}

	/**
	 * Ajoute une gemme dans l'inventaire des gemmes.
	 * @param gem gemme à ajouter.
	 */
	public void addGem(Gem gem){
		GemSlot slot = new GemSlot(gem, this);
		gemSlots.add(slot);
		updateGemOwner();
	}

	/**
	 * Ajoute une tour dans l'inventaire des tour.
	 * @param tower tour à ajouter.
	 */
	public void addTower(Tower tower){
		TowerSlot slot = new TowerSlot(tower, this);
		towerSlots.add(slot);
		updateTowerOwner();
	}

	/**
	 * Ajoute une gemme dans l'inventaire des gemmes.
	 * @param gem gemme à ajouter.
	 */
	public void removeGem(GemSlot slot){
		gemSlots.remove(slot);
		updateGemOwner();
	}

	/**
	 * Retire une tour dans l'inventaire des tour.
	 * @param slot de la tour à retirer.
	 */
	public void removeTower(TowerSlot slot){
		towerSlots.remove(slot);
		updateTowerOwner();
	}

	/**
	 * Met à jour la table d'inventaire des gemmes du joueur.
	 * Notons que nous vidons la table car l'ajout d'un Actor cause 
	 * automatiquement la création d'une nouvelle ligne dans la table.
	 */
	public void updateGemOwner(){
		tb_gemOwner.clearChildren();
		for(int i=0; i<gemSlots.size(); i++){
			if(i != 0 && i % COLUMNS == 0)
				tb_gemOwner.row();

			tb_gemOwner.add(gemSlots.get(i)).size(MyMap.TILE_SIZE, MyMap.TILE_SIZE);
		}
	}

	/**
	 * Met à jour la table d'inventaire des tours du joueur.
	 */
	public void updateTowerOwner(){
		tb_towerOwner.clearChildren();
		for(int i=0; i<towerSlots.size(); i++){
			if(i != 0 && i % COLUMNS == 0)
				tb_towerOwner.row();

			tb_towerOwner.add(towerSlots.get(i)).size(MyMap.TILE_SIZE, MyMap.TILE_SIZE);
		}
	}

	public float getWidth(){
		return WIDTH + (MARGIN*2);
	}

	public float getHeight(){
		return (TOWERSHOP_HEIGHT + TOWER_HEIGHT + GEMSHOP_HEIGHT + GEM_HEIGHT) + (MARGIN*3);
	}

	public ArrayList<GemSlot> getGemSlots() {
		return gemSlots;
	}

	public ArrayList<TowerSlot> getTowerSlots() {
		return towerSlots;
	}

}
