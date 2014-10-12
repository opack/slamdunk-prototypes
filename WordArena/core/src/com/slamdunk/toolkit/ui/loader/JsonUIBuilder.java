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
 * Crée l'IHM à partir d'un descripteur JSON.
 * Le créateur est initialisé en appelant la méthode {@link #load(String)}
 * afin de charger la description des composants contenue dans un fichier JSON. Des
 * objets Actor sont alors créés à partir de ces propriétés.
 * 
 * Ensuite, la méthode {@link #populate(Stage)} remplit le Stage indiqué avec
 * les Actors précédemment créés.
 */
public class JsonUIBuilder {
	private Map<String, JsonComponentBuilder> builders;
	private Map<String, Actor> actorsMap;
	private Skin skin;
	/**
	 * Table de valeurs à utiliser pendant la création de ce layout
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
	 * Parse le fichier indiqué et crée les widgets décrits
	 * @param uiFile
	 */
	public void load(String uiFile) {
		// Ouvre le fichier et récupère les racines
		JsonValue root = new JsonReader().parse(Gdx.files.internal(uiFile));
		JsonValue widgets = root.get("widgets");
		values = root.get("values");
		
		// Crée les widgets
		JsonValue widget = widgets.child();
		while (widget != null) {
			// Crée l'objet
			Actor actor = build(widget);
			
			// Stocke l'objet dans la table
			actorsMap.put(actor.getName(), actor);
			
			// Prend le widget suivant
			widget = widget.next();
		}
	}

	/**
	 * Crée un objet en s'appuyant sur les builders définis
	 * et les données lues dans le json
	 * @param values
	 * @param widget
	 * @return
	 */
	public Actor build(JsonValue widget) {
		// Récupère et configure le builder adéquat
		JsonComponentBuilder builder = getBuilder(widget);
		if (builder == null) {
			throw new IllegalArgumentException("Aucun builder pour le widget " + widget);
		}
		builder.setWidgetDescription(widget);
		builder.setValues(values);
		
		// Crée l'objet indiqué
		Actor actor = builder.build(skin);
		return actor;
	}

	/**
	 * Retourne le builder capable de créer le widget indiqué, d'après sa classe
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
	 * Ajoute les widgets créés au stage indiqué
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
