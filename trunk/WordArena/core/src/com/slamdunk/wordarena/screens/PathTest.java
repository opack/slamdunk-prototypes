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
	int SAMPLE_POINTS = 100;
	float SAMPLE_POINT_DISTANCE = 1f / SAMPLE_POINTS;
	
	SpriteBatch spriteBatch;
	ImmediateModeRenderer20 renderer;
	Sprite obj;
	Array<ComplexPath<Vector2>> paths;
	ComplexPathCursor<Vector2> cursor;
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
		paths = new Array<ComplexPath<Vector2>>();
//		paths.add(new ComplexPath<Vector2>(false,
//			new Vector2(120, 400),
//			new Vector2(120, 80), 
//			new Vector2(690, 80),
//			new Vector2(690, 270),
//			new Vector2(470, 270),
//			new Vector2(470, 460),
//			new Vector2(340, 460),
//			new Vector2(340, 680),
//			new Vector2(120, 680)
//		));
//		paths.add(new ComplexPath<Vector2>(
//			new Vector2[]{new Vector2(120, 400), new Vector2(120, 80), new Vector2(690, 80)},
//			new Vector2[]{new Vector2(690, 80), new Vector2(470, 270)},
//			new Vector2[]{new Vector2(470, 270), new Vector2(340, 460), new Vector2(340, 680)},
//			new Vector2[]{new Vector2(340, 680), new Vector2(120, 400)}
//		));
		
//		paths.add(new ComplexPath<Vector2>(
//			/**
//			 * m 152.85715,561.85715
//			 * 		c
//			 * 			0,0
//			 * 			17.14286,-114.28572
//			 * 			105.71428,-168.57143
//			 */
//			new Vector2[]{transform(152.85715f,561.85715f), transform(152.85715f,561.85715f), transform(170.00001f,447.57143f), transform(275.71429f,279f)},
//			/**
//			 * 			88.57143,-54.28572
//			 * 			190,-82.85714
//			 * 			190,-82.85714
//			 */
//			new Vector2[]{transform(275.71429f,279f), transform(364.28572f,224.71428f), transform(557.28572f,141.85714f), transform(744.28572f,59f)},
//			/**
//			 * 			0,0
//			 * 			57.14286,-22.85715
//			 * 			455.71429,-98.57144"
//			 */
//			new Vector2[]{transform(744.28572f,59f), transform(744.28572f,59f), transform(801.42858f,36.14285f), transform(1257.14287f,-62.42859f)}
//		));
		
		//M 158.57143,599.50504 C 191.32127,504.64831 264.52851,426.57881 354.60035,383.27495
//		paths.add(ComplexPath.createCubicBezierPathList(2,
//			158.57143f,599.50504f, 191.32127f,504.64831f, 264.52851f,426.57881f, 354.60035f,383.27495f,
//			545.32269f,297.00714f, 732.99567f,299.73707f, 914.28571f,252.36218f));
		
//		ComplexPath<Vector2> path = new ComplexPath<Vector2>();
//		path.add(new Bezier<Vector2>(new Vector2(152.85715f,768-561.85715f), new Vector2(194.17157f,768-443.63789f), new Vector2(258.57143f,768-393.28572f)));
//		path.add(new Bezier<Vector2>(new Vector2(258.57143f,768-393.28572f), new Vector2(313.00254f,768-350.72779f), new Vector2(382.66735f,768-331.17889f), new Vector2(448.57143f,768-310.42858f)));
//		path.add(new Bezier<Vector2>(new Vector2(448.57143f,768-310.42858f), new Vector2(497.48495f,768-295.02786f), new Vector2(505.71429f,768-287.57143f), new Vector2(904.28572f,768-211.85714f)));
//		paths.add(path);
		
		parse("battlefields/battlefield0.svg");
		cursor = new ComplexPathCursor<Vector2>(paths.get(0), speed, CursorMode.LOOP);
		
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
				ComplexPath<Vector2> path = new ComplexPath<Vector2>();
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
			cursor.move(Gdx.graphics.getDeltaTime());
			cursor.valueAt(computedPosition);
			obj.setCenter(computedPosition.x, computedPosition.y);
		}
		
		// Dessin des courbes
		for (ComplexPath<Vector2> path : paths) {
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
		ComplexPathCursor<Vector2> nearestCursor = null;
		for (int pathIndex = 0; pathIndex < paths.size; pathIndex++) {
			// Récupère le curseur du point le plus proche sur ce chemin
			ComplexPath<Vector2> path = paths.get(pathIndex);
			ComplexPathCursor<Vector2> probe = new ComplexPathCursor<Vector2>(path, cursor.getSpeed(), cursor.getMode());
			path.locate(tmpTouchPos, probe);
			
			// Regarde s'il est plus près que les autres curseurs
			path.valueAt(tmpNearestCoords, probe);
			float distance = tmpNearestCoords.dst(tmpTouchPos);
			if (nearestCursor == null
			|| distance < minDistance) {
				minDistance = distance;
				nearestCursor = probe;
			}
		}
		
		// Récupère la position correspondante
		cursor = nearestCursor;
		cursor.valueAt(tmpComputedPos);
		obj.setCenter(tmpComputedPos.x, tmpComputedPos.y);
		wait = 0.2f;
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