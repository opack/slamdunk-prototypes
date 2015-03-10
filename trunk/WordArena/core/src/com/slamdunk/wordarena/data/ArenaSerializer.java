package com.slamdunk.wordarena.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class ArenaSerializer implements Json.Serializer<ArenaData>{

	@Override
	public void write(Json json, ArenaData arena, Class knownType) {
		StringBuilder sb = new StringBuilder();
		
		json.writeObjectStart();
		json.writeValue("name", arena.name);
		json.writeValue("width", arena.width);
		json.writeValue("height", arena.height);
		json.writeArrayStart("plan.types");
		for (int y = 0; y < arena.height; y++) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].getData().type).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		json.writeArrayStart("plan.letters");
		for (int y = 0; y < arena.height; y++) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].getData().planLetter).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		json.writeArrayStart("plan.powers");
		for (int y = 0; y < arena.height; y++) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].getData().power).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		json.writeArrayStart("plan.owners");
		for (int y = 0; y < arena.height; y++) {
			sb.setLength(0);
			for (int x = 0; x < arena.width; x++) {
				sb.append(arena.cells[x][y].getData().owner.uid).append(" ");
			}
			json.writeValue(sb.toString());
		}
		json.writeArrayEnd();
		
		json.writeArrayStart("plan.zone");
		for (int y = 0; y < arena.height; y++) {
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
		ArenaData data = new ArenaData();
		
		return data;
	}
}
