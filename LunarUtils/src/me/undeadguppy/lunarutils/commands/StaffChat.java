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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.undeadguppy.lunarutils.Main;
import net.md_5.bungee.api.ChatColor;

public class StaffChat implements CommandExecutor, Listener {

	public StaffChat(Main pl) {
		pl.getCommand("staffchat").setExecutor(this);
		pl.getServer().getPluginManager().registerEvents(this, pl);
	}

	public HashSet<UUID> staffChat = new HashSet<UUID>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("staffchat")) {
			if (sender.hasPermission("ventos.staffchat")) {
				Player p = (Player) sender;
				if (staffChat.contains(p.getUniqueId())) {
					p.sendMessage(ChatColor.BLUE + "Ventos" + ChatColor.DARK_GRAY + "»" + ChatColor.GRAY
							+ " You have exited staff chat!");
					staffChat.remove(p.getUniqueId());
					return true;
				}
				p.sendMessage(ChatColor.BLUE + "Ventos" + ChatColor.DARK_GRAY + "»" + ChatColor.GRAY
						+ " You have entered staff chat!");
				staffChat.add(p.getUniqueId());
				return true;
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Ventos&8» &7Unknown command."));
				return true;
			}
		}
		return true;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (staffChat.contains(p.getUniqueId())) {
			for (UUID players : staffChat) {
				Player staff = Bukkit.getPlayer(players);
				if (staff != null) {
					staff.sendMessage((ChatColor.BLUE + "StaffChat" + ChatColor.DARK_GRAY + "»" + ChatColor.GRAY + " ["
							+ p.getName() + "]: " + ChatColor.WHITE + e.getMessage()));
				}
			}
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (staffChat.contains(e.getPlayer().getUniqueId())) {
			staffChat.remove(e.getPlayer().getUniqueId());
		}
	}

}
