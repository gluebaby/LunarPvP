package me.undeadguppy.lunarpvp.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.undeadguppy.lunarpvp.commands.ShopCMD;
import me.undeadguppy.lunarpvp.commands.StatsCMD;
import me.undeadguppy.vaults.managers.DataManager;
import net.md_5.bungee.api.ChatColor;

public class ClickListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getClickedInventory().getTitle().equals(ShopCMD.inv.getTitle())) {
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			if (e.getSlot() == 1) {
				// Koth Passes
				if (DataManager.getInstance().getMoney(p) >= 250) {
					DataManager.getInstance().addKothPasses(p, 1);
					DataManager.getInstance().removeMoney(p, 250);
					p.sendMessage(ChatColor.BLUE + "You have purchased 1 KoTH pass for 250 credits!");
					p.closeInventory();
				} else {
					p.sendMessage(ChatColor.GRAY + "You don't have enough credits for this item!");
					p.closeInventory();
				}
			} else if (e.getSlot() == 7) {
				if (DataManager.getInstance().getMoney(p) >= 25) {
					DataManager.getInstance().addBountyPasses(p, 1);
					DataManager.getInstance().removeMoney(p, 25);
					p.sendMessage(ChatColor.BLUE + "You have purchased 1 Bounty pass for 25 credits!");
					p.closeInventory();
				} else {
					p.sendMessage(ChatColor.GRAY + "You don't have enough credits for this item!");
					p.closeInventory();
				}
			}

		} else if (e.getClickedInventory().getTitle().equals(StatsCMD.statsgui.getTitle())) {
			e.setCancelled(true);
			return;
		} else {
			return;
		}
	}
}
