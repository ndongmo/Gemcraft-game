package com.ndop.gemcraft.framework.view.panel;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.view.screen.GameScreen;
import com.ndop.gemcraft.framework.view.screen.MyScreen;

/**
 * Panneau d'affichage du générique de fin !
 * @author --
 * @version 1.0
 */
public class GenericPanel extends MyPanel{
	private static final float FRAME = 4;
	private float time = 0;
	private int count = 0;
	private int sens = 1;
	private TextArea ta_text;
	private Label lb_title;
	private ArrayList<String> texts;
	private ArrayList<String> titles;

	public GenericPanel(MyScreen parent) {
		super(parent);
	}

	@Override
	public void initPanel() {
		this.setBackground(ResourceManager.skin.getDrawable("default-round-large"));

		texts = new ArrayList<String>();
		titles = new ArrayList<String>();
		
		titles.add(ResourceManager.bundle.get("developpePar"));
		titles.add(ResourceManager.bundle.get("music"));
		titles.add(ResourceManager.bundle.get("son"));
		titles.add("");

		texts.add(ResourceManager.bundle.format("monNom", "Ndongmo silatsa .f","(+32)488030816", "barosndongmo@yahoo.fr"));
		texts.add(ResourceManager.bundle.format("PersoMusic", "Eric Skiff", "https://soundcloud.com/eric-skiff"));
		texts.add(ResourceManager.bundle.format("PersoSon", "Damaged Panda", 
				"http://opengameart.org/content/100-plus-game-sound-effects-wavoggm4a"));
		texts.add(ResourceManager.bundle.get("merci"));


		lb_title = new Label(titles.get(0), ResourceManager.skin, "default-font", Color.GREEN);
		ta_text = new TextArea(texts.get(0), ResourceManager.skin);
		ta_text.setDisabled(true);
		count++;
		
		this.setFillParent(true);
		this.add(lb_title).center().padBottom(20).row();
		this.add(ta_text).center().expand().fill();
		time = 0;
	}

	@Override
	public void draw(Batch batch, float parentAlpha){
		time += (Gdx.graphics.getDeltaTime()*sens);
		if(time > FRAME || time < 0){
			if(sens < 0){
				if(titles.size()-1 == count){
					ta_text.getStyle().fontColor = new Color(ta_text.getStyle().fontColor.r, ta_text.getStyle().fontColor.g, 
							ta_text.getStyle().fontColor.b, 1);
					((GameScreen)parent).stop();

					return;
				}
				count++;
				lb_title.setText(titles.get(count));
				ta_text.setText(texts.get(count));
			}
			
			sens *= -1;
			time = (time < 0) ? 0 : FRAME;
		}
		
		float alpha = (time / FRAME);
		lb_title.setColor(lb_title.getColor().r, lb_title.getColor().g, lb_title.getColor().b, alpha);
		ta_text.getStyle().fontColor = new Color(ta_text.getStyle().fontColor.r, ta_text.getStyle().fontColor.g, 
				ta_text.getStyle().fontColor.b, alpha);
		
		super.draw(batch, parentAlpha);
	}

}
