package com.slamdunk.wordarena.utils;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.script.SimpleButtonScript;

public class Overlap2DUtils {

	public static void createSimpleButtonScript(SceneLoader sceneLoader, String buttonId, ClickListener clickListener) {
		SimpleButtonScript script = new SimpleButtonScript();
		script.addListener(clickListener);
		sceneLoader.sceneActor.getCompositeById(buttonId).addScript(script);
	}
}
