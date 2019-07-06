package me.undeadguppy.lunarpvp.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import me.undeadguppy.lunarpvp.Main;

public class KitGiver implements Listener {

	private Main m;

	public KitGiver(Main pl) {
		this.m = pl;
		Bukkit.getServer().getPluginManager().registerEvents(this, m);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		applyKit(p);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		applyKit(e.getPlayer());
	}

	public static void applyKit(Player p) {
		PlayerInventory inv = p.getInventory();
		p.getActivePotionEffects().clear();
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 0));
		inv.clear();
		ItemStack helm = new ItemStack(Material.IRON_HELMET);
		helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		inv.setHelmet(helm);
		ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		inv.setChestplate(chest);
		ItemStack leg = new ItemStack(Material.IRON_LEGGINGS);
		leg.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		inv.setLeggings(leg);
		ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		inv.setBoots(boots);
		inv.addItem(new ItemStack(Material.DIAMOND_SWORD));
		ItemStack pot = new ItemStack(Material.POTION);
		Potion potion = Potion.fromItemStack(pot);
		potion.setType(PotionType.INSTANT_HEAL);
		potion.setLevel(2);
		potion.setSplash(true);
		potion.apply(pot);
		for (int i = 0; i < 35; i++) {
			inv.addItem(pot);
		}
	}

}
