package com.ndop.gemcraft.framework.view.panel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.view.screen.GameScreen;
import com.ndop.gemcraft.framework.view.screen.MyScreen;

/**
 * Panneau d'affichage lors du chargement d'un niveau.
 * @author --
 * @version 
 */
public class LoadingPanel extends MyPanel{
	/**
	 * Temps de chargement par défaut.
	 */
	private static final float TIME = 5;
    /**
     * Temps écoulé.
     */
    private float elapsedTime; 
    /**
     * Chargement terminé.
     */
    private boolean loadOK;
    /**
     * Label de chargement.
     */
    private Label lb_title, lb_load;
    private TextArea ta_text;
    
	public LoadingPanel(MyScreen parent) {
		super(parent);
	}

	@Override
	public void initPanel() {
		lb_title = new Label("", ResourceManager.skin, "title");
		ta_text = new TextArea("", ResourceManager.skin);
		ta_text.setDisabled(true);
		
        lb_load = new Label(ResourceManager.bundle.get("chargement"), ResourceManager.skin, "default-font", Color.GOLD);
        this.setFillParent(true);
		
		this.add(lb_title).padBottom(20).row();
        this.add(ta_text).expand().fill().row();
        this.add(lb_load).left().bottom();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		int nb = (int)elapsedTime % 4;
		String points = ResourceManager.bundle.get("chargement");
		for(int i=0; i<nb; i++) points+=" .";
		lb_load.setText(points);
		
		super.draw(batch, parentAlpha);
		
		elapsedTime += Gdx.graphics.getDeltaTime();
		if(elapsedTime > TIME && loadOK){
			((GameScreen)parent).start();
		}
	}
	
	/**
	 * Initialise l'affichage pour le chargement d'un niveau.
	 * @param level niveau en cours de chargement.
	 */
	public void initLoading(String level){
		int l = ResourceManager.levelFactory.getLevel(level);
		String text = ResourceManager.levelFactory.format(level);
		loadOK = false;
		elapsedTime = 0;
		
		lb_title.setText(ResourceManager.bundle.get("niveau_")+" "+l);
		ta_text.setText(text);
	}
	
	public void setLoad(boolean value){
		loadOK = value;
	}
	
}
