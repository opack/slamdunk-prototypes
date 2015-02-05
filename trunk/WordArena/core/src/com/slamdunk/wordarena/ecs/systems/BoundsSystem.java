package com.slamdunk.wordarena.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.slamdunk.wordarena.ecs.components.BoundsComponent;
import com.slamdunk.wordarena.ecs.components.TransformComponent;

public class BoundsSystem extends IteratingSystem {
	
	@SuppressWarnings("unchecked")
	public BoundsSystem() {
		super(Family.all(BoundsComponent.class, TransformComponent.class).get());
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		TransformComponent transform = ComponentMappers.TRANSFORM.get(entity);
		BoundsComponent bounds = ComponentMappers.BOUNDS.get(entity);
		
		bounds.bounds.x = transform.pos.x;
		bounds.bounds.y = transform.pos.y;
	}
}
