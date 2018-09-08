package com.ndop.gemcraft.framework.view.panel;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.model.Player;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.view.screen.GameScreen;
import com.ndop.gemcraft.framework.view.screen.MyScreen;

/**
 * Classe représentant le panneau d'affichage des meilleurs scores. 
 * @author --
 * @version 1.0
 */
public class HighScorePanel extends MyPanel{

	private TextButton tb_level, tb_game;
	private Table table;

	public HighScorePanel(MyScreen parent) {
		super(parent);
	}

	@Override
	public void initPanel() {
		this.setBackground(ResourceManager.skin.getDrawable("default-round-large"));

		table = new Table();
		table.setSkin(ResourceManager.skin);
		table.setBackground(ResourceManager.skin.getDrawable("default-round-large"));

		ScrollPane scrol = new ScrollPane(table, ResourceManager.skin,"mine");
		scrol.setOverscroll(false, false);
		scrol.setFadeScrollBars(false);
		scrol.setScrollingDisabled(true, false);

		tb_level = new TextButton(ResourceManager.bundle.get("RangNiveau"), ResourceManager.skin, "toggle");
		tb_game = new TextButton(ResourceManager.bundle.get("RangFinal"), ResourceManager.skin, "toggle");
		
		tb_level.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				ResourceManager.playSound(SoundEffect.CLICK);
				drawLevelRanking();
			}
		});
		
		tb_game.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				ResourceManager.playSound(SoundEffect.CLICK);
				drawGameRanking();
			}
		});

		this.add(scrol).colspan(2).expand().fill().pad(10).row();
		this.add(tb_level).expand().bottom().left();
		this.add(tb_game).expand().bottom().right();
		
		drawLevelRanking();
	}

	public void drawLevelRanking(){
		tb_level.setChecked(true);
		tb_level.setTouchable(Touchable.disabled);
		tb_game.setChecked(false);
		tb_game.setTouchable(Touchable.enabled);
		table.clear();

		for(int i=0; i<ResourceManager.levelFactory.getLevelFiles().size(); i++){
			int index = i+1;
			String levelName = ResourceManager.levelFactory.getLevelName(index);

			Table t = new Table();
			t.top();
			t.setSkin(ResourceManager.skin);
			t.setBackground(ResourceManager.skin.getDrawable("textfield"));
			t.add(levelName, "default-font", Color.GREEN).expandX().colspan(3).row();

			ArrayList<Player> tmp = ResourceManager.playerFactory.getLevelBestPlayers(index);

			for(int j=0; j<tmp.size(); j++){
				Player p = tmp.get(j);
				float curtime = p.getStats().get(index-1).getTime();

				t.add(""+(j+1));
				t.add(p.getName()).expandX().pad(2);
				t.add(GameScreen.convert(curtime)).row();
			}

			table.add(t).expand().fill().pad(5).row();
		}
	}

	public void drawGameRanking(){
		tb_level.setChecked(false);
		tb_level.setTouchable(Touchable.enabled);
		tb_game.setChecked(true);
		tb_game.setTouchable(Touchable.disabled);
		table.clear();

		Table t = new Table();
		t.top();
		t.setSkin(ResourceManager.skin);
		t.setBackground(ResourceManager.skin.getDrawable("textfield"));
		t.add(ResourceManager.bundle.get("titre"), "default-font", Color.GREEN).expandX().colspan(3).row();

		ArrayList<Player> tmp = ResourceManager.playerFactory.getGameBestPlayers();

		for(int j=0; j<tmp.size(); j++){
			Player p = tmp.get(j);
			float curtime = p.getAllTime();

			t.add(""+(j+1));
			t.add(p.getName()).expandX().pad(5);
			t.add(GameScreen.convert(curtime)).row();
		}

		table.add(t).expand().fill().pad(5).row();
	}

}
