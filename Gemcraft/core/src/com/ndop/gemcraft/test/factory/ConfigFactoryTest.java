package com.ndop.gemcraft.test.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ndop.gemcraft.framework.factory.ConfigFactory;
import com.ndop.gemcraft.test.GdxTestRunner;

/**
 * Test de la classe ConfigFactory.
 * @author --
 * @see ConfigFactory
 */
@RunWith(GdxTestRunner.class)
public class ConfigFactoryTest {
	/**
	 * Factory à tester.
	 */
	private ConfigFactory factory = null;

	@Before
	public void setUp() {
		initConfigTest();
	}

	@Test
	public void initConfigTest(){

		factory = new ConfigFactory();
		assertTrue(""+factory.locales.size(), factory.locales.size() == 2);
		assertFalse(factory.config.language.isEmpty());
		boolean b = false;
		for(Locale loc : factory.locales)
			if(loc.equals(new Locale(factory.config.language)))
				b = true;
		assertTrue(b);
	}

	@Test
	public void defaultConfigTest(){
		if(factory != null){
			factory.defaultConfig();
			assertTrue(factory.config.language+" != "+factory.locales.get(0).getLanguage(), 
					factory.config.language == factory.locales.get(0).getLanguage());
		}
		else
			assertFalse(true);
	}

	@Test
	public void findLocaleTest(){
		if(factory != null){
			Locale loc = factory.findLocale(factory.config.language);
			assertEquals(factory.config.language+" != "+loc.getLanguage(), 
					factory.config.language, loc.getLanguage());
			//S'assurer au préalable que les fichiers de langue "fr" et "en" sont bien présents.
			loc = factory.findLocale("fr");
			assertTrue("fr != "+loc.getLanguage(), "fr" == loc.getLanguage());
			loc = factory.findLocale("en");
			assertTrue("en != "+loc.getLanguage(), "en" == loc.getLanguage());
		}
		else
			assertFalse(true);
	}

	@Test
	public void saveConfigTest(){
		if(factory != null){
			factory.config.language = "fr";
			factory.saveConfig();
			FileHandle fh = Gdx.files.local("configs/config.json");
			if(fh.exists()){
				factory.loadConfig();
				assertEquals("fr != "+factory.config.language, factory.config.language, "fr");

				factory.config.language = "en";
				factory.saveConfig();

				factory.loadConfig();
				assertEquals("en != "+factory.config.language, factory.config.language, "en");
			}
			else
				assertFalse("le fichier config.json n'a pas été créé !", true);
		}
		else
			assertFalse(true);
	}

}
