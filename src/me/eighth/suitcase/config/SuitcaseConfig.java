package me.eighth.suitcase.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.actionType;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SuitcaseConfig {
	
	// define variables
	private FileConfiguration cfConfig;
	private File cfFile;
	
	// get config file
	public boolean initConfig() {
		
		// load default values
		loadConfig();
		
		// read file
		cfFile = new File(Suitcase.plugin.getDataFolder(), "config.yml");
		if (cfFile.exists()) {
			cfConfig = YamlConfiguration.loadConfiguration(cfFile);
			// add property if missing
			for (String path : Suitcase.configKeys.getKeys(true)) {
				if (!cfConfig.contains(path)) {
					// set missing property and log to console
					cfConfig.set(path, Suitcase.configKeys.get(path));
					Suitcase.utConsole.sendAction(actionType.PROPERTY_MISSING, (ArrayList<String>) Arrays.asList(path, "config.yml", Suitcase.configKeys.getString(path)));
				}
				// compare object types
				// TODO: check if this is working, should always return true. Object == Object
				else if (cfConfig.get(path).getClass() != Suitcase.configKeys.get(path).getClass()) {
					// reset value and log to console
					cfConfig.set(path, Suitcase.configKeys.get(path));
					Suitcase.utConsole.sendAction(actionType.PROPERTY_BAD_TYPE, (ArrayList<String>) Arrays.asList(path, "config.yml", cfConfig.get(path).getClass().toString(), Suitcase.configKeys.get(path).getClass().toString()));
				}
			}
			// save and use verified configKeys
			try {
				saveConfig();
				Suitcase.configKeys = cfConfig;
				return true;
			} catch (IOException e) {
				Suitcase.utConsole.sendAction(actionType.FILE_SAVE_ERROR, (ArrayList<String>) Arrays.asList("config.yml", e.toString()));
				return false;
			}
		}
		else {
			Suitcase.utConsole.sendAction(actionType.FILE_NOT_FOUND, (ArrayList<String>) Arrays.asList("config.yml"));
			try {
				cfFile.createNewFile();
				cfConfig = Suitcase.configKeys;
				saveConfig();
				return true;
			} catch (IOException e) {
				Suitcase.utConsole.sendAction(actionType.FILE_SAVE_ERROR, (ArrayList<String>) Arrays.asList("config.yml", e.toString()));
				return false;
			}
		}
	}
	
	public boolean freeConfig() {
		cfConfig = null;
		cfFile = null;
		Suitcase.messagesKeys = null;
		return true;
	}
	
	public boolean reloadConfig() {
		if (freeConfig() && initConfig()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	// save config to file
	private void saveConfig() throws IOException {
		cfConfig.save(cfFile);
	}
	
	private void loadConfig() {
		Suitcase.configKeys.set("mechanics.language", "en");
		Suitcase.configKeys.set("mechanics.full-help", false);
		Suitcase.configKeys.set("mechanics.op-permissions", true);
		Suitcase.configKeys.set("mechanics.rating.enable", true);
		Suitcase.configKeys.set("mechanics.rating.multiple-rating", false);
		Suitcase.configKeys.set("mechanics.rating.interval", "3d");
		Suitcase.configKeys.set("mechanics.rating.minimum", 0);
		Suitcase.configKeys.set("mechanics.rating.maximum", 100);
		Suitcase.configKeys.set("mechanics.rating.default", 0);
		Suitcase.configKeys.set("mechanics.warning.enable", true);
		Suitcase.configKeys.set("mechanics.warning.maximum", 3);
		Suitcase.configKeys.set("log.rate", true);
		Suitcase.configKeys.set("log.warn", true);
		Suitcase.configKeys.set("log.system", true);
		Suitcase.configKeys.set("log.database.enable", true);
		Suitcase.configKeys.set("log.database.type", "MySQL");
		Suitcase.configKeys.set("log.database.database-name", "minecraft");
		Suitcase.configKeys.set("log.database.table", "suitcase");
		Suitcase.configKeys.set("log.database.username", "root");
		Suitcase.configKeys.set("log.database.password", "root");
		Suitcase.configKeys.set("log.file.enable", true);
		Suitcase.configKeys.set("log.file.max-players", 100);
		Suitcase.configKeys.set("stats.enable", false);
	}
}
