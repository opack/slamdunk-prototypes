package com.slamdunk.toolkit.ui.loader.builders;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.ui.loader.JsonUIBuilder;

public class TableJsonBuilder extends JsonComponentBuilder {
	protected JsonUIBuilder creator;
	
	public TableJsonBuilder(JsonUIBuilder creator) {
		this.creator = creator;
	}

	@Override
	protected Table createEmpty(Skin skin, String style) {
		return new Table(skin);
	}
	
	@Override
	public Actor build(Skin skin) {
		// Gère les propriétés basiques du widget
		Table table = (Table)super.build(skin);
		
		// Gère les propriétés par défaut de la table
		parseDefaults(table);
		
		// Gère les propriétés par défaut des colonnes
		parseColumnDefaults(table);
		
		// Gère la propriété fillParent
		parseFillParent(table);
		
		// Gère la propriété rows
		parseRows(table);

		// Gère la propriété debug
		parseDebug(table);
		
		table.pack();
		return table;
	}
	
	private void parseColumnDefaults(Table table) {
		if (hasProperty("columnDefaults")) {
			JsonValue allColumnDefaults = actorDescription.get("columnDefaults");
			JsonValue columnDefaults;
			Cell<?> defaultsCell;
			for (int curDef = 0; curDef < allColumnDefaults.size; curDef++) {
				columnDefaults = allColumnDefaults.get(curDef);
				defaultsCell = table.columnDefaults(Integer.parseInt(columnDefaults.name()));
				parseCellProperties(columnDefaults, defaultsCell);
			}
		}
	}
	
	private void parseDefaults(Table table) {
		if (hasProperty("defaults")) {
			Cell<?> defaultsCell = table.defaults();
			parseCellProperties(actorDescription.get("defaults"), defaultsCell);
		}
	}

	private void parseDebug(Table table) {
		if (hasProperty("debug")) {
			table.setDebug(actorDescription.getBoolean("debug"));
		}
	}

	private void parseFillParent(Table table) {
		if (hasProperty("fill-parent")) {
			table.setFillParent(actorDescription.getBoolean("fill-parent"));
		}
	}

	private void parseRows(Table table) {
		if (hasProperty("rows")) {
			JsonValue rows = actorDescription.get("rows");
			JsonValue row;
			// Parcours toutes les lignes...
			for (int curRow = 0; curRow < rows.size; curRow++) {
				row = rows.get(curRow);
				parseRow(row, table);
			}
		}
	}

	private void parseRow(JsonValue row, Table table) {
		// Applique les propriétés sur la ligne
		Cell<?> rowCell = table.row();
		parseCellProperties(row, rowCell);
		
		// Ajoute les cellules
		if (row.has("cells")) {
			JsonValue cells = row.get("cells");
			JsonValue cell;
			for (int curCell = 0; curCell < cells.size; curCell++) {
				cell = cells.get(curCell);
				parseCell(cell, table);
			}
		}
	}

	private void parseCell(JsonValue jsonCell, Table table) {
		// Crée le widget qui ira dans la cellule
		Actor widget = creator.build(jsonCell.get("widget"));
		
		// Crée la cellule et lui applique les propriétés voulues
		Cell<Actor> cell = table.add(widget);
		parseCellProperties(jsonCell, cell);
	}
	
	private void parseCellProperties(JsonValue jsonCell, Cell<?> cell) {
		if (jsonCell.has("width")) {
			cell.width(jsonCell.getFloat("width"));
		}
		if (jsonCell.has("colspan")) {
			cell.colspan(jsonCell.getInt("colspan"));
		}
		if (jsonCell.has("expand")) {
			cell.expand();
		}
		if (jsonCell.has("expand-xy")) {
			JsonValue expandValues = jsonCell.get("expand-xy");
			cell.expand(expandValues.getBoolean(0), expandValues.getBoolean(1));
		}
		if (jsonCell.has("fill")) {
			cell.fill();
		}
		if (jsonCell.has("fill-xy")) {
			JsonValue fillValues = jsonCell.get("fill-xy");
			cell.fill(fillValues.getBoolean(0), fillValues.getBoolean(1));
		}
		if (jsonCell.has("align")) {
			String align = jsonCell.getString("align");
			if ("top".equals(align)) {
				cell.top();
			} else if ("bottom".equals(align)) {
				cell.bottom();
			} else if ("left".equals(align)) {
				cell.left();
			} else if ("right".equals(align)) {
				cell.right();
			}
			
		}
		if (jsonCell.has("pad")) {
			cell.pad(jsonCell.getFloat("pad"));
		}
		if (jsonCell.has("pad-tlbr")) {
			JsonValue padValues = jsonCell.get("pad-tlbr");
			cell.pad(padValues.getFloat(0), padValues.getFloat(1), padValues.getFloat(2), padValues.getFloat(3));
		}
	}
}
