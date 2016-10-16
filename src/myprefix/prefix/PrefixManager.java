package myprefix.prefix;

import java.util.List;

import cn.nukkit.utils.Config;
import myprefix.Main;
import myprefix.database.DataBase;

public class PrefixManager {
	private Main plugin;
	public static PrefixManager instance;
	
	public PrefixManager(Main plugin) {
		this.plugin = plugin;
		if (instance == null) instance = this;
	}
	
	public static PrefixManager getInstance() {
		return instance;
	}
	
	private DataBase getDB() {
		return plugin.getDB();
	}
	
	private Config getPrefixListConfig() {
		return getDB().getDB("prefixlist");
	}
	
	private Config getPrefixConfig() {
		return getDB().getDB("prefix");
	}
	
	public String getDefaultPrefix() {
		return getDB().getConfig().getString("default-prefix");
	}
	
	public void addPrefix(String player, String prefix) {
		List<String> list = getPrefixListConfig().getStringList(player.toLowerCase());
		list.add(prefix);
		getPrefixListConfig().set(player.toLowerCase(), list);
	}
	
	/**
	 * 
	 * @param player
	 * @param index
	 * 
	 * @return removed prefix
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public String removePrefix(String player, int index) {
		String prefix = getPrefixList(player).get(index);
		if (getPrefix(player).equals(prefix)) {
			getPrefixConfig().remove(player.toLowerCase());
		}
		getPrefixListConfig().getStringList(player.toLowerCase()).remove(index);
		return prefix;
	}
	
	public List<String> getPrefixList(String player) {
		return getPrefixListConfig().getStringList(player.toLowerCase());
	}
	
	/**
	 * 
	 * @param player
	 * @param index
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public void setPrefix(String player, int index) {
		getPrefixConfig().set(player.toLowerCase(), getPrefixList(player.toLowerCase()).get(index));
	}
	
	public String getPrefix(String player) {
		String prefix = getPrefixConfig().getString(player.toLowerCase());
		return prefix.equals("") ? getDefaultPrefix() : prefix;
	}
}
