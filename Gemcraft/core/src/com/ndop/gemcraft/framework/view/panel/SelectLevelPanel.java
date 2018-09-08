package com.ndop.gemcraft.framework.view.panel;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ndop.gemcraft.framework.model.Player;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.view.AreaClickListener;
import com.ndop.gemcraft.framework.view.screen.GameScreen;
import com.ndop.gemcraft.framework.view.screen.GameScreen.GameMode;
import com.ndop.gemcraft.framework.view.screen.MyScreen;

/**
 * Panneau de sélection d'un niveau à jouer.
 * @author --
 * @version 1.0
 */
public class SelectLevelPanel extends MyPanel{

	/**
	 * Liste ordonnée des niveaux où chaque entrée 
	 * contient le nom du fichier niveau et sa description.
	 */
	private final LinkedHashMap<String, String>levels;
	/**
	 * Entrée courante du panneau.
	 */
	private Entry<String, String> currentLevel;
	/**
	 * Avancer, précédent.
	 */
	private TextButton bt_next, bt_previous;
	/**
	 * Zone de texte clickable qui affiche la description du niveau courant.
	 */
	private TextArea ta_text;
	/**
	 * Label du niveau courant.
	 */
	private Label lb_level;
	/**
	 * Index du niveau courant.
	 */
	private int level;
	
	public SelectLevelPanel(MyScreen parent) {
		super(parent);
		this.levels = ResourceManager.levelFactory.loadLevels();
		level = 0;
		if(this.levels != null && this.levels.size() > 0){
			currentLevel = this.levels.entrySet().iterator().next();
			level = 1;
			lb_level.setText(ResourceManager.bundle.format("listNiveau", level, this.levels.size()));
			ta_text.setText(currentLevel.getValue());
		}
		if(this.levels.size() < 2)
			bt_next.setTouchable(Touchable.disabled);
	}

	@Override
	public void initPanel() {
		this.setBackground(ResourceManager.skin.getDrawable("default-round-large"));
		
		ta_text = new TextArea("", ResourceManager.skin);
		ta_text.addListener(new AreaClickListener(this));
		ta_text.setDisabled(true);
		
		bt_next = new TextButton(ResourceManager.bundle.get("suivant"), ResourceManager.skin, "toggle");
		bt_next.addListener(new ButtonClickListener());
		bt_previous = new TextButton(ResourceManager.bundle.get("precedent"), ResourceManager.skin, "toggle");
		bt_previous.addListener(new ButtonClickListener());
		bt_previous.setTouchable(Touchable.disabled);
		bt_previous.setChecked(true);
		
		lb_level = new Label("", ResourceManager.skin);
		lb_level.setAlignment(Align.center);
		
		Table tbe = new Table();
		
		tbe.add(bt_previous).pad(5);
		tbe.add(lb_level).expand().fill();
		tbe.add(bt_next);
		
		this.add(ta_text).expand().fill().row();
		this.add(tbe).expandX().fillX();
	}
	
	/**
	 * Classe interne qui gère le click des boutons avancer et précédent.
	 * @author --
	 *
	 */
	private class ButtonClickListener extends ClickListener{
		@Override
	    public void clicked(InputEvent event, float x, float y) {
			int count = 1;
			ResourceManager.playSound(SoundEffect.CLICK);
			
			if(event.getListenerActor().equals(bt_previous))
				level--;
			else
				level++;
			
			for(Entry<String, String> entry : levels.entrySet()){
				if(count == level){
					currentLevel = entry;
					lb_level.setText(ResourceManager.bundle.format("listNiveau", level, levels.size()));
					ta_text.setText(currentLevel.getValue());
					break;
				}
				count++;
			}
			
			Touchable canBack = (level <= 1)?Touchable.disabled:Touchable.enabled;
			Touchable canProgress = (level >= levels.size())?Touchable.disabled:Touchable.enabled;
			
			bt_previous.setTouchable(canBack);
			bt_previous.setChecked((level <= 1));
			bt_next.setTouchable(canProgress);
			bt_next.setChecked(level >= levels.size());
	    }
	}

	@Override
	public void updatePanel(Actor actor) {
		String level = currentLevel.getKey();
		int index = ResourceManager.levelFactory.getLevel(level);
		Player player = new Player(ResourceManager.bundle.get("joueur"), index);
		parent.game.setScreen(new GameScreen(parent.game, level, player, GameMode.FREEMAP));
	}
}
