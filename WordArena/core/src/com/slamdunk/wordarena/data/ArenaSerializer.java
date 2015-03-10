package com.slamdunk.wordarena.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.wordarena.GameManager;

public class ArenaSerializer implements Json.Serializer<ArenaData>{
	private GameManager gameManager;
	
	public ArenaSerializer(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	@Override
	public void write(Json json, ArenaData arena, Class knownType) {
		StringBuilder sb = new StringBuilder();
		
		json.writeObjectStart();
		json.writeValue("name", arena.name);
		json.writeValue("width", arena.width);
		json.writeValue("height", arena.height);
		
		// Types de cellules
		json.writeArrayStart("plan.types");
		for (int y = arena.height - 1; y >= 0; y--) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].getData().type).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		// Lettres initiales
		json.writeArrayStart("plan.letters");
		for (int y = arena.height - 1; y >= 0; y--) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].getData().planLetter).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		// Puissances
		json.writeArrayStart("plan.powers");
		for (int y = arena.height - 1; y >= 0; y--) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].getData().power).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		// Possesseurs
		json.writeArrayStart("plan.owners");
		for (int y = arena.height - 1; y >= 0; y--) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].getData().owner.uid).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		// Zones
		json.writeArrayStart("plan.zones");
		for (int y = arena.height - 1; y >= 0; y--) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].getData().zone.getData().id).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		json.writeObjectEnd();
	}

	@Override
	public ArenaData read(Json json, JsonValue jsonData, Class type) {
		ArenaBuilderJson builder = new ArenaBuilderJson(gameManager);
		builder.load(jsonData);
		return builder.build();
	}
}
