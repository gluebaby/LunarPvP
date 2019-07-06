package me.undeadguppy.lunarpvp.managers;

import net.md_5.bungee.api.ChatColor;

public class Messages {

	public Messages() {
	};

	public static Messages msg = new Messages();

	public static Messages getInstance() {
		return msg;
	}

	public String getHelpMessage() {
		return ChatColor.BLUE + "*****KoTH*****\n" + ChatColor.GRAY + "- /koth start\n- /koth loot\n- /koth info ";

	}

	public String getHelp() {
		return ChatColor.BLUE + "*****Help*****\n" + ChatColor.GRAY
				+ "- Use /duel [player] to start a duel!\n- Use /rules to read the rules!\n- Use /bounty to place a bounty!\n- Use /koth to host a KoTH\n- Use /spawn to teleport back to spawn!\nVisit our website at "
				+ ChatColor.BLUE + "www.ventos.us" + ChatColor.GRAY + "!";
	}

	public String getRules() {
		return ChatColor.BLUE + "*****Rules*****\n" + ChatColor.GRAY
				+ "- No hacking, cheating, or exploiting at any time.\n- No teams of above 2!\n- Do not disrespect staff or other players.\n- Racism, sexism, or any other form of bigotry is prohibited.";
	}

}
