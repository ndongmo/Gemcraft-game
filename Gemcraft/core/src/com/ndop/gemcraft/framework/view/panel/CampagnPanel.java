package com.ndop.gemcraft.framework.view.panel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.ndop.gemcraft.framework.model.Player;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.view.AreaClickListener;
import com.ndop.gemcraft.framework.view.screen.GameScreen;
import com.ndop.gemcraft.framework.view.screen.GameScreen.GameMode;
import com.ndop.gemcraft.framework.view.screen.MyScreen;

/**
 * Panneau qui affiche une description du mode campagne. 
 * @author --
 * @version 1.0
 * @see MyPanel
 */
public class CampagnPanel extends MyPanel{

	private TextField tf_name;
	private Label lb_error;
	private boolean ok = false;
	
	public CampagnPanel(MyScreen parent) {
		super(parent);
	}
	
	public void initPanel(){
		this.setBackground(ResourceManager.skin.getDrawable("default-round-large"));
		
		TextArea ta_descr = new TextArea(ResourceManager.bundle.get("campagneDescr"), ResourceManager.skin);
		ta_descr.addListener(new AreaClickListener(this));
		ta_descr.setDisabled(true);
		Image drawable = new Image(ResourceManager.playerTexture);
		
		Label lb_name = new Label(ResourceManager.bundle.get("entrerPseudo"), ResourceManager.skin);
		lb_error = new Label("", ResourceManager.skin);
		lb_error.setColor(Color.RED);
		
		tf_name = new TextField("", ResourceManager.skin);
		tf_name.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				process();
			}
		});
		
		Table tb = new Table();
		tb.add(lb_name);
		tb.add(tf_name).row();
		tb.add(lb_error).colspan(2);
		

		
		this.add(ta_descr).colspan(2).expand().fill().row();
		this.add(drawable).padTop(10).left();
		this.add(tb);
	}
	
	public void process(){
		Player p = new Player(tf_name.getText(), 1);
		if(ResourceManager.playerFactory.exists(p)){
			lb_error.setText(ResourceManager.bundle.get("pseudoExiste"));
			ok = false;
		}
		else if(tf_name.getText().isEmpty()){
			lb_error.setText(ResourceManager.bundle.get("pseudoRequis"));
			ok = false;
		}
		else{
			lb_error.setText("");
			ok = true;
		}
	}
	
	@Override
	public void updatePanel(Actor actor) {
		if(ok){
			String level = ResourceManager.levelFactory.getNextLevel("");
			int index = ResourceManager.levelFactory.getLevel(level);
			String name = tf_name.getText();
			Player player = new Player(name, index);
			ResourceManager.playerFactory.addPlayer(player);
			parent.game.setScreen(new GameScreen(parent.game, level, player, GameMode.CAMPAGN));
		}
		else {
			ResourceManager.playSound(SoundEffect.ERROR);
			process();
		}
	}
}
