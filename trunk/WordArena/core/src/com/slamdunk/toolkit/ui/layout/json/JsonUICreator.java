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
 * Cr�e l'IHM � partir d'un descripteur json
 */
public class JsonUICreator {
	private static final Map<String, JsonComponentBuilder> BUILDERS;
	private Map<String, Actor> actorsMap;
	private Skin skin;
	
	static {
		// Charge les builders de widget
		BUILDERS = new HashMap<String, JsonComponentBuilder>();
		BUILDERS.put("Label", new LabelJsonBuilder("fr"));// DBG langCode � variabiliser !
		BUILDERS.put("TextButton", new TextButtonJsonBuilder("fr"));// DBG langCode � variabiliser !
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
	 * Parse le fichier indiqu� et cr�e les widgets d�crits
	 * @param uiFile
	 */
	public void load(String uiFile) {
		// Ouvre le fichier et r�cup�re les racines
		JsonValue root = new JsonReader().parse(Gdx.files.internal(uiFile));
		JsonValue widgets = root.get("widgets");
		JsonValue values = root.get("values");
		
		// Cr�e les widgets
		JsonValue widget = widgets.child();
		while (widget != null) {
			// R�cup�re et configure le builder ad�quat
			JsonComponentBuilder builder = getBuilder(widget);
			if (builder == null) {
				throw new IllegalArgumentException("Aucun builder pour le widget " + widget);
			}
			builder.setWidgetDescription(widget);
			builder.setValues(values);
			
			// Cr�e l'objet indiqu�
			Actor actor = builder.build(skin);
			
			// Stocke l'objet dans la table
			actorsMap.put(actor.getName(), actor);
			
			// Prend le widget suivant
			widget = widget.next();
		}
	}

	/**
	 * Retourne le builder capable de cr�er le widget indiqu�, d'apr�s sa classe
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
