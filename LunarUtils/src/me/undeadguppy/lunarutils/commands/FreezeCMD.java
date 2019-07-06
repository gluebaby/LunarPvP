package me.undeadguppy.lunarutils.commands;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.undeadguppy.lunarutils.Main;
import net.md_5.bungee.api.ChatColor;

public class FreezeCMD implements CommandExecutor, Listener {

	private HashSet<UUID> frozen = new HashSet<UUID>();

	public FreezeCMD(Main pl) {
		pl.getCommand("freeze").setExecutor(this);
		pl.getServer().getPluginManager().registerEvents(this, pl);

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("freeze")) {
			if (sender.hasPermission("ventos.freeze")) {
				if (args.length == 0) {
					sender.sendMessage(ChatColor.GRAY + "Please use /freeze <player>!");
					return true;
				}
				Player t = Bukkit.getPlayer(args[0]);
				if (t == null) {
					sender.sendMessage(ChatColor.BLUE + args[0] + ChatColor.GRAY + " is not online!");
					return true;
				}
				if (frozen.contains(t.getUniqueId())) {
					sender.sendMessage(ChatColor.BLUE + "Ventos" + ChatColor.DARK_GRAY + "»" + ChatColor.GRAY
							+ " You have unfrozen " + ChatColor.BLUE + t.getName() + ChatColor.GRAY + "!");
					t.sendMessage(ChatColor.BLUE + "Blade" + ChatColor.DARK_GRAY + "»" + ChatColor.GRAY
							+ " You have been unfrozen! Thank you for your patience.");
					frozen.remove(t.getUniqueId());
					return true;
				}
				sender.sendMessage(ChatColor.BLUE + "Ventos" + ChatColor.DARK_GRAY + "»" + ChatColor.GRAY
						+ " You have frozen " + ChatColor.BLUE + t.getName() + ChatColor.GRAY + "!");
				t.sendMessage(ChatColor.BLUE + "Luna" + ChatColor.DARK_GRAY + "»" + ChatColor.GRAY
						+ " You have been frozen! You have 2 minutes to join the discord: " + ChatColor.BLUE + ""
						+ ChatColor.GRAY + "! " + ChatColor.BLUE + "DO NOT LOG OUT!");
				frozen.add(t.getUniqueId());
				return true;
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Ventos&8» &7Unknown command."));
				return true;
			}
		}
		return true;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (frozen.contains(e.getPlayer().getUniqueId())) {
			frozen.remove(e.getPlayer().getUniqueId());
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
					"tempban " + e.getPlayer().getName() + "7d Logging out while frozen.");
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (frozen.contains(e.getPlayer().getUniqueId())) {
			e.setTo(e.getFrom());
		}
	}

}
