package com.slamdunk.wordarena.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.slamdunk.wordarena.components.BoundsComponent;
import com.slamdunk.wordarena.components.CameraComponent;
import com.slamdunk.wordarena.components.LetterCellComponent;
import com.slamdunk.wordarena.components.TextureComponent;
import com.slamdunk.wordarena.components.TransformComponent;

public class ComponentMappers {
	public static final ComponentMapper<TransformComponent> TRANSFORM = ComponentMapper.getFor(TransformComponent.class);
	public static final ComponentMapper<BoundsComponent> BOUNDS = ComponentMapper.getFor(BoundsComponent.class);
	public static final ComponentMapper<CameraComponent> CAMERA = ComponentMapper.getFor(CameraComponent.class);
	public static final ComponentMapper<LetterCellComponent> LETTER_CELL = ComponentMapper.getFor(LetterCellComponent.class);
	public static final ComponentMapper<TextureComponent> TEXTURE = ComponentMapper.getFor(TextureComponent.class);
}
