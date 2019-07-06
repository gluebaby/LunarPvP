package me.undeadguppy.lunarutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.undeadguppy.lunarutils.Main;
import net.md_5.bungee.api.ChatColor;

public class InvseeCMD implements CommandExecutor {

	public InvseeCMD(Main pl) {
		pl.getCommand("invsee").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("invsee")) {
			Player p = (Player) sender;
			if (p.hasPermission("ventos.invsee")) {
				if (args.length == 0) {
					p.sendMessage(ChatColor.GRAY + "Please use /invsee <player>!");
					return true;
				}
				Player t = Bukkit.getPlayer(args[0]);
				if (t == null) {
					p.sendMessage(ChatColor.BLUE + args[0] + ChatColor.GRAY + " is not online!");
					return true;
				}
				p.openInventory(t.getInventory());
				return true;
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Ventos&8» &7Unknown command."));
				return true;
			}
		}
		return true;
	}

}
