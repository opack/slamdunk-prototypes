package com.slamdunk.toolkit.lang;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileHelper {
	/**
	 * Retourne les FileHandle des fichiers contenus dans le 
	 * répertoire spécifié.
	 * Cette méthode travail dans Gdx.files.internal.
	 * Un hack est utilisé pour les applis Desktop : on
	 * va chercher dans bin car ce type d'application ne
	 * supporte pas la méthode FileHandle.list().
	 * @param directory
	 * @return
	 */
	public static FileHandle[] getFiles(String directory) {
		FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Android) {
		   dirHandle = Gdx.files.internal(directory);
		} else {
		  // ApplicationType.Desktop ..
		  dirHandle = Gdx.files.internal("./bin/" + directory);
		}
		return dirHandle.list();
	}
}
