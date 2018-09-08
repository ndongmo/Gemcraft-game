package com.ndop.gemcraft.framework.view.slot;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.model.Gem;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.view.MyMap;
import com.ndop.gemcraft.framework.view.panel.TowerPanelBox;
import com.ndop.gemcraft.framework.view.screen.GameScreen;

/**
* Slot contenant une gemme contenue dans une tour.
* @author --
* @version 1.0
*/
public class MapGemSlot extends Slot<Gem>{

	public MapGemSlot(Gem object, TowerPanelBox panel) {
		super(object, panel);
		this.addListener(new RemoveListener());
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
		object.setBounds(this.getX(), this.getY(), MyMap.TILE_SIZE, MyMap.TILE_SIZE);
		object.draw(batch);
	}
	
	private class RemoveListener extends ClickListener{
		@Override
	    public void clicked(InputEvent event, float x, float y) {
			ResourceManager.playSound(SoundEffect.DROP);
			object.setSize(MyMap.TILE_SIZE, MyMap.TILE_SIZE);
			((TowerPanelBox)panel).getTower().remove(object);
			((GameScreen)panel.parent).getInventoryPanel().addGem(object);
			((TowerPanelBox)panel).updateGems();
	    }
	}
	
}

