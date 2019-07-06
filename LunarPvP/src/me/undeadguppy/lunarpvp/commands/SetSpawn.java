package me.undeadguppy.lunarpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.undeadguppy.lunarpvp.Main;
import net.md_5.bungee.api.ChatColor;

public class SetSpawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("setspawn")) {
			Player p = (Player) sender;
			if (p.hasPermission("ventos.setspawn")) {
				Main.getInstance().getConfig().set("Spawn", p.getLocation());
				Main.getInstance().saveConfig();
				p.sendMessage("Spawn set");
				return true;
			} else {
				p.sendMessage(ChatColor.BLUE + "Luna " + ChatColor.GRAY + "» Unknown command.");
				return true;
			}
		}
		return true;
	}

}
