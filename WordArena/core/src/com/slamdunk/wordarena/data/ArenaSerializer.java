package com.slamdunk.wordarena.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.wordarena.enums.CellStates;
import com.slamdunk.wordarena.screens.editor.EditorScreen;

public class ArenaSerializer implements Json.Serializer<ArenaData>{
	private EditorScreen editorScreen;
	
	public void setEditorScreen(EditorScreen editorScreen) {
		this.editorScreen = editorScreen;
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
				// Si la cellule n'est pas possédée (donc si elle est juste contrôlée)
				// alors on l'attribue au joueur neutre de façon à ce que dans le plan
				// ce contrôle (qui est déterminé dynamiquement) ne soit pas vu comme
				// une possession.
				if (arena.cells[x][y].getData().state == CellStates.OWNED) {
					sb.append(arena.cells[x][y].getData().owner.uid).append(" ");
				} else {
					sb.append(Player.NEUTRAL.uid).append(" ");
				}
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
	public ArenaData read(Json json, JsonValue plan, Class type) {
		ArenaBuilder builder = new ArenaBuilder(null);
		builder.setEditorScreen(editorScreen);
		builder.load(plan);
		return builder.build();
	}
}
