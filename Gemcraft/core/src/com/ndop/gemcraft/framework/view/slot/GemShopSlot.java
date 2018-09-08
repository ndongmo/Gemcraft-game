package com.ndop.gemcraft.framework.view.slot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.model.Gem;
import com.ndop.gemcraft.framework.model.Player;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.model.Tower;
import com.ndop.gemcraft.framework.view.MyMap;
import com.ndop.gemcraft.framework.view.panel.InventoryPanel;
import com.ndop.gemcraft.framework.view.screen.GameScreen;

/**
 * Slot d'achat d'une gemme.
 * @author --
 * @version 1.0
 */
public class GemShopSlot extends Slot<Gem>{

	/**
	 * Etat de l'affichage du prix.
	 */
	private boolean showCost = false;
	private TextureRegion disabled;
	private Label lb_cost;
	private int cost;

	public GemShopSlot(Gem object, InventoryPanel panel) {
		super(object, panel);
		
		this.lb_cost = new Label("", ResourceManager.skin, "arial-small", Color.WHITE);
		int x = 10 * MyMap.TILE_SIZE;
		int y = 5 * MyMap.TILE_SIZE;
		int w = MyMap.TILE_SIZE;
		int h = MyMap.TILE_SIZE;
		this.disabled = new TextureRegion(ResourceManager.texture, x, y, w, h);
		this.addListener(new ShopListener());
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		GameScreen screen = ((GameScreen)panel.parent);
		cost =  (int) object.computeCost(getNbGem());
		
		object.setPosition(this.getX(), this.getY());
		object.draw(batch);
		if(screen.getSelectedObject() != null || screen.getPlayer().getMana() < cost)
			batch.draw(disabled, this.getX(), this.getY());
		if(showCost){
			lb_cost.setText(cost+"");
			lb_cost.setPosition(this.getX()+(MyMap.TILE_SIZE/3), this.getY()+(MyMap.TILE_SIZE));
			lb_cost.draw(batch, parentAlpha);
		}
	}

	private class ShopListener extends ClickListener{
		@Override
		public void clicked(InputEvent event, float x, float y) {
			
			((GameScreen)panel.parent).hideDialogBox();
			if(((GameScreen)panel.parent).getSelectedObject() == null){
				Player player = ((GameScreen)panel.parent).getPlayer();
				if(player.getMana() >= cost){
					player.setMana(player.getMana()- cost);
					Gem gem = ResourceManager.levelFactory.createGem(object.getGemColor());
					((InventoryPanel)panel).addGem(gem);
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
	
	public int getNbGem(){
		int nbGem = ((InventoryPanel)panel).getGemSlots().size();
		for(Tower t : ((GameScreen)panel.parent).getTowers()){
			nbGem += t.getGems().size();
		}
		return nbGem;
	}

}
