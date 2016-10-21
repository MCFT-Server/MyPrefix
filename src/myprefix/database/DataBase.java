package myprefix.database;

import java.io.File;

import cn.nukkit.utils.Config;
import myprefix.Main;

public class DataBase extends BaseDB<Main> {

	public DataBase(Main plugin) {
		super(plugin);
		
		setPrefix("[칭호]");
		initDB("prefixlist", new File(plugin.getDataFolder(), "prefixlist.json"), Config.JSON);
		initDB("prefix", new File(plugin.getDataFolder(), "prefix.json"), Config.JSON);
		initConfig();
	}
	
	private void initConfig() {
		saveDefaultConfig();
		if (getConfig().get("nametag-format") == null) {
			saveDefaultConfig();
			reloadConfig();
		}
	}
}
