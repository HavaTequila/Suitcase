package me.eighth.suitcase;

import me.eighth.suitcase.config.SuitcaseConfig;
import me.eighth.suitcase.config.SuitcaseEvent;
import me.eighth.suitcase.config.SuitcaseMessage;
import me.eighth.suitcase.event.SuitcaseCommandExecutor;
import me.eighth.suitcase.event.SuitcasePlayerListener;
import me.eighth.suitcase.log.SuitcaseConnector;
import me.eighth.suitcase.log.SuitcaseConsole;
import me.eighth.suitcase.log.SuitcaseDatabase;
import me.eighth.suitcase.log.SuitcaseYMLFile;
import me.eighth.suitcase.log.SuitcaseConsole.actionType;
import me.eighth.suitcase.util.SuitcaseFile;
import me.eighth.suitcase.util.SuitcasePermission;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Suitcase extends JavaPlugin {
	
	public final String name = "Leather";
	public final String tag = "[Suitcase] ";
	public final String cmdTag = "[PLAYER_COMMAND] ";
	public final SuitcaseConfig cfg = new SuitcaseConfig(this);
	public final SuitcaseEvent event = new SuitcaseEvent(this);
	public final SuitcaseMessage msg = new SuitcaseMessage(this);
	private final SuitcaseCommandExecutor command = new SuitcaseCommandExecutor(this);
	private final SuitcasePlayerListener player = new SuitcasePlayerListener(this);
	public final SuitcaseConnector con = new SuitcaseConnector(this);
	public final SuitcaseConsole console = new SuitcaseConsole(this);
	public final SuitcaseDatabase db = new SuitcaseDatabase(this);
	public final SuitcaseYMLFile yml = new SuitcaseYMLFile(this);
	public final SuitcaseFile file = new SuitcaseFile(this);
	public final SuitcasePermission perm = new SuitcasePermission(this);

	@Override
	public void onEnable() {
		// plugin startup
		con.log(actionType.PLUGIN_ENABLE_START);
		
		// set command executors and event listeners
		getCommand("suitcase").setExecutor(command);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_PRELOGIN, player, Event.Priority.Low, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, player, Event.Priority.Low, this);
		
		
		// load and check configuration
		if (!cfg.init()) {
			disable();
			return;
		}
		else if (!msg.init()) {
			disable();
			return;
		}
		else if (!event.init()) {
			disable();
			return;
		}
		else if (!con.init()) {
			disable();
			return;
		}
		
		// add online players
		for (Player player : getServer().getOnlinePlayers()) {
			con.setRating("CONSOLE", player.getName(), cfg.data.getInt("mechanics.rating.default"));
		}
		
		// enabling finished, send to log
		con.log(actionType.PLUGIN_ENABLE_FINISH);
	}
	
	@Override
	public void onDisable() {
		// plugin unload
		con.log(actionType.PLUGIN_DISABLE_START);
		
		// save and dispose configuration
		if (!cfg.free()) {
			return;
		}
		else if (!msg.free()) {
			return;
		}
		else if (!event.free()) {
			return;
		}
		else if (!con.free()) {
			return;
		}
		
		// disabling finished, send to log
		con.log(actionType.PLUGIN_DISABLE_FINISH);
	}
	
	public void reload() {
		// plugin reload
		con.log(actionType.PLUGIN_RELOAD_START);
		
		// reload configuration
		if (!cfg.reload()) {
			disable();
			return;
		}
		else if (!msg.reload()) {
			disable();
			return;
		}
		else if (!event.reload()) {
			disable();
			return;
		}
		else if (!con.reload()) {
			disable();
			return;
		}
		
		// reloading finished, send to log
		con.log(actionType.PLUGIN_RELOAD_FINISH);
	}
	
	// disable plugin due to an internal error
	public void disable() {
		setEnabled(false);
	}

}
