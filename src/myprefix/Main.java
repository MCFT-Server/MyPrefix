package myprefix;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import myprefix.database.DataBase;
import myprefix.listener.EventListener;
import myprefix.prefix.PrefixManager;

public class Main extends PluginBase {
	private DataBase db;
	private EventListener listener;
	private PrefixManager manager;
	
	@Override
	public void onEnable() {
		db = new DataBase(this);
		listener = new EventListener(this);
		manager = new PrefixManager(this);
	}
	
	@Override
	public void onDisable() {
		getDB().save();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return listener.onCommand(sender, command, label, args);
	}
	
	public DataBase getDB() {
		return db;
	}
	
	public PrefixManager getManager() {
		return manager;
	}
}
