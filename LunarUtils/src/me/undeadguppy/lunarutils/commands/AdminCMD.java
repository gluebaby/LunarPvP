package me.undeadguppy.lunarutils.commands;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.undeadguppy.lunarutils.Main;
import net.md_5.bungee.api.ChatColor;

public class AdminCMD implements CommandExecutor, Listener {

	private HashSet<UUID> inAdminMode = new HashSet<UUID>();

	public AdminCMD(Main pl) {
		pl.getCommand("admin").setExecutor(this);
		pl.getServer().getPluginManager().registerEvents(this, pl);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("admin")) {
			Player p = (Player) sender;
			if (p.hasPermission("ventos.adminmode")) {
				if (inAdminMode.contains(p.getUniqueId())) {
					p.sendMessage(ChatColor.BLUE + "Ventos" + ChatColor.DARK_GRAY + "»" + ChatColor.GRAY
							+ " You have exited admin mode! You are now visible!");
					p.setFlying(false);
					p.setHealth(20);
					p.setFoodLevel(20);
					p.setGameMode(GameMode.ADVENTURE);
					for (Player players : Bukkit.getOnlinePlayers()) {
						if (!players.canSee(p)) {
							players.showPlayer(p);
							players.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "+" + ChatColor.GRAY + "] "
									+ p.getName() + " has joined the game.");
						}
					}
					p.setPlayerListName(ChatColor.RESET + p.getName());
					inAdminMode.remove(p.getUniqueId());
					return true;
				}
				p.sendMessage(ChatColor.BLUE + "Ventos" + ChatColor.DARK_GRAY + "»" + ChatColor.GRAY
						+ " You have entered admin mode! You are now invisible!");
				p.setGameMode(GameMode.CREATIVE);
				for (Player players : Bukkit.getOnlinePlayers()) {
					if (!players.hasPermission("ventos.adminmode")) {
						if (players.canSee(p)) {
							players.hidePlayer(p);
							players.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "-" + ChatColor.GRAY + "] "
									+ p.getName() + " has left the game.");
						}
					}
				}
				p.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.BLUE + "*" + ChatColor.GRAY + "]" + ChatColor.RESET
						+ p.getName());
				inAdminMode.add(p.getUniqueId());
				return true;
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Ventos&8» &7Unknown command."));
				return true;
			}

		}
		return true;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (inAdminMode.contains(e.getPlayer().getUniqueId())) {
			e.setJoinMessage(null);
			p.sendMessage(ChatColor.BLUE + "Ventos" + ChatColor.DARK_GRAY + "»" + ChatColor.GRAY
					+ " You have logged in silently!");
			p.setGameMode(GameMode.CREATIVE);
			for (Player players : Bukkit.getOnlinePlayers()) {
				if (!players.hasPermission("ventos.adminmode")) {
					if (players.canSee(p)) {
						players.hidePlayer(p);
					}
				}
				p.setPlayerListName(ChatColor.GRAY + "[" + ChatColor.BLUE + "*" + ChatColor.GRAY + "]" + ChatColor.RESET
						+ p.getName());

			}
			return;
		}
		for (UUID players : inAdminMode) {
			Player staff = Bukkit.getPlayer(players);
			if (staff != null) {
				if (p.canSee(staff)) {
					if (!p.hasPermission("ventos.adminmode")) {
						p.hidePlayer(staff);
					}
				}
			}
		}
	}

}