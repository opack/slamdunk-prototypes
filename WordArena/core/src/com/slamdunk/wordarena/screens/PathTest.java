package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.svg.SVGParse;
import com.slamdunk.toolkit.svg.converters.SVGPathToBezier;
import com.slamdunk.toolkit.svg.elements.SVGElement;
import com.slamdunk.toolkit.svg.elements.SVGElementPath;
import com.slamdunk.toolkit.svg.elements.SVGRootElement;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.CursorMode;
import com.slamdunk.toolkit.world.path.ComplexPathCursor;

public class PathTest extends SlamScreen {
	final int SAMPLE_POINTS = 100;
	final float SAMPLE_POINT_DISTANCE = 1f / SAMPLE_POINTS;
	
	SpriteBatch spriteBatch;
	ImmediateModeRenderer20 renderer;
	Sprite obj;
	Array<ComplexPath> paths;
	ComplexPathCursor cursor;
	float t;
	float speed = 570; // 570 pixels en 1 secondes
	float wait = 0f;
	
	final Vector2 tmpTouchPos = new Vector2();
	final Vector2 tmpNearestCoords = new Vector2();
	final Vector2 tmpComputedPos = new Vector2();
	final Vector2 computedPosition = new Vector2();

	public PathTest() {
		renderer = new ImmediateModeRenderer20(false, false, 0);
		spriteBatch = new SpriteBatch();
		obj = new Sprite(new Texture(Gdx.files.internal("textures/dbg_ninja.png")));
		obj.setSize(40, 40);
		obj.setOriginCenter();
		
		paths = new Array<ComplexPath>();
		parse("battlefields/battlefield0.svg");
		cursor = new ComplexPathCursor(paths.get(0), speed, CursorMode.LOOP_PINGPONG);
		
		Gdx.input.setInputProcessor(this);
	}

	private void parse(String file) {
		SVGParse parser = new SVGParse(Gdx.files.internal(file));
		SVGRootElement root = new SVGRootElement();
		parser.parse(root);
		
		SVGElement svgPaths = root.getChildById("paths");
		SVGPathToBezier converter = new SVGPathToBezier(root.height);
		for (SVGElement child : svgPaths.getChildren()) {
			if("path".equals(child.getName())) {
				Array<Bezier<Vector2>> beziers = converter.convert((SVGElementPath)child);
				ComplexPath path = new ComplexPath();
				for (Bezier<Vector2> bezier : beziers) {
					path.add(bezier);
				}
				paths.add(path);
			}
		}
	}

	@Override
	public void render(float delta) {
		// Efface l'écran
		GL20 gl = Gdx.gl20;
		gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if (wait > 0) {
			// Pause, le cas échéant
			wait -= Gdx.graphics.getDeltaTime();
		} else {
			// Calcul de la position du sprite
			cursor.move(Gdx.graphics.getDeltaTime(), computedPosition);
			obj.setCenter(computedPosition.x, computedPosition.y);
		}
		
		// Dessin des courbes
		for (ComplexPath path : paths) {
			renderer.begin(spriteBatch.getProjectionMatrix(), GL20.GL_LINE_STRIP);
			renderer.color(0f, 0f, 0f, 1f);
			for (Path<Vector2> segment : path) {
				float val = 0f;
				while (val <= 1f) {
					segment.valueAt(/* out: */computedPosition, val);
					renderer.vertex(computedPosition.x, computedPosition.y, 0);
					val += SAMPLE_POINT_DISTANCE;
				}
			}
			renderer.end();
		}
		
		// Dessin de l'image
		spriteBatch.begin();
		obj.draw(spriteBatch);
		spriteBatch.end();
	}
	
	private void touch (int x, int y) {
		// Récupère un curseur sur le chemin le plus proche de la touche
		// parmi tous les chemins
		tmpTouchPos.set(x, Gdx.graphics.getHeight() - y);
		float minDistance = -1;
		float distance;
		ComplexPathCursor nearestCursor = null;
		for (int pathIndex = 0; pathIndex < paths.size; pathIndex++) {
			// Récupère le curseur du point le plus proche sur ce chemin
			ComplexPath path = paths.get(pathIndex);
			ComplexPathCursor probe = new ComplexPathCursor(path, cursor.getSpeed(), cursor.getMode());
			distance = path.locate(tmpTouchPos, probe);
			
			// Regarde s'il est plus près que les autres curseurs
			if (nearestCursor == null
			|| distance < minDistance) {
				minDistance = distance;
				nearestCursor = probe;
			}
		}
		
		// Récupère la position correspondante si on a cliqué assez proche d'un chemin
		//if (minDistance < 25) {
			cursor = nearestCursor;
			cursor.valueAt(tmpComputedPos);
			obj.setCenter(tmpComputedPos.x, tmpComputedPos.y);
			wait = 0.2f;
		//}
	}
	
	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		touch(screenX, screenY);
		return super.touchUp(screenX, screenY, pointer, button);
	}
	
	@Override
	public boolean touchDragged (int screenX, int screenY, int pointer) {
		touch(screenX, screenY);
		return super.touchDragged(screenX, screenY, pointer);
	}

	@Override
	public String getName() {
		return "PATHTEST";
	}
}