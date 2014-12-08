package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.world.path.ComplexPath;
import com.slamdunk.toolkit.world.path.ComplexPathCursor;
import com.slamdunk.toolkit.world.path.CursorMode;
import com.slamdunk.toolkit.world.path.PathUtils;

public class PathTest extends SlamScreen {
	final int NB_COLORS = 200;
	final int SAMPLE_POINTS = 50;
	final float SAMPLE_POINT_DISTANCE = 1f / SAMPLE_POINTS;
	
	SpriteBatch spriteBatch;
	ImmediateModeRenderer20 renderer;
	Sprite obj;
	Array<ComplexPath> paths;
	ComplexPathCursor cursor;
	float t;
	float speed = 250; // 250 pixels en 1 secondes
	
	final Vector2 tmpTouchPos = new Vector2();
	final Vector2 tmpNearestCoords = new Vector2();
	final Vector2 tmpComputedPos = new Vector2();
	final Vector2 computedPosition = new Vector2();

	final Color[] colors;
	
	public PathTest() {
		renderer = new ImmediateModeRenderer20(false, true, 0);
		spriteBatch = new SpriteBatch();
		obj = new Sprite(new Texture(Gdx.files.internal("textures/dbg_X.png")));
		obj.setSize(16, 16);
		obj.setOriginCenter();
		
		paths = PathUtils.parseSVG("battlefields/battlefield0.svg", "paths");
		cursor = new ComplexPathCursor(paths.get(0), speed, CursorMode.LOOP_PINGPONG);
		
		colors = new Color[NB_COLORS];
		for (int i = 0; i < NB_COLORS; i++) {
			colors[i] = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);
		}
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		// Efface l'écran
		GL20 gl = Gdx.gl20;
		gl.glClearColor(0f, 0f, 0f, 1);
		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Calcul de la position du sprite
		cursor.move(Gdx.graphics.getDeltaTime(), computedPosition);
		obj.setCenter(computedPosition.x, computedPosition.y);
		
		// Dessin des courbes
		for (ComplexPath path : paths) {
			renderer.begin(spriteBatch.getProjectionMatrix(), GL20.GL_POINTS);
			int i = 0;
			for (Path<Vector2> segment : path) {
				float val = 0f;
				while (val <= 1f) {
					segment.valueAt(/* out: */computedPosition, val);
					renderer.color(colors[i]);
					renderer.vertex(computedPosition.x, computedPosition.y, 0);
					val += SAMPLE_POINT_DISTANCE;
				}
//				segment.valueAt(/* out: */computedPosition, 0);
//				renderer.color(colors[i]);
//				renderer.vertex(computedPosition.x, computedPosition.y, 0);
//				segment.valueAt(/* out: */computedPosition, 1);
//				renderer.color(colors[i]);
//				renderer.vertex(computedPosition.x, computedPosition.y, 0);
			}
			i++;
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
		ComplexPathCursor nearestCursor = PathUtils.selectNearestPath(paths, tmpTouchPos, 25, cursor);
		if (nearestCursor != null) {
			cursor = nearestCursor;
			cursor.valueAt(tmpComputedPos);
			obj.setCenter(tmpComputedPos.x, tmpComputedPos.y);
		}
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