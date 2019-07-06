package me.undeadguppy.lunarpvp.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.undeadguppy.vaults.managers.DataManager;
import net.md_5.bungee.api.ChatColor;

public class ShopCMD implements CommandExecutor {

	public static Inventory inv = Bukkit.getServer().createInventory(null, 9, ChatColor.BLUE + "Shop:");

	private void openInv(Player p) {
		ItemStack kothpasses = new ItemStack(Material.NAME_TAG);
		ItemMeta kothmeta = kothpasses.getItemMeta();
		kothmeta.setDisplayName(ChatColor.BLUE + "Buy 1 KoTH pass!");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "$250");
		kothmeta.setLore(lore);
		kothpasses.setItemMeta(kothmeta);
		ItemStack bounty = new ItemStack(Material.SKULL_ITEM, 1);
		ItemMeta bountymeta = bounty.getItemMeta();
		bountymeta.setDisplayName(ChatColor.BLUE + "Buy 1 Bounty pass!");
		ArrayList<String> bountylore = new ArrayList<String>();
		bountylore.add(ChatColor.GRAY + "$25");
		bountymeta.setLore(bountylore);
		bounty.setItemMeta(bountymeta);
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
		ItemStack head = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta meta = head.getItemMeta();
		meta.setDisplayName(ChatColor.BLUE + "Owned Items:");
		ArrayList<String> metalore = new ArrayList<String>();
		metalore.add(ChatColor.GRAY + "Credits: $" + DataManager.getInstance().getMoney(p));
		metalore.add(ChatColor.GRAY + "Bounty Passes: " + DataManager.getInstance().getBountyPasses(p));
		metalore.add(ChatColor.GRAY + "KoTH Passes: " + DataManager.getInstance().getKothPasses(p));
		meta.setLore(metalore);
		head.setItemMeta(meta);
		inv.setItem(0, glass);
		inv.setItem(1, kothpasses);
		inv.setItem(2, glass);
		inv.setItem(3, glass);
		inv.setItem(4, head);
		inv.setItem(5, glass);
		inv.setItem(6, glass);
		inv.setItem(7, bounty);
		inv.setItem(8, glass);
		p.openInventory(inv);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("shop")) {
			Player p = (Player) sender;
			openInv(p);
			return true;
		}
		return true;
	}

}
