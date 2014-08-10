package com.slamdunk.toolkit.ui.layout.json;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.ui.layout.json.builders.GroupJsonBuilder;
import com.slamdunk.toolkit.ui.layout.json.builders.ImageJsonBuilder;
import com.slamdunk.toolkit.ui.layout.json.builders.JsonComponentBuilder;
import com.slamdunk.toolkit.ui.layout.json.builders.LabelJsonBuilder;
import com.slamdunk.toolkit.ui.layout.json.builders.ScrollPaneJsonBuilder;
import com.slamdunk.toolkit.ui.layout.json.builders.TableJsonBuilder;
import com.slamdunk.toolkit.ui.layout.json.builders.TextButtonJsonBuilder;

/**
 * Crée l'IHM à partir d'un descripteur json
 */
public class JsonUICreator {
	private static final Map<String, JsonComponentBuilder> BUILDERS;
	private Map<String, Actor> actorsMap;
	private Skin skin;
	
	static {
		// Charge les builders de widget
		BUILDERS = new HashMap<String, JsonComponentBuilder>();
		BUILDERS.put("Label", new LabelJsonBuilder("fr"));// DBG langCode à variabiliser !
		BUILDERS.put("TextButton", new TextButtonJsonBuilder("fr"));// DBG langCode à variabiliser !
		BUILDERS.put("ScrollPane", new ScrollPaneJsonBuilder());
		BUILDERS.put("Image", new ImageJsonBuilder());
		BUILDERS.put("Group", new GroupJsonBuilder());
		BUILDERS.put("Table", new TableJsonBuilder());
	}
	
	public JsonUICreator(Skin skin) {
		this.skin = skin;
		actorsMap = new HashMap<String, Actor>();
	}

	/**
	 * Parse le fichier indiqué et crée les widgets décrits
	 * @param uiFile
	 */
	public void load(String uiFile) {
		// Ouvre le fichier et récupère les racines
		JsonValue root = new JsonReader().parse(Gdx.files.internal(uiFile));
		JsonValue widgets = root.get("widgets");
		JsonValue values = root.get("values");
		
		// Crée les widgets
		JsonValue widget = widgets.child();
		while (widget != null) {
			// Récupère et configure le builder adéquat
			JsonComponentBuilder builder = getBuilder(widget);
			if (builder == null) {
				throw new IllegalArgumentException("Aucun builder pour le widget " + widget);
			}
			builder.setWidgetDescription(widget);
			builder.setValues(values);
			
			// Crée l'objet indiqué
			Actor actor = builder.build(skin);
			
			// Stocke l'objet dans la table
			actorsMap.put(actor.getName(), actor);
			
			// Prend le widget suivant
			widget = widget.next();
		}
	}

	/**
	 * Retourne le builder capable de créer le widget indiqué, d'après sa classe
	 * @param widget
	 * @return
	 */
	private JsonComponentBuilder getBuilder(JsonValue widget) {
		String clazz = widget.getString("class");
		JsonComponentBuilder builder = BUILDERS.get(clazz);
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
