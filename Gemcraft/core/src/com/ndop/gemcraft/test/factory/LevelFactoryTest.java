package com.ndop.gemcraft.test.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.math.Vector2;
import com.ndop.gemcraft.framework.factory.FactoryException;
import com.ndop.gemcraft.framework.factory.LevelFactory;
import com.ndop.gemcraft.framework.model.Enemy;
import com.ndop.gemcraft.framework.model.Gem;
import com.ndop.gemcraft.framework.model.GemColor;
import com.ndop.gemcraft.framework.model.Group;
import com.ndop.gemcraft.framework.model.Level;
import com.ndop.gemcraft.framework.model.ResourceManager;
import com.ndop.gemcraft.framework.model.Spawn;
import com.ndop.gemcraft.framework.model.Wave;
import com.ndop.gemcraft.test.GdxTestRunner;

/**
 * Classe de test de LevelFactory.
 * @author --
 * @see LevelFactory
 */
@RunWith(GdxTestRunner.class)
public class LevelFactoryTest {
	/**
	 * Factory á tester;
	 */
	private LevelFactory factory;

	@Before
	public void setUp() {
		ResourceManager.updateConfig("../core/assets/");
		factory = ResourceManager.levelFactory;
	}

	/**
	 * Test de la méthode loadFiles.
	 */
	@Test
	public void loadFilesTest() {
		assertTrue(factory.getLevelFiles().size()+" != 1", factory.getLevelFiles().size() == 1);
		assertEquals(factory.getLevelFiles().get(0)+" != g1_A-1.xml", factory.getLevelFiles().get(0), "g1_A-1.xml");
	}

	/**
	 * Test de la méthode loadLevels.
	 */
	@Test
	public void loadLevelsTest() {
		LinkedHashMap<String, String> list = factory.loadLevels();
		for(Entry<String, String> e : list.entrySet()){
			assertFalse("Nom de fichier vide !", e.getKey().isEmpty());
			assertTrue("Pas de nom de niveau dans "+e.getKey(), e.getValue().contains(ResourceManager.bundle.get("nom")));
			assertTrue("Pas de sentier dans "+e.getKey(), e.getValue().contains(ResourceManager.bundle.get("nbSentier")));
			assertTrue("Pas de gemme initiale dans "+e.getKey(), e.getValue().contains(ResourceManager.bundle.get("gemmeIni")));
			assertTrue("Pas de type d'énemie dans "+e.getKey(), e.getValue().contains(ResourceManager.bundle.get("enemyType")));
			assertTrue(e.getValue()+"Pas de vague dans "+e.getKey(), e.getValue().contains(ResourceManager.bundle.get("vagues")));
		}
		assertTrue(factory.getLevelFiles().size()+" != "+list.size(), factory.getLevelFiles().size() == list.size());
	}

