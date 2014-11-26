package com.slamdunk.wordarena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import com.slamdunk.toolkit.screen.SlamScreen;
import com.slamdunk.toolkit.world.path.CursorMode;
import com.slamdunk.toolkit.world.path.PathList;
import com.slamdunk.toolkit.world.path.PathListCursor;

public class PathTest extends SlamScreen {
	int SAMPLE_POINTS = 100;
	float SAMPLE_POINT_DISTANCE = 1f / SAMPLE_POINTS;
	
	SpriteBatch spriteBatch;
	ImmediateModeRenderer20 renderer;
	Sprite obj;
	PathList<Vector2> path;
	PathListCursor<Vector2> cursor;
	float t;
	float speed = 570; // 570 pixels en 1 secondes
	float wait = 0f;

	public PathTest() {
		renderer = new ImmediateModeRenderer20(false, false, 0);
		spriteBatch = new SpriteBatch();
		obj = new Sprite(new Texture(Gdx.files.internal("textures/dbg_ninja.png")));
		obj.setSize(40, 40);
		obj.setOriginCenter();
//		path = new PathList<Vector2>(false,
//			new Vector2(120, 400),
//			new Vector2(120, 80), 
//			new Vector2(690, 80),
//			new Vector2(690, 270),
//			new Vector2(470, 270),
//			new Vector2(470, 460),
//			new Vector2(340, 460),
//			new Vector2(340, 680),
//			new Vector2(120, 680)
//		);
		path = new PathList<Vector2>(
				new Vector2[]{new Vector2(120, 400), new Vector2(120, 80), new Vector2(690, 80)},
				new Vector2[]{new Vector2(690, 80), new Vector2(470, 270)},
				new Vector2[]{new Vector2(470, 270), new Vector2(340, 460), new Vector2(340, 680)},
				new Vector2[]{new Vector2(340, 680), new Vector2(120, 400)}
			);
		cursor = new PathListCursor<Vector2>(path, speed, CursorMode.LOOP_PINGPONG);
		Gdx.input.setInputProcessor(this);
	}

	final Vector2 computedPosition = new Vector2();

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
			cursor.move(Gdx.graphics.getDeltaTime());
			path.valueAt(computedPosition, cursor);
			obj.setCenter(computedPosition.x, computedPosition.y);
		}
		
		// Dessin de la courbe à suivre
		renderer.begin(spriteBatch.getProjectionMatrix(), GL20.GL_LINE_STRIP);
		for (Path<Vector2> segment : path) {
			float val = 0f;
			while (val <= 1f) {
				renderer.color(0f, 0f, 0f, 1f);
				segment.valueAt(/* out: */computedPosition, val);
				renderer.vertex(computedPosition.x, computedPosition.y, 0);
				val += SAMPLE_POINT_DISTANCE;
			}
		}
		renderer.end();
		
		// Dessin de l'image
		spriteBatch.begin();
		obj.draw(spriteBatch);
		spriteBatch.end();
	}

	@Override
	public String getName() {
		return "PATHTEST";
	}
}