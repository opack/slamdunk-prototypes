package com.slamdunk.wordarena.gameparts.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.gameparts.components.position.CircleLayoutScript;
import com.slamdunk.toolkit.gameparts.components.position.GridLayoutScript;
import com.slamdunk.toolkit.gameparts.components.position.PathFollowerScript;
import com.slamdunk.toolkit.gameparts.components.position.TrackerScript;
import com.slamdunk.toolkit.gameparts.components.renderers.ParticleRendererPart;
import com.slamdunk.toolkit.gameparts.components.renderers.SpriteRendererPart;
import com.slamdunk.toolkit.gameparts.components.ui.UIButtonPart;
import com.slamdunk.toolkit.gameparts.components.ui.UIComponent;
import com.slamdunk.toolkit.gameparts.components.ui.UIImagePart;
import com.slamdunk.toolkit.gameparts.components.ui.UIProgressBarPart;
import com.slamdunk.toolkit.gameparts.components.ui.UITextFieldPart;
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
		
		// TextField
		GameObject textField = foregroundLayer.createChild();
		textField.transform.relativePosition.set(150, 240, 0);
		textField.addComponent(UITextFieldPart.class);
		textField.getComponent(UITextFieldPart.class).skin = skin;
		textField.getComponent(UITextFieldPart.class).text = "Test TextField";
		
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
		anyWidgetUI.getComponent(UIComponent.class).actor = new SelectBox<String>(skin);
		anyWidgetUI.transform.relativePosition.set(100, 150, 0);
		
		// Test layout en cercle
		GameObject circleLayout = foregroundLayer.createChild();
		circleLayout.transform.relativePosition.set(400, 240, 0);
		circleLayout.addComponent(UIImagePart.class);
		circleLayout.getComponent(UIImagePart.class).textureRegion = new TextureRegion(new Texture(Gdx.files.internal("textures/dot.png")));
		circleLayout.addComponent(CircleLayoutScript.class);
		circleLayout.getComponent(CircleLayoutScript.class).radius = 50;
		GameObject circleLayoutChild1 = circleLayout.createChild();
		circleLayoutChild1.addComponent(UIButtonPart.class);
		circleLayoutChild1.getComponent(UIButtonPart.class).skin = skin;
		circleLayoutChild1.getComponent(UIButtonPart.class).text = "C1";
		GameObject circleLayoutChild2 = circleLayout.createChild();
		circleLayoutChild2.addComponent(UIButtonPart.class);
		circleLayoutChild2.getComponent(UIButtonPart.class).skin = skin;
		circleLayoutChild2.getComponent(UIButtonPart.class).text = "C2";
		GameObject circleLayoutChild3 = circleLayout.createChild();
		circleLayoutChild3.addComponent(UIButtonPart.class);
		circleLayoutChild3.getComponent(UIButtonPart.class).skin = skin;
		circleLayoutChild3.getComponent(UIButtonPart.class).text = "C3";
		GameObject circleLayoutChild4 = circleLayout.createChild();
		circleLayoutChild4.addComponent(UIButtonPart.class);
		circleLayoutChild4.getComponent(UIButtonPart.class).skin = skin;
		circleLayoutChild4.getComponent(UIButtonPart.class).text = "C4";
		GameObject circleLayoutChild5 = circleLayout.createChild();
		circleLayoutChild5.addComponent(UIButtonPart.class);
		circleLayoutChild5.getComponent(UIButtonPart.class).skin = skin;
		circleLayoutChild5.getComponent(UIButtonPart.class).text = "C5";
		
		// Test layout en grille
		GameObject gridLayout = foregroundLayer.createChild();
		gridLayout.transform.relativePosition.set(400, 100, 0);
		gridLayout.addComponent(UIImagePart.class);
		gridLayout.getComponent(UIImagePart.class).textureRegion = new TextureRegion(new Texture(Gdx.files.internal("textures/dot.png")));
		gridLayout.addComponent(GridLayoutScript.class);
		gridLayout.getComponent(GridLayoutScript.class).nbColumns = 3;
		gridLayout.getComponent(GridLayoutScript.class).nbRows = 3;
		gridLayout.getComponent(GridLayoutScript.class).columnWidth = 100;
		gridLayout.getComponent(GridLayoutScript.class).rowHeight = 50;
		GameObject gridLayoutChild1 = gridLayout.createChild();
		gridLayoutChild1.addComponent(UIButtonPart.class);
		gridLayoutChild1.getComponent(UIButtonPart.class).skin = skin;
		gridLayoutChild1.getComponent(UIButtonPart.class).text = "G1";
		GameObject gridLayoutChild2 = gridLayout.createChild();
		gridLayoutChild2.addComponent(UIButtonPart.class);
		gridLayoutChild2.getComponent(UIButtonPart.class).skin = skin;
		gridLayoutChild2.getComponent(UIButtonPart.class).text = "G2";
		GameObject gridLayoutChild3 = gridLayout.createChild();
		gridLayoutChild3.addComponent(UIButtonPart.class);
		gridLayoutChild3.getComponent(UIButtonPart.class).skin = skin;
		gridLayoutChild3.getComponent(UIButtonPart.class).text = "G3";
		GameObject gridLayoutChild4 = gridLayout.createChild();
		gridLayoutChild4.addComponent(UIButtonPart.class);
		gridLayoutChild4.getComponent(UIButtonPart.class).skin = skin;
		gridLayoutChild4.getComponent(UIButtonPart.class).text = "G4";
		GameObject gridLayoutChild5 = gridLayout.createChild();
		gridLayoutChild5.addComponent(UIButtonPart.class);
		gridLayoutChild5.getComponent(UIButtonPart.class).skin = skin;
		gridLayoutChild5.getComponent(UIButtonPart.class).text = "G5";
		
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
