package com.ndop.gemcraft.framework.view.slot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.model.Player;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.model.Tower;
import com.ndop.gemcraft.framework.view.MyMap;
import com.ndop.gemcraft.framework.view.panel.InventoryPanel;
import com.ndop.gemcraft.framework.view.screen.GameScreen;

/**
 * Slot d'achat d'une tour.
 * @author --
 * @version 1.0
 */
public class TowerShopSlot extends Slot<Tower>{

	/**
	 * Etat de l'affichage du prix.
	 */
	private boolean showCost = false;
	private TextureRegion disabled;
	private Label lb_cost;

	public TowerShopSlot(Tower object, InventoryPanel panel) {
		super(object, panel);
		this.lb_cost = new Label("", ResourceManager.skin, "arial-small", Color.WHITE);
		int x = 11 * MyMap.TILE_SIZE;
		int y = 5 * MyMap.TILE_SIZE;
		int w = MyMap.TILE_SIZE;
		int h = MyMap.TILE_SIZE;
		this.disabled = new TextureRegion(ResourceManager.texture, x, y, w, h);
		this.addListener(new ShopListener());
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		GameScreen screen = ((GameScreen)panel.parent);

		object.setPosition(this.getX(), this.getY());
		object.draw(batch);

		if(screen.getSelectedObject() != null || screen.getPlayer().getMana() < towerCost())
			batch.draw(disabled, this.getX(), this.getY());

		if(showCost){
			lb_cost.setText(towerCost()+"");
			lb_cost.setPosition(this.getX()+(MyMap.TILE_SIZE/6), this.getY()+(MyMap.TILE_SIZE+5));
			lb_cost.draw(batch, parentAlpha);
		}
	}

	/**
	 * Calcul le coût d'une tour.
	 * @return le coût de la tour.
	 */
	private int towerCost(){
		int nbTower = ((InventoryPanel)panel).getTowerSlots().size()+((GameScreen)panel.parent).getTowers().size();
		return (int) object.computeCost(nbTower);
	}

	private class ShopListener extends ClickListener{
		@Override
		public void clicked(InputEvent event, float x, float y) {
			((GameScreen)panel.parent).hideDialogBox();
			if(((GameScreen)panel.parent).getSelectedObject() == null){
				Player player = ((GameScreen)panel.parent).getPlayer();
				float cost = towerCost();
				if(player.getMana() >= cost){
					player.setMana(player.getMana()- cost);
					Tower tower = object.createNew();
					((InventoryPanel)panel).addTower(tower);
					ResourceManager.playSound(SoundEffect.COIN);
				}
				else
					ResourceManager.playSound(SoundEffect.ERROR);
			}
		}
		@Override
		public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
			if(((GameScreen)panel.parent).getSelectedObject() == null)
				showCost = true;
		}
		@Override
		public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
			showCost = false;
		}
	}
}
