package me.eighth.suitcase;

import java.util.ArrayList;

import me.eighth.suitcase.config.SuitcaseConfig;
import me.eighth.suitcase.config.SuitcaseEvent;
import me.eighth.suitcase.config.SuitcaseMessage;
import me.eighth.suitcase.event.SuitcaseCommandExecutor;
import me.eighth.suitcase.event.SuitcasePlayerListener;
import me.eighth.suitcase.log.SuitcaseConnector;
import me.eighth.suitcase.log.SuitcaseDatabase;
import me.eighth.suitcase.log.SuitcaseYMLFile;
import me.eighth.suitcase.util.SuitcaseConsole;
import me.eighth.suitcase.util.SuitcaseFile;
import me.eighth.suitcase.util.SuitcasePermission;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Suitcase extends JavaPlugin {
	
	/** Current version name */
	public final String name = "Leather";
	
	/** Prefix for console notifications */
	public final String pluginTag = "[Suitcase] ";
	
	/** Prefix for player commands */
	public final String playerTag = "[PLAYER_COMMAND] ";
	
	/** Prefix for console commands */
	public final String consoleTag = "[CONSOLE_COMMAND] ";
	
	// Suitcase classes
	
	public final SuitcaseConfig cfg = new SuitcaseConfig(this);
	public final SuitcaseEvent event = new SuitcaseEvent(this);
	public final SuitcaseMessage msg = new SuitcaseMessage(this);
	
	public final SuitcaseCommandExecutor command = new SuitcaseCommandExecutor(this);
	public final SuitcasePlayerListener player = new SuitcasePlayerListener(this);
	
	public final SuitcaseConnector con = new SuitcaseConnector(this);
	public final SuitcaseDatabase db = new SuitcaseDatabase(this);
	public final SuitcaseYMLFile yml = new SuitcaseYMLFile(this);
	
	public final SuitcaseConsole console = new SuitcaseConsole(this);
	public final SuitcaseFile file = new SuitcaseFile(this);
	public final SuitcasePermission perm = new SuitcasePermission(this);

	@Override
	public void onEnable() {
		// set command executors and event listeners
		getCommand("suitcase").setExecutor(command);
		
		// register event
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvent(Event.Type.PLAYER_JOIN, player, Event.Priority.Lowest, this);
		manager.registerEvent(Event.Type.PLAYER_INTERACT, player, Event.Priority.Lowest, this);
		
		// load and check configuration
		if (!cfg.init() || !msg.init() || !event.init() || !con.init()) {
			disable();
			return;
		}
		
		// add online players
		con.registerAll();
		
		// enabling finished, send to log
		console.log(Action.PLUGIN_ENABLE);
	}
	
	@Override
	public void onDisable() {
		// save and dispose configuration in reverse order, ignore errors and continue disabling
		con.free();
		event.free();
		msg.free();
		cfg.free();
		
		// disabling finished, send to log
		console.log(Action.PLUGIN_DISABLE);
	}
	
	/**
	 * Sends a debug message to console
	 * @param arguments Debug information
	 */
	public void debug(String...arguments) {
		console.log(Action.DEBUG, arguments);
	}
	
	/**
	 * Gets string from ArrayList and removes brackets
	 * @param list An ArrayList of Strings to be converted to a single String
	 * @param separator Set an item separator
	 */
	public static String getStringFromList(ArrayList<String> list, String separator) {
		return list.toString().replaceAll("^\\[|\\]$", "").replaceAll(", ", separator + " ");
	}
	
	/** Reloads plugin */
	public boolean reload() {
		// reload settings and connector
		if (cfg.reload() && msg.reload() && event.reload() && con.reload()) {
			// reloading finished, send to log
			console.log(Action.PLUGIN_RELOAD);
			return true;
		}
		else {
			return false;
		}
		
	}
	
	/** Resets configuration and player data */
	public boolean reset() {
		// reset player ratings
		if (cfg.reset() && msg.reset() && event.reset() && con.reset()) {
			console.log(Action.RESET);
			return true;
		}
		else {
			return false;
		}
	}
	
	/** Disables plugin due to an internal error */
	public void disable() {
		setEnabled(false);
	}
}
