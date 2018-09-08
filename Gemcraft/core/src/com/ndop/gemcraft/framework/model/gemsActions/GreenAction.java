package com.ndop.gemcraft.framework.model.gemsActions;

import java.util.ArrayList;

/**
 * Action de la gemme verte (poison). Un ennemi touché recoit, en plus des dégâts infligés, 
 * 20% des dégâts pendant les 5 secondes suivantes.
 * @author --
 * @version 1.0
 */
public abstract class GreenAction extends GemAction{
	/**
	 * Liste des dégâts de poison.
	 */
	protected ArrayList<Float> damages = new ArrayList<Float>();
	/**
	 * Liste des timer.
	 */
	protected ArrayList<Float> timers = new ArrayList<Float>();
}
