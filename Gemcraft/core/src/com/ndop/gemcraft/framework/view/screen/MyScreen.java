package com.ndop.gemcraft.framework.view.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ndop.gemcraft.framework.view.MyGemcraft;

/**
 * Classe qui généralise les écrans du jeu. 
 * Elle herite de Screen et contient les conteneurs que vont remplir ses différents fils.
 * @author --
 * @version 1.0
 *
 */
public abstract class MyScreen implements Screen{
	/**
	 * Conteneur de la scène 2D.
	 */
	protected Stage stage;
	/**
	 * Classeur des différents acteurs de la scène.
	 */
	protected Table table;
	/**
	 * parent.
	 */
	public MyGemcraft game;
	
	public MyScreen(MyGemcraft game) {	
		this.game = game;
		stage = new Stage();
		table = new Table();
		
		table.left().top();
		table.setFillParent(true);

		stage.addActor(table);
	    
		Gdx.input.setInputProcessor(stage);
		
		initScreen();
	}
	
	/**
	 * Initialise l'écran en cours.
	 */
	public abstract void initScreen();

	public Table getTable() {
		return table;
	}
	
	public Stage getStage(){
		return stage;
	}
	
	public MyGemcraft getGame(){
		return game;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(float delta) {
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
	    stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
