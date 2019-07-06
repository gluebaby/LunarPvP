package me.undeadguppy.lunarpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.undeadguppy.vaults.managers.DataManager;
import net.md_5.bungee.api.ChatColor;

public class SetStatsCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setstats")) {
			if (sender.hasPermission("ventos.setstats")) {
				if (args.length < 2) {
					sender.sendMessage(ChatColor.GRAY + "Please use: /setstats <player> <type> [amount]");
					return true;
				}
				OfflinePlayer t = Bukkit.getPlayer(args[0]);
				if (!DataManager.getInstance().playerExists(t)) {
					DataManager.getInstance().createProfile(t);
					sender.sendMessage(ChatColor.GRAY + "Profile created!");
				}
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("reset")) {
						DataManager.getInstance().resetProfile(t);
						sender.sendMessage(ChatColor.BLUE + "Reset stats of " + t.getName() + "!");
						return true;
					}
					return true;
				} else if (args.length == 3) {
					int amount = 0;
					try {
						amount = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.GRAY + args[2] + " is not a valid amount!");
						return true;
					}
					if (args[1].equalsIgnoreCase("money")) {
						DataManager.getInstance().addMoney(t, amount);
						sender.sendMessage(ChatColor.BLUE + "Added " + amount + " to " + t.getName() + "'s money!");
						return true;
					} else if (args[1].equalsIgnoreCase("kpasses")) {
						DataManager.getInstance().addKothPasses(t, amount);
						sender.sendMessage(
								ChatColor.BLUE + "Added " + amount + " to " + t.getName() + "'s koth passes!");
						return true;
					} else if (args[1].equalsIgnoreCase("bpasses")) {
						DataManager.getInstance().addBountyPasses(t, amount);
						sender.sendMessage(
								ChatColor.BLUE + "Added " + amount + " to " + t.getName() + "'s bounty passes!");
						return true;
					} else if (args[1].equalsIgnoreCase("boosters")) {
						DataManager.getInstance().addBoosters(t, amount);
						sender.sendMessage(ChatColor.BLUE + "Added " + amount + " to " + t.getName() + "'s boosters!");
						return true;
					} else {
						sender.sendMessage(ChatColor.GRAY + args[1] + " is not a valid type!");
						return true;
					}
				} else {
					sender.sendMessage(ChatColor.GRAY + "Please use: /setstats <player> <type> [amount]");
					return true;
				}
			}
		}
		return true;
	}

}
