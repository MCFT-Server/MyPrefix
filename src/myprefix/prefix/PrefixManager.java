package myprefix.prefix;

import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import myprefix.Main;
import myprefix.database.DataBase;

public class PrefixManager {
	private Main plugin;
	public static PrefixManager instance;

	public PrefixManager(Main plugin) {
		this.plugin = plugin;
		if (instance == null)
			instance = this;
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
		return getDB().getConfig().getString("default-prefix", "서버원");
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
		List<String> list = getPrefixListConfig().getStringList(player.toLowerCase());
		list.remove(index);
		getPrefixListConfig().set(player.toLowerCase(), list);
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
	public String setPrefix(String player, int index) {
		String prefix = getPrefixList(player.toLowerCase()).get(index);
		getPrefixConfig().set(player.toLowerCase(), prefix);
		Player onPlayer = plugin.getServer().getPlayer(player);
		if (onPlayer != null) {
			onPlayer.setNameTag(getDB().getConfig().getString("nametag-format").replace("%player", onPlayer.getName())
					.replace("%prefix", getPrefix(onPlayer.getName())));
		}
		return prefix;
	}

	/**
	 * 현재 플레이어가 장착한 칭호를 가져옵니다.
	 * 
	 * @param player
	 * @return prefix
	 */
	public String getPrefix(String player) {
		return getPrefixConfig().getString(player.toLowerCase(), getDefaultPrefix());
	}
	
	/**
	 * 플레이어의 해당 인덱스의 칭호를 가져옵니다.
	 * 
	 * @param player
	 * @param index
	 * 
	 * 
	 * @return prefix
	 */
	public String getPrefix(String player, int index) {
		return getPrefixList(player).get(index);
	}
	
	/**
	 * 플레이어 보유 칭호 목록중 해당 칭호가 위치한 인덱스를 얻습니다.
	 * 
	 * @param player
	 * @param prefix
	 * @return index. if player doesn't have that prefix, it returns -1
	 */
	public int getIndex(String player, String prefix) {
		List<String> list = getPrefixList(player);
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(prefix)) return i;
		}
		return -1;
	}
}
