package me.undeadguppy.lunarutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.undeadguppy.lunarutils.Main;
import net.md_5.bungee.api.ChatColor;

public class ReportCMD implements CommandExecutor {

	public ReportCMD(Main pl) {
		pl.getCommand("report").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("report")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GRAY + "Please use /report <player> <reason>!");
				return true;
			}

			if (args.length == 1) {
				sender.sendMessage(ChatColor.GRAY + "Please use /report <player> <reason>!");
				return true;
			}

			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
				sender.sendMessage(ChatColor.BLUE + args[0] + ChatColor.GRAY + " is not online!");
				return true;
			}
			StringBuilder str = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				str.append(args[i] + " ");
			}
			String s = str.toString();
			for (Player staff : Bukkit.getOnlinePlayers()) {
				if (staff.hasPermission("ventos.reportview")) {
					staff.sendMessage(ChatColor.BLUE + sender.getName() + ChatColor.GRAY + " reported " + ChatColor.BLUE
							+ t.getName() + ChatColor.GRAY + " for " + ChatColor.BLUE + s);
				}
			}
			return true;
		}
		return true;
	}

}
