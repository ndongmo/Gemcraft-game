package com.ndop.gemcraft.framework.view.panel;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ndop.gemcraft.framework.model.Player;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.view.AreaClickListener;
import com.ndop.gemcraft.framework.view.screen.GameScreen;
import com.ndop.gemcraft.framework.view.screen.MyScreen;
import com.ndop.gemcraft.framework.view.screen.GameScreen.GameMode;

/**
 * Cette classe représente le panneau de sélection des sauvegardes des parties.
 * @author --
 * @version 1.0
 */
public class LoadSavePanel extends MyPanel{

	public LoadSavePanel(MyScreen parent) {
		super(parent);
	}

	@Override
	public void initPanel() {
		this.setBackground(ResourceManager.skin.getDrawable("default-round-large"));
		
		Table list = new Table();
		
		for(Player p : ResourceManager.playerFactory.getPlayers()){
			MyRow tb = new MyRow(p);
			list.add(tb).expandX().fillX().row();
		}
		
		ScrollPane scrol = new ScrollPane(list, ResourceManager.skin,"mine");
		scrol.setOverscroll(false, false);
		scrol.setFadeScrollBars(false);
		scrol.setScrollingDisabled(true, false);
		
		this.add(scrol).expandX().fillX().top();
	}
	
	@Override
	public void updatePanel(Actor actor){
		String playerName = ((MyRow)actor).playerName;
		int index = ((MyRow)actor).level;
		
		String level = ResourceManager.levelFactory.getFileName(index);
		Player player = ResourceManager.playerFactory.getPlayer(playerName, index);
		
		parent.game.setScreen(new GameScreen(parent.game, level, player, GameMode.CAMPAGN));
	}
	
	class MyRow extends Table{
		public String playerName;
		public int level;
		
		public MyRow(Player p){
			super();
			this.addListener(new AreaClickListener(LoadSavePanel.this));
			this.setSkin(ResourceManager.skin);
			this.setBackground(ResourceManager.skin.getDrawable("default-round-large"));
			this.playerName = p.getName();
			this.level = p.getCurrentLevel();
			String levelName = ResourceManager.levelFactory.getLevelName(p.getCurrentLevel());
			Image drawable = new Image(ResourceManager.playerTexture);
			
			this.add(drawable).size(32, 32);
			this.add(p.getName()).expandX().fillX().spaceLeft(100);
			this.add(levelName).spaceLeft(10);
		}
	}

}
