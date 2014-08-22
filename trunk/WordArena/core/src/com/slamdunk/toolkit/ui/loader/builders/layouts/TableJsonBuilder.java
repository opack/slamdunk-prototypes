package com.slamdunk.toolkit.ui.loader.builders.layouts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.JsonValue;
import com.slamdunk.toolkit.ui.loader.JsonUIBuilder;

public class TableJsonBuilder extends LayoutJsonBuilder {
	
	public TableJsonBuilder(JsonUIBuilder creator) {
		super(creator);
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
			JsonValue allColumnDefaults = getJsonProperty("columnDefaults");
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
			parseCellProperties(getJsonProperty("defaults"), defaultsCell);
		}
	}

	private void parseDebug(Table table) {
		if (hasProperty("debug")) {
			table.setDebug(getBooleanProperty("debug"));
		}
	}

	private void parseFillParent(Table table) {
		if (hasProperty("fill-parent")) {
			table.setFillParent(getBooleanProperty("fill-parent"));
		}
	}

	private void parseRows(Table table) {
		if (hasProperty("rows")) {
			JsonValue rows = getJsonProperty("rows");
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
		Actor widget = getCreator().build(jsonCell.get("widget"));
		
		// Crée la cellule et lui applique les propriétés voulues
		Cell<Actor> cell = table.add(widget);
		parseCellProperties(jsonCell, cell);
	}
	
	private void parseCellProperties(JsonValue jsonCell, Cell<?> cell) {
		parseCellWidth(jsonCell, cell);
		parseCellColspan(jsonCell, cell);
		parseCellExpand(jsonCell, cell);
		parseCellExpandXY(jsonCell, cell);
		parseCellFill(jsonCell, cell);
		parseCellFillXY(jsonCell, cell);
		parseCellAlign(jsonCell, cell);
		parseCellPad(jsonCell, cell);
		parseCellPadTLBR(jsonCell, cell);
	}

	private void parseCellPadTLBR(JsonValue jsonCell, Cell<?> cell) {
		if (jsonCell.has("pad-tlbr")) {
			JsonValue padValues = jsonCell.get("pad-tlbr");
			cell.pad(padValues.getFloat(0), padValues.getFloat(1), padValues.getFloat(2), padValues.getFloat(3));
		}
	}

	private void parseCellPad(JsonValue jsonCell, Cell<?> cell) {
		if (jsonCell.has("pad")) {
			cell.pad(jsonCell.getFloat("pad"));
		}
	}

	private void parseCellAlign(JsonValue jsonCell, Cell<?> cell) {
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
	}

	/**
	 * Prend un tableau de 2 entrées. Chaque valeur peut être
	 * float ou boolean.
	 * Si float, indique le pourcentage (0.0-1.0) de la
	 * largeur/hauteur à occuper.
	 * Si boolean, true représente 1.0 et false représente 0.0.
	 */
	private void parseCellFillXY(JsonValue jsonCell, Cell<?> cell) {
		if (jsonCell.has("fill-xy")) {
			JsonValue fillValues = jsonCell.get("fill-xy");
			
			float fillX = parseFillFromJson(fillValues.get(0));
			float fillY = parseFillFromJson(fillValues.get(1));
			cell.fill(fillX, fillY);
		}
	}
	
	/**
	 * La valeur peut être float ou boolean.
	 * Si float, indique le pourcentage (0.0-1.0) de la
	 * largeur/hauteur à occuper.
	 * Si boolean, true représente 1.0 et false représente 0.0.
	 * @return
	 */
	private float parseFillFromJson(JsonValue jsonValue) {
		float fill = 0;
		if (jsonValue.isNumber()) {
			fill = jsonValue.asFloat();
		} else if (jsonValue.isBoolean() && jsonValue.asBoolean()) {
			fill = 1;
		}
		return fill;
	}

	private void parseCellFill(JsonValue jsonCell, Cell<?> cell) {
		if (jsonCell.has("fill")) {
			cell.fill();
		}
	}

	private void parseCellExpandXY(JsonValue jsonCell, Cell<?> cell) {
		if (jsonCell.has("expand-xy")) {
			JsonValue expandValues = jsonCell.get("expand-xy");
			cell.expand(expandValues.getBoolean(0), expandValues.getBoolean(1));
		}
	}

	private void parseCellExpand(JsonValue jsonCell, Cell<?> cell) {
		if (jsonCell.has("expand")) {
			cell.expand();
		}
	}

	private void parseCellColspan(JsonValue jsonCell, Cell<?> cell) {
		if (jsonCell.has("colspan")) {
			cell.colspan(jsonCell.getInt("colspan"));
		}
	}

	private void parseCellWidth(JsonValue jsonCell, Cell<?> cell) {
		if (jsonCell.has("width")) {
			cell.width(jsonCell.getFloat("width"));
		}
	}
}
