package com.ndop.gemcraft.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ndop.gemcraft.test.factory.ConfigFactoryTest;
import com.ndop.gemcraft.test.factory.LevelFactoryTest;

@RunWith(Suite.class)
@SuiteClasses({ConfigFactoryTest.class, LevelFactoryTest.class})

public class TestAll {
	
	public static void main(String[] args) {
		JUnitCore.runClasses(TestAll.class);
	}
}
