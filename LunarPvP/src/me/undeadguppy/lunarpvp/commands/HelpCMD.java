package me.undeadguppy.lunarpvp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.undeadguppy.lunarpvp.managers.Messages;

public class HelpCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("help")) {
			sender.sendMessage(Messages.getInstance().getHelp());
			return true;
		}
		return true;
	}

}
