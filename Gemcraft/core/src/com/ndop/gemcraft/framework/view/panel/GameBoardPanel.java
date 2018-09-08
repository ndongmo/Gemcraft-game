package com.ndop.gemcraft.framework.view.panel;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.view.IGameBoard;
import com.ndop.gemcraft.framework.view.screen.GameScreen;
import com.ndop.gemcraft.framework.view.screen.GameScreen.GameState;
import com.ndop.gemcraft.framework.view.screen.MyScreen;


/**
 * Panneau d'affichage des paramètres du joueur.
 * @author --
 * @version 1.0
 */
public class GameBoardPanel extends MyPanel implements IGameBoard{
	
	public final float HEIGHT = 60;
	/**
	 * Les barres de progression vie et mana.
	 */
	private ProgressBar lifeBar, manaBar;
	/**
	 * Label d'information.
	 */
	private Label lb_infos, lb_life, lb_mana;
	/**
	 * Button de gestion des états du jeu.
	 */
	private Button bt_stop, bt_pause, bt_play, bt_acc;

	public GameBoardPanel(MyScreen parent) {
		super(parent);
	}

	@Override
	public void initPanel() {
		ProgressBarStyle st_life = ResourceManager.skin.get("default-life", ProgressBarStyle.class);
		ProgressBarStyle st_mana = ResourceManager.skin.get("default-mana", ProgressBarStyle.class);
		//st_life.background.setMinHeight(50);
		st_life.background.setLeftWidth(0);
		st_life.background.setRightWidth(0);
		//st_life.knobBefore.setMinHeight(50);
		//st_mana.background.setMinHeight(50);
		st_mana.background.setLeftWidth(0);
		st_mana.background.setRightWidth(0);
		//st_mana.knobBefore.setMinHeight(50);

		this.lifeBar = new ProgressBar(0, 100, 1, false, st_life);
		this.manaBar = new ProgressBar(0, 100, 5, false, st_mana);
		this.lifeBar.setAnimateDuration(2);
		this.manaBar.setAnimateDuration(2);

		this.lb_infos = new Label("", ResourceManager.skin);
		this.lb_life = new Label("", ResourceManager.skin);
		this.lb_mana = new Label("", ResourceManager.skin);
		this.lb_mana.setFillParent(true);
		this.lb_life.setFillParent(true);

		this.bt_stop = new Button(ResourceManager.skin, "stop");
		this.bt_pause = new Button(ResourceManager.skin, "pause");
		this.bt_play = new Button(ResourceManager.skin, "play");
		this.bt_acc = new Button(ResourceManager.skin, "acc");
		this.bt_stop.addListener(new ButtonClickListener());
		this.bt_pause.addListener(new ButtonClickListener());
		this.bt_play.addListener(new ButtonClickListener());
		this.bt_acc.addListener(new ButtonClickListener());
		this.bt_play.setTouchable(Touchable.disabled);
		this.bt_play.setChecked(true);
		Table hg = new Table();
		hg.left();
		hg.add(bt_stop).padRight(10);
		hg.add(bt_pause).padRight(10);
		hg.add(bt_play).padRight(10);
		hg.add(bt_acc);

		this.add(lb_infos).left().expand();
		this.add(lifeBar).padRight(10);
		this.add(manaBar).row();
		this.add(hg).left().expand().fill().padTop(10);
		this.add(lb_life).center().padRight(10);
		this.add(lb_mana).center();
	}

	public void init(float life, float mana, String levelName, String playerName){
		lifeBar.setRange(0, life);
		manaBar.setRange(0, mana);

		lb_infos.setText(playerName + " : " + levelName);
		lb_life.setText((int)life+" / "+(int)life);
		lb_mana.setText((int)mana+"");
	}

	@Override
	public void updateLife(float life) {
		lifeBar.setValue(life);
		lb_life.setText((int)life+" / "+(int)lifeBar.getMaxValue());
	}

	@Override
	public void updateMana(float mana) {
		if(mana > manaBar.getMaxValue())
			manaBar.setRange(0, mana);
		
		manaBar.setValue(mana);
		lb_mana.setText((int)mana+"");
	}

	private class ButtonClickListener extends ClickListener{
		@Override
		public void clicked(InputEvent event, float x, float y) {
			ResourceManager.playSound(SoundEffect.CLICK);
			
			if(event.getListenerActor().equals(bt_stop)){
				((GameScreen)parent).stop();
			}
			else if(event.getListenerActor().equals(bt_pause)){
				pause();
			}
			else if(event.getListenerActor().equals(bt_play)){
				play();
			}
			else if(event.getListenerActor().equals(bt_acc)){
				((GameScreen)parent).acc();
				bt_acc.setTouchable(Touchable.disabled);
				bt_play.setTouchable(Touchable.enabled);
				bt_pause.setTouchable(Touchable.enabled);
				bt_play.setChecked(false);
				bt_pause.setChecked(false);
			}
		}
	}
	
	public void pause(){
		((GameScreen)parent).setState(GameState.PAUSED);
		bt_pause.setTouchable(Touchable.disabled);
		bt_play.setTouchable(Touchable.enabled);
		bt_acc.setTouchable(Touchable.enabled);
		bt_pause.setChecked(true);
		bt_play.setChecked(false);
		bt_acc.setChecked(false);
	}
	
	public void play(){
		((GameScreen)parent).play();
		bt_play.setTouchable(Touchable.disabled);
		bt_pause.setTouchable(Touchable.enabled);
		bt_acc.setTouchable(Touchable.enabled);
		bt_play.setChecked(true);
		bt_pause.setChecked(false);
		bt_acc.setChecked(false);
	}

}
