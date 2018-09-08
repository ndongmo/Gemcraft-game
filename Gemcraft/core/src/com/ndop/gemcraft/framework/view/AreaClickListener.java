package com.ndop.gemcraft.framework.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.view.panel.MyPanel;

/**
 * Classe interne qui gère le click sur la zone de texte et démarre le mode campagne.
 * @author --
 *
 */
public class AreaClickListener extends ClickListener {
	private MyPanel panel;
	
	public AreaClickListener(MyPanel panel){
		this.panel = panel;
	}
	
	@Override
    public void clicked(InputEvent event, float x, float y) {
		ResourceManager.playSound(SoundEffect.CLICK);
		panel.updatePanel(event.getListenerActor());
    }
	@Override
	public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
		ResourceManager.setCursor("data/cursor_hand1.png", 12, 0);
	}
	@Override
	public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
		if(toActor != event.getTarget()){
			ResourceManager.setDefaultCursor();
		}
	}
}