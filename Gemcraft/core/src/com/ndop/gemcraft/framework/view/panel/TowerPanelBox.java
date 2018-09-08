package com.ndop.gemcraft.framework.view.panel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ndop.gemcraft.framework.model.Player;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.SoundEffect;
import com.ndop.gemcraft.framework.model.Tower;
import com.ndop.gemcraft.framework.model.Trap;
import com.ndop.gemcraft.framework.model.towerStrategies.EnemiesClosestTargetTracker;
import com.ndop.gemcraft.framework.model.towerStrategies.FastestTargetTracker;
import com.ndop.gemcraft.framework.model.towerStrategies.ITargetTracker;
import com.ndop.gemcraft.framework.model.towerStrategies.LeastEnergyTargetTracker;
import com.ndop.gemcraft.framework.model.towerStrategies.MostEnergyTargetTracker;
import com.ndop.gemcraft.framework.model.towerStrategies.NexusClosestTargetTracker;
import com.ndop.gemcraft.framework.view.MyMap;
import com.ndop.gemcraft.framework.view.screen.GameScreen;
import com.ndop.gemcraft.framework.view.screen.MyScreen;
import com.ndop.gemcraft.framework.view.slot.MapGemSlot;

/**
 * Boite de dialogue d'information et de configuration d'une tour.
 * @author --
 * @version 1.0
 */
public class TowerPanelBox extends MyPanel{

	/**
	 * Nombre de colonne de l'inventaire.
	 */
	private static final int MARGIN = 10;

	private SelectBox<ITargetTracker> sb_tracker;
	private TextButton tb_upgrade;
	private Label lb_upgrade, lb_level, lb_fireRate, lb_locGem, lb_scope, lb_damage, lb_text7;
	private Table tb_gems;
	private Tower tower;

	public TowerPanelBox(MyScreen parent) {
		super(parent);
	}

	@Override
	public void initPanel() {
		this.setBackground(ResourceManager.skin.getDrawable("default-round-large"));

		Label lb_text1 = new Label(ResourceManager.bundle.get("niveau"), ResourceManager.skin, "infos");
		Label lb_text2 = new Label(ResourceManager.bundle.get("portee"), ResourceManager.skin, "infos");
		Label lb_text3 = new Label(ResourceManager.bundle.get("degats"), ResourceManager.skin, "infos");
		Label lb_text4 = new Label(ResourceManager.bundle.get("cadenceTir"), ResourceManager.skin, "infos");
		Label lb_text5 = new Label(ResourceManager.bundle.get("nbEmplacement"), ResourceManager.skin, "infos");
		Label lb_text6 = new Label(ResourceManager.bundle.get("coutAmelioration"), ResourceManager.skin, "infos");
		lb_text7 = new Label(ResourceManager.bundle.get("traqueurCible"), ResourceManager.skin, "infos");

		lb_level = new Label("", ResourceManager.skin, "infos");
		lb_upgrade = new Label("", ResourceManager.skin, "infos");
		lb_fireRate = new Label("", ResourceManager.skin, "infos");
		lb_locGem = new Label("", ResourceManager.skin, "infos");
		lb_scope = new Label("", ResourceManager.skin, "infos");
		lb_damage = new Label("", ResourceManager.skin, "infos");

		TextButton.TextButtonStyle st_button = ResourceManager.skin.get("infos", TextButton.TextButtonStyle.class);
		st_button.font.setColor(Color.BLUE);
		tb_upgrade = new TextButton(ResourceManager.bundle.get("ameliorer"), st_button);
		tb_upgrade.addListener(new UpgradeListener());

		sb_tracker = new SelectBox<ITargetTracker>(ResourceManager.skin, "infos");
		sb_tracker.setItems(new ITargetTracker[]{new NexusClosestTargetTracker(), new EnemiesClosestTargetTracker(),
				new LeastEnergyTargetTracker(), new MostEnergyTargetTracker(), new FastestTargetTracker()});
		sb_tracker.addListener(new TrackerListener());

		tb_gems = new Table();

		ScrollPane sc_gem = new ScrollPane(tb_gems, ResourceManager.skin,"mine");
		sc_gem.setOverscroll(false, false);
		sc_gem.setFadeScrollBars(false);
		sc_gem.setScrollingDisabled(true, false);

		HorizontalGroup hg1 = new HorizontalGroup();
		hg1.setHeight(10);
		hg1.addActor(lb_upgrade);
		hg1.addActor(tb_upgrade);

		this.add(lb_text1).left();
		this.add(lb_level).left().colspan(2).row();
		this.add(lb_text2).left();
		this.add(lb_scope).left().colspan(2).row();
		this.add(lb_text3).left();
		this.add(lb_damage).left().colspan(2).row();
		this.add(lb_text4).left();
		this.add(lb_fireRate).left().colspan(2).row();
		this.add(lb_text5).left();
		this.add(lb_locGem).left().colspan(2).row();
		this.add(lb_text6).left();
		this.add(lb_upgrade).left();
		this.add(tb_upgrade).left().height(15).padBottom(5).row();
		this.add(lb_text7).left();
		this.add(sb_tracker).left().colspan(2).padBottom(5).row();
		this.add(sc_gem).colspan(3).fillX().expandX().height(MyMap.TILE_SIZE);
		this.pack();
	}

