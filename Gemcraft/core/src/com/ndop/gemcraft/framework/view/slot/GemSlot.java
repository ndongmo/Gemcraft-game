package com.ndop.gemcraft.framework.view.slot;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.model.Gem;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.view.MyMap;
import com.ndop.gemcraft.framework.view.panel.InventoryPanel;
import com.ndop.gemcraft.framework.view.screen.GameScreen;

/**
* Slot contenant une gemme de l'inventaire du joueur.
* @author --
* @version 1.0
*/
public class GemSlot extends Slot<Gem>{

	public GemSlot(Gem object, InventoryPanel panel) {
		super(object, panel);
		this.addListener(new ShopListener());
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
		object.setPosition(this.getX(), this.getY());
		object.draw(batch);
	}
	
	private class ShopListener extends ClickListener{
		@Override
	    public void clicked(InputEvent event, float x, float y) {
			((GameScreen)panel.parent).hideDialogBox();
			Sprite sprite = ((GameScreen)panel.parent).getSelectedObject();

			if(sprite == null){
				((GameScreen)panel.parent).setSelectedObject(object);
				((InventoryPanel)panel).removeGem(GemSlot.this);
				ResourceManager.playSound(SoundEffect.PICK);
			}
			else if(sprite instanceof Gem){
				sprite.setSize(MyMap.TILE_SIZE, MyMap.TILE_SIZE);
				((InventoryPanel)panel).addGem((Gem)sprite);
				((GameScreen)panel.parent).setSelectedObject(null);
			}
	    }
	}
	
}