	/**
	 * Test de la méthode loadLevel.
	 */
	@Test
	public void loadLevelTest() {
		try{
			factory.loadLevel("fichier_inexistant");
		}catch(FactoryException e){
			assertTrue(true);
		}

		try{
			Level level = factory.loadLevel("g1_A-1.xml");
			assertEquals(level.getLife()+" != 1000", level.getLife(), 1000, 0);
			assertEquals(level.getMana()+" != 200", level.getMana(), 200, 0);

			Vector2 nexus = new Vector2(level.getNexus().x, level.getNexus().y);
			Vector2 realNexus = new Vector2(12, 7);
			assertEquals(nexus+" != "+realNexus, nexus, realNexus);

			int nbRedGems = 0, redGems = 1, nbYellowGems = 0, yellowGems = 5;
			for(Gem gem : level.getGems()){
				if(gem.getGemColor() == GemColor.RED)
					nbRedGems++;
				else if(gem.getGemColor() == GemColor.YELLOW)
					nbYellowGems++;
			}
			assertEquals("redGem("+nbRedGems+") != "+redGems, nbRedGems, redGems);
			assertEquals("yellowGem("+nbYellowGems+") != "+yellowGems, nbYellowGems, yellowGems);

			Spawn p1 = new Spawn("P1", new Vector2(0, 5));
			Spawn p2 = new Spawn("P2", new Vector2(7, 10));
			assertEquals("nbSpawn("+level.getSpawns().size()+") != 2", level.getSpawns().size(), 2);
			if(level.getSpawns().size() > 0)
				assertEquals("Spawn("+level.getSpawns().get(0)+") != "+p1, level.getSpawns().get(0), p1);
			if(level.getSpawns().size() > 1)
				assertEquals("Spawn("+level.getSpawns().get(1)+") != "+p2, level.getSpawns().get(1), p2);

			assertEquals("nbWaves("+level.getWaves().size()+") != 2", level.getWaves().size(), 2);
			if(level.getWaves().size() > 0){
				Wave wave1 = level.getWaves().get(0);
				assertEquals("Wave1Delay("+wave1.getDelay()+") != 15", (int)wave1.getDelay(), 15);
				assertEquals("Wave1nbGroup("+wave1.getGroups().size()+") != 2", wave1.getGroups().size(), 2);
				if(wave1.getGroups().size() > 0){
					Group group = wave1.getGroups().get(0);
					assertEquals("Wave1Group1Spawn("+group.getSpawn()+") != "+p1, group.getSpawn(), p1);
					assertEquals("Wave1Group1Lapse("+group.getLapse()+") != 0.5", group.getLapse(), 0.5, 0);
					assertEquals("Wave1Group1Timer("+group.getTimer()+") != "+wave1.getDelay()+0, group.getTimer(), wave1.getDelay()+0, 0.01);

					int nbFly = 0, fly = 20,nbFast = 0, fast = 0, nbBoss = 0, boss = 1;
					for(Enemy e : group.getEnemies()){
						if(e.getName().equals("fly")){
							assertEquals("Wave1Group1FlyLife("+e.getLife()+") != 50", e.getLife(), 50, 0);
							assertEquals("Wave1Group1FlyMana("+e.getMana()+") != 10", e.getMana(), 10, 0);
							assertEquals("Wave1Group1FlyRealSpeed("+e.getRealVelocity()+") != 0.5", e.getRealVelocity(), 0.5, 0.01);
							assertEquals("Wave1Group1FlySpeed("+e.getCurrentVelocity()+") != 0.5", e.getCurrentVelocity(), 0.5, 0.01);
							assertEquals("Wave1Group1FlyIm("+e.getImmunity()[GemColor.BLUE.ordinal()]+") != true", 
									e.getImmunity()[GemColor.BLUE.ordinal()], true);
							nbFly++;
						}
						else if(e.getName().equals("boss")){
							assertEquals("Wave1Group1BossLife("+e.getLife()+") != 2000", e.getLife(), 2000, 0);
							assertEquals("Wave1Group1BossMana("+e.getMana()+") != 100", e.getMana(), 100, 0);
							assertEquals("Wave1Group1BossRealSpeed("+e.getRealVelocity()+") != 0.1", e.getRealVelocity(), 0.1, 0.01);
							assertEquals("Wave1Group1BossSpeed("+e.getCurrentVelocity()+") != 0.1", e.getCurrentVelocity(), 0.1, 0.01);
							nbBoss++;
						}
						else if(e.getName().equals("fast")){
							assertEquals("Wave1Group1FastLife("+e.getLife()+") != 50", e.getLife(), 50, 0.01);
							assertEquals("Wave1Group1FastMana("+e.getMana()+") != 0.3", e.getMana(), 0.3, 0.01);
							assertEquals("Wave1Group1FastRealSpeed("+e.getRealVelocity()+") != 0.9", e.getRealVelocity(), 0.9, 0.01);
							assertEquals("Wave1Group1FastSpeed("+e.getCurrentVelocity()+") != 0.9", e.getCurrentVelocity(), 0.9, 0.01);
							nbFast++;
						}
					}
					assertEquals("Wave1Group1FlyEnemies("+nbFly+") != "+fly, nbFly, fly);
					assertEquals("Wave1Group1FastEnemies("+nbFast+") != "+fast, nbFast, fast);
					assertEquals("Wave1Group1BossEnemies("+nbBoss+") != "+boss, nbBoss, boss);
					/* Teste l'ordonancement par rapport à la vie
					 * Enemy last = group.getEnemies().get(group.getEnemies().size()-1);
					 * assertEquals("Wave1Group1OrderByLife_lastEnemyName("+last.getName()+") != boss", last.getName(), "boss");
					 */
				}

				if(wave1.getGroups().size() > 1){
					Group group2 = wave1.getGroups().get(1);
					assertEquals("Wave1Group2Spawn("+group2.getSpawn()+") != "+p2, group2.getSpawn(), p2);
					assertEquals("Wave1Group2Lapse("+group2.getLapse()+") != 0.7", group2.getLapse(), 0.7, 0.01);
					assertEquals("Wave1Group2Timer("+group2.getTimer()+") != "+wave1.getDelay()+2, group2.getTimer(), wave1.getDelay()+2, 0.01);

					int nbFly = 0, fly = 20,nbFast = 0, fast = 0, nbBoss = 0, boss = 1;
					for(Enemy e : group2.getEnemies()){
						if(e.getName().equals("fly")){
							assertEquals("Wave1Group2FlyLife("+e.getLife()+") != 50", e.getLife(), 50, 0.01);
							assertEquals("Wave1Group2FlyMana("+e.getMana()+") != 10", e.getMana(), 10, 0.01);
							assertEquals("Wave1Group2FlyRealSpeed("+e.getRealVelocity()+") != 0.5", e.getRealVelocity(), 0.5, 0.01);
							assertEquals("Wave1Group2FlySpeed("+e.getCurrentVelocity()+") != 0.5", e.getCurrentVelocity(), 0.5, 0.01);
							assertEquals("Wave1Group2FlyIm("+e.getImmunity()[GemColor.BLUE.ordinal()]+") != true", 
									e.getImmunity()[GemColor.BLUE.ordinal()], true);
							nbFly++;
						}
						else if(e.getName().equals("boss")){
							assertEquals("Wave1Group2BossLife("+e.getLife()+") != 2000", e.getLife(), 2000, 0.01);
							assertEquals("Wave1Group2BossMana("+e.getMana()+") != 100", e.getMana(), 100, 0.01);
							assertEquals("Wave1Group2BossRealSpeed("+e.getRealVelocity()+") != 0.1", e.getRealVelocity(), 0.1, 0.01);
							assertEquals("Wave1Group2BossSpeed("+e.getCurrentVelocity()+") != 0.1", e.getCurrentVelocity(), 0.1, 0.01);
							nbBoss++;
						}
						else if(e.getName().equals("fast")){
							assertEquals("Wave1Group2FastLife("+e.getLife()+") != 50", e.getLife(), 50, 0.01);
							assertEquals("Wave1Group2FastMana("+e.getMana()+") != 0.3", e.getMana(), 0.3, 0.01);
							assertEquals("Wave1Group2FastRealSpeed("+e.getRealVelocity()+") != 0.9", e.getRealVelocity(), 0.9, 0.01);
							assertEquals("Wave1Group2FastSpeed("+e.getCurrentVelocity()+") != 0.9", e.getCurrentVelocity(), 0.9, 0.01);
							nbFast++;
						}
					}
					assertEquals("Wave1Group2FlyEnemies("+nbFly+") != "+fly, nbFly, fly);
					assertEquals("Wave1Group2FastEnemies("+nbFast+") != "+fast, nbFast, fast);
					assertEquals("Wave1Group2BossEnemies("+nbBoss+") != "+boss, nbBoss, boss);
					// Teste l'ordonancement par rapport à la vie
					Enemy last = group2.getEnemies().get(group2.getEnemies().size()-1);
					assertEquals("Wave1Group1OrderByLife_lastEnemyName("+last.getName()+") != boss", last.getName(), "boss");

				}
			}

			if(level.getWaves().size() > 1){
				Wave wave2 = level.getWaves().get(1);
				assertEquals("Wave1Delay("+wave2.getDelay()+") != 16", (int)wave2.getDelay(), 16);
				assertEquals("Wave1nbGroup("+wave2.getGroups().size()+") != 1", wave2.getGroups().size(), 1);
				if(wave2.getGroups().size() > 0){
					Group group = wave2.getGroups().get(0);
					assertEquals("Wave2Group1Spawn("+group.getSpawn()+") != "+p1, group.getSpawn(), p1);
					assertEquals("Wave2Group1Lapse("+group.getLapse()+") != 0.2", group.getLapse(), 0.2, 0.01);
					assertEquals("Wave2Group1Timer("+group.getTimer()+") != "+wave2.getDelay()+0, group.getTimer(), wave2.getDelay()+0, 0.01);

					int nbFly = 0, fly = 0,nbFast = 0, fast = 15, nbBoss = 0, boss = 0;
					for(Enemy e : group.getEnemies()){
						if(e.getName().equals("fly")){
							assertEquals("Wave2Group1FlyLife("+e.getLife()+") != 50", e.getLife(), 50, 0.01);
							assertEquals("Wave2Group1FlyMana("+e.getMana()+") != 10", e.getMana(), 10, 0.01);
							assertEquals("Wave2Group1FlyRealSpeed("+e.getRealVelocity()+") != 0.5", e.getRealVelocity(), 0.5, 0.01);
							assertEquals("Wave2Group1FlySpeed("+e.getCurrentVelocity()+") != 0.5", e.getCurrentVelocity(), 0.5, 0.01);
							assertEquals("Wave2Group1FlyIm("+e.getImmunity()[GemColor.BLUE.ordinal()]+") != true", 
									e.getImmunity()[GemColor.BLUE.ordinal()], true);
							nbFly++;
						}
						else if(e.getName().equals("boss")){
							assertEquals("Wave2Group1BossLife("+e.getLife()+") != 2000", e.getLife(), 2000, 0.01);
							assertEquals("Wave2Group1BossMana("+e.getMana()+") != 100", e.getMana(), 100, 0.01);
							assertEquals("Wave2Group1BossRealSpeed("+e.getRealVelocity()+") != 0.1", e.getRealVelocity(), 0.1, 0.01);
							assertEquals("Wave2Group1BossSpeed("+e.getCurrentVelocity()+") != 0.1", e.getCurrentVelocity(), 0.1, 0.01);
							nbBoss++;
						}
						else if(e.getName().equals("fast")){
							assertEquals("Wave2Group1FastLife("+e.getLife()+") != 60", e.getLife(), 60, 0.01);
							assertEquals("Wave2Group1FastMana("+e.getMana()+") != 0.3", e.getMana(), 0.3, 0.01);
							assertEquals("Wave2Group1FastRealSpeed("+e.getRealVelocity()+") != 0.9", e.getRealVelocity(), 0.9, 0.01);
							assertEquals("Wave2Group1FastSpeed("+e.getCurrentVelocity()+") != 0.9", e.getCurrentVelocity(), 0.9, 0.01);
							nbFast++;
						}
					}
					assertEquals("Wave2Group1FlyEnemies("+nbFly+") != "+fly, nbFly, fly);
					assertEquals("Wave2Group1FastEnemies("+nbFast+") != "+fast, nbFast, fast);
					assertEquals("Wave2Group1BossEnemies("+nbBoss+") != "+boss, nbBoss, boss);
					/* Teste l'ordonancement par rapport à la vie
					 * Enemy last = group.getEnemies().get(group.getEnemies().size()-1);
					 * assertEquals("Wave1Group1OrderByLife_lastEnemyName("+last.getName()+") != boss", last.getName(), "boss");
					 */
				}
			}

		}catch(FactoryException e){
			assertFalse(e.getMessage(), true);
		}
	}

}
