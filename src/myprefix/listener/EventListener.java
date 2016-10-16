package myprefix.listener;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import myprefix.Main;
import myprefix.database.DataBase;
import myprefix.prefix.PrefixManager;
import myprefix.utils.PageCreater;

public class EventListener {
	private Main plugin;
	
	public EventListener(Main plugin) {
		this.plugin = plugin;
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
					message(sender, "칭호를 " + PrefixManager.getInstance().getPrefix(sender.getName()) + TextFormat.DARK_AQUA + "로 설정했습니다.");
				} catch (ArrayIndexOutOfBoundsException e) {
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
				} catch (ArrayIndexOutOfBoundsException e) {
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
					PageCreater creater = new PageCreater();
					Object[] pages = creater.getPage(PrefixManager.getInstance().getPrefixList(sender.getName()).toArray(), page);
					for (int i = 0; i < creater.getPageCount(); i++) {
						//TODO
					}
				} catch (NumberFormatException e) {
					alert(sender, "페이지는 정수만 입력 가능합니다.");
				}
				break;
			}
			break;
		case "칭호관리":
			//TODO
			break;
		}
		return true;
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
