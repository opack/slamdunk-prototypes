package com.slamdunk.wordarena.utils;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.actor.CompositeItem;
import com.uwsoft.editor.renderer.script.SimpleButtonScript;

public class Overlap2DUtils {

	public static CompositeItem createSimpleButtonScript(SceneLoader sceneLoader, String buttonId, ClickListener clickListener) {
		CompositeItem composite = sceneLoader.sceneActor.getCompositeById(buttonId);
		
		if (composite != null) {
			SimpleButtonScript script = new SimpleButtonScript();
			script.addListener(clickListener);
			composite.addScript(script);
		}
		
		return composite;
	}
}
