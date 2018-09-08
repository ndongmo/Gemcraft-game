package com.ndop.gemcraft.framework.view.slot;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.model.Tower;
import com.ndop.gemcraft.framework.view.MyMap;
import com.ndop.gemcraft.framework.view.panel.InventoryPanel;
import com.ndop.gemcraft.framework.view.screen.GameScreen;

public class TowerSlot extends Slot<Tower>{

	public TowerSlot(Tower object, InventoryPanel panel) {
		super(object, panel);
		this.addListener(new ShopListener());
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
		object.setPosition(this.getX(), this.getY());
		object.draw(batch, parentAlpha);
	}

	private class ShopListener extends ClickListener{
		@Override
		public void clicked(InputEvent event, float x, float y) {
			((GameScreen)panel.parent).hideDialogBox();
			Sprite sprite = ((GameScreen)panel.parent).getSelectedObject();

			if(sprite == null){
				((GameScreen)panel.parent).setSelectedObject(object);
				((InventoryPanel)panel).removeTower(TowerSlot.this);
				ResourceManager.playSound(SoundEffect.PICK);
			}
			else if(sprite instanceof Tower){
				sprite.setSize(MyMap.TILE_SIZE, MyMap.TILE_SIZE);
				((InventoryPanel)panel).addTower((Tower)sprite);
				((GameScreen)panel.parent).setSelectedObject(null);
			}
		}
	}

}
