package me.eighth.suitcase.log;

import me.eighth.suitcase.Suitcase;

public class SuitcaseDatabase {
	
	// private Suitcase plugin;
	
	public SuitcaseDatabase(Suitcase plugin) {
		// this.plugin = plugin;
	}

	protected boolean init() {
		
		return true;
	}

	protected boolean free() {
		
		return true;
	}

	protected boolean reload() {
		if (free() && init()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	// TODO: Hibernate database interface for MySQL, h2 and some others
}
