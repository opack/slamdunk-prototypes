package com.slamdunk.toolkit.ui.loader;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.settings.SlamSettings;
import com.slamdunk.toolkit.ui.loader.builders.layouts.PopupJsonBuilder;
import com.slamdunk.toolkit.ui.loader.builders.layouts.ScrollPaneJsonBuilder;
import com.slamdunk.toolkit.ui.loader.builders.layouts.TableJsonBuilder;
import com.slamdunk.toolkit.ui.loader.builders.layouts.VerticalGroupJsonBuilder;
import com.slamdunk.toolkit.ui.loader.builders.layouts.WindowJsonBuilder;
import com.slamdunk.toolkit.ui.loader.builders.widgets.GroupJsonBuilder;
import com.slamdunk.toolkit.ui.loader.builders.widgets.ImageJsonBuilder;
import com.slamdunk.toolkit.ui.loader.builders.widgets.JsonComponentBuilder;
import com.slamdunk.toolkit.ui.loader.builders.widgets.LabelJsonBuilder;
import com.slamdunk.toolkit.ui.loader.builders.widgets.TextButtonJsonBuilder;
import com.slamdunk.toolkit.ui.loader.builders.widgets.TextFieldJsonBuilder;

/**
 * Cr�e l'IHM � partir d'un descripteur JSON.
 * Le cr�ateur est initialis� en appelant la m�thode {@link #load(String)}
 * afin de charger la description des composants contenue dans un fichier JSON. Des
 * objets Actor sont alors cr��s � partir de ces propri�t�s.
 * 
 * Ensuite, la m�thode {@link #populate(Stage)} remplit le Stage indiqu� avec
 * les Actors pr�c�demment cr��s.
 */
public class JsonUIBuilder {
	private Map<String, JsonComponentBuilder> builders;
	private Map<String, Actor> actorsMap;
	private Skin skin;
	/**
	 * Table de valeurs � utiliser pendant la cr�ation de ce layout
	 */
	private JsonValue values; 
	
	public JsonUIBuilder(Skin skin) {
		this.skin = skin;
		actorsMap = new HashMap<String, Actor>();
		initBuilders();
	}

	/**
	 * Charge les builders de widget.
	 */
	private void initBuilders() {
		builders = new HashMap<String, JsonComponentBuilder>();
		
		builders.put("ScrollPane", new ScrollPaneJsonBuilder(this));
		builders.put("Table", new TableJsonBuilder(this));
		builders.put("VerticalGroup", new VerticalGroupJsonBuilder(this));
		builders.put("Window", new WindowJsonBuilder(this));
		builders.put("Popup", new PopupJsonBuilder(this));
		
		final String lang = SlamSettings.LANGUAGE.get();
		builders.put("Group", new GroupJsonBuilder());
		builders.put("Image", new ImageJsonBuilder());
		builders.put("Label", new LabelJsonBuilder(lang));
		builders.put("TextButton", new TextButtonJsonBuilder(lang));
		builders.put("TextField", new TextFieldJsonBuilder(lang));
	}

	/**
	 * Parse le fichier indiqu� et cr�e les widgets d�crits
	 * @param uiFile
	 */
	public void load(String uiFile) {
		// Ouvre le fichier et r�cup�re les racines
		JsonValue root = new JsonReader().parse(Gdx.files.internal(uiFile));
		JsonValue widgets = root.get("widgets");
		values = root.get("values");
		
		// Cr�e les widgets
		JsonValue widget = widgets.child();
		while (widget != null) {
			// Cr�e l'objet
			Actor actor = build(widget);
			
			// Stocke l'objet dans la table
			actorsMap.put(actor.getName(), actor);
			
			// Prend le widget suivant
			widget = widget.next();
		}
	}

	/**
	 * Cr�e un objet en s'appuyant sur les builders d�finis
	 * et les donn�es lues dans le json
	 * @param values
	 * @param widget
	 * @return
	 */
	public Actor build(JsonValue widget) {
		// R�cup�re et configure le builder ad�quat
		JsonComponentBuilder builder = getBuilder(widget);
		if (builder == null) {
			throw new IllegalArgumentException("Aucun builder pour le widget " + widget);
		}
		builder.setWidgetDescription(widget);
		builder.setValues(values);
		
		// Cr�e l'objet indiqu�
		Actor actor = builder.build(skin);
		return actor;
	}

	/**
	 * Retourne le builder capable de cr�er le widget indiqu�, d'apr�s sa classe
	 * @param widget
	 * @return
	 */
	private JsonComponentBuilder getBuilder(JsonValue widget) {
		String clazz = widget.getString("class");
		JsonComponentBuilder builder = builders.get(clazz);
		if (builder == null) {
			throw new IllegalStateException("Class " + clazz + " has no associated UIWidgetBuilder.");
		}
		return builder;
	}

	/**
	 * Ajoute les widgets cr��s au stage indiqu�
	 * @param stage
	 */
	public void populate(Stage stage) {
		for (Actor actor : actorsMap.values()) {
			stage.addActor(actor);
		}
	}

	public Actor getActor(String name) {
		return actorsMap.get(name);
	}
}
