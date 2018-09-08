package com.ndop.gemcraft.framework.model;

import java.io.File;
import java.io.FileFilter;

/**
 * Classe de filtre des fichiers.
 * @author ndongmo
 *
 */
public class MyFileFilter implements FileFilter{
	private String regex;
	
	public MyFileFilter(String regex){
		this.regex = regex;
	}

	@Override
	public boolean accept(File f) {
		return f.getName().matches(regex);
	}

}