package com.slamdunk.pixelkingdomadvanced.units;

import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.slamdunk.pixelkingdomadvanced.ai.States;
import com.slamdunk.toolkit.lang.KeyListMap;

/**
 * Gestionnaire des unités créées sur le terrain
 */
public class UnitManager {
	/**
	 * Unités par faction
	 */
	private KeyListMap<Factions, SimpleUnit> units;
	
	/**
	 * Group dans lequel sont ajoutées et retirées les unités
	 */
	private Group stageContainer;
	
	private static UnitManager instance;
	
	private UnitManager() {
		units = new KeyListMap<Factions, SimpleUnit>();
	}
	
	public static UnitManager getInstance() {
		if (instance == null) {
			instance = new UnitManager();
		}
		return instance;
	}
	
	public void setStageContainer(Group container) {
		this.stageContainer = container;
	}

	public void addUnit(SimpleUnit unit) {
		stageContainer.addActor(unit);
		units.putValue(unit.getFaction(), unit);
	}
	
	/**
	 * Retire l'unité du monde
	 * @param unit
	 */
	public void removeUnit(SimpleUnit unit) {
		stageContainer.removeActor(unit);
		List<SimpleUnit> unitsOfFaction = units.get(unit.getFaction());
		if (unitsOfFaction != null) {
			unitsOfFaction.remove(unit);
		}
	}
	
	/**
	 * Retourne la liste des unités du camp indiqué
	 * @return
	 */
	public Collection<SimpleUnit> getUnits(Factions faction) {
		return units.get(faction);
	}

	public void stopAllUnits() {
		for (List<SimpleUnit> factionUnits : units.values()) {
			for (SimpleUnit unit : factionUnits) {
				unit.setState(States.IDLE);
			}
		}
	}

	public void removeAllUnits() {
		units.clear();
	}
}
