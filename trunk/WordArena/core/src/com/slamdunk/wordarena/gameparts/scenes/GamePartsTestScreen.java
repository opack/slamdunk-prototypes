package com.slamdunk.wordarena.gameparts.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slamdunk.toolkit.gameparts.components.position.AlignScript;
import com.slamdunk.toolkit.gameparts.components.position.AlignScript.AlignSpots;
import com.slamdunk.toolkit.gameparts.components.position.CircleLayoutScript;
import com.slamdunk.toolkit.gameparts.components.position.GridLayoutScript;
import com.slamdunk.toolkit.gameparts.components.position.PathFollowerScript;
import com.slamdunk.toolkit.gameparts.components.position.SizePart;
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
import com.slamdunk.wordarena.gameparts.scripts.ChangeAlignScript;
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
		button.transform.relativePosition.set(100, 300, 0);
		button.addComponent(SizePart.class);
		button.addComponent(UIButtonPart.class);
		button.getComponent(UIButtonPart.class).skin = skin;
		button.getComponent(UIButtonPart.class).onClickScript = new ButtonClickTestScript();
		button.getComponent(UIButtonPart.class).text = "Test bouton";
		
		// Bouton fils avec alignement
		GameObject buttonChild = button.createChild();
		buttonChild.addComponent(AlignScript.class);
		buttonChild.getComponent(AlignScript.class).anchor = button;
		buttonChild.getComponent(AlignScript.class).alignSpot = AlignSpots.BOTTOM_CENTER;
		buttonChild.getComponent(AlignScript.class).anchorAlignSpot = AlignSpots.BOTTOM_RIGHT;
		buttonChild.addComponent(UIButtonPart.class);
		buttonChild.getComponent(UIButtonPart.class).skin = skin;
		buttonChild.getComponent(UIButtonPart.class).text = "Child";
		buttonChild.addComponent(ChangeAlignScript.class);
		buttonChild.getComponent(UIButtonPart.class).onClickScript = buttonChild.getComponent(ChangeAlignScript.class);
		GameObject buttonGrandChild = buttonChild.createChild();
		buttonGrandChild.transform.relativePosition.set(30, 30, 0);
		buttonGrandChild.addComponent(UIButtonPart.class);
		buttonGrandChild.getComponent(UIButtonPart.class).skin = skin;
		buttonGrandChild.getComponent(UIButtonPart.class).text = "Grand Child";
		
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
		circleLayout.transform.relativePosition.set(400, 350, 0);
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
		
		// Test layout en cercle avec AlignScript
		GameObject circleLayoutAlign = foregroundLayer.createChild();
		circleLayoutAlign.transform.relativePosition.set(600, 350, 0);
		circleLayoutAlign.addComponent(CircleLayoutScript.class);
		circleLayoutAlign.getComponent(CircleLayoutScript.class).radius = 50;
		
		GameObject circleLayoutAlignCenter = foregroundLayer.createChild();
		circleLayoutAlignCenter.addComponent(SizePart.class);
		circleLayoutAlignCenter.addComponent(AlignScript.class);
		circleLayoutAlignCenter.getComponent(AlignScript.class).anchor = circleLayoutAlign;
		circleLayoutAlignCenter.addComponent(UIImagePart.class);
		circleLayoutAlignCenter.getComponent(UIImagePart.class).textureRegion = new TextureRegion(new Texture(Gdx.files.internal("textures/dot.png")));
		
		GameObject circleLayoutAlignChild1 = circleLayoutAlign.createChild();
		GameObject circleLayoutAlignChild1Btn = circleLayoutAlignChild1.createChild();
		circleLayoutAlignChild1Btn.addComponent(SizePart.class);
		circleLayoutAlignChild1Btn.addComponent(AlignScript.class);
		circleLayoutAlignChild1Btn.getComponent(AlignScript.class).anchor = circleLayoutAlignChild1;
		circleLayoutAlignChild1Btn.addComponent(UIButtonPart.class);
		circleLayoutAlignChild1Btn.getComponent(UIButtonPart.class).skin = skin;
		circleLayoutAlignChild1Btn.getComponent(UIButtonPart.class).text = "C1a";
		circleLayoutAlignChild1Btn.addComponent(ChangeAlignScript.class);
		circleLayoutAlignChild1Btn.getComponent(UIButtonPart.class).onClickScript = circleLayoutAlignChild1Btn.getComponent(ChangeAlignScript.class);
		
		GameObject circleLayoutAlignChild2 = circleLayoutAlign.createChild();
		GameObject circleLayoutAlignChild2Btn = circleLayoutAlignChild2.createChild();
		circleLayoutAlignChild2Btn.addComponent(SizePart.class);
		circleLayoutAlignChild2Btn.addComponent(AlignScript.class);
		circleLayoutAlignChild2Btn.getComponent(AlignScript.class).anchor = circleLayoutAlignChild2;
		circleLayoutAlignChild2Btn.addComponent(UIButtonPart.class);
		circleLayoutAlignChild2Btn.getComponent(UIButtonPart.class).skin = skin;
		circleLayoutAlignChild2Btn.getComponent(UIButtonPart.class).text = "C2a";
		
		GameObject circleLayoutAlignChild3 = circleLayoutAlign.createChild();
		GameObject circleLayoutAlignChild3Btn = circleLayoutAlignChild3.createChild();
		circleLayoutAlignChild3Btn.addComponent(SizePart.class);
		circleLayoutAlignChild3Btn.addComponent(AlignScript.class);
		circleLayoutAlignChild3Btn.getComponent(AlignScript.class).anchor = circleLayoutAlignChild3;
		circleLayoutAlignChild3Btn.addComponent(UIButtonPart.class);
		circleLayoutAlignChild3Btn.getComponent(UIButtonPart.class).skin = skin;
		circleLayoutAlignChild3Btn.getComponent(UIButtonPart.class).text = "C3a";
		
		GameObject circleLayoutAlignChild4 = circleLayoutAlign.createChild();
		GameObject circleLayoutAlignChild4Btn = circleLayoutAlignChild4.createChild();
		circleLayoutAlignChild4Btn.addComponent(SizePart.class);
		circleLayoutAlignChild4Btn.addComponent(AlignScript.class);
		circleLayoutAlignChild4Btn.getComponent(AlignScript.class).anchor = circleLayoutAlignChild4;
		circleLayoutAlignChild4Btn.addComponent(UIButtonPart.class);
		circleLayoutAlignChild4Btn.getComponent(UIButtonPart.class).skin = skin;
		circleLayoutAlignChild4Btn.getComponent(UIButtonPart.class).text = "C4a";
		
		GameObject circleLayoutAlignChild5 = circleLayoutAlign.createChild();
		GameObject circleLayoutAlignChild5Btn = circleLayoutAlignChild5.createChild();
		circleLayoutAlignChild5Btn.addComponent(SizePart.class);
		circleLayoutAlignChild5Btn.addComponent(AlignScript.class);
		circleLayoutAlignChild5Btn.getComponent(AlignScript.class).anchor = circleLayoutAlignChild5;
		circleLayoutAlignChild5Btn.addComponent(UIButtonPart.class);
		circleLayoutAlignChild5Btn.getComponent(UIButtonPart.class).skin = skin;
		circleLayoutAlignChild5Btn.getComponent(UIButtonPart.class).text = "C5a";
		
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
