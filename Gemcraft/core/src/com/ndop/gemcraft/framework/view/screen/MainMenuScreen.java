package com.ndop.gemcraft.framework.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.view.MyGemcraft;
import com.ndop.gemcraft.framework.view.panel.CampagnPanel;
import com.ndop.gemcraft.framework.view.panel.ConfigPanel;
import com.ndop.gemcraft.framework.view.panel.HighScorePanel;
import com.ndop.gemcraft.framework.view.panel.LoadSavePanel;
import com.ndop.gemcraft.framework.view.panel.MyPanel;
import com.ndop.gemcraft.framework.view.panel.SelectLevelPanel;


/**
 * Ecran du menu principal du jeu. 
 * @author --
 * @version 1.0
 * @see MyScreen
 */
public class MainMenuScreen extends MyScreen{
	/**
	 * Boutons du menu principal.
	 */
	private TextButton[] menus;
	/**
	 * Tableau des différents panneaux d'affichage.
	 */
	private MyPanel[] panels;
	/**
	 * Panneau courant.
	 */
	private MyPanel currentPanel;
	
	enum Menu{
		CAMPAGN, LOAD_GAME, FREE_LEVEL, HIGHSCORE, CONFIGURE, QUIT
	}

	public MainMenuScreen(MyGemcraft game) {
		super(game);
	}

	@Override
	public void initScreen(){
		menus = new TextButton[Menu.values().length];
		panels = new MyPanel[Menu.values().length];
		
		TextButton.TextButtonStyle st_button = ResourceManager.skin.get("toggle", TextButton.TextButtonStyle.class);
		Label.LabelStyle st_title = ResourceManager.skin.get("title", Label.LabelStyle.class);
		
		menus[Menu.CAMPAGN.ordinal()] = new TextButton(ResourceManager.bundle.get("campagne"), st_button);
		menus[Menu.FREE_LEVEL.ordinal()] = new TextButton(ResourceManager.bundle.get("niveauLibre"), st_button);
		menus[Menu.LOAD_GAME.ordinal()] = new TextButton(ResourceManager.bundle.get("chargerNiveau"), st_button);
		menus[Menu.HIGHSCORE.ordinal()] = new TextButton(ResourceManager.bundle.get("meilleurScore"), st_button);
		menus[Menu.CONFIGURE.ordinal()] = new TextButton(ResourceManager.bundle.get("configurer"), st_button);
		menus[Menu.QUIT.ordinal()] = new TextButton(ResourceManager.bundle.get("quitter"), st_button);
				
		panels[Menu.CAMPAGN.ordinal()] = new CampagnPanel(this);
		panels[Menu.LOAD_GAME.ordinal()] = new LoadSavePanel(this);
		panels[Menu.FREE_LEVEL.ordinal()] = new SelectLevelPanel(this);
		panels[Menu.HIGHSCORE.ordinal()] = new HighScorePanel(this);
		panels[Menu.CONFIGURE.ordinal()] = new ConfigPanel(this);

		Label lb_title = new Label(ResourceManager.bundle.get("titre"), st_title);

		VerticalGroup vg = new VerticalGroup();
		vg.fill();
		
		for(TextButton tb : menus){
			tb.addListener(new ButtonClickListener());
			vg.addActor(tb);
		}
		
	    table.add(lb_title).pad(10).colspan(2);
	    table.row().top().left();
		table.add(vg).pad(10).fillX();
		
		menus[Menu.CAMPAGN.ordinal()].setChecked(true);
		currentPanel = panels[Menu.CAMPAGN.ordinal()];
		table.add(currentPanel).left().pad(10).expand().fill();
		ResourceManager.playTheme();
	}
	
	@Override
	public void resize(int width, int height) {
		stage.setViewport(new StretchViewport(width, height));
		stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
	}
	
	private class ButtonClickListener extends ClickListener{
		@Override
	    public void clicked(InputEvent event, float x, float y) {
			ResourceManager.playSound(SoundEffect.CLICK);
			for(Menu m : Menu.values()){
				TextButton tb = menus[m.ordinal()];
				if(event.getListenerActor().equals(tb)){
					tb.setChecked(true);
					if(m.equals(Menu.QUIT))
						Gdx.app.exit();
					else
						table.getCell(currentPanel).setActor(currentPanel = panels[m.ordinal()]);
				}
				else if(tb.isChecked())
					tb.setChecked(false);
			}
	    }
	}

}
