package com.slamdunk.wordarena.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.slamdunk.wordarena.ecs.components.BoundsComponent;
import com.slamdunk.wordarena.ecs.components.CameraComponent;
import com.slamdunk.wordarena.ecs.components.ColliderComponent;
import com.slamdunk.wordarena.ecs.components.LetterCellComponent;
import com.slamdunk.wordarena.ecs.components.ShapeComponent;
import com.slamdunk.wordarena.ecs.components.TextureComponent;
import com.slamdunk.wordarena.ecs.components.TransformComponent;
import com.slamdunk.wordarena.ecs.components.ZoneComponent;

public class ComponentMappers {
	public static final ComponentMapper<TransformComponent> TRANSFORM = ComponentMapper.getFor(TransformComponent.class);
	public static final ComponentMapper<BoundsComponent> BOUNDS = ComponentMapper.getFor(BoundsComponent.class);
	public static final ComponentMapper<ColliderComponent> COLLIDER = ComponentMapper.getFor(ColliderComponent.class);
	public static final ComponentMapper<CameraComponent> CAMERA = ComponentMapper.getFor(CameraComponent.class);
	public static final ComponentMapper<LetterCellComponent> LETTER_CELL = ComponentMapper.getFor(LetterCellComponent.class);
	public static final ComponentMapper<TextureComponent> TEXTURE = ComponentMapper.getFor(TextureComponent.class);
	public static final ComponentMapper<ZoneComponent> ZONE = ComponentMapper.getFor(ZoneComponent.class);
	public static final ComponentMapper<ShapeComponent> SHAPE = ComponentMapper.getFor(ShapeComponent.class);
}
