package myprefix;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import myprefix.database.DataBase;
import myprefix.listener.EventListener;

public class Main extends PluginBase {
	private DataBase db;
	private EventListener listener;
	
	@Override
	public void onEnable() {
		db = new DataBase(this);
		listener = new EventListener(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return listener.onCommand(sender, command, label, args);
	}
	
	public DataBase getDB() {
		return db;
	}
}
