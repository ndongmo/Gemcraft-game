package com.ndop.gemcraft.framework.view.panel;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.factory.PlayerFactory;
import com.ndop.gemcraft.framework.model.Level;
import com.ndop.gemcraft.framework.model.Player;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.view.screen.GameScreen;
import com.ndop.gemcraft.framework.view.screen.GameScreen.GameMode;
import com.ndop.gemcraft.framework.view.screen.MyScreen;

/**
 * Panneau d'affichage de la fin du jeu.
 * @author --
 * @version 1.0
 */
public class GameOverPanel extends MyPanel{
	/**
	 * Labels d'information.
	 */
	private Label lb_title, lb_text;
	private TextButton tb_ok;
	private Table tb_stat;
	private boolean canContinue, end;

	public GameOverPanel(MyScreen parent) {
		super(parent);
	}

	@Override
	public void initPanel() {
		this.tb_stat = new Table();
		this.tb_stat.setSkin(ResourceManager.skin);
		this.tb_stat.setBackground(ResourceManager.skin.getDrawable("default-round-large"));

		this.lb_title = new Label("", ResourceManager.skin, "title");
		this.lb_text = new Label("", ResourceManager.skin, "default-font", Color.GREEN);
		this.tb_ok = new TextButton(ResourceManager.bundle.get("continuer"), ResourceManager.skin, "toggle");
		this.tb_ok.addListener(new ClickListener(){
			public void clicked(InputEvent event, float x, float y) {
				ResourceManager.playSound(SoundEffect.CLICK);
				tb_ok.setChecked(false);
				updatePanel(null);
			}
		});

		this.setFillParent(true);
		this.add(lb_title).center().padBottom(20).row();
		this.add(lb_text).center().row();
		this.add(tb_stat).fillX().expandX().center().pad(10).row();
		this.add(tb_ok).center();
	}

	@Override
	public void updatePanel(Actor actor){
		if(((GameScreen)parent).getMode() == GameMode.CAMPAGN){
			if(canContinue)
				((GameScreen)parent).load();
			else if(end)
				initLevelOver(0);
			else if(((GameScreen)parent).getPlayer().isAlive())
				((GameScreen)parent).win();
			else
				((GameScreen)parent).stop();
		}
		else
			((GameScreen)parent).stop();
	}

	public void initLevelOver(float time){	
		Level level = ((GameScreen)parent).getCurrentLevel();
		Player player = ((GameScreen)parent).getPlayer();
		boolean isOver = end;

		if(!end){
			if(!player.isAlive()){
				ResourceManager.playSound(SoundEffect.DEAD);
				lb_title.setText(ResourceManager.bundle.get("finPartie"));
				lb_text.setText(ResourceManager.bundle.get("echouer"));
				canContinue = false;
				return;
			}
			else if(ResourceManager.levelFactory.hasNext(level.getFileName())){
				ResourceManager.playSound(SoundEffect.SUCCESS);
				lb_title.setText(ResourceManager.bundle.get("felicitations"));
				lb_text.setText(ResourceManager.bundle.format("niveauOK", level.getName()));
				canContinue = true;
			}
			else{
				ResourceManager.playSound(SoundEffect.SUCCESS);
				lb_title.setText(ResourceManager.bundle.get("felicitations"));
				lb_text.setText(ResourceManager.bundle.format("niveauOK", level.getName()));
				canContinue = false;
				end = true;
			}
		}
		else{
			lb_title.setText(ResourceManager.bundle.get("felicitations"));
			lb_text.setText(ResourceManager.bundle.format("jeuOK", level.getName()));
			end = false;
		}

		Table t1 = new Table();
		Table t2 = new Table();
		t1.setSkin(ResourceManager.skin);
		t2.setSkin(ResourceManager.skin);
		t1.setBackground(ResourceManager.skin.getDrawable("textfield"));
		t2.setBackground(ResourceManager.skin.getDrawable("textfield"));

		ArrayList<Player> tmp = null;
		int indexLevel = player.getCurrentLevel();
		String title = "";

		if(isOver){
			tmp = ResourceManager.playerFactory.getGameBestPlayers();
			time = player.getAllTime();
			title = ResourceManager.bundle.get("titre");

		}else{
			tmp = ResourceManager.playerFactory.getLevelBestPlayers(indexLevel);
			player.updateLevel(time);
			title = level.getName();
		}

		for(int i=0; i<tmp.size(); i++){
			Player p = tmp.get(i);
			float curtime = (isOver) ? p.getAllTime() : p.getStats().get(indexLevel-1).getTime();

			t1.add(""+(i+1));
			t1.add(p.getName()).expandX().pad(5);
			t1.add(GameScreen.convert(curtime)).row();
		}

		if(tmp.contains(player)) tmp.remove(player);
		boolean best = false;

		for(int i=0, j=1; i<tmp.size() && j<= PlayerFactory.BEST_PLAYER; i++, j++){
			Player p = tmp.get(i);
			float curtime = (isOver) ? p.getAllTime() : p.getStats().get(indexLevel-1).getTime();

			if(curtime > time && !best){
				t2.add(""+(j));
				t2.add(player.getName(), "default-font", Color.RED).expandX().pad(5);
				t2.add(GameScreen.convert(time), "default-font", Color.RED).row();
				best = true;
				i--;
			}
			else{
				t2.add(""+(j));
				t2.add(p.getName()).expandX().pad(5);
				t2.add(GameScreen.convert(curtime)).row();
			}
		}

		if(!best){
			if(tmp.size() == PlayerFactory.BEST_PLAYER){
				t2.add("-");
				t2.add("-").expandX().pad(5);
				t2.add("-").row();
				t2.add("-", "default-font", Color.RED);
			}
			else
				t2.add(""+(tmp.size()+1));

			t2.add(player.getName(), "default-font", Color.RED).expandX().pad(5);
			t2.add(GameScreen.convert(time), "default-font", Color.RED);
		}

		tb_stat.clear();
		tb_stat.add(title).colspan(2).row();
		tb_stat.add(ResourceManager.bundle.get("dernierRang"));
		tb_stat.add(ResourceManager.bundle.get("nouveauRang")).row();
		tb_stat.add(t1).expand().fill().pad(5);
		tb_stat.add(t2).expand().fill().pad(5);
	}

}
