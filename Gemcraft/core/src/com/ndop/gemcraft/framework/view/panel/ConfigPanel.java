package com.ndop.gemcraft.framework.view.panel;

import java.util.ArrayList;
import java.util.Locale;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.view.screen.MyScreen;

/**
 * Panneau de configuration. 
 * @author --
 * @version 1.0
 * @see MyPanel
 */
public class ConfigPanel extends MyPanel{

	private SelectBox<String> sb_language;
	private String[] items;
	private TextButton tb_restore, tb_save;
	private Label lb_soundValue, lb_musicValue;
	private Slider pb_sound, pb_music;
	
	public ConfigPanel(MyScreen parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initPanel() {
		this.setBackground(ResourceManager.skin.getDrawable("default-round-large"));
		
		Table table = new Table();
		table.setBackground(ResourceManager.skin.getDrawable("textfield"));
		
		Label lb_language = new Label(ResourceManager.bundle.get("langue"), ResourceManager.skin);
		Label lb_sound = new Label(ResourceManager.bundle.get("volumeSon"), ResourceManager.skin);
		Label lb_music = new Label(ResourceManager.bundle.get("volumeMusic"), ResourceManager.skin);
		lb_soundValue = new Label((int)(ResourceManager.configFactory.config.soundVolume*100)+"", ResourceManager.skin);
		lb_musicValue = new Label((int)(ResourceManager.configFactory.config.musicVolume*100)+"", ResourceManager.skin);
		
		sb_language = new SelectBox<String>(ResourceManager.skin);
		pb_sound = new Slider(0, 1, 0.1f, false, ResourceManager.skin);
		pb_music = new Slider(0, 1, 0.1f, false, ResourceManager.skin);
		
		tb_restore = new TextButton(ResourceManager.bundle.get("restorer"), ResourceManager.skin, "toggle");
		tb_restore.addListener(new RestoreClickListener());
		tb_save = new TextButton(ResourceManager.bundle.get("enregistrer"), ResourceManager.skin, "toggle");
		tb_save.addListener(new SaveClickListener());
		
		items = new String[ResourceManager.configFactory.locales.size()];
		this.updatePanel();
		
		pb_sound.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				lb_soundValue.setText((int)(pb_sound.getValue()*100)+"");
			}
		});
		
		pb_music.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				lb_musicValue.setText((int)(pb_music.getValue()*100)+"");
			}
		});
		
		HorizontalGroup hg1 = new HorizontalGroup();
		hg1.addActor(pb_music);
		hg1.addActor(lb_musicValue);
		hg1.space(10);
		HorizontalGroup hg2 = new HorizontalGroup();
		hg2.addActor(pb_sound);
		hg2.addActor(lb_soundValue);
		hg2.space(10);
		
		table.top();
		table.add(lb_language).left().padRight(10);
		table.add(sb_language).expandX().fillX().row();
		table.add(lb_music).left().padRight(10);
		table.add(hg1).expandX().fillX().row();
		table.add(lb_sound).left().padRight(10);
		table.add(hg2).expandX().fillX();
		
		this.add(table).expand().fill().colspan(2).row();
		this.add(tb_save).right().pad(5);
		this.add(tb_restore).left().pad(5);
	}
	
	public void updatePanel(){
		ArrayList<Locale> locales = ResourceManager.configFactory.locales;
		Locale loc = ResourceManager.configFactory.findLocale(ResourceManager.configFactory.config.language);
		Locale.setDefault(loc);
		
		for(int i=0; i<locales.size(); i++)
			items[i] = locales.get(i).getDisplayLanguage();
		
		sb_language.setItems(items);
		sb_language.setSelected(loc.getDisplayLanguage());
		
		pb_music.setValue(ResourceManager.configFactory.config.musicVolume);
		pb_sound.setValue(ResourceManager.configFactory.config.soundVolume);
	}
	
	/**
	 * Classe interne qui gère le click sur le bouton de sauvegarde.
	 * @author --
	 *
	 */
	public class SaveClickListener extends ClickListener {
		@Override
	    public void clicked(InputEvent event, float x, float y) {
			ResourceManager.playSound(SoundEffect.CLICK);
			
			Locale selected = ResourceManager.configFactory.locales.get(sb_language.getSelectedIndex());
			ResourceManager.configFactory.config.language = selected.getLanguage();
			ResourceManager.configFactory.config.musicVolume = pb_music.getValue();
			ResourceManager.configFactory.config.soundVolume = pb_sound.getValue();
			ResourceManager.configFactory.saveConfig();
			ResourceManager.updateConfig();
			tb_save.setChecked(false);
			tb_save.setChecked(false);
	    }
	}
	
	/**
	 * Classe interne qui gère le click sur le bouton de sauvegarde.
	 * @author --
	 *
	 */
	public class RestoreClickListener extends ClickListener {
		@Override
	    public void clicked(InputEvent event, float x, float y) {
			ResourceManager.playSound(SoundEffect.CLICK);
			
			ResourceManager.configFactory.defaultConfig();
			ResourceManager.updateConfig();
			updatePanel();
			tb_restore.setChecked(false);
	    }
	}

}
