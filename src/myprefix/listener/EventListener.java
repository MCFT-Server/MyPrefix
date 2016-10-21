package myprefix.listener;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import myprefix.Main;
import myprefix.database.DataBase;
import myprefix.prefix.PrefixManager;
import myprefix.utils.PageCreater;

public class EventListener implements Listener {
	private Main plugin;

	public EventListener(Main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		switch (command.getName().toLowerCase()) {
		case "칭호":
			if (sender instanceof ConsoleCommandSender) {
				sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.ingame"));
				break;
			}
			if (args.length < 1) {
				return false;
			}
			switch (args[0].toLowerCase()) {
			case "설정":
				if (!sender.hasPermission("myprefix.commands.prefix.set")) {
					sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
					break;
				}
				if (args.length < 2) {
					alert(sender, "/칭호 설정 <번호>");
					break;
				}
				try {
					int num = Integer.parseInt(args[1]);
					PrefixManager.getInstance().setPrefix(sender.getName(), num);
					message(sender, "칭호를 " + PrefixManager.getInstance().getPrefix(sender.getName())
							+ TextFormat.DARK_AQUA + "로 설정했습니다.");
				} catch (IndexOutOfBoundsException e) {
					alert(sender, "당신은 해당 번호의 칭호를 보유하고 있지 않습니다.");
				} catch (NumberFormatException e) {
					alert(sender, "번호는 정수만 입력 가능합니다.");
				}
				break;
			case "제거":
				if (!sender.hasPermission("myprefix.commands.prefix.remove")) {
					sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
					break;
				}
				if (args.length < 2) {
					alert(sender, "/칭호 제거 <번호>");
					break;
				}
				try {
					int num = Integer.parseInt(args[1]);
					String removed = PrefixManager.getInstance().removePrefix(sender.getName(), num);
					message(sender, removed + TextFormat.DARK_AQUA + " 칭호를 삭제했습니다.");
					break;
				} catch (IndexOutOfBoundsException e) {
					alert(sender, "당신은 해당 번호의 칭호를 보유하고 있지 않습니다.");
				} catch (NumberFormatException e) {
					alert(sender, "번호는 정수만 입력 가능합니다.");
				}
				break;
			case "목록":
				if (!sender.hasPermission("myprefix.commands.prefix.list")) {
					sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
					break;
				}
				if (args.length < 2) {
					alert(sender, "/칭호 목록 <페이지>");
					break;
				}
				try {
					int page = Integer.parseInt(args[1]);
					showPrefixList(sender, sender.getName(), page);
				} catch (NumberFormatException e) {
					alert(sender, "페이지는 정수만 입력 가능합니다.");
				}
				break;
			default:
				return false;
			}
			break;
		case "칭호관리":
			if (args.length < 1) {
				return false;
			}
			switch (args[0]) {
			case "추가":
				if (!sender.hasPermission("myprefix.commands.prefixmanage.add")) {
					sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
					break;
				}
				if (args.length < 3) {
					alert(sender, "/칭호관리 추가 <플레이어> <칭호>");
					break;
				}
				PrefixManager.getInstance().addPrefix(args[1], args[2]);
				message(sender, args[1] + "에게 [" + args[2] + TextFormat.DARK_AQUA + "]칭호를 추가했습니다.");
				break;
			case "제거":
				if (!sender.hasPermission("myprefix.commands.prefixmanage.remove")) {
					sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
					break;
				}
				if (args.length < 3) {
					alert(sender, "/칭호관리 제거 <플레이어> <번호>");
					break;
				}
				try {
					int index = Integer.parseInt(args[2]);
					String removed = PrefixManager.getInstance().removePrefix(args[1], index);
					message(sender, "칭호 [" + removed + TextFormat.DARK_AQUA + "]를 제거했습니다.");
				} catch (NumberFormatException e) {
					alert(sender, "번호는 정수만 입력 가능합니다.");
				}
				break;
			case "목록":
				if (!sender.hasPermission("myprefix.commands.prefixmanage.list")) {
					sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
					break;
				}
				if (args.length < 3) {
					alert(sender, "/칭호관리 목록 <플레이어> <페이지>");
					break;
				}
				try {
					int page = Integer.parseInt(args[2]);
					showPrefixList(sender, args[1], page);
				} catch (NumberFormatException e) {
					alert(sender, "페이지는 정수만 입력 가능합니다.");
				}
				break;
			case "설정":
				if (!sender.hasPermission("myprefix.commands.prefixmanage.set")) {
					sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
					break;
				}
				if (args.length < 3) {
					alert(sender, "/칭호관리 설정 <플레이어> <번호>");
					break;
				}
				try {
					int num = Integer.parseInt(args[2]);
					String prefix = PrefixManager.getInstance().setPrefix(args[1], num);
					message(sender, "칭호를 [" + prefix + TextFormat.DARK_AQUA + "]로 설정했습니다.");
				} catch (NumberFormatException e) {
					alert(sender, "번호는 정수만 입력 가능합니다.");
				} catch (IndexOutOfBoundsException e) {
					alert(sender, args[1] + "는 해당 번호의 칭호를 보유하고 있지 않습니다.");
				}
				break;
			default:
				return false;
			}
			break;
		}
		return true;
	}

	@EventHandler
	public void onChat(PlayerChatEvent event) {
		event.setFormat(getDB().getConfig().getString("chat-format").replace("%player", "{%0}").replace("%chat", "{%1}")
				.replace("%prefix", PrefixManager.getInstance().getPrefix(event.getPlayer().getName())));
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		event.getPlayer()
				.setNameTag(getDB().getConfig().getString("nametag-format")
						.replace("%player", event.getPlayer().getName()).replace("%prefix",
								PrefixManager.getInstance().getPrefix(event.getPlayer().getName())));
	}

	private void showPrefixList(CommandSender sender, String who, int page) {
		PageCreater creater = new PageCreater();
		int i = page * creater.getPageCount() - creater.getPageCount();
		for (String prefix : creater.getPage(PrefixManager.getInstance().getPrefixList(who.toLowerCase()), page)) {
			message(sender, "[" + i++ + "] " + prefix);
		}
	}

	private DataBase getDB() {
		return plugin.getDB();
	}

	private void alert(CommandSender sender, String msg) {
		getDB().alert(sender, msg);
	}

	private void message(CommandSender sender, String msg) {
		getDB().message(sender, msg);
	}
}
