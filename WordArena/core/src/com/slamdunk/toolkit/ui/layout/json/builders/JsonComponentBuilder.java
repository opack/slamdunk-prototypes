package com.slamdunk.toolkit.ui.layout.json.builders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Cr�e un actor � partir de donn�es JSON.
 * Doit �tre stateless !!!
 */
public abstract class JsonComponentBuilder {
	protected JsonValue actorDescription;
	protected JsonValue values;
	
	public JsonValue getWidgetDescription() {
		return actorDescription;
	}

	public void setWidgetDescription(JsonValue widgetDescription) {
		this.actorDescription = widgetDescription;
	}

	public JsonValue getValues() {
		return values;
	}

	public void setValues(JsonValue values) {
		this.values = values;
	}

	/**
	 * Construit l'objet
	 * @return
	 */
	public Actor build(Skin skin) {
		if (actorDescription == null) {
			throw new IllegalStateException("Call setWidgetDescription() first.");
		}
		
		// Construit un objet vierge
		String style = null;
		if (hasProperty("style")) {
			style = actorDescription.getString("style");
		}
		Actor actor = createEmpty(skin, style);
		
		// G�re la propri�t� name
		parseName(actor);
		
		// G�re la propri�t� x
		if (!parseX(actor)) {
			parseXKey(actor);
		}
		
		
		// G�re la propri�t� y
		parseYKey(actor);
		parseY(actor);
		
		// G�re la propri�t� w
		parseWidthKey(actor);
		parseWidth(actor);
		
		// G�re la propri�t� h
		parseHeightKey(actor);
		parseHeight(actor);
		
		// G�re la propri�t� visible
		parseVisibleKey(actor);
		parseVisible(actor);
		
		return actor;
	}
	
	protected abstract Actor createEmpty(Skin skin, String style);
	
	/**
	 * 
	 * @param actor
	 * @return true si la propri�t� a �t� trouv�e et lue
	 */
	protected boolean parseName(Actor actor) {
		actor.setName(actorDescription.getString("name"));
		return true;
	}
	
	/**
	 * 
	 * @param actor
	 * @return true si la propri�t� a �t� trouv�e et lue
	 */
	protected boolean parseX(Actor actor) {
		if (hasProperty("x")) {
			actor.setX(actorDescription.getFloat("x"));
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param actor
	 * @return true si la propri�t� a �t� trouv�e et lue
	 */
	protected boolean parseXKey(Actor actor) {
		if (hasProperty("x-key")) {
			String key = actorDescription.getString("x-key");
			actor.setX(values.getFloat(key));
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param actor
	 * @return true si la propri�t� a �t� trouv�e et lue
	 */
	protected boolean parseY(Actor actor) {
		if (hasProperty("y")) {
			actor.setY(actorDescription.getFloat("y"));
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param actor
	 * @return true si la propri�t� a �t� trouv�e et lue
	 */
	protected boolean parseYKey(Actor actor) {
		if (hasProperty("y-key")) {
			String key = actorDescription.getString("y-key");
			actor.setY(values.getFloat(key));
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param actor
	 * @return true si la propri�t� a �t� trouv�e et lue
	 */
	protected boolean parseWidth(Actor actor) {
		if (hasProperty("width")) {
			actor.setWidth(actorDescription.getFloat("width"));
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param actor
	 * @return true si la propri�t� a �t� trouv�e et lue
	 */
	protected boolean parseWidthKey(Actor actor) {
		if (hasProperty("width-key")) {
			String key = actorDescription.getString("width-key");
			actor.setWidth(values.getFloat(key));
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param actor
	 * @return true si la propri�t� a �t� trouv�e et lue
	 */
	protected boolean parseHeight(Actor actor) {
		if (hasProperty("height")) {
			actor.setHeight(actorDescription.getFloat("height"));
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param actor
	 * @return true si la propri�t� a �t� trouv�e et lue
	 */
	protected boolean parseHeightKey(Actor actor) {
		if (hasProperty("height-key")) {
			String key = actorDescription.getString("height-key");
			actor.setHeight(values.getFloat(key));
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param actor
	 * @return true si la propri�t� a �t� trouv�e et lue
	 */
	protected boolean parseVisible(Actor actor) {
		if (hasProperty("visible")) {
			actor.setVisible(actorDescription.getBoolean("visible"));
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param actor
	 * @return true si la propri�t� a �t� trouv�e et lue
	 */
	protected boolean parseVisibleKey(Actor actor) {
		if (hasProperty("visible-key")) {
			String key = actorDescription.getString("visible-key");
			actor.setVisible(values.getBoolean(key));
			return true;
		}
		return false;
	}

	protected boolean hasProperty(String property) {
		return actorDescription.get(property) != null;
	}
	
	/**
	 * Retourne la valeur de la table values associ�e � la cl� key.
	 * Si cette valeur est un objet, la valeur correspondant �
	 * la cl� discriminant est retourn�e.
	 * 
	 * @param key
	 * @param discriminant
	 * @return
	 */
	protected String getValueString(String key, String discriminant) {
		JsonValue value = values.get(key);
		if (value.isString()) {
			return value.asString();
		}
		if (value.isObject()) {
			return value.getString(discriminant);
		}
		return null;
	}
}