	/**
	 * Initialise le panneau avec une tour.
	 * @param tower
	 */
	public void init(Tower tower){
		this.tower = tower;

		checkUpgrade();
		updateGems();
	}

	/**
	 * Met à jour la table des gemmes de la tour.
	 */
	public void updateGems(){
		tb_gems.clearChildren();
		int columns = (int)((this.getWidth()-(MARGIN*3))/MyMap.TILE_SIZE);

		for(int i=0; i<tower.getGems().size(); i++){
			if(i != 0 && i % columns == 0)
				tb_gems.row();

			tb_gems.add(new MapGemSlot(tower.getGems().get(i), this)).size(MyMap.TILE_SIZE, MyMap.TILE_SIZE);
		}
	}

	public Tower getTower(){
		return tower;
	}

	/**
	 * Listener qui gère les changements de stratégie.
	 * @author --
	 *
	 */
	private class TrackerListener extends ChangeListener{
		@Override
		public void changed(ChangeEvent event, Actor actor) {
			if(tower != null)
				tower.setTracker(sb_tracker.getSelected());
		}
	}

	private class UpgradeListener extends ClickListener{
		@Override
		public void clicked(InputEvent event, float x, float y) {
			GameScreen game = (GameScreen)parent;
			Player player = game.getPlayer();
			int sum = ((GameScreen)parent).getSumlLevel();
			
			if(tower != null){
				ResourceManager.playSound(SoundEffect.GROW);
				player.setMana(player.getMana() - tower.getEvolutionCost(sum));
				tower.upgrade();
				checkUpgrade();
			}
		}
	}

	public void checkUpgrade(){
		int sum = ((GameScreen)parent).getSumlLevel();
		
		float cost = tower.getEvolutionCost(sum);
		lb_level.setText(tower.getLevel()+"");
		lb_upgrade.setText(cost+"");
		lb_fireRate.setText(tower.getFireRate()+"");
		lb_locGem.setText(tower.getNbLocations()+"");
		lb_scope.setText(tower.getScope()+"");
		lb_damage.setText(tower.getDamage()+"");
		if(tower instanceof Trap){
			sb_tracker.setVisible(false);
			lb_text7.setVisible(false);
			
		}
		else{
			sb_tracker.setVisible(true);
			lb_text7.setVisible(true);
			sb_tracker.setSelected(tower.getTracker());
		}
		
		if(((GameScreen)parent).getPlayer().getMana() >= cost){
			tb_upgrade.setChecked(false);
			tb_upgrade.setTouchable(Touchable.enabled);
		}
		else{
			tb_upgrade.setChecked(true);
			tb_upgrade.setTouchable(Touchable.disabled);
		}
	}

}
