package com.slamdunk.wordarena;

import com.slamdunk.wordarena.data.UserData;

/**
 * Classe chargée de la gestion du compte utilisateur (connexion,
 * déconnexion, récupération des infos sur l'utilisateur...)
 */
public class UserManager {
	private static UserManager instance;
	
	private UserData user;
	
	private UserManager() {
	}
	
	public static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}
	
	/**
	 * Tente de connecter l'utilisateur en utilisant les informations
	 * de connexion enregistrées.
	 * @return false si l'utilisateur n'a pas pu être connecté pour une
	 * raison ou une autre (pas de réseau, pas d'identifiants...)
	 */
	public boolean logIn() {
		// TODO DBG Triche en attendant la vraie connexion de l'utilisateur
		user = new UserData();
		user.name = "Alan";
		return true;
	}
	
	public void logOut() {
		// TODO DBG Faire une vraie déconnexion
		user = null;
	}
	
	/**
	 * Retourne les informations sur l'utilisateur connecté
	 * @return null si personne n'est connecté
	 */
	public UserData getUserData() {
		return user;
	}
}
