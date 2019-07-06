package me.undeadguppy.lunarpvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.undeadguppy.lunarpvp.managers.Game;
import me.undeadguppy.lunarpvp.managers.Game.GameState;
import me.undeadguppy.vaults.managers.DataManager;
import me.undeadguppy.lunarpvp.managers.Messages;

public class Koth implements CommandExecutor, Listener {

	Game game = new Game();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String l, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use KoTH commands!");
			return true;
		}
		// Sender is a player
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("koth")) {
			if (args.length == 0) {
				// No args entered.
				p.sendMessage(Messages.getInstance().getHelpMessage());
				return true;
			}
			if (args[0].equalsIgnoreCase("start")) {
				if (p.hasPermission("gkoth.host") || DataManager.getInstance().getKothPasses(p) >= 1) {
					// Start game
					if (Bukkit.getServer().getOnlinePlayers().size() >= 3) {
						if (game.isState(GameState.NO_GAME)) {
							// Start countdown
							game.setState(GameState.COUNTDOWN);
							game.runCountdown();
							DataManager.getInstance().removeKothPasses(p, 1);
							p.sendMessage(ChatColor.BLUE + "You have started a KoTH for x1 KoTH pass!");
							return true;
						} else {
							// Stop code
							p.sendMessage(ChatColor.GRAY + "There is already a KoTH in progress!");
							return true;
						}
					} else {
						p.sendMessage(ChatColor.GRAY + "There are not enough players to start a KoTH!");
					}
				} else {
					p.sendMessage(ChatColor.GRAY + "You are out of KoTH passes!");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("stop")) {
				if (p.hasPermission("gkoth.host")) {
					// Stop game
					if (game.isState(GameState.IN_GAME) || game.isState(GameState.COUNTDOWN)) {
						// Stop game
						game.stop();
						return true;
					} else { // Stop code
						p.sendMessage(ChatColor.GRAY + "There is no KoTH in progress!");
						return true;
					}
				} else {
					p.sendMessage(ChatColor.GRAY + "You do not have access to this command!");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("loot")) {
				p.sendMessage(ChatColor.BLUE + "KoTH Loot:");
				p.sendMessage(ChatColor.GRAY + "- Either:");
				p.sendMessage(ChatColor.GRAY + "- 1x KoTH pass");
				p.sendMessage(ChatColor.GRAY + "- 2x Bounty passes");
				p.sendMessage(ChatColor.GRAY + "- 200 credits");
			} else if (args[0].equalsIgnoreCase("info")) {
				p.sendMessage(ChatColor.BLUE + "KoTH Info:");
				p.sendMessage(ChatColor.GRAY + "Active: " + ChatColor.BLUE
						+ (game.isState(GameState.COUNTDOWN) || game.isState(GameState.IN_GAME)));
				p.sendMessage(ChatColor.GRAY + "Location: " + ChatColor.BLUE + "Main FFA arena.");
			} else { // Invalid arg
				p.sendMessage(Messages.getInstance().getHelpMessage());
				return true;

			}
		}
		return true;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (game.isState(GameState.COUNTDOWN) || game.isState(GameState.IN_GAME)) {
			e.getPlayer().sendMessage(ChatColor.BLUE + "There is currently an active KoTH! Use /koth info!");
		}
	}

}
