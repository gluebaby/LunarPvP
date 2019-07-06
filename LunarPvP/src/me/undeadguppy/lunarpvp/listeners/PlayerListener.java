package me.undeadguppy.lunarpvp.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.titleapi.TitleAPI;

import me.undeadguppy.lunarpvp.Main;
import me.undeadguppy.lunarpvp.commands.BountyCMD;
import me.undeadguppy.lunarpvp.kits.KitGiver;
import me.undeadguppy.vaults.managers.DataManager;
import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Location loc = (Location) Main.getInstance().getConfig().get("Spawn");
		p.teleport(loc);
		p.sendMessage(ChatColor.GRAY + "Welcome to the server " + ChatColor.BLUE + p.getName() + ChatColor.GRAY + "!");
		TitleAPI.sendTitle(p, 10, 70, 20, ChatColor.BLUE + "Welcome to Ventos", ChatColor.GRAY + "www.ventos.us");
		TitleAPI.sendTabTitle(p, ChatColor.GRAY + "[" + ChatColor.BLUE + "ventos" + ChatColor.GRAY + "]",
				ChatColor.GRAY + "www.ventos.us ");
		if (!DataManager.getInstance().playerExists(p)) {
			DataManager.getInstance().createProfile(p);
			Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {

				@Override
				public void run() {
					try {
						p.sendMessage(ChatColor.GRAY + "Here's a quick overview of how to play!");
						Thread.sleep(2000L); // 2 seconds
						p.sendMessage(ChatColor.GRAY + "On Ventos, you can fight both players and training bots!");
						Thread.sleep(2000L); // 2 seconds
						p.sendMessage(ChatColor.GRAY + "Use /duel to challenge a player to a 1v1!");
						Thread.sleep(2000L); // 2 seconds
						p.sendMessage(ChatColor.GRAY + "Use /spawn to go back to spawn!");
						Thread.sleep(2000L); // 2 seconds
						p.sendMessage(ChatColor.GRAY + "Use /rules for a list of rules!");
						Thread.sleep(2000L); // 2 seconds
						p.sendMessage(ChatColor.GRAY + "Use /shop to purchase passes!");
						Thread.sleep(2000L); // 2 seconds
						p.sendMessage(ChatColor.GRAY + "Use /help for more general information!");
						Thread.sleep(2000L); // 2 seconds
						p.sendMessage(ChatColor.GRAY + "Enjoy your stay!");
						Thread.sleep(2000L);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		e.setJoinMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "+" + ChatColor.GRAY + "] " + p.getName()
				+ " has joined the game.");

	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		e.setQuitMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "-" + ChatColor.GRAY + "] " + e.getPlayer().getName()
				+ " has left the game.");
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		int prestige = DataManager.getInstance().getPrestige(p);
		String league = null;
		if (prestige < 100) {
			league = ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "C" + ChatColor.GRAY + "]";
		} else if (prestige < 250) {
			league = ChatColor.GRAY + "[" + ChatColor.GOLD + "B" + ChatColor.GRAY + "]";
		} else if (prestige < 500) {
			league = ChatColor.GRAY + "[" + ChatColor.AQUA + "D" + ChatColor.GRAY + "]";
		} else if (prestige < 1000) {
			league = ChatColor.GRAY + "[" + ChatColor.GREEN + "E" + ChatColor.GRAY + "]";
		} else {
			league = ChatColor.GRAY + "[" + ChatColor.WHITE + "P" + ChatColor.GRAY + "]";
		}
		e.setFormat(league + ChatColor.GRAY + " [" + ChatColor.BLUE + DataManager.getInstance().getPrestige(p)
				+ ChatColor.GRAY + "] " + getNameColor(p) + "%s" + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + "%s");
	}

	private boolean isNPC(Entity e) {
		return e.hasMetadata("NPC");
	}

	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (isNPC(e.getEntity())) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (p.getKiller() == null) {
			return;
		}
		if (!(p.getKiller() instanceof Player)) {
			e.setDeathMessage(ChatColor.BLUE + p.getName() + ChatColor.GRAY + " died.");
			return;
		}
		if (isNPC(p.getKiller())) {
			e.setDeathMessage(ChatColor.BLUE + p.getName() + ChatColor.GRAY + " was slain by a trainer.");
			return;
		}
		Player k = (Player) p.getKiller();
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.spigot().respawn();
		new BukkitRunnable() {

			@Override
			public void run() {
				KitGiver.applyKit(p);
				cancel();

			}
		}.runTaskLater(Main.getInstance(), 20L);
		Location loc = (Location) Main.getInstance().getConfig().get("Spawn");
		p.teleport(loc);
		p.setHealth(20);
		p.setFoodLevel(20);
		DataManager.getInstance().addDeath(p);
		DataManager.getInstance().addKill(k);
		ItemStack pot = new ItemStack(Material.POTION);
		Potion potion = Potion.fromItemStack(pot);
		potion.setType(PotionType.INSTANT_HEAL);
		potion.setLevel(2);
		potion.setSplash(true);
		potion.apply(pot);
		for (int i = 0; i < 10; i++) {
			k.getInventory().addItem(pot);
		}
		DataManager.getInstance().addPrestige(k, 5);
		DataManager.getInstance().addMoney(k, 2);

		e.getDrops().clear();
		String[] messages = {
				ChatColor.BLUE + k.getName() + ChatColor.GRAY + " couldn't resist killing " + ChatColor.BLUE
						+ p.getName() + ChatColor.GRAY + "!",
				ChatColor.BLUE + k.getName() + ChatColor.GRAY + " has slain " + ChatColor.BLUE + p.getName()
						+ ChatColor.GRAY + "!",
				ChatColor.BLUE + k.getName() + ChatColor.GRAY + " put " + ChatColor.BLUE + p.getName() + ChatColor.GRAY
						+ " out of their misery!",
				ChatColor.BLUE + k.getName() + ChatColor.GRAY + " just strafed " + ChatColor.BLUE + p.getName()
						+ ChatColor.GRAY + " to death!",
				ChatColor.BLUE + k.getName() + ChatColor.GRAY + " just smashed " + ChatColor.BLUE + p.getName()
						+ ChatColor.GRAY + "!" };
		String msg = messages[(int) (Math.random() * messages.length)];
		e.setDeathMessage(msg);
		if (BountyCMD.bounties.containsKey(p.getUniqueId())) {
			Bukkit.getServer().broadcastMessage(
					ChatColor.BLUE + k.getName() + ChatColor.GRAY + " has just collected the bounty of "
							+ ChatColor.BLUE + "$" + BountyCMD.bounties.get(p.getUniqueId()) + ChatColor.GRAY + " on "
							+ ChatColor.BLUE + p.getName() + ChatColor.GRAY + "!");
			DataManager.getInstance().addMoney(k, BountyCMD.bounties.get(p.getUniqueId()));
			BountyCMD.bounties.remove(p.getUniqueId());

		}
		if (DataManager.getInstance().getPrestige(k) == 100) {
			TitleAPI.sendTitle(k, 10, 70, 20, ChatColor.BLUE + "Ranked up!", ChatColor.GRAY + "[" + ChatColor.DARK_GRAY
					+ "Coal" + ChatColor.GRAY + "] » [" + ChatColor.GOLD + "Bronze" + ChatColor.GRAY + "]");
			k.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
		} else if (DataManager.getInstance().getPrestige(k) == 250) {
			TitleAPI.sendTitle(k, 10, 70, 20, ChatColor.BLUE + "Ranked up!", ChatColor.GRAY + "[" + ChatColor.GOLD
					+ "Bronze" + ChatColor.GRAY + "] » [" + ChatColor.AQUA + "Diamond" + ChatColor.GRAY + "]");
			k.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
		} else if (DataManager.getInstance().getPrestige(k) == 500) {
			TitleAPI.sendTitle(k, 10, 70, 20, ChatColor.BLUE + "Ranked up!", ChatColor.GRAY + "[" + ChatColor.AQUA
					+ "Diamond" + ChatColor.GRAY + "] » [" + ChatColor.GREEN + "Emerald" + ChatColor.GRAY + "]");
			k.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
		} else if (DataManager.getInstance().getPrestige(k) == 1000) {
			TitleAPI.sendTitle(k, 10, 70, 20, ChatColor.BLUE + "Ranked up!", ChatColor.GRAY + "[" + ChatColor.AQUA
					+ "Diamond" + ChatColor.GRAY + "] » [" + ChatColor.WHITE + "Platinum" + ChatColor.GRAY + "]");
			k.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
		}

	}

	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("ventos.drop")) {
			if (!(e.getItemDrop().getItemStack().getType() == Material.BOWL)) {
				e.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!e.getWhoClicked().hasPermission("ventos.drop")) {
			if (e.getClickedInventory() == e.getWhoClicked().getInventory()) {
				if (e.getSlotType() == SlotType.ARMOR) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onDamage(PlayerItemDamageEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void soup(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (player.getItemInHand().getType() == Material.MUSHROOM_SOUP) {
				event.setCancelled(true);
				Damageable damageable = (Damageable) player;
				if (damageable.getHealth() != 20) {
					double newHealth = damageable.getHealth() + 7;
					if (newHealth > 20D) {
						player.setHealth(20D);
					} else {
						player.setHealth(newHealth);
					}
					player.getInventory().setItem(player.getInventory().getHeldItemSlot(),
							new ItemStack(Material.BOWL));
				} else if (player.getFoodLevel() != 20) {
					int newHunger = player.getFoodLevel() + 7;
					if (newHunger > 20) {
						player.setFoodLevel(20);
					} else {
						player.setFoodLevel(newHunger);
					}
					player.getInventory().setItem(player.getInventory().getHeldItemSlot(),
							new ItemStack(Material.BOWL));
				}

			}
		}
	}

	private ChatColor getNameColor(Player p) {
		if (p.hasPermission("ventos.patron")) {
			return ChatColor.AQUA;
		} else if (p.hasPermission("ventos.lunar")) {
			return ChatColor.DARK_AQUA;
		} else if (p.hasPermission("ventos.mod")) {
			return ChatColor.DARK_PURPLE;
		} else if (p.hasPermission("ventos.admin")) {
			return ChatColor.RED;
		} else {
			return ChatColor.GRAY;
		}

	}

}