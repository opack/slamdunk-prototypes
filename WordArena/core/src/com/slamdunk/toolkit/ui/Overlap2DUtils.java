package com.slamdunk.toolkit.ui;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.slamdunk.wordarena.screens.SimpleButtonI18NScript;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;

public class Overlap2DUtils {

	public static SimpleButtonI18NScript createSimpleButtonScript(SceneLoader sceneLoader, String buttonId, ClickListener clickListener) {
		CompositeItem composite = sceneLoader.sceneActor.getCompositeById(buttonId);
		if (composite == null) {
			return null;
		}
		
		SimpleButtonI18NScript script = new SimpleButtonI18NScript();
		script.addListener(clickListener);
		composite.addScript(script);
		return script;
	}
}
