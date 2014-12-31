package com.slamdunk.wordarena.gameparts.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.gameparts.components.position.PathFollowerScript;
import com.slamdunk.toolkit.gameparts.components.position.TrackerScript;
import com.slamdunk.toolkit.gameparts.components.renderers.ParticleRendererPart;
import com.slamdunk.toolkit.gameparts.components.renderers.SpriteRendererPart;
import com.slamdunk.toolkit.gameparts.components.ui.UIButtonPart;
import com.slamdunk.toolkit.gameparts.components.ui.UIComponent;
import com.slamdunk.toolkit.gameparts.components.ui.UIProgressBarPart;
import com.slamdunk.toolkit.gameparts.creators.GameObjectFactory;
import com.slamdunk.toolkit.gameparts.gameobjects.GameObject;
import com.slamdunk.toolkit.gameparts.gameobjects.Sprite;
import com.slamdunk.toolkit.gameparts.scene.Layer;
import com.slamdunk.toolkit.gameparts.scene.Scene;
import com.slamdunk.wordarena.gameparts.creators.CustomSVGLoader;
import com.slamdunk.wordarena.gameparts.prefabs.Castle;
import com.slamdunk.wordarena.gameparts.prefabs.Paladin;
import com.slamdunk.wordarena.gameparts.scripts.ButtonClickTestScript;
import com.slamdunk.wordarena.gameparts.scripts.PathSpeedTweakerScript;

public class GamePartsTestScreen implements Screen {
	private Scene scene;
	
	public GamePartsTestScreen() {
		createScene();
	}
	
	private void createScene() {
		// Crée une nouvelle scène à la taille de l'écran et avec une couche d'IHM
		scene = new Scene(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		final Layer backgroundLayer = scene.addLayer("background");	// Le fond de carte sera ajouté dans cette couche
		final Layer foregroundLayer = scene.addLayer("foreground");	// Cette couche contiendra tous les autres éléments
		
		// Charge le SVG contenant le level design
		CustomSVGLoader sceneLoader = new CustomSVGLoader("battlefields/battlefield0.svg");
		
		// Ajoute un fond de carte à la scène
		Sprite map = GameObjectFactory.create(Sprite.class);
		map.spriteRenderer.spriteFile = "maps/battlefield0.png";
		backgroundLayer.addChild(map);
		
		// Ajoute un château
		foregroundLayer.createChild(Castle.class);
		
		// Ajoute un paladin
		Paladin paladin = foregroundLayer.createChild(Paladin.class);
		paladin.name = "Paladin";
		paladin.getComponent(SpriteRendererPart.class).origin.set(0.5f,0.5f); // L'origine des rotations est le centre
		paladin.getComponent(SpriteRendererPart.class).anchor.set(0.5f,0.5f); // Position toujours exprimée par rapport au centre
		paladin.getComponent(PathFollowerScript.class).path = sceneLoader.paths.get(0);
		paladin.getComponent(PathFollowerScript.class).speed = 50;
		
		// Ajoute un point qui suivra le paladin
		GameObject dot = paladin.createChild();
		dot.addComponent(SpriteRendererPart.class);
		dot.getComponent(SpriteRendererPart.class).spriteFile = "textures/dot.png";
		dot.transform.relativePosition.set(16, 16, 0);
		dot.addComponent(ParticleRendererPart.class);
		dot.getComponent(ParticleRendererPart.class).effectFile = "particles/firebeam.p";
		dot.getComponent(ParticleRendererPart.class).imagesDirectory = "particles";
		dot.getComponent(ParticleRendererPart.class).active = false;
		
		// Bouton
		Skin skin = new Skin(Gdx.files.internal("skins/uiskin/uiskin.json"));
		GameObject button = foregroundLayer.createChild();
		button.transform.relativePosition.set(10, 240, 0);
		button.addComponent(UIButtonPart.class);
		button.getComponent(UIButtonPart.class).skin = skin;
		button.getComponent(UIButtonPart.class).script = new ButtonClickTestScript();
		button.getComponent(UIButtonPart.class).text = "Test bouton";
		
		// Slider
		GameObject slider = foregroundLayer.createChild();
		slider.addComponent(UIProgressBarPart.class);
		slider.transform.relativePosition.set(100, 120, 0);
		slider.getComponent(UIProgressBarPart.class).skin = skin;
		slider.getComponent(UIProgressBarPart.class).minValue = 0;
		slider.getComponent(UIProgressBarPart.class).currentValue = 50;
		slider.getComponent(UIProgressBarPart.class).maxValue = 100;
		slider.getComponent(UIProgressBarPart.class).stepSize = 1;
		slider.getComponent(UIProgressBarPart.class).verticalOriented = false;
		slider.getComponent(UIProgressBarPart.class).isSlider = true;
		slider.addComponent(PathSpeedTweakerScript.class);
		slider.getComponent(PathSpeedTweakerScript.class).pathFollower = paladin.getComponent(PathFollowerScript.class);
		
		// GameObject de test pour attacher un widget lambda
		GameObject anyWidgetUI = foregroundLayer.createChild();
		anyWidgetUI.addComponent(UIComponent.class);
		anyWidgetUI.getComponent(UIComponent.class).skin = skin;
		anyWidgetUI.getComponent(UIComponent.class).actor = new Label("Test label", skin);
		anyWidgetUI.transform.relativePosition.set(100, 150, 0);
		
		// Place la caméra au centre de l'écran et suit le Paladin
		scene.observationPoint.transform.relativePosition.x = scene.observationPoint.camera.viewportWidth / 2;
		scene.observationPoint.transform.relativePosition.y = scene.observationPoint.camera.viewportHeight / 2;
		scene.observationPoint.addComponent(TrackerScript.class);
		scene.observationPoint.getComponent(TrackerScript.class).target = paladin;
		scene.observationPoint.getComponent(TrackerScript.class).leech = 100;
		scene.observationPoint.getComponent(TrackerScript.class).reachTime = 1;
		
		// Initialise les GameObjects de la scène
		scene.init();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

	float dbg;
	@Override
	public void render(float delta) {
		scene.render(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
}
