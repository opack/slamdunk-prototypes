package com.slamdunk.wordarena.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.slamdunk.wordarena.ecs.components.ColliderComponent;
import com.slamdunk.wordarena.ecs.components.TransformComponent;

public class ColliderSystem extends IteratingSystem {
	
	@SuppressWarnings("unchecked")
	public ColliderSystem() {
		super(Family.all(ColliderComponent.class, TransformComponent.class).get());
	}

	@Override
	public void processEntity(Entity entity, float deltaTime) {
		TransformComponent transform = ComponentMappers.TRANSFORM.get(entity);
		ColliderComponent collider = ComponentMappers.COLLIDER.get(entity);
		
		// Place la zone de collision par rapport à l'objet
		collider.bounds.x = transform.pos.x + collider.relativeOrigin.x;
		collider.bounds.y = transform.pos.y + collider.relativeOrigin.x;
	}
}
