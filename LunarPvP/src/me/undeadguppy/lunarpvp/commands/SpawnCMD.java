package me.undeadguppy.lunarpvp.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.undeadguppy.lunarpvp.Main;
import me.undeadguppy.lunarpvp.kits.KitGiver;
import net.md_5.bungee.api.ChatColor;

public class SpawnCMD implements CommandExecutor, Listener {

	HashMap<UUID, BukkitRunnable> cooldown = new HashMap<UUID, BukkitRunnable>();
	HashMap<UUID, Integer> time = new HashMap<UUID, Integer>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("spawn")) {
			final Player p = (Player) sender;
			if (cooldown.containsKey(p.getUniqueId()) && time.containsKey(p.getUniqueId())) {
				p.sendMessage(ChatColor.GRAY + "You are already attempting to enter spawn!");
				return true;
			}
			time.put(p.getUniqueId(), 6);
			cooldown.put(p.getUniqueId(), new BukkitRunnable() {
				int timeleft = time.get(p.getUniqueId());

				@Override
				public void run() {
					if (cooldown.containsKey(p.getUniqueId())) {
						if (timeleft == 1) {
							time.remove(p.getUniqueId());
							cooldown.remove(p.getUniqueId());
							p.sendMessage(ChatColor.BLUE + "Teleport success!");
							Location loc = (Location) Main.getInstance().getConfig().get("Spawn");
							p.teleport(loc);
							p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							KitGiver.applyKit(p);
							cancel();
							return;
						}

						timeleft--;
						p.sendMessage(ChatColor.BLUE + "Teleporting to spawn " + timeleft + "...");

					} else {
						cancel();
						return;
					}
				}

			});
			cooldown.get(p.getUniqueId()).runTaskTimer(Main.getInstance(), 0L, 20L);
		}
		return true;

	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (cooldown.containsKey(e.getPlayer().getUniqueId())) {
			cooldown.remove(e.getPlayer().getUniqueId());
			time.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (cooldown.containsKey(e.getPlayer().getUniqueId())) {
			if (e.getTo().getX() != e.getFrom().getX() && e.getTo().getY() != e.getFrom().getY()
					&& e.getTo().getZ() != e.getFrom().getZ()) {
				cooldown.remove(e.getPlayer().getUniqueId());
				time.remove(e.getPlayer().getUniqueId());
				e.getPlayer().sendMessage(ChatColor.GRAY + "You moved so teleportation was cancelled!");
			}
		}
	}

	@EventHandler
	public void onHit(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (cooldown.containsKey(p.getUniqueId())) {
				cooldown.remove(p.getUniqueId());
				time.remove(p.getUniqueId());
				p.sendMessage(ChatColor.GRAY + "You've taken damage so teleportation was cancelled!");
			}
		}
	}

}
