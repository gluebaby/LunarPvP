package me.undeadguppy.lunarpvp.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.undeadguppy.vaults.managers.DataManager;
import net.md_5.bungee.api.ChatColor;

public class BountyCMD implements CommandExecutor {

	public static HashMap<UUID, Integer> bounties = new HashMap<UUID, Integer>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("bounty")) {
			Player p = (Player) sender;
			if (DataManager.getInstance().getBountyPasses(p) < 1) {
				p.sendMessage(ChatColor.GRAY
						+ "You currently don't have any bounty passes! Buy them with /shop or donate for them on "
						+ ChatColor.BLUE + "www.ventos.us" + ChatColor.GRAY + "!");
				return true;
			}

			if (args.length == 0) {
				p.sendMessage(ChatColor.GRAY + "Please use: /bounty <player> <amount>!");
				return true;
			}
			if (args.length == 1) {
				p.sendMessage(ChatColor.GRAY + "Please use: /bounty <player> <amount>!");
				return true;
			}

			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
				p.sendMessage(ChatColor.BLUE + args[0] + ChatColor.GRAY + " is not online!");
				return true;
			}

			int amount = 0;
			try {
				amount = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				p.sendMessage(ChatColor.GRAY + args[1] + " is not a number!");
				return true;
			}
			if (DataManager.getInstance().getMoney(p) < amount) {
				p.sendMessage(ChatColor.GRAY + "You don't have enough credits for that bounty!");
				return true;
			}
			DataManager.getInstance().removeMoney(p, amount);
			DataManager.getInstance().removeBountyPasses(p, 1);
			if (bounties.containsKey(t.getUniqueId())) {
				int prev = bounties.get(t.getUniqueId());
				bounties.put(t.getUniqueId(), amount + prev);
				Bukkit.getServer()
						.broadcastMessage(ChatColor.BLUE + p.getName() + ChatColor.GRAY + " has just added a bounty of "
								+ ChatColor.BLUE + "$" + amount + ChatColor.GRAY + " on " + ChatColor.BLUE + t.getName()
								+ ChatColor.GRAY + "! They are now worth $" + ChatColor.BLUE
								+ bounties.get(t.getUniqueId()) + ChatColor.GRAY + "!");
				return true;
			}
			bounties.put(t.getUniqueId(), amount);
			Bukkit.getServer()
					.broadcastMessage(ChatColor.BLUE + p.getName() + ChatColor.GRAY + " has just set a bounty of "
							+ ChatColor.BLUE + "$" + amount + ChatColor.GRAY + " on " + ChatColor.BLUE + t.getName()
							+ ChatColor.GRAY + "!");

		}
		return true;
	}

}
