package com.ndop.gemcraft.framework.factory;

import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

@SuppressWarnings("serial")
public class FactoryException extends Exception{
	/**
	 * Fichier de log. Important pour les fichiers qui ne seront pas dans le jar.
	 */
	private static FileHandle logFile = Gdx.files.local("log.txt");
	/**
	 * Exception dans le chargement des fichiers du jeu.
	 * @param message description de l'erreur.
	 */
	public FactoryException(String message) {
		super(message);
		
		String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
	    String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
	    int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
	    
		logFile.writeString(className+"->"+methodName+"["+lineNumber+"] : "+message + "  ->  [" + new Date() + "] \n", true);
	}

	/**
	 * Exception dans le chargement des fichiers du jeu.
	 * @param message description de l'erreur.
	 * @param cause cause de l'erreur.
	 */
	public FactoryException(String message, Throwable cause) {
		super(message, cause);
		
		String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
	    String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
	    int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
	    
		logFile.writeString(className+"->"+methodName+"["+lineNumber+"] : "+message + " Cause : " + cause.getMessage() + "  ->  [" + new Date() + "] \n", true);
	}
}
