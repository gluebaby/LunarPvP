package me.undeadguppy.lunarpvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.undeadguppy.vaults.managers.DataManager;
import net.md_5.bungee.api.ChatColor;

public class StatsCMD implements CommandExecutor {

	public static Inventory statsgui = Bukkit.getServer().createInventory(null, 9, ChatColor.BLUE + "Stats:");

	private void openGui(OfflinePlayer p) {
		int pkills = DataManager.getInstance().getKills(p);
		int pdeaths = DataManager.getInstance().getDeaths(p);
		int credits = DataManager.getInstance().getMoney(p);
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		SkullMeta skullmeta = (SkullMeta) skull.getItemMeta();
		skullmeta.setDisplayName(ChatColor.GRAY + p.getName());
		skullmeta.setOwner(p.getName());
		skull.setItemMeta(skullmeta);
		ItemStack kills = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta killsmeta = kills.getItemMeta();
		killsmeta.setDisplayName(ChatColor.GRAY + "Kills: " + pkills);
		kills.setItemMeta(killsmeta);
		ItemStack deaths = new ItemStack(Material.COAL_BLOCK);
		ItemMeta deathsmeta = deaths.getItemMeta();
		deathsmeta.setDisplayName(ChatColor.GRAY + "Deaths: " + pdeaths);
		deaths.setItemMeta(deathsmeta);
		ItemStack kdr = new ItemStack(Material.GOLDEN_APPLE);
		ItemMeta kdrmeta = kdr.getItemMeta();
		kdrmeta.setDisplayName(ChatColor.GRAY + "Prestige: " + DataManager.getInstance().getPrestige(p));
		kdr.setItemMeta(kdrmeta);
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
		ItemStack money = new ItemStack(Material.GOLD_NUGGET);
		ItemMeta moneymeta = money.getItemMeta();
		moneymeta.setDisplayName(ChatColor.GRAY + "Credits: " + credits);
		money.setItemMeta(moneymeta);
		statsgui.setItem(0, skull);
		statsgui.setItem(1, glass);
		statsgui.setItem(2, kills);
		statsgui.setItem(3, glass);
		statsgui.setItem(4, deaths);
		statsgui.setItem(5, glass);
		statsgui.setItem(6, kdr);
		statsgui.setItem(7, glass);
		statsgui.setItem(8, money);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("stats")) {
			if (args.length == 0) {
				openGui(p);
				p.openInventory(statsgui);
			} else if (args.length == 1) {
				@SuppressWarnings("deprecation")
				OfflinePlayer t = Bukkit.getServer().getOfflinePlayer(args[0]);
				if (!DataManager.getInstance().playerExists(t)) {
					p.sendMessage(ChatColor.GRAY + args[0] + " has never joined the server before!");
					return true;
				}
				// Show other players GUI
				openGui(t);
				p.openInventory(statsgui);
			} else {
				p.sendMessage(ChatColor.GRAY + "Please use /stats [player]!");
				return true;
			}
		}
		return true;

	}

}
